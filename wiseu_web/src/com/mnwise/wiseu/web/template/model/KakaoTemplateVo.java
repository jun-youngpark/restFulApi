package com.mnwise.wiseu.web.template.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTemplateVo {
    private List<KakaoButton> buttons;
    private List<KakaoQuickReply> quickReplies;
    private List<KakaoTemplateComment> comments;
    private String buttonName;
    private String buttonType;
    private String buttonUrl;
    private String categoryCode;
    private String createdAt;
    private String inspectionStatus;
    private String modifiedAt;
    private String securityFlag;
    private String senderKey;
    private String senderKeyType;
    private String status;
    private String templateAd;
    private String templateCode;
    private String templateContent;
    private String templateEmphasizeType;
    private String templateExtra;
    private String templateMessageType;
    private String templateName;
    private String templateSubtitle;
    private String templateTitle;
    private String templateImageUrl;
    private String templateImageName;


    public String getTemplateImageUrl() {
        return templateImageUrl;
    }

    public void setTemplateImageUrl(String templateImageUrl) {
        this.templateImageUrl = templateImageUrl;
    }

    public String getTemplateImageName() {
        return templateImageName;
    }

    public void setTemplateImageName(String templateImageName) {
        this.templateImageName = templateImageName;
    }

    public String getButtonName() {
        return buttonName;
    }

    public List<KakaoButton> getButtons() {
        return buttons;
    }

    public String getButtonType() {
        return buttonType;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public List<KakaoTemplateComment> getComments() {
        return comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public String convertButtonsToJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for(KakaoButton button : buttons) {
            sb.append(button.toJsonString()).append(",");
        }

        sb.append("]");

        return sb.toString().replaceAll(",]", "]").replaceAll("\\[\\]", "");
    }

    public String convertQuickRepliesToJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for(KakaoQuickReply quickReply : quickReplies) {
            sb.append(quickReply.toJsonString()).append(",");
        }

        sb.append("]");

        return sb.toString().replaceAll(",]", "]").replaceAll("\\[\\]", "");
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public String getSenderKeyType() {
        return senderKeyType;
    }

    public String getStatus() {
        return status;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public void setButtons(List<KakaoButton> buttons) {
        this.buttons = buttons;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public void setComments(List<KakaoTemplateComment> comments) {
        this.comments = comments;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public void setSenderKeyType(String senderKeyType) {
        this.senderKeyType = senderKeyType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<KakaoQuickReply> getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(List<KakaoQuickReply> quickReplies) {
        this.quickReplies = quickReplies;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSecurityFlag() {
        return securityFlag;
    }

    public void setSecurityFlag(String securityFlag) {
        this.securityFlag = securityFlag;
    }

    public String getTemplateAd() {
        return templateAd;
    }

    public void setTemplateAd(String templateAd) {
        this.templateAd = templateAd;
    }

    public String getTemplateEmphasizeType() {
        return templateEmphasizeType;
    }

    public void setTemplateEmphasizeType(String templateEmphasizeType) {
        this.templateEmphasizeType = templateEmphasizeType;
    }

    public String getTemplateExtra() {
        return templateExtra;
    }

    public void setTemplateExtra(String templateExtra) {
        this.templateExtra = templateExtra;
    }

    public String getTemplateMessageType() {
        return templateMessageType;
    }

    public void setTemplateMessageType(String templateMessageType) {
        this.templateMessageType = templateMessageType;
    }

    public String getTemplateSubtitle() {
        return templateSubtitle;
    }

    public void setTemplateSubtitle(String templateSubtitle) {
        this.templateSubtitle = templateSubtitle;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

}
