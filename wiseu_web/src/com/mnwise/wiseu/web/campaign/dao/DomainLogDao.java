package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportErrorVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;

/**
 * NVDOMAINLOG 테이블 DAO 클래스
 */
@Repository
public class DomainLogDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectErrCdResultMsgList(ScenarioInfoVo scenarioInfoVo) {
        return selectList("DomainLog.selectErrCdResultMsgList", scenarioInfoVo);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignReportErrorVo> getCampaignReportErrorList(Map<String, Object> paramMap) {
        return selectList("DomainLog.getCampaignReportErrorList", paramMap);
    }

    /**
     * 캠페인 요약 분석 스팸차단
     *
     * @param campaignNo
     * @param lang
     * @return
     */
    public int getCampaignSummarySpam(int campaignNo, String lang) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("lang", lang);

        return (Integer) selectOne("DomainLog.getCampaignSummarySpam", paramMap);
    }

}