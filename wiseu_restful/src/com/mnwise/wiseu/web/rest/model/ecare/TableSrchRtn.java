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
public class TableSrchRtn{

	private String seq;
	private String sendFg;
	private String reqDt;
	private String reqTm;
	private String errorCd;
	private String errMsg;
	
	
    public String toStringJson() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE, true, false, true , null);
    }
}
