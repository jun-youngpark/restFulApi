package com.mnwise.wiseu.web.editor.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;

@Controller
public class EditorAreaPopupController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EditorAreaPopupController.class);

    @RequestMapping(value = "/editor/editAreaPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /editor/edit_area_popup.do
    public ModelAndView popupEditor() throws Exception {
        try {
            Map<String, String> returnData = new HashMap<>();
            returnData.put("lang", getLoginUserVo().getLanguage());
            return new ModelAndView("editor/editAreaPopup", returnData);  // editor/edit_area_popup
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
