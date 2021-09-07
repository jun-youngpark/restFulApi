package com.mnwise.wiseu.web.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.mnwise.wiseu.web.rest.factory.CodeValidator;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CodeValidator.class)
public @interface Code  {	/** * 특정code 허용체크 */
    String message() default "invalid.";
	String[] codes() default {"Y"};
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
