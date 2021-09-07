/* **************************************************************************
*  File Name : 알림톡5.3.1 Agent 설치 스키마_MSSQL.sql
*
*    Author : @mnwise.com
*
*    Desc : MSSQL용 알림톡 Agent 테이블 생성 스키마
*
*************************************************************************** */


/*
-------------------------------------------------------------------------------
-- CREATE MZSENDTRAN
-------------------------------------------------------------------------------
*/

CREATE TABLE MZSENDTRAN (
    SN              VARCHAR(100)    NOT NULL
  , SENDER_KEY      VARCHAR(40)     NOT NULL
  , CHANNEL         VARCHAR(1)      NOT NULL
  , SND_TYPE        VARCHAR(1)      NOT NULL  DEFAULT 'P'
  , PHONE_NUM       VARCHAR(16)     NOT NULL
  , TMPL_CD         VARCHAR(30)     NOT NULL
  , SUBJECT         VARCHAR(40)     NULL
  , SND_MSG         NVARCHAR(1000)  NOT NULL
  , SMS_SND_MSG     VARCHAR(2000)   NULL
  , SMS_SND_NUM     VARCHAR(16)     NULL
  , REQ_DEPT_CD     VARCHAR(50)     NOT NULL  DEFAULT 'admin'
  , REQ_USR_ID      VARCHAR(50)     NOT NULL  DEFAULT 'admin'
  , REQ_DTM         VARCHAR(14)     NOT NULL
  , SND_DTM         VARCHAR(14)     NULL
  , RSLT_CD         VARCHAR(4)      NULL
  , RCPT_MSG        VARCHAR(250)    NULL
  , RCPT_DTM        VARCHAR(14)     NULL
  , SMS_SND_DTM     VARCHAR(14)     NULL
  , SMS_RSLT_CD     VARCHAR(4)      NULL
  , SMS_RCPT_MSG    VARCHAR(250)    NULL
  , SMS_RCPT_DTM    VARCHAR(14)     NULL
  , SMS_GB          VARCHAR(1)      NULL
  , SMS_SND_YN      VARCHAR(1)      NOT NULL  DEFAULT 'N'
  , TRAN_SN         DECIMAL(16,0)   NULL
  , TRAN_STS        VARCHAR(1)      NOT NULL  DEFAULT '1'
  , AGENT_ID        VARCHAR(20)     NULL
  , SLOT1           VARCHAR(100)    NULL
  , SLOT2           VARCHAR(100)    NULL
  , TR_TYPE_CD      VARCHAR(1)                DEFAULT '9'
  , ATTACHMENT      VARCHAR(4000)   NULL
  , APP_USER_ID     VARCHAR(20)     NULL
  , TITLE           VARCHAR(50  )   NULL
  , SUPPLEMENT      VARCHAR(4000)   NULL
  , LIST_SEQ                  VARCHAR(10)        NOT NULL
  , CUSTOMER_KEY              VARCHAR(50)        NOT NULL
  , SERVICE_NO               DECIMAL(15,0)         NOT NULL
  , SERVICE_TYPE               VARCHAR(2)         NOT NULL
  , RESULT_SEQ                DECIMAL(16,0)         NOT NULL
);

/*
-------------------------------------------------------------------------------
-- PK & INDEX
-------------------------------------------------------------------------------
*/

ALTER TABLE MZSENDTRAN ADD CONSTRAINT PK_MZSENDTRAN PRIMARY KEY (SN);

CREATE INDEX IDX_MZSENDTRAN_TRAN_SN ON MZSENDTRAN(
    TRAN_SN
);

CREATE INDEX IDX_MZSENDTRAN_REQ_DTM ON MZSENDTRAN(
    REQ_DTM
);

CREATE INDEX IDX_MZSENDTRAN_MLT1 ON MZSENDTRAN
(
    TRAN_STS, TR_TYPE_CD, REQ_DTM
);



/*
-------------------------------------------------------------------------------
-- COMMENT
-------------------------------------------------------------------------------
*/

EXEC sp_addextendedproperty 'MS_Description', '알림톡발송트랜젝션', 'user', dbo, 'table', MZSENDTRAN;
EXEC sp_addextendedproperty 'MS_Description', '일련번호', 'user', dbo, 'table', MZSENDTRAN, 'column', SN;
EXEC sp_addextendedproperty 'MS_Description', '발송프로필키', 'user', dbo, 'table', MZSENDTRAN, 'column', SENDER_KEY;
EXEC sp_addextendedproperty 'MS_Description', '채널', 'user', dbo, 'table', MZSENDTRAN, 'column', CHANNEL;
EXEC sp_addextendedproperty 'MS_Description', '알림톡발송방식', 'user', dbo, 'table', MZSENDTRAN, 'column', SND_TYPE;
EXEC sp_addextendedproperty 'MS_Description', '수신자휴대폰번호', 'user', dbo, 'table', MZSENDTRAN, 'column', PHONE_NUM;
EXEC sp_addextendedproperty 'MS_Description', '알림톡탬플릿코드', 'user', dbo, 'table', MZSENDTRAN, 'column', TMPL_CD;
EXEC sp_addextendedproperty 'MS_Description', 'LMS제목', 'user', dbo, 'table', MZSENDTRAN, 'column', SUBJECT;
EXEC sp_addextendedproperty 'MS_Description', '발송메시지', 'user', dbo, 'table', MZSENDTRAN, 'column', SND_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'SMS발송메시지', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_SND_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'SMS발신번호', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_SND_NUM;
EXEC sp_addextendedproperty 'MS_Description', '발송요청부서코드', 'user', dbo, 'table', MZSENDTRAN, 'column', REQ_DEPT_CD;
EXEC sp_addextendedproperty 'MS_Description', '발송요청자ID', 'user', dbo, 'table', MZSENDTRAN, 'column', REQ_USR_ID;
EXEC sp_addextendedproperty 'MS_Description', '발송요청일시', 'user', dbo, 'table', MZSENDTRAN, 'column', REQ_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'Agent 발송일시', 'user', dbo, 'table', MZSENDTRAN, 'column', SND_DTM;
EXEC sp_addextendedproperty 'MS_Description', '알림톡처리결과코드', 'user', dbo, 'table', MZSENDTRAN, 'column', RSLT_CD;
EXEC sp_addextendedproperty 'MS_Description', '알림톡처리결과메시지', 'user', dbo, 'table', MZSENDTRAN, 'column', RCPT_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'Agent 수신일시', 'user', dbo, 'table', MZSENDTRAN, 'column', RCPT_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS발송일시', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_SND_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS처리결과코드', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_RSLT_CD;
EXEC sp_addextendedproperty 'MS_Description', 'SMS처리메시지', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_RCPT_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'SMS수신일시', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_RCPT_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS 구분', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_GB;
EXEC sp_addextendedproperty 'MS_Description', 'SMS 발송여부', 'user', dbo, 'table', MZSENDTRAN, 'column', SMS_SND_YN;
EXEC sp_addextendedproperty 'MS_Description', '처리일련번호', 'user', dbo, 'table', MZSENDTRAN, 'column', TRAN_SN;
EXEC sp_addextendedproperty 'MS_Description', '처리상태', 'user', dbo, 'table', MZSENDTRAN, 'column', TRAN_STS;
EXEC sp_addextendedproperty 'MS_Description', 'Agent 서버 식별자', 'user', dbo, 'table', MZSENDTRAN, 'column', AGENT_ID;
EXEC sp_addextendedproperty 'MS_Description', '발송요청부가정보1', 'user', dbo, 'table', MZSENDTRAN, 'column', SLOT1;
EXEC sp_addextendedproperty 'MS_Description', '발송요청부가정보2', 'user', dbo, 'table', MZSENDTRAN, 'column', SLOT2;
EXEC sp_addextendedproperty 'MS_Description', '발송유형. B:배치, R:실시간', 'user', dbo, 'table', MZSENDTRAN, 'column', TR_TYPE_CD;
EXEC sp_addextendedproperty 'MS_Description', '링크 버튼 JSON', 'user', dbo, 'table', MZSENDTRAN, 'column', ATTACHMENT;
EXEC sp_addextendedproperty 'MS_Description', '앱유저아이디', 'user', dbo, 'table', MZSENDTRAN, 'column', APP_USER_ID;
EXEC sp_addextendedproperty 'MS_Description', '강조표기', 'user', dbo, 'table', MZSENDTRAN, 'column', TITLE;
EXEC sp_addextendedproperty 'MS_Description', '바로연결 버튼 JSON', 'user', dbo, 'table', MZSENDTRAN, 'column', SUPPLEMENT;



/*
-------------------------------------------------------------------------------
-- CREATE MZSENDLOG
-------------------------------------------------------------------------------
*/

CREATE TABLE MZSENDLOG (
    SN             VARCHAR(100)    NOT NULL
  , SENDER_KEY     VARCHAR(40)     NOT NULL
  , CHANNEL        VARCHAR(1)      NOT NULL
  , SND_TYPE       VARCHAR(1)      NOT NULL
  , PHONE_NUM      VARCHAR(16)     NOT NULL
  , TMPL_CD        VARCHAR(30)     NOT NULL
  , SUBJECT        VARCHAR(40)     NULL
  , SND_MSG        NVARCHAR(1000)  NOT NULL
  , SMS_SND_MSG    VARCHAR(2000)   NULL
  , SMS_SND_NUM    VARCHAR(16)     NULL
  , REQ_DEPT_CD    VARCHAR(50)     NOT NULL
  , REQ_USR_ID     VARCHAR(50)     NOT NULL
  , REQ_DTM        VARCHAR(14)     NOT NULL
  , SND_DTM        VARCHAR(14)     NULL
  , RSLT_CD        VARCHAR(4)      NULL
  , RCPT_MSG       VARCHAR(250)    NULL
  , RCPT_DTM       VARCHAR(14)     NULL
  , SMS_SND_DTM    VARCHAR(14)     NULL
  , SMS_RSLT_CD    VARCHAR(4)      NULL
  , SMS_RCPT_MSG   VARCHAR(250)    NULL
  , SMS_RCPT_DTM   VARCHAR(14)     NULL
  , SMS_GB         VARCHAR(1)      NULL
  , SMS_SND_YN     VARCHAR(1)      NOT NULL
  , TRAN_SN        DECIMAL(16,0)   NULL
  , TRAN_STS       VARCHAR(1)      NOT NULL
  , AGENT_ID       VARCHAR(20)     NULL
  , SLOT1          VARCHAR(100)    NULL
  , SLOT2          VARCHAR(100)    NULL
  , TR_TYPE_CD     VARCHAR(1)      NULL
  , ATTACHMENT     VARCHAR(4000)   NULL
  , APP_USER_ID    VARCHAR(20)     NULL
  , TITLE          VARCHAR(50  )   NULL
  , SUPPLEMENT     VARCHAR(4000)   NULL
  , LIST_SEQ                  VARCHAR(10)        NOT NULL
  , CUSTOMER_KEY              VARCHAR(50)        NOT NULL
  , SERVICE_NO               DECIMAL(15,0)         NOT NULL
  , SERVICE_TYPE               VARCHAR(2)         NOT NULL
  , RESULT_SEQ                DECIMAL(16,0)         NOT NULL
);


/*
-------------------------------------------------------------------------------
-- PK & INDEX
-------------------------------------------------------------------------------
*/
CREATE INDEX IDX_MZSENDLOG_SN ON MZSENDLOG(
    SN
);

CREATE INDEX IDX_MZSENDLOG_TRAN_STS ON MZSENDLOG(
    TRAN_STS
);

CREATE INDEX IDX_MZSENDLOG_RDT_RCD ON MZSENDLOG(
    REQ_DTM, RSLT_CD
);

CREATE INDEX IDX_MZSENDLOG_SND_DTM ON MZSENDLOG(
    SND_DTM
);



/*
-------------------------------------------------------------------------------
-- COMMENT
-------------------------------------------------------------------------------
*/

EXEC sp_addextendedproperty 'MS_Description', '알림톡발송로그', 'user', dbo, 'table', MZSENDLOG;
EXEC sp_addextendedproperty 'MS_Description', '일련번호', 'user', dbo, 'table', MZSENDLOG, 'column', SN;
EXEC sp_addextendedproperty 'MS_Description', '발송프로필키', 'user', dbo, 'table', MZSENDLOG, 'column', SENDER_KEY;
EXEC sp_addextendedproperty 'MS_Description', '채널', 'user', dbo, 'table', MZSENDLOG, 'column', CHANNEL;
EXEC sp_addextendedproperty 'MS_Description', '알림톡발송방식', 'user', dbo, 'table', MZSENDLOG, 'column', SND_TYPE;
EXEC sp_addextendedproperty 'MS_Description', '수신자휴대폰번호', 'user', dbo, 'table', MZSENDLOG, 'column', PHONE_NUM;
EXEC sp_addextendedproperty 'MS_Description', '알림톡탬플릿코드', 'user', dbo, 'table', MZSENDLOG, 'column', TMPL_CD;
EXEC sp_addextendedproperty 'MS_Description', 'LMS제목', 'user', dbo, 'table', MZSENDLOG, 'column', SUBJECT;
EXEC sp_addextendedproperty 'MS_Description', '발송메시지', 'user', dbo, 'table', MZSENDLOG, 'column', SND_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'SMS발신번호', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_SND_NUM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS발송메시지', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_SND_MSG;
EXEC sp_addextendedproperty 'MS_Description', '발송요청부서코드', 'user', dbo, 'table', MZSENDLOG, 'column', REQ_DEPT_CD;
EXEC sp_addextendedproperty 'MS_Description', '발송요청자ID', 'user', dbo, 'table', MZSENDLOG, 'column', REQ_USR_ID;
EXEC sp_addextendedproperty 'MS_Description', '발송요청일시', 'user', dbo, 'table', MZSENDLOG, 'column', REQ_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'Agent 발송일시', 'user', dbo, 'table', MZSENDLOG, 'column', SND_DTM;
EXEC sp_addextendedproperty 'MS_Description', '알림톡처리결과코드', 'user', dbo, 'table', MZSENDLOG, 'column', RSLT_CD;
EXEC sp_addextendedproperty 'MS_Description', '알림톡처리결과메시지', 'user', dbo, 'table', MZSENDLOG, 'column', RCPT_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'Agent 수신일시', 'user', dbo, 'table', MZSENDLOG, 'column', RCPT_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS발송일시', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_SND_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS처리결과코드', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_RSLT_CD;
EXEC sp_addextendedproperty 'MS_Description', 'SMS처리메시지', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_RCPT_MSG;
EXEC sp_addextendedproperty 'MS_Description', 'SMS수신일시', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_RCPT_DTM;
EXEC sp_addextendedproperty 'MS_Description', 'SMS 구분', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_GB;
EXEC sp_addextendedproperty 'MS_Description', 'SMS 발송여부', 'user', dbo, 'table', MZSENDLOG, 'column', SMS_SND_YN;
EXEC sp_addextendedproperty 'MS_Description', '처리일련번호', 'user', dbo, 'table', MZSENDLOG, 'column', TRAN_SN;
EXEC sp_addextendedproperty 'MS_Description', '처리상태', 'user', dbo, 'table', MZSENDLOG, 'column', TRAN_STS;
EXEC sp_addextendedproperty 'MS_Description', 'Agent 서버 식별자', 'user', dbo, 'table', MZSENDLOG, 'column', AGENT_ID;
EXEC sp_addextendedproperty 'MS_Description', '발송요청부가정보1', 'user', dbo, 'table', MZSENDLOG, 'column', SLOT1;
EXEC sp_addextendedproperty 'MS_Description', '발송요청부가정보2', 'user', dbo, 'table', MZSENDLOG, 'column', SLOT2;
EXEC sp_addextendedproperty 'MS_Description', '발송유형. B:배치, R:실시간', 'user', dbo, 'table', MZSENDLOG, 'column', TR_TYPE_CD;
EXEC sp_addextendedproperty 'MS_Description', '링크 버튼 JSON', 'user', dbo, 'table', MZSENDLOG, 'column', ATTACHMENT;
EXEC sp_addextendedproperty 'MS_Description', '앱유저아이디', 'user', dbo, 'table', MZSENDLOG, 'column', APP_USER_ID;
EXEC sp_addextendedproperty 'MS_Description', '강조표기', 'user', dbo, 'table', MZSENDLOG, 'column', TITLE;
EXEC sp_addextendedproperty 'MS_Description', '바로연결 버튼 JSON', 'user', dbo, 'table', MZSENDLOG, 'column', SUPPLEMENT;


-- 발송 결과를 wiseU SENDLOG 테이블에 업데이트하는 작업 플래그
ALTER TABLE MZSENDLOG ADD SYNC_FLAG CHAR(1) DEFAULT 'N' NOT NULL;
CREATE INDEX IDX_MZSENDLOG_SYNC_FLAG ON MZSENDLOG (SYNC_FLAG);
