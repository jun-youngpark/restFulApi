package com.mnwise.wiseu.web.resend.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class LstResendVo extends SearchVo {
    /**
     *
     */
    private static final long serialVersionUID = 2610199756032035088L;
    private String customerNm;
    private String customerEmail;
    private String sendDt;
    private String sendTm;
    private String errorCd;
    private String errMsg;
    private String errMsgKor;
    private String resultSeq;
    private String openDt;
    private String serviceNo;
    private String serviceNm;
    private String listSeq;
    private String recordSeq;
    private String seq;
    private String receiverId;
    private String receiverNm;
    private String receiverEmail;
    private String customerKey;
    private String channel;
    private String slot1;
    private String slot2;
    private String resendSts;
    private String superSeq;
    private String client;
    private String serviceType;
    private String subType;
    private String resendReason;
    private String filePreviewPath;
    private String tmplCd;

    public String getTmplCd() {
        return tmplCd;
    }

    public void setTmplCd(String tmplCd) {
        this.tmplCd = tmplCd;
    }

    // A/B 테스트 기본 값을 사용 안 함("N")으로 설정
    private String abTestType = "N";

    public String getFilePreviewPath() {
        return filePreviewPath;
    }

    public void setFilePreviewPath(String filePreviewPath) {
        this.filePreviewPath = filePreviewPath;
    }

    public String getAbTestType() {
        return abTestType;
    }

    public void setAbTestType(String abTestType) {
        this.abTestType = abTestType;
    }

    public String getResendReason() {
        return resendReason;
    }

    public void setResendReason(String resendReason) {
        this.resendReason = resendReason;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSendDt() {
        return sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getSendTm() {
        return sendTm;
    }

    public void setSendTm(String sendTm) {
        this.sendTm = sendTm;
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsgKor() {
        return errMsgKor;
    }

    public void setErrMsgKor(String errMsgKor) {
        this.errMsgKor = errMsgKor;
    }

    public String getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }

    public String getOpenDt() {
        return openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getServiceNm() {
        return serviceNm;
    }

    public void setServiceNm(String serviceNm) {
        this.serviceNm = serviceNm;
    }

    public String getListSeq() {
        return listSeq;
    }

    public void setListSeq(String listSeq) {
        this.listSeq = listSeq;
    }

    public String getRecordSeq() {
        return recordSeq;
    }

    public void setRecordSeq(String recordSeq) {
        this.recordSeq = recordSeq;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverNm() {
        return receiverNm;
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSlot1() {
        return slot1;
    }

    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    public String getSlot2() {
        return slot2;
    }

    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }

    public String getResendSts() {
        return resendSts;
    }

    public void setResendSts(String resendSts) {
        this.resendSts = resendSts;
    }

    public String getSuperSeq() {
        return superSeq;
    }

    public void setSuperSeq(String superSeq) {
        this.superSeq = superSeq;
    }

}
