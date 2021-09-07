package com.mnwise.wiseu.web.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.SendResultVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

/**
 * NVKAKAORPTSENDRESULT 테이블 DAO 클래스
 */
@Repository
public class KakaoRptSendResultDao extends BaseDao {

    /**
     * 캠페인 월별 통계 데이터를 가져온다. 카카오톡
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendResultVo> selectCampaignKakaoMonthlyStat(Map<String, Object> paramMap) {
        return selectList("KakaoRptSendResult.selectCampaignKakaoMonthlyStat", paramMap);
    }

    /**
     * 브랜드톡 일별 리포트 리스트 2020.06
     * TODO: 카카오 모두 사용
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareKakaoDailyReportList(Map<String, Object> paramMap) {
        return selectList("KakaoRptSendResult.selectEcareKakaoDailyReportList", paramMap);
    }

    /**
     * 카카오 월별 통계(브랜드톡,알림톡,친구톡)
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareKakaoMonthlyStat(Map<String, Object> paramMap) {
        return selectList("KakaoRptSendResult.selectEcareKakaoMonthlyStat", paramMap);
    }

    /**
     * 카카오 월별 통계(브랜드톡,알림톡,친구톡)
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareKakaoDailyStat(Map<String, Object> paramMap) {
        return selectList("KakaoRptSendResult.selectEcareKakaoDailyStat", paramMap);
    }

    /**
     * 알림톡/친구톡/브랜드톡 사용
     *
     * @param scenarioInfoVo
     * @return
     */
    public RptSendResultVo selectKakaoSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecare_no", scenarioInfoVo.getEcareInfoVo().getEcareNo());
        paramMap.put("report_dt", scenarioInfoVo.getEcareInfoVo().getReportDt());
        paramMap.put("isDaily", ("N".equals(scenarioInfoVo.getEcareInfoVo().getSubType()) || "R".equals(scenarioInfoVo.getEcareInfoVo().getServiceType())) ? "Y" : "N");
        paramMap.put("result_seq", scenarioInfoVo.getEcareInfoVo().getResultSeq());
        return (RptSendResultVo) selectOne("KakaoRptSendResult.selectKakaoSendResult", paramMap);
    }

}