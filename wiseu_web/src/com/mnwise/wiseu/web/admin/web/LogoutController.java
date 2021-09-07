package com.mnwise.wiseu.web.admin.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;

/**
 * - [로그인] 로그아웃 <br/>
 * - URL : /admin/logout.do <br/>
 * - JSP : /common/logout.jsp <br/>
 * 로그아웃 컨트롤러
 */
@Controller
public class LogoutController extends BaseController {
    @RequestMapping(value="/admin/logout.do")
    protected ModelAndView handleRequestInternal(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/");
    }
}