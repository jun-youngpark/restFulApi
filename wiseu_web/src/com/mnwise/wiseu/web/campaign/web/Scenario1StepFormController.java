package com.mnwise.wiseu.web.campaign.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.service.ScenarioService;

/**
 * 캠페인 등록 1단계 캠페인 기본정보 등록관리 Controller
 */
@Controller
public class Scenario1StepFormController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Scenario1StepFormController.class);

    @Autowired private Scenario1StepValidator scenario1StepValidator;
    @Autowired private ScenarioService scenarioService;

    @Value("${campaign.ab.test}")
    private String campaignAbTest;

    @ModelAttribute("scenarioVo")
    public ScenarioVo formBackingObject(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int campaignNo, String handlerType,
                                        @RequestParam(defaultValue="0") int depthNo, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();

            ScenarioVo scenarioVo = new ScenarioVo();
            scenarioVo.setScenarioNo(scenarioNo);
            //scenarioVo.setSiteNm(this.siteNm);
            scenarioVo.setUserVo(userVo);
            scenarioVo.getCampaignVo().setCampaignNo(campaignNo);

            // 시나리오 번호가 존재하는 경우 수정인경우
            if(scenarioNo > 0) {
                // 시나리오에 속해있는 캠페인 정보를 가져온다.
                scenarioVo = scenarioService.getScenario1StepInfo(scenarioVo);
                if(scenarioVo == null) {
                    scenarioVo = new ScenarioVo();
                }

                // 핸들러 타입 정보를 DB에서 가져오지 않는다. 파라미터로 처리한다.
                if(handlerType != null)
                    scenarioVo.setHandlerType(handlerType);

                // 등록된 캠페인 채널 가져와서 저장가능 모드인지를 체크한다.
                List<CampaignVo> campaignList = scenarioVo.getCampaignList();
                boolean noChange = false;
                boolean needAgree = false;
                boolean serviceTypeNoChange = false; // 수정 시 캠페인 채널유형을 바꾸지 못하도록 하는 용도

                for(CampaignVo campaignVo : campaignList) {
                    serviceTypeNoChange = true;

                    // 수정이 가능한지 여부를 검사한다.
                    if(!noChange && !campaignVo.getEditAble()) {
                        noChange = true;
                    }
                    // 승인요청 중인 캠페인인지를 체크한다.
                    if(!needAgree && "C".equals(campaignVo.getCampaignSts())) {
                        needAgree = true;
                    }
                }

                // scenarioNo, depthNo로 campaignNo를 가져온다.
                if(campaignNo == 0) {
                    scenarioVo.getCampaignVo().setCampaignNo(scenarioService.getCampaignNo(scenarioVo.getScenarioNo(), depthNo == 0 ? 1 : depthNo));
                } else {
                    scenarioVo.getCampaignVo().setCampaignNo(campaignNo);
                }

                scenarioVo.setNoChange(noChange);
                scenarioVo.setNeedAgree(needAgree);
                scenarioVo.setServiceTypeNoChange(serviceTypeNoChange);
                scenarioVo.setChannelUseList(super.campaignChannelUseList);
                //scenarioVo.setSiteNm(this.siteNm);
                scenarioVo.setSmsIndividualCharge(super.smsIndividualCharge);

                // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
                if(isInvalidAccess(scenarioVo.getGrpCd(), scenarioVo.getUserId())) {
                    return null;
                }

                scenarioVo.setWebExecMode(super.webExecMode);
            } else { // 수정이 아닌 신규 입력되는 캠페인
                scenarioVo = new ScenarioVo();

                scenarioVo.setChannels(new String[] {
                    String.valueOf(super.campaignChannelUseList.charAt(0))
                });
                scenarioVo.setChannelUseList(super.campaignChannelUseList);
                //scenarioVo.setSiteNm(this.siteNm);
                scenarioVo.setAbTestType("N");  //A/B 테스트 미사용

                // 신규 캠페인 등록일 경우 요청자 부서코드와 요청자 아이디(담당자)를 셋팅한다.
                scenarioVo.setReqDept(userVo.getReqDept());
                scenarioVo.setReqUser(userVo.getUserId());
                scenarioVo.setSmsIndividualCharge(super.smsIndividualCharge);
                scenarioVo.setWebExecMode(super.webExecMode);
            }

            return scenarioVo;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>1단계] 캠페인 기본정보 등록 화면 출력
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/campaign1Step.do", method=RequestMethod.GET)  // /campaign/campaign_step_form.do
    public ModelAndView referenceData(ScenarioVo scenarioVo, @RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="") String channelType,
                                      @RequestParam(defaultValue="0") int depthNo, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("campaign/campaign1Step");  // campaign/campaign_1step_form

            mav.addObject("campaignNo", campaignNo);
            mav.addObject("depthNo", depthNo);
            mav.addObject("channelType", channelType);
            mav.addObject("campaignAbTest", this.campaignAbTest);

            CampaignVo campaignVo = new CampaignVo();
            campaignVo.setCampaignNo(campaignNo);
            scenarioVo.setUserVo(getLoginUserVo());
            scenarioVo.setCampaignVo(campaignVo);

            if(campaignNo > 0) {
                mav.addObject("scenarioList", scenarioService.findOmniChannelCampaigns(scenarioVo));
                campaignVo.setChannelType(scenarioVo.getChannels()[0]);
            } else {
                campaignVo.setChannelType(String.valueOf(this.channelUseList.charAt(0)));
            }

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>1단계] 캠페인 기본정보 등록 저장
     *
     * @param request
     * @param response
     * @param command
     * @param exception
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/campaign1Step.do", method=RequestMethod.POST)  // /campaign/campaign_step_form.do
    public ModelAndView onSubmit(ScenarioVo scenarioVo, @RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="") String channelType,
                                 @RequestParam(defaultValue="0") int depthNo, String nextStep, HttpServletRequest request, BindingResult errors) throws Exception {
        try {
            scenarioVo.getCampaignVo().setCampaignNo(campaignNo);
            scenarioVo.getCampaignVo().setChannelType(channelType);

            scenario1StepValidator.validate(scenarioVo, errors);
            if(errors.hasErrors()) {
                ModelAndView mav = new ModelAndView("campaign/campaign1Step");  // campaign/campaign_1step_form
                mav.addObject("campaignNo", campaignNo);
                mav.addObject("depthNo", depthNo);
                mav.addObject("channelType", scenarioVo.getChannels()[0]);
                mav.addObject("campaignAbTest", this.campaignAbTest);
                mav.addObject("scenarioVo", scenarioVo);
                return mav;
            }

            //부서코드를 입력하지 않았을 경우 등록자 부서코드 설정
            if(StringUtil.isBlank(scenarioVo.getReqDept())) {
                scenarioVo.setReqDept(scenarioVo.getGrpCd());
            }

            // 세션에서 개인 정보를 넣는다. (신규 입력일 경우 )
            if(scenarioVo.getScenarioNo() == 0) {
                scenarioVo.setUserVo(getLoginUserVo());
            } else { // 수정일 경우 USER_ID, GRP_CD를 NVSCENARIO, NVCAMPAIGN에 모두 넣어 처리
                UserVo userVo = new UserVo();
                userVo.setGrpCd(scenarioVo.getGrpCd());
                userVo.setUserId(scenarioVo.getUserId());
                scenarioVo.setUserVo(userVo);
            }

            // 기본정보를 저장한다.
            scenarioService.setRegistScenario1StepInfo(scenarioVo, campaignNo);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("scenarioNo", String.valueOf(scenarioVo.getScenarioNo()));
            returnData.put("handlerType", scenarioVo.getHandlerType());
            returnData.put("campaignNo", scenarioVo.getCampaignVo().getCampaignNo());
            returnData.put("depthNo", depthNo);
            returnData.put("channelType", StringUtil.defaultIfBlank(channelType, scenarioVo.getCampaignVo().getChannelType()));
            returnData.put("campaignVo.channelType", StringUtil.defaultIfBlank(channelType, scenarioVo.getCampaignVo().getChannelType()));
            returnData.put("webExecMode", scenarioVo.getWebExecMode());

            // 기본정보에서 입력된 Action이 다음 단계 버튼일 경우
            if("nextStep".equalsIgnoreCase(nextStep)) {
                return new ModelAndView(new RedirectView("/campaign/campaign2Step.do"), returnData);  // /campaign/campaign_2step_form.do
            } else { // 기본정보에서 입력된 Action 이 저장일 경우
                return new ModelAndView(new RedirectView("/campaign/campaign1Step.do"), returnData);  // /campaign/campaign_step_form.do
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
