package com.mnwise.wiseu.web.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;

/**
 * BSE에서 생성한 CSV 파일을 다운로드 받기 위한 컨트롤러
 *
 * 씨티은행 버젼에서는 파일을 미리 생성하지 않고 아이콘 클릭시 바로 생성해서 다운로드 받는 방식으로 변경되었다.
 */
@Controller
public class FileDownloadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FileDownloadController.class);

    @RequestMapping(value = "/common/file_download.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ModelAndView view = null;
            String type = ServletRequestUtils.getStringParameter(request, "type");
            if(type != null) {
                if(type.equals("segment")) {
                    String segmentNo = ServletRequestUtils.getStringParameter(request, "segmentNo");
                    int dirNum = Integer.parseInt(segmentNo) / 100;
                    String fileDir = super.importUploadDir + "/" + dirNum + "/" + segmentNo + ".csv";
                    File uFile = new File(StringUtil.escapeFilePath(fileDir));
                    String fileName = URLEncoder.encode(FilenameUtils.getName(fileDir), "UTF-8");

                    int fSize = (int) uFile.length();
                    if(fSize > 0 && uFile != null && uFile.isFile() && uFile.exists() && uFile.canRead()) {
                        response.setContentType("application/download");
                        response.setHeader("Pragma", "public");
                        response.setHeader("Cache-Control", "max-age=0");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                        response.setContentLength(fSize);
                        byte[] fileBytes = getBytesFromFile(uFile);
                        FileCopyUtils.copy(fileBytes, response.getOutputStream());
                        IOUtil.closeQuietly(response.getOutputStream());
                        return null;
                    } else {
                        String message = "파일 " + fileName + "을(를) 찾을 수 없습니다.";
                        view = new ModelAndView("viewfile", "message", message);
                    }
                    response.setContentType("text/html; charset=euc-kr");
                    response.getWriter().write("<script>alert('파일이 없습니다.'); history.back();</script>");
                    view = null;
                } else if(type.equals("importFile")) { // 위에거랑 같은 걸 하는데..음 나중에 없애는 걸로 하자.
                    String fileDir = ServletRequestUtils.getStringParameter(request, "fileDir");
                    File uFile = new File(StringUtil.escapeFilePath(fileDir));
                    String fileName = URLEncoder.encode(FilenameUtils.getName(fileDir), "UTF-8");

                    int fSize = (int) uFile.length();
                    if(fSize > 0 && uFile != null && uFile.isFile() && uFile.exists() && uFile.canRead()) {

                        response.setContentType("application/download");
                        response.setHeader("Pragma", "public");
                        response.setHeader("Cache-Control", "max-age=0");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                        response.setContentLength(fSize);
                        byte[] fileBytes = getBytesFromFile(uFile);
                        FileCopyUtils.copy(fileBytes, response.getOutputStream());
                        IOUtil.closeQuietly(response.getOutputStream());
                        return null;
                    } else {
                        String message = "파일 " + fileName + "을(를) 찾을 수 없습니다.";
                        view = new ModelAndView("viewfile", "message", message);
                    }
                    response.setContentType("text/html; charset=euc-kr");
                    response.getWriter().write("<script>alert('파일이 없습니다.'); history.back();</script>");
                    view = null;

                } else {
                    response.setContentType("text/html; charset=euc-kr");
                    response.getWriter().write("<script>alert('파일 다운로드 옵션이 잘못 되었습니다.'); history.back();</script>");
                    view = null;
                }
            }
            return view;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    private byte[] getBytesFromFile(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            int len = in.available();
            byte bytes[] = new byte[len];

            in.read(bytes);

            return bytes;
        } catch(Exception e) {
            return null;
        } finally {
            IOUtil.closeQuietly(in);
        }
    }
}
