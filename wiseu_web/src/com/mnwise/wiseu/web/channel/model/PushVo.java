package com.mnwise.wiseu.web.channel.model;

import com.mnwise.wiseu.web.editor.model.EditorVo;

public class PushVo extends EditorVo {

    private String svcType;
    private String pushAppId;
    private String pushMsgInfo;
    private String pushMenuId;
    private String pushPopImgUse; 
    private String pushImgUrl;
    private String pushBigImgUse;
    private String pushBigImgUrl;
    private String pushWebUrl;
    
    public String getSvcType() {
        return svcType;
    }
    public void setSvcType(String svcType) {
        this.svcType = svcType;
    }
    public String getPushAppId() {
        return pushAppId;
    }
    public void setPushAppId(String pushAppId) {
        this.pushAppId = pushAppId;
    }
    public String getPushMsgInfo() {
        return pushMsgInfo;
    }
    public void setPushMsgInfo(String pushMsgInfo) {
        this.pushMsgInfo = pushMsgInfo;
    }
    public String getPushMenuId() {
        return pushMenuId;
    }
    public void setPushMenuId(String pushMenuId) {
        this.pushMenuId = pushMenuId;
    }
    public String getPushPopImgUse() {
        return pushPopImgUse;
    }
    public void setPushPopImgUse(String pushPopImgUse) {
        this.pushPopImgUse = pushPopImgUse;
    }
    public String getPushImgUrl() {
        return pushImgUrl;
    }
    public void setPushImgUrl(String pushImgUrl) {
        this.pushImgUrl = pushImgUrl;
    }
    public String getPushBigImgUse() {
        return pushBigImgUse;
    }
    public void setPushBigImgUse(String pushBigImgUse) {
        this.pushBigImgUse = pushBigImgUse;
    }
    public String getPushBigImgUrl() {
        return pushBigImgUrl;
    }
    public void setPushBigImgUrl(String pushBigImgUrl) {
        this.pushBigImgUrl = pushBigImgUrl;
    }
    public String getPushWebUrl() {
        return pushWebUrl;
    }
    public void setPushWebUrl(String pushWebUrl) {
        this.pushWebUrl = pushWebUrl;
    }
    
    

}
