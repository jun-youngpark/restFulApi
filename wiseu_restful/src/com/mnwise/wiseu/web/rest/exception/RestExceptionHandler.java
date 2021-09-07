package com.mnwise.wiseu.web.rest.exception;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.common.RestApiUtils;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.dto.ResultDto;
import com.mnwise.wiseu.web.rest.model.custom.CustomRealtimeaccept;
import com.mnwise.wiseu.web.rest.model.ecare.BatchRequest;
import com.mnwise.wiseu.web.rest.service.CommonService;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

	@Autowired private MessageSource messageSource;
	@Autowired private CommonService commonSerivce;

	 public Object getException(String errMsg, JSONObject data) {
		 return new ResultDto<>().setCode(Const.ResultCode.FAIL)
				 .setMessage(errMsg)
				 .setData(data);
	 }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> Exception(HttpServletRequest request, Exception e) throws IOException {
		final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
		ObjectMapper objectMapper = new ObjectMapper();
		final String requestBody = objectMapper.readTree(cachingRequest.getContentAsByteArray()).toString();
		String logSeq = UUID.randomUUID().toString();
		String interfaceId = RestApiUtils.getUri();
		log.error("에러내용:{} 데이터:{}", e, requestBody);
		commonSerivce.regApiLog(logSeq, Const.ApiLogType.EXCEPTION, interfaceId, "처리 중 예외가 발생하였습니다.", requestBody, RestApiUtils.getError(e));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getException(ExceptionUtils.getMessage(e), null));
	}

	@ExceptionHandler(NotUseException.class)
	public ResponseEntity<?> NotUseException(HttpServletRequest request, Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getException("해당 API는 사용 중지 중입니다.",null));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<?> NoHandlerFoundException(HttpServletRequest request, Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getException("Not Found",null));
	}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest webRequest) throws JsonProcessingException{
    	 BindingResult bindingResult = ex.getBindingResult();
         StringBuilder builder = new StringBuilder();
         for (FieldError fieldError : bindingResult.getFieldErrors()) {
             builder.append("[");
             builder.append(fieldError.getField());
             builder.append("](은)는 ");
             builder.append(messageSource.getMessage("vaild."+fieldError.getCodes()[3], null, "ko", null));
             builder.append(" 입력된 값: [");
             builder.append(fieldError.getRejectedValue());
             builder.append("]");
             builder.append(System.lineSeparator());
         }

         JSONObject data = null;
         if(ex.getBindingResult().getTarget() instanceof CustomRealtimeaccept){
 	    	data = new DataMap<String, Object>().put("seq","").build();
         }else if(ex.getBindingResult().getTarget() instanceof BatchRequest){
        	 BatchRequest dto =(BatchRequest) ex.getBindingResult().getTarget();
        	 data = new DataMap<String, Object>().put("batchSeq",dto.getBatchSeq()).build();
         }
        return ResponseEntity.badRequest().body(getException(builder.toString(), data));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleValidationExceptions(HttpMessageNotReadableException ex){
    	JSONObject data = new JSONObject();
    	return ResponseEntity.badRequest().body(getException("[Malformed JSON request] JSON 요청 값 형식 오류입니다."+ex.getMessage(), data));
    }


}
