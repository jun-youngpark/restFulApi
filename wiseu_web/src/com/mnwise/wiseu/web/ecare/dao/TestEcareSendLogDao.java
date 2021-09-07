package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.TestSendVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVTESTECARESENDLOG 테이블 DAO 클래스
 */
@Repository
public class TestEcareSendLogDao extends BaseDao {

    public int getTestEcareSendListCount(int serviceNo) {
        Integer count = (Integer) selectOne("TestEcareSendLog.getTestEcareSendListCount", serviceNo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<TestSendVo> selectTestEcareSend(TestSendVo testSendVo) {
        List<TestSendVo> tmp = (List<TestSendVo>) selectList("TestEcareSendLog.selectTestEcareSendList", testSendVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

}