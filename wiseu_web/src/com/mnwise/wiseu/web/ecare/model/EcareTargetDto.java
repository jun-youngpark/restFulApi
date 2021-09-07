package com.mnwise.wiseu.web.ecare.model;

public class EcareTargetDto {
    private String sqlHeadBody;
    private int dbInfoSeq;
    private String updateQuery;
    private int segmentNo;

    public int getSegmentNo() {
        return segmentNo;
    }
    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }
    public String getSqlHeadBody() {
        return sqlHeadBody;
    }
    public void setSqlHeadBody(String sqlHeadBody) {
        this.sqlHeadBody = sqlHeadBody;
    }

    public int getDbInfoSeq() {
        return dbInfoSeq;
    }
    public void setDbInfoSeq(int dbInfoSeq) {
        this.dbInfoSeq = dbInfoSeq;
    }
    public String getUpdateQuery() {
        return updateQuery;
    }
    public void setUpdateQuery(String updateQuery) {
        this.updateQuery = updateQuery;
    }

}
