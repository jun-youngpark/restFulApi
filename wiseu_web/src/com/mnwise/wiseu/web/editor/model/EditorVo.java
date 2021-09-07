package com.mnwise.wiseu.web.editor.model;

import java.util.List;

public class EditorVo {

    private int no;
    private int segmentNo;
    private String handlerType;
    private String channel;
    private int segIndex;   //
    private String seg;
    private int templateType;
    private int linkSeq;
    private int linkSeqAb;
    private String tagType; //
    private String abTestType;
    private int handlerSeq;
    private String campaignPreface;
    private String campaignPrefaceAb;
    private String handler;
    private String template;
    private String templateContent; //
    private String templateAb;
    private String templateAbContent; //

    // sms
    private int retryCnt;
    // kakao
    private String kakaoButtons;
    private String kakaoSenderkey;
    private String kakaoTemplateCd;
    // ecare
    private String serviceType;
    private String userId;
    private String subType;
    private String historyMsg;
    private boolean isRealSave;
    private List<TemplateVo> templateVos;
    //첨부파일 데이터(json)
    private List<MultipartFileVo> multipartFileVos;

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
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public int getSegIndex() {
        return segIndex;
    }
    public void setSegIndex(int segIndex) {
        this.segIndex = segIndex;
    }
    public String getSeg() {
        return seg;
    }
    public void setSeg(String seg) {
        this.seg = seg;
    }
    public int getTemplateType() {
        return templateType;
    }
    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }
    public int getLinkSeq() {
        return linkSeq;
    }
    public void setLinkSeq(int linkSeq) {
        this.linkSeq = linkSeq;
    }
    public int getLinkSeqAb() {
        return linkSeqAb;
    }
    public void setLinkSeqAb(int linkSeqAb) {
        this.linkSeqAb = linkSeqAb;
    }
    public String getTagType() {
        return tagType;
    }
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
    public String getAbTestType() {
        return abTestType;
    }
    public void setAbTestType(String abTestType) {
        this.abTestType = abTestType;
    }
    public int getHandlerSeq() {
        return handlerSeq;
    }
    public void setHandlerSeq(int handlerSeq) {
        this.handlerSeq = handlerSeq;
    }
    public String getCampaignPreface() {
        return campaignPreface;
    }
    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
    }
    public String getCampaignPrefaceAb() {
        return campaignPrefaceAb;
    }
    public void setCampaignPrefaceAb(String campaignPrefaceAb) {
        this.campaignPrefaceAb = campaignPrefaceAb;
    }
    public String getHandler() {
        return handler;
    }
    public void setHandler(String handler) {
        this.handler = handler;
    }
    public String getTemplate() {
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public String getTemplateContent() {
        return templateContent;
    }
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    public String getTemplateAb() {
        return templateAb;
    }
    public void setTemplateAb(String templateAb) {
        this.templateAb = templateAb;
    }
    public String getTemplateAbContent() {
        return templateAbContent;
    }
    public void setTemplateAbContent(String templateAbContent) {
        this.templateAbContent = templateAbContent;
    }
    public int getRetryCnt() {
        return retryCnt;
    }
    public void setRetryCnt(int retryCnt) {
        this.retryCnt = retryCnt;
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


}
