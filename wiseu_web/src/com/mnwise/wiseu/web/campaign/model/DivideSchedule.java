package com.mnwise.wiseu.web.campaign.model;

/**
 * NVDIVIDESCHEDULE 테이블 모델 클래스
 */
public class DivideSchedule {
    private String client;
    private int serviceNo;
    private int divideSeq;
    private int targetCnt;
    private String startDt;

    // 추가 멤버변수

    // 생성자
    /**
     * 기본 생성자
     */
    public DivideSchedule() {
        super();
    }

    // 기본 getter/setter
    public String getClient() {
        return this.client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getServiceNo() {
        return this.serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public int getDivideSeq() {
        return this.divideSeq;
    }

    public void setDivideSeq(int divideSeq) {
        this.divideSeq = divideSeq;
    }

    public int getTargetCnt() {
        return this.targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
    }

    public String getStartDt() {
        return this.startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    // 추가 getter/setter
}