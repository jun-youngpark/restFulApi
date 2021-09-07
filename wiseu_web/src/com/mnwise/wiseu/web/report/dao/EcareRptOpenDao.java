package com.mnwise.wiseu.web.report.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import org.springframework.stereotype.Repository;

/**
 * NVECARERPTOPEN 테이블 DAO 클래스
 */
@Repository
public class EcareRptOpenDao extends BaseDao {

    public int selectOpenCnt(EcareScenarioInfoVo scenarioInfoVo) {
        return (Integer) selectOne("EcareRptOpen.selectOpenCnt", scenarioInfoVo);
    }

}