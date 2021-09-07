package com.mnwise.wiseu.web.ecare.web;

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
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.CampaignPerHistoryVo;
import com.mnwise.wiseu.web.common.model.SendErrVo;
import com.mnwise.wiseu.web.common.service.ErrorCodeSerivice;
import com.mnwise.wiseu.web.ecare.model.EcarePerHistoryVo;
import com.mnwise.wiseu.web.ecare.service.EcarePerHistoryService;

/**
 * 이케어 고객이력 Controller
 * 발송한 대상자 리스트를 검색하는 페이지 채널별로 불러오는 조회조건과 에러리스트가 다르다.
 */
@Controller
public class EcarePerHistoryController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcarePerHistoryController.class);

    @Autowired private EcarePerHistoryService ecarePerHistoryService;
    @Autowired private ErrorCodeSerivice errorCodeSerivice;

    @Value("${ecare.mime.save}")
    private String ecareMimeSave;

    /**
     * [이케어>이케어 고객이력] 이케어 고객이력
     */
    @RequestMapping(value="/ecare/ecareSendLog.do", method={RequestMethod.GET, RequestMethod.POST})  // /ecare/ecare_per_history.do
    public ModelAndView list(EcarePerHistoryVo ecarePerHistoryVo, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("ecare/ecareSendLog");  // ecare/ecare_per_history
            UserVo userVo = getLoginUserVo();

            PagingUtil.setPagingInfo(request, ecarePerHistoryVo);
            PagingUtil.setPagingRowcount(ecarePerHistoryVo);
            ecarePerHistoryVo.setUserVo(userVo);
            String channel = ecarePerHistoryVo.getChannel();

            if(channel == null) {
                for(int i = 0, len = super.ecareChannelUseList.length(); i < len; i++) {
                    char ch = super.ecareChannelUseList.charAt(i);
                    if(ch >= 'A' && ch <= 'Z') {
                        channel = String.valueOf(ch);
                        break;
                    }
                }
            }

            List<?> ecarePerHistoryList = null;
            String paging = "";
            if(StringUtil.isNotBlank(request.getParameter("searchQstartDt"))) {
                DataSecurity.getInstance().securityObject(ecarePerHistoryVo, "ENCRYPT");
                // mssql 페이징 처리 수정
                int iTotCnt = ecarePerHistoryService.getTotalCount(ecarePerHistoryVo);
                mav.addObject("iTotCnt", iTotCnt);
                if(iTotCnt>0) {
                    int aCountPerPage = ecarePerHistoryVo.getUserVo().getListCountPerPage();
                    int qCountPerPage = (iTotCnt / aCountPerPage); // 몫
                    int rCountPerPage = (iTotCnt % aCountPerPage); // 나머지

                    if(ecarePerHistoryVo.getNowPage() <= qCountPerPage) {
                        ecarePerHistoryVo.setListCnt(ecarePerHistoryVo.getUserVo().getListCountPerPage());
                    } else {
                        ecarePerHistoryVo.setListCnt(rCountPerPage);
                    }
                    ecarePerHistoryList = ecarePerHistoryService.getPerHistoryList(ecarePerHistoryVo);
                }
                // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
                PageStringBean pgBean = new PageStringBean();
                pgBean.setCurrentPage(ecarePerHistoryVo.getNowPage());
                pgBean.setTotalRowCnt(iTotCnt);
                pgBean.setPageSize(userVo.getListCountPerPage());
                paging = PageStringUtil.printPaging(request, "nowPage", pgBean);
                mav.addObject("pgBean", pgBean);
            } else {
                mav.addObject("searchQstartDt", DateUtil.addDays(-7, "yyyyMMdd"));
                mav.addObject("searchQendDt", DateUtil.getNowDate());
            }

            List<SendErrVo> sendErrList = errorCodeSerivice.getBychannelErrList(channel);

            mav.addObject("useChannelList", super.ecareChannelUseList);
            mav.addObject("channel", channel);
            mav.addObject("perHistoryList", ecarePerHistoryList);
            mav.addObject("perHistoryVo", ecarePerHistoryVo);
            mav.addObject("sendErrList", sendErrList);
            mav.addObject("paging", paging);
            mav.addObject("searchResendSts", ecarePerHistoryVo.getResendSts());
            mav.addObject("mimeSave", this.ecareMimeSave);  // 마임저장여부 (마임을 남기면 마임보기, 아니면 템플릿 보여주기)

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>이케어 고객이력] 이케어 고객이력 - 채널 콤보박스 선택 클릭
     * 주어진 채널에 대한 에러코드 목록을 조회하여 발송결과 콤보박스를 재구성
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/ecare/selectErrorCdList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<SendErrVo> errlist(CampaignPerHistoryVo ecarePerHistoryVo, String channelType, HttpServletRequest request) throws Exception {
        try {
            List<SendErrVo> sendErrList = errorCodeSerivice.getBychannelErrList(channelType);
            return sendErrList;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
