-- 서비스트랜잭션
CREATE TABLE NV_SVC_TRANS (
    T_ID                      VARCHAR2(31)        NOT NULL,
    SERVER_ID                 VARCHAR2(10)        NOT NULL,
    CREATE_TM                 VARCHAR2(17),
    PRC_FLAG                  CHAR(1)
);
ALTER TABLE NV_SVC_TRANS ADD CONSTRAINT PK_NV_SVC_TRANS PRIMARY KEY (T_ID, SERVER_ID);

-- LTS서비스정보
CREATE TABLE NV_SVC_MAIN (
    T_ID                      VARCHAR2(31)        NOT NULL,
    SERVER_ID                 VARCHAR2(10)        NOT NULL,
    START_TM                  VARCHAR2(14),
    END_TM                    VARCHAR2(14),
    SERVICE_STS               VARCHAR2(2),
    SERVICE_NM                VARCHAR2(100),
    SEND_MODE                 CHAR(1),
    SERVICE_TYPE              CHAR(1),
    CLIENT                    VARCHAR2(2),
    USER_ID                   VARCHAR2(20),
    MTS_CNT                   NUMERIC(2),
    TARGET_CNT                NUMERIC(10),
    CHANNEL                   CHAR(1),
    ETC_INFO                  VARCHAR2(500),
    ERR_MSG                   VARCHAR2(500),
    DISPLAY_YN                VARCHAR2(1),
    STATUS                    VARCHAR2(2),
    DEL_YN                    CHAR(1),
    RTN_MAIL_CNT              NUMERIC(10),
    RCV_CNFM_EM               NUMERIC(10),
    LNK_TRC_EM                NUMERIC(10),
    RCV_CNFM_EC               NUMERIC(10),
    RCV_TRC_EC                NUMERIC(10),
    RCV_USR_DEF               NUMERIC(10),
    UPDATE_TM                 VARCHAR2(14),
    CREATE_TM                 VARCHAR2(14)
);
ALTER TABLE NV_SVC_MAIN ADD CONSTRAINT PK_NV_SVC_MAIN PRIMARY KEY (T_ID, SERVER_ID);

-- MTS서비스정보
CREATE TABLE NV_SVC_MTS (
    T_ID                      VARCHAR2(31)        NOT NULL,
    SERVER_ID                 VARCHAR2(10)        NOT NULL,
    START_TM                  VARCHAR2(14),
    END_TM                    VARCHAR2(14),
    SERVICE_STS               VARCHAR2(2),
    TOT_CNT                   NUMERIC(10),
    MADE_CNT                  NUMERIC(10),
    MAX_RETRY                 NUMERIC(10),
    SEND_CNT                  NUMERIC(10),
    SUCCESS_CNT               NUMERIC(10),
    UNKNOWN_USER_CNT          NUMERIC(10),
    UNKNOWN_HOST_CNT          NUMERIC(10),
    SMTP_EXCEPT_CNT           NUMERIC(10),
    NO_ROUTE_CNT              NUMERIC(10),
    REFUSED_CNT               NUMERIC(10),
    ETC_EXCEPT_CNT            NUMERIC(10),
    INVALID_ADDR_CNT          NUMERIC(10),
    QUEUE_CNT                 NUMERIC(4),
    THREAD_CNT                NUMERIC(4),
    HANDLER_THREAD_CNT        NUMERIC(4),
    ERR_MSG                   VARCHAR2(500),
    UPDATE_TM                 VARCHAR2(14),
    CREATE_TM                 VARCHAR2(14)
);
ALTER TABLE NV_SVC_MTS ADD CONSTRAINT PK_NV_SVC_MTS PRIMARY KEY (T_ID, SERVER_ID);

-- 서버상태정보
CREATE TABLE NV_SVR_INFO (
    SERVER_ID                                     VARCHAR2(10)        NOT NULL,
    LAST_UPDATE_TM                                VARCHAR2(14),
    START_TM                                      VARCHAR2(14),
    JOB_CNT                                       NUMERIC(10),
    PROCESS_THREAD_CNT                            NUMERIC(5),
    WORK_THREAD_CNT                               NUMERIC(5),
    OPENFILE_DESC_CNT                             NUMERIC(5),
    MAX_MEMORY                                    NUMERIC(10),
    USED_MEMORY                                   NUMERIC(10),
    CPU_RATE                                      NUMERIC(10,2),
    MAX_QUEUE_SIZE                                NUMERIC(5),
    USED_QUEUE_SIZE                               NUMERIC(5),
    STATUS                                        NUMERIC(1),
    POLLING_ACT_STATUS                            CHAR(1),
    POLLING_ACT_STATUS_UPDATE_TM                  VARCHAR2(14),
    DISP_NO                                       NUMERIC(2),
    EXEC_INFO                                     VARCHAR2(500),
    CONFIG_CONT                                   CLOB
);
ALTER TABLE NV_SVR_INFO ADD CONSTRAINT PK_NV_SVR_INFO PRIMARY KEY (SERVER_ID);

-- 서버/서비스동작요청
CREATE TABLE NV_STATUS_REQ (
    REQ_KIND                  VARCHAR2(10)        NOT NULL,
    CREATE_TM                 VARCHAR2(14)        NOT NULL,
    TASK_ID                   VARCHAR2(31)        NOT NULL,
    REQ                       VARCHAR2(20),
    RESULT                    NUMERIC(1),
    UPDATE_TM                 VARCHAR2(14),
    USER_ID                   VARCHAR2(20)
);
ALTER TABLE NV_STATUS_REQ ADD CONSTRAINT PK_NV_STATUS_REQ PRIMARY KEY (REQ_KIND, CREATE_TM, TASK_ID);

-- 서버/서비스동작요청결과
CREATE TABLE NV_STATUS_REQ_RESULT (
    SERVER_ID                 VARCHAR2(10)        NOT NULL,
    REQ_CREATE_TM             VARCHAR2(14)        NOT NULL,
    TASK_ID                   VARCHAR2(31)        NOT NULL,
    REQ                       VARCHAR2(20)        NOT NULL,
    RESULT                    NUMERIC(1),
    RESULT_MSG                VARCHAR2(500),
    CREATE_TM                 VARCHAR2(14)
);
ALTER TABLE NV_STATUS_REQ_RESULT ADD CONSTRAINT PK_NV_STATUS_REQ_RESULT PRIMARY KEY (SERVER_ID, REQ_CREATE_TM, TASK_ID, REQ);