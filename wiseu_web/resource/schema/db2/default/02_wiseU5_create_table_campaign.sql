-- 캠페인시나리오
CREATE TABLE NVSCENARIO (
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
    TAG_NO                    NUMERIC(10),
    CREATE_TM                 CHAR(6),
    LASTUPDATE_TM             CHAR(6),
    HANDLER_TYPE              CHAR(1)             DEFAULT 'G'
);
ALTER TABLE NVSCENARIO ADD CONSTRAINT PK_NVSCENARIO PRIMARY KEY (SCENARIO_NO);

-- 캠페인
CREATE TABLE NVCAMPAIGN (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    GRP_CD                    VARCHAR(12),
    USER_ID                   VARCHAR(15),
    SEGMENT_NO                NUMERIC(15),
    CAMPAIGN_NM               VARCHAR(100),
    CAMPAIGN_DESC             VARCHAR(250),
    CAMPAIGN_PREFACE          VARCHAR(250),
    CAMPAIGN_STS              CHAR(1),
    CREATE_DT                 CHAR(8),
    CREATE_TM                 CHAR(6),
    LASTUPDATE_DT             CHAR(8),
    LASTUPDATE_TM             CHAR(6),
    CAMPAIGN_TYPE             CHAR(1),
    TEMPLATE_TYPE             NUMERIC(2),
    SENDING_STS               CHAR(1),
    SENDING_CYCLE             CHAR(20),
    SENDSTART_DT              CHAR(8),
    SENDSTART_TM              CHAR(6),
    SENDFINISH_DT             CHAR(8),
    SENDFINISH_TM             CHAR(6),
    TARGET_CNT                NUMERIC(10),
    SURVEY_END_YN             CHAR(1),
    SENDING_MODE              CHAR(1),
    SURVEY_RESPONSE_CNT       NUMERIC(10),
    SURVEY_NO                 NUMERIC(15),
    LOG_YN                    CHAR(1),
    KEEPDAY                   NUMERIC(4),
    SHARE_YN                  CHAR(1),
    SURVEY_START_DT           CHAR(8),
    SURVEY_START_TM           CHAR(6),
    SURVEY_END_DT             CHAR(8),
    SURVEY_END_TM             CHAR(6),
    RETRY_CNT                 NUMERIC(2),
    RPTCREATE_DT              CHAR(8),
    SENDER_NM                 VARCHAR(50),
    SENDER_EMAIL              VARCHAR(100),
    RECEIVER_NM               VARCHAR(50),
    SENDER_TEL                VARCHAR(50),
    RETMAIL_RECEIVER          VARCHAR(100),
    HTMLUPDATE_YN             CHAR(1),
    REPORT_STS                CHAR(1),
    CAMPAIGN_CLASS            VARCHAR(10),
    CHANNEL_TYPE              CHAR(1),
    RELATION_TYPE             CHAR(1)             DEFAULT 'N',
    RELATION_TREE             VARCHAR(100)        DEFAULT 'N',
    RETMAIL_SEND_YN           CHAR(1),
    ETC_INFO1                 VARCHAR(250),
    ETC_INFO2                 VARCHAR(250),
    PROMOTION_TYPE            CHAR(1),
    CAMPAIGN_LEVEL            CHAR(1)             DEFAULT 0,
    CATEGORY_CD               VARCHAR(12),
    SCENARIO_NO               NUMERIC(15),
    DEPTH_NO                  NUMERIC(2)          DEFAULT 1,
    EDITOR_ID                 VARCHAR(15),
    SEND_SERVER               NUMERIC(1),
    APPROVAL_STS              CHAR(1),
    REQ_DEPT_ID               VARCHAR(50),
    REQ_USER_ID               VARCHAR(15),
    DIVIDE_YN                 CHAR(1)             DEFAULT 'N',
    DIVIDE_INTERVAL           NUMERIC(8),
    DIVIDE_CNT                NUMERIC(10),
    AB_TEST_TYPE              CHAR(1)             DEFAULT 'N',
    AB_TEST_COND              CHAR(1),
    AB_TEST_RATE              NUMERIC(3),
    CAMPAIGN_PREFACE_AB       VARCHAR(250),
    MULTI_CONT_INFO           CHAR(10),
    HANDLER_SEQ               NUMERIC(10),
    KAKAO_SENDER_KEY          VARCHAR(40),
    KAKAO_TMPL_CD             VARCHAR(50),
    KAKAO_IMAGE_NO            NUMERIC(15,0)       DEFAULT 0,
    FAILBACK_SEND_YN          CHAR(1)             DEFAULT 'N',
    FAILBACK_SUBJECT          VARCHAR(100)
);
ALTER TABLE NVCAMPAIGN ADD CONSTRAINT PK_NVCAMPAIGN PRIMARY KEY (CAMPAIGN_NO);
CREATE INDEX IDX_NVCAMPAIGN_SCENARIO_NO ON NVCAMPAIGN (SCENARIO_NO);

-- 캠페인태그
CREATE TABLE NVCAMPTAG (
    TAG_NO                    NUMERIC(10)         NOT NULL,
    TAG_NM                    VARCHAR(50)
);
ALTER TABLE NVCAMPTAG ADD CONSTRAINT PK_NVCAMPTAG PRIMARY KEY (TAG_NO);

-- 캠페인스케쥴
CREATE TABLE NVSCHEDULE (
    SCENARIO_NO               NUMERIC(15)         NOT NULL,
    SCHEDULE_SEQ              NUMERIC(15)         NOT NULL,
    CAMPAIGN_NO               NUMERIC(15),
    SUPRACAMPAIGN_NO          NUMERIC(15),
    CAMPAIGN_DT               CHAR(8),
    POSITION_X                NUMERIC(5),
    POSITION_Y                NUMERIC(5),
    SIZEWIDTH                 NUMERIC(10),
    SIZEHEIGHT                NUMERIC(10),
    DURATION_TM               NUMERIC(5),
    RECEIPT_YN                CHAR(1),
    FROMSQL                   VARCHAR(1000),
    WHERESQL                  VARCHAR(1000)
);
ALTER TABLE NVSCHEDULE ADD CONSTRAINT PK_NVSCHEDULE PRIMARY KEY (SCENARIO_NO, SCHEDULE_SEQ);

-- 캠페인핸들러
CREATE TABLE NVAPPLICATION (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    TYPE                      CHAR(1),
    APPSOURCE                 CLOB(5M)            NOT LOGGED
);
ALTER TABLE NVAPPLICATION ADD CONSTRAINT PK_NVAPPLICATION PRIMARY KEY (CAMPAIGN_NO);

-- 캠페인템플릿
CREATE TABLE NVTEMPLATE (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    SEG                       VARCHAR(20)         NOT NULL,
    TEMPLATE                  CLOB(5M)            NOT LOGGED,
    KAKAO_BUTTONS             VARCHAR(4000)
);
ALTER TABLE NVTEMPLATE ADD CONSTRAINT PK_NVTEMPLATE PRIMARY KEY (CAMPAIGN_NO, SEG);

-- 캠페인첨부파일
CREATE TABLE NVMULTIPARTFILE (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    SEQ                       NUMERIC(10)         NOT NULL,
    FILE_ALIAS                VARCHAR(100)        NOT NULL,
    FILE_PATH                 VARCHAR(250)        NOT NULL,
    FILE_SIZE                 NUMERIC(15)         NOT NULL,
    FILE_NAME                 VARCHAR(100)        NOT NULL
);
ALTER TABLE NVMULTIPARTFILE ADD CONSTRAINT PK_NVMLTPARTFILE PRIMARY KEY (CAMPAIGN_NO, SEQ, FILE_ALIAS);

-- 캠페인반응추적기간
CREATE TABLE NVTRACEINFO (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    TRACE_TYPE                VARCHAR(10)         NOT NULL,
    START_DT                  CHAR(8),
    START_TM                  CHAR(6),
    END_DT                    CHAR(8),
    END_TM                    CHAR(6)
);
ALTER TABLE NVTRACEINFO ADD CONSTRAINT PK_NVTRACEINFO PRIMARY KEY (CAMPAIGN_NO, TRACE_TYPE);

-- 링크조건
CREATE TABLE NVLINKCONDITION (
    SCENARIO_NO               NUMERIC(15)         NOT NULL,
    SCHEDULE_SEQ              NUMERIC(15)         NOT NULL,
    LINK_SEQ                  NUMERIC(3)          NOT NULL,
    TRACKING_YN               CHAR(1)
);
ALTER TABLE NVLINKCONDITION ADD CONSTRAINT PK_NVLINKCONDITION PRIMARY KEY (SCENARIO_NO, SCHEDULE_SEQ, LINK_SEQ);

-- 캠페인링크추적
CREATE TABLE NVLINKTRACE (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    LINK_SEQ                  NUMERIC(3)          NOT NULL,
    LINK_URL                  VARCHAR(2000),
    LINK_DESC                 VARCHAR(2000),
    LINK_TITLE                VARCHAR(2000)
);
ALTER TABLE NVLINKTRACE ADD CONSTRAINT PK_NVLINKTRACE PRIMARY KEY (CAMPAIGN_NO, LINK_SEQ);

-- 캠페인마임로그
CREATE TABLE NVMIMEINFOLOG (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    CUSTOMER_NM               VARCHAR(100),
    CUSTOMER_EMAIL            VARCHAR(100)        NOT NULL,
    SID                       VARCHAR(5)          NOT NULL,
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
ALTER TABLE NVMIMEINFOLOG ADD CONSTRAINT PK_NVMIMEINFOLOG PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, CUSTOMER_KEY, LIST_SEQ, RECORD_SEQ);

-- 캠페인발송결과
CREATE TABLE NVSENDRESULT (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
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
    LOG_STS                   CHAR(2),
    DOMAIN_STS                CHAR(2),
    RETURNMAIL_CNT            NUMERIC(10)         DEFAULT 0,
    OPEN_CNT                  NUMERIC(10)         DEFAULT 0,
    DURATION_CNT              NUMERIC(10)         DEFAULT 0,
    LINK_CNT                  NUMERIC(10)         DEFAULT 0,
    MANUALBATCH_STS           CHAR(1),
    RESEND_STS                CHAR(1)             DEFAULT NULL,
    SUPER_SEQ                 NUMERIC(16)
);
ALTER TABLE NVSENDRESULT ADD CONSTRAINT PK_NVSENDRESULT PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ);

-- 캠페인발송로그
CREATE TABLE NVSENDLOG (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    CUSTOMER_NM               VARCHAR(100),
    CUSTOMER_EMAIL            VARCHAR(100)        NOT NULL,
    SID                       VARCHAR(5)          NOT NULL,
    SEND_DT                   CHAR(8),
    SEND_TM                   CHAR(6),
    END_DT                    CHAR(8),
    END_TM                    CHAR(6),
    ERROR_CD                  VARCHAR(5),
    SEND_DOMAIN               VARCHAR(50),
    ERR_MSG                   VARCHAR(250),
    SLOT1                     VARCHAR(100),
    SLOT2                     VARCHAR(100),
    RESEND_YN                 CHAR(1),
    REQ_DEPT_ID               VARCHAR(50),
    REQ_USER_ID               VARCHAR(50),
    MESSAGE_KEY               VARCHAR(20),
    SEQ                       VARCHAR(100),
    SRFIDD                    VARCHAR(50),
    OPEN_DT                   VARCHAR(14),
    EAI_SEND_FG               VARCHAR(1)          DEFAULT 'N' NOT NULL,
    PART_MESSAGE                   VARCHAR(500),
    FAIL_BACK_CHANNEL                   VARCHAR(1),
    FAIL_BACK_RESULT_CD                   VARCHAR(5),
    FAIL_BACK_SENDDTM                   VARCHAR(14),
    TMPL_CD                   VARCHAR(50)
);
ALTER TABLE NVSENDLOG ADD CONSTRAINT PK_NVSENDLOG PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, LIST_SEQ, CUSTOMER_KEY, RECORD_SEQ);
CREATE INDEX IDX_NVSENDLOG_SDT_CKY ON NVSENDLOG (SEND_DT, CUSTOMER_KEY);
CREATE INDEX IDX_NVSENDLOG_SDT_CML ON NVSENDLOG (SEND_DT, CUSTOMER_EMAIL);
CREATE INDEX IDX_NVSENDLOG_SEQ ON NVSENDLOG (SEQ);
CREATE INDEX IDX_NVSENDLOG_MESSAGE_KEY ON NVSENDLOG (MESSAGE_KEY);
CREATE INDEX IDX_NVSENDLOG_CNO_ECD ON NVSENDLOG (CAMPAIGN_NO, ERROR_CD);
CREATE INDEX IDX_NVSENDLOG_SDT_EFG ON NVSENDLOG (SEND_DT, EAI_SEND_FG);

-- 캠페인테스트발송로그
CREATE TABLE NVTESTSENDLOG (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    CUSTOMER_NM               VARCHAR(100),
    CUSTOMER_EMAIL            VARCHAR(100)        NOT NULL,
    SID                       VARCHAR(5)          NOT NULL,
    SEND_DT                   CHAR(8),
    SEND_TM                   CHAR(6),
    ERROR_CD                  VARCHAR(5),
    SEND_DOMAIN               VARCHAR(50),
    ERR_MSG                   VARCHAR(250),
    SLOT1                     VARCHAR(100),
    SLOT2                     VARCHAR(100),
    RESEND_YN                 CHAR(1),
    REQ_DEPT_ID               VARCHAR(50),
    REQ_USER_ID               VARCHAR(50),
    MESSAGE_KEY               VARCHAR(20),
    SEQ                       VARCHAR(200),
    PART_MESSAGE                   VARCHAR(500),
    TMPL_CD                   VARCHAR(50)
);
ALTER TABLE NVTESTSENDLOG ADD CONSTRAINT PK_NVTESTSENDLOG PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, LIST_SEQ, CUSTOMER_KEY, RECORD_SEQ);
CREATE INDEX IDX_NVTESTSENDLOG_SEND_DT ON NVTESTSENDLOG (SEND_DT);

-- 캠페인발송결과상세
CREATE TABLE NVSENDRESULTDETAIL (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    ERROR_CD                  VARCHAR(4)          NOT NULL,
    CNT                       NUMERIC(10),
    CHANNEL_TYPE              CHAR(1)
);
ALTER TABLE NVSENDRESULTDETAIL ADD CONSTRAINT PK_NVSENDRESULTDETAIL PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, ERROR_CD);

-- 스팸로그
CREATE TABLE NV_SPAM_SENDLOG (
    CAMPAIGN_NO               NUMERIC(15)         NOT NULL,
    RESULT_SEQ                NUMERIC(16)         NOT NULL,
    LIST_SEQ                  VARCHAR(10)         NOT NULL,
    CUSTOMER_KEY              VARCHAR(50)         NOT NULL,
    RECORD_SEQ                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    SID                       VARCHAR(5)          NOT NULL,
    SEND_DOMAIN               VARCHAR(50)         NOT NULL
);
ALTER TABLE NV_SPAM_SENDLOG ADD CONSTRAINT PK_NV_SPAM_SENDLOG PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, LIST_SEQ, CUSTOMER_KEY, RECORD_SEQ);