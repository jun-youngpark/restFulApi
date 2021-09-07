package com.mnwise.wiseu.web.campaign.model;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

/**
 * NVSENDRESULT 테이블 모델 클래스
 */
public class SendResultVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int campaignNo;
    private int resultSeq;
    private String resultDesc;
    private String resultSts;
    private String startDt;
    private String startTm;
    private String endDt;
    private String endTm;
    private int targetCnt;
    private int sendCnt;
    private int successCnt;
    private int failCnt;
    private int unknownUserCnt;
    private int unknownHostCnt;
    private int smtpExceptCnt;
    private int noRouteCnt;
    private int refusedCnt;
    private int etcExceptCnt;
    private int invalidAddressCnt;
    private String logSts;
    private String domainSts;
    private int returnMailCnt;
    private int openCnt;
    private int durationCnt;
    private int linkCnt;
    private String manualbatchSts;
    private String resendSts;
    private int superSeq;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String reportDt;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getCampaignNo() {
        return this.campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public int getResultSeq() {
        return this.resultSeq;
    }

    public void setResultSeq(int resultSeq) {
        this.resultSeq = resultSeq;
    }

    public String getResultDesc() {
        return this.resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultSts() {
        return this.resultSts;
    }

    public void setResultSts(String resultSts) {
        this.resultSts = resultSts;
    }

    public String getStartDt() {
        return this.startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getStartTm() {
        return this.startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public String getEndDt() {
        return this.endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getEndTm() {
        return this.endTm;
    }

    public void setEndTm(String endTm) {
        this.endTm = endTm;
    }

    public int getTargetCnt() {
        return this.targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
    }

    public int getSendCnt() {
        return (this.sendCnt == 0) ? this.successCnt + this.failCnt : this.sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getSuccessCnt() {
        return this.successCnt;
    }

    public void setSuccessCnt(int successCnt) {
        this.successCnt = successCnt;
    }

    public int getFailCnt() {
        return this.failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public int getUnknownUserCnt() {
        return this.unknownUserCnt;
    }

    public void setUnknownUserCnt(int unknownUserCnt) {
        this.unknownUserCnt = unknownUserCnt;
    }

    public int getUnknownHostCnt() {
        return this.unknownHostCnt;
    }

    public void setUnknownHostCnt(int unknownHostCnt) {
        this.unknownHostCnt = unknownHostCnt;
    }

    public int getSmtpExceptCnt() {
        return this.smtpExceptCnt;
    }

    public void setSmtpExceptCnt(int smtpExceptCnt) {
        this.smtpExceptCnt = smtpExceptCnt;
    }

    public int getNoRouteCnt() {
        return this.noRouteCnt;
    }

    public void setNoRouteCnt(int noRouteCnt) {
        this.noRouteCnt = noRouteCnt;
    }

    public int getRefusedCnt() {
        return this.refusedCnt;
    }

    public void setRefusedCnt(int refusedCnt) {
        this.refusedCnt = refusedCnt;
    }

    public int getEtcExceptCnt() {
        return this.etcExceptCnt;
    }

    public void setEtcExceptCnt(int etcExceptCnt) {
        this.etcExceptCnt = etcExceptCnt;
    }

    public int getInvalidAddressCnt() {
        return this.invalidAddressCnt;
    }

    public void setInvalidAddressCnt(int invalidAddressCnt) {
        this.invalidAddressCnt = invalidAddressCnt;
    }

    public String getLogSts() {
        return this.logSts;
    }

    public void setLogSts(String logSts) {
        this.logSts = logSts;
    }

    public String getDomainSts() {
        return this.domainSts;
    }

    public void setDomainSts(String domainSts) {
        this.domainSts = domainSts;
    }

    public int getReturnMailCnt() {
        return this.returnMailCnt;
    }

    public void setReturnMailCnt(int returnMailCnt) {
        this.returnMailCnt = returnMailCnt;
    }

    public int getOpenCnt() {
        return this.openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getDurationCnt() {
        return this.durationCnt;
    }

    public void setDurationCnt(int durationCnt) {
        this.durationCnt = durationCnt;
    }

    public int getLinkCnt() {
        return this.linkCnt;
    }

    public void setLinkCnt(int linkCnt) {
        this.linkCnt = linkCnt;
    }

    public String getManualbatchSts() {
        return this.manualbatchSts;
    }

    public void setManualbatchSts(String manualbatchSts) {
        this.manualbatchSts = manualbatchSts;
    }

    public String getResendSts() {
        return this.resendSts;
    }

    public void setResendSts(String resendSts) {
        this.resendSts = resendSts;
    }

    public int getSuperSeq() {
        return this.superSeq;
    }

    public void setSuperSeq(int superSeq) {
        this.superSeq = superSeq;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getReportDt() {
        return reportDt;
    }

    public void setReportDt(String reportDt) {
        this.reportDt = reportDt;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public double getSendCntToPercent() {
        return FormatUtil.toNumPercent(sendCnt, sendCnt, 1);
    }

    public double getSuccessCntToNumber() {
        return FormatUtil.toNumPercent(successCnt, getSendCnt(), 1);
    }

    public double getSuccessCntToPercent() {
        return FormatUtil.toNumPercent(successCnt, getSendCnt(), 1);
    }

    public double getFailCntToPercent() {
        return FormatUtil.toNumPercent(failCnt, getSendCnt(), 1);
    }

    public double getOpenCntToPercent() {
        return FormatUtil.toNumPercent(openCnt, successCnt, 1);
    }

    public double getDurationCntToPercent() {
        return FormatUtil.toNumPercent(durationCnt, successCnt, 1);
    }

    public double getLinkCntToPercent() {
        return FormatUtil.toNumPercent(linkCnt, successCnt, 1);
    }

    public double getReturnMailCntToPercent() {
        return FormatUtil.toNumPercent(returnMailCnt, successCnt, 1);
    }
/*
    public double getSoftBounceCntToPercent() {
        return FormatUtil.toNumPercent(softBounceCnt, sendCnt, 1);
    }

    public double getSoftBounceCntToNumber() {
        return FormatUtil.toNumPercent(softBounceCnt, sendCnt, 1);
    }

    public double getHardBounceCntToPercent() {
        return FormatUtil.toNumPercent(hardBounceCnt, sendCnt, 1);
    }

    public double getHardBounceCntToNumber() {
        return FormatUtil.toNumPercent(hardBounceCnt, sendCnt, 1);
    }

    public double getBounceCntToPercent() {
        return FormatUtil.toNumPercent(hardBounceCnt + softBounceCnt, sendCnt, 1);
    }*/
}
