package com.mnwise.wiseu.web.report.model.campaign;

import java.util.List;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class ScenarioInfoVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private int scenarioNo;
    private String scenarioNm;
    private String scenarioDesc;
    private int tagNo;
    private CampaignInfoVo campaignInfoVo = new CampaignInfoVo();
    private String[] channels;
    private int[] campaignNoArray;
    private String userId;
    @SuppressWarnings("rawtypes")
    private List campaignList;
    private String reqGrpNm;
    private String tagNm;

    private String sendstartDt;
    private String sendstartTm;

    private String channelType;
    private int campaignNo;
    private String campaignPreface;
    private int targetCnt;
    private int sendCnt;
    private double sendCntPer;
    private int successCnt;
    private double successCntPer;
    private int failCnt;
    private double failCntPer;
    private int openCnt;
    private double openCntPer;
    private int durationCnt;
    private double durationCntPer;
    private int softBounceCnt;
    private double softBounceCntPer;
    private int hardBounceCnt;
    private double hardBounceCntPer;
    private int returnmailCnt;
    private double returnmailCntPer;
    private String logSts;

    public int getScenarioNo() {
        return scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public String getScenarioNm() {
        return scenarioNm;
    }

    public void setScenarioNm(String scenarioNm) {
        this.scenarioNm = scenarioNm;
    }

    public String getScenarioDesc() {
        return scenarioDesc;
    }

    public void setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
    }

    public CampaignInfoVo getCampaignInfoVo() {
        return campaignInfoVo;
    }

    public void setCampaignInfoVo(CampaignInfoVo campaignInfoVo) {
        this.campaignInfoVo = campaignInfoVo;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public int getTagNo() {
        return tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public int[] getCampaignNoArray() {
        return campaignNoArray;
    }

    public void setCampaignNoArray(int[] campaignNoArray) {
        this.campaignNoArray = campaignNoArray;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SuppressWarnings("rawtypes")
    public List getCampaignList() {
        return campaignList;
    }

    @SuppressWarnings("rawtypes")
    public void setCampaignList(List campaignList) {
        this.campaignList = campaignList;
    }

    public String getReqGrpNm() {
        return reqGrpNm;
    }

    public void setReqGrpNm(String reqGrpNm) {
        this.reqGrpNm = reqGrpNm;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getCampaignPreface() {
        return campaignPreface;
    }

    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
    }

    public int getTargetCnt() {
        return targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
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

    public int getSoftBounceCnt() {
        return softBounceCnt;
    }

    public void setSoftBounceCnt(int softBounceCnt) {
        this.softBounceCnt = softBounceCnt;
    }

    public int getHardBounceCnt() {
        return hardBounceCnt;
    }

    public void setHardBounceCnt(int hardBounceCnt) {
        this.hardBounceCnt = hardBounceCnt;
    }

    public int getReturnmailCnt() {
        return returnmailCnt;
    }

    public void setReturnmailCnt(int returnmailCnt) {
        this.returnmailCnt = returnmailCnt;
    }

    public String getLogSts() {
        return logSts;
    }

    public void setLogSts(String logSts) {
        this.logSts = logSts;
    }

    public String getSendstartDt() {
        return sendstartDt;
    }

    public void setSendstartDt(String sendstartDt) {
        this.sendstartDt = sendstartDt;
    }

    public String getSendstartTm() {
        return sendstartTm;
    }

    public void setSendstartTm(String sendstartTm) {
        this.sendstartTm = sendstartTm;
    }

    public double getSendCntPer() {
        return sendCntPer;
    }

    public void setSendCntPer(double sendCntPer) {
        this.sendCntPer = sendCntPer;
    }

    public double getSuccessCntPer() {
        return successCntPer;
    }

    public void setSuccessCntPer(double successCntPer) {
        this.successCntPer = successCntPer;
    }

    public int getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public double getFailCntPer() {
        return failCntPer;
    }

    public void setFailCntPer(double failCntPer) {
        this.failCntPer = failCntPer;
    }

    public double getOpenCntPer() {
        return openCntPer;
    }

    public void setOpenCntPer(double openCntPer) {
        this.openCntPer = openCntPer;
    }

    public double getDurationCntPer() {
        return durationCntPer;
    }

    public void setDurationCntPer(double durationCntPer) {
        this.durationCntPer = durationCntPer;
    }

    public double getSoftBounceCntPer() {
        return softBounceCntPer;
    }

    public void setSoftBounceCntPer(double softBounceCntPer) {
        this.softBounceCntPer = softBounceCntPer;
    }

    public double getHardBounceCntPer() {
        return hardBounceCntPer;
    }

    public void setHardBounceCntPer(double hardBounceCntPer) {
        this.hardBounceCntPer = hardBounceCntPer;
    }

    public double getReturnmailCntPer() {
        return returnmailCntPer;
    }

    public void setReturnmailCntPer(double returnmailCntPer) {
        this.returnmailCntPer = returnmailCntPer;
    }
}
