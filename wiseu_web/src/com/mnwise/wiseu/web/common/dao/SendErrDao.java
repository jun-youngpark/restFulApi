package com.mnwise.wiseu.web.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.SendErrVo;

/**
 * NVSENDERR 테이블 DAO 클래스
 */
@Repository
public class SendErrDao extends BaseDao {
    public int insertSendErr(SendErrVo sendErr) {
        return insert("SendErr.insertSendErr", sendErr);
    }

    public int updateSendErrByPk(SendErrVo sendErr) {
        return update("SendErr.updateSendErrByPk", sendErr);
    }

    public int deleteSendErrByPk(String errorCd) {
        return delete("SendErr.deleteSendErrByPk", errorCd);
    }

    public int deleteSmtpCodeList(SendErrVo sendErr) {
        return delete("SendErr.deleteSmtpCodeList", sendErr);
    }

    public SendErrVo selectSendErrByPk(String errorCd) {
        return (SendErrVo) selectOne("SendErr.selectSendErrByPk", errorCd);
    }

    /**
     * SMPT 코드 정보를 불러온다 (캠페인 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendErrVo> selectSendErrList() {
        return selectList("SendErr.selectSendErrList");
    }

    @SuppressWarnings("rawtypes")
    public List selectCode(String categoryCd) {
        return selectList("SendErr.selectSmtpCodeList", categoryCd);
    }

    public int codeChecker(String errorCd) {
        Integer count = (Integer) selectOne("SendErr.codeChecker", errorCd);
        return (count == null) ? 0 : count;
    }
}