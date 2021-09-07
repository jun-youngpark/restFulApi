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
 * NVECARETEMPLATEHISTORY 테이블 DAO 클래스
 */
@Repository
public class EcareTemplateHistoryDao extends BaseDao {
    public int insertEcareTemplateHistory(HistoryVo history) {
        return insert("EcareTemplateHistory.insertEcareTemplateHistory", history);
    }

    public int deleteEcareTemplateHistoryAll(int ecareNo) {
        return delete("EcareTemplateHistory.deleteEcareTemplateHistoryAll", ecareNo);
    }

    public MailPreviewVo selectEcarePreviousTemplate(MailPreviewVo mailPreview) {
        return (MailPreviewVo) selectOne("EcareTemplateHistory.selectEcarePreviousTemplate", mailPreview);
    }

    public MailPreviewVo selectEcarePreviousCoverTemplate(MailPreviewVo mailPreview) {
        return (MailPreviewVo) selectOne("EcareTemplateHistory.selectEcarePreviousCoverTemplate", mailPreview);
    }

    public MailPreviewVo selectEcarePreviousPrefaceTemplate(MailPreviewVo mailPreview) {
        return (MailPreviewVo) selectOne("EcareTemplateHistory.selectEcarePreviousPrefaceTemplate", mailPreview);
    }

    /**
     * 검색된 템플릿 컨텐츠 변경이력 총 건수를 가져온다.
     *
     * @param contsVo 컨텐츠 구분, 이케어번호, 이케어명 검색조건을 받는 VO
     * @return 검색된 템플릿 변경이력 총 카운트
     */
    public int selectContsTemplateTotalCount(EcareContsHistoryVo contsVo) {
        Integer count = (Integer) selectOne("EcareTemplateHistory.selectContsTemplateTotalCount", contsVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 검색된 템플릿 변경이력 리스트를 가져온다.
     *
     * @param contsVo 컨텐츠 구분, 이케어번호, 이케어명 검색조건을 받는 VO
     * @return 검색된 템플릿 변경이력 리스트
     */
    @SuppressWarnings("unchecked")
    public List<EcareContsHistoryVo> selectContsTemplateHistoryList(EcareContsHistoryVo contsVo) {
        List<EcareContsHistoryVo> tmp = (List<EcareContsHistoryVo>) selectList("EcareTemplateHistory.selectContsTemplateHistoryList", contsVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 선택된 템플릿 히스토리 정보를 가져온다.
     *
     * @param contsVo 선택된 템플릿 조회 정보를 담는 VO
     * @return 선택된 템플릿 히스토리 VO
     */
    public EcareContsHistoryVo selectContsTemplateHistoryInfo(EcareContsHistoryVo contsVo) {
        EcareContsHistoryVo tmp = (EcareContsHistoryVo) selectOne("EcareTemplateHistory.selectContsTemplateHistoryInfo", contsVo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 템플릿의 다음 버전을 가져온다.
     *
     * @param ecareNo, seg(템플릿 구분)
     * @return 템플릿 버전 정보
     */
    public int selectNextTemplateVersion(int ecareNo, String seg) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("seg", seg);
        return (Integer) selectOne("EcareTemplateHistory.selectNextTemplateVersion", paramMap);
    }
}