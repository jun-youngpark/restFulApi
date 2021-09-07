package com.mnwise.wiseu.web.rest.model.custom;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;
import com.mnwise.wiseu.web.rest.model.Groups;
import com.mnwise.wiseu.web.rest.model.ecare.Cancel;

import lombok.Getter;
import lombok.Setter;

/**
 * 고객사별 발송결과 테이블 커스터마이징 필드 추가
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class CustomResend extends BaseVo {
	@NotNull(groups = {Groups.create.class})
	private String seq;
	private String newSeq;

	@NotNull(groups = {Groups.create.class})
	private String ecareNo;

	@NotNull(groups = {Groups.create.class})
	private String receiver;

	@NotNull(groups = {Groups.create.class})
	private String omniReceiver;


}
