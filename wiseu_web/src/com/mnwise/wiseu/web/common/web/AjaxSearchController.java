package com.mnwise.wiseu.web.common.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.AjaxSearchVo;
import com.mnwise.wiseu.web.common.service.AjaxSearchService;

/**
 * 검색어 자동완성 Controller
 */
@Controller
public class AjaxSearchController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AjaxSearchController.class);

    @Autowired private AjaxSearchService ajaxSearchService;

    @RequestMapping(value = "/common/list_search.do", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView handle(AjaxSearchVo ajaxSearchVo, HttpServletResponse response) throws Exception {
        try {
            List<AjaxSearchVo> stList = ajaxSearchService.getAjaxSearchList(ajaxSearchVo);
            for(int i = 0, n = stList.size(); i < n; i++) {
                response.getWriter().write(stList.get(i).getSearchedWord() + "\n");
            }

            return null;
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
