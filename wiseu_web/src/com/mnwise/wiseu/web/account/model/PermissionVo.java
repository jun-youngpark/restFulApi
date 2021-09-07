package com.mnwise.wiseu.web.account.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * 권한 관리 Vo
 */
public class PermissionVo extends SearchVo {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userType;
    private String menuCd;
    private String readAuth;
    private String writeAuth;
    private String executeAuth;
    private String pmenuCd;
    private String menuNm;
    private String grpCd;
    private String gubun;
    private String roleId;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMenuCd() {
        return menuCd;
    }

    public void setMenuCd(String menuCd) {
        this.menuCd = menuCd;
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

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
