package com.mnwise.wiseu.web.rest.model.ecare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnwise.wiseu.web.rest.model.campaign.Campaign;

import lombok.Getter;
import lombok.Setter;

/**
 * 캠페인발송 결과 리스트 Vo
 */
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignLog extends Campaign{

	private String receiver;
	private String receiverId;


}

