package com.mnwise.wiseu.web.account.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.account.service.AccountService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;

/**
 * 부서 권한 관리 Controller
 */
@Controller
public class AccountDeptPermissionController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AccountDeptPermissionController.class);

    @Autowired private AccountService accountService;

    /**
     * [사용자 관리>부서 권한 관리] 부서 권한 메인 페이지
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/accountDeptRole.do", method={RequestMethod.GET, RequestMethod.POST})  // /account/account_dept_permission.do
    public ModelAndView list(@RequestParam(defaultValue="") String grpCd, HttpServletRequest request) throws Exception {
        try {

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("smsIndividualCharge",smsIndividualCharge);
            return new ModelAndView("account/accountDeptRole", returnData);  // account/account_dept_permission
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [사용자 관리>부서 권한 관리] 부서 권한 관리 - 권한 관리 페이지
     *
     * @param grpCd 부서코드
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/deptRoleList.do", method={RequestMethod.GET, RequestMethod.POST})  // /account/account_dept_permission_list.do
    public ModelAndView deptPermissionList(@RequestParam(defaultValue="") String grpCd, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            UserVo uv = accountService.getUserInfo(userVo.getUserId());
            // 부서 정보를 가져온다. (부서코드가 없으면 최상위 부서 정보를 가져온다.)
            UserGrpVo userGrpVo = accountService.getUserGrpInfo(grpCd);
            List<PermissionVo> permissionList = null;

            // 선택된 부서 코드가 존재하고, 선택한 부서코드가 최상위 부서가 아닐경우 권한 정보를 가져온다.
            if(StringUtil.isNotBlank(grpCd) && !grpCd.equals(Const.SUPER_GRP_CD)) {
                permissionList = accountService.getGrpRoleList(grpCd, userVo.getLanguage());  // 다국어 처리를 위해 언어를 넘겨준다.
            }
            List<UserGrpVo> requestPermitList = accountService.getRequestPermitGrpList();

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("userGrpVo", userGrpVo);
            returnData.put("permissionList", permissionList);
            returnData.put("requestPermitList", requestPermitList);
            returnData.put("grpCd", grpCd);
            returnData.put("UserVo", uv);

            // 권한이 넘어가지 않아서 관리자도 수정을 할수 없어서 추가
            String isaRole = userVo.getUserRole();
            returnData.put("isaRole", isaRole);

            return new ModelAndView("account/deptRoleList", returnData);  // account/accountDeptPermissionList
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 부서별 권한 변경요청
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/updateDeptPermission.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateDeptPermission(String grpCd, String userRole, String workDoc, HttpServletRequest request) {
        try {
            // 유저 권한에 따라 방식이 다르다
            // userRole 이 A 일 경우 임시 테이블을 사용하지 않고 바로 권한을 저장한다.
            if((Const.USER_ROLE_ADMIN).equals(userRole)) {
                Map<String, Object> menuRoleMap = accountService.getMenuRoleMap(request);
                // 권한 정보 저장
                accountService.insertAdminGrpPermission(grpCd, getLoginId(), menuRoleMap, workDoc);

                UserGrpVo userGrp = new UserGrpVo();
                userGrp.setGrpCd(grpCd);
                userGrp.setWorkDoc(workDoc);
                // 작업 근거문서 저장
                accountService.updateAccountDeptWorkDoc(userGrp);

                // 권한변경 요청 상태값 업데이트
                accountService.updateGrpPermissionInfo(grpCd, "", DateUtil.getNowDate());
            }

            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
