package com.mnwise.wiseu.web.segment.web;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.upload.formobjects.DataImportForm;
import com.mnwise.wiseu.web.common.upload.resolver.AjaxFileUploadMultipartResolver;
import com.mnwise.wiseu.web.template.service.MobileTemplateService;

/**
 * 대상자 파일 올리기 1단계 Controller
 */
@Controller
public class UploadFilesController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UploadFilesController.class);

    @Autowired private MobileTemplateService mobileTemplateService;
    @Autowired private AjaxFileUploadMultipartResolver customMultipartResolver;

    /**
     * [대상자 관리>대상자파일 올리기>1단계] 대상자파일 올리기 화면 출력
     * [공통팝업>대상자관리>1단계] 대상자 등록 (팝업) 화면 출력
     *
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/target/upload.do, /segment/upload/upload.do
    @RequestMapping(value={"/target/fileSegment1Step.do", "/segment/fileSegment1Step.do"}, method=RequestMethod.GET)
    public ModelAndView referenceData(String tabUse, String tabType, HttpServletRequest request) throws Exception {
        try {
            if(super.webExecMode.equals("2")) {
                tabUse = "N";
            }

            double uploadSizeMax = customMultipartResolver.getFileUpload().getSizeMax();

            ModelAndView mav = new ModelAndView("/segment/fileSegment1Step");  // /segment/upload/upload
            mav.addObject("uploadSizeMax", String.valueOf(uploadSizeMax));
            mav.addObject("tabUse", tabUse);
            mav.addObject("tabType", tabType);
            mav.addObject("command", new DataImportForm());

            if(request.getRequestURI().indexOf("target") > -1) {
                mav.addObject("action", "/target/fileSegment1Step.do");  // /segment/target/upload.do
            } else {
                mav.addObject("action", "/segment/fileSegment1Step.do");  // /segment/upload/upload.do
            }

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자 관리>대상자파일 올리기>1단계] 대상자파일 올리기 - 다음단계 버튼 클릭
     * [공통팝업>대상자관리>1단계] 대상자 등록 (팝업) - 다음단계 버튼 클릭
     *
     * @param request
     * @param response
     * @param errors
     * @return
     * @throws Exception
     */
    // /segment/target/upload.do, /segment/upload/upload.do
    @RequestMapping(value={"/target/fileSegment1Step.do", "/segment/fileSegment1Step.do"}, method=RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute("command") DataImportForm dataImportForm, HttpServletRequest request, BindingResult errors) throws Exception {
        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = customMultipartResolver.resolveMultipart(request);
        } catch(MaxUploadSizeExceededException e) {
            log.error("업로드 파일 사이즈 초과!");
            errors.rejectValue("file", "errors.upload.size.exceeded", null, "bundle errors.upload.size.exceeded not found");
            return new ModelAndView("segment/fileSegment1Step");  // segment/upload/upload
        }

        if(StringUtil.isNotBlank(multipartRequest.getParameter("cancel"))) {
            log.info("Canceling operation.");
            return new ModelAndView("segment/fileSegment1Step");  // segment/upload/upload
        }

        MultipartFile file = multipartRequest.getFile("file");
        if(file.getSize() == 0) {
            log.error("파일 사이즈가 0입니다.");
            errors.rejectValue("file", "errors.upload.size.zero", null, "bundle errors.upload.size.zero not found");
            return new ModelAndView("segment/fileSegment1Step");  // segment/upload/upload
        }

        try {
            String inMetaData = multipartRequest.getParameter("inMetaData");
            String delimiter = multipartRequest.getParameter("delimiter");
            String vmail = multipartRequest.getParameter("vmail");
            String vphone = multipartRequest.getParameter("vphone");
            String reject = multipartRequest.getParameter("reject");
            String tabUse = multipartRequest.getParameter("tabUse");

            if(StringUtil.equals(delimiter, "etc")) {
                delimiter = multipartRequest.getParameter("etcDelimiter");
            }

            // 동시에 같은 파일명을 올리는 경우를 대비해 업로드한 파일명을 현재시간값을 앞에 붙여서 저장
            String startTime = DateUtil.getNowDateTime("hhmmssSSS");
            String fileName = "CSVcopy_" + startTime + "_" + file.getOriginalFilename();
            mobileTemplateService.saveFile(file.getInputStream(), fileName, "");

            Map<String, String> returnData = new HashMap<>();
            returnData.put("importedFilePath", URLEncoder.encode(mobileTemplateService.getImportedFilePath(fileName), "UTF-8"));
            returnData.put("inMetaData", inMetaData);
            returnData.put("delimiter", delimiter);
            returnData.put("vmail", vmail);
            returnData.put("vphone", vphone);
            returnData.put("reject", reject);
            returnData.put("tabUse", tabUse);

            if(request.getRequestURI().indexOf("target") > -1) {
                if(file.getOriginalFilename().endsWith(".xls")) {
                    returnData.put("submitPage", "/target/fileSegment2Step_xls.do");  // /segment/target/excelimport.do
                } else if(file.getOriginalFilename().endsWith(".xlsx")) {
                    returnData.put("submitPage", "/target/fileSegment2Step_xlsx.do");  // /segment/target/xlsximport.do
                } else {
                    returnData.put("submitPage", "/target/fileSegment2Step_csv.do");  // /segment/target/csvimport.do
                }
            } else {
                if(file.getOriginalFilename().endsWith(".xls")) {
                    returnData.put("submitPage", "/segment/fileSegment2Step_xls.do");  // /segment/upload/excelimport.do
                } else if(file.getOriginalFilename().endsWith(".xlsx")) {
                    returnData.put("submitPage", "/segment/fileSegment2Step_xlsx.do");  // /segment/upload/xlsximport.do
                } else {
                    returnData.put("submitPage", "/segment/fileSegment2Step_csv.do");  // /segment/upload/csvimport.do
                }
            }

            return new ModelAndView(new RedirectView(returnData.get("submitPage")), returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
