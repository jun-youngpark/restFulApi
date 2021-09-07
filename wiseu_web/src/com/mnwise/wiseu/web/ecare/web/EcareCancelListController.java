package com.mnwise.wiseu.web.ecare.web;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.util.PageStringBean;
import com.mnwise.wiseu.web.base.util.PageStringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.service.CdMstService;
import com.mnwise.wiseu.web.ecare.model.CancelVo;
import com.mnwise.wiseu.web.ecare.model.EcarePerHistoryVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.ecare.service.EcareCancelServcie;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.report.model.EcareSendLogVo;
import com.mnwise.wiseu.web.report.service.ecare.EcareCommonService;
import com.mnwise.wiseu.web.rest.model.NrealtimeRequestVo;

/**
 * 이케어 취소이력 Controller
 * 발송예약된 I/F 대상자에 대해서 취소요청 이력을 출력한다.
 */
@Controller
public class EcareCancelListController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareCancelListController.class);
    
    @Autowired private EcareCancelServcie ecareCancelServcie;
    @Autowired private EcareCommonService ecareCommonService;
    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private CdMstService cdMstService;

    /**
     * [이케어>이케어 취소이력] 이케어 취소이력
     */
    @RequestMapping(value="/ecare/cancel_list.do", method={RequestMethod.GET})
    public ModelAndView list(EcarePerHistoryVo ecarePerHistoryVo, HttpServletRequest request) throws Exception {
        try {
            // 세션정보 담기
            UserVo userVo = getLoginUserVo();
            ModelAndView mav = new ModelAndView("ecare/ecare_cancel_list");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String maximumDate = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));   // 최대일자
            maximumDate = maximumDate.length() == 1 ? ("0" + maximumDate) : maximumDate;
            String now = DateUtil.getNowDateTime("yyyyMM");
            int nowPage = Integer.parseInt(ServletRequestUtils.getStringParameter(request, "nowPage", "1"));            // 현재 페이지
            
            String searchQstartDt = ServletRequestUtils.getStringParameter(request, "searchQstartDt", now + "01").replace("-", "");      // 취소요청일자 검색 시작일자
            String searchQendDt = ServletRequestUtils.getStringParameter(request, "searchQendDt", now + maximumDate).replace("-", "");   // 취소요청일자 검색 종료일자
            int ecareNo = Integer.parseInt(ServletRequestUtils.getStringParameter(request, "ecareNo", "0"));            // 이케어번호
            String seq = ServletRequestUtils.getStringParameter(request, "seq", "");                                    // SEQ
            String canFg = ServletRequestUtils.getStringParameter(request, "canFg", "");                                // 취소접수상태
            String rsltCd = ServletRequestUtils.getStringParameter(request, "rsltCd", "");                              // 취소결과
            
            CancelVo cancelVo = new CancelVo();
            cancelVo.setSearchQstartDt(searchQstartDt + "000000");
            cancelVo.setSearchQendDt(searchQendDt + "235959");
            cancelVo.setEcareNo(ecareNo);
            cancelVo.setOrgSeq(seq);
            cancelVo.setCanFg(canFg);
            cancelVo.setRsltCd(rsltCd);
            cancelVo.setListCountPerPage(userVo.getListCountPerPage());
            cancelVo.setNowPage(nowPage);
            
            // 페이징을 위한 기본 값 설정 - 현재 페이지, 전체 로우 카운트, 페이지당 출력 갯수
            PageStringBean pgBean = new PageStringBean();
            pgBean.setCurrentPage(nowPage);
            pgBean.setTotalRowCnt(ecareCancelServcie.selectCancelTotalCount(cancelVo));
            pgBean.setPageSize(cancelVo.getListCountPerPage());
            // 이케어 삭제 action 이후에 아래 파라미터를 제거하지 않으면 페이징 할때마다 alert문구가 생김
            pgBean.setNotUseParameter("deleteCount=");
            String paging = PageStringUtil.printPaging(request, "nowPage", pgBean);
            
            mav.addObject("rsltCdList", cdMstService.getCdMstList("AS0004", "ko"));
            mav.addObject("ecareNo", ecareNo);
            mav.addObject("seq", seq);
            mav.addObject("canFg", canFg);
            mav.addObject("rsltCd", rsltCd);
            mav.addObject("searchQstartDt", searchQstartDt);
            mav.addObject("searchQendDt", searchQendDt);
            mav.addObject("paging", paging);
            mav.addObject("cancelList", ecareCancelServcie.selectCancelList(cancelVo));   // 취소이력 조회
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>이케어 취소이력] 발송요청(인터페이스 대상자)정보 조회
     * @param seq       I/F 대상자 SEQ
     * @param reqDt     I/F 대상자 REQ_DT
     * @param ecareNo   I/F 대상자 ECARE_NO
     */
    @RequestMapping(value="/ecare/selectInterface.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<NrealtimeRequestVo> selectInterface(String seq, String reqDt, int ecareNo) {
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("seq", seq);
            paramMap.put("reqDt", reqDt);
            paramMap.put("ecareNo", ecareNo);
            EcareVo ecareVo = ecareCancelServcie.getEcareByEcareNo(ecareNo);
            if(ecareVo == null || ecareVo.getSubType() == null) {
                return null;
            }
            
            if(ecareVo.getSubType().equalsIgnoreCase("N")) {    // 준실시간
                return ecareCancelServcie.selectRealInterface(paramMap);
            }else { // 배치
                return ecareCancelServcie.selectBatchInterface(paramMap);
            }
            
        } catch (Exception e) {
            log.error(null, e);
        }
        return null;
    }
    
    /**
     * [이케어>이케어 취소이력] 발송이력 조회
     * @param seq       I/F 대상자 SEQ
     * @param reqDt     I/F 대상자 REQ_DT
     * @param ecareNo   I/F 대상자 ECARE_NO
     */
    @RequestMapping(value="/ecare/selectSendLog.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public EcareSendLogVo selectSendLog(int ecareNo, String resultSeq, String listSeq, String customerKey) {
        try {
            log.info("select sendlog : ecareNo={}, resultSeq={}, listSeq={}, customerKey={}", ecareNo, resultSeq, listSeq, customerKey);
            return ecareCancelServcie.selectEcareSendLogByPk(ecareNo, resultSeq, listSeq, customerKey);
        } catch (Exception e) {
            log.error(null, e);
        }
        return null;
    }

}
