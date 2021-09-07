package com.mnwise.wiseu.web.report.service.ecare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.Const.Channel;
import com.mnwise.wiseu.web.ecare.dao.EcareReceiptDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendLogDao;
import com.mnwise.wiseu.web.ecare.dao.EcareSendResultDao;
import com.mnwise.wiseu.web.report.dao.EcareRptSendResultDao;
import com.mnwise.wiseu.web.report.dao.KakaoRptSendResultDao;
import com.mnwise.wiseu.web.report.dao.SmsRptSendResultDao;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;

@Service
public class EcareDailyService extends BaseService {
    @Autowired private EcareReceiptDao ecareReceiptDao;
    @Autowired private EcareRptSendResultDao ecareRptSendResultDao;
    @Autowired private EcareSendLogDao ecareSendLogDao;
    @Autowired private EcareSendResultDao ecareSendResultDao;
    @Autowired private SmsRptSendResultDao smsRptSendResultDao;
    @Autowired private KakaoRptSendResultDao kakaoRptSendResultDao;

    /**
     * 이케어 일별발송현황 수신확인 업데이트 인터페이스-스케쥴
     *
     * @param ecareNo, sendStartDt, resultSeq
     * @return
     */
    public int updateEcareDailyScheduleReceiptList(int ecareNo, String sendStartDt, String resultSeq) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("sendStartDt", sendStartDt);
        paramMap.put("resultSeq", resultSeq);

        List<String> result = ecareRptSendResultDao.selectEcareDailyScheduleReceiptList(paramMap);
        if(result.size() == 0) {
            ecareRptSendResultDao.insertEcareDailyScheduleReceiptList(paramMap);
        }

        return ecareRptSendResultDao.updateEcareDailyScheduleReceiptList(paramMap);
    }

    /**
     * 이케어 일별발송현황 수신확인 업데이트 인터페이스-실시간
     *
     * @param ecareNo, sendStartDt
     * @return
     */
    public int updateEcareDailyRealtimeReceiptList(int ecareNo, String sendStartDt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ecareNo", ecareNo);
        paramMap.put("sendStartDt", sendStartDt);

        List<String> result = ecareRptSendResultDao.selectEcareDailyRealtimeReceiptList(paramMap);
        if(result.size() == 0) {
            ecareRptSendResultDao.insertEcareDailyRealtimeReceiptList(paramMap);
        }

        return ecareRptSendResultDao.updateEcareDailyRealtimeReceiptList(paramMap);
    }

    public Map<String, Object> selectEcareScheduleDailyReportList(String channelType, String serviceType, String subType, Map<String,Object> paramMap) throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        List<RptSendResultVo> ecareDailyReportList = null;

        // 스케쥴 :
        if(("S".equals(serviceType)) && ("S".equals(subType))) {
            if(Channel.MAIL.equals(channelType)) {// 스케쥴 : MAIL => NVECARERPTSENDRESULT ==> 차후 SENDRESULT로 수정요
                ecareDailyReportList = ecareSendResultDao.selectEcareScheduleDailyReportList(paramMap);
            } else if(Channel.FAX.equals(channelType)) { // 스케줄 : FAX => NVECARESENDRESULT
                ecareDailyReportList = ecareSendResultDao.selectEcareFaxScheduleDailyReportList(paramMap);
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {// 스케줄 : SMS => NVSMSRPTSENDRESULT
                ecareDailyReportList = smsRptSendResultDao.selectEcareSMSScheduleDailyReportList(paramMap);
            } else if(Channel.ALIMTALK.equals(channelType)) {// 알림톡 => MZSENDLOG
                ecareDailyReportList = ecareSendResultDao.selectEcareAlimtalkDailyReportList(paramMap);
            } else if(Channel.FRIENDTALK.equals(channelType)) {// 친구톡 => MZFTSENDLOG 2018.03
                ecareDailyReportList = ecareSendResultDao.selectEcareFriendtalkDailyReportList(paramMap);
            }else if(Channel.BRANDTALK.equals(channelType)) {// 브랜드톡 => MZBTSENDLOG 2020.06
                ecareDailyReportList = kakaoRptSendResultDao.selectEcareKakaoDailyReportList(paramMap);
            } else if(Channel.PUSH.equals(channelType)) {
                ecareDailyReportList = ecareSendResultDao.selectEcareScheduleDailyReportList(paramMap);
            }
        } else {
            // 실시간 :
            if(Channel.MAIL.equals(channelType)) {// MAIL => NVECARERPTSENDRESULT ==> 차후 SENDRESULT로 수정요
                ecareDailyReportList = ecareSendResultDao.selectEcareRealtimeDailyReportList(paramMap);
            } else if(Channel.FAX.equals(channelType)) { // 실시간 : FAX => NVECARESENDRESULT
                ecareDailyReportList = ecareSendResultDao.selectEcareFaxDailyReportList(paramMap);
            } else if(Channel.SMS.equals(channelType) || Channel.LMSMMS.equals(channelType)) {// 실시간 : SMS => NVSMSRPTSENDRESULT
                ecareDailyReportList = smsRptSendResultDao.selectEcareSMSDailyReportList(paramMap);
            } else if(Channel.ALIMTALK.equals(channelType)) {// 알림톡 => MZSENDLOG
                ecareDailyReportList = ecareSendResultDao.selectEcareAlimtalkDailyReportList(paramMap);
            } else if(Channel.FRIENDTALK.equals(channelType)) {// 친구톡 => MZFTSENDLOG 2018.03
                ecareDailyReportList = ecareSendResultDao.selectEcareFriendtalkDailyReportList(paramMap);
            }else if(Channel.BRANDTALK.equals(channelType)) {// 브랜드톡 => MZBTSENDLOG 2020.06
                ecareDailyReportList = kakaoRptSendResultDao.selectEcareKakaoDailyReportList(paramMap);
            }else if(Channel.PUSH.equals(channelType)) {
                ecareDailyReportList = ecareSendResultDao.selectEcareRealtimeDailyReportList(paramMap);
            }
        }
        if(Channel.PUSH.equals(channelType)) {
            List<CaseInsensitiveMap> pushReceiveList = new ArrayList<>();
            List<CaseInsensitiveMap> pushSendlogList = new ArrayList<>();
            int ecareNo = (int)paramMap.get("ecareNo");
            int sendCnt = 0, succCnt = 0, openCnt = 0;
            for(RptSendResultVo resultVo : ecareDailyReportList) {
                Map<String, Object> countMaps = getEcareResultCount(ecareNo, resultVo.getResultSeq(), resultVo.getSendStartDt(), resultVo.getSendStartDt());
                openCnt += (Integer)countMaps.get("openCnt");
                succCnt += (Integer)countMaps.get("succCnt");
                sendCnt += (Integer)countMaps.get("sendCnt");
                pushReceiveList.add((CaseInsensitiveMap)countMaps.get("receiptCountMap"));
                pushSendlogList.add((CaseInsensitiveMap)countMaps.get("sendlogCountMap"));
            }
            returnData.put("pushReceiveCnt", openCnt);
            returnData.put("pushSendCnt", sendCnt);
            returnData.put("pushSuccCnt", succCnt);
            returnData.put("pushReceiveList", pushReceiveList);
            returnData.put("pushSendlogList", pushSendlogList);
        }

        returnData.put("ecareDailyReportList", ecareDailyReportList);
        return returnData;
    }

    public Map<String, Object> getEcareResultCount(int ecareNo, String resultSeq, String startDt, String endDt) throws Exception {
        int openCnt=0, succCnt=0, sendCnt=0;
        Map<String, Object> resultMap = new HashMap<>();
        CaseInsensitiveMap receiptCountMap = ecareReceiptDao.selectPushEcareReceiptCount(ecareNo, resultSeq, startDt, endDt);
        CaseInsensitiveMap sendlogCountMap = ecareSendLogDao.selectPushEcareSendlogCount(ecareNo, resultSeq, startDt, endDt);
        if(null!=receiptCountMap) {
            openCnt += Integer.parseInt(String.valueOf(receiptCountMap.get("IOS_RECEIPT"))) + Integer.parseInt(String.valueOf(receiptCountMap.get("AND_RECEIPT")));
        }
        if(null!=sendlogCountMap) {
            sendCnt += Integer.parseInt(String.valueOf(sendlogCountMap.get("SEND_CNT")));
            succCnt += Integer.parseInt(String.valueOf(sendlogCountMap.get("SUCC_CNT")));
        }

        // 결과 반환
        resultMap.put("openCnt", openCnt);
        resultMap.put("succCnt", succCnt);
        resultMap.put("sendCnt", sendCnt);
        resultMap.put("receiptCountMap", receiptCountMap);
        resultMap.put("sendlogCountMap", sendlogCountMap);
        return resultMap;
    }
}
