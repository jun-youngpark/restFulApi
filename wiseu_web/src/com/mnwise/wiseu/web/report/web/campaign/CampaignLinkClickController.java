package com.mnwise.wiseu.web.report.web.campaign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;
import com.mnwise.wiseu.web.editor.service.EditorCampaignService;
import com.mnwise.wiseu.web.report.model.campaign.CampaignInfoVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportLinkClickVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCommonService;

/**
 * 캠페인 리포트 - 링크클릭 분석(메일) Controller
 */
@Controller
public class CampaignLinkClickController extends CampainTabMenuController {
    private static final Logger log = LoggerFactory.getLogger(CampaignLinkClickController.class);

    @Autowired private CampaignCommonService campaignCommonService;
    @Autowired private EditorCampaignService editorCampaignService;

    /**
     * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 링크클릭 분석(메일)<br/>
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/report/campaign/linkClickRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/linkclick.do
    public ModelAndView list(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int scenarioNo,
                             @RequestParam(defaultValue="N") String abTestType, @RequestParam(defaultValue="") String abTestCond,
                             HttpServletRequest request) throws Exception {
        try {
            addTabMenu(request);

            CaseInsensitiveMap userInfo = campaignReportService.selectCreateUserInfo(scenarioNo);

            // 파라미터 조작으로 다른 계정의 정보 조회 권한 제한
            if(isInvalidAccess(userInfo.get("GRP_CD").toString(), userInfo.get("USER_ID").toString())) {
                return new ModelAndView("");
            }

            // 1회 클릭, 1회이상 클릭의 링크 총 합계를 가져온다 (%를 구하기 위하여)
            CampaignReportLinkClickVo campaignReportLinkClickVo = campaignReportService.getCampaignReportLinkClickTotal(scenarioNo, campaignNo);

            List<CampaignReportLinkClickVo> campaignReportLinkClickList = campaignReportService.getCampaignReportLinkClickList(scenarioNo, campaignNo);

            /* oracle 8i로 포팅 중 집계 함수 적용 문제로 union 하여서 합계 로우를 추가해서 가져오는데 데이터가 없는 경우 합계로우만 가지오게 되는 문제로 인하여 row 가 1인경우에는 합계로우를 삭제 하여 return 함 */
            if(campaignReportLinkClickList.size() == 1)
                campaignReportLinkClickList.remove(0);

            ModelAndView mav = new ModelAndView("report/campaign/linkClickRpt");  // report/campaign/linkclick_view
            mav.addObject("campaignReportLinkClickList", campaignReportLinkClickList);
            mav.addObject("campaignReportLinkClickVo", campaignReportLinkClickVo);
            mav.addObject("abTestType", abTestType);
            mav.addObject("abTestCond", abTestCond);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 링크클릭 분석(메일) - 컨텐츠 반응분석(팝업)
     */
    @RequestMapping(value="/report/campaign/templateViewPopupByLinkClick.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/template_view_popup.do
    public ModelAndView handle(ScenarioInfoVo scenarioInfoVo, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("report/campaign/templateViewPopup");  // report/campaign/template_view

            UserVo userVo = getLoginUserVo();
            scenarioInfoVo.setUserVo(userVo);
            scenarioInfoVo.setLanguage(userVo.getLanguage());
            scenarioInfoVo.getCampaignInfoVo().setCampaignNo(scenarioInfoVo.getCampaignNo());

            scenarioInfoVo = campaignCommonService.selectScenarioInfo(scenarioInfoVo.getCampaignNo());

            CampaignInfoVo campaignInfoVo = scenarioInfoVo.getCampaignInfoVo();
            List<TemplateVo> templateList = editorCampaignService.selectEditorCampaignTemplateForReport(campaignInfoVo.getCampaignNo(), campaignInfoVo.getSegmentNo());
            // 일단 멀티 탬플릿을 추후 고려하도록 한다.
            if(templateList.size() == 1) {
                TemplateVo templateVo = templateList.get(0);
                String template = templateVo.getTemplate();
                // 캠페인에 삽입되는 파라미터코드를 제거한다.
                template = template.substring(template.indexOf("%>") + 2);
                template = template.replaceAll("<%=", "").replaceAll("%>", "");

                mav.addObject("template", template);
            }

            mav.addObject("scenarioInfoVo", scenarioInfoVo);

            int scenarioNo = scenarioInfoVo.getScenarioNo();
            int campaignNo = campaignInfoVo.getCampaignNo();
            List<CampaignReportLinkClickVo> campaignReportLinkClickList = campaignReportService.getCampaignReportLinkClickList(scenarioNo, campaignNo);
            mav.addObject("campaignReportLinkClickList", campaignReportLinkClickList);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
