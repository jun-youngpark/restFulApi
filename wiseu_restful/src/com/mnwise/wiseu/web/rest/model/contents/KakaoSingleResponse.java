package com.mnwise.wiseu.web.rest.model.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoSingleResponse {
    private String code;
    private KakaoTemplateVo data;
    private String message;

    public String getCode() {
        return code;
    }

    public KakaoTemplateVo getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(KakaoTemplateVo data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
