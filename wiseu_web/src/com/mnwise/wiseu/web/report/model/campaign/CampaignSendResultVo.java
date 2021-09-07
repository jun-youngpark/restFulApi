package com.mnwise.wiseu.web.report.model.campaign;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

public class CampaignSendResultVo extends BaseVo {
    /**
     *
     */
    private static final long serialVersionUID = 7802454432773132802L;
    private int campaignNo;
    private int targetCnt;
    private int sendCnt;
    private int successCnt;
    private int returnMailCnt;
    private int openCnt;
    private int durationCnt;
    private int linkCnt;
    private int softBounce;
    private int hardBounce;
    private String sendStartDt;
    private String sendStartTm;
    private String sendEndDt;
    private String sendEndTm;

    private String startDate;
    private String resultState;
    private String resultStsNm;

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
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

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(int successCnt) {
        this.successCnt = successCnt;
    }

    public int getSoftBounce() {
        return softBounce;
    }

    public void setSoftBounce(int softBounce) {
        this.softBounce = softBounce;
    }

    public int getHardBounce() {
        return hardBounce;
    }

    public void setHardBounce(int hardBounce) {
        this.hardBounce = hardBounce;
    }

    public int getDurationCnt() {
        return durationCnt;
    }

    public void setDurationCnt(int durationCnt) {
        this.durationCnt = durationCnt;
    }

    public int getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(int linkCnt) {
        this.linkCnt = linkCnt;
    }

    public int getReturnMailCnt() {
        return returnMailCnt;
    }

    public void setReturnMailCnt(int returnMailCnt) {
        this.returnMailCnt = returnMailCnt;
    }

    public String getSendCntPercent() {
        return FormatUtil.toStrPercent(sendCnt, sendCnt, 1);
    }

    public String getSuccessCntPercent() {
        return FormatUtil.toStrPercent(successCnt, sendCnt, 1);
    }

    public String getHardBouncePercent() {
        return FormatUtil.toStrPercent(hardBounce, sendCnt, 1);
    }

    public String getSoftBouncePercent() {
        return FormatUtil.toStrPercent(softBounce, sendCnt, 1);
    }

    public double getSendCntNumber() {
        return FormatUtil.toNumPercent(sendCnt, sendCnt, 1);
    }

    public double getSuccessCntNumber() {
        return FormatUtil.toNumPercent(successCnt, sendCnt, 1);
    }

    public double getHardBounceNumber() {
        return FormatUtil.toNumPercent(hardBounce, sendCnt, 1);
    }

    public double getSoftBounceNumber() {
        return FormatUtil.toNumPercent(softBounce, sendCnt, 1);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getResultState() {
        return resultState;
    }

    public void setResultState(String resultState) {
        this.resultState = resultState;
    }

    public String getStartDateYear() {
        return startDate.substring(0, 4);
    }

    public String getStartDateMonth() {
        return startDate.substring(4, 6);
    }

    public String getStartDateDay() {
        return startDate.substring(6);
    }

    public String getSendStartDt() {
        return sendStartDt;
    }

    public void setSendStartDt(String sendStartDt) {
        this.sendStartDt = sendStartDt;
    }

    public String getSendStartTm() {
        return sendStartTm;
    }

    public void setSendStartTm(String sendStartTm) {
        this.sendStartTm = sendStartTm;
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

    public String getSendStartDtToSimpleDateStr() {
        return FormatUtil.toSimpleBasicStrDate(sendStartDt, sendStartTm);
    }

    public String getSendStartDtToDateStr() {
        return FormatUtil.toBasicStrDate(sendStartDt, sendStartTm);
    }

    public String getSendEndDtToDateStr() {
        return FormatUtil.toBasicStrDate(sendEndDt, sendEndTm);
    }

    public String getFormatPeriod() {
        // 발송이 종료되지 않은 경우
        if(sendEndDt.equals("00000000")) {
            return resultState.equalsIgnoreCase("XW") ? "sending" : "fail";
        } else {
            long stime = FormatUtil.toBasicDate(sendStartDt, sendStartTm).getTime();
            long etime = FormatUtil.toBasicDate(sendEndDt, sendEndTm).getTime();
            return DurationFormatUtils.formatPeriod(stime, etime, "HH:mm:ss");
        }
    }

    public String getSendResultStatusNm() {
        return resultStsNm;
    }

    public void setSendResultStatusNm(String resultStsNm) {
        this.resultStsNm = resultStsNm;
    }
}
