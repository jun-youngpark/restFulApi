package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.editor.model.LinkTraceVo;
import com.mnwise.wiseu.web.report.model.LinkTraceResult2Vo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportLinkClickVo;
import com.mnwise.wiseu.web.segment.model.LinkclickVo;

/**
 * NVLINKTRACE 테이블 DAO 클래스
 */
@Repository
public class LinkTraceDao extends BaseDao {
    public int insertLinkTrace(LinkTraceVo linkTrace) {
        return insert("LinkTrace.insertLinkTrace", linkTrace);
    }

    public int copyLinkTrace(CampaignVo campaignVo) {
        return insert("LinkTrace.copyLinkTrace", campaignVo);
    }

    public int deleteCampLinkTraceAll(ScenarioVo scenarioVo) {
        return delete("LinkTrace.deleteCampLinkTraceAll", scenarioVo);
    }

    /**
     * LINK_SEQ 최대 값 +1 를 얻는다.
     */
    public int selectEditorCampaignLinkseqMax(int no) {
        return (Integer) selectOne("LinkTrace.selectEditorCampaignLinkseqMax", no);
    }

    public int selectEditorCampaignLinktraceExist(LinkTraceVo linkTrace) {
        Integer count = (Integer) selectOne("LinkTrace.selectEditorCampaignLinktraceExist", linkTrace);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<LinkTraceResult2Vo> selectLinkTraceResult2List(Map<String, Object> map) {
        return selectList("LinkTrace.selectSummaryLinkTraceResult2List", map);
    }

    @SuppressWarnings("unchecked")
    public List<LinkclickVo> selectLinkCampaignInfo(int campaignNo) {
        return selectList("LinkTrace.selectLinkCampaignInfo", campaignNo);
    }

    /**
     * 캠페인 링크클릭 리포트
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CampaignReportLinkClickVo> getCampaignReportLinkClickList(int scenarioNo, int campaignNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("campaignNo", campaignNo);

        return selectList("LinkTrace.getCampaignReportLinkClickList", paramMap);
    }

    /**
     * 캠페인 링크클릭 리포트 총 합계
     *
     * @param scenarioNo 시나리오 번호
     * @param campaignNo 캠페인 번호
     * @return
     */
    public CampaignReportLinkClickVo getCampaignReportLinkClickTotal(int scenarioNo, int campaignNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("campaignNo", campaignNo);

        return (CampaignReportLinkClickVo) selectOne("LinkTrace.getCampaignReportLinkClickTotal", paramMap);
    }
}