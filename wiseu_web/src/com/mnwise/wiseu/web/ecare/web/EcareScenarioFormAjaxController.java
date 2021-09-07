package com.mnwise.wiseu.web.ecare.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.BeanUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.ResultDto;
import com.mnwise.wiseu.web.base.WiseuLocaleChangeInterceptor;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.common.model.TargetQueryInfo;
import com.mnwise.wiseu.web.common.service.MailPreviewService;
import com.mnwise.wiseu.web.common.ui.upload.SendInfo;
import com.mnwise.wiseu.web.ecare.service.EcareScenarioService;
import com.mnwise.wiseu.web.ecare.service.EcareService;
import com.mnwise.wiseu.web.segment.service.SegmentService;

/**
 * 이케어 등록 2~3단계에서 호출되는 Ajax Controller
 * 기존 dwr 메소드를 대체 구현
 */
@Controller
public class EcareScenarioFormAjaxController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EcareScenarioFormAjaxController.class);

    @Autowired private EcareService ecareService;
    @Autowired private EcareScenarioService ecareScenarioService;
    @Autowired private MailPreviewService mailPreviewService;
    @Autowired private SegmentService segmentService;
    @Autowired private MessageSourceAccessor messageSourceAccessor;
    private static WiseuLocaleChangeInterceptor localeChangeInterceptor;

    @Autowired
    public EcareScenarioFormAjaxController(WiseuLocaleChangeInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
    }



    /**
     * [이케어>이케어 등록>1단계] NVECAREMSG 테이블에서 주어진 서비스ID로 등록된 이케어수를 가져온다.
     *
     * @param svcId 서비스ID
     * @return
     */
    @RequestMapping(value="/ecare/selectServiceCount.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto checkServiceID(String svcId) {
        try {
            int count = ecareService.checkServiceID(svcId);  // 등록된 이케어수
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(count + "");
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
    /**
     * [이케어>이케어 등록>2단계] NVECAREMSG 테이블에서 주어진 서비스ID로 등록된 이케어수를 가져온다.
     *
     * @param svcId 서비스ID
     * @return
     */
    @RequestMapping(value="/ecare/checkTargetQuery.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto checkTargetQuery(int segmentNo) {
        try {
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            TargetQueryInfo target = segmentService.selectTargetQueryInfo(segmentNo);
            if(target== null) {
                return new ResultDto(ResultDto.FAIL, "segment No check");
            }

            if(StringUtils.isBlank(target.getSqlHead())) {
                return new ResultDto(ResultDto.FAIL, messageSourceAccessor.getMessage("segment.valid", localeChangeInterceptor.getLocale()));
            }
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [이케어>이케어 등록>2단계] NVECAREMSG 테이블에서 주어진 이케어 번호의 세그먼트 번호의 쿼리로 변경
     *
     * @param ecareNo 이케어 번호
     * @param segmentNo 세그먼트 번호
     * @return
     */
    @RequestMapping(value="/ecare/updateTargetChgToOrg.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto updateSegmentNo(@RequestParam(defaultValue="0") int ecareNo, @RequestParam(defaultValue="0") int segmentNo) {
        try {
            ecareService.updateTargetChgToOrg(ecareNo, segmentNo);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [이케어>이케어 등록>2~3단계] NVECAREMSG 테이블에서 주어진 이케어 번호의 이케어 상태를 변경
     *
     * @param ecareNo 이케어 번호
     * @param ecareSts 이케어 상태
     * @return
     */
    @RequestMapping(value="/ecare/setEcareStsInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setEcareStsInfo(@RequestParam(defaultValue="0") int ecareNo, String ecareSts) {
        try {
            String value = ecareService.setEcareStsInfo(ecareNo, ecareSts);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(value);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [이케어>이케어 등록>3단계] NVECAREMSG 테이블에서 주어진 이케어 번호의 검증여부 정보를 변경
     *
     * @param ecareNo 이케어 번호
     * @param verifyYn 검증여부
     * @param verifyGrpCd 검증부서코드
     * @return
     */
    @RequestMapping(value="/ecare/setEcareVerifyInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setEcareVerifyInfo(int ecareNo, String verifyYn, String verifyGrpCd) {
        try {
            String value = ecareService.setEcareVerifyInfo(ecareNo, verifyYn, verifyGrpCd);
            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(value);
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [이케어>이케어 등록>3단계] 스케줄 정보 저장
     *
     * @param scenarioNo
     * @param scheduleNo
     * @param queryString
     * @param serviceType
     * @return
     */
    @RequestMapping(value="/ecare/setRegistEcareScheduleInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setRegistEcareScheduleInfo(int scenarioNo, int scheduleNo, String queryString, String serviceType) {
        try {
            ecareService.setRegistEcareScheduleInfo(scenarioNo, scheduleNo, StringUtil.unescapeXss(queryString), serviceType);
            return new ResultDto(ResultDto.OK);
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }

    /**
     * [이케어>이케어 등록>3단계] LTS에 주어진 이케어 번호의 이케어를 발송요청 - TCP/IP 실시간발송 기능으로 추측됨
     *
     * @param ecareNo
     * @return
     */
    @RequestMapping(value="/ecare/requestSendToLts.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public String requestSendToLts(int ecareNo) throws Exception {
        try {
            return mailPreviewService.sendMailRequestAll(ecareNo);
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }

    /**
     * [이케어>이케어 등록>3단계] MOKA에서 사용하는 기능으로 프로그래스바에 진행상태를 출력
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/ecare/getSendInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public SendInfo getSendInfo(HttpServletRequest request) throws Exception {
        try {
            if(request.getSession().getAttribute("sendInfo") != null) {
                return (SendInfo) request.getSession().getAttribute("sendInfo");
            }

            return new SendInfo();
        } catch (Exception e) {
            log.error(null, e);
            throw e;
        }
    }
    
    /**
     * [이케어>이케어 등록>3단계] 분할 버튼 클릭
     *
     * @param ecareNo
     * @param divideScheduleList
     * @param divideInterval
     * @param startDate
     * @param endDate
     * @param ecareSts
     * @return
     */
    @RequestMapping(value="/ecare/setRegistEcareDivideScheduleInfo.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public ResultDto setRegistEcareDivideScheduleInfo(@RequestBody Map<String, Object> paramMap) {
        try {
            int ecareNo = (int)paramMap.get("ecareNo");
            int divideInterval = (int)paramMap.get("divideInterval");
            String startDate = (String)paramMap.get("startDate");
            String endDate = (String)paramMap.get("endDate");
            String ecareSts = (String)paramMap.get("ecareSts");
            String channelType = (String)paramMap.get("channelType");
            String cycleCd = (String)paramMap.get("cycleCd");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> divideScheduleList = (List<Map<String, Object>>) paramMap.get("divideScheduleList");
            int listSize = (divideScheduleList == null) ? 0 : divideScheduleList.size();
            DivideSchedule[] divideScheduleArray = new DivideSchedule[listSize];
            for(int i = 0; i < listSize; i++) {
                Map<String, Object> propMap = divideScheduleList.get(i);
                divideScheduleArray[i] = new DivideSchedule();
                BeanUtil.populate(divideScheduleArray[i], propMap);
            }

            int value = ecareService.setRegistEcareDivideScheduleInfo(ecareNo, divideScheduleArray, divideInterval, startDate, endDate, ecareSts, channelType, cycleCd);

            ResultDto resultDto = new ResultDto(ResultDto.OK);
            resultDto.setValue(String.valueOf(value));
            return resultDto;
        } catch(Exception e) {
            log.error(null, e);
            return new ResultDto(ResultDto.FAIL, e.getMessage());
        }
    }
}
