package com.mnwise.wiseu.web.template.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.formobjects.DataImportForm;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.template.model.ContentVo;
import com.mnwise.wiseu.web.template.service.TemplateService;
import com.mnwise.wiseu.web.template.util.TemplateFileUtil;

/**
 * 템플릿 등록/수정 Controller
 */
@Controller
public class TemplateUploadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TemplateUploadController.class);

    @Autowired private TemplateService templateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    /**
     * [템플릿>템플릿 등록] 템플릿 등록/수정
     */
    @RequestMapping(value="/template/templateUpload.do", method=RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute("command") DataImportForm dataImportForm, HttpServletRequest request, BindingResult errors) throws Exception {
        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = customMultipartResolver.resolveMultipart(request);
        } catch(MaxUploadSizeExceededException e) {
            log.error(null, e);
            errors.rejectValue("file", "errors.upload.size.exceeded", null, "bundle errors.upload.size.exceeded not found");
            return new ModelAndView("template/templateReg");  // template/template_reg
        }

        if(StringUtil.isNotBlank(multipartRequest.getParameter("cancel"))) {
            log.info("Canceling operation.");
            return new ModelAndView("template/templateReg");  // template/template_reg
        }

        try {
            log.debug("Entering 'onSubmit' method...");

            UserVo userVo = getLoginUserVo();
            String userId = userVo.getUserId();

            String cmd = ServletRequestUtils.getStringParameter(multipartRequest, "com", "");
            Integer contsNo = ServletRequestUtils.getIntParameter(multipartRequest, "contsNo");
            String contsNm = ServletRequestUtils.getStringParameter(multipartRequest, "contsNm");
            String tagNm = ServletRequestUtils.getStringParameter(multipartRequest, "tagNm");
            String contsDesc = ServletRequestUtils.getStringParameter(multipartRequest, "contsDesc");

            ContentVo contentVo = new ContentVo();
            contentVo.setContsNm(StringUtil.escapeXss(contsNm));
            contentVo.setTagNm(StringUtil.escapeXss(tagNm));
            contentVo.setContsDesc(StringUtil.escapeXss(contsDesc));
            contentVo.setAuthType(ServletRequestUtils.getStringParameter(multipartRequest, "authType"));
            contentVo.setCommand(cmd);
            contentVo.setUserId(userId);
            contentVo.setGrpCd(userVo.getGrpCd());
            contentVo.setContsNo(contsNo.intValue());

            Map<String, Object> returnData = new HashMap<>();

            if("modify".equals(cmd)) {
                ContentVo originalVo = templateService.getTemplateInfo(contsNo);

                // 세션에 저장된 계정과 템플릿 등록 계정이 다를 경우 예외 처리
                if(Const.USER_ROLE_ADMIN.equals(userVo.getUserTypeCd()) == false && userId.equals(originalVo.getUserId()) == false) {
                    errors.rejectValue("file", "template.alert.msg.4", null, "계정 미 일치 오류");
                    returnData.put("contentVo", contentVo);
                    return new ModelAndView("template/templateReg", returnData);  // template/template_reg
                }
            }

            MultipartFile multipartFile = (MultipartFile) multipartRequest.getFile("fileNm");
            String fileExt = multipartFile.getOriginalFilename();
            fileExt = fileExt.substring(fileExt.lastIndexOf(".") + 1);

            boolean isUpdate = false;
            if(cmd.equalsIgnoreCase("modify") && multipartFile.isEmpty()) {
                contentVo.setContsNo(contsNo.intValue());
                this.templateService.setUpdateTemplate(contentVo);

                returnData.put("viewTemplate", true);

                return new ModelAndView(new RedirectView("/template/templateView.do?contsNo=" + contentVo.getContsNo()), returnData);  // /template/viewTemplate.do
            } else if(cmd.equalsIgnoreCase("modify") && !multipartFile.isEmpty()) {
                isUpdate = true;
                contentVo.setContsNo(contsNo.intValue());
            } else {
                contentVo.setContsNo(this.templateService.selectNextContsNo());
            }
            log.debug("[ contsNo ] " + contsNo);

            if(multipartFile.getSize() == 0) {
                errors.rejectValue("file", "errors.upload.size.zero", null, "bundle errors.upload.size.zero not found");
                log.warn("파일 사이즈가 0입니다.");
                returnData.put("contentVo", contentVo); // 템플릿 오류 난 이후 재등록시 no값이 없어 오류나는 것을 방지하기 위해 추가
                return new ModelAndView("template/templateReg", returnData);  // template/template_reg
            }

            contentVo.setCreateDt(DateUtil.getNowDateTime("yyyyMMdd"));
            contentVo.setCreateTm(DateUtil.getNowDateTime("HHmmss"));

            // - 파일 업로드 및 디비업로드
            Map<String, String> map = null;
            try {
                map = templateService.saveFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), contentVo, isUpdate);
            } catch(Exception e) {
                errors.rejectValue("file", "errors.upload.template", null, "파일 업로드 중 오류.");
                log.error(null, e);
                returnData.put("contentVo", contentVo);
                return new ModelAndView("template/templateReg", returnData);  // template/template_reg
            }

            String oriFileName = (String) map.get("fileNm");

            // 압축해제
            File htmlFile = null;
            if(oriFileName != null && oriFileName.endsWith(".zip")) {
                try {
                    htmlFile = TemplateFileUtil.unZipFile(new File(((String) map.get("fileUrl"))));
                } catch(Exception e) {
                    errors.rejectValue("file", "errors.upload.template", null, "압축 파일 형식이 잘 못되었습니다. 압축파일을 확인하세요.");
                    log.error(null, e);
                    returnData.put("contentVo", contentVo);
                    returnData.put("errors", errors);
                    return new ModelAndView("template/templateReg", returnData);  // template/template_reg
                }

                if(htmlFile == null) {
                    TemplateFileUtil.deleteFile(new File(((String) map.get("fileUrl"))).getParentFile());
                    errors.rejectValue("file", "errors.upload.template.not.contanin.html", null, "압축 파일에 HTML 파일이 존재 하지 않습니다.");
                    returnData.put("contentVo", contentVo);
                    return new ModelAndView("template/templateReg", returnData);  // template/template_reg
                }

                map.put("fileNm", htmlFile.getName());
                if(!((String) map.get("fileNm")).toLowerCase().endsWith(".gif") && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpg")
                    && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpeg")) {
                    map.put("fileUrl", htmlFile.getPath());
                }

                // 압축해제 끝
                File replacedFile = null;
                try {
                    if(!((String) map.get("fileNm")).toLowerCase().endsWith(".gif") && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpg")
                        && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpeg")) {
                        replacedFile = TemplateFileUtil.imgReplace(htmlFile, contentVo, super.templateDefaultUrl, oriFileName);
                    }
                } catch(Exception e) {
                    TemplateFileUtil.deleteFile(new File(((String) map.get("fileUrl"))).getParentFile());
                    errors.rejectValue("file", "errors.upload.template.not.contanin.html", null, "HTML파일의 경로를 수정 중 오류(IMG, LINK, SCRIPT)");
                    log.error(null, e);
                    returnData.put("contentVo", contentVo);
                    return new ModelAndView("template/templateReg", returnData);  // template/template_reg
                }
                if(replacedFile == null && (!((String) map.get("fileNm")).toLowerCase().endsWith(".gif") && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpg")
                    && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpeg"))) {
                    TemplateFileUtil.deleteFile(new File(((String) map.get("fileUrl"))).getParentFile());
                    errors.rejectValue("file", "errors.upload.template.not.contanin.html", null, "압축 파일에 HTML 파일이 존재 하지 않습니다.");
                    returnData.put("contentVo", contentVo);
                    return new ModelAndView("template/templateReg", returnData);  // template/template_reg
                }
                if(replacedFile != null && (!((String) map.get("fileNm")).toLowerCase().endsWith(".gif") && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpg")
                    && !((String) map.get("fileNm")).toLowerCase().endsWith(".jpeg"))) {
                    map.put("fileNm", replacedFile.getName());
                    map.put("fileUrl", replacedFile.getPath());
                } else {
                    map.put("fileNm", map.get("fileNm"));
                }
            }

            contentVo.setFileType(fileExt.equalsIgnoreCase("html") || fileExt.equalsIgnoreCase("htm") ? "H" : fileExt.equalsIgnoreCase("zip") ? "H" : "I");
            contentVo.setFileNm((String) map.get("fileNm"));  // TODO : 제거 필요
            contentVo.setFileName((String) map.get("fileNm"));
            contentVo.setFileUrlNm((String) map.get("fileUrl"));  // TODO : 제거 필요
            contentVo.setFileUrlName((String) map.get("fileUrl"));

            if(isUpdate) {
                this.templateService.setUpdateTemplate(contentVo);
            } else {
                this.templateService.setInsertTemplate(contentVo);
            }

            returnData.put("viewTemplate", true);

            return new ModelAndView(new RedirectView("/template/templateView.do?contsNo=" + contentVo.getContsNo()), returnData);  // /template/viewTemplate.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
