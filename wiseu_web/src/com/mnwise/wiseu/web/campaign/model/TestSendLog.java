package com.mnwise.wiseu.web.campaign.model;

/**
 * NVTESTSENDLOG 테이블 모델 클래스
 */
public class TestSendLog {
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
    private String partMessage;

    // 추가 멤버변수

    // 생성자
    /**
     * 기본 생성자
     */
    public TestSendLog() {
        super();
    }

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

    public String getPartMessage() {
        return this.partMessage;
    }

    public void setPartMessage(String partMessage) {
        this.partMessage = partMessage;
    }

    // 추가 getter/setter
}