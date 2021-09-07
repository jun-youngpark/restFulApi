package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.ecare.model.EcareContsHistoryVo;
import com.mnwise.wiseu.web.editor.model.HistoryVo;

/**
 * NVECMSGHANDLERHISTORY 테이블 DAO 클래스
 */
@Repository
public class EcMsgHandlerHistoryDao extends BaseDao {
    public int insertEcareHandlerHistory(HistoryVo history) {
        return insert("EcMsgHandlerHistory.insertEcareHandlerHistory", history);
    }

    public int deleteEcmsgHandlerHistoryAll(int ecareNo) {
        return delete("EcMsgHandlerHistory.deleteEcmsgHandlerHistoryAll", ecareNo);
    }

    public MailPreviewVo selectEcarePreviousHandler(MailPreviewVo mailPreview) {
        return (MailPreviewVo) selectOne("EcMsgHandlerHistory.selectEcarePreviousHandler", mailPreview);
    }

    /**
     * 검색된 핸들러 변경이력 총 건수를 가져온다.
     *
     * @param contsVo 컨텐츠 구분, 이케어번호, 이케어명 검색조건을 받는 VO
     * @return 검색된 핸들러 변경이력 총 카운트
     */
    public int selectContsHandlerTotalCount(EcareContsHistoryVo contsVo) {
        Integer count = (Integer) selectOne("EcMsgHandlerHistory.selectContsHandlerTotalCount", contsVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 검색된 핸들러 변경이력 리스트를 가져온다.
     *
     * @param contsVo 컨텐츠 구분, 이케어번호, 이케어명 검색조건을 받는 VO
     * @return 검색된 핸들러 변경이력 리스트
     */
    @SuppressWarnings("unchecked")
    public List<EcareContsHistoryVo> selectContsHandlerHistoryList(EcareContsHistoryVo contsVo) {
        List<EcareContsHistoryVo> tmp = (List<EcareContsHistoryVo>) selectList("EcMsgHandlerHistory.selectContsHandlerHistoryList", contsVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 선택된 핸들러 히스토리 정보를 가져온다.
     *
     * @param contsVo 선택된 핸들러 조회 정보를 담는 VO
     * @return 선택된 핸들러 히스토리 VO
     */
    public EcareContsHistoryVo selectContsHandlerHistoryInfo(EcareContsHistoryVo contsVo) {
        EcareContsHistoryVo tmp = (EcareContsHistoryVo) selectOne("EcMsgHandlerHistory.selectContsHandlerHistoryInfo", contsVo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 핸들러의 다음 버전을 가져온다.
     *
     * @param ecareNo
     * @return 핸들러 버전 정보
     */
    public int selectNextHandlerVersion(int ecareNo) {
        return (Integer) selectOne("EcMsgHandlerHistory.selectNextHandlerVersion", ecareNo);
    }
}