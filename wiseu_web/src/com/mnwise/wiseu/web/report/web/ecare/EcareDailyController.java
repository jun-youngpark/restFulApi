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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.FormatUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareCommonService;
import com.mnwise.wiseu.web.report.service.ecare.EcareDailyCommonService;
import com.mnwise.wiseu.web.report.service.ecare.EcareDailyService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;

/**
 * 이케어 리포트 Controller
 */
@Controller
public class EcareDailyController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareDailyController.class);

    @Autowired private EcareCommonService ecareCommonService;
    @Autowired private EcareDailyCommonService ecareDailyCommonService;
    @Autowired private EcareDailyService ecareDailyService;

    /**
     * - [리포트>이케어>이케어 리스트] 이케어 리포트 - 일정별 발송현황<br/>
     * - [리포트>이케어>이케어 리스트] 이케어 리포트 - 엑셀다운로드<br/>
     *
     * @throws Exception
     */
    @RequestMapping(value="/report/ecare/dailyRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/ecare/ecare_daily.do
    protected ModelAndView handle(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int ecareNo,
                                  String serviceType, @RequestParam(defaultValue="") String subType, String channelType,
                                  String searchYear, String searchMonth, String searchStartDt, String searchEndDt,
                                  String btnClick, String format, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            EcareScenarioInfoVo scenarioInfoVo = new EcareScenarioInfoVo();
            scenarioInfoVo.setUserVo(userVo);
            scenarioInfoVo.setScenarioNo(scenarioNo);
            scenarioInfoVo.setServiceType(serviceType);
            scenarioInfoVo.setSubType(subType);
            //scenarioInfoVo.setChannelType(channelType);
            scenarioInfoVo.setEcareNo(ecareNo);
            //scenarioInfoVo.getEcareInfoVo().setEcareNo(ecareNo);

            // 시나리오 채널정보
            List<EcareVo> scenarioChannelList = ecareDailyCommonService.getScenarioChannelList(scenarioInfoVo);

            // 이케어 기본정보
            scenarioInfoVo = ecareDailyCommonService.selectScenarioInfo(scenarioInfoVo);
            channelType = ecareCommonService.getChannel(ecareNo);
            scenarioInfoVo.getEcareInfoVo().setChannelType(channelType);

            if(StringUtil.isBlank(searchStartDt)) {
                searchStartDt = DateUtil.getNowDateTime("yyyyMM") + "01";
            }

            if(StringUtil.isBlank(searchEndDt)) {
                searchEndDt = DateUtil.getNowDate();
            }

            if(StringUtil.isBlank(serviceType)) {
                if(!StringUtil.isBlank((String) WebUtils.getSessionAttribute(request, "serviceType"))) {
                    serviceType = (String) WebUtils.getSessionAttribute(request, "serviceType");
                } else {
                    serviceType = "R";
                }
            }

            scenarioInfoVo.setServiceType(serviceType);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("serviceType", serviceType);
            returnData.put("subType", subType);

            List<RptSendResultVo> ecareDailyReportList = null;

            //EcareInfoVo ecareInfoVo = scenarioChannelList.get(0);
            // channelType = ecareInfoVo.getChannelType();

            // 서비스 타입과 채널별 쿼리 분기
            if(scenarioChannelList.size() > 0) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("ecareNo", ServletRequestUtils.getIntParameter(request, "ecareNo", 0));
                paramMap.put("searchStartDt", searchStartDt);
                paramMap.put("searchEndDt", searchEndDt);
                paramMap.put("isDaily", ("N".equals(subType) || "R".equals(serviceType)) ? "Y" : "N");
                Map<String, Object> resultMap = ecareDailyService.selectEcareScheduleDailyReportList(channelType, serviceType, subType, paramMap);

                ecareDailyReportList  = (List<RptSendResultVo>) resultMap.get("ecareDailyReportList");
                returnData.put("pushReceiveList", resultMap.get("pushReceiveList"));
                returnData.put("pushSendlogList", resultMap.get("pushSendlogList"));
                resultMap.clear();
            }

            // 재발송 관련 추가
            returnData.put("lstResendUse", super.lstResendUse);
            returnData.put("searchYear", searchYear);
            returnData.put("searchMonth", searchMonth);
            returnData.put("searchStartDt", searchStartDt);
            returnData.put("searchEndDt", searchEndDt);
            returnData.put("scenarioChannelList", scenarioChannelList);
            returnData.put("scenarioInfoVo", scenarioInfoVo);
            returnData.put("ecareDailyReportList", ecareDailyReportList);
            returnData.put("btnClick", btnClick);
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("channelType", channelType);

            // 요약화면 엑셀 다운로드 처리 20130513()
            if("excel".equals(format)) {
                doExcel(channelType, userVo.getLanguage(), ecareDailyReportList, returnData, response);
                return null;
            }

            if(Channel.ALIMTALK.equals(channelType) || Channel.FRIENDTALK.equals(channelType))
                return new ModelAndView("report/ecare/dailyRpt_kko", returnData);  // report/ecare/ecare_daily_kakaotalk
            else if(Channel.PUSH.equals(channelType))
                return new ModelAndView("report/ecare/dailyRpt_push", returnData);  // report/ecare/ecare_daily_push
            else
                return new ModelAndView("report/ecare/dailyRpt_etc", returnData);  // report/ecare/ecare_daily
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    private void doExcel(String channelType, String language, List<RptSendResultVo> ecareDailyReportList, Map<String, Object> returnData, HttpServletResponse response) throws Exception {
        try {
            // Total Count
            double totalSendCnt = 0;
            double totalSuccessCnt = 0;
            double totalBounceCnt = 0;
            double totalOpenCnt = 0;
            double totalDurationCnt = 0;
            double totalLinkCnt = 0;
            // 알림톡 추가
            double totalSmsSuccessCnt = 0;
            double totalLmsSuccessCnt = 0;
            double totalSmsFailCnt = 0;
            double totalLmsFailCnt = 0;

            RptSendResultVo[] srv = new RptSendResultVo[ecareDailyReportList.size()];
            srv = ecareDailyReportList.toArray(srv);
            for(int ix = 0; ix < srv.length; ix++) {
                totalSendCnt += srv[ix].getSendCnt();
                totalSuccessCnt += srv[ix].getSuccessCnt();
                totalBounceCnt += srv[ix].getBounceCnt();
                if(Channel.MAIL.equals(channelType) || Channel.PUSH.equals(channelType)) {
                    totalOpenCnt += srv[ix].getOpenCnt();
                    if(Channel.MAIL.equals(channelType)) {
                        totalDurationCnt += srv[ix].getDurationCnt();
                        totalLinkCnt += srv[ix].getLinkCnt();
                    }
                } else if(Channel.ALIMTALK.equals(channelType) || Channel.FRIENDTALK.equals(channelType)) {
                    totalSmsSuccessCnt += srv[ix].getSmsSuccessCnt();
                    totalLmsSuccessCnt += srv[ix].getLmsSuccessCnt();
                    totalSmsFailCnt += srv[ix].getSmsFailCnt();
                    totalLmsFailCnt += srv[ix].getLmsFailCnt();
                }
            }

            returnData.put("totalSendCnt", totalSendCnt);
            returnData.put("totalSuccessCnt", totalSuccessCnt);
            returnData.put("totalBounceCnt", totalBounceCnt);
            returnData.put("successCntToPercent", FormatUtil.toNumPercent((int) totalSuccessCnt, (int) totalSendCnt, 1));
            returnData.put("totalSuccessCntToPercent", FormatUtil.toNumPercent((int) totalSuccessCnt + (int) totalSmsSuccessCnt + (int) totalLmsSuccessCnt, (int) totalSendCnt, 1));

            if(Channel.MAIL.equals(channelType) || Channel.PUSH.equals(channelType)) {
                returnData.put("totalOpenCnt", totalOpenCnt);
                if(Channel.MAIL.equals(channelType)) {
                    returnData.put("totalDurationCnt", totalDurationCnt);
                    returnData.put("totalLinkCnt", totalLinkCnt);
                }
            } else if(Channel.ALIMTALK.equals(channelType) || Channel.FRIENDTALK.equals(channelType)) { //2018.03 친구톡 추가
                returnData.put("totalSmsSuccessCnt", totalSmsSuccessCnt);
                returnData.put("totalLmsSuccessCnt", totalLmsSuccessCnt);
                returnData.put("totalSmsFailCnt", totalSmsFailCnt);
                returnData.put("totalLmsFailCnt", totalLmsFailCnt);
            }

            String fileName = "";
            if(!ecareDailyReportList.isEmpty()) {
                if(Channel.MAIL.equals(channelType)) {
                    fileName = "ecaredaily_Email_" + language;
                } else if(Channel.SMS.equals(channelType)) {
                    fileName = "ecaredaily_SMS_" + language;
                } else if(Channel.FAX.equals(channelType)) {
                    fileName = "ecaredaily_FAX_" + language;
                } else if(Channel.LMSMMS.equals(channelType)) {
                    fileName = "ecaredaily_MMS_" + language;
                } else if(Channel.ALIMTALK.equals(channelType)) {
                    fileName = "ecaredaily_Alimtalk_" + language;
                } else if(Channel.FRIENDTALK.equals(channelType)) {
                    fileName = "ecaredaily_Friendmtalk_" + language;
                } else if(Channel.PUSH.equals(channelType)) {
                    fileName = "ecaredaily_PUSH_" + language;
                }
            } else {
                fileName = "ecaredaily_Fail_" + language;
            }

            ReportExcelDownload reportExcel = new ReportExcelDownload();
            reportExcel.makeExcel(response, returnData, "ecare", fileName + ".xls");
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트] 일정별 발송현황 - 업데이트 아이콘 클릭 (스케줄)
     *
     * @param ecareNo
     * @param sendStartDt
     * @param resultSeq
     * @return
     */
    @RequestMapping(value="/report/ecare/updateEcareDailyScheduleReceiptList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateEcareDailyScheduleReceiptList(int ecareNo, String sendStartDt, String resultSeq) {
        try {
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            int row = ecareDailyService.updateEcareDailyScheduleReceiptList(ecareNo, sendStartDt, resultSeq);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [리포트>이케어>이케어 리스트>이케어 리포트] 일정별 발송현황 - 업데이트 아이콘 클릭 (실시간, 준실시간, 스케줄분)
     *
     * @param ecareNo
     * @param sendStartDt
     * @return
     */
    @RequestMapping(value="/report/ecare/updateEcareDailyRealtimeReceiptList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateEcareDailyRealtimeReceiptList(int ecareNo, String sendStartDt) {
        try {
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            int row = ecareDailyService.updateEcareDailyRealtimeReceiptList(ecareNo, sendStartDt);
            resultDto.setRowCount(row);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
