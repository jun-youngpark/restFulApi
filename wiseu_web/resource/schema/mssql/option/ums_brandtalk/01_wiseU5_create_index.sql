CREATE TABLE MZBTSENDTRAN
(
    SN           VARCHAR(100)     NOT NULL  ,
    SENDER_KEY   VARCHAR(40)      NOT NULL  ,
    CHANNEL      VARCHAR(1)       NOT NULL  ,
    PHONE_NUM    VARCHAR(16)      NOT NULL  ,
    MSG_TYPE     VARCHAR(2)       NOT NULL  ,
    TMPL_CD      VARCHAR(50)      NOT NULL  ,
    REQ_DEPT_CD  VARCHAR(50)      NOT NULL DEFAULT 'admin' ,
    REQ_USR_ID   VARCHAR(50)      NOT NULL DEFAULT 'admin' ,
    REQ_DTM      VARCHAR(14)      NOT NULL  ,
    SND_DTM      VARCHAR(14)      NULL      ,
    RSLT_CD      VARCHAR(10)      NULL      ,
    RCPT_MSG     VARCHAR(250)     NULL      ,
    RCPT_DTM     VARCHAR(14)      NULL      ,
    TRAN_SN      DECIMAL(16,0)    NULL      ,
    TRAN_STS     VARCHAR(1)       NOT NULL DEFAULT '1'  ,
    AGENT_ID     VARCHAR(20)      NULL      ,
    SLOT1        VARCHAR(100)     NULL      ,
    SLOT2        VARCHAR(100)     NULL      ,
    REQ_ID       VARCHAR(30)      NULL      ,
    MSG_ID       VARCHAR(30)      NULL      ,
    CONT_TYPE    VARCHAR(1)       NULL      ,
    TMPL_TYPE    VARCHAR(1)       NULL      ,
    SND_MSG      VARCHAR(4000)    NULL      ,
    ATTACHMENT   VARCHAR(4000)    NULL      ,
    MSG_VR       VARCHAR(4000)    NULL      ,
    BTN_VR       VARCHAR(4000)    NULL      ,
    TARGET_TYPE  VARCHAR(2)       NULL DEFAULT 'BD' ,
    BT_TYPE      VARCHAR(1)       NULL
);

ALTER TABLE MZBTSENDTRAN ADD CONSTRAINT PK_MZBTSENDTRAN PRIMARY KEY (SN);
CREATE INDEX IDX_MZBTSENDTRAN_MLT1 ON MZBTSENDTRAN (TRAN_STS, REQ_DTM);
CREATE INDEX IDX_MZBTSENDTRAN_MLT2 ON MZBTSENDTRAN (REQ_DTM, AGENT_ID);
CREATE INDEX IDX_MZBTSENDTRAN_MSG_ID ON MZBTSENDTRAN (MSG_ID);
CREATE INDEX IDX_MZBTSENDTRAN_REQ_ID ON MZBTSENDTRAN (REQ_ID);
CREATE INDEX IDX_MZBTSENDTRAN_TRAN_SN ON MZBTSENDTRAN (TRAN_SN);


CREATE TABLE MZBTSENDLOG
(
    SN           VARCHAR(100)     NOT NULL  ,
    SENDER_KEY   VARCHAR(40)      NOT NULL  ,
    CHANNEL      VARCHAR(1)       NOT NULL  ,
    PHONE_NUM    VARCHAR(16)      NOT NULL  ,
    MSG_TYPE     VARCHAR(2)       NOT NULL  ,
    TMPL_CD      VARCHAR(50)      NOT NULL  ,
    REQ_DEPT_CD  VARCHAR(50)      NOT NULL  DEFAULT 'admin' ,
    REQ_USR_ID   VARCHAR(50)      NOT NULL  DEFAULT 'admin' ,
    REQ_DTM      VARCHAR(14)      NOT NULL  ,
    SND_DTM      VARCHAR(14)      NULL      ,
    RSLT_CD      VARCHAR(10)      NULL      ,
    RCPT_MSG     VARCHAR(250)     NULL      ,
    RCPT_DTM     VARCHAR(14)      NULL      ,
    TRAN_SN      DECIMAL(16,0)    NULL      ,
    TRAN_STS     VARCHAR(1)       NOT NULL  DEFAULT '1'  ,
    AGENT_ID     VARCHAR(20)      NULL      ,
    SLOT1        VARCHAR(100)     NULL      ,
    SLOT2        VARCHAR(100)     NULL      ,
    REQ_ID       VARCHAR(30)      NULL      ,
    MSG_ID       VARCHAR(30)      NULL      ,
    CONT_TYPE    VARCHAR(1)       NULL      ,
    TMPL_TYPE    VARCHAR(1)       NULL      ,
    SND_MSG      VARCHAR(4000)    NULL      ,
    ATTACHMENT   VARCHAR(4000)    NULL      ,
    MSG_VR       VARCHAR(4000)    NULL      ,
    BTN_VR       VARCHAR(4000)    NULL      ,
    TARGET_TYPE  VARCHAR(2)       NOT NULL DEFAULT 'BD' ,
    BT_TYPE      VARCHAR(1)       NULL
);
/* **************************************************************************
*  wiseU 연동 스키마
*************************************************************************** */

-- 재발송 시 사용
-- SERVICE_NO / SERVICE_TYPE / RESULT_SEQ 추가
ALTER TABLE MZBTSENDTRAN  ADD LIST_SEQ        VARCHAR(10);
ALTER TABLE MZBTSENDTRAN  ADD CUSTOMER_KEY    VARCHAR(100);
ALTER TABLE MZBTSENDTRAN  ADD SERVICE_NO    NUMERIC(15);
ALTER TABLE MZBTSENDTRAN  ADD SERVICE_TYPE  VARCHAR(2);
ALTER TABLE MZBTSENDTRAN  ADD RESULT_SEQ    NUMERIC(16);
ALTER TABLE MZBTSENDLOG   ADD LIST_SEQ        VARCHAR(10);
ALTER TABLE MZBTSENDLOG   ADD CUSTOMER_KEY    VARCHAR(100);
ALTER TABLE MZBTSENDLOG   ADD SERVICE_NO    NUMERIC(15);
ALTER TABLE MZBTSENDLOG   ADD SERVICE_TYPE  VARCHAR(2);
ALTER TABLE MZBTSENDLOG   ADD RESULT_SEQ    NUMERIC(16);

-- 발송 결과를 wiseU SENDLOG 테이블에 업데이트하는 작업 플래그
ALTER TABLE MZBTSENDLOG ADD SYNC_FLAG CHAR(1) DEFAULT 'N' NOT NULL;
CREATE INDEX IDX_MZBTSENDLOG_SYNC_FLAG ON MZBTSENDLOG (SYNC_FLAG);

