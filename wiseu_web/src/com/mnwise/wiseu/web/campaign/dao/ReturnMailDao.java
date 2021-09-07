package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportReturnMailVo;

/**
 * NVRETURNMAIL 테이블 DAO 클래스
 */
@Repository
public class ReturnMailDao extends BaseDao {

    /**
     * 캠페인 리포트 리턴메일 분석
     *
     * @param campaignNo 캠페인 번호
     * @param lang 언어 구분
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportReturnMailVo> getCampaignReturnMailDetailList(int campaignNo, String lang) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("lang", lang);

        return selectList("ReturnMail.selectReturnMailList", paramMap);
    }

}