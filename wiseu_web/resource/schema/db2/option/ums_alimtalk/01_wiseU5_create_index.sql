/* **************************************************************************
*  File Name : 알림톡5.3.1 Agent 설치 스키마_DB2.sql
*
*    Author : @mnwise.com
*
*    Desc : DB2용 알림톡 Agent 테이블 생성 스키마
*
*************************************************************************** */

/*
-------------------------------------------------------------------------------
-- CREATE MZSENDTRAN
-------------------------------------------------------------------------------
*/

CREATE TABLE MZSENDTRAN (
    SN            VARCHAR (100 )  NOT NULL
  , SENDER_KEY    VARCHAR (40  )  NOT NULL
  , CHANNEL       VARCHAR (1   )  NOT NULL
  , SND_TYPE      VARCHAR (1   )  DEFAULT 'P' NOT NULL
  , PHONE_NUM     VARCHAR (16  )  NOT NULL
  , TMPL_CD       VARCHAR (30  )  NOT NULL
  , SUBJECT       VARCHAR (40  )
  , SND_MSG       NVARCHAR(1000)  NOT NULL
  , SMS_SND_MSG   VARCHAR (2000)
  , SMS_SND_NUM   VARCHAR (16  )
  , REQ_DEPT_CD   VARCHAR (50  )  DEFAULT 'admin' NOT NULL
  , REQ_USR_ID    VARCHAR (50  )  DEFAULT 'admin' NOT NULL
  , REQ_DTM       VARCHAR (14  )  NOT NULL
  , SND_DTM       VARCHAR (14  )
  , RSLT_CD       VARCHAR (4   )
  , RCPT_MSG      VARCHAR (250 )
  , RCPT_DTM      VARCHAR (14  )
  , SMS_SND_DTM   VARCHAR (14  )
  , SMS_RSLT_CD   VARCHAR (4   )
  , SMS_RCPT_MSG  VARCHAR (250 )
  , SMS_RCPT_DTM  VARCHAR (14  )
  , SMS_GB        VARCHAR (1   )
  , SMS_SND_YN    VARCHAR (1   )  DEFAULT 'N'     NOT NULL
  , TRAN_SN       DECIMAL  (16,0)
  , TRAN_STS      VARCHAR (1   )  DEFAULT '1'     NOT NULL
  , AGENT_ID      VARCHAR (20  )
  , SLOT1         VARCHAR (100 )
  , SLOT2         VARCHAR (100 )
  , TR_TYPE_CD    VARCHAR (1   )  DEFAULT '9'
  , ATTACHMENT    VARCHAR (4000)
  , APP_USER_ID   VARCHAR (20  )
  , TITLE         VARCHAR (50  )
  , SUPPLEMENT    VARCHAR (4000)
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

COMMENT ON COLUMN MZSENDTRAN.SN           IS  '일련번호';
COMMENT ON COLUMN MZSENDTRAN.SENDER_KEY   IS  '발송프로필키';
COMMENT ON COLUMN MZSENDTRAN.CHANNEL      IS  '채널';
COMMENT ON COLUMN MZSENDTRAN.SND_TYPE     IS  '알림톡 발송방식';
COMMENT ON COLUMN MZSENDTRAN.PHONE_NUM    IS  '수신자휴대폰번호';
COMMENT ON COLUMN MZSENDTRAN.TMPL_CD      IS  '알림톡탬플릿코드';
COMMENT ON COLUMN MZSENDTRAN.SUBJECT      IS  'LMS제목';
COMMENT ON COLUMN MZSENDTRAN.SND_MSG      IS  '발송메시지';
COMMENT ON COLUMN MZSENDTRAN.SMS_SND_MSG  IS  'SMS발송메시지';
COMMENT ON COLUMN MZSENDTRAN.SMS_SND_NUM  IS  'SMS발신번호';
COMMENT ON COLUMN MZSENDTRAN.REQ_DEPT_CD  IS  '발송요청부서코드';
COMMENT ON COLUMN MZSENDTRAN.REQ_USR_ID   IS  '발송요청자ID';
COMMENT ON COLUMN MZSENDTRAN.REQ_DTM      IS  '발송요청일시';
COMMENT ON COLUMN MZSENDTRAN.SND_DTM      IS  'Agent 발송일시';
COMMENT ON COLUMN MZSENDTRAN.RSLT_CD      IS  '알림톡처리결과코드';
COMMENT ON COLUMN MZSENDTRAN.RCPT_MSG     IS  '알림톡처리결과메시지';
COMMENT ON COLUMN MZSENDTRAN.RCPT_DTM     IS  'Agent 수신일시';
COMMENT ON COLUMN MZSENDTRAN.SMS_SND_DTM  IS  'SMS발송일시';
COMMENT ON COLUMN MZSENDTRAN.SMS_RSLT_CD  IS  'SMS처리결과코드';
COMMENT ON COLUMN MZSENDTRAN.SMS_RCPT_MSG IS  'SMS처리메시지';
COMMENT ON COLUMN MZSENDTRAN.SMS_RCPT_DTM IS  'SMS수신일시';
COMMENT ON COLUMN MZSENDTRAN.SMS_GB       IS  'SMS 구분. S:SMS, L:LMS';
COMMENT ON COLUMN MZSENDTRAN.SMS_SND_YN   IS  'SMS 발송여부';
COMMENT ON COLUMN MZSENDTRAN.TRAN_SN      IS  '처리일련번호';
COMMENT ON COLUMN MZSENDTRAN.TRAN_STS     IS  '처리상태';
COMMENT ON COLUMN MZSENDTRAN.AGENT_ID     IS  'Agent 서버 식별자';
COMMENT ON COLUMN MZSENDTRAN.SLOT1        IS  '발송요청부가정보1';
COMMENT ON COLUMN MZSENDTRAN.SLOT2        IS  '발송요청부가정보2';
COMMENT ON COLUMN MZSENDTRAN.TR_TYPE_CD   IS  '발송유형. 9:배치, 1:실시간';
COMMENT ON COLUMN MZSENDTRAN.ATTACHMENT   IS  '링크 버튼 JSON';
COMMENT ON COLUMN MZSENDTRAN.APP_USER_ID  IS  '앱유저아이디';
COMMENT ON COLUMN MZSENDTRAN.TITLE        IS  '강조표기';
COMMENT ON COLUMN MZSENDTRAN.SUPPLEMENT   IS  '바로연결 버튼 JSON';

/*
-------------------------------------------------------------------------------
-- CREATE MZSENDLOG
-------------------------------------------------------------------------------
*/

CREATE TABLE MZSENDLOG (
    SN            VARCHAR (100 )  NOT NULL
  , SENDER_KEY    VARCHAR (40  )  NOT NULL
  , CHANNEL       VARCHAR (1   )  NOT NULL
  , SND_TYPE      VARCHAR (1   )  NOT NULL
  , PHONE_NUM     VARCHAR (16  )  NOT NULL
  , TMPL_CD       VARCHAR (30  )  NOT NULL
  , SUBJECT       VARCHAR (40  )
  , SND_MSG       NVARCHAR(1000)  NOT NULL
  , SMS_SND_MSG   VARCHAR (2000)
  , SMS_SND_NUM   VARCHAR (16  )
  , REQ_DEPT_CD   VARCHAR (50  )  NOT NULL
  , REQ_USR_ID    VARCHAR (50  )  NOT NULL
  , REQ_DTM       VARCHAR (14  )  NOT NULL
  , SND_DTM       VARCHAR (14  )
  , RSLT_CD       VARCHAR (4   )
  , RCPT_MSG      VARCHAR (250 )
  , RCPT_DTM      VARCHAR (14  )
  , SMS_SND_DTM   VARCHAR (14  )
  , SMS_RSLT_CD   VARCHAR (4   )
  , SMS_RCPT_MSG  VARCHAR (250 )
  , SMS_RCPT_DTM  VARCHAR (14  )
  , SMS_GB        VARCHAR (1   )
  , SMS_SND_YN    VARCHAR (1   )  NOT NULL
  , TRAN_SN       DECIMAL  (16,0)
  , TRAN_STS      VARCHAR (1   )  NOT NULL
  , AGENT_ID      VARCHAR (20  )
  , SLOT1         VARCHAR (100 )
  , SLOT2         VARCHAR (100 )
  , TR_TYPE_CD    VARCHAR (1   )
  , ATTACHMENT    VARCHAR (4000)
  , APP_USER_ID   VARCHAR (20  )
  , TITLE         VARCHAR (50  )
  , SUPPLEMENT    VARCHAR (4000)
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

COMMENT ON COLUMN MZSENDLOG.SN            IS  '일련번호';
COMMENT ON COLUMN MZSENDLOG.SENDER_KEY    IS  '발송프로필키';
COMMENT ON COLUMN MZSENDLOG.CHANNEL       IS  '채널';
COMMENT ON COLUMN MZSENDLOG.SND_TYPE      IS  '알림톡 발송방식';
COMMENT ON COLUMN MZSENDLOG.PHONE_NUM     IS  '수신자휴대폰번호';
COMMENT ON COLUMN MZSENDLOG.TMPL_CD       IS  '알림톡탬플릿코드';
COMMENT ON COLUMN MZSENDLOG.SUBJECT       IS  'LMS제목';
COMMENT ON COLUMN MZSENDLOG.SND_MSG       IS  '발송메시지';
COMMENT ON COLUMN MZSENDLOG.SMS_SND_MSG   IS  'SMS발송메시지';
COMMENT ON COLUMN MZSENDLOG.SMS_SND_NUM   IS  'SMS발신번호';
COMMENT ON COLUMN MZSENDLOG.REQ_DEPT_CD   IS  '발송요청부서코드';
COMMENT ON COLUMN MZSENDLOG.REQ_USR_ID    IS  '발송요청자ID';
COMMENT ON COLUMN MZSENDLOG.REQ_DTM       IS  '발송요청일시';
COMMENT ON COLUMN MZSENDLOG.SND_DTM       IS  'Agent 발송일시';
COMMENT ON COLUMN MZSENDLOG.RSLT_CD       IS  '알림톡처리결과코드';
COMMENT ON COLUMN MZSENDLOG.RCPT_MSG      IS  '알림톡처리결과메시';
COMMENT ON COLUMN MZSENDLOG.RCPT_DTM      IS  'Agent 수신일시';
COMMENT ON COLUMN MZSENDLOG.SMS_SND_DTM   IS  'SMS발송일시';
COMMENT ON COLUMN MZSENDLOG.SMS_RSLT_CD   IS  'SMS처리결과코드';
COMMENT ON COLUMN MZSENDLOG.SMS_RCPT_MSG  IS  'SMS처리메시지';
COMMENT ON COLUMN MZSENDLOG.SMS_RCPT_DTM  IS  'SMS수신일시';
COMMENT ON COLUMN MZSENDLOG.SMS_GB        IS  'SMS 구분. S:SMS, L:LMS';
COMMENT ON COLUMN MZSENDLOG.SMS_SND_YN    IS  'SMS 발송여부';
COMMENT ON COLUMN MZSENDLOG.TRAN_SN       IS  '처리일련번호';
COMMENT ON COLUMN MZSENDLOG.TRAN_STS      IS  '처리상태';
COMMENT ON COLUMN MZSENDLOG.AGENT_ID      IS  'Agent 서버 식별자';
COMMENT ON COLUMN MZSENDLOG.SLOT1         IS  '발송요청부가정보1';
COMMENT ON COLUMN MZSENDLOG.SLOT2         IS  '발송요청부가정보2';
COMMENT ON COLUMN MZSENDLOG.TR_TYPE_CD    IS  '발송유형. 9:배치, 1:실시간';
COMMENT ON COLUMN MZSENDLOG.ATTACHMENT    IS  '링크 버튼 JSON';
COMMENT ON COLUMN MZSENDLOG.APP_USER_ID   IS  '앱유저아이디';
COMMENT ON COLUMN MZSENDLOG.TITLE         IS  '강조표기';
COMMENT ON COLUMN MZSENDLOG.SUPPLEMENT    IS  '바로연결 버튼 JSON';

-- 발송 결과를 wiseU SENDLOG 테이블에 업데이트하는 작업 플래그
ALTER TABLE MZSENDLOG ADD SYNC_FLAG CHAR(1) DEFAULT 'N' NOT NULL;
CREATE INDEX IDX_MZSENDLOG_SYNC_FLAG ON MZSENDLOG (SYNC_FLAG);
