package com.mnwise.wiseu.web.rest.model;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnwise.wiseu.web.rest.common.RestResponseCode;

import net.sf.json.JSONObject;

/**
 * 요청을 객체화 시킨다. 요청객체는 객체의 상태를 가지고 있다.
 */
public class NrealtimeRequestVo implements RequestVo {
    private static final Logger log = LoggerFactory.getLogger(NrealtimeRequestVo.class);

    private String requestMethod; // 요청 Method
    private String uri; // 요청 uri
    private String remoteHost; // 요청자 ip

    private int stateCode = 0; // 요청상태 코드
    private String stateMsg = "success"; // 요청상태 메시지
    private int httpCode = 200; // Http 코드

    // 준실시간 요청에 대해 사용 가능한 필드
    private String ecareNo; //이케어 번호
    private String receiverId; //고객ID (개인 식별키)
    private String channel; //발송 채널 구분 (MAIL: M, SMS: S, MMS/LMS: T, FAX: F)
    private String seq; //사용자 식별 키
    private String tmplType; //템플릿 구분 (T: Template, J: Json)
    private String receiverNm; //수신자 이름
    private String receiver; //수신자 [메일주소/핸드폰번호/팩스번호]
    private String sendNm; //발송자 이름
    private String sender; //발송자 [메일주소/핸드폰번호/팩스번호]
    private String previewType; //미리보기 유무

    private JSONObject jonmun; //전문 또는 템플릿
    private String subject; //제목
    private String slot1; //여분 필드1
    private String slot2; //여분 필드2
    private String secuKey; //보안메일 암호화 키
    private String securityPath; //보안메일 커버 경로,파일명
    private String reqUserId; //요청자 아이디
    private String reqDeptId; //요청 부서
    private String reservedDate; //예약발송 일시
    private String filePath1; //첨부파일 경로1
    private String filePath2; //첨부파일 경로2
    private String filePath3; //첨부파일 경로3

    public String getChannel() {
        return channel;
    }

    public String getEcareNo() {
        return ecareNo;
    }

    public String getFilePath1() {
        return filePath1;
    }

    public String getFilePath2() {
        return filePath2;
    }

    public String getFilePath3() {
        return filePath3;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getJonmun() {
        return jonmun != null ? jonmun.toString() : "";
    }

    public String getPreviewType() {
        return previewType;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getReceiverNm() {
        return receiverNm;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public String getReqDeptId() {
        return reqDeptId;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public String getSecuKey() {
        return secuKey;
    }

    public String getSecurityPath() {
        return securityPath;
    }

    public String getSender() {
        return sender;
    }

    public String getSendNm() {
        return sendNm;
    }

    public String getSeq() {
        return seq;
    }

    public String getSlot1() {
        return slot1;
    }

    public String getSlot2() {
        return slot2;
    }

    @Override
    public Object getState() {
        final String msgTrim = stateMsg.trim();

        if(msgTrim.startsWith("{") && msgTrim.endsWith("}")) {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

            try {
                return objectMapper.readValue(stateMsg, Map.class);
            } catch(Exception e) {
                log.error("while parsing the json object, exception occurred. " + e.getMessage());
            }
        }

        return stateMsg;
    }

    public int getStateCode() {
        return stateCode;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public String getSubject() {
        return subject;
    }

    public String getTmplType() {
        return tmplType;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public boolean isError() {
        return (stateCode == RestResponseCode.SUCCESS) ? false : true;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setEcareNo(String ecareNo) {
        this.ecareNo = ecareNo;
    }

    public void setFilePath1(String filePath1) {
        this.filePath1 = filePath1;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

    public void setFilePath3(String filePath3) {
        this.filePath3 = filePath3;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setJonmun(JSONObject jonmun) {
        this.jonmun = jonmun;
    }

    public void setRequestMethod(String method) {
        this.requestMethod = method;
    }

    public void setPreviewType(String previewType) {
        this.previewType = previewType;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setReqDeptId(String reqDeptId) {
        this.reqDeptId = reqDeptId;
    }

    @Override
    public void setRequestInfo(HttpServletRequest req) {
        requestMethod = req.getMethod();
        remoteHost = req.getRemoteHost();
        uri = req.getRequestURI();
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public void setSecuKey(String secuKey) {
        this.secuKey = secuKey;
    }

    public void setSecurityPath(String securityPath) {
        this.securityPath = securityPath;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSendNm(String sendNm) {
        this.sendNm = sendNm;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }

    @Override
    public void setState(int httpCd, int stateCd, String stateMsg) {
        this.httpCode = httpCd;
        this.stateCode = stateCd;
        this.stateMsg = stateMsg;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTmplType(String tmplType) {
        this.tmplType = tmplType;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "NrealtimeRequestVo [method=" + requestMethod + ", uri=" + uri + ", remoteHost=" + remoteHost + ", stateCode=" + stateCode + ", stateMsg=" + stateMsg + ", httpCode=" + httpCode
            + ", ecareNo=" + ecareNo + ", receiverId=" + receiverId + ", channel=" + channel + ", seq=" + seq + ", tmplType=" + tmplType + ", receiverNm=" + receiverNm + ", receiver=" + receiver
            + ", sendNm=" + sendNm + ", sender=" + sender + ", previewType=" + previewType + ", jonmun=" + jonmun + ", subject=" + subject + ", slot1=" + slot1 + ", slot2=" + slot2 + ", secuKey="
            + secuKey + ", securityPath=" + securityPath + ", reqUserId=" + reqUserId + ", reqDeptId=" + reqDeptId + ", reservedDate=" + reservedDate + ", filePath1=" + filePath1 + ", filePath2="
            + filePath2 + ", filePath3=" + filePath3 + "]";
    }

}
