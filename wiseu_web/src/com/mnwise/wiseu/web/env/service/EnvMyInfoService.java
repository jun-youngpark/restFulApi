package com.mnwise.wiseu.web.env.service;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.wiseu.web.account.dao.UserDao;
import com.mnwise.wiseu.web.account.dao.UserPwHistoryDao;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.xCryptionSha;
import com.mnwise.wiseu.web.env.model.EnvMyInfoVo;

/**
 * 개인정보 관리 Service
 */
@Service
public class EnvMyInfoService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(EnvMyInfoService.class);

    @Autowired private UserDao userDao;
    @Autowired private UserPwHistoryDao userPwHistoryDao;

    /**
     * 환경설정 - 개인정보 관리에서 개인정보를 가져온다.
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public UserVo selectEnvMyInfo(String userId) throws Exception {
        UserVo userVo = userDao.selectEnvMyInfo(userId);
        if(super.passwordEncUse.equalsIgnoreCase("on")) {
            // String passWd = envMyInfoVo.getPassWd();
            // passWd = xCryption.decrypt(passWd);
            // envMyInfoVo.setPassWd(passWd);
        }
        return userVo;
    }

    /**
     * 환경설정 - 개인정보 관리에서 개인정보를 변경한다.
     *
     * @param envMyInfoVo
     * @throws Exception
     */
    public void updateEnvMyInfo(EnvMyInfoVo envMyInfoVo) throws Exception {
        if(super.passwordEncUse.equalsIgnoreCase("on")) {
            try {
                String salt = xCryptionSha.getSalt();
                String pwd = envMyInfoVo.getPassWd();
                envMyInfoVo.setPassWd(xCryptionSha.encryptWithSalt_sha256(pwd, salt));
                envMyInfoVo.setPassSalt(salt);
            } catch(NoSuchAlgorithmException e) {
                log.error(null, e);
            }
        } else {
            envMyInfoVo.setPassSalt("");
        }

        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(userVo, envMyInfoVo);
        userVo.setListCountPerPage(Integer.parseInt(envMyInfoVo.getListCountPerPage()));
        userVo.setDefaultPassYn("N");
        userPwHistoryDao.insertUserPwHistory(userVo);

        userDao.updateEnvMyInfo(envMyInfoVo);
    }
}
