package com.mnwise.wiseu.web.common.util;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.mnwise.wiseu.web.base.util.xCryptionAria;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;

public class DBConnectionUtil {
    private static final Logger log = LoggerFactory.getLogger(DBConnectionUtil.class);

    private String driverClassName;
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    public DBConnectionUtil(DbInfoVo dbInfoVo) {
        this.setDatabaseInfoForDbInfo(dbInfoVo);
    }

    public DBConnectionUtil(DbInfoVo dbInfoVo, ApplicationContext context) {
        this.setDatabaseInfoForDbInfo(dbInfoVo);
    }

    private void setDatabaseInfoForDbInfo(DbInfoVo dbInfoVo) {
        this.driverClassName = dbInfoVo.getDriverNm();
        this.jdbcUrl = dbInfoVo.getDriverDsn();
        this.jdbcUser = dbInfoVo.getDbUserId();
        if(PropertyUtil.getProperty("cipher","off").equalsIgnoreCase("on")) {
            try {
                this.jdbcPassword = xCryptionAria.decrypt(dbInfoVo.getDbPassword());
            } catch(Exception e) {
                log.error(null, e);
            }
        } else {
            this.jdbcPassword = dbInfoVo.getDbPassword();
        }
    }

    public DBConnectionUtil(String driverClassName, String jdbcUrl, String jdbcUser, String jdbcPassword) {
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
        if(PropertyUtil.getProperty("cipher","off").equalsIgnoreCase("on")) {
            try {
                this.jdbcPassword = xCryptionAria.decrypt(jdbcPassword);
            } catch(Exception e) {
                log.error(null, e);
            }
        } else {
            this.jdbcPassword = jdbcPassword;
        }
    }

    public Connection getConnection() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);

        JdbcTemplate jt = new JdbcTemplate();
        jt.setDataSource(dataSource);

        try {
            return dataSource.getConnection();
        } catch(Exception e) {
            log.error(null, e);
        }
        return null;
    }

    public JdbcTemplate getJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUser);
        dataSource.setPassword(jdbcPassword);

        JdbcTemplate jt = new JdbcTemplate();
        jt.setDataSource(dataSource);

        return jt;
    }
}
