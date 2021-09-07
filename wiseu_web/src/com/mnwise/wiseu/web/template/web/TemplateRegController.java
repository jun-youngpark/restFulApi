package com.mnwise.wiseu.web.template.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.template.model.ContentVo;
import com.mnwise.wiseu.web.template.service.TemplateService;

@Controller
public class TemplateRegController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TemplateRegController.class);

    @Autowired private TemplateService templateService;

    /**
     * [템플릿>템플릿 등록] 템플릿 등록 화면 출력
     */
    @RequestMapping(value="/template/templateReg.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            ContentVo paramContentVo = new ContentVo();
            paramContentVo.setUserId(userVo.getUserId());
            paramContentVo.setGrpCd(userVo.getGrpCd());

            ModelAndView mav = new ModelAndView("template/templateReg");  // template/template_reg
            mav.addObject("contentVo", paramContentVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>템플릿 등록] 템플릿 등록정보 조회
     */
    @RequestMapping(value="/template/templateView.do", method={RequestMethod.GET, RequestMethod.POST})  // /template/viewTemplate.do
    public ModelAndView view(int contsNo, HttpServletRequest request) throws Exception {
        try {
            ContentVo contentVo = new ContentVo();
            contentVo.setContsNo(contsNo);
            contentVo = templateService.getTemplateInfo(contsNo);
            contentVo.setCommand("modify");

            ModelAndView mav = new ModelAndView("template/templateView");  // template/template_view
            mav.addObject("userVo", getLoginUserVo());
            mav.addObject("contentVo", contentVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
