package com.mnwise.wiseu.web.campaign.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import org.springframework.stereotype.Repository;

/**
 * NVSCHEDULE 테이블 DAO 클래스
 */
@Repository
public class ScheduleDao extends BaseDao {

    public int insertCamSchedule1StepInfo(ScenarioVo scenarioVo) {
        return insert("Schedule.insertCamSchedule1StepInfo", scenarioVo);
    }

    public int copySchedule(ScenarioVo scenarioVo) {
        return insert("Schedule.copySchedule", scenarioVo);
    }

    public int copyScheduleForResend(ScenarioVo scenarioVo) {
        return insert("Schedule.copyScheduleForResend", scenarioVo);
    }

    public int deleteCamScheduleAll(ScenarioVo scenarioVo) {
        return delete("Schedule.deleteCamScheduleAll", scenarioVo);
    }

}