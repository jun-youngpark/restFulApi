package com.mnwise.wiseu.web.segment.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.service.SegmentService;
import com.mnwise.wiseu.web.segment.service.SqlSegmentService;

/**
 * 대상자 등록 1단계 Controller
 */
@Controller
public class Segment1StepController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Segment1StepController.class);

    @Autowired private SegmentService segmentService;
    @Autowired private SqlSegmentService sqlSegmentService;

    /**
     * [대상자>대상자 등록>1단계] 대상자 등록/수정 화면
     *
     * @param segmentNo
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/sqlSegment1step.do, /segment/target/sqlSegment1step.do
    @RequestMapping(value={"/segment/sqlSegment1Step.do", "/target/sqlSegment1Step.do"}, method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(@RequestParam(defaultValue="0") int segmentNo, String tabUse, String tabType, HttpServletRequest request) throws Exception {
        try {
            ModelAndView mav = new ModelAndView("segment/sqlSegment1Step");  // segment/sqlSegment1step
            SegmentVo segmentVo = new SegmentVo();

            // PagingUtil.setPagingInfo(request);

            // 이전 등록된 세그먼트 저장 시
            if(segmentNo > 0) {
                segmentVo.setSegmentNo(segmentNo);
                segmentVo = sqlSegmentService.selectSegmentByPk(segmentNo);

                // sql세그먼트 DBINFO 로드
                segmentVo.setDbInfoList(sqlSegmentService.selectDbInfoList());

                List<DbInfoVo> dbInfoList = segmentVo.getDbInfoList();

                for(DbInfoVo dbInfoVo : dbInfoList) {
                    if(dbInfoVo.getDbInfoSeq() == segmentVo.getDbinfoSeq()) {
                        segmentVo.setDbInfoVo(dbInfoVo);
                    }
                }
                // 기본세그먼트 여부
                String bookMark = sqlSegmentService.getBookMarkYn(segmentVo.getSegmentNo());
                segmentVo.setBookmarkSeg(bookMark);

                if(request.getRequestURI().indexOf("target") > -1) {
                    mav.addObject("action", "/target/sqlSegment2Step.do");  // /segment/target/sqlSegment2step.do
                    mav.addObject("targetUrl", "true");
                } else {
                    mav.addObject("action", "/segment/sqlSegment2Step.do");  // /segment/sqlSegment2step.do
                }
            } else {
                // sql세그먼트 신규 등록 시 DBINFO 로드
                segmentVo.setDbInfoList(sqlSegmentService.selectDbInfoList());
                segmentVo.setBookmarkSeg("Y"); // 기본 대상자를 디폴트로 한다.

                if(request.getRequestURI().indexOf("target") > -1) {
                    mav.addObject("action", "/target/sqlSegment2Step.do");  // /segment/target/sqlSegment2step.do
                } else {
                    mav.addObject("action", "/segment/sqlSegment2Step.do");  // /segment/sqlSegment2step.do
                }
            }

            mav.addObject("segmentVo", segmentVo);
            mav.addObject("tabUse", tabUse);
            mav.addObject("tabType", tabType);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 업데이트 쿼리체크
     *
     * @param segmentNo
     * @return
     */
    @RequestMapping(value="/segment/checkUpdateQuery.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto modSegmentCheck(int segmentNo) {
        try {
            int result = sqlSegmentService.modSegmentCheck(segmentNo);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(result + "");
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 업데이트 쿼리 - SQL 수정(팝업)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/updateQueryPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /segment/popupUpdateQuery.do
    public ModelAndView selectUpdateSql(int segmentNo) throws Exception {
        try {
            SegmentVo segmentVo = sqlSegmentService.selectSegmentByPk(segmentNo);

            ModelAndView mav = new ModelAndView("segment/updateQueryPopup");  // segment/modify_update_popup
            mav.addObject("segmentVo", segmentVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 업데이트 쿼리 - SQL 수정(팝업) - 수정 버튼
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/updateUpdateQuery.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView modifyUpdateSql(SegmentVo segmentVo) throws Exception {
        try {
            segmentService.updateUpdateQuery(segmentVo);

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/updateQueryPopup.do?segmentNo=" + segmentVo.getSegmentNo()));  // /segment/popupUpdateQuery.do
            mav.addObject("segmentVo", segmentVo);
            return mav;
            //request.setAttribute("segmentVo", segmentVo);
            //return new ModelAndView(new RedirectView("/segment/updateQueryPopup.do?segmentNo=" + segmentVo.getSegmentNo()));  // /segment/popupUpdateQuery.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 대상자 쿼리체크
     *
     * @param dbInfoSeq
     * @param query
     * @return
     */
    @RequestMapping(value="/segment/checkTargetQuery.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto checkTargetQuery(int dbInfoSeq, String query) {
        try {
            String returnMsg = sqlSegmentService.checkTargetQuery(dbInfoSeq, query);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(returnMsg);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 대상자 쿼리 - SQL 수정(팝업)
     *
     * @param segmentNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/targetQueryPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /segment/popupTargetQuery.do
    public ModelAndView list(int segmentNo) throws Exception {
        try {
            SegmentVo segmentVo = sqlSegmentService.selectSegmentByPk(segmentNo);

            ModelAndView mav = new ModelAndView("segment/targetQueryPopup");  // segment/sqlModify_popup
            mav.addObject("segmentVo", segmentVo);
            mav.addObject("segmentNo", segmentNo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 대상자 쿼리 - SQL 수정(팝업) - 수정 버튼
     *
     * @param segmentVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/updateTargetQuery.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView update(SegmentVo segmentVo) throws Exception {
        try {
            int result = sqlSegmentService.updateSegmentSql(segmentVo);
            String message = (result > 0) ? "update" : "fail";

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/targetQueryPopup.do"));  // /segment/popupTargetQuery.do
            mav.addObject("segmentNo", segmentVo.getSegmentNo());
            mav.addObject("message", message);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 테스트 쿼리 - SQL 수정(팝업)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/testQueryPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /segment/popupTestQuery.do
    public ModelAndView selectTestSql(int segmentNo) throws Exception {
        try {
            SegmentVo segmentVo = sqlSegmentService.selectSegmentByPk(segmentNo);

            ModelAndView mav = new ModelAndView("segment/testQueryPopup");  // segment/modify_test_popup
            mav.addObject("segmentVo", segmentVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 등록>1단계] 테스트 쿼리 - SQL 수정(팝업) - 수정 버튼
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/updateTestQuery.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView modifyTestSql(SegmentVo segmentVo) throws Exception {
        try {
            segmentService.updateTestQuery(segmentVo);

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/popupTestQuery.do?segmentNo=" + segmentVo.getSegmentNo()));
            mav.addObject("segmentVo", segmentVo);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
