package com.mnwise.wiseu.web.campaign.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.service.ScenarioService;

/**
 * 캠페인 발송에러 - 에러 로그(팝업) Controller
 */
@Controller
public class CampaignErrorPopupController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CampaignErrorPopupController.class);

    @Autowired private ScenarioService scenarioService;

    /**
     * - [캠페인>캠페인 리스트] 캠페인 리스트 - 에러 로그(팝업) <br/>
     *   캠페인 리스트에서 수행상태가 발송에러를 클릭시 호출
     *
     * - 20100611 : 검색 후 에러리스트를 채널별로 가져오게 분기문처리.
     */
    @RequestMapping(value = "/campaign/campaignErrorPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /campaign/campaign_error_popup.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignNo", 0);
            long resultSeq = ServletRequestUtils.getLongParameter(request, "resultSeq", 0);
            String campaignPreface = StringUtil.defaultString(request.getParameter("campaignPreface"));
            String scenarioNm = StringUtil.defaultString(request.getParameter("scenarioNm"));

            String errMsg = scenarioService.getCampaignError(campaignNo, resultSeq);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("campaignNo", campaignNo);
            returnData.put("scenarioNm", scenarioNm);
            returnData.put("campaignPreface", campaignPreface);
            returnData.put("errMsg", errMsg);

            return new ModelAndView("campaign/campaignErrorPopup", returnData);  // campaign/campaign_error_popup
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
