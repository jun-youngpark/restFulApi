package com.mnwise.wiseu.web.ecare.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class EcareContsHistoryVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private int no;
    private int ecareNo;
    private String ecareNm;
    private String seg;
    private String template;
    private int tmplVer;
    private String userNm;
    private String lastupdateDt;
    private String lastupdateTm;
    private int handlerVer;
    private String contsFlag;
    private String source;
    private String channelType;
    private String historyMsg;
    private String previewTemplate;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getEcareNo() {
        return ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getEcareNm() {
        return ecareNm;
    }

    public void setEcareNm(String ecareNm) {
        this.ecareNm = ecareNm;
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

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getLastupdateDt() {
        return lastupdateDt;
    }

    public void setLastupdateDt(String lastupdateDt) {
        this.lastupdateDt = lastupdateDt;
    }

    public String getLastupdateTm() {
        return lastupdateTm;
    }

    public void setLastupdateTm(String lastupdateTm) {
        this.lastupdateTm = lastupdateTm;
    }

    public int getHandlerVer() {
        return handlerVer;
    }

    public void setHandlerVer(int handlerVer) {
        this.handlerVer = handlerVer;
    }

    public String getContsFlag() {
        return contsFlag;
    }

    public void setContsFlag(String contsFlag) {
        this.contsFlag = contsFlag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getHistoryMsg() {
        return historyMsg;
    }

    public void setHistoryMsg(String historyMsg) {
        this.historyMsg = historyMsg;
    }

    public String getPreviewTemplate() {
        return previewTemplate;
    }

    public void setPreviewTemplate(String previewTemplate) {
        this.previewTemplate = previewTemplate;
    }

}
