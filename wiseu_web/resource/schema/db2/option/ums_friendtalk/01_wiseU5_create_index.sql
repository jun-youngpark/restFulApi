/*
-------------------------------------------------------------------------------
-- CREATE MZFTSENDTRAN
-------------------------------------------------------------------------------
*/

CREATE TABLE MZFTSENDTRAN (
    SN            VARCHAR (100 )  NOT NULL
  , SENDER_KEY    VARCHAR (40  )  NOT NULL
  , CHANNEL       VARCHAR (1   )  NOT NULL
  , SND_TYPE      VARCHAR (1   )  DEFAULT 'P'
  , PHONE_NUM     VARCHAR (16  )  NOT NULL
  , TMPL_CD       VARCHAR (30  )  NULL
  , SUBJECT       VARCHAR (40  )  NULL
  , SND_MSG       NVARCHAR (1000)  NOT NULL
  , SMS_SND_MSG   VARCHAR (2000  )  NULL
  , SMS_SND_NUM   VARCHAR (16  )  NULL
  , REQ_DEPT_CD   VARCHAR (50  )  DEFAULT 'admin'
  , REQ_USR_ID    VARCHAR (50  )  DEFAULT 'admin'
  , REQ_DTM       VARCHAR (14  )  NOT NULL
  , SND_DTM       VARCHAR (14  )  NULL
  , RSLT_CD       VARCHAR (4   )  NULL
  , RCPT_MSG      VARCHAR (250 )  NULL
  , RCPT_DTM      VARCHAR (14  )  NULL
  , SMS_SND_DTM   VARCHAR (14  )  NULL
  , SMS_RSLT_CD   VARCHAR (4   )  NULL
  , SMS_RCPT_MSG  VARCHAR (250 )  NULL
  , SMS_RCPT_DTM  VARCHAR (14  )  NULL
  , SMS_GB        VARCHAR (1   )  NULL
  , SMS_SND_YN    VARCHAR (1   )  DEFAULT 'N'
  , TRAN_SN       DECIMAL  (16,0)  NULL
  , TRAN_STS      VARCHAR (1   )  DEFAULT '1'
  , AGENT_ID      VARCHAR (20  )  NULL
  , SLOT1         VARCHAR (100 )  NULL
  , SLOT2         VARCHAR (100 )  NULL
  , TR_TYPE_CD    VARCHAR (1   )  DEFAULT '9'
  , USER_KEY      VARCHAR (30  )  NULL
  , MSG_GRP_CD    VARCHAR (30  )  NULL
  , AD_FLAG       VARCHAR (1   )  DEFAULT 'Y'
  , ATTACHMENT    VARCHAR (4000)  NULL
  , WIDE          VARCHAR (1   )  DEFAULT 'N'
  , APP_USER_ID   VARCHAR (20  )  NULL
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

ALTER TABLE MZFTSENDTRAN ADD CONSTRAINT PK_MZFTSENDTRAN PRIMARY KEY (SN);

CREATE INDEX IDX_MZFTSENDTRAN_TRAN_SN ON MZFTSENDTRAN(
    TRAN_SN
);

CREATE INDEX IDX_MZFTSENDTRAN_PHONE_NUM ON MZFTSENDTRAN(
    PHONE_NUM
);

CREATE INDEX IDX_MZFTSENDTRAN_REQ_DTM ON MZFTSENDTRAN(
    REQ_DTM
);

CREATE INDEX IDX_MZFTSENDTRAN_MLT1 ON MZFTSENDTRAN
(
    TRAN_STS, TR_TYPE_CD, REQ_DTM
);




/*
-------------------------------------------------------------------------------
-- CREATE MZSENDLOG
-------------------------------------------------------------------------------
*/

CREATE TABLE MZFTSENDLOG (
    SN            VARCHAR (100 )  NOT NULL
  , SENDER_KEY    VARCHAR (40  )  NOT NULL
  , CHANNEL       VARCHAR (1   )  NOT NULL
  , SND_TYPE      VARCHAR (1   )  NOT NULL
  , PHONE_NUM     VARCHAR (16  )  NOT NULL
  , TMPL_CD       VARCHAR (30  )  NULL
  , SUBJECT       VARCHAR (40  )  NULL
  , SND_MSG       NVARCHAR(1000)  NULL
  , SMS_SND_MSG   VARCHAR (2000)  NULL
  , SMS_SND_NUM   VARCHAR (16  )  NULL
  , REQ_DEPT_CD   VARCHAR (50  )  NOT NULL
  , REQ_USR_ID    VARCHAR (50  )  NOT NULL
  , REQ_DTM       VARCHAR (14  )  NOT NULL
  , SND_DTM       VARCHAR (14  )  NULL
  , RSLT_CD       VARCHAR (4   )  NULL
  , RCPT_MSG      VARCHAR (250 )  NULL
  , RCPT_DTM      VARCHAR (14  )  NULL
  , SMS_SND_DTM   VARCHAR (14  )  NULL
  , SMS_RSLT_CD   VARCHAR (4   )  NULL
  , SMS_RCPT_MSG  VARCHAR (250 )  NULL
  , SMS_RCPT_DTM  VARCHAR (14  )  NULL
  , SMS_GB        VARCHAR (1   )  NULL
  , SMS_SND_YN    VARCHAR (1   )  NULL
  , TRAN_SN       DECIMAL  (16,0)  NULL
  , TRAN_STS      VARCHAR (1   )  NOT NULL
  , AGENT_ID      VARCHAR (20  )  NULL
  , SLOT1         VARCHAR (100 )  NULL
  , SLOT2         VARCHAR (100 )  NULL
  , TR_TYPE_CD    VARCHAR (1   )  NULL
  , USER_KEY      VARCHAR (30  )  NULL
  , MSG_GRP_CD    VARCHAR (30  )  NULL
  , AD_FLAG       VARCHAR (1   )  NULL
  , ATTACHMENT    VARCHAR (4000)  NULL
  , WIDE          VARCHAR (1   )  NULL
  , APP_USER_ID   VARCHAR (20  )  NULL
);


/*
-------------------------------------------------------------------------------
-- PK & INDEX
-------------------------------------------------------------------------------
*/

CREATE INDEX IDX_MZFTSENDLOG_SN ON MZFTSENDLOG(
    SN
);

CREATE INDEX IDX_MZFTSENDLOG_TRAN_SN ON MZFTSENDLOG(
    TRAN_SN
);

CREATE INDEX IDX_MZFTSENDLOG_TRAN_STS ON MZFTSENDLOG(
    TRAN_STS
);

CREATE INDEX IDX_MZFTSENDLOG_PHONE_NUM ON MZFTSENDLOG(
    PHONE_NUM
);

CREATE INDEX IDX_MZFTSENDLOG_RDT_RCD ON MZFTSENDLOG(
    REQ_DTM, RSLT_CD
);

CREATE INDEX IDX_MZFTSENDLOG_SND_DTM ON MZFTSENDLOG(
    SND_DTM
);


-- 친구톡 발송 mms 우회발송시 사용할 이미지경로
ALTER TABLE MZFTSENDTRAN	ADD IMAGE_PATH 		VARCHAR(250);
ALTER TABLE MZFTSENDLOG		ADD IMAGE_PATH 		VARCHAR(250);

-- 발송 결과를 wiseU SENDLOG 테이블에 업데이트하는 작업 플래그
ALTER TABLE MZFTSENDLOG ADD SYNC_FLAG CHAR(1) DEFAULT 'N' NOT NULL;
CREATE INDEX IDX_MZFTSENDLOG_SYNC_FLAG ON MZFTSENDLOG (SYNC_FLAG);