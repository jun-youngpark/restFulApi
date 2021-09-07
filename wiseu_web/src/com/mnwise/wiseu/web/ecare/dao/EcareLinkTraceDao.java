package com.mnwise.wiseu.web.ecare.dao;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.editor.model.LinkTraceVo;
import com.mnwise.wiseu.web.report.model.LinkTraceResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NVECARELINKTRACE 테이블 DAO 클래스
 */
@Repository
public class EcareLinkTraceDao extends BaseDao {
    public int insertEcareLinkTrace(LinkTraceVo linkTrace) {
        return insert("EcareLinkTrace.insertEcareLinkTrace", linkTrace);
    }

    public int copyEcareLinkTrace(EcareVo ecare) {
        return insert("EcareLinkTrace.copyEcareLinkTrace", ecare);
    }

    public int deleteEcareLinkTrace(int ecareNo) {
        return delete("EcareLinkTrace.deleteEcareLinkTrace", ecareNo);
    }

    /**
     * 이케어 LINK_SEQ 최대 값 +1 를 조회한다.
     */
    public int selectEditorEcareLinkseqMax(int no) {
        return (Integer) selectOne("EcareLinkTrace.selectEditorEcareLinkseqMax", no);
    }

    public int selectEditorEcareLinktraceExist(LinkTraceVo linkTrace) {
        Integer count = (Integer) selectOne("EcareLinkTrace.selectEditorEcareLinktraceExist", linkTrace);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<LinkTraceResultVo> selectLinkTraceResultList(EcareScenarioInfoVo scenarioInfoVo) {
        return selectList("EcareLinkTrace.selectSummaryLinkTraceResultList", scenarioInfoVo);
    }
}