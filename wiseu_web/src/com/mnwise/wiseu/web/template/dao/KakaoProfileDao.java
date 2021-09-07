package com.mnwise.wiseu.web.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.template.model.KakaoProfile;

/**
 * NVKAKAOPROFILE 테이블 DAO 클래스
 */
@Repository
public class KakaoProfileDao extends BaseDao {
    public int insertKakaoProfile(KakaoProfile kakaoProfile) {
        return insert("KakaoProfile.insertKakaoProfile", kakaoProfile);
    }

    /**
     * 발신 프로필 키와 마스터계정으로 등록된 플러스친구 ID를 조회하여 저장
     *
     * @param userId
     * @param kakaoSenderKey
     * @param masterId
     */
    public int insertUserKakaoProfile(String userId, String kakaoSenderKey, String masterId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("kakaoSenderKey", kakaoSenderKey);
        map.put("masterId", masterId);

        return insert("KakaoProfile.insertUserKakaoProfile", map);
    }

    public int updateKakaoProfileByPk(KakaoProfile kakaoProfile) {
        return update("KakaoProfile.updateKakaoProfileByPk", kakaoProfile);
    }

    public int deleteKakaoProfileByPk(String userId, String kakaoSenderKey) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("kakaoSenderKey", kakaoSenderKey);
        return delete("KakaoProfile.deleteKakaoProfileByPk", paramMap);
    }

    public int deleteUserKakaoProfile(String userId) {
        return delete("KakaoProfile.deleteUserKakaoProfile", userId);
    }

    public KakaoProfile selectKakaoProfileByPk(String userId, String kakaoSenderKey) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("kakaoSenderKey", kakaoSenderKey);
        return (KakaoProfile) selectOne("KakaoProfile.selectKakaoProfileByPk", paramMap);
    }

    /**
     * NVKAKAOPROFILE 테이블에서 USER_ID 컬럼을 조건으로 이용하여 KAKAO_YELLOW_ID, KAKAO_SENDER_KEY 컬럼 SELECT
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<KakaoProfile> selectKakaoProfileList(String userId) {
        return selectList("KakaoProfile.selectKakaoProfileList", userId);
    }

    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> getUserKakaoSenderKeyList(String userId) {
        return selectList("KakaoProfile.getUserKakaoSenderKeyList", userId);
    }

    @SuppressWarnings("unchecked")
    public KakaoProfile selectKakaoProfileByYellowId(String yellowId, String userId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("kakaoYellowId", yellowId);
        return (KakaoProfile) selectOne("KakaoProfile.selectKakaoProfileByYellowId", paramMap);
    }

}