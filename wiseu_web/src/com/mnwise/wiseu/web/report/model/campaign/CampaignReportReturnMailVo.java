package com.mnwise.wiseu.web.report.model.campaign;

/**
 * 캠페인 리턴메일 Vo
 */
public class CampaignReportReturnMailVo {

    private String errorGubun;
    private String smtpCode;
    private String errorMsg;
    private int sendCnt;
    private int gid;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getErrorGubun() {
        return errorGubun;
    }

    public void setErrorGubun(String errorGubun) {
        this.errorGubun = errorGubun;
    }

    public String getSmtpCode() {
        return smtpCode;
    }

    public void setSmtpCode(String smtpCode) {
        this.smtpCode = smtpCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

}