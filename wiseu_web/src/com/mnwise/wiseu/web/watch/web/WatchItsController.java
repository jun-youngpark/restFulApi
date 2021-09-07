package com.mnwise.wiseu.web.watch.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.sw.tms.server.GenericServer;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.watch.service.WatchService;

@Controller
public class WatchItsController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WatchItsController.class);

    @Autowired private WatchService watchService;

    /**
     * - [wiseWatch>프로세스 탭] ITS 프로세스 상태 출력
     * - JSP : /watch/processStatus.jsp
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/watch/processStatus_its.do")
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            String serverId = request.getParameter("serverId");
            String menu = request.getParameter("menu");
            Map<String, GenericServer> returnData = new HashMap<>();

            returnData.put("infoServer", watchService.getInfoServer(serverId));

            return new ModelAndView("watch/processStatus", returnData);  // watch/watch_info_server
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
