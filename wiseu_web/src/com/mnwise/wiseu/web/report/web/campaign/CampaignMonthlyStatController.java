package com.mnwise.wiseu.web.report.web.campaign;

import java.util.GregorianCalendar;
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
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.SendResultVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignSummaryService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;

/**
 * 캠페인 월별 발송 통계 Controller
 */
@Controller
public class CampaignMonthlyStatController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CampaignMonthlyStatController.class);

    @Autowired private CampaignSummaryService campaignSummaryService;

    /**
     * - [리포트>캠페인] 캠페인 월별 발송 통계 <br/>
     * - URL : /report/campaign/monthlyRpt.do <br/>
     * - JSP : /report/campaign/monthlyRpt.jsp <br/>
     *
     * @throws Exception
     */
    @RequestMapping(value = "/report/campaign/monthlyRpt.do", method = {RequestMethod.GET, RequestMethod.POST})  // /report/campaign/monthly_stat.do
    protected ModelAndView reportMonthly(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            String format = ServletRequestUtils.getStringParameter(request, "format");

            final GregorianCalendar calendar = new GregorianCalendar();
            final int thisYear = calendar.get(GregorianCalendar.YEAR);
            final int pastYear = thisYear - 9;

            String searchChannel = ServletRequestUtils.getStringParameter(request, "searchChannel", "");
            String searchYear = ServletRequestUtils.getStringParameter(request, "searchYear", String.valueOf(thisYear));
            String searchMonth = ServletRequestUtils.getStringParameter(request, "searchMonth", "");
            String searchId = ServletRequestUtils.getStringParameter(request, "searchId");

            Map<String, Object> map = new HashMap<>();
            map.put("searchChannel", searchChannel);
            map.put("searchDate", searchYear + searchMonth);
            map.put("searchId", searchId);

            UserVo userVo = getLoginUserVo();
            map.put("userVo", userVo);

            // 캠페인에서 사용하는 채널 리스트 쿼리문에서 SMS , MMS 쿼리가 분리되어있어서 T,S 제거 함.
            //map.put("useChannelList" , (campaignSummaryService.userChannelList().replaceAll(",+[T|S]|[T|S],+" , "")).split(","));
            map.put("useChannelList" , new String[] {"M","F","P"});
            String lang = userVo.getLanguage();

            List<SendResultVo> sendResultList = null;
            if(StringUtil.isEmpty(searchMonth)) {
                // 채널구분
                if(Channel.MAIL.equals(searchChannel) || Channel.FAX.equals(searchChannel) || Channel.PUSH.equals(searchChannel)) {
                    sendResultList = campaignSummaryService.selectCampaignMonthlyStat(map);
                } else if(Channel.SMS.equals(searchChannel)) { // SMS
                    map.put("sendGbn1", "sms");
                    map.put("sendGbn2", "");
                    sendResultList = campaignSummaryService.selectCampaignSMSMonthlyStat(map);
                } else if(Channel.LMSMMS.equals(searchChannel)) { // MMS, LMS
                    map.put("sendGbn1", "mms");
                    map.put("sendGbn2", "lms");
                    sendResultList = campaignSummaryService.selectCampaignSMSMonthlyStat(map);
                } else if(ChannelUtil.isKakao(searchChannel)){
                    map.put("gubun", "month");
                    sendResultList = campaignSummaryService.selectCampaignKakaoMonthlyStat(map);
                } /* else { // 채널 전체 - 통계건수 결함이 있어 주석처리
                    sendResultList = campaignSummaryService.selectCampaignTOTMonthlyStat(map);
                }*/
            } else {
                // 채널 구분
                if(Channel.MAIL.equals(searchChannel) || Channel.FAX.equals(searchChannel) || Channel.PUSH.equals(searchChannel)) {
                    sendResultList = campaignSummaryService.selectCampaignDailyStat(map);
                } else if(Channel.SMS.equals(searchChannel)) { // SMS
                    map.put("sendGbn1", "sms");
                    map.put("sendGbn2", "");
                    sendResultList = campaignSummaryService.selectCampaignSMSDailyStat(map);
                } else if(Channel.LMSMMS.equals(searchChannel)) { // MMS, LMS
                    map.put("sendGbn1", "mms");
                    map.put("sendGbn2", "lms");
                    sendResultList = campaignSummaryService.selectCampaignSMSDailyStat(map);
                } else if(ChannelUtil.isKakao(searchChannel)){
                    map.put("gubun", "daily");
                    sendResultList = campaignSummaryService.selectCampaignKakaoMonthlyStat(map);
                } /*else { // 채널 전체 - 통계건수 결함이 있어 주석처리
                    sendResultList = campaignSummaryService.selectCampaignTOTDailyStat(map);
                }*/
            }

            // Total Count
            int totalSendCnt = 0;
            int totalSuccessCnt = 0;
            int totalFailCnt = 0;
            int totalOpenCnt = 0;
            int totalDurationCnt = 0;
            int totalLinkCnt = 0;
            int totalReturnMailCnt = 0;

            if(sendResultList != null) {
                for(SendResultVo sendResult : sendResultList) {
                    totalSendCnt += sendResult.getSendCnt();
                    totalSuccessCnt += sendResult.getSuccessCnt();
                    totalFailCnt += sendResult.getFailCnt();
                    totalOpenCnt += sendResult.getOpenCnt();
                    totalDurationCnt += sendResult.getDurationCnt();
                    totalLinkCnt += sendResult.getLinkCnt();
                    totalReturnMailCnt += sendResult.getReturnMailCnt();
                }
            }

            returnData.put("totalSendCnt", totalSendCnt);
            returnData.put("totalSuccessCnt", totalSuccessCnt);
            returnData.put("totalSuccessPer", (float)totalSuccessCnt / totalSendCnt);
            returnData.put("totalFailCnt", totalFailCnt);
            returnData.put("totalFailPer", (float)totalFailCnt / totalSendCnt);
            returnData.put("totalOpenCnt", totalOpenCnt);
            returnData.put("totalOpenPer", (float)totalOpenCnt / totalSuccessCnt);
            returnData.put("totalDurationCnt", totalDurationCnt);
            returnData.put("totalDurationPer", (float)totalDurationCnt / totalSuccessCnt);
            returnData.put("totalLinkCnt", totalLinkCnt);
            returnData.put("totalLinkPer", (float) totalLinkCnt / totalSuccessCnt);
            returnData.put("totalReturnMailCnt", totalReturnMailCnt);
            returnData.put("totalReturnMailPer", (float)totalReturnMailCnt / totalSuccessCnt);
            returnData.put("minDt", pastYear);
            returnData.put("maxDt", thisYear);
            returnData.put("searchYear", searchYear);
            returnData.put("searchMonth", searchMonth);
            returnData.put("searchChannel", searchChannel);
            returnData.put("searchId", searchId);

            String[] useChannelArray = super.campaignChannelUseList.split(",");
            returnData.put("channelUseSize", useChannelArray.length);
            returnData.put("channelUseList", useChannelArray);
            returnData.put("campaignStat", sendResultList);

            if(StringUtil.isNotEmpty(format) && format.equals("excel")) {
                ReportExcelDownload reportExcel = new ReportExcelDownload();
                if("M".equals(searchChannel)) {
                    reportExcel.makeExcel(response, returnData, "campaign", "monthly_stat_" + lang + ".xls");
                } else {
                    reportExcel.makeExcel(response, returnData, "campaign", "monthly_stat_" + lang + "_etc.xls");
                }
                return null;
            } else {
                return new ModelAndView("report/campaign/monthlyRpt", returnData);  // report/campaign/monthly_stat_view
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
