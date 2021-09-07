package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;
import com.mnwise.wiseu.web.env.service.EnvServerInfoService;

/**
 * 서버환경정보 Controller
 */
@Controller
public class EnvServerInfoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvServerInfoController.class);

    @Autowired private EnvServerInfoService envServerInfoService;

    /**
     * - [환경설정>서버환경정보] 서버환경정보 <br/>
     * - URL : /env/env.do <br/>
     * - JSP : /env/env.jsp <br/>
     *
     * @return
     */
    @RequestMapping(value="/env/env.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view() throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            returnData.put("envServerInfoVo", envServerInfoService.selectEnvServerInfo());

            return new ModelAndView("/env/env", returnData);  // /env/env_serverinfo
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     *  [환경설정>서버환경정보] 서버환경정보 - 저장
     *
     * @param serverInfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateServerInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto update(ServerInfoVo serverInfo) {
        try {
            BeanUtil.unescapeXss(serverInfo, "linkPath");
            BeanUtil.unescapeXss(serverInfo, "openclickPath");
            BeanUtil.unescapeXss(serverInfo, "rejectPath");
            BeanUtil.unescapeXss(serverInfo, "aseLinkMergeParam");
            BeanUtil.unescapeXss(serverInfo, "aseOpenScriptlet");
            BeanUtil.unescapeXss(serverInfo, "aseRejectMergeParam");
            BeanUtil.unescapeXss(serverInfo, "groovyLinkMergeParam");
            BeanUtil.unescapeXss(serverInfo, "groovyOpenScriptlet");
            BeanUtil.unescapeXss(serverInfo, "groovyRejectMergeParam");

            envServerInfoService.updateEnvServerInfo(serverInfo);
            return new ResultDto(ResultDto.OK);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
