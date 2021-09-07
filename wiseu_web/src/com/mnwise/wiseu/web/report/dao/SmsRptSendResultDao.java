package com.mnwise.wiseu.web.report.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.SendResultVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * NVSMSRPTSENDRESULT 테이블 DAO 클래스
 */
@Repository
public class SmsRptSendResultDao extends BaseDao {

    /**
     * 캠페인 월별 통계 데이터를 가져온다. SMS, LMS, MMS
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignSMSMonthlyStat(Map<String, Object> map) {
        return selectList("SmsRptSendResult.selectCampaignSMSMonthlyStat", map);
    }

    /**
     * 캠페인 일별 통계 데이터를 가져온다. (SMS., LMS, MMS)
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignSMSDailyStat(Map<String, Object> map) {
        return selectList("SmsRptSendResult.selectCampaignSMSDailyStat", map);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareSMSDailyReportList(Map<String, Object> paramMap) {
        return selectList("SmsRptSendResult.selectEcareSMSDailyReportList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareSMSScheduleDailyReportList(Map<String, Object> paramMap) {
        return selectList("SmsRptSendResult.selectEcareSMSScheduleDailyReportList", paramMap);
    }

    /**
     * 이케어 월별 통계 데이터를 가져온다.
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareSMSMonthlyStat(Map<String, Object> paramMap) {
        return selectList("SmsRptSendResult.selectEcareSMSMonthlyStat", paramMap);
    }

    /**
     * 이케어 SMS 일별 통계 데이터를 가져온다.
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareSMSDailyStat(Map<String, Object> paramMap) {
        return selectList("SmsRptSendResult.selectEcareSMSDailyStat", paramMap);
    }

    public RptSendResultVo selectEcareSMSScheduleSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return (RptSendResultVo) selectOne("SmsRptSendResult.selectEcareSMSScheduleSendResult", scenarioInfoVo);
    }

    public RptSendResultVo selectSMSSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        return (RptSendResultVo) selectOne("SmsRptSendResult.selectEcareSMSSendResult", scenarioInfoVo);
    }

}