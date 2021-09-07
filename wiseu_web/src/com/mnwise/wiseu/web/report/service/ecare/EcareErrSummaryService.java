package com.mnwise.wiseu.web.report.service.ecare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.channel.dao.EmLogDao;
import com.mnwise.wiseu.web.channel.dao.MzFtSendLogDao;
import com.mnwise.wiseu.web.channel.dao.MzSendLogDao;
import com.mnwise.wiseu.web.report.dao.EcareDomainLogDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendLogDao;
import com.mnwise.wiseu.web.report.model.DomainLogVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

@Service
public class EcareErrSummaryService extends BaseService {
    @Autowired private EcareDomainLogDao ecareDomainLogDao;
    @Autowired private EcareSendLogDao ecareSendLogDao;
    @Autowired private EmLogDao emLogDao;
    @Autowired private MzFtSendLogDao mzFtSendLogDao;
    @Autowired private MzSendLogDao mzSendLogDao;

    public Map<String, Object> selectDomainResultMsgList(EcareScenarioInfoVo scenarioInfoVo) {
        List<DomainLogVo> domainResultMsgList = ecareDomainLogDao.selectDomainResultMsgList(scenarioInfoVo);
        int totalSoftBounceCnt = 0;
        int totalHardBounceCnt = 0;

        List<DomainLogVo> domainResultMsgListBySoft = new ArrayList<>();
        List<DomainLogVo> domainResultMsgListByHard = new ArrayList<>();
        if(domainResultMsgList != null) {
            for(int i = 0; i < domainResultMsgList.size(); i++) {
                if((domainResultMsgList.get(i)).getBounce().equals("SOFT")) {
                    totalSoftBounceCnt += (domainResultMsgList.get(i)).getTargetCnt();
                } else {
                    totalHardBounceCnt += (domainResultMsgList.get(i)).getTargetCnt();
                }
            }

            for(int i = 0; i < domainResultMsgList.size(); i++) {
                DomainLogVo domainLogVo = domainResultMsgList.get(i);
                if(domainLogVo.getBounce().equals("SOFT")) {
                    domainResultMsgListBySoft.add(domainLogVo);
                } else {
                    domainResultMsgListByHard.add(domainLogVo);
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("domainResultMsgListBySoft", domainResultMsgListBySoft);
        map.put("totalSoftBounceCnt", new Integer(totalSoftBounceCnt));
        map.put("domainResultMsgListByHard", domainResultMsgListByHard);
        map.put("totalHardBounceCnt", new Integer(totalHardBounceCnt));
        return map;
    }

    public Map<String, Object> selectDomainLogList(EcareScenarioInfoVo scenarioInfoVo) {
        List<DomainLogVo> domainLogList = ecareDomainLogDao.selectDomainLogList(scenarioInfoVo);
        int totalSoftBounceCnt = 0;
        int totalHardBounceCnt = 0;
        if(domainLogList != null) {
            for(int i = 0; i < domainLogList.size(); i++) {
                totalSoftBounceCnt += domainLogList.get(i).getSoftCnt();
                totalHardBounceCnt += domainLogList.get(i).getHardCnt();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("domainLogList", domainLogList);
        map.put("totalSoftBounceCnt", new Integer(totalSoftBounceCnt));
        map.put("totalHardBounceCnt", new Integer(totalHardBounceCnt));
        return map;
    }

    // 이케어 스케줄 팩스 오류 분석을 위한 List 조회
    public List<DomainLogVo> selectFaxLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareSendLogDao.selectFaxLogList(scenarioInfoVo);
    }

    public List<DomainLogVo> selectSMSLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return emLogDao.selectSMSLogList(scenarioInfoVo);
    }

    public List<DomainLogVo> selectErrCdResultMsgList(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareDomainLogDao.selectErrCdResultMsgList(scenarioInfoVo);
    }

    /**
     * 알림톡 오류분석
     *
     * @param scenarioInfoVo
     * @return
     */
    public List<DomainLogVo> selectAlimtalkLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return mzSendLogDao.selectAlimtalkLogList(scenarioInfoVo);
    }

    /**
     * 알림톡 오류분석
     *
     * @param scenarioInfoVo
     * @return
     */
    public List<DomainLogVo> selectFriendtalkLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return mzFtSendLogDao.selectFriendtalkLogList(scenarioInfoVo);
    }

    /**
     * 카카오톡 오류분석
     *
     * @param scenarioInfoVo
     * @return
     */
    public List<DomainLogVo> selectKakaoLogList(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareSendLogDao.selectKakaoLogList(scenarioInfoVo);
    }
}
