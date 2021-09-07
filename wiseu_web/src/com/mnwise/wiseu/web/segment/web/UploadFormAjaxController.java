package com.mnwise.wiseu.web.segment.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.ui.upload.CSVInsertedCntInfo;
import com.mnwise.wiseu.web.common.ui.upload.UploadInfo;

/**
 * 파일업로드시 호출되는 Ajax Controller
 * 기존 dwr 메소드를 대체 구현
 */
@Controller
public class UploadFormAjaxController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UploadFormAjaxController.class);

    /**
     * [대상자 관리>대상자파일 올리기>1단계] 대상자파일 업로드시 프로그래스바에 진행상태를 출력
     * [공통팝업>대상자관리>1단계] 대상자 등록 (팝업) 업로드시 프로그래스바에 진행상태를 출력
     * [공통팝업>불러오기] ZIP 파일 불러오기 (팝업) 업로드시 프로그래스바에 진행상태를 출력
     * [공통팝업>불러오기] HTML 파일 불러오기 (팝업) 업로드시 프로그래스바에 진행상태를 출력
     * [공통팝업>불러오기] 파일 첨부하기 (팝업) 업로드시 프로그래스바에 진행상태를 출력
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/segment/getUploadInfo.json", method={RequestMethod.GET, RequestMethod.POST})  // /segment/upload/getUploadInfo.json
    @ResponseBody public UploadInfo getUploadInfo(HttpServletRequest request) {
        try {
            if(request.getSession().getAttribute("uploadInfo") != null) {
                return (UploadInfo) request.getSession().getAttribute("uploadInfo");
            }
        } catch (Exception e) {
            log.error(null, e);
        }

        return new UploadInfo();
    }

    /**
     * [대상자 관리>대상자파일 올리기>2단계] CSV 파일 업로드시 프로그래스바에 진행상태를 출력
     * [공통팝업>대상자관리>2단계] CSV 파일 업로드시 프로그래스바에 진행상태를 출력
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/segment/getInsertedCntInfo.json", method={RequestMethod.GET, RequestMethod.POST})  // /segment/upload/getInsertedCntInfo.json
    @ResponseBody public CSVInsertedCntInfo getInsertedCntInfo(HttpServletRequest request) {
        try {
            if(request.getSession().getAttribute("insertedCntInfo") != null) {
                return (CSVInsertedCntInfo) request.getSession().getAttribute("insertedCntInfo");
            }
        } catch (Exception e) {
            log.error(null, e);
        }

        return new CSVInsertedCntInfo();
    }
}
