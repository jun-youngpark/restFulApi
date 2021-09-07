package com.mnwise.wiseu.web.template.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.JsonUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.common.util.MultipartFileUtil;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.template.model.BrandtalkTemplateVo;
import com.mnwise.wiseu.web.template.model.KakaoButton;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import com.mnwise.wiseu.web.template.service.BrandtalkTemplateService;


@Controller
public class BrandtalkTemplateRegController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BrandtalkTemplateRegController.class);

    @Autowired private BrandtalkTemplateService brandTalkTemplateService;
    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private AjaxFileUploadMultipartResolver editorMultipartResolver;

    /**
     * - [템플릿>브랜드톡 템플릿 등록] 브랜드톡템플릿 첫 등록 페이지 <br/>
     * - URL : /template/brtTemplateReg.do (페이지출력)<br/>
     * - URL : /template/BrandTalkUpload.do (저장버튼을 누를시)<br/>
     * - JSP : /template/brandtalkTemplateReg.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/brtTemplateReg.do" , method=RequestMethod.GET)  // /template/brandtalkTemplateReg.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            final List<KakaoProfile> profileList = ecareScenarioService.getKakaoProfileList(getLoginId());
            returnData.put("kakaoProfileList", profileList);
            return new ModelAndView("template/brtTemplateReg", returnData);  // template/brandtalkTemplateReg
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [템플릿>브랜드톡 템플릿 등록] 브랜드톡템플릿 REST API 카카오 등록 <br/>
     * - URL : /template/BrandTalkUpload.do (저장버튼을 누를시)<br/>
     * - JSP : /template/brandtalkTemplateReg.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/template/brtTemplateReg.do" , method=RequestMethod.POST)  // /template/brandtalkTemplateReg.do
    public ResponseEntity<ResultDto> create(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = null;
        BrandtalkTemplateVo brandtalkTemplateVo = new BrandtalkTemplateVo();
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            // 파일 업로드 처리 1단계
            multipartRequest = editorMultipartResolver.resolveMultipart(request);
        } catch(MaxUploadSizeExceededException e) {
            log.warn("Upload file size exceed");
        }
        try {
            responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
            brandtalkTemplateVo.setKakaoTmplStatus(multipartRequest.getParameter("kakaoTmplStatus"));
            brandtalkTemplateVo.setCode(multipartRequest.getParameter("code"));
            brandtalkTemplateVo.setUnsubscribeContent(multipartRequest.getParameter("unsubscribeContent"));
            brandtalkTemplateVo.setAuthType(multipartRequest.getParameter("authType"));
            brandtalkTemplateVo.setTemplateName(multipartRequest.getParameter("templateName"));
            brandtalkTemplateVo.setName(multipartRequest.getParameter("templateName"));
            brandtalkTemplateVo.setMessageType(multipartRequest.getParameter("messageType"));
            brandtalkTemplateVo.setImageLink(multipartRequest.getParameter("imageLink"));
            brandtalkTemplateVo.setContent(multipartRequest.getParameter("content"));
            brandtalkTemplateVo.setSenderKey(multipartRequest.getParameter("senderKey"));
            brandtalkTemplateVo.setContentType(multipartRequest.getParameter("contentType"));
            brandtalkTemplateVo.setUserId(getLoginId());
            brandtalkTemplateVo.setBrandTalkImageFile(multipartRequest.getFile("brandTalkImageFile"));
            String button = multipartRequest.getParameter("button");
            if(StringUtil.isNotBlank(button)) {
                brandtalkTemplateVo.setButtons(JsonUtil.toList(button, KakaoButton.class));
            }
            brandTalkTemplateService.createBrandTalkTemplate(brandtalkTemplateVo);
            return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.OK), responseHeaders, HttpStatus.CREATED);
            //return new ResultDto(ResultDto.OK,brandTalkTemplateService.createBrandTalkTemplate(brandtalkTemplateVo));
        } catch(Exception e) {
            log.error(null, e);
            return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.FAIL, "while requesting kakao template create, exception occurred.\n" + e.getMessage()), responseHeaders, HttpStatus.CREATED);
            //return new ResultDto(ResultDto.FAIL, "while requesting kakao template create, exception occurred.\n" + e.getMessage());
        }
    }

    /**
     * 브랜드톡 템플릿 수정
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/template/updateBrandtalkTemplate.do" , method=RequestMethod.POST)
     public ResponseEntity<ResultDto> update(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = null;
        BrandtalkTemplateVo brandtalkTemplateVo = new BrandtalkTemplateVo();
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            // 파일 업로드 처리 1단계
            multipartRequest = editorMultipartResolver.resolveMultipart(request);
        } catch(MaxUploadSizeExceededException e) {
            log.warn("Upload file size exceed");
        }
        try {
            responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
            brandtalkTemplateVo.setKakaoTmplStatus(multipartRequest.getParameter("kakaoTmplStatus"));
            brandtalkTemplateVo.setCode(multipartRequest.getParameter("code"));
            brandtalkTemplateVo.setUnsubscribeContent(multipartRequest.getParameter("unsubscribeContent"));
            brandtalkTemplateVo.setAuthType(multipartRequest.getParameter("authType"));
            brandtalkTemplateVo.setTemplateName(multipartRequest.getParameter("templateName"));
            brandtalkTemplateVo.setName(multipartRequest.getParameter("templateName"));
            brandtalkTemplateVo.setMessageType(multipartRequest.getParameter("messageType"));
            brandtalkTemplateVo.setImageLink(multipartRequest.getParameter("imageLink"));
            brandtalkTemplateVo.setContentType(multipartRequest.getParameter("contentType"));
            brandtalkTemplateVo.setContent(multipartRequest.getParameter("content"));
            brandtalkTemplateVo.setContsNo(Integer.parseInt(multipartRequest.getParameter("contsNo")));
            brandtalkTemplateVo.setSenderKey(multipartRequest.getParameter("senderKey"));
            String button = multipartRequest.getParameter("button");
            String filePath = multipartRequest.getParameter("filePath");
            MultipartFile multipartFile = multipartRequest.getFile("brandTalkImageFile");
            if(StringUtil.isNotBlank(button)) {
                 brandtalkTemplateVo.setButtons(JsonUtil.toList(button, KakaoButton.class));
              }
             if(multipartFile != null && multipartFile.getSize() > 0) {
                 brandtalkTemplateVo.setBrandTalkImageFile(multipartFile);
              }else {
                  brandtalkTemplateVo.setBrandTalkImageFile(MultipartFileUtil.fileToMultipartFileConvert(filePath, "image"));
              }
              brandTalkTemplateService.updateBrandTalkTemplate(brandtalkTemplateVo);
              return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.OK), responseHeaders, HttpStatus.CREATED);
            //return new ResultDto(ResultDto.OK, brandTalkTemplateService.updateBrandTalkTemplate(brandtalkTemplateVo));
        } catch(Exception e) {
            log.error(null, e);
            return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.FAIL, "while requesting kakao template create, exception occurred.\n" + e.getMessage()), responseHeaders, HttpStatus.CREATED);
            //return new ResultDto(ResultDto.FAIL, "while requesting kakao template create, exception occurred.\n" + e.getMessage());
        }
    }

}
