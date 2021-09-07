package com.mnwise.wiseu.web.env.dao;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;

/**
 * NVSENDBLOCKDATEINFO 테이블 DAO 클래스
 */
@Repository
public class SendBlockDateInfoDao extends BaseDao {
    /*public int insertSendBlockDateInfo(SendBlockDateInfo sendBlockDateInfo) {
        return insert("SendBlockDateInfo.insertSendBlockDateInfo", sendBlockDateInfo);
    }

    public int updateSendBlockDateInfoByPk(SendBlockDateInfo sendBlockDateInfo) {
        return update("SendBlockDateInfo.updateSendBlockDateInfoByPk", sendBlockDateInfo);
    }*/

    public int deleteSendBlockDateInfoByPk(String regYear) {
        return delete("SendBlockDateInfo.deleteSendBlockDateInfoByPk", regYear);
    }

    /*public SendBlockDateInfo selectSendBlockDateInfoByPk(String regYear) {
        return selectOne("SendBlockDateInfo.selectSendBlockDateInfoByPk", regYear);
    }*/

    /**
     * 각 휴일(토, 일, 법정공휴일) 등록 여부.
     *
     * @param year
     * @return
     */
    public String selectRegistInfo(int year) {
        return (String) selectOne("SendBlockDateInfo.selectRegistDays", year);
    }
}