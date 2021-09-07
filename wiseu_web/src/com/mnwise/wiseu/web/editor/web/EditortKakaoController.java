package com.mnwise.wiseu.web.editor.web;


import static org.springframework.web.bind.ServletRequestUtils.getIntParameter;
import static org.springframework.web.bind.ServletRequestUtils.getStringParameter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.service.EditorKakaoService;
import com.mnwise.wiseu.web.template.model.MobileVo;
import com.mnwise.wiseu.web.template.service.BrandtalkTemplateService;
/**
 * 카카오(알림톡,친구톡,브랜드톡) 저작기 구현
 *
 */
@Controller
public class EditortKakaoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BrandtalkTemplateService.class);

    @Autowired private EditorKakaoService editorKakaoService;

    /**
     * 카카오(알림톡,친구톡,브랜드톡) 저작기 상세 보기
     * 템플릿 리스트 불러오기
     * @throws ServletRequestBindingException
     *
     */
    @RequestMapping(value="/editor/brtContentView.do", method={RequestMethod.GET, RequestMethod.POST})  // /editor/kakaoContentView.do
    public ModelAndView view(HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            try {
                MobileVo kakaoVo = new MobileVo();
                kakaoVo.setUserVo(getLoginUserVo());
                kakaoVo.setUserId(getLoginId());
                kakaoVo.setFileType(getStringParameter(request,"fileType"));
                kakaoVo.setContsNo(getIntParameter(request,"contsNo"));
                returnData.put("kakaoContent", editorKakaoService.selectKakaoContent(kakaoVo));
                returnData.put("fileType", kakaoVo.getFileType());
                returnData.put("userId", kakaoVo.getUserVo().getUserId());
                returnData.put("actionPath", request.getRequestURI());
            } catch(Exception e) {
                   log.error(e.getMessage());
            }
            return new ModelAndView("/editor/brtContentView", returnData);  // /editor/kakaoContentView
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 카카오(알림톡,친구톡,브랜드톡) 저작기 리스트
     * @throws ServletRequestBindingException
     *
     */
    @RequestMapping(value="/editor/kkoContsList.do", method={RequestMethod.GET, RequestMethod.POST})  // /editor/kakaoContentsList.do
    public ModelAndView list(MobileVo kakaoVo, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            String viewName ="/editor/altContsList";
            kakaoVo.setUserVo(getLoginUserVo());
            kakaoVo.setUserId(getLoginId());
            String fileType = kakaoVo.getFileType();
            PagingUtil.setPagingInfo(request, kakaoVo);
            try {

                int kakaoContentsListTotalCount = editorKakaoService.selectKakaoContentsListMax(kakaoVo);
                returnData.put("kakaoContentsListTotalCount", String.valueOf(kakaoContentsListTotalCount));
                returnData.put("kakaoContentsList", editorKakaoService.selectKakaoContentsList(kakaoVo));
                returnData.put("actionPath", request.getRequestURI());
                returnData.put("userId", kakaoVo.getUserId());
                PagingUtil.transferPagingInfo(request, kakaoContentsListTotalCount);
                if(Channel.ALIMTALK.equalsIgnoreCase(fileType)) {
                    viewName ="/editor/altContsList";
                }else if(Channel.BRANDTALK.equalsIgnoreCase(fileType)) {
                    viewName ="/editor/brtContsList";
                }else if(Channel.FRIENDTALK.equalsIgnoreCase(fileType)) {
                    viewName ="/editor/frtContsList";
                }

            } catch(Exception e) {
                log.error(e.getMessage());
            }
            return new ModelAndView(viewName, returnData);  // /editor/kakaoContentsList
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

}
