package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;
import com.mnwise.wiseu.web.segment.model.LinkclickVo;

/**
 * NVSCENARIO 테이블 DAO 클래스
 */
@Repository
public class ScenarioDao extends BaseDao {
    /**
     * 시나리오 정보를 변경한다.
     *
     * @param scenarioVo
     */
    public int updateScenario2StepInfo(ScenarioVo scenarioVo) {
        return update("Scenario.updateScenario2StepInfo", scenarioVo);
    }

    /**
     * 캠페인 기본정보를 등록한다.
     *
     * @param scenarioVo
     */
    public int insertScenario1StepInfo(ScenarioVo scenarioVo) {
        return insert("Scenario.insertScenario1StepInfo", scenarioVo);
    }

    /**
     * 캠페인 기본정보를 변경한다.
     *
     * @param scenarioVo
     */
    public int updateScenario1StepInfo(ScenarioVo scenarioVo) {
        return update("Scenario.updateScenario1StepInfo", scenarioVo);
    }

    public int deleteScenario(ScenarioVo scenarioVo) {
        return delete("Scenario.deleteScenario", scenarioVo);
    }

    /**
     * 시나리오 및 캠페인 기본정보를 가져온다.
     *
     * @param scenarioVo
     */
    public ScenarioVo selectScenarioBasicInfo(ScenarioVo scenarioVo) {
        ScenarioVo tmp = (ScenarioVo) selectOne("Scenario.selectScenarioBasicInfo", scenarioVo);
        security.securityObject(tmp, "DECRYPT");
        security.securityObject(tmp.getUserVo(), "DECRYPT");
        security.securityObjectList(tmp.getCampaignList(), "DECRYPT");
        return tmp;
    }

    /**
     * 시나리오 정보를 가져온다.
     *
     * @param scenarioNo
     * @param campaignNo
     * @return
     */
    public ScenarioVo selectScenarioDetailInfo(int scenarioNo, int campaignNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scenarioNo", scenarioNo);
        paramMap.put("campaignNo", campaignNo);

        ScenarioVo decScenarioVo = (ScenarioVo) selectOne("Scenario.selectScenarioDetailInfo", paramMap);

        if(null != decScenarioVo) {
            security.securityObject(decScenarioVo, "DECRYPT");
            security.securityObject(decScenarioVo.getCampaignVo(), "DECRYPT");
            security.securityObjectList(decScenarioVo.getCampaignList(), "DECRYPT");
        }

        return decScenarioVo;
    }

    /**
     * 시나리오를 복사한다.
     *
     * @param scenarioVo
     * @return
     */
    public int copyScenario(ScenarioVo scenarioVo) {
        int maxScenarioNo = selectMaxScenarioNo(scenarioVo);

        scenarioVo.setNewScenarioNo(maxScenarioNo);
        insert("Scenario.copyScenario", scenarioVo);
        return maxScenarioNo;
    }

    /**
     * 캠페인 리스트의 총 Row Count를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    public int getScenarioTotalCount(ScenarioVo scenarioVo) {
        Integer count = (Integer) selectOne("Scenario.getScenarioTotalCount", scenarioVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 캠페인 리스트를 가져온다.
     *
     * @param scenarioVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ScenarioVo> getCampaignScenarioList(ScenarioVo scenarioVo) {
        List<ScenarioVo> scenarioVoList = selectList("Scenario.getCampaignScenarioList", scenarioVo);
        for(ScenarioVo vo : scenarioVoList) {
            security.securityObject(vo.getUserVo(), "DECRYPT");
        }

        return scenarioVoList;
    }

    /**
     * 시나리오 번호가 동일하고 NVCAMPAIGN.RELATION_TYPE 값이 'N', 'S', 'F', 'O' 인 것을 모두 조회한다.
     *
     * @param scenarioVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ScenarioVo> selectOmniChannelCampaigns(ScenarioVo scenarioVo) {
        return selectList("Scenario.selectOmniChannelCampaigns", scenarioVo);
    }

    public int selectMaxScenarioNo(ScenarioVo scenarioVo) {
        return (Integer) selectOne("Scenario.selectMaxScenarioNo", scenarioVo);
    }

    public ScenarioInfoVo selectScenarioInfo(int campaignNo) {
        ScenarioInfoVo tmp =  (ScenarioInfoVo) selectOne("Scenario.selectScenarioInfo", campaignNo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    public int selectCampaignListTotalCount(ScenarioVo scenarioVo) {
        Integer count = (Integer) selectOne("Scenario.selectLinkCampaignListTotalCount", scenarioVo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<LinkclickVo> selectCampaignList(ScenarioVo scenarioVo) {
        List<LinkclickVo> tmp = (List<LinkclickVo>) selectList("Scenario.selectLinkCampaignList", scenarioVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 캠페인 작성자 정보 조회
     *
     * @param scenarioNo
     * @return
     */
    public CaseInsensitiveMap selectCreateUserInfo(int scenarioNo) {
        return (CaseInsensitiveMap) selectOne("Scenario.selectCreateUserInfo", scenarioNo);
    }
}