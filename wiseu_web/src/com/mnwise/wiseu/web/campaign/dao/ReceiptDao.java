package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportVo;

/**
 * NVRECEIPT 테이블 DAO 클래스
 */
@Repository
public class ReceiptDao extends BaseDao {

    /**
     * 캠페인 리포트 PUSH 수신확인 정보
     * @param campaignNo
     * @return
     */
    public CaseInsensitiveMap selectPushReceiptCount(int campaignNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        return (CaseInsensitiveMap) selectOne("Receipt.selectPushReceiptCount", paramMap);
    }

    /**
     * 캠페인 요약 분석 수신확인 정보
     *
     * @param campaignNo
     * @return
     */
    public CampaignReportVo getCampaignSummaryReceive(int campaignNo) {
        return (CampaignReportVo) selectOne("Receipt.getCampaignSummaryReceive", campaignNo);
    }

    public int getCampaignReportAbRealOpenCnt(int campaignNo) {
        Integer openCnt = (Integer) selectOne("Receipt.getCampaignReportAbRealOpenCnt", campaignNo);
        return (openCnt == null) ? 0 : openCnt;
    }
}