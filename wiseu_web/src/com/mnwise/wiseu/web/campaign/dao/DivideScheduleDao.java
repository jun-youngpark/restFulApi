package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVDIVIDESCHEDULE 테이블 DAO 클래스
 */
@Repository
public class DivideScheduleDao extends BaseDao {
    public int insertDivideSchedule(DivideSchedule divideSchedule) {
        return insert("DivideSchedule.insertDivideSchedule", divideSchedule);
    }

    public int copyDivideSchedule(CampaignVo campaignVo) {
        return insert("DivideSchedule.copyDivideSchedule", campaignVo);
    }

    public int deleteDivideScheduleAll(ScenarioVo scenarioVo) {
        return delete("DivideSchedule.deleteDivideScheduleAll", scenarioVo);
    }

    public int deleteDivideSchedule(int campaignNo) {
        return delete("DivideSchedule.deleteDivideSchedule", campaignNo);
    }

    /**
     * 캠페인 분할발송 예약정보를 가져온다.
     */
    @SuppressWarnings("unchecked")
    public List<DivideSchedule> getCampaignDivideSchedule(int campaignNo) {
        return selectList("DivideSchedule.selectDivideSchedule", campaignNo);
    }


}