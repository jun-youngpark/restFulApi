package com.mnwise.wiseu.web.ecare.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.editor.model.HandlerVo;

/**
 * NVECMSGHANDLER 테이블 DAO 클래스
 */
@Repository
public class EcMsgHandlerDao extends BaseDao {
    public int insertEcMsgHandler(HandlerVo handler) {
        return insert("EcMsgHandler.insertEcMsgHandler", handler);
    }

    public int copyEcmHandler(EcareVo ecare) {
        return insert("EcMsgHandler.copyEcmHandler", ecare);
    }

    public int updateEditorEcareHandler(HandlerVo handler) {
        return update("EcMsgHandler.updateEditorEcareHandler", handler);
    }

    public int updateEcareHandlerFromDefault(Map<String, Object> paramMap) {
        return update("EcMsgHandler.updateEcareHandlerFromDefault", paramMap);
    }

    public int deleteEcMsgHandlerByPk(int ecareNo) {
        return delete("EcMsgHandler.deleteEcMsgHandlerByPk", ecareNo);
    }

    /**
     * 이케어 핸들러 정보를 가져온다.
     *
     * @param ecareNo
     * @return
     */
    public HandlerVo selectEcMsgHandlerByPk(int ecareNo) {
        return (HandlerVo) selectOne("EcMsgHandler.selectEcMsgHandlerByPk", ecareNo);
    }

    /**
     * 모든 핸들러를 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<HandlerVo> selectHandlerList() {
        return selectList("EcMsgHandler.selectHandlerList");
    }

    public int selectEditorEcareHandlerCount(int ecareNo) {
        Integer count = (Integer) selectOne("EcMsgHandler.selectEditorEcareHandlerCount", ecareNo);
        return (count == null) ? 0 : count;
    }

}