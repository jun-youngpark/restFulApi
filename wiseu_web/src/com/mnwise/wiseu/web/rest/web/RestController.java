package com.mnwise.wiseu.web.rest.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.rest.model.RequestVo;
import com.mnwise.wiseu.web.rest.service.RequestService;

@Controller
public class RestController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired private RequestService requestService;

    @RequestMapping(value = "/v1.0/targets" , method=RequestMethod.GET )
    public ModelAndView nRealtime(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            // RequestVo는 요청을 객체화 시킨 대상이며, 요청에 대한 상태(State)를 가지고 있다.
            // 요청을 파싱하여 요청객체로 '변환' 한다.
            final RequestVo vo = requestService.parser(request);

            // 요청을 '수행' 한다.
            requestService.operate(vo);

            // 요청에 대한 결과를 '응답' 한다.
            return requestService.response(response, vo);
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

    @RequestMapping (value="/v.3.2/test")
    public ModelAndView test() {
        ModelAndView result = new ModelAndView();
        result.setViewName("admin/index");

        return result;
    }

}
