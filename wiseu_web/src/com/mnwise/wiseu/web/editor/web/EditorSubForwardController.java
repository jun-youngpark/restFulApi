package com.mnwise.wiseu.web.editor.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.net.HttpUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;

@Controller
public class EditorSubForwardController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorSubForwardController.class);

    @Autowired private MessageSourceAccessor messageSourceAccessor;

    /**
     * - [공통팝업>불러오기] ZIP 파일 불러오기 (팝업)
     * - [공통팝업>불러오기] HTML 파일 불러오기 (팝업)
     * - [공통팝업>불러오기] 파일 첨부하기 (팝업)
     * - [공통팝업>불러오기] URL 불러오기 (팝업)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editor/importPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /editor/editor_sub.do
    public ModelAndView list(HttpServletRequest request) throws Exception {
        try {
            String type = ServletRequestUtils.getStringParameter(request, "type");
            String no = ServletRequestUtils.getStringParameter(request, "no");
            // 에디터에서 요청 하는 페이지 정보
            String uploadType = ServletRequestUtils.getRequiredStringParameter(request, "uploadType");
            String name = "";
            //A/B 기능 (템플릿) 사용시 구분
            String templateType = ServletRequestUtils.getStringParameter(request, "templateType", "");

            Map<String, String> returnData = new HashMap<>();
            String forwardJsp = null;
            if(uploadType.equals("template")) {
                forwardJsp = "editor/importPopup_file";  // editor/upload_editor
                name = messageSourceAccessor.getMessage("editor.import.zip");
            } else if(uploadType.equals("html")) {
                forwardJsp = "editor/importPopup_file";  // editor/upload_editor
                name = messageSourceAccessor.getMessage("editor.import.html");
            } else if(uploadType.equals("file")) {
                forwardJsp = "editor/importPopup_file";  // editor/upload_editor
                name = messageSourceAccessor.getMessage("editor.import.attach");
            } else if(uploadType.equals("url")) {
                forwardJsp = "editor/importPopup_url";  // editor/url_editor
            }else if(uploadType.equals("upload")) {
                forwardJsp = "editor/importPopup_upload";  // 이케어 내 pc 파일 올리기
            }

            returnData.put("type", type);
            returnData.put("uploadType", uploadType);
            returnData.put("no", no);
            returnData.put("name", name);
            returnData.put("templateType", templateType);

            return new ModelAndView(forwardJsp, returnData);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계>불러오기>URL 불러오기(팝업)] 입력된 URL의 HTML을 읽어와 저작기에 삽입
     *
     * @param web
     * @param templateType
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editor/url_template.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView onSubmit(String web, String templateType, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("editor/importPopup_url");  // editor/url_editor
            mav.addObject("templateType", templateType);

            String template = " ";
            if(web.matches("^\\s*http.+htm[l]?\\s*")) {
                template = HttpUtil.getUrlContent(web);
            }
            mav.addObject("template", template);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
