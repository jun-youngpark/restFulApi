package com.mnwise.wiseu.web.campaign.model;

import org.springframework.util.StringUtils;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

/**
 * NVTRACEINFO 테이블 모델 클래스
 */
public class TraceInfoVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int campaignNo;
    private String traceType;
    private String startDt;
    private String startTm;
    private String endDt;
    private String endTm;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getCampaignNo() {
        return this.campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getTraceType() {
        return this.traceType;
    }

    public void setTraceType(String traceType) {
        this.traceType = traceType;
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

    /////////////////////////////////////////////////////////////////
    // 멤버 함수
    public String getStartDtDB() {
        return StringUtils.deleteAny(startDt, "-:");
    }

    public String getStartTmDB() {
        return StringUtils.deleteAny(startTm, "-:");
    }

    public String getEndDtDB() {
        return StringUtils.deleteAny(endDt, "-:");
    }

    public String getEndTmDB() {
        return StringUtils.deleteAny(endTm, "-:");
    }

    public String getStartDtToDateStr() {
        return FormatUtil.toBasicStrDate(startDt, startTm, "yyyyMMdd HHmmss");
    }

    public String getEndDtToDateStr() {
        return FormatUtil.toBasicStrDate(endDt, endTm, "yyyyMMdd HHmmss");
    }
}
