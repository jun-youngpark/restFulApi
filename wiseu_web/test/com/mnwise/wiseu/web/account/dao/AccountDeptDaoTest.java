package com.mnwise.wiseu.web.account.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.mnwise.wiseu.web.account.model.GrpMenuRole;
import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 부서 관리
 */
public class AccountDeptDaoTest extends BaseDaoTestCase {
    private UserGrpDao userGrpDao;
    private MenuDao menuDao;
    private GrpMenuRoleDao grpMenuRoleDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/account/account-applicationContext.xml"
        };
    }

    public AccountDeptDaoTest() {
    }

//    public AccountDeptDaoTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new AccountDeptDaoTest("testGetUserGrpInfo"));
//        suite.addTest(new AccountDeptDaoTest("testGetGrpRoleList"));
//        suite.addTest(new AccountDeptDaoTest("testGetSupraInfo"));
//        suite.addTest(new AccountDeptDaoTest("testInsertAccountDept"));
//        suite.addTest(new AccountDeptDaoTest("testUpdateAccountDept"));
//        suite.addTest(new AccountDeptDaoTest("testUpdateAccountDeptActiveYn"));
//        suite.addTest(new AccountDeptDaoTest("testDeleteGrpPermission"));
//        suite.addTest(new AccountDeptDaoTest("testInsertGrpPermission"));
//        return suite;
//    }

    /**
     * 부서 정보를 가져온다. - 부서 코드가 존재하지 않을 경우 최상위 부서를 가져온다.
     */
    @Test
    public void testGetUserGrpInfo() throws Exception {

        String grpCd = "";

        UserGrpVo userGrpVo = userGrpDao.selectGroupInfo(grpCd);
        assertTrue(userGrpVo != null);
    }

    /**
     * 부서별 권한 정보를 가져온다
     */
    @Test
    public void testGetGrpRoleList() throws Exception {

        String grpCd = "0101";
        String language = "ko";

        List<PermissionVo> permissionList = menuDao.getGrpRoleList(grpCd, language);
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

        UserGrpVo userGrpVo = userGrpDao.getSupraInfo(supraGrpCd);
        assertTrue(userGrpVo != null);
    }

    /**
     * 부서 저장
     *
     * @param userGrpVo
     * @return
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

        UserGrpVo userGrpVo = new UserGrpVo();
        userGrpVo.setSupraGrpCd(supraGrpCd);
        userGrpVo.setGrpNm(grpNm);
        userGrpVo.setFuncCode(funcCode);
        userGrpVo.setFuncDesc(funcDesc);
        userGrpVo.setEditorId(editorId);
        userGrpVo.setLastUpdateDt(lastUpdateDt);

        String grpCd = userGrpDao.insertUserGrp(userGrpVo);
        assertTrue(grpCd != null);
    }

    /**
     * 부서 정보 수정
     *
     * @param userGrpVo
     * @return
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

        assertEquals(1, userGrpDao.updateAccountDept(userGrpVo));

    }

    /**
     * 부서의 권한 정보를 모두 삭제한다
     *
     * @param grpCd
     * @return
     * @throws DataAccException
     */
    @Test
    public void testDeleteGrpPermission() throws Exception {
        String grpCd = "01";

        assertTrue(grpMenuRoleDao.deleteGrpPermission(grpCd) >= 0);
    }

    /**
     * 부서의 권한을 저장한다.
     *
     * @param permissionVo
     * @return
     * @throws Exception
     */
    @Test
    public void testInsertGrpPermission() throws Exception {
        String grpCd = "01";
        String menuCd = "0101";
        String readAuth = "R";
        String writeAuth = "W";
        String executeAuth = "X";

        GrpMenuRole grpMenuRole = new GrpMenuRole();
        grpMenuRole.setGrpCd(grpCd);
        grpMenuRole.setMenuCd(menuCd);
        grpMenuRole.setReadAuth(readAuth);
        grpMenuRole.setWriteAuth(writeAuth);
        grpMenuRole.setExecuteAuth(executeAuth);

        assertTrue(grpMenuRoleDao.insertGrpMenuRole(grpMenuRole) >= 0);

    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setUserGrpDao(UserGrpDao userGrpDao) {
        this.userGrpDao = userGrpDao;
    }

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public void setGrpMenuRoleDao(GrpMenuRoleDao grpMenuRoleDao) {
        this.grpMenuRoleDao = grpMenuRoleDao;
    }
}
