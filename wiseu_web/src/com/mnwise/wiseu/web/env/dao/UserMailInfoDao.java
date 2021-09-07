package com.mnwise.wiseu.web.env.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.env.model.EnvSenderInfoVo;

/**
 * NVUSERMAILINFO 테이블 DAO 클래스
 */
@Repository
public class UserMailInfoDao extends BaseDao {
    public int insertUserMailInfo(EnvSenderInfoVo senderInfo) {
        security.securityObject(senderInfo, "ENCRYPT");
        return insert("UserMailInfo.insertUserMailInfo", senderInfo);
    }

    public int updateUserMailInfoByUserId(EnvSenderInfoVo senderInfo) {
        security.securityObject(senderInfo, "ENCRYPT");
        return update("UserMailInfo.updateUserMailInfoByUserId", senderInfo);
    }

    public int deleteUserMailInfoByPk(String userId, int seqNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("seqNo", seqNo);
        return delete("UserMailInfo.deleteUserMailInfoByPk", paramMap);
    }

    /**
     * 환경설정 - 메시지 발신자 정보에서 개인정보를 가져온다.
     *
     * @param envSenderInfoVo
     * @return
     */
    public EnvSenderInfoVo selectUserMailInfoByUserId(String userId) {
        EnvSenderInfoVo tmp = (EnvSenderInfoVo) selectOne("UserMailInfo.selectUserMailInfoByUserId", userId);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }
}