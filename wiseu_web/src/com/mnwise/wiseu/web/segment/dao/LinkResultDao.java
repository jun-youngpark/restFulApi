package com.mnwise.wiseu.web.segment.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.segment.model.LinkclickVo;

/**
 * NVLINKRESULT 테이블 DAO 클래스
 */
@Repository
public class LinkResultDao extends BaseDao {
    /*public int insertLinkResult(LinkResult linkResult) {
        return insert("LinkResult.insertLinkResult", linkResult);
    }

    public int updateLinkResultByPk(LinkResult linkResult) {
        return update("LinkResult.updateLinkResultByPk", linkResult);
    }*/

    public int deleteLinkResultByPk(int campaignNo, int resultSeq, String listSeq, String linkDt, String linkTm) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("resultSeq", resultSeq);
        paramMap.put("listSeq", listSeq);
        paramMap.put("linkDt", linkDt);
        paramMap.put("linkTm", linkTm);
        return delete("LinkResult.deleteLinkResultByPk", paramMap);
    }

    /*public LinkResult selectLinkResultByPk(int campaignNo, int resultSeq, String listSeq, String linkDt, String linkTm) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("resultSeq", resultSeq);
        paramMap.put("listSeq", listSeq);
        paramMap.put("linkDt", linkDt);
        paramMap.put("linkTm", linkTm);
        return selectOne("LinkResult.selectLinkResultByPk", paramMap);
    }*/

    public int selectCampaignTargetCount(LinkclickVo linkclickVo) {
        Integer count = (Integer) selectOne("LinkResult.selectCampaignTargetCount", linkclickVo);
        return (count == null) ? 0 : count;
    }

    public int getCampaignReportAbRealLinkCnt(int campaignNo) {
        Integer count = (Integer) selectOne("LinkResult.getCampaignReportAbRealLinkCnt", campaignNo);
        return (count == null) ? 0 : count;
    }
}