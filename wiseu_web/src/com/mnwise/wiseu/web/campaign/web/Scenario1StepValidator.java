package com.mnwise.wiseu.web.campaign.web;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;

/**
 * 캠페인 등록 1단계 캠페인 기본정보 Validator
 */
@Component
public class Scenario1StepValidator implements Validator {
    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return ScenarioVo.class.isAssignableFrom(clazz);
    }

    /**
     * Validatio 체크
     */
    public void validate(Object object, Errors errors) {
        ScenarioVo scenarioVo = (ScenarioVo) object;

        // 캠페인 태그 입력 여부 검사
        if(StringUtil.isNotEmpty(scenarioVo.getTagNm()) && scenarioVo.getTagNm().getBytes().length > 50) {
            errors.rejectValue("tagNm", "campaign.error1.msg6");
        }

        // 캠페인 명 입력 여부 검사
        if(StringUtil.isWhitespace(scenarioVo.getScenarioNm())) {
            errors.rejectValue("scenarioNm", "campaign.error1.msg1");
        }

        if(scenarioVo.getScenarioNm().getBytes().length > 100) {
            errors.rejectValue("scenarioNm", "campaign.error1.msg4");
        }

        if(StringUtil.isNotEmpty(scenarioVo.getScenarioDesc()) && scenarioVo.getScenarioDesc().getBytes().length > 250) {
            errors.rejectValue("scenarioDesc", "campaign.error1.msg5");
        }

        // 채널 타입이 하나 이상 선택되어 있는 지 여부를 검사
        // if (scenarioVo.getChannels().length == 0)
        // 복수 채널을 선택하는 로직이 빠져서 아래와 같이 체크한다.
        if(scenarioVo.getChannels() == null)
            errors.rejectValue("channels", "campaign.error1.msg2");

        // 핸들러 타입 입력 여부 검사
        if(StringUtil.isWhitespace(scenarioVo.getHandlerType())) {
            errors.rejectValue("handlerType", "campaign.error1.msg3");
        }

        String selChannel[] = scenarioVo.getChannels();

        // 부서별 집계 사용시에만
        if(StringUtil.equals(scenarioVo.getSmsIndividualCharge(), "on")) {
            // 채널이 sms이거나 mms/lms 인경우에만 필수 입력 체크
            if(selChannel[0].equals(Channel.LMSMMS) || selChannel[0].equals(Channel.SMS)) {
                // 요청부서 입력 여부 검사
                if(StringUtil.isWhitespace(scenarioVo.getReqDept())) {
                    errors.rejectValue("reqDept", "campaign.error1.msg7");
                }

                // 요청자 아이디 입력 여부 검사
                if(StringUtil.isWhitespace(scenarioVo.getReqUser())) {
                    errors.rejectValue("reqUser", "campaign.error1.msg8");
                }
            }
        }
    }
}
