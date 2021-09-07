package com.mnwise.wiseu.web.rest.dao.campaign;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.campaign.Campaign;
import com.mnwise.wiseu.web.rest.model.common.TraceInfo;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVTRACEINFO 테이블 DAO 클래스
 */
@Repository
public class TraceInfoDao extends BaseDao {


	 /**
	 * 이메일 trace 기본정보를 등록한다.
	 *
	 * @param Campaign
	 */
	public int insertTraceInfo(TraceInfo traceInfo) {
		return insert("TraceInfo.insertTraceInfo", traceInfo);
	}
	/**
     * 옴니 복사
     */
	public int copyTraceInfo(Campaign campaign) {
		return insert("TraceInfo.copyTraceInfo", campaign);
	}
}