package com.mnwise.wiseu.web.rest.dao.segement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.segment.Semantic;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVSEMANTIC 테이블 DAO 클래스
 */
@Repository
public class SemanticDao extends BaseDao {
    public int insertSemantic(Semantic semantic) {
        return insert("Semantic.insertSemantic", semantic);
    }

    public int copySemantic(int fromSegmentNo, int toSegmentNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fromSegmentNo", fromSegmentNo);
        paramMap.put("toSegmentNo", toSegmentNo);
        return insert("Semantic.copySemantic", paramMap);
    }

    public int updateSemanticByPk(Semantic semantic) {
        return update("Semantic.updateSemanticByPk", semantic);
    }

    public int deleteSemanticByPk(int segmentNo, int fieldSeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("segmentNo", segmentNo);
        paramMap.put("fieldSeq", fieldSeq);
        return delete("Semantic.deleteSemanticByPk", paramMap);
    }
    /**
     * 본 캠페인에 설정한 대상자의 시맨틱 키 목록을 조회한다.
     *
     * @param scenarioNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectCampaignSemanticKey(int scenarioNo) {
        return (List<String>) selectList("Semantic.selectCampaignSemanticKey", scenarioNo);
    }


}