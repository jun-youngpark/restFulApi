package com.mnwise.wiseu.web.account.model;

/**
 * NVGRPMENUROLE 테이블 모델 클래스
 */
public class GrpMenuRole {
    private String grpCd;
    private String menuCd;
    private String readAuth;
    private String writeAuth;
    private String executeAuth;

    // 추가 멤버변수

    // 생성자
    /**
     * 기본 생성자
     */
    public GrpMenuRole() {
        super();
    }

    // 기본 getter/setter
    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
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
}