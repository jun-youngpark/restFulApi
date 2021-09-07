package com.mnwise.wiseu.web.report.model.ecare;


import com.mnwise.wiseu.web.base.model.SearchVo;

import net.sf.json.JSONObject;

/**
 * 준실시간 테이블 Vo
 */
public class RealtimeacceptVo extends SearchVo {
    // 준실시간 요청에 대해 사용 가능한 필드
    private static final long serialVersionUID = 90085083464468264L;
    private String seq; //사용자 식별 키
    private int ecareNo; //이케어 번호
    private String channel; //발송 채널 구분 (MAIL: M, SMS: S, MMS/LMS: T, FAX: F, P:PUSH, A:알림톡, C:친구톡, B:브랜드톡)
    private String resultSeq; //결과 순번
    private String listSeq; //목록순번
    private String svcId; //SVCID
    private String reqUserId; //요청자 아이디
    private String reqDeptId; //요청 부서
    private String reqDt; //요청 일자
    private String reqTm; //요청 시간
    private String tmplType; //템플릿 구분 (T: Template, J: Json)
    private String receiverId; //고객ID (개인 식별키)
    private String receiverNm; //수신자 이름
    private String receiver; //수신자 [메일주소/핸드폰번호/팩스번호]
    private String senderNm; //발송자 이름
    private String sender; //발송자 [메일주소/핸드폰번호/팩스번호]
    private String subject; //제목
    private String sendFg; //발송상태
    private String secuKey; //보안메일 암호화 키
    private String securityPath; //보안메일 커버 경로,파일명
    private String errorMsg; //발송 에러 메세지
    private String previewType; //미리보기 유무
    private String reservedDate; //예약발송 일시
    private int datCnt;	// data 테이블 레코드 수
    private String filePath1; //첨부파일 경로1
    private String filePath2; //첨부파일 경로2
    private String filePath3; //첨부파일 경로3
    private String spfidd; //팩스키
    private String jonmun; //전문 또는 템플릿
    private String slot1; //여분 필드1
    private String slot2; //여분 필드2
    private String slot3; //여분 필드3
    

    protected String searchStartReqTm;
    protected String searchEndReqTm;
    protected String searchChannel;
    protected String searchSendFg;
    protected String searchSeq;
    
    public String getSearchSeq() {
        return searchSeq;
    }
    public void setSearchSeq(String searchSeq) {
        this.searchSeq = searchSeq;
    }
    public String getSeq() {
        return seq;
    }
    public void setSeq(String seq) {
        this.seq = seq;
    }
    public int getEcareNo() {
        return ecareNo;
    }
    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getResultSeq() {
        return resultSeq;
    }
    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }
    public String getListSeq() {
        return listSeq;
    }
    public void setListSeq(String listSeq) {
        this.listSeq = listSeq;
    }
    public String getSvcId() {
        return svcId;
    }
    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }
    public String getReqUserId() {
        return reqUserId;
    }
    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }
    public String getReqDeptId() {
        return reqDeptId;
    }
    public void setReqDeptId(String reqDeptId) {
        this.reqDeptId = reqDeptId;
    }
    public String getReqDt() {
        return reqDt;
    }
    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }
    public String getReqTm() {
        return reqTm;
    }
    public void setReqTm(String reqTm) {
        this.reqTm = reqTm;
    }
    public String getTmplType() {
        return tmplType;
    }
    public void setTmplType(String tmplType) {
        this.tmplType = tmplType;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getReceiverNm() {
        return receiverNm;
    }
    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getSenderNm() {
        return senderNm;
    }
    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSendFg() {
        return sendFg;
    }
    public void setSendFg(String sendFg) {
        this.sendFg = sendFg;
    }
    public String getSecuKey() {
        return secuKey;
    }
    public void setSecuKey(String secuKey) {
        this.secuKey = secuKey;
    }
    public String getSecurityPath() {
        return securityPath;
    }
    public void setSecurityPath(String securityPath) {
        this.securityPath = securityPath;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getPreviewType() {
        return previewType;
    }
    public void setPreviewType(String previewType) {
        this.previewType = previewType;
    }
    public String getReservedDate() {
        return reservedDate;
    }
    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }
    public int getDatCnt() {
        return datCnt;
    }
    public void setDatCnt(int datCnt) {
        this.datCnt = datCnt;
    }
    public String getFilePath1() {
        return filePath1;
    }
    public void setFilePath1(String filePath1) {
        this.filePath1 = filePath1;
    }
    public String getFilePath2() {
        return filePath2;
    }
    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }
    public String getFilePath3() {
        return filePath3;
    }
    public void setFilePath3(String filePath3) {
        this.filePath3 = filePath3;
    }
    public String getSpfidd() {
        return spfidd;
    }
    public void setSpfidd(String spfidd) {
        this.spfidd = spfidd;
    }
    public String getJonmun() {
        return jonmun;
    }
    public void setJonmun(String jonmun) {
        this.jonmun = jonmun;
    }
    public String getSlot1() {
        return slot1;
    }
    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }
    public String getSlot2() {
        return slot2;
    }
    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }
    public String getSlot3() {
        return slot3;
    }
    public void setSlot3(String slot3) {
        this.slot3 = slot3;
    }
    public String getSearchStartReqTm() {
        return searchStartReqTm;
    }
    public void setSearchStartReqTm(String searchStartReqTm) {
        this.searchStartReqTm = searchStartReqTm;
    }
    public String getSearchEndReqTm() {
        return searchEndReqTm;
    }
    public void setSearchEndReqTm(String searchEndReqTm) {
        this.searchEndReqTm = searchEndReqTm;
    }
    public String getSearchChannel() {
        return searchChannel;
    }
    public void setSearchChannel(String searchChannel) {
        this.searchChannel = searchChannel;
    }
    public String getSearchSendFg() {
        return searchSendFg;
    }
    public void setSearchSendFg(String searchSendFg) {
        this.searchSendFg = searchSendFg;
    }
    
    	  
}
