package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.ecare.model.EcarePerHistoryVo;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.resend.model.LstResendVo;

/**
 * NVECARESENDLOG 테이블 DAO 클래스
 */
@Repository
public class EcareSendLogDao extends BaseDao {

    public String selectFaxSrfidd(Map<String, Object> paramMap) {
        return (String) selectOne("EcareSendLog.selectFaxSrfidd", paramMap);
    }

    /**
     * 이케어 리포트 PUSH OS별 발송 정보
     * @param serviceNo
     * @param resultSeq
     * @param startDt
     * @param endDt
     * @return
     */
    public CaseInsensitiveMap selectPushEcareSendlogCount(int serviceNo, String resultSeq, String startDt, String endDt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("service_no", serviceNo);
        paramMap.put("result_seq", resultSeq);
        paramMap.put("start_dt", startDt);
        paramMap.put("end_dt", endDt);
        return (CaseInsensitiveMap) selectOne("EcareSendLog.selectPushEcareSendlogCount", paramMap);
    }

    /**
     * PUSH 에러 코드 - ECARE
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectEcarePushErrorReportList(Map<String, Object> paramMap) {
        paramMap.put("successCodeArray", PropertyUtil.getProperty("push.code.success", "000").split(","));
        return selectList("EcareSendLog.selectEcarePushErrorReportList",paramMap);
    }

    /**
     * 검색된 E-MAIL 의 총 대상자 수를 구해온다.
     */
    public int selectTotalCount(EcarePerHistoryVo historyVo) {
        Integer count = (Integer) selectOne("EcareSendLog.selectEcareTotalCount", historyVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 검색된 이메일 발송고객이력리스트를 가져온다.
     */
    @SuppressWarnings("rawtypes")
    public List selectPerHistoryList(EcarePerHistoryVo historyVo) {
        List tmp = selectList("EcareSendLog.selectEcarePerHistoryList", historyVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    public LstResendVo selectEcarePrevious(LstResendVo historyVo) {
        LstResendVo lstResend = (LstResendVo) selectOne("EcareSendLog.selectEcarePrevious", historyVo);
        security.securityObject(lstResend, "DECRYPT");
        return lstResend;
    }

    public int selectEcareResendTargetCount(Map<String, Object> paramMap) {
        Integer count = (Integer) selectOne("EcareSendLog.selectEcareResendTargetCount", paramMap);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectFaxLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareSendLog.selectFaxLogList", scenarioInfoVo);
    }

    /**
     * 카카오톡 오류분석
     *
     * @param scenarioInfoVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectKakaoLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareSendLog.selectKakaoLogList", scenarioInfoVo);
    }

}