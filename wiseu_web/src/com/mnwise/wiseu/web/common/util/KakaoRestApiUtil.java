package com.mnwise.wiseu.web.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.mnwise.common.util.JsonUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.template.model.BrandtalkSingleResponse;
import com.mnwise.wiseu.web.template.model.KakaoResponse;
import com.mnwise.wiseu.web.template.model.KakaoSingleResponse;

public class KakaoRestApiUtil {
    private static final Logger log = LoggerFactory.getLogger(KakaoRestApiUtil.class);

    public static final String SUCCESS_CODE = "200";
    public static final String FAIL_CODE = "999";

    /**
     * 알림톡 REST API 서버에 URL을 요청해서 JSON 형태 결과를 받아온다.
     * POST 방식만 지원
     * FILE 전송 추가 multipartFileList
     * @param url
     * @param params
     * @param multipartFileList
     * @return
     */
    public static final String callRestApi(final String url, final List<NameValuePair> params, List<MultipartFile> multipartFileList) {
        try {
            return HttpUtils.sendPostRequest(url, params, multipartFileList);
        } catch(ClientProtocolException e) {
            log.error("while sending http request, client protocol exception occurred. " + e.getMessage());
        } catch(IOException e) {
            log.error("while sending http request, io exception occurred. " + e.getMessage());
        }

        return "";
    }
    /**
     * 알림톡 REST API 서버에 URL을 요청해서 JSON 형태 결과를 받아온다.
     * POST 방식만 지원
     * @param url
     * @param params
     * @return
     */
    public static final String callRestApi(final String url, final List<NameValuePair> params) {
        try {
            return HttpUtils.sendPostRequest(url, params);
        } catch(ClientProtocolException e) {
            log.error("while sending http request, client protocol exception occurred. " + e.getMessage());
        } catch(IOException e) {
            log.error("while sending http request, io exception occurred. " + e.getMessage());
        }

        return "";
    }
    /**
     * REST API 서버에 URL을 요청해서 JSON 형태 결과를 받아온다.
     * GET 방식만 지원
     *
     * @param url
     * @param
     * @return
     */
    public static final String callRestApi(final String url) {
        try {
            return HttpUtils.sendGetRequest(url);
        } catch(ClientProtocolException e) {
            log.error("while sending http request, client protocol exception occurred. " + e.getMessage());
        } catch(IOException e) {
            log.error("while sending http request, io exception occurred. " + e.getMessage());
        }

        return "";
    }

    /**
     * 친구톡 REST API 서버에 URL을 요청해서 JSON 형태 결과를 받아온다.
     * POST 방식만 지원
     * @param url
     * @param params
     * @return
     */
    public static final String callRestApi(final String url, final String jsonData) {
        try {
            return HttpUtils.sendPostRequest(url, jsonData);
        } catch(ClientProtocolException e) {
            log.error("while sending http request, client protocol exception occurred. " + e.getMessage());
        } catch(IOException e) {
            log.error("while sending http request, io exception occurred. " + e.getMessage());
        }

        return "";
    }
    /**
     * 친구톡 REST API 서버에 URL을 요청해서 JSON 형태 결과를 받아온다.
     *
     * @param url
     * @param params
     * @return
     */
    public static final String callRestFileSendApi(final String url, final List<NameValuePair> params, Map<String,MultipartFile> multipartFile) {
        try {
            return HttpUtils.sendPostandFileRequest(url, params, multipartFile);
        } catch(ClientProtocolException e) {
            log.error("while sending http request, client protocol exception occurred. " + e.getMessage());
        } catch(IOException e) {
            log.error("while sending http request, io exception occurred. " + e.getMessage());
        }

        return "";
    }

    /**
     * 알림톡 REST API 호출에 필요한 파라미터 생성
     *
     * @param senderKey
     * @param senderKeyType
     * @param tmplCd
     * @param tmplNm
     * @param tmplContent
     * @return
     */
    public static final List<NameValuePair> createParameters(final String senderKey, final String senderKeyType, final String tmplCd, final String tmplNm, final String tmplContent) {
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("senderKey", senderKey));
        params.add(new BasicNameValuePair("senderKeyType", senderKeyType));
        params.add(new BasicNameValuePair("templateCode", tmplCd));
        if(StringUtil.isNotEmpty(tmplNm)) {
            params.add(new BasicNameValuePair("templateName", tmplNm));
        }
        if(StringUtil.isNotEmpty(tmplContent)) {
            params.add(new BasicNameValuePair("templateContent", tmplContent));
        }
        return params;
    }



    /**
     * JSON 결과 데이터를 파싱하여 KakaoResponse 객체로 생성 후 반환한다.
     *
     * @param jsonData
     * @return
     */
    public static final KakaoResponse parseResponse(final String jsonData) {
        KakaoResponse res = null;
        try {
            res = (KakaoResponse) JsonUtil.toObject(jsonData, KakaoResponse.class);
        } catch(Exception e) {
            log.error("while parsing json data, exception occurred. " + e.getMessage());
        }
        return res;
    }

    /**
     * JSON 결과 데이터를 파싱하여 KakaoSingleResponse 객체로 생성 후 반환한다.
     *
     * @param jsonData
     * @return
     */
    public static final KakaoSingleResponse parseSingleResponse(final String jsonData) {
        KakaoSingleResponse res = null;
        try {
            res = (KakaoSingleResponse) JsonUtil.toObject(jsonData, KakaoSingleResponse.class);
        } catch(Exception e) {
            log.error("while parsing json data, exception occurred. " + e.getMessage());
        }
        return res;
    }
    /**
     * JSON 결과 데이터를 파싱하여 KakaoSingleResponse 객체로 생성 후 반환한다.
     *
     * @param jsonData
     * @return
     */
    public static final BrandtalkSingleResponse parseSingleBrandTalkResponse(final String jsonData) {
        BrandtalkSingleResponse res = null;
        try {
            res = (BrandtalkSingleResponse) JsonUtil.toObject(jsonData, BrandtalkSingleResponse.class);
        } catch(Exception e) {
            log.error("while parsing json data, exception occurred. " + e.getMessage());
        }
        return res;
    }

    public static String createSubApiUrl(String url, String subUrl){
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("/")
            .append(subUrl);
        return sb.toString();
    }


    /**
     * 엑셀 업로드시 tmplMsgType을 확인후 리턴
     * @param tmplMsgType (엑셀 업로드시 사용 변수)
     * @return tmplMsgType (카카오 tmplMsgType 변수)
     */
    public static String getTmplMsgType(String tmplMsgType) {
        String result = null;

        // 만약 linkType이 비어있다면 빈 값을 리턴
        if(StringUtils.isBlank(tmplMsgType)) {
            return result;
        }

        // 그렇지 않다면 유형 확인
        switch(StringUtils.upperCase(tmplMsgType)) {
        case "기본형":
        case "BA":
            result = "BA";
        break;
        case "부가 정보형":
        case "EX":
            result = "EX";
        break;
        case "광고 추가형":
        case "AD":
            result = "AD";
        break;
        case "복합형":
        case "MI":
            result = "MI";
        break;
        default:
            result = Const.UNKNOWN;
        }
        return result;
    }

}
