package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MessageVo;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * NVECARESENDRESULT 테이블 DAO 클래스
 */
@Repository
public class EcareSendResultDao extends BaseDao {

    public int insertEcareSendResultByMessage(MessageVo messageVo) {
        return insert("EcareSendResult.insertEcareSendResultByMessage", messageVo);
    }

    public int selectEcareSendResultCountByPk(Map<String, Object> paramMap) {
        Integer count = (Integer) selectOne("EcareSendResult.selectEcareSendResultCountByPk", paramMap);
        return (count == null) ? 0 : count;
    }

    public int ecSuspendResultSeq(int ecareNo) {
        return (Integer) selectOne("EcareSendResult.ecSuspendResultSeq", ecareNo);
    }

    public Integer selectEcareSendResult(int ecareNo) {
        return (Integer) selectOne("EcareSendResult.selectEcareSendResult", ecareNo);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareRealtimeDailyReportList(Map<String,Object> paramMap) {
        return selectList("EcareSendResult.selectEcareRealtimeDailyReportList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareFaxDailyReportList(Map<String,Object> paramMap) {
        return selectList("EcareSendResult.selectEcareFaxDailyReportList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareScheduleDailyReportList(Map<String,Object> paramMap) {
        return selectList("EcareSendResult.selectEcareScheduleDailyReportList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareFaxScheduleDailyReportList(Map<String,Object> paramMap) {
        return selectList("EcareSendResult.selectEcareFaxScheduleDailyReportList", paramMap);
    }

    public RptSendResultVo selectRealtimeSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return (RptSendResultVo) selectOne("EcareSendResult.selectEcareRealtimeSendResult", scenarioInfoVo);
    }

    public RptSendResultVo selectFaxSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return (RptSendResultVo) selectOne("EcareSendResult.selectEcareFaxSendResult", scenarioInfoVo);
    }

    public RptSendResultVo selectScheduleSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return (RptSendResultVo) selectOne("EcareSendResult.selectEcareScheduleSendResult", scenarioInfoVo);
    }

    public RptSendResultVo selectEcareFaxScheduleSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return (RptSendResultVo) selectOne("EcareSendResult.selectEcareFaxScheduleSendResult", scenarioInfoVo);
    }

    /**
     * 이케어 월별 통계 데이터를 가져온다.
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareFAXMonthlyStat(Map<String, Object> paramMap) {
        return selectList("EcareSendResult.selectEcareFAXMonthlyStat", paramMap);
    }

    /**
     * 이케어 일별 통계 데이터를 가져온다.
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareFAXDailyStat(Map<String, Object> paramMap) {
        return selectList("EcareSendResult.selectEcareFAXDailyStat", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareAlimtalkDailyReportList(Map<String, Object> paramMap) {
        paramMap.put("altOkCdArray", PropertyUtil.getProperty("alimtalk.code.success").split(","));
        paramMap.put("smsOkCdArray", PropertyUtil.getProperty("alimtalk.other.code.success").split(","));
        return selectList("EcareSendResult.selectEcareAlimtalkDailyReportList", paramMap);
    }

    /**
     * 친구톡 일별 리포트 리스트 2018.03 wiseMOKA 병합
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareFriendtalkDailyReportList(Map<String, Object> paramMap) {
        paramMap.put("frtOkCdArray", PropertyUtil.getProperty("friendtalk.code.success").split(","));
        paramMap.put("smsOkCdArray", PropertyUtil.getProperty("friendtalk.other.code.success").split(","));
        return selectList("EcareSendResult.selectEcareFriendtalkDailyReportList", paramMap);
    }

}