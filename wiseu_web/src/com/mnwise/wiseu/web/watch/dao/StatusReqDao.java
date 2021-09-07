package com.mnwise.wiseu.web.watch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.watch.model.StatusReq;

/**
 * NV_STATUS_REQ 테이블 DAO 클래스
 */
@Repository
public class StatusReqDao extends BaseDao {
    public int insertStatusReq(StatusReq statusReq) {
        return insert("StatusReq.insertStatusReq", statusReq);
    }

    public int insertSvrStatusReq(Map<String, Object> paramMap) {
        return insert("StatusReq.insertSvrStatusReq", paramMap);
    }

    public int updateStatusReqByPk(StatusReq statusReq) {
        return update("StatusReq.updateStatusReqByPk", statusReq);
    }

    public int updateSvrStatusReq(Map<String, Object> paramMap) {
        return update("StatusReq.updateSvrStatusReq", paramMap);
    }

    public int deleteStatusReqByPk(String reqKind, String createTm, String taskId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", reqKind);
        paramMap.put("createTm", createTm);
        paramMap.put("taskId", taskId);
        return delete("StatusReq.deleteStatusReqByPk", paramMap);
    }

    public StatusReq selectStatusReqByPk(String reqKind, String createTm, String taskId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("reqKind", reqKind);
        paramMap.put("createTm", createTm);
        paramMap.put("taskId", taskId);
        return (StatusReq) selectOne("StatusReq.selectStatusReqByPk", paramMap);
    }

    @SuppressWarnings("unchecked")
    public String retrieveSvrStatusReqResult(Map<String, Object> paramMap) {
        List<String> resultList = selectList("StatusReq.retrieveSvrStatusReqResult", paramMap);
        return (resultList == null || resultList.size() == 0) ? "" : resultList.get(0);
    }
}