package com.mnwise.wiseu.web.watch.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NV_STATUS_REQ_RESULT 테이블 DAO 클래스
 */
@Repository
public class StatusReqResultDao extends BaseDao {
    /*public int insertStatusReqResult(StatusReqResult statusReqResult) {
        return insert("StatusReqResult.insertStatusReqResult", statusReqResult);
    }

    public int updateStatusReqResultByPk(StatusReqResult statusReqResult) {
        return update("StatusReqResult.updateStatusReqResultByPk", statusReqResult);
    }*/

    public int deleteStatusReqResultByPk(String serverId, String reqCreateTm, String taskId, String req) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serverId", serverId);
        paramMap.put("reqCreateTm", reqCreateTm);
        paramMap.put("taskId", taskId);
        paramMap.put("req", req);
        return delete("StatusReqResult.deleteStatusReqResultByPk", paramMap);
    }

    /*public StatusReqResult selectStatusReqResultByPk(String serverId, String reqCreateTm, String taskId, String req) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serverId", serverId);
        paramMap.put("reqCreateTm", reqCreateTm);
        paramMap.put("taskId", taskId);
        paramMap.put("req", req);
        return selectOne("StatusReqResult.selectStatusReqResultByPk", paramMap);
    }*/

    public CaseInsensitiveMap retrieveSvcStatusReqResult(Map<String, Object> paramMap) {
        return (CaseInsensitiveMap) selectOne("StatusReqResult.retrieveSvcStatusReqResult", paramMap);
    }
}