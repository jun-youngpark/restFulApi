package com.mnwise.wiseu.web.channel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MessageVo;

/**
 * MZSENDTRAN 테이블 DAO 클래스
 */
@Repository
public class MzSendTranDao extends BaseDao {

    public int bulkAlimtalkDataInsert(List<MessageVo> alimtalkList) {
        int row = 0;

        for(int i = 0; i < alimtalkList.size(); i++) {
            MessageVo mv = alimtalkList.get(i);
            row += insert("MzSendTran.insertAlimtalk", mv);
        }

        return row;
    }

}