package com.mnwise.wiseu.web.resend.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVRESENDREQUEST 테이블 DAO 클래스
 */
@Repository
public class ResendRequestDao extends BaseDao {
    /*public int insertResendRequest(ResendRequest resendRequest) {
        return insert("ResendRequest.insertResendRequest", resendRequest);
    }*/

    public int insertLstResendRequest(Map<String, Object> resendRequest) {
        security.securityMap(resendRequest, "ENCRYPT");
        return (Integer) insert("ResendRequest.insertLstResendRequest", resendRequest);
    }

    /*public int updateResendRequestByPk(ResendRequest resendRequest) {
        return update("ResendRequest.updateResendRequestByPk", resendRequest);
    }*/

    public int deleteResendRequestByPk(String reqDt, String reqUserId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqDt", reqDt);
        paramMap.put("reqUserId", reqUserId);
        return delete("ResendRequest.deleteResendRequestByPk", paramMap);
    }

    /*public ResendRequest selectResendRequestByPk(String reqDt, String reqUserId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqDt", reqDt);
        paramMap.put("reqUserId", reqUserId);
        return selectOne("ResendRequest.selectResendRequestByPk", paramMap);
    }*/

}