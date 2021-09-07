package com.mnwise.wiseu.web.report.model.campaign;

import java.io.Serializable;

/**
 * 캠페인 리포트 수신확인 Vo
 * 
 * @author hjlee
 */
public class CampaignReportReceiveVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7188385419281273516L;
    private int gid;
    private int scenarioNo;
    private int campaignNo;
    private String scenarioNm;
    private String customerKey;
    private String customerNm;
    private String customerEmail;
    private String durationYn;
    private String duplicationYn;
    private int sendCnt;
    private int successCnt;
    private int openCnt;
    private int durationCnt;
    private int duplicationCnt;
    // 2013-03-26 남기욱 추가
    private int mobileCnt;
    private int depthNo;
    private String channelType;
    // 2013-05-21 김민성 추가
    private String relationType;
    // 2013-06-19 정규필 추가
    private String clientInfo;
    private String client;
    private String device;
    private String openTime;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getScenarioNo() {
        return scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getScenarioNm() {
        return scenarioNm;
    }

    public void setScenarioNm(String scenarioNm) {
        this.scenarioNm = scenarioNm;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(int successCnt) {
        this.successCnt = successCnt;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getDurationCnt() {
        return durationCnt;
    }

    public void setDurationCnt(int durationCnt) {
        this.durationCnt = durationCnt;
    }

    public int getDuplicationCnt() {
        return duplicationCnt;
    }

    public void setDuplicationCnt(int duplicationCnt) {
        this.duplicationCnt = duplicationCnt;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getDurationYn() {
        return durationYn;
    }

    public void setDurationYn(String durationYn) {
        this.durationYn = durationYn;
    }

    public String getDuplicationYn() {
        return duplicationYn;
    }

    public void setDuplicationYn(String duplicationYn) {
        this.duplicationYn = duplicationYn;
    }

    // 2013-03-26 남기욱 추가
    public int getMobileCnt() {
        return mobileCnt;
    }

    public void setMobileCnt(int mobileCnt) {
        this.mobileCnt = mobileCnt;
    }

    public int getDepthNo() {
        return depthNo;
    }

    public void setDepthNo(int depthNo) {
        this.depthNo = depthNo;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    // 2013-05-21 김민성 추가
    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    // 2014-06-19 정규필 추가
    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

}
