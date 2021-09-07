package com.mnwise.wiseu.web.common.model;

/**
 * @변경이력 : 20100824 전석하 미리보기 시 대상자 쿼리를 불러올 때 DB별로 코덱정보가 다르므로 기존 APPLICATION.CONF 에서 가져오던 코덱정보를 NVDBINFO 테이블에서 가져오도록 수정함
 **/

public class TargetQueryInfo {
    private String dbInfoSeq;
    private String sqlHead;
    private String sqlBody;
    private String sqlTail;
    private String dbKind;
    private String driverNm;
    private String driverDsn;
    private String dbUserId;
    private String dbPassword;
    private String encoding;
    private String decoding;
    private String testQuery;
    private String updateQuery;

    public String getUpdateQuery() {
        return updateQuery;
    }

    public void setUpdateQuery(String updateQuery) {
        this.updateQuery = updateQuery;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }

    public String getDbInfoSeq() {
        return dbInfoSeq;
    }

    public void setDbInfoSeq(String dbInfoSeq) {
        this.dbInfoSeq = dbInfoSeq;
    }

    public String getSqlHead() {
        return sqlHead;
    }

    public void setSqlHead(String sqlHead) {
        this.sqlHead = sqlHead;
    }

    public String getSqlBody() {
        sqlBody = sqlBody.replace("AND RESULT_SEQ = \'$RSEQ$\'", "").replace("SEND_FG = \'H\'", "SEND_FG = \'R\'");
        return sqlBody;
    }

    public void setSqlBody(String sqlBody) {
        this.sqlBody = sqlBody;
    }

    public String getSqlTail() {
        return sqlTail;
    }

    public void setSqlTail(String sqlTail) {
        this.sqlTail = sqlTail;
    }

    public String getDbKind() {
        return dbKind;
    }

    public void setDbKind(String dbKind) {
        this.dbKind = dbKind;
    }

    public String getDriverNm() {
        return driverNm;
    }

    public void setDriverNm(String driverNm) {
        this.driverNm = driverNm;
    }

    public String getDriverDsn() {
        return driverDsn;
    }

    public void setDriverDsn(String driverDsn) {
        this.driverDsn = driverDsn;
    }

    public String getDbUserId() {
        return dbUserId;
    }

    public void setDbUserId(String dbUserId) {
        this.dbUserId = dbUserId;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDecoding() {
        return decoding;
    }

    public void setDecoding(String decoding) {
        this.decoding = decoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }
}
