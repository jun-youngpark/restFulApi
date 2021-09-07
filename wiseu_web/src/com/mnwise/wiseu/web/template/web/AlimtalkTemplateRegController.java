package com.mnwise.wiseu.web.template.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.common.util.MultipartFileUtil;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.service.KakaoTemplateService;

@Controller
public class AlimtalkTemplateRegController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AlimtalkTemplateRegController.class);

    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private KakaoTemplateService kakaoTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;
    /**
     * [템플릿>알림톡 템플릿 등록] 알림톡 템플릿 등록 화면 출력
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/template/altTemplateReg.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            List<KakaoProfile> profileList = ecareScenarioService.getKakaoProfileList(getLoginId());

            ModelAndView mav = new ModelAndView("template/altTemplateReg");
            mav.addObject("mobileVo", new MobileVo());
            mav.addObject("kakaoProfileList", profileList);
            mav.addObject("alimTalkTemplateCategoryGroupList", kakaoTemplateService.findAlimtalkTemplateCategoryGroupList());
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>알림톡 템플릿 등록>등록방식:단건] 등록 버튼 클릭 - 알림톡 템플릿 단건 저장
     *
     * @param paramMap
     * @param request
     * @return
     */
    @RequestMapping(value="/template/createAlimtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ResultDto> createAlimtalkTemplate(HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");

             MultipartHttpServletRequest req = customMultipartResolver.resolveMultipart(request);
             List<MultipartFile> multipartFileList = new ArrayList<MultipartFile>();
             final MultipartFile multipartFile = req.getFile("image");
             Map<String, String> paramMap = new HashMap<String, String>();
             setParamMap(req, paramMap);
             if(multipartFile != null && multipartFile.getSize() > 0) {
                 String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                 kakaoTemplateService.saveFile(paramMap, multipartFile.getInputStream(), fileName, "preview");
                 multipartFileList.add(multipartFile);
             }
             paramMap.put("buttons", ServletRequestUtils.getStringParameter(req, "buttons",""));
             paramMap.put("kakaoQuickReplies", ServletRequestUtils.getStringParameter(req, "kakaoQuickReplies",""));
             kakaoTemplateService.createAlimtalkTemplate(paramMap,getLoginId() , multipartFileList);
             return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.OK), responseHeaders, HttpStatus.CREATED);
             //return new ResultDto(ResultDto.OK);  //IE 9에서 ajaxForm json 형태 return 시 파일 다운로드 되는 현상이 있어 수정
         } catch(Exception e) {
            log.error(null, e);
            return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.FAIL ,"while requesting kakao template create, exception occurred.\n" + e.getMessage()), responseHeaders, HttpStatus.CREATED);
            //return new ResultDto(ResultDto.FAIL, "while requesting kakao template create, exception occurred.\n" + e.getMessage());
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 저장 버튼 클릭
     *
     * @param paramMap
     * @param request
     * @return
     */
    @RequestMapping(value="/template/updateAlimtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
     public ResponseEntity<ResultDto> updateAlimtalkTemplate(HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
            MultipartHttpServletRequest req = customMultipartResolver.resolveMultipart(request);
            List<MultipartFile> multipartFileList = new ArrayList<MultipartFile>();
            final MultipartFile multipartFile = req.getFile("image");
            Map<String, String> paramMap = new HashMap<String, String>();
            String filePath = ServletRequestUtils.getStringParameter(req, "filePath","");
            String kakaoEmType = ServletRequestUtils.getStringParameter(req, "kakaoEmType","");
            setParamMap(req, paramMap);
            paramMap.put("buttons", ServletRequestUtils.getStringParameter(req, "buttons",""));
            paramMap.put("kakaoQuickReplies", ServletRequestUtils.getStringParameter(req, "kakaoQuickReplies",""));

            if(multipartFile != null && multipartFile.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                kakaoTemplateService.saveFile(paramMap, multipartFile.getInputStream(), fileName, "preview");
                multipartFileList.add(multipartFile);
            }else {
                if(Const.KkoEmType.IMAGE.equals(kakaoEmType)) {
                    if(StringUtils.isNotBlank(filePath)) {  //카카오 강조유형 이미지인 경우 기존 이미지 활용
                        multipartFileList.add(MultipartFileUtil.fileToMultipartFileConvert(filePath, "image"));
                    }
                }
            }
            kakaoTemplateService.updateAlimtalkTemplate(paramMap, getLoginUserVo() , multipartFileList);
            return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.OK), responseHeaders, HttpStatus.CREATED);
        } catch(Exception e) {
            log.error(null, e);
            return new ResponseEntity<ResultDto>(new ResultDto(ResultDto.FAIL, "while requesting kakao template update, exception occurred.\n" + e.getMessage()), responseHeaders, HttpStatus.CREATED);
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 저장 버튼 클릭
     *
     * @param paramMap
     * @param request
     * @return
     */
    @RequestMapping(value = "/template/getKakaoCategoryCd.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto getKakaoCategoryCd(@RequestParam Map<String, String> paramMap, HttpServletRequest request) {
        try {
            List<CaseInsensitiveMap> categoryCdList = kakaoTemplateService.findAlimtalkTemplateCategoryCdList(paramMap.get("categoryGroup"));
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(categoryCdList);
            return resultDto;
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, "while requesting kakao template update, exception occurred.\n" + e.getMessage());
        }
    }

    public void setParamMap(MultipartHttpServletRequest multipartRequest, Map<String, String> paramMap) {
        Enumeration<String> iterator = multipartRequest.getParameterNames();
        while(iterator.hasMoreElements()) {
            String key =iterator.nextElement();
            String value = multipartRequest.getParameter(key);
            if(StringUtils.isNotBlank(value)) {
                paramMap.put(key , value);
            }
        }
    }

}
