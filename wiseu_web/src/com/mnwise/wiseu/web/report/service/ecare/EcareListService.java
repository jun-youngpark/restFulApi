package com.mnwise.wiseu.web.report.service.ecare;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

/**
 * 이케어 리포트 리스트
 */
@Service
public class EcareListService extends BaseService {
    @Autowired private EcareDao ecareDao;

    public int selectEcareReportListCount(EcareScenarioInfoVo ecareScenarioInfoVo) {
        return ecareDao.selectEcareReportListCount(ecareScenarioInfoVo);
    }

    public List<EcareScenarioInfoVo> selectEcareReportList(EcareScenarioInfoVo ecareScenarioInfoVo) {
        PagingUtil.setPagingRowcount(ecareScenarioInfoVo);
        return ecareDao.selectEcareReportList(ecareScenarioInfoVo);
    }
}
