package com.mnwise.wiseu.web.ecare.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.ecare.model.CancelVo;
import com.mnwise.wiseu.web.ecare.model.EcarePerHistoryVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.ecare.service.EcareCancelServcie;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.report.model.ecare.RealtimeacceptVo;

/**
 * 이케어 취소요청 Controller
 * 발송예약된 I/F 대상자에 대해서 취소요청 처리를 진행한다.
 */
@Controller
public class EcareCancelFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareCancelFormController.class);
    
    @Autowired private EcareCancelServcie ecareCancelServcie;
    @Autowired private EcareScenarioService ecareScenarioService;

    
    /**
     * [이케어>이케어 취소요청] 이케어 취소
     */
    @RequestMapping(value="/ecare/cancel_form.json", method={RequestMethod.POST})
    public @ResponseBody int cancel(EcarePerHistoryVo ecarePerHistoryVo, HttpServletRequest request) throws Exception {
        Map<String, Object> returnData = new HashMap<String, Object>();
        String reqDt = ServletRequestUtils.getStringParameter(request, "reqDt", DateUtil.getNowDateTime("yyyyMMdd")).replace("-", "");   // 요청일자
        String seq = ServletRequestUtils.getStringParameter(request, "seq", "");   // SEQ
        int ecareNo = Integer.parseInt(ServletRequestUtils.getStringParameter(request, "ecareNo", "0"));   // 이케어번호
        
        String now = DateUtil.getNowDateTime("yyyyMMddHHmmssSSSSS");
        CancelVo cancelVo = new CancelVo(); // 취소클래스 객체
        cancelVo.setCanReqDtm(now.substring(0, 14));
        cancelVo.setCanSeq(ecareNo + "_" + now);
        cancelVo.setOrgSeq(seq);
        cancelVo.setOrgReqDt(reqDt);
        cancelVo.setEcareNo(ecareNo);
        int cnt = ecareCancelServcie.insertCancel(cancelVo);
        
        return cnt;
    }

    @RequestMapping(value = "/ecare/cancelSendAll.json", method = { RequestMethod.POST})  
    @ResponseBody public int cancelAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String searchQstartDt = StringUtil.defaultString(request.getParameter("searchQstartDt"));  // 검색 시작일
        String searchQendDt = StringUtil.defaultString(request.getParameter("searchQendDt"));  // 검색 종료일
        String searchWord = StringUtil.defaultString(request.getParameter("searchCustomerEmail"));  // 주소
        String searchColumn = StringUtil.defaultString(request.getParameter("searchEcareNo"));  //이케어번호
        String searchSeq = StringUtil.defaultString(request.getParameter("searchSeq"));  // seq
        String searchStartReqTm = StringUtil.defaultString(request.getParameter("searchStartReqTm"));  // 발송시간 검색
        String searchEndReqTm = StringUtil.defaultString(request.getParameter("searchEndReqTm"));  // 발송시간 검색
        String searchChannel = StringUtil.defaultString(request.getParameter("searchChannel"));  // 채널 검색
        String searchSendFg = StringUtil.defaultString(request.getParameter("searchSendFg"));  // 발송상태 검색
        // 기본값 추가.
        if("".equals(searchQendDt) || "".equals(searchQstartDt)) {
            searchQstartDt = DateUtil.addDays(-7, "yyyyMMdd");
            searchQendDt = DateUtil.getNowDate();
        }

        /** 실시간, 스케쥴 여부 */
        String subType = StringUtil.defaultString(request.getParameter("subType"));
        UserVo userVo = getLoginUserVo();
        // Session 검색결과 담기
        request.getSession().setAttribute("searchEWord", searchWord);
        request.getSession().setAttribute("searchEQstartDt", searchQstartDt);
        request.getSession().setAttribute("searchEQendDt", searchQendDt);
        RealtimeacceptVo realtimeacceptVo = new RealtimeacceptVo();
        realtimeacceptVo.setSearchWord(searchWord);
        realtimeacceptVo.setUserVo(userVo);
        realtimeacceptVo.setSearchQstartDt(searchQstartDt);
        realtimeacceptVo.setSearchQendDt(searchQendDt);
        realtimeacceptVo.setChannel(searchChannel);
        realtimeacceptVo.setSearchColumn(searchColumn);
        realtimeacceptVo.setSearchWord(searchWord);
        realtimeacceptVo.setSearchChannel(searchChannel);
        realtimeacceptVo.setSearchSendFg(searchSendFg);
        realtimeacceptVo.setSearchSeq(searchSeq);
        realtimeacceptVo.setSearchStartReqTm(searchStartReqTm.replace(":", ""));
        realtimeacceptVo.setSearchEndReqTm(searchEndReqTm.replace(":", ""));
        

        int cnt =  ecareCancelServcie.insertMultiCancel(realtimeacceptVo);
        
        return cnt;
    }
    /**
     *  [이케어>이케어 취소요청] 이케어목록 조회
    *
    * @param serverInfo
    * @return
    * @throws Exception
    */
   @RequestMapping(value="/ecare/selectEcareList.json", method={RequestMethod.GET, RequestMethod.POST})
   @ResponseBody public List<EcareVo> selectEcareList() {
       try {
           return ecareScenarioService.selectEcareList();
       } catch (Exception e) {
           log.error(null, e);
       }
       return null;
   }
}
