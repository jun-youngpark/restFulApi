package com.mnwise.wiseu.web.rest.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.DateUtil;
import com.mnwise.wiseu.web.rest.dao.cancel.CancelDao;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.exception.RestException;
import com.mnwise.wiseu.web.rest.model.ecare.Cancel;
import com.mnwise.wiseu.web.rest.parent.BaseService;

import org.json.simple.JSONObject;

@Service
public class CancelService extends BaseService {
	
	@Autowired private CancelDao cancelDao;
	

	public JSONObject realtimeCancel(Cancel cancel) throws Exception{
		// Response
        String now = DateUtil.getNowDateTime("yyyyMMddHHmmssSSSSS");
        cancel.setCanReqDtm(now.substring(0, 14));
        cancel.setCanSeq(cancel.getEcareNo() + "_" + now);
        String orgReqDt = cancelDao.selectRealtimeReqDt(cancel.getSeq());
        if(null != orgReqDt) {
        	cancel.setOrgReqDt(cancelDao.selectRealtimeReqDt(cancel.getSeq()));
        }else {
			throw new RestException("데이터가 없습니다.("+cancel.getSeq()+")");
        }
		//테이블 인서트
		cancelDao.insertCancel(cancel);
		return new DataMap<>()
				.build();
	}

	public JSONObject batchCancel(Cancel cancel) throws Exception{
		
		// Response 
        String now = DateUtil.getNowDateTime("yyyyMMddHHmmssSSSSS");
        cancel.setCanReqDtm(now.substring(0, 14));
        cancel.setCanSeq(cancel.getEcareNo() + "_" + now);
        String orgReqDt = cancelDao.selectBatchReqDt(cancel.getSeq());
        if(null != orgReqDt) {
        	cancel.setOrgReqDt(cancelDao.selectBatchReqDt(cancel.getSeq()));
        }else {
        	throw new RestException("데이터가 없습니다.("+cancel.getSeq()+")");
        }
        
		//테이블 인서트
		cancelDao.insertCancel(cancel);
		return new DataMap<>()
				.build();
	}

	public JSONObject selectSingleCancelLog(Cancel customCancelLog) throws Exception{
		// Response
		Cancel cancel = cancelDao.selectSingleCancelLog(customCancelLog);
		return new DataMap<>()
				.put("orgSeq", cancel.getOrgSeq())
				.put("ecareNo", cancel.getEcareNo())
				.put("canReqDtm", cancel.getCanReqDtm())
				.put("canFg", cancel.getCanFg())
				.put("rsltCd", cancel.getRsltCd())
				.build();
	}
	public JSONObject selectListCancelLog(Cancel customCancelLog) throws Exception{
		// Response
		List<Cancel> list =  cancelDao.selectListCancelLog(customCancelLog);
		return new DataMap<>()
				.put("list", list)
				.build();
	}
	
	
	

}