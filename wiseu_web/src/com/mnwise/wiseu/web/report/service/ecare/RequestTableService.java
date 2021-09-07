package com.mnwise.wiseu.web.report.service.ecare;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.report.dao.RequestTableDao;
import com.mnwise.wiseu.web.report.model.ecare.RealtimeacceptVo;

/**
 * 이케어 요청테이블 리스트
 */
@Service
public class RequestTableService extends BaseService {
    @Autowired private RequestTableDao requstTableDao;

    public int selectRealtimeacceptCount(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.selectRealtimeacceptCount(realtimeacceptVo);
    }
    public int selectBatchCount(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.selectBatchCount(realtimeacceptVo);
    }
    public int selectAllInterfaceCount(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.selectAllInterfaceCount(realtimeacceptVo);
    }
    public List<RealtimeacceptVo> selectAllInterfaceList(RealtimeacceptVo realtimeacceptVo) {
        PagingUtil.setPagingRowcount(realtimeacceptVo);
        return requstTableDao.selectAllInterfaceList(realtimeacceptVo);
    }
    public List<RealtimeacceptVo> selectRealtimeacceptList(RealtimeacceptVo realtimeacceptVo) {
        PagingUtil.setPagingRowcount(realtimeacceptVo);
        return requstTableDao.selectRealtimeacceptList(realtimeacceptVo);
    }
    public List<RealtimeacceptVo> selectBatchList(RealtimeacceptVo realtimeacceptVo) {
        PagingUtil.setPagingRowcount(realtimeacceptVo);
        return requstTableDao.selectBatchList(realtimeacceptVo);
    }
    public int insertResendOne(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.insertResendOne(realtimeacceptVo);
    }
    public int updateCancelSend(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.updateCancelSend(realtimeacceptVo);
    }
    public int updateCancelSendAll(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.updateCancelSendAll(realtimeacceptVo);
    }
    public int insertResendOneBatch(RealtimeacceptVo realtimeacceptVo) {
        return requstTableDao.insertResendOneBatch(realtimeacceptVo);
    }
    
    public String getSubType(String ecareNo) {
        return requstTableDao.seleceSubType(ecareNo);
    }
}
