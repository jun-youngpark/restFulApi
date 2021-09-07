package com.mnwise.wiseu.web.template.web;

import static org.springframework.web.bind.ServletRequestUtils.getStringParameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.formobjects.DataImportForm;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.editor.service.EditorEcareService;
import com.mnwise.wiseu.web.template.service.KakaoTemplateService;

@Controller
public class AlimtalkTemplateUploadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AlimtalkTemplateUploadController.class);

    @Autowired private EditorEcareService editorEcareService;
    @Autowired private KakaoTemplateService kakaoTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    /**
     * - [템플릿>알림톡 템플릿 등록>등록방식:파일] 엑셀 다운로드 버튼 클릭 - 알림톡 업로드 양식 엑셀파일 다운로드
     * - [템플릿>알림톡 템플릿 등록>등록방식:파일] 등록 버튼 클릭 - 알림톡 엑셀파일 업로드 저장
     */
    @RequestMapping(value="/template/alimtalkUpload.do", method=RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute("command") DataImportForm dataImportForm, HttpServletRequest request, HttpServletResponse response, BindingResult errors) {
        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = customMultipartResolver.resolveMultipart(request);
        } catch(final MaxUploadSizeExceededException e) {
            log.error(null, e);
            errors.rejectValue("file", "errors.upload.size.exceeded", null, "bundle errors.upload.size.exceeded not found");
            return new ModelAndView("template/mobileTemplateReg");  // template/mobile_reg
        }

        if(StringUtil.isNotBlank(multipartRequest.getParameter("cancel"))) {
            log.info("Canceling operation.");
            return new ModelAndView("template/templateReg");  // template/template_reg
        }

        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("isAlimtalk", true);

        try {
            String contsDesc = ServletRequestUtils.getStringParameter(multipartRequest, "contsDesc");
            String authType = ServletRequestUtils.getStringParameter(multipartRequest, "authType");
            String formDownload = getStringParameter(multipartRequest, "formDownload");
            if("Y".equals(formDownload)) {
                editorEcareService.makeAlimtalkFormFile(response);
                return null;
            }

            String uploadType = getStringParameter(multipartRequest, "uploadType");
            boolean isFileUpload = "F".equals(uploadType);

            if(isFileUpload) {
                final MultipartFile multipartFile = multipartRequest.getFile("fileName");
                if(multipartFile.getSize() == 0) {
                    errors.rejectValue("file", "errors.upload.size.zero", null, "bundle errors.upload.size.zero not found");
                    log.error("file size is zero.");
                    return new ModelAndView("template/mobileTemplateReg");  // template/mobile_reg
                }

                loadFile(multipartFile, getLoginId(), authType, contsDesc);
            }
        } catch(Exception e) {
            log.error("while creating alimtalk template with file, exception occurred. Message: " + e.getMessage());
            returnMap.put("isFail", "true");
        }

        return new ModelAndView("template/mobileUpload", returnMap);
    }

    /**
     * 알림톡 템플릿이 저장된 엑셀 파일을 읽어와서 알림톡 REST API에 서버에 전송하고 NVMOBILECONTENTS 테이블에 INSERT 한다.
     *
     * @param multipartFile
     * @param userId
     * @throws IOException
     */
    private void loadFile(final MultipartFile multipartFile, final String userId, final String authType, final String contsDesc) throws Exception {
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        kakaoTemplateService.saveFile(multipartFile.getInputStream(), fileName, "");
        kakaoTemplateService.createAlimtalkTemplate(userId, authType, contsDesc, fileName);
    }
}
