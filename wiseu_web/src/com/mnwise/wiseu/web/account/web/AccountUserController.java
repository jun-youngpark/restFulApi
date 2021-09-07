package com.mnwise.wiseu.web.account.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.account.service.AccountService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.service.CdMstService;

/**
 * 사용자관리 Controller
 */
@Controller
public class AccountUserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AccountUserController.class);

    @Autowired private AccountService accountService;
    @Autowired private CdMstService cdMstService;

    private static final String CD_USERTYPE_CD = "C00061";

    @Value("${kakao.profile.master.id:admin}")
    private String kkoProfileMasterId;

    /**
     * [사용자>사용자 관리] 사용자 관리
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/accountUser.do", method={RequestMethod.GET, RequestMethod.POST})  // /account/account_user.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            UserVo uv = accountService.getUserInfo(getLoginId());
            returnData.put("UserVo", uv);
            returnData.put("smsIndividualCharge",smsIndividualCharge);
            return new ModelAndView("account/accountUser", returnData);  // account/account_user
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
    /**
     * [사용자>사용자 관리] 사용자 관리
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/userList.do", method={RequestMethod.GET, RequestMethod.POST})  // /account/account_user_list.do
    public ModelAndView userList(HttpServletRequest request) throws Exception {
        try {
            String grpCd = ServletRequestUtils.getStringParameter(request, "grpCd", Const.SUPER_GRP_CD);
            String nowPage = ServletRequestUtils.getStringParameter(request, "nowPage", "1");
            String serchID = ServletRequestUtils.getStringParameter(request, "serchID", "");

            UserVo userVo = getLoginUserVo();
            String userId = userVo.getUserId();
            String isMaker = userVo.getUserRole();

            UserVo uv = accountService.getUserInfo(userId);
            UserGrpVo ugv = accountService.selectGroupInfo(grpCd);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("UserVo", uv);
            returnData.put("UserGrpVo", ugv);

            String language = userVo.getLanguage();

            // 페이징 처리
            int rowCnt = accountService.getAccountTotalCount(grpCd, language, serchID); // TOTAL ROW 를 가져온다.
            int listCountPerPage = userVo.getListCountPerPage(); // 한페이지당 10개
            int nowPageInt = Integer.parseInt(nowPage) * userVo.getListCountPerPage(); // 현재페이지 * 한페이지당  10개
            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (rowCnt / aCountPerPage); // 몫
            int rCountPerPage = (rowCnt % aCountPerPage); // 나머지

            if(!grpCd.equals("01")) {
                if(Integer.parseInt(nowPage) <= qCountPerPage) {
                    returnData.put("userList", accountService.selectUserListPageing(grpCd, language, listCountPerPage, nowPageInt, serchID));
                } else {
                    returnData.put("userList", accountService.selectUserListPageing(grpCd, language, rCountPerPage, nowPageInt, serchID));
                }
            } else { // 부서 선택 전에는 페이징 버튼과 검색칸 안보여줌
                returnData.put("serchYn", "N");
            }

            /** Paging 및 검색을 위한 Parameter를 셋팅한다 */
            PagingUtil.transferPagingInfo(request, rowCnt);

            returnData.put("notRegList", accountService.selectNotRegList("C".equals(isMaker) ? null : userId, language));
            returnData.put("notDelList", accountService.selectNotDelList("C".equals(isMaker) ? null : userId, language));
            returnData.put("requestPermissionList", accountService.getRequestPermitUserList(language));
            returnData.put("grpCd", grpCd);
            returnData.put("isaRole", isMaker);
            returnData.put("countPerPage", listCountPerPage);
            returnData.put("nowPage", nowPage);

            return new ModelAndView("account/userList", returnData);  // account/accountUserList
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [사용자>사용자 관리>사용자 리스트] 잠금해제
     *
     * @param userId
     * @throws Exception
     */
    @RequestMapping(value="/account/resetLoginCnt.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto resetLoginCnt(String userId) {
        try {
            accountService.updateLoginCntReset(userId);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [사용자>사용자 관리] 사용자 등록/수정 (팝업)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/userPopup.do", method={RequestMethod.GET})  // /account/account_user_popup.do
    public ModelAndView list(String grpCd, String userId, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            String language = userVo.getLanguage();

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("UserGrpVo", accountService.selectGroupInfo(grpCd));
            returnData.put("userPermision", "off");

            List<PermissionVo> permissionList = null;
            int usePermission = 0;
            if(userId != null) {
                UserVo uv = accountService.getUserInfo(userId);
                returnData.put("userVo", uv);
                returnData.put("readonly", "true");

                // 사용자 권한이 존재하는 지 확인
                usePermission = accountService.selectUsePermission(userId);

                // 사용자 권한이 존재하면 사용자 권한을 가져오고 권한이 존재하지 않으면 부서권한을 가져온다. (디폴트로 보여주기 위해)
                if(usePermission > 0) {
                    permissionList = accountService.selectPermission(userId, language);
                } else {
                    permissionList = accountService.getGrpRoleList(grpCd, language);
                }

                returnData.put("userKakaoSenderKeyList", accountService.getUserKakaoSenderKeyList(userId));
            } else {
                returnData.put("readonly", "false");
                permissionList = accountService.getGrpRoleList(grpCd, language);
            }

            returnData.put("adminKakaoSenderKeyList", accountService.getUserKakaoSenderKeyList(this.kkoProfileMasterId));
            returnData.put("isaRole", userVo.getUserRole());
            returnData.put("permissionList", permissionList);
            returnData.put("userPermisionView", (usePermission > 0) ? "block" : "none");
            returnData.put("userPermision", (usePermission > 0) ? "on" : "off");
            returnData.put("webExecMode", super.webExecMode);
            // nv_cd_mst 에서 코드명을 가져오기 위해 language를 전달한다.
            returnData.put("userTypeCd", cdMstService.getCdMstList(CD_USERTYPE_CD, language));

            return new ModelAndView("account/userPopup", returnData);  // account/account_user_popup
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     *  [사용자>사용자 관리>사용자 등록(팝업)] ID 중복 체크
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/checkUserIdDuplication.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto checkUserIdDuplication(String userId) {
        try {
            int count = accountService.userChecker(userId);  // 등록된 사용자ID 개수 (0이면 미중복, 1이상이면 중복)

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(count + "");
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [사용자>사용자 관리>사용자 등록(팝업)] 저장 - 사용자 등록
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/insertUser.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertUser(UserVo user, String isaRole, String[] kakaoKey, String userPermision, HttpServletRequest request) {
        try {
            String makerId = getLoginId();

            if(StringUtil.isBlank(user.getPassWd())) {
                user.setPassWd("1111");
            }
            user.setCreateUser(makerId);

            if(isaRole.equals(Const.USER_ROLE_ADMIN)) {
                user.setAcceptYn("Y");
                user.setActiveYn("Y");
            } else {
                user.setAcceptYn("Y");
                user.setActiveYn("R");
            }

            // 플러스친구ID 권한추가
            if(kakaoKey != null) {
                for(String kakaoSenderKey : kakaoKey) {
                    accountService.insertUserKakaoProfile(user.getUserId(), kakaoSenderKey, this.kkoProfileMasterId);
                }
            }

            accountService.insertUserInfo(user, makerId, isaRole);

            // 사용자 권한 삭제
            accountService.deleteUserPermission(user.getUserId());

            if(userPermision.equals("on")) {
                Map<String, Object> menuRoleMap = accountService.getMenuRoleMap(request);

                if(menuRoleMap.size() > 0) {
                    accountService.insertUserPermission(user.getUserId(), menuRoleMap, user.getWorkDoc(), makerId);
                }
            }

            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [사용자>사용자 관리>사용자 수정(팝업)] 저장 - 사용자 사용자 정보 수정
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/updateUser.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateUser(UserVo user, String isaRole, String[] kakaoKey, String userPermision, String pwChg, HttpServletRequest request) {
        try {
            if(StringUtil.equals(pwChg, "true") == false) {  // 비밀번호가 변경되지 않은 경우
                user.setPassWd("");
            }

            // 기존 플러스친구ID 권한을 모두 삭제하고 새로 등록한다.
            accountService.deleteUserKakaoProfile(user.getUserId());

            // 플러스친구ID 권한추가
            if(kakaoKey != null) {
                for(String kakaoSenderKey : kakaoKey) {
                    accountService.insertUserKakaoProfile(user.getUserId(), kakaoSenderKey, this.kkoProfileMasterId);
                }
            }

            // 사용자 정보 수정
            accountService.updateUserInfo(user);

            if(userPermision.equals("on") && StringUtil.equals(isaRole, Const.USER_ROLE_ADMIN)) {
                // 사용자 권한 삭제
                accountService.deleteUserPermission(user.getUserId());

                Map<String, Object> menuRoleMap = accountService.getMenuRoleMap(request);
                accountService.insertUserPermission(user.getUserId(), menuRoleMap, user.getWorkDoc(), getLoginId());
            }

            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [사용자>사용자 관리>사용자 리스트] 삭제
     *
     * @param params
     * @throws Exception
     */
    @RequestMapping(value="/account/deleteUser.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteUser(String userId, String makerId, String workDoc, String userRole) {
        try {
            int row = accountService.updateUserDelete(userId, makerId, workDoc, userRole);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(row + "");
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
