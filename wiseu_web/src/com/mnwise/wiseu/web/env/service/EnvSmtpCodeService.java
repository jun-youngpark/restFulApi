package com.mnwise.wiseu.web.env.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.common.dao.SendErrDao;
import com.mnwise.wiseu.web.common.model.SendErrVo;
import com.mnwise.wiseu.web.env.dao.SmtpCategoryDao;
import com.mnwise.wiseu.web.env.model.json.SmtpCodeTreeEelement;

@Service
public class EnvSmtpCodeService extends BaseService {
    @Autowired private SendErrDao sendErrDao;
    @Autowired private SmtpCategoryDao smtpCategoryDao;

    /**
     * 환경설정 - 코드관리에서 SMTP 코드 그룹 리스트를 가져온다.
     *
     * @param treeEelement
     * @return
     */
    public List<SmtpCodeTreeEelement> selectSmtpCategoryList(String pcategoryCd) {
        return smtpCategoryDao.selectSmtpCategoryList(pcategoryCd);
    }

    public int insertSmtpCodeList(String categoryCd, String errorCd, String errorDesc, String spamYn) {
        int row = sendErrDao.codeChecker(errorCd);
        if(row == 0) {
            SendErrVo sendErr = new SendErrVo();
            sendErr.setCategoryCd(categoryCd);
            sendErr.setErrorCd(errorCd);
            sendErr.setErrorDesc(errorDesc);
            sendErr.setSpamYn(spamYn);
            sendErr.setChannelType("M");

            sendErrDao.insertSendErr(sendErr);
            return 0;
        }

        return row;
    }

    public int deleteSmtpCodeList(String categoryCd, String errorCd) {
        SendErrVo sendErr = new SendErrVo();
        sendErr.setCategoryCd(categoryCd);
        sendErr.setErrorCd(errorCd);

        return sendErrDao.deleteSmtpCodeList(sendErr);
    }

    @SuppressWarnings("rawtypes")
    public List selectCode(String categoryCd) {
        return sendErrDao.selectCode(categoryCd);
    }
}
