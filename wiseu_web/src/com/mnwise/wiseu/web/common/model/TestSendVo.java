package com.mnwise.wiseu.web.common.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class TestSendVo extends SearchVo {
    private static final long serialVersionUID = -131494012430444325L;

    private int serviceNo;
    private String listSeq;
    private String customerNm;
    private String customerEmail;
    private String sendDt;
    private String sendTm;
    private String errorCd;
    private String errorMsg;
    private String errMsgKor;

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getListSeq() {
        return listSeq;
    }

    public void setListSeq(String listSeq) {
        this.listSeq = listSeq;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerName) {
        this.customerNm = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSendDt() {
        if(sendDt.indexOf(".") == 4) {
            return sendDt;
        }
        String year = sendDt.substring(0, 4);
        String month = sendDt.substring(4, 6);
        String day = sendDt.substring(6, 8);
        sendDt = year + "." + month + "." + day;
        return sendDt;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setErrMsgKor(String errMsgKor) {
        this.errMsgKor = errMsgKor;
    }

    public String getErrMsgKor() {
        return errMsgKor;
    }

    public void setSendTm(String sendTm) {
        this.sendTm = sendTm;
    }

    public String getSendTm() {
        if(sendTm.indexOf(":") == 2) {
            return sendTm;
        }
        String hour = sendTm.substring(0, 2);
        String min = sendTm.substring(2, 4);
        String mil = sendTm.substring(4, 6);
        sendTm = hour + ":" + min + ":" + mil;
        return sendTm;
    }

}
