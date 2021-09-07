package com.mnwise.wiseu.web.report.web.campaign;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportBasicVo;

/**
 * 캠페인 리포트 - 수신거부 분석(메일) Controller
 */
@Controller
public class CampaignRejectSummaryController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignRejectSummaryController.class);

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 수신거부 분석(메일)<br/>
     * - JSP : /report/campaign/rejectRpt.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/rejectRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/reject_summary.do
    public ModelAndView list(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                             @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String abTestCond,
                             HttpServletRequest request) throws Exception {
        try {
            addTabMenu(request);

            CaseInsensitiveMap userInfo = campaignReportService.selectCreateUserInfo(scenarioNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(userInfo.get("GRP_CD").toString(), userInfo.get("USER_ID").toString())) {
                return new ModelAndView("");
            }

            List<CampaignReportBasicVo> campaignReportRejectList = campaignReportService.getCampaignReportRejectList(scenarioNo, campaignNo);

            ModelAndView mav = new ModelAndView("report/campaign/rejectRpt");  // report/campaign/reject_summary_view
            mav.addObject("campaignReportRejectList", campaignReportRejectList);
            mav.addObject("abTestType", abTestType);
            mav.addObject("abTestCond", abTestCond);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 캠페인 에러코드별 대상자 리스트 엑셀 다운로드
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/excelRejectSummary.do", method={RequestMethod.GET, RequestMethod.POST})
    public void excel(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo, HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;

        try {
            String fileName = "reject_" + DateUtil.getNowDateTime() + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            out = response.getOutputStream();
            campaignReportService.makeCsvCampaignReportRejectDetailList(out, scenarioNo, campaignNo);
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }
}
