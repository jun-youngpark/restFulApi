package com.mnwise.wiseu.web.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.auth.dao.AuthDao;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    public void insertAuthToken(Map<String, String> param) throws Exception {
        authDao.insertAuthToken(param);
    }

    public int checkToken(Map<String, String> param) throws Exception {
        return authDao.checkToken(param);
    }

    public int checkTimeout(Map<String, String> param) throws Exception {
        return authDao.checkTimeout(param);
    }

    public int checkUsedToken(Map<String, String> param) throws Exception {
        return authDao.checkUsedToken(param);
    }

    public void updateVaildToken(Map<String, String> param) throws Exception {
        authDao.updateVaildToken(param);
    }
}
