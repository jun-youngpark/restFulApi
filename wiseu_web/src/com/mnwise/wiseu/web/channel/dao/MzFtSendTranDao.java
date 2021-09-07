package com.mnwise.wiseu.web.channel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MessageVo;

/**
 * MZFTSENDTRAN 테이블 DAO 클래스
 */
@Repository
public class MzFtSendTranDao extends BaseDao {

    public int bulkFriendtalkDataInsert(List<MessageVo> friendtalkList) {
        int row = 0;

        for(int i = 0; i < friendtalkList.size(); i++) {
            MessageVo mv = friendtalkList.get(i);
            row += insert("MzFtSendTran.insertFriendtalk", mv);
        }

        return row;
    }

}