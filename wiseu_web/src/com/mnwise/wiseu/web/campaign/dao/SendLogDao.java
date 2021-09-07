package com.mnwise.wiseu.web.campaign.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignPerHistoryVo;
import com.mnwise.wiseu.web.report.model.SendLogVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignAbTestVo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReportErrorVo;
import com.mnwise.wiseu.web.report.rowhandler.ErrorListCsvDownloadCallback;
import com.mnwise.wiseu.web.resend.model.LstResendVo;

/**
 * NVSENDLOG 테이블 DAO 클래스
 */
@Repository
public class SendLogDao extends BaseDao {

    public String selectFaxSrfidd(Map<String, Object> paramMap) {
        return (String) selectOne("SendLog.selectFaxSrfidd", paramMap);
    }

    /**
     * 캠페인 리포트 PUSH OS별 발송 정보
     * @param campaignNo
     * @return
     */
    public CaseInsensitiveMap selectPushSendlogCount(int campaignNo) {
        return (CaseInsensitiveMap) selectOne("SendLog.selectPushSendlogCount", campaignNo);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignReportErrorVo> getCampaignReportPushErrorList(Map<String, Object> paramMap) {
        return selectList("SendLog.getCampaignReportPushErrorList", paramMap);
    }

    /**
     * 검색된 E-MAIL 의 총 대상자 수를 구해온다.
     */
    public int selectTotalCount(CampaignPerHistoryVo historyVo) {
        Integer count = (Integer) selectOne("SendLog.selectCamTotalCount", historyVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 검색된 이메일 발송고객이력리스트를 가져온다.
     */
    @SuppressWarnings("unchecked")
    public List<CampaignPerHistoryVo> selectPerHistoryList(CampaignPerHistoryVo historyVo) {
        List<CampaignPerHistoryVo> tmp = (List<CampaignPerHistoryVo>) selectList("SendLog.selectCamPerHistoryList", historyVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    public int selectCamResendTargetCount(Map<String, Object> map) {
        Integer count = (Integer) selectOne("SendLog.selectCamResendTargetCount", map);
        return (count == null) ? 0 : count;
    }

    public LstResendVo selectCamPrevious(LstResendVo historyVo) {
        LstResendVo lstResend = (LstResendVo) selectOne("SendLog.selectCamPrevious", historyVo);
        security.securityObject(lstResend, "DECRYPT");
        return lstResend;
    }

    @SuppressWarnings("unchecked")
    public List<CampaignReportErrorVo> getCampaignReportFaxErrorList(Map<String, Object> paramMap) {
        return selectList("SendLog.getCampaignReportFaxErrorList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignReportErrorVo> getCampaignReportBRTErrorList(Map<String, Object> paramMap) {
        return selectList("SendLog.getCampaignReportBRTErrorList", paramMap);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignAbTestVo> getCampaignReportAbTestOpenList(int campaignNo) {
        return selectList("SendLog.getCampaignReportAbTestOpenList", campaignNo);
    }

    @SuppressWarnings("unchecked")
    public List<CampaignAbTestVo> getCampaignReportAbTestLinkClickList(int campaignNo) {
        return selectList("SendLog.getCampaignReportAbTestLinkClickList", campaignNo);
    }

    public void makeCsvEmErrorCodeDetailList(int campaignNo, long resultSeq, String errorCd, ErrorListCsvDownloadCallback callback) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("campaignNo", campaignNo);
        paramMap.put("resultSeq", resultSeq);
        paramMap.put("errorCd", errorCd);

        select("SendLog.makeCsvEmErrorCodeDetailList", paramMap, callback);
    }
}