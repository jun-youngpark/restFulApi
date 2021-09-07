package com.mnwise.wiseu.web.env.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mnwise.common.util.DateUtil;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.web.spring.BaseController;
import com.mnwise.wiseu.web.common.ChannelType;
import com.mnwise.wiseu.web.env.service.EnvMakeDataService;

/**
 * 검증데이터 생성기 Controller
 */
@Controller
public class EnvMakeDataController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(EnvMakeDataController.class);

    @Autowired private MessageSourceAccessor messageSourceAccessor;
    @Autowired private EnvMakeDataService envMakeDataService;
    
    @Value("${channel.use.list}")
    private String channelUseList;      // 사용 채널
    
    @Value("${make.data.security.key}")
    private String securityKey;         // 보안메일 검증데이터 생성시 보안키
    
    @Value("${make.data.mms.attach}")
    private String mmsAttachPath;       // 검증데이터 첨부파일 경로(MMS)
    
    @Value("${make.data.email.attach}")
    private String emailAttachPath;     // 검증데이터 첨부파일 경로(이메일)
    
    @Value("${make.data.email.attach.name}")
    private String emailAttachName;     // 검증데이터 첨부파일명
    
    @Value("${make.data.attach.type}")
    private String attachType;          // 검증데이터 첨부파일 타입 (J: json타입, T: 절대경로) 
    
    @Value("${make.data.default.senderTel}")
    private String defaultSendTel;      // 기본 발신자 번호
    
    @Value("${make.data.default.senderEmail}")
    private String defaultSendEmail;      // 기본 발신자 번호
    
    @Value("${make.data.default.list.size}")
    private int listSize;       // 리스트 사이즈

    /**
     * - [환경설정>검증데이터 생성기] 검증데이터 생성기 <br/>
     * - JSP : /env/env_makeDataList.jsp <br/>
     * 환경설정 - 검증데이터를 생성하여 인터페이스 테이블에 넣는 화면을 제공
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/makeData.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView view(HttpServletRequest request) throws Exception {
        String channel = ServletRequestUtils.getStringParameter(request, "channel", "M");
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("channelList", channelUseList);
        returnData.put("channel", channel);
        returnData.put("sendType", "");
        returnData.put("pirodoCkYn", "");
        returnData.put("sendObjCd", "");
        returnData.put("attach", "");
        returnData.put("status", "");
        
        return new ModelAndView("/env/env_makeDataList", returnData);
    }

    /**
     * 환경설정 - 검증데이터를 인터페이스 테이블에 저장
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/registData.do", method={RequestMethod.POST})
    public ModelAndView regist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        String serviceNo = ServletRequestUtils.getStringParameter(request, "serviceNo");
        String serviceFrom = ServletRequestUtils.getStringParameter(request, "serviceNoFrom", "");
        String serviceTo = ServletRequestUtils.getStringParameter(request, "serviceNoTo", "");
        String channel = ServletRequestUtils.getStringParameter(request, "channel", "");
        int sendCnt = ServletRequestUtils.getIntParameter(request, "sendCnt", 0);
        String sendType = ServletRequestUtils.getStringParameter(request, "sendType", "");
        String receiverEmail = ServletRequestUtils.getStringParameter(request, "receiverEmail", "");
        String receiverTel = ServletRequestUtils.getStringParameter(request, "receiverTel", "");
        String receiverFax = ServletRequestUtils.getStringParameter(request, "receiverFax", "");
        int successCnt = 0; 
        Locale locale = new Locale(getLoginUserVo().getLanguage());
        
        try {
            //return될 데이터 미리 세팅 (중간에 오류가 발생할 경우를 대비함)
            returnData.put("sendCnt", sendCnt);
            returnData.put("serviceNoFrom", serviceFrom);
            returnData.put("serviceNoTo", serviceTo);
            returnData.put("sendType", sendType);
            returnData.put("channelList", channelUseList);
            returnData.put("channel", channel);
            returnData.put("receiverFax", receiverFax);
            returnData.put("receiverEmail", receiverEmail);
            returnData.put("receiverTel", receiverTel);
            returnData.put("serviceNo", serviceNo);
            
            if(sendType.equals("channel")) {
                List<String> serviceNoList = null;
                serviceNoList =  envMakeDataService.getServiceNoForFromTo(channel, serviceFrom, serviceTo);
                log.info("selected.service.list = {}", serviceNoList.toString());
                if(serviceNoList != null && serviceNoList.size() > 0) {
                    for(int i=0; i<serviceNoList.size(); i++) {
                        serviceNo = serviceNoList.get(i);
                        returnData = inputTestData(request, serviceNo, returnData);
                        successCnt += Integer.parseInt(returnData.get("insertCnt").toString());
                    }
                }
            }else {
                returnData = inputTestData(request, serviceNo, returnData);
                successCnt = Integer.parseInt(returnData.get("insertCnt").toString());
            }
            
            if(successCnt != 0) {       // 처리 성공
                log.info("insert success count = {}", successCnt);
                returnData.put("result", "SUCCESS");
                returnData.put("message", messageSourceAccessor.getMessage("env.make.data.alert.save.msg5", new Object[] {successCnt}, locale));
            }else { // 처리 실패
                if(returnData.get("result") != null) {  // 결과코드가 존재하는 경우
                    String result = returnData.get("result").toString();
                    log.info("return.result = {}", result);
                    switch(result) { 
                        case "NOSERVICE":   // 서비스 없음
                            returnData.put("message", messageSourceAccessor.getMessage("env.make.data.alert.save.msg6", locale));
                            break;
                        case "NOTEMPLATE":  // 템플릿 없음
                            returnData.put("message", messageSourceAccessor.getMessage("env.make.data.alert.save.msg7", locale));
                            break;
                    }
                }else { // 검증데이터 생성 중 Exception이 발생한 경우
                    log.info("process fail.");
                    returnData.put("result", "FAILED");
                    returnData.put("message", messageSourceAccessor.getMessage("env.make.data.alert.save.msg8", locale));
                }
            }
            log.info("result=" + returnData.get("message"));
        }catch(Exception e) {
            e.printStackTrace();
            log.debug("e : " + e.toString());
        }
        
        return new ModelAndView("/env/env_makeDataList", returnData);
    }
    
    public Map<String, Object> inputTestData(HttpServletRequest request, String serviceNo, Map<String, Object> returnData) {
        String serviceType = ServletRequestUtils.getStringParameter(request, "serviceType", "EC");
        int sendCnt = ServletRequestUtils.getIntParameter(request, "sendCnt", 0);
        String templeteType = ServletRequestUtils.getStringParameter(request, "templeteType", "J");
        String attach = ServletRequestUtils.getStringParameter(request, "attach", "N");     // 첨부파일 사용여부(Y: 추가, N: 추가 안함)
        String status = ServletRequestUtils.getStringParameter(request, "status", "");      // 발송상태(공백: 전체, W: 작성중/중지, R: 실행중)
        
        int insertCnt = 0;
        Map<String, Object> ecareMap = new HashMap<String, Object>();
        String templeteMsg = "";
        String addedMsg = "";
        //ecare 정보 가져오기 (jsp에서 가져온 serviceNo로 조회가 안되면 에러)
        ecareMap = envMakeDataService.getServiceInfo(serviceNo, serviceType, status);
        returnData.put("status", status);
        returnData.put("attach", attach);
        
        if(ecareMap == null) {
            returnData.put("insertCnt", 0);
            returnData.put("result", "NOSERVICE");
            return returnData;
        }
        
        String ch = ecareMap.get("CHANNEL_TYPE").toString();    // 발송채널
        returnData.put("ecareMap", ecareMap);

        //템플릿 메시지 가져오기 (메시지가 없으면 에러)
        templeteMsg = envMakeDataService.getTempleteMsg(serviceNo, serviceType);
        if(templeteMsg == null || templeteMsg.trim().equals("")) {
            returnData.put("insertCnt", 0);
            returnData.put("result", "NOTEMPLATE");
            return returnData;
        }
        
        //푸시일 경우 본문과 팝업 메시지로 나눠지므로 팝업 메시지를 따로 가져와준다.
        if(ch.equalsIgnoreCase("P")) {
            addedMsg = envMakeDataService.getPushPopMsg(serviceNo, serviceType);
        }else if(ch.equalsIgnoreCase("M") 
            && ecareMap.get("SECURITY_MAIL_YN").toString().equalsIgnoreCase("Y")) {  // 보안메일인 경우, 커버 템플릿을 조회
            addedMsg = envMakeDataService.getCoverTemplateMsg(serviceNo, serviceType);
        }
        
        ArrayList<Map<String, Object>> nvMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> nvMap = new HashMap<String, Object>();
        
        //템플릿을 로딩하여 변수를 임의로 입력하여 Json으로 변환
        String jsonTempleteMsg = "";
        
        //기본적으로 필요한 값은 Controller에서 직접 입력
        for(int i=1; i<=sendCnt; i++) {
            nvMap = new HashMap<String, Object>();
            nvMap.putAll(ecareMap);
            
            nvMap.put("tmplType", templeteType);
            nvMap.put("receiverId", createSeq(i, ch));
            nvMap.put("channelType", ch);
            nvMap.put("senderNm", ecareMap.get("SENDER_NM"));
            if(ch.equalsIgnoreCase("M")) {      // 메일
                nvMap.put("sender", ecareMap.get("SENDER_EMAIL")==null ? defaultSendEmail : ecareMap.get("SENDER_EMAIL").toString());
                nvMap.put("receiver", returnData.get("receiverEmail").toString());
            }else {     // 그 외 채널
                nvMap.put("sender", ecareMap.get("SENDER_TEL")==null ? defaultSendTel : ecareMap.get("SENDER_TEL").toString());
                
                if(ch.equalsIgnoreCase("F")) {  // FAX
                    nvMap.put("receiver", returnData.get("receiverFax").toString());
                }else {
                    nvMap.put("receiver", returnData.get("receiverTel").toString());
                }
            }
            setSubject(nvMap, ch);             // 채널에 따른 메시지 제목 적용
            setSecurityKey(nvMap, ch);         // 보안메일인 경우 암호키 적용
            setAttachment(nvMap, ch, attach);  // 첨부파일 적용
            nvMap.put("reqUserId", ecareMap.get("REQ_USER_ID"));
            nvMap.put("reqDeptId", ecareMap.get("REQ_DEPT_ID"));
            nvMap.put("receiverNm", " ");
            nvMap.put("status", status);
            
            //템플릿을 로딩하여 변수를 임의로 입력하여 Json으로 변환
            jsonTempleteMsg = elTempleteVarToJson(templeteMsg + addedMsg, i, listSize);
            nvMap.put("jonmun", jsonTempleteMsg);
            
            if(ChannelType.PUSH.equalsIgnoreCase(ch)) {
                nvMap.put("cnts", "");
                nvMap.put("appNo", ecareMap.get("PUSH_APP_ID"));
                nvMap.put("msgType", ecareMap.get("PUSH_MSG_TYPE"));
//                nvMap.put("pushTmpltTpcd", ecareMap.get("PUSH_TEMPLATE_CODE"));
                nvMap.put("retryCnt", ecareMap.get("RETRY_CNT"));
                nvMap.put("pushDtlMsgDvcd", "1"); //푸시 메시지 형식 구분
                //umsFwDvcd = "0001";
            }else if(ChannelType.ALIMTALK.equalsIgnoreCase(ch) || ChannelType.FRIENDTALK.equalsIgnoreCase(ch)) {
                if(ecareMap.get("KAKAO_IMAGE_NO") != null && !ecareMap.get("KAKAO_IMAGE_NO").equals("")) {
                    nvMap.put("kakaoImageNo", ecareMap.get("KAKAO_IMAGE_NO"));
                }
            }
            nvMapList.add(nvMap);
        }
        try {
            insertCnt = envMakeDataService.insertMakeData(nvMapList);
            returnData.put("insertCnt", insertCnt);
        }catch(Exception e) {
            String errMsg = e.toString();
            returnData.put("errMsg", errMsg);
        }
        
        return returnData;
    }
    
    /**
     * 첨부파일 사용시, 첨부파일경로를 FILE_PATH에 적용
     * 
     *  attach타입이 "J"(전문)인 경우
     *  - 이메일 : {"attach":[{"path":"파일경로","name":"파일명"}]}
     *  - MMS : {"attach":[{"path":"파일경로"}]}
     * @param nvMap     I/F테이블에 넣을 POJO객체
     * @param channel   발송채널
     * @param attach    첨부파일여부(Y: 추가, N:추가 안함)
     */
    private void setAttachment(Map<String, Object> nvMap, String channel, String attach) {
        if(attach.equalsIgnoreCase("Y")) {  // 첨부파일 추가시
            // email 인 경우 이메일 첨부파일경로, MMS인 경우 이미지 경로를 적용
            if(attachType.equalsIgnoreCase("T")) {  // 절대경로를 삽입
                nvMap.put("filePath1", channel.equalsIgnoreCase("M") ? emailAttachPath : channel.equalsIgnoreCase("T") ? mmsAttachPath : "");
            }else if(attachType.equalsIgnoreCase("J")) {    // JSON 타입으로 적용
                JSONObject jsonObj = new JSONObject();  // FILE_PATH
                JSONArray attachArr = new JSONArray();  // 첨부파일 목록
                
                JSONObject attachObj = new JSONObject();   // 첨부파일
                
                if(channel.equalsIgnoreCase("M")) {     // 이메일인 경우, 파일명과 파일경로를 JSON에 추가
                    attachObj.put("name", emailAttachName);
                    attachObj.put("path", emailAttachPath);
                    attachArr.add(attachObj);
                    jsonObj.put("attach", attachArr);
                    nvMap.put("filePath1", jsonObj.toJSONString());
                }else if(channel.equalsIgnoreCase("T")) {   // MMS인 경우, 파일경로만 JSON에 추가
                    attachObj.put("path", mmsAttachPath);
                    attachArr.add(attachObj);
                    jsonObj.put("attach", attachArr);
                    nvMap.put("filePath1", jsonObj.toJSONString());
                }
                
            }
        }
    }

    /**
     * 보안 메일인 경우 암호키 적용
     * @param nvMap     인터페이스전용 POJO객체
     * @param channel   채널코드
     */
    private void setSecurityKey(Map<String, Object> nvMap, String channel) {
        if(channel.equalsIgnoreCase("M") && nvMap.get("SECURITY_MAIL_YN").toString().equals("Y")) {
            nvMap.put("secuKey", securityKey);
        }
    }

    /**
     * HashMap내 채널에 따른 제목 추가
     * @param nvMap
     * @param channel
     */
    private void setSubject(Map<String, Object> nvMap, String channel) {
        if(channel.equalsIgnoreCase("M") || channel.equalsIgnoreCase("T") || channel.equalsIgnoreCase("F")){
            // 이케어 제목이 존재하지 않은 경우 하드코딩된 제목으로 발송
            nvMap.put("ecare_preface", nvMap.get("ecare_preface") == null ? "UMS테스트 발송메시지입니다." : nvMap.get("ecare_preface").toString());
        }
    }

    /**
     * 검증데이터 SEQ 자동생성
     * [이메일] SEQ포맷 : UMS + 현재일시(yyyyMMddHHmmssSSS) + 인덱스(6자리)
     * [그 외] SEQ포맷 : 현재일시(yyMMddHHmmssSS) + 인덱스(6자리)
     * @param i 인덱스
     * @param channel 발송채널
     * @return SEQ 값
     */
    private String createSeq(int i, String channel) {
        if(channel.equalsIgnoreCase("M")) {
            return "UMS" + DateUtil.getNowMillisDateTime() + StringUtil.leftPad(String.valueOf(i), 6, "0");
        }else {
            return DateUtil.getNowDateTime("yyMMddHHmmss") + StringUtil.leftPad(String.valueOf(i), 6, "0");
        }
    }

    /**
     *  [환경설정>검증데이터 생성기] 채널목록조회
     *
     * @param serverInfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/env/channelServiceList.json", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody public List<Map<String, Object>> channelServiceList(String channel, String status) {
        try {
            return envMakeDataService.channelServiceList(channel, status);
        } catch (Exception e) {
            log.error(null, e);
        }
        return null;
    }
    
    //[fullTempleteStr : 템플릿의 변수 형식 -  #{ }]
    public static String elTempleteVarToJson(String fullTempleteStr, int cnt, int listSize) {
        String returnJsonData = null;
        String content = fullTempleteStr;
        JSONObject object = new JSONObject();
        
        //정규식 - '#{문자}' 패턴으로 추출
        Pattern p = Pattern.compile("\\#[{](.*?)[}]");
        StringBuilder sb = new StringBuilder();
        sb.append(content);
        Matcher m = p.matcher(sb.toString());
        //패턴을 찾을 때 마다 
        while(m.find()) {
            String item = m.group(1);
            if(item.contains("[")) {    // ARRAY 처리
                String arrayKey = item.substring(0, item.indexOf("["));
                JSONArray array = new JSONArray();
                for(int i=0; i<listSize; i++) {
                    array.add(arrayKey + "[" + i + "]");
                }
                object.put(arrayKey, array);
            }else {         // DATA 처리
                // 변수명을 Key 값으로 두고 변수명과 증가값을 더하여 Value값으로 하여 Map형식으로 변환
                object.put(item, item + String.valueOf(cnt));
            }
        }
        
        returnJsonData = object.toString();
        return returnJsonData;
    }
}
