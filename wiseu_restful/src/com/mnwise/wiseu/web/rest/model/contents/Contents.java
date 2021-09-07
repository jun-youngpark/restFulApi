package com.mnwise.wiseu.web.rest.model.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class Contents extends BaseVo {
	private String contsNo;
	private String grpCd;
	private String categoryCd;
	private String userId;
	private String contsNm;
	private String contsDesc;
	private String fileUrlName;
	private String fileType;
	private String fileName;
	private String createDt;
	private String createTm;
	private String authType;
	private String tagNo;
}
