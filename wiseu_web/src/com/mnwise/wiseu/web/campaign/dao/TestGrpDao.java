package com.mnwise.wiseu.web.campaign.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.TestGrpVo;

/**
 * NV_TEST_GRP 테이블 DAO 클래스
 */
@Repository
public class TestGrpDao extends BaseDao {
    /**
     * 테스트 그룹 추가
     *
     * @param testGrp
     * @return
     */
    public int insertTestGrp(TestGrpVo testGrp) {
        insert("TestGrp.insertTestGrp", testGrp);
        return testGrp.getTestGrpCd();
    }

    /**
     * 테스트 그룹 삭제 ( ActiveYn 값을 N으로)
     *
     * @param testGrpCd
     * @return
     */
    public int updateTestGrpActiveYn(int testGrpCd) {
        return update("TestGrp.updateTestGrpActiveYn", testGrpCd);
    }

    /**
     * 테스트 그룹 수정
     *
     * @param testGrpVo
     * @return
     */
    public int updateTestGrp(TestGrpVo testGrpVo) {
        return insert("TestGrp.updateTestGrp", testGrpVo);
    }

    /**
     * 테스트 그룹 리스트를 가져온다
     *
     * @param userId 아이디
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TestGrpVo> getTestGrpList(String userId) {
        return selectList("TestGrp.getTestGrpList", userId);
    }

    /**
     * 테스트 그룹 정보
     *
     * @param map 테스트 그룹코드
     * @return
     */
    public TestGrpVo getTestGrpInfo(Map<String, Object> map) {
        return (TestGrpVo) selectOne("TestGrp.getTestGrpInfo", map);
    }
}