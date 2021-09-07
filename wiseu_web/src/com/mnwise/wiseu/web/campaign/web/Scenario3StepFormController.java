package com.mnwise.wiseu.web.campaign.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.service.ScenarioService;
import com.mnwise.wiseu.web.env.service.EnvServerInfoService;

/**
 * 캠페인 등록 3단계 캠페인 수행 Controller
 */
@Controller
public class Scenario3StepFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Scenario3StepFormController.class);

    @Autowired private EnvServerInfoService envServerInfoService;
    @Autowired private ScenarioService scenarioService;

    @Value("${divide.schedule.use}")
    private String useDivideSchdule;
    @Value("${omnichannel.message.use:off}")
    private String useOmniChannelMessage;

    @ModelAttribute("scenarioVo")
    public ScenarioVo formBackingObject(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="1") int depthNo, HttpServletRequest request) throws Exception {
        try {
            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignVo.campaignNo", 0);

            ScenarioVo scenarioVo = new ScenarioVo();
            scenarioVo.setScenarioNo(scenarioNo);
            scenarioVo.getCampaignVo().setDepthNo(depthNo);
            scenarioVo.setUserVo(getLoginUserVo());
            scenarioVo.getCampaignVo().setCampaignNo(campaignNo);

            if(scenarioNo > 0) {
                scenarioVo = scenarioService.getScenario3StepInfo(scenarioVo);
                // DB에서 가져와야 하는데..그냥 이렇게 처리함.
                scenarioVo.getCampaignVo().setDepthNo(depthNo);
                scenarioVo.setWebExecMode(super.webExecMode);
                // dwr로 저장 후 servlet로 가져오는 부분 xss 처리
                for(CampaignVo campaignVo : scenarioVo.getCampaignList()) {
                    campaignVo.setCampaignPreface(StringUtil.escapeXss(campaignVo.getCampaignPreface()));
                }

                // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
                if(isInvalidAccess(scenarioVo.getGrpCd(), scenarioVo.getUserId())) {
                    return null;
                }

                return scenarioVo;
            } else {
                return new ScenarioVo();
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>3단계] 캠페인 수행 화면 출력
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/campaign3Step.do", method={RequestMethod.GET, RequestMethod.POST})  // /campaign/campaign_3step_form.do
    public ModelAndView referenceData(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="1") int depthNo,
                                      @RequestParam(defaultValue="") String channelType, HttpServletRequest request) throws Exception {
        try {
            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignVo.campaignNo", 0);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("useB4SendApproveYn", envServerInfoService.selectEnvServerInfo().getB4SendApproveYn());
            returnData.put("seedTime", System.currentTimeMillis());
            returnData.put("channelType", channelType);
            returnData.put("campaignNo", campaignNo);
            returnData.put("depthNo", depthNo);
            returnData.put("useDivide", this.useDivideSchdule);

            if(this.useOmniChannelMessage.equals("on")) {
                scenarioService.findSubCampaignAndStoreMap(scenarioNo, campaignNo, depthNo, returnData);
                returnData.put("channelList", scenarioService.findAvailableChannels(scenarioNo));
            }

            String viewName = "/campaign/campaign3Step";  // /campaign/campaign_3step_form
            return new ModelAndView(viewName, returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
