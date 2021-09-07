package com.mnwise.wiseu.web.env.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.env.model.SendBlockDateVo;

/**
 * NVSENDBLOCKDATE 테이블 DAO 클래스
 */
@Repository
public class SendBlockDateDao extends BaseDao {
    public int insertSendBlockDate(SendBlockDateVo sendBlockDate) {
        return insert("SendBlockDate.insertSendBlockDate", sendBlockDate);
    }

    /**
     * 휴일을 등록한다.
     *
     * @param chkYear
     * @param blockDt
     */
    public int insertRestDays(String chkYear, String blockDt) {
        SendBlockDateVo sendBlockDate = new SendBlockDateVo();
        sendBlockDate.setChkYear(chkYear);
        sendBlockDate.setBlockDt(blockDt);

        return insert("SendBlockDate.insertRestDays", sendBlockDate);
    }

    public int updateSendBlockDateByPk(SendBlockDateVo sendBlockDate) {
        return update("SendBlockDate.updateSendBlockDateByPk", sendBlockDate);
    }

    public int deleteSendBlockDateByPk(String chkYear, String blockDt, String channelType) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("chkYear", chkYear);
        paramMap.put("blockDt", blockDt);
        paramMap.put("channelType", channelType);
        return delete("SendBlockDate.deleteSendBlockDateByPk", paramMap);
    }

    /**
     * 등록된 휴일을 삭제한다.
     *
     * @param chkYear
     * @param blockDt
     */
    public int deleteRestDays(String chkYear, String blockDt) {
        SendBlockDateVo sendBlockDate = new SendBlockDateVo();
        sendBlockDate.setChkYear(chkYear);
        sendBlockDate.setBlockDt(blockDt);

        return delete("SendBlockDate.deleteRestDays", sendBlockDate);
    }

    /**
     * 휴일 목록을 가져온다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendBlockDateVo> selectRestDays() {
        return selectList("SendBlockDate.selectRestDays");
    }

    /**
     * 휴일 등록 여부를 확인한다.
     *
     * @param chkYear
     * @param blockDt
     * @return
     */
    public boolean confirmRestDays(String chkYear, String blockDt) {
        SendBlockDateVo envBlockDateVo = new SendBlockDateVo();
        envBlockDateVo.setChkYear(chkYear);
        envBlockDateVo.setBlockDt(blockDt);

        int cnt = (Integer) selectOne("SendBlockDate.confirmRestDays", envBlockDateVo);
        return (cnt == 0) ? true : false;
    }

}