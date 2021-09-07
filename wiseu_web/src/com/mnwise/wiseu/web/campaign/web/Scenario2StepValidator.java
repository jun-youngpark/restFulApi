package com.mnwise.wiseu.web.campaign.web;

import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;

/**
 * 캠페인 등록 2단계 메세지 작성 Validator
 */
@Component
public class Scenario2StepValidator implements Validator {
    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return ScenarioVo.class.isAssignableFrom(clazz);
    }

    public void validate(Object object, Errors errors) {
        ScenarioVo scenarioVo = (ScenarioVo) object;
        String channelType = scenarioVo.getCampaignVo().getChannelType();
        String senderTel = scenarioVo.getCampaignVo().getSenderTel();
        String failBackSendYn = scenarioVo.getCampaignVo().getFailbackSendYn();
        if(StringUtil.isNotBlank(senderTel)) {
            senderTel = senderTel.replaceAll("[-()]", "");
        }

        if(StringUtil.isWhitespace(scenarioVo.getSegmentNm())) {
            errors.rejectValue("segmentNm", "campaign.error2.msg1");
        }
        if(Channel.MAIL.equals(channelType)  && StringUtil.isWhitespace(scenarioVo.getCampaignVo().getSenderNm())) {
            errors.rejectValue("campaignVo.senderNm", "campaign.error2.msg3");
        }
        if(Channel.MAIL.equals(channelType)) {
            if(StringUtil.isWhitespace(scenarioVo.getCampaignVo().getSenderEmail())) {
                errors.rejectValue("campaignVo.senderEmail", "campaign.error2.msg4");
            }
            if(StringUtil.isWhitespace(scenarioVo.getCampaignVo().getRetmailReceiver())) {
                errors.rejectValue("campaignVo.retmailReceiver", "campaign.error2.msg5");
            }

            // 이메일 유효성 검사
            EmailValidator emailValidator = EmailValidator.getInstance();
            if(!StringUtil.isEmpty(scenarioVo.getCampaignVo().getSenderEmail()) && !emailValidator.isValid(scenarioVo.getCampaignVo().getSenderEmail())) {
                errors.rejectValue("campaignVo.senderEmail", "campaign.error2.msg6");
            }

            if(!StringUtil.isEmpty(scenarioVo.getCampaignVo().getRetmailReceiver()) && !emailValidator.isValid(scenarioVo.getCampaignVo().getRetmailReceiver())) {
                errors.rejectValue("campaignVo.retmailReceiver", "campaign.error2.msg7");
            }
            if(StringUtil.isWhitespace(scenarioVo.getCampaignVo().getCampaignPreface())) {
                errors.rejectValue("campaignVo.campaignPreface", "campaign.error2.msg8");
            }
        } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType) || Channel.FAX.equals(channelType)
            || (Channel.FRIENDTALK.equals(channelType)&& "Y".equalsIgnoreCase(failBackSendYn))) {
            if(StringUtil.isWhitespace(senderTel)) {
                errors.rejectValue("campaignVo.senderTel", "campaign.error2.msg9");
            }
            if(!StringUtil.isNumeric(senderTel)) {
                errors.rejectValue("campaignVo.senderTel", "campaign.error2.msg10");
            }
        }
    }
}
