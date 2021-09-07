package com.mnwise.wiseu.web.report.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.common.util.PropertyUtil;

import net.sf.jxls.transformer.XLSTransformer;

public class ReportExcelDownload {
    private static final Logger log = LoggerFactory.getLogger(ReportExcelDownload.class);

    @SuppressWarnings("rawtypes")
    public void makeExcel(HttpServletResponse response, Map returnData, String service, String fileName) throws Exception {
        XLSTransformer transformer = new XLSTransformer();
        InputStream in = null;
        HSSFWorkbook workbook = null;
        try {
            in = new FileInputStream(StringUtil.escapeFilePath(PropertyUtil.getProperty("web.root") + "/WEB-INF/report/template/" + service + "/" + fileName));
            try {
                workbook = (HSSFWorkbook) transformer.transformXLS(in, returnData);
            } catch(Exception e) {
                log.error(null, e);
            }
        } catch(Exception e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }
        response.setContentType("application/download");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        workbook.write(response.getOutputStream());
        return;
    }

    public void downLoadFile(HttpServletResponse response, String filePath) throws Exception {
        FileInputStream fis = null;
        OutputStream out = null;
        try {
            File file = new File(filePath);
            if(file.length()>0) {
                String fileName = new String(file.getName().getBytes("utf-8"));
                response.setContentLength((int)file.length());
                response.setContentType("application/download");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                out = response.getOutputStream();
                fis = new FileInputStream(file);
                FileCopyUtils.copy(fis, out);
            }else {
                throw new Exception("file size is 0 byte : "+ file.getName()) ;
            }
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        } finally {
            IOUtil.closeQuietly(fis);
        }
        out.flush();
        return;
    }
}
