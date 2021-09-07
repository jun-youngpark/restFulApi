package com.mnwise.wiseu.web.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.account.model.json.TreeEelement;
import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVUSERGRP 테이블 DAO 클래스
 */
@Repository
public class UserGrpDao extends BaseDao {
    /**
     * 부서 저장 TODO 부서번호 채번을 위한 로직이 부서 변경등의 작업이 이루어졌을때 문제가 발생할 소지가 다분함. 동적으로 늘어나는 코드의 경우에는 '0101%' 식의 코드 사용이 이루어지면 안되고 시퀀스나 max + 1 을 사용해야 함.
     *
     * @param userGrp
     */
    public String insertUserGrp(UserGrpVo userGrp) {
        int row = insert("UserGrp.insertUserGrp", userGrp);
        return row == 1 ? userGrp.getGrpCd() : null;
    }

    /**
     * 부서 정보 수정
     *
     * @param userGrpVo
     * @return
     */
    public int updateAccountDept(UserGrpVo userGrpVo) {
        return update("UserGrp.updateAccountDept", userGrpVo);
    }

    /**
     * 부서 정보 삭제
     *
     * @param grpCd
     * @param acceptYn
     * @param activeYn
     * @param userRole
     * @return
     */
    public int updateAccountDeptAcceptYn(String grpCd, String acceptYn, String activeYn, String userRole) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("grpCd", grpCd);
        paramMap.put("acceptYn", acceptYn);
        paramMap.put("activeYn", activeYn);
        paramMap.put("userRole", userRole);

        return update("UserGrp.updateAccountDeptAcceptYn", paramMap);
    }

    /**
     * 권한 변경 요청시 정보 업데이트
     *
     * @param paramMap
     * @return
     */
    public int updateGrpPermissionInfo(Map<String, Object> paramMap) {
        return update("UserGrp.updateGrpPermissionInfo", paramMap);
    }

    /**
     * 부서권한 변경 요청시 관련근구 문서 입력
     *
     * @param userGrpVo
     * @return
     */
    public int updateAccountDeptWorkDoc(UserGrpVo userGrpVo) {
        return update("UserGrp.updateAccountDeptWorkDoc", userGrpVo);
    }

    /**
     * 그룹정보 조회
     *
     * @param grpCd
     * @return
     */
    public UserGrpVo selectGroupInfo(String grpCd) {
        return (UserGrpVo) selectOne("UserGrp.selectGroupInfo", grpCd);
    }

    /**
     * 상위 부서의 정보를 가져온다
     *
     * @param supraGrpCd 상위 부서 코드
     * @return
     */
    public UserGrpVo getSupraInfo(String supraGrpCd) {
        return (UserGrpVo) selectOne("UserGrp.getSupraInfo", supraGrpCd);
    }

    /**
     * 부서 리스트를 가져온다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserGrpVo> getGrpList() {
        return selectList("UserGrp.getGrpList");
    }

    /**
     * 권한 변경 요청한 부서 리스트를 가져온다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserGrpVo> getRequestPermitGrpList() {
        return selectList("UserGrp.getRequestPermitGrpList");
    }

    /**
     * 그룹 미등록 리스트
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserGrpVo> getNotRegGrpList() {
        List<UserGrpVo> tmp = (List<UserGrpVo>) selectList("UserGrp.selectNotRegList");
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 그룹 미삭제 리스트
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserGrpVo> getNotDelGrpList() {
        List<UserGrpVo> tmp = (List<UserGrpVo>) selectList("UserGrp.selectNotDelList");
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    @SuppressWarnings("unchecked")
    public List<TreeEelement> selectGroupTree(TreeEelement treeEelement) {
        List<TreeEelement> tmp = selectList("UserGrp.selectGroupTree", treeEelement);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

}