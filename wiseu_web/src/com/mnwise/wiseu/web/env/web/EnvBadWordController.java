package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.BadWordVo;
import com.mnwise.wiseu.web.env.service.EnvBadWordService;

/**
 * 금칙어 관리 Controller
 */
@Controller
public class EnvBadWordController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvBadWordController.class);

    @Autowired private EnvBadWordService envBadWordService;

    /**
     * - [환경설정>금칙어 관리] 금칙어 관리 <br/>
     * - JSP : /env/badWord.jsp <br/>
     * 환경설정 - 금칙어 관리 데이터를 가져온다.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/badWord.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view() throws Exception {
        try {
            Map<String, BadWordVo> returnData = new HashMap<>();
            BadWordVo badWord = new BadWordVo();
            List<BadWordVo> badWordList = envBadWordService.selectBadWord();
            badWord.setChennelUseList(super.channelUseList);

            for(int i = 0; i < badWordList.size(); i++) {
                BadWordVo tmp = (BadWordVo) badWordList.get(i);
                if(tmp.getChannelType().equalsIgnoreCase("M")) {
                    badWord.setMailBadWord(tmp.getBadWords());
                } else {
                    badWord.setSmsBadWord(tmp.getBadWords());
                }
            }

            returnData.put("envBadWordVo", badWord);

            return new ModelAndView("/env/badWord", returnData);  // /env/env_badword
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [환경설정>금칙어 관리] 금칙어 관리 - 저장<br/>
     *
     * @param mailBadWord
     * @param smsBadWord
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateBadWord.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView update(@RequestParam(value="mailBadWord", defaultValue="") String mailBadWord,
                               @RequestParam(value="smsBadWord", defaultValue="") String smsBadWord) throws Exception {
        try {
            BadWordVo badWord = new BadWordVo();

            badWord.setBadWords(mailBadWord);
            badWord.setChannelType("M");
            envBadWordService.updateBadWord(badWord);

            badWord.setBadWords(smsBadWord);
            badWord.setChannelType("S,T,P");
            envBadWordService.updateBadWord(badWord);

            return new ModelAndView(new org.springframework.web.servlet.view.RedirectView("/env/badWord.do"));
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
