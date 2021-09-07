package com.mnwise.wiseu.web.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.JsonView;
import org.springframework.web.util.WebUtils;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.admin.model.AdminSessionVo;
import com.mnwise.wiseu.web.admin.service.AdminService;
import com.mnwise.wiseu.web.admin.service.MenuService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.MenuVo;

/**
 * 사용자 로그인 컨트롤러 - IT 관리자들을 위한 별도의 로그인 화면 제공
 */
@Controller
public class AdminExController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AdminExController.class);

    @Autowired private AdminService adminService;
    @Autowired private MenuService menuService;
    @Autowired(required=true) private HttpServletRequest request;

    private String loginedPath = "/campaign/campaignList.do";  // /campaign/campaign.do

    /**
     * 로그인 화면 출력
     *
     * @return
     */
    @RequestMapping(value= "/admin/index_ex.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index() {
        return new ModelAndView("/admin/index_ex");
    }

    @RequestMapping(value="/admin/login_ex.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loginEx() throws Exception {
        try {
            Map<String, String> returnData = new HashMap<>();
            String adminId = ServletRequestUtils.getStringParameter(request, "adminId");
            String adminPwd = ServletRequestUtils.getStringParameter(request, "adminPwd");

            if(log.isInfoEnabled()) {
                log.info("Java 가상 머신 메모리의 총용량: " + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " MB");
                log.info("Java 가상 머신이 사용을 시도하는 최대 메모리 용량: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB");
                log.info("Java 가상 머신 내의 빈메모리의 양: " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
                log.info("env product use : " + super.envProductUse);
            }

            // 기존에 ajax form을 이용해서 처리하기 때문에 redirect용 view를
            // 같이 사용하기 위해서 returnView를 따로 선언함
            View returnView = new JsonView();

            // Session 생성
            if(StringUtil.checkNull(adminId) && StringUtil.checkNull(adminPwd)) {
                UserVo userVo = null;

                try {
                    userVo = adminService.getAdminByAdminId(adminId, adminPwd);
                } catch(Exception e) {
                    /* 여기오면 JDBC 연결에 문제가 있음 */
                    returnData.put("result", "false");
                    returnData.put("errMsg", "DB 연결에 실패하였습니다.");
                    return new ModelAndView(returnView, "login", returnData);
                }

                if(userVo == null) {
                    returnData.put("result", "false");
                    returnData.put("errMsg", "잘못된 아이디입니다.");
                    return new ModelAndView(returnView, "login", returnData);
                } else {

                    if(!adminPwd.trim().equals(userVo.getPassWd())) {
                        returnData.put("result", "false");
                        returnData.put("errMsg", "잘못된 비밀번호 입니다.");
                        return new ModelAndView(returnView, "login", returnData);
                    } else {
                        AdminSessionVo adminSessionVo = new AdminSessionVo();
                        adminSessionVo.setUserVo(userVo);

                        adminSessionVo.setRoleMap(adminService.getMenuRole(userVo));

                        // 사용자 접근 가능 메뉴 목록 조회
                        List<MenuVo> userMenuList = adminService.getUserMenuList(adminId, userVo.getGrpCd());
                        WebUtils.setSessionAttribute(request, "userMenuList", userMenuList);

                        // 사용자의 캠페인, 이케어등 권한이 있는지 담는다.
                        // 캠페인만 있으면 C 둘다 있으면 CE 이다.
                        String sTemp = "";
                        MenuVo mv = null;
                        for(int i = 0, n = userMenuList.size(); i < n; i++) {
                            mv = userMenuList.get(i);
                            if(mv.getMenuCd().startsWith("01") && sTemp.indexOf(Const.P_CAMPAIGN) == -1) {
                                sTemp += Const.P_CAMPAIGN;
                            } else if(mv.getMenuCd().startsWith("02") && sTemp.indexOf(Const.P_ECARE) == -1) {
                                sTemp += Const.P_ECARE;
                            }
                        }
                        adminSessionVo.setPermssion(sTemp);

                        WebUtils.setSessionAttribute(request, "adminSessionVo", adminSessionVo);
                        returnData.put("result", "true");

                        // 사용자 첫 로그인 화면 조회
                        String strPath = getStartMenuLink(userMenuList, adminSessionVo.getLanguage());

                        if(userMenuList.size() == 0) {
                            returnData.put("result", "false");
                            returnData.put("errMsg", "접속 권한이 없는 계정입니다. 관리자에게 문의하시기 바랍니다.");
                            return new ModelAndView(returnView, "login", returnData);
                        } else if(strPath == null || "".equals(strPath)) {
                            returnData.put("goPath", this.loginedPath);
                        } else {
                            returnData.put("goPath", strPath);
                        }

                        // 씨티은행 세션 로그 관리
                        // 관리자가 아닌 계정만 로그 관리한다.
                        return new ModelAndView(returnView, "login", returnData);
                    }
                }
            }

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 씨티은행 추가 - 사용자의 첫 로그인 메뉴 URL 가져오기
     *
     * @param userMenuList
     * @return
     * @throws Exception
     */
    private String getStartMenuLink(List<MenuVo> userMenuList, String language) throws Exception {
        try {
            String strPath = null;

            // 처음 이동하기 위한 메뉴 URL 찾기
            List<MenuVo> topMenuList = menuService.getTopMenuList();
            List<MenuVo> subMenuList = menuService.getSubMenuList(language);
            MenuVo topMenuVo = null;
            MenuVo userMenuVo = null;
            MenuVo subMenuVo = null;
            String selectTopMenuCd = null;

            // 탑메뉴중에 사용자 권한에 해당하는 탑메뉴가 있는지 먼저 확인한다.
            for(int i = 0; i < topMenuList.size(); i++) {
                topMenuVo = topMenuList.get(i);
                for(int j = 0; j < userMenuList.size(); j++) {
                    userMenuVo = userMenuList.get(j);
                    if(userMenuVo.getMenuCd().startsWith(topMenuVo.getMenuCd()) && userMenuVo.getMenuLinkUrl() != null) {
                        selectTopMenuCd = topMenuVo.getMenuCd();
                        break;
                    }
                }

                // 탑메뉴를 찾은후에 맨 처음 등장하는 메뉴가 사용자가 처음 로그인할 화면이다.
                if(selectTopMenuCd != null) {
                    for(int k = 0; k < subMenuList.size(); k++) {
                        subMenuVo = subMenuList.get(k);
                        if(subMenuVo.getMenuCd().startsWith(selectTopMenuCd)) {
                            strPath = subMenuVo.getMenuLinkUrl();
                            break;
                        }
                    }
                }

                if(strPath != null) {
                    break;
                }
            }
            return strPath;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
