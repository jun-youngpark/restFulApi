package com.mnwise.wiseu.web.report.web.campaign;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCommonService;

/**
 * 템플릿 보기 - 리포트 상세조회 화면에서 템플릿 보기 버튼을 눌러서 실행한다.
 */
@Controller
public class TemplateViewController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TemplateViewController.class);

    @Autowired private CampaignCommonService campaignCommonService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 템플릿 보기(팝업)<br/>
     * - JSP : /report/campaign/template_view.jsp <br/>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/report/campaign/templateViewPopup.do", method = {RequestMethod.GET, RequestMethod.POST})  // /report/campaign/template_view.do
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            TemplateVo templateVo = new TemplateVo();
            BeanUtils.populate(templateVo, request.getParameterMap());

            templateVo = campaignCommonService.getTemplate(templateVo);

            Map<String, Object> returnData = new HashMap<>();

            if(templateVo == null) {
                templateVo = new TemplateVo();
                templateVo.setTemplate("<p>&nbsp;</p><b>" + messageSourceAccessor.getMessage("report.campaign.head.template") + "</b>");
            }

            // 저작기 수신확인, 링크추적, 수신거부 관련 공통 코드 제거 wiseU5.5 부터 저작기의 수신확인, 링크추적, 수신거부 관련 표시 방식이 변경되었다.
            // 따라서 개인화 정보만 변환작업을 진행한다.
            returnData.put("template", templateVo.getTemplate());

            return new ModelAndView("report/campaign/templateViewPopup", returnData);  // report/campaign/template_view
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
