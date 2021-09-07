package com.mnwise.wiseu.web.account.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.PermissionVo;
import com.mnwise.wiseu.web.account.model.UserGrpVo;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.account.model.json.TreeEelement;
import com.mnwise.wiseu.web.account.service.AccountService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.TreeNode;
import com.mnwise.wiseu.web.env.model.TreeNodeState;

/**
 * 부서관리 Controller
 */
@Controller
public class AccountDeptController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AccountDeptController.class);

    @Autowired private AccountService accountService;

    /**
     * - [사용자 관리>부서 관리] 부서 관리 <br/>
     * - URL : /account/accountDept.do <br/>
     * - JSP : /account/accountDept.jsp <br/>
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/accountDept.do", method={RequestMethod.GET, RequestMethod.POST})  // /account/account_dept.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            String grpCd = StringUtil.defaultString(request.getParameter("grpCd"));  // 부서코드
            UserVo userVo = getLoginUserVo();
            // 미등록 리스트
            List<UserGrpVo> notRegList = accountService.getNotRegGrpList();
            // 미삭제 리스트
            List<UserGrpVo> notDelList = accountService.getNotDelGrpList();
            UserVo uv = accountService.getUserInfo(userVo.getUserId());
            List<PermissionVo> permissionList = null;
            // 선택된 부서 코드가 존재하고, 선택한 부서코드가 최상위 부서가 아닐경우 권한 정보를 가져온다.
            if(!grpCd.equals("") && !grpCd.equals(Const.SUPER_GRP_CD)) {
                permissionList = accountService.getGrpRoleList(grpCd, userVo.getLanguage());  // 다국어 처리를 위해 언어를 넘겨준다.
            }
            // 부서 정보를 가져온다. (부서코드가 없으면 최상위 부서 정보를 가져온다.)
            UserGrpVo userGrpVo = accountService.getUserGrpInfo(grpCd);
            Map<String, Object> returnData = new HashMap<>();
            returnData.put("UserVo", uv);
            returnData.put("permissionList", permissionList);
            returnData.put("grpCd", grpCd);
            returnData.put("notRegList", notRegList);
            returnData.put("notDelList", notDelList);
            returnData.put("userGrpVo", userGrpVo);

            return new ModelAndView("account/accountDept", returnData);  // account/account_dept
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [사용자 관리>부서 관리] 부서 관리
     * - 부서코드를 tree형태로 만든다.<br/>
     * - URL : /account/accountDept.do <br/>
     * - JSP : /account/accountDept.jsp <br/>
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/account_dept.json", method = RequestMethod.GET)
    @ResponseBody public ArrayList<TreeNode> codeTree(String grpCd, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 부서 리스트를 가져온다
            TreeEelement treeEelement = new TreeEelement();
            treeEelement.setId(grpCd);
            List<TreeEelement> groupList = this.accountService.selectGroup(treeEelement);
            //List<UserGrpVo> groupList = accountService.getGrpList();
            ArrayList<TreeNode> treeList = new ArrayList<TreeNode>();
            for(TreeEelement tree : groupList) {
                treeList.add(new TreeNode(tree.getId(),tree.getText(), tree.getSuperId() ,tree.getHasChildren(), new TreeNodeState()));
            }
            return treeList;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [사용자 관리>부서 관리] 부서 관리
     * - 클릭한 부서 정보를 가져온다 <br/>
     * - URL : /account/accountDept.do <br/>
     * - JSP : /account/accountDept.jsp <br/>
     *
     * @param grpCd
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/get_account_dept.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public UserGrpVo selectCodeList(String grpCd) throws Exception {
        try {
            // 부서 정보를 가져온다. (부서코드가 없으면 최상위 부서 정보를 가져온다.)
            UserGrpVo userGrpVo = accountService.getUserGrpInfo(grpCd);
            return userGrpVo;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [사용자 관리>부서 관리] 부서 등록 (팝업) <br/>
     * - [사용자 관리>부서 관리] 부서 수정 (팝업) <br/>
     * - URL : /account/deptPopup.do <br/>
     * - JSP : /account/deptPopup.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/deptPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /account/account_dept_popup.do
    public ModelAndView popupUserGrp(String grpCd, String supraGrpCd, String userRole) throws Exception {
        try {
            UserGrpVo userGrpVo = null;

            // 수정일 경우 grpCd 값이, 신규일경우 supraGrpCd 값이 들어온다.
            if(StringUtil.isBlank(grpCd)) { // 신규일 경우 상위 부서의 네임을 가져온다.
                userGrpVo = accountService.getSupraInfo(supraGrpCd);
            } else { // 수정일 경우 부서의 정보를 가져온다.
                userGrpVo = accountService.getUserGrpInfo(grpCd);
            }

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("userGrpVo", userGrpVo);
            returnData.put("grpCd", grpCd);
            returnData.put("supraGrpCd", supraGrpCd);
            returnData.put("userRole", userRole);

            return new ModelAndView("/account/deptPopup", returnData);  // /account/account_dept_popup
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 부서 등록
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/insertUserGrp.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertUserGrp(UserGrpVo userGrpVo, HttpServletRequest request) {
        try {
            userGrpVo.setLastUpdateDt(DateUtil.getNowDate());
            userGrpVo.setEditorId(getLoginId());

            String smsIndividualCharge = userGrpVo.getSmsIndividualCharge();

            // 부서별 개별 과금을 사용하지 않는경우에 요청부서 값을 blank로 셋팅
            if(!smsIndividualCharge.equals("on")) {
                userGrpVo.setReqDeptCd("");
            }
             String grpCd = accountService.insertAccountDept(userGrpVo, getLoginId(), userGrpVo.getUserRole());

            ResultDto resultDto = new ResultDto(ResultDto.OK, "save");
            resultDto.setValue(grpCd);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 부서 수정
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/updateUserGrp.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateUserGrp(UserGrpVo userGrpVo, HttpServletRequest request) {
        try {
            userGrpVo.setLastUpdateDt(DateUtil.getNowDate());
            userGrpVo.setEditorId(getLoginId());

            String smsIndividualCharge = userGrpVo.getSmsIndividualCharge();

            // 부서별 개별 과금을 사용하지 않는경우에 요청부서 값을 blank로 셋팅
            if(!smsIndividualCharge.equals("on")) {
                userGrpVo.setReqDeptCd("");
            }
            accountService.updateAccountDept(userGrpVo);

            ResultDto resultDto = new ResultDto(ResultDto.OK, "update");
            resultDto.setValue(userGrpVo.getGrpCd());
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 부서 삭제
     *
     * @param userGrpVo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/account/deleteUserGrp.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteUserGrp(UserGrpVo userGrpVo, HttpServletRequest request) {
        try {
            int result = accountService.updateAccountDeptAcceptYn(userGrpVo.getGrpCd(), getLoginId(), userGrpVo.getWorkDoc(), userGrpVo.getUserRole());
            if(result < 1) {
                return new ResultDto(ResultDto.FAIL);
            }

            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
