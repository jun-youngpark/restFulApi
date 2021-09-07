package com.mnwise.wiseu.web.report.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.SendResultVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.report.model.ecare.RealtimeacceptVo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * NVREALTIMEACCEPT, NVBATCH 테이블 DAO 클래스
 */
@Repository
public class RequestTableDao extends BaseDao {

    /**
     * NVREALTIMEACCEPT 의 건 수 출력 
     *
     * @param RealtimeacceptVo
     * @return int
     */
    public int selectRealtimeacceptCount(RealtimeacceptVo realtimeacceptVo) {
        return (int) selectOne("RequestTable.selectRealtimeacceptCount", realtimeacceptVo);
    }

    /**
     * NVBATCH 의 건 수 출력 
     *
     * @param RealtimeacceptVo
     * @return int 
     */
    public int selectBatchCount(RealtimeacceptVo realtimeacceptVo) {
        return (int) selectOne("RequestTable.selectBatchCount", realtimeacceptVo);
    }

    /**
     * NVBATCH 와 NVREALTIMEACCEPT 의 건 수 출력 
     *
     * @param RealtimeacceptVo
     * @return omt 
     */
    public int selectAllInterfaceCount(RealtimeacceptVo realtimeacceptVo) {
        return (int) selectOne("RequestTable.selectAllInterfaceCount", realtimeacceptVo);
    }
    
    /**
     * NVBATCH 와 NVREALTIMEACCEPT 데이터 출력
     *
     * @param RealtimeacceptVo
     * @return List<RealtimeacceptVo>
     */
    @SuppressWarnings("unchecked")
    public List<RealtimeacceptVo> selectAllInterfaceList(RealtimeacceptVo realtimeacceptVo) {
        return selectList("RequestTable.selectAllInterfaceList", realtimeacceptVo);
    }
    
    /**
     * NVREALTIMEACCEPT 데이터 출력
     * 
     * @param RealtimeacceptVo
     * @return List<RealtimeacceptVo>
     */
    @SuppressWarnings("unchecked")
    public List<RealtimeacceptVo> selectRealtimeacceptList(RealtimeacceptVo realtimeacceptVo) {
        return selectList("RequestTable.selectRealtimeacceptList", realtimeacceptVo);
    }

    /**
     * NVBATCH 데이터 출력
     *
     * @param map
     * @return List<RealtimeacceptVo>
     */
    @SuppressWarnings("unchecked")
    public List<RealtimeacceptVo> selectBatchList(RealtimeacceptVo realtimeacceptVo) {
        return selectList("RequestTable.selectBatchList", realtimeacceptVo);
    }
    
    public int insertResendOne(RealtimeacceptVo realtimeacceptVo) {
        return update("RequestTable.insertResendOne", realtimeacceptVo);
    }
    public int insertResendOneBatch(RealtimeacceptVo realtimeacceptVo) {
        return update("RequestTable.insertResendOneBatch", realtimeacceptVo);
    }
    public int updateCancelSend(RealtimeacceptVo realtimeacceptVo) {
        return update("RequestTable.updateCancelSend", realtimeacceptVo);
    }
    public int updateCancelSendAll(RealtimeacceptVo realtimeacceptVo) {
        return update("RequestTable.updateCancelSendAll", realtimeacceptVo);
    }
    public String seleceSubType(String ecareNo) {
        return (String) selectOne("RequestTable.seleceSubType", ecareNo);
    }
    
}