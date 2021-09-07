package com.mnwise.wiseu.web.segment.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mnwise.wiseu.web.account.model.UserVo;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.segment.model.SegmentVo;
import com.mnwise.wiseu.web.segment.model.SemanticVo;
import com.mnwise.wiseu.web.segment.service.SqlSegmentService;
import com.mnwise.xsqlparser.SqlAnalyzer;

/**
 * 대상자 등록 2단계 Controller
 */
@Controller
public class Segment2StepController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Segment2StepController.class);

    @Autowired private SqlSegmentService sqlSegmentService;

    /**
     * [대상자>대상자 등록>2단계] 대상자 쿼리컬럼 지정 <br/>
     *
     * @param segmentVo
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/sqlSegment2step.do, /segment/target/sqlSegment2step.do
    @RequestMapping(value={"/segment/sqlSegment2Step.do", "/target/sqlSegment2Step.do"}, method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView nextPage(SegmentVo segmentVo, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("segment/sqlSegment2Step");  // segment/sqlSegment2step
        String shareYn = ServletRequestUtils.getStringParameter(request, "shareYn");
        String bookmark = ServletRequestUtils.getStringParameter(request, "bookmarkSeg");
        segmentVo.setDbinfoSeq(Integer.parseInt(ServletRequestUtils.getStringParameter(request, "dbInfoSeq")));
        segmentVo.setBookmarkSeg(bookmark != null ? "Y" : "N");
        segmentVo.setShareYn(shareYn != null ? "Y" : "N");
        try {
            if(segmentVo.getSegmentNo() == 0) {
                SegmentVo tmpSegmentVo = new SegmentVo();
                tmpSegmentVo = sqlSegmentService.getSemantic1step(segmentVo);
                segmentVo.setDbInfoList(tmpSegmentVo.getDbInfoList());
                segmentVo.setDbinfoSeq(tmpSegmentVo.getDbinfoSeq());
                segmentVo.setSemanticList(tmpSegmentVo.getSemanticList());
                mav.addObject("segmentVo", segmentVo);
            } else {
                SegmentVo returnSegmentVo = new SegmentVo();
                sqlSegmentService.saveSqlSegment1step(segmentVo);
                returnSegmentVo = sqlSegmentService.selectSegmentByPk(segmentVo.getSegmentNo());
                returnSegmentVo.setDbInfoVo(sqlSegmentService.selectDbInfo(segmentVo));
                returnSegmentVo.setUserId(getLoginId());
                returnSegmentVo.setSemanticList(sqlSegmentService.selectSemanticList(segmentVo.getSegmentNo()));
                mav.addObject("segmentVo", returnSegmentVo);
            }
        } catch(Exception e) {
            log.error(null, e);
            mav.setViewName("segment/sqlSegment1step");
            mav.addObject("error", "에러가 발생하였습니다.");
            mav.addObject("segmentVo", segmentVo);
            return mav;
        }

        if(request.getRequestURI().indexOf("target") > -1) {
            mav.addObject("actionGubun", "target");
            mav.addObject("action", "/target/saveSqlSegment2step.do");  // /segment/target/saveSqlSegment2step.do
        } else {
            mav.addObject("actionGubun", "");
            mav.addObject("action", "/segment/saveSqlSegment2step.do");  // /segment/saveSqlSegment2step.do
        }

        return mav;
    }

    /**
     * [대상자>대상자 등록>2단계] 저장 <br/>
     *
     * @param segmentVo
     * @param request
     * @return
     * @throws Exception
     */
    // /segment/saveSqlSegment2step.do, /segment/target/saveSqlSegment2step.do
    @RequestMapping(value={"/segment/saveSqlSegment2step.do", "/target/saveSqlSegment2step.do"}, method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView list(SegmentVo segmentVo, HttpServletRequest request) throws Exception {
        try {
            UserVo userVo = getLoginUserVo();
            segmentVo.setSegmentType("N");
            segmentVo.setActiveYn("Y");
            segmentVo.setSegType("Q");
            segmentVo.setUserId(userVo.getUserId());
            segmentVo.setGrpCd(userVo.getGrpCd());
            segmentVo.setEditorId(userVo.getUserId());
            if(segmentVo.getSegmentNo() == 0) {
                String sqlQuery = segmentVo.getSqlContext();
                SqlAnalyzer sa = new SqlAnalyzer();
                if(!sa.parse(sqlQuery)) {
                    segmentVo.setSqlHead(sqlQuery);
                } else {
                    segmentVo.setSqlHead(sa.getSqlHead());
                    segmentVo.setSqlBody(sa.getSqlBody());
                    segmentVo.setSqlTail(sa.getSqlTail());
                }
                segmentVo.setFileToDbYn("N");
                segmentVo.setSegmentSize(this.sqlSegmentService.getSegmentSize(segmentVo));
                // default content save
                segmentVo.setSegmentNo(this.sqlSegmentService.saveSqlSegment1step(segmentVo));
                List<SemanticVo> semanticList = new ArrayList<>();
                String[] fieldNmList = ServletRequestUtils.getStringParameters(request, "fieldNm");
                Map<String, String> fieldNmMap = new HashMap<>();
                String[] fieldKeyList = getFieldKeyList(request, fieldNmList.length);
                Map<String, String> fieldKeyMap = new HashMap<>();

                for(int i = 0; i < fieldKeyList.length; i++) {
                    String key = fieldKeyList[i].substring(0, fieldKeyList[i].indexOf("_"));
                    String seq = fieldKeyList[i].substring(fieldKeyList[i].indexOf("_") + 1);
                    log.debug("key : " + key + ", seq : " + seq);
                    fieldKeyMap.put(seq, key);
                }

                for(int i = 0; i < fieldNmList.length; i++) {
                    String key = fieldNmList[i].substring(0, fieldNmList[i].lastIndexOf("_"));
                    String seq = fieldNmList[i].substring(fieldNmList[i].lastIndexOf("_") + 1);
                    log.debug("key : " + key + ", seq : " + seq);
                    fieldNmMap.put(seq, key);
                }

                for(Iterator<String> it = fieldNmMap.keySet().iterator(); it.hasNext();) {
                    String seq = (String) it.next();
                    SemanticVo tempVo = new SemanticVo();
                    tempVo.setFieldSeq(Integer.parseInt(seq));
                    tempVo.setFieldNm((String) fieldNmMap.get(seq));
                    tempVo.setFieldKey((String) fieldKeyMap.get(seq));
                    tempVo.setFieldDesc(ServletRequestUtils.getStringParameter(request, "fieldDesc_" + seq));

                    semanticList.add(tempVo);
                }
                segmentVo.setSemanticList(semanticList);

                try {
                    sqlSegmentService.saveSqlSegment2step(segmentVo);
                    sqlSegmentService.saveBookMarkSeg(segmentVo);
                    //mav.addObject 데이터를 step1에서 사용하지않는것으로 판단되어 주석처리(다른언어 지원시 인코딩방식이 깨질수있기때문.)
                    //mav.addObject("message", "새로운 대상자가 저장되었습니다.");
                } catch(Exception e) {
                    log.error(null, e);
                    //mav.addObject("message", "새로운 대상자의 등록에 실패하였습니다.");
                }
            }

            // /segment/sqlSegment1step.do, /segment/target/sqlSegment1step.do
            String action = (request.getRequestURI().indexOf("target") > -1) ? "/target/sqlSegment1Step.do" : "/segment/sqlSegment1Step.do";

            ModelAndView mav = new ModelAndView(new RedirectView(action + "?segmentNo=" + segmentVo.getSegmentNo()));
            mav.addObject("action", action);
            return mav;
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    private String[] getFieldKeyList(HttpServletRequest request, int length) throws ServletRequestBindingException {
        String[] fieldKeyList = new String[length];

        for (int i = 1; i <= length; i++) {
            fieldKeyList[i - 1] = ServletRequestUtils.getStringParameter(request, "fieldKey_" + i);
        }

        return fieldKeyList;
    }
}
