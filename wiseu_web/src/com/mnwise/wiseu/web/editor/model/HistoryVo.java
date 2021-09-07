package com.mnwise.wiseu.web.editor.model;

/**
 * NVECMSGHANDLERHISTORY, NVECARETEMPLATEHISTORY 테이블 통합 모델 클래스
 */
public class HistoryVo {
    private int ecareNo;
    private String userId;
    private String lastupdateDt;
    private String lastupdateTm;
    private String type;

    /** 핸들러 */
    private String appsource;
    private String historyMsg;
    private int handlerVer;

    private String seg;
    /** 템플릿 */
    private String template;
    private int tmplVer;
    private String kakaoButtons;
    private int contsNo;
    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter

    public int getEcareNo() {
        return this.ecareNo;
    }

    public int getContsNo() {
        return contsNo;
    }

    public void setContsNo(int contsNo) {
        this.contsNo = contsNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastupdateDt() {
        return this.lastupdateDt;
    }

    public void setLastupdateDt(String lastupdateDt) {
        this.lastupdateDt = lastupdateDt;
    }

    public String getLastupdateTm() {
        return this.lastupdateTm;
    }

    public void setLastupdateTm(String lastupdateTm) {
        this.lastupdateTm = lastupdateTm;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppsource() {
        return this.appsource;
    }

    public void setAppsource(String appsource) {
        this.appsource = appsource;
    }

    public String getHistoryMsg() {
        return this.historyMsg;
    }

    public void setHistoryMsg(String historyMsg) {
        this.historyMsg = historyMsg;
    }

    public int getHandlerVer() {
        return this.handlerVer;
    }

    public void setHandlerVer(int handlerVer) {
        this.handlerVer = handlerVer;
    }

    public String getSeg() {
        return seg;
    }

    public void setSeg(String seg) {
        this.seg = seg;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getTmplVer() {
        return tmplVer;
    }

    public void setTmplVer(int tmplVer) {
        this.tmplVer = tmplVer;
    }

    public String getKakaoButtons() {
        return kakaoButtons;
    }

    public void setKakaoButtons(String kakaoButtons) {
        this.kakaoButtons = kakaoButtons;
    }

}
