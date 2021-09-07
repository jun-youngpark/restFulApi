package com.mnwise.wiseu.web.segment.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;

@Controller
public class UploadResultController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UploadResultController.class);

    /**
     * - [대상자 관리>대상자파일 올리기] 대상자 등록결과확인 <br/>
     * - URL : /segment/fileSegment3Step.do <br/>
     * - JSP : /segment/upload_result.jsp <br/>
     */
    // /segment/target/upload_result.do, /segment/upload/upload_result.do
    @RequestMapping(value = {"/target/fileSegment3Step.do", "/segment/fileSegment3Step.do"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView resultUpload(String segmentNo, String segmentNm, String segmentSts, String segmentSize, String totalLine,
                                     String insertedErrFilePath, String reject, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("segment/fileSegment3Step");  // segment/upload/upload_result
            mav.addObject("segmentNo", segmentNo);
            mav.addObject("segmentNm", segmentNm);
            mav.addObject("segmentSts", segmentSts);
            mav.addObject("segmentSize", segmentSize);
            mav.addObject("totalLine", totalLine);
            mav.addObject("insertedErrFilePath", insertedErrFilePath);
            mav.addObject("reject", reject);

            if(request.getRequestURI().indexOf("target") > -1) {
                mav.addObject("uploadUrl", "true");
            }

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
