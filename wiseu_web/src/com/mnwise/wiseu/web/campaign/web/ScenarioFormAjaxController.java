package com.mnwise.wiseu.web.campaign.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.campaign.service.CampaignService;

/**
 * 캠페인 등록 2~3단계에서 호출되는 Ajax Controller
 * 기존 dwr 메소드를 대체 구현
 */
@Controller
public class ScenarioFormAjaxController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ScenarioFormAjaxController.class);

    @Autowired private CampaignService campaignService;

    /**
     * [캠페인>캠페인 등록>2단계] 저장 버튼 클릭 (메일, FAX)
     * [캠페인>캠페인 등록>3단계] 보류 버튼 클릭
     *
     * @param campaignNo
     * @param campaignSts
     * @return
     */
    @RequestMapping(value="/campaign/setCampaignStsInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setCampaignStsInfo(@RequestParam(defaultValue="0") int campaignNo, String campaignSts) {
        try {
            String value = campaignService.setCampaignStsInfo(campaignNo, campaignSts);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(value);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계] 다음단계 버튼 클릭 (메일, FAX)
     *
     * @param campaignNo
     * @return
     */
    @RequestMapping(value="/campaign/isCampaignTestSend.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto isCampaignTestSend(@RequestParam(defaultValue="0") int campaignNo) {
        try {
            boolean isTestSend = campaignService.isCampaignTestSend(campaignNo);
            return isTestSend ? new ResultDto(ResultDto.OK) : new ResultDto(ResultDto.FAIL);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계] 임시저장 버튼 클릭
     *
     * TODO 미사용 기능으로 추측
     *
     * @param scenarioNo
     * @param campaignNo
     * @param queryString
     * @return
     */
    @RequestMapping(value="/campaign/setRegistTempCampaign2StepInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setRegistTempCampaign2StepInfo(@RequestParam(defaultValue="0") int scenarioNo, @RequestParam(defaultValue="0") int campaignNo, String queryString) {
        try {
            campaignService.setRegistTempCampaign2StepInfo(scenarioNo, campaignNo, queryString);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계] 테스트 발송 버튼 클릭시 대상자가 선책된 경우 호출
     *
     * TODO 이 기능 제거해도 동작에 문제는 없어 보임
     *
     * @param campaignNo
     * @param segmentNo
     * @return
     */
    @RequestMapping(value="/campaign/setTestCheck.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setTestCheck(@RequestParam(defaultValue="0") int campaignNo, @RequestParam(defaultValue="0") int segmentNo) {
        try {
            campaignService.setTestCheck(campaignNo, segmentNo);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [캠페인>캠페인 등록>2단계] 승인 요청 버튼 클릭
     *
     * @param campaignNo
     * @param approvalSts
     * @return
     */
    @RequestMapping(value="/campaign/setApprovalStsInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setApprovalStsInfo(@RequestParam(defaultValue="0") int campaignNo, String approvalSts) {
        try {
            String value = campaignService.setApprovalStsInfo(campaignNo, approvalSts);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(value);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [캠페인>캠페인 등록>3단계] 저장/발송 버튼 클릭 - 발송 스케줄 정보 저장
     *
     * @param campaignNo
     * @param channelType
     * @param sendStartDate
     * @param startDate
     * @param endDate
     * @param divideCnt
     * @param divideInterval
     * @param divideScheduleList
     * @param abTestCond
     * @param abTestRate
     * @param campaignSts
     * @return
     */
    @RequestMapping(value="/campaign/setRegistCampaignScheduleInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setRegistCampaignScheduleInfo(@RequestBody Map<String, Object> paramMap) {
        try {
            int campaignNo = (int)paramMap.get("campaignNo");
            String channelType = (String)paramMap.get("channelType");
            String sendStartDate = (String)paramMap.get("sendStartDate");
            String startDate = (String)paramMap.get("startDate");
            String endDate = (String)paramMap.get("endDate");
            int divideCnt = (int)paramMap.get("divideCnt");
            int divideInterval = (int)paramMap.get("divideInterval");
            String abTestCond = (String)paramMap.get("abTestCond");
            int abTestRate = (int)paramMap.get("abTestRate");
            String campaignSts = (String)paramMap.get("campaignSts");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> divideScheduleList = (List<Map<String, Object>>) paramMap.get("divideScheduleList");
            int listSize = (divideScheduleList == null) ? 0 : divideScheduleList.size();
            DivideSchedule[] divideScheduleArray = new DivideSchedule[listSize];
            for(int i = 0; i < listSize; i++) {
                Map<String, Object> propMap = divideScheduleList.get(i);
                divideScheduleArray[i] = new DivideSchedule();
                BeanUtil.populate(divideScheduleArray[i], propMap);
            }

            int value = campaignService.setRegistCampaignScheduleInfo(campaignNo, channelType, sendStartDate, startDate, endDate, divideCnt, divideInterval, divideScheduleArray, abTestCond, abTestRate, campaignSts);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(String.valueOf(value));
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [캠페인>캠페인 등록>3단계] 분할 버튼 클릭
     *
     * @param campaignNo
     * @param divideScheduleList
     * @param divideInterval
     * @param startDate
     * @param endDate
     * @param campaignSts
     * @return
     */
    @RequestMapping(value="/campaign/setRegistCampaignDivideScheduleInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setRegistCampaignDivideScheduleInfo(@RequestBody Map<String, Object> paramMap) {
        try {
            int campaignNo = (int)paramMap.get("campaignNo");
            int divideInterval = (int)paramMap.get("divideInterval");
            String startDate = (String)paramMap.get("startDate");
            String endDate = (String)paramMap.get("endDate");
            String campaignSts = (String)paramMap.get("campaignSts");
            String campaighType = (String)paramMap.get("campaighType");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> divideScheduleList = (List<Map<String, Object>>) paramMap.get("divideScheduleList");
            int listSize = (divideScheduleList == null) ? 0 : divideScheduleList.size();
            DivideSchedule[] divideScheduleArray = new DivideSchedule[listSize];
            for(int i = 0; i < listSize; i++) {
                Map<String, Object> propMap = divideScheduleList.get(i);
                divideScheduleArray[i] = new DivideSchedule();
                BeanUtil.populate(divideScheduleArray[i], propMap);
            }

            int value = campaignService.setRegistCampaignDivideScheduleInfo(campaignNo, divideScheduleArray, divideInterval, startDate, endDate, campaignSts, campaighType);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(String.valueOf(value));
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
