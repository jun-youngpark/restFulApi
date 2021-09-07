package com.mnwise.wiseu.web.segment.model;

/**
 * 사용중인 세그먼트 정보를 담는 Value Object
 */
public class UsingSegmentVo {
    private String type;
    private String serviceNo;
    private String serviceNm;
    private String grpCd;
    private String grpNm;
    private String userId;
    private String nameKor;
    private String lastupdateDate;
    private String serviceSts;
    private String statusNm;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getLastupdateDate() {
        return lastupdateDate;
    }

    public void setLastupdateDate(String lastupdateDate) {
        this.lastupdateDate = lastupdateDate;
    }

    public String getServiceSts() {
        return serviceSts;
    }

    public void setServiceSts(String serviceSts) {
        this.serviceSts = serviceSts;
    }

    public String getStatusNm() {
        return statusNm;
    }

    public void setStatusNm(String statusNm) {
        this.statusNm = statusNm;
    }

}
