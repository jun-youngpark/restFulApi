package com.mnwise.wiseu.web.template.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.common.util.KakaoRestApiUtil;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.service.MobileTemplateService;

@Controller
public class MobileTemplateRegController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(MobileTemplateRegController.class);

    @Autowired private MobileTemplateService mobileTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    /**
     * [템플릿>모바일 템플릿 등록] 모바일 템플릿 등록 화면 출력
     *
     * @return
     */
    @RequestMapping(value="/template/mobileTemplateReg.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list() throws Exception {
        try {
            ModelAndView mav = new ModelAndView("template/mobileTemplateReg");  // template/mobile_reg
            mav.addObject("mobileVo", new MobileVo());
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>모바일 템플릿 등록] 친구톡 이미지 파일 올리기
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/ftrImgUpload.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/friendtalkImageUpload.do
    public ModelAndView uploadImage(HttpServletRequest request) throws Exception {
        try {
            MultipartHttpServletRequest multipartRequest = customMultipartResolver.resolveMultipart(request);

            String nowDateTime = DateUtil.getNowDateTime();

            MobileVo mobileVo = new MobileVo();
            mobileVo.setCreateDt(nowDateTime.substring(0, 8));
            mobileVo.setCreateTm(nowDateTime.substring(8, 14));
            mobileVo.setContsNo(mobileTemplateService.selectNextContsNo());

            // 친구톡 이미지 업로드 사용 경로. 파일을 업로드 한 거니깐 파일을 저장하고 등록.
            MultipartFile originalFile = multipartRequest.getFile("fileName2");
            String fileName = originalFile.getOriginalFilename();
            Map<String, String> originalFileUploadMap = mobileTemplateService.saveFile(originalFile.getInputStream(), fileName, mobileVo, null);

            // 미리보기 파일 숨김
            MultipartFile previewFile = multipartRequest.getFile("fileName2");
            Map<String, String> previewFileUploadMap = mobileTemplateService.saveFile(previewFile.getInputStream(), fileName, mobileVo, "preview");

            ModelAndView mav = new ModelAndView("template/ftrImgUpload");  // template/mobile_upload
            // 오류여부 체크
            if((originalFileUploadMap.get("isFail") != null)) {
                mav.addObject("isFail", "true");
                mav.addObject("originalFile", originalFileUploadMap);
                mav.addObject("previewFile", previewFileUploadMap);
            } else {
                mobileVo.setFileName(originalFileUploadMap.get("fileNm"));
                mobileVo.setFilePath(originalFileUploadMap.get("filePath"));
                mobileVo.setFilePreviewName(previewFileUploadMap.get("fileNm"));
                mobileVo.setFilePreviewPath(previewFileUploadMap.get("filePath"));
            }

            String jsonData = mobileTemplateService.getImageUploadUrl(originalFileUploadMap.get("filePath"), originalFileUploadMap.get("fileNm"));
            log.info(jsonData);

            String code = "888";
            String url = "";
            String message = "파일을 선택하세요";

            if(StringUtil.isBlank(jsonData)) {
                log.error("Kakao Friendtalk Image Upload Response is empty");
            } else {
                JSONParser jsonParser = new JSONParser();
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser.parse(jsonData);
                code = (String) jsonObject.get("code");
                url = (String) jsonObject.get("image");
                message = (String) jsonObject.get("message");
                if(KakaoRestApiUtil.SUCCESS_CODE.equals(code)) {
                    log.info("Kakao ImageUpload is success. code: " + code + "/ message: " + message + "/ url: " + url);
                } else {
                    log.error("Kakao ImageUpload is fail. code: " + code + " message: " + message);
                }
            }

            mav.addObject("code", code);
            mav.addObject("message", message);
            mav.addObject("url", url);
            mav.addObject("mobileVo", mobileVo);
            return mav;
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
