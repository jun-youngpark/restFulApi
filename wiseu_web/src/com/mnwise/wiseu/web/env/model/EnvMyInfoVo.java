package com.mnwise.wiseu.web.env.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

public class EnvMyInfoVo extends BaseVo {

    /**
     * 
     */
    private static final long serialVersionUID = -3485167449181922949L;
    private String userId;
    private String usertypeCd;
    private String lastupdateDt;
    private String passWd;
    private String passSalt;
    private String passWdChk;
    private String nameKor;
    private String userClass;
    private String telNo;
    private String email;
    private String listCountPerPage;

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    public String getListCountPerPage() {
        return listCountPerPage;
    }

    public void setListCountPerPage(String listCountPerPage) {
        this.listCountPerPage = listCountPerPage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsertypeCd() {
        return usertypeCd;
    }

    public void setUsertypeCd(String usertypeCd) {
        this.usertypeCd = usertypeCd;
    }

    public String getLastupdateDt() {
        return lastupdateDt;
    }

    public void setLastupdateDt(String lastupdateDt) {
        this.lastupdateDt = lastupdateDt;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getPassWdChk() {
        return passWdChk;
    }

    public void setPassWdChk(String passWdChk) {
        this.passWdChk = passWdChk;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
