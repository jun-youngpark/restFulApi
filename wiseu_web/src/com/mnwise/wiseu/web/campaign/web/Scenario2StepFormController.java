package com.mnwise.wiseu.web.campaign.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.service.ScenarioService;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.env.service.EnvServerInfoService;
import com.mnwise.wiseu.web.template.util.KakaoButtonUtils;

/**
 * 캠페인 등록 2단계 메세지 작성 Controller
 */
@Controller
public class Scenario2StepFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Scenario2StepFormController.class);

    @Autowired private ScenarioService scenarioService;
    @Autowired private EnvServerInfoService envServerInfoService;
    @Autowired private ServletContext servletContext;
    @Autowired private Scenario2StepValidator scenario2StepValidator;

    @Value("${editor.multilang.use:off}")
    private String useMultiLang;

    @ModelAttribute("scenarioVo")
    public ScenarioVo formBackingObject(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="1") int depthNo, String msg,
                                        HttpServletRequest request) throws Exception {
        try {
            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignVo.campaignNo", 0);
            String channelType = ServletRequestUtils.getStringParameter(request, "campaignVo.channelType");
            int maxDepthNo = 1;
            if(depthNo == 0) {
                depthNo = 1;
            }

            request.setAttribute("msg", msg);

            List<CampaignVo> campaignList = scenarioService.getScenariochannelInfo(scenarioNo);

            if(campaignNo == 0) {
                for(CampaignVo campaignVo : campaignList) {
                    if(campaignVo.getDepthNo() != depthNo)
                        continue;
                    campaignNo = campaignVo.getCampaignNo();
                    channelType = campaignVo.getChannelType();
                    depthNo = campaignVo.getDepthNo();
                    break;
                }
            } else {
                // 해당 채널의 최대 DEPTH_NO을 알기 위함
                for(CampaignVo campaignVo : campaignList) {
                    if(campaignVo.getChannelType().equals(channelType)) {
                        maxDepthNo = campaignVo.getDepthNo();
                        // break;
                    }
                }
            }

            ScenarioVo scenarioVo = scenarioService.getScenario2StepInfo(scenarioNo, campaignNo);
            scenarioVo.setCampaignList(campaignList);
            scenarioVo.setViewDepthNo(depthNo);
            scenarioVo.setMaxDepthNo(maxDepthNo);
            scenarioVo.getCampaignVo().setEditorId(getLoginId());
            scenarioVo.setWebExecMode(super.webExecMode);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(scenarioVo.getGrpCd(), scenarioVo.getUserId())) {
                return null;
            }

            return scenarioVo;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계] 캠페인 메세지 작성 화면 출력 (전체 채널)
     * - JSP : /campaign/campaign2Step_mail.jsp (Email)
     * - JSP : /campaign/campaign2Step_sms.jsp (SMS)
     * - JSP : /campaign/campaign2Step_mms.jsp (LMS/MMS)
     * - JSP : /campaign/campaign2Step_fax.jsp (FAX)
     * - JSP : /campaign/campaign2Step_frt.jsp (친구톡)
     * - JSP : /campaign/campaign2Step_brt.jsp (브랜드톡)
     * - JSP : /campaign/campaign2Step_push.jsp (PUSH)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/campaign2Step.do", method=RequestMethod.GET)  // /campaign/campaign_2step_form.do
    public ModelAndView referenceData(@RequestParam(defaultValue="1") int depthNo, HttpServletRequest request) throws Exception {
        try {
            String channelType = ServletRequestUtils.getStringParameter(request, "campaignVo.channelType", "M");
            int campaignNo = ServletRequestUtils.getIntParameter(request, "campaignVo.campaignNo", 0);

            String viewName = "/campaign/campaign2Step_"+Const.enumChannel.findName(channelType);  // /campaign/campaign_2step_채널_form
            ModelAndView mav = new ModelAndView(viewName);

            Object obj = servletContext.getAttribute("envServerInfoVo");
            ServerInfoVo serverInfo = (obj != null) ? (ServerInfoVo) obj : envServerInfoService.selectEnvServerInfo();

            mav.addObject("useTestSend", "Y".equalsIgnoreCase(serverInfo.getB4RealSendTestSendYn()) ? "on" : "off");
            mav.addObject("depthNo", depthNo);
            mav.addObject("useMultiLang", this.useMultiLang);

            if(ChannelUtil.isKakao(channelType)) {
                mav.addObject("kakaoProfileList", scenarioService.getKakaoProfileList(getLoginId()));

                if(campaignNo > 0) {
                    mav.addObject("kakaoButtonList", KakaoButtonUtils.convertJsonToKakaoButtonList(scenarioService.selectKakaoButtons(campaignNo, Const.ServiceType.CAMPAIGN)));
                }
            }

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계] 캠페인 메세지 작성 내용 저장 (전체 채널)
     *
     * @param request
     * @param response
     * @param command
     * @param exception
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/campaign2Step.do", method=RequestMethod.POST)  // /campaign/campaign_2step_form.do
    public ModelAndView onSubmit(ScenarioVo scenarioVo, String nextStep, HttpServletRequest request, BindingResult errors) throws Exception {
        scenario2StepValidator.validate(scenarioVo, errors);
        if(errors.hasErrors()) {
            ModelAndView mav = new ModelAndView("/campaign/campaign2Step_"+Const.enumChannel.findName(scenarioVo.getCampaignVo().getChannelType()));  // /campaign/campaign_2step_채널_form
            mav.addObject("scenarioVo", scenarioVo);
            if(ChannelUtil.isKakao(scenarioVo.getCampaignVo().getChannelType())) {
                mav.addObject("kakaoProfileList", scenarioService.getKakaoProfileList(getLoginId()));
                if(scenarioVo.getCampaignVo().getCampaignNo() > 0) {
                    mav.addObject("kakaoButtonList", KakaoButtonUtils.convertJsonToKakaoButtonList(scenarioService.selectKakaoButtons(scenarioVo.getCampaignVo().getCampaignNo(), Const.ServiceType.CAMPAIGN)));
                }
            }
            return mav;
        }

        // 개인화 정보를 넣는다.
        if(scenarioVo.getScenarioNo() == 0) {
            scenarioVo.setUserVo(getLoginUserVo());
        }

        Object obj = servletContext.getAttribute("envServerInfoVo");
        ServerInfoVo serverInfo = (obj != null) ? (ServerInfoVo) obj : envServerInfoService.selectEnvServerInfo();

        // 테스트 발송을 해야 하는 경우 테스트 발송 없이 바로 다음단계로 넘어갈 수 없도록 CAMPAIGN LEVEL을 2로
        // 지정한다.
        if("N".equalsIgnoreCase(serverInfo.getB4RealSendTestSendYn()) || "nextStep".equalsIgnoreCase(nextStep)) {
            scenarioVo.getCampaignVo().setCampaignLevel("3");
        } else {
            scenarioVo.getCampaignVo().setCampaignLevel("2");
        }

        CampaignVo campaignVo = scenarioVo.getCampaignVo();

        if(campaignVo.getRetryCnt()== null || campaignVo.getRetryCnt() < 1) {
            campaignVo.setRetryCnt(1);
        } else if(campaignVo.getRetryCnt() > 99) {
            campaignVo.setRetryCnt(99);
        }

        scenarioService.setRegistScenario2StepInfo(scenarioVo);

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("scenarioNo", String.valueOf(scenarioVo.getScenarioNo()));
        returnData.put("campaignVo.campaignNo", String.valueOf(scenarioVo.getCampaignVo().getCampaignNo()));
        returnData.put("campaignVo.channelType", scenarioVo.getCampaignVo().getChannelType());
        returnData.put("depthNo", scenarioVo.getCampaignVo().getDepthNo());
        returnData.put("channelType", scenarioVo.getCampaignVo().getChannelType());

        if("nextStep".equalsIgnoreCase(nextStep)) {
            return new ModelAndView(new RedirectView("/campaign/campaign3Step.do"), returnData);  // /campaign/campaign_3step_form.do
        } else {
            return new ModelAndView(new RedirectView("/campaign/campaign2Step.do"), returnData);  // /campaign/campaign_2step_form.do
        }
    }
}
