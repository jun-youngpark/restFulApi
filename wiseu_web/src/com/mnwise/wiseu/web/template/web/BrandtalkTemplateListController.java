package com.mnwise.wiseu.web.template.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.JsonUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.common.util.MultipartFileUtil;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;
import com.mnwise.wiseu.web.template.model.BrandtalkTemplateVo;
import com.mnwise.wiseu.web.template.model.KakaoButton;
import com.mnwise.wiseu.web.template.model.KakaoProfile;
import com.mnwise.wiseu.web.template.service.BrandtalkTemplateService;
import com.mnwise.wiseu.web.template.service.KakaoTemplateService;
import com.mnwise.wiseu.web.template.service.MobileTemplateService;

@Controller
public class BrandtalkTemplateListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BrandtalkTemplateListController.class);

    @Autowired private BrandtalkTemplateService brandTalkTemplateService;
    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private MobileTemplateService mobileTemplateService;
    @Autowired private KakaoTemplateService kakaoTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver editorMultipartResolver;

    @RequestMapping(value="/template/brandtalkTemplateDownLoad.do" , method= {RequestMethod.GET,RequestMethod.POST})
    public void downloadBrandtalkTemplate(@RequestParam(defaultValue="") String senderKey, @RequestParam(defaultValue="Y") String kakaoTemplateUseYn,
                                          @RequestParam(defaultValue="") String kakaoTemplateStatus,HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            final UserVo userVo = getLoginUserVo();
            BrandtalkTemplateVo brandtalkTemplateVo=new BrandtalkTemplateVo();
            brandtalkTemplateVo.setUserVo(userVo);
            brandtalkTemplateVo.setFileType(Const.Channel.BRANDTALK);
            final String userId = userVo.getUserId();
            brandtalkTemplateVo.setUserId(userId);
            brandtalkTemplateVo.setSenderKey(senderKey);
            brandtalkTemplateVo.setUseYn(kakaoTemplateUseYn);
            brandtalkTemplateVo.setKakaoTmplStatus(kakaoTemplateStatus);

            /** Paging 및 검색을 위한 Parameter를 셋팅한다 */
            PagingUtil.setPagingInfo(request, brandtalkTemplateVo);
            /** totalCount > 0 이상일 때만. 리스트를 쿼리한다 * */
            final int totalCount = brandTalkTemplateService.getBrandTalkTemplateTotalCount(brandtalkTemplateVo);
            if(totalCount > 0) {
                brandtalkTemplateVo.setListCnt(getListCount(brandtalkTemplateVo, totalCount));
                brandtalkTemplateVo.setTotalRowCnt(totalCount);
                Map<String, List<BrandtalkTemplateVo>> map = new HashMap<>();
                map.put("brandtalkTemplateList", brandTalkTemplateService.getBrandTalkTemplateList(brandtalkTemplateVo));
                final ReportExcelDownload reportExcel = new ReportExcelDownload();
                try {
                    reportExcel.makeExcel(response, map, "ecare", "brandtalkTemplateList.xls");
                } catch(Exception e) {
                    log.error("while making excel file, io exception occurred. " + e.getMessage());
                }
            }
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
    private int getListCount(BrandtalkTemplateVo brandtalkTemplateVo, final int totalCount) {
        final int perPage = brandtalkTemplateVo.getCountPerPage();
        final int lastRow = perPage * brandtalkTemplateVo.getNowPage();
        if(lastRow <= totalCount) {
            return perPage;
        } else {
            return (totalCount + perPage) % lastRow;
        }
    }


    /**
     * - [템플릿>브랜드톡 템플릿 리스트] 브랜드톡 템플릿 리스트<br/>
     * - URL : /template/brtTemplateList.do (페이지출력)<br/>
     * - JSP : /template/brtTemplateList.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/brtTemplateList.do" , method={RequestMethod.GET, RequestMethod.POST})  // /template/brandtalkTemplateList.do
    public ModelAndView list(HttpServletRequest request, @RequestParam(defaultValue="") String senderKey, @RequestParam(defaultValue="Y") String kakaoTemplateUseYn,
                             @RequestParam(defaultValue="") String kakaoTemplateStatus) throws Exception {
        try {
            final BrandtalkTemplateVo brandtalkTemplateVo = new BrandtalkTemplateVo();
            final UserVo userVo = getLoginUserVo();
            brandtalkTemplateVo.setUserVo(userVo);
            brandtalkTemplateVo.setFileType(Const.Channel.BRANDTALK);
            final String userId = userVo.getUserId();
            brandtalkTemplateVo.setUserId(userId);
            brandtalkTemplateVo.setSenderKey(senderKey);
            brandtalkTemplateVo.setUseYn(kakaoTemplateUseYn);
            brandtalkTemplateVo.setKakaoTmplStatus(kakaoTemplateStatus);

            Map<String, Object> returnData = new HashMap<>();
            PagingUtil.setPagingInfo(request, brandtalkTemplateVo);
            // totalCount > 0 이상일 때만. 리스트를 쿼리한다
            final int totalCount = brandTalkTemplateService.getBrandTalkTemplateTotalCount(brandtalkTemplateVo);
            if(totalCount > 0) {
                brandtalkTemplateVo.setListCnt(getListCount(brandtalkTemplateVo, totalCount));
                brandtalkTemplateVo.setTotalRowCnt(totalCount);
                returnData.put("templateList", brandTalkTemplateService.getBrandTalkTemplateList(brandtalkTemplateVo));
            }
            PagingUtil.transferPagingInfo(request, totalCount);

            final List<KakaoProfile> profileList = ecareScenarioService.getKakaoProfileList(userId);
            returnData.put("kakaoProfileList", profileList);
            returnData.put("fileType", brandtalkTemplateVo.getFileType());
            returnData.put("searchWord", brandtalkTemplateVo.getSearchWord());
            returnData.put("contsNoArr", new ArrayList<String>());
            returnData.put("selectedSenderKey", senderKey);
            returnData.put("kakaoTemplateStatus", kakaoTemplateStatus);
            returnData.put("kakaoTemplateUseYn", kakaoTemplateUseYn);
            returnData.put("kakaoTemplateStatusMap", kakaoTemplateService.findKakaoTemplateStatusNameMap());
            return new ModelAndView("template/brtTemplateList", returnData);  // template/brandtalkTemplateList
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * 브랜드톡 템플릿 상세화면 표시
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/brtTemplateView.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/brandtalkTemplateView.do
    public ModelAndView view(HttpServletRequest request, BrandtalkTemplateVo paramVo) throws Exception {
        try {
            paramVo.setName(paramVo.getTemplateName());
            paramVo.setUserId(getLoginId());
            paramVo.setFileType(Const.Channel.BRANDTALK);
            final BrandtalkTemplateVo brandtalkTemplateVo = (BrandtalkTemplateVo) brandTalkTemplateService.findBrandtalkTemplateInfo(paramVo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(brandtalkTemplateVo.getGrpCd(), brandtalkTemplateVo.getUserId(), brandtalkTemplateVo.getAuthType())) {
                        return new ModelAndView("");
                    }

            Map<String, Object> returnData = new HashMap<>();
            final String jsonData = brandTalkTemplateService.getBrandTalkTemplate(brandtalkTemplateVo);
            returnData.put("brandtalkTemplateVo", brandtalkTemplateVo);
            returnData.put("jsonData", jsonData);
            return new ModelAndView("template/brtTemplateView", returnData);  // template/brandtalkTemplateView
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * 브랜드톡 템플릿 삭제
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/deleteBrandtalkTemplate.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public String delete(@RequestBody Map<String, Object> paramMap) throws Exception {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> contsMapList = (List<Map<String, Object>>)paramMap.get("contsNoList");
        String result ="";
        for(Map<String, Object> contsMap : contsMapList) {
            BrandtalkTemplateVo brandtalkTemplateVo = new BrandtalkTemplateVo();
            brandtalkTemplateVo.setCode((String)contsMap.get("code"));
            brandtalkTemplateVo.setContsNo(Integer.parseInt((String)contsMap.get("contsNo")));
            result =brandTalkTemplateService.deleteBrandTalkTemplate(brandtalkTemplateVo);
        }
        return result;
    }

}
