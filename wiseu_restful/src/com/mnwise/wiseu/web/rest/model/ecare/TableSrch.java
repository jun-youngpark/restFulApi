package com.mnwise.wiseu.web.rest.model.ecare;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class TableSrch{
	
	@NotNull private int ecareNo;	//이케어 번호
	@NotNull private String channel;
	@NotNull private String reqDt;
	@NotNull private String reqTm;
	@NotNull private int nowPage;
	@NotNull private int limit=10;
	private String reqUserId;
	private String reqDeptId;
	private String receiverId;
	private String receiverNm;
	private String receiver;
	private String seq;

	
	
    public String toStringJson() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE, true, false, true , null);
    }
}
