package com.mnwise.wiseu.web.segment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.segment.model.SegGenealogyVo;

/**
 * NVSEGGENEALOGY 테이블 DAO 클래스
 */
@Repository
public class SegGenealogyDao extends BaseDao {
    public int insertSegGenealogy(int segmentNo, int genealogySeq, int supraSegmentNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("genealogySeq", genealogySeq);
        paramMap.put("supraSegmentNo", supraSegmentNo);

        return insert("SegGenealogy.insertSegGenealogy", paramMap);
    }

    public int copySegGenealogy(int fromSegmentNo, int toSegmentNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fromSegmentNo", fromSegmentNo);
        paramMap.put("toSegmentNo", toSegmentNo);

        return insert("SegGenealogy.copySegGenealogy", paramMap);
    }

    public int updateSegGenealogyByPk(SegGenealogyVo segGenealogy) {
        return update("SegGenealogy.updateSegGenealogyByPk", segGenealogy);
    }

    public int deleteSegGenealogyByPk(int segmentNo, int genealogySeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("genealogySeq", genealogySeq);
        return delete("SegGenealogy.deleteSegGenealogyByPk", paramMap);
    }

    /**
     * 기본 세그먼트 할당 이력을 삭제한다.
     *
     * @param segmentVo
     */
    public int deleteSegGenealogyBySegmentNo(int segmentNo) {
        return delete("SegGenealogy.deleteSegGenealogyBySegmentNo", segmentNo);
    }

    public SegGenealogyVo selectSegGenealogyByPk(int segmentNo, int genealogySeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("genealogySeq", genealogySeq);
        return (SegGenealogyVo) selectOne("SegGenealogy.selectSegGenealogyByPk", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<SegGenealogyVo> selectSegGenealogyList(int segmentNo) {
        return selectList("SegGenealogy.selectSegGenealogyList", segmentNo);
    }

    public int getSubSegmentCnt(int segmentNo) {
        Integer count = (Integer) selectOne("SegGenealogy.getSubSegmentCnt", segmentNo);
        return (count == null) ? 0 : count;
    }

}