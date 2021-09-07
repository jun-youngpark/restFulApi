package com.mnwise.wiseu.web.report.model.campaign;

import java.io.Serializable;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * 캠페인 오류 리포트 VO
 * 
 * @author hkwee
 * @since 2010-02-04
 */
public class CampaignReportErrorVo extends SearchVo implements Serializable {

    private static final long serialVersionUID = 7305963043956499789L;

    private int campaignNo;
    private String errorGubun;
    private String errorCd;
    private String errorMsg;

    private String customerNm;
    private String customerEmail;
    private String sendDtm;
    private String domainNm;

    private int sendCnt;
    private int rnum;
    private int gid;

    public void setErrorGubun(String errorGubun) {
        this.errorGubun = errorGubun;
    }

    public String getErrorGubun() {
        return errorGubun;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setRnum(int rnum) {
        this.rnum = rnum;
    }

    public int getRnum() {
        return rnum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSendDtm() {
        return sendDtm;
    }

    public void setSendDtm(String sendDtm) {
        this.sendDtm = sendDtm;
    }

    public String getDomainNm() {
        return domainNm;
    }

    public void setDomainNm(String domainNm) {
        this.domainNm = domainNm;
    }

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }
}
