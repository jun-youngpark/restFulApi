package com.mnwise.wiseu.web.report.web.ecare;

import java.util.HashMap;
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

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareInfoVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareCommonService;
import com.mnwise.wiseu.web.report.service.ecare.EcareErrSummaryService;

/**
 * 이케어 리포트 - 오류분석 Controller
 */
@Controller
public class EcareErrSummaryController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareErrSummaryController.class);

    @Autowired private EcareCommonService ecareCommonService;
    @Autowired private EcareErrSummaryService ecareErrSummaryService;
    @Autowired private PushService pushService;

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/report/ecare/errorRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/ecare/err_summary.do
    protected ModelAndView summary(EcareScenarioInfoVo scenarioInfoVo, @RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int ecareNo,
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

            Map<String, Object> returnData = new HashMap<>();

            List<DomainLogVo> domainResultMsgListBySoft = null;
            List<DomainLogVo> domainResultMsgListByHard = null;
            int domainResultMsgListBySoftSize = 0;
            int totalSoftBounceCnt = 0;
            int domainResultMsgListByHardSize = 0;
            int totalHardBounceCnt = 0;
            List<DomainLogVo> domainLogList = null;

            // 채널 타입별 오류분석 내용 분기
            if(Channel.MAIL.equals(channelType)) {
                Map<String, Object> domainResultMsgListMap = ecareErrSummaryService.selectDomainResultMsgList(scenarioInfoVo);  // 반송메일 원인별 현황

                if(domainResultMsgListMap.get("domainResultMsgListBySoft") != null) {
                    domainResultMsgListBySoft = (List<DomainLogVo>) domainResultMsgListMap.get("domainResultMsgListBySoft");
                    domainResultMsgListBySoftSize = domainResultMsgListBySoft.size();
                    totalSoftBounceCnt = (Integer) domainResultMsgListMap.get("totalSoftBounceCnt");
                }
                if(domainResultMsgListMap.get("domainResultMsgListByHard") != null) {
                    domainResultMsgListByHard = (List<DomainLogVo>) domainResultMsgListMap.get("domainResultMsgListByHard");
                    domainResultMsgListByHardSize = domainResultMsgListByHard.size();
                    totalHardBounceCnt = (Integer) domainResultMsgListMap.get("totalHardBounceCnt");
                }

                Map<String, Object> domainLogListMap = ecareErrSummaryService.selectDomainLogList(scenarioInfoVo);

                if(domainLogListMap.get("domainLogList") != null) {
                    domainLogList = (List<DomainLogVo>) domainLogListMap.get("domainLogList");
                }
            } else if(Channel.FAX.equals(channelType)) {
                // Fax 오류내용 목록 가져오기
                domainResultMsgListBySoft = ecareErrSummaryService.selectFaxLogList(scenarioInfoVo);
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
                // SMS 오류내용 목록 가져오기
                domainResultMsgListBySoft = ecareErrSummaryService.selectSMSLogList(scenarioInfoVo);
            } else if(Channel.ALIMTALK.equals(channelType)) {
                // 알림톡 오류내용 목록 가져오기
                scenarioInfoVo.setChannelType(Channel.SMS);
                scenarioInfoVo.setAlimtalkSearchCodes(PropertyUtil.getProperty("alimtalk.other.code.success").split(","));
                returnData.put("smsResultMsgList", ecareErrSummaryService.selectAlimtalkLogList(scenarioInfoVo));
                scenarioInfoVo.setChannelType("L");
                returnData.put("lmsResultMsgList", ecareErrSummaryService.selectAlimtalkLogList(scenarioInfoVo));
                scenarioInfoVo.setChannelType(Channel.ALIMTALK);
                scenarioInfoVo.setAlimtalkSearchCodes(PropertyUtil.getProperty("alimtalk.code.success").split(","));
                domainResultMsgListBySoft = ecareErrSummaryService.selectAlimtalkLogList(scenarioInfoVo);
            } else if(Channel.FRIENDTALK.equals(channelType)) {
                // 친구톡 오류내용 목록 가져오기
                scenarioInfoVo.setChannelType(Channel.SMS);
                scenarioInfoVo.setFriendtalkSearchCodes(PropertyUtil.getProperty("friendtalk.other.code.success").split(","));
                returnData.put("smsResultMsgList", ecareErrSummaryService.selectFriendtalkLogList(scenarioInfoVo));
                scenarioInfoVo.setChannelType("L");
                returnData.put("lmsResultMsgList", ecareErrSummaryService.selectFriendtalkLogList(scenarioInfoVo));
                scenarioInfoVo.setChannelType(Channel.FRIENDTALK);
                scenarioInfoVo.setFriendtalkSearchCodes(PropertyUtil.getProperty("friendtalk.code.success").split(","));
                domainResultMsgListBySoft = ecareErrSummaryService.selectFriendtalkLogList(scenarioInfoVo);
            } else if (Channel.PUSH.equals(channelType)) {
                domainResultMsgListBySoft = pushService.selectEcarePushErrorReportList(sendResultVo.getEcareNo(), sendResultVo.getResultSeq(), scenarioInfoVo.getEcareInfoVo().getReportDt(), userVo.getLanguage());
            }else if(Channel.BRANDTALK.equals(channelType)) {
                // 친구톡 오류내용 목록 가져오기
                scenarioInfoVo.setChannelType(Channel.BRANDTALK);
                scenarioInfoVo.setSearchCodes(PropertyUtil.getProperty("brandtalk.code.success").split(","));
                scenarioInfoVo.setSerarchCdCat("AB0001");
                domainResultMsgListBySoft = ecareErrSummaryService.selectKakaoLogList(scenarioInfoVo);
            }

            returnData.put("scenarioChannelList", scenarioChannelList);
            returnData.put("scenarioInfoVo", scenarioInfoVo); // 이케어 시나리오 정보
            returnData.put("ecareSendResultVo", sendResultVo);
            returnData.put("domainResultMsgListBySoft", domainResultMsgListBySoft);
            returnData.put("domainResultMsgListBySoftSize", domainResultMsgListBySoftSize);
            returnData.put("totalSoftBounceCnt", totalSoftBounceCnt);
            returnData.put("domainResultMsgListByHard", domainResultMsgListByHard);
            returnData.put("domainResultMsgListByHardSize", domainResultMsgListByHardSize);
            returnData.put("totalHardBounceCnt", totalHardBounceCnt);
            returnData.put("totalBounceCnt", totalSoftBounceCnt + totalHardBounceCnt);
            returnData.put("domainLogList", domainLogList);
            returnData.put("requestUri", request.getRequestURI());

            if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType))
                return new ModelAndView("report/ecare/errorRpt_sms", returnData);  // report/ecare/message_err_summary_view
            else if(Channel.FAX.equals(channelType))
                return new ModelAndView("report/ecare/errorRpt_fax", returnData);  // report/ecare/fax_err_summary_view
            else if(ChannelUtil.isKakao(channelType)) //2020.06 브랜드톡 추가
                return new ModelAndView("report/ecare/errorRpt_kko", returnData);  // report/ecare/kakaotalk_err_summary_view
            else if(Channel.PUSH.equals(channelType))
                return new ModelAndView("report/ecare/errorRpt_push", returnData);  // report/ecare/push_err_summary_view
            else
                return new ModelAndView("report/ecare/errorRpt_mail", returnData);  // report/ecare/err_summary_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석 - 에러코드 리포트(팝업)
     * - 해당 에러코드의 도메인별 리포트를 출력
     *
     * @throws Exception
     */
    @RequestMapping(value="/report/ecare/errorCodeRptPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/ecare/err_bycode_popup.do
    protected ModelAndView bycode(EcareScenarioInfoVo scenarioInfoVo, @RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int ecareNo,
                                  @RequestParam(defaultValue="0") long resultSeq, String serviceType, String subType, String reportDt,
                                  String searchErrCd, HttpServletRequest request) throws Exception {
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
            String channelType = scenarioChannelList.get(0).getChannelType();

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

            scenarioInfoVo.getEcareInfoVo().setSearchErrCd(searchErrCd);

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

            ModelAndView mav = new ModelAndView("report/ecare/errorCodeRptPopup");  // report/ecare/err_bycode_view
            mav.addObject("scenarioInfoVo", scenarioInfoVo);
            mav.addObject("ecareSendResultVo", sendResultVo);
            mav.addObject("errCdResultMsgList", ecareErrSummaryService.selectErrCdResultMsgList(scenarioInfoVo));
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석  - 도메인 리포트(팝업)
     * - 해당 도메인의 에러코드별 리포트를 출력
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/report/ecare/errorDomainRptPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/ecare/err_bydomain_popup.do
    protected ModelAndView bydomain(@RequestParam(defaultValue="0") int ecareNo, @RequestParam(defaultValue="0") long resultSeq,
                                    String serviceType, String subType, String reportDt, String searchDomainNm, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();

            EcareScenarioInfoVo scenarioInfoVo = new EcareScenarioInfoVo();
            scenarioInfoVo.setEcareInfoVo(new EcareInfoVo());
            scenarioInfoVo.getEcareInfoVo().setEcareNo(ecareNo);
            scenarioInfoVo.getEcareInfoVo().setResultSeq(resultSeq);
            scenarioInfoVo.getEcareInfoVo().setReportDt(reportDt);
            scenarioInfoVo.getEcareInfoVo().setServiceType(serviceType);
            scenarioInfoVo.getEcareInfoVo().setSubType(subType);
            scenarioInfoVo.getEcareInfoVo().setSearchDomainNm(searchDomainNm);
            scenarioInfoVo.setUserVo(userVo);
            scenarioInfoVo.setLanguage(userVo.getLanguage());

            // 반송메일 원인 별 현황
            Map<String, Object> domainResultMsgListMap = ecareErrSummaryService.selectDomainResultMsgList(scenarioInfoVo);
            List<DomainLogVo> domainResultMsgListBySoft = null;
            List<DomainLogVo> domainResultMsgListByHard = null;
            int domainResultMsgListBySoftSize = 0;
            int totalSoftBounceCnt = 0;
            int domainResultMsgListByHardSize = 0;
            int totalHardBounceCnt = 0;

            if(domainResultMsgListMap.get("domainResultMsgListBySoft") != null) {
                domainResultMsgListBySoft = (List<DomainLogVo>) domainResultMsgListMap.get("domainResultMsgListBySoft");
                domainResultMsgListBySoftSize = domainResultMsgListBySoft.size();
                totalSoftBounceCnt = (Integer) domainResultMsgListMap.get("totalSoftBounceCnt");
            }

            if(domainResultMsgListMap.get("domainResultMsgListByHard") != null) {
                domainResultMsgListByHard = (List<DomainLogVo>) domainResultMsgListMap.get("domainResultMsgListByHard");
                domainResultMsgListByHardSize = domainResultMsgListByHard.size();
                totalHardBounceCnt = (Integer) domainResultMsgListMap.get("totalHardBounceCnt");
            }

            ModelAndView mav = new ModelAndView("report/ecare/errorDomainRptPopup");  // report/ecare/err_bydomain_view
            mav.addObject("scenarioInfoVo", scenarioInfoVo);
            mav.addObject("ecareSendResultVo", new RptSendResultVo());
            mav.addObject("domainResultMsgListBySoft", domainResultMsgListBySoft);
            mav.addObject("domainResultMsgListBySoftSize", domainResultMsgListBySoftSize);
            mav.addObject("totalSoftBounceCnt", totalSoftBounceCnt);
            mav.addObject("domainResultMsgListByHard", domainResultMsgListByHard);
            mav.addObject("domainResultMsgListByHardSize", domainResultMsgListByHardSize);
            mav.addObject("totalHardBounceCnt", totalHardBounceCnt);
            mav.addObject("totalBounceCnt", totalSoftBounceCnt + totalHardBounceCnt);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
