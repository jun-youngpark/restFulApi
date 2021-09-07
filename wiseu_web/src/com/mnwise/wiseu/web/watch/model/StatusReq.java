package com.mnwise.wiseu.web.watch.model;

/**
 * NV_STATUS_REQ 테이블 모델 클래스
 */
public class StatusReq {
    private String reqKind;
    private String createTm;
    private String taskId;
    private String req;
    private int result;
    private String updateTm;
    private String userId;

    // 추가 멤버변수

    // 생성자
    /**
     * 기본 생성자
     */
    public StatusReq() {
        super();
    }

    // 기본 getter/setter
    public String getReqKind() {
        return this.reqKind;
    }

    public void setReqKind(String reqKind) {
        this.reqKind = reqKind;
    }

    public String getCreateTm() {
        return this.createTm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getReq() {
        return this.req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUpdateTm() {
        return this.updateTm;
    }

    public void setUpdateTm(String updateTm) {
        this.updateTm = updateTm;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // 추가 getter/setter
}