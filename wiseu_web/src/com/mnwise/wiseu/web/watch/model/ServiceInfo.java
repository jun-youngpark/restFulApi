package com.mnwise.wiseu.web.watch.model;

public class ServiceInfo {
    String startTime; // 시작시간 12
    int no; // 캠페인 번호 15
    String serviceName; // 서비스명 50
    String channel; // 채널 M/S/T/F 1
    String userId; // 사용자아이디 15
    String sendMode; // 발송모드 T/R 1
    String client; // EM/EC 2
    String tid; // 31
    String elapsedTime; // 걸린시간 4
    String serviceType; // 서비스타입 1(S:스케쥴, N:준실시간, R:실시간)
    String serviceStatus; // 서비스상태 1

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSendMode() {
        return sendMode;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

}
