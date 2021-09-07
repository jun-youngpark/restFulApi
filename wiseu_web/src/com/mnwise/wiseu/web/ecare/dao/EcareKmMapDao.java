package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.editor.model.EcareItemVo;

/**
 * NVECAREKMMAP 테이블 DAO 클래스
 */
@Repository
public class EcareKmMapDao extends BaseDao {
    public int insertEcareKmMap(EcareItemVo ecareItem) {
        return insert("EcareKmMap.insertEcareKmMap", ecareItem);
    }

    public int insertEditorEcareItem(EcareItemVo ecareItem) {
        return insert("EcareKmMap.insertEditorEcareItem", ecareItem);
    }

    public int updateEditorEcareItem(EcareItemVo ecareItem) {
        return update("EcareKmMap.updateEditorEcareItem", ecareItem);
    }

    /**
     * 이케어 실시간 개인화인자 값을 삭제한다.
     */
    public int deleteEcareKmMapByPk(int ecareNo, String itemfieldNm) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("itemfieldNm", itemfieldNm);
        return delete("EcareKmMap.deleteEcareKmMapByPk", paramMap);
    }

    public int deleteEcareKMMap(int ecareNo) {
        return delete("EcareKmMap.deleteEcareKMMap", ecareNo);
    }

    @SuppressWarnings("unchecked")
    public List<EcareItemVo> selectEditorEcareItem(int ecareNo) {
        List<EcareItemVo> list = selectList("EcareKmMap.selectEditorEcareItem", ecareNo);
        security.securityObjectList(list, "DECRYPT");
        return list;
    }

    public int selectEditorEcareItemCount(EcareItemVo ecareItem) {
        Integer count = (Integer) selectOne("EcareKmMap.selectEditorEcareItemCount", ecareItem);
        return (count == null) ? 0 : count;
    }

    /**
     * 이케어 실시간 NVECAREKMMAP 테이블의 KNOWLEDGEMAP_ID 필드 값의 최대 값을 조회한다.
     *
     * @return knowledgemapId
     */
    public String selectEditorEcareItemMax() {
        String knowledgemapId = (String) selectOne("EcareKmMap.selectEditorEcareItemMax");
        return StringUtil.defaultIfBlank(knowledgemapId, "900100000000");
    }
}