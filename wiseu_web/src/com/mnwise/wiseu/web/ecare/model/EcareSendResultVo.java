package com.mnwise.wiseu.web.ecare.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVECARESENDRESULT 테이블 모델 클래스
 */
public class EcareSendResultVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int ecareNo;
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
    private String crtgrpCd;
    private String editorId;
    private String logSts;
    private int superSeq;
    private int resendRetryCnt;
    private String resendSts;
    private int returnmailCnt;
    private String manualbatchSts;
    private int tmplVer;
    private int coverVer;
    private int prefaceVer;
    private int handlerVer;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String domainSts;
    private int returnMailCnt;
    private int smsSuccessCnt;
    private int smsFailCnt;
    private int lmsSuccessCnt;
    private int lmsFailCnt;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getEcareNo() {
        return this.ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
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
        return this.sendCnt;
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

    public String getCrtgrpCd() {
        return this.crtgrpCd;
    }

    public void setCrtgrpCd(String crtgrpCd) {
        this.crtgrpCd = crtgrpCd;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getLogSts() {
        return this.logSts;
    }

    public void setLogSts(String logSts) {
        this.logSts = logSts;
    }

    public int getSuperSeq() {
        return this.superSeq;
    }

    public void setSuperSeq(int superSeq) {
        this.superSeq = superSeq;
    }

    public int getResendRetryCnt() {
        return this.resendRetryCnt;
    }

    public void setResendRetryCnt(int resendRetryCnt) {
        this.resendRetryCnt = resendRetryCnt;
    }

    public String getResendSts() {
        return this.resendSts;
    }

    public void setResendSts(String resendSts) {
        this.resendSts = resendSts;
    }

    public int getReturnmailCnt() {
        return this.returnmailCnt;
    }

    public void setReturnmailCnt(int returnmailCnt) {
        this.returnmailCnt = returnmailCnt;
    }

    public String getManualbatchSts() {
        return this.manualbatchSts;
    }

    public void setManualbatchSts(String manualbatchSts) {
        this.manualbatchSts = manualbatchSts;
    }

    public int getTmplVer() {
        return this.tmplVer;
    }

    public void setTmplVer(int tmplVer) {
        this.tmplVer = tmplVer;
    }

    public int getCoverVer() {
        return this.coverVer;
    }

    public void setCoverVer(int coverVer) {
        this.coverVer = coverVer;
    }

    public int getPrefaceVer() {
        return this.prefaceVer;
    }

    public void setPrefaceVer(int prefaceVer) {
        this.prefaceVer = prefaceVer;
    }

    public int getHandlerVer() {
        return this.handlerVer;
    }

    public void setHandlerVer(int handlerVer) {
        this.handlerVer = handlerVer;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getDomainSts() {
        return domainSts;
    }

    public void setDomainSts(String domainSts) {
        this.domainSts = domainSts;
    }

    public int getReturnMailCnt() {
        return returnMailCnt;
    }

    public void setReturnMailCnt(int returnMailCnt) {
        this.returnMailCnt = returnMailCnt;
    }

    public int getSmsSuccessCnt() {
        return smsSuccessCnt;
    }

    public void setSmsSuccessCnt(int smsSuccessCnt) {
        this.smsSuccessCnt = smsSuccessCnt;
    }

    public int getSmsFailCnt() {
        return smsFailCnt;
    }

    public void setSmsFailCnt(int smsFailCnt) {
        this.smsFailCnt = smsFailCnt;
    }

    public int getLmsSuccessCnt() {
        return lmsSuccessCnt;
    }

    public void setLmsSuccessCnt(int lmsSuccessCnt) {
        this.lmsSuccessCnt = lmsSuccessCnt;
    }

    public int getLmsFailCnt() {
        return lmsFailCnt;
    }

    public void setLmsFailCnt(int lmsFailCnt) {
        this.lmsFailCnt = lmsFailCnt;
    }

}
