package com.mnwise.wiseu.web.common.util;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.security.DataSecurity;
import com.mnwise.common.security.aria.Aria;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.util.xCryptionAria;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;

/**
 * 쿼리 실행기
 */
public class QueryExecutor {
    private static final Logger log = LoggerFactory.getLogger(QueryExecutor.class);

    private List<String[]> queryData;
    private String[] columnHeader;
    private String dbEnc;
    private String viewEnc;

    public QueryExecutor() {
    }

    public QueryExecutor(String dbEnc, String viewEnc) {
        this.dbEnc = dbEnc;
        this.viewEnc = viewEnc;
    }

    /**
     * 사용자가 입력한 쿼리를 실행하면서 해당 쿼리의 메타데이타 정보를 세팅한다.
     *
     * @param query
     * @return
     */
    public void executeQuery(DbInfoVo dbInfoVo, String query) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        columnHeader = null;
        queryData = null;
        if(PropertyUtil.getProperty("cipher","off").equalsIgnoreCase("on")) {
            try {
                dbInfoVo.setDbPassword(Aria.wiseuDecrypt(dbInfoVo.getDbPassword()));
            } catch(Exception e) {
                log.error(null, e);
            }
        }

        try {
            if(log.isDebugEnabled()) {
                log.debug(query);
            }

            Class.forName(dbInfoVo.getDriverNm());
            conn = DriverManager.getConnection(dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            setColumnHeader(rsmd);
            setQueryData(rs);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 사용자가 입력한 쿼리를 실행하면서 해당 쿼리의 메타데이타 정보를 세팅한다.
     *
     * @param query
     * @return
     */
    public void executeQuery(DbInfoVo dbInfoVo, String query, int pageSize, int startRow) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        columnHeader = null;
        queryData = null;
        if(PropertyUtil.getProperty("cipher","off").equalsIgnoreCase("on")) {
            try {
                dbInfoVo.setDbPassword(xCryptionAria.decrypt(dbInfoVo.getDbPassword()));
            } catch(Exception e) {
                log.error(null, e);
            }
        }

        try {
            if(log.isDebugEnabled()) {
                log.debug(query);
            }

            Class.forName(dbInfoVo.getDriverNm());

            conn = DriverManager.getConnection(dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            setColumnHeader(rsmd);
            setQueryData(rs, pageSize, startRow);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }
    }

    /**
     * 사용자가 입력한 insert/update 쿼리를 실행
     *
     * @param dbInfoVo
     * @param query
     */
    public int executeUpdate(DbInfoVo dbInfoVo, String query) {
        Statement stmt = null;
        Connection conn = null;
        int ret = 0;
        if(PropertyUtil.getProperty("cipher","off").equalsIgnoreCase("on")) {
            try {
                dbInfoVo.setDbPassword(xCryptionAria.decrypt(dbInfoVo.getDbPassword()));
            } catch(Exception e) {
                log.error(null, e);
            }
        }

        try {
            Class.forName(dbInfoVo.getDriverNm());

            conn = DriverManager.getConnection(dbInfoVo.getDriverDsn(), dbInfoVo.getDbUserId(), dbInfoVo.getDbPassword());

            stmt = conn.createStatement();
            ret = stmt.executeUpdate(query);
        } catch(Exception e) {
            log.error(null, e);
        } finally {
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }
        return ret;
    }

    /**
     * 실행한 쿼리의 컬럼 정보 세팅
     *
     * @param rsmd
     * @throws SQLException
     */
    public void setColumnHeader(ResultSetMetaData rsmd) throws SQLException {
        columnHeader = new String[rsmd.getColumnCount()];

        for(int i = 1, j = 0; i <= rsmd.getColumnCount(); i++, j++) {
            columnHeader[j] = rsmd.getColumnName(i);
        }
    }

    /**
     * 실행한 쿼리의 데이타 세팅
     *
     * @param rsmd
     * @throws SQLException
     */
    public void setQueryData(ResultSet rs) throws SQLException {
        queryData = new ArrayList<>();
        int columnCnt = rs.getMetaData().getColumnCount();
        int cnt = 0;

        String[] data = null;
        while(rs.next()) {
            if(cnt >= 10000) {
                break;
            }

            data = new String[columnCnt];
            for(int i = 0; i < data.length; i++) {
                if(StringUtil.isEmpty(dbEnc)) {
                    data[i] = rs.getString(i + 1);
                } else {
                    try {
                        data[i] = new String(rs.getString(i + 1).getBytes(dbEnc), viewEnc);
                    } catch(UnsupportedEncodingException e) {
                        log.error(null, e);
                    }
                }
            }
            queryData.add(data);
            cnt++;
        }
    }

    /**
     * 페이징 만큼 실행한 쿼리의 데이타 세팅
     *
     * @param rs
     * @param pageSize
     * @param startRow
     * @throws SQLException
     */
    public void setQueryData(ResultSet rs, int pageSize, int startRow) throws SQLException {
        queryData = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCnt = meta.getColumnCount();
        int cnt = 0;

        String[] data = null;
        // while (rs.next()) {
        while(rs.next() && cnt < startRow + pageSize) {
            if(cnt >= startRow) {
                data = new String[columnCnt];
                for(int i = 0; i < data.length; i++) {
                    if(StringUtil.isEmpty(dbEnc)) {
                        data[i] = rs.getString(i + 1);
                        // 암/복호화 처리
                        data[i] = DataSecurity.getInstance().securityWithColumn(data[i],meta.getColumnName(i+1) , "DECRYPT");

                    } else {
                        try {
                            if(rs.getString(i + 1) != null) {
                                data[i] = new String(rs.getString(i + 1).getBytes(dbEnc), viewEnc);
                                // 암/복호화 처리
                                data[i] = DataSecurity.getInstance().securityWithColumn(data[i],meta.getColumnName(i+1) , "DECRYPT");
                            } else {
                                data[i] = "";
                            }
                        } catch(UnsupportedEncodingException e) {
                            log.error(null, e);
                        }
                    }
                }
                queryData.add(data);
            }
            cnt++;
        }
    }

    /**
     * 실행한 쿼리의 데이타 목록 조회
     *
     * @return
     */
    public List<String[]> getQueryData() {
        return queryData;
    }

    /**
     * 실행한 쿼리의 컬럼 정보 조회
     *
     * @return
     */
    public String[] getColumnHeader() {
        return this.columnHeader;
    }
}
