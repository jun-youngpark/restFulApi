package com.mnwise.wiseu.web.admin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.admin.model.AdminSessionVo;
import com.mnwise.wiseu.web.admin.service.AdminService;
import com.mnwise.wiseu.web.admin.service.MenuService;
import com.mnwise.wiseu.web.common.model.MenuVo;
import com.mnwise.wiseu.web.common.util.PropertyUtil;

/**
 * 로그인 인터셉터
 */
@Component
public class AdminLoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AdminLoginInterceptor.class);

    @Autowired private AdminService adminService;
    @Autowired private MenuService menuService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;

    private static final Map<String, String> PARENT_URI_MAP = initMap();

    /**
     * 캠페인, 이케어의 3 단계 등록 화면 대표 URI가 저장된 Map을 초기화
     *
     * @return
     */
    private static Map<String, String> initMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("/campaign/campaign2Step.do", "/campaign/campaign1Step.do");  // /campaign/campaign_2step_form.do, /campaign/campaign_step_form.do
        map.put("/campaign/campaign3Step.do", "/campaign/campaign1Step.do");  // /campaign/campaign_3step_form.do, /campaign/campaign_step_form.do
        map.put("/ecare/ecare2Step.do", "/ecare/ecare1Step.do");  // /ecare/ecare_2step_form.do, /ecare/ecare_step_form.do

        return map;
    }

    /**
     * 요청한 URI의 메뉴 권한 객체를 반환
     *
     * @param roleMap
     * @param requestUri
     * @return
     */
    private Map<String, String> getMenuRole(final Map<String, Map<String, String>> roleMap, String requestUri) {
        final Map<String, String> role = roleMap.get(requestUri);
        if(role != null) {
            return role;
        }

        final String uri = PARENT_URI_MAP.get(requestUri);
        if(uri == null) {
            return null;
        }

        final Map<String, String> parentRole = roleMap.get(uri);
        roleMap.put(requestUri, parentRole);

        return parentRole;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AdminSessionVo adminSessionVo = (AdminSessionVo) WebUtils.getSessionAttribute(request, "adminSessionVo");
        boolean getTagFlag = false;

        log.info("REQ-URI [{}] {}", WebUtils.getSessionId(request), request.getMethod() + " " + request.getContextPath() + request.getServletPath());

        String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/i18n/")) {  // 다국어 리소스 파일 요청은 예외처리
            return true;
        } else if(adminSessionVo == null) {
            ModelAndView modelAndView = new ModelAndView("redirect:/admin/index.do");
            throw new ModelAndViewDefiningException(modelAndView);
        } else {
            // 예외 처리
            if(requestUri.startsWith("/watch/") || requestUri.startsWith("/common/") || requestUri.startsWith("/bookmark/") || requestUri.startsWith("/jsp/common/")
                || requestUri.startsWith("/resend/")|| requestUri.startsWith("/jsp/template/")) {
                return true;
            }

            // spring에서 WAS 기동시에 로딩된 메뉴 데이타를 가져온다.
            // spring 빈은 기본적으로 싱글턴이므로 로딩될때 한번만 sql이 실행된다.
            // 사용자 사용가능 메뉴는 로그인 액션에서 저장함
            List<MenuVo> topMenuList = menuService.getTopMenuList();
            final String language = adminSessionVo.getLanguage();
            List<MenuVo> subMenuList = menuService.getSubMenuList(language);
            List<MenuVo> userMenuList = (List<MenuVo>) WebUtils.getSessionAttribute(request, "userMenuList");

            MenuVo userMenuVo = null;
            MenuVo topMenuVo = null;
            MenuVo subMenuVo = null;

            String selectTopMenuCd = "";

            List<MenuVo> userTopMenuList = new ArrayList<>();
            List<MenuVo> userTopSubMenuList = new ArrayList<>();
            List<MenuVo> userSubMenuList = new ArrayList<>();

            Map<String, CaseInsensitiveMap> menuNameMapOfAllLang = menuService.getMenuNameMap();
            CaseInsensitiveMap menuNameMap = menuNameMapOfAllLang.get(language);
            request.setAttribute("menuNameMap", menuNameMap);

            // 사용자 사용가능 탑 메뉴 설정
            for(int i = 0; i < topMenuList.size(); i++) {
                topMenuVo = topMenuList.get(i);
                for(int j = 0; j < userMenuList.size(); j++) {
                    userMenuVo = userMenuList.get(j);
                    // 사용자 권한이 있는 메뉴의 상위 메뉴를 탑 메뉴 출력 목록에 넣는다.
                    // 상위 메뉴는 01, 02, 03... 하위메뉴는 0101, 0102, 0201 .. 등의 룰을 지켜야
                    // 한다.

                    if(getTagFlag) {
                        log.debug(userMenuVo.getMenuCd() + ":" + topMenuVo.getMenuCd() + ":" + userMenuVo.getMenuCd().startsWith(topMenuVo.getMenuCd()));
                    }

                    if(userMenuVo.getMenuCd().startsWith(topMenuVo.getMenuCd()) && userMenuVo.getMenuLinkUrl() != null) {
                        // 사용자가 선택한 탑 메뉴
                        if(topMenuVo.getModuleNm().equals(request.getRequestURI().split("/")[1])) {
                            topMenuVo.setRollOver("true");
                            selectTopMenuCd = topMenuVo.getMenuCd();

                            if(getTagFlag) {
                                log.debug("- " + selectTopMenuCd);
                            }
                            request.setAttribute("moduleNm", topMenuVo.getModuleNm());
                            request.setAttribute("menuNm", menuNameMap.get(topMenuVo.getMenuCd()));
                        } else {
                            topMenuVo.setRollOver("false");
                        }

                        topMenuVo.setMenuLinkUrl(userMenuVo.getMenuLinkUrl());

                        if(topMenuVo.getMType().equals("S")) { // TOP MENU의 서브메뉴 인 경우
                            userTopSubMenuList.add(topMenuVo);
                        } else {
                            userTopMenuList.add(topMenuVo);
                        }
                        break;
                    }
                }
            }

            String menuUrl;
            // 전체 서브 메뉴 목록 중 선택된 대메뉴(selectTopMenuCd)의 서브메뉴만 userSubMenuList에 담는다.
            // 비밀번호가 틀렸을 시 userMenuList, subMenuList가 없으므로 조건 처리함 - 한규현
            if(userMenuList != null && subMenuList != null) {
                for(int j = 0; j < userMenuList.size(); j++) {
                    userMenuVo = userMenuList.get(j);
                    // 서브 메뉴 목록 선택
                    for(int k = 0; k < subMenuList.size(); k++) {
                        subMenuVo = subMenuList.get(k);
                        menuUrl= subMenuVo.getMenuLinkUrl();
                        if(subMenuVo.getMenuCd().equals(userMenuVo.getMenuCd())) {
                            userSubMenuList.add(subMenuVo);
                            if(StringUtil.isNotBlank(menuUrl) && StringUtil.equalsIgnoreCase(subMenuVo.getMenuLinkUrl(), requestUri)) {
                                request.setAttribute("subMenuNm", subMenuVo.getMenuNm());
                            }
                        }
                    }
                }
            }

            // 태그 박스 데이터를 가져온다
            String onMenu = "";
            if(requestUri.startsWith("/")) {
                String[] uriParts = requestUri.split("/");
                onMenu = uriParts[1];
            }
            String targetTable = onMenu;
            if(requestUri.contains("mobileTemplateList.do")) {  // mobileTemplate.do
                targetTable = "mobileTemplate";
            }

            if((targetTable.equalsIgnoreCase("campaign") || targetTable.equalsIgnoreCase("ecare") || targetTable.equalsIgnoreCase("template") || targetTable.equalsIgnoreCase("mobileTemplate")
                || targetTable.equalsIgnoreCase("segment")) && requestUri.endsWith(targetTable + ".do")) {
                getTagFlag = true;
            }

            @SuppressWarnings("rawtypes")
            List tagListMenu = null;
            if(getTagFlag) {
                tagListMenu = adminService.getTagList(targetTable, adminSessionVo.getUserVo());
            }

            request.setAttribute("tagListMenu", tagListMenu);
            request.setAttribute("userTopMenuList", userTopMenuList);
            request.setAttribute("userTopSubMenuList", userTopSubMenuList);
            request.setAttribute("userSubMenuList", userSubMenuList);
            request.setAttribute("webExecMode", PropertyUtil.getProperty("web.exec.mode", "1"));

            // 로고에 링크를 달기 위한 값 설정
            if(userTopMenuList != null) {
                request.setAttribute("logoLinkUrl", userTopMenuList.get(0).getMenuLinkUrl());
            }

            final Map<String, String> role = getMenuRole(adminSessionVo.getRoleMap(), requestUri);

            if(role != null) {
                WebUtils.setSessionAttribute(request, "requestUri", requestUri);
                WebUtils.setSessionAttribute(request, "write", role.get("write"));
                WebUtils.setSessionAttribute(request, "execute", role.get("execute"));

                final String readRole = role.get("read");
                if(readRole != null && readRole.length() == 0) {
                    response.sendRedirect("/jsp/common/error/error.jsp");
                    return false;
                }
            } else {
                for(MenuVo subMenu : subMenuList) {
                    if(StringUtil.isNotBlank(subMenu.getMenuLinkUrl()) && subMenu.getMenuLinkUrl().equals(requestUri)) {
                        request.setAttribute("errMsg", messageSourceAccessor.getMessage("common.msg.noauth", new Locale(language)));

                        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/common/error/error.jsp");
                        dispatcher.forward(request, response);
                        return false;
                    }
                }
            }
            return true;
        }
    }

}
