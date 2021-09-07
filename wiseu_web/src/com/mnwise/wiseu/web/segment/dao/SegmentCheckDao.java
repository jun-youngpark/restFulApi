package com.mnwise.wiseu.web.segment.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.segment.model.SegmentVo;

/**
 * NVSEGMENTCHECK 테이블 DAO 클래스
 */
@Repository
public class SegmentCheckDao extends BaseDao {
    public int insertSegmentCheck(int segmentNo, String userId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("userId", userId);

        return insert("SegmentCheck.insertSegmentCheck", paramMap);
    }

    public int copySegmentCheck(SegmentVo segment) {
        return insert("SegmentCheck.copySegmentCheck", segment);
    }

    /*public int updateSegmentCheckByPk(SegmentCheck segmentCheck) {
        return update("SegmentCheck.updateSegmentCheckByPk", segmentCheck);
    }*/

    public int deleteSegmentCheckByPk(int segmentNo, String userId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("userId", userId);
        return delete("SegmentCheck.deleteSegmentCheckByPk", paramMap);
    }

    public int deleteSegmentCheckBySegmentNo(int segmentNo) {
        return delete("SegmentCheck.deleteSegmentCheckBySegmentNo", segmentNo);
    }

    /*public SegmentCheck selectSegmentCheckByPk(int segmentNo, String userId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("userId", userId);
        return selectOne("SegmentCheck.selectSegmentCheckByPk", paramMap);
    }*/

    public int selectBookMarkCnt(int segmentNo) {
        Integer count = (Integer) selectOne("SegmentCheck.selectBookMarkCnt", segmentNo);
        return (count == null) ? 0 : count;
    }
}