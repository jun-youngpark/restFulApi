package com.mnwise.wiseu.web.report.model;

import com.mnwise.wiseu.web.base.util.FormatUtil;

/**
 * NVDOMAINLOG 테이블 모델 클래스
 */
public class DomainLogVo {
    private int campaignNo;
    private int resultSeq;
    private String domainNm;
    private String errorCd;
    private int sendCnt;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String errorDesc;
    private String domain;
    private int targetCnt;
    private int successCnt;
    private int failCnt;
    private int hardCnt;
    private int softCnt;
    private String bounce;
    private int totalSoftBounceCnt;
    private int totalHardBounceCnt;
    private double sendRate;
    private double sendShareRate;
    private double targetRate;
    private double successRate;
    private double softRate;
    private double softShareRate;
    private double hardRate;
    private double hardShareRate;

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

    public String getDomainNm() {
        return this.domainNm;
    }

    public void setDomainNm(String domainNm) {
        this.domainNm = domainNm;
    }

    public String getErrorCd() {
        return this.errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public int getSendCnt() {
        return this.sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public double getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(double targetRate) {
        this.targetRate = targetRate;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public double getSoftRate() {
        return softRate;
    }

    public void setSoftRate(double softRate) {
        this.softRate = softRate;
    }

    public double getSoftShareRate() {
        return softShareRate;
    }

    public void setSoftShareRate(double softShareRate) {
        this.softShareRate = softShareRate;
    }

    public double getHardRate() {
        return hardRate;
    }

    public void setHardRate(double hardRate) {
        this.hardRate = hardRate;
    }

    public double getHardShareRate() {
        return hardShareRate;
    }

    public void setHardShareRate(double hardShareRate) {
        this.hardShareRate = hardShareRate;
    }

    public double getSendRate() {
        return sendRate;
    }

    public void setSendRate(double sendRate) {
        this.sendRate = sendRate;
    }

    public double getSendShareRate() {
        return sendShareRate;
    }

    public void setSendShareRate(double sendShareRate) {
        this.sendShareRate = sendShareRate;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getTargetCnt() {
        return targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
    }

    public int getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public int getHardCnt() {
        return hardCnt;
    }

    public void setHardCnt(int hardCnt) {
        this.hardCnt = hardCnt;
    }

    public int getSoftCnt() {
        return softCnt;
    }

    public void setSoftCnt(int softCnt) {
        this.softCnt = softCnt;
    }

    public String getBounce() {
        return bounce;
    }

    public void setBounce(String bounce) {
        this.bounce = bounce;
    }

    public int getTotalSoftBounceCnt() {
        return totalSoftBounceCnt;
    }

    public void setTotalSoftBounceCnt(int totalSoftBounceCnt) {
        this.totalSoftBounceCnt = totalSoftBounceCnt;
    }

    public int getTotalHardBounceCnt() {
        return totalHardBounceCnt;
    }

    public void setTotalHardBounceCnt(int totalHardBounceCnt) {
        this.totalHardBounceCnt = totalHardBounceCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(int successCnt) {
        this.successCnt = successCnt;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public double getHardTargetCntToPercent() {
        return FormatUtil.toNumPercent(targetCnt, hardCnt, 1);
    }

    // 전체요약 softbounce 그래프위한 추가
    public double getSoftTargetCntToPercentAll() {
        return FormatUtil.toNumPercent(targetCnt, softCnt, 1);
    }

    // 요약분석 softbounce 그래프위한 변경
    public double getSoftTargetCntToPercent() {
        return FormatUtil.toNumPercent(targetCnt, softCnt, 1);
    }
    /* public double getSoftTargetCntToPercent() { return FormatUtil.toNumPercent(softCnt, targetCnt, 1); } */

    public String getDomainNmToStr() {
        if("ZZZ.DOMAIN".equals(domainNm)) {
            return "기타";
        } else {
            return domainNm;
        }
    }
}
