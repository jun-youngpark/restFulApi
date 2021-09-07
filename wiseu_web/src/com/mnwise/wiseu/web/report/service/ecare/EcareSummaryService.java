package com.mnwise.wiseu.web.report.service.ecare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.EcareDao;
import com.mnwise.wiseu.web.ecare.dao.EcareLinkTraceDao;
import com.mnwise.wiseu.web.ecare.dao.EcareReceiptDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;
import com.mnwise.wiseu.web.ecare.model.EcareVo;
import com.mnwise.wiseu.web.report.dao.EcareRptSendResultDao;
import com.mnwise.wiseu.web.report.dao.KakaoRptSendResultDao;
import com.mnwise.wiseu.web.report.dao.SmsRptSendResultDao;
import com.mnwise.wiseu.web.report.model.LinkTraceResultVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;
import com.mnwise.wiseu.web.report.model.ecare.EcareScenarioInfoVo;

@Service
public class EcareSummaryService extends BaseService {
    @Autowired private EcareDao ecareDao;
    @Autowired private EcareLinkTraceDao ecareLinkTraceDao;
    @Autowired private EcareReceiptDao ecareReceiptDao;
    @Autowired private EcareRptSendResultDao ecareRptSendResultDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private KakaoRptSendResultDao kakaoRptSendResultDao;
    @Autowired private SmsRptSendResultDao smsRptSendResultDao;

    public List<EcareScenarioInfoVo> selectReactionResultList(EcareScenarioInfoVo scenarioInfoVo) {
        return ecareReceiptDao.selectReactionResultList(scenarioInfoVo);
    }

    public Map<String, Object> selectLinkTraceResultList(EcareScenarioInfoVo scenarioInfoVo, int sendCnt, int openCnt) {
        List<LinkTraceResultVo> linkTraceResultList = ecareLinkTraceDao.selectLinkTraceResultList(scenarioInfoVo);
        // 링크클릭 반응결과의 발송대비, 오픈 대비를 위한 send 및 open count 값을 세팅한다.

        int totalUniqueLinkClickCnt = 0;
        int totalAllLinkClickCnt = 0;

        if(linkTraceResultList != null) {
            for(int i = 0; i < linkTraceResultList.size(); i++) {
                linkTraceResultList.get(i).setSendCnt(sendCnt);
                linkTraceResultList.get(i).setOpenCnt(openCnt);

                totalUniqueLinkClickCnt += linkTraceResultList.get(i).getUniqueLinkCnt();
                totalAllLinkClickCnt += linkTraceResultList.get(i).getAllLinkCnt();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("linkTraceResultList", linkTraceResultList);
        map.put("totalUniqueLinkClickCnt", new Integer(totalUniqueLinkClickCnt));
        map.put("totalAllLinkClickCnt", new Integer(totalAllLinkClickCnt));

        return map;
    }

    /**
     * 이케어 월별 통계 데이터를 가져온다.
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareMonthlyStat(Map<String, Object> map) {
        return ecareRptSendResultDao.selectEcareMonthlyStat(map);
    }

    /**
     * 이케어 월별 통계 데이터를 가져온다.
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareFAXMonthlyStat(Map<String, Object> map) {
        return ecareSendResultDao.selectEcareFAXMonthlyStat(map);
    }

    /**
     * 이케어 월별 통계 데이터를 가져온다.
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareSMSMonthlyStat(Map<String, Object> map) {
        return smsRptSendResultDao.selectEcareSMSMonthlyStat(map);
    }

    /**
     * 이케어 일별 통계 데이터를 가져온다.
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareDailyStat(Map<String, Object> map) {
        return ecareRptSendResultDao.selectEcareDailyStat(map);
    }

    /**
     * 이케어 일별 통계 데이터를 가져온다.
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareFAXDailyStat(Map<String, Object> map) {
        return ecareSendResultDao.selectEcareFAXDailyStat(map);
    }

    /**
     * 이케어 SMS 일별 통계 데이터를 가져온다.
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareSMSDailyStat(Map<String, Object> map) {
        return smsRptSendResultDao.selectEcareSMSDailyStat(map);
    }

    /**
     * 리포트 > 이케어 월별 발송 통계에서 이케어리스트 dwr로 가져와 셀렉트박스의 옵션으로 뿌려준다
     *
     * @param serviceType 서비스타입 R(실시간), S(스케쥴), SR(준실시간), SN(준실시간 DB 인터페이스)
     * @param startDt 검색년월
     * @return
     */
    public List<EcareVo> selectEcareType(String ecareType, String startDt) {
        Map<String, Object> map = new HashMap<>();
        String serviceType = ecareType;
        String subType = null;
        if(StringUtil.isNotBlank(ecareType)) {
            // 준실시간(SN,SR)인경우
            if(ecareType.length() == 2) {
                serviceType = ecareType.substring(0, 1);
                subType = ecareType.substring(1, 2);

            }
        }
        map.put("serviceType", serviceType);
        map.put("subType", subType);
        map.put("startDt", startDt);
        return ecareDao.selectEcareType(map);
    }

    /**
     * 카카오 월별 (년단위) 통계(브랜드톡,알림톡,친구톡)
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareKakaoMonthlyStat(Map<String, Object> map) {
        return kakaoRptSendResultDao.selectEcareKakaoMonthlyStat(map);
    }

    /**
     * 카카오 월별 (월단위)통계(브랜드톡,알림톡,친구톡)
     *
     * @param map
     * @return
     */
    public List<RptSendResultVo> selectEcareKakaoDailyStat(Map<String, Object> map) {
        return kakaoRptSendResultDao.selectEcareKakaoDailyStat(map);
    }

}
