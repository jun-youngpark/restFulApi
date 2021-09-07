package com.mnwise.wiseu.web.watch.model;

/**
 * NV_SVR_INFO 테이블 모델 클래스
 */
public class SvrInfo {
    private String serverId;
    private String lastUpdateTm;
    private String startTm;
    private int jobCnt;
    private int processThreadCnt;
    private int workThreadCnt;
    private int openfileDescCnt;
    private long maxMemory;
    private long usedMemory;
    private double cpuRate;
    private int maxQueueSize;
    private int usedQueueSize;
    private int status;
    private String pollingActStatus;
    private String pollingActStatusUpdateTm;
    private int dispNo;
    private String execInfo;
    private String configCont;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String curTm;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getServerId() {
        return this.serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getLastUpdateTm() {
        return this.lastUpdateTm;
    }

    public void setLastUpdateTm(String lastUpdateTm) {
        this.lastUpdateTm = lastUpdateTm;
    }

    public String getStartTm() {
        return this.startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public int getJobCnt() {
        return this.jobCnt;
    }

    public void setJobCnt(int jobCnt) {
        this.jobCnt = jobCnt;
    }

    public int getProcessThreadCnt() {
        return this.processThreadCnt;
    }

    public void setProcessThreadCnt(int processThreadCnt) {
        this.processThreadCnt = processThreadCnt;
    }

    public int getWorkThreadCnt() {
        return this.workThreadCnt;
    }

    public void setWorkThreadCnt(int workThreadCnt) {
        this.workThreadCnt = workThreadCnt;
    }

    public int getOpenfileDescCnt() {
        return this.openfileDescCnt;
    }

    public void setOpenfileDescCnt(int openfileDescCnt) {
        this.openfileDescCnt = openfileDescCnt;
    }

    public long getMaxMemory() {
        return this.maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public long getUsedMemory() {
        return this.usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public double getCpuRate() {
        return this.cpuRate;
    }

    public void setCpuRate(double cpuRate) {
        this.cpuRate = cpuRate;
    }

    public int getMaxQueueSize() {
        return this.maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int getUsedQueueSize() {
        return this.usedQueueSize;
    }

    public void setUsedQueueSize(int usedQueueSize) {
        this.usedQueueSize = usedQueueSize;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPollingActStatus() {
        return this.pollingActStatus;
    }

    public void setPollingActStatus(String pollingActStatus) {
        this.pollingActStatus = pollingActStatus;
    }

    public String getPollingActStatusUpdateTm() {
        return this.pollingActStatusUpdateTm;
    }

    public void setPollingActStatusUpdateTm(String pollingActStatusUpdateTm) {
        this.pollingActStatusUpdateTm = pollingActStatusUpdateTm;
    }

    public int getDispNo() {
        return this.dispNo;
    }

    public void setDispNo(int dispNo) {
        this.dispNo = dispNo;
    }

    public String getExecInfo() {
        return this.execInfo;
    }

    public void setExecInfo(String execInfo) {
        this.execInfo = execInfo;
    }

    public String getConfigCont() {
        return this.configCont;
    }

    public void setConfigCont(String configCont) {
        this.configCont = configCont;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getCurTm() {
        return curTm;
    }

    public void setCurTm(String curTm) {
        this.curTm = curTm;
    }


}