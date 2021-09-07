package com.mnwise.wiseu.web.rest.model.ecare;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class CampaignLogRtn{

	private String sendFg;
	private String sendDt;
	private String sendTm;
	private String receiverId;
	private String receriverNm;
	private String receiver;
	private int campaignNo;
	private String resultSeq;
	private String listSeq;
	private String custmerKey;
	private String recordSeq;
	private String errorCd;
	private String errMsg;
	
	
    public String toStringJson() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE, true, false, true , null);
    }
}
