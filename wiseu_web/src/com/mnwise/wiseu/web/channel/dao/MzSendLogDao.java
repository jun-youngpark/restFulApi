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
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import com.mnwise.wiseu.web.resend.model.LstResendVo;

/**
 * MZSENDLOG 테이블 DAO 클래스
 */
@Repository
public class MzSendLogDao extends BaseDao {

    public String getAltalkMsg(LstResendVo lstResendVo) {
        String sndMsg= (String) selectOne("MzSendLog.selectAltalkMsg", lstResendVo);
        return security.securityWithColumn(sndMsg, "SND_MSG", "DECRYPT");
    }

    /**
     * 알림톡 & 대체채널(sms,lms) 발송결과
     *
     * @param scenarioInfoVo
     * @return
     */
    public RptSendResultVo selectAlimtalkSendResult(EcareScenarioInfoVo scenarioInfoVo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("altOkCdArray", PropertyUtil.getProperty("alimtalk.code.success").split(","));
        paramMap.put("smsOkCdArray", PropertyUtil.getProperty("alimtalk.other.code.success").split(","));
        paramMap.put("ecare_no", scenarioInfoVo.getEcareInfoVo().getEcareNo());
        paramMap.put("report_dt", scenarioInfoVo.getEcareInfoVo().getReportDt());
        paramMap.put("result_seq", scenarioInfoVo.getEcareInfoVo().getResultSeq());
        paramMap.put("isDaily", ("N".equals(scenarioInfoVo.getEcareInfoVo().getSubType()) || "R".equals(scenarioInfoVo.getEcareInfoVo().getServiceType())) ? "Y" : "N");

        if("N".equals(paramMap.get("isDaily"))) {
            paramMap.put("result_seq", scenarioInfoVo.getEcareInfoVo().getResultSeq());
        }
        return (RptSendResultVo) selectOne("MzSendLog.selectAlimtalkSendResult", paramMap);
    }

    /**
     * 알림톡 오류분석
     *
     * @param scenarioInfoVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DomainLogVo> selectAlimtalkLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("MzSendLog.selectAlimtalkLogList", scenarioInfoVo);
    }

    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> selectAlimtalkCsvDataList(int ecareNo, String reportDt, String channelType, String successGbn) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("alimtalk_success_code", PropertyUtil.getProperty("alimtalk.code.success").split(","));
        paramMap.put("alimtalk_other_success_code", PropertyUtil.getProperty("alimtalk.other.code.success").split(","));
        paramMap.put("ecare_no", ecareNo);
        paramMap.put("report_dt", reportDt);
        paramMap.put("successGbn", successGbn);
        if("AS".equals(channelType))
            channelType = "S";
        if("AL".equals(channelType))
            channelType = "L";
        paramMap.put("channel", channelType);

        return selectList("MzSendLog.selectAlimtalkCsvDataList", paramMap);
    }

}