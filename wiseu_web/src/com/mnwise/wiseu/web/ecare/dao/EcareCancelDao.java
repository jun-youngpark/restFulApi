package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.CancelVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.EcareSendLogVo;
import com.mnwise.wiseu.web.report.model.ecare.RealtimeacceptVo;
import com.mnwise.wiseu.web.rest.model.NrealtimeRequestVo;

/**
 * NVCANCEL 테이블 DAO 클래스
 */
@Repository
public class EcareCancelDao extends BaseDao {
    /**
     * 취소처리 진행
     * @param cancelVo  취소클래스 객체
     * @return  등록건수
     */
    public int insertCancel(CancelVo cancelVo) {
        return insert("EcareCancel.insertCancel", cancelVo);    // EcareCancel
    }
    
    /**
     * 다중건 취소처리 진행
     * @param cancelVo  취소클래스 객체
     * @return  등록건수
     */
    public int insertMultiCancel(RealtimeacceptVo realtimeAcceptVo) {
        return insert("EcareCancel.insertMultiCancel", realtimeAcceptVo);    // EcareCancel
    }
    
    /**
     * 취소 이력조회
     * @param cancelVo  이력조회를 위한 취소클래스 객체
     * @return          취소이력 목록
     */
    public List<CancelVo> selectCancelList(CancelVo cancelVo) {
        return selectList("EcareCancel.selectCancelList", cancelVo);
    }

    /**
     * 취소이력 전체건수 조회 (페이징 용도)
     * @return  취소이력 전체건수
     */
    public int selectCancelTotalCount(CancelVo cancelVo) {
        return (int) selectOne("EcareCancel.selectCancelTotalCount", cancelVo);
    }
    
    public List<NrealtimeRequestVo> selectRealInterface(Map<String, Object> paramMap) {
        return selectList("EcareCancel.selectRealInterface", paramMap);
    }

    public List<NrealtimeRequestVo> selectBatchInterface(Map<String, Object> paramMap) {
        return selectList("EcareCancel.selectBatchInterface", paramMap);
    }
    public EcareVo getEcareByEcareNo(int ecareNo) {
        return (EcareVo) selectOne("Ecare.getEcareByEcareNo", ecareNo);
    }
    public EcareSendLogVo selectEcareSendLogByPk(int ecareNo, String resultSeq, String listSeq, String customerKey) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("resultSeq", resultSeq);
        paramMap.put("listSeq", listSeq);
        paramMap.put("customerKey", customerKey);
        return (EcareSendLogVo) selectOne("EcareCancel.selectEcareSendLogByPk", paramMap);
    }

}