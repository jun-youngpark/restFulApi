package com.mnwise.wiseu.web.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.common.RestApiUtils;
import com.mnwise.wiseu.web.rest.dao.common.CommonDao;
import com.mnwise.wiseu.web.rest.model.common.ApiLog;
import com.mnwise.wiseu.web.rest.parent.BaseService;

@Service
public class CommonService extends BaseService {
	
	private static final Logger log = LoggerFactory.getLogger(CommonService.class);
	
	@Autowired private CommonDao commonDao;
	

    @Value("${api.LogType}")
    protected String apiLogType;
	
    public int insertApiLog(ApiLog apiLogVo) throws Exception{
    	return commonDao.insertApiLog(apiLogVo);
    }
    
	public void regApiLog(String seq, String regType, String interfaceId, String msg, String inVal, String dtlMsg) {
		
		ApiLog apiLog = new ApiLog();
		apiLog.setSeq(seq);
		apiLog.setRegType(regType);
		apiLog.setInterfaceId(interfaceId);
		apiLog.setMsg(msg);
		
		if (regType.equalsIgnoreCase(Const.ApiLogType.EXCEPTION)) {

			apiLog.setInVal(inVal);
			apiLog.setDtlMsg(dtlMsg);
		}
		
		try {
			if (apiLogType.equalsIgnoreCase(Const.ApiLogType.EXCEPTION)) {
				if (regType.equalsIgnoreCase(Const.ApiLogType.EXCEPTION)) {
					
					insertApiLog(apiLog);
				}
			} else if (apiLogType.equalsIgnoreCase(Const.ApiLogType.LOG)) {
				if (regType.equalsIgnoreCase(Const.ApiLogType.LOG)) {
					
					insertApiLog(apiLog);
				}
			} else {
				
				insertApiLog(apiLog);
			}
		} catch (Exception e1) {
			
			log.error("APILOG기록중 에러가 발생하였습니다." + RestApiUtils.getError(e1));
		}
	}
}