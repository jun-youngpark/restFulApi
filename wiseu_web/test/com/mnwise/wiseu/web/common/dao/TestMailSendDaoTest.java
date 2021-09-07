package com.mnwise.wiseu.web.common.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.TestGrpDao;
import com.mnwise.wiseu.web.campaign.dao.TestUserDao;
import com.mnwise.wiseu.web.campaign.dao.TestUserPoolDao;
import com.mnwise.wiseu.web.common.model.TestGrpVo;
import com.mnwise.wiseu.web.common.model.TesterPoolVo;
import com.mnwise.wiseu.web.common.model.TesterVo;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 테스트 발송 (그룹포함) 단위 테스트 Dao
 */
public class TestMailSendDaoTest extends BaseDaoTestCase {
    CampaignDao campaignDao;
    EcareDao ecareDao;
    TestGrpDao testGrpDao;
    TestUserDao testUserDao;
    TestUserPoolDao testUserPoolDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/common/common-applicationContext.xml"
        };
    }

    public TestMailSendDaoTest() {

    }

//    public TestMailSendDaoTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new TestMailSendDaoTest("testGetTestGrpList"));
//        suite.addTest(new TestMailSendDaoTest("testUpdateTestGrpActiveYn"));
//        suite.addTest(new TestMailSendDaoTest("testUpdateTestGrp"));
//        suite.addTest(new TestMailSendDaoTest("testInsertTestGrp"));
//        suite.addTest(new TestMailSendDaoTest("testGetTesterList"));
//        suite.addTest(new TestMailSendDaoTest("testGetTestGrpInfo"));
//        suite.addTest(new TestMailSendDaoTest("testInsertTester"));
//        suite.addTest(new TestMailSendDaoTest("testUpdateTester"));
//        suite.addTest(new TestMailSendDaoTest("testDeleteTester"));
//        suite.addTest(new TestMailSendDaoTest("testInsertFileUploadTester"));
//        suite.addTest(new TestMailSendDaoTest("testGetTesterPoolMaxSeq"));
//        suite.addTest(new TestMailSendDaoTest("testInsertTestUser"));
//        suite.addTest(new TestMailSendDaoTest("testDeleteTestUser"));
//        suite.addTest(new TestMailSendDaoTest("testUpdateTestModeCampaign"));
//        suite.addTest(new TestMailSendDaoTest("testUpdateTestModeEcare"));
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
        List<TestGrpVo> list = testGrpDao.getTestGrpList(userId);
        assertTrue(list != null);
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
        int testSupragrpCd = 2;

        TestGrpVo testGrpVo = new TestGrpVo();
        testGrpVo.setUserId(userId);
        testGrpVo.setTestGrpNm(testGrpNm);
        testGrpVo.setTestSupragrpCd(testSupragrpCd);
        int testGrpCd = testGrpDao.insertTestGrp(testGrpVo);
        assertTrue(testGrpCd > 0);
    }

    /**
     * 테스트 그룹 수정
     *
     * @param testGrpVo
     * @return
     * @throws Exception
     */
    @Test
    public void testUpdateTestGrp() throws Exception {
        String userId = "admin";
        String testGrpNm = "test";
        int testSupragrpCd = 1;

        TestGrpVo testGrpVo = new TestGrpVo();
        testGrpVo.setUserId(userId);
        testGrpVo.setTestGrpNm(testGrpNm);
        testGrpVo.setTestSupragrpCd(testSupragrpCd);

        assertEquals(0, testGrpDao.updateTestGrp(testGrpVo));

    }

    /**
     * 테스트 그룹 삭제 ( ActiveYn 값을 N으로)
     *
     * @param testGrpCd 테스트 그룹 코드
     * @return
     * @throws Exception
     */
    @Test
    public void testUpdateTestGrpActiveYn() throws Exception {
        int testGrpCd = 3;
        int result = testGrpDao.updateTestGrpActiveYn(testGrpCd);
        assertEquals(0, result);
    }

    /**
     * 테스터 리스트를 가져온다.
     *
     * @param grpCd 그룹코드
     * @return
     * @throws Exception
     */
    @Test
    public void testGetTesterList() throws Exception {
        String userId = "admin";
        int testGrpCd = 1;

        TesterPoolVo testPoolVo = new TesterPoolVo();
        testPoolVo.setUserId(userId);
        testPoolVo.setTestGrpCd(testGrpCd);

        List<TesterPoolVo> list = testUserPoolDao.getTesterList(testPoolVo);

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
        int testGrpCd = 0;
        String userId = "admin";

        Map<String, Object> map = new HashMap<>();
        map.put("testGrpCd", testGrpCd);
        map.put("userId", userId);

        TestGrpVo testgrpVo = testGrpDao.getTestGrpInfo(map);
        assertTrue(testgrpVo != null);
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

        int result = testUserPoolDao.insertTester(testerPoolVo);
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

        int result = testUserPoolDao.updateTestUserPoolByPk(testerPoolVo);
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

        int result = 0;
        for(int i = 0; i < 3; i++) {
            result += testUserPoolDao.deleteTestUserPoolByPk(userId, seqNo[i]);
        }

        assertTrue(result >= 0);
    }

    /**
     * 파일업로드 테스터 저장
     *
     * @param testerPoolList
     * @return
     * @throws Exception
     */
    @Test
    public void testInsertFileUploadTester() throws Exception {
        String userId = "admin";
        int testGrpCd = 1;
        String testReceiverNm = "test1";
        String testReceiverEmail = "test@test.com";
        String testReceiverTel = "11111";
        int[] seqNo = {
            100, 101, 102
        };

        List<TesterPoolVo> list = new ArrayList();
        for(int i = 0; i < 3; i++) {
            TesterPoolVo testerPoolVo = new TesterPoolVo();
            testerPoolVo.setUserId(userId);
            testerPoolVo.setTestReceiverEmail(testReceiverEmail);
            testerPoolVo.setTestReceiverNm(testReceiverNm);
            testerPoolVo.setTestReceiverTel(testReceiverTel);
            testerPoolVo.setSeqNo(seqNo[i]);

            list.add(testerPoolVo);
        }

        int result = testUserPoolDao.insertFileUploadTester(list);
        System.out.println("============================ > result : " + result);
        assertTrue(result >= 0);

    }

    /**
     * seqNo Max 가져오기
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Test
    public void testGetTesterPoolMaxSeq() throws Exception {
        String userId = "admin";

        int result = testUserPoolDao.getTesterPoolMaxSeq(userId);
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
        int[] seqNo = {
            5, 6, 7
        };
        String userId = "admin";
        String serviceType = "C";
        int no = 1;

        List<TesterVo> list = new ArrayList();
        for(int i = 0; i < 3; i++) {
            TesterVo testerVo = new TesterVo();
            testerVo.setUserId(userId);
            testerVo.setSeqNo(seqNo[i]);
            testerVo.setServiceType(serviceType);
            testerVo.setServiceNo(no);

            list.add(testerVo);
        }

        int result = testUserDao.insertTestUser(list);
        System.out.println("============================ > result : " + result);
        assertTrue(result >= 0);

    }

    /**
     * 기존에 발송한 테스트 대상자가 있을 경우 삭제한다.
     *
     * @param testerVo
     * @return
     * @throws Exception
     */
    @Test
    public void testDeleteTestUser() throws Exception {
        String userId = "admin";
        String serviceType = "C";
        int no = 1;

        TesterVo testerVo = new TesterVo();
        testerVo.setUserId(userId);
        testerVo.setServiceType(serviceType);
        testerVo.setServiceNo(no);

        assertEquals(0, testUserDao.deleteTestUser(testerVo));

    }

    /**
     * 테스트 발송시 ServiceType이 C NVCAMAPAIGN의 SENDING_MODE 를 T로 바꿔준다.
     *
     * @param no
     * @return
     * @throws Exception
     */
    @Test
    public void testUpdateTestModeCampaign() throws Exception {
        int no = 1;

        assertTrue(campaignDao.updateTestModeCampaign(no) >= 0);
    }

    /**
     * 테스트 발송시 ServiceType이 E NVECAREMSG의 SENDING_MODE 를 T로 바꿔준다.
     *
     * @param no
     * @return
     * @throws Exception
     */
    @Test
    public void testUpdateTestModeEcare() throws Exception {
        int no = 1;

        assertEquals(0, ecareDao.updateTestModeEcare(no));
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setTestGrpDao(TestGrpDao testGrpDao) {
        this.testGrpDao = testGrpDao;
    }

    public void setTestUserDao(TestUserDao testUserDao) {
        this.testUserDao = testUserDao;
    }

    public void setTestUserPoolDao(TestUserPoolDao testUserPoolDao) {
        this.testUserPoolDao = testUserPoolDao;
    }

}
