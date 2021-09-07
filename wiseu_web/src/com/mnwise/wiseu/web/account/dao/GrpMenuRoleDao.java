package com.mnwise.wiseu.web.account.dao;

import com.mnwise.wiseu.web.account.model.GrpMenuRole;
import com.mnwise.wiseu.web.base.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * NVGRPMENUROLE 테이블 DAO 클래스
 */
@Repository
public class GrpMenuRoleDao extends BaseDao {
    /**
     * 부서의 권한을 저장한다.
     *
     * @param grpMenuRole
     * @return
     */
    public int insertGrpMenuRole(GrpMenuRole grpMenuRole) {
        return insert("GrpMenuRole.insertGrpMenuRole", grpMenuRole);
    }

    /**
     * 부서의 권한 정보를 모두 삭제한다
     *
     * @param grpCd
     * @return
     */
    public int deleteGrpPermission(String grpCd) {
        return delete("GrpMenuRole.deleteGrpPermission", grpCd);
    }

}