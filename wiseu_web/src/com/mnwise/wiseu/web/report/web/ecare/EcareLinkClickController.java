package com.mnwise.wiseu.web.report.web.ecare;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.LinkTraceResultVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareCommonService;
import com.mnwise.wiseu.web.report.service.ecare.EcareLinkClickService;

@Controller
public class EcareLinkClickController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareLinkClickController.class);

    @Autowired protected EcareCommonService ecareCommonService;
    @Autowired private EcareLinkClickService ecareLinkClickService;
    @Autowired protected PushService pushService;

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 링크클릭 분석
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/report/ecare/linkClickRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/ecare/linkclick.do
    protected ModelAndView handle(EcareScenarioInfoVo scenarioInfoVo, @RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int ecareNo,
                                  @RequestParam(defaultValue="0") long resultSeq, String serviceType, String subType, String reportDt, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            scenarioInfoVo.setScenarioNo(scenarioNo);
            scenarioInfoVo.getEcareInfoVo().setEcareNo(ecareNo);
            scenarioInfoVo.getEcareInfoVo().setResultSeq(resultSeq);
            scenarioInfoVo.getEcareInfoVo().setServiceType(serviceType);
            scenarioInfoVo.getEcareInfoVo().setSubType(subType);
            scenarioInfoVo.setUserVo(userVo);

            // 시나리오 채널정보
            List<EcareVo> scenarioChannelList = ecareCommonService.getScenarioChannelList(scenarioInfoVo);
            String channelType = ecareCommonService.getChannel(ecareNo);

            // 이케어 기본정보
            scenarioInfoVo = ecareCommonService.selectScenarioInfo(scenarioInfoVo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(scenarioInfoVo.getUserVo().getGrpCd(), scenarioInfoVo.getUserVo().getUserId())) {
                throw new Exception("Invalid access");
            }

            // PARAMETER RESULT_SEQ값을 세팅
            scenarioInfoVo.getEcareInfoVo().setResultSeq(resultSeq);
            scenarioInfoVo.getEcareInfoVo().setReportDt(reportDt);
            scenarioInfoVo.setLanguage(userVo.getLanguage());

            //PARAMETER SERVICE 유형 셋팅
            scenarioInfoVo.getEcareInfoVo().setServiceType(serviceType);
            scenarioInfoVo.getEcareInfoVo().setSubType(subType);

            RptSendResultVo sendResultVo = null;

            // 채널 별로 발송결과 쿼리 분기
            if(Channel.MAIL.equals(channelType) || Channel.PUSH.equals(channelType)) {
                // 스케쥴 발송결과
                if("S".equals(serviceType) && "S".equals(subType)) {// 스케쥴
                    sendResultVo = ecareCommonService.selectScheduleSendResult(scenarioInfoVo);
                } else { // 실시간 발송결과
                    sendResultVo = ecareCommonService.selectRealtimeSendResult(scenarioInfoVo);
                }

                int openCnt = ecareCommonService.selectOpenCnt(scenarioInfoVo);  // 오픈 수
                sendResultVo.setOpenCnt(openCnt);
            } else if(Channel.FAX.equals(channelType)) {
                // 스케쥴 발송결과
                if("S".equals(serviceType) && "S".equals(subType)) {// 스케쥴
                    sendResultVo = ecareCommonService.selectEcareFaxScheduleSendResult(scenarioInfoVo);
                } else { // 실시간 발송결과
                    sendResultVo = ecareCommonService.selectFaxSendResult(scenarioInfoVo);
                }

                sendResultVo.setOpenCnt(0);  // 오픈 수
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
                // 스케쥴 발송결과
                if("S".equals(serviceType) && "S".equals(subType)) {// 스케쥴
                    sendResultVo = ecareCommonService.selectEcareSMSScheduleSendResult(scenarioInfoVo);
                } else { // 실시간 발송결과
                    sendResultVo = ecareCommonService.selectSMSSendResult(scenarioInfoVo);
                }

                sendResultVo.setOpenCnt(0);  // 오픈 수
            } else if(Channel.ALIMTALK.equals(channelType)) {
                sendResultVo = ecareCommonService.selectAlimtalkSendResult(scenarioInfoVo);
                sendResultVo.setOpenCnt(0);
            } else if(Channel.FRIENDTALK.equals(channelType)) {
                sendResultVo = ecareCommonService.selectFriendtalkSendResult(scenarioInfoVo);
                sendResultVo.setOpenCnt(0);
            }

            if(Channel.PUSH.equals(channelType)) {
                sendResultVo.setEcareNo(ecareNo);
                sendResultVo.setResultSeq(Long.toString(resultSeq));

                Map<String, Object> countMaps = pushService.getEcareResultCount(ecareNo, Long.toString(resultSeq), reportDt, reportDt);
                sendResultVo.setOpenCnt((Integer)countMaps.get("openCnt"));
                sendResultVo.setSuccessCnt((Integer)countMaps.get("succCnt"));
                sendResultVo.setSendCnt((Integer)countMaps.get("sendCnt"));
            }

            // 링크클릭 반응결과
            Map<String, Object> linkTraceResultListMap = ecareLinkClickService.selectLinkTraceResultList(scenarioInfoVo, sendResultVo.getSendCnt(), sendResultVo.getOpenCnt());

            List<LinkTraceResultVo> linkTraceResultList = null;
            int totalUniqueLinkClickCnt = 0;
            int totalAllLinkClickCnt = 0;
            if(linkTraceResultListMap.get("linkTraceResultList") != null) {
                linkTraceResultList = (List<LinkTraceResultVo>) linkTraceResultListMap.get("linkTraceResultList");
                totalUniqueLinkClickCnt = (Integer) linkTraceResultListMap.get("totalUniqueLinkClickCnt");
                totalAllLinkClickCnt = (Integer) linkTraceResultListMap.get("totalAllLinkClickCnt");
            }

            ModelAndView mav = new ModelAndView("report/ecare/linkClickRpt");  // report/ecare/linkclick_view
            mav.addObject("scenarioChannelList", scenarioChannelList);
            mav.addObject("scenarioInfoVo", scenarioInfoVo);
            mav.addObject("ecareSendResultVo", sendResultVo);
            mav.addObject("linkTraceResultList", linkTraceResultList);
            mav.addObject("totalUniqueLinkClickCnt", totalUniqueLinkClickCnt);
            mav.addObject("totalAllLinkClickCnt", totalAllLinkClickCnt);
            mav.addObject("requestUri", request.getRequestURI());
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
