package com.mnwise.wiseu.web.report.web.ecare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.report.model.ecare.RealtimeacceptVo;
import com.mnwise.wiseu.web.report.service.ecare.RequestTableService;

/**
 * 이케어 리포트 리스트 Controller
 */
@Controller
@RequestMapping(value="/ecare")
public class RequestTableController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(RequestTableController.class);

    @Autowired private RequestTableService requestTableService;

    /**
     * - [리포트>이케어>이케어 리스트] 이케어 리포트 리스트 <br/>
     * - URL : /report/ecare/requestTableView.do <br/>
     * - JSP : /report/ecare/requestTableView.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/requestTableView.do", method = {RequestMethod.GET, RequestMethod.POST}) 
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String searchQstartDt = StringUtil.defaultString(request.getParameter("searchQstartDt"));  // 검색 시작일
            String searchQendDt = StringUtil.defaultString(request.getParameter("searchQendDt"));  // 검색 종료일
            String searchCustomerEmail = StringUtil.defaultString(request.getParameter("searchCustomerEmail"));  // 주소
            String searchEcareNo = StringUtil.defaultString(request.getParameter("searchEcareNo"));  // 이케어번호,
            String searchSeq = StringUtil.defaultString(request.getParameter("searchSeq"));  // seq
            String searchStartReqTm = StringUtil.defaultString(request.getParameter("searchStartReqTm"), "000000");  // 발송시간 검색
            String searchEndReqTm = StringUtil.defaultString(request.getParameter("searchEndReqTm"), "235959");  // 발송시간 검색
            String searchChannel = StringUtil.defaultString(request.getParameter("searchChannel"));  // 채널 검색
            String searchSendFg = StringUtil.defaultString(request.getParameter("searchSendFg"));  // 발송상태 검색

            System.out.println(searchStartReqTm);
            System.out.println(searchEndReqTm);
            // 기본값 추가.
            if("".equals(searchQendDt) || "".equals(searchQstartDt)) {
                searchQstartDt = DateUtil.addDays(-7, "yyyyMMdd");
                searchQendDt = DateUtil.getNowDate();
            }

            /** 실시간, 스케쥴 여부 */
            String subType = StringUtil.defaultString(request.getParameter("subType"), "A");


            UserVo userVo = getLoginUserVo();

            int nowPage = Integer.parseInt(StringUtil.defaultString(request.getParameter("nowPage"), "1"));
            int countPerPage = userVo.getListCountPerPage();

            Map<String, Object> returnData = new HashMap<>();

            RealtimeacceptVo realtimeacceptVo = new RealtimeacceptVo();
            realtimeacceptVo.setNowPage(nowPage);
            realtimeacceptVo.setSearchWord(searchCustomerEmail);
            realtimeacceptVo.setCountPerPage(countPerPage);
            realtimeacceptVo.setUserVo(userVo);
            realtimeacceptVo.setSearchQstartDt(searchQstartDt);
            realtimeacceptVo.setSearchQendDt(searchQendDt);
            realtimeacceptVo.setEndRow(countPerPage * nowPage);
            realtimeacceptVo.setChannel(searchChannel);
            realtimeacceptVo.setSearchColumn(searchEcareNo);
            realtimeacceptVo.setSearchChannel(searchChannel);
            realtimeacceptVo.setSearchSendFg(searchSendFg);
            realtimeacceptVo.setSearchSeq(searchSeq);
            realtimeacceptVo.setSearchStartReqTm(searchStartReqTm.replace(":", ""));
            realtimeacceptVo.setSearchEndReqTm(searchEndReqTm.replace(":", ""));
            int realtimeaccptTotalCount = 0;
            if("A".equals(subType)) {       //전체
                realtimeaccptTotalCount = requestTableService.selectAllInterfaceCount(realtimeacceptVo);
            }else if("N".equals(subType)) { //준실시간
                realtimeaccptTotalCount = requestTableService.selectRealtimeacceptCount(realtimeacceptVo);
            }else {                         //스케줄
                realtimeaccptTotalCount = requestTableService.selectBatchCount(realtimeacceptVo);
            }

            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (realtimeaccptTotalCount / aCountPerPage); // 몫
            int rCountPerPage = (realtimeaccptTotalCount % aCountPerPage); // 나머지

            if(realtimeacceptVo.getNowPage() <= qCountPerPage) {
                realtimeacceptVo.setListCnt(userVo.getListCountPerPage());
            } else {
                realtimeacceptVo.setListCnt(rCountPerPage);
            }

            if(realtimeaccptTotalCount > 0) {
                List<RealtimeacceptVo> RealtimeacceptList;
                if("A".equals(subType)) {       //전체
                    RealtimeacceptList = requestTableService.selectAllInterfaceList(realtimeacceptVo);
                }else if("N".equals(subType)) { //준실시간
                    RealtimeacceptList = requestTableService.selectRealtimeacceptList(realtimeacceptVo);
                }else {                         //스케줄
                    RealtimeacceptList = requestTableService.selectBatchList(realtimeacceptVo);
                }
                System.out.println(RealtimeacceptList.toString());
                returnData.put("requestTableList", RealtimeacceptList);
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(nowPage);
            pgBean.setTotalRowCnt(realtimeaccptTotalCount);
            pgBean.setPageSize(countPerPage);
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            returnData.put("realtimeaccptTotalCount", String.valueOf(realtimeaccptTotalCount));
            returnData.put("actionUrl", "/ecare/requestTableView.do");
            returnData.put("subType", subType);
            returnData.put("paging", paging);
            returnData.put("searchQstartDt", searchQstartDt);
            returnData.put("searchQendDt", searchQendDt);
            returnData.put("searchCustomerEmail", searchCustomerEmail);
            returnData.put("searchEcareNo", searchEcareNo);
            returnData.put("searchSendFg", searchSendFg);
            returnData.put("searchSeq", searchSeq);
            returnData.put("searchChannel", searchChannel);
            returnData.put("searchStartReqTm", searchStartReqTm);
            returnData.put("searchEndReqTm", searchEndReqTm);
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("userChannelList", super.ecareChannelUseList);
            return new ModelAndView("report/ecare/requestTableView", returnData);  
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
    @RequestMapping(value = "/requestTableResend.json", method = { RequestMethod.POST})  
    @ResponseBody public int resend(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String seq = StringUtil.defaultString(request.getParameter("seq")); 
        String ecareNo = StringUtil.defaultString(request.getParameter("ecareNo")); 
        /** 실시간, 스케쥴 여부 */
        String subType = requestTableService.getSubType(ecareNo);
        RealtimeacceptVo realtimeacceptVo = new RealtimeacceptVo();
        realtimeacceptVo.setSeq(seq);
        int cnt;
        if("N".equals(subType)) {
            cnt = requestTableService.insertResendOne(realtimeacceptVo);
        }else {
            cnt = requestTableService.insertResendOneBatch(realtimeacceptVo);
        }
        
        return cnt;
    }
    
    
    /**
     * - 이케어 리포트 리스트 비계정 접속<br/>
     * - URL : /report/ecare/requestTableView.do <br/>
     * - JSP : /report/ecare/requestTableView.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ext_requestView.do", method = {RequestMethod.GET, RequestMethod.POST}) 
    public ModelAndView extlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String searchQstartDt = StringUtil.defaultString(request.getParameter("searchQstartDt"));  // 검색 시작일
            String searchQendDt = StringUtil.defaultString(request.getParameter("searchQendDt"));  // 검색 종료일
            String searchCustomerEmail = StringUtil.defaultString(request.getParameter("searchCustomerEmail"));  // 주소
            String searchEcareNo = StringUtil.defaultString(request.getParameter("searchEcareNo"));  // 이케어번호,
            String searchSeq = StringUtil.defaultString(request.getParameter("searchSeq"));  // seq
            String searchStartReqTm = StringUtil.defaultString(request.getParameter("searchStartReqTm"), "000000");  // 발송시간 검색
            String searchEndReqTm = StringUtil.defaultString(request.getParameter("searchEndReqTm"), "235959");  // 발송시간 검색
            String searchChannel = StringUtil.defaultString(request.getParameter("searchChannel"));  // 채널 검색
            String searchSendFg = StringUtil.defaultString(request.getParameter("searchSendFg"));  // 발송상태 검색
            // 기본값 추가.
            if("".equals(searchQendDt) || "".equals(searchQstartDt)) {
                searchQstartDt = DateUtil.addDays(-7, "yyyyMMdd");
                searchQendDt = DateUtil.getNowDate();
            }

            /** 실시간, 스케쥴 여부 */
            String subType = StringUtil.defaultString(request.getParameter("subType"), "A");



            int nowPage = Integer.parseInt(StringUtil.defaultString(request.getParameter("nowPage"), "1"));
            int countPerPage = 10;

            Map<String, Object> returnData = new HashMap<>();

            RealtimeacceptVo realtimeacceptVo = new RealtimeacceptVo();
            realtimeacceptVo.setNowPage(nowPage);
            realtimeacceptVo.setSearchWord(searchCustomerEmail);
            realtimeacceptVo.setCountPerPage(countPerPage);
            realtimeacceptVo.setSearchQstartDt(searchQstartDt);
            realtimeacceptVo.setSearchQendDt(searchQendDt);
            realtimeacceptVo.setEndRow(countPerPage * nowPage);
            realtimeacceptVo.setChannel(searchChannel);
            realtimeacceptVo.setSearchColumn(searchEcareNo);
            realtimeacceptVo.setSearchChannel(searchChannel);
            realtimeacceptVo.setSearchSendFg(searchSendFg);
            realtimeacceptVo.setSearchSeq(searchSeq);
            realtimeacceptVo.setSearchStartReqTm(searchStartReqTm.replace(":", ""));
            realtimeacceptVo.setSearchEndReqTm(searchEndReqTm.replace(":", ""));
            int realtimeaccptTotalCount = 0;
            if("A".equals(subType)) {       //전체
                realtimeaccptTotalCount = requestTableService.selectAllInterfaceCount(realtimeacceptVo);
            }else if("N".equals(subType)) { //준실시간
                realtimeaccptTotalCount = requestTableService.selectRealtimeacceptCount(realtimeacceptVo);
            }else {                         //스케줄
                realtimeaccptTotalCount = requestTableService.selectBatchCount(realtimeacceptVo);
            }

            int aCountPerPage = 10;
            int qCountPerPage = (realtimeaccptTotalCount / aCountPerPage); // 몫
            int rCountPerPage = (realtimeaccptTotalCount % aCountPerPage); // 나머지

            if(realtimeacceptVo.getNowPage() <= qCountPerPage) {
                realtimeacceptVo.setListCnt(10);
            } else {
                realtimeacceptVo.setListCnt(rCountPerPage);
            }

            if(realtimeaccptTotalCount > 0) {
                List<RealtimeacceptVo> RealtimeacceptList;
                if("A".equals(subType)) {       //전체
                    RealtimeacceptList = requestTableService.selectAllInterfaceList(realtimeacceptVo);
                }else if("N".equals(subType)) { //준실시간
                    RealtimeacceptList = requestTableService.selectRealtimeacceptList(realtimeacceptVo);
                }else {                         //스케줄
                    RealtimeacceptList = requestTableService.selectBatchList(realtimeacceptVo);
                }
                
                returnData.put("requestTableList", RealtimeacceptList);
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(nowPage);
            pgBean.setTotalRowCnt(realtimeaccptTotalCount);
            pgBean.setPageSize(countPerPage);
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            returnData.put("realtimeaccptTotalCount", String.valueOf(realtimeaccptTotalCount));
            returnData.put("actionUrl", "/ecare/ext_requestView.do");
            returnData.put("subType", subType);
            returnData.put("paging", paging);
            returnData.put("searchQstartDt", searchQstartDt);
            returnData.put("searchQendDt", searchQendDt);
            returnData.put("searchCustomerEmail", searchCustomerEmail);
            returnData.put("searchEcareNo", searchEcareNo);
            returnData.put("searchSendFg", searchSendFg);
            returnData.put("searchSeq", searchSeq);
            returnData.put("searchChannel", searchChannel);
            returnData.put("searchStartReqTm", searchStartReqTm);
            returnData.put("searchEndReqTm", searchEndReqTm);
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("userChannelList", super.ecareChannelUseList);
            return new ModelAndView("report/ecare/requestTableView", returnData);  
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
