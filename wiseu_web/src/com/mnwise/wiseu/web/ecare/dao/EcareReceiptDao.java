package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

/**
 * NVECARERECEIPT 테이블 DAO 클래스
 */
@Repository
public class EcareReceiptDao extends BaseDao {

    /**
     * 이케어 리포트 PUSH 수신확인 정보
     * @param serviceNo
     * @param resultSeq
     * @param startDt
     * @param endDt
     * @return
     */
    public CaseInsensitiveMap selectPushEcareReceiptCount(int serviceNo, String resultSeq, String startDt, String endDt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("service_no", serviceNo);
        paramMap.put("result_seq", resultSeq);
        paramMap.put("start_dt", startDt);
        paramMap.put("end_dt", endDt);

        return (CaseInsensitiveMap) selectOne("EcareReceipt.selectPushEcareReceiptCount", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<EcareScenarioInfoVo> selectReactionResultList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareReceipt.selectSummaryReactionResultList", scenarioInfoVo);
    }

}