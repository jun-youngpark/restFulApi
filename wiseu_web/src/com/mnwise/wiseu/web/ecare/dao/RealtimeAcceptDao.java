package com.mnwise.wiseu.web.ecare.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MimeViewVo;
import com.mnwise.wiseu.web.rest.model.RequestVo;

/**
 * NVREALTIMEACCEPT 테이블 DAO 클래스
 */
@Repository
public class RealtimeAcceptDao extends BaseDao {

    public int defferedTargetReSend(Map<String, Object> paramMap) {
        return insert("RealtimeAccept.defferedTargetReSend", paramMap);
    }

    public int insertResendDataForDefferedTime(MimeViewVo mimeVo) {
        return insert("RealtimeAccept.insertResendDataForDefferedTime", mimeVo);
    }

    public int insertResendDataForJeonmun(MimeViewVo mimeVo) {
        return insert("RealtimeAccept.insertResendDataForJeonmun", mimeVo);
    }

    public int insertResendDataForSchedule(MimeViewVo mimeVo) {
        return insert("RealtimeAccept.insertResendDataForSchedule", mimeVo);
    }

    public int create(RequestVo reqVo) {
        security.securityObject(reqVo, "ENCRYPT");
        return insert("RealtimeAccept.create", reqVo);
    }

    public int updateJeonmun(MimeViewVo mimeVo) {
        return update("RealtimeAccept.updateJeonmun", mimeVo);
    }

    public int update(RequestVo reqVo) {
        return update("RealtimeAccept.update", reqVo);
    }

    /**
     * 준실시간 미리보기 후 발송 처리
     *
     * @param seq
     * @return
     */
    public int defferedTargetSend(String seq) {
        return update("RealtimeAccept.defferedTargetSend", seq);
    }

    public int delete(RequestVo reqVo) {
        return delete("RealtimeAccept.delete", reqVo);
    }

    public String selectDefferedTimeJeonmun(MimeViewVo mimeVo) {
        return (String) selectOne("RealtimeAccept.selectDefferedTimeJeonmun", mimeVo);
    }

    public String read(RequestVo reqVo) {
        return (String) selectOne("RealtimeAccept.read", reqVo);
    }

}