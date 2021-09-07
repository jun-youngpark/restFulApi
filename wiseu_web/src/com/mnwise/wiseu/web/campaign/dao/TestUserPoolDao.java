package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.TesterPoolVo;

/**
 * NVTESTUSERPOOL 테이블 DAO 클래스
 */
@Repository
public class TestUserPoolDao extends BaseDao {

    /**
     * 파일업로드 테스터 저장
     *
     * @param testerPoolList
     * @return
     */
    public int insertFileUploadTester(List<TesterPoolVo> testerPoolList) {
        int row = 0;

        for(TesterPoolVo testerPoolVo : testerPoolList) {
            row += insert("TestUserPool.insertTestUserPool", testerPoolVo);
        }

        return row;
    }

    /**
     * 테스터 추가
     *
     * @param testerPoolVo
     * @return
     */
    public int insertTester(TesterPoolVo testerPoolVo) {
        security.securityObject(testerPoolVo, "ENCRYPT");
        Integer ret = insert("TestUserPool.insertTester", testerPoolVo);
        return ret != null ? ret : 0;
    }

    public int updateTestUserPoolByPk(TesterPoolVo testUserPool) {
        security.securityObject(testUserPool, "ENCRYPT");
        return update("TestUserPool.updateTestUserPoolByPk", testUserPool);
    }

    public int deleteTestUserPoolByPk(String userId, int seqNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("seqNo", seqNo);
        return delete("TestUserPool.deleteTestUserPoolByPk", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<TesterPoolVo> getTestSendList(List<Integer> seqNoList) {
        return selectList("TestUserPool.getTestSendList", seqNoList);
    }

    /**
     * 테스터 리스트를 가져온다.
     *
     * @param testPoolVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TesterPoolVo> getTesterList(TesterPoolVo testPoolVo) {
        List<TesterPoolVo> tmp = (List<TesterPoolVo>) selectList("TestUserPool.getTesterList", testPoolVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * seqNo Max 가져오기
     *
     * @param userId
     * @return
     */
    public int getTesterPoolMaxSeq(String userId) {
        return (Integer) selectOne("TestUserPool.getTesterPoolMaxSeq", userId);
    }

}