package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.RealtimeAcceptDataVo;

/**
 * NVREALTIMEACCEPTDATA 테이블 DAO 클래스
 */
@Repository
public class RealtimeAcceptDataDao extends BaseDao {
    public int insertRealtimeAcceptData(RealtimeAcceptDataVo realtimeAcceptData) {
        security.securityObject(realtimeAcceptData, "ENCRYPT");
        return insert("RealtimeAcceptData.insertRealtimeAcceptData", realtimeAcceptData);
    }

    @SuppressWarnings("unchecked")
    public List<RealtimeAcceptDataVo> selectRealtimeAcceptDataList(Map<String, Object> paramMap) {
        List<RealtimeAcceptDataVo> list = selectList("RealtimeAcceptData.selectRealtimeAcceptDataList", paramMap);
        security.securityObjectList(list, "DECRYPT");
        return list;
    }
}