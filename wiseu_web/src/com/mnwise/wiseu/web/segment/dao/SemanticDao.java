package com.mnwise.wiseu.web.segment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;

/**
 * NVSEMANTIC 테이블 DAO 클래스
 */
@Repository
public class SemanticDao extends BaseDao {
    public int insertSemantic(SemanticVo semantic) {
        return insert("Semantic.insertSemantic", semantic);
    }

    public int copySemantic(int fromSegmentNo, int toSegmentNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fromSegmentNo", fromSegmentNo);
        paramMap.put("toSegmentNo", toSegmentNo);
        return insert("Semantic.copySemantic", paramMap);
    }

    public int updateSemanticByPk(SemanticVo semantic) {
        return update("Semantic.updateSemanticByPk", semantic);
    }

    public int deleteSemanticByPk(int segmentNo, int fieldSeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("fieldSeq", fieldSeq);
        return delete("Semantic.deleteSemanticByPk", paramMap);
    }

    /**
     * 시멘틱 정보를 삭제한다.
     *
     * @param segmentNo
     */
    public int deleteSemanticBySegmentNo(int segmentNo) {
        return delete("Semantic.deleteSemanticBySegmentNo", segmentNo);
    }

    public SemanticVo selectSemanticByPk(int segmentNo, int fieldSeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("fieldSeq", fieldSeq);
        return (SemanticVo) selectOne("Semantic.selectSemanticByPk", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<SemanticVo> getEcareSemanticList(int serviceNo) {
        return selectList("Semantic.getEcareSemanticList", serviceNo);
    }

    /**
     * 대상자 SEMANTIC 정보를 얻어온다.
     *
     * @param segmentNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SemanticVo> selectSemanticListBySegmentNo(int segmentNo) {
        return selectList("Semantic.selectSemanticListBySegmentNo", segmentNo);
    }

    /**
     * Semantic 정보를 가져온다.
     *
     * @param segmentVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SemanticVo> selectSementicInfoForTarget(SegmentVo segmentVo) {
        return selectList("Semantic.selectSementicInfoForTarget", segmentVo);
    }

    @SuppressWarnings("unchecked")
    public List<SemanticVo> selectSemanticList(int segmentNo) {
        return selectList("Semantic.selectSementicInfo", segmentNo);
    }

    /**
     * segmentNo에 해당하는 시맨틱 필드명을 조회한다.
     *
     * @param segmentNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectSegmentField(int segmentNo) {
        return selectList("Semantic.selectSementField", segmentNo);
    }

    /**
     * 본 캠페인에 설정한 대상자의 시맨틱 키 목록을 조회한다.
     *
     * @param scenarioNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectCampaignSemanticKey(int scenarioNo) {
        return selectList("Semantic.selectCampaignSemanticKey", scenarioNo);
    }

    /**
     * 본 이케어에 설정한 대상자의 시맨틱 키 목록을 조회한다.
     *
     * @param scenarioNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectEcareSemanticKey(int scenarioNo) {
        return selectList("Semantic.selectEcareSemanticKey", scenarioNo);
    }


}