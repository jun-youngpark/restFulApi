package com.mnwise.wiseu.web.rest.dao.interfaces;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.custom.CustomRealtimeaccept;
import com.mnwise.wiseu.web.rest.model.custom.CustomResend;
import com.mnwise.wiseu.web.rest.model.ecare.Batch;
import com.mnwise.wiseu.web.rest.model.ecare.BatchRequest;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class SendDao extends BaseDao {

	/**
	 * 준실시간 테이블 데이터 Insert
	 */
    public int insertRealtime(CustomRealtimeaccept realtimeacceptVo) throws Exception {
    	return insert("RealTimeAccept.insertRealTimeAccept", realtimeacceptVo);
    }

    /**
     * 스케줄 발송 개별부 데이터 INSERT
     * @param batchVo
     */
	public int insertBatch(Batch batchVo) throws Exception {
		return insert("Batch.insertBatch", batchVo);
	}

	/**
	 * 스케줄 발송 공통부 데이터 INSERT
	 * @param batchReqDto
	 */
	public void insertBatchRequest(BatchRequest batchReqDto) throws Exception{
		insert("BatchRequest.insertBatchRequest", batchReqDto);
	}
    /**
     * 동일 자료 재발송
     * @param batchVo
     */
	public void insertBatchResend(CustomResend resendDto) throws Exception {
		insert("Batch.insertBatchResend", resendDto);
	}
    /**
     * 동일 자료 재발송
     * @param batchVo
     */
	public void insertRealtimeResend(CustomResend resendDto) throws Exception {
		insert("RealTimeAccept.insertRealtimeResend", resendDto);
	}
	/**
     * 서브타입 조회
     * @param resendDto
     */
	public String selectSubType(CustomResend resendDto) throws Exception {
		return (String) selectOne("Ecare.selectEcareSubType", resendDto.getEcareNo());
	}


}