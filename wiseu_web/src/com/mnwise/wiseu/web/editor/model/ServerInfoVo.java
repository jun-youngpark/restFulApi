package com.mnwise.wiseu.web.editor.model;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVSERVERINFO 테이블 모델 클래스
 */
public class ServerInfoVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private String hostNm;
    private String portNo;
    private String driverNm;
    private String driverDsn;
    private String dbuserId;
    private String dbpassword;
    private String openclickPath;
    private String surveyPath;
    private String linkPath;
    private String htmlmakerPath;
    private String openimagePath;
    private String durationPath;
    private String rejectPath;
    private String smtpIp;
    private String smtpPort;
    private String fulaIp;
    private String fulaPort;
    private String ftpYn;
    private String ftpUserId;
    private String ftpPassword;
    private String lastupdateDt;
    private String editorId;
    private String retDomain;
    private int retryCnt;
    private String b4SendApproveYn;
    private String b4SendVerifyYn;
    private String b4RealSendTestSendYn;
    private String aseLinkMergeParam;
    private String aseRejectMergeParam;
    private String aseOpenScriptlet;
    private String groovyLinkMergeParam;
    private String groovyRejectMergeParam;
    private String groovyOpenScriptlet;
    private String resendIncludeReturnmailYn;
    private String resendIncludeMailKeyYn;
    private String resendErrorCd;
    private String faxResendErrorCd;
    private String smsResendErrorCd;
    private String altalkResendErrorCd;
    private String pushResendErrorCd;
    private String spoolPreservePeriod;
    private String logPreservePeriod;
    private String resultFileDownloadYn;
    private String sucsResultFileDownloadYn;
    private String kakaoTemplateLastSyncDtm;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String durationTime;
    private String resendFaxErrorCd;
    private String resendAltalkErrorCd;
    private String resendSmsErrorCd;
    private String resendPushErrorCd;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public String getHostNm() {
        return this.hostNm;
    }

    public void setHostNm(String hostNm) {
        this.hostNm = hostNm;
    }

    public String getPortNo() {
        return this.portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getDriverNm() {
        return this.driverNm;
    }

    public void setDriverNm(String driverNm) {
        this.driverNm = driverNm;
    }

    public String getDriverDsn() {
        return this.driverDsn;
    }

    public void setDriverDsn(String driverDsn) {
        this.driverDsn = driverDsn;
    }

    public String getDbuserId() {
        return this.dbuserId;
    }

    public void setDbuserId(String dbuserId) {
        this.dbuserId = dbuserId;
    }

    public String getDbpassword() {
        return this.dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public String getOpenclickPath() {
        return this.openclickPath;
    }

    public void setOpenclickPath(String openclickPath) {
        this.openclickPath = openclickPath;
    }

    public String getSurveyPath() {
        return this.surveyPath;
    }

    public void setSurveyPath(String surveyPath) {
        this.surveyPath = surveyPath;
    }

    public String getLinkPath() {
        return this.linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    public String getHtmlmakerPath() {
        return this.htmlmakerPath;
    }

    public void setHtmlmakerPath(String htmlmakerPath) {
        this.htmlmakerPath = htmlmakerPath;
    }

    public String getOpenimagePath() {
        return this.openimagePath;
    }

    public void setOpenimagePath(String openimagePath) {
        this.openimagePath = openimagePath;
    }

    public String getDurationPath() {
        return this.durationPath;
    }

    public void setDurationPath(String durationPath) {
        this.durationPath = durationPath;
    }

    public String getRejectPath() {
        return this.rejectPath;
    }

    public void setRejectPath(String rejectPath) {
        this.rejectPath = rejectPath;
    }

    public String getSmtpIp() {
        return this.smtpIp;
    }

    public void setSmtpIp(String smtpIp) {
        this.smtpIp = smtpIp;
    }

    public String getSmtpPort() {
        return this.smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFulaIp() {
        return this.fulaIp;
    }

    public void setFulaIp(String fulaIp) {
        this.fulaIp = fulaIp;
    }

    public String getFulaPort() {
        return this.fulaPort;
    }

    public void setFulaPort(String fulaPort) {
        this.fulaPort = fulaPort;
    }

    public String getFtpYn() {
        return this.ftpYn;
    }

    public void setFtpYn(String ftpYn) {
        this.ftpYn = ftpYn;
    }

    public String getFtpUserId() {
        return this.ftpUserId;
    }

    public void setFtpUserId(String ftpUserId) {
        this.ftpUserId = ftpUserId;
    }

    public String getFtpPassword() {
        return this.ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getLastupdateDt() {
        return this.lastupdateDt;
    }

    public void setLastupdateDt(String lastupdateDt) {
        this.lastupdateDt = lastupdateDt;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getRetDomain() {
        return this.retDomain;
    }

    public void setRetDomain(String retDomain) {
        this.retDomain = retDomain;
    }

    public int getRetryCnt() {
        return this.retryCnt;
    }

    public void setRetryCnt(int retryCnt) {
        this.retryCnt = retryCnt;
    }

    public String getB4SendApproveYn() {
        return this.b4SendApproveYn;
    }

    public void setB4SendApproveYn(String b4SendApproveYn) {
        this.b4SendApproveYn = b4SendApproveYn;
    }

    public String getB4SendVerifyYn() {
        return this.b4SendVerifyYn;
    }

    public void setB4SendVerifyYn(String b4SendVerifyYn) {
        this.b4SendVerifyYn = b4SendVerifyYn;
    }

    public String getB4RealSendTestSendYn() {
        return this.b4RealSendTestSendYn;
    }

    public void setB4RealSendTestSendYn(String b4RealSendTestSendYn) {
        this.b4RealSendTestSendYn = b4RealSendTestSendYn;
    }

    public String getAseLinkMergeParam() {
        return this.aseLinkMergeParam;
    }

    public void setAseLinkMergeParam(String aseLinkMergeParam) {
        this.aseLinkMergeParam = aseLinkMergeParam;
    }

    public String getAseRejectMergeParam() {
        return this.aseRejectMergeParam;
    }

    public void setAseRejectMergeParam(String aseRejectMergeParam) {
        this.aseRejectMergeParam = aseRejectMergeParam;
    }

    public String getAseOpenScriptlet() {
        return this.aseOpenScriptlet;
    }

    public void setAseOpenScriptlet(String aseOpenScriptlet) {
        this.aseOpenScriptlet = aseOpenScriptlet;
    }

    public String getGroovyLinkMergeParam() {
        return this.groovyLinkMergeParam;
    }

    public void setGroovyLinkMergeParam(String groovyLinkMergeParam) {
        this.groovyLinkMergeParam = groovyLinkMergeParam;
    }

    public String getGroovyRejectMergeParam() {
        return this.groovyRejectMergeParam;
    }

    public void setGroovyRejectMergeParam(String groovyRejectMergeParam) {
        this.groovyRejectMergeParam = groovyRejectMergeParam;
    }

    public String getGroovyOpenScriptlet() {
        return this.groovyOpenScriptlet;
    }

    public void setGroovyOpenScriptlet(String groovyOpenScriptlet) {
        this.groovyOpenScriptlet = groovyOpenScriptlet;
    }

    public String getResendIncludeReturnmailYn() {
        return this.resendIncludeReturnmailYn;
    }

    public void setResendIncludeReturnmailYn(String resendIncludeReturnmailYn) {
        this.resendIncludeReturnmailYn = resendIncludeReturnmailYn;
    }

    public String getResendIncludeMailKeyYn() {
        return this.resendIncludeMailKeyYn;
    }

    public void setResendIncludeMailKeyYn(String resendIncludeMailKeyYn) {
        this.resendIncludeMailKeyYn = resendIncludeMailKeyYn;
    }

    public String getResendErrorCd() {
        return this.resendErrorCd;
    }

    public void setResendErrorCd(String resendErrorCd) {
        this.resendErrorCd = resendErrorCd;
    }

    public String getFaxResendErrorCd() {
        return this.faxResendErrorCd;
    }

    public void setFaxResendErrorCd(String faxResendErrorCd) {
        this.faxResendErrorCd = faxResendErrorCd;
    }

    public String getSmsResendErrorCd() {
        return this.smsResendErrorCd;
    }

    public void setSmsResendErrorCd(String smsResendErrorCd) {
        this.smsResendErrorCd = smsResendErrorCd;
    }

    public String getAltalkResendErrorCd() {
        return this.altalkResendErrorCd;
    }

    public void setAltalkResendErrorCd(String altalkResendErrorCd) {
        this.altalkResendErrorCd = altalkResendErrorCd;
    }

    public String getPushResendErrorCd() {
        return this.pushResendErrorCd;
    }

    public void setPushResendErrorCd(String pushResendErrorCd) {
        this.pushResendErrorCd = pushResendErrorCd;
    }

    public String getSpoolPreservePeriod() {
        return this.spoolPreservePeriod;
    }

    public void setSpoolPreservePeriod(String spoolPreservePeriod) {
        this.spoolPreservePeriod = spoolPreservePeriod;
    }

    public String getLogPreservePeriod() {
        return this.logPreservePeriod;
    }

    public void setLogPreservePeriod(String logPreservePeriod) {
        this.logPreservePeriod = logPreservePeriod;
    }

    public String getResultFileDownloadYn() {
        return this.resultFileDownloadYn;
    }

    public void setResultFileDownloadYn(String resultFileDownloadYn) {
        this.resultFileDownloadYn = resultFileDownloadYn;
    }

    public String getSucsResultFileDownloadYn() {
        return this.sucsResultFileDownloadYn;
    }

    public void setSucsResultFileDownloadYn(String sucsResultFileDownloadYn) {
        this.sucsResultFileDownloadYn = sucsResultFileDownloadYn;
    }

    public String getKakaoTemplateLastSyncDtm() {
        return this.kakaoTemplateLastSyncDtm;
    }

    public void setKakaoTemplateLastSyncDtm(String kakaoTemplateLastSyncDtm) {
        this.kakaoTemplateLastSyncDtm = kakaoTemplateLastSyncDtm;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public String getResendFaxErrorCd() {
        return resendFaxErrorCd;
    }

    public void setResendFaxErrorCd(String resendFaxErrorCd) {
        this.resendFaxErrorCd = resendFaxErrorCd;
    }

    public String getResendAltalkErrorCd() {
        return resendAltalkErrorCd;
    }

    public void setResendAltalkErrorCd(String resendAltalkErrorCd) {
        this.resendAltalkErrorCd = resendAltalkErrorCd;
    }

    public String getResendSmsErrorCd() {
        return resendSmsErrorCd;
    }

    public void setResendSmsErrorCd(String resendSmsErrorCd) {
        this.resendSmsErrorCd = resendSmsErrorCd;
    }

    public String getResendPushErrorCd() {
        return resendPushErrorCd;
    }

    public void setResendPushErrorCd(String resendPushErrorCd) {
        this.resendPushErrorCd = resendPushErrorCd;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getOpenclickPath(String client) {
        if(openclickPath.length() == 0)
            return null;

        StringBuffer sb = new StringBuffer();
        sb.append(openclickPath.substring(0, openclickPath.length() - 2));
        sb.append(client);

        return sb.toString();
    }

    public String getRejectPath(String client) {
        if(rejectPath.length() == 0)
            return null;

        StringBuffer sb = new StringBuffer();
        sb.append(rejectPath.substring(0, rejectPath.length() - 2));
        sb.append(client);

        return sb.toString();
    }

    public String getLinkPath(String client) {
        if(linkPath.length() == 0)
            return null;

        StringBuffer sb = new StringBuffer();
        sb.append(linkPath.substring(0, linkPath.length() - 2));
        sb.append(client);

        return sb.toString();
    }
}
