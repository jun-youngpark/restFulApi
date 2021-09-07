package com.mnwise.wiseu.web.report.model.campaign;

import java.io.Serializable;

/**
 * 캠페인 기본정보 Vo
 * 
 * @author hjlee
 */
public class CampaignAbTestVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2606887820663994013L;
    private String abType;
    private int sendCnt;
    private int openCnt;
    private double totRate;

    public double getTotRate() {
        return totRate;
    }

    public void setTotRate(double totRate) {
        this.totRate = totRate;
    }

    public String getAbType() {
        return abType;
    }

    public void setAbType(String abType) {
        this.abType = abType;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

}
