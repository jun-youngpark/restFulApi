package com.mnwise.wiseu.web.dbclient.model;

public class DataBaseClientVo {
    /**
     * @변경이력 {10.08.02} {유선문} { Exception 발생시 에러 메시지 추가를 위한 errMsg 추가}
     * @author {미상}
     * @since {미상}
     */
    private String dbms = null;
    private String serverName = null;
    private String jdbcUrl = null;
    private String jdbcDriver = null;
    private String jdbcUserName = null;
    private String jdbcPassWord = null;
    private String errMsg = null;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private String query = null;

    private String columnNm = null;

    public String getDbms() {
        return dbms;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUserName() {
        return jdbcUserName;
    }

    public void setJdbcUserName(String jdbcUserName) {
        this.jdbcUserName = jdbcUserName;
    }

    public String getJdbcPassWord() {
        return jdbcPassWord;
    }

    public void setJdbcPassWord(String jdbcPassWord) {
        this.jdbcPassWord = jdbcPassWord;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getColumnNm() {
        return columnNm;
    }

    public void setColumnNm(String columnNm) {
        this.columnNm = columnNm;
    }

}
