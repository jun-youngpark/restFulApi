package com.mnwise.wiseu.web.editor.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * NVDEFAULTHANDLER 테이블 모델 클래스
 */
public class DefaultHandlerVo extends SearchVo {
    private int seq;
    private String handleNm;
    private String handleDesc;
    private String serviceType;
    private String channel;
    private String handleType;
    private String handleAttr;
    private String userId;
    private String createDt;
    private String createTm;
    private String handler;
    private String abTestYn;

    /** 메시지 구분: 캠페인, 이케어 */
    private String msgType;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String subType;
    private String searchServiceType;
    private String searchChannel;
    private int[] noArray;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getSeq() {
        return this.seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getHandleNm() {
        return this.handleNm;
    }

    public void setHandleNm(String handleNm) {
        this.handleNm = handleNm;
    }

    public String getHandleDesc() {
        return this.handleDesc;
    }

    public void setHandleDesc(String handleDesc) {
        this.handleDesc = handleDesc;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHandleType() {
        return this.handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getHandleAttr() {
        return this.handleAttr;
    }

    public void setHandleAttr(String handleAttr) {
        this.handleAttr = handleAttr;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDt() {
        return this.createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getCreateTm() {
        return this.createTm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    public String getHandler() {
        return this.handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getAbTestYn() {
        return this.abTestYn;
    }

    public void setAbTestYn(String abTestYn) {
        this.abTestYn = abTestYn;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSearchServiceType() {
        return searchServiceType;
    }

    public void setSearchServiceType(String searchServiceType) {
        this.searchServiceType = searchServiceType;
    }

    public String getSearchChannel() {
        return searchChannel;
    }

    public void setSearchChannel(String searchChannel) {
        this.searchChannel = searchChannel;
    }

    public int[] getNoArray() {
        return noArray;
    }

    public void setNoArray(int[] noArray) {
        this.noArray = noArray;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    @Override
    public String toString() {
        return "DefaultHandlerVo [channel=" + channel + ", createDt=" + createDt + ", createTm=" + createTm + ", handleAttr=" + handleAttr + ", handleDesc=" + handleDesc + ", handleNm=" + handleNm
            + ", handleType=" + handleType + ", handler=" + handler + ", seq=" + seq + ", serviceType=" + serviceType + ", userId=" + userId + "]";
    }
}
