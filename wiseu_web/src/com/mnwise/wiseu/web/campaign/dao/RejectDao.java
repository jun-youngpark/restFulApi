package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportBasicVo;

/**
 * NVREJECT 테이블 DAO 클래스
 */
@Repository
public class RejectDao extends BaseDao {

    /**
     * 캠페인 요약 분석 수신거부
     *
     * @param campaignNo
     * @return
     */
    public int getCampaignSummaryReject(int campaignNo) {
        Integer count = (Integer) selectOne("Reject.getCampaignSummaryReject", campaignNo);
        return (count == null) ? 0 : count;
    }

    /**
     * 캠페인 수신거부 리포트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportBasicVo> getCampaignReportRejectList(int scenarioNo, int campaignNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("campaignNo", campaignNo);

        return selectList("Reject.getCampaignReportRejectList", paramMap);
    }

}