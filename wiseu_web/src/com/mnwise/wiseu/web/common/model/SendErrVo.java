package com.mnwise.wiseu.web.common.model;

/**
 * NVSENDERR 테이블 모델 클래스
 */
public class SendErrVo {
    private String errorCd;
    private String errorNm;
    private String errorDesc;
    private String errorSmsDesc;
    private String categoryCd;
    private String channelType;
    private String spamYn;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getErrorCd() {
        return this.errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrorNm() {
        return this.errorNm;
    }

    public void setErrorNm(String errorNm) {
        this.errorNm = errorNm;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorSmsDesc() {
        return this.errorSmsDesc;
    }

    public void setErrorSmsDesc(String errorSmsDesc) {
        this.errorSmsDesc = errorSmsDesc;
    }

    public String getCategoryCd() {
        return this.categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getSpamYn() {
        return this.spamYn;
    }

    public void setSpamYn(String spamYn) {
        this.spamYn = spamYn;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getChannel() {
        return this.channelType;
    }
}
