package com.mnwise.wiseu.web.admin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.JsonView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.admin.model.AdminSessionVo;
import com.mnwise.wiseu.web.admin.service.AdminService;
import com.mnwise.wiseu.web.admin.service.MenuService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.util.LoginManager;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.MenuVo;

/**
 * 사용자 로그인 컨트롤러
 */
@Controller
public class AdminController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired private AdminService adminService;
    @Autowired private MenuService menuService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;
    @Autowired(required=true) private HttpServletRequest request;

    @Value("${login.session.check:''}")
    private String loginSessionCheck;
    @Value("${web.root}")
    private String webRoot;

    @Value("${login.mail.auth}")
    private String loginMailAuth;

    private LoginManager loginManager = LoginManager.getInstance();

    /**
     * 로그인 화면 출력
     *
     * @return
     */
    @RequestMapping(value= "/admin/index.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index() {
        if("on".equalsIgnoreCase(loginMailAuth)) {
            return new ModelAndView("/admin/index_auth");
        }else {
            return new ModelAndView("/admin/index");
        }
    }

    /**
     * 로그인 처리
     */
    @RequestMapping(value= "/admin/login.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(HttpSession session) throws Exception {
        try {
            Map<String, String> returnData = new HashMap<>();
            String adminId = ServletRequestUtils.getStringParameter(request, "adminId");
            String adminPwd = ServletRequestUtils.getStringParameter(request, "adminPwd");
            String language = ServletRequestUtils.getStringParameter(request, "language");
            String pullLoginUser = ServletRequestUtils.getStringParameter(request, "pullLoginUser", "N");

            if(log.isInfoEnabled()) {
                log.info("Java 가상 머신 메모리의 총용량: " + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " MB");
                log.info("Java 가상 머신이 사용을 시도하는 최대 메모리 용량: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB");
                log.info("Java 가상 머신 내의 빈메모리의 양: " + (Runtime.getRuntime().freeMemory() / (1024 * 1024)) + " MB");
                log.info("env product use : " + super.envProductUse);
            }

            // 기존 세션을 무효화
            session.invalidate();

            // 기존에 ajax form을 이용해서 처리하기 때문에 redirect용 view를
            // 같이 사용하기 위해서 returnView를 따로 선언함
            View returnView = new JsonView();

            // 운영계에서 기동할때는 sso 로그인을 사용함
            if(super.envProductUse != null && super.envProductUse.equals("on")) {
                // SSO 로그인 추가
                String soeid = StringUtil.defaultString(request.getParameter("id"));
                boolean isSoeidValid = false;

                if(!soeid.equals("")) {
                    UserVo userVo = null;

                    try {
                        userVo = adminService.getUserInfoBySoeid(soeid);
                    } catch(Exception e) {
                        // SOEID 가 중복되면 다른 사용자의 아이디로 로그인 될 수 있으므로 로그인을 차단한다.
                        returnData.put("msgTitle", "로그인 실패");
                        returnData.put("msgBody", "SOEID가 중복되어 로그인 시도를 차단하였습니다. 관리자에게 문의하세요.");
                        return new ModelAndView(new RedirectView("/admin/logout.do"), returnData);
                    }

                    if(userVo != null) {
                        adminId = userVo.getUserId();
                        adminPwd = userVo.getPassWd();
                        isSoeidValid = true;
                        returnView = new RedirectView("/campaign/campaignList.do");  // /campaign/campaign.do
                    }
                } else {
                    // product로 동작중이고 SOE아이디가 없이 넘어왔다면 로그인 화면에서 넘어온것으로 간주함
                    returnData.put("result", "false");
                    returnData.put("errMsg", "Product 환경에서는 아이디/패스워드 접속 로그인을 이용하실 수 없습니다.");
                    return new ModelAndView(returnView, "login", returnData);
                }

                // SOEID가 유효하지 않다면 로그아웃 페이지로 이동함
                if(!isSoeidValid) {
                    returnData.put("msgTitle", "로그인 실패");
                    returnData.put("msgBody", "SOEID가 유효하지 않습니다.");
                    return new ModelAndView(new RedirectView("/admin/logout.do"), returnData);
                }
            }

            // Session 생성
            if(StringUtil.checkNull(adminId) && StringUtil.checkNull(adminPwd)) {
                Locale locale = new Locale(language);
                String loginfail = messageSourceAccessor.getMessage("common.msg.loginfail", locale);
                String dbfail = messageSourceAccessor.getMessage("common.msg.dbfail", locale);
                String noauth = messageSourceAccessor.getMessage("common.msg.noauth", locale);
                String pullLoginUserMsg = messageSourceAccessor.getMessage("common.msg.pullLoginUser", locale);

                UserVo userVo = null;
                try {
                    userVo = adminService.getAdminByAdminId(adminId, adminPwd);
                } catch(Exception e) {
                    log.error("while getting UserVo, Exception occurred. " + e.getMessage());
                    returnData.put("result", "false");
                    returnData.put("errMsg", dbfail);
                    return new ModelAndView(returnView, "login", returnData);
                }

                if(userVo == null) {
                    returnData.put("result", "false");
                    returnData.put("errMsg", loginfail);
                    log.info("접속 권한이 없거나 존재하지 않는 계정입니다. : ID " + adminId);
                    return new ModelAndView(returnView, "login", returnData);
                } else {
                    // 로그인 시도 최종 횟수 가져옴 default = 0
                    int failCount = userVo.getLoginCnt();

                    GregorianCalendar date = new GregorianCalendar();
                    date.setGregorianChange(new Date());
                    date.add(Calendar.DATE, -90); // 3개월 전 날짜 추출

                    // 일반계정이고 login cnt 가 5회 넘을 경우 로그인 불가능
                    if(!adminId.equals("admin") && failCount >= 5) {
                        returnData.put("result", "false");
                        returnData.put("errMsg", loginfail);
                        log.info("계정이 잠겼습니다. : ID " + adminId);
                        return new ModelAndView(returnView, "login", returnData);
                    }

                    if(!userVo.getPassWd().equals(adminPwd)) {// 암호 불일치
                        String userId = userVo.getUserId();
                        if(!userId.equals("admin")) {
                            // admin이 아닐 경우 login cnt +1 업데이트
                            adminService.updateLoginFailCount(userId);
                            returnData.put("result", "false");
                            returnData.put("errMsg", loginfail);
                            log.info("5회 이상 로그인 실패 시 계정 잠깁니다. 현재 로그인 시도 횟수 : " + (failCount + 1) + " ID : " + adminId);
                            return new ModelAndView(returnView, "login", returnData);
                        } else {
                            returnData.put("result", "false");
                            returnData.put("errMsg", loginfail);
                            log.info("잘못된 비밀번호 입니다 ID : " + adminId);
                            return new ModelAndView(returnView, "login", returnData);
                        }
                    } else {// 암호 일치
                        boolean isSessionCheck = this.loginSessionCheck.equalsIgnoreCase("on");

                        if(isSessionCheck) {
                            // 동일 계정 로그인 하게 되면 이전 세션은 종료시킨다.
                            if(loginManager.isUsing(adminId)) {
                                if("Y".equals(pullLoginUser)) { // 기존 접속자를 밀어냄.
                                    //기존의 접속(세션)을 끊는다.
                                    loginManager.removeSession(adminId);
                                    //새로운 세션을 등록한다. setSession함수를 수행하면 valueBound()함수가 호출된다.
                                    loginManager.setSession(request.getSession(), adminId);
                                    returnData.put("isUsing", "true");
                                } else {
                                    returnData.put("pullLoginUser", "N");
                                    returnData.put("errMsg", pullLoginUserMsg);
                                    return new ModelAndView(returnView, "login", returnData);
                                }
                            } else {
                                //새로운 세션을 등록한다. setSession함수를 수행하면 valueBound()함수가 호출된다.
                                loginManager.setSession(request.getSession(), adminId);
                            }
                        }

                        AdminSessionVo adminSessionVo = new AdminSessionVo();
                        adminSessionVo.setUserVo(userVo);

                        // Xml에 userVo를 넘기므로 언어를 세팅해 준다.
                        userVo.setLanguage(language);
                        adminSessionVo.setLanguage(language);
                        Map<String, Map<String, String>> roleMap = adminService.getMenuRole(userVo);
                        adminSessionVo.setRoleMap(roleMap);

                        // 로그인 후 login_cnt 및 lastlogin_dt update
                        adminService.updateLoginFailCountToInitial(userVo.getUserId());

                        // 마지막 비밀번호 수정일이 3개월 전이면 암호 변경 페이지로 이동하도록 함
                        if((userVo.getLastUpdateDt().compareTo(DateFormatUtils.format(date.getTime(), "yyyyMMdd")) < 0) && !userVo.getUserId().equals("admin")) {
                            MenuVo mv = new MenuVo();
                            // 비밀번호 변경을 위한 메뉴 접근 권한 부여
                            List<MenuVo> envList = new ArrayList<>();
                            mv.setMenuLinkUrl("/env/myInfo.do");  // /env/env_myinfo.do
                            mv.setReadAuth("R");
                            mv.setWriteAuth("W");
                            mv.setMenuCd("0712");
                            envList.add(mv);
                            List<MenuVo> userMenuList = envList;

                            // 메뉴 role 수동으로 부여
                            Map<String, String> envMap = new HashMap<>();
                            envMap.put("menuLinkUrl", "/env/myInfo.do");  // /env/env_myinfo.do
                            envMap.put("write", "W");
                            envMap.put("read", "R");
                            envMap.put("menuCd", "0712");
                            roleMap.put("/env/myInfo.do", envMap);  // /env/env_myinfo.do
                            adminSessionVo.setRoleMap(roleMap);

                            WebUtils.setSessionAttribute(request, "userMenuList", userMenuList);
                            WebUtils.setSessionAttribute(request, "adminSessionVo", adminSessionVo);
                            WebUtils.setSessionAttribute(request, "passwdChange", "passwdChange");
                            returnData.put("result", "true");
                            returnData.put("goPath", "/env/myInfo.do");  // /env/env_myinfo.do
                            return new ModelAndView(new JsonView(), "login", returnData);
                        }

                        // 사용자 접근 가능 메뉴 목록 조회
                        List<MenuVo> userMenuList = adminService.getUserMenuList(adminId, userVo.getGrpCd());
                        WebUtils.setSessionAttribute(request, "userMenuList", userMenuList);

                        // 사용자의 캠페인, 이케어등 권한이 있는지 담는다.
                        // 캠페인만 있으면 C 둘다 있으면 CE 이다.
                        String permssion = "";
                        for(int i = 0, n = userMenuList.size(); i < n; i++) {
                            MenuVo mv = userMenuList.get(i);
                            if(mv.getMenuCd().startsWith("01") && permssion.indexOf(Const.P_CAMPAIGN) == -1) {
                                permssion += Const.P_CAMPAIGN;
                            } else if(mv.getMenuCd().startsWith("02") && permssion.indexOf(Const.P_ECARE) == -1) {
                                permssion += Const.P_ECARE;
                            }
                        }
                        adminSessionVo.setPermssion(permssion);

                        WebUtils.setSessionAttribute(request, "adminSessionVo", adminSessionVo);
                        returnData.put("result", "true");

                        // 사용자 첫 로그인 화면 조회
                        // 사용 언어에 따라 서브메뉴를 가져오도록 변경 (carpe3m)
                        String strPath = getStartMenuLink(userMenuList, adminSessionVo.getLanguage());

                        // production 환경이라면 sso 로그인이므로 RedirectView로 바꿔준다.
                        if(super.envProductUse.equals("on")) {
                            returnView = new RedirectView(strPath);
                        }

                        if(userMenuList.size() == 0) {
                            returnData.put("result", "false");
                            returnData.put("errMsg", noauth);
                            return new ModelAndView(returnView, "login", returnData);
                        } else if(strPath == null || "".equals(strPath)) {
                            returnData.put("goPath", "/campaign/campaignList.do");  // /campaign/campaign.do
                        } else {
                            returnData.put("goPath", strPath);
                        }

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
     * 사용자의 첫 로그인 메뉴 URL 가져오기
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
                    for(int k = 0; k < userMenuList.size(); k++) {
                        subMenuVo = userMenuList.get(k);
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

    // 다국어 리소스 파일로드
    @RequestMapping(value = "/i18n/*.properties", method = RequestMethod.GET)
    @ResponseBody public void selectI18nProperties(HttpServletRequest request, HttpServletResponse response) {
        FileInputStream in = null;
        OutputStream out = null;

        try {
            response.setContentType("text/plan;charset=UTF-8");
            String url = request.getServletPath();
            int index = url.lastIndexOf("/");
            String filePath = webRoot + "/WEB-INF/messages/" + url.substring(index);
            File file = ResourceUtils.getFile(filePath.replaceAll("_ko", ""));

            long clientDate = request.getDateHeader("If-Modified-Since");
            long fileDate = file.lastModified();
            if (clientDate != -1 && clientDate >= fileDate) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                log.error("/i18n/*.properties HttpServletResponse.SC_NOT_MODIFIED={}", HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }

            response.setDateHeader("Last-Modified", fileDate);
            in = FileUtil.openInputStream(file);
            out = response.getOutputStream();
            IOUtil.copyLarge(in, out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage());

            try {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        } finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
        }
    }
}
