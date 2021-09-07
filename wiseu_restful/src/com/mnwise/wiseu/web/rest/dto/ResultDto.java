package com.mnwise.wiseu.web.rest.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.simple.JSONObject;


public class ResultDto<T> {
	private String code;
	private String message;
	private JSONObject data;

	public JSONObject getData() {
		return data;
	}
	public ResultDto setData(JSONObject data) {
		this.data = data;
		 return this;
	}
	public String getCode() {
		return code;
	}
	public ResultDto setCode(String code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public ResultDto setMessage(String message) {
		this.message = message;
		return this;
	}
    public String toStringJson() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE, true, false, true , null);
    }
    @Override
    public String toString() {
 	   return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
