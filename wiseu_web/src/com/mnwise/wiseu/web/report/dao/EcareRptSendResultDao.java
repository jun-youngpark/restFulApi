package com.mnwise.wiseu.web.report.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * NVECARERPTSENDRESULT 테이블 DAO 클래스
 */
@Repository
public class EcareRptSendResultDao extends BaseDao {

    public int insertEcareDailyScheduleReceiptList(Map<String, Object> paramMap) {
        return insert("EcareRptSendResult.insertEcareDailyScheduleReceiptList", paramMap);
    }

    public int insertEcareDailyRealtimeReceiptList(Map<String, Object> paramMap) {
        return insert("EcareRptSendResult.insertEcareDailyRealtimeReceiptList", paramMap);
    }

    public int updateEcareDailyScheduleReceiptList(Map<String, Object> paramMap) {
        return update("EcareRptSendResult.updateEcareDailyScheduleReceiptList", paramMap);
    }

    public int updateEcareDailyRealtimeReceiptList(Map<String, Object> paramMap) {
        return update("EcareRptSendResult.updateEcareDailyRealtimeReceiptList", paramMap);
    }

    /**
     * 이케어 일별발송현황 수신확인 데이터 확인-스케쥴
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectEcareDailyScheduleReceiptList(Map<String, Object> paramMap) {
        return selectList("EcareRptSendResult.selectEcareDailyScheduleReceiptList", paramMap);
    }

    /**
     * 이케어 일별발송현황 수신확인 데이터 확인-실시간
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> selectEcareDailyRealtimeReceiptList(Map<String, Object> paramMap) {
        return selectList("EcareRptSendResult.selectEcareDailyRealtimeReceiptList", paramMap);
    }

    /**
     * 이케어 월별 통계 데이터를 가져온다.
     *
     * @param paramMap
 * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareMonthlyStat(Map<String, Object> paramMap) {
        return selectList("EcareRptSendResult.selectEcareMonthlyStat", paramMap);
    }

    /**
     * 이케어 일별 통계 데이터를 가져온다.
     *
     * @param paramMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RptSendResultVo> selectEcareDailyStat(Map<String, Object> paramMap) {
        return selectList("EcareRptSendResult.selectEcareDailyStat", paramMap);
    }
}