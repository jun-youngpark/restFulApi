package com.mnwise.wiseu.web.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.AjaxSearchVo;

@Repository
public class AjaxSearchDao extends BaseDao  {
    private static final int CAMPAIGN_SENARIO_NM = 1;
    private static final int ECARE_SENARIO_NM = 2;
    private static final int CAMPAIGN_SENARIO_TAG = 3;
    private static final int ECARE_SENARIO_TAG = 4;
    private static final int SEGMENT_NM = 5;
    private static final int SEGMENT_TAG = 6;
    private static final int CONTENTS_NM = 7;
    private static final int CONTENTS_TAG = 8;
    //private static final int POLL_NM = 9;
    private static final int MOBILE_TAG = 10;
    private static final int MOBILE_NM = 11;
    private static final int CAMPAIGN_PREFACE_NM = 12;
    private static final int ECARE_PREFACE_NM = 13;

    @SuppressWarnings("unchecked")
    public List<AjaxSearchVo> selectListSearch(AjaxSearchVo ajaxSearchVo) {
        String sqlMapNameSpacePlusId;
        switch(ajaxSearchVo.getKey()) {
            case CAMPAIGN_SENARIO_NM:
                sqlMapNameSpacePlusId = "Common.scenarioNmSearch";
                break;
            case ECARE_SENARIO_NM:
                sqlMapNameSpacePlusId = "Common.ecareScenarioNmSearch";
                break;
            case CAMPAIGN_SENARIO_TAG:
                sqlMapNameSpacePlusId = "Common.scenarioTagNmSearch";
                break;
            case ECARE_SENARIO_TAG:
                sqlMapNameSpacePlusId = "Common.ecareScenarioTagNmSearch";
                break;
            case SEGMENT_NM:
                sqlMapNameSpacePlusId = "Common.segmentNmSearch";
                break;
            case SEGMENT_TAG:
                sqlMapNameSpacePlusId = "Common.segmentTagNmSearch";
                break;
            case CONTENTS_NM:
                sqlMapNameSpacePlusId = "Common.contentsNmSearch";
                break;
            case CONTENTS_TAG:
                sqlMapNameSpacePlusId = "Common.contentsTagNmSearch";
                break;
            case MOBILE_TAG:
                sqlMapNameSpacePlusId = "Common.mobileTagSearch";
                break;
            case MOBILE_NM:
                sqlMapNameSpacePlusId = "Common.mobileNmSearch";
                break;
            case CAMPAIGN_PREFACE_NM:
                sqlMapNameSpacePlusId = "Common.campaignPrefaceNmSearch";
                break;
            case ECARE_PREFACE_NM:
                sqlMapNameSpacePlusId = "Common.ecarePrefaceNmSearch";
                break;
            default:
                sqlMapNameSpacePlusId = "Common.scenarioNmSearch";
                break;
        }
        return selectList(sqlMapNameSpacePlusId, ajaxSearchVo);
    }
}
