package com.mnwise.wiseu.web.channel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MessageVo;

/**
 * EM_TRAN 테이블 DAO 클래스
 */
@Repository
public class EmTranDao extends BaseDao {

    public int bulkSMSDataInsert(List<MessageVo> smsList) {
        int row = 0;

        for(int i = 0; i < smsList.size(); i++) {
            MessageVo mv = smsList.get(i);
            row += insert("EmTran.insertDBROSMS", mv);
        }

        return row;
    }

    public int insertDBROLMS(MessageVo message) {
        return insert("EmTran.insertDBROLMS", message);
    }

    public int insertDBROMMS(MessageVo message) {
        return insert("EmTran.insertDBROMMS", message);
    }

}