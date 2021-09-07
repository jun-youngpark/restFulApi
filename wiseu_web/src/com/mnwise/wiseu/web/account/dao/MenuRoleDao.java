package com.mnwise.wiseu.web.account.dao;

import com.mnwise.wiseu.web.account.model.MenuRole;
import com.mnwise.wiseu.web.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVMENUROLE 테이블 DAO 클래스
 */
@Repository
public class MenuRoleDao extends BaseDao {
    /**
     * 사용자 권한 DB저장
     *
     * @param menuRole
     * @return
     */
    public int insertMenuRole(MenuRole menuRole) {
        return insert("MenuRole.insertMenuRole", menuRole);
    }

    public int deletePermission(String userId) {
        return delete("MenuRole.deletePermission", userId);
    }

    /**
     * 사용자 권한설정 확인
     *
     * @param userId
     * @return
     */
    public int selectUsePermission(String userId) {
        Integer count = (Integer) selectOne("MenuRole.selectUsePermission", userId);
        return (count == null) ? 0 : count;
    }

    /**
     * 사용자 사용 가능 메뉴 목록 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MenuRole> getUserMenuList(String userId) {
        return selectList("MenuRole.getUserMenuList", userId);
    }
}