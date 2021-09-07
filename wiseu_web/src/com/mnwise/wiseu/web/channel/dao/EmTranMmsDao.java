package com.mnwise.wiseu.web.channel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MessageVo;

/**
 * EM_TRAN_MMS 테이블 DAO 클래스
 */
@Repository
public class EmTranMmsDao extends BaseDao {

    public int bulkLMSDataInsert(List<MessageVo> lmsList) {
        int row = 0;

        for(int i = 0; i < lmsList.size(); i++) {
            MessageVo mv = lmsList.get(i);
            row += insert("EmTranMms.insertDBROLMSTRAN", mv);
        }

        return row;
    }

    public int bulkMMSDataInsert(List<MessageVo> mmsList) {
        int row = 0;

        for(int i = 0; i < mmsList.size(); i++) {
            MessageVo mv = mmsList.get(i);
            row += insert("EmTranMms.insertDBROMMSTRAN", mv);
        }

        return row;
    }

    @SuppressWarnings("unchecked")
    public List<MessageVo> getSequence(MessageVo message) {
        return selectList("EmTranMms.getSequence", message);
    }

    public int getEmTranMmsSequence() {
        return (Integer) selectOne("EmTranMms.getEmTranMmsSequence");
    }
}