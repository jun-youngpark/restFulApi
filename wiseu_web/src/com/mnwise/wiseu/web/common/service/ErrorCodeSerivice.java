package com.mnwise.wiseu.web.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.ChannelUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.common.dao.CdMstDao;
import com.mnwise.wiseu.web.common.dao.SendErrDao;
import com.mnwise.wiseu.web.common.model.SendErrVo;

@Service
public class ErrorCodeSerivice extends BaseService {
    @Autowired private CdMstDao cdMstDao;
    @Autowired private SendErrDao sendErrDao;

    public List<SendErrVo> getBychannelErrList(String channel) {
        if(Channel.SMS.equals(channel)) {
            return this.getSMSSendErrList();
        } else if(Channel.LMSMMS.equals(channel)) {
            return this.getMMSSendErrList();
        } else if(Channel.FAX.equals(channel)) {
            return this.getFAXSendErrList();
        } else if(ChannelUtil.isKakao(channel)) {
            return this.getKakaoSendErrList(channel);
        } else if(Channel.PUSH.equals(channel)) {
            return this.getPushSendErrCodeList();
        } else {
            return this.getSendErrList();
        }
    }
    /**
     * SMPT 코드 정보를 불러온다 (캠페인/이케어 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    public List<SendErrVo> getSendErrList() {
        return sendErrDao.selectSendErrList();
    }

    /**
     * FAX 코드 정보를 불러온다 (캠페인/이케어 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    public List<SendErrVo> getFAXSendErrList() {
        return cdMstDao.selectFAXSendErrList();
    }

    /**
     * SMS 코드 정보를 불러온다 (캠페인/이케어 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    public List<SendErrVo> getSMSSendErrList() {
        return cdMstDao.selectSMSSendErrList();
    }

    /**
     * MMS/LMS 코드 정보를 불러온다 (캠페인/이케어 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    public List<SendErrVo> getMMSSendErrList() {
        return cdMstDao.selectMMSSendErrList();
    }

    /**
     * PUSH ERROR 코드 리스트
     * @return
     */
    public List<SendErrVo> getPushSendErrCodeList() {
        return cdMstDao.getPushSendErrCodeList();
    }
    /**
     * 카카오 에러코드 리스트
     *
     * @return
     */
    public List<SendErrVo> getKakaoSendErrList(String channel) {
        return cdMstDao.getKakaoSendErrList(channel);
    }



}
