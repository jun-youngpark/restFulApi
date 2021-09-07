package com.mnwise.wiseu.web.template.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.formobjects.DataImportForm;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.service.MobileTemplateService;

/**
 * 모바일 템플릿 등록 Controller
 */
@Controller
public class MobileTemplateUploadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MobileTemplateUploadController.class);

    @Autowired private MobileTemplateService mobileTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    @Value("${mms.image.max.size:40960}")
    private long mmsImageMaxSize;

    /**
     * [템플릿>모바일 템플릿 등록] 모바일 템플릿 등록
     */
    @RequestMapping(value="/template/mobileUpload.do", method=RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute("command") DataImportForm dataImportForm, HttpServletRequest request, BindingResult errors) throws Exception {
        MultipartHttpServletRequest multipartRequest = null;

        try {
            multipartRequest = customMultipartResolver.resolveMultipart(request);
        } catch(MaxUploadSizeExceededException e) {
            log.error(null, e);
            errors.rejectValue("file", "errors.upload.size.exceeded", null, "bundle errors.upload.size.exceeded not found");
            return new ModelAndView("template/mobileTemplateReg");  // template/mobile_reg
        }

        if(StringUtil.isNotBlank(multipartRequest.getParameter("cancel"))) {
            log.info("Canceling operation.");
            return new ModelAndView("template/templateReg");  // template/template_reg
        }

        try {
            String tagNm = ServletRequestUtils.getStringParameter(multipartRequest, "tagNm");
            String contsNm = ServletRequestUtils.getStringParameter(multipartRequest, "contsNm");
            String contsDesc = ServletRequestUtils.getStringParameter(multipartRequest, "contsDesc");

            MobileVo mobileVo = new MobileVo();
            mobileVo.setUserId(getLoginId());
            mobileVo.setTagNm(StringUtil.escapeXss(tagNm));
            mobileVo.setContsNm(StringUtil.escapeXss(contsNm));
            mobileVo.setContsDesc(StringUtil.escapeXss(contsDesc));
            mobileVo.setFileType(ServletRequestUtils.getStringParameter(multipartRequest, "mobile_type"));
            mobileVo.setAuthType(ServletRequestUtils.getStringParameter(multipartRequest, "authType"));
            mobileVo.setCreateDt(DateUtil.getNowDateTime("yyyyMMdd"));
            mobileVo.setCreateTm(DateUtil.getNowDateTime("HHmmss"));
            mobileVo.setContsNo(mobileTemplateService.selectNextContsNo());
            mobileVo.setContsTxt("");

            Map<String, Object> returnMap = new HashMap<>();
            if(mobileVo.getFileType().equals("I") || mobileVo.getFileType().equals("S")) {
                // 파일을 업로드 한 거니깐 파일을 저장하고 등록하자.
                MultipartFile originalFile = multipartRequest.getFile("fileName");

                // 20kbyte 이상 이미지가 올려지지 않도록 변경
                if(originalFile.getSize() >= this.mmsImageMaxSize) {
                    errors.rejectValue("file", "template.mobiletemplate.alert.size.over", null, "bundle template.mobiletemplate.alert.size.over not found");
                    log.warn("파일 사이즈가 제한 사이즈를 초과합니다.");
                    return new ModelAndView("template/mobileTemplateReg");  // template/mobile_reg
                } else if(originalFile.getSize() == 0) {
                    errors.rejectValue("file", "template.mobiletemplate.alert.size.zero", null, "bundle template.mobiletemplate.alert.size.zero not found");
                    log.warn("파일 사이즈가 0입니다.");
                    return new ModelAndView("template/mobileTemplateReg");  // template/mobile_reg
                }

                mobileVo.setFileSize(originalFile.getSize());
                String fileName = originalFile.getOriginalFilename();
                Map<String, String> originalFileUploadMap = this.mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);

                MultipartFile previewFile = multipartRequest.getFile("fileName");  // 미리보기 파일 숨김
                Map<String, String> previewFileUploadMap = this.mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");

                if(originalFileUploadMap.get("isFail") != null) {  // 오류여부 체크
                    returnMap.put("isFail", "true");
                    returnMap.put("originalFile", originalFileUploadMap);
                    returnMap.put("previewFile", previewFileUploadMap);
                } else {
                    mobileVo.setFileName(originalFileUploadMap.get("fileNm"));
                    mobileVo.setFilePath(originalFileUploadMap.get("filePath"));
                    mobileVo.setFilePreviewName(previewFileUploadMap.get("fileNm"));
                    mobileVo.setFilePreviewPath(previewFileUploadMap.get("filePath"));
                }
            } else if(mobileVo.getFileType().equals("C")) {
                mobileVo.setImageUrl(ServletRequestUtils.getStringParameter(multipartRequest, "friendtalkImageUrl"));
                mobileVo.setImageLink(ServletRequestUtils.getStringParameter(multipartRequest, "friendtalkImageUrlLink"));

                MultipartFile originalFile = multipartRequest.getFile("fileName2");
                String fileName = originalFile.getOriginalFilename();
                Map<String, String> originalFileUploadMap = this.mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);

                MultipartFile previewFile = multipartRequest.getFile("fileName2");  // 미리보기 파일 숨김
                Map<String, String> previewFileUploadMap = this.mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");

                if(originalFileUploadMap.get("isFail") != null) {  // 오류여부 체크
                    returnMap.put("isFail", "true");
                    returnMap.put("originalFile", originalFileUploadMap);
                    returnMap.put("previewFile", previewFileUploadMap);
                } else {
                    mobileVo.setFileName(originalFileUploadMap.get("fileNm"));
                    mobileVo.setFilePath(originalFileUploadMap.get("filePath"));
                    mobileVo.setFilePreviewName(previewFileUploadMap.get("fileNm"));
                    mobileVo.setFilePreviewPath(previewFileUploadMap.get("filePath"));
                }

                originalFile = multipartRequest.getFile("fileName3");
                previewFile = multipartRequest.getFile("fileName3");  // 미리보기 파일 숨김
                // MMS 최대 BYTE 사이즈 이상의 이미지가 올려지지 않도록 변경
                if(originalFile.getSize() >= this.mmsImageMaxSize) {
                    errors.rejectValue("file", "template.mobiletemplate.alert.size.over", null, "bundle template.mobiletemplate.alert.size.over not found");
                    log.warn("파일 사이즈가 제한 사이즈를 초과합니다.");
                    return new ModelAndView("template/mobileTemplateReg");
                }
                if((originalFile.getSize() > 0)) {
                    fileName = originalFile.getOriginalFilename();
                    originalFileUploadMap = this.mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);
                    previewFileUploadMap = this.mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");
                    // 오류여부 체크
                    if((originalFileUploadMap.get("isFail") != null)) {
                        returnMap.put("isFail", "true");
                        returnMap.put("originalFile", originalFileUploadMap);
                        returnMap.put("previewFile", previewFileUploadMap);
                    } else {
                        mobileVo.setDetourFileName(originalFileUploadMap.get("fileNm"));
                        mobileVo.setDetourFilePath(originalFileUploadMap.get("filePath"));
                        mobileVo.setDetourPreviewName(previewFileUploadMap.get("fileNm"));
                        mobileVo.setDetourPreviewPath(previewFileUploadMap.get("filePath"));
                    }
                }
            } else {  // 텍스트 저장시
                String contsTxt = ServletRequestUtils.getStringParameter(multipartRequest, "contsTxt");
                mobileVo.setContsTxt(StringUtil.escapeXss(contsTxt));
                mobileVo.setKakaoSenderKey(ServletRequestUtils.getStringParameter(multipartRequest, "kakaoSenderKey"));
                mobileVo.setKakaoTmplCd(ServletRequestUtils.getStringParameter(multipartRequest, "kakaoTmplCd"));
            }

            returnMap.put("contsNo", mobileVo.getContsNo());
            returnMap.put("mobileVo", mobileVo);

            if(returnMap.get("isFail") == null) {
                this.mobileTemplateService.insertMobileContent(mobileVo);
            }

            return new ModelAndView("template/mobileUpload", returnMap);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
