package com.mnwise.wiseu.web.common.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.security.aria.Aria;

import java.sql.SQLException;

/**
 * 시큐어 코딩 관련 DB 설정 암호화 위한 datasource 설정 추가한 class
 */

public class SecureBasicDataSource extends BasicDataSource {
    private static final Logger log = LoggerFactory.getLogger(SecureBasicDataSource.class);

    // jdbc 암호 암호화 해제 설정
    public void setPassword(String password) {
        try {
            if("on".equals(PropertyUtil.getProperty("cipher","off").toLowerCase())) {
                super.setPassword(Aria.wiseuDecrypt(password));
            } else {
                super.setPassword(password);
            }
        } catch(Exception e) {
            log.error(null, e);
        }

    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == null) {
            throw new NullPointerException("Class is null");
        }

        return iface.cast(this);
    }

}