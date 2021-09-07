package com.mnwise.wiseu.web.ecare.model;

import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * NVCYCLEITEM 테이블 모델 클래스
 */
public class CycleItemVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int ecmScheduleNo;
    private String cycleItem;
    private String checkYn;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getEcmScheduleNo() {
        return ecmScheduleNo;
    }

    public void setEcmScheduleNo(int ecmScheduleNo) {
        this.ecmScheduleNo = ecmScheduleNo;
    }

    public String getCycleItem() {
        return cycleItem;
    }

    public void setCycleItem(String cycleItem) {
        this.cycleItem = cycleItem;
    }

    public String getCheckYn() {
        return checkYn;
    }

    public void setCheckYn(String checkYn) {
        this.checkYn = checkYn;
    }

}
