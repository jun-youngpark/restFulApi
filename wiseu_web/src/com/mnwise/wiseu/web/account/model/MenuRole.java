package com.mnwise.wiseu.web.account.model;


/**
 * NVMENUROLE 테이블 모델 클래스
 */
public class MenuRole {
    private String userId;
    private String menuCd;
    private String readAuth;
    private String writeAuth;
    private String executeAuth;

    // 추가 멤버변수
    private String pmenuCd;
    private String menuNm;
    private String menuLinkUrl;

    // 생성자
    /**
     * 기본 생성자
     */
    public MenuRole() {
        super();
    }

    // 기본 getter/setter
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMenuCd() {
        return this.menuCd;
    }

    public void setMenuCd(String menuCd) {
        this.menuCd = menuCd;
    }

    public String getReadAuth() {
        return this.readAuth;
    }

    public void setReadAuth(String readAuth) {
        this.readAuth = readAuth;
    }

    public String getWriteAuth() {
        return this.writeAuth;
    }

    public void setWriteAuth(String writeAuth) {
        this.writeAuth = writeAuth;
    }

    public String getExecuteAuth() {
        return this.executeAuth;
    }

    public void setExecuteAuth(String executeAuth) {
        this.executeAuth = executeAuth;
    }

    // 추가 getter/setter
    public String getPmenuCd() {
        return pmenuCd;
    }

    public void setPmenuCd(String pmenuCd) {
        this.pmenuCd = pmenuCd;
    }

    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }

    public String getMenuLinkUrl() {
        return menuLinkUrl;
    }

    public void setMenuLinkUrl(String menuLinkUrl) {
        this.menuLinkUrl = menuLinkUrl;
    }


}