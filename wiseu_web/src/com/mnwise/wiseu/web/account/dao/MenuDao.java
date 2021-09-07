package com.mnwise.wiseu.web.account.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MenuVo;

/**
 * NVMENU 테이블 DAO 클래스
 */
@Repository
public class MenuDao extends BaseDao {

    /**
     * Top 메뉴 목록 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MenuVo> getTopMenuList() {
        return selectList("Menu.getTopMenuList");
    }

    /**
     * Sub 메뉴 목록 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MenuVo> getSubMenuList(String language) {
        return selectList("Menu.getSubMenuList", language);
    }

    /**
     * 부서별 사용 가능 메뉴 목록 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MenuVo> getGrpMenuList(String grpCd) {
        return selectList("Menu.getGrpMenuList", grpCd);
    }

    public Map<String, Map<String, String>> selectMenuRole(String id) {
        return selectMap("Menu.selectRoleMap", id, "menuLinkUrl");
    }

    /**
     * 씨티은행 - 그룹별 권한 조회
     *
     * @param grpCd 그룹코드
     * @return
     */
    public Map<String, Map<String, String>> getGrpMenuRole(String grpCd) {
        return selectMap("Menu.getGrpMenuRoleMap", grpCd, "menuLinkUrl");
    }

    /**
     * 권한 정보를 가져온다.
     *
     * @param userId
     * @param language
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PermissionVo> selectPermission(String userId, String language) throws Exception {
        List<PermissionVo> resultList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("language", language);
        List<MenuVo> menuList = selectList("Menu.selectPermission", paramMap);
        for(MenuVo menu : menuList) {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtil.copyProperties(permissionVo, menu);
            resultList.add(permissionVo);
        }

        return resultList;
    }

    /**
     * 부서의 메뉴권한을 가져온다
     *
     * @param grpCd 부서코드
     * @param language 언어
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PermissionVo> getGrpRoleList(String grpCd, String language) throws Exception {
        List<PermissionVo> resultList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("grpCd", grpCd);
        map.put("language", language);
        List<MenuVo> menuList = selectList("Menu.getGrpRoleList", map);
        for(MenuVo menu : menuList) {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtil.copyProperties(permissionVo, menu);
            resultList.add(permissionVo);
        }

        return resultList;
    }

}