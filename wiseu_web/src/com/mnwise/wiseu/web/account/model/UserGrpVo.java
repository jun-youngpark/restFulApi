package com.mnwise.wiseu.web.account.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVUSERGRP 테이블 모델 클래스
 */
public class UserGrpVo extends BaseVo {
    private static final long serialVersionUID = -3406339585735434965L;
    private String grpCd;
    private String grpNm;
    private String supraGrpCd;
    private String grpDesc;
    private int grpLevel;
    private String permission;
    private String editorId;
    private String lastUpdateDt;
    private String managerId;
    private String activeYn;
    private String etcInfo1;
    private String managerNm;
    private String serverIp;
    private String serverPort;
    private String funcCode;
    private String funcDesc;
    private String workDoc;
    private String acceptYn;
    private String permitDt;
    private String permitSts;
    private String reqDeptId;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String reqDeptCd;
    private String supraGrpNm;
    private String mainUser;
    private String hasChildren;
    private String userRole;
    private String smsIndividualCharge;
    private String[] saGrpCd;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getGrpNm() {
        return this.grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getSupraGrpCd() {
        return supraGrpCd;
    }

    public void setSupraGrpCd(String supraGrpCd) {
        this.supraGrpCd = supraGrpCd;
    }

    public String getGrpDesc() {
        return this.grpDesc;
    }

    public void setGrpDesc(String grpDesc) {
        this.grpDesc = grpDesc;
    }

    public int getGrpLevel() {
        return this.grpLevel;
    }

    public void setGrpLevel(int grpLevel) {
        this.grpLevel = grpLevel;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getLastUpdateDt() {
        return lastUpdateDt;
    }

    public void setLastUpdateDt(String lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public String getManagerId() {
        return this.managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getActiveYn() {
        return this.activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    public String getEtcInfo1() {
        return this.etcInfo1;
    }

    public void setEtcInfo1(String etcInfo1) {
        this.etcInfo1 = etcInfo1;
    }

    public String getManagerNm() {
        return this.managerNm;
    }

    public void setManagerNm(String managerNm) {
        this.managerNm = managerNm;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getFuncCode() {
        return this.funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getFuncDesc() {
        return this.funcDesc;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }

    public String getWorkDoc() {
        return this.workDoc;
    }

    public void setWorkDoc(String workDoc) {
        this.workDoc = workDoc;
    }

    public String getAcceptYn() {
        return this.acceptYn;
    }

    public void setAcceptYn(String acceptYn) {
        this.acceptYn = acceptYn;
    }

    public String getPermitDt() {
        return this.permitDt;
    }

    public void setPermitDt(String permitDt) {
        this.permitDt = permitDt;
    }

    public String getPermitSts() {
        return this.permitSts;
    }

    public void setPermitSts(String permitSts) {
        this.permitSts = permitSts;
    }

    public String getReqDeptId() {
        return this.reqDeptId;
    }

    public void setReqDeptId(String reqDeptId) {
        this.reqDeptId = reqDeptId;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getReqDeptCd() {
        return reqDeptCd;
    }

    public void setReqDeptCd(String reqDeptCd) {
        this.reqDeptCd = reqDeptCd;
    }

    public String getSupraGrpNm() {
        return supraGrpNm;
    }

    public void setSupraGrpNm(String supraGrpNm) {
        this.supraGrpNm = supraGrpNm;
    }

    public String getMainUser() {
        return mainUser;
    }

    public void setMainUser(String mainUser) {
        this.mainUser = mainUser;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getSmsIndividualCharge() {
        return smsIndividualCharge;
    }

    public void setSmsIndividualCharge(String smsIndividualCharge) {
        this.smsIndividualCharge = smsIndividualCharge;
    }

    public String[] getSaGrpCd() {
        return saGrpCd;
    }

    public void setSaGrpCd(String[] saGrpCd) {
        this.saGrpCd = saGrpCd;
    }
}
