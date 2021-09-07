package com.mnwise.wiseu.web.account.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * AccountDao
 */
public class AccountDaoTest extends BaseDaoTestCase {
    private UserGrpDao userGrpDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            // ,"file:**/conf/account/account-applicationContext.xml"
            , "file:**/conf/account/account-applicationContext.xml"
        };
    }

    public AccountDaoTest() {
    }

//    public AccountDaoTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new AccountDaoTest("testGetGrpList"));
//        return suite;
//    }

    /**
     * 부서 리스트를 가져온다.
     */
    @Test
    public void testGetGrpList() throws Exception {
        List<UserGrpVo> grpList = userGrpDao.getGrpList();
        assertTrue("", grpList != null);
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setUserGrpDao(UserGrpDao userGrpDao) {
        this.userGrpDao = userGrpDao;
    }
}
