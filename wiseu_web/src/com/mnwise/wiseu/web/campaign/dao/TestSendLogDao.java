package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.TestSendLog;
import com.mnwise.wiseu.web.common.model.TestSendVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVTESTSENDLOG 테이블 DAO 클래스
 */
@Repository
public class TestSendLogDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public List<TestSendLog> selectCampaignTestSend(int campaignNo) {
        return selectList("TestSendLog.selectCampaignTestSend", campaignNo);
    }

    public int getTestSendListCount(int serviceNo) {
        Integer count = (Integer) selectOne("TestSendLog.getTestSendListCount", serviceNo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<TestSendVo> selectTestSend(TestSendVo testSendVo) {
        List<TestSendVo> tmp = (List<TestSendVo>) selectList("TestSendLog.selectTestSendList", testSendVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }


}