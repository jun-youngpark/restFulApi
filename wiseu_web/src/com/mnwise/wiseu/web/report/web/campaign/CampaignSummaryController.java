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
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportBasicVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignReportService;
import com.mnwise.wiseu.web.report.web.ReportExcelDownload;

@Controller
public class CampaignSummaryController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignSummaryAllController.class);

    @Autowired private CampaignReportService campaignReportService;

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 요약 분석(메일,FAX)<br/>
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 요약 분석(메일) - 엑셀 다운로드(발송결과)<br/>
     * - JSP : /report/campaign/summary_view.jsp <br/>
     * - JSP : /report/campaign/fax_summary_view.jsp <br/>
     *
     * @param format 요약 화면 엑셀 다운로드 인자
     * @param request
     * @param response
     * @return
     * @throws ServletRequestBindingException
     * @throws IOException
     *
     * TODO : 메일/FAX 채널의 [요약분석] 화면도 [전체요약] 화면에 통합 고려
     *        - 문자/친구톡/PUSH 채널의 [요약분석] 화면과 [전체요약] 화면 출력내용이 같음.
     */
    /*
    @RequestMapping(value="/report/campaign/summary.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                             @RequestParam(defaultValue="N") String relationType, @RequestParam(defaultValue="M") String channelType,
                             @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String abTestCond,
                             @RequestParam(defaultValue="0") long resultSeq, String format,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            addTabMenu(request);

            CaseInsensitiveMap userInfo = campaignReportService.selectCreateUserInfo(scenarioNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(userInfo.get("GRP_CD").toString(), userInfo.get("USER_ID").toString())) {
                return new ModelAndView("");
            }

            UserVo userVo = getLoginUserVo();
            String language = userVo.getLanguage();
            // 캠페인 발송 결과
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("scenarioNo", scenarioNo);
            paramMap.put("campaignNo", campaignNo);
            paramMap.put("lang", language);
            paramMap.put("resultSeq", resultSeq);
            paramMap.put("abTestType", abTestType);
            paramMap.put("abTestCond", abTestCond);

            Map<String, Object> returnData = new HashMap<>();
            CampaignReportVo campaignReportVo = null;
            if(Channel.MAIL.equals(channelType) || Channel.FAX.equals(channelType)) {
                campaignReportVo = campaignReportService.getCampaignSummaryDetail(paramMap);
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
                List<CampaignReportVo> listCampaignReportVo = campaignReportService.getCampaignSMSSummaryDetail(paramMap);
                returnData.put("listCampaignReportVo", listCampaignReportVo);
            }
            // 최대 DepthNo 값 가져오기
            int maxDepthNo = campaignReportService.getCampaignReportMaxDepth(scenarioNo, campaignNo, relationType);

            returnData.put("campaignReportVo", campaignReportVo);
            returnData.put("maxDepthNo", maxDepthNo);
            returnData.put("scenarioNo", scenarioNo);
            returnData.put("campaignNo", campaignNo);
            returnData.put("relationType", relationType);

            if(Channel.MAIL.equals(channelType)) {
                // 캠페인 수신확인
                CampaignReportVo campaignReportReceiveVo = campaignReportService.getCampaignSummaryReceive(campaignNo);

                int returnCnt = campaignReportService.getCampaignSummaryReturn(campaignNo);  // 리턴메일
                int rejectCnt = campaignReportService.getCampaignSummaryReject(campaignNo);  // 수신거부
                int spamCnt = campaignReportService.getCampaignSummarySpam(campaignNo, language);  // 스팸차단

                returnData.put("campaignReportReceiveVo", campaignReportReceiveVo);
                returnData.put("returnCnt", returnCnt);
                returnData.put("rejectCnt", rejectCnt);
                returnData.put("spamCnt", spamCnt);

                // 요약화면 엑셀 다운로드 처리
                if(StringUtil.isNotEmpty(format) && format.equals("excel")) {
                    // CampaignReportInterceptor 에서 인터셉터 한 campaignReportBasicVo를
                    // 가져온다. report-web.xml 참고
                    CampaignReportBasicVo campaignReportBasicVo = (CampaignReportBasicVo) request.getAttribute("campaignReportBasicVo");
                    returnData.put("campaignReportBasicVo", campaignReportBasicVo);
                    ReportExcelDownload reportExcel = new ReportExcelDownload();
                    reportExcel.makeExcel(response, returnData, "campaign", "summary_" + language + ".xls");
                    return null;
                }
            } else if(Channel.FAX.equals(channelType)) {
                return new ModelAndView("report/campaign/fax_summary_view", returnData);
            }

            return new ModelAndView("report/campaign/summary_view", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }*/

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 요약 분석(메일) - 엑셀 다운로드(성공)<br/>
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 요약 분석(메일) - 엑셀 다운로드(리턴메일 분석)<br/>
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 요약 분석(메일) - 엑셀 다운로드(수신거부 분석)<br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/excelSummary.do", method={RequestMethod.GET, RequestMethod.POST})
    public void excel(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                      @RequestParam(defaultValue="") String gubun, @RequestParam(defaultValue="") String channelType,
                      @RequestParam(defaultValue="0") long resultSeq, HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;

        try {
            String fileName = gubun + "_" + DateUtil.getNowDateTime() + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            out = response.getOutputStream();
            // 채널 타입에 따른 엑셀 쿼리를 분기 한다.
            if(Channel.MAIL.equals(channelType)) {
                if(gubun.equals("open")) { // 수신확인 대상자
                    campaignReportService.makeCsvCampaignReceiveDetailList(out, scenarioNo, campaignNo);
                } else if(gubun.equals("returnMail")) { // 리턴메일 대상자
                    campaignReportService.makeCsvCampaignReturnDetailList(out, scenarioNo, campaignNo);
                } else if(gubun.equals("reject")) { // 수신거부 대상자
                    campaignReportService.makeCsvCampaignRejectDetailList(out, scenarioNo, campaignNo);
                } else if(gubun.equals("success")) { // 성공대상자
                    campaignReportService.makeCsvCampaignSuccessDetailList(out, scenarioNo, campaignNo, channelType);
                }
            } else if(Channel.FAX.equals(channelType)) {
                if(gubun.equals("success")) { // 성공대상자
                    campaignReportService.makeCsvCampaignSuccessDetailFaxList(out, scenarioNo, campaignNo, resultSeq);
                }
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {
                if(gubun.equals("success")) { // 성공대상자
                    campaignReportService.makeCsvCampaignSuccessDetailSmsList(out, scenarioNo, campaignNo, channelType, resultSeq);
                }
            } else if(Channel.FRIENDTALK.equals(channelType)) {
                if(gubun.equals("success")) { // 성공대상자
                    campaignReportService.makeCsvCampaignSuccessDetailFRTList(out, scenarioNo, campaignNo, channelType, resultSeq);
                }
            } else if(Channel.PUSH.equals(channelType)) {
                if(gubun.equals("success")) { // 성공대상자
                    campaignReportService.makeCsvCampaignSuccessDetailList(out, scenarioNo, campaignNo, channelType);
                }
            }
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }
}