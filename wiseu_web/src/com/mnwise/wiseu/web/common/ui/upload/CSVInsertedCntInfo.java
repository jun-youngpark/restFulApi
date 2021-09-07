package com.mnwise.wiseu.web.common.ui.upload;

public class CSVInsertedCntInfo {
    private int totalCnt;
    private int insertedCnt;
    private boolean isInProgress;

    public CSVInsertedCntInfo() {
        totalCnt = 0;
        insertedCnt = 0;
        isInProgress = false;
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

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }
}
