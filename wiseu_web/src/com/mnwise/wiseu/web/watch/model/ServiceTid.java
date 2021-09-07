package com.mnwise.wiseu.web.watch.model;

public class ServiceTid {
    /* common */
    String serverName;
    String serverId;
    String serviceId;
    String serviceName;
    String userId;
    String sendMode;
    String serviceType;
    String client;
    String sid;

    /* server common */
    String startTime;
    String serviceStatus;

    /* lts */
    int targetCount;
    String channel;
    int mtsNo;

    /* mts */
    int totalCount;
    int madeCount;
    int retry;
    int sendCount;
    int successCount;
    int unknownUserCount;
    int unknownHostCount;
    int smtpExceptCount;
    int noRouteCount;
    int refusedCount;
    int etcExceptCount;
    int invalidAddressCount;
    String errorMsg;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String chnnel) {
        this.channel = chnnel;
    }

    public int getMtsNo() {
        return mtsNo;
    }

    public void setMtsNo(int mtsNo) {
        this.mtsNo = mtsNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getMadeCount() {
        return madeCount;
    }

    public void setMadeCount(int madeCount) {
        this.madeCount = madeCount;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getUnknownUserCount() {
        return unknownUserCount;
    }

    public void setUnknownUserCount(int unknownUserCount) {
        this.unknownUserCount = unknownUserCount;
    }

    public int getUnknownHostCount() {
        return unknownHostCount;
    }

    public void setUnknownHostCount(int unknownHostCount) {
        this.unknownHostCount = unknownHostCount;
    }

    public int getSmtpExceptCount() {
        return smtpExceptCount;
    }

    public void setSmtpExceptCount(int smtpExceptCount) {
        this.smtpExceptCount = smtpExceptCount;
    }

    public int getNoRouteCount() {
        return noRouteCount;
    }

    public void setNoRouteCount(int noRouteCount) {
        this.noRouteCount = noRouteCount;
    }

    public int getRefusedCount() {
        return refusedCount;
    }

    public void setRefusedCount(int refusedCount) {
        this.refusedCount = refusedCount;
    }

    public int getEtcExceptCount() {
        return etcExceptCount;
    }

    public void setEtcExceptCount(int etcExceptCount) {
        this.etcExceptCount = etcExceptCount;
    }

    public int getInvalidAddressCount() {
        return invalidAddressCount;
    }

    public void setInvalidAddressCount(int invalidAddressCount) {
        this.invalidAddressCount = invalidAddressCount;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
