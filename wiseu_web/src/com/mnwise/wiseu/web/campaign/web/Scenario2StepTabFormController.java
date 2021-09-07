package com.mnwise.wiseu.web.campaign.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;

/**
 * 캠페인 저작 2단계에 TAB폼 콘트롤러
 * -20091007 : mail,sms,mms 등의 채널과 재발송관련하여 시나리오번호, 캠페인번호, 채널타잎, 재발송차수 등의 값을 받아와 다시 캠페인저작2단계 페이지에 넘겨준다.
 */
@Controller
public class Scenario2StepTabFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Scenario2StepTabFormController.class);

    /**
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @param channelType 채널종류
     * @param depthNo 재발송차수
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/campaign/campaign_2step_tab_form.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView formCampaignStep2(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();

            int scenarioNo = ServletRequestUtils.getIntParameter(request, "scenarioNo", 0);
            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignVo.campaignNo", 0);
            String channelType = ServletRequestUtils.getStringParameter(request, "campaignVo.channelType");
            // 재발송 차수(depthNo) 받아옴 2009.10.07
            int depthNo = ServletRequestUtils.getIntParameter(request, "campaignVo.depthNo", 1);

            returnData.put("scenarioNo", String.valueOf(scenarioNo));
            returnData.put("depthNo", depthNo);
            returnData.put("campaignVo.campaignNo", String.valueOf(campaignNo));
            returnData.put("campaignVo.channelType", channelType);

            return new ModelAndView(new org.springframework.web.servlet.view.RedirectView("/campaign/campaign2Step.do"), returnData);  // /campaign/campaign_2step_form.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
