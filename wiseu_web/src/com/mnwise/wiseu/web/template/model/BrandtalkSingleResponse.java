package com.mnwise.wiseu.web.template.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandtalkSingleResponse {
    private String code;
    private Object message;
    private BrandtalkTemplateVo data;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public  Object getMessage() {
        return message;
    }
    public void setMessage( Object message) {
        this.message = message;
    }
    public BrandtalkTemplateVo getData() {
        return data;
    }
    public void setData(BrandtalkTemplateVo data) {
        this.data = data;
    }

}
