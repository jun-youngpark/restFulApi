package com.mnwise.wiseu.web.report.web.campaign;

import java.util.HashMap;
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

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.model.Tag;
import com.mnwise.wiseu.web.report.model.LinkTraceResult2Vo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReactionResult2Vo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignSendResultVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCommonService;
import com.mnwise.wiseu.web.report.service.campaign.CampaignCompareWithService;

/**
 * 캠페인 비교 분석 Controller
 */
@Controller
public class CampaignCompareWithController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CampaignCompareWithController.class);

    @Autowired private CampaignCommonService campaignCommonService;
    @Autowired private CampaignCompareWithService campaignCompareWithService;

    /**
     * [리포트>캠페인] 캠페인 비교분석
     */
    @RequestMapping(value="/report/campaign/compareRpt.do", method={RequestMethod.GET, RequestMethod.POST})  // /report/campaign/compare_with.do
    protected ModelAndView handle(ScenarioInfoVo scenarioInfoVo, @RequestParam(defaultValue="-1") int searchTagNo,
                                  String searchChannel, HttpServletRequest request) throws Exception {
        try {
            String searchStartDt = StringUtil.defaultString(request.getParameter("searchStartDt"), DateUtil.addMonths(-1, "yyyyMMdd"));  // 검색 시작일
            String searchEndDt = StringUtil.defaultString(request.getParameter("searchEndDt"), DateUtil.getNowDate());  // 검색 종료일

            UserVo userVo = getLoginUserVo();
            scenarioInfoVo.setUserVo(userVo);
            scenarioInfoVo.setLanguage(userVo.getLanguage());

            int campaignNo = campaignCommonService.getMaxCampaignNo(scenarioInfoVo);
            scenarioInfoVo.getCampaignInfoVo().setCampaignNo(campaignNo);

            CampaignSendResultVo campaignSendResultVo = null;
            if(campaignNo > 0) {
                // 캠페인 기본정보
                scenarioInfoVo = campaignCommonService.selectScenarioInfo(campaignNo);
                // 발송결과
                campaignSendResultVo = campaignCommonService.selectSendResult(scenarioInfoVo);
            }

            if(campaignSendResultVo == null) {
                return new ModelAndView(new RedirectView("/report/campaign/campaignRptList.do"));  // /report/campaign/campaign_list.do
            }

            scenarioInfoVo.setTagNo(searchTagNo);
            scenarioInfoVo.getCampaignInfoVo().setChannelType(searchChannel);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("searchTagNo", searchTagNo);
            paramMap.put("searchChannel", searchChannel);
            paramMap.put("searchStartDt", searchStartDt);
            paramMap.put("searchEndDt", searchEndDt);
            paramMap.put("userVo", userVo);

            // tag cloud
            List<Tag> tagList = campaignCompareWithService.selectTagCloud(paramMap);

            ModelAndView mav = new ModelAndView("report/campaign/compareRpt");  // report/campaign/compare_with_view
            if(scenarioInfoVo.getTagNo() > -1) {
                List<Integer> campaignNoList = campaignCompareWithService.selectCampaignNoList(paramMap);

                if(campaignNoList != null && campaignNoList.size() > 0) {
                    int[] campaignNoArray = new int[campaignNoList.size()];
                    for(int i = 0; i < campaignNoList.size(); i++) {
                        campaignNoArray[i] = campaignNoList.get(i);
                    }
                    paramMap.put("campaignNoArray", campaignNoArray);
                    scenarioInfoVo.setCampaignNoArray(campaignNoArray);
                    // 공통 코드 테이블 조회를 위한 언어를 넘겨준다.
                    paramMap.put("language", userVo.getLanguage());

                    List<CampaignSendResultVo> sendResultList = campaignCompareWithService.selectSendResultList(paramMap);
                    mav.addObject("sendResultList", sendResultList);

                    List<CampaignReactionResult2Vo> campaignReactionResultList = campaignCompareWithService.selectReactionResult2List(paramMap);
                    mav.addObject("campaignReactionResultList", campaignReactionResultList);

                    List<LinkTraceResult2Vo> linkTraceResultList = campaignCompareWithService.selectLinkTraceResult2List(paramMap);
                    mav.addObject("linkTraceResultList", linkTraceResultList);
                }
            }

            String[] useChannelArray = super.campaignChannelUseList.split(",");

            mav.addObject("scenarioInfoVo", scenarioInfoVo);
            mav.addObject("campaignSendResultVo", campaignSendResultVo);
            mav.addObject("tagList", tagList);
            mav.addObject("searchTagNo", searchTagNo);
            mav.addObject("searchChannel", searchChannel);
            mav.addObject("searchStartDt", searchStartDt);
            mav.addObject("searchEndDt", searchEndDt);
            mav.addObject("channelUseSize", useChannelArray.length);
            mav.addObject("channelUseList", useChannelArray);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
