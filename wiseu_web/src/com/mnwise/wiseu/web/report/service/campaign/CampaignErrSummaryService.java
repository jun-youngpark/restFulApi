package com.mnwise.wiseu.web.report.service.campaign;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.campaign.dao.DomainLogDao;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.campaign.ScenarioInfoVo;

@Service
public class CampaignErrSummaryService extends BaseService {
    @Autowired private DomainLogDao domainLogDao;

    public List<DomainLogVo> selectErrCdResultMsgList(ScenarioInfoVo scenarioInfoVo) {
        return domainLogDao.selectErrCdResultMsgList(scenarioInfoVo);
    }
}
