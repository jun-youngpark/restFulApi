package com.mnwise.wiseu.web.account.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVUSER 테이블 모델 클래스
 */
public class UserVo extends BaseVo {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String passWd;
    private String passSalt;
    private String grpCd;
    private String nameKor;
    private String nameEng;
    private String telNo;
    private String email;
    private String userClass;
    private String userTypeCd;
    private String lastUpdateDt;
    private String activeYn;
    private String acceptYn;
    private int listCountPerPage;
    private String soeid;
    private String geid;
    private String isaRole;
    private String workDoc;
    private String userRole;
    private String createUser;
    private String permitSts;
    private String permitDt;
    private int loginCnt;
    private String lastLoginDt;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String grpNm;

    /** SMS 부서별 과금을 위한 요청자 부서 코드 */
    private String reqDept;

    /** SMS 부서별 과금을 위한 요청자 아이디 */
    private String reqUser;

    private String userTypeNm;
    private String defaultPassYn;

    private String roleName;
    private String roleDesc;
    private String[] saUserId;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWd() {
        return this.passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getPassSalt() {
        return this.passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getNameKor() {
        return this.nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getNameEng() {
        return this.nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getTelNo() {
        return this.telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserClass() {
        return this.userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserTypeCd() {
        return userTypeCd;
    }

    public void setUserTypeCd(String userTypeCd) {
        this.userTypeCd = userTypeCd;
    }

    public String getLastUpdateDt() {
        return this.lastUpdateDt;
    }

    public void setLastUpdateDt(String lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public String getActiveYn() {
        return this.activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    public String getAcceptYn() {
        return this.acceptYn;
    }

    public void setAcceptYn(String acceptYn) {
        this.acceptYn = acceptYn;
    }

    public int getListCountPerPage() {
        return this.listCountPerPage;
    }

    public void setListCountPerPage(int listCountPerPage) {
        this.listCountPerPage = listCountPerPage;
    }

    public String getSoeid() {
        return this.soeid;
    }

    public void setSoeid(String soeid) {
        this.soeid = soeid;
    }

    public String getGeid() {
        return this.geid;
    }

    public void setGeid(String geid) {
        this.geid = geid;
    }

    public String getIsaRole() {
        return this.isaRole;
    }

    public void setIsaRole(String isaRole) {
        this.isaRole = isaRole;
    }

    public String getWorkDoc() {
        return this.workDoc;
    }

    public void setWorkDoc(String workDoc) {
        this.workDoc = workDoc;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getPermitSts() {
        return this.permitSts;
    }

    public void setPermitSts(String permitSts) {
        this.permitSts = permitSts;
    }

    public String getPermitDt() {
        return this.permitDt;
    }

    public void setPermitDt(String permitDt) {
        this.permitDt = permitDt;
    }

    public int getLoginCnt() {
        return this.loginCnt;
    }

    public void setLoginCnt(int loginCnt) {
        this.loginCnt = loginCnt;
    }

    public String getLastLoginDt() {
        return this.lastLoginDt;
    }

    public void setLastLoginDt(String lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getReqDept() {
        return reqDept;
    }

    public void setReqDept(String reqDept) {
        this.reqDept = reqDept;
    }

    public String getReqUser() {
        return reqUser;
    }

    public void setReqUser(String reqUser) {
        this.reqUser = reqUser;
    }

    public String getUserTypeNm() {
        return userTypeNm;
    }

    public void setUserTypeNm(String userTypeNm) {
        this.userTypeNm = userTypeNm;
    }

    public String getDefaultPassYn() {
        return defaultPassYn;
    }

    public void setDefaultPassYn(String defaultPassYn) {
        this.defaultPassYn = defaultPassYn;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String[] getSaUserId() {
        return saUserId;
    }

    public void setSaUserId(String[] saUserId) {
        this.saUserId = saUserId;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getUserRoleDesc() {
        return (userRole == null || userRole.equals("N")) ? "N/A" : (userRole.equals("M")) ? "Maker" : "Checker";
    }
}
