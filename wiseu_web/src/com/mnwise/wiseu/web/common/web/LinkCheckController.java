package com.mnwise.wiseu.web.common.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.util.LinkExtractor;

/**
 * 링크유효성 검사 Controller
 */
@Controller
public class LinkCheckController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(LinkCheckController.class);

    /**
     * 템플릿 본문 안에 있는 링크 클릭의 유효성을 검사한다.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/common/linkCheckPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /common/linkcheck_popup.do
    public ModelAndView checkLinkValidation(HttpServletRequest request) throws Exception {
        try {
            String imsiTemplate = ServletRequestUtils.getStringParameter(request, "imsiTemplate");
            String[] errCode = {
                "404", "500"
            };
            ModelAndView mav = new ModelAndView();
            mav.addObject("linkList", linkExtract(imsiTemplate, errCode));
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 템플릿 본문 안에 있는 링크 클릭의 유효성을 검사한다.
     *
     * @param template
     * @param errCode
     * @return
     */
    public Map<String, Object> linkExtract(String template, String[] errCode) {
        if(template == null)
            return null;
        LinkExtractor extractor = new LinkExtractor();
        return extractor.linkExtractFromString(StringUtil.unescapeXss(template), errCode);
    }
}
