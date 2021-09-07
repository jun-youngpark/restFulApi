package com.mnwise.wiseu.web.common.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mnwise.wiseu.web.common.model.TestGrpVo;
import com.mnwise.wiseu.web.common.model.TesterPoolVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 테스트 발송
 */
public class TesterServiceTest extends BaseDaoTestCase {

    private TesterService testerService;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/common/common-applicationContext.xml"
        };
    }

    public TesterServiceTest() {
        super();
    }

//    public TesterServiceTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new TesterServiceTest("testGetTestGrpList"));
//        suite.addTest(new TesterServiceTest("testGetTestGrpInfo"));
//        suite.addTest(new TesterServiceTest("testInsertTestGrp"));
//        suite.addTest(new TesterServiceTest("testUpdateTestGrp"));
//        suite.addTest(new TesterServiceTest("testUpdateTestGrpActiveYn"));
//        suite.addTest(new TesterServiceTest("testInsertTester"));
//        suite.addTest(new TesterServiceTest("testUpdateTester"));
//        suite.addTest(new TesterServiceTest("testDeleteTester"));
//        suite.addTest(new TesterServiceTest("testInsertTestUser"));
//        return suite;
//    }

    /**
     * 테스트 그룹 리스트를 가져온다
     *
     * @param userId 아이디
     * @return
     */
    @Test
    public void testGetTestGrpList() throws Exception {
        String userId = "admin";

        List<TestGrpVo> list = testerService.getTestGrpList(userId);

        assertTrue(list != null);
    }

    /**
     * 테스트 그룹 정보
     *
     * @param testGrpCd 테스트 그룹코드
     * @return
     * @throws Exception
     */
    @Test
    public void testGetTestGrpInfo() throws Exception {
        int testGrpCd = 1;
        String userId = "admin";

        TestGrpVo testgrpVo = testerService.getTestGrpInfo(testGrpCd, userId);
        assertTrue(testgrpVo != null);
    }

    /**
     * 테스트 그룹 추가
     *
     * @param testGrpVo
     * @return
     * @throws Exception
     */
    @Test
    public void testInsertTestGrp() throws Exception {
        String userId = "admin";
        String testGrpNm = "test";
        int testSupragrpCd = 1;

        TestGrpVo testGrpVo = new TestGrpVo();
        testGrpVo.setUserId(userId);
        testGrpVo.setTestGrpNm(testGrpNm);
        testGrpVo.setTestSupragrpCd(testSupragrpCd);

        assertTrue(testerService.insertTestGrp(testGrpVo) >= 0);
    }

    /**
     * 테스트 그룹 수정
     *
     * @throws Exception
     */
    @Test
    public void testUpdateTestGrp() throws Exception {
        String userId = "admin";
        String testGrpNm = "test";
        int testGrpCd = 5;

        TestGrpVo testGrpVo = new TestGrpVo();
        testGrpVo.setUserId(userId);
        testGrpVo.setTestGrpNm(testGrpNm);
        testGrpVo.setTestGrpCd(testGrpCd);

        assertTrue(testerService.updateTestGrp(testGrpVo) >= 0);
    }

    /**
     * 테스트 그룹 삭제
     *
     * @throws Exception
     */
    @Test
    public void testUpdateTestGrpActiveYn() throws Exception {
        int testGrpCd = 5;
        int result = testerService.updateTestGrpActiveYn(testGrpCd);

        assertTrue(result >= 0);
    }

    /**
     * 테스터 추가
     *
     * @param testerPoolVo
     * @return
     * @throws Exception
     */
    @Test
    public void testInsertTester() throws Exception {

        String userId = "admin";
        int seqNo = 1;
        String testReceiverEmail = "test@test.com";
        String testReceiverNm = "홍길동";
        String testReceiverTel = "5946";
        int testGrpCd = 1;

        TesterPoolVo testerPoolVo = new TesterPoolVo();
        testerPoolVo.setTestGrpCd(testGrpCd);
        testerPoolVo.setUserId(userId);
        testerPoolVo.setTestReceiverEmail(testReceiverEmail);
        testerPoolVo.setTestReceiverNm(testReceiverNm);
        testerPoolVo.setTestReceiverTel(testReceiverTel);
        testerPoolVo.setSeqNo(seqNo);

        int result = testerService.insertTester(testerPoolVo);
        assertTrue(result >= 0);
    }

    /**
     * 테스터 수정
     *
     * @param testerPoolVo
     * @return
     * @throws Exception
     */
    @Test
    public void testUpdateTester() throws Exception {
        String userId = "admin";
        int seqNo = 1;
        String testReceiverEmail = "test@test.com";
        String testReceiverNm = "홍길동";
        String testReceiverTel = "5946";
        int testGrpCd = 1;

        TesterPoolVo testerPoolVo = new TesterPoolVo();
        testerPoolVo.setTestGrpCd(testGrpCd);
        testerPoolVo.setUserId(userId);
        testerPoolVo.setTestReceiverEmail(testReceiverEmail);
        testerPoolVo.setTestReceiverNm(testReceiverNm);
        testerPoolVo.setTestReceiverTel(testReceiverTel);
        testerPoolVo.setSeqNo(seqNo);

        int result = testerService.updateTester(testerPoolVo);
        assertTrue(result >= 0);

    }

    /**
     * 테스터 삭제
     *
     * @param userId
     * @param seqNo
     * @return
     * @throws Exception
     */
    @Test
    public void testDeleteTester() throws Exception {
        String userId = "admin";
        int[] seqNo = {
            1, 2, 3
        };

        int result = testerService.deleteTester(userId, seqNo);
        assertTrue(result >= 0);
    }

    /**
     * 테스트 발송
     *
     * @param testerVoList
     * @return
     * @throws Exception
     */
    @Test
    public void testInsertTestUser() throws Exception {
        int testGrpCd = 1;
        String checkSeqNo = "5,6,7,";
        String serviceType = "C";
        String userId = "admin";
        int no = 1;

        int result = testerService.insertTestUser(checkSeqNo, no, serviceType, userId);
        assertTrue(result >= 0);

    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setTesterService(TesterService testerService) {
        this.testerService = testerService;
    }

}
