package com.mnwise.wiseu.web.rest.model.ecare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class EcareSendlLog extends BaseVo {
	
	protected int ecareNo;	//이케어 번호
	protected String resultSeq;
	protected String listSeq;
	protected String customerKey;
	protected String recordSeq;
	protected String sid;
	protected String customerNm;
	protected String customerEmail;
	protected String sendDt;
	protected String sendTm;
	protected String endDt;
	protected String endTm;
	protected String errorCd;
	protected String sendDomain;
	protected String errMsg;
	protected String resendYn;
	protected String reqDeptId;
	protected String reqUserId;
	protected String seq;
	protected String openDt;
	protected String subEcareNo;
	protected String subResultSeq;
	protected String partMessage;
	protected String failBackChannel;
	protected String failBackResultCd;
	protected String failBackSendDtm;
	protected String tmplCd;
	protected String slot1; //여분 필드1
	protected String slot2; //여분 필드2
	protected String slot3; //여분 필드3
	protected String slot4; //여분 필드4
	protected String slot5; //여분 필드5
	protected String slot6; //여분 필드6
	protected String slot7; //여분 필드7
	protected String slot8; //여분 필드8
	protected String slot9; //여분 필드9
	protected String slot10; //여분 필드10
 	
}
