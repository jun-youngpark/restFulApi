-- 대상자DB접속정보
CREATE TABLE NVDBINFO (
    DBINFO_SEQ                NUMERIC(2)          NOT NULL,
    DRIVER_NM                 VARCHAR2(250),
    SERVER_NM                 VARCHAR2(100),
    DRIVER_DSN                VARCHAR2(250),
    DBUSER_ID                 VARCHAR2(30),
    DBPASSWORD                VARCHAR2(50),
    ENCODING                  VARCHAR2(20),
    DECODING                  VARCHAR2(20),
    DBKIND                    VARCHAR2(20),
    TESTQUERY                 VARCHAR2(250)
);
ALTER TABLE NVDBINFO ADD CONSTRAINT PK_NVDBINFO PRIMARY KEY (DBINFO_SEQ);

-- 금칙어
CREATE TABLE NVBADWORD (
    CHANNEL_TYPE              VARCHAR2(10)        NOT NULL,
    BAD_WORDS                 CLOB
);
ALTER TABLE NVBADWORD ADD CONSTRAINT PK_NVBADWORD PRIMARY KEY (CHANNEL_TYPE);

-- 기본핸들러
CREATE TABLE NVDEFAULTHANDLER (
    SEQ                       NUMERIC(10)         NOT NULL,
    HANDLE_NM                 VARCHAR2(300),
    HANDLE_DESC               VARCHAR2(500),
    SERVICE_TYPE              CHAR(1),
    CHANNEL                   CHAR(1)             NOT NULL,
    HANDLE_TYPE               CHAR(1)             DEFAULT 'G',
    HANDLE_ATTR               VARCHAR2(10),
    USER_ID                   VARCHAR2(50),
    CREATE_DT                 CHAR(8),
    CREATE_TM                 CHAR(6),
    HANDLER                   CLOB,
    AB_TEST_YN                VARCHAR(1)          DEFAULT 'N',
    MSG_TYPE                  VARCHAR2(1)
);
ALTER TABLE NVDEFAULTHANDLER ADD CONSTRAINT PK_NVDEFAULTHANDLER PRIMARY KEY (SEQ);

-- 유효수신구간
CREATE TABLE NVDURATIONINFO (
    DURATIONINFO_CD           CHAR(2)             NOT NULL,
    MAXTIME                   NUMERIC(15),
    MINTIME                   NUMERIC(15),
    VALID_CHK                 CHAR(1)
);
ALTER TABLE NVDURATIONINFO ADD CONSTRAINT PK_NVDURATIONINFO PRIMARY KEY (DURATIONINFO_CD);

-- 대상자파일업로드
CREATE TABLE NVFILEUPLOAD (
    TARGET_NO                 NUMERIC(15)         NOT NULL,
    CUSTOMER_ID               VARCHAR2(50)        NOT NULL,
    CUSTOMER_NM               VARCHAR2(100),
    CUSTOMER_EMAIL            VARCHAR2(100),
    CUSTOMER_TEL              VARCHAR2(15),
    CUSTOMER_FAX              VARCHAR2(15),
    SLOT1                     VARCHAR2(100),
    SLOT2                     VARCHAR2(100),
    SLOT3                     VARCHAR2(100),
    SLOT4                     VARCHAR2(100),
    SLOT5                     VARCHAR2(100),
    SLOT6                     VARCHAR2(100),
    SLOT7                     VARCHAR2(100),
    SLOT8                     VARCHAR2(100),
    SLOT9                     VARCHAR2(100),
    SLOT10                    VARCHAR2(100),
    SLOT11                    VARCHAR2(100),
    SLOT12                    VARCHAR2(100),
    SLOT13                    VARCHAR2(100),
    SLOT14                    VARCHAR2(100),
    SLOT15                    VARCHAR2(100),
    SLOT16                    VARCHAR2(100),
    SLOT17                    VARCHAR2(100),
    SLOT18                    VARCHAR2(100),
    SLOT19                    VARCHAR2(100),
    SLOT20                    VARCHAR2(100),
    SLOT21                    VARCHAR2(100),
    SLOT22                    VARCHAR2(100),
    SLOT23                    VARCHAR2(100),
    SLOT24                    VARCHAR2(100),
    SLOT25                    VARCHAR2(100),
    SLOT26                    VARCHAR2(100),
    SLOT27                    VARCHAR2(100),
    SLOT28                    VARCHAR2(100),
    SLOT29                    VARCHAR2(100),
    SLOT30                    VARCHAR2(100),
    SLOT31                    VARCHAR2(100),
    SLOT32                    VARCHAR2(100),
    SLOT33                    VARCHAR2(100),
    SLOT34                    VARCHAR2(100),
    SLOT35                    VARCHAR2(100),
    SLOT36                    VARCHAR2(100),
    SLOT37                    VARCHAR2(100),
    SLOT38                    VARCHAR2(100),
    SLOT39                    VARCHAR2(100),
    SLOT40                    VARCHAR2(100),
    SLOT41                    VARCHAR2(100),
    SLOT42                    VARCHAR2(100),
    SLOT43                    VARCHAR2(100),
    SLOT44                    VARCHAR2(100),
    SLOT45                    VARCHAR2(100),
    SLOT46                    VARCHAR2(100),
    SLOT47                    VARCHAR2(100),
    SLOT48                    VARCHAR2(100),
    SLOT49                    VARCHAR2(100),
    SLOT50                    VARCHAR2(100),
    SEG                       VARCHAR2(20),
    CALL_BACK                 VARCHAR2(50),
    CUSTOMER_SLOT1            VARCHAR2(50),
    CUSTOMER_SLOT2            VARCHAR2(50),
    SENDER_NM                 VARCHAR2(50),
    SENDER_EMAIL              VARCHAR2(100),
    RETMAIL_RECEIVER          VARCHAR2(100),
    SENTENCE                  VARCHAR2(2000)
);
ALTER TABLE NVFILEUPLOAD ADD CONSTRAINT PK_NVFILEUPLOAD PRIMARY KEY (TARGET_NO, CUSTOMER_ID);

-- 발송주기
CREATE TABLE NVSENDCYCLE (
    CYCLE_CD                  CHAR(1)             NOT NULL,
    CYCLE_NM                  VARCHAR2(20)
);
ALTER TABLE NVSENDCYCLE ADD CONSTRAINT PK_NVSENDCYCLE PRIMARY KEY (CYCLE_CD);

-- 메일발송결과코드
CREATE TABLE NVSENDERR (
    ERROR_CD                  VARCHAR2(4)         NOT NULL,
    ERROR_NM                  VARCHAR2(100),
    ERROR_DESC                VARCHAR2(250),
    ERROR_SMS_DESC            VARCHAR2(250),
    CATEGORY_CD               VARCHAR2(12),
    CHANNEL_TYPE              CHAR(1),
    SPAM_YN                   CHAR(1)             DEFAULT 'N'
);
ALTER TABLE NVSENDERR ADD CONSTRAINT PK_NVSENDERR PRIMARY KEY (ERROR_CD);

-- 서버환경정보
CREATE TABLE NVSERVERINFO (
    HOST_NM                                       VARCHAR2(250)       NOT NULL,
    PORT_NO                                       VARCHAR2(5),
    DRIVER_NM                                     VARCHAR2(250),
    DRIVER_DSN                                    VARCHAR2(250),
    DBUSER_ID                                     VARCHAR2(30),
    DBPASSWORD                                    VARCHAR2(30),
    OPENCLICK_PATH                                VARCHAR2(250),
    SURVEY_PATH                                   VARCHAR2(250),
    LINK_PATH                                     VARCHAR2(250),
    HTMLMAKER_PATH                                VARCHAR2(250),
    OPENIMAGE_PATH                                VARCHAR2(250),
    DURATION_PATH                                 VARCHAR2(250),
    REJECT_PATH                                   VARCHAR2(250),
    SMTP_IP                                       VARCHAR2(250),
    SMTP_PORT                                     VARCHAR2(5),
    FULA_IP                                       VARCHAR2(250),
    FULA_PORT                                     VARCHAR2(5),
    FTP_YN                                        CHAR(1),
    FTP_USER_ID                                   VARCHAR2(30),
    FTP_PASSWORD                                  VARCHAR2(30),
    LASTUPDATE_DT                                 CHAR(8),
    EDITOR_ID                                     VARCHAR2(15),
    RET_DOMAIN                                    VARCHAR2(100),
    RETRY_CNT                                     NUMERIC(2),
    B4_SEND_APPROVE_YN                            CHAR(1)             DEFAULT 'N',
    B4_SEND_VERIFY_YN                             CHAR(1)             DEFAULT 'N',
    B4_REAL_SEND_TEST_SEND_YN                     CHAR(1)             DEFAULT 'N',
    ASE_LINK_MERGE_PARAM                          VARCHAR2(500),
    ASE_REJECT_MERGE_PARAM                        VARCHAR2(500),
    ASE_OPEN_SCRIPTLET                            VARCHAR2(2000),
    GROOVY_LINK_MERGE_PARAM                       VARCHAR2(500),
    GROOVY_REJECT_MERGE_PARAM                     VARCHAR2(500),
    GROOVY_OPEN_SCRIPTLET                         VARCHAR2(2000),
    RESEND_INCLUDE_RETURNMAIL_YN                  CHAR(1)             DEFAULT 'Y',
    RESEND_INCLUDE_MAIL_KEY_YN                    CHAR(1)             DEFAULT 'Y',
    RESEND_ERROR_CD                               VARCHAR2(500),
    FAX_RESEND_ERROR_CD                           VARCHAR2(500),
    SMS_RESEND_ERROR_CD                           VARCHAR2(500),
    ALTALK_RESEND_ERROR_CD                        VARCHAR2(500),
    PUSH_RESEND_ERROR_CD                          VARCHAR2(500),
    SPOOL_PRESERVE_PERIOD                         VARCHAR2(4)         DEFAULT '7',
    LOG_PRESERVE_PERIOD                           VARCHAR2(4),
    RESULT_FILE_DOWNLOAD_YN                       CHAR(1)             DEFAULT 'Y',
    SUCS_RESULT_FILE_DOWNLOAD_YN                  CHAR(1)             DEFAULT 'N',
    KAKAO_TEMPLATE_LAST_SYNC_DTM                  VARCHAR2(14)
);
ALTER TABLE NVSERVERINFO ADD CONSTRAINT PK_NVSERVERINFO PRIMARY KEY (HOST_NM);

-- 메일발송결과카테고리
CREATE TABLE NVSMTPCATEGORY (
    CATEGORY_CD               VARCHAR2(12)        NOT NULL,
    GRP_CD                    CHAR(2),
    PCATEGORY_CD              VARCHAR2(12),
    LEVEL_CD                  NUMERIC(2),
    CATEGORY_NM               CHAR(50),
    CATEGORY_DESC             CHAR(100),
    ACTIVE_YN                 CHAR(1)
);
ALTER TABLE NVSMTPCATEGORY ADD CONSTRAINT PK_NVSMTPCATEGORY PRIMARY KEY (CATEGORY_CD);

-- 테스트수신자
CREATE TABLE NVTESTUSER (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    CAMPAIGN_TYPE             CHAR(1)             NOT NULL,
    USER_ID                   VARCHAR2(15)        NOT NULL,
    SEQ_NO                    NUMERIC(5)          NOT NULL
);
ALTER TABLE NVTESTUSER ADD CONSTRAINT PK_NVTESTUSER PRIMARY KEY (CAMPAIGN_NO, CAMPAIGN_TYPE, USER_ID, SEQ_NO);

-- 테스트수신자풀
CREATE TABLE NVTESTUSERPOOL (
    USER_ID                   VARCHAR2(15)        NOT NULL,
    SEQ_NO                    NUMERIC(5)          NOT NULL,
    TESTRECEIVER_EMAIL        VARCHAR2(100)       NOT NULL,
    TESTRECEIVER_TEL          VARCHAR2(50),
    TESTRECEIVER_NM           VARCHAR2(50)        NOT NULL,
    TESTRECEIVER_FAX          VARCHAR2(50),
    TEST_GRP_CD               NUMERIC(5)
);
ALTER TABLE NVTESTUSERPOOL ADD CONSTRAINT PK_NVTESTUSERPOOL PRIMARY KEY (USER_ID, SEQ_NO);

-- 최상위도메인
CREATE TABLE NVTOPDOMAIN (
    DOMAIN_NM                 VARCHAR2(50)        NOT NULL
);
ALTER TABLE NVTOPDOMAIN ADD CONSTRAINT PK_NVTOPDOMAIN PRIMARY KEY (DOMAIN_NM);

-- 메시지발신자정보
CREATE TABLE NVUSERMAILINFO (
    USER_ID                   VARCHAR2(15)        NOT NULL,
    SEQ_NO                    NUMERIC(5)          NOT NULL,
    SENDER_NM                 VARCHAR2(50),
    SENDER_EMAIL              VARCHAR2(100),
    RECEIVER_NM               VARCHAR2(50),
    SENDER_TEL                VARCHAR2(50),
    SENDER_FAX                VARCHAR2(50),
    RECEIVER_FAX              VARCHAR2(50),
    RETMAIL_RECEIVER          VARCHAR2(100)
);
ALTER TABLE NVUSERMAILINFO ADD CONSTRAINT PK_NVUSERMAILINFO PRIMARY KEY (USER_ID, SEQ_NO);

-- 대상자계층
CREATE TABLE NVSEGGENEALOGY (
    SEGMENT_NO                NUMERIC(15)         NOT NULL,
    GENEALOGY_SEQ             NUMERIC(3)          NOT NULL,
    SUPRASEGMENT_NO           NUMERIC(15)
);
ALTER TABLE NVSEGGENEALOGY ADD CONSTRAINT PK_NVSEGGENEALOGY PRIMARY KEY (SEGMENT_NO, GENEALOGY_SEQ);

-- 대상자[세그먼트]
CREATE TABLE NVSEGMENT (
    SEGMENT_NO                NUMERIC(15)         NOT NULL,
    USER_ID                   VARCHAR2(15),
    GRP_CD                    VARCHAR2(12),
    SEGMENT_NM                VARCHAR2(100),
    DBINFO_SEQ                NUMERIC(2),
    SEGMENT_DESC              VARCHAR2(250),
    SQLHEAD                   VARCHAR2(4000),
    SQLTAIL                   VARCHAR2(4000),
    SQLBODY                   VARCHAR2(4000),
    SQLFILTER                 VARCHAR2(4000),
    LASTUPDATE_DT             CHAR(8),
    SEGMENT_SIZE              NUMERIC(10),
    CRTGRP_CD                 VARCHAR2(12),
    SEGMENT_TYPE              CHAR(1),
    FILETODB_YN               CHAR(1),
    EDITOR_ID                 VARCHAR2(15),
    SHARE_YN                  CHAR(1),
    ACTIVE_YN                 CHAR(1),
    CATEGORY_CD               VARCHAR2(12),
    SEGMENT_STS               CHAR(1),
    TAG_NO                    NUMERIC(10),
    PSEGMENT_NO               NUMERIC(15),
    PLINK_SEQ                 VARCHAR2(250),
    SEG_TYPE                  CHAR(1),
    TESTQUERY                 VARCHAR2(4000),
    UPDATEQUERY               VARCHAR2(4000)
);
ALTER TABLE NVSEGMENT ADD CONSTRAINT PK_NVSEGMENT PRIMARY KEY (SEGMENT_NO);

-- 대상자태그
CREATE TABLE NVSEGMENTTAG (
    TAG_NO                    NUMERIC(10)         NOT NULL,
    TAG_NM                    VARCHAR2(50)
);
ALTER TABLE NVSEGMENTTAG ADD CONSTRAINT PK_NVSEGMENTTAG PRIMARY KEY (TAG_NO);

-- 기본대상자
CREATE TABLE NVSEGMENTCHECK (
    SEGMENT_NO                NUMERIC(15)         NOT NULL,
    USER_ID                   VARCHAR2(15)        NOT NULL
);
ALTER TABLE NVSEGMENTCHECK ADD CONSTRAINT PK_NVSEGMENTCHECK PRIMARY KEY (SEGMENT_NO, USER_ID);

-- 대상자개인화필드[시멘틱]
CREATE TABLE NVSEMANTIC (
    SEGMENT_NO                NUMERIC(15)         NOT NULL,
    FIELD_SEQ                 NUMERIC(3)          NOT NULL,
    FIELD_NM                  VARCHAR2(250)       NOT NULL,
    FIELD_DESC                VARCHAR2(250),
    FIELD_TYPE                VARCHAR2(20),
    FIELD_LENGTH              NUMERIC(5),
    INITVALUE                 VARCHAR2(250),
    MINVALUE                  VARCHAR2(250),
    MAXVALUE                  VARCHAR2(250),
    NULL_YN                   CHAR(1),
    FIELD_KEY                 CHAR(1)
);
ALTER TABLE NVSEMANTIC ADD CONSTRAINT PK_NVSEMANTIC PRIMARY KEY (SEGMENT_NO, FIELD_SEQ);

-- 템플릿
CREATE TABLE NVCONTENTS (
    CONTS_NO                  NUMERIC(8)          NOT NULL,
    GRP_CD                    VARCHAR2(12),
    CATEGORY_CD               VARCHAR2(12),
    USER_ID                   VARCHAR2(15),
    CONTS_NM                  VARCHAR2(50),
    CONTS_DESC                VARCHAR2(100),
    FILE_URL_NAME             VARCHAR2(500),
    FILE_TYPE                 CHAR(1),
    FILE_NAME                 VARCHAR2(250),
    CREATE_DT                 CHAR(8),
    CREATE_TM                 CHAR(6),
    AUTH_TYPE                 CHAR(1),
    TAG_NO                    NUMERIC(10)
);
ALTER TABLE NVCONTENTS ADD CONSTRAINT PK_NVCONTENTS PRIMARY KEY (CONTS_NO);

-- 템플릿태그
CREATE TABLE NVCONTENTSTAG (
    TAG_NO                    NUMERIC(10)         NOT NULL,
    TAG_NM                    VARCHAR2(50)
);
ALTER TABLE NVCONTENTSTAG ADD CONSTRAINT PK_NVCONTENTSTAG PRIMARY KEY (TAG_NO);

-- 모바일템플릿
CREATE TABLE NVMOBILECONTENTS (
    CONTS_NO                  NUMERIC(8)          NOT NULL,
    USER_ID                   VARCHAR2(15),
    CONTS_NM                  VARCHAR2(600),
    CONTS_DESC                VARCHAR2(100),
    FILE_PATH                 VARCHAR2(250),
    FILE_NAME                 VARCHAR2(100),
    IMAGE_URL                 VARCHAR2(500),
    IMAGE_LINK                VARCHAR2(500),
    DETOUR_FILE_PATH          VARCHAR2(250),
    DETOUR_FILE_NAME          VARCHAR2(100),
    DETOUR_PREVIEW_PATH       VARCHAR2(250),
    DETOUR_PREVIEW_NAME       VARCHAR2(100),
    FILE_TYPE                 CHAR(1),
    CREATE_DT                 CHAR(8),
    CREATE_TM                 CHAR(6),
    AUTH_TYPE                 CHAR(1),
    TAG_NO                    NUMERIC(10),
    CONTS_TXT                 VARCHAR2(4000),
    FILE_PREVIEW_PATH         VARCHAR2(250),
    FILE_PREVIEW_NAME         VARCHAR2(50),
    GRP_CD                    VARCHAR2(12),
    CATEGORY_CD               VARCHAR2(12),
    FILE_SIZE                 NUMERIC(15),
    KAKAO_SENDER_KEY          VARCHAR2(40),
    KAKAO_TMPL_CD             VARCHAR2(50),
    KAKAO_TMPL_STATUS         CHAR(1),
    KAKAO_INSP_STATUS         VARCHAR2(3),
    KAKAO_BUTTONS             VARCHAR2(4000),
    USE_YN                    CHAR(1)             DEFAULT 'Y'    NOT NULL,
    CONTENT_TYPE              VARCHAR2(10),
    UNSUBSCRIBE_CONTENT       VARCHAR2(20),
    MESSAGE_TYPE              VARCHAR2(10),
    KAKAO_TMPL_MSG_TYPE       CHAR(2),
    KAKAO_TMPL_AD             VARCHAR(240),
    KAKAO_TMPL_EX             VARCHAR(1500),
    KAKAO_SECURITY_YN         CHAR(1),
    KAKAO_EM_TYPE             VARCHAR(5),
    KAKAO_EM_TITLE            VARCHAR(150),
    KAKAO_EM_SUBTITLE         VARCHAR(150),
    KAKAO_QUICK_REPLIES       VARCHAR(4000)
);
ALTER TABLE NVMOBILECONTENTS ADD CONSTRAINT PK_NVMOBILECONTENTS PRIMARY KEY (CONTS_NO);

-- 모바일템플릿태그
CREATE TABLE NVMOBILECONTENTSTAG (
    TAG_NO                    NUMERIC(10)         NOT NULL,
    TAG_NM                    VARCHAR2(50)
);
ALTER TABLE NVMOBILECONTENTSTAG ADD CONSTRAINT PK_NVMOBILECONTENTSTAG PRIMARY KEY (TAG_NO);

-- 준실시간서비스ID
CREATE TABLE NVACCEPTSVCID (
    SVC_ID                    VARCHAR2(20)        NOT NULL,
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    SVC_DESC                  VARCHAR2(200)
);
ALTER TABLE NVACCEPTSVCID ADD CONSTRAINT PK_NVACCEPTSVCID PRIMARY KEY (SVC_ID);

-- 준실시간발송요청
CREATE TABLE NVREALTIMEACCEPT (
    SEQ                       VARCHAR2(100)       NOT NULL,
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16,0),
    LIST_SEQ                  VARCHAR2(10),
    CHANNEL                   VARCHAR2(2)         NOT NULL,
    SVC_ID                    VARCHAR2(20),
    REQ_USER_ID               VARCHAR2(50),
    REQ_DEPT_ID               VARCHAR2(50),
    REQ_DT                    VARCHAR2(8)         NOT NULL,
    REQ_TM                    VARCHAR2(6)         NOT NULL,
    TMPL_TYPE                 CHAR(1)             NOT NULL,
    RECEIVER_ID               VARCHAR2(50)        NOT NULL,
    RECEIVER_NM               VARCHAR2(50)        NOT NULL,
    RECEIVER                  VARCHAR2(100)       NOT NULL,
    SENDER_NM                 VARCHAR2(50)        NOT NULL,
    SENDER                    VARCHAR2(100)       NOT NULL,
    SUBJECT                   VARCHAR2(250),
    SEND_FG                   CHAR(1)             DEFAULT 'R',
    SECU_KEY                  VARCHAR2(13),
    SECURITY_PATH             VARCHAR2(1000),
    ERROR_MSG                 VARCHAR2(250),
    RESERVED_DATE             VARCHAR2(14),
    PREVIEW_TYPE              CHAR(1)             DEFAULT 'N'    NOT NULL,
    DATA_CNT                  NUMERIC(3),
    FILE_PATH1                VARCHAR2(250),
    FILE_PATH2                VARCHAR2(250),
    FILE_PATH3                VARCHAR2(250),
    SRFIDD                    VARCHAR2(50),
    JONMUN                    CLOB,
    SLOT1                     VARCHAR2(100),
    SLOT2                     VARCHAR2(100),
    SLOT3                     VARCHAR2(100),
    SLOT4                     VARCHAR2(100),
    SLOT5                     VARCHAR2(100),
    SLOT6                     VARCHAR2(100),
    SLOT7                     VARCHAR2(100),
    SLOT8                     VARCHAR2(100),
    SLOT9                     VARCHAR2(100),
    SLOT10                    VARCHAR2(100)
);
ALTER TABLE NVREALTIMEACCEPT ADD CONSTRAINT PK_NVREALTIMEACCEPT PRIMARY KEY (SEQ);
CREATE INDEX IDX_NVREALTIMEACCEPT_SEND_FG ON NVREALTIMEACCEPT (SEND_FG);
CREATE INDEX IDX_NVREALTIMEACCEPT_REQ_DT ON NVREALTIMEACCEPT (REQ_DT);
CREATE INDEX IDX_NVREALTIMEACCEPT_ENO_RSEQ ON NVREALTIMEACCEPT (ECARE_NO, RESULT_SEQ);

-- 준실시간발송요청데이터
CREATE TABLE NVREALTIMEACCEPTDATA (
    SEQ                       VARCHAR2(100)       NOT NULL,
    DATA_SEQ                  NUMERIC(3)          NOT NULL,
    ATTACH_YN                 CHAR(1),
    ATTACH_NAME               VARCHAR2(100),
    SECU_KEY                  VARCHAR2(13),
    DATA                      CLOB
);
ALTER TABLE NVREALTIMEACCEPTDATA ADD CONSTRAINT PK_NVREALTIMEACCEPTDATA PRIMARY KEY (SEQ, DATA_SEQ);

-- 일별발송제한
CREATE TABLE NVSENDBLOCKDATE (
    CHK_YEAR                  CHAR(4)             NOT NULL,
    BLOCK_DT                  CHAR(4)             NOT NULL,
    CHANNEL_TYPE              CHAR(1)             DEFAULT 'M'    NOT NULL
);
ALTER TABLE NVSENDBLOCKDATE ADD CONSTRAINT PK_NVSENDBLOCKDATE PRIMARY KEY (CHK_YEAR, BLOCK_DT, CHANNEL_TYPE);

-- 주말발송제한
CREATE TABLE NVSENDBLOCKDATEINFO (
    REG_YEAR                  CHAR(4)             NOT NULL,
    REG_SATURDAY_YN           CHAR(1),
    REG_SUNDAY_YN             CHAR(1)
);
ALTER TABLE NVSENDBLOCKDATEINFO ADD CONSTRAINT PK_NVSENDBLOCKDATEINFO PRIMARY KEY (REG_YEAR);

-- 캠페인Failover이력
CREATE TABLE NVCAMPAIGNINVOKEHISTORY (
    INVOKE_TIME               CHAR(12)            NOT NULL
);
ALTER TABLE NVCAMPAIGNINVOKEHISTORY ADD CONSTRAINT PK_NVCAMPAIGNINVOKEHISTORY PRIMARY KEY (INVOKE_TIME);

-- 이케어Failover이력
CREATE TABLE NVECAREINVOKEHISTORY (
    INVOKE_TIME               CHAR(12)            NOT NULL
);
ALTER TABLE NVECAREINVOKEHISTORY ADD CONSTRAINT PK_NVECAREINVOKEHISTORY PRIMARY KEY (INVOKE_TIME);

-- 코드마스터
CREATE TABLE NV_CD_MST (
    CD_CAT                    VARCHAR2(20)        NOT NULL,
    CD                        VARCHAR2(20)        NOT NULL,
    LANG                      VARCHAR2(10)        NOT NULL,
    PAR_CD_CAT                VARCHAR2(20)        NOT NULL,
    VAL                       VARCHAR2(100),
    CD_DESC                   VARCHAR2(2000),
    USE_COL                   VARCHAR2(50),
    USE_YN                    CHAR(1)             DEFAULT 'y'    NOT NULL,
    REG_DTM                   VARCHAR2(14)        DEFAULT TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')    NOT NULL,
    MOD_DTM                   VARCHAR2(14),
    CD_ORD                    NUMERIC(10)
);
ALTER TABLE NV_CD_MST ADD CONSTRAINT PK_NV_CD_MST PRIMARY KEY (CD_CAT, CD, LANG);
CREATE INDEX IDX_NV_CD_MST_PAR_CD_CAT ON NV_CD_MST (PAR_CD_CAT);

-- 부서메뉴권한
CREATE TABLE NVGRPMENUROLE (
    GRP_CD                    VARCHAR2(12)        NOT NULL,
    MENU_CD                   VARCHAR2(10)        NOT NULL,
    READ_AUTH                 CHAR(1),
    WRITE_AUTH                CHAR(1),
    EXECUTE_AUTH              CHAR(1)
);
ALTER TABLE NVGRPMENUROLE ADD CONSTRAINT PK_NVGRPMENUROLE PRIMARY KEY (GRP_CD, MENU_CD);

-- 테스트수신그룹
CREATE TABLE NV_TEST_GRP (
    TEST_GRP_CD               NUMERIC(5)          NOT NULL,
    TEST_GRP_NM               VARCHAR2(100),
    TEST_SUPRAGRP_CD          NUMERIC(5),
    USER_ID                   VARCHAR2(15),
    ACTIVE_YN                 CHAR(1)
);
ALTER TABLE NV_TEST_GRP ADD CONSTRAINT PK_NV_TEST_GRP PRIMARY KEY (TEST_GRP_CD);

-- 스케줄발송요청
CREATE TABLE NVSCHEDULEACCEPT (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    SEQ                       VARCHAR2(100)       NOT NULL,
    RESULT_SEQ                NUMERIC(16,0),
    LIST_SEQ                  VARCHAR2(10),
    CHANNEL                   VARCHAR2(2)         NOT NULL,
    SVC_ID                    VARCHAR2(20),
    REQ_USER_ID               VARCHAR2(50),
    REQ_DEPT_ID               VARCHAR2(50),
    REQ_DT                    VARCHAR2(8)         NOT NULL,
    REQ_TM                    VARCHAR2(6)         NOT NULL,
    TMPL_TYPE                 CHAR(1)             NOT NULL,
    RECEIVER_ID               VARCHAR2(50)        NOT NULL,
    RECEIVER_NM               VARCHAR2(50)        NOT NULL,
    RECEIVER                  VARCHAR2(100)       NOT NULL,
    SENDER_NM                 VARCHAR2(50)        NOT NULL,
    SENDER                    VARCHAR2(100)       NOT NULL,
    SUBJECT                   VARCHAR2(250),
    SEND_FG                   CHAR(1)             DEFAULT 'R',
    SLOT1                     VARCHAR2(100),
    SLOT2                     VARCHAR2(100),
    SECU_KEY                  VARCHAR2(13),
    SECURITY_PATH             VARCHAR2(1000),
    ERROR_MSG                 VARCHAR2(250),
    RESERVED_DATE             VARCHAR2(14),
    PREVIEW_TYPE              CHAR(1)             DEFAULT 'N',
    DATA_CNT                  NUMERIC(3),
    FILE_PATH1                VARCHAR2(250),
    FILE_PATH2                VARCHAR2(250),
    FILE_PATH3                VARCHAR2(250),
    SRFIDD                    VARCHAR2(50),
    JONMUN                    CLOB
);
ALTER TABLE NVSCHEDULEACCEPT ADD CONSTRAINT PK_NVSCHEDULEACCEPT PRIMARY KEY (ECARE_NO, SEQ);
CREATE INDEX IDX_NVSCHEDULEACCEPT_SEND_FG ON NVSCHEDULEACCEPT (SEND_FG);
CREATE INDEX IDX_NVSCHEDULEACCEPT_REQ_DT ON NVSCHEDULEACCEPT (REQ_DT);

-- 스케줄발송요청데이타
CREATE TABLE NVSCHEDULEACCEPTDATA (
    SEQ                       VARCHAR2(100)       NOT NULL,
    DATA_SEQ                  NUMERIC(3)          NOT NULL,
    ATTACH_YN                 CHAR(1),
    ATTACH_NAME               VARCHAR2(100),
    SECU_KEY                  VARCHAR2(13),
    DATA                      CLOB
);
ALTER TABLE NVSCHEDULEACCEPTDATA ADD CONSTRAINT PK_NVSCHEDULEACCEPTDATA PRIMARY KEY (SEQ, DATA_SEQ);

-- 재발송 요청
CREATE TABLE NVRESENDREQUEST (
    REQ_DT                    VARCHAR2(14)        NOT NULL,
    REQ_USER_ID               VARCHAR2(15)        NOT NULL,
    SEND_FG                   CHAR(1)             NOT NULL,
    SERVICE_NO                NUMERIC(15)         NOT NULL,
    SUPER_SEQ                 NUMERIC(16)         NOT NULL,
    RESULT_SEQ                NUMERIC(16),
    LIST_SEQ                  VARCHAR2(10),
    TARGET_KEY                VARCHAR2(100),
    TARGET_CONTACT            VARCHAR2(100),
    CLIENT                    VARCHAR(2)          NOT NULL,
    SUB_TYPE                  CHAR(1)             NOT NULL,
    CHANNEL                   CHAR(1)             NOT NULL,
    RESEND_TYPE               CHAR(1)             NOT NULL,
    RESEND_REASON             VARCHAR2(1000)
);
ALTER TABLE NVRESENDREQUEST ADD CONSTRAINT PK_NVRESENDREQUEST PRIMARY KEY (REQ_DT, REQ_USER_ID);
CREATE INDEX IDX_NVRESENDREQUEST_RDTSFG ON NVRESENDREQUEST (SEND_FG, RESULT_SEQ);

-- 재발송 데이터
CREATE TABLE NVRESENDTARGET (
    SERVICE_NO                NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR2(10)        NOT NULL,
    CLIENT                    VARCHAR(2)          NOT NULL,
    TARGET_KEY                VARCHAR2(100)       NOT NULL,
    TARGET_NM                 VARCHAR2(100),
    TARGET_CONTACT            VARCHAR2(100),
    SEND_DT                   VARCHAR2(14)        NOT NULL,
    TARGET_LST1               VARCHAR2(4000)      NOT NULL,
    TARGET_LST2               VARCHAR2(4000)
);
ALTER TABLE NVRESENDTARGET ADD CONSTRAINT PK_NVRESENDTARGET PRIMARY KEY (SERVICE_NO, RESULT_SEQ, LIST_SEQ, CLIENT);
CREATE INDEX IDX_NVRESENDTARGET_SDT ON NVRESENDTARGET (SEND_DT);

-- 분할발송스케쥴
CREATE TABLE NVDIVIDESCHEDULE (
    CLIENT                    VARCHAR(2)          NOT NULL,
    SERVICE_NO                NUMERIC(15)         NOT NULL,
    DIVIDE_SEQ                NUMERIC(2)          NOT NULL,
    TARGET_CNT                NUMERIC(10),
    START_DT                  VARCHAR(14)
);
ALTER TABLE NVDIVIDESCHEDULE ADD CONSTRAINT PK_NVDIVIDESCHEDULE PRIMARY KEY (CLIENT, SERVICE_NO, DIVIDE_SEQ);
CREATE INDEX IDX_NVDIVIDESCHEDULE_START_DT ON NVDIVIDESCHEDULE(START_DT);

-- LTS분할발송서비스정보
CREATE TABLE NV_SVC_DIVIDE (
    TASK_ID                   VARCHAR(31)         NOT NULL,
    SERVER_ID                 VARCHAR(10)         NOT NULL,
    DIVIDE_SEQ                NUMERIC(2)          NOT NULL,
    CLIENT                    VARCHAR(2),
    SERVICE_NO                NUMERIC(15),
    TARGET_CNT                NUMERIC(10),
    SEND_CNT                  NUMERIC(10),
    START_DT                  VARCHAR(14),
    END_DT                    VARCHAR(14),
    STATUS                    CHAR(1)
);
ALTER TABLE NV_SVC_DIVIDE ADD CONSTRAINT PK_NV_SVC_DIVIDE PRIMARY KEY (TASK_ID, SERVER_ID, DIVIDE_SEQ);
CREATE INDEX IDX_NV_SVC_DIVIDE_MLT1 ON NV_SVC_DIVIDE (CLIENT, SERVICE_NO, DIVIDE_SEQ);

-- 카카오 프로필 데이터
CREATE TABLE NVKAKAOPROFILE (
    USER_ID                   VARCHAR2(15)        NOT NULL,
    KAKAO_SENDER_KEY          VARCHAR2(40)        NOT NULL,
    KAKAO_YELLOW_ID           VARCHAR2(50)        NOT NULL,
    LAST_SYNC_DTM             VARCHAR2(14)
);
ALTER TABLE NVKAKAOPROFILE ADD CONSTRAINT PK_NVKAKAOPROFILE PRIMARY KEY (USER_ID, KAKAO_SENDER_KEY);

-- PUSH서비스정보
CREATE TABLE NVPUSHSERVICEINFO (
    SVC_TYPE                  VARCHAR2(1)         NOT NULL,
    SVC_NO                    NUMERIC(15)         NOT NULL,
    PUSH_APP_ID               VARCHAR2(20)        NOT NULL,
    PUSH_MSG_TYPE             VARCHAR2(2),
    PUSH_MENU_ID              VARCHAR2(6),
    PUSH_POP_IMG_USE          VARCHAR2(1)         DEFAULT 'N'    NOT NULL,
    PUSH_IMG_URL              VARCHAR2(500),
    PUSH_POP_BIG_IMG_USE      VARCHAR2(1)         DEFAULT 'N'    NOT NULL,
    PUSH_BIG_IMG_URL          VARCHAR2(500),
    PUSH_CLICK_LINK           VARCHAR2(500)
);

ALTER TABLE NVPUSHSERVICEINFO ADD CONSTRAINT IDX_NVPUSHSERVICEINFO_PK PRIMARY KEY (SVC_TYPE, SVC_NO);

--   PUSH 앱 관리
CREATE TABLE NVPUSHAPP (
    PUSH_APP_ID               VARCHAR2(20)        NOT NULL,
    PUSH_APP_NM               VARCHAR2(100)       NOT NULL,
    USE_TESTMODE              VARCHAR2(1)         DEFAULT 'N'    NOT NULL,
    USE_YN                    VARCHAR2(1)         DEFAULT 'Y'    NOT NULL,
    INS_DTS                   VARCHAR2(14),
    INS_ID                    VARCHAR2(50),
    UPD_DTS                   VARCHAR2(14),
    UPD_ID                    VARCHAR2(50)
);
ALTER TABLE NVPUSHAPP ADD CONSTRAINT IDX_NVPUSHAPP_PK PRIMARY KEY (PUSH_APP_ID);

-- 리포트 더미 테이블 생성
CREATE TABLE COPY_T (
    NO                        NUMERIC(3)              NOT NULL,
    CHR                       VARCHAR(3)          NOT NULL
);