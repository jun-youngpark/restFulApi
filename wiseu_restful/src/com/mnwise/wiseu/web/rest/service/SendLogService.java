package com.mnwise.wiseu.web.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.common.RestApiUtils;
import com.mnwise.wiseu.web.rest.dao.SendLogDao;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.exception.RestException;
import com.mnwise.wiseu.web.rest.model.custom.CustomSendlLog;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignLog;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignLogRtn;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignSingleLog;
import com.mnwise.wiseu.web.rest.model.ecare.EcareLog;
import com.mnwise.wiseu.web.rest.model.ecare.EcareLogRtn;
import com.mnwise.wiseu.web.rest.model.ecare.EcareSingleLogRtn;
import com.mnwise.wiseu.web.rest.model.ecare.TableSrch;
import com.mnwise.wiseu.web.rest.model.ecare.TableSrchRtn;
import com.mnwise.wiseu.web.rest.parent.BaseService;

import org.json.simple.JSONObject;

@Service
public class SendLogService extends BaseService {
	@Autowired private SendLogDao sendLogDao;
    @Autowired private CommonService commonSerivce;

	/**
	 *  발송 결과  로그테이블 조회
	 */
    public List<CustomSendlLog> selectSendlog(CustomSendlLog sendlLogVo) throws Exception{
    	return sendLogDao.selectSendlog(sendlLogVo);
    }


    public JSONObject selectTblSrch(TableSrch tableSrchVo, String interfaceId, String logSeq, String funcNm) throws Exception {

		List<TableSrchRtn> list = sendLogDao.selectTblSrch(tableSrchVo);
		int size = list.size();
		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " count : " + size, "", "");

		if (size == 0) {

			throw new RestException("검색결과가 없습니다.");
		}

		// Response
		return new DataMap<>()
				.put("size",size)
				.put("list",list)
				.build();
	}

    public JSONObject selectCampaignLog(CampaignLog campaignLog, String interfaceId, String logSeq, String funcNm) throws Exception {

		List<CampaignLogRtn> list = sendLogDao.selectCampaignLog(campaignLog);
		int size = list.size();
		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " count : " + size, "", "");

		if (size == 0) {

			throw new RestException("검색결과가 없습니다.");
		}

		// Response
		return new DataMap<>()
				.put("size",size)
				.put("list",list)
				.build();
	}

    public JSONObject selectCampaignSingleLog(CampaignSingleLog campaignSingleLog) throws Exception {

    	CampaignSingleLog data = sendLogDao.selectCampaignSingleLog(campaignSingleLog);
    	if (data == null) {
    		throw new RestException("검색결과가 없습니다.");
    	}
    	DataMap datamap = new DataMap<>();
    	datamap.put("campaignNo", data.getCampaignNo());
    	datamap.put("channel",    RestApiUtils.nullToBlank(data.getChannel()));
    	datamap.put("sendDt",     RestApiUtils.nullToBlank(data.getSendDt()));
    	datamap.put("sendTm",     RestApiUtils.nullToBlank(data.getSendTm()));
    	datamap.put("reqUserId",  RestApiUtils.nullToBlank(data.getReqUserId()));
    	datamap.put("reqDeptId",  RestApiUtils.nullToBlank(data.getReqDeptId()));
    	datamap.put("receiverId", RestApiUtils.nullToBlank(data.getReceiverId()));
    	datamap.put("receriverNm",RestApiUtils.nullToBlank(data.getReceriverNm()));
    	datamap.put("receiver",   RestApiUtils.nullToBlank(data.getReceiver()));
    	datamap.put("seq",        RestApiUtils.nullToBlank(data.getSeq()));
    	datamap.put("resultCd",   RestApiUtils.nullToBlank(data.getResultCd()));
    	datamap.put("resultMsg",  RestApiUtils.nullToBlank(data.getResultMsg()));
    	datamap.put("openDate",   RestApiUtils.nullToBlank(data.getOpenDate()));
    	datamap.put("template",   RestApiUtils.nullToBlank("")); // TODO : 링크로 해야 하지 않나?


    	return datamap.build();
	}


    public JSONObject selectEcareLog(EcareLog ecareLog, String interfaceId, String logSeq, String funcNm) throws Exception {

		List<EcareLogRtn> list = sendLogDao.selectEcareLog(ecareLog);
		int size = list.size();
		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " count : " + size, "", "");

		if (size == 0) {

			throw new RestException("검색결과가 없습니다.");
		}

		// Response
		return new DataMap<>()
				.put("size",size)
				.put("list",list)
				.build();
	}

    public JSONObject selectEcareSingleLog(EcareLog ecareLog) throws Exception {

    	EcareSingleLogRtn data = sendLogDao.selectEcareSingleLog(ecareLog);
    	if (data == null) {
    		throw new RestException("검색결과가 없습니다.");
    	}
    	DataMap datamap = new DataMap<>();
    	datamap.put("ecareNo", 	  data.getEcareNo());
    	datamap.put("channel",    RestApiUtils.nullToBlank(data.getChannel()));
    	datamap.put("sendDt",     RestApiUtils.nullToBlank(data.getSendDt()));
    	datamap.put("sendTm",     RestApiUtils.nullToBlank(data.getSendTm()));
    	datamap.put("reqUserId",  RestApiUtils.nullToBlank(data.getReqUserId()));
    	datamap.put("reqDeptId",  RestApiUtils.nullToBlank(data.getReqDeptId()));
    	datamap.put("receiverId", RestApiUtils.nullToBlank(data.getReceiverId()));
    	datamap.put("receriverNm",RestApiUtils.nullToBlank(data.getReceriverNm()));
    	datamap.put("receiver",   RestApiUtils.nullToBlank(data.getReceiver()));
    	datamap.put("seq",        RestApiUtils.nullToBlank(data.getSeq()));
    	datamap.put("resultCode", RestApiUtils.nullToBlank(data.getResultCode()));
    	datamap.put("resultMsg",  RestApiUtils.nullToBlank(data.getResultMsg()));
    	datamap.put("openDate",   RestApiUtils.nullToBlank(data.getOpenDate()));
    	datamap.put("template",   RestApiUtils.nullToBlank("")); // TODO : 링크로 해야 하지 않나?
    	datamap.put("cover",   	  RestApiUtils.nullToBlank("")); // TODO : 링크로 해야 하지 않나?

    	return datamap.build();
	}
}