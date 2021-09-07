package com.mnwise.wiseu.web.editor.model;

public class CampaignEditorVo {
    private int no;
    private String campaignPreface;
    private int templateType;
    private Integer surveyNo;
    private String campaignSts;
    private String htmlUpdateYn;

    /** A/B테스트 제목 */
    private String campaignPrefaceAb;
    /** 기본핸들러 SEQ */
    private int handlerSeq;

    public String getCampaignPrefaceAb() {
        return campaignPrefaceAb;
    }

    public void setCampaignPrefaceAb(String campaignPrefaceAb) {
        this.campaignPrefaceAb = campaignPrefaceAb;
    }

    public String getHtmlUpdateYn() {
        return htmlUpdateYn;
    }

    public void setHtmlUpdateYn(String htmlUpdateYn) {
        this.htmlUpdateYn = htmlUpdateYn;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCampaignPreface() {
        return campaignPreface;
    }

    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
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

    public String getCampaignSts() {
        return campaignSts;
    }

    public void setCampaignSts(String campaignSts) {
        this.campaignSts = campaignSts;
    }

    public int getHandlerSeq() {
        return handlerSeq;
    }

    public void setHandlerSeq(int handlerSeq) {
        this.handlerSeq = handlerSeq;
    }
}
