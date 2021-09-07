package com.mnwise.wiseu.web.segment.model;

/**
 * NVDBINFO 테이블 모델 클래스
 */
public class DbInfoVo {
    private int dbInfoSeq;
    private String driverNm;
    private String serverNm;
    private String driverDsn;
    private String dbUserId;
    private String dbPassword;
    private String encoding;
    private String decoding;
    private String dbKind;
    private String testQuery;

    // 기본 getter/setter
    public int getDbInfoSeq() {
        return this.dbInfoSeq;
    }

    public void setDbInfoSeq(int dbInfoSeq) {
        this.dbInfoSeq = dbInfoSeq;
    }

    public String getDriverNm() {
        return this.driverNm;
    }

    public void setDriverNm(String driverNm) {
        this.driverNm = driverNm;
    }

    public String getServerNm() {
        return this.serverNm;
    }

    public void setServerNm(String serverNm) {
        this.serverNm = serverNm;
    }

    public String getDriverDsn() {
        return this.driverDsn;
    }

    public void setDriverDsn(String driverDsn) {
        this.driverDsn = driverDsn;
    }

    public String getDbUserId() {
        return this.dbUserId;
    }

    public void setDbUserId(String dbUserId) {
        this.dbUserId = dbUserId;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getDecoding() {
        return this.decoding;
    }

    public void setDecoding(String decoding) {
        this.decoding = decoding;
    }

    public String getDbKind() {
        return this.dbKind;
    }

    public void setDbKind(String dbKind) {
        this.dbKind = dbKind;
    }

    public String getTestQuery() {
        return this.testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }
}
