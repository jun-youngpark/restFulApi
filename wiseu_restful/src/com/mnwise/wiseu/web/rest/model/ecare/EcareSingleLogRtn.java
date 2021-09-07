package com.mnwise.wiseu.web.rest.model.ecare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 이케어발송 결과 리스트 Vo
 */
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcareSingleLogRtn extends BaseVo{



	private String sendDt;
	private String sendTm;
	private String reqUserId;
	private String reqDeptId;
	private String resultCode;
	private String resultMsg;
	private String openDate;
	private String template;
	private String cover;
	
	private int ecareNo;
	private String receiver;
	private String receiverId;
	private String channel;
	private String receriverNm;
	
	private String seq;
	
}

