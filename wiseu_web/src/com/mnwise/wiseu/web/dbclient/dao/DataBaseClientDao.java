package com.mnwise.wiseu.web.dbclient.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mnwise.common.io.IOUtil;
import com.mnwise.wiseu.web.common.util.DBConnectionUtil;
import com.mnwise.wiseu.web.common.util.StringCharsetConverter;
import com.mnwise.wiseu.web.dbclient.model.DataBaseClientVo;

@Repository
public class DataBaseClientDao {
    private static final Logger log = LoggerFactory.getLogger(DataBaseClientDao.class);

    public int setResultData(DataBaseClientVo dbClientVo) {
        DBConnectionUtil dbUtil = new DBConnectionUtil(dbClientVo.getJdbcDriver(), dbClientVo.getJdbcUrl(), dbClientVo.getJdbcUserName(), dbClientVo.getJdbcPassWord());

        Connection conn = dbUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        int result = -1;
        try {
            stmt = conn.createStatement();
            result = stmt.executeUpdate(dbClientVo.getQuery());
            conn.commit();
        } catch(Exception e) {
            try {
                conn.rollback();
            } catch(Exception ee) {
                log.error(ee.getMessage());
            }
            log.error(e.getMessage());
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }
        return result;
    }

    public List<String> getResultMetaData(DataBaseClientVo dbClientVo) {
        DBConnectionUtil dbUtil = new DBConnectionUtil(dbClientVo.getJdbcDriver(), dbClientVo.getJdbcUrl(), dbClientVo.getJdbcUserName(), dbClientVo.getJdbcPassWord());

        Connection conn = dbUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        List<String> columnList = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(dbClientVo.getQuery());

            ResultSetMetaData rsmd = rs.getMetaData();

            columnList = new ArrayList<>();
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnList.add(rsmd.getColumnName(i));
            }
        } catch(Exception e) {
            dbClientVo.setErrMsg(e.toString());
            log.error("컬럼 정보를 가져올 수 없습니다", e);
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return columnList;
    }

    public List<String> getResultData(DataBaseClientVo dbClientVo) {
        DBConnectionUtil dbUtil = new DBConnectionUtil(dbClientVo.getJdbcDriver(), dbClientVo.getJdbcUrl(), dbClientVo.getJdbcUserName(), dbClientVo.getJdbcPassWord());

        Connection conn = dbUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        List<String> columnValue = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(dbClientVo.getQuery());
            ResultSetMetaData rsmd = rs.getMetaData();

            columnValue = new ArrayList<>();
            if(rs != null) {
                while(rs.next()) {
                    for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String data = StringCharsetConverter.convert((rs.getString(i)));
                        columnValue.add(data == null ? "-" : data.trim());
                    }
                }
            }
        } catch(Exception e) {
            dbClientVo.setErrMsg(e.toString());
            log.error("데이터를 가져올 수 없습니다", e);
        } finally {
            IOUtil.closeQuietly(rs);
            IOUtil.closeQuietly(stmt);
            IOUtil.closeQuietly(conn);
        }

        return columnValue;
    }
}