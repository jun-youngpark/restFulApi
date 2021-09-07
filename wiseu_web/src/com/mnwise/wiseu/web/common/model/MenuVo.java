package com.mnwise.wiseu.web.common.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVMENU 테이블 모델 클래스
 */
public class MenuVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private String menuCd;
    private String pmenuCd;
    private int levelNo;
    private String menuNm;
    private String menuLinkUrl;
    private String menuIconImg;
    private String activeYn;
    private String moduleNm;
    private int sortNo;
    private String mType;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String userId;
    private String grpCd;
    private String readAuth;
    private String writeAuth;
    private String executeAuth;
    private String rollOver;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getMenuCd() {
        return this.menuCd;
    }

    public void setMenuCd(String menuCd) {
        this.menuCd = menuCd;
    }

    public String getPmenuCd() {
        return this.pmenuCd;
    }

    public void setPmenuCd(String pmenuCd) {
        this.pmenuCd = pmenuCd;
    }

    public int getLevelNo() {
        return this.levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public String getMenuNm() {
        return this.menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }

    public String getMenuLinkUrl() {
        return this.menuLinkUrl;
    }

    public void setMenuLinkUrl(String menuLinkUrl) {
        this.menuLinkUrl = menuLinkUrl;
    }

    public String getMenuIconImg() {
        return this.menuIconImg;
    }

    public void setMenuIconImg(String menuIconImg) {
        this.menuIconImg = menuIconImg;
    }

    public String getActiveYn() {
        return this.activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    public String getModuleNm() {
        return this.moduleNm;
    }

    public void setModuleNm(String moduleNm) {
        this.moduleNm = moduleNm;
    }

    public int getSortNo() {
        return this.sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public String getMType() {
        return this.mType;
    }

    public void setMType(String mType) {
        this.mType = mType;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getReadAuth() {
        return readAuth;
    }

    public void setReadAuth(String readAuth) {
        this.readAuth = readAuth;
    }

    public String getWriteAuth() {
        return writeAuth;
    }

    public void setWriteAuth(String writeAuth) {
        this.writeAuth = writeAuth;
    }

    public String getExecuteAuth() {
        return executeAuth;
    }

    public void setExecuteAuth(String executeAuth) {
        this.executeAuth = executeAuth;
    }

    public String getRollOver() {
        return rollOver;
    }

    public void setRollOver(String rollOver) {
        this.rollOver = rollOver;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getStepExMenuLinkUrl() {
        if(menuLinkUrl == null)
            return menuLinkUrl;
        else
            return this.menuLinkUrl.replaceAll("\\d", "");
    }

}
