package com.mnwise.wiseu.web.ecare.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.ecare.model.EcareScenarioVo;
import com.mnwise.wiseu.web.ecare.model.EcareVo;

/**
 * NVDIVIDESCHEDULE 테이블 DAO 클래스
 */
@Repository
public class EcareDivideScheduleDao extends BaseDao {
    public int insertDivideSchedule(DivideSchedule divideSchedule) {
        return insert("DivideSchedule.insertDivideSchedule", divideSchedule);
    }

//    public int copyDivideSchedule(EcareVo ecareVo) {
//        return insert("DivideSchedule.copyDivideSchedule", ecareVo);
//    }
//
//    public int deleteDivideScheduleAll(EcareScenarioVo ecareScenarioVo) {
//        return delete("DivideSchedule.deleteDivideScheduleAll", ecareScenarioVo);
//    }

    public int deleteEcareDivideSchedule(int ecareNo) {
        return delete("DivideSchedule.deleteEcareDivideSchedule", ecareNo);
    }

    /**
     * 캠페인 분할발송 예약정보를 가져온다.
     */
    @SuppressWarnings("unchecked")
    public List<DivideSchedule> getEcareDivideSchedule(int ecareNo) {
        return selectList("DivideSchedule.selectEcareDivideSchedule", ecareNo);
    }


}