package com.mnwise.wiseu.web.editor.model;

import java.util.List;

public class EcareEditorVo {
    private int no;
    private String ecarePreface;
    private int templateType;
    private Integer surveyNo;
    private String ecareSts;
    private String htmlUpdateYn;
    private String senderNm;
    private String senderEmail;
    private String receiverNm;
    private String retmailReceiver;
    private String senderTel;
    private int retryCnt;
    private String mailType;

    // 20170321 홍휘정 버전추가
    private int tmplVer;
    private int prefaceVer;
    private int coverVer;
    private int handlerVer;

    /** 기본핸들러 SEQ */
    private int handlerSeq;
    private String handler;

    // kakao
    private String kakaoButtons;
    private String kakaoSenderkey;
    private String kakaoTemplateCd;
    private int kakaoImageNo;
    // ecare
    private String serviceType;
    private String userId;
    private String subType;
    private String historyMsg;
    private boolean isRealSave;
    private List<TemplateVo> templateVos;
    //첨부파일 데이터(json)
    private List<MultipartFileVo> multipartFileVos;
    private int segmentNo;
    private String handlerType;
    private String channelType;

    public int getKakaoImageNo() {
        return kakaoImageNo;
    }
    public void setKakaoImageNo(int kakaoImageNo) {
        this.kakaoImageNo = kakaoImageNo;
    }
    private String pushData;

    public String getPushData() {
        return pushData;
    }
    public void setPushData(String pushData) {
        this.pushData = pushData;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getKakaoButtons() {
        return kakaoButtons;
    }

    public void setKakaoButtons(String kakaoButtons) {
        this.kakaoButtons = kakaoButtons;
    }

    public String getKakaoSenderkey() {
        return kakaoSenderkey;
    }

    public void setKakaoSenderkey(String kakaoSenderkey) {
        this.kakaoSenderkey = kakaoSenderkey;
    }

    public String getKakaoTemplateCd() {
        return kakaoTemplateCd;
    }

    public void setKakaoTemplateCd(String kakaoTemplateCd) {
        this.kakaoTemplateCd = kakaoTemplateCd;
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

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getHistoryMsg() {
        return historyMsg;
    }

    public void setHistoryMsg(String historyMsg) {
        this.historyMsg = historyMsg;
    }

    public boolean isRealSave() {
        return isRealSave;
    }

    public void setRealSave(boolean isRealSave) {
        this.isRealSave = isRealSave;
    }

    public List<TemplateVo> getTemplateVos() {
        return templateVos;
    }

    public void setTemplateVos(List<TemplateVo> templateVos) {
        this.templateVos = templateVos;
    }

    public List<MultipartFileVo> getMultipartFileVos() {
        return multipartFileVos;
    }

    public void setMultipartFileVos(List<MultipartFileVo> multipartFileVos) {
        this.multipartFileVos = multipartFileVos;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getEcarePreface() {
        return ecarePreface;
    }

    public void setEcarePreface(String ecarePreface) {
        this.ecarePreface = ecarePreface;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public Integer getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(Integer surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getEcareSts() {
        return ecareSts;
    }

    public void setEcareSts(String ecareSts) {
        this.ecareSts = ecareSts;
    }

    public String getHtmlUpdateYn() {
        return htmlUpdateYn;
    }

    public void setHtmlUpdateYn(String htmlUpdateYn) {
        this.htmlUpdateYn = htmlUpdateYn;
    }

    public String getSenderNm() {
        return senderNm;
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverNm() {
        return receiverNm;
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getRetmailReceiver() {
        return retmailReceiver;
    }

    public void setRetmailReceiver(String retmailReceiver) {
        this.retmailReceiver = retmailReceiver;
    }

    public String getSenderTel() {
        return senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public int getRetryCnt() {
        return retryCnt;
    }

    public void setRetryCnt(int retryCnt) {
        this.retryCnt = retryCnt;
    }

    public int getTmplVer() {
        return tmplVer;
    }

    public void setTmplVer(int tmplVer) {
        this.tmplVer = tmplVer;
    }

    public int getPrefaceVer() {
        return prefaceVer;
    }

    public void setPrefaceVer(int prefaceVer) {
        this.prefaceVer = prefaceVer;
    }

    public int getCoverVer() {
        return coverVer;
    }

    public void setCoverVer(int coverVer) {
        this.coverVer = coverVer;
    }

    public int getHandlerVer() {
        return handlerVer;
    }

    public void setHandlerVer(int handlerVer) {
        this.handlerVer = handlerVer;
    }

    public int getHandlerSeq() {
        return handlerSeq;
    }

    public void setHandlerSeq(int handlerSeq) {
        this.handlerSeq = handlerSeq;
    }
}
