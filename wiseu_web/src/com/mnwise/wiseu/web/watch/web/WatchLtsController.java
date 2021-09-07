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

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.watch.service.WatchService;

@Controller
public class WatchLtsController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WatchLtsController.class);

    @Autowired private WatchService watchService;

    /**
     * - [wiseWatch>프로세스 탭] LTS 프로세스 상태 출력
     * - JSP : /watch/processStatus.jsp
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/watch/processStatus_lts.do")
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            String serverId = request.getParameter("serverId");
            String menu = request.getParameter("menu");
            Map<String, Object> returnData = new HashMap<>();

            returnData.put("infoServer", watchService.getInfoServer(serverId));

            // LTS서버 모드 상태값 셋팅
            String pollingActStatus = watchService.getPollingActStatus(serverId);
            if(pollingActStatus != null) {
                returnData.put("pollingActStatus", pollingActStatus);
            }

            return new ModelAndView("watch/processStatus", returnData);  // watch/watch_info_server
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
