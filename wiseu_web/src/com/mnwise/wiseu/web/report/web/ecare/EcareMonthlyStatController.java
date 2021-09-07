package com.mnwise.wiseu.web.report.web.ecare;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareSummaryService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;

/**
 * 이케어 월별 발송 통계 Controller
 */
@Controller
public class EcareMonthlyStatController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareMonthlyStatController.class);

    @Autowired private EcareSummaryService ecareSummaryService;

    /**
     * - [리포트>이케어] 이케어 월별 발송 통계 <br/>
     * - URL : /report/ecare/monthlyRpt.do <br/>
     * - JSP : /report/ecare/monthlyRpt.jsp <br/>
     * - 월별발송 통계
     *
     * @throws Exception
     */
    @RequestMapping(value = "/report/ecare/monthlyRpt.do", method = {RequestMethod.GET, RequestMethod.POST})  // /report/ecare/monthly_stat.do
    protected ModelAndView reportMonthly(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String format = ServletRequestUtils.getStringParameter(request, "format");

            String thisYear = DateFormatUtils.format(new Date(), "yyyy");
            String searchChannel = ServletRequestUtils.getStringParameter(request, "searchChannel", "");
            /*if(StringUtil.isBlank(searchChannel)) {
                String[] split = super.ecareChannelUseList.split(",");
                searchChannel = split[0];
            }*/
            String searchYear = ServletRequestUtils.getStringParameter(request, "searchYear", thisYear);
            String searchMonth = ServletRequestUtils.getStringParameter(request, "searchMonth", "");
            String searchServiceType = ServletRequestUtils.getStringParameter(request, "ecareType", "");
            int ecareNo = ServletRequestUtils.getIntParameter(request, "ecareNo", 0);

            Map<String, Object> map = new HashMap<>();
            map.put("searchChannel", searchChannel);
            map.put("ecareNo", ecareNo);

            // 서비스 타입이 실시간 또는 청구서일경우
            if(searchServiceType.startsWith("R") || searchServiceType.startsWith("B")) {
                map.put("searchServiceType", searchServiceType);
                map.put("searchSubType", "");
            } else if(searchServiceType.startsWith("S")) { // 서비스 타입이 스케쥴 종류인경우
                String subType = null;
                // 준실시간(SN,SR)인경우
                if(searchServiceType.length() == 2) {
                    subType = searchServiceType.substring(1, 2);
                } else { // 스케쥴인 경우
                    subType = "S";
                }

                map.put("searchServiceType", "S");
                map.put("searchSubType", subType);
            }

            UserVo userVo = getLoginUserVo();
            map.put("userVo", userVo);

            String lang = userVo.getLanguage();

            List<RptSendResultVo> ecareStat = null;
            if(StringUtil.isBlank(searchMonth)) {
                if(Channel.MAIL.equals(searchChannel) || Channel.PUSH.equals(searchChannel)) {
                    map.put("searchDate", searchYear + searchMonth);
                    ecareStat = ecareSummaryService.selectEcareMonthlyStat(map);
                } else if(Channel.FAX.equals(searchChannel)) {
                    map.put("searchStartDate", searchYear + searchMonth);
                    ecareStat = ecareSummaryService.selectEcareFAXMonthlyStat(map);
                } else if(Channel.SMS.equals(searchChannel) || Channel.LMSMMS.equals(searchChannel)) {
                    map.put("searchDate", searchYear + searchMonth);
                    map.put("searchChannel", searchChannel);
                    ecareStat = ecareSummaryService.selectEcareSMSMonthlyStat(map);
                } else if(ChannelUtil.isKakao(searchChannel)) {
                    map.put("searchDate", searchYear + searchMonth);
                    map.put("searchChannel", searchChannel);
                    ecareStat = ecareSummaryService.selectEcareKakaoMonthlyStat(map);
                }
            } else {
                if(Channel.MAIL.equals(searchChannel) || Channel.PUSH.equals(searchChannel)) {
                    map.put("searchDate", searchYear + searchMonth);
                    ecareStat = ecareSummaryService.selectEcareDailyStat(map);
                } else if(Channel.FAX.equals(searchChannel)) {
                    map.put("searchStartDate", searchYear + searchMonth);
                    ecareStat = ecareSummaryService.selectEcareFAXDailyStat(map);
                } else if(Channel.SMS.equals(searchChannel) || Channel.LMSMMS.equals(searchChannel)) {
                    map.put("searchDate", searchYear + searchMonth);
                    map.put("searchChannel", searchChannel);
                    ecareStat = ecareSummaryService.selectEcareSMSDailyStat(map);
                }  else if(ChannelUtil.isKakao(searchChannel)) {
                    map.put("searchDate", searchYear + searchMonth);
                    map.put("searchChannel", searchChannel);
                    ecareStat = ecareSummaryService.selectEcareKakaoDailyStat(map);
                }
            }

            // Total Count
            double totalSendCnt = 0;
            double totalSuccessCnt = 0;
            double totalFailCnt = 0;
            double totalOpenCnt = 0;
            double totalDurationCnt = 0;
            double totalLinkCnt = 0;
            double totalReturnMailCnt = 0;

            // 알림톡 추가
            double totalAlimtalkSmsSuccessCnt = 0;
            double totalAlimtalkSmsFailCnt = 0;
            double totalAlimtalkLmsSuccessCnt = 0;
            double totalAlimtalkLmsFailCnt = 0;

            //친구톡 추가
            double totalFriendtalkSmsSuccessCnt = 0;
            double totalFriendtalkSmsFailCnt = 0;
            double totalFriendtalkLmsSuccessCnt = 0;
            double totalFriendtalkLmsFailCnt = 0;

            if(ecareStat != null) {
                RptSendResultVo[] sVo = new RptSendResultVo[ecareStat.size()];
                sVo = ecareStat.toArray(sVo);
                for(int ix = 0; ix < sVo.length; ix++) {
                    totalSendCnt += sVo[ix].getSendCnt();
                    totalSuccessCnt += sVo[ix].getSuccessCnt();
                    totalFailCnt += sVo[ix].getFailCnt();
                    totalOpenCnt += sVo[ix].getOpenCnt();
                    totalDurationCnt += sVo[ix].getDurationCnt();
                    totalLinkCnt += sVo[ix].getLinkCnt();
                    totalReturnMailCnt += sVo[ix].getReturnMailCnt();

                    // 알림톡 추가
                    if("A".equals(searchChannel)) {
                        totalAlimtalkSmsSuccessCnt += sVo[ix].getSmsSuccessCnt();
                        totalAlimtalkSmsFailCnt += sVo[ix].getSmsFailCnt();
                        totalAlimtalkLmsSuccessCnt += sVo[ix].getLmsSuccessCnt();
                        totalAlimtalkLmsFailCnt += sVo[ix].getLmsFailCnt();
                    } else if("C".equals(searchChannel)) {
                        totalFriendtalkSmsSuccessCnt += sVo[ix].getSmsSuccessCnt();
                        totalFriendtalkSmsFailCnt += sVo[ix].getSmsFailCnt();
                        totalFriendtalkLmsSuccessCnt += sVo[ix].getLmsSuccessCnt();
                        totalFriendtalkLmsFailCnt += sVo[ix].getLmsFailCnt();
                    }
                }
            }

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("totalSendCnt", totalSendCnt);
            returnData.put("totalSuccessCnt", totalSuccessCnt);
            returnData.put("totalFailCnt", totalFailCnt);
            returnData.put("totalOpenCnt", totalOpenCnt);
            returnData.put("totalDurationCnt", totalDurationCnt);
            returnData.put("totalLinkCnt", totalLinkCnt);
            returnData.put("totalReturnMailCnt", totalReturnMailCnt);
            returnData.put("webExecMode", super.webExecMode);
            // 알림톡 추가
            if(Channel.ALIMTALK.equals(searchChannel)) {
                returnData.put("totalAlimtalkSmsSuccessCnt", totalAlimtalkSmsSuccessCnt);
                returnData.put("totalAlimtalkSmsFailCnt", totalAlimtalkSmsFailCnt);
                returnData.put("totalAlimtalkLmsSuccessCnt", totalAlimtalkLmsSuccessCnt);
                returnData.put("totalAlimtalkLmsFailCnt", totalAlimtalkLmsFailCnt);
            } else if(Channel.FRIENDTALK.equals(searchChannel)) {
                returnData.put("totalFriendtalkSmsSuccessCnt", totalFriendtalkSmsSuccessCnt);
                returnData.put("totalFriendtalkSmsFailCnt", totalFriendtalkSmsFailCnt);
                returnData.put("totalFriendtalkLmsSuccessCnt", totalFriendtalkLmsSuccessCnt);
                returnData.put("totalFriendtalkLmsFailCnt", totalFriendtalkLmsFailCnt);
            }

            // 최소/최대년도를 DB쿼리없이 최근 10년치 선택가능하도록 처리
            String minDt = (Integer.parseInt(thisYear) - 9) + "";
            returnData.put("minDt", minDt);
            returnData.put("maxDt", thisYear);

            returnData.put("searchYear", searchYear);
            returnData.put("searchMonth", searchMonth);
            returnData.put("searchChannel", searchChannel);
            returnData.put("searchServiceType", searchServiceType);
            returnData.put("ecareType", searchServiceType);
            returnData.put("ecareNo", ecareNo);

            String[] useChannelArray = super.ecareChannelUseList.split(",");
            returnData.put("channelUseSize", useChannelArray.length);
            returnData.put("channelUseList", useChannelArray);
            returnData.put("ecareStat", ecareStat);

            if(StringUtil.isNotBlank(format) && format.equals("excel")) {
                ReportExcelDownload reportExcel = new ReportExcelDownload();
                if(Channel.ALIMTALK.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_alimtalk_" + lang + ".xls");
                } else if(Channel.FRIENDTALK.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_friendtalk_" + lang + ".xls");
                } else if(Channel.SMS.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_sms_" + lang + ".xls");
                } else if(Channel.LMSMMS.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_mms_" + lang + ".xls");
                } else if(Channel.FAX.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_fax_" + lang + ".xls");
                } else if(Channel.PUSH.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_push_" + lang + ".xls");
                } else if(Channel.BRANDTALK.equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_brandtalk_" + lang + ".xls");
                }
                else {
                    reportExcel.makeExcel(response, returnData, "ecare", "monthly_stat_" + lang + ".xls");
                }
                return null;
            } else {
                return new ModelAndView("report/ecare/monthlyRpt", returnData);  // report/ecare/monthly_stat_view
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value = "/report/ecare/selectEcareType.do", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<EcareVo> selectEcareType(String ecareType, String startDt) {
        try {
            return ecareSummaryService.selectEcareType(ecareType, startDt);
        } catch(Exception e) {
            log.error(null, e);
        }

        return null;
    }
}
