package com.mnwise.wiseu.web.common.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.service.BadWordService;

/**
 * 캠페인/이케어 금칙어 검사 Controller
 */
@Controller
public class BadWordController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BadWordController.class);

    @Autowired private BadWordService badWordService;

    @RequestMapping(value = "/common/badWordCheckPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /common/badword_popup.do
    public ModelAndView checkBadword(HttpServletRequest request) throws Exception {
        try {
            String channelType = ServletRequestUtils.getStringParameter(request, "imsiChannelType");
            String imsiTemplate = ServletRequestUtils.getStringParameter(request, "imsiTemplate");

            String badWordsText = badWordService.getBadWordsInfo(channelType);
            String checkedBadWord = "";
            if(badWordsText != null && !badWordsText.equals("")) {
                checkedBadWord = isBadWord(badWordsText, imsiTemplate);

                if(checkedBadWord.lastIndexOf(",") > -1) {
                    checkedBadWord = checkedBadWord.substring(0, checkedBadWord.lastIndexOf(","));
                }
            }
            ModelAndView mav = new ModelAndView();
            mav.addObject("checkedBadWord", checkedBadWord);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    private String isBadWord(String badWords, String text) {
        List<String> badWordsList = StringUtil.splitToList(badWords, ",");
        // HTML 코드 제거
        String noHTMLString = text.replaceAll("\\<.*?\\>", "");
        // 영문자 제거 (다국어 처리를 위해 영문자 제거는 주석 처리함 - 한규현
        // noHTMLString = noHTMLString.replaceAll("([a-zA-Z])", "");
        String returnString = "";

        for(int i = 0, n = badWordsList.size(); i < n; i++) {
            String badWord = (String) badWordsList.get(i);
            if(StringUtil.contains(noHTMLString, badWord.trim())) {
                returnString += badWord + ",";
            }
        }
        return returnString;
    }
}
