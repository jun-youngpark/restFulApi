package com.mnwise.wiseu.web.report.web.ecare;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.report.service.ecare.EcareCommonService;

/**
 * 이케어 리포트 발송 로그 다운로드
 */
@Controller
public class EcareReportCsvDownloadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareReportCsvDownloadController.class);

    @Autowired private EcareCommonService ecareCommonService;

    /**
     * - [리포트>이케어>이케어 리스트] 이케어 리포트 - 요약분석 - 다운로드(성공,실패)<br/>
     * - URL : /report/ecare/ecare_report_csv_download.do <br/>
     *
     * @throws Exception
     */
    @RequestMapping(value = "/report/ecare/ecare_report_csv_download.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int ecareNo = Integer.parseInt(StringUtil.defaultString(request.getParameter("ecareNo"), "0"));
            String reportDt = StringUtil.defaultString(request.getParameter("reportDt"), "");
            String resultSeq = StringUtil.defaultString(request.getParameter("resultSeq"), "");
            String serviceType = StringUtil.defaultString(request.getParameter("serviceType"), "");
            String subType = StringUtil.defaultString(request.getParameter("subType"), "");
            String channelType = StringUtil.defaultString(request.getParameter("channelType"), "");

            if(ecareNo == 0) {
                return null;
            }

            String mode = StringUtil.defaultString(request.getParameter("mode"), "send_success");

            String fileName = mode + "_" + DateUtil.dateToString("yyyyMMddhhmmss", new Date()) + ".csv";

            response.setContentType("application/download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // response.setContentLength((int) file.length());

            ecareCommonService.makeCsvEcareReportLog(response.getOutputStream(), mode, ecareNo, reportDt, resultSeq, serviceType, subType, channelType);

            return null;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
