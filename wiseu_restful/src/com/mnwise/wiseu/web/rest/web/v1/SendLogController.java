package com.mnwise.wiseu.web.rest.web.v1;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.common.RestApiUtils;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.exception.RestException;
import com.mnwise.wiseu.web.rest.model.Groups;
import com.mnwise.wiseu.web.rest.model.custom.CustomSendlLog;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignLog;
import com.mnwise.wiseu.web.rest.model.ecare.CampaignSingleLog;
import com.mnwise.wiseu.web.rest.model.ecare.EcareLog;
import com.mnwise.wiseu.web.rest.model.ecare.TableSrch;
import com.mnwise.wiseu.web.rest.parent.BaseController;
import com.mnwise.wiseu.web.rest.service.CommonService;
import com.mnwise.wiseu.web.rest.service.SendLogService;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

@RestController
@RequestMapping("/api/v1/send/")
@Slf4j
public class SendLogController extends BaseController {

    @Autowired private SendLogService sendLogService;
    @Autowired private CommonService commonSerivce;

    @Value("${api.v1.send.tableSrch}")
    private String useTableSrch;

    @Value("${api.v1.send.campaignlog}")
    private String useCampaignLog;

    @Value("${api.v1.send.campaignSinglelog}")
    private String useCampaignSingleLog;

    @Value("${api.v1.send.ecarelog}")
    private String useEcareLog;

    @Value("${api.v1.send.ecareSinglelog}")
    private String useEcareSingleLog;

	/**
	 * 발송 결과 인터페이스
	 */
    @RequestMapping(value = "/sendLog.do" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> getSendLog(@RequestBody @Valid final CustomSendlLog sendlLogDto) {
    	JSONObject data = null;
    	try{
    		List<CustomSendlLog> result = sendLogService.selectSendlog(sendlLogDto);

	    	data = new DataMap<String, Object>().put("reqSeq",sendlLogDto.getReqSeq())
	    			.put("seq",sendlLogDto.getSeq())
	    			.build();
	    	return ResponseEntity.ok().body(getSuccessResult(data));
    	}catch(Exception e){
    		log.error("에러내용:{} 데이터:{}", e, sendlLogDto.toStringJson());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getFailResult(e.getMessage(), data));
    	}

    }

    /**
	 * 요청 테이블 조회(상태/결과)
	 */
    @RequestMapping(value = "/tableSrch" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> tableSrch(@RequestBody @Validated final TableSrch tableSrch) {

    	if (useTableSrch.equalsIgnoreCase(Const.OnOff.ON)) {

    		String logSeq = UUID.randomUUID().toString();
    		String interfaceId = RestApiUtils.getUri();
    		String funcNm = "요청 테이블 조회";

    		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + "접수", "", "");

    		JSONObject data	= new JSONObject();
        	try {

        		data = sendLogService.selectTblSrch(tableSrch, interfaceId ,logSeq, funcNm);
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 정상종료", "", "");
        		return new ResponseEntity<>(getSuccessResultJson(data),HttpStatus.OK);

        	}catch(RestException e1) {

        		log.info("예외:{} 데이터:{}", e1, tableSrch.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, e1.getMessage(), "", "");
        		return new ResponseEntity<>(getRestExceptionResult(e1.getMessage(), data),HttpStatus.OK);

        	}catch(Exception e) {

        		log.error("에러내용:{} 데이터:{}", e, tableSrch.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.EXCEPTION, interfaceId, funcNm +" 처리 중 예외가 발생하였습니다.", tableSrch.toString(), RestApiUtils.getError(e));
        		return new ResponseEntity<>(getExceptionResult(funcNm, data),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
    	} else {

    		return new ResponseEntity<>(getNoUseResult(),HttpStatus.OK);
    	}
    }

    /**
	 * 캠페인 발송결과 리스트 조회
	 */
    @RequestMapping(value = "/campaignLog" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> campaignLog(@RequestBody @Validated(Groups.list.class) final CampaignLog campaignLog) {

    	if (useCampaignLog.equalsIgnoreCase(Const.OnOff.ON)) {

    		String logSeq = UUID.randomUUID().toString();
    		String interfaceId = RestApiUtils.getUri();
    		String funcNm = "캠페인 발송결과 리스트 조회";

    		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 접수", "", "");

    		JSONObject data	= new JSONObject();
        	try {

        		data = sendLogService.selectCampaignLog(campaignLog, interfaceId ,logSeq, funcNm);

        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 정상종료", "", "");
        		return new ResponseEntity<>(getSuccessResultJson(data),HttpStatus.OK);

        	}catch(RestException e1) {

        		log.info("예외:{} 데이터:{}", e1, campaignLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, e1.getMessage(), "", "");
        		return new ResponseEntity<>(getRestExceptionResult(e1.getMessage(), data),HttpStatus.OK);

        	}catch(Exception e) {

        		log.error("에러내용:{} 데이터:{}", e, campaignLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.EXCEPTION, interfaceId, funcNm +" 처리 중 예외가 발생하였습니다.", campaignLog.toString(), RestApiUtils.getError(e));
        		return new ResponseEntity<>(getExceptionResult(funcNm, data),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
    	} else {

    		return new ResponseEntity<>(getNoUseResult(),HttpStatus.OK);
    	}
    }

    /**
	 * 캠페인 발송 결과 리스트 단건 조회
	 */
    @RequestMapping(value = "/campaignSingleLog" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> campaignSingleLog(@RequestBody final CampaignSingleLog campaignSingleLog) {

    	if (useCampaignSingleLog.equalsIgnoreCase(Const.OnOff.ON)) {

    		String logSeq = UUID.randomUUID().toString();
    		String interfaceId = RestApiUtils.getUri();
    		String funcNm = "캠페인 발송 결과 리스트 단건 조회";

    		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 접수", "", "");

    		JSONObject data	= new JSONObject();
        	try {

        		data = sendLogService.selectCampaignSingleLog(campaignSingleLog);

        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 정상종료", "", "");
        		return new ResponseEntity<>(getSuccessResultJson(data),HttpStatus.OK);

        	}catch(RestException e1) {

        		log.info("예외:{} 데이터:{}", e1, campaignSingleLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, e1.getMessage(), "", "");
        		return new ResponseEntity<>(getRestExceptionResult(e1.getMessage(), data),HttpStatus.OK);

        	}catch(Exception e) {

        		log.error("에러내용:{} 데이터:{}", e, campaignSingleLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.EXCEPTION, interfaceId, funcNm +" 처리 중 예외가 발생하였습니다.", campaignSingleLog.toString(), RestApiUtils.getError(e));
        		return new ResponseEntity<>(getExceptionResult(funcNm, data),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
    	} else {

    		return new ResponseEntity<>(getNoUseResult(),HttpStatus.OK);
    	}
    }

    /**
	 * 이케어 발송결과 리스트 조회
	 */
    @RequestMapping(value = "/ecarelog" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> ecareLog(@RequestBody @Validated(Groups.list.class) final EcareLog ecareLog) {

    	if (useEcareLog.equalsIgnoreCase(Const.OnOff.ON)) {

    		String logSeq = UUID.randomUUID().toString();
    		String interfaceId = RestApiUtils.getUri();
    		String funcNm = "이케어 발송결과 리스트 조회";

    		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 접수", "", "");

    		JSONObject data	= new JSONObject();
        	try {

        		data = sendLogService.selectEcareLog(ecareLog, interfaceId ,logSeq, funcNm);

        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 정상종료", "", "");
        		return new ResponseEntity<>(getSuccessResultJson(data),HttpStatus.OK);

        	}catch(RestException e1) {

        		log.info("예외:{} 데이터:{}", e1, ecareLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, e1.getMessage(), "", "");
        		return new ResponseEntity<>(getRestExceptionResult(e1.getMessage(), data),HttpStatus.OK);

        	}catch(Exception e) {

        		log.error("에러내용:{} 데이터:{}", e, ecareLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.EXCEPTION, interfaceId, funcNm +" 처리 중 예외가 발생하였습니다.", ecareLog.toString(), RestApiUtils.getError(e));
        		return new ResponseEntity<>(getExceptionResult(funcNm, data),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
    	} else {

    		return new ResponseEntity<>(getNoUseResult(),HttpStatus.OK);
    	}
    }

    /**
	 * 이케어 발송 결과 리스트 단건 조회
	 */
    @RequestMapping(value = "/ecareSinglelog" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> ecareSingleLog(@RequestBody final EcareLog ecareLog) {

    	if (useEcareSingleLog.equalsIgnoreCase(Const.OnOff.ON)) {

    		String logSeq = UUID.randomUUID().toString();
    		String interfaceId = RestApiUtils.getUri();
    		String funcNm = "이케어 발송 결과 리스트 단건 조회";

    		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 접수", "", "");

    		JSONObject data	= new JSONObject();
        	try {

        		data = sendLogService.selectEcareSingleLog(ecareLog);

        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, funcNm + " 정상종료", "", "");
        		return new ResponseEntity<>(getSuccessResultJson(data),HttpStatus.OK);

        	}catch(RestException e1) {

        		log.info("예외:{} 데이터:{}", e1, ecareLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.LOG, interfaceId, e1.getMessage(), "", "");
        		return new ResponseEntity<>(getRestExceptionResult(e1.getMessage(), data),HttpStatus.OK);

        	}catch(Exception e) {

        		log.error("에러내용:{} 데이터:{}", e, ecareLog.toStringJson());
        		commonSerivce.regApiLog(logSeq, Const.ApiLogType.EXCEPTION, interfaceId, funcNm +" 처리 중 예외가 발생하였습니다.", ecareLog.toString(), RestApiUtils.getError(e));
        		return new ResponseEntity<>(getExceptionResult(funcNm, data),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
    	} else {

    		return new ResponseEntity<>(getNoUseResult(),HttpStatus.OK);
    	}
    }
}
