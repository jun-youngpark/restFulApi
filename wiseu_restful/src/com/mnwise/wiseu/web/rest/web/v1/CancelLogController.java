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
@RequestMapping("/api/v1/cancelResult/")
public class CancelLogController extends BaseController {

    @Autowired private CancelService cancelService;

	/**
	 * @prarm seq, ecareNo
	 * 취소 결과 단건 조회
	 */
    @RequestMapping(value = "/singleView" , method={RequestMethod.POST} )
    public ResponseEntity<?> singleCancelLog(@RequestBody @Validated(Groups.create.class) final Cancel cancel) {
    	JSONObject data	= null;
    	try {
    		data = cancelService.selectSingleCancelLog(cancel);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e, cancel.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }


	/**
	 * @prarm 
	 * 취소 결과 리스트 조회
	 */
    @RequestMapping(value = "/listView" , method={RequestMethod.POST} )
    public ResponseEntity<?> listCancelLog(@RequestBody @Validated(Groups.list.class) final Cancel cancel) {
    	JSONObject data	= null;
    	try {
    		data = cancelService.selectListCancelLog(cancel);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e, cancel.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }


}