package com.mnwise.wiseu.web.admin.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;


@Controller
public class PwdEditController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PwdEditController.class);

    @RequestMapping(value="/admin/pwdedit.do" , method=RequestMethod.GET)
    public ModelAndView list() throws Exception {
        try {
            Map<String, String> returnData = new HashMap<>();
            returnData.put("userPw", getLoginUserVo().getPassWd());

            return new ModelAndView("/admin/pwdedit", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
