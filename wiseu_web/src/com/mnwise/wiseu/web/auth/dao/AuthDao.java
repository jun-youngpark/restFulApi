package com.mnwise.wiseu.web.auth.dao;

import java.sql.SQLException;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
@Repository
public class AuthDao extends BaseDao {

    public void insertAuthToken(Map<String, String> param) throws SQLException {
        insert("Auth.insertAuthToken", param);
    }

    public int checkToken(Map<String, String> param) throws SQLException {
        return update("Auth.authCheckToken", param);
    }

    public int checkTimeout(Map<String, String> param) throws SQLException {
        return (int) selectOne("Auth.checkTimeout", param);
    }

    public int checkUsedToken(Map<String, String> param) throws SQLException {
        return (int) selectOne("Auth.checkUsedToken", param);
    }

    public void updateVaildToken(Map<String, String> param) throws SQLException {
        update("Auth.updateVaildToken", param);
    }
}
