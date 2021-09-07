package com.mnwise.wiseu.web.rest.model.campaign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 캠페인 시나리오 테이블 Vo
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario extends BaseVo{
	private int scenarioNo;
	private String userId;
	private String grpCd;
	private String scenarioNm;
	private String scenarioDesc;
	private String scenarioType;
	private String createDt;
	private String lastupdateDt;
	private String finishYn;
	private String finishDt;
	private int tagNo;
	private String createTm;
	private String lastupdateTm;
	private String handlerType;

	public int getTagNo() {
		return tagNo == 0 ? 1 : tagNo;
	}

}
