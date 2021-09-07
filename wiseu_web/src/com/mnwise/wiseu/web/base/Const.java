package com.mnwise.wiseu.web.base;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Const {
    private static final Logger log = LoggerFactory.getLogger(Const.class);
    /////////////////////////////////////////////////////////////////
    // 공통 상수

    /**
     *  대상자 result id 값
     */
    public static final String RESULT_ID_PREFIX = "data";
    public static final String UNKNOWN = "UNKNOWN";

    /**
     *  템플릿 강조 표기 유형
     */
    public class KkoEmType{
        /** 사용안함 */
        public static final String NONE = "NONE";
        /** 강조표기  */
        public static final String TEXT = "TEXT";
        /** 이미지 */
        public static final String IMAGE = "IMAGE";
    }
    /**
     *  첨부파일 타입.
     */
    public class AttachFileType{
        /** 내 PC 불러오기 */
        public static final String UPLOAD = "UPLOAD";
        /** 경로 입력 */
        public static final String PATH = "PATH";
        /** PDF 파일 암호화*/
        public static final String ENCPDF = "ENCPDF";
    }

    /**
     * 이케어 서비스 타입.
     */
    public class EcareSubType{
        /** 스케줄(분): S */
        public static final String SCHEDULE = "S";
        public static final String SCHEDULE_MINUTE = "R";
        /** 준실시간: */
        public static final String NREALTIME = "N";
    }
    /**
     * 메일 타입.
     */
    public class MailType{
        /** 보안메일: SMAIL */
        public static final String SECUMAIL = "SMAIL";
        /** HTML TO PDF : HTMLPDF */
        public static final String HTML2PDF = "HTMLPDF";
        /** 사용안함 : NONE */
        public static final String NONE = "NONE";
    }

    /**
     * 카카오 템플릿 상태
     * READY:R
     */
    public class KakaoTemplateStatus{
        /** 대기: R */
        public static final String READY = "R";
    }
    /**
     * 서비스 타입.
     * ECARE:E, CAMPAIGN:C
     */
    public class ServiceType{
        /** 이케어 : E */
        public static final String ECARE = "E";
        /** 캠페인 : C */
        public static final String CAMPAIGN = "C";
    }

    /**
     * CLIENT 타입.
     * Ecare: EC,
     * Campaign: EM
     */
    public class Client {
        /** 이케어 */
        public static final String EC = "EC";
        /** 캠페인 */
        public static final String EM = "EM";
    }

    /**
     * - 채널 별 Enum 생성, 신규 추가시 아래 코드 작성
     *   코드 테이블
     * @param 채널코드
     * @param JSP 파일명
     */
     public enum enumChannel {
       EMAIL  ("M","mail"),
       SMS  ("S","sms"),
       LMSMMS  ("T","mms"),
       FAX  ("F","fax"),
       ALIMTALK  ("A","alt"),
       FRIENDTALK  ("C","frt"),
       PUSH  ("P","push"),
       BRANDTALK  ("B","brt");

       private String key;
       private String name;

       enumChannel(String key, String name) {
           this.key = key;
           this.name = name;
       }

       public static String findName(String code){
           for(enumChannel e : enumChannel.values()){
             if(code.equalsIgnoreCase(e.getKey())) {
                 return e.getName();
             }
           }
           return null;
       }

       public String getKey() {
           return key;
       }

       public String getName() {
           return name;
       }
    }

    /**
     * 채널정보
     */
    public static class Channel {
        public static final String MAIL       = "M";
        public static final String SMS        = "S";
        public static final String LMSMMS     = "T";
        public static final String FAX        = "F";
        public static final String PUSH       = "P";
        public static final String ALIMTALK   = "A";
        public static final String FRIENDTALK = "C";
        public static final String BRANDTALK = "B";

        public static String getChannelName(String channelType) {
            switch(channelType.toUpperCase()) {
                case MAIL       : return "EMAIL";
                case SMS        : return "SMS";
                case LMSMMS     : return "LMSMMS";
                case FAX        : return "FAX";
                case PUSH       : return "PUSH";
                case ALIMTALK   : return "ALIMTALK";
                case FRIENDTALK : return "FRIENDTALK";
                case BRANDTALK : return "BRANDTALK";
            }

            return null;
        }
    }

    public static final String SEMANTIC_KEY_EMAIL = "E";
    public static final String SEMANTIC_KEY_TELEPHONE = "S";
    public static final String SEMANTIC_KEY_FAX = "F";

    public static final String WISEU = "1";
    public static final String WISEMOKA = "2";

    /**
     * 메시지 유형. 기본핸들러 생성시 참조됨.
     */
    public class MsgType{
        /** 이케어 */
        public static final String ECARE = "A";
        /** 캠페인 */
        public static final String CAMPAIGN = "C";

        public class full{
            /** 이케어 */
            public static final String ECARE = "ECARE";
            /** 캠페인 */
            public static final String CAMPAIGN = "CAMPAIGN";
        }
    }

    /////////////////////////////////////////////////////////////////
    // 코드 상수

    // 캠페인 권한
    public static final String P_CAMPAIGN = "C";
    // 이케어 권한
    public static final String P_ECARE = "S";

    /////////////////////////////////////////////////////////////////
    // 사용자관리 상수

    /** 최상위 부서 코드 **/
    public static final String SUPER_GRP_CD = "01";

    /** User Role 사용자 권한 **/
    //public static final String USER_ROLE_MAKER = "M"; // Maker
    //public static final String USER_ROLE_CHECKER = "C"; // Checker
    public static final String USER_ROLE_ADMIN = "A"; // admin


    /////////////////////////////////////////////////////////////////
    // 캠페인 상수

    /** 캠페인 재발송 구분 **/
    public static final String RESEND_L_OPEN = "resendOpen"; // 타겟발송 - 수신확인
    public static final String RESEND_L_DURATION = "resendDuration"; // 타겟발송 - 10초이상 확인
    public static final String RESEND_L_DUPLICATION = "resendDuplication"; // 타겟발송 - 1회이상 확인
    public static final String RESEND_L_LINK = "resendLink"; // 타겟발송 - 링크 클릭 대상자
    public static final String RESEND_R_FAIL = "resendFail"; // 재발송 - 실패 재발송
    public static final String RESEND_R_SUCCESS = "resendSuccess"; // 재발송 - 성공 대상자 재발송
    public static final String RESEND_L_MOBILE = "resendMobile"; // 타겟발송 - 1회이상 확인

    public static final String RELATION_SUCCESS = "S";
    public static final String RELATION_FAIL = "F";
    public static final String RELATION_OPEN = "O";
    public static final String RELATION_NORMAL = "N";
    public static final String RELATION_RESEND = "R";
    public static final String RELATION_LINK = "L";

    /////////////////////////////////////////////////////////////////
    // 세그먼트 상수

    // 세그먼트 비동기 업로드 수행 대기
    public static final String SEGMENT_STS_UPLOAD_WAIT = "0";
    // 세그먼트 비동기 업로드 수행중
    public static final String SEGMENT_STS_UPLOAD_RUN = "1";
    // 세그먼트 비동기 업로드 수행완료
    public static final String SEGMENT_STS_UPLOAD_END = "2";
    // 세그먼트 비동기 업로드 수행실패
    public static final String SEGMENT_STS_UPLOAD_FAIL = "3";

    /** 수신거부자관리 **/
    public static final String UPLOAD_REJECT_NO = "0"; // 선택안함

    /////////////////////////////////////////////////////////////////
    // 리포트 상수

    public static final String UFT16LE = "UTF-16LE";
    public static final byte[] TAB = toByteArray("\t", UFT16LE);
    public static final byte[] NEWLINE = toByteArray("\n", UFT16LE);

    private static byte[] toByteArray(String str, String encoding) {
        try {
            return str.getBytes(encoding);
        } catch(UnsupportedEncodingException e) {
            log.error(null, e);
        }
        return null;
    }
}
