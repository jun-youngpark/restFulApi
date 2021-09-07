package com.mnwise.wiseu.web.env.dao;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVDURATIONINFO 테이블 DAO 클래스
 */
@Repository
public class DurationInfoDao extends BaseDao {
    /*public int insertDurationInfo(DurationInfo durationInfo) {
        return insert("DurationInfo.insertDurationInfo", durationInfo);
    }

    public int updateDurationInfoByPk(DurationInfo durationInfo) {
        return update("DurationInfo.updateDurationInfoByPk", durationInfo);
    }*/

    public int updateEnvServerInfoDurationTime(int durationTime) {
        return update("DurationInfo.updateEnvServerInfoDurationTime", durationTime);
    }

    public int deleteDurationInfoByPk(String durationinfoCd) {
        return delete("DurationInfo.deleteDurationInfoByPk", durationinfoCd);
    }

    /*public DurationInfo selectDurationInfoByPk(String durationinfoCd) {
        return selectOne("DurationInfo.selectDurationInfoByPk", durationinfoCd);
    }*/

}