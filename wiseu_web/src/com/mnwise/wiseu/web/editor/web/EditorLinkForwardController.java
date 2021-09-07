package com.mnwise.wiseu.web.editor.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;

@Controller
public class EditorLinkForwardController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorLinkForwardController.class);

    /**
     * - [공통팝업>불러오기] 하이퍼링크 (팝업)<br/>
     * - [공통팝업>불러오기] 링크추적 (팝업)<br/>
     * - URL : /editor/linkPopup.do <br/>
     * - JSP : /editor/linkPopup_hyperlink.jsp <br/>
     * - JSP : /editor/linkPopup_linktrace.jsp <br/>
     * 에디터에서 불러오는 페이지를 관리한다. MessageSource를 얻기 위해 MultiActionController를 상속함.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editor/linkPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /editor/editor_link.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            String type = ServletRequestUtils.getStringParameter(request, "type");
            String hyperlink = ServletRequestUtils.getStringParameter(request, "hyperlink");
            //A/B 기능 (템플릿) 사용시 구분
            String templateType = ServletRequestUtils.getStringParameter(request, "templateType", "");

            Map<String, String> returnData = new HashMap<>();
            String forwardJsp = null;
            if(type.equals("hyperlink")) {
                forwardJsp = "editor/linkPopup_hyperlink";  // editor/hyperlink_editor
            } else if(type.equals("linktrace")) {
                 forwardJsp = "editor/linkPopup_linktrace";  // editor/linktrace_editor
            }

            returnData.put("hyperlink", hyperlink);
            returnData.put("templateType", templateType);
            return new ModelAndView(forwardJsp, returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
