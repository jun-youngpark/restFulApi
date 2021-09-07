package com.mnwise.wiseu.web.ecare.dao;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.EcareScheduleVo;

/**
 * NVECMSCHEDULE 테이블 DAO 클래스
 */
@Repository
public class EcmScheduleDao extends BaseDao {
    public int insertEcmSchedule(EcareScheduleVo ecareSchedule) {
        return insert("EcmSchedule.insertEcmSchedule", ecareSchedule);
    }

    public int copyEcmSchedule(EcareScheduleVo ecareSchedule) {
        return insert("EcmSchedule.copyEcmSchedule", ecareSchedule);
    }

    /**
     * 이케어 스케쥴(NVECMSCHEDULE)을 변경한다.
     *
     * @param ecareScheduleVo
     */
    public int updateEcareScheduleInfo(EcareScheduleVo ecareScheduleVo) {
        return update("EcmSchedule.updateEcmSchedule1StepInfo", ecareScheduleVo);
    }

    public int deleteEcmScheduleByPk(int ecmScheduleNo) {
        return delete("EcmSchedule.deleteEcmScheduleByPk", ecmScheduleNo);
    }

    /**
     * 이케어 스케쥴 정보를 가져온다.
     *
     * @param ecmScheduleNo
     * @return
     */
    public EcareScheduleVo selectEcmScheduleByPk(int ecmScheduleNo) {
        return (EcareScheduleVo) selectOne("EcmSchedule.selectEcmScheduleByPk", ecmScheduleNo);
    }

    public int selectNextEcmScheduleNo() {
        return (Integer) selectOne("EcmSchedule.selectNextEcmScheduleNo");
    }
}