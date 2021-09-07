package com.mnwise.wiseu.web.common.ui.upload;

import java.io.Serializable;

public class SendInfo implements Serializable {
    private static final long serialVersionUID = -8966043080772705556L;
    private int totalCnt;
    private int insertedCnt;
    private boolean isInProgress;

    public SendInfo() {
        totalCnt = 0;
        insertedCnt = 0;
        isInProgress = false;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getInsertedCnt() {
        return insertedCnt;
    }

    public void setInsertedCnt(int insertedCnt) {
        this.insertedCnt = insertedCnt;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean isInProgress) {
        this.isInProgress = isInProgress;
    }
}
