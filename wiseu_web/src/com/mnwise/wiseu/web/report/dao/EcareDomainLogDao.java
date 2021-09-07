package com.mnwise.wiseu.web.report.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

/**
 * NVECAREDOMAINLOG 테이블 DAO 클래스
 */
@Repository
public class EcareDomainLogDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectDomainResultMsgList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareDomainLog.selectDomainResultMsgList", scenarioInfoVo);
    }

    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectErrCdResultMsgList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareDomainLog.selectErrCdResultMsgList", scenarioInfoVo);
    }

    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectDomainLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareDomainLog.selectDomainLogList", scenarioInfoVo);
    }

}