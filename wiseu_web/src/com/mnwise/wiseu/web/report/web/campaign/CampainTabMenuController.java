package com.mnwise.wiseu.web.report.web.campaign;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.util.FormatUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.channel.service.PushService;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportBasicVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignReportService;

public class CampainTabMenuController extends BaseController {
    @Autowired protected CampaignReportService campaignReportService;
    @Autowired protected PushService pushService;

    public boolean addTabMenu(HttpServletRequest request) throws Exception {
        int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignNo", 0);
        int scenarioNo = ServletRequestUtils.getIntParameter(request, "scenarioNo", 0);
        String channelType = ServletRequestUtils.getStringParameter(request, "channelType", "M");
        String relationType = ServletRequestUtils.getStringParameter(request, "relationType", "N");

        //A/B테스트 기능
        String abTestType = ServletRequestUtils.getStringParameter(request, "abTestType", "N");
        String abTestCond = ServletRequestUtils.getStringParameter(request, "abTestCond", "");

        /* 각 탭 메뉴 정보를 담는다. menuInfo[][0] - menuUrl menuInfo[][1] - on/off 를 제외한 image name menuInfo[][2] - on/off 여부 */
        //ab 테스트 기능 관련하여 리포트 탭메뉴 추가로 인해 배열 사이즈 +1
        String[][] menuInfo = new String[10][3];
        String menuDetail = "?scenarioNo=" + scenarioNo + "&campaignNo=" + campaignNo + "&abTestType=" + abTestType + "&abTestCond=" + abTestCond + "&relationType=" + relationType;

        // 요약분석
        menuInfo[0][0] = "/report/campaign/summary.do" + menuDetail;
        menuInfo[0][1] = "tab_sum";
        menuInfo[0][2] = "off";

        // 오류분석
        menuInfo[1][0] = "/report/campaign/errorRpt.do" + menuDetail;  // /report/campaign/err_summary.do
        menuInfo[1][1] = "tab_error";
        menuInfo[1][2] = "off";

        // 전체요약
        menuInfo[2][0] = "/report/campaign/summaryRpt.do" + menuDetail;  // /report/campaign/summary_all.do
        menuInfo[2][1] = "tab_all";
        menuInfo[2][2] = "off";

        // 스팸차단분석 (미사용)
        menuInfo[3][0] = "";  // /report/campaign/err_spam.do" + menuDetail;
        menuInfo[3][1] = "tab_spam";
        menuInfo[3][2] = "off";

        // 도메인별분석
        menuInfo[4][0] = "/report/campaign/domainRpt.do" + menuDetail;  // /report/campaign/domain_summary.do
        menuInfo[4][1] = "tab_domain";
        menuInfo[4][2] = "off";

        // 링크클릭분석
        menuInfo[5][0] = "/report/campaign/linkClickRpt.do" + menuDetail;  // /report/campaign/linkclick.do
        menuInfo[5][1] = "tab_link";
        menuInfo[5][2] = "off";

        // 리턴메일 분석
        menuInfo[6][0] = "/report/campaign/returnMailRpt.do" + menuDetail;  // /report/campaign/return_summary.do
        menuInfo[6][1] = "tab_return";
        menuInfo[6][2] = "off";

        // 수신거부 분석
        menuInfo[7][0] = "/report/campaign/rejectRpt.do" + menuDetail;  // /report/campaign/reject_summary.do
        menuInfo[7][1] = "tab_receive";
        menuInfo[7][2] = "off";

        // 수신확인 분석 (미사용)
        menuInfo[8][0] = "/report/campaign/open_summary.do" + menuDetail;
        menuInfo[8][1] = "tab_open";
        menuInfo[8][2] = "off";

        // A/B 결과 분석
        menuInfo[9][0] = "/report/campaign/abTestRpt.do" + menuDetail;  //   // /report/campaign/ab_summary.do;
        menuInfo[9][1] = "tab_ab";
        menuInfo[9][2] = "off";

        // 현재 페이지가 어딘지 비교하여 이미지를 on 시킨다.
        for(int j = 0; j < menuInfo.length; j++) {
            if(menuInfo[j][0].indexOf(request.getRequestURI()) > -1) {
                menuInfo[j][2] = "active";
                break;
            }
        }

        // 리포트 기본정보를 가져온다.
        CampaignReportBasicVo campaignReportBasicVo = null;
        if(Channel.MAIL.equals(channelType) || Channel.FAX.equals(channelType) || Channel.PUSH.equals(channelType)) {
            campaignReportBasicVo = campaignReportService.getCampaignReportBasicInfo(campaignNo);
            if(Channel.PUSH.equals(channelType)) {
                CaseInsensitiveMap sendlogCountMap = pushService.selectPushSendlogCount(campaignNo);
                if(sendlogCountMap != null) {
                    String sendCnt = String.valueOf(sendlogCountMap.get("SEND_CNT"));
                    String succCnt = String.valueOf(sendlogCountMap.get("SUCC_CNT"));
                    campaignReportBasicVo.setSendCnt(sendCnt.matches("\\d") ? Integer.parseInt(sendCnt) : 0);
                    campaignReportBasicVo.setSuccessCnt(succCnt.matches("\\d") ? Integer.parseInt(succCnt) : 0);
                }
            }
        } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
            campaignReportBasicVo = campaignReportService.getCampaignSMSReportBasicInfo(campaignNo);
        } else if(ChannelUtil.isKakao(channelType)) {   //카카오
            campaignReportBasicVo = campaignReportService.getCampaignKakaoReportBasicInfo(campaignNo);
        }

        long stime = FormatUtil.toBasicDate(campaignReportBasicVo.getSendstartDt(), campaignReportBasicVo.getSendstartTm()).getTime();
        long etime = FormatUtil.toBasicDate(campaignReportBasicVo.getSendEndDt(), campaignReportBasicVo.getSendEndTm()).getTime();
        String period ;
        try {
            period = DurationFormatUtils.formatPeriod(stime, etime, "HH:mm:ss");
        }catch(IllegalArgumentException e) {
            period = "00:00:00";
        }
        campaignReportBasicVo.setSendDurationTm(period);

        // 채널 탭 정보 설정
        //TODO 미사용 이미지로 판단, 삭제필요
        String channelImg = "";
        if(Channel.MAIL.equals(campaignReportBasicVo.getChannelType())) { // EMAIL
            channelImg = "tab_mail_on.gif";
        } else if(Channel.SMS.equals(campaignReportBasicVo.getChannelType())) { // SMS
            channelImg = "tab_sms_on.gif";
        } else if(Channel.LMSMMS.equals(campaignReportBasicVo.getChannelType())) { // MMS
            channelImg = "tab_mms_on.gif";
        } else if(Channel.FAX.equals(campaignReportBasicVo.getChannelType())) { // FAX
            channelImg = "tab_fax_on.gif";
        } else if(Channel.PUSH.equals(campaignReportBasicVo.getChannelType())) { // PUSH
            channelImg = "tab_push_on.gif";
        } else if(Channel.FRIENDTALK.equals(campaignReportBasicVo.getChannelType())) { // 친구톡
            channelImg = "tab_friendtalk_on.gif";
        }

        request.setAttribute("menuInfo", menuInfo);
        request.setAttribute("channelImg", channelImg);
        request.setAttribute("campaignReportBasicVo", campaignReportBasicVo);
        request.setAttribute("templateList", campaignReportService.getTemplateList(campaignNo));
        request.setAttribute("abTestType", abTestType);
        request.setAttribute("abTestCond", abTestCond);

        return true;
    }
}
