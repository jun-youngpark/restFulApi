package com.mnwise.wiseu.web.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.wiseu.web.account.dao.MenuDao;
import com.mnwise.wiseu.web.account.dao.MenuLangDao;
import com.mnwise.wiseu.web.account.dao.MenuRoleDao;
import com.mnwise.wiseu.web.account.dao.UserDao;
import com.mnwise.wiseu.web.account.model.MenuRole;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.xCryptionSha;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.common.model.MenuVo;
import com.mnwise.wiseu.web.common.model.Tag;

/**
 * 사용자 로그인 서비스
 */
@Service
public class AdminService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired private MenuDao menuDao;
    @Autowired private MenuLangDao menuLangDao;
    @Autowired private MenuRoleDao menuRoleDao;
    @Autowired private TagDao tagDao;
    @Autowired private UserDao userDao;

    @Value("${env.product.use}")
    private String envProductUse;

    public UserVo getAdminByAdminId(String adminId, String adminPwd) throws Exception {
        log.debug("envProductUse : " + envProductUse);
        UserVo userVo = userDao.selectAdminByAdminId(adminId);
        if(super.passwordEncUse.equalsIgnoreCase("on") && userVo != null) {
            String passWd = userVo.getPassWd();
            String passSalt = userVo.getPassSalt();
            if(xCryptionSha.confirmWithSalt_sha256(adminPwd, passSalt, passWd)) {
                log.debug("<<< Login :: Authenticate Success2");
                userVo.setPassWd(adminPwd);
            } else {
                log.debug("<<< Login :: Authenticate Fail2");
                userVo.setPassWd("");
            }
        }
        return userVo;
    }

    /**
     * 씨티은행 - SOEID 확인
     *
     * @param soeid SOEID
     * @return
     * @throws Exception
     */
    public UserVo getUserInfoBySoeid(String soeid) throws Exception {
        return userDao.getUserInfoBySoeid(soeid);
    }

    /**
     * 씨티은행 - 사용자/부서 권한 조회 사용자 권한을 조회한 후에 별도의 설정된 권한이 없다면 부서별 권한을 조회하여 온다.
     *
     * @param userVo
     * @return
     * @throws Exception
     */
    public Map<String, Map<String, String>> getMenuRole(UserVo userVo) throws Exception {
        Map<String, Map<String, String>> map = menuDao.selectMenuRole(userVo.getUserId());

        if(map.size() == 0) {
            map = menuDao.getGrpMenuRole(userVo.getGrpCd());
        }

        return map;
    }

    /**
     * Sub 메뉴 목록 조회
     *
     * @return
     * @throws Exception
     */
    public List<MenuVo> getSubMenuList(String language) throws Exception {
        return menuDao.getSubMenuList(language);
    }

    /**
     * 현재 사용중인 언어 조회
     *
     * @return
     * @throws Exception
     */
    public List<String> getLangList() throws Exception {
        return menuLangDao.getLangList();
    }

    /**
     * Top 메뉴 목록 조회
     *
     * @return
     * @throws Exception
     */
    public List<MenuVo> getTopMenuList() throws Exception {
        return menuDao.getTopMenuList();
    }

    /**
     * 사용자 사용 가능 메뉴 목록 조회
     *
     * @return
     * @throws Exception
     */
    public List<MenuVo> getUserMenuList(String userId, String grpCd) throws Exception {
        List<MenuVo> menuList = new ArrayList<>();
        List<MenuRole> menuRoleList = menuRoleDao.getUserMenuList(userId);
        for(MenuRole menuRole : menuRoleList) {
            MenuVo menuVo = new MenuVo();
            BeanUtil.copyProperties(menuVo, menuRole);
            menuList.add(menuVo);
        }

        if(menuList.size() == 0) {
            menuList = menuDao.getGrpMenuList(grpCd);
        }

        return menuList;
    }

    /**
     * 태그 리스트 가져오기
     *
     * @param onMenu 각 module_nm
     * @param userVo
     * @return
     */
    public List<Tag> getTagList(String onMenu, UserVo userVo) throws Exception {
        return tagDao.getTagList(onMenu, userVo);
    }

    public void updateLoginFailCount(String userId) throws Exception {
        userDao.updateLoginFailCount(userId);
    }

    public void updateLoginFailCountToInitial(String userId) throws Exception {
        userDao.updateLoginFailCountToInitial(userId);
    }
}