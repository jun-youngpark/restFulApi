package com.mnwise.wiseu.web.ecare.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.MailPreviewVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;

/**
 * NVECARETEMPLATE 테이블 DAO 클래스
 */
@Repository
public class EcareTemplateDao extends BaseDao {
    public int copyEcareTemplate(Map<String, Object> paramMap) {
        return insert("EcareTemplate.copyEcareTemplate", paramMap);
    }

    public int insertEditorEcareTemplate(TemplateVo template) {
        return insert("EcareTemplate.insertEditorEcareTemplate", template);
    }

    public int updateEditorEcareTemplate(TemplateVo template) {
        return update("EcareTemplate.updateEditorEcareTemplate", template);
    }

    public int deleteEcareTemplateByPk(int ecareNo, String seg) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("seg", seg);
        return delete("EcareTemplate.deleteEcareTemplateByPk", paramMap);
    }

    public int deleteEcareTemplate(int ecareNo) {
        return delete("EcareTemplate.deleteEcareTemplate", ecareNo);
    }

    @SuppressWarnings("unchecked")
    public List<MailPreviewVo> selectEcareTemplate(Map<String, Object> paramMap) {
        return selectList("EcareTemplate.selectEcareTemplate", paramMap);
    }

    /**
     * 결과보기 용 템플릿 가져오기
     *
     * @param ecareNo 이케어 번호
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MailPreviewVo> selectEcareTemplateHistory(int ecareNo) {
        return selectList("EcareTemplate.selectEcareTemplateHistory", ecareNo);
    }

    public String getEcareSendTemplate(int ecareNo) {
        return (String) selectOne("EcareTemplate.getEcareTemplate", ecareNo);
    }

    /**
     * 모든 템플릿을 조회
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TemplateVo> selectTemplateList() {
        return selectList("EcareTemplate.selectTemplateList");
    }

    /**
     * 발송 이케어를 생성하기 위해 부모 이케어의 템플릿을 가져온다.
     *
     * @param ecareNo 이케어 번호
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TemplateVo> selectEditorEcareTemplate(int ecareNo) {
        return selectList("EcareTemplate.selectEditorEcareTemplate", ecareNo);
    }

    @SuppressWarnings("unchecked")
    public List<TemplateVo> selectEcareTemplateAndKakaoButtons(int ecareNo) {
        return selectList("EcareTemplate.selectEcareTemplateAndKakaoButtons", ecareNo);
    }

    public String selectEcareKakaoButtons(int ecareNo) {
        return (String) selectOne("EcareTemplate.selectEcareKakaoButtons", ecareNo);
    }
}