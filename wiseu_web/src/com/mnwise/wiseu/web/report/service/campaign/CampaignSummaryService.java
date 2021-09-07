package com.mnwise.wiseu.web.report.service.campaign;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.campaign.model.SendResultVo;
import com.mnwise.wiseu.web.report.dao.KakaoRptSendResultDao;
import com.mnwise.wiseu.web.report.dao.SmsRptSendResultDao;

@Service
public class CampaignSummaryService extends BaseService {
    @Autowired private KakaoRptSendResultDao kakaoRptSendResultDao;
    @Autowired private SendResultDao sendResultDao;
    @Autowired private SmsRptSendResultDao smsRptSendResultDao;

    public List<SendResultVo> selectCampaignMonthlyStat(Map<String, Object> map) {
        return sendResultDao.selectCampaignMonthlyStat(map);
    }

    public List<SendResultVo> selectCampaignKakaoMonthlyStat(Map<String, Object> map) {
        return kakaoRptSendResultDao.selectCampaignKakaoMonthlyStat(map);
    }

    public List<SendResultVo> selectCampaignSMSMonthlyStat(Map<String, Object> map) {
        return smsRptSendResultDao.selectCampaignSMSMonthlyStat(map);
    }

    public List<SendResultVo> selectCampaignTOTMonthlyStat(Map<String, Object> map) {
        return sendResultDao.selectCampaignTOTMonthlyStat(map);
    }

    public List<SendResultVo> selectCampaignDailyStat(Map<String, Object> map) {
        return sendResultDao.selectCampaignDailyStat(map);
    }

    public List<SendResultVo> selectCampaignSMSDailyStat(Map<String, Object> map) {
        return smsRptSendResultDao.selectCampaignSMSDailyStat(map);
    }

    public List<SendResultVo> selectCampaignTOTDailyStat(Map<String, Object> map) {
        return sendResultDao.selectCampaignTOTDailyStat(map);
    }
}
