package com.mnwise.wiseu.web.rest.factory;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mnwise.wiseu.web.base.util.PropertyUtil;
import com.mnwise.wiseu.web.rest.annotation.CheckFunction;
import com.mnwise.wiseu.web.rest.annotation.LoggingApi;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.common.RestApiUtils;
import com.mnwise.wiseu.web.rest.exception.NotUseException;
import com.mnwise.wiseu.web.rest.service.CommonService;


@Aspect
@Component
public class AspectContext {

	@Autowired private CommonService commonSerivce;

	/**
     * @CheckFunction 어노테이션에 대한 AOP 설정
     *
     */
	@Pointcut("within(com.mnwise.wiseu.web.rest.web..*)")
	public void CheckFunction() {}

	/**
     * REST API Class 별 ON/OFF 설정 기능
     *
     */
	@Before("com.mnwise.wiseu.web.rest.factory.AspectContext.CheckFunction()")
    public void checkFunction(JoinPoint joinPoint) throws Exception{
		Object target = joinPoint.getTarget();
		CheckFunction checkFunction =
				target.getClass().getAnnotation(CheckFunction.class);
		if(checkFunction == null) throw new Exception("설정파일 on/off 기능 누락");

		if (PropertyUtil.getProperty(checkFunction.prefix(),"OFF").equalsIgnoreCase(Const.OnOff.OFF)) {
			 throw new NotUseException();
		}
    }

	/**
     *  @LoggingApi 선언 된 Method 실행 전 로깅
     *
     */
	@Before("@annotation(com.mnwise.wiseu.web.rest.annotation.LoggingApi)")
    public void LogginApiBefore(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		LoggingApi loggingApi =
				  methodSignature.getMethod().getAnnotation(LoggingApi.class);

		String logSeq = UUID.randomUUID().toString();
		String interfaceId = RestApiUtils.getUri();
		String funcNm = loggingApi.funcNm();
		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 접수", "", "");
    }

	/**
     *  @LoggingApi 선언 된 Method 실행 후 로깅
     *
     */
	@After("@annotation(com.mnwise.wiseu.web.rest.annotation.LoggingApi)")
    public void LogginApiAfter(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		LoggingApi loggingApi =
				  methodSignature.getMethod().getAnnotation(LoggingApi.class);

		String logSeq = UUID.randomUUID().toString();
		String interfaceId = RestApiUtils.getUri();
		String funcNm = loggingApi.funcNm();
		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 종료", "", "");
    }

}
