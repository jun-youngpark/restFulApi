package com.mnwise.wiseu.web.report.web.campaign;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.ChannelUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportErrorVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignSendResultVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCommonService;
import com.mnwise.wiseu.web.report.service.campaign.CampaignErrSummaryService;

/**
 * 캠페인 리포트 - 오류 분석 Controller
 */
@Controller
public class CampaignErrSummaryController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignErrSummaryController.class);

    @Autowired private CampaignCommonService campaignCommonService;
    @Autowired private CampaignErrSummaryService campaignErrSummaryService;

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 오류 분석(메일,SMS,LMS/MMS,친구톡,푸시)<br/>
     * - JSP : /report/campaign/errorRpt_mail.jsp (메일)<br/>
     * - JSP : /report/campaign/errorRpt_etc.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/errorRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/err_summary.do
    public ModelAndView list(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                             @RequestParam(defaultValue="M") String channelType, @RequestParam(defaultValue="0") long resultSeq,
                             @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String abTestCond,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            addTabMenu(request);

            UserVo userVo = getLoginUserVo();
            String language = userVo.getLanguage();

            List<CampaignReportErrorVo> campaignReportErrorList = campaignReportService.getCampaignReportErrorList(scenarioNo, campaignNo, language, channelType, resultSeq);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("campaignReportErrorList", campaignReportErrorList);
            returnData.put("abTestType", abTestType);
            returnData.put("abTestCond", abTestCond);

            CaseInsensitiveMap userInfo = campaignReportService.selectCreateUserInfo(scenarioNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(userInfo.get("GRP_CD").toString(), userInfo.get("USER_ID").toString())) {
                return new ModelAndView("");
            }

            if(Channel.MAIL.equals(channelType)) {
                return new ModelAndView("report/campaign/errorRpt_mail", returnData);  // report/campaign/mail_err_summary_view
            } else {
                return new ModelAndView("report/campaign/errorRpt_etc", returnData);  // report/campaign/etc_err_summary_view
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 오류 분석(메일) - 엑셀 다운로드
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/report/campaign/excelErrSummary.do", method={RequestMethod.GET, RequestMethod.POST})
    public void excel(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                      @RequestParam(defaultValue="M") String channelType, @RequestParam(defaultValue="0") long resultSeq,
                      @RequestParam(defaultValue="") String errorCd, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;

        try {
            String fileName = "error_" + errorCd + "_" + DateUtil.getNowDateTime() + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            String language = getLoginUserVo().getLanguage();
            out = response.getOutputStream();

            if(Channel.MAIL.equals(channelType)) {
                campaignReportService.makeCsvCampaignErrorCodeDetailList(out, scenarioNo, campaignNo, errorCd, language);
            } else if(Channel.FAX.equals(channelType)) {
                campaignReportService.makeCsvCampaignFaxErrorCodeDetailList(out, scenarioNo, campaignNo, errorCd, language, resultSeq);
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
                campaignReportService.makeCsvCampaignSMSErrorCodeDetailList(out, scenarioNo, campaignNo, errorCd, language, resultSeq, channelType);
            } else if(Channel.PUSH.equals(channelType)) {
                pushService.makeCsvCampaignPushErrorCodeDetailList(out, scenarioNo, campaignNo, errorCd, language, resultSeq);
            } else if(Channel.FRIENDTALK.equals(channelType) ) {
                campaignReportService.makeCsvCampaignFtErrorCodeDetailList(out, scenarioNo, campaignNo, errorCd, language, resultSeq, channelType);
            } else if(Channel.BRANDTALK.equals(channelType) ) {
                campaignReportService.makeCsvEmErrorCodeDetailList(campaignNo, resultSeq, errorCd, out);
            }
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 오류 분석(메일) - 캠페인 코드별 도메인 리포트(팝업)
     */
    @RequestMapping(value="/report/campaign/errorCodeRptPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/err_bycode_popup.do
    protected ModelAndView handle(ScenarioInfoVo scenarioInfoVo, HttpServletRequest request) throws Exception {
        try {
            String searchErrCd = ServletRequestUtils.getStringParameter(request, "searchErrCd");

            UserVo userVo = getLoginUserVo();
            scenarioInfoVo.setUserVo(userVo);
            scenarioInfoVo.setLanguage(userVo.getLanguage());
            scenarioInfoVo.getCampaignInfoVo().setCampaignNo(scenarioInfoVo.getCampaignNo());

            // 캠페인 기본정보
            scenarioInfoVo = campaignCommonService.selectScenarioInfo(scenarioInfoVo.getCampaignNo());
            scenarioInfoVo.getCampaignInfoVo().setSearchErrCd(searchErrCd);

            // 발송결과
            CampaignSendResultVo campaignSendResultVo = campaignCommonService.selectSendResult(scenarioInfoVo);

            ModelAndView mav = new ModelAndView("report/campaign/errorCodeRptPopup");  // report/campaign/err_bycode_view
            mav.addObject("scenarioInfoVo", scenarioInfoVo);
            mav.addObject("campaignSendResultVo", campaignSendResultVo);
            mav.addObject("errCdResultMsgList", campaignErrSummaryService.selectErrCdResultMsgList(scenarioInfoVo));
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
