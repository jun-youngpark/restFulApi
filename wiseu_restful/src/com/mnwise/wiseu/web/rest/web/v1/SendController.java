package com.mnwise.wiseu.web.rest.web.v1;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.model.custom.CustomBatch;
import com.mnwise.wiseu.web.rest.model.custom.CustomRealtimeaccept;
import com.mnwise.wiseu.web.rest.model.custom.CustomResend;
import com.mnwise.wiseu.web.rest.model.ecare.BatchRequest;
import com.mnwise.wiseu.web.rest.parent.BaseController;
import com.mnwise.wiseu.web.rest.service.interfaces.SendService;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

@RestController
@RequestMapping("/api/v1/send/")
@Slf4j
public class SendController extends BaseController {

    @Autowired private SendService sendService;


	/**
	 * 준실시간 발송 인터페이스
	 */
    @RequestMapping(value = "/realtime" , method={RequestMethod.POST} )
    public ResponseEntity<?> SendSingleNrealtime(@RequestBody @Valid final CustomRealtimeaccept realTimeDto) {
    	JSONObject data = null;
    	try{
	    	sendService.insertSingleRealtime(realTimeDto);

			return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e){
    		log.error("에러내용:{} 데이터:{}", e, realTimeDto.toStringJson());
			return new ResponseEntity<>(getFailResult(e.getMessage(),data),HttpStatus.INTERNAL_SERVER_ERROR);    
		}
    }

    
    /**
	 * 스케줄 발송 인터페이스
	 */
    @RequestMapping(value = "/batch" , method={RequestMethod.GET,RequestMethod.POST} )
    public ResponseEntity<?> sendScheduleNvbatch(@RequestBody @Valid final BatchRequest batchReqDto) {
    	JSONObject data = null;
    	try{
    		long fileRow = Files.lines(Paths.get(batchReqDto.getFilePath()),Charset.forName("euc_kr")).count();
    		log.info("DATA CNT : {}", batchReqDto.getDatCnt());
    		log.info("FILE ROW CNT : {}", fileRow);
    		if(batchReqDto.getDatCnt()  == fileRow){
    			data = sendService.insertScheduleBatch(batchReqDto);
				return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    		}else{
    			return new ResponseEntity<>(getFailResult("CNT와 실제 파일 로우 수가 같지 않습니다", data),HttpStatus.INTERNAL_SERVER_ERROR);    
    		}
    	}catch(Exception e){
    		log.error("에러내용:{} 데이터:{}", e.getStackTrace(), batchReqDto.toStringJson());
			return new ResponseEntity<>(getFailResult(e.getMessage(),data),HttpStatus.INTERNAL_SERVER_ERROR);    
		}
    }
	/**
	 * 재발송
	 */
    @RequestMapping(value = "/resend" , method={RequestMethod.POST} )
    public ResponseEntity<?> resend(@RequestBody @Valid final CustomResend resendDto) {
    	JSONObject data = null;
    	try{
			data = sendService.insertResend(resendDto);

			return new ResponseEntity<>(getSuccessResult(data),HttpStatus.OK);
    	}catch(Exception e){
    		log.error("에러내용:{} 데이터:{}", e, resendDto.toStringJson());
			return new ResponseEntity<>(getFailResult(e.getMessage(),data),HttpStatus.INTERNAL_SERVER_ERROR);    
		}
    }
}
