package com.mnwise.wiseu.web.rest.factory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.ArrayUtils;

import com.mnwise.wiseu.web.rest.annotation.Code;

public class CodeValidator implements ConstraintValidator<Code, String>{

	private static String[] ALLOW_ARRAY;
	@Override
	public void initialize(Code constraintAnnotation){
		this.ALLOW_ARRAY = constraintAnnotation.codes(); // 허용할 코드 설정
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context){
		if (value == null) {
			return false;
		}
		if (ArrayUtils.contains(ALLOW_ARRAY, value)){
			return true;
		}
		return false;
	}

}
