package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.EcareVo;

/**
 * NVECARETRACEINFO 테이블 DAO 클래스
 */
@Repository
public class EcareTraceInfoDao extends BaseDao {

    public int insertEcareTraceInfoByEcare(EcareVo ecare) {
        return insert("EcareTraceInfo.insertEcareTraceInfoByEcare", ecare);
    }

    public int copyEcareTraceInfo(EcareVo ecare) {
        return insert("EcareTraceInfo.copyEcareTraceInfo", ecare);
    }

    public int updateEcareTraceInfo(EcareVo ecare) {
        return update("EcareTraceInfo.updateEcareTraceInfo", ecare);
    }

    public int deleteEcareTraceInfo(int ecareNo) {
        return delete("EcareTraceInfo.deleteEcareTraceInfo", ecareNo);
    }

    public String selectEcareTraceInfo(int ecareNo) {
        return (String) selectOne("EcareTraceInfo.selectEcareTraceInfo", ecareNo);
    }
}