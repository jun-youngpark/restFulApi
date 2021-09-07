package com.mnwise.wiseu.web.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.env.model.EnvMyInfoVo;

/**
 * NVUSER 테이블 DAO 클래스
 */
@Repository
public class UserDao extends BaseDao {
    public int insertUser(UserVo user) {
        return insert("User.insertUser", user);
    }

    public int updateUserByPk(UserVo user) {
        return update("User.updateUserByPk", user);
    }

    /**
     * 사용자 정보 수정
     *
     * @param user
     */
    public int updateUserInfo(UserVo user) {
        security.securityObject(user, "ENCRYPT");
        return update("User.updateUserInfo", user);
    }

    public int updateLoginCntReset(String userId) {
        // 비밀번호 수정 할 경우 비밀번호 이력관리 테이블에도 인서트 해준다. default_passyn = 'N'
        return update("User.updateLoginCntReset", userId);
    }

    /**
     * 해당 사용자 ID를 삭제 리스트 대상자로 분류 한다.
     *
     * @param uv
     * @return
     */
    public int updateUserDelete(UserVo uv) {
        return update("User.updateUserDelete", uv);
    }

    /**
     * 삭제 리스트 대상자에서 삭제한다. 실제 DB에 Flag 값 업데이트.
     *
     * @param uv
     * @return
     */
    public int updateUserErase(UserVo uv) {
        return update("User.updateUserErase", uv);
    }

    /**
     * 환경설정 - 개인정보 관리에서 개인정보를 변경한다.
     *
     * @param envMyInfoVo
     */
    public int updateEnvMyInfo(EnvMyInfoVo envMyInfoVo) {
        security.securityObject(envMyInfoVo, "ENCRYPT");
        return update("User.updateEnvMyInfo", envMyInfoVo);
    }

    public int updateLoginFailCountToInitial(String userId) {
        return update("User.updateLoginFailCountToInitial", userId);
    }

    public int updateLoginFailCount(String userId) {
        return update("User.updateLoginFailCount", userId);
    }

    public UserVo selectUserByPk(String userId) {
        UserVo user = (UserVo) selectOne("User.selectUserByPk", userId);
        security.securityObject(user, "DECRYPT");
        return user;
    }

    /**
     * 사용자 아이디 조회
     *
     * @param userId 사용자 아이디
     * @return
     */
    public UserVo selectAdminByAdminId(String userId) {
        UserVo user = (UserVo) selectOne("User.selectAdminByAdminId", userId);
        security.securityObject(user, "DECRYPT");
        return user;
    }

    public int userChecker(String userId) {
        Integer count = (Integer) selectOne("User.userChecker", userId);
        return (count == null) ? 0 : count;
    }

    /**
     * 미등록 사용자 리스트 조회
     *
     * @param userId
     * @param language
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserVo> selectNotRegList(String userId, String language) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("language", language);

        List<UserVo> userList = (List<UserVo>) selectList("User.selectNotRegList", paramMap);
        security.securityObjectList(userList, "DECRYPT");
        return userList;
    }

    /**
     * 미삭제 사용자 리스트 조회
     *
     * @param userId
     * @param language
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserVo> selectNotDelList(String userId, String language) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("language", language);

        List<UserVo> userList = (List<UserVo>) selectList("User.selectNotDelList", paramMap);
        security.securityObjectList(userList, "DECRYPT");
        return userList;
    }

    /**
     * 권한 변경 요청한 사용자 리스트를 가져온다.
     *
     * @param language
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserVo> getRequestPermitUserList(String language) {
        List<UserVo> userList = (List<UserVo>) selectList("User.getRequestPermitUserList", language);
        security.securityObjectList(userList, "DECRYPT");
        return userList;
    }

    // 페이징 처리
    public int getAccountTotalCount(String grpCd, String language, String serchID) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("grpCd", grpCd);
        paramMap.put("language", language);
        paramMap.put("serchID", serchID);
        Integer count = (Integer) selectOne("User.getAccountTotalCount", paramMap);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<UserVo> selectUserListPageing(String grpCd, String language, int listCountPerPage, int nowPage, String serchID) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("grpCd", grpCd);
        paramMap.put("language", language);
        paramMap.put("listCountPerPage", (listCountPerPage == 0) ? 1 : listCountPerPage);
        paramMap.put("nowPage", nowPage);
        paramMap.put("serchID", serchID);

        List<UserVo> userList = (List<UserVo>) selectList("User.selectUserListPageing", paramMap);
        security.securityObjectList(userList, "DECRYPT");
        return userList;
    }

    /**
     * 씨티은행 - SOEID 확인
     *
     * @param soeid SOEID
     * @return
     */
    public UserVo getUserInfoBySoeid(String soeid) {
        return (UserVo) selectOne("User.getUserInfoBySoeid", soeid);
    }

    /**
     * 환경설정 - 개인정보 관리에서 개인정보를 가져온다.
     *
     * @param userId
     * @return
     */
    public UserVo selectEnvMyInfo(String userId) {
        UserVo user = (UserVo) selectOne("User.selectEnvMyInfo", userId);
        security.securityObject(user, "DECRYPT");
        return user;
    }
}