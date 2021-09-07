package com.mnwise.wiseu.web.rest.dao.ecare;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.common.TraceInfo;
import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVECARETRACEINFO 테이블 DAO 클래스
 */
@Repository
public class EcareTraceInfoDao extends BaseDao {

    public int insertTraceInfo(TraceInfo traceInfo) {
        return insert("EcareTraceInfo.insertTraceInfo", traceInfo);
    }

    public int copyEcareTraceInfo(Ecare ecare) {
        return insert("EcareTraceInfo.copyEcareTraceInfo", ecare);
    }

    public int updateEcareTraceInfo(Ecare ecare) {
        return update("EcareTraceInfo.updateEcareTraceInfo", ecare);
    }

    public int deleteEcareTraceInfo(int ecareNo) {
        return delete("EcareTraceInfo.deleteEcareTraceInfo", ecareNo);
    }

    public String selectEcareTraceInfo(int ecareNo) {
        return (String) selectOne("EcareTraceInfo.selectEcareTraceInfo", ecareNo);
    }
}