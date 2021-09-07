package com.mnwise.wiseu.web.segment.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;

@Controller
public class SegmentAddTabController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SegmentAddTabController.class);

    /**
     * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)] 파일올리기/반응대상자 탭
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/segment/segment_addtab_include.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addSegmentTab(String viewPath, String tabUse, String tabType, String segmentNo) throws Exception {
        try {
            ModelAndView mav = new ModelAndView(viewPath);
            mav.addObject("tabUse", tabUse);
            mav.addObject("tabType", tabType);
            mav.addObject("segmentNo", segmentNo);
            mav.addObject("indexUrl", getLoginSessionVo().getIndexUrl());
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
