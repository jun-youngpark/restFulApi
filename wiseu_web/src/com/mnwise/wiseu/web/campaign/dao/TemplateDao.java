package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;

/**
 * NVTEMPLATE 테이블 DAO 클래스
 */
@Repository
public class TemplateDao extends BaseDao {
    public int insertTemplate(TemplateVo template) {
        return insert("Template.insertTemplate", template);
    }

    public int insertEditorCampaignTemplate(TemplateVo template) {
        return insert("Template.insertEditorCampaignTemplate", template);
    }

    public int copyTemplate(Map<String, Object> paramMap) {
        return insert("Template.copyTemplate", paramMap);
    }

    public int updateEditorCampaignTemplate(TemplateVo template) {
        return update("Template.updateEditorCampaignTemplate", template);
    }

    public int updateCampaignKakaoButtons(TemplateVo templateVo) {
        return update("Template.updateCampaignKakaoButtons", templateVo);
    }

    /**
     * 캠페인 템플릿 삭제한다.
     */
    public int deleteTemplateByPk(int campaignNo, String seg) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("seg", seg);
        return delete("Template.deleteTemplateByPk", paramMap);
    }

    public int deleteTemplateAll(ScenarioVo scenarioVo) {
        return delete("Template.deleteTemplateAll", scenarioVo);
    }

    /**
     * 템플릿 조회
     *
     * @param templateVo 템플릿 정보
     * @return
     */
    public TemplateVo getTemplate(TemplateVo templateVo) {
        return (TemplateVo) selectOne("Template.getTemplate", templateVo);
    }

    /**
     * 캠페인 템플릿 리스트를 가져온다.
     *
     * @param campaignNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TemplateVo> getTemplateList(int campaignNo) {
        return selectList("Template.getTemplateList", campaignNo);
    }

    @SuppressWarnings("unchecked")
    public List<TemplateVo> selectEditorCampaignFrtalkTemplate(int no) {
        return selectList("Template.selectEditorCampaignFrtalkTemplate", no);
    }

    /**
     * 캠페인 템플릿 정보를 가져온다.
     *
     * @param no 캠페인 정보
     */
    @SuppressWarnings("unchecked")
    public List<TemplateVo> selectEditorCampaignTemplate(int no) {
        return selectList("Template.selectEditorCampaignTemplate", no);
    }

    @SuppressWarnings("unchecked")
    public List<MailPreviewVo> selectCampaignTemplate(Map<String, Object> paramMap) {
        return selectList("Template.selectCampaignTemplate", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<MailPreviewVo> selectCampaignTemplateAb(Map<String, Object> paramMap) {
        return selectList("Template.selectCampaignTemplateAb", paramMap);
    }

    public String getCampaignSendTemplate(int serviceNo) {
        return (String) selectOne("Template.getCampaignTemplate", serviceNo);
    }

    public String selectCampaignKakaoButtons(int no) {
        return (String) selectOne("Template.selectCampaignKakaoButtons", no);
    }

    public int selectEditorCampaignTemplateCount(TemplateVo template) {
        Integer count = (Integer) selectOne("Template.selectEditorCampaignTemplateCount", template);
        return (count == null) ? 0 : count;
    }

}