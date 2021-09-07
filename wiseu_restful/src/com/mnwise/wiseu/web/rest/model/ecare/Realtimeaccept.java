package com.mnwise.wiseu.web.rest.model.ecare;


import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

/**
 * 준실시간 테이블 Vo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class Realtimeaccept extends BaseVo {
    // 준실시간 요청에 대해 사용 가능한 필드
    protected String seq; //사용자 식별 키
    protected int ecareNo; //이케어 번호
    protected String channel; //발송 채널 구분 (MAIL: M, SMS: S, MMS/LMS: T, FAX: F, P:PUSH, A:알림톡, C:친구톡, B:브랜드톡)
    protected String resultSeq; //결과 순번
    protected String listSeq; //목록순번
    protected String svcId; //SVCID
    protected String reqUserId; //요청자 아이디
    protected String reqDeptId; //요청 부서
    protected String reqDt; //요청 일자
    protected String reqTm; //요청 시간
    @Size(max=1) protected String tmplType; //템플릿 구분 (T: Template, J: Json)
    @NotBlank @Size(max=50) protected String receiverId; //고객ID (개인 식별키)
    @NotBlank @Size(max=50) protected String receiverNm; //수신자 이름
    @NotBlank @Size(max=50) protected String receiver; //수신자 [메일주소/핸드폰번호/팩스번호]
    @NotBlank @Size(max=50) protected String senderNm; //발송자 이름
    @NotBlank @Size(max=50) protected String sender; //발송자 [메일주소/핸드폰번호/팩스번호]
    @NotBlank protected String subject; //제목
    protected String sendFg; //발송상태
    protected String secuKey; //보안메일 암호화 키
    protected String securityPath; //보안메일 커버 경로,파일명
    protected String errorMsg; //발송 에러 메세지
    protected String previewType; //미리보기 유무
    protected String reservedDate; //예약발송 일시
    protected int datCnt;	// data 테이블 레코드 수
    protected String filePath1; //첨부파일 경로1
    protected String filePath2; //첨부파일 경로2
    protected String filePath3; //첨부파일 경로3
    protected String spfidd; //팩스키
    protected JSONObject jonmun; //전문 또는 템플릿
    protected String slot1; //여분 필드1
    protected String slot2; //여분 필드2
    protected String slot3; //여분 필드3
    protected String slot4; //여분 필드4
    protected String slot5; //여분 필드5
    protected String slot6; //여분 필드6
    protected String slot7; //여분 필드7
    protected String slot8; //여분 필드8
    protected String slot9; //여분 필드9
    protected String slot10; //여분 필드10


}
