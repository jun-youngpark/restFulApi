package com.mnwise.wiseu.web.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mnwise.wiseu.web.account.dao.AccountDaoTest;
import com.mnwise.wiseu.web.account.dao.AccountDeptDaoTest;
import com.mnwise.wiseu.web.account.service.AccountServiceTest;
import com.mnwise.wiseu.web.admin.dao.AdminDaoTest;
import com.mnwise.wiseu.web.admin.service.AdminServiceTest;
import com.mnwise.wiseu.web.base.CepacTest;
import com.mnwise.wiseu.web.common.dao.CdMstDaoTest;
import com.mnwise.wiseu.web.common.dao.TestMailSendDaoTest;
import com.mnwise.wiseu.web.common.service.CdMstServiceTest;
import com.mnwise.wiseu.web.common.service.TesterServiceTest;
import com.mnwise.wiseu.web.common.util.DataDownloadConverterTest;
import com.mnwise.wiseu.web.editor.dao.EditorCampaignDaoTest;
import com.mnwise.wiseu.web.env.service.EnvMyInfoServiceTest;
import com.mnwise.wiseu.web.report.dao.CampaignCommonDaoTest;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCommonServiceTest;

import junit.framework.TestCase;

/**
 * Dao 통합 테스트
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AccountDaoTest.class,
    AccountDeptDaoTest.class,
    AccountServiceTest.class,
    AdminDaoTest.class,
    AdminServiceTest.class,
    CepacTest.class,
    CdMstDaoTest.class,
    CdMstServiceTest.class,
    TesterServiceTest.class,
    DataDownloadConverterTest.class,
    EditorCampaignDaoTest.class,
    EnvMyInfoServiceTest.class,
    CampaignCommonDaoTest.class,
    CampaignCommonServiceTest.class,
    TestMailSendDaoTest.class,
    TesterServiceTest.class,
    EnvMyInfoServiceTest.class
})
public class AllTest extends TestCase {

//    public static Test suite() {
//        TestSuite testSuite = new TestSuite();
//
//        /** 사용자 관리 테스트 **/
//        testSuite.addTestSuite(AccountDaoTest.class);
//        testSuite.addTestSuite(AccountDeptDaoTest.class);
//        testSuite.addTestSuite(AccountServiceTest.class);
//        testSuite.addTestSuite(AdminDaoTest.class);
//        testSuite.addTestSuite(AdminServiceTest.class);
//
//        /** 공통 Dao & 서비스 테스트 **/
//        testSuite.addTestSuite(CepacPropertyPlaceholderConfigurerTest.class);
//        testSuite.addTestSuite(CepacTest.class);
//
//        testSuite.addTestSuite(CdMstDaoTest.class);
//        testSuite.addTestSuite(CdMstServiceTest.class);
//        testSuite.addTestSuite(TesterServiceTest.class);
//        testSuite.addTestSuite(DataDownloadConverterTest.class);
//        testSuite.addTestSuite(EditorCampaignDaoTest.class);
//        testSuite.addTestSuite(EnvMyInfoServiceTest.class);
//
//        /** 리포트 테스트 */
//        testSuite.addTestSuite(CampaignCommonDaoTest.class);
//        testSuite.addTestSuite(CampaignCommonServiceTest.class);
//
//        /** 테스트 발송 테스트 **/
//        testSuite.addTestSuite(TestMailSendDaoTest.class);
//        testSuite.addTestSuite(TesterServiceTest.class);
//
//        /** 개인정보관리 테스트 **/
//        testSuite.addTestSuite(EnvMyInfoServiceTest.class);
//
//        return testSuite;
//    }
}
