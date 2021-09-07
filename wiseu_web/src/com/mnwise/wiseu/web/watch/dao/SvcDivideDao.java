package com.mnwise.wiseu.web.watch.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NV_SVC_DIVIDE 테이블 DAO 클래스
 */
@Repository
public class SvcDivideDao extends BaseDao {
    /*public int insertSvcDivide(SvcDivide svcDivide) {
        return insert("SvcDivide.insertSvcDivide", svcDivide);
    }

    public int updateSvcDivideByPk(SvcDivide svcDivide) {
        return update("SvcDivide.updateSvcDivideByPk", svcDivide);
    }*/

    public int updateDivideStatusStop(String taskId) {
        return update("SvcDivide.updateDivideStatusStop", taskId);
    }

    public int updateDivideStatusSuspend(String taskId) {
        return update("SvcDivide.updateDivideStatusSuspend", taskId);
    }

    public int deleteSvcDivideByPk(String taskId, String serverId, int divideSeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("taskId", taskId);
        paramMap.put("serverId", serverId);
        paramMap.put("divideSeq", divideSeq);
        return delete("SvcDivide.deleteSvcDivideByPk", paramMap);
    }

    /*public SvcDivide selectSvcDivideByPk(String taskId, String serverId, int divideSeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("taskId", taskId);
        paramMap.put("serverId", serverId);
        paramMap.put("divideSeq", divideSeq);
        return selectOne("SvcDivide.selectSvcDivideByPk", paramMap);
    }*/

    public int selectDivideStatusStop(String taskId) {
        Integer count = (Integer) selectOne("SvcDivide.selectDivideStatusStop", taskId);
        return (count == null) ? 0 : count;
    }

    public int selectDivideStatusSuspend(String taskId) {
        Integer count = (Integer) selectOne("SvcDivide.selectDivideStatusSuspend", taskId);
        return (count == null) ? 0 : count;
    }
}