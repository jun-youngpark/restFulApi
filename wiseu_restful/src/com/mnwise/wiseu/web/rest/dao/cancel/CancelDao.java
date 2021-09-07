package com.mnwise.wiseu.web.rest.dao.cancel;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.model.ecare.Cancel;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

import org.json.simple.JSONObject;

@Repository
public class CancelDao extends BaseDao {

	/**
	 * 준실시간 요청시간 조회
	 */
    public String selectRealtimeReqDt(String seq) throws SQLException {
		return (String) selectOne("Cancel.selectRealtimeReqDt", seq);
    }
    /**
     * 배치 요청시간 조회
     */
    public String selectBatchReqDt(String seq) throws SQLException {
    	return (String) selectOne("Cancel.selectBatchReqDt", seq);
    }
    /**
     * 취소 테이블 인서트
     */
	public int insertCancel(Cancel cancel) {
		return insert("Cancel.insertCancel", cancel);

	}
	
	public Cancel selectSingleCancelLog(Cancel customCancelLog) throws Exception{
    	return (Cancel) selectOne("Cancel.selectSingleCancelLog", customCancelLog);
	}
	public ArrayList<Cancel> selectListCancelLog(Cancel customCancelLog) throws Exception{
    	return (ArrayList<Cancel>) selectList("Cancel.selectListCancelLog", customCancelLog);
	}
	


}