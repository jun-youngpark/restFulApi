package com.mnwise.wiseu.web.segment.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.account.model.json.TreeEelement;
import com.mnwise.wiseu.web.account.service.AccountService;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.env.model.TreeNode;
import com.mnwise.wiseu.web.env.model.TreeNodeState;
import com.mnwise.wiseu.web.segment.model.DbInfoVo;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.segment.model.TargetVo;
import com.mnwise.wiseu.web.segment.service.SegmentService;
import com.mnwise.wiseu.web.segment.service.SqlSegmentService;

/**
 * 대상자 리스트 Controller
 */
@Controller
public class SegmentListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SegmentListController.class);

    @Autowired private AccountService accountService;
    @Autowired private SegmentService segmentService;
    @Autowired private SqlSegmentService sqlSegmentService;

    /**
     * [대상자>대상자 조회] 대상자 리스트
     */
    @RequestMapping(value="/segment/segmentList.do", method={RequestMethod.GET, RequestMethod.POST})  // /segment/segment.do
    public ModelAndView list(SegmentVo segmentVo, @RequestParam(defaultValue="-1") int tagNo, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            segmentVo.setUserId(userVo.getUserId());
            segmentVo.setUserVo(userVo);
            segmentVo.setCountPerPage(userVo.getListCountPerPage());
            segmentVo.setEndRow(userVo.getListCountPerPage() * segmentVo.getNowPage());
            segmentVo.setTagNo(tagNo);
            if(StringUtil.isBlank(segmentVo.getOrderColumn())) {
                segmentVo.setOrderColumn("S.SEGMENT_NO");
                segmentVo.setOrderSort("DESC");
            }

            // 페이징을 위해 리스트의 TOTAL ROW 를 가져온다.
            int segmentListTotalCount = segmentService.getSegmentListTotalCount(segmentVo);
            int aCountPerPage = userVo.getListCountPerPage();
            int qCountPerPage = (segmentListTotalCount / aCountPerPage); // 몫
            int rCountPerPage = (segmentListTotalCount % aCountPerPage); // 나머지

            if(segmentVo.getNowPage() <= qCountPerPage) {
                segmentVo.setListCnt(userVo.getListCountPerPage());
            } else {
                segmentVo.setListCnt(rCountPerPage);
            }

            // TOTAL COUNT 가 존재할 경우 리스트를 가져온다.
            List<SegmentVo> segmentList = null;
            if(segmentListTotalCount > 0) {
                segmentList = segmentService.selectSegmentList(segmentVo);
            }

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(segmentVo.getNowPage());
            pgBean.setTotalRowCnt(segmentListTotalCount);
            pgBean.setPageSize(segmentVo.getCountPerPage());
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            ModelAndView mav = new ModelAndView("segment/segmentList");  // segment/segment_list
            mav.addObject("segmentListTotalCount", String.valueOf(segmentListTotalCount));
            mav.addObject("segmentList", segmentList);
            mav.addObject("segmentVo", segmentVo);
            mav.addObject("paging", paging);
            mav.addObject("searchWord", segmentVo.getSearchWord());
            mav.addObject("webExecMode", super.webExecMode);
            PagingUtil.transferPagingInfo(request, segmentListTotalCount);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회] 대상자 리스트 - 수행이력
     *
     * @param segmentVo
     * @return
     */
    @RequestMapping(value="/segment/usingHistoryPopup.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView popupUsing(SegmentVo segmentVo) {
        try {
            ModelAndView mav = new ModelAndView("segment/usingHistoryPopup");
            mav.addObject("usingList", segmentService.selectUsingList(segmentVo, getLoginUserVo()));
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회] 대상자 보기(팝업)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/targetListPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /segment/segmentpopup.do
    public ModelAndView targetList(int segmentNo, @RequestParam(defaultValue="1") int nowPage, String searchWord, String searchColumn
            ,@RequestParam(defaultValue="em")String serviceType, String gotoList, String message, HttpServletRequest request) throws Exception {
        try {
            // 선택한 대상자의 기본정보를 가져온다.
            SegmentVo segmentVo = sqlSegmentService.selectSegmentByPk(segmentNo);
            segmentVo.setSegmentNo(segmentNo);
            segmentVo.setSearchWord(searchWord);
            segmentVo.setSearchColumn(searchColumn);

            // 개인 세션 정보
            UserVo userVo = getLoginUserVo();
            segmentVo.setUserId(userVo.getUserId());
            segmentVo.setUserVo(userVo);
            segmentVo.setCountPerPage(userVo.getListCountPerPage());
            segmentVo.setNowPage(nowPage);

            // 대상자 전체 count 를 가져온다.
            int targetListTotalCount = segmentService.selectTargetListTotalCount(segmentVo);

            // Semantic 정보를 가져온다.
            List<SemanticVo> semanticList = segmentService.getSementicInfoForTarget(segmentVo);

            // 대상자의 DB정보를 가져온다.
            DbInfoVo dbInfoVo = segmentService.getDbInfo(segmentVo.getDbinfoSeq());

            ModelAndView mav = new ModelAndView("segment/targetListPopup");  // segment/target_list
            // 대상자 전체 정보를 가져온다.
            if(dbInfoVo == null) { // 디비정보가 없을경우 return;
                mav.addObject("message", "fail");
                return mav;
            }

            String[] header = null;
            boolean startFlag = false;

            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(segmentVo.getNowPage());
            pgBean.setPageSize(segmentVo.getCountPerPage());
            pgBean.setNotUseParameter("message=");

            // 검색을 시도했을때만 리스트 목록 보여줌
            if(request.getMethod().equals("POST") || "Y".equals(gotoList)) {
                startFlag = true;

                Map<String, Object> target = segmentService.selectTargetList(segmentVo);

                header = (String[]) target.get("header");
                mav.addObject("header", header);
                mav.addObject("queryDataList", target.get("queryDataList"));

                // 검색을 시도했을 경우만 리스트가 나타나므로, 페이지의 총 Row 갯수는 검색을 시도했을 경우에만 넣어준다.
                pgBean.setTotalRowCnt(targetListTotalCount);
            }
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);

            // 대상자의 타입이 F 인 파일업로드형일 경우 TARGET 번호를 찾는다. TARGET 번호로 대상자 수정,추가를 하게 된다.
            if(segmentVo.getSegType() != null && segmentVo.getSegType().equalsIgnoreCase("F")) {
                String sqlBody = (segmentVo.getSqlBody().trim()).replaceAll(" ", "");
                String targetNo = sqlBody.replaceAll("WHERETARGET_NO=", "");

                mav.addObject("targetNo", targetNo);
            }
            mav.addObject("startFlag", startFlag);
            mav.addObject("paging", paging);
            mav.addObject("segmentVo", segmentVo);
            //mav.addObject("segmentNo", segmentNo);
            mav.addObject("dbInfoVo", dbInfoVo);
            mav.addObject("sementicList", semanticList);
            mav.addObject("semancticHeaders", header);
            mav.addObject("colCnt", semanticList.size());
            // mav.addObject("colCnt", qe.getColumnHeader().length);
            mav.addObject("targetListTotalCount", targetListTotalCount);
            mav.addObject("message", message);  // 대상자 추가/수정/삭제 결과 메시지 출력
            mav.addObject("serviceType", serviceType);  // 이케어 캠페인 구분
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회>대상자 보기(팝업)] 저장 버튼 - 파일 대상자 추가
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/insertTarget.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView targetInsert(HttpServletRequest request) throws Exception {
        try {
            TargetVo targetVo = new TargetVo();
            targetVo.setTargetNo(ServletRequestUtils.getIntParameter(request, "targetNo", 0));
            targetVo.setCustomerId(StringUtil.defaultString(request.getParameter("CUSTOMER_ID"), null));
            targetVo.setCustomerNm(StringUtil.defaultString(request.getParameter("CUSTOMER_NM"), null));
            targetVo.setCustomerEmail(StringUtil.defaultString(request.getParameter("CUSTOMER_EMAIL"), null));
            targetVo.setCustomerTel(StringUtil.defaultString(request.getParameter("CUSTOMER_TEL"), null));
            targetVo.setCustomerSlot1(StringUtil.defaultString(request.getParameter("CUSTOMER_SLOT1"), null));
            targetVo.setCustomerSlot2(StringUtil.defaultString(request.getParameter("CUSTOMER_SLOT2"), null));
            targetVo.setCallback(StringUtil.defaultString(request.getParameter("CALL_BACK"), null));
            targetVo.setSenderNm(StringUtil.defaultString(request.getParameter("SENDER_NM"), null));
            targetVo.setSenderEmail(StringUtil.defaultString(request.getParameter("SENDER_EMAIL"), null));
            targetVo.setRetmailReceiver(StringUtil.defaultString(request.getParameter("RETMAIL_RECEIVER"), null));
            targetVo.setCustomerFax(StringUtil.defaultString(request.getParameter("CUSTOMER_FAX"), null));
            targetVo.setSentence(StringUtil.defaultString(request.getParameter("SENTENCE"), null));
            targetVo.setSeg(StringUtil.defaultString(request.getParameter("SEG"), null));
            targetVo.setSlot1(StringUtil.defaultString(request.getParameter("SLOT1"), null));
            targetVo.setSlot2(StringUtil.defaultString(request.getParameter("SLOT2"), null));
            targetVo.setSlot3(StringUtil.defaultString(request.getParameter("SLOT3"), null));
            targetVo.setSlot4(StringUtil.defaultString(request.getParameter("SLOT4"), null));
            targetVo.setSlot5(StringUtil.defaultString(request.getParameter("SLOT5"), null));
            targetVo.setSlot6(StringUtil.defaultString(request.getParameter("SLOT6"), null));

            int nowPage = ServletRequestUtils.getIntParameter(request, "nowPage", 1);
            int segmentNo = ServletRequestUtils.getIntParameter(request, "segmentNo", 0);
            String segType = StringUtil.defaultString(request.getParameter("segType"));
            String gotoList = StringUtil.defaultString(request.getParameter("gotoList"));

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/targetListPopup.do"));  // /segment/segmentpopup.do
            // 입력받은 customerId값이 중복인지 여부를 체크한다.
            int check = segmentService.getCustomerIdCount(targetVo.getTargetNo(), targetVo.getCustomerId());
            if(check > 0) {
                // 중복된 ID값이 존재할 경우
                mav.addObject("message", "checkFail");
            } else { // 중복된 ID값이 존재하지 않을 경우

                // 대상자를 저장한다.
                segmentService.insertSegmentTarget(targetVo);

                // 대상자 숫자를 갱신한다.
                segmentService.renewalSegment(segmentNo);

                mav.addObject("message", "success");
            }

            mav.addObject("segmentNo", segmentNo);
            mav.addObject("nowPage", nowPage);
            mav.addObject("segType", segType);
            mav.addObject("gotoList", gotoList);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회>대상자 보기(팝업)] 저장 버튼 - 파일 대상자 수정
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/updateTarget.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView targetModify(HttpServletRequest request) throws Exception {
        try {
            TargetVo targetVo = new TargetVo();

            int nowPage = ServletRequestUtils.getIntParameter(request, "nowPage", 1);
            int segmentNo = ServletRequestUtils.getIntParameter(request, "segmentNo", 0);
            String segType = StringUtil.defaultString(request.getParameter("segType"));
            String gotoList = StringUtil.defaultString(request.getParameter("gotoList"));

            targetVo.setTargetNo(ServletRequestUtils.getIntParameter(request, "targetNo", 0));
            targetVo.setCustomerId(StringUtil.defaultString(request.getParameter("CUSTOMER_ID"), null));
            targetVo.setCustomerNm(StringUtil.defaultString(request.getParameter("CUSTOMER_NM"), null));
            targetVo.setCustomerEmail(StringUtil.defaultString(request.getParameter("CUSTOMER_EMAIL"), null));
            targetVo.setCustomerTel(StringUtil.defaultString(request.getParameter("CUSTOMER_TEL"), null));
            targetVo.setCustomerSlot1(StringUtil.defaultString(request.getParameter("CUSTOMER_SLOT1"), null));
            targetVo.setCustomerSlot2(StringUtil.defaultString(request.getParameter("CUSTOMER_SLOT2"), null));
            targetVo.setCallback(StringUtil.defaultString(request.getParameter("CALL_BACK"), null));
            targetVo.setSenderNm(StringUtil.defaultString(request.getParameter("SENDER_NM"), null));
            targetVo.setSenderEmail(StringUtil.defaultString(request.getParameter("SENDER_EMAIL"), null));
            targetVo.setRetmailReceiver(StringUtil.defaultString(request.getParameter("RETMAIL_RECEIVER"), null));
            targetVo.setCustomerFax(StringUtil.defaultString(request.getParameter("CUSTOMER_FAX"), null));
            targetVo.setSentence(StringUtil.defaultString(request.getParameter("SENTENCE"), null));
            targetVo.setSeg(StringUtil.defaultString(request.getParameter("SEG"), null));
            targetVo.setSlot1(StringUtil.defaultString(request.getParameter("SLOT1"), null));
            targetVo.setSlot2(StringUtil.defaultString(request.getParameter("SLOT2"), null));
            targetVo.setSlot3(StringUtil.defaultString(request.getParameter("SLOT3"), null));
            targetVo.setSlot4(StringUtil.defaultString(request.getParameter("SLOT4"), null));
            targetVo.setSlot5(StringUtil.defaultString(request.getParameter("SLOT5"), null));
            targetVo.setSlot6(StringUtil.defaultString(request.getParameter("SLOT6"), null));

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/targetListPopup.do"));  // /segment/segmentpopup.do
            int result = segmentService.updateSegmentTarget(targetVo);
            if(result == 0) {
                mav.addObject("message", "updateFail");
            } else {
                mav.addObject("message", "success");
            }
            mav.addObject("segmentNo", segmentNo);
            mav.addObject("nowPage", nowPage);
            mav.addObject("segType", segType);
            mav.addObject("gotoList", gotoList);

            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회>대상자 보기(팝업)] 삭제 버튼 - 파일 대상자 삭제
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/deleteTarget.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView targetDelete(HttpServletRequest request) throws Exception {
        try {
            TargetVo targetVo = new TargetVo();
            targetVo.setTargetNo(ServletRequestUtils.getIntParameter(request, "targetNo", 0));
            targetVo.setCustomerId(StringUtil.defaultString(request.getParameter("CUSTOMER_ID"), null));

            int nowPage = ServletRequestUtils.getIntParameter(request, "nowPage", 1);
            int segmentNo = ServletRequestUtils.getIntParameter(request, "segmentNo", 0);
            String segType = StringUtil.defaultString(request.getParameter("segType"));
            String gotoList = StringUtil.defaultString(request.getParameter("gotoList"));

            ModelAndView mav = new ModelAndView(new RedirectView("/segment/targetListPopup.do"));  // /segment/segmentpopup.do
            int result = segmentService.deleteSegmentTarget(targetVo.getTargetNo(), targetVo.getCustomerId());
            if(result == 0) {
                mav.addObject("message", "updateFail");
            } else {
                mav.addObject("message", "deleteSuccess");
            }

            // 대상자 숫자를 갱신한다.
            segmentService.renewalSegment(segmentNo);

            mav.addObject("segmentNo", segmentNo);
            mav.addObject("nowPage", nowPage);
            mav.addObject("segType", segType);
            mav.addObject("gotoList", gotoList);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회] 대상자 리스트 - 대상자 갱신
     *
     * @param segmentNo
     * @return
     */
    @RequestMapping(value="/segment/renewalSegment.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public SegmentVo renewalSegment(int segmentNo) {
        try {
            return segmentService.renewalSegment(segmentNo);
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

    /**
     * [대상자>대상자 조회] 대상자 리스트 - 삭제 - 사전 체크
     *
     * @param segmentNo
     * @return
     */
    @RequestMapping(value="/segment/deleteCheck.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto deleteCheck(int segmentNo) {
        try {
            int result = sqlSegmentService.deleteCheck(segmentNo, getLoginUserVo());
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(result + "");
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [대상자>대상자 조회] 대상자 리스트 - 삭제
     *
     * @param segmentNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/deleteSegment.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView delete(int segmentNo) throws Exception {
        try {
            SegmentVo segmentVo = sqlSegmentService.selectSegmentByPk(segmentNo);
            if(segmentVo.getSegType().equals("S")) {
                sqlSegmentService.deleteGenealogy(segmentNo);
            }
            segmentService.deleteSegmentCheckBySegmentNo(segmentNo);
            segmentService.deleteSegment(segmentNo);

            return new ModelAndView(new RedirectView("/segment/segmentList.do"));  // /segment/segment.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회] 대상자 리스트 - 삭제
     * 세그먼트 리스트에서 발송내역이 있을경우 DB에서 삭제가 불가능한데 ACTIVE_YN 의 FLAG 처리를 통하여 리스트에서 없앨수 있다.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/updateUnactive.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView updateUnactive(int segmentNo) throws Exception {
        try {
            segmentService.updateListDelete(segmentNo);
            return new ModelAndView(new RedirectView("/segment/segmentList.do"));  // /segment/segment.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회] 대상자 리스트 - 권한할당(팝업)
     *
     * @param segmentNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/permitAssignPopup.do", method={RequestMethod.GET, RequestMethod.POST})  // /segment/popupPermission.do
    public ModelAndView permission(int segmentNo) throws Exception {
        try {
            SegmentVo segmentVo = new SegmentVo();
            segmentVo.setSegmentNo(segmentNo);

            ModelAndView mav = new ModelAndView("segment/permitAssignPopup");  // segment/permission
            mav.addObject("segmentVo", segmentVo);
            mav.addObject("checkSegmentVo", segmentService.assignPermission(segmentNo));
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [대상자>대상자 조회>권한할당(팝업)] 선택 버튼
     *
     * @param segmentVo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/savePermission.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto savePermission(SegmentVo segmentVo, HttpServletRequest request) {
        try {
            segmentVo.setUserId(getLoginId());
            segmentService.permissionSegment(segmentVo);

            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [대상자>대상자 조회>권한할당(팝업)] 부서트리 출력
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/permissionTree.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody ArrayList<TreeNode> tree(HttpServletRequest request) throws Exception {
        try {
            String grpCd = ServletRequestUtils.getStringParameter(request, "grpCd");
            TreeEelement treeEelement = new TreeEelement();
            treeEelement.setId(grpCd);
            Integer segmentNo = ServletRequestUtils.getIntParameter(request, "segmentNo");
            SegmentVo segmentVo = new SegmentVo();
            segmentVo.setSegmentNo(segmentNo);
            List<SegmentVo> checkSegment = segmentService.assignPermission(segmentNo);
            List<TreeEelement> groupList = this.accountService.selectGroup(treeEelement);
            //List<UserGrpVo> groupList = accountService.getGrpList();
            ArrayList<TreeNode> treeList = new ArrayList<TreeNode>();
            for(TreeEelement tree : groupList) {
                TreeNodeState state =new TreeNodeState();
                for(SegmentVo segment : checkSegment) {
                    if(tree.getId().equals(segment.getGrpCd())) {
                        if(tree.getSuperId().equals("#")){
                            state.setOpened(true);
                        }else {
                            state.setSelected(true);
                        }
                        int chk = sqlSegmentService.deleteCheck(segmentNo.intValue(), getLoginUserVo());
                        if(chk == 1 || chk == 2 || chk == 3) {
                            state.setDisabled(true);
                        }
                    }
                }
                treeList.add(new TreeNode(tree.getId(),tree.getText(), tree.getSuperId() ,tree.getHasChildren(), state));
            }

            return treeList;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }


    /**
     * [대상자>대상자 조회] 대상자 리스트 - 대상자 복사
     *
     * @param segmentNo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/segment/copySegment.do", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView copySegment(int segmentNo, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();

            SegmentVo segmentVo = new SegmentVo();
            segmentVo.setSegmentNo(segmentNo);
            segmentVo.setUserId(userVo.getUserId());
            segmentVo.setGrpCd(userVo.getGrpCd());
            segmentService.setRegistSegmentCopy(segmentVo);

            return new ModelAndView(new RedirectView("/segment/segmentList.do"));  // /segment/segment.do
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
}
