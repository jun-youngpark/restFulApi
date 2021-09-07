package com.mnwise.wiseu.web.common.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class TesterVo extends SearchVo {
    private static final long serialVersionUID = 318581774519764841L;
    private int serviceNo;
    private String serviceType;
    private String userId;
    private int seqNo;

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

}
