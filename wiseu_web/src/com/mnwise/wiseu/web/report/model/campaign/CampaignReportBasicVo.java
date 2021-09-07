package com.mnwise.wiseu.web.report.model.campaign;

import java.io.Serializable;

/**
 * 캠페인 기본정보 Vo
 * 
 * @author hjlee
 */
public class CampaignReportBasicVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4535980101777687710L;
    private int scenarioNo;
    private String scenarioNm;
    private int campaignNo;
    private int depthNo;
    private String userId;
    private String grpCd;
    private String campaignPreface;
    private String campaignSts;
    private String campaignStsNm;
    private String relationType;
    private String relationTypeNm;
    private String channelType;
    private String sendstartDt;
    private String sendstartTm;
    private String reportSts;
    private String startDt;
    private String startTm;
    private String endDt;
    private String endTm;
    private String nameKor;
    private String grpNm;
    private int targetCnt;
    private int sendCnt;
    private int successCnt;
    private int failCnt;
    private int linkCnt;
    private String sendEndDt;
    private String sendEndTm;
    private String sendDurationTm;
    private int rejectCnt;
    private String resultSeq;

    private String campaignPrefaceAb;
    private String abTestType;
    private String abTestCond;
    private int abTestTarget;

    public int getAbTestTarget() {
        return abTestTarget;
    }

    public void setAbTestTarget(int abTestTarget) {
        this.abTestTarget = abTestTarget;
    }

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

    public String getCampaignPrefaceAb() {
        return campaignPrefaceAb;
    }

    public void setCampaignPrefaceAb(String campaignPrefaceAb) {
        this.campaignPrefaceAb = campaignPrefaceAb;
    }

    public int getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public String getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
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

    public int getDepthNo() {
        return depthNo;
    }

    public void setDepthNo(int depthNo) {
        this.depthNo = depthNo;
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

    public String getCampaignPreface() {
        return campaignPreface;
    }

    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
    }

    public String getCampaignSts() {
        return campaignSts;
    }

    public void setCampaignSts(String campaignSts) {
        this.campaignSts = campaignSts;
    }

    public String getCampaignStsNm() {
        return campaignStsNm;
    }

    public void setCampaignStsNm(String campaignStsNm) {
        this.campaignStsNm = campaignStsNm;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationTypeNm() {
        return relationTypeNm;
    }

    public void setRelationTypeNm(String relationTypeNm) {
        this.relationTypeNm = relationTypeNm;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public String getReportSts() {
        return reportSts;
    }

    public void setReportSts(String reportSts) {
        this.reportSts = reportSts;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getStartTm() {
        return startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getEndTm() {
        return endTm;
    }

    public void setEndTm(String endTm) {
        this.endTm = endTm;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
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

    public String getSendEndDt() {
        return sendEndDt;
    }

    public void setSendEndDt(String sendEndDt) {
        this.sendEndDt = sendEndDt;
    }

    public String getSendEndTm() {
        return sendEndTm;
    }

    public void setSendEndTm(String sendEndTm) {
        this.sendEndTm = sendEndTm;
    }

    public String getSendDurationTm() {
        return sendDurationTm;
    }

    public void setSendDurationTm(String sendDurationTm) {
        this.sendDurationTm = sendDurationTm;
    }

    public int getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(int linkCnt) {
        this.linkCnt = linkCnt;
    }

    public int getRejectCnt() {
        return rejectCnt;
    }

    public void setRejectCnt(int rejectCnt) {
        this.rejectCnt = rejectCnt;
    }

}
