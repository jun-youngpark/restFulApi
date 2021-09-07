package com.mnwise.wiseu.web.env.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVUSERMAILINFO 테이블 모델 클래스
 */
public class EnvSenderInfoVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private String userId;
    private int seqNo;
    private String senderNm;
    private String senderEmail;
    private String receiverNm;
    private String senderTel;
    private String senderFax;
    private String receiverFax;
    private String retmailReceiver;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String channelUseList;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getSenderNm() {
        return this.senderNm;
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }

    public String getSenderEmail() {
        return this.senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverNm() {
        return this.receiverNm;
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getSenderTel() {
        return this.senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getSenderFax() {
        return this.senderFax;
    }

    public void setSenderFax(String senderFax) {
        this.senderFax = senderFax;
    }

    public String getReceiverFax() {
        return this.receiverFax;
    }

    public void setReceiverFax(String receiverFax) {
        this.receiverFax = receiverFax;
    }

    public String getRetmailReceiver() {
        return this.retmailReceiver;
    }

    public void setRetmailReceiver(String retmailReceiver) {
        this.retmailReceiver = retmailReceiver;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getChannelUseList() {
        return channelUseList;
    }

    public void setChannelUseList(String channelUseList) {
        this.channelUseList = channelUseList;
    }
}
