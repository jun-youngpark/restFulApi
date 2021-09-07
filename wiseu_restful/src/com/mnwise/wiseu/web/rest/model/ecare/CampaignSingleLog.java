package com.mnwise.wiseu.web.rest.model.ecare;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * 캠페인 발송 결과 리스트 단건 조회 Vo
 */
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignSingleLog {

	// Input
	private int campaignNo;
	private String resultSeq;
	private String listSeq;
	private String custmerKey;
	private String recordSeq;

	// output
	private String channel;
	private String sendDt;
	private String sendTm;
	private String reqUserId;
	private String reqDeptId;
	private String receiverId;
	private String receriverNm;
	private String receiver;
	private String seq;
	private String resultCd;
	private String resultMsg;
	private String openDate;
	
	public String toStringJson() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE, true, false, true , null);
    }
}