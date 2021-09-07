package com.mnwise.wiseu.web.campaign.web;

import java.util.List;
import java.util.Map;

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

import com.mnwise.common.util.BeanUtil;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.service.ScenarioService;

/**
 * 캠페인 목록 조회 Controller
 */
@Controller
public class ScenarioController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ScenarioController.class);

    @Autowired private ScenarioService scenarioService;

    /**
     * - [캠페인>캠페인 리스트] 캠페인 리스트
     * - [캠페인>캠페인 리스트] 캠페인 리스트 - 수행 상태 선택
     * - [캠페인>캠페인 리스트] 캠페인 리스트 - 승인요청만 보기 버튼 클릭
     * - [캠페인>캠페인 리스트] 캠페인 리스트 - 검색 버튼 클릭
     * - [캠페인>캠페인 리스트] 캠페인 리스트 - 태그 클릭
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/campaignList.do", method={RequestMethod.GET, RequestMethod.POST})  // /campaign/campaign.do
    public ModelAndView list(@RequestParam(defaultValue="-1") int tagNo, @RequestParam(defaultValue="") String approvalSts, HttpServletRequest request) throws Exception {
        try {
            String campaignSts = StringUtil.defaultString(request.getParameter("campaignVo.campaignSts"));
            String searchQstartDt = StringUtil.defaultString(request.getParameter("searchQstartDt"), DateUtil.addMonths(-12, "yyyyMMdd"));  // 검색 시작일
            String searchQendDt = StringUtil.defaultString(request.getParameter("searchQendDt"), DateUtil.getNowDate());  // 검색 종료일

            ScenarioVo scenarioVo = new ScenarioVo();
            BeanUtil.populate(scenarioVo, request.getParameterMap());
            scenarioVo.setTagNo(tagNo);

            if(StringUtil.equalsAny(campaignSts, new String[] {"C", "D", "A"})) {
                approvalSts = campaignSts;
                scenarioVo.getCampaignVo().setCampaignSts("P");
            }

            scenarioVo.getCampaignVo().setApprovalSts(approvalSts);
            UserVo userVo = getLoginUserVo();
            scenarioVo.setUserVo(userVo);
            scenarioVo.setSearchQstartDt(searchQstartDt);
            scenarioVo.setSearchQendDt(searchQendDt);

            // scenarioVo.setGrpCd(grpCd)
            // 페이징을 위해 리스트의 TOTAL ROW 를 가져온다.
            int rowCnt = scenarioService.getScenarioTotalCount(scenarioVo);

            scenarioVo.setCountPerPage(userVo.getListCountPerPage());

            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (rowCnt / aCountPerPage);  // 몫
            int rCountPerPage = (rowCnt % aCountPerPage); // 나머지

            if(scenarioVo.getNowPage() <= qCountPerPage) {
                scenarioVo.setListCnt(userVo.getListCountPerPage());
            } else {
                scenarioVo.setListCnt(rCountPerPage);
            }

            scenarioVo.setEndRow(scenarioVo.getNowPage() * userVo.getListCountPerPage());

            Map<String, String> roleMap = (Map<String, String>) getLoginRoleMap().get("/campaign/campaign1Step.do");  // /campaign/campaign_step_form.do
            /// campaign/campaign_step_form.do에 접근 권한이 없는 경우 roleMap이 null 이므로 이 처리를 한다.
            String execute = (roleMap == null) ? null : roleMap.get("execute");

            // TOTAL COUNT 가 존재할 경우 리스트를 가져온다.
            List<ScenarioVo> scenarioList = null;
            if(rowCnt > 0) {
                scenarioList = scenarioService.getCampaignScenarioList(scenarioVo);
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(scenarioVo.getNowPage());
            pgBean.setTotalRowCnt(rowCnt);
            pgBean.setPageSize(scenarioVo.getCountPerPage());
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            ModelAndView mav = new ModelAndView("campaign/campaignList");  // campaign/campaign_list
            mav.addObject("scenarioList", scenarioList);
            mav.addObject("scenarioVo", scenarioVo);
            mav.addObject("searchQstartDt", searchQstartDt);
            mav.addObject("searchQendDt", searchQendDt);
            mav.addObject("paging", paging);
            mav.addObject("execute", execute);
            mav.addObject("webExecMode", super.webExecMode);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 리스트] 캠페인 리스트 - 캠페인 삭제
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/deleteCampaign.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteCampaign(int scenarioNo, int campaignNo, String channelType, @RequestParam(defaultValue="1") int nowPage, HttpServletRequest request) throws Exception {
        try {
            ScenarioVo scenarioVo = new ScenarioVo();
            BeanUtil.populate(scenarioVo, request.getParameterMap());

            scenarioVo.setScenarioNo(scenarioNo);
            scenarioVo.getCampaignVo().setCampaignNo(campaignNo);
            scenarioVo.getCampaignVo().setChannelType(channelType);
            scenarioService.setDeleteCampaign(scenarioVo);

            ModelAndView mav = new ModelAndView(new RedirectView("/campaign/campaignList.do"));  // /campaign/campaign.do
            mav.addObject("searchQstartDt", scenarioVo.getSearchQstartDt());
            mav.addObject("searchQendDt", scenarioVo.getSearchQendDt());
            mav.addObject("searchWord", scenarioVo.getSearchWord());
            mav.addObject("searchColumn", scenarioVo.getSearchColumn());
            mav.addObject("nowPage", nowPage);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 리스트] 캠페인 리스트 - 캠페인 복사
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/copyCampaign.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView copy(int scenarioNo, @RequestParam(defaultValue="1") int nowPage, HttpServletRequest request) throws Exception {
        try {
            ScenarioVo scenarioVo = new ScenarioVo();
            BeanUtil.populate(scenarioVo, request.getParameterMap());
            BeanUtil.populate(scenarioVo.getCampaignVo(), request.getParameterMap());

            scenarioVo.setUserVo(getLoginUserVo());
            scenarioVo.setScenarioNo(scenarioNo);

            try {
                scenarioService.setRegistCopyScenario(scenarioVo);
            } catch(Exception e) {
                log.error(e.toString());
            }

            ModelAndView mav = new ModelAndView(new RedirectView("/campaign/campaignList.do"));  // /campaign/campaign.do
            mav.addObject("searchQstartDt", scenarioVo.getSearchQstartDt());
            mav.addObject("searchQendDt", scenarioVo.getSearchQendDt());
            mav.addObject("searchWord", scenarioVo.getSearchWord());
            mav.addObject("searchColumn", scenarioVo.getSearchColumn());
            mav.addObject("nowPage", nowPage);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 리스트] 캠페인 리스트 - 상태변경
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/changeCampaignSts.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView changeSts(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="1") int nowPage, HttpServletRequest request) throws Exception {
        try {
            ScenarioVo scenarioVo = new ScenarioVo();
            BeanUtil.populate(scenarioVo, request.getParameterMap());

            scenarioVo.setUserVo(getLoginUserVo());
            scenarioVo.getCampaignVo().setCampaignNo(campaignNo);

            scenarioService.changeStatus(scenarioVo);

            ModelAndView mav = new ModelAndView(new RedirectView("/campaign/campaignList.do"));  // /campaign/campaign.do
            mav.addObject("searchQstartDt", scenarioVo.getSearchQstartDt());
            mav.addObject("searchQendDt", scenarioVo.getSearchQendDt());
            mav.addObject("searchWord", scenarioVo.getSearchWord());
            mav.addObject("searchColumn", scenarioVo.getSearchColumn());
            mav.addObject("nowPage", nowPage);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
