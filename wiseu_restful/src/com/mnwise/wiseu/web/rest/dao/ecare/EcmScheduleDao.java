package com.mnwise.wiseu.web.rest.dao.ecare;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.ecare.Ecare;
import com.mnwise.wiseu.web.rest.model.ecare.EcmSchedule;
import com.mnwise.wiseu.web.rest.parent.BaseDao;

/**
 * NVECMSCHEDULE 테이블 DAO 클래스
 */
@Repository
public class EcmScheduleDao extends BaseDao {
	public int insertScheduleForFirst(Ecare ecare) {
		return insert("EcmSchedule.insertScheduleForFirst", ecare);
	}

    public int insertEcmSchedule(EcmSchedule ecmSchedule) {
        return insert("EcmSchedule.insertEcmSchedule", ecmSchedule);
    }

    public int copyEcmSchedule(EcmSchedule ecmSchedule) {
        return insert("EcmSchedule.copyEcmSchedule", ecmSchedule);
    }

    /**
     * 이케어 스케쥴(NVECMSCHEDULE)을 변경한다.
     *
     * @param ecareScheduleVo
     */
    public int updateEcareScheduleInfo(EcmSchedule ecmSchedule) {
        return update("EcmSchedule.updateEcmSchedule1StepInfo", ecmSchedule);
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
    public EcmSchedule selectEcmScheduleByPk(int ecmScheduleNo) {
        return (EcmSchedule) selectOne("EcmSchedule.selectEcmScheduleByPk", ecmScheduleNo);
    }

    public int selectNextEcmScheduleNo() {
        return (Integer) selectOne("EcmSchedule.selectNextEcmScheduleNo");
    }

	public EcmSchedule selectEcmScheduleByEcareNo(int ecareNo) {
		return (EcmSchedule) selectOne("EcmSchedule.selectEcmScheduleByEcareNo", ecareNo);
    }

}