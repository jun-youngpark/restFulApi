package com.mnwise.wiseu.web.env.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.editor.model.DefaultHandlerVo;

/**
 * NVDEFAULTHANDLER 테이블 DAO 클래스
 */
@Repository
public class DefaultHandlerDao extends BaseDao {
    public int insertDefaultHandler(DefaultHandlerVo defaultHandler) {
        return insert("DefaultHandler.insertDefaultHandler", defaultHandler);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 등록한다.
     *
     * @param defaultHandler
     */
    public int insertHandler(DefaultHandlerVo defaultHandler) {
        return insert("DefaultHandler.insertHandler", defaultHandler);
    }

    public int updateDefaultHandlerByPk(DefaultHandlerVo defaultHandler) {
        return update("DefaultHandler.updateDefaultHandlerByPk", defaultHandler);
    }

    public int deleteDefaultHandlerByPk(int seq) {
        return delete("DefaultHandler.deleteDefaultHandlerByPk", seq);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 변경한다.
     *
     * @param defaultHandler
     */
    public int deleteHandler(DefaultHandlerVo defaultHandler) {
        return delete("DefaultHandler.deleteHandler", defaultHandler);
    }

    public int deleteEditorDefaultHandler() {
        return delete("DefaultHandler.deleteEditorDefaultHandler");
    }

    public DefaultHandlerVo selectDefaultHandlerByPk(int seq) {
        return (DefaultHandlerVo) selectOne("DefaultHandler.selectDefaultHandlerByPk", seq);
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러를 가져온다.
     *
     * @param defaultHandler
     * @return
     */
    public DefaultHandlerVo selectDefaultHandler(int seq) {
        return (DefaultHandlerVo) selectOne("DefaultHandler.selectDefaultHandler", seq);
    }

    /**
     * 기본핸들러 수를 가져온다
     * @return
     */
    public int selectDefaultHandlerCount(DefaultHandlerVo defaultHandler) {
        Integer count = (Integer) selectOne("DefaultHandler.selectDefaultHandlerCount", defaultHandler);
        return (count == null) ? 0 : count;
    }

    /**
     * 환경설정 - 기본핸들러 핸들러 설정에서 기본 핸들러 목록을 가져온다.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DefaultHandlerVo> getHandlerList(DefaultHandlerVo defaultHandler) {
        // mysql에서만 사용함.
        if(null!=defaultHandler) {
            int nowPage = (((defaultHandler.getNowPage()-1) * defaultHandler.getCountPerPage()));
            defaultHandler.setStartRow(nowPage);
        }
        return selectList("DefaultHandler.selectHandlerList", defaultHandler);
    }

    /**
     * 사용가능한 핸들러 목록을 얻는다.
     *
     * @param serviceGubun
     * @param serviceNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DefaultHandlerVo> selectHandlerUseList(String serviceGubun, int serviceNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("service_gubun", serviceGubun);
        paramMap.put("service_no", serviceNo);
        return selectList("DefaultHandler.selectHandlerUseList", paramMap);
    }
}