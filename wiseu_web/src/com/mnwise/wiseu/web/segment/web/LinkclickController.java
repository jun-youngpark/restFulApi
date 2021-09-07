package com.mnwise.wiseu.web.segment.web;

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
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.segment.model.LinkclickVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.service.LinkclickService;

@Controller
public class LinkclickController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(LinkclickController.class);

    @Autowired private LinkclickService linkclickService;

    /**
     * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)>반응대상자 탭] 반응대상자 목록
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    // /segment/target/linkservice.do", /segment/linkclick/linkservice.do
    @RequestMapping(value = {"/target/linkClickServiceList.do", "/segment/linkClickServiceList.do"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(@RequestParam(defaultValue="-1") int segmentNo, @RequestParam(defaultValue="0") int serviceNo, String serviceType,String tabUse, HttpServletRequest request) throws Exception {
        try {
            ScenarioVo scenarioVo = null;
            int serviceListTotalCount = 0;
            if(StringUtil.equals(serviceType, "EM")) {
                scenarioVo = new ScenarioVo();
                scenarioVo.setUserVo(getLoginUserVo());
                serviceListTotalCount = linkclickService.getServiceListTotalCount(serviceType, scenarioVo);
            }

            PagingUtil.setPagingInfo(request, scenarioVo);

            ModelAndView mav = new ModelAndView("segment/linkClickServiceList");  // segment/linkclick/service_list
            mav.addObject("serviceListTotalCount", serviceListTotalCount);
            PagingUtil.transferPagingInfo(request, serviceListTotalCount);

            if(serviceListTotalCount > 0) {
                mav.addObject("serviceList", linkclickService.getServiceList(serviceType, scenarioVo));
            }

            mav.addObject("serviceNo", serviceNo);
            mav.addObject("segmentNo", segmentNo);
            mav.addObject("target", "upload");
            mav.addObject("serviceType", serviceType);
            mav.addObject("tabUse", tabUse);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)>반응대상자 탭] 반응대상자 목록 하단의 URL/이미지 목록 출력
     *
     * @param segmentNo
     * @param serviceNo
     * @param serviceType
     * @param segmentSize
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/target/linkclick_popup.do, /segment/linkclick/linkclick_popup.do
    @RequestMapping(value = {"/target/linkClickRptListDiv.do", "/segment/linkClickRptListDiv.do"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(@RequestParam(defaultValue="-1") int segmentNo, @RequestParam(defaultValue="0") int serviceNo, String serviceType, @RequestParam(defaultValue="0") int segmentSize, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("segment/linkClickRptListDiv");  // segment/linkclick/linkclick_list
            if(serviceNo > 0) {
                mav.addObject("linkInfoList", linkclickService.getServiceLinkInfo(serviceType, serviceNo));
            }

            String segmentNm = null;
            LinkclickVo linkClickVo = new LinkclickVo();
            int checkedLinkSeqCnt = 0;
            if(segmentNo > 0) {
                // 이미 체크되어 있는 정보를 가져온다.
                linkClickVo = linkclickService.getCheckedLinkClickInfo(serviceType, segmentNo);
                if(linkClickVo.getLinkSeqArray() != null) {
                    checkedLinkSeqCnt = linkClickVo.getLinkSeqArray().length;
                }

                SegmentVo segmentVo = linkclickService.selectSegmentInfo(segmentNo);
                segmentNm = segmentVo.getSegmentNm();
            }

            mav.addObject("linkclickVo", linkClickVo);
            mav.addObject("serviceNo", serviceNo);
            mav.addObject("checkedLinkSeqCnt", checkedLinkSeqCnt);
            mav.addObject("segmentNo", segmentNo); // 자신의(발송 할) 세그먼트 번호
            mav.addObject("segmentSize", segmentSize);
            mav.addObject("segmentNm", StringUtil.defaultString(segmentNm));

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)>반응대상자 탭] 저장 버튼 클릭
     *
     * @param segmentNo
     * @param serviceNo
     * @param serviceType
     * @param linkSeqArray
     * @param checkedLinkSeqCnt
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/segment/linkclick/insertLinkClickSegment.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView save(int segmentNo, int serviceNo, String serviceType, int linkSeqArray[], int checkedLinkSeqCnt, HttpServletRequest request) throws Exception {
        try {
            LinkclickVo linkClickVo = new LinkclickVo();
            linkClickVo.setServiceType(serviceType);
            linkClickVo.setServiceNo(serviceNo);
            linkClickVo.setLinkSeqArray(linkSeqArray);
            linkClickVo.setSegmentNo(segmentNo);

            int segmentSize = linkclickService.getServiceLinkTargetCount(linkClickVo);
            linkClickVo.setSegmentSize(segmentSize);

            UserVo userVo = getLoginUserVo();
            String mode = (checkedLinkSeqCnt > 0) ? "UPDATE" : "INSERT";
            segmentNo = linkclickService.setRegistSegmentInfo(linkClickVo, mode, userVo.getUserId(), userVo.getGrpCd());

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/linkClickRptListDiv.do?cmd=list"));  // /segment/linkclick/linkclick_popup.do?cmd=list
            mav.addObject("serviceNo", serviceNo);
            mav.addObject("serviceType", serviceType);
            mav.addObject("segmentSize", segmentSize);
            mav.addObject("segmentNo", segmentNo);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
