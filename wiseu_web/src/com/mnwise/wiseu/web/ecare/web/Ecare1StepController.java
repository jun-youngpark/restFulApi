package com.mnwise.wiseu.web.ecare.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.service.EcareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;

/**
 * 이케어 등록 화면 프로그램
 */
@Controller
public class Ecare1StepController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Ecare1StepController.class);

    @Autowired private Ecare1StepValidator ecare1StepValidator;
    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private EcareService ecareService;

    /**
     * 이케어 등록 화면 출력
     */
    @GetMapping(value = "/ecare/ecare1Step.do")
    public ModelAndView view() {
        return new ModelAndView("/ecare/ecare1Step", getModelFor1Step(ecareService.getEcareVoWithDefaultValue()));
    }

    private Map<String, Object> getModelFor1Step(EcareVo ecareVo) {
        Map<String, Object> model = new HashMap<>();
        model.put("channelList", super.ecareChannelUseList);
        model.put("ecareVo", ecareVo);
        model.put("mailTypeList", ecareService.getMailTypeList());
        model.put("subTypeList", ecareService.getSubTypeList());
        model.put("usePdfMail", super.usePdfMail.equals("on"));
        model.put("useSecurityMail", super.useSecurityMail.equals("on"));
        model.put("webExecMode", super.webExecMode);

        return model;
    }

    /**
     * 이케어 생성 후 채널별 작성 화면으로 이동
     *
     * @param ecareVo 이케어 정보
     * @param errors 유효성 체크 에러 정보
     * @throws Exception
     */
    @PostMapping(value = "/ecare/ecare1Step.do")
    public ModelAndView create(EcareVo ecareVo, BindingResult errors) throws SQLException {
        ecare1StepValidator.validate(ecareVo, errors);

        if (errors.hasErrors()) {
            return new ModelAndView("/ecare/ecare1Step", getModelFor1Step(ecareService.getEcareVoWithDefaultValue()));
        }

        EcareScenarioVo ecareScenarioVo = ecareScenarioService.createEcareScenario(ecareVo, getLoginUserVo());
        ecareService.createEcare(ecareVo, ecareScenarioVo);

        return new ModelAndView(new RedirectView("/ecare/ecare2Step.do"), getModelFor2Step(ecareVo));
    }

    private Map<String, Object> getModelFor2Step(EcareVo ecareVo) {
        Map<String, Object> model = new HashMap<>();
        model.put("scenarioNo", String.valueOf(ecareVo.getScenarioNo()));
        model.put("ecareVo.ecareNo", String.valueOf(ecareVo.getEcareNo()));
        model.put("ecareVo.channelType", ecareVo.getChannelType());

        return model;
    }
}
