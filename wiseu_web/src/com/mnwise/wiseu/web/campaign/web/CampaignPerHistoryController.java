package com.mnwise.wiseu.web.campaign.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.security.DataSecurity;
import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.CampaignPerHistoryVo;
import com.mnwise.wiseu.web.campaign.service.CampaignPerHistoryService;
import com.mnwise.wiseu.web.common.model.SendErrVo;
import com.mnwise.wiseu.web.common.service.ErrorCodeSerivice;

/**
 * 캠페인 고객이력 Controller
 */
@Controller
public class CampaignPerHistoryController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CampaignPerHistoryController.class);

    @Autowired private CampaignPerHistoryService campaignPerHistoryService;
    @Autowired private ErrorCodeSerivice errorCodeSerivice;

    @Value("${campaign.mime.save}")
    private String campaignMimeSave;

    /**
     * [캠페인>캠페인 고객이력] 캠페인 고객이력
     */
    @RequestMapping(value="/campaign/campaignSendLog.do", method={RequestMethod.GET, RequestMethod.POST})  // /campaign/campaign_per_history.do
    public ModelAndView list(CampaignPerHistoryVo campaignPerHistoryVo, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("campaign/campaignSendLog");  // campaign/campaign_per_history

            PagingUtil.setPagingInfo(request, campaignPerHistoryVo);
            PagingUtil.setPagingRowcount(campaignPerHistoryVo);

            campaignPerHistoryVo.setUserVo(getLoginUserVo());
            String channel = campaignPerHistoryVo.getChannel();
            if(channel == null) {
                for(int i = 0, len = super.campaignChannelUseList.length(); i < len; i++) {
                    char ch = super.campaignChannelUseList.charAt(i);
                    if(ch >= 'A' && ch <= 'Z') {
                        channel = String.valueOf(ch);
                        break;
                    }
                }
            }

            List<CampaignPerHistoryVo> campaignPerHistoryList = null;
            String paging = "";
            if(StringUtil.isNotBlank(request.getParameter("searchQstartDt"))) {
                DataSecurity.getInstance().securityObject(campaignPerHistoryVo, "ENCRYPT");
                int iTotCnt = campaignPerHistoryService.getTotalCount(campaignPerHistoryVo);
                mav.addObject("iTotCnt", iTotCnt);
                if(iTotCnt>0) {
                    int aCountPerPage = campaignPerHistoryVo.getUserVo().getListCountPerPage();
                    int qCountPerPage = (iTotCnt / aCountPerPage); // 몫
                    int rCountPerPage = (iTotCnt % aCountPerPage); // 나머지

                    if(campaignPerHistoryVo.getNowPage() <= qCountPerPage) {
                        campaignPerHistoryVo.setListCnt(campaignPerHistoryVo.getUserVo().getListCountPerPage());
                    } else {
                        campaignPerHistoryVo.setListCnt(rCountPerPage);
                    }
                    campaignPerHistoryList = campaignPerHistoryService.getPerHistoryList(campaignPerHistoryVo);
                }

                // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
                PageStringBean pgBean = new PageStringBean();
                pgBean.setCurrentPage(campaignPerHistoryVo.getNowPage());
                pgBean.setTotalRowCnt(iTotCnt);
                pgBean.setPageSize(getLoginUserVo().getListCountPerPage());
                paging = PageStringUtil.printPaging(request, "nowPage", pgBean);
                mav.addObject("pgBean", pgBean);
            } else {
                mav.addObject("searchQstartDt", DateUtil.addMonths(-1, "yyyyMMdd"));
                mav.addObject("searchQendDt", DateUtil.getNowDate());
            }
            //채널별 에러코드 목록을 조회
            List<SendErrVo> sendErrList = errorCodeSerivice.getBychannelErrList(channel);

            mav.addObject("useChannelList", super.campaignChannelUseList);
            mav.addObject("channel", channel);
            mav.addObject("searchResendSts", campaignPerHistoryVo.getResendSts());
            mav.addObject("perHistoryList", campaignPerHistoryList);
            mav.addObject("perHistoryVo", campaignPerHistoryVo);
            mav.addObject("sendErrList", sendErrList);
            mav.addObject("paging", paging);
            // 마임을 남기는지 여부 (마임을 남기면 마임보기, 아니면 그냥 템플릿 보여주기
            mav.addObject("mimeSave", this.campaignMimeSave);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 고객이력] 캠페인 고객이력 - 채널 콤보박스 선택 클릭
     * 주어진 채널에 대한 에러코드 목록을 조회
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/campaign/selectErrorCdList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<SendErrVo> errlist(CampaignPerHistoryVo campaignPerHistoryVo, String channelType, HttpServletRequest request) throws Exception {
        try {
            List<SendErrVo> sendErrList = errorCodeSerivice.getBychannelErrList(channelType);
            return sendErrList;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
