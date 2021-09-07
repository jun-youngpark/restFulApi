package com.mnwise.wiseu.web.ecare.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.EcareCancelDao;
import com.mnwise.wiseu.web.ecare.model.CancelVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.EcareSendLogVo;
import com.mnwise.wiseu.web.report.model.ecare.RealtimeacceptVo;
import com.mnwise.wiseu.web.rest.model.NrealtimeRequestVo;

/**
 * 이케어 취소 서비스
 */
@Service
public class EcareCancelServcie extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(EcareCancelServcie.class);
    
    @Autowired private EcareCancelDao ecareCancelDao;
    
    /**
     * 취소처리 진행
     * @param cancelVo  취소클래스 객체
     * @return  등록건수
     */
    public int insertCancel(CancelVo cancelVo) {
        return ecareCancelDao.insertCancel(cancelVo);
    }
    /**
     * 다중건 취소처리 진행
     * @param cancelVo  취소클래스 객체
     * @return  등록건수
     */
    public int insertMultiCancel(RealtimeacceptVo realtimeAcceptVo) {
        return ecareCancelDao.insertMultiCancel(realtimeAcceptVo);
    }


    /**
     * 취소 이력조회
     * @param cancelVo  이력조회를 위한 취소클래스 객체
     * @return          취소이력 목록
     */
    public List<CancelVo> selectCancelList(CancelVo cancelVo) {
        return ecareCancelDao.selectCancelList(cancelVo);
    }
    
    /**
     * 취소이력 전체건수 조회 (페이징 용도)
     * @return  취소이력 전체건수
     */
    public int selectCancelTotalCount(CancelVo cancelVo) {
        return ecareCancelDao.selectCancelTotalCount(cancelVo);
    }
    

    public List<NrealtimeRequestVo> selectRealInterface(Map<String, Object> paramMap) {
        return ecareCancelDao.selectRealInterface(paramMap);
    }
    
    public List<NrealtimeRequestVo> selectBatchInterface(Map<String, Object> paramMap) {
        return ecareCancelDao.selectBatchInterface(paramMap);
    }
    
    /**
     * 이케어번호로 이케어 조회
     * @param ecareNo   이케어번호
     * @return  이케어
     */
    public EcareVo getEcareByEcareNo(int ecareNo) {
        return ecareCancelDao.getEcareByEcareNo(ecareNo);
    }
    

    public EcareSendLogVo selectEcareSendLogByPk(int ecareNo, String resultSeq, String listSeq, String customerKey) {
        return ecareCancelDao.selectEcareSendLogByPk(ecareNo, resultSeq, listSeq, customerKey);
    }
}
