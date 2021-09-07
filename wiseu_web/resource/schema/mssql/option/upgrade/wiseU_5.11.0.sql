-- wiseU 5.10.0 -> 5.11.0 업그레이드 시 실행

-- 재발송 요청
CREATE TABLE NVRESENDREQUEST (
    REQ_DT              VARCHAR(14)         NOT NULL,
    SEND_FG             CHAR(1)             NOT NULL,
    SERVICE_NO          NUMERIC(15)         NOT NULL,
    SUPER_SEQ           NUMERIC(16)         NOT NULL,
    RESULT_SEQ          NUMERIC(16)                 ,
    LIST_SEQ            VARCHAR(10)                 ,
    TARGET_KEY          VARCHAR(100)                ,
    TARGET_CONTACT      VARCHAR(100)                ,
    CLIENT              VARCHAR(2)          NOT NULL,
    SUB_TYPE            CHAR(1)             NOT NULL,
    CHANNEL             CHAR(1)             NOT NULL,
    RESEND_TYPE         CHAR(1)             NOT NULL,
    RESEND_REASON       VARCHAR(1000)
);
CREATE INDEX IDX_NVRESENDREQUEST_RDTSFG ON NVRESENDREQUEST (SEND_FG, RESULT_SEQ);

-- 재발송 데이터
CREATE TABLE NVRESENDTARGET (
    SERVICE_NO          NUMERIC(15)         NOT NULL,
    RESULT_SEQ          NUMERIC(16)         NOT NULL,
    LIST_SEQ            VARCHAR(10)         NOT NULL,
    CLIENT              VARCHAR(2)          NOT NULL,
    TARGET_KEY          VARCHAR(100)        NOT NULL,
    TARGET_NM           VARCHAR(100)                ,
    TARGET_CONTACT      VARCHAR(100)                ,
    SEND_DT             VARCHAR(14)         NOT NULL,
    TARGET_LST1         VARCHAR(4000)       NOT NULL,
    TARGET_LST2         VARCHAR(4000)
);
ALTER TABLE NVRESENDTARGET        ADD CONSTRAINT PK_NVRESENDTARGET PRIMARY KEY (SERVICE_NO, RESULT_SEQ, LIST_SEQ, CLIENT);
CREATE INDEX IDX_NVRESENDTARGET_SDT ON NVRESENDTARGET (SEND_DT);

ALTER TABLE NVSENDRESULT          ADD RESEND_STS CHAR(1) DEFAULT NULL; 
ALTER TABLE NVSENDRESULT          ADD SUPER_SEQ  NUMERIC(16);

ALTER TABLE NVECARESENDRESULT     ADD TMPL_VER NUMERIC(5) DEFAULT 0 NOT NULL; 
ALTER TABLE NVECARESENDRESULT     ADD COVER_VER NUMERIC(5) DEFAULT 0 NOT NULL;
ALTER TABLE NVECARESENDRESULT     ADD PREFACE_VER NUMERIC(5) DEFAULT 0 NOT NULL;
ALTER TABLE NVECARESENDRESULT     ADD HANDLER_VER NUMERIC(5) DEFAULT 0 NOT NULL;

ALTER TABLE NVSERVERINFO          ADD FAX_RESEND_ERROR_CD VARCHAR(500);
ALTER TABLE NVSERVERINFO          ADD SMS_RESEND_ERROR_CD VARCHAR(500);
ALTER TABLE NVSERVERINFO          ADD ALTALK_RESEND_ERROR_CD VARCHAR(500);

ALTER TABLE NVRPTLINKDISTINCT     ADD RESULT_SEQ NUMERIC(16) NOT NULL;
ALTER TABLE NVRPTDURATION         ADD RESULT_SEQ NUMERIC(16) NOT NULL;
ALTER TABLE NVRPTLINK             ADD RESULT_SEQ NUMERIC(16) NOT NULL;
ALTER TABLE NVRPTOPEN             ADD RESULT_SEQ NUMERIC(16) NOT NULL;

ALTER TABLE NVRPTLINKDISTINCT     DROP CONSTRAINT PK_NVRPTLINKDISTINCT;
ALTER TABLE NVRPTDURATION         DROP CONSTRAINT PK_NVRPTDURATION;
ALTER TABLE NVRPTLINK             DROP CONSTRAINT PK_NVRPTLINK;
ALTER TABLE NVRPTOPEN             DROP CONSTRAINT PK_NVRPTOPEN;

ALTER TABLE NVRPTLINKDISTINCT     ADD CONSTRAINT PK_NVRPTLINKDISTINCT PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, REPORT_DT, REPORT_TM);
ALTER TABLE NVRPTDURATION         ADD CONSTRAINT PK_NVRPTDURATION     PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, REPORT_DT, REPORT_TM);
ALTER TABLE NVRPTLINK             ADD CONSTRAINT PK_NVRPTLINK         PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, LINK_SEQ, REPORT_DT, REPORT_TM);
ALTER TABLE NVRPTOPEN             ADD CONSTRAINT PK_NVRPTOPEN         PRIMARY KEY (CAMPAIGN_NO, RESULT_SEQ, REPORT_DT, REPORT_TM);

-- SMS(다우기술) 사용 시
ALTER TABLE EM_TRAN ADD LIST_SEQ VARCHAR(10);
ALTER TABLE EM_LOG ADD LIST_SEQ VARCHAR(10);

CREATE INDEX IDX_EMLOG_SRLS    ON EM_LOG (SERVICE_NO, RESULT_SEQ, LIST_SEQ, SERVICE_TYPE);
CREATE INDEX IDX_EMLOG_TDT     ON EM_LOG (TRAN_DATE);

-- 알림톡 사용 시
ALTER TABLE MZSENDTRAN            ADD LIST_SEQ        VARCHAR(10);
ALTER TABLE MZSENDTRAN            ADD CUSTOMER_KEY    VARCHAR(100);
ALTER TABLE MZSENDLOG             ADD LIST_SEQ        VARCHAR(10);
ALTER TABLE MZSENDLOG             ADD CUSTOMER_KEY    VARCHAR(100);

CREATE INDEX IDX_SENDLOG_SSL    ON EM_LOG (SLOT1, SLOT2, LIST_SEQ)