package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.common.model.TesterVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * NVTESTUSER 테이블 DAO 클래스
 */
@Repository
public class TestUserDao extends BaseDao {

    /**
     * 테스트 발송
     *
     * @param testerList
     * @return
     */
    public int insertTestUser(List<TesterVo> testerList) {
        int row = 0;

        for(TesterVo testerVo : testerList) {
            row += insert("TestUser.insertTestUser", testerVo);
        }

        return row;
    }

    public int copyTestUserByCampaign(CampaignVo campaignVo) {
        return insert("TestUser.copyTestUserByCampaign", campaignVo);
    }

    public int copyTestUserByEcare(EcareVo ecare) {
        return insert("TestUser.copyTestUserByEcare", ecare);
    }

    public int deleteTestUserAll(ScenarioVo scenarioVo) {
        return delete("TestUser.deleteTestUserAll", scenarioVo);
    }

    /**
     * 기존에 발송한 테스트 대상자가 있을 경우 삭제한다.
     *
     * @param testerVo
     * @return
     */
    public int deleteTestUser(TesterVo testerVo) {
        return delete("TestUser.deleteTestUser", testerVo);
    }

    public int deleteEcareTestUser(Map<String, Object> paramMap) {
        return delete("TestUser.deleteEcareTestUser", paramMap);
    }

}