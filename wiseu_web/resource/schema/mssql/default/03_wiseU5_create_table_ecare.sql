-- 이케어시나리오
CREATE TABLE NVECARESCENARIO (
    SCENARIO_NO               NUMERIC(15)         NOT NULL,
    USER_ID                   VARCHAR(15),
    GRP_CD                    VARCHAR(12),
    SCENARIO_NM               VARCHAR(100),
    SCENARIO_DESC             VARCHAR(250),
    SCENARIO_TYPE             CHAR(1),
    CREATE_DT                 CHAR(8),
    LASTUPDATE_DT             CHAR(8),
    FINISH_YN                 CHAR(1),
    FINISH_DT                 CHAR(8),
    CREATE_TM                 CHAR(6),
    LASTUPDATE_TM             CHAR(6),
    SERVICE_TYPE              CHAR(1),
    TAG_NO                    NUMERIC(10),
    SUB_TYPE                  CHAR(1),
    HANDLER_TYPE              CHAR(1),
    CHRG_NM                   VARCHAR(30),
    BRC_NM                    VARCHAR(30)
);
ALTER TABLE NVECARESCENARIO ADD CONSTRAINT PK_NVECARESCENARIO PRIMARY KEY (SCENARIO_NO);

-- 이케어
CREATE TABLE NVECAREMSG (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    USER_ID                   VARCHAR(15),
    GRP_CD                    VARCHAR(12),
    SEGMENT_NO                NUMERIC(15),
    ECARE_NM                  VARCHAR(100),
    ECARE_DESC                VARCHAR(250),
    ECARE_PREFACE             VARCHAR(250),
    ECARE_STS                 CHAR(1),
    CREATE_DT                 CHAR(8),
    CREATE_TM                 CHAR(6),
    LASTUPDATE_DT             CHAR(8),
    LASTUPDATE_TM             CHAR(6),
    CAMPAIGN_TYPE             CHAR(1),
    TEMPLATE_TYPE             NUMERIC(2),
    SENDING_STS               CHAR(1),
    TARGET_CNT                NUMERIC(10),
    SHARE_YN                  CHAR(1),
    MSGASSORT_CD              CHAR(1),
    LOG_YN                    CHAR(1),
    KEEPDAY                   NUMERIC(4),
    ECMSCHEDULE_NO            NUMERIC(5),
    SENDER_NM                 VARCHAR(50),
    SENDER_EMAIL              VARCHAR(100),
    SENDING_MODE              CHAR(1),
    RETRY_CNT                 NUMERIC(2),
    RECEIVER_NM               VARCHAR(50),
    SENDER_TEL                VARCHAR(50),
    RETMAIL_RECEIVER          VARCHAR(100),
    ECARE_CLASS               CHAR(1),
    RELATION_TYPE             CHAR(1)             DEFAULT 'N',
    RELATION_TREE             VARCHAR(100)        DEFAULT 'N',
    CHANNEL_TYPE              CHAR(1),
    HTMLMAKER_TYPE            CHAR(1),
    SERVICE_TYPE              CHAR(1),
    ACCOUNT_DT                CHAR(2),
    ECARE_LEVEL               CHAR(1)             DEFAULT 1,
    CATEGORY_CD               VARCHAR(12),
    RESEND_YN                 CHAR(1),
    RESEND_CNT                NUMERIC(2),
    RESEND_TM                 NUMERIC(2),
    SVC_ID                    VARCHAR(20),
    SUB_TYPE                  CHAR(1),
    SURVEY_END_YN             CHAR(1),
    SURVEY_RESPONSE_CNT       NUMERIC(10),
    SURVEY_NO                 NUMERIC(15),
    SURVEY_START_DT           CHAR(8),
    SURVEY_START_TM           CHAR(6),
    SURVEY_END_DT             CHAR(8),
    SURVEY_END_TM             CHAR(6),
    DEPTH_NO                  NUMERIC(2)          DEFAULT 1,
    EDITOR_ID                 VARCHAR(15),
    SCENARIO_NO               NUMERIC(15),
    VERIFY_YN                 CHAR(1),
    VERIFY_GRP_CD             VARCHAR(12),
    SEND_SERVER               NUMERIC(1),
    BLOCK_YN                  CHAR(1),
    VERIFY_B4_SEND            CHAR(1)             DEFAULT 'N',
    DELETE_YN                 CHAR(1),
    SECURITY_MAIL_YN          CHAR(1)             DEFAULT 'N',
    REQ_DEPT_ID               VARCHAR(50),
    REQ_USER_ID               VARCHAR(15),
    RESEND_ECARE_NO           NUMERIC(15),
    TEMPLATE_SENDER_KEY       VARCHAR(40),
    KAKAO_SENDER_KEY          VARCHAR(40),
    KAKAO_TMPL_CD             VARCHAR(50),
    KAKAO_IMAGE_NO            NUMERIC(15)         DEFAULT 0,
    FAILBACK_SEND_YN          CHAR(1)             DEFAULT 'N',
    FAILBACK_SUBJECT          VARCHAR(100),
    TMPL_VER                  NUMERIC(5)          DEFAULT 0    NOT NULL,
    COVER_VER                 NUMERIC(5)          DEFAULT 0    NOT NULL,
    HANDLER_VER               NUMERIC(5)          DEFAULT 0    NOT NULL,
    PREFACE_VER               NUMERIC(5)          DEFAULT 0    NOT NULL,
    HANDLER_SEQ               NUMERIC(10),
    DEPLOY_TYPE               VARCHAR(10),
    MAIL_TYPE                 VARCHAR(10) DEFAULT 'NONE',
    OPEN_TYPE                VARCHAR(10),
    SLOT1_FIELD               VARCHAR(30),
    SLOT2_FIELD               VARCHAR(30),
    SLOT3_FIELD               VARCHAR(30),
    SLOT4_FIELD               VARCHAR(30),
    SLOT5_FIELD               VARCHAR(30),
    SLOT6_FIELD               VARCHAR(30),
    SLOT7_FIELD               VARCHAR(30),
    SLOT8_FIELD               VARCHAR(30),
    SLOT9_FIELD               VARCHAR(30),
    SLOT10_FIELD              VARCHAR(30)
);
ALTER TABLE NVECAREMSG ADD CONSTRAINT PK_NVECAREMSG PRIMARY KEY (ECARE_NO);

-- 이케어태그
CREATE TABLE NVECAREMSGTAG (
    TAG_NO                    NUMERIC(10)         NOT NULL,
    TAG_NM                    VARCHAR(50)
);
ALTER TABLE NVECAREMSGTAG ADD CONSTRAINT PK_NVECAREMSGTAG PRIMARY KEY (TAG_NO);

-- 이케어실시간발송맵
CREATE TABLE NVECAREKMMAP (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    ITEMFIELD_NM              VARCHAR(50)         NOT NULL,
    KNOWLEDGEMAP_ID           VARCHAR(12)         NOT NULL,
    GRP_CD                    VARCHAR(12),
    USER_ID                   VARCHAR(15),
    ITEM_CD                   CHAR(2),
    ITEM_NM                   VARCHAR(50),
    ITEMINDENT                NUMERIC(2),
    ITEM_LENGTH               NUMERIC(5),
    ITEM_TYPE                 VARCHAR(50),
    ITEM_FORMAT               VARCHAR(50),
    QUERY_SEQ                 NUMERIC(2),
    ITEM_PRAM_VALUE           VARCHAR(100)
);
ALTER TABLE NVECAREKMMAP ADD CONSTRAINT PK_NVECAREKMMAP PRIMARY KEY (ECARE_NO, ITEMFIELD_NM);

-- 이케어스케쥴일정
CREATE TABLE NVCYCLEITEM (
    ECMSCHEDULE_NO            NUMERIC(5)          NOT NULL,
    CYCLEITEM                 VARCHAR(10)         NOT NULL,
    CHECK_YN                  CHAR(1)
);
ALTER TABLE NVCYCLEITEM ADD CONSTRAINT PK_NVCYCLEITEM PRIMARY KEY (ECMSCHEDULE_NO, CYCLEITEM);

-- 이케어스케쥴
CREATE TABLE NVECMSCHEDULE (
    ECMSCHEDULE_NO            NUMERIC(5)          NOT NULL,
    ECMSCHEDULE_NM            VARCHAR(50),
    CYCLE_CD                  CHAR(1),
    SENDSTART_DT              CHAR(8),
    SENDEND_DT                CHAR(8),
    INVOKE_TM                 CHAR(6),
    INVOKE_EVERY_MIN          CHAR(1),
    DAY                       NUMERIC(2),
    SCHE_WEEKNUMBER           NUMERIC(1),
    WEEKDAY                   VARCHAR(10),
    START_TM                  CHAR(6),
    END_TM                    CHAR(6),
    TERM_MIN                  CHAR(2)
);
ALTER TABLE NVECMSCHEDULE ADD CONSTRAINT PK_NVECMSCHEDULE PRIMARY KEY (ECMSCHEDULE_NO);

-- 이케어핸들러
CREATE TABLE NVECMSGHANDLER (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    TYPE                      CHAR(1),
    APPSOURCE                 TEXT
);
ALTER TABLE NVECMSGHANDLER ADD CONSTRAINT PK_NVECMSGHANDLER PRIMARY KEY (ECARE_NO);

-- 이케어기본핸들러
CREATE TABLE NVECMSGHANDLERBASIC (
    SEQ                       NUMERIC(10)         NOT NULL,
    SERVICE_TYPE              CHAR(1)             NOT NULL,
    DEFAULT_YN                CHAR(1)             DEFAULT 'N'    NOT NULL,
    HANDLER_DESC              VARCHAR(250),
    APPSOURCE                 TEXT
);
ALTER TABLE NVECMSGHANDLERBASIC ADD CONSTRAINT PK_NVECMSGHANDLERBASIC PRIMARY KEY (SEQ);

-- 이케어템플릿
CREATE TABLE NVECARETEMPLATE (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    SEG                       VARCHAR(20)         NOT NULL,
    TEMPLATE                  TEXT,
    KAKAO_BUTTONS             VARCHAR(4000),
    CONTS_NO                  NUMERIC(8)
);
ALTER TABLE NVECARETEMPLATE ADD CONSTRAINT PK_NVECARETEMPLATE PRIMARY KEY (ECARE_NO, SEG);

-- 이케어반응추적기간
CREATE TABLE NVECARETRACEINFO (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    TRACE_TYPE                VARCHAR(10)         NOT NULL,
    START_DT                  CHAR(8),
    START_TM                  CHAR(6),
    END_DT                    CHAR(8),
    END_TM                    CHAR(6),
    TERM_TYPE                 CHAR(1)
);
ALTER TABLE NVECARETRACEINFO ADD CONSTRAINT PK_NVECARETRACEINFO PRIMARY KEY (ECARE_NO, TRACE_TYPE);

-- 이케어링크추적
CREATE TABLE NVECARELINKTRACE (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    LINK_SEQ                  NUMERIC(2)          NOT NULL,
    LINK_DESC                 VARCHAR(2000),
    LINK_URL                  VARCHAR(2000),
    LINK_TITLE                VARCHAR(2000)
);
ALTER TABLE NVECARELINKTRACE ADD CONSTRAINT PK_NVECARELINKTRACE PRIMARY KEY (ECARE_NO, LINK_SEQ);

-- 이케어마임로그
CREATE TABLE NVECAREMIMEINFOLOG (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    SID                       VARCHAR(5)          NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    CUSTOMER_NM               VARCHAR(100),
    CUSTOMER_EMAIL            VARCHAR(100)        NOT NULL,
    SEND_DT                   CHAR(8),
    SEND_TM                   CHAR(6),
    HANDLER_INDEX             VARCHAR(2),
    FILE_INDEX                VARCHAR(2),
    START_OFFSET              NUMERIC(15),
    END_OFFSET                NUMERIC(15),
    SLOT1                     VARCHAR(100),
    SLOT2                     VARCHAR(100),
    MIME_FULL_PATH            VARCHAR(250)
);
ALTER TABLE NVECAREMIMEINFOLOG ADD CONSTRAINT PK_NVECAREMIMEINFOLOG PRIMARY KEY (ECARE_NO, RESULT_SEQ, LIST_SEQ, RECORD_SEQ);

-- 이케어발송결과
CREATE TABLE NVECARESENDRESULT (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    RESULT_DESC               VARCHAR(250),
    RESULT_STS                CHAR(2),
    START_DT                  CHAR(8),
    START_TM                  CHAR(6),
    END_DT                    CHAR(8),
    END_TM                    CHAR(6),
    TARGET_CNT                NUMERIC(10),
    SEND_CNT                  NUMERIC(10),
    SUCCESS_CNT               NUMERIC(10),
    FAIL_CNT                  NUMERIC(10),
    UNKNOWN_USER_CNT          NUMERIC(10),
    UNKNOWN_HOST_CNT          NUMERIC(10),
    SMTP_EXCEPT_CNT           NUMERIC(10),
    NO_ROUTE_CNT              NUMERIC(10),
    REFUSED_CNT               NUMERIC(10),
    ETC_EXCEPT_CNT            NUMERIC(10),
    INVALID_ADDRESS_CNT       NUMERIC(10),
    CRTGRP_CD                 VARCHAR(12),
    EDITOR_ID                 VARCHAR(15),
    LOG_STS                   CHAR(2),
    SUPER_SEQ                 NUMERIC(16),
    RESEND_RETRY_CNT          NUMERIC(2),
    RESEND_STS                CHAR(1),
    RETURNMAIL_CNT            NUMERIC(10)         DEFAULT 0,
    MANUALBATCH_STS           CHAR(1),
    TMPL_VER                  NUMERIC(5)          DEFAULT 0    NOT NULL,
    COVER_VER                 NUMERIC(5)          DEFAULT 0    NOT NULL,
    PREFACE_VER               NUMERIC(5)          DEFAULT 0    NOT NULL,
    HANDLER_VER               NUMERIC(5)          DEFAULT 0    NOT NULL
);
ALTER TABLE NVECARESENDRESULT ADD CONSTRAINT PK_NVECARESENDRESULT PRIMARY KEY (ECARE_NO, RESULT_SEQ);
CREATE INDEX IDX_NVECARESENDRESULT_MLT1 ON NVECARESENDRESULT (ECARE_NO, LOG_STS, RESULT_STS, START_DT);
CREATE INDEX IDX_NVECARESENDRESULT_SDT_ENO ON NVECARESENDRESULT (START_DT, ECARE_NO);
CREATE INDEX IDX_NVECARESENDRESULT_EDT_ENO ON NVECARESENDRESULT (END_DT, ECARE_NO);
CREATE INDEX IDX_NVECARESENDRESULT_MANUBAT ON NVECARESENDRESULT (MANUALBATCH_STS);

-- 이케어발송로그
CREATE TABLE NVECARESENDLOG (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    SID                       VARCHAR(5)          NOT NULL,
    CUSTOMER_NM               VARCHAR(100),
    CUSTOMER_EMAIL            VARCHAR(100)        NOT NULL,
    SEND_DT                   CHAR(8),
    SEND_TM                   CHAR(6),
    END_DT                    CHAR(8),
    END_TM                    CHAR(6),
    ERROR_CD                  VARCHAR(5),
    SEND_DOMAIN               VARCHAR(50),
    ERR_MSG                   VARCHAR(250),
    RESEND_YN                 CHAR(1),
    REQ_DEPT_ID               VARCHAR(50),
    REQ_USER_ID               VARCHAR(50),
    MESSAGE_KEY               VARCHAR(20),
    SEQ                       VARCHAR(100),
    SRFIDD                    VARCHAR(50),
    OPEN_DT                   VARCHAR(14),
    EAI_SEND_FG               VARCHAR(1)         DEFAULT 'N' NOT NULL,
    SUB_ECARE_NO              NUMERIC(15)        DEFAULT 0,
    SUB_RESULT_SEQ            NUMERIC(16)        DEFAULT 0,
    PART_MESSAGE              VARCHAR(500),
    FAIL_BACK_CHANNEL         VARCHAR(1),
    FAIL_BACK_RESULT_CD       VARCHAR(5),
    FAIL_BACK_SENDDTM         VARCHAR(14),
    TMPL_CD                   VARCHAR(50),
    SLOT1                     VARCHAR(100),
    SLOT2                     VARCHAR(100),
    SLOT3                     VARCHAR(100),
    SLOT4                     VARCHAR(100),
    SLOT5                     VARCHAR(100),
    SLOT6                     VARCHAR(100),
    SLOT7                     VARCHAR(100),
    SLOT8                     VARCHAR(100),
    SLOT9                     VARCHAR(100),
    SLOT10                    VARCHAR(100)
);
ALTER TABLE NVECARESENDLOG ADD CONSTRAINT PK_NVECARESENDLOG PRIMARY KEY (ECARE_NO, RESULT_SEQ, LIST_SEQ, CUSTOMER_KEY, RECORD_SEQ);
CREATE INDEX IDX_NVECARESENDLOG_SDT_CKY ON NVECARESENDLOG (SEND_DT, CUSTOMER_KEY);
CREATE INDEX IDX_NVECARESENDLOG_SDT_CML ON NVECARESENDLOG (SEND_DT, CUSTOMER_EMAIL);
CREATE INDEX IDX_NVECARESENDLOG_SDT_ENO ON NVECARESENDLOG (SEND_DT, ECARE_NO);
CREATE INDEX IDX_NVECARESENDLOG_SEQ ON NVECARESENDLOG (SEQ);
CREATE INDEX IDX_NVECARESENDLOG_MESSAGE_KEY ON NVECARESENDLOG (MESSAGE_KEY);
CREATE INDEX IDX_NVECARESENDLOG_SDT_EFG  ON NVECARESENDLOG (SEND_DT, EAI_SEND_FG);
CREATE INDEX IDX_NVECARESENDLOG_SUB1_SUB2 ON NVECARESENDLOG (SUB_RESULT_SEQ, SUB_ECARE_NO);

-- 이케어테스트발송로그
CREATE TABLE NVTESTECARESENDLOG (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    CUSTOMER_EMAIL            VARCHAR(100)        NOT NULL,
    CUSTOMER_NM               VARCHAR(100),
    SID                       VARCHAR(5)          NOT NULL,
    SEND_DT                   CHAR(8),
    SEND_TM                   CHAR(6),
    SEND_DOMAIN               VARCHAR(50),
    ERROR_CD                  VARCHAR(5),
    ERR_MSG                   VARCHAR(250),
    RESEND_YN                 CHAR(1),
    REQ_DEPT_ID               VARCHAR(50),
    REQ_USER_ID               VARCHAR(50),
    MESSAGE_KEY               VARCHAR(20),
    SEQ                       VARCHAR(200),
    PART_MESSAGE              VARCHAR(500),
    TMPL_CD                   VARCHAR(50),
    SLOT1                     VARCHAR(100),
    SLOT2                     VARCHAR(100),
    SLOT3                     VARCHAR(100),
    SLOT4                     VARCHAR(100),
    SLOT5                     VARCHAR(100),
    SLOT6                     VARCHAR(100),
    SLOT7                     VARCHAR(100),
    SLOT8                     VARCHAR(100),
    SLOT9                     VARCHAR(100),
    SLOT10                    VARCHAR(100)
);
ALTER TABLE NVTESTECARESENDLOG ADD CONSTRAINT PK_NVTESTECARESENDLOG PRIMARY KEY (ECARE_NO, RESULT_SEQ, LIST_SEQ, CUSTOMER_KEY, RECORD_SEQ);
CREATE INDEX IDX_NVTESTECARESENDLOG_SEND_DT ON NVTESTECARESENDLOG (SEND_DT);

-- 이케어발송결과상세
CREATE TABLE NVECARESENDRESULTDETAIL (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    ERROR_CD                  VARCHAR(4)          NOT NULL,
    CNT                       NUMERIC(10),
    CHANNEL_TYPE              CHAR(1)
);
ALTER TABLE NVECARESENDRESULTDETAIL ADD CONSTRAINT PK_NVECARESENDRESULTDETAIL PRIMARY KEY (ECARE_NO, RESULT_SEQ, ERROR_CD);

-- 이케어첨부파일
CREATE TABLE NVECAREMULTIPARTFILE (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    SEQ                       NUMERIC(10)         NOT NULL,
    FILE_ALIAS                VARCHAR(100)        NOT NULL,
    FILE_PATH                 VARCHAR(250)        NOT NULL,
    FILE_SIZE                 NUMERIC(15)         NOT NULL,
    FILE_NAME                 VARCHAR(100)        NOT NULL,
    FILE_TYPE                 VARCHAR(10)         NOT NULL,
    ENC_YN                    VARCHAR(1)          DEFAULT 'N' NOT NULL,
    SECU_FIELD                VARCHAR(30)
);
ALTER TABLE NVECAREMULTIPARTFILE ADD CONSTRAINT PK_NVECAREMLTPARTFILE PRIMARY KEY (ECARE_NO, SEQ, FILE_ALIAS);

-- 이케어핸들러이력
CREATE TABLE NVECMSGHANDLERHISTORY (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    USER_ID                   VARCHAR(15)         NOT NULL,
    LASTUPDATE_DT             CHAR(8),
    LASTUPDATE_TM             VARCHAR(9),
    TYPE                      CHAR(1)             NOT NULL,
    APPSOURCE                 TEXT,
    HISTORY_MSG               VARCHAR(1000),
    HANDLER_VER               NUMERIC(5)          DEFAULT 0    NOT NULL
);
CREATE INDEX IDX_NVECMSGHANDLERHISTORY_ENO ON NVECMSGHANDLERHISTORY (ECARE_NO);
ALTER TABLE NVECMSGHANDLERHISTORY ADD CONSTRAINT PK_NVECMSGHANDLERHISTORY PRIMARY KEY (ECARE_NO, HANDLER_VER);

-- 이케어템플릿이력
CREATE TABLE NVECARETEMPLATEHISTORY (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    USER_ID                   VARCHAR(15)         NOT NULL,
    LASTUPDATE_DT             CHAR(8),
    LASTUPDATE_TM             VARCHAR(9),
    SEG                       VARCHAR(20)         NOT NULL,
    TEMPLATE                  TEXT,
    HISTORY_MSG               VARCHAR(1000),
    TMPL_VER                  NUMERIC(5)          DEFAULT 0    NOT NULL,
    KAKAO_BUTTONS             VARCHAR(4000),
    CONTS_NO                  NUMERIC(8)
);
CREATE INDEX IDX_NVECARETEMPLATEHISTORY_ENO ON NVECARETEMPLATEHISTORY (ECARE_NO);
ALTER TABLE NVECARETEMPLATEHISTORY ADD CONSTRAINT PK_NVECARETEMPLATEHISTORY PRIMARY KEY (ECARE_NO, SEG, TMPL_VER);

-- 부가데이터 쿼리 테이블
CREATE TABLE NVADDQUERY (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    QUERY_SEQ                 NUMERIC(2)          NOT NULL,
    QUERY_TYPE                VARCHAR(10)         NOT NULL,
    EXECUTE_TYPE              VARCHAR(10)         NOT NULL,
    RESULT_ID                 VARCHAR(10),
    DBINFO_SEQ                NUMERIC(2)          NOT NULL,
    QUERY                     VARCHAR(4000)       NOT NULL
);
ALTER TABLE NVADDQUERY ADD CONSTRAINT PK_NVADDQUERY PRIMARY KEY (ECARE_NO, QUERY_SEQ);