package com.mnwise.wiseu.web.account.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.account.dao.GrpMenuRoleDao;
import com.mnwise.wiseu.web.account.dao.MenuDao;
import com.mnwise.wiseu.web.account.dao.MenuRoleDao;
import com.mnwise.wiseu.web.account.dao.UserDao;
import com.mnwise.wiseu.web.account.dao.UserGrpDao;
import com.mnwise.wiseu.web.account.dao.UserPwHistoryDao;
import com.mnwise.wiseu.web.account.model.GrpMenuRole;
import com.mnwise.wiseu.web.account.model.MenuRole;
import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.account.model.UserPwVo;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.account.model.json.TreeEelement;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.util.xCryptionSha;
import com.mnwise.wiseu.web.template.dao.KakaoProfileDao;

/**
 * 사용자 관리 서비스
 */
@Service
public class AccountService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired private GrpMenuRoleDao grpMenuRoleDao;
    @Autowired private KakaoProfileDao kakaoProfileDao;
    @Autowired private MenuDao menuDao;
    @Autowired private MenuRoleDao menuRoleDao;
    @Autowired private UserDao userDao;
    @Autowired private UserGrpDao userGrpDao;
    @Autowired private UserPwHistoryDao userPwHistoryDao;

    private static final String READ_AUTH_CD = "R"; // 읽기
    private static final String WRITE_AUTH_CD = "W"; // 쓰기
    private static final String EXCUTE_AUTH_CD = "X"; // 승인
    //private static final String HOLD_PERMISSION = "H";

    @Value("${sms.individual.charge}")
    private String smsIndividualCharge;

    public List<TreeEelement> selectGroup(TreeEelement treeEelement) throws Exception {
        return userGrpDao.selectGroupTree(treeEelement);
    }

    public int userChecker(String userId) throws Exception {
        return userDao.userChecker(userId);
    }

    /**
     * 권한 정보를 가져온다.
     *
     * @param userId
     * @param language
     * @return
     */
    public List<PermissionVo> selectPermission(String userId, String language) throws Exception {
        return menuDao.selectPermission(userId, language);
    }

    /**
     * 그룹정보 조회
     *
     * @param grpCd
     * @return
     * @throws Exception
     */
    public UserGrpVo selectGroupInfo(String grpCd) throws Exception {
        UserGrpVo userGrpVo = new UserGrpVo();
        userGrpVo = userGrpDao.selectGroupInfo(grpCd);
        userGrpVo.setSmsIndividualCharge(smsIndividualCharge);
        return userGrpVo;
    }

    /**
     * 미등록 사용자 리스트 조회
     *
     * @param grpCd
     * @param language
     * @return
     * @throws Exception
     */
    public List<UserVo> selectNotRegList(String userId, String language) throws Exception {
        return userDao.selectNotRegList(userId, language);
    }

    /**
     * 미삭제 사용자 리스트 조회
     *
     * @param grpCd
     * @param language
     * @return
     * @throws Exception
     */
    public List<UserVo> selectNotDelList(String userId, String language) throws Exception {
        return userDao.selectNotDelList(userId, language);
    }

    /**
     * 부서 정보를 가져온다.
     *
     * @param grpCd 부서코드
     * @return
     * @throws Exception
     */
    public UserGrpVo getUserGrpInfo(String grpCd) throws Exception {
        UserGrpVo userGrpVo = new UserGrpVo();
        userGrpVo = userGrpDao.selectGroupInfo(grpCd);
        userGrpVo.setSmsIndividualCharge(smsIndividualCharge);
        return userGrpVo;
    }

    /**
     * 부서의 메뉴권한을 가져온다
     *
     * @param grpCd 부서코드
     * @param language 언어
     * @return
     * @throws Exception
     */
    public List<PermissionVo> getGrpRoleList(String grpCd, String language) throws Exception {
        return menuDao.getGrpRoleList(grpCd, language);
    }

    /**
     * 상위 부서의 정보를 가져온다
     *
     * @param supraGrpCd 상위 부서 코드
     * @return
     * @throws Exception
     */
    public UserGrpVo getSupraInfo(String supraGrpCd) throws Exception {
        UserGrpVo userGrpVo = new UserGrpVo();
        userGrpVo = userGrpDao.getSupraInfo(supraGrpCd);
        userGrpVo.setSmsIndividualCharge(smsIndividualCharge);
        return userGrpVo;
    }

    /**
     * 부서 저장
     *
     * @param userGrpVo
     * @param userId
     * @param userRole 유저 역활
     * @return
     * @throws Exception
     */
    public String insertAccountDept(UserGrpVo userGrpVo, String userId, String userRole) throws Exception {
        // 유저 타입이 부서 생성 권한을 가진 Maker 유저가 아닌 Admin 인 A 일 경우 승인 로직을 거치지 않고 바로 부서를
        // 생성한다.
        if(userRole.equals(Const.USER_ROLE_ADMIN)) {
            userGrpVo.setActiveYn("Y");
            userGrpVo.setAcceptYn("Y");
        } else {
            userGrpVo.setActiveYn("R");
            userGrpVo.setAcceptYn("Y");
        }

        // 부서 저장
        return userGrpDao.insertUserGrp(userGrpVo);
    }

    /**
     * 부서 정보 수정
     *
     * @param userGrpVo
     * @return
     * @throws Exception
     */
    public int updateAccountDept(UserGrpVo userGrpVo) throws Exception {
        return userGrpDao.updateAccountDept(userGrpVo);
    }

    /**
     * 부서 권한 변경 요청시 관련근거문서 입력
     *
     * @param userGrpVo
     * @return
     * @throws Exception
     */
    public int updateAccountDeptWorkDoc(UserGrpVo userGrpVo) throws Exception {
        return userGrpDao.updateAccountDeptWorkDoc(userGrpVo);
    }

    /**
     * 부서 정보 삭제 (Active_yn 만 N으로 처리한다.)
     *
     * @param grpCd
     * @param userId
     * @param workDoc 작업근거문서
     * @param userRole 유저 권한
     * @return
     * @throws Exception
     */
    public int updateAccountDeptAcceptYn(String grpCd, String userId, String workDoc, String userRole) throws Exception {
        String acceptYn = "N";
        String activeYn = "Y";

        // 유저 타입이 부서 생성 권한을 가진 Maker 유저가 아닌 Admin 인 A 일 경우 승인 로직을 거치지 않고 바로 부서를
        // 삭제한다.
        if(userRole.equals(Const.USER_ROLE_ADMIN)) {
            activeYn = "N";
        }

        // 부서 삭제
        int result = userGrpDao.updateAccountDeptAcceptYn(grpCd, acceptYn, activeYn, userRole);
        return result;
    }

    /**
     * 부서 리스트를 가져온다.
     *
     * @return
     */
    public List<UserGrpVo> getGrpList() {
        return userGrpDao.getGrpList();
    }

    /**
     * 해당 사용자 ID를 삭제 리스트 대상자로 분류 한다.
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public int updateUserDelete(String userId, String makerId, String workDoc, String userRole) throws Exception {
        String[] saTemp = userId.split(",");
        int result = 0;

        UserVo uv = new UserVo();
        uv.setSaUserId(saTemp);

        if(userRole.equals("A")) {
            result = userDao.updateUserErase(uv);
        } else {
            result = userDao.updateUserDelete(uv);
        }

        // 기존 플러스친구ID 권한을 모두 삭제한다.
        kakaoProfileDao.deleteUserKakaoProfile(userId);

        return result;
    }

    /**
     * 사용자 정보를 가져온다.
     *
     * @param userId
     * @return
     */
    public UserVo getUserInfo(String userId) {
        return userDao.selectUserByPk(userId);
    }

    /**
     * userRole 이 A 인 관리자 계정일 경우 부서별 권한 리스트를 저장한다.
     *
     * @param grpCd
     * @param userId
     * @param map
     * @param workDoc
     * @return
     * @throws Exception
     */
    public int insertAdminGrpPermission(String grpCd, String userId, Map<String, Object> map, String workDoc) throws Exception {
        // 부서의 권한을 삭제 및 Insert 한다.
        setGrpPermission(grpCd, map);
        return 1;
    }

    /**
     * 부서별 권한을 Delete / Insert 한다 - 유저 권한과 관련하여 로그를 위해 분리한다.
     *
     * @param grpCd
     * @param map
     * @return
     * @throws Exception
     */
    public int setGrpPermission(String grpCd, Map<String, Object> map) throws Exception {
        // 기존에 설정된 부서의 권한들을 삭제한다.
        grpMenuRoleDao.deleteGrpPermission(grpCd);

        String[] menuCds = (String[]) map.get("menuCd");

        // 각 메뉴별로 loop
        for(String menuCd : menuCds) {
            GrpMenuRole grpMenuRole = new GrpMenuRole();
            grpMenuRole.setGrpCd(grpCd);
            grpMenuRole.setMenuCd(menuCd);

            // 메뉴에 선택한 권한들의 값을 가져온다.
            String[] auths = (String[]) map.get(menuCd);

            // 해당 메뉴에 선택값이 있을 경우 각 Vo 에 권한 값을 넣는다.
            if(auths != null && auths.length > 0) {
                for(String auth : auths) {
                    switch(auth) {
                        case READ_AUTH_CD :
                            grpMenuRole.setReadAuth(auth);
                            break;
                        case WRITE_AUTH_CD :
                            grpMenuRole.setWriteAuth(auth);
                            break;
                        case EXCUTE_AUTH_CD :
                            grpMenuRole.setExecuteAuth(auth);
                            break;
                        default :
                            break;
                    }
                }
                // 부서의 권한을 insert 한다.
                grpMenuRoleDao.insertGrpMenuRole(grpMenuRole);
            }
        }

        return 1;
    }

    /**
     * HTTP 요청객체로부터 요청된 메뉴별 권한 설정값을 메뉴권한맵으로 리턴한다.
     *
     * @param request HTTP 요청객체
     * @return 메뉴권한맵<메뉴코드,권한>
     */
    public Map<String, Object> getMenuRoleMap(HttpServletRequest request) {
        Enumeration<String> en = request.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        while(en.hasMoreElements()) {
            String param = en.nextElement();
            // 캠페인, 이케어 리포트 중분류 코드는 소분류 등록시 기본적으로 넣어줌. 이 코드가 없으면 좌측 메뉴에서 중분류가 빠짐
            if(param.startsWith("0301") || param.startsWith("0302") || param.startsWith("0305")) {
                map.put(param.substring(0, 4), new String[] {"R"});
            }

            // 캠페인/이케어 '실행/발송' 권한이 있을 경우 캠페인/이케어 리스트 보기 권한이 자동으로 부여됨
            // 실행/발송권한이 있을 경우 쓰기권한이 없어짐 버그 수정
            if(param.equals("0103")) {
                List<String> paramValueX = new ArrayList<>();
                Collections.addAll(paramValueX, request.getParameterValues("0103"));
                if(paramValueX.contains("X") && request.getParameterValues("0102") == null) {
                    map.put("0102", new String[] {"R"});
                }
            }

            if(param.equals("0202")) {
                List<String> paramValueX = new ArrayList<>();
                Collections.addAll(paramValueX, request.getParameterValues("0202"));
                if(paramValueX.contains("X") && request.getParameterValues("0201") == null) {
                    map.put("0201", new String[] {"R"});
                }
            }

            map.put(param, request.getParameterValues(param));
        }

        return map;
    }

    /**
     * 사용자 권한 설정
     *
     * @param userId
     * @param map
     * @param workDoc
     * @param makerId
     * @return
     * @throws Exception
     */
    public int insertUserPermission(String userId, Map<String, Object> map, String workDoc, String makerId) throws Exception {
        String[] menuCd = (String[]) map.get("menuCd");

        if(menuCd == null) {
            return 0;
        }
        // 각 메뉴별로 loop
        for(int i = 0; i < menuCd.length; i++) {
            MenuRole menuRole = new MenuRole();
            menuRole.setUserId(userId);
            menuRole.setMenuCd(menuCd[i]);

            // 메뉴에 선택한 권한들의 값을 가져온다.
            String[] auth = (String[]) map.get(menuCd[i]);

            // 해당 메뉴에 선택값이 있을 경우 각 Vo 에 권한 값을 넣는다.
            if(auth != null && auth.length > 0) {
                for(int j = 0; j < auth.length; j++) {
                    if(auth[j].equals(READ_AUTH_CD)) {
                        menuRole.setReadAuth(auth[j]);
                    } else if(auth[j].equals(WRITE_AUTH_CD)) {
                        menuRole.setWriteAuth(auth[j]);
                    } else if(auth[j].equals(EXCUTE_AUTH_CD)) {
                        menuRole.setExecuteAuth(auth[j]);
                    } else {
                    }
                }
                // 부서의 권한을 insert 한다.
                menuRoleDao.insertMenuRole(menuRole);
            }
        }

        return 1;
    }

    /**
     * 권한 변경 요청한 부서 리스트를 가져온다.
     *
     * @return
     */
    public List<UserGrpVo> getRequestPermitGrpList() {
        return userGrpDao.getRequestPermitGrpList();
    }

    /**
     * 권한 변경 요청한 사용자 리스트를 가져온다.
     *
     * @param language
     * @return
     * @throws Exception
     */
    public List<UserVo> getRequestPermitUserList(String language) throws Exception {
        return userDao.getRequestPermitUserList(language);
    }

    /**
     * 해당 사용자의 메뉴 사용권한을 모두 삭제.
     *
     * @param userId
     * @return
     */
    public void deleteUserPermission(String userId) {
        PermissionVo pv = new PermissionVo();
        pv.setUserId(userId);
        menuRoleDao.deletePermission(pv.getUserId());
    }

    /**
     * 사용자별 메뉴 권한이 있는지 확인
     *
     * @param userId
     * @return
     */
    public int selectUsePermission(String userId) {
        return menuRoleDao.selectUsePermission(userId);
    }

    /**
     * 사용자 생성
     *
     * @param uv
     * @throws Exception
     */
    public void insertUserInfo(UserVo uv, String makerId, String isaRole) throws Exception {
        if(super.passwordEncUse.equalsIgnoreCase("on")) {
            try {
                String salt = xCryptionSha.getSalt();
                String pwd = uv.getPassWd();
                uv.setPassWd(xCryptionSha.encryptWithSalt_sha256(pwd, salt));
                uv.setPassSalt(salt);
            } catch(NoSuchAlgorithmException e) {
                log.error(null, e);
            }
        } else {
            uv.setPassSalt("");
        }
        uv.setDefaultPassYn("Y");
        userPwHistoryDao.insertUserPwHistory(uv);
        userDao.insertUser(uv);
    }

    /**
     * 사용자 정보 수정
     *
     * @param uv
     * @throws Exception
     */
    public void updateUserInfo(UserVo uv) throws Exception {
        if(super.passwordEncUse.equalsIgnoreCase("on") && !uv.getPassWd().equals("")) {
            try {
                String salt = xCryptionSha.getSalt();
                String pwd = uv.getPassWd();
                uv.setPassWd(xCryptionSha.encryptWithSalt_sha256(pwd, salt));
                uv.setPassSalt(salt);
            } catch(NoSuchAlgorithmException e) {
                log.error(null, e);
            }
        } else {
            uv.setPassSalt("");
        }
        uv.setDefaultPassYn("Y");
        if(!uv.getPassWd().equals(""))
            userPwHistoryDao.insertUserPwHistory(uv);
        userDao.updateUserInfo(uv);
    }

    /**
     * 부서 권한 변경 요청시에 관련 테이블에 정보 업데이트
     *
     * @param grpCd
     * @param permitSts
     * @param permitDt
     * @throws Exception
     */
    public void updateGrpPermissionInfo(String grpCd, String permitSts, String permitDt) throws Exception {
        String[] saTemp = grpCd.split(",");
        for(int i = 0, n = saTemp.length; i < n; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("grpCd", saTemp[i]);
            map.put("permitSts", (permitSts == null || permitSts.equals("")) ? null : permitSts);
            map.put("permitDt", (permitDt == null || permitDt.equals("")) ? null : permitDt);

            userGrpDao.updateGrpPermissionInfo(map);
        }
    }

    /**
     * 그룹 미등록 리스트
     *
     * @return
     * @throws Exception
     */
    public List<UserGrpVo> getNotRegGrpList() throws Exception {
        return userGrpDao.getNotRegGrpList();
    }

    /**
     * 그룹 미삭제 리스트
     *
     * @return
     * @throws Exception
     */
    public List<UserGrpVo> getNotDelGrpList() throws Exception {
        return userGrpDao.getNotDelGrpList();
    }

    // 페이징 처리
    public int getAccountTotalCount(String grpCd, String language, String serchID) throws Exception {
        return userDao.getAccountTotalCount(grpCd, language, serchID);
    }

    public List<UserVo> selectUserListPageing(String grpCd, String language, int listCountPerPage, int nowPage, String serchID) throws Exception {
        return userDao.selectUserListPageing(grpCd, language, listCountPerPage, nowPage, serchID);
    }

    // 3개월안에 사용한 비밀번호는 다시 사용 못함
    public int checkShaPw(String checkId, String passWd1) throws Exception {
        int checkCount = 0;
        List<UserPwVo> pwHistoryList = userPwHistoryDao.getCheckShaPwHistory(checkId);
        // 검증이 되면 예전에 사용한적이 있는 비밀번호,검증이 안되면 사용한적이 없는 비밀번호, 검증되면 카운트를 증가 시킴
        for(int i = 0; i < pwHistoryList.size(); i++) {
            UserPwVo upv = pwHistoryList.get(i);
            if(super.passwordEncUse.equalsIgnoreCase("on")) {
                if(xCryptionSha.confirmWithSalt_sha256(passWd1, upv.getPassSalt(), upv.getPassWd())) {
                    checkCount++;
                }
            } else {
                if(passWd1.equals(upv.getPassWd())) {
                    checkCount++;
                }
            }
        }
        return checkCount;
    }

    public void updateLoginCntReset(String userId) throws Exception {
        userDao.updateLoginCntReset(userId);
    }

    public List<CaseInsensitiveMap> getUserKakaoSenderKeyList(String userId) {
        return kakaoProfileDao.getUserKakaoSenderKeyList(userId);
    }

    public void insertUserKakaoProfile(String userId, String kakaoSenderKey, String masterId) {
        kakaoProfileDao.insertUserKakaoProfile(userId, kakaoSenderKey, masterId);
    }

    public void deleteUserKakaoProfile(String userId) {
        kakaoProfileDao.deleteUserKakaoProfile(userId);
    }
}
