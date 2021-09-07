package com.mnwise.wiseu.web.common.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.campaign.dao.TemplateDao;
import com.mnwise.wiseu.web.ecare.dao.EcareTemplateDao;

public class AbstractScenarioService extends BaseService {
    @Autowired private EcareTemplateDao ecareTemplateDao;
    @Autowired private TemplateDao templateDao;

    public String selectKakaoButtons(int serviceNo, String serviceType) {
        if(Const.ServiceType.CAMPAIGN.equalsIgnoreCase(serviceType)) {
            return templateDao.selectCampaignKakaoButtons(serviceNo);
        } else {
            return ecareTemplateDao.selectEcareKakaoButtons(serviceNo);
        }
    }
}
