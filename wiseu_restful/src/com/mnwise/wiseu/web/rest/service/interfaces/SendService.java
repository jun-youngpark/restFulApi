package com.mnwise.wiseu.web.rest.service.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.common.util.DateUtil;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.common.FileUtil;
import com.mnwise.wiseu.web.rest.dao.interfaces.SendDao;
import com.mnwise.wiseu.web.rest.dto.DataMap;
import com.mnwise.wiseu.web.rest.model.custom.CustomBatch;
import com.mnwise.wiseu.web.rest.model.custom.CustomRealtimeaccept;
import com.mnwise.wiseu.web.rest.model.custom.CustomResend;
import com.mnwise.wiseu.web.rest.model.ecare.BatchRequest;
import com.mnwise.wiseu.web.rest.parent.BaseService;

import org.json.simple.JSONObject;

@Service
public class SendService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(SendService.class);
	interface BATCHDATA{
		int batchSeq = 0;
		int ecareNo = 1;
		int svcId = 2;
		int channel = 3;
		int tmplType = 4;
		int jonmun = 5;
		int reservedDate = 6;
		int reqUserId = 7;
		int reqDeptId = 8;
		int receiverId = 9;
		int receiverNm = 10;
		int receiver = 11;
		int senderNm = 12;
		int sender = 13;
		int subject = 14;
		int secuKey = 15;
		int slot1 = 16;
		int slot2 = 17;
		int slot3 = 18;
		int slot4 = 19;
		int slot5 = 20;
		int slot6 = 21;
		int slot7 = 22;
		int slot8 = 23;
		int slot9 = 24;
		int filePathList = 25;
	}
	@Autowired private SendDao sendDao;

	/**
	 * 준실시간 발송
	 */
    public JSONObject insertSingleRealtime(CustomRealtimeaccept realtimeacceptVo) throws Exception{

    	realtimeacceptVo.setSendFg(Const.Flag.READY);
    	sendDao.insertRealtime(realtimeacceptVo);
    	return new DataMap()
    			  .put("seq", realtimeacceptVo.getSeq())
    			  .build();

    }

    /**
     * 스케줄 발송 (다건 발송)
     * @param batchDto
     * @throws IOException
     */
    @Async
	public JSONObject insertScheduleBatch(BatchRequest batchReqDto) throws Exception {
		// 공통부 처리
		//sendDao.insertBatchRequest(batchReqDto);
		// 개별부 처리
		List<String> dataList = FileUtil.readSamFile(batchReqDto.getFilePath());
		CustomBatch batchVo = null;
		String batchSeq = batchReqDto.getBatchSeq();
		int insertCnt = 0;
		for (String data : dataList) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
			batchVo = objectMapper.readValue(objectMapper.writeValueAsString(objectMapper.readValue(data, Map.class)),
					CustomBatch.class);
			batchVo.setBatchSeq(batchSeq);
			System.out.println(batchVo.toString());
			insertCnt += sendDao.insertBatch(batchVo);
		}
		log.info("batchSeq : {} insert completed", batchReqDto.getBatchSeq());
		return new DataMap().put("batchSeq",batchReqDto.getBatchSeq())
				  .build();

	}
    /**
     * 인터페이스 재발송
     * @param batchDto
     * @throws IOException
     */
    @Async
	public JSONObject insertResend(CustomResend resendDto) throws Exception {
    	String subType = sendDao.selectSubType(resendDto);
        String now = DateUtil.getNowDateTime("yyyyMMddHHmmssSSSSS");
    	if("S".equals(subType)) {
    		resendDto.setNewSeq("RESENDB"+now);
    		sendDao.insertBatchResend(resendDto);
    	}else {
    		resendDto.setNewSeq("RESENDR"+now);
    		sendDao.insertRealtimeResend(resendDto);
    	}
		return new DataMap().put("seq",resendDto.getNewSeq())
		  .build();
	}
	/*
    //@Autowired private BaseDao baseDao;

     서비스의 경우 Spring 컨테이너에 Bean객체가 이미 지정되어 올라가 유동적인 요청 Method에 대응할수 없다. 따라서 서비스에서 parser 부분은 분기 처리로 Method를 구분 했으며, operate는 MethodWorker로 분리하였다.

    *//**
     * 요청데이터 필수 정보 체크
     *
     * @param reqVo
     * @return
     *//*
    public boolean checkEssential(final BaseVo reqVo) {

        if("POST".equals(reqVo.getRequestMethod())) {
            if(StringUtil.isBlank(nReqVo.getEcareNo()) || StringUtil.isBlank(nReqVo.getChannel()) || StringUtil.isBlank(nReqVo.getTmplType())
                || StringUtil.isBlank(nReqVo.getPreviewType()) || StringUtil.isBlank(nReqVo.getSeq()) || StringUtil.isBlank(nReqVo.getReceiverId())
                || StringUtil.isBlank(nReqVo.getReceiverNm()) || StringUtil.isBlank(nReqVo.getReceiver()) || StringUtil.isBlank(nReqVo.getSendNm())
                || StringUtil.isBlank(nReqVo.getSender())) {
                return false;
            }
        } else {
            if(StringUtil.isBlank(nReqVo.getSeq())) {
                return false;
            }
        }

        return true;
    }

    *//**
     * 요청 Body를 얻어온다.
     *
     * @param req
     * @return String
     * @throws IOException
     * @throws UnsupportedEncodingException
     *//*
    private String getBody(final HttpServletRequest req) throws IOException {
        String body = "";
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));

            while((line = in.readLine()) != null) {
                body += line;
            }
        } catch(IOException e) {
            throw e;
        } finally {
            IOUtil.closeQuietly(in);
        }

        return body;
    }

    *//**
     * json 형태 문자열을 RequestVo로 반환
     *
     * @param req
     * @return RequestVo
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws IOException
     *//*
    private BaseVo jsonParse(final HttpServletRequest req) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        return objectMapper.readValue(objectMapper.writeValueAsString(objectMapper.readValue(getBody(req), Map.class)), NrealtimeRequestVo.class);
    }

    *//**
     * 요청에 대한 작업을 수행
     *
     * @param reqVo
     *//*
    public void operate(final BaseVo reqVo) {
        // 에러유무 체크
        if(reqVo.isError()) {
            return;
        } else if(!checkEssential(reqVo)) {
            reqVo.setState(RestResponseCode.BAD, RestResponseCode.ESSENTIAL_DATA_ERROR, "ESSENTIAL_DATA_ERROR");
            return;
        }

        final String method = reqVo.getRequestMethod();

        try {
            if("POST".equals(method)) {
                realtimeAcceptDao.create(reqVo);
            } else if("GET".equals(method)) {
                validateResult(realtimeAcceptDao.read(reqVo), "No Data to select", reqVo);
            } else if("PUT".equals(method)) {
                validateResult(realtimeAcceptDao.update(reqVo), "No Data to update", reqVo);
            } else if("DELETE".equals(method)) {
                validateResult(realtimeAcceptDao.delete(reqVo), "Cannot delete data send to customer", reqVo);
            }
        } catch(Exception e) {
            reqVo.setState(RestResponseCode.BAD, RestResponseCode.DATABASE_CRUD_ERROR, e.getCause().getMessage());
        }
    }

    *//**
     * 요청을 RequestVo로 반환
     *
     * @param req
     * @return
     *//*
    public BaseVo parser(final HttpServletRequest req) {
        BaseVo reqVo;

        try {
            // POST를 제외한 METHOD의 파라메터는 queryString 으로 전달 받는다.
            if("POST".equals(req.getMethod())) {
                reqVo = jsonParse(req);
            } else {
                reqVo = queryStringParse(req);
            }
        } catch(Exception e) {
            reqVo = new NrealtimeRequestVo();
            reqVo.setState(RestResponseCode.BAD, RestResponseCode.DATA_PARSING_ERROR, e.getMessage());
        }

        // 요청에대한 기본정보 셋팅
        reqVo.setRequestInfo(req);

        return reqVo;
    }

    *//**
     * 요청 파라미터를 RequestVo로 변환
     *
     * @param req
     * @return RequestVo
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     *//*
    private BaseVo queryStringParse(final HttpServletRequest req) throws IllegalAccessException, InvocationTargetException {
        NrealtimeRequestVo reqVo = new NrealtimeRequestVo();
        BeanUtils.populate(reqVo, req.getParameterMap());

        return reqVo;
    }

    *//**
     * 요청에 대해 응답 한다. 요청에 대한 응답은 요청 객체의 상태(State)를 사용한다.
     *
     * @param res
     * @param reqVo
     * @return
     *//*
    public ModelAndView response(final HttpServletResponse res, final BaseVo reqVo) {
        final NrealtimeRequestVo vo = (NrealtimeRequestVo) reqVo;

        res.setStatus(vo.getHttpCode());

        final ModelAndView mnv = new ModelAndView();
        mnv.setViewName("jsonView");
        mnv.addObject("code", vo.getStateCode());
        mnv.addObject("msg", vo.getState());

        return mnv;
    }

    *//**
     * SELECT, DETELE, UPDATE SQL 결과가 null 또는 0 으로 정상적이지 않을 경우 실패 메시지 설정
     *
     * @param result
     * @param failMsg
     * @param vo
     *//*
    private void validateResult(final Object result, final String failMsg, final BaseVo vo) {
        // SELECT SQL을 실행하여 결과가 있다면 String 객체이고 없으면 null
        if(result instanceof String) {
            vo.setState(RestResponseCode.OK, RestResponseCode.SUCCESS, (String) result);
            return;
        }

        if(result == null || (result instanceof Integer && (Integer) result == 0)) {
            vo.setState(RestResponseCode.BAD, RestResponseCode.DATABASE_CRUD_ERROR, failMsg);
        }
    }*/
}