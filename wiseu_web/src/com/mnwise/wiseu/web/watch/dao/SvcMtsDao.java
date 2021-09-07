package com.mnwise.wiseu.web.watch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.watch.model.SvcMts;

/**
 * NV_SVC_MTS 테이블 DAO 클래스
 */
@Repository
public class SvcMtsDao extends BaseDao {
    public int insertSvcMts(SvcMts svcMts) {
        return insert("SvcMts.insertSvcMts", svcMts);
    }

    public int updateSvcMtsByPk(SvcMts svcMts) {
        return update("SvcMts.updateSvcMtsByPk", svcMts);
    }

    public int deleteSvcMtsByPk(String tId, String serverId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tId", tId);
        paramMap.put("serverId", serverId);
        return delete("SvcMts.deleteSvcMtsByPk", paramMap);
    }

    public SvcMts selectSvcMtsByPk(String tId, String serverId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tId", tId);
        paramMap.put("serverId", serverId);
        return (SvcMts) selectOne("SvcMts.selectSvcMtsByPk", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<SvcMts> getInfoService_mts(String tId) {
        return selectList("SvcMts.getInfoService_mts", tId);
    }
}