package com.mnwise.wiseu.web.report.web.campaign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportReturnMailVo;

/**
 * 캠페인 리포트 - 리턴메일 분석(메일) Controller
 */
@Controller
public class CampaignReportReturnMailController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignReportReturnMailController.class);

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 리턴메일 분석(메일)<br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/report/campaign/returnMailRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/return_summary.do
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
            List<CampaignReportReturnMailVo> campaignReportReturnMailDetailList = campaignReportService.getCampaignReturnMailDetailList(campaignNo, userVo.getLanguage());

            ModelAndView mav = new ModelAndView("report/campaign/returnMailRpt");  // report/campaign/rtn_summary_view
            mav.addObject("campaignReportReturnMailDetailList", campaignReportReturnMailDetailList);
            mav.addObject("abTestType", abTestType);
            mav.addObject("abTestCond", abTestCond);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
