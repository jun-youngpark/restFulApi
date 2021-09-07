package com.mnwise.wiseu.web.env.web;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const.MsgType;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.editor.model.DefaultHandlerVo;
import com.mnwise.wiseu.web.env.service.EnvDefaultHandleService;

/**
 * 기본 핸들러 설정 Controller
 */
@Controller
public class EnvDefaultHandleController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvDefaultHandleController.class);

    @Autowired private EnvDefaultHandleService envDefaultHandleService;

    /**
     * - [환경설정>기본핸들러 설정] 기본핸들러 리스트 - 핸들러 목록 출력<br/>
     * - URL : /env/defaultHandlerList.do <br/>
     * - JSP : /env/defaultHandlerList.jsp <br/>
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러 목록을 가져온다.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/defaultHandlerList.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/deafulthandler.do
    public ModelAndView list(DefaultHandlerVo defaultHandler, HttpServletRequest request) throws Exception {
        try {
            defaultHandler.setCountPerPage(getLoginUserVo().getListCountPerPage());

            final int totalCnt = envDefaultHandleService.selectDefaultHandlerCount(defaultHandler);
            List<DefaultHandlerVo> handlerList = envDefaultHandleService.getHandlerList(defaultHandler);

            String paging = PageStringUtil.printPaging(request, "nowPage", defaultHandler.getNowPage(), defaultHandler.getCountPerPage(), totalCnt);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("paging", paging);
            returnData.put("handlerList", handlerList);
            returnData.put("channelUseList", super.channelUseList);
            returnData.put("useSecurityMail", super.useSecurityMail.equalsIgnoreCase("on"));
            returnData.put("usePdfMail", super.usePdfMail.equalsIgnoreCase("on"));
            returnData.put("totalCnt", totalCnt);

            return new ModelAndView("env/defaultHandlerList", returnData);  // env_handlerList
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>기본 핸들러 설정] 신규 핸들러 등록 또는 기존 핸들러 편집 화면 출력
     *
     * @param seq 기본핸들러 번호 (0이면 신규 핸들러 등록)
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/defaultHandlerView.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/viewHandler.do
    public ModelAndView viewHandler(int seq) throws Exception {
        try {
            Map<String, Object> returnData = new HashMap<>();
            returnData.put("webExecMode", super.webExecMode);
            returnData.put("channelUseList", super.channelUseList);
            returnData.put("useSecurityMail", super.useSecurityMail.equalsIgnoreCase("on"));
            returnData.put("usePdfMail", super.usePdfMail.equalsIgnoreCase("on"));
            if(seq > 0) {  // 기존 핸들러 편집
                DefaultHandlerVo returnVo = envDefaultHandleService.selectDefaultHandler(seq);
                returnData.put("envHandlerVo", returnVo);
            }

            return new ModelAndView("env/defaultHandlerView", returnData);  // env/env_handlerRegist
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>기본 핸들러 설정>핸들러 편집 화면] 저장 버튼 - 신규 핸들러 등록
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/insertHandler.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView insertHandler(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
        try {
            DefaultHandlerVo defaultHandler = new DefaultHandlerVo();
            BeanUtil.populate(defaultHandler, paramMap);
            BeanUtil.unescapeXss(defaultHandler, "handler");

            defaultHandler.setUserId(getLoginId());

            String nowDateTime = DateUtil.getNowDateTime();
            defaultHandler.setCreateDt(nowDateTime.substring(0, 8));
            defaultHandler.setCreateTm(nowDateTime.substring(8, 14));

            if(defaultHandler.getAbTestYn() == null || defaultHandler.getAbTestYn().equalsIgnoreCase("N")) {
                defaultHandler.setAbTestYn("N");
            }

            String serviceType = StringUtil.defaultIfBlank(defaultHandler.getServiceType(), "");
            if("".equals(serviceType)) {
                defaultHandler.setMsgType(MsgType.CAMPAIGN);
            } else {
                defaultHandler.setMsgType(MsgType.ECARE);
            }

            envDefaultHandleService.insertHandler(defaultHandler);

            String searchParam = "?nowPage=" + defaultHandler.getNowPage();
            searchParam += "&searchServiceType=" + defaultHandler.getSearchServiceType();
            searchParam += "&searchChannel="+ defaultHandler.getChannel();
            searchParam += "&searchWord=" + defaultHandler.getSearchWord();
            return new ModelAndView(new RedirectView("/env/defaultHandlerList.do"+searchParam));  // /env/deafulthandler.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [환경설정>기본 핸들러 설정>핸들러 편집 화면] 저장 버튼 - 기존 핸들러 수정
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/updateHandler.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateHandler(DefaultHandlerVo defaultHandler) throws Exception {
        try {
            BeanUtil.unescapeXss(defaultHandler, "handler");

            defaultHandler.setUserId(getLoginId());

            String nowDateTime = DateUtil.getNowDateTime();
            defaultHandler.setCreateDt(nowDateTime.substring(0, 8));
            defaultHandler.setCreateTm(nowDateTime.substring(8, 14));

            if(defaultHandler.getAbTestYn() == null || defaultHandler.getAbTestYn().equalsIgnoreCase("N")) {
                defaultHandler.setAbTestYn("N");
            }

            envDefaultHandleService.updateHandler(defaultHandler);

            Map<String, Object> returnData = new HashMap<>();
            returnData.put("seq", defaultHandler.getSeq());

            return new ModelAndView(new RedirectView("/env/defaultHandlerView.do"), returnData);  // /env/viewHandler.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * [환경설정>기본 핸들러 설정] 삭제 버튼 - 선택한 핸들러 삭제
     *
     * @param request
     * @param response
     * @return
     * @throws ServletRequestBindingException
     * @throws Exception
     */
    @RequestMapping(value="/env/deleteHandler.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteHandler(HttpServletRequest request) throws Exception {
        try {
            DefaultHandlerVo defaultHandler = new DefaultHandlerVo();
            defaultHandler.setNoArray(ServletRequestUtils.getRequiredIntParameters(request, "noArray"));

            envDefaultHandleService.deleteHandler(defaultHandler);

            return new ModelAndView(new RedirectView("/env/defaultHandlerList.do"));  // /env/deafulthandler.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/env/getHandlerUseList.do", method={RequestMethod.GET, RequestMethod.POST})  // /env/{serviceGubun}/{serviceNo}/deafulthandler.do
    @ResponseBody public Map<String, Object> getHandlerUseList(String serviceGubun, int serviceNo) throws Exception {
        try {
            return envDefaultHandleService.getHandlerUseList(serviceGubun, serviceNo);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    @RequestMapping(value="/env/loadHandler.json", method={RequestMethod.GET, RequestMethod.POST})  // /env/{seq}/loadhandler.do
    @ResponseBody public DefaultHandlerVo loadHandler(int seq) throws Exception {
        try {
            return envDefaultHandleService.selectDefaultHandler(seq);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * - [환경설정>기본핸들러 설정] 기본핸들러 설정 버튼 - 기등록된 핸들러를 모두 삭제하고 기본 핸들러를 새로 모두 등록
     * - URL : /env/defaultHandlerList.do
     * - JSP : /env/defaultHandlerList.jsp
     */
    @RequestMapping(value="/env/insertEditorDefaultHandler.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto insertEditorDefaultHandler(HttpServletRequest request) {
        try {
            int row = envDefaultHandleService.insertEditorDefaultHandler(getLoginId());

            return (row > 0) ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
