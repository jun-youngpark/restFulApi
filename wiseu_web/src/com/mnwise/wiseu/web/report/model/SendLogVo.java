package com.mnwise.wiseu.web.report.model;

import java.io.Serializable;

/**
 * NVSENDLOG 테이블 모델 클래스
 */
public class SendLogVo implements Serializable {
    private static final long serialVersionUID = -1L;
    private int campaignNo;
    private int resultSeq;
    private String listSeq;
    private String customerKey;
    private String recordSeq;
    private String customerNm;
    private String customerEmail;
    private String sid;
    private String sendDt;
    private String sendTm;
    private String endDt;
    private String endTm;
    private String errorCd;
    private String sendDomain;
    private String errMsg;
    private String slot1;
    private String slot2;
    private String resendYn;
    private String reqDeptId;
    private String reqUserId;
    private String messageKey;
    private String seq;
    private String srfidd;
    private String openDt;
    private String eaiSendFg;
    private String partMessage;
    private String failBackChannel;
    private String failBackResultCd;
    private String failBackSenddtm;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String sendDtm;

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

    public String getListSeq() {
        return this.listSeq;
    }

    public void setListSeq(String listSeq) {
        this.listSeq = listSeq;
    }

    public String getCustomerKey() {
        return this.customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getRecordSeq() {
        return this.recordSeq;
    }

    public void setRecordSeq(String recordSeq) {
        this.recordSeq = recordSeq;
    }

    public String getCustomerNm() {
        return this.customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSendDt() {
        return this.sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getSendTm() {
        return this.sendTm;
    }

    public void setSendTm(String sendTm) {
        this.sendTm = sendTm;
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

    public String getErrorCd() {
        return this.errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getSendDomain() {
        return this.sendDomain;
    }

    public void setSendDomain(String sendDomain) {
        this.sendDomain = sendDomain;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getSlot1() {
        return this.slot1;
    }

    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    public String getSlot2() {
        return this.slot2;
    }

    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }

    public String getResendYn() {
        return this.resendYn;
    }

    public void setResendYn(String resendYn) {
        this.resendYn = resendYn;
    }

    public String getReqDeptId() {
        return this.reqDeptId;
    }

    public void setReqDeptId(String reqDeptId) {
        this.reqDeptId = reqDeptId;
    }

    public String getReqUserId() {
        return this.reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getSeq() {
        return this.seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSrfidd() {
        return this.srfidd;
    }

    public void setSrfidd(String srfidd) {
        this.srfidd = srfidd;
    }

    public String getOpenDt() {
        return this.openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    public String getEaiSendFg() {
        return this.eaiSendFg;
    }

    public void setEaiSendFg(String eaiSendFg) {
        this.eaiSendFg = eaiSendFg;
    }

    public String getPartMessage() {
        return this.partMessage;
    }

    public void setPartMessage(String partMessage) {
        this.partMessage = partMessage;
    }

    public String getFailBackChannel() {
        return this.failBackChannel;
    }

    public void setFailBackChannel(String failBackChannel) {
        this.failBackChannel = failBackChannel;
    }

    public String getFailBackResultCd() {
        return this.failBackResultCd;
    }

    public void setFailBackResultCd(String failBackResultCd) {
        this.failBackResultCd = failBackResultCd;
    }

    public String getFailBackSenddtm() {
        return this.failBackSenddtm;
    }

    public void setFailBackSenddtm(String failBackSenddtm) {
        this.failBackSenddtm = failBackSenddtm;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public void setSendDtm(String sendDtm) {
        this.sendDtm = sendDtm;
    }

    public String getSendDtm() {
        return sendDtm;
    }
}
