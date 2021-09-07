package com.mnwise.wiseu.web.env.model;

/**
 * NVSENDBLOCKDATE 테이블 모델 클래스
 */
public class SendBlockDateVo {
    private String chkYear;
    private String blockDt;
    private String channelType;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String regYear;
    private String regSaturdayYn;
    private String regSundayYn;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getChkYear() {
        return this.chkYear;
    }

    public void setChkYear(String chkYear) {
        this.chkYear = chkYear;
    }

    public String getBlockDt() {
        return this.blockDt;
    }

    public void setBlockDt(String blockDt) {
        this.blockDt = blockDt;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getRegYear() {
        return regYear;
    }

    public void setRegYear(String regYear) {
        this.regYear = regYear;
    }

    public String getRegSaturdayYn() {
        return regSaturdayYn;
    }

    public void setRegSaturdayYn(String regSaturdayYn) {
        this.regSaturdayYn = regSaturdayYn;
    }

    public String getRegSundayYn() {
        return regSundayYn;
    }

    public void setRegSundayYn(String regSundayYn) {
        this.regSundayYn = regSundayYn;
    }

}
