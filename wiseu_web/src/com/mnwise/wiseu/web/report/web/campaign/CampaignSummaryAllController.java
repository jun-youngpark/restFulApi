package com.mnwise.wiseu.web.report.web.campaign;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CampaignSummaryAllController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignSummaryAllController.class);

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 전체 요약(메일,SMS,LMS/MMS,친구톡,푸시,FAX)<br/>
     * - JSP : /report/campaign/summaryRpt_mail.jsp <br/>
     * - JSP : /report/campaign/summaryRpt_push.jsp <br/>
     * - JSP : /report/campaign/summaryRpt_etc.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/summaryRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/summary_all.do
    public ModelAndView list(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                             @RequestParam(defaultValue="") String relationType, @RequestParam(defaultValue="M") String channelType,
                             @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String abTestCond,
                             HttpServletRequest request) throws Exception {
        try {
            addTabMenu(request);

            UserVo userVo = getLoginUserVo();
            String language = userVo.getLanguage();

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("scenarioNo", scenarioNo);
            paramMap.put("campaignNo", campaignNo);
            paramMap.put("lang", language);
            paramMap.put("mode", getCampaignMode(relationType));

            // 해당 시나리오의 캠페인 리스트 가져오기.

            // 일반/재발송 최대 depthNo 가져오기
            int maxDepthNoN = campaignReportService.getCampaignReportMaxDepth(scenarioNo, campaignNo, "N");

            // 타겟발송 최대 depthNo 가져오기
            int maxDepthNoL = campaignReportService.getCampaignReportMaxDepth(scenarioNo, campaignNo, "L");

            Map<String, Object> returnData = new HashMap<>();

            // 총발송합계 successCnt, hard/soft bounce 가져오기.
            List<CampaignReportVo> campaignSummaryAllList = null;
            if (Channel.MAIL.equals(channelType)) {
                campaignSummaryAllList = campaignReportService.getCampaignSummaryAllList(paramMap);
                returnData.put("campaignSummaryAllList", campaignSummaryAllList);
                returnData.put("campaignSendResultVo", campaignReportService.getCampaignReportAllCntInfo(paramMap));
            } else if(Channel.PUSH.equals(channelType)) {
                campaignSummaryAllList = campaignReportService.getCampaignSummaryAllList(paramMap);
                returnData.put("campaignSummaryAllList", campaignSummaryAllList);
            } else if(Channel.FAX.equals(channelType)) {
                List<CampaignReportVo> listCampaignReportVo = campaignReportService.getCampaignFaxSummaryDetail(paramMap);
                returnData.put("listCampaignReportVo", listCampaignReportVo);
            }else if(ChannelUtil.isKakao(channelType)) {
                List<CampaignReportVo> listCampaignReportVo = campaignReportService.getCampaignKakaoSummaryDetail(paramMap);
                returnData.put("listCampaignReportVo", listCampaignReportVo);
            }else {
                List<CampaignReportVo> listCampaignReportVo = campaignReportService.getCampaignSMSSummaryDetail(paramMap);
                returnData.put("listCampaignReportVo", listCampaignReportVo);
            }
            returnData.put("lstResendUse", super.lstResendUse);
            returnData.put("scenarioNo", scenarioNo);
            returnData.put("campaignNo", campaignNo);
            returnData.put("maxDepthNoN", maxDepthNoN);
            returnData.put("maxDepthNoL", maxDepthNoL);
            returnData.put("abTestType", abTestType);
            returnData.put("abTestCond", abTestCond);
            returnData.put("relationType", relationType);

            CaseInsensitiveMap userInfo = campaignReportService.selectCreateUserInfo(scenarioNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(userInfo.get("GRP_CD").toString(), userInfo.get("USER_ID").toString())) {
                return new ModelAndView("");
            }

            if(Channel.MAIL.equals(channelType)) {
                // 수신확인 결과 리스트 가져오기.
                returnData.put("campaignReportReceiveList", campaignReportService.getCampaignSummaryReceiveList(scenarioNo, language));
                int returnCnt = campaignReportService.getCampaignSummaryReturn(campaignNo);  // 리턴메일
                int rejectCnt = campaignReportService.getCampaignSummaryReject(campaignNo);  // 수신거부
                int spamCnt = campaignReportService.getCampaignSummarySpam(campaignNo, language);  // 스팸차단
                returnData.put("returnCnt", returnCnt);
                returnData.put("rejectCnt", rejectCnt);
                returnData.put("spamCnt", spamCnt);

                return new ModelAndView("report/campaign/summaryRpt_mail", returnData);  // report/campaign/mail_summary_all_view
            } else if(Channel.PUSH.equals(channelType)) {
                List<CaseInsensitiveMap> pushReceiveList = new ArrayList<>();
                List<CaseInsensitiveMap> pushSendlogList = new ArrayList<>();
                for(CampaignReportVo reportVo : campaignSummaryAllList) {
                    pushReceiveList.add(pushService.selectPushReceiptCount(reportVo.getCampaignNo()));
                    pushSendlogList.add(pushService.selectPushSendlogCount(reportVo.getCampaignNo()));
                }
                returnData.put("pushReceiveList", pushReceiveList);
                returnData.put("pushSendlogList", pushSendlogList);
                return new ModelAndView("report/campaign/summaryRpt_push", returnData);  // report/campaign/push_summary_all_view
            } else {
                //문자(sms,lms),카카오(알림톡,친구톡,브랜드톡),팩스
                return new ModelAndView("report/campaign/summaryRpt_etc", returnData);  // report/campaign/etc_summary_all_view
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 전체 요약(메일) - 엑셀다운로드(성공)<br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/excelAllSummary.do", method={RequestMethod.GET, RequestMethod.POST})
    public void excel(@RequestParam(defaultValue="0") int excelCampaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                      @RequestParam(defaultValue="") String gubun, @RequestParam(defaultValue="") String channelType,
                      HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;

        try {
            String fileName = "gubun_" + DateUtil.getNowDateTime() + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            out = response.getOutputStream();
            if(gubun.equals("open")) { // 수신확인 대상자 다운로드
                campaignReportService.makeCsvCampaignReceiveDetailList(out, scenarioNo, excelCampaignNo);
            } else if(gubun.equals("success")) { // 성공대상자 다운로드
                campaignReportService.makeCsvCampaignSuccessDetailList(out, scenarioNo, excelCampaignNo, channelType);
            }
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * relationType 값으로 '일반' 혹은 '멀티채널' 모드 결정
     *
     * - 일반: 'N', 'R', 'L'
     * - 멀티채널: 'S', 'F', 'O'
     *
     * @param relationType
     * @return
     */
    private String getCampaignMode(String relationType) {
        if(Const.RELATION_NORMAL.equals(relationType) || Const.RELATION_RESEND.equals(relationType) || Const.RELATION_LINK.equals(relationType)) {
            return "normal";
        } else if(Const.RELATION_SUCCESS.equals(relationType) || Const.RELATION_FAIL.equals(relationType) || Const.RELATION_OPEN.equals(relationType)) {
            return "omnichannel";
        }

        return "normal";
    }
}