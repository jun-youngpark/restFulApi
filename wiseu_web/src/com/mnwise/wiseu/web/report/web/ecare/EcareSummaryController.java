package com.mnwise.wiseu.web.report.web.ecare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareCommonService;
import com.mnwise.wiseu.web.report.service.ecare.EcareSummaryService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;

/**
 * 이케어 리포트 - 요약분석 Controller
 */
@Controller
public class EcareSummaryController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareSummaryController.class);

    @Autowired private EcareCommonService ecareCommonService;
    @Autowired private EcareSummaryService ecareSummaryService;
    @Autowired private PushService pushService;

    @Value("${report.result.excel.use}")
    private String resultExcelUse;

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 요약분석<br/>
     * [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 요약분석 - 엑셀다운로드<br/>
     *
     * @throws Exception
     */
    @RequestMapping(value="/report/ecare/summaryRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/ecare/summary.do
    protected ModelAndView handle(EcareScenarioInfoVo scenarioInfoVo, @RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int ecareNo,
                                  @RequestParam(defaultValue="0") long resultSeq, String serviceType, String subType, String reportDt, String format, String nowPage,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            // 시나리오만 가져올 경우 처리 할 것
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
            if(scenarioInfoVo == null) {
                return new ModelAndView(new RedirectView("/report/ecare/ecareRptList.do"));  // /report/ecare/ecare_list.do
            }

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

            scenarioInfoVo.getEcareInfoVo().setReportDt(reportDt);
            scenarioInfoVo.setLanguage(userVo.getLanguage());

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
            }else if(Channel.BRANDTALK.equals(channelType)) {
                sendResultVo = ecareCommonService.selectKakaoSendResult(scenarioInfoVo);
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

            Cookie cookie = new Cookie("ecListNowPage", nowPage);
            cookie.setSecure(true); // [강욱] 2015-02-17, 보안취약점 조치 - Cookie Security: // Cookie not Sent Over SSL
            response.addCookie(cookie);

            Map<String, Object> returnData = new HashMap<>();


            // 반응결과 (다국어 처리를 위해 언어를 넘겨준다)

            List<EcareScenarioInfoVo> ecareReactionResultList = null;

            if("M".equals(channelType)) {
                ecareReactionResultList = ecareSummaryService.selectReactionResultList(scenarioInfoVo);
                // 링크클릭 반응결과
                Map<String, Object> linkTraceResultListMap = ecareSummaryService.selectLinkTraceResultList(scenarioInfoVo, sendResultVo.getSendCnt(), sendResultVo.getOpenCnt());

                List linkTraceResultList = null;
                Integer totalUniqueLinkClickCnt = new Integer(0);
                Integer totalAllLinkClickCnt = new Integer(0);
                if(linkTraceResultListMap.get("linkTraceResultList") != null) {
                    linkTraceResultList = (List) linkTraceResultListMap.get("linkTraceResultList");
                    totalUniqueLinkClickCnt = (Integer) linkTraceResultListMap.get("totalUniqueLinkClickCnt");
                    totalAllLinkClickCnt = (Integer) linkTraceResultListMap.get("totalAllLinkClickCnt");
                }

                returnData.put("linkTraceResultList", linkTraceResultList);
                returnData.put("totalUniqueLinkClickCnt", totalUniqueLinkClickCnt);
                returnData.put("totalAllLinkClickCnt", totalAllLinkClickCnt);
            }

            returnData.put("scenarioChannelList", scenarioChannelList);
            returnData.put("scenarioInfoVo", scenarioInfoVo);
            returnData.put("ecareSendResultVo", sendResultVo);
            returnData.put("ecareReactionResultList", ecareReactionResultList);
            returnData.put("requestUri", request.getRequestURI());

            // 상세리포트 엑셀 다운로드 사용여부
            returnData.put("resultExcelUse", resultExcelUse);
            returnData.put("webExecMode", super.webExecMode);
            if(StringUtil.isNotBlank(format) && format.equals("excel")) {
                ReportExcelDownload reportExcel = new ReportExcelDownload();
                reportExcel.makeExcel(response, returnData, "ecare", "summary_" + userVo.getLanguage() + ".xls");
                return null;
            } else {
                if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType))
                    return new ModelAndView("report/ecare/summaryRpt_sms", returnData);  // report/ecare/message_summary_view
                else if(Channel.FAX.equals(channelType))
                    return new ModelAndView("report/ecare/summaryRpt_fax", returnData);  // report/ecare/fax_summary_view
                else if(ChannelUtil.isKakao(channelType))
                    return new ModelAndView("report/ecare/summaryRpt_kko", returnData);  // report/ecare/kakaotalk_summary_view

                else
                    return new ModelAndView("report/ecare/summaryRpt_etc", returnData);  // report/ecare/summary_view
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
