package com.mnwise.wiseu.web.template.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponse {
    private String code;
    private KakaoTemplateResult data;
    private String message;

    public String getCode() {
        return code;
    }

    public KakaoTemplateResult getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(KakaoTemplateResult data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
