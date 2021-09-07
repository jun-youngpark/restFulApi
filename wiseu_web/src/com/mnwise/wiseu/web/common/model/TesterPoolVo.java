package com.mnwise.wiseu.web.common.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * NVTESTUSERPOOL 테이블 모델 클래스
 */

public class TesterPoolVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private String userId;
    private int seqNo;
    private String testReceiverEmail;
    private String testReceiverTel;
    private String testReceiverNm;
    private String testReceiverFax;
    private int testGrpCd;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int serviceNo;
    private String serviceType;
    private String checked;
    private String testGrpNm;

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

    public String getTestReceiverEmail() {
        return this.testReceiverEmail;
    }

    public void setTestReceiverEmail(String testReceiverEmail) {
        this.testReceiverEmail = testReceiverEmail;
    }

    public String getTestReceiverTel() {
        return this.testReceiverTel;
    }

    public void setTestReceiverTel(String testReceiverTel) {
        this.testReceiverTel = testReceiverTel;
    }

    public String getTestReceiverNm() {
        return this.testReceiverNm;
    }

    public void setTestReceiverNm(String testReceiverNm) {
        this.testReceiverNm = testReceiverNm;
    }

    public String getTestReceiverFax() {
        return this.testReceiverFax;
    }

    public void setTestReceiverFax(String testReceiverFax) {
        this.testReceiverFax = testReceiverFax;
    }

    public int getTestGrpCd() {
        return this.testGrpCd;
    }

    public void setTestGrpCd(int testGrpCd) {
        this.testGrpCd = testGrpCd;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int sericeNo) {
        this.serviceNo = sericeNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTestGrpNm() {
        return testGrpNm;
    }

    public void setTestGrpNm(String testGrpNm) {
        this.testGrpNm = testGrpNm;
    }
}
