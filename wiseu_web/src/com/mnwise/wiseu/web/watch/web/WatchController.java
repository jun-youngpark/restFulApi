package com.mnwise.wiseu.web.watch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.sw.tms.server.GenericServer;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.watch.model.ServiceTid;
import com.mnwise.wiseu.web.watch.service.WatchService;

/**
 * Watch Controller 프로세스, 캠페인, 이케어, 실시간 서비스 모니터링을 제공하기 위한 컨트롤러
 */
@Controller
public class WatchController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WatchController.class);

    @Autowired private WatchService watchService;

    /**
     * - [wiseWatch] 메인 (팝업) <br/>
     * - [wiseWatch] 프로세스 탭 <br/>
     * - [wiseWatch] 캠페인 탭 <br/>
     * - [wiseWatch] 이케어 탭 <br/>
     * - URL : /watch/watch.do <br/>
     * - JSP : /watch/watch.jsp <br/>
     * - JSP : /watch/watch_server.jsp <br/>
     * - JSP : /watch/watch_campaign.jsp <br/>
     * - JSP : /watch/watch_ecare.jsp <br/>
     * 모니터링 탭 메뉴에 따라 로그인/로그아웃 수행
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/watch/watch.do")
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            String menu = request.getParameter("menu");
            String session = request.getParameter("session");
            String useSubMenu = request.getParameter("useSubMenu");
            String selectNum = request.getParameter("selectNum");
            if(selectNum == null) {
                selectNum = "10";
            }

            UserVo userVo = getLoginUserVo();
            String userId = userVo.getUserId();
            String passWd = userVo.getPassWd();

            log.debug("menu: " + menu + ", session: " + session + ", userId: " + userId + ", selectNum: " + selectNum);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("useSubMenu", useSubMenu);
            returnData.put("selectNum", selectNum);

            // 로그인
            if(menu == null || !menu.equalsIgnoreCase("") || StringUtil.equals(session, "logOut") == false) {
                String result = watchService.connectTms(userId, passWd);
                if(result.indexOf("+") != 0) {
                    returnData.put("errorMsg", "-ERR");
                    return new ModelAndView("watch/watch_campaign", returnData);
                } else {
                    returnData.put("errorMsg", "+OK");
                }
            }

            // Watch 첫 페이지를 캠페인으로 지정
            if(menu == null || menu.length() == 0) {
                returnData.put("menu", "campaign");

                return new ModelAndView("watch/watch_campaign", returnData);
            } else {
                returnData.put("menu", menu);
                if(menu.equalsIgnoreCase("server"))
                    returnData.put("getListServer", watchService.getListServer());

                return new ModelAndView("watch/watch_" + menu, returnData);
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [wiseWatch>캠페인 탭] 캠페인 목록 <br/>
     * - [wiseWatch>이케어 탭] 이케어 목록 <br/>
     * - URL : /watch/serviceList.do <br/>
     * - JSP : /watch/serviceList_campaign.jsp <br/>
     * - JSP : /watch/serviceList_ecare.jsp <br/>
     * 캠페인/이케어 서비스 중 발송중이거나 완료된 목록을 가져온다.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/watch/serviceList.do")  // /watch/watch_list.do, params ="cmd=listService"
    public ModelAndView listService(HttpServletRequest request) throws Exception {
        try {
            String menu = StringUtil.defaultString(request.getParameter("menu"));
            String selectNum = StringUtil.defaultString(request.getParameter("selectNum"));
            String tid = request.getParameter("tid");
            Map<String, Object> returnData = new HashMap<>();
            String client = null;

            if(menu.equals("ecare")) {
                client = "EC";
                String serviceType = request.getParameter("serviceType");
                if(serviceType == null || serviceType.equals(""))
                    serviceType = "S";
                returnData.put("serviceType", serviceType);
            }else {
                client = "EM";
            }

            if(menu.equals("realtime")) { // 실시간
                returnData.put("monitorList", watchService.getEcareMonitorStatusList2(getLoginId()));
                return new ModelAndView("watch/serviceList_realtime", returnData);  // watch/watch_list_realtime
            } else {
                returnData.put("tid", tid);
                returnData.put("selectNum", selectNum);
                returnData.put("getListService", watchService.getListService(client, selectNum));

                return new ModelAndView("watch/serviceList_" + menu, returnData);  // "watch/watch_list_" + menu
            }
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [wiseWatch>캠페인 탭] 서비스 상태 <br/>
     * [wiseWatch>이케어 탭] 서비스 상태 <br/>
     * - URL : /watch/serviceStatus.do <br/>
     * - JSP : /watch/serviceStatus.jsp <br/>
     *
     * 하나의 TID 에 대한 LTS, MTS 서비스 상세내역 보기
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/watch/serviceStatus.do")  // /watch/watch_info_service.do, params ="cmd=infoService"
    public ModelAndView infoService(HttpServletRequest request) throws Exception {
        try {
            String tid = request.getParameter("tid");
            Map<String, Object> returnData = new HashMap<>();

            // 모든 서비스 목록 가져옴
            returnData.put("tid", tid);
            returnData.put("getInfoService", watchService.getInfoService(tid));

            return new ModelAndView("watch/serviceStatus", returnData);  // watch/watch_info_service
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 프로세스의 서버 목록
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    /*@RequestMapping(value="/watch/watch.do",params ="cmd=listServer")
    public ModelAndView listServer(HttpServletRequest request) throws Exception {
        try {
            String menu = request.getParameter("menu");
            Map<String, Object> returnData = new HashMap<>();

            // 모든 서버 목록 가져옴
            returnData.put("getListServer", watchService.getListServer());

            return new ModelAndView("watch/watch_list_server", returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }*/

    /**
     * watch 실시간 모니터링 서비스 추가 팝업
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    /*@RequestMapping(value="/watch/watch.do", params ="realtimeAddPopup")
    public ModelAndView realtimeAddPopup(HttpServletRequest request) throws Exception {
        try {
            return new ModelAndView("watch/watch_realtime_add_popup");
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }*/

    /**
     * - [wiseWatch>프로세스 탭] 환경설정 (팝업)
     * - JSP : /watch/watch_config_server.jsp
     *
     * 프로세스에서 환경설정을 눌렀을 시 그 serverID 에 대한 환경설정 정보를 TMS 에서 받아서 보여줌
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/watch/processConfigPopup.do")  // /watch/watch_config_server.do, params ="cmd=configServer"
    public ModelAndView configServer(HttpServletRequest request) throws Exception {
        try {
            String serverId = request.getParameter("serverId");
            Map<String, Object> returnData = new HashMap<>();

            returnData.put("serverId", serverId);
            returnData.put("getConfig", watchService.getConfig(serverId));
            returnData.put("getConfigString", watchService.getConfigNew(serverId));

            return new ModelAndView("watch/processConfigPopup", returnData);  // watch/watch_config_server
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value = "/watch/getProcessInfo.json", method=RequestMethod.GET)
    @ResponseBody public GenericServer getInfoServer(String serverId) throws Exception {
        try {
            return watchService.getInfoServer(serverId);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value = "/watch/saveProcessConfig.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto saveProcessConfig(String serverId, String config) {
        try {
            watchService.saveProcessConfig(serverId, config);
            return new ResultDto(ResultDto.OK);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value = "/watch/processStatus.json", method=RequestMethod.GET)
    @ResponseBody public ResultDto getPollingActStatus(String serverId) {
        try {
            String status = watchService.getPollingActStatus(serverId);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(status);
            return resultDto;
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value = "/watch/startProcess.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto startServer(String serverId) {
        try {
            boolean isSuccess = watchService.startServer(serverId);
            return isSuccess ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    @RequestMapping(value = "/watch/stopProcess.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto stopServer(String serverId) {
        try {
            boolean isSuccess = watchService.stopServer(serverId);
            return isSuccess ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 서비스 상태 정보 조회
     *
     * @param tid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/watch/getServiceInfo.json", method=RequestMethod.POST)
    @ResponseBody public List<ServiceTid> getInfoService(String tid) throws Exception {
        try {
            return watchService.getInfoService(tid);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 서비스 재기동 버튼 클릭
     *
     * @param tid
     * @return
     */
    @RequestMapping(value = "/watch/restartService.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto restartService(String tid) {
        try {
            boolean isSuccess = watchService.restartService(tid);
            return isSuccess ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 서비스 일시정지 버튼 클릭
     *
     * @param tid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/watch/pauseService.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto pauseService(String tid) {
        try {
            boolean isSuccess = watchService.pauseService(tid);
            return isSuccess ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 서비스 정지 버튼 클릭
     *
     * @param tid
     * @return
     */
    @RequestMapping(value = "/watch/stopService.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto stopService(String tid) {
        try {
            boolean isSuccess = watchService.stopService(tid);
            return isSuccess ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * 서비스 삭제 버튼 클릭
     *
     * @param tid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/watch/deleteService.json", method=RequestMethod.POST)
    @ResponseBody public ResultDto deleteService(String tid) {
        try {
            boolean isSuccess = watchService.deleteService(tid);
            return isSuccess ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch (Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
