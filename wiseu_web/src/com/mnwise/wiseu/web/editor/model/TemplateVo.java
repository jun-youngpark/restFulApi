package com.mnwise.wiseu.web.editor.model;

import com.mnwise.common.util.StringUtil;

/**
 * NVTEMPLATE 테이블 모델 클래스
 */
public class TemplateVo {
    private int campaignNo;
    private String seg;
    private String template;
    private String kakaoButtons = "";

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int no;
    private String dbTemplate;
    private int segNo;
    private int result;
    private int beginIndex;
    private int endIndex;
    private String imageUrl;
    private String exceptionMsg;
    private String uploadType;
    private String handlerType;
    private int tmplVer;
    private String kakaoSenderKey = "";
    private String kakaoTmplCd = "";
    private int contsNo;    //mobilecontents 테이블 join key
    private String kakaoQuickReplies;   //바로연결
    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter

    public int getCampaignNo() {
        return this.campaignNo;
    }

    public String getKakaoQuickReplies() {
        return kakaoQuickReplies;
    }

    public void setKakaoQuickReplies(String kakaoQuickReplies) {
        this.kakaoQuickReplies = kakaoQuickReplies;
    }

    public int getContsNo() {
        return contsNo;
    }


    public void setContsNo(int contsNo) {
        this.contsNo = contsNo;
    }


    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getSeg() {
        return this.seg;
    }

    public void setSeg(String seg) {
        this.seg = seg;
    }

    public String getTemplate() {
        return StringUtil.isBlank(template) ? "" : template.replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getKakaoButtons() {
        return StringUtil.defaultIfBlank(kakaoButtons, " ");
    }

    public void setKakaoButtons(String kakaoButtons) {
        this.kakaoButtons = kakaoButtons;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getDbTemplate() {
        return dbTemplate;
    }

    public void setDbTemplate(String dbTemplate) {
        this.dbTemplate = dbTemplate;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSegNo() {
        return segNo;
    }

    public void setSegNo(int segNo) {
        this.segNo = segNo;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public int getTmplVer() {
        return tmplVer;
    }

    public void setTmplVer(int tmplVer) {
        this.tmplVer = tmplVer;
    }

    public String getKakaoSenderKey() {
        return kakaoSenderKey;
    }

    public void setKakaoSenderKey(String kakaoSenderKey) {
        this.kakaoSenderKey = kakaoSenderKey;
    }

    public String getKakaoTmplCd() {
        return kakaoTmplCd;
    }

    public void setKakaoTmplCd(String kakaoTmplCd) {
        this.kakaoTmplCd = kakaoTmplCd;
    }
}
