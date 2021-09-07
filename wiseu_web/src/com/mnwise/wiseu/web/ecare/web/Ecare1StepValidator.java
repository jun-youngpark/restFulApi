package com.mnwise.wiseu.web.ecare.web;

import com.mnwise.wiseu.web.ecare.model.EcareVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mnwise.common.util.StringUtil;

/**
 * 이케어 등록 화면 Validator
 */
@Component
public class Ecare1StepValidator implements Validator {
    @Value("${web.exec.mode:1}")
    private String webExecMode;

    @Override
    public boolean supports(Class clazz) {
        return EcareVo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        EcareVo ecareVo = (EcareVo) object;

        if (StringUtil.isBlank(ecareVo.getEcareNm())) {
            errors.rejectValue("ecareNm", "ecare.error1.msg1_" + webExecMode);
        }

        if (ecareVo.getEcareNm().getBytes().length > 100) {
            errors.rejectValue("ecareNm", "ecare.error1.msg4_" + webExecMode);
        }
    }

}
