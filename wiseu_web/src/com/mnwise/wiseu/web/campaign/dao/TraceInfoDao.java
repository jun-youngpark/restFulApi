package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.model.TraceInfoVo;
import org.springframework.stereotype.Repository;

/**
 * NVTRACEINFO 테이블 DAO 클래스
 */
@Repository
public class TraceInfoDao extends BaseDao {

    public int insertTraceInfoByCampaign(CampaignVo campaignVo) {
        return insert("TraceInfo.insertTraceInfoByCampaign", campaignVo);
    }

    public int insertCamTraceInfo1StepInfo(ScenarioVo scenarioVo) {
        return insert("TraceInfo.insertCamTraceInfo1StepInfo", scenarioVo);
    }

    public int copyTraceInfo(CampaignVo campaignVo) {
        return insert("TraceInfo.copyTraceInfo", campaignVo);
    }

    public int updateTraceInfo(CampaignVo campaignVo) {
        return update("TraceInfo.updateTraceInfo", campaignVo);
    }

    /**
     * 캠페인 수신확인정보(TRACEINFO)를 등록/변경한다.
     *
     * @param campaignVo
     */
    public int registTraceInfo(CampaignVo campaignVo) {
        Integer traceInfoCnt = selectTraceInfoCnt(campaignVo.getCampaignNo());
        if(traceInfoCnt != null && traceInfoCnt > 0) {
            return updateTraceInfo(campaignVo);
        }

        return insertTraceInfoByCampaign(campaignVo);
    }

    public int deleteCamTraceInfoAll(ScenarioVo scenarioVo) {
        return delete("TraceInfo.deleteCamTraceInfoAll", scenarioVo);
    }

    /**
     * 캠페인 반응 추적 정보를 가져온다.
     *
     * @param campaignNo
     * @return
     */
    public TraceInfoVo selectTraceInfo(int campaignNo) {
        return (TraceInfoVo) selectOne("TraceInfo.selectTraceInfo", campaignNo);
    }

    public int selectTraceInfoCnt(int campaignNo) {
        return (Integer) selectOne("TraceInfo.selectTraceInfoCnt", campaignNo);
    }
}