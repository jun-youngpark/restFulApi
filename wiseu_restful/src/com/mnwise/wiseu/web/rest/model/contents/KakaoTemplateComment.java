package com.mnwise.wiseu.web.rest.model.contents;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoTemplateComment {
    private String content;
    private String createdAt;
    private int id;
    private String status;
    private String userName;
    private List<KakaoAttachMent> attachment;

    public List<KakaoAttachMent> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<KakaoAttachMent> attachment) {
        this.attachment = attachment;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
