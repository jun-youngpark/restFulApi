package com.mnwise.wiseu.web.admin.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 사용자 로그인 서비스 테스트
 */
public class AdminServiceTest extends BaseDaoTestCase {

    private AdminService adminService;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/admin/admin-applicationContext.xml"
        };
    }

    public AdminServiceTest() {
        super();
    }

//    public AdminServiceTest(String name) {
//        super(name);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new AdminServiceTest("testGetUserInfoBySoeid"));
//
//        return suite;
//    }

    /**
     * SOEID 인증 체크
     *
     * @throws Exception
     */
    @Test
    public void testGetUserInfoBySoeid() throws Exception {
        String soeid = "MK19142";

        UserVo userVo = adminService.getUserInfoBySoeid(soeid);
        assertNotNull(userVo);
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }
}
