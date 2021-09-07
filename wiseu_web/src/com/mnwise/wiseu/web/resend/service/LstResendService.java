package com.mnwise.wiseu.web.resend.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const.Client;
import com.mnwise.wiseu.web.campaign.dao.SendLogDao;
import com.mnwise.wiseu.web.channel.dao.MzFtSendLogDao;
import com.mnwise.wiseu.web.channel.dao.MzSendLogDao;
import com.mnwise.wiseu.web.common.dao.ServerInfoDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendLogDao;
import com.mnwise.wiseu.web.resend.dao.ResendRequestDao;
import com.mnwise.wiseu.web.resend.model.LstResendVo;

@Service
public class LstResendService extends BaseService {
    @Autowired private EcareSendLogDao ecareSendLogDao;
    @Autowired private MzFtSendLogDao mzFtSendLogDao;
    @Autowired private MzSendLogDao mzSendLogDao;
    @Autowired private ResendRequestDao resendRequestDao;
    @Autowired private SendLogDao sendLogDao;
    @Autowired private ServerInfoDao serverInfoDao;

    public int insertLstResendRequest(Map<String, Object> resendRequest) {
        return resendRequestDao.insertLstResendRequest(resendRequest);
    }

    public LstResendVo selectPreviousDetail(LstResendVo lstResendVo) {
        if(lstResendVo.getClient().equalsIgnoreCase(Client.EM)) {
            return sendLogDao.selectCamPrevious(lstResendVo);
        }

        return ecareSendLogDao.selectEcarePrevious(lstResendVo);
    }

    public String getAltalkMsg(LstResendVo lstResendVo) {
        return mzSendLogDao.getAltalkMsg(lstResendVo);
    }

    public String getFrtalkMsg(LstResendVo lstResendVo) {
        return mzFtSendLogDao.getFrtalkMsg(lstResendVo);
    }

    public int getResendTargetCount(Map<String, Object> map) {
        if(Client.EC.equalsIgnoreCase((String) map.get("client"))) {
            return ecareSendLogDao.selectEcareResendTargetCount(map);
        } else {
            return sendLogDao.selectCamResendTargetCount(map);
        }
    }

    public String getResendMailErrorCd() {
        return serverInfoDao.getResendMailErrorCd();
    }

    public String getResendFaxErrorCd() {
        return serverInfoDao.getResendFaxErrorCd();
    }

    public String getResendSmsErrorCd() {
        return serverInfoDao.getResendSmsErrorCd();
    }

    public String getResendAltalkErrorCd() {
        return serverInfoDao.getResendAltalkErrorCd();
    }

    public String getResendFRTErrorCd() {
        return serverInfoDao.getResendFRTErrorCd();
    }
}
