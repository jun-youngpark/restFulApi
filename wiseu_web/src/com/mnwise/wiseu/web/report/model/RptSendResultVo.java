package com.mnwise.wiseu.web.report.model;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.util.FormatUtil;

public class RptSendResultVo {
    private int ecareNo;
    private String ecareNm;
    private String startDt;

    private String resultSeq;
    private int sendCnt;
    private int successCnt;

    private int failCnt;
    private int openCnt;
    private int durationCnt;
    private int linkCnt;
    private int hardBounceCnt;
    private int softBounceCnt;
    private int returnMailCnt;
    private int target_cnt;

    private String reportDt;
    private String reportTm;

    private String sendStartDt;
    private String sendStartTm;
    private String sendEndDt;
    private String sendEndTm;

    private String userId; // 사용자아이디
    private String userNm; // 사용자명
    private String grpCd; // 부서코드
    private String gprNm; // 부서명

    private String reesultSeq; // 발송일련번호

    private String reqDept;
    private String reqUser;

    // 부서별과금 통계
    private int smsSendCnt;
    private int lmsSendCnt;
    private int mmsSendCnt;
    private int smsSuccessCnt;
    private int lmsSuccessCnt;
    private int mmsSuccessCnt;

    private int smsFailCnt;
    private int lmsFailCnt;

    // 추가부분 20130513
    private int sendCntTotal;

    //2017.07.10 KSM 재발송/다시보기
    private String resendSts;
    private String superSeq;
    
    // 2021.04.14 이현섭 발송취소건수
    private int cancelCnt;


    public void setCancelCnt(int cancelCnt) {
        this.cancelCnt = cancelCnt;
    }
    
    public int getCancelCnt() {
        return cancelCnt;
    }
    
    public String getSuperSeq() {
        return superSeq;
    }

    public void setSuperSeq(String superSeq) {
        this.superSeq = superSeq;
    }

    public String getResendSts() {
        return resendSts;
    }

    public void setResendSts(String resendSts) {
        this.resendSts = resendSts;
    }

    public int getSendCntTotal() {
        return sendCntTotal;
    }

    public void setSendCntTotal(int sendCntTotal) {
        this.sendCntTotal = sendCntTotal;
    }

    public int getTarget_cnt() {
        return target_cnt;
    }

    public void setTarget_cnt(int targetCnt) {
        target_cnt = targetCnt;
    }

    public int getSmsSendCnt() {
        return smsSendCnt;
    }

    public void setSmsSendCnt(int smsSendCnt) {
        this.smsSendCnt = smsSendCnt;
    }

    public int getLmsSendCnt() {
        return lmsSendCnt;
    }

    public void setLmsSendCnt(int lmsSendCnt) {
        this.lmsSendCnt = lmsSendCnt;
    }

    public int getMmsSendCnt() {
        return mmsSendCnt;
    }

    public void setMmsSendCnt(int mmsSendCnt) {
        this.mmsSendCnt = mmsSendCnt;
    }

    public int getSmsSuccessCnt() {
        return smsSuccessCnt;
    }

    public void setSmsSuccessCnt(int smsSuccessCnt) {
        this.smsSuccessCnt = smsSuccessCnt;
    }

    public int getLmsSuccessCnt() {
        return lmsSuccessCnt;
    }

    public void setLmsSuccessCnt(int lmsSuccessCnt) {
        this.lmsSuccessCnt = lmsSuccessCnt;
    }

    public int getMmsSuccessCnt() {
        return mmsSuccessCnt;
    }

    public void setMmsSuccessCnt(int mmsSuccessCnt) {
        this.mmsSuccessCnt = mmsSuccessCnt;
    }

    public String getReesultSeq() {
        return reesultSeq;
    }

    public void setReesultSeq(String reesultSeq) {
        this.reesultSeq = reesultSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getGprNm() {
        return gprNm;
    }

    public void setGprNm(String gprNm) {
        this.gprNm = gprNm;
    }

    public int getEcareNo() {
        return ecareNo;
    }

    public String getReqDept() {
        return reqDept;
    }

    public void setReqDept(String reqDept) {
        this.reqDept = reqDept;
    }

    public String getReqUser() {
        return reqUser;
    }

    public void setReqUser(String reqUser) {
        this.reqUser = reqUser;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getEcareNm() {
        return ecareNm;
    }

    public void setEcareNm(String ecareNm) {
        this.ecareNm = ecareNm;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }

    public int getSendCnt() {
        if(sendCnt == 0)
            return successCnt + failCnt;
        else
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

    public int getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(int linkCnt) {
        this.linkCnt = linkCnt;
    }

    public int getHardBounceCnt() {
        return hardBounceCnt;
    }

    public void setHardBounceCnt(int hardBounceCnt) {
        this.hardBounceCnt = hardBounceCnt;
    }

    public int getSoftBounceCnt() {
        return softBounceCnt;
    }

    public void setSoftBounceCnt(int softBounceCnt) {
        this.softBounceCnt = softBounceCnt;
    }

    public int getReturnMailCnt() {
        return returnMailCnt;
    }

    public void setReturnMailCnt(int returnMailCnt) {
        this.returnMailCnt = returnMailCnt;
    }

    public int getDurationCnt() {
        return durationCnt;
    }

    public void setDurationCnt(int durationCnt) {
        this.durationCnt = durationCnt;
    }

    public String getReportDt() {
        return reportDt;
    }

    public void setReportDt(String reportDt) {
        this.reportDt = reportDt;
    }

    public String getReportTm() {
        return reportTm;
    }

    public void setReportTm(String reportTm) {
        this.reportTm = reportTm;
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
    }

    public int getBounceCnt() {
        return(hardBounceCnt + softBounceCnt);
    }

    public String getReportDtToDateStr() {
        return FormatUtil.toYmdStrDate(reportDt);
    }

    public String getSendStartDtToSimpleDateStr() {
        return FormatUtil.toYmdStrDate(sendStartDt);
    }

    public String getSendStartDtToDateStr() {
        // 실시간,준실시간의 경우는 1day 리포트를 하므로 이케어 스케쥴처럼 발송 시간을 계산할 수 없음.
        if(sendStartTm == null)
            return "";
        return FormatUtil.toBasicStrDate(sendStartDt, sendStartTm);
    }

    public String getSendEndDtToDateStr() {
        // 실시간,준실시간의 경우는 1day 리포트를 하므로 이케어 스케쥴처럼 발송 시간을 계산할 수 없음.
        if(sendEndTm == null)
            return "";
        return FormatUtil.toBasicStrDate(sendEndDt, sendEndTm);
    }

    public String getFormatPeriod() {
        // 실시간,준실시간의 경우는 1day 리포트를 하므로 이케어 스케쥴처럼 발송 시간을 계산할 수 없음.
        if(sendEndDt == null)
            return "";
        if(StringUtil.isNotEmpty(sendEndDt) && sendEndDt.equals("00000000")) {
            return "-";
        } else {
            long stime = FormatUtil.toBasicDate(sendStartDt, sendStartTm).getTime();
            long etime = FormatUtil.toBasicDate(sendEndDt, sendEndTm).getTime();
            return DurationFormatUtils.formatPeriod(stime, etime, "HH:mm:ss");
        }
    }

    // public String getWeekday() {
    // return FormatUtil.toWeekday(reportDt);
    // }

    public String getWeekday() {
        return FormatUtil.toWeekday(sendStartDt);
    }

    public int getSmsFailCnt() {
        return smsFailCnt;
    }

    public void setSmsFailCnt(int smsFailCnt) {
        this.smsFailCnt = smsFailCnt;
    }

    public int getLmsFailCnt() {
        return lmsFailCnt;
    }

    public void setLmsFailCnt(int lmsFailCnt) {
        this.lmsFailCnt = lmsFailCnt;
    }

    public double getCancelCntToPercent() {
        return FormatUtil.toNumPercent(cancelCnt, getSendCnt(), 1);
    }
    public double getSmsSuccessCntToPercent() {
        return FormatUtil.toNumPercent(smsSuccessCnt, smsSuccessCnt + smsFailCnt, 1);
    }

    public double getSmsFailCntToPercent() {
        return FormatUtil.toNumPercent(smsFailCnt, smsSuccessCnt + smsFailCnt, 1);
    }

    public double getLmsSuccessCntToPercent() {
        return FormatUtil.toNumPercent(lmsSuccessCnt, lmsSuccessCnt + lmsFailCnt, 1);
    }

    public double getLmsFailCntToPercent() {
        return FormatUtil.toNumPercent(lmsFailCnt, lmsSuccessCnt + lmsFailCnt, 1);
    }

    public double getTotalSuccessCntToPercent() {
        return FormatUtil.toNumPercent(successCnt + smsSuccessCnt + lmsSuccessCnt, sendCnt, 1);
    }
}
