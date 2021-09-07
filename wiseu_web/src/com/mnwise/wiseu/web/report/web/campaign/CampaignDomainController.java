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
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;

/**
 * 캠페인 리포트 - 도메인별 분석(메일) Controller
 */
@Controller
public class CampaignDomainController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignDomainController.class);

    /**
     * [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 도메인별 분석(메일)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/domainRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/domain_summary.do
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

            UserVo userVo = getLoginUserVo();
            List<CampaignReportVo> campaignReportDomainList = campaignReportService.getCampaignReportDomainList(scenarioNo, campaignNo, userVo.getLanguage());

            ModelAndView mav = new ModelAndView("report/campaign/domainRpt");  // report/campaign/domain_summary_view
            mav.addObject("campaignReportDomainList", campaignReportDomainList);
            mav.addObject("abTestType", abTestType);
            mav.addObject("abTestCond", abTestCond);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 도메인별 분석(메일) - 엑셀 다운로드
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/excelDomainSummary.do", method={RequestMethod.GET, RequestMethod.POST})
    public void excel(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                      @RequestParam(defaultValue="") String errorCd, @RequestParam(defaultValue="") String domainNm,
                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream out = null;

        try {
            String fileName = "error_" + errorCd + "_" + DateUtil.getNowDateTime() + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            String lang = getLoginUserVo().getLanguage();

            campaignReportService.makeCsvCampaignErrorCodeDetailList(response.getOutputStream(), scenarioNo, campaignNo, errorCd, lang, domainNm);
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(out);
        }
    }
}
