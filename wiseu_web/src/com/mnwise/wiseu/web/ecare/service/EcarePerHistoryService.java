package com.mnwise.wiseu.web.ecare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.PagingUtil;
import com.mnwise.wiseu.web.ecare.dao.EcareSendLogDao;
import com.mnwise.wiseu.web.ecare.model.EcarePerHistoryVo;

/**
 * 이케어 고객이력 Service
 */
@Service
public class EcarePerHistoryService extends BaseService {
    @Autowired private EcareSendLogDao ecareSendLogDao;

    /**
     * 검색된 E-MAIL 의 총 대상자 수를 구해온다.
     */
    public int getTotalCount(EcarePerHistoryVo historyVo) {
        return ecareSendLogDao.selectTotalCount(historyVo);
    }


    /**
     * 검색된 이메일 발송고객이력리스트를 가져온다.
     */
    @SuppressWarnings("rawtypes")
    public List getPerHistoryList(EcarePerHistoryVo historyVo) {
        PagingUtil.setPagingRowcount(historyVo);
        return ecareSendLogDao.selectPerHistoryList(historyVo);
    }
}
