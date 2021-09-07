package com.mnwise.wiseu.web.rest.common;

public class Const {


    /** 대상자 Result ID 값 **/
    public static final String RESULT_ID_PREFIX = "data";
    /** 공백 **/
	public static final String SPACE = " ";
	/** 그루비 **/
	public static final String GROOVY = "G";
	/** 캠페인 **/
	public static final String EM = "EM";
	/** 이케어 **/
	public static final String EC = "EC";
	/** 캠페인 재발송 구분(성공) **/
	public static final String RELATION_SUCCESS = "S";
	/** 캠페인 재발송 구분(실패) **/
	public static final String RELATION_FAIL = "F";
	/** 캠페인 재발송 구분(오픈) **/
	public static final String RELATION_OPEN = "O";
	/** 발송상태 : 종료 **/
	public static final String STATUS_END = "E";

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

    public class Flag {
    	/** 전송대기(SEND_FG) */
        public static final String READY = "R";
    }

    public class Semantic {
	    public static final String KEY_EMAIL = "E";
	    public static final String KEY_TELEPHONE = "S";
	    public static final String KEY_FAX = "F";
    }
    public class SubType {
    	/** 스케줄 */
    	public static final String SCHDULE = "S";
    	/** 스케줄(분) */
    	public static final String SCHDULE_M = "R";
    	/** 준실시간 */
    	public static final String NREALTIME = "N";
    }


    public class Channel {
    	/** 메일 */
    	public static final String EMAIL = "M";
    	/** SMS 문자 */
    	public static final String SMS = "S";
    	/** LMS 문자 */
    	public static final String LMSMMS = "T";
    	/** PUSH */
    	public static final String PUSH = "P";
    	/** FAX */
    	public static final String FAX = "F";
    	/** 알림톡 */
    	public static final String AT = "A";
    	/** 친구톡 */
    	public static final String FT = "C";
    }

    public class ResultCode {
    	/** 성공 */
	    public static final String SUCCESS = "SUCCESS";
	    /** 실패 */
	    public static final String FAIL = "FAIL";
    }
    // API사용 여부
    public class OnOff {
    	public static final String ON = "ON"; // 사용
    	public static final String OFF = "OFF"; // 사용
    }
    // Log사용 여부
    public class ApiLogType {
    	public static final String ALL = "A";  // ALL
    	public static final String LOG = "L";  // LOG Only
    	public static final String EXCEPTION = "E";  // Exception Only
    }
}
