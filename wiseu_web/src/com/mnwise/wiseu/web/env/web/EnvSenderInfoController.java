package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.EnvSenderInfoVo;
import com.mnwise.wiseu.web.env.service.EnvSenderInfoService;

/**
 * 메시지 발신자 정보 Controller
 */
@Controller
public class EnvSenderInfoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvSenderInfoController.class);

    @Autowired private EnvSenderInfoService envSenderInfoService;

    /**
     * - [환경설정>메시지 발신자 정보] 메시지 발신자 정보 <br/>
     * - URL : /env/senderInfo.do <br/>
     * - JSP : /env/env_senderinfo.jsp <br/>
     *
     * @param ret
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/senderInfo.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/senderinfo.do
    public ModelAndView view(String ret, HttpServletRequest request) throws Exception {
        try {
            EnvSenderInfoVo envSenderInfoVo = new EnvSenderInfoVo();
            envSenderInfoVo.setUserId(getLoginId());
            envSenderInfoVo = envSenderInfoService.selectEnvSenderInfo(envSenderInfoVo);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("envSenderInfoVo", envSenderInfoVo);
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("ret", ret);
            return new ModelAndView("/env/senderInfo", returnData);  // /env/env_senderinfo
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 환경설정 - 메시지 발신자 정보에서 개인정보를 변경한다.
     *
     * @param envSenderInfoVo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateSenderInfo.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateSenderInfo(EnvSenderInfoVo envSenderInfoVo, HttpServletRequest request) throws Exception {
        try {
            envSenderInfoVo.setUserId(getLoginId());
            envSenderInfoService.updateEnvSenderInfo(envSenderInfoVo);

            Map<String, String> returnData = new HashMap<>();
            returnData.put("ret", "success");
            return new ModelAndView(new RedirectView("/env/senderInfo.do"), returnData);  // /env/senderinfo.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 환경설정 - 메시지 발신자 정보에서 개인정보를 등록한다.
     *
     * @param envSenderInfoVo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/insertSenderInfo.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView insertSenderInfo(EnvSenderInfoVo envSenderInfoVo, HttpServletRequest request) throws Exception {
        try {
            envSenderInfoVo.setUserId(getLoginId());
            envSenderInfoService.insertEnvSenderInfo(envSenderInfoVo);

            Map<String, String> returnData = new HashMap<>();
            returnData.put("ret", "success");
            return new ModelAndView(new RedirectView("/env/senderInfo.do"), returnData);  // /env/senderinfo.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
