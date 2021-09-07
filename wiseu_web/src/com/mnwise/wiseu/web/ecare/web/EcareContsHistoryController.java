package com.mnwise.wiseu.web.ecare.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.ecare.model.EcareContsHistoryVo;
import com.mnwise.wiseu.web.ecare.service.EcareContsHistoryService;

/**
 * 이케어 컨텐츠 변경이력 Controller
 */
@Controller
public class EcareContsHistoryController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareContsHistoryController.class);

    @Autowired private EcareContsHistoryService ecareContsHistoryService;

    /**
     * [이케어>컨텐츠 변경이력] 컨텐츠 변경이력
     *
     * @param contentRadio
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/contsHistoryList.do", method={RequestMethod.GET, RequestMethod.POST})  // /ecare/ecare_conts_history.do
    public ModelAndView list(@RequestParam(defaultValue="T") String contentRadio, HttpServletRequest request) throws Exception {
        try {
            EcareContsHistoryVo contsVo = new EcareContsHistoryVo();
            BeanUtil.populate(contsVo, request.getParameterMap());

            // 템플릿인지 핸들러인지 구분
            contsVo.setContsFlag(contentRadio);

            UserVo userVo = getLoginUserVo();
            contsVo.setUserVo(userVo);

            int countPerPage = userVo.getListCountPerPage();
            contsVo.setCountPerPage(countPerPage);

            int nowPage = contsVo.getNowPage();
            contsVo.setEndRow(countPerPage * nowPage);

            int totalCount = ecareContsHistoryService.getContsTotalCount(contsVo);
            if(nowPage <= (totalCount / countPerPage)) {
                contsVo.setListCnt(countPerPage);
            } else {
                contsVo.setListCnt((totalCount % countPerPage));
            }

            ModelAndView mav = new ModelAndView("ecare/contsHistoryList");  // ecare/ecare_conts_history
            mav.addObject("paging", PageStringUtil.printPaging(request, "nowPage", getPageStringBean(nowPage, countPerPage, totalCount)));
            mav.addObject("contsHistoryList", totalCount > 0 ? ecareContsHistoryService.getContsHistoryList(contsVo) : null);
            mav.addObject("contsHistoryVo", contsVo);
            mav.addObject("contentRadio", contsVo.getContsFlag());
            mav.addObject("compareYn", getCompareYn(request));
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * 비교함 세션List에 데이터가 있으면 비교함플래그를 Y로 변경한다.
     *
     * @param request
     * @return
     */
    private String getCompareYn(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        List<EcareContsHistoryVo> compareContsList = (List<EcareContsHistoryVo>) WebUtils.getSessionAttribute(request, "compareContsList");

        if(compareContsList != null && compareContsList.size() > 0) {
            return "Y";
        } else {
            return ServletRequestUtils.getStringParameter(request, "compareYn", "N");
        }
    }

    /**
     * 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
     *
     * @param currentPage
     * @param pageSize
     * @param totalCount
     * @return
     */
    private PageStringBean getPageStringBean(final int currentPage, final int pageSize, final int totalCount) {
        PageStringBean pgBean = new PageStringBean();
        pgBean.setCurrentPage(currentPage);
        pgBean.setTotalRowCnt(totalCount);
        pgBean.setPageSize(pageSize);
        return pgBean;
    }

    /**
     * [이케어>컨텐츠 변경이력] 컨텐츠(템플릿/핸들러) 내용조회(팝업)
     *
     * @param version
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/contsViewPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /ecare/popupContsView.do
    public ModelAndView view(@RequestParam(defaultValue="0") int version, HttpServletRequest request) throws Exception {
        try {
            EcareContsHistoryVo contsVo = new EcareContsHistoryVo();
            BeanUtil.populate(contsVo, request.getParameterMap());

            String flag = contsVo.getContsFlag();
            if("T".equals(flag)) {
                contsVo.setTmplVer(version);
            } else if("H".equals(flag)) {
                contsVo.setHandlerVer(version);
            } else {
                throw new IllegalArgumentException("Conts Flag is not 'T' or 'H'. Conts Flag Value [" + flag + "]");
            }

            EcareContsHistoryVo retContsVo = ecareContsHistoryService.getContsHistoryInfo(contsVo);

            if(retContsVo != null) {
                retContsVo.setContsFlag(flag);

                if(StringUtil.isNotBlank(retContsVo.getTemplate())) {
                    StringBuffer templateBuffer = new StringBuffer(retContsVo.getTemplate());

                    // 미리보기 >> HTML템플릿 데이터추출을 위해 정규식으로 처리한다.
                    Pattern pattern = Pattern.compile("<%[^=].*\n*[^<%]*\n*%>");
                    Matcher match = pattern.matcher(templateBuffer.toString());

                    while(match.find()) {
                        templateBuffer.replace(match.start(), match.end(), "");
                        match = pattern.matcher(templateBuffer.toString());
                    }

                    // 개인화 변수 <%=변수1%>를 {$변수1}로 치환한다.
                    retContsVo.setPreviewTemplate((templateBuffer.toString()).replaceAll("<%=", "{\\$").replaceAll("%>", "}"));
                }
            }

            ModelAndView mav = new ModelAndView("ecare/contsViewPopup");  // ecare/ecare_conts_history_view
            mav.addObject("contsHistoryVo", retContsVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>컨텐츠 변경이력] 컨텐츠(템플릿/핸들러) 비교(팝업)
     *
     * @param contentType
     * @param contents
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/contsComparePopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /ecare/popupContsCompare.do
    public ModelAndView compare(@RequestParam(defaultValue="T") String contentType, @RequestParam String contents) throws Exception {
        try {
            List<EcareContsHistoryVo> contentList = new ArrayList<>();

            if (contents.startsWith("[") && contents.endsWith("]")) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    EcareContsHistoryVo[] historyVos = objectMapper.readValue(StringUtil.unescapeXss(contents), EcareContsHistoryVo[].class);
                    for (EcareContsHistoryVo historyVo : historyVos) {
                        contentList.add(ecareContsHistoryService.getContsHistoryInfo(historyVo));
                    }
                } catch (Exception e) {
                    log.error("while parsing the json object, exception occurred. " + e.getMessage());
                }
            }

            ModelAndView mav = new ModelAndView("ecare/contsComparePopup");  // ecare/ecare_conts_history_compare
            if(contentList.size() == 2) {
                mav.addObject("contsFlag", contentType);
                mav.addObject("compareDataVo1", contentList.get(0));
                mav.addObject("compareDataVo2", contentList.get(1));
            }
            return mav;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}