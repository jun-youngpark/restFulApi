package com.mnwise.wiseu.web.rest.dao.common;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.common.ApiLog;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * 공통 Dao
 */
@Repository
public class CommonDao extends BaseDao {
    public int selectCountBySql(String sql) {
        return (Integer) selectOne("Common.selectCount", sql);
    }

    /**
     * 해당 핸들러를 사용하는 서비스리스트를 반환
     * @param handlerSeq
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> selectHandlerUseServiceList(int handlerSeq) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("handlerSeq", handlerSeq);
        return (List<CaseInsensitiveMap>) selectList("Common.selectHandlerUseServiceList", paramMap);
    }
    
    public int insertApiLog(ApiLog apiLog) throws SQLException {
    	return insert("Common.insertApiLog", apiLog);
    }
}
