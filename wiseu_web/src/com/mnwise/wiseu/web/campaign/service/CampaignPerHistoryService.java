package com.mnwise.wiseu.web.campaign.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.campaign.dao.SendLogDao;
import com.mnwise.wiseu.web.campaign.model.CampaignPerHistoryVo;

/**
 * 캠페인 고객이력 Service 캠페인 고객이력조회에서 검색을 DB에서 검색된 값을 반환한다.
 */
@Service
public class CampaignPerHistoryService extends BaseService {
    @Autowired private SendLogDao sendLogDao;

    /**
     * 검색된 E-MAIL 의 총 대상자 수를 구해온다.
     * 메일과 팩스의 총대상자를 따로 구하도록 분리
     */
    public int getTotalCount(CampaignPerHistoryVo historyVo) {
        return sendLogDao.selectTotalCount(historyVo);
    }

    /**
     * 검색된 이메일 발송고객이력리스트를 가져온다.
     */
    public List<CampaignPerHistoryVo> getPerHistoryList(CampaignPerHistoryVo historyVo) {
        return sendLogDao.selectPerHistoryList(historyVo);
    }
}
