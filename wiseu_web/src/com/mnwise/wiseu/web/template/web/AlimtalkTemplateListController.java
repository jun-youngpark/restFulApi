package com.mnwise.wiseu.web.template.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.http.NameValuePair;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.common.util.KakaoRestApiUtil;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import com.mnwise.wiseu.web.template.model.KakaoSingleResponse;
import com.mnwise.wiseu.web.template.model.KakaoTemplateComment;
import com.mnwise.wiseu.web.template.model.KakaoTemplateVo;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.service.KakaoTemplateService;
import com.mnwise.wiseu.web.template.service.MobileTemplateService;

@Controller
public class AlimtalkTemplateListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AlimtalkTemplateListController.class);

    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private KakaoTemplateService kakaoTemplateService;
    @Autowired private MobileTemplateService mobileTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    @Value("${kakao.biz.api.template}")
    private String kakaoBizApiTemplateUrl;

    /**
     * - [템플릿>알림톡 템플릿 리스트] 알림톡 템플릿 리스트 <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/altTemplateList.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/alimtalkTemplate.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            String userId = userVo.getUserId();

            MobileVo mobileVo = new MobileVo();
            mobileVo.setUserVo(userVo);
            mobileVo.setTagNo(ServletRequestUtils.getIntParameter(request, "tagNo", -1));
            mobileVo.setFileType(Channel.ALIMTALK);
            mobileVo.setUserId(userId);

            String kakaoSenderKey = ServletRequestUtils.getStringParameter(request, "kakaoSenderKey");
            if(StringUtil.isNotEmpty(kakaoSenderKey)) {
                mobileVo.setKakaoSenderKey(kakaoSenderKey);
            }
            String kakaoInspStatus = ServletRequestUtils.getStringParameter(request, "kakaoInspStatus");
            if(StringUtil.isNotEmpty(kakaoInspStatus)) {
                mobileVo.setKakaoInspStatus(kakaoInspStatus);
            }
            String kakaoTemplateStatus = ServletRequestUtils.getStringParameter(request, "kakaoTemplateStatus");
            if(StringUtil.isNotEmpty(kakaoTemplateStatus)) {
                mobileVo.setKakaoTmplStatus(kakaoTemplateStatus);
            }
            String kakaoTemplateUseYn = ServletRequestUtils.getStringParameter(request, "kakaoTemplateUseYn", "A");
            mobileVo.setUseYn(kakaoTemplateUseYn);

            /** Paging 및 검색을 위한 Parameter를 셋팅한다 */
            PagingUtil.setPagingInfo(request, mobileVo);

            ModelAndView mav = new ModelAndView("template/altTemplateList");  // template/alimtalk_template_list
            int totalCount = kakaoTemplateService.getAlimtalkTemplateTotalCount(mobileVo);
            if(totalCount > 0) {
                mobileVo.setListCnt(getListCount(mobileVo, totalCount));
                mobileVo.setTotalRowCnt(totalCount);
                mav.addObject("templateList", kakaoTemplateService.getAlimtalkTemplateList(mobileVo));
            }

            PagingUtil.transferPagingInfo(request, totalCount);

            List<KakaoProfile> profileList = ecareScenarioService.getKakaoProfileList(userId);
            mav.addObject("kakaoProfileList", profileList);
            mav.addObject("fileType", mobileVo.getFileType());
            mav.addObject("searchWord", mobileVo.getSearchWord());
            mav.addObject("contsNoArr", new ArrayList<String>());
            mav.addObject("kakaoInspStatus", kakaoInspStatus);
            mav.addObject("kakaoTemplateStatus", kakaoTemplateStatus);
            mav.addObject("selectedKakaoSenderKey", kakaoSenderKey);
            mav.addObject("kakaoTemplateUseYn", kakaoTemplateUseYn);
            mav.addObject("kakaoInspStatusMap", kakaoTemplateService.findKakaoTemplateInspStatusNameMap());
            mav.addObject("kakaoTemplateStatusMap", kakaoTemplateService.findKakaoTemplateStatusNameMap());

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 현재 페이지에 출력할 건수를 구하는 역할
     *
     * @param mobileVo
     * @param totalCount
     * @return
     */
    private int getListCount(MobileVo mobileVo, int totalCount) {
        int perPage = mobileVo.getCountPerPage();
        int lastRow = perPage * mobileVo.getNowPage();

        if(lastRow <= totalCount) {
            return perPage;
        } else {
            return (totalCount + perPage) % lastRow;
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트] 알림톡 템플릿 내용조회
     *
     * @param contsNo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value={"/template/altTemplateView.do", "/template/altTemplatePopupView.do"}, method={RequestMethod.GET, RequestMethod.POST})  // /template/viewAlimtalkTemplate.do
    public ModelAndView view(int contsNo,@RequestParam(defaultValue="N") String popupYn, HttpServletRequest request) throws Exception {
        try {
            MobileVo paramVo = new MobileVo();
            paramVo.setContsNo(contsNo);
            paramVo.setUserId(getLoginId());

            MobileVo mobileVo = mobileTemplateService.findAlimtalkTemplateInfo(paramVo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(mobileVo.getGrpCd(), mobileVo.getUserId(), mobileVo.getAuthType())) {
                return new ModelAndView("");
            }

            String kakaoSenderKey = mobileVo.getKakaoSenderKey();
            String kakaoTmplCd = mobileVo.getKakaoTmplCd();
            List<NameValuePair> params = KakaoRestApiUtil.createParameters(kakaoSenderKey, "S", kakaoTmplCd, "", "");
            String jsonData = KakaoRestApiUtil.callRestApi(this.kakaoBizApiTemplateUrl, params);

            if(StringUtil.isEmpty(jsonData)) {
                log.error("Kakao Template Response is empty");
            } else {
                KakaoSingleResponse kakaoResponse = KakaoRestApiUtil.parseSingleResponse(jsonData);
                String code = kakaoResponse.getCode();

                if(KakaoRestApiUtil.SUCCESS_CODE.equals(code)) {
                    CaseInsensitiveMap statusMap = kakaoTemplateService.findKakaoTemplateStatusNameMap();
                    CaseInsensitiveMap inspStatusMap = kakaoTemplateService.findKakaoTemplateInspStatusNameMap();
                    CaseInsensitiveMap commentStatusMap = kakaoTemplateService.findKakaoTemplateCommentStatusNameMap();

                    KakaoTemplateVo data = kakaoResponse.getData();
                    String inspectionStatus = data.getInspectionStatus();
                    String templateStatus = data.getStatus();

                    mobileVo.setKakaoInspStatus(inspectionStatus);
                    mobileVo.setKakaoInspStatusNm((String)inspStatusMap.get(inspectionStatus));
                    mobileVo.setKakaoTmplStatus(templateStatus);
                    mobileVo.setKakaoTmplStatusNm((String)statusMap.get(templateStatus));

                    String statusNm;
                    List<KakaoTemplateComment> comments = data.getComments();
                    for(KakaoTemplateComment comment : comments) {
                        statusNm = (String)commentStatusMap.get(comment.getStatus());
                        if(statusNm != null) {
                            comment.setStatus(statusNm);
                        }
                    }
                    mobileVo.setKakaoTemplateComments(comments);
                } else {
                    log.error("Kakao Template is not created.");
                    log.error("Result Code: " + code + ", Message: " + kakaoResponse.getMessage());
                }
            }

            ModelAndView mav = new ModelAndView("template/altTemplateView");  // template/alimtalk_template_view
            mav.addObject("mobileVo", mobileVo);
            mav.addObject("jsonData", jsonData);
            mav.addObject("popupYn", popupYn);
            mav.addObject("alimTalkTemplateCategoryGroupList", kakaoTemplateService.findAlimtalkTemplateCategoryGroupList());
            String categoryCd = mobileVo.getCategoryCd();
            if (StringUtil.isNotBlank(categoryCd) && categoryCd.length() == 6) {
                String categoryGroup = categoryCd.substring(0, 3);
                mobileVo.setCategoryGroup(categoryGroup);
                mav.addObject("alimTalkTemplateCategoryCdList", kakaoTemplateService.findAlimtalkTemplateCategoryCdList(categoryGroup));
            }

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트] 선택 템플릿 다운로드 버튼 클릭
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/template/downloadAlimtalkTemplate.do", method={RequestMethod.GET, RequestMethod.POST})
    public void downloadAlimtalkTemplate(int[] contsNoArr, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            MobileVo mobileVo = new MobileVo();
            mobileVo.setUserVo(getLoginUserVo());
            mobileVo.setUserId(getLoginId());
            mobileVo.setMobileNoArray(contsNoArr);
            List<MobileVo> list = kakaoTemplateService.findAlimtalkTemplates(mobileVo);

            Map<String, List<MobileVo>> map = new HashMap<>();
            map.put("alimtalkTemplateList", list);

            ReportExcelDownload reportExcel = new ReportExcelDownload();
            try {
                reportExcel.makeExcel(response, map, "ecare", "alimtalkTemplateList.xls");
            } catch(Exception e) {
                log.error("while making excel file, io exception occurred. " + e.getMessage());
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트] 일괄검수요청 버튼 클릭
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/template/inspectAllAlimtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto inspectAllAlimtalkTemplate(HttpServletRequest request) {
        try {
            kakaoTemplateService.inspectAllAlimtalkTemplate(getLoginId());
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, "while requesting kakao template inspection, exception occurred.\n" + e.getMessage());
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트] 선택검수요청 버튼 클릭
     * [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 검수요청 버튼 클릭
     *
     * @param contsNoArr
     * @param request
     * @return
     */
    @RequestMapping(value="/template/inspectAlimtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto inspectAlimtalkTemplate(int[] contsNoArr, HttpServletRequest request) {
        try {
            kakaoTemplateService.inspectAlimtalkTemplate(contsNoArr, getLoginId());
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, "while requesting kakao template inspection, exception occurred.\n" + e.getMessage());
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트] 검수요청취소 버튼 클릭
     * [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 검수요청취소 버튼 클릭
     *
     * @param contsNoArr
     * @param request
     * @return
     */
    @RequestMapping(value="/template/cancleInspectAlimtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto cancleInspectAlimtalkTemplate(int[] contsNoArr, HttpServletRequest request) {
        try {
            kakaoTemplateService.cancleInspectAlimtalkTemplate(contsNoArr, getLoginId());
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, "while requesting kakao template cancel inspection, exception occurred.\n" + e.getMessage());
        }
    }

    /**
     * [템플릿>알림톡 템플릿 리스트] 삭제 버튼 클릭
     * [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 삭제 버튼 클릭
     *
     * @param contsNoArr
     * @param request
     * @return
     */
    @RequestMapping(value="/template/deleteAlimtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteAlimtalkTemplate(int[] contsNoArr, HttpServletRequest request) {
        try {
            kakaoTemplateService.deleteAlimtalkTemplate(contsNoArr, getLoginId());
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, "while requesting kakao template delete, exception occurred.\n" + e.getMessage());
        }
    }


    /**
     * [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 문의 등록
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/template/altCommentUpload.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/alimtalkCommentUpload.do
    public final ModelAndView addComment(final HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("template/altCommentUpload");  // template/comment_upload

        String code = "999";
        try {
            MultipartHttpServletRequest multipartRequest = null;
            JSONObject jsonObj = new JSONObject();
            try {
                multipartRequest = customMultipartResolver.resolveMultipart(request);
                jsonObj.put("contsNo", ServletRequestUtils.getStringParameter(multipartRequest, "contsNo","0"));
                jsonObj.put("comment", ServletRequestUtils.getStringParameter(multipartRequest, "comment"));
            } catch(final MaxUploadSizeExceededException e) {
                code="size Error";
            }

            List<MultipartFile> multipartFileList = new ArrayList<>();
            Iterator<String> iterator = multipartRequest.getFileNames();
            while(iterator.hasNext()) {
                MultipartFile multipartFile = multipartRequest.getFile(iterator.next());
                if(multipartFile.isEmpty()==false && multipartFile.getSize() > 0) {
                    multipartFileList.add(multipartFile);
                }
            }

            log.debug(jsonObj.toJSONString());

            if(multipartFileList.size()>0) {
                code = kakaoTemplateService.addAlimtalkTemplateComment(jsonObj.toJSONString() , multipartFileList);
            } else {
                code = kakaoTemplateService.addAlimtalkTemplateComment(jsonObj.toJSONString());
            }
        } catch(final Exception e) {
            log.error("while creating alimtalk template with file, exception occurred. Message: " + e.getMessage());
            mav.addObject("isFail", "true");
        }

        mav.addObject("code", code);

        return mav;
    }
}
