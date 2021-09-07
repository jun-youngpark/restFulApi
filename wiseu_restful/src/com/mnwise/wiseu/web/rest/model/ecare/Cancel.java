package com.mnwise.wiseu.web.rest.model.ecare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 발송취소 테이블 Vo
 */
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cancel extends BaseVo{
	private String canReqDtm;
	private String canSeq;
	private String orgSeq;
	private String orgReqDt;
	private String seq;
	private String ecareNo;
	private String canFg;
	private String canDtm;
	private String acceptDtm;
	private String rsltCd;
	private String rsltMsg;
	private String divideYn;
	private String divideInterval;
	private String divideCnt;
	private String grpCd;


}

