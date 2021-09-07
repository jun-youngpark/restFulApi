package com.mnwise.wiseu.web.campaign.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;

/**
 * NVAPPLICATION 테이블 DAO 클래스
 */
@Repository
public class ApplicationDao extends BaseDao {

    public int copyApplication(CampaignVo campaignVo) {
        return insert("Application.copyApplication", campaignVo);
    }

    public int insertEditorCampaignHandler(HandlerVo handler) {
        return insert("Application.insertEditorCampaignHandler", handler);
    }

    public int updateEditorCampaignHandler(HandlerVo handler) {
        return update("Application.updateEditorCampaignHandler", handler);
    }

    public int updateCampaignHandlerFromDefault(Map<String, Object> paramMap) {
        return update("Application.updateCampaignHandlerFromDefault", paramMap);
    }

    public int deleteApplicationAll(ScenarioVo scenarioVo) {
        return delete("Application.deleteApplicationAll", scenarioVo);
    }

    /**
     * 캠페인 핸들러 정보를 가져온다.
     *
     * @param campaignNo 캠페인 번호
     */
    public HandlerVo selectEditorCampaignHandler(int campaignNo) {
        return (HandlerVo) selectOne("Application.selectEditorCampaignHandler", campaignNo);
    }

    public int selectEditorCampaignHandlerCount(int campaignNo) {
        Integer count = (Integer) selectOne("Application.selectEditorCampaignHandlerCount", campaignNo);
        return (count == null) ? 0 : count;
    }
}