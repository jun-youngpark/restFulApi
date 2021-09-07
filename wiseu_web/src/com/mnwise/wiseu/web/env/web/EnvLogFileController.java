package com.mnwise.wiseu.web.env.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.FileUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.service.EnvLogFileService;
import com.mnwise.wiseu.web.env.util.LogHomeUtil;

/**
 * 로그 관리 Controller
 */
@Controller
public class EnvLogFileController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvLogFileController.class);

    @Autowired private EnvLogFileService envLogFileService;

    /**
     * [환경설정>로그 관리] 디렉터리와 파일 목록 출력
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/env/logFileList.do", method = {RequestMethod.GET, RequestMethod.POST})  // /env/env_logfiledown.do
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            final String selectedPaths = StringUtil.escapeFilePath(ServletRequestUtils.getStringParameter(request, "selectedPaths"));

            if(StringUtil.isNotEmpty(selectedPaths) && !LogHomeUtil.startsWithLogHomeDir(selectedPaths)) {
                response.setContentType("text/html; charset=euc-kr");
                response.getWriter().write("<script>alert('<spring:message code='env.msg.nopathfound'/>'); history.back();</script>");
                return null;
            }

            // /env/env_logfiledown
            return new ModelAndView("/env/logFileList", envLogFileService.getDirFileList(ServletRequestUtils.getIntParameter(request, "selectedIndex", 0), selectedPaths.replace("\\", "/")));
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>로그 관리] zip 파일로 압축하여 다운로드
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/env/downloadLogFile.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView downloadLogFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String selectedPaths = StringUtil.escapeFilePath(ServletRequestUtils.getStringParameter(request, "selectedPaths"));
            if(!LogHomeUtil.startsWithLogHomeDir(selectedPaths)) {
                response.setContentType("text/html; charset=euc-kr");
                response.getWriter().write("<script>alert('<spring:message code='env.msg.downloadfail'/>'); history.back();</script>");
                return null;
            }

            // 다중 파일 압축
            File zipDir = new File(System.getProperty("wiseu.home") + "/upload/download/log/" + DateUtil.dateToString("yyyyMMddhhmmss", new Date()) + "/");
            File zipFile = envLogFileService.downloadZip(selectedPaths, zipDir);

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFile.getName() + "\"");
            response.setContentLength((int) zipFile.length());

            OutputStream out = null;
            FileInputStream in = null;

            try {
                in = new FileInputStream(zipFile);
                out = response.getOutputStream();
                IOUtil.copyLarge(in, out);
            } catch(Exception e) {
                throw e;
            } finally {
                IOUtil.closeQuietly(in);
                IOUtil.closeQuietly(out);
                try {
                    FileUtil.forceDelete(zipFile);
                } catch(Exception e) {
                    log.debug("zipEntry Exception>>" + e);
                }

                try {
                    FileUtil.deleteDirectory(zipDir);
                } catch(Exception e) {
                    log.debug("zipEntry Exception>>" + e);
                }
            }
            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>로그 관리] 로그파일 미리보기 (팝업)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/env/logFileViewPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /env/previewLogFile.do
    public ModelAndView previewLogFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            final String selectedPaths = StringUtil.escapeFilePath(ServletRequestUtils.getStringParameter(request, "selectedPaths"));
            if(!LogHomeUtil.startsWithLogHomeDir(selectedPaths)) {
                response.setContentType("text/html; charset=euc-kr");
                response.getWriter().write("<script>alert('<spring:message code='env.msg.downloadfail'/>'); history.back();</script>");
                return null;
            }

            Map<String, String> returnData = new HashMap<>();
            returnData.put("folderPath", selectedPaths);
            returnData.put("fileContent", FileUtil.readFileToString(new File(selectedPaths)));

            return new ModelAndView("/env/logFileViewPopup", returnData);  // /env/env_logfilepreview
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}