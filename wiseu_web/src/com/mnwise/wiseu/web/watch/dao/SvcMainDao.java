package com.mnwise.wiseu.web.watch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.watch.model.SvcMain;

/**
 * NV_SVC_MAIN 테이블 DAO 클래스
 */
@Repository
public class SvcMainDao extends BaseDao {
    public int insertSvcMain(SvcMain svcMain) {
        return insert("SvcMain.insertSvcMain", svcMain);
    }

    public int updateSvcMainByPk(SvcMain svcMain) {
        return update("SvcMain.updateSvcMainByPk", svcMain);
    }

    public int updateSvcDelYn(String tId) {
        return update("SvcMain.updateSvcDelYn", tId);
    }

    public int deleteSvcMainByPk(String tId, String serverId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tId", tId);
        paramMap.put("serverId", serverId);
        return delete("SvcMain.deleteSvcMainByPk", paramMap);
    }

    /*public SvcMain selectSvcMainByPk(String tId, String serverId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tId", tId);
        paramMap.put("serverId", serverId);
        return selectOne("SvcMain.selectSvcMainByPk", paramMap);
    }*/

    @SuppressWarnings("unchecked")
    public List<SvcMain> selectServiceList(String client, String selectNum) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("client", client);
        paramMap.put("selectNum", selectNum);
        return selectList("SvcMain.selectServiceList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<SvcMain> getInfoService_lts(String tId) {
        return selectList("SvcMain.getInfoService_lts", tId);
    }
}