package com.mnwise.wiseu.web.watch.model;

/**
 * NV_SVC_MTS 테이블 모델 클래스
 */
public class SvcMts {
    private String tId;
    private String serverId;
    private String startTm;
    private String endTm;
    private String serviceSts;
    private int totCnt;
    private int madeCnt;
    private int maxRetry;
    private int sendCnt;
    private int successCnt;
    private int unknownUserCnt;
    private int unknownHostCnt;
    private int smtpExceptCnt;
    private int noRouteCnt;
    private int refusedCnt;
    private int etcExceptCnt;
    private int invalidAddrCnt;
    private int queueCnt;
    private int threadCnt;
    private int handlerThreadCnt;
    private String errMsg;
    private String updateTm;
    private String createTm;

    // 추가 멤버변수

    // 생성자
    /**
     * 기본 생성자
     */
    public SvcMts() {
        super();
    }

    // 기본 getter/setter
    public String getTId() {
        return this.tId;
    }

    public void setTId(String tId) {
        this.tId = tId;
    }

    public String getServerId() {
        return this.serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getStartTm() {
        return this.startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public String getEndTm() {
        return this.endTm;
    }

    public void setEndTm(String endTm) {
        this.endTm = endTm;
    }

    public String getServiceSts() {
        return this.serviceSts;
    }

    public void setServiceSts(String serviceSts) {
        this.serviceSts = serviceSts;
    }

    public int getTotCnt() {
        return this.totCnt;
    }

    public void setTotCnt(int totCnt) {
        this.totCnt = totCnt;
    }

    public int getMadeCnt() {
        return this.madeCnt;
    }

    public void setMadeCnt(int madeCnt) {
        this.madeCnt = madeCnt;
    }

    public int getMaxRetry() {
        return this.maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
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

    public int getInvalidAddrCnt() {
        return this.invalidAddrCnt;
    }

    public void setInvalidAddrCnt(int invalidAddrCnt) {
        this.invalidAddrCnt = invalidAddrCnt;
    }

    public int getQueueCnt() {
        return this.queueCnt;
    }

    public void setQueueCnt(int queueCnt) {
        this.queueCnt = queueCnt;
    }

    public int getThreadCnt() {
        return this.threadCnt;
    }

    public void setThreadCnt(int threadCnt) {
        this.threadCnt = threadCnt;
    }

    public int getHandlerThreadCnt() {
        return this.handlerThreadCnt;
    }

    public void setHandlerThreadCnt(int handlerThreadCnt) {
        this.handlerThreadCnt = handlerThreadCnt;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getUpdateTm() {
        return this.updateTm;
    }

    public void setUpdateTm(String updateTm) {
        this.updateTm = updateTm;
    }

    public String getCreateTm() {
        return this.createTm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    // 추가 getter/setter
}