package com.mnwise.wiseu.web.editor.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;

/**
 * 링크추적리스트 Controller
 */
@Controller
public class EditorLinktraceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorLinktraceController.class);

    /**
     * [캠페인>캠페인 등록>2단계] 저작기>링크>링크추적 클릭 - 링크추적리스트(팝업) 화면 출력
     *
     * @param templateType
     * @param template
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editor/linkTraceList.do", method = {RequestMethod.GET, RequestMethod.POST})  // /editor/linktrace_template.do
    public ModelAndView onSubmit(String templateType, String template) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("editor/linkTraceList");  // editor/linktrace_template
            mav.addObject("templateType", templateType);
            mav.addObject("template", StringUtil.unescapeXss(template)); // 템플릿
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
