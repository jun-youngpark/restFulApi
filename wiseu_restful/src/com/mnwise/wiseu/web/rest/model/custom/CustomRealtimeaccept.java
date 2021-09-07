package com.mnwise.wiseu.web.rest.model.custom;

import javax.validation.constraints.NotNull;

import com.mnwise.wiseu.web.rest.model.Groups;
import com.mnwise.wiseu.web.rest.model.ecare.Realtimeaccept;

import lombok.Getter;
import lombok.Setter;

/**
 * 고객사별 준실시간 테이블 커스터마이징 필드 추가
 */
@Getter @Setter
public class CustomRealtimeaccept extends Realtimeaccept  {
/*
	@NotBlank
	private String reqSeq;
	private JSONArray filePathList;
*/
	@NotNull(groups = {Groups.create.class})
	private String omniReceiver;
}
