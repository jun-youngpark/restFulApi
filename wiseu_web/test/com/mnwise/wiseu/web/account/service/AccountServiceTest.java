package com.mnwise.wiseu.web.account.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 사용자 관리 Test
 */
public class AccountServiceTest extends BaseDaoTestCase {

    private AccountService accountService;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/account/account-applicationContext.xml"
        };
    }

    public AccountServiceTest() {
    }

//    public AccountServiceTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new AccountServiceTest("testInsertSessionlog"));
//        suite.addTest(new AccountServiceTest("testGetAdminLogList"));
//        suite.addTest(new AccountServiceTest("testGetAdminDeptLogList"));
//        suite.addTest(new AccountServiceTest("testGetUserGrpInfo"));
//        suite.addTest(new AccountServiceTest("testGetGrpRoleList"));
//        suite.addTest(new AccountServiceTest("testGetSupraInfo"));
//        suite.addTest(new AccountServiceTest("testInsertAccountDept"));
//        suite.addTest(new AccountServiceTest("testUpdateAccountDept"));
//        suite.addTest(new AccountServiceTest("testUpdateAccountDeptActiveYn"));
//        suite.addTest(new AccountServiceTest("testGetGrpList"));
//        suite.addTest(new AccountServiceTest("testInsertGrpPermission"));
//        return suite;
//    }

    /**
     * 부서정보
     *
     * @throws Exception
     */
    @Test
    public void testGetUserGrpInfo() throws Exception {

        String grpCd = "01";

        UserGrpVo userGrpVo = accountService.getUserGrpInfo(grpCd);
        assertTrue(userGrpVo != null);
    }

    /**
     * 부서별 권한 정보를 가져온다
     *
     * @throws Exception
     */
    @Test
    public void testGetGrpRoleList() throws Exception {

        String grpCd = "0101";
        String lang = "ko";

        List<PermissionVo> permissionList = accountService.getGrpRoleList(grpCd, lang);
        assertTrue(permissionList != null);
    }

    /**
     * 상위 부서의 정보를 가져온다
     *
     * @throws Exception
     */
    @Test
    public void testGetSupraInfo() throws Exception {

        String supraGrpCd = "0101";

        UserGrpVo userGrpVo = accountService.getSupraInfo(supraGrpCd);
        assertTrue(userGrpVo != null);
    }

    /**
     * 부서 저장
     *
     * @param userGrpVo
     * @return
     * @throws Exception
     * @throws Exception
     */
    @Test
    public void testInsertAccountDept() throws Exception {
        String supraGrpCd = "01";
        String grpNm = "테스트 부서";
        String funcCode = "funcCode";
        String funcDesc = "funcDesc";
        String workDoc = "workDoc";
        String editorId = "admin";
        String lastUpdateDt = "20091023";
        String soeId = "1111111";
        String userRole = "A";

        UserGrpVo userGrpVo = new UserGrpVo();
        userGrpVo.setSupraGrpCd(supraGrpCd);
        userGrpVo.setGrpNm(grpNm);
        userGrpVo.setFuncCode(funcCode);
        userGrpVo.setFuncDesc(funcDesc);
        userGrpVo.setEditorId(editorId);
        userGrpVo.setLastUpdateDt(lastUpdateDt);

        String grpCd = accountService.insertAccountDept(userGrpVo, soeId, userRole);
        assertTrue(grpCd != null);
    }

    /**
     * 부서 정보 수정
     *
     * @param userGrpVo
     * @return
     * @throws Exception
     * @throws Exception
     */
    @Test
    public void testUpdateAccountDept() throws Exception {
        String grpNm = "테스트 부서";
        String funcCode = "funcCode";
        String funcDesc = "funcDesc";
        String workDoc = "workDoc";
        String editorId = "admin";
        String lastUpdateDt = "20091023";
        String grpCd = "0101";

        UserGrpVo userGrpVo = new UserGrpVo();
        userGrpVo.setGrpNm(grpNm);
        userGrpVo.setFuncCode(funcCode);
        userGrpVo.setFuncDesc(funcDesc);
        userGrpVo.setEditorId(editorId);
        userGrpVo.setLastUpdateDt(lastUpdateDt);
        userGrpVo.setGrpCd(grpCd);

        assertEquals(1, accountService.updateAccountDept(userGrpVo));

    }

    /**
     * 부서 리스트를 가져온다.
     */
    @Test
    public void testGetGrpList() throws Exception {

        List<UserGrpVo> grpList = accountService.getGrpList();
        assertTrue(grpList != null);
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
