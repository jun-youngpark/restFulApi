package com.mnwise.wiseu.web.report.web.ecare;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareListService;

/**
 * 이케어 리포트 리스트 Controller
 */
@Controller
public class EcareListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareListController.class);

    @Autowired private EcareListService ecareListService;

    /**
     * - [리포트>이케어>이케어 리스트] 이케어 리포트 리스트 <br/>
     * - URL : /report/ecare/ecareRptList.do <br/>
     * - JSP : /report/ecare/ecareRptList.jsp <br/>
     *   wiseMoka버전 일 경우(web.exec.mode=2) 이케어 리포트 리스트 스케줄만 보이도록. 이케어 리포트 리스트
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/report/ecare/ecareRptList.do", method = {RequestMethod.GET, RequestMethod.POST})  // /report/ecare/ecare_list.do
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String searchQstartDt = StringUtil.defaultString(request.getParameter("searchQstartDt"));  // 검색 시작일
            String searchQendDt = StringUtil.defaultString(request.getParameter("searchQendDt"));  // 검색 종료일
            // 기본값 추가.
            if("".equals(searchQendDt) || "".equals(searchQstartDt)) {
                searchQstartDt = DateUtil.addMonths(-12, "yyyyMMdd");
                searchQendDt = DateUtil.getNowDate();
            }

            /** 실시간, 스케쥴 여부 */
            String serviceType = StringUtil.defaultString(request.getParameter("serviceType"));
            String subType = StringUtil.defaultString(request.getParameter("subType"));
            //wiseMoka 버전일 경우
            if("2".equals(super.webExecMode) || (StringUtil.isBlank(serviceType) && StringUtil.isBlank(subType))) {
                serviceType = "S";
                subType = "S";
            } else {
                if(StringUtil.isEmpty(serviceType)) {
                    if(!StringUtil.isEmpty((String) WebUtils.getSessionAttribute(request, "serviceType"))) {
                        serviceType = (String) WebUtils.getSessionAttribute(request, "serviceType");
                        subType = (String) WebUtils.getSessionAttribute(request, "subType");
                    } else {
                        serviceType = "R";
                    }
                }
            }
            String searchWord = StringUtil.defaultString(request.getParameter("searchWord"));

            // Session 검색결과 담기
            request.getSession().setAttribute("searchEWord", searchWord);
            request.getSession().setAttribute("searchEQstartDt", searchQstartDt);
            request.getSession().setAttribute("searchEQendDt", searchQendDt);

            UserVo userVo = getLoginUserVo();

            int nowPage = Integer.parseInt(StringUtil.defaultString(request.getParameter("nowPage"), "1"));
            int countPerPage = userVo.getListCountPerPage();
            String searchChannel = request.getParameter("searchChannel");

            Map<String, Object> returnData = new HashMap<>();

            EcareScenarioInfoVo ecareScenarioInfoVo = new EcareScenarioInfoVo();
            ecareScenarioInfoVo.setServiceType(serviceType);
            ecareScenarioInfoVo.setNowPage(nowPage);
            ecareScenarioInfoVo.setSearchWord(searchWord);
            ecareScenarioInfoVo.setCountPerPage(countPerPage);
            ecareScenarioInfoVo.setSubType(subType);
            ecareScenarioInfoVo.setUserVo(userVo);
            ecareScenarioInfoVo.setSearchQstartDt(searchQstartDt);
            ecareScenarioInfoVo.setSearchQendDt(searchQendDt);
            ecareScenarioInfoVo.setEndRow(countPerPage * nowPage);
            ecareScenarioInfoVo.setChannelType(searchChannel);

            int ecareReportListTotalCount = ecareListService.selectEcareReportListCount(ecareScenarioInfoVo);
            returnData.put("ecareReportListTotalCount", String.valueOf(ecareReportListTotalCount));

            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (ecareReportListTotalCount / aCountPerPage); // 몫
            int rCountPerPage = (ecareReportListTotalCount % aCountPerPage); // 나머지

            if(ecareScenarioInfoVo.getNowPage() <= qCountPerPage) {
                ecareScenarioInfoVo.setListCnt(userVo.getListCountPerPage());
            } else {
                ecareScenarioInfoVo.setListCnt(rCountPerPage);
            }

            if(ecareReportListTotalCount > 0) {
                // 공통 테이블 조회를 위해 언어를 넘겨준다.
                ecareScenarioInfoVo.setLanguage(userVo.getLanguage());
                returnData.put("ecareReportList", ecareListService.selectEcareReportList(ecareScenarioInfoVo));
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(nowPage);
            pgBean.setTotalRowCnt(ecareReportListTotalCount);
            pgBean.setPageSize(countPerPage);
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            returnData.put("serviceType", serviceType);
            returnData.put("subType", subType);
            returnData.put("paging", paging);
            returnData.put("searchQstartDt", searchQstartDt);
            returnData.put("searchQendDt", searchQendDt);
            returnData.put("searchWord", searchWord);
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("userChannelList", super.ecareChannelUseList);
            return new ModelAndView("report/ecare/ecareRptList", returnData);  // report/ecare/ecare_list
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
