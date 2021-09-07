package com.mnwise.wiseu.web.template.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.template.model.ContentVo;
import com.mnwise.wiseu.web.template.service.TemplateService;

@Controller
public class TemplateListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TemplateListController.class);

    @Autowired private TemplateService templateService;

    /**
     * - [템플릿>템플릿 리스트] 템플릿 리스트 <br/>
     * - JSP : /template/templateList.jsp <br/>
     *
     * - [캠페인>캠페인 등록>2단계] 템플릿 불러오기 (팝업) - 템플릿 리스트<br/>
     * - JSP : /editor/template_editor.jsp <br/>
     *
     * @param templateType 캠페인 저작기 화면에서  A/B 기능 (템플릿) 사용시 구분
     */
    // /template/template.do
    @RequestMapping(value={"/template/templateList.do", "/editor/importTemplateListPopup.do"}, method={RequestMethod.GET, RequestMethod.POST})  // /template/templateList.do, /editor/template_editor.do
    public ModelAndView list(@RequestParam(defaultValue="") String searchWord, @RequestParam(defaultValue="-1") int tagNo, @RequestParam(defaultValue="") String templateType,
                             @RequestParam(defaultValue="") String searchQstartDt, @RequestParam(defaultValue="") String searchQendDt, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();

            ContentVo contentVo = new ContentVo();
            contentVo.setUserVo(userVo);
            contentVo.setCountPerPage(userVo.getListCountPerPage());
            contentVo.setEndRow(userVo.getListCountPerPage() * contentVo.getNowPage());
            contentVo.setTagNo(tagNo);
            contentVo.setUserId(userVo.getUserId());
            contentVo.setContsNm(searchWord);
            contentVo.setSearchWord(searchWord);
            contentVo.setSearchQstartDt(searchQstartDt);
            contentVo.setSearchQendDt(searchQendDt);
            PagingUtil.setPagingInfo(request, contentVo);
            PagingUtil.setPagingRowcount(contentVo);
            // 페이징을 위해 리스트의 TOTAL ROW 를 가져온다.
            int templateTotalCnt = templateService.getTemplateTotalCount(contentVo);
            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (templateTotalCnt / aCountPerPage); // 몫
            int rCountPerPage = (templateTotalCnt % aCountPerPage); // 나머지

            if(contentVo.getNowPage() <= qCountPerPage) {
                contentVo.setListCnt(userVo.getListCountPerPage());
            } else {
                contentVo.setListCnt(rCountPerPage);
            }

            List<ContentVo> templateList = null;
            // TOTAL COUNT 가 존재할 경우 리스트를 가져온다.
            if(templateTotalCnt > 0) {
                if(StringUtil.isEmpty(contentVo.getOrderColumn())) {
                    contentVo.setOrderColumn("CONTS_NO");
                    contentVo.setOrderSort("DESC");
                }

                templateList = templateService.getTemplateList(contentVo);
            }

            PagingUtil.transferPagingInfo(request, templateTotalCnt);


            ModelAndView mav = null;
            if("editor".equals(templateType) || "ab".equals(templateType)) {
                mav = new ModelAndView("editor/importTemplateListPopup");  // [캠페인>캠페인 등록>2단계] 템플릿 불러오기(팝업) - 템플릿 리스트 : editor/template_editor
            } else {
                mav = new ModelAndView("template/templateList");  // [템플릿>템플릿 리스트] 템플릿 리스트 : template/template_list
            }

            mav.addObject("templateTotalCount", templateTotalCnt);
            mav.addObject("templateList", templateList);
            mav.addObject("contentVo", contentVo);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [템플릿>템플릿 리스트] 템플릿 삭제
     *
     * @param contsNo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/template/deleteTemplate.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView delete(int contsNo, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView(new RedirectView("/template/templateList.do"));

            UserVo userVo = getLoginUserVo();

            ContentVo contentVo = new ContentVo();
            contentVo.setUserVo(userVo);
            contentVo.setContsNo(contsNo);

            // User Type Code가 '관리자(A)'일 경우 템플릿 삭제
            if(Const.USER_ROLE_ADMIN.equals(userVo.getUserTypeCd())) {
                templateService.setDeleteTemplate(contentVo);
            } else {
                ContentVo originalVo = templateService.getTemplateInfo(contsNo);

                // 세션에 저장된 계정과 템플릿 등록 계정이 동일할 경우 템플릿 삭제
                if(userVo.getUserId().equals(originalVo.getUserId())) {
                    templateService.setDeleteTemplate(contentVo);
                }
            }

            return mav;
        } catch(Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
