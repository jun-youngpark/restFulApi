package com.mnwise.wiseu.web.channel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.util.PropertyUtil;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportErrorVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.resend.model.LstResendVo;

/**
 * MZFTSENDLOG 테이블 DAO 클래스
 */
@Repository
public class MzFtSendLogDao extends BaseDao {

    public String getFrtalkMsg(LstResendVo lstResendVo) {
        String sndMsg = (String) selectOne("MzFtSendLog.selectFrtalkMsg", lstResendVo);
        return security.securityWithColumn(sndMsg, "SND_MSG", "DECRYPT");
    }

    @SuppressWarnings("unchecked")
    public List<CampaignReportErrorVo> getCampaignReportFRTErrorList(Map<String, Object> paramMap) {
        return selectList("MzFtSendLog.getCampaignReportFRTErrorList", paramMap);
    }

    /**
     * 2018.03 wiseMoka 병합 친구톡 & 대체채널(sms,lms) 발송결과
     *
     * @param scenarioInfoVo
     * @return
     */
    public RptSendResultVo selectFriendtalkSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("frtOkCdArray", PropertyUtil.getProperty("friendtalk.code.success").split(","));
        paramMap.put("smsOkCdArray", PropertyUtil.getProperty("friendtalk.other.code.success").split(","));
        paramMap.put("ecare_no", scenarioInfoVo.getEcareInfoVo().getEcareNo());
        paramMap.put("report_dt", scenarioInfoVo.getEcareInfoVo().getReportDt());
        paramMap.put("isDaily", ("N".equals(scenarioInfoVo.getEcareInfoVo().getSubType()) || "R".equals(scenarioInfoVo.getEcareInfoVo().getServiceType())) ? "Y" : "N");

        if("N".equals(paramMap.get("isDaily"))) {
            paramMap.put("result_seq", scenarioInfoVo.getEcareInfoVo().getResultSeq());
        }
        return (RptSendResultVo) selectOne("MzFtSendLog.selectFriendtalkSendResult", paramMap);
    }

    /**
     * 친구톡 오류분석
     *
     * @param scenarioInfoVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectFriendtalkLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("MzFtSendLog.selectFriendtalkLogList", scenarioInfoVo);
    }

    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> selectFriendtalkCsvDataList(int ecareNo, String reportDt, String channelType, String successGbn) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("frtOkCdArray", PropertyUtil.getProperty("friendtalk.code.success").split(","));
        paramMap.put("smsOkCdArray", PropertyUtil.getProperty("friendtalk.other.code.success").split(","));
        paramMap.put("ecare_no", ecareNo);
        paramMap.put("report_dt", reportDt);
        paramMap.put("successGbn", successGbn);
        if("CS".equals(channelType))
            channelType = "S";
        if("CL".equals(channelType))
            channelType = "L";
        paramMap.put("channel", channelType);


        return selectList("MzFtSendLog.selectFriendtalkCsvDataList", paramMap);
    }

}