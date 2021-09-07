package com.mnwise.wiseu.web.rest.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.custom.CustomSendlLog;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignLog;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignLogRtn;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignSingleLog;
import com.mnwise.wiseu.web.rest.model.ecare.EcareLog;
import com.mnwise.wiseu.web.rest.model.ecare.EcareLogRtn;
import com.mnwise.wiseu.web.rest.model.ecare.EcareSingleLogRtn;
import com.mnwise.wiseu.web.rest.model.ecare.TableSrch;
import com.mnwise.wiseu.web.rest.model.ecare.TableSrchRtn;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

@Repository
public class SendLogDao extends BaseDao {

	/**
	 *  발송 결과  로그테이블 조회
	 */
    public List<CustomSendlLog> selectSendlog(CustomSendlLog sendlLogVo) throws Exception {
    	return (List<CustomSendlLog>) selectList("SendLog.selectSendlog", sendlLogVo);
    }
    
    public List<TableSrchRtn> selectTblSrch(TableSrch tableSrchVo) throws Exception {
    	return (List<TableSrchRtn>) selectList("sendLog.selectTblSrch", tableSrchVo);
    }

    public List<CampaignLogRtn> selectCampaignLog(CampaignLog campaignLog) throws Exception {
    	return (List<CampaignLogRtn>) selectList("sendLog.selectCampaignLog", campaignLog);
    }
    
    public CampaignSingleLog selectCampaignSingleLog(CampaignSingleLog campaignSingleLog) throws Exception {
    	return (CampaignSingleLog) selectOne("sendLog.selectCampaignSingleLog", campaignSingleLog);
    }
    
    public List<EcareLogRtn> selectEcareLog(EcareLog ecareLog) throws Exception {
    	return (List<EcareLogRtn>) selectList("sendLog.selectEcareLog", ecareLog);
    }
    
    public EcareSingleLogRtn selectEcareSingleLog(EcareLog ecareLog) throws Exception {
    	return (EcareSingleLogRtn) selectOne("sendLog.selectEcareSingleLog", ecareLog);
    }
}