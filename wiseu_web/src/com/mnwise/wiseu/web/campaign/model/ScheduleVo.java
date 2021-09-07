package com.mnwise.wiseu.web.campaign.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVSCHEDULE 테이블 모델 클래스
 */
public class ScheduleVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int scenarioNo;
    private int scheduleSeq;
    private int campaignNo;
    private int supraCampaignNo;
    private String campaignDt;
    private int positionX;
    private int positionY;
    private int sizewidth;
    private int sizeheight;
    private int durationTm;
    private String receiptYn;
    private String fromSql;
    private String whereSql;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String durationDt;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getScenarioNo() {
        return this.scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public int getScheduleSeq() {
        return this.scheduleSeq;
    }

    public void setScheduleSeq(int scheduleSeq) {
        this.scheduleSeq = scheduleSeq;
    }

    public int getCampaignNo() {
        return this.campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public int getSupraCampaignNo() {
        return this.supraCampaignNo;
    }

    public void setSupraCampaignNo(int supraCampaignNo) {
        this.supraCampaignNo = supraCampaignNo;
    }

    public String getCampaignDt() {
        return this.campaignDt;
    }

    public void setCampaignDt(String campaignDt) {
        this.campaignDt = campaignDt;
    }

    public int getPositionX() {
        return this.positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return this.positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getSizewidth() {
        return this.sizewidth;
    }

    public void setSizewidth(int sizewidth) {
        this.sizewidth = sizewidth;
    }

    public int getSizeheight() {
        return this.sizeheight;
    }

    public void setSizeheight(int sizeheight) {
        this.sizeheight = sizeheight;
    }

    public int getDurationTm() {
        return this.durationTm;
    }

    public void setDurationTm(int durationTm) {
        this.durationTm = durationTm;
    }

    public String getReceiptYn() {
        return this.receiptYn;
    }

    public void setReceiptYn(String receiptYn) {
        this.receiptYn = receiptYn;
    }

    public String getFromSql() {
        return fromSql;
    }

    public void setFromSql(String fromSql) {
        this.fromSql = fromSql;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getDurationDt() {
        return durationDt;
    }

    public void setDurationDt(String durationDt) {
        this.durationDt = durationDt;
    }
}
