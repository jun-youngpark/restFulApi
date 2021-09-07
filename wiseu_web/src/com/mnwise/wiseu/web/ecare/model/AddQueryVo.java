package com.mnwise.wiseu.web.ecare.model;

/**
 * NVADDQUERY 테이블 모델 클래스
 */
public class AddQueryVo {
    private int ecareNo;
    private int querySeq;
    private String queryType;
    private String executeType;
    private String resultId;
    private int dbInfoSeq;
    private String query;
    public AddQueryVo() {}
    public int getDbInfoSeq() {
        return dbInfoSeq;
    }
    public void setDbInfoSeq(int dbInfoSeq) {
        this.dbInfoSeq = dbInfoSeq;
    }
    public int getEcareNo() {
        return ecareNo;
    }
    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }
    public int getQuerySeq() {
        return querySeq;
    }
    public void setQuerySeq(int querySeq) {
        this.querySeq = querySeq;
    }
    public String getQueryType() {
        return queryType;
    }
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getExecuteType() {
        return executeType;
    }
    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }
    public String getResultId() {
        return resultId;
    }
    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }

    //builder pattern
    public AddQueryVo(Builder builder) {
        this.ecareNo =builder.ecareNo;
        this.querySeq =builder.querySeq;
        this.queryType =builder.queryType;
        this.executeType =builder.executeType;
        this.resultId =builder.resultId;
        this.dbInfoSeq =builder.dbInfoSeq;
        this.query =builder.query;
    }
    //builder pattern
    public static class Builder  {
        private int ecareNo;
        private int querySeq;
        private String queryType;
        private String executeType;
        private String resultId;
        private int dbInfoSeq;
        private String query;

        public Builder(int ecareNo) {
            this.ecareNo = ecareNo;
        }

        public Builder setQuerySeq(int querySeq) {
            this.querySeq = querySeq;
            return this;
        }

        public Builder setQueryType(String queryType) {
            this.queryType = queryType;
            return this;
        }

        public Builder setExecuteType(String executeType) {
            this.executeType = executeType;
            return this;
        }
        public Builder setResultId(String resultId) {
            this.resultId = resultId;
            return this;
        }
        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder setDbInfoSeq(int dbInfoSeq) {
            this.dbInfoSeq = dbInfoSeq;
            return this;
        }
        public AddQueryVo build(){
            return new AddQueryVo(this);
        }
    }
}
