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
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.service.ScenarioService;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.env.service.EnvServerInfoService;

/**
 * 캠페인 타겟발송(재발송) Controller
 */
@Controller("campaignResendController")
public class ResendController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ResendController.class);

    @Autowired private EnvServerInfoService envServerInfoService;
    @Autowired private ScenarioService scenarioService;

    /**
     * 캠페인 리포트에서 타겟발송 아이콘 클릭시 호출
     * 타겟발송(재발송) 대상자 수집 후에 캠페인 2단계 저작화면으로 이동
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/campaign/resend.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView resend(HttpServletRequest request) throws Exception {
        try {
            final ScenarioVo scenarioVo = new ScenarioVo();
            scenarioVo.setScenarioNo(ServletRequestUtils.getIntParameter(request, "scenarioNo", 0));
            scenarioVo.setAbTestType(ServletRequestUtils.getStringParameter(request, "abTestType", "N"));
            scenarioVo.setUserVo(getLoginUserVo());

            final CampaignVo campaignVo = scenarioVo.getCampaignVo();
            campaignVo.setCampaignNo(ServletRequestUtils.getIntParameter(request, "campaignNo", 0));
            campaignVo.setAbTestCond(ServletRequestUtils.getStringParameter(request, "abTestCond", ""));

            final ServerInfoVo serverInfo = envServerInfoService.selectEnvServerInfo();
            final String linkSeq = ServletRequestUtils.getStringParameter(request, "linkSeq", "");
            final String resendParam = ServletRequestUtils.getStringParameter(request, "resendMode", " ");

            final int newCampaignNo;
            if(isOmniChannelMessage(resendParam.substring(0, 1))) {
                final int scenarioNo = scenarioVo.getScenarioNo();
                final int campaignNo = campaignVo.getCampaignNo();
                final UserVo userVo = scenarioVo.getUserVo();
                newCampaignNo = scenarioService.makeOmniChannelSubCampaign(scenarioNo, campaignNo, resendParam, serverInfo, userVo);
            } else {
                newCampaignNo = makeCampaignAndGetNo(scenarioVo, serverInfo, resendParam, linkSeq);
            }

            final Map<String, Object> model = makeModel(request, scenarioVo, campaignVo, newCampaignNo);
            return new ModelAndView(new RedirectView("/campaign/campaign2Step.do"), model);  // /campaign/campaign_2step_form.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 생성할 캠페인이 옴니채널 메시지인지 relationType 으로 확인한다.
     *
     * @param relationType 'S': 성공, 'F': 실패, 'O': 오픈
     * @return
     */
    private boolean isOmniChannelMessage(String relationType) {
        return Const.RELATION_SUCCESS.equals(relationType) || Const.RELATION_FAIL.equals(relationType) || Const.RELATION_OPEN.equals(relationType);
    }

    /**
     * 재발송 캠페인을 생성하고 새 캠페인 번호를 반환
     *
     * @param scenarioVo
     * @param serverInfo
     * @param resendMode
     * @param linkSeq
     * @return
     * @throws Exception
     */
    private int makeCampaignAndGetNo(final ScenarioVo scenarioVo, final ServerInfoVo serverInfo, final String resendMode, final String linkSeq) throws Exception {
        try {
            return scenarioService.setRegistResendCampaign(scenarioVo, resendMode, serverInfo, linkSeq);
        } catch(Exception e) {
            log.error("while creating resend campaign, exception occurred. " + e.getMessage());
        }
        return 0;
    }

    private Map<String, Object> makeModel(final HttpServletRequest request, final ScenarioVo scenarioVo, final CampaignVo campaignVo, int newCampaignNo) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("scenarioNo", scenarioVo.getScenarioNo());
        model.put("depthNo", ServletRequestUtils.getIntParameter(request, "depthNo", 1) + 1);
        model.put("campaignVo.campaignNo", newCampaignNo);
        model.put("campaignVo.channelType", ServletRequestUtils.getStringParameter(request, "channelType"));
        model.put("abTestType", scenarioVo.getAbTestType());
        model.put("abTestCond", campaignVo.getAbTestCond());
        return model;
    }

}
