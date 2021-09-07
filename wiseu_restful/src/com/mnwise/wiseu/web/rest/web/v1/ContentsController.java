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
import com.mnwise.wiseu.web.rest.model.custom.CustomContents;
import com.mnwise.wiseu.web.rest.parent.BaseController;
import com.mnwise.wiseu.web.rest.service.Contents.ContentsService;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

@RestController
@Slf4j
@RequestMapping("/api/v1/tempalte/")
public class ContentsController extends BaseController {

	@Autowired
	private ContentsService contentsService;

	/**
	 * 템플릿 생성
	 */
    @RequestMapping(value = "/create" , method={RequestMethod.POST} )
    public ResponseEntity<?> create(@RequestBody @Validated(Groups.create.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.insertTemplate(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }
	/**
	 * 템플릿 수정
	 */
    @RequestMapping(value = "/update" , method={RequestMethod.POST} )
    public ResponseEntity<?> update(@RequestBody @Validated(Groups.update.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.updateTemplate(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }
	/**
	 * 템플릿 삭제
	 */
    @RequestMapping(value = "/delete" , method={RequestMethod.POST} )
    public ResponseEntity<?> delete(@RequestBody @Validated(Groups.update.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.deleteTemplate(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }
	/**
	 * 템플릿 조회
	 */
    @RequestMapping(value = "/list" , method={RequestMethod.POST} )
    public ResponseEntity<?> list(@RequestBody @Validated(Groups.update.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.list(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }
	/**
	 * 템플릿 건수
	 */
    @RequestMapping(value = "/count" , method={RequestMethod.POST} )
    public ResponseEntity<?> count(@RequestBody @Validated(Groups.update.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.selectTemplateCount(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }

	/**
	 * 템플릿 검수
	 */
    @RequestMapping(value = "/inspect" , method={RequestMethod.POST} )
    public ResponseEntity<?> inspect(@RequestBody @Validated(Groups.update.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.inspectAlimtalkTemplate(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }

	/**
	 * 템플릿 검수 결과
	 */
    @RequestMapping(value = "/inspectResult" , method={RequestMethod.POST} )
    public ResponseEntity<?> inspectResult(@RequestBody @Validated(Groups.update.class) final CustomContents customContents) {
    	JSONObject data	= null;
    	try {
    		data = contentsService.selectAlimtalkInspect(customContents);
    		return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e) {
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), customContents.toStringJson());
    		return new ResponseEntity<>(getFailResult(e.getMessage(), data),HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    }
}
