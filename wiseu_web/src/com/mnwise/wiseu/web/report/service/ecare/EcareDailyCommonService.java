package com.mnwise.wiseu.web.report.service.ecare;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

@Service
public class EcareDailyCommonService extends BaseService {
    @Autowired private EcareDao ecareDao;

    public List<EcareVo> getScenarioChannelList(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareDao.getScenarioChannelList(scenarioInfoVo);
    }

    public EcareScenarioInfoVo selectScenarioInfo(EcareScenarioInfoVo scenarioInfoVo) {
        EcareScenarioInfoVo tmp;

        if(scenarioInfoVo.getServiceType().equals("R") || scenarioInfoVo.getSubType().equals("N")) {
            tmp = ecareDao.selectScenarioRealtimeInfo(scenarioInfoVo.getEcareNo());
        } else {
            tmp = ecareDao.selectScenarioScheduleInfo(scenarioInfoVo.getEcareNo());
        }

        return tmp;
    }
}
