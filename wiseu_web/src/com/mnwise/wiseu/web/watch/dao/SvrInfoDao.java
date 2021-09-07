package com.mnwise.wiseu.web.watch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.watch.model.SvrInfo;

/**
 * NV_SVR_INFO 테이블 DAO 클래스
 */
@Repository
public class SvrInfoDao extends BaseDao {
    public int insertSvrInfo(SvrInfo svrInfo) {
        return insert("SvrInfo.insertSvrInfo", svrInfo);
    }

    public int updateSvrInfoByPk(SvrInfo svrInfo) {
        return update("SvrInfo.updateSvrInfoByPk", svrInfo);
    }

    public int updateConfigContByPk(String serverId, String configCont) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serverId", serverId);
        paramMap.put("configCont", configCont);
        return update("SvrInfo.updateConfigContByPk", paramMap);
    }

    public int deleteSvrInfoByPk(String serverId) {
        return delete("SvrInfo.deleteSvrInfoByPk", serverId);
    }

    public SvrInfo selectSvrInfoByPk(String serverId) {
        return (SvrInfo) selectOne("SvrInfo.selectSvrInfoByPk", serverId);
    }

    @SuppressWarnings("unchecked")
    public List<String> selectServerIdList() {
        return selectList("SvrInfo.selectServerIdList");
    }
}