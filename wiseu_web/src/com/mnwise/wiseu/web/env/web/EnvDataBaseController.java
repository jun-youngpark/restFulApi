package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.service.EnvDataBaseService;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;

/**
 * DB 관리 Controller
 */
@Controller
public class EnvDataBaseController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvDataBaseController.class);

    @Autowired private EnvDataBaseService envDataBaseService;

    /**
     * - [환경설정>DB 관리] DB 관리 - 목록 <br/>
     * - URL : /env/database.do <br/>
     * - JSP : /env/database.jsp <br/>
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/database.do" , method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list() throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            returnData.put("databaseList", envDataBaseService.selectDataBaseList());
            return new ModelAndView("/env/database", returnData);  // /env/env_database
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [환경설정>DB 관리] DB 관리 - 저장(추가)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/insertDatabase.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView insert(HttpServletRequest request) throws Exception {
        try {
            DbInfoVo dbInfo = new DbInfoVo();
            dbInfo.setDbInfoSeq(ServletRequestUtils.getIntParameter(request, "dbInfoSeq", -1));
            dbInfo.setServerNm(ServletRequestUtils.getStringParameter(request, "serverNm"));
            dbInfo.setDriverNm(ServletRequestUtils.getStringParameter(request, "driverNm"));
            dbInfo.setDriverDsn(ServletRequestUtils.getStringParameter(request, "driverDsn"));
            dbInfo.setDbUserId(ServletRequestUtils.getStringParameter(request, "dbUserId"));
            dbInfo.setDbPassword(ServletRequestUtils.getStringParameter(request, "dbPassword"));
            dbInfo.setDecoding(ServletRequestUtils.getStringParameter(request, "de"));
            dbInfo.setEncoding(ServletRequestUtils.getStringParameter(request, "en"));
            dbInfo.setDbKind(ServletRequestUtils.getStringParameter(request, "dbKind"));
            dbInfo.setTestQuery(ServletRequestUtils.getStringParameter(request, "testQuery"));

            envDataBaseService.insertDataBaseList(dbInfo);

            return new ModelAndView(new RedirectView("/env/database.do"));
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * - [환경설정>DB 관리] DB 관리 - DB 체크
     *
     * @param driverNm
     * @param driverDsn
     * @param dbUserId
     * @param dbPass
     * @param testQuery
     * @return
     */
    @RequestMapping(value = "/env/checkDatabase.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto checkDatabase(String driverNm, String driverDsn, String dbUserId, String dbPassword, String testQuery) {
        try {
            return envDataBaseService.dbChk(driverNm, driverDsn, dbUserId, dbPassword, testQuery);
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

    /**
     * - [환경설정>DB 관리] DB 관리 - 저장(변경)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateDatabase.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView update(HttpServletRequest request) throws Exception {
        try {
            DbInfoVo dbInfo = new DbInfoVo();
            dbInfo.setDbInfoSeq(ServletRequestUtils.getIntParameter(request, "dbInfoSeq", -1));
            dbInfo.setServerNm(ServletRequestUtils.getStringParameter(request, "serverNm"));
            dbInfo.setDriverNm(ServletRequestUtils.getStringParameter(request, "driverNm"));
            dbInfo.setDriverDsn(ServletRequestUtils.getStringParameter(request, "driverDsn"));
            dbInfo.setDbUserId(ServletRequestUtils.getStringParameter(request, "dbUserId"));
            dbInfo.setDbPassword(ServletRequestUtils.getStringParameter(request, "dbPassword"));
            dbInfo.setDecoding(ServletRequestUtils.getStringParameter(request, "de"));
            dbInfo.setEncoding(ServletRequestUtils.getStringParameter(request, "en"));
            dbInfo.setDbKind(ServletRequestUtils.getStringParameter(request, "dbKind"));
            dbInfo.setTestQuery(ServletRequestUtils.getStringParameter(request, "testQuery"));

            if(log.isDebugEnabled())
                log.debug("dbSeq : " + dbInfo.getDbInfoSeq());

            envDataBaseService.updateDataBaseList(dbInfo);

            return new ModelAndView(new RedirectView("/env/database.do"));
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [환경설정>DB 관리] DB 관리 - 삭제
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/deleteDatabase.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView delete(HttpServletRequest request) throws Exception {
        try {
            DbInfoVo dbInfo = new DbInfoVo();
            dbInfo.setDbInfoSeq(ServletRequestUtils.getIntParameter(request, "dbInfoSeq", -1));
            dbInfo.setServerNm(ServletRequestUtils.getStringParameter(request, "serverNm"));
            dbInfo.setDriverNm(ServletRequestUtils.getStringParameter(request, "driverNm"));
            dbInfo.setDriverDsn(ServletRequestUtils.getStringParameter(request, "driverDsn"));
            dbInfo.setDbUserId(ServletRequestUtils.getStringParameter(request, "dbUserId"));
            dbInfo.setDbPassword(ServletRequestUtils.getStringParameter(request, "dbPassword"));
            dbInfo.setDecoding(ServletRequestUtils.getStringParameter(request, "de"));
            dbInfo.setEncoding(ServletRequestUtils.getStringParameter(request, "en"));
            dbInfo.setDbKind(ServletRequestUtils.getStringParameter(request, "dbKind"));
            dbInfo.setTestQuery(ServletRequestUtils.getStringParameter(request, "testQuery"));

            if(log.isDebugEnabled())
                log.debug("dbSeq : " + dbInfo.getDbInfoSeq());

            int deleteCount = envDataBaseService.deleteDataBaseList(dbInfo);

            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("deleteCount", deleteCount);

            return new ModelAndView(new RedirectView("/env/database.do"), returnMap);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
