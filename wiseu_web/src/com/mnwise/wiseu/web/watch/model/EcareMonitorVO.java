package com.mnwise.wiseu.web.watch.model;

import java.io.Serializable;

/**
 * wiseWatch 실시간 모니터링 객체
 * 
 * @author Wee
 * @since 2009-06-18
 */
public class EcareMonitorVO implements Serializable {

    private static final long serialVersionUID = -3121968524594214710L;

    private String userId;
    private int ecareNo;
    private String svcId;
    private String ecareNm;
    private String serviceType;
    private String channelType;
    private String sendstartDt;
    private String sendendDt;
    private String daySendCnt;
    private String daySendSuccessCnt;
    private String daySendFailCnt;
    private String hourSendCnt;

    public int getEcareNo() {
        return ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getSvcId() {
        return svcId;
    }

    public void setEcareNm(String ecareNm) {
        this.ecareNm = ecareNm;
    }

    public String getEcareNm() {
        return ecareNm;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setSendstartDt(String sendstartDt) {
        this.sendstartDt = sendstartDt;
    }

    public String getSendstartDt() {
        return sendstartDt;
    }

    public void setSendendDt(String sendendDt) {
        this.sendendDt = sendendDt;
    }

    public String getSendendDt() {
        return sendendDt;
    }

    public void setDaySendCnt(String daySendCnt) {
        this.daySendCnt = daySendCnt;
    }

    public String getDaySendCnt() {
        return daySendCnt;
    }

    public void setDaySendSuccessCnt(String daySendSuccessCnt) {
        this.daySendSuccessCnt = daySendSuccessCnt;
    }

    public String getDaySendSuccessCnt() {
        return daySendSuccessCnt;
    }

    public void setDaySendFailCnt(String daySendFailCnt) {
        this.daySendFailCnt = daySendFailCnt;
    }

    public String getDaySendFailCnt() {
        return daySendFailCnt;
    }

    public void setHourSendCnt(String hourSendCnt) {
        this.hourSendCnt = hourSendCnt;
    }

    public String getHourSendCnt() {
        return hourSendCnt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}