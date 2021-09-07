package com.mnwise.wiseu.web.admin.dao;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mnwise.wiseu.web.account.dao.MenuDao;
import com.mnwise.wiseu.web.account.dao.UserDao;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.common.model.MenuVo;
import com.mnwise.wiseu.web.test.BaseDaoTestCase;

/**
 * 관리자 메뉴 Dao Test
 */
public class AdminDaoTest extends BaseDaoTestCase {
    private UserDao userDao;
    private MenuDao menuDao;

    protected String[] getConfigLocations() {
        return new String[] {
            "file:**/applicationContext-test.xml"
            // ,"file:**/conf/**/**-applicationContext.xml"};
            , "file:**/conf/admin/admin-applicationContext.xml"
        };
    }

    public AdminDaoTest() {
    }

//    public AdminDaoTest(String runTest) {
//        super(runTest);
//    }

    // -------------------------------------------------------------------------
    // unit test
    // -------------------------------------------------------------------------
//    public static Test suite() {
//        TestSuite suite = new TestSuite();
//        suite.addTest(new AdminDaoTest("testGetUserInfoBySoeid"));
//        suite.addTest(new AdminDaoTest("testGetGrpMenuRole"));
//        suite.addTest(new AdminDaoTest("testGetTopMenuList"));
//        suite.addTest(new AdminDaoTest("testGetSubMenuList"));
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

        UserVo userVo = userDao.getUserInfoBySoeid(soeid);
        assertNotNull(userVo);
    }

    /**
     * 그룹별 권한 조회
     *
     * @throws Exception
     */
    @Test
    public void testGetGrpMenuRole() throws Exception {
        String grpCd = "0101";
        Map map = menuDao.getGrpMenuRole(grpCd);
    }

    /**
     * Top 메뉴 조회
     *
     * @throws Exception
     */
    @Test
    public void testGetTopMenuList() throws Exception {
        List<MenuVo> list = menuDao.getTopMenuList();
        assertNotNull(list);
    }

    /**
     * Sub 메뉴 조회
     *
     * @throws Exception
     */
    @Test
    public void testGetSubMenuList() throws Exception {
        List<MenuVo> list = menuDao.getSubMenuList("ko");
        assertNotNull(list);
    }

    // -------------------------------------------------------------------------
    // Setter methods for dependency injection
    // -------------------------------------------------------------------------
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

}
