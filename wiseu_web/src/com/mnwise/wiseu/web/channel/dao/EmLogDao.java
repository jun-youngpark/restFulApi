package com.mnwise.wiseu.web.channel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportErrorVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

/**
 * EM_LOG 테이블 DAO 클래스
 */
@Repository
public class EmLogDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public List<CampaignReportErrorVo> getCampaignReportSMSErrorList(Map<String, Object> paramMap) {
        return selectList("EmLog.getCampaignReportSMSErrorList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectSMSLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EmLog.selectSMSLogList", scenarioInfoVo);
    }

}