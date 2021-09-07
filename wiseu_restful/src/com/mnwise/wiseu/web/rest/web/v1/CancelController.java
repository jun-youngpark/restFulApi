package com.mnwise.wiseu.web.rest.web.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mnwise.wiseu.web.rest.model.Groups;
import com.mnwise.wiseu.web.rest.model.ecare.Cancel;
import com.mnwise.wiseu.web.rest.parent.BaseController;
import com.mnwise.wiseu.web.rest.service.CancelService;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

@RestController
@Slf4j
@RequestMapping("/api/v1/cancel/")
public class CancelController extends BaseController {

    @Autowired private CancelService cancelService;

	/**
	 * 준실시간 취소
	 */
    @RequestMapping(value = "/realtimeCancel" , method={RequestMethod.POST} )
    public ResponseEntity<?> realtimeCancel(@RequestBody @Validated(Groups.create.class) final Cancel cancel) {
    	JSONObject data	= null;
    	try {
    		data = cancelService.realtimeCancel(cancel);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e, cancel.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }


	/**
	 * 배치 스케줄 취소
	 */
    @RequestMapping(value = "/batchCancel" , method={RequestMethod.POST} )
    public ResponseEntity<?> batchCancel(@RequestBody @Validated(Groups.create.class) final Cancel cancel) {
    	JSONObject data	= null;
    	try {
    		data = cancelService.batchCancel(cancel);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e, cancel.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }


}