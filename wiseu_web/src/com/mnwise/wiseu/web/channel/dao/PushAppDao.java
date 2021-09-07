package com.mnwise.wiseu.web.channel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVPUSHAPP 테이블 DAO 클래스
 */
@Repository
public class PushAppDao extends BaseDao {
    /**
     * PUSH APP 정보 추가
     * @param paramMap
     */
    public int insertPushApp(Map<String, Object> paramMap) {
        return insert("PushApp.insertPushApp", paramMap);
    }

    /**
     * PUSH APP 정보 수정
     * @param paramMap
     */
    public int updatePushApp(Map<String, Object> paramMap) {
        return update("PushApp.updatePushApp", paramMap);
    }

    /**
     * PUSH APP 목록
     * @param useOnly
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> selectPushAppList(boolean useOnly) {
        Map<String, Object> paramMap = new HashMap<>();
        if(useOnly) {
            paramMap.put("useOnly", "Y");
        }
        return selectList("PushApp.selectPushAppList", paramMap);
    }
}