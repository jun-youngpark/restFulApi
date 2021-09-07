package com.mnwise.wiseu.web.report.service.campaign;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.campaign.dao.LinkTraceDao;
import com.mnwise.wiseu.web.campaign.dao.SendResultDao;
import com.mnwise.wiseu.web.common.dao.TagDao;
import com.mnwise.wiseu.web.common.model.Tag;
import com.mnwise.wiseu.web.report.model.LinkTraceResult2Vo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignReactionResult2Vo;
import com.mnwise.wiseu.web.report.model.campaign.CampaignSendResultVo;

@Service
public class CampaignCompareWithService extends BaseService {
    @Autowired private TagDao tagDao;
    @Autowired private SendResultDao sendResultDao;
    @Autowired private LinkTraceDao linkTraceDao;

    public List<Tag> selectTagCloud(Map<String, Object> map) {
        return tagDao.selectTagCloud(map);
    }

    public List<CampaignSendResultVo> selectSendResultList(Map<String, Object> map) {
        return sendResultDao.selectSendResultList(map);
    }

    public List<Integer> selectCampaignNoList(Map<String, Object> map) {
        return sendResultDao.selectCampaignNoList(map);
    }

    public List<CampaignReactionResult2Vo> selectReactionResult2List(Map<String, Object> map) {
        return sendResultDao.selectReactionResult2List(map);
    }

    public List<LinkTraceResult2Vo> selectLinkTraceResult2List(Map<String, Object> map) {
        return linkTraceDao.selectLinkTraceResult2List(map);
    }
}
