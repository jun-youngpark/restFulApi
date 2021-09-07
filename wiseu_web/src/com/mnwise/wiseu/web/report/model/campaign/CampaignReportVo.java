package com.mnwise.wiseu.web.report.model.campaign;

import java.io.Serializable;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * 캠페인 리포트 Vo
 */
public class CampaignReportVo extends SearchVo implements Serializable {
    private static final long serialVersionUID = -1L;
    private int gid;
    private String userId;
    private String grpCd;
    private String usertypeCd;
    private int scenarioNo;
    private String scenarioNm;
    private int campaignNo;
    private String channelType;
    private String relationType;
    private int depthNo;
    private String sendstartDt;
    private String sendstartTm;
    private String campaignPreface;
    private String logSts;
    private int targetCnt;
    private int domainSendCnt;
    private int sendCnt;
    private int successCnt;
    private int openCnt;
    private int durationCnt;
    private int duplicationCnt;
    private int softBounceCnt;
    private int hardBounceCnt;
    private int returnmailCnt;
    private int sendCntPer;
    private int successCntPer;
    private int failCnt;
    private double failCntPer;
    private double openCntPer;
    private double durationCntPer;
    private double softBounceCntPer;
    private double hardBounceCntPer;
    private double returnmailCntPer;
    private String lang;

    private String domainNm;
    private String errorCd;
    private String errorMsg;
    private String resultSeq;
    private String resendSts;

    private String abTestType;
    private String abTestCond;

    private String searchChannel;
    private int mobileCnt;

    public String getAbTestCond() {
        return abTestCond;
    }

    public void setAbTestCond(String abTestCond) {
        this.abTestCond = abTestCond;
    }

    public String getAbTestType() {
        return abTestType;
    }

    public void setAbTestType(String abTestType) {
        this.abTestType = abTestType;
    }

    public String getResendSts() {
        return resendSts;
    }

    public void setResendSts(String resendSts) {
        this.resendSts = resendSts;
    }

    public String getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getUsertypeCd() {
        return usertypeCd;
    }

    public void setUsertypeCd(String usertypeCd) {
        this.usertypeCd = usertypeCd;
    }

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

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public int getDepthNo() {
        return depthNo;
    }

    public void setDepthNo(int depthNo) {
        this.depthNo = depthNo;
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

    public String getCampaignPreface() {
        return campaignPreface;
    }

    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
    }

    public String getLogSts() {
        return logSts;
    }

    public void setLogSts(String logSts) {
        this.logSts = logSts;
    }

    public int getTargetCnt() {
        return targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
    }

    public int getDomainSendCnt() {
        return domainSendCnt;
    }

    public void setDomainSendCnt(int domainSendCnt) {
        this.domainSendCnt = domainSendCnt;
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

    public int getSendCntPer() {
        return sendCntPer;
    }

    public void setSendCntPer(int sendCntPer) {
        this.sendCntPer = sendCntPer;
    }

    public int getSuccessCntPer() {
        return successCntPer;
    }

    public void setSuccessCntPer(int successCntPer) {
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

    public String getDomainNm() {
        return domainNm;
    }

    public void setDomainNm(String domainNm) {
        this.domainNm = domainNm;
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSearchChannel() {
        return searchChannel;
    }

    public void setSearchChannel(String searchChannel) {
        this.searchChannel = searchChannel;
    }

    public int getMobileCnt() {
        return mobileCnt;
    }

    public void setMobileCnt(int mobileCnt) {
        this.mobileCnt = mobileCnt;
    }


}
