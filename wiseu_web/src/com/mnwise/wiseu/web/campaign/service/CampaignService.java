package com.mnwise.wiseu.web.campaign.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.base.util.FormatUtil;
import com.mnwise.wiseu.web.campaign.dao.CampaignDao;
import com.mnwise.wiseu.web.campaign.dao.DivideScheduleDao;
import com.mnwise.wiseu.web.campaign.dao.ScenarioDao;
import com.mnwise.wiseu.web.campaign.dao.TestSendLogDao;
import com.mnwise.wiseu.web.campaign.dao.TraceInfoDao;
import com.mnwise.wiseu.web.campaign.model.CampaignVo;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.campaign.model.ScenarioVo;
import com.mnwise.wiseu.web.campaign.model.TestSendLog;

@Service
public class CampaignService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(CampaignService.class);

    @Autowired private CampaignDao campaignDao;
    @Autowired private DivideScheduleDao divideScheduleDao;
    @Autowired private ScenarioDao scenarioDao;
    @Autowired private TestSendLogDao testSendLogDao;
    @Autowired private TraceInfoDao traceInfoDao;

    /**
     * 캠페인 상세 정보를 임시 저장한다. campaing_2step_**_form.jsp에서 dwr로 호출
     *
     * @param scenarioNo
     * @param campaignNo
     * @param param
     * @return
     */
    public void setRegistTempCampaign2StepInfo(int scenarioNo, int campaignNo, String param) {
        StringTokenizer st = new StringTokenizer(param, "&");
        String[] temp = null;
        ScenarioVo scenarioVo = new ScenarioVo();
        MutablePropertyValues values = new MutablePropertyValues();
        while(st.hasMoreTokens()) {
            temp = org.springframework.util.StringUtils.delimitedListToStringArray(st.nextToken(), "=");
            try {
                values.addPropertyValue(temp[0], URLDecoder.decode(temp[1], "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                log.error(null, e);
            }
        }

        DataBinder binder = new DataBinder(scenarioVo);
        binder.bind(values);
        CampaignVo campaignVo = scenarioVo.getCampaignVo();
        campaignVo.setSegmentNo(scenarioVo.getSegmentNo());
        campaignVo.setTargetCnt(scenarioVo.getSegmentSize());

        String nowDt = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String nowTm = DateFormatUtils.format(new Date(), "HHmmss");

        scenarioVo.setFinishYn("N");
        scenarioVo.setLastUpdateDt(nowDt);
        scenarioVo.setLastUpdateTm(nowTm);

        campaignVo.setLastUpdateDt(nowDt);
        campaignVo.setLastUpdateTm(nowTm);

        scenarioDao.updateScenario2StepInfo(scenarioVo);
        campaignDao.updateCampaign2StepInfo(campaignVo);

        // 3STEP으로 스케쥴 설정을 옮김
        // campaignDao.registTraceInfo(campaignVo);
    }

    /**
     * 캠페인 상태를 변경한다. campaign2Step_채널.jsp, campaign3Step.jsp 에서 dwr로 호출
     *
     * @param campaignNo
     * @param campaignSts
     * @return
     */
    public String setCampaignStsInfo(int campaignNo, String campaignSts) {
        CampaignVo campaignVo = campaignDao.selectCampaignInfo(campaignNo);
        // 승인 요청 중인 캠페인 인지를 우선 체크한다.
        if("C".equals(campaignSts) && campaignSts.equals(campaignVo.getCampaignSts())) {
            // 승인 취소 이므로 미작성 상태로 변경한다.
            campaignSts = "I";
        }
        // 스케쥴이 저장되었는지 우선 체크한다.
        if(StringUtil.isEmpty(campaignVo.getSendStartDt()) || StringUtil.isEmpty(campaignVo.getSendStartTm())) {
            return "notSchedule";
        }
        // 이미 다른 사람(또는 다른 브라우저)에서 상태가 변경된 경우
        if(campaignSts.equals(campaignVo.getCampaignSts())) {
            return "blockChange";
        } else {
            campaignVo.setCampaignNo(campaignNo);
            campaignVo.setCampaignSts(campaignSts);

            campaignDao.updateCampaignStsInfo(campaignVo);
            return campaignSts;
        }
    }

    /**
     * 캠페인 승인 상태를 변경한다. campaing_3step_**_form.jsp에서 dwr로 호출
     *
     * @param campaignNo
     * @param approvalSts
     * @return
     */
    public String setApprovalStsInfo(int campaignNo, String approvalSts) {
        CampaignVo campaignVo = campaignDao.selectCampaignInfo(campaignNo);
        // 승인 요청 중인 캠페인 인지를 우선 체크한다.
        // 스케쥴이 저장되었는지 우선 체크한다.
        if(StringUtil.isEmpty(campaignVo.getSendStartDt()) || StringUtil.isEmpty(campaignVo.getSendStartTm())) {
            return "notSchedule";
        }
        // 이미 다른 사람(또는 다른 브라우저)에서 상태가 변경된 경우
        if(approvalSts.equals(campaignVo.getApprovalSts())) {
            return "blockChange";
        } else {
            campaignVo.setCampaignNo(campaignNo);
            campaignVo.setApprovalSts(approvalSts);
            log.debug("updateApprovalStsInfo");

            campaignDao.updateApprovalStsInfo(campaignVo);
            return approvalSts;
        }

    }

    /**
     * 캠페인 스케쥴 정보를 등록한다. campaing_3step_**_form.jsp에서 dwr로 호출
     *
     * @param campaignNo
     * @param channelType
     * @param sendStartDate
     * @param startDate
     * @param endDate
     * @return
     */
    public int setRegistCampaignScheduleInfo(int campaignNo, String channelType, String sendStartDate, String startDate, String endDate, int divideCnt, int divideInterval,
        DivideSchedule[] divideScheduleList, String abTestCond, int abTestRate, String campaignSts) {
        CampaignVo campaignVo = campaignDao.selectCampaignInfo(campaignNo);
        if("C".equals(campaignVo.getCampaignSts()) && sendStartDate.equals("undefined undefined")) {
            return 300;
        }
        campaignVo.setCampaignNo(campaignNo);
        if(StringUtil.isBlank(campaignSts)) {
            campaignVo.setCampaignSts("P");
        } else {
            campaignVo.setCampaignSts(campaignSts);
        }

        sendStartDate = sendStartDate.replaceAll("-", "").replaceAll(":", "");

        // 발송 시작일
        campaignVo.setSendStartDt(sendStartDate.split(" ")[0]);
        campaignVo.setSendStartTm(sendStartDate.split(" ")[1]);

        // 발송대기인 경우
        if("M".equals(channelType)) {
            // 캠페인타입(수신확인 여부) 정보를 가져온다.
            String campaignType = campaignDao.selectCampaignType(campaignNo);

            if("Y".equals(campaignType)) {
                // 반응추적 시작일
                startDate = startDate.replaceAll("-", "").replaceAll(":", "");
                endDate = endDate.replaceAll("-", "").replaceAll(":", "");

                campaignVo.getTraceInfoVo().setStartDt(startDate.split(" ")[0]);
                campaignVo.getTraceInfoVo().setStartTm(startDate.split(" ")[1]);
                // 반응추적 종료일
                campaignVo.getTraceInfoVo().setEndDt(endDate.split(" ")[0]);
                campaignVo.getTraceInfoVo().setEndTm(endDate.split(" ")[1]);

                traceInfoDao.registTraceInfo(campaignVo);
            }
        }

        //A/B 테스트 발송 일 경우 동부생명 분할발송 테이블에 데이터 적재 필요
        if(divideScheduleList != null && divideScheduleList.length > 0) {

            // 등록되어있는 분할예약 스케쥴을 삭제하고 새로 등록함.
            divideScheduleDao.deleteDivideSchedule(campaignNo);

            for(int i = 0; i < divideScheduleList.length; i++) {
                //A/B 두번째 본 발송일 경우 첫번째 시간 값에서 입력받은 분을 더한 값으로 셋팅한다.
                if(i == 1) {
                    String secondTime = FormatUtil.addMinute(divideScheduleList[0].getStartDt(), divideInterval);
                    divideScheduleList[1].setStartDt(secondTime);
                }

                divideScheduleList[i].setClient("EM");
                divideScheduleDao.insertDivideSchedule(divideScheduleList[i]);
            }

            //분할발송 사용 여부 Y 사용
            campaignVo.setDivideYn("Y");
            //분할발송 횟수 2회
            campaignVo.setDivideCnt(divideCnt);
            //분할발송 간격 (단위:분)
            campaignVo.setDivideInterval(divideInterval);
            //A/B 테스트 조건
            campaignVo.setAbTestCond(abTestCond);
            //A/B 테스트 대상자 비율
            campaignVo.setAbTestRate(abTestRate);
        } else {
            campaignVo.setDivideYn("N");
            campaignVo.setDivideCnt(2);
            campaignVo.setDivideInterval(10);
        }

        return campaignDao.updateCampaignScheduleInfo(campaignVo);
    }

    /**
     * 캠페인 분할발송 스케쥴 정보를 등록한다. campaing_3step_**_form.jsp에서 dwr로 호출
     *
     * @param campaignNo
     * @param divideScheduleList
     * @param divideInterval
     * @param startDate
     * @param endDate
     * @param campaignSts
     * @return
     */
    public int setRegistCampaignDivideScheduleInfo(int campaignNo, DivideSchedule[] divideScheduleList, int divideInterval, String startDate, String endDate, String campaignSts, String campaighType) {
        CampaignVo campaignVo = campaignDao.selectCampaignInfo(campaignNo);
        if("C".equals(campaignVo.getCampaignSts())) {
            return 300;
        }
        campaignVo.setCampaignNo(campaignNo);
        if(StringUtil.isBlank(campaignSts)) {
            campaignVo.setCampaignSts("P");
        } else {
            campaignVo.setCampaignSts(campaignSts);
        }

        // 발송 시작일
        campaignVo.setSendStartDt((divideScheduleList[0].getStartDt()).substring(0, 8));
        campaignVo.setSendStartTm((divideScheduleList[0].getStartDt()).substring(8));

        // 분할발송 여부
        campaignVo.setDivideYn("Y");
        // 분할간격
        campaignVo.setDivideInterval(divideInterval);
        // 분할횟수
        campaignVo.setDivideCnt(divideScheduleList.length);

        // 캠페인타입(수신확인 여부) 정보를 가져온다.
        // [20210810][ljh][이메일만 반응추적 정보 저장]
        String campaignType = campaignDao.selectCampaignType(campaignNo);
        if("Y".equals(campaignType) && "M".equals(campaighType)) {
            // 반응추적 시작일
            startDate = startDate.replaceAll("-", "").replaceAll(":", "");
            endDate = endDate.replaceAll("-", "").replaceAll(":", "");

            campaignVo.getTraceInfoVo().setStartDt(startDate.split(" ")[0]);
            campaignVo.getTraceInfoVo().setStartTm(startDate.split(" ")[1]);
            // 반응추적 종료일
            campaignVo.getTraceInfoVo().setEndDt(endDate.split(" ")[0]);
            campaignVo.getTraceInfoVo().setEndTm(endDate.split(" ")[1]);

            traceInfoDao.registTraceInfo(campaignVo);
        }

        // 등록되어있는 분할예약 스케쥴을 삭제하고 새로 등록함.
        divideScheduleDao.deleteDivideSchedule(campaignNo);

        for(int i = 0; i < divideScheduleList.length; i++) {
            divideScheduleList[i].setClient("EM");
            divideScheduleDao.insertDivideSchedule(divideScheduleList[i]);
        }

        return campaignDao.updateCampaignScheduleInfo(campaignVo);
    }

    /**
     * 캠페인의 세그먼트 번호를 변경한다. campaing_2step_**_form.jsp에서 dwr로 호출 (테스트 발송 시)
     *
     * @param campaignNo
     * @param segmentNo
     */
    public void setTestCheck(int campaignNo, int segmentNo) {
        CampaignVo campaignVo = campaignDao.selectCampaignInfo(campaignNo);
        if(campaignVo.getSegmentNo() == 0) {
            campaignVo.setSegmentNo(segmentNo);
            campaignDao.updateCampaignSegmentNoInfo(campaignVo);
        }
    }

    /**
     * 테스트 발송이 되었는지 확인한다. campaing_2step_**_form.jsp에서 dwr로 호출
     *
     * @param campaignNo
     * @return
     */
    public boolean isCampaignTestSend(int campaignNo) {
        List<TestSendLog> list = testSendLogDao.selectCampaignTestSend(campaignNo);
        Date currentDate = new Date();
        boolean isTested = false;

        if(list.size() > 0) {
            TestSendLog testSendLog = list.get(0);
            Date sendDate;
            try {
                sendDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(testSendLog.getSendDt() + testSendLog.getSendTm());

                long elapseTime = (currentDate.getTime() - sendDate.getTime()) / (60 * 1000);

                log.info("[" + campaignNo + "] Elapse time: " + elapseTime + "분");

                if(elapseTime <= 10) {
                    isTested = true;
                } else {
                    isTested = false;
                }
            } catch(ParseException e) {
                log.error(null, e);
            }

        } else {
            isTested = false;
        }

        return isTested;
    }
}
