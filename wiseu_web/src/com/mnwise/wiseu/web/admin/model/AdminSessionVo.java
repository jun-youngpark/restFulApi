package com.mnwise.wiseu.web.admin.model;

import java.util.Map;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.model.BaseVo;

public class AdminSessionVo extends BaseVo {
    private static final long serialVersionUID = 1L;
    private UserVo userVo;
    private String goUrl;
    private Map<String, Map<String, String>> roleMap;
    private String permssion;
    private String indexUrl;

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public String getGoUrl() {
        return goUrl;
    }

    public void setGoUrl(String goUrl) {
        this.goUrl = goUrl;
    }

    public Map<String, Map<String, String>> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(Map<String, Map<String, String>> roleMap) {
        this.roleMap = roleMap;
    }

    public String getPermssion() {
        return permssion;
    }

    public void setPermssion(String permssion) {
        this.permssion = permssion;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }
}