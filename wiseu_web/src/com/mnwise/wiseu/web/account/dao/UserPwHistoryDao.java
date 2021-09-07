package com.mnwise.wiseu.web.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.account.model.UserPwVo;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVUSERPWHISTORY 테이블 DAO 클래스
 */
@Repository
public class UserPwHistoryDao extends BaseDao {
    /**
     * 사용자 생성
     *
     * @param userVo
     */
    public int insertUserPwHistory(UserVo userVo) {
        security.securityObject(userVo, "ENCRYPT");
        return insert("UserPwHistory.insertUserPwHistory", userVo);
    }


    @SuppressWarnings("unchecked")
    public List<UserPwVo> getCheckShaPwHistory(String userId) {
        List<UserPwVo> userPwList = (List<UserPwVo>) selectList("UserPwHistory.getCheckShaPwHistory", userId);
        security.securityObjectList(userPwList, "DECRYPT");
        return userPwList;
    }

}