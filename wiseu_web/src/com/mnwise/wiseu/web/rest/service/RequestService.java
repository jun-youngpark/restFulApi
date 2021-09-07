package com.mnwise.wiseu.web.rest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.common.io.IOUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.BaseService;
import com.mnwise.wiseu.web.ecare.dao.RealtimeAcceptDao;
import com.mnwise.wiseu.web.rest.common.RestResponseCode;
import com.mnwise.wiseu.web.rest.model.NrealtimeRequestVo;
import com.mnwise.wiseu.web.rest.model.RequestVo;

@Service
public class RequestService extends BaseService {
    @Autowired private RealtimeAcceptDao realtimeAcceptDao;

    /* 서비스의 경우 Spring 컨테이너에 Bean객체가 이미 지정되어 올라가 유동적인 요청 Method에 대응할수 없다. 따라서 서비스에서 parser 부분은 분기 처리로 Method를 구분 했으며, operate는 MethodWorker로 분리하였다. */

    /**
     * 요청데이터 필수 정보 체크
     *
     * @param reqVo
     * @return
     */
    public boolean checkEssential(final RequestVo reqVo) {
        final NrealtimeRequestVo nReqVo = (NrealtimeRequestVo) reqVo;

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

    /**
     * 요청 Body를 얻어온다.
     *
     * @param req
     * @return String
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
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

    /**
     * json 형태 문자열을 RequestVo로 반환
     *
     * @param req
     * @return RequestVo
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws IOException
     */
    private RequestVo jsonParse(final HttpServletRequest req) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        return objectMapper.readValue(objectMapper.writeValueAsString(objectMapper.readValue(getBody(req), Map.class)), NrealtimeRequestVo.class);
    }

    /**
     * 요청에 대한 작업을 수행
     *
     * @param reqVo
     */
    public void operate(final RequestVo reqVo) {
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

    /**
     * 요청을 RequestVo로 반환
     *
     * @param req
     * @return
     */
    public RequestVo parser(final HttpServletRequest req) {
        RequestVo reqVo;

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

    /**
     * 요청 파라미터를 RequestVo로 변환
     *
     * @param req
     * @return RequestVo
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private RequestVo queryStringParse(final HttpServletRequest req) throws IllegalAccessException, InvocationTargetException {
        NrealtimeRequestVo reqVo = new NrealtimeRequestVo();
        BeanUtils.populate(reqVo, req.getParameterMap());

        return reqVo;
    }

    /**
     * 요청에 대해 응답 한다. 요청에 대한 응답은 요청 객체의 상태(State)를 사용한다.
     *
     * @param res
     * @param reqVo
     * @return
     */
    public ModelAndView response(final HttpServletResponse res, final RequestVo reqVo) {
        final NrealtimeRequestVo vo = (NrealtimeRequestVo) reqVo;

        res.setStatus(vo.getHttpCode());

        final ModelAndView mnv = new ModelAndView();
        mnv.setViewName("jsonView");
        mnv.addObject("code", vo.getStateCode());
        mnv.addObject("msg", vo.getState());

        return mnv;
    }

    /**
     * SELECT, DETELE, UPDATE SQL 결과가 null 또는 0 으로 정상적이지 않을 경우 실패 메시지 설정
     *
     * @param result
     * @param failMsg
     * @param vo
     */
    private void validateResult(final Object result, final String failMsg, final RequestVo vo) {
        // SELECT SQL을 실행하여 결과가 있다면 String 객체이고 없으면 null
        if(result instanceof String) {
            vo.setState(RestResponseCode.OK, RestResponseCode.SUCCESS, (String) result);
            return;
        }

        if(result == null || (result instanceof Integer && (Integer) result == 0)) {
            vo.setState(RestResponseCode.BAD, RestResponseCode.DATABASE_CRUD_ERROR, failMsg);
        }
    }
}