package com.mnwise.wiseu.web.ecare.model;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVECMSCHEDULE 테이블 모델 클래스
 */
public class EcareScheduleVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int ecmScheduleNo;
    private String ecmScheduleNm;
    private String cycleCd;
    private String sendStartDt;
    private String sendEndDt;
    private String invokeTm;
    private String invokeEveryMin;
    private int day;
    private int scheWeeknumber;
    private String weekday;
    private String startTm;
    private String endTm;
    private String termMin;
    private String monthOpt;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int newEcmScheduleNo;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getEcmScheduleNo() {
        return this.ecmScheduleNo;
    }


    public String getMonthOpt() {
        return monthOpt;
    }


    public void setMonthOpt(String monthOpt) {
        this.monthOpt = monthOpt;
    }


    public void setEcmScheduleNo(int ecmScheduleNo) {
        this.ecmScheduleNo = ecmScheduleNo;
    }

    public String getEcmScheduleNm() {
        return this.ecmScheduleNm;
    }

    public void setEcmScheduleNm(String ecmScheduleNm) {
        this.ecmScheduleNm = ecmScheduleNm;
    }

    public String getCycleCd() {
        return this.cycleCd;
    }

    public void setCycleCd(String cycleCd) {
        this.cycleCd = cycleCd;
    }

    public String getSendStartDt() {
        return this.sendStartDt;
    }

    public void setSendStartDt(String sendStartDt) {
        this.sendStartDt = sendStartDt;
    }

    public String getSendEndDt() {
        return this.sendEndDt;
    }

    public void setSendEndDt(String sendEndDt) {
        this.sendEndDt = sendEndDt;
    }

    public String getInvokeTm() {
        return this.invokeTm;
    }

    public void setInvokeTm(String invokeTm) {
        this.invokeTm = invokeTm;
    }

    public String getInvokeEveryMin() {
        return this.invokeEveryMin;
    }

    public void setInvokeEveryMin(String invokeEveryMin) {
        this.invokeEveryMin = invokeEveryMin;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getScheWeeknumber() {
        return this.scheWeeknumber;
    }

    public void setScheWeeknumber(int scheWeeknumber) {
        this.scheWeeknumber = scheWeeknumber;
    }

    public String getWeekday() {
        return this.weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStartTm() {
        return this.startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public String getEndTm() {
        return this.endTm;
    }

    public void setEndTm(String endTm) {
        this.endTm = endTm;
    }

    public String getTermMin() {
        return this.termMin;
    }

    public void setTermMin(String termMin) {
        this.termMin = termMin;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public int getNewEcmScheduleNo() {
        return newEcmScheduleNo;
    }

    public void setNewEcmScheduleNo(int newEcmScheduleNo) {
        this.newEcmScheduleNo = newEcmScheduleNo;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getStartTmToDateStr() {
        if(StringUtil.isNotBlank(startTm)) {
            startTm = startTm.substring(0, 2) + ":" + startTm.substring(2, 4);
        }

        return startTm;
    }

    public String getEndTmToDateStr() {
        if(StringUtil.isNotBlank(endTm)) {
            endTm = endTm.substring(0, 2) + ":" + endTm.substring(2, 4);
        }

        return endTm;
    }

}
