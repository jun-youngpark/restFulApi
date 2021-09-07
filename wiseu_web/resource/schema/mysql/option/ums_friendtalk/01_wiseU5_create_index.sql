/*
-------------------------------------------------------------------------------
-- CREATE MZFTSENDTRAN
-------------------------------------------------------------------------------
*/

CREATE TABLE mzftsendtran (
    sn            VARCHAR (100 )  NOT NULL
  , sender_key    VARCHAR (40  )  NOT NULL
  , channel       VARCHAR (1   )  NOT NULL
  , snd_type      VARCHAR (1   )  DEFAULT 'P'
  , phone_num     VARCHAR (16  )  NOT NULL
  , tmpl_cd       VARCHAR (30  )  NULL
  , subject       VARCHAR (40  )  NULL
  , snd_msg       NVARCHAR (1000)  NOT NULL
  , sms_snd_msg   VARCHAR (2000  )  NULL
  , sms_snd_num   VARCHAR (16  )  NULL
  , req_dept_cd   VARCHAR (50  )  DEFAULT 'admin'
  , req_usr_id    VARCHAR (50  )  DEFAULT 'admin'
  , req_dtm       VARCHAR (14  )  NOT NULL
  , snd_dtm       VARCHAR (14  )  NULL
  , rslt_cd       VARCHAR (4   )  NULL
  , rcpt_msg      VARCHAR (250 )  NULL
  , rcpt_dtm      VARCHAR (14  )  NULL
  , sms_snd_dtm   VARCHAR (14  )  NULL
  , sms_rslt_cd   VARCHAR (4   )  NULL
  , sms_rcpt_msg  VARCHAR (250 )  NULL
  , sms_rcpt_dtm  VARCHAR (14  )  NULL
  , sms_gb        VARCHAR (1   )  NULL
  , sms_snd_yn    VARCHAR (1   )  DEFAULT 'N'
  , tran_sn       DECIMAL  (16,0)  NULL
  , tran_sts      VARCHAR (1   )  DEFAULT '1'
  , agent_id      VARCHAR (20  )  NULL
  , slot1         VARCHAR (100 )  NULL
  , slot2         VARCHAR (100 )  NULL
  , tr_type_cd    VARCHAR (1   )  DEFAULT '9'
  , user_key      VARCHAR (30  )  NULL
  , msg_grp_cd    VARCHAR (30  )  NULL
  , ad_flag       VARCHAR (1   )  DEFAULT 'Y'
  , attachment    VARCHAR (4000)  NULL
  , wide          VARCHAR (1   )  DEFAULT 'N'
  , app_user_id   VARCHAR (20  )  NULL
  , list_seq                  VARCHAR(10)
  , customer_key              VARCHAR(50)
  , service_no               DECIMAL(15,0)
  , service_type               VARCHAR(2)
  , result_seq                DECIMAL(16,0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;






/*
-------------------------------------------------------------------------------
-- PK & INDEX
-------------------------------------------------------------------------------
*/

ALTER TABLE mzftsendtran ADD CONSTRAINT pk_mzftsendtran PRIMARY KEY (sn);

CREATE INDEX idx_mzftsendtran_tran_sn ON mzftsendtran(
    tran_sn
);

CREATE INDEX idx_mzftsendtran_phone_num ON mzftsendtran(
    phone_num
);

CREATE INDEX idx_mzftsendtran_req_dtm ON mzftsendtran(
    req_dtm
);

CREATE INDEX idx_mzftsendtran_mlt1 ON mzftsendtran
(
    tran_sts, tr_type_cd, req_dtm
);




/*
-------------------------------------------------------------------------------
-- CREATE MZSENDLOG
-------------------------------------------------------------------------------
*/

CREATE TABLE mzftsendlog (
    sn            VARCHAR (100 )  NOT NULL
  , sender_key    VARCHAR (40  )  NOT NULL
  , channel       VARCHAR (1   )  NOT NULL
  , snd_type      VARCHAR (1   )  NOT NULL
  , phone_num     VARCHAR (16  )  NOT NULL
  , tmpl_cd       VARCHAR (30  )  NULL
  , subject       VARCHAR (40  )  NULL
  , snd_msg       NVARCHAR(1000)  NULL
  , sms_snd_msg   VARCHAR (2000)  NULL
  , sms_snd_num   VARCHAR (16  )  NULL
  , req_dept_cd   VARCHAR (50  )  NOT NULL
  , req_usr_id    VARCHAR (50  )  NOT NULL
  , req_dtm       VARCHAR (14  )  NOT NULL
  , snd_dtm       VARCHAR (14  )  NULL
  , rslt_cd       VARCHAR (4   )  NULL
  , rcpt_msg      VARCHAR (250 )  NULL
  , rcpt_dtm      VARCHAR (14  )  NULL
  , sms_snd_dtm   VARCHAR (14  )  NULL
  , sms_rslt_cd   VARCHAR (4   )  NULL
  , sms_rcpt_msg  VARCHAR (250 )  NULL
  , sms_rcpt_dtm  VARCHAR (14  )  NULL
  , sms_gb        VARCHAR (1   )  NULL
  , sms_snd_yn    VARCHAR (1   )  NULL
  , tran_sn       DECIMAL  (16,0)  NULL
  , tran_sts      VARCHAR (1   )  NOT NULL
  , agent_id      VARCHAR (20  )  NULL
  , slot1         VARCHAR (100 )  NULL
  , slot2         VARCHAR (100 )  NULL
  , tr_type_cd    VARCHAR (1   )  NULL
  , user_key      VARCHAR (30  )  NULL
  , msg_grp_cd    VARCHAR (30  )  NULL
  , ad_flag       VARCHAR (1   )  NULL
  , attachment    VARCHAR (4000)  NULL
  , wide          VARCHAR (1   )  NULL
  , app_user_id   VARCHAR (20  )  NULL
  , list_seq                  VARCHAR(10)
  , customer_key              VARCHAR(50)
  , service_no               DECIMAL(15,0)
  , service_type               VARCHAR(2)
  , result_seq                DECIMAL(16,0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


/*
-------------------------------------------------------------------------------
-- PK & INDEX
-------------------------------------------------------------------------------
*/

CREATE INDEX idx_mzftsendlog_sn ON mzftsendlog(
    sn
);

CREATE INDEX idx_mzftsendlog_tran_sn ON mzftsendlog(
    tran_sn
);

CREATE INDEX idx_mzftsendlog_tran_sts ON mzftsendlog(
    tran_sts
);

CREATE INDEX idx_mzftsendlog_phone_num ON mzftsendlog(
    phone_num
);

CREATE INDEX idx_mzftsendlog_rdt_rcd ON mzftsendlog(
    req_dtm, rslt_cd
);

CREATE INDEX idx_mzftsendlog_snd_dtm ON mzftsendlog(
    snd_dtm
);

-- 친구톡 발송 mms 우회발송시 사용할 이미지경로
ALTER TABLE mzftsendtran	ADD image_path 		VARCHAR(250);
ALTER TABLE mzftsendlog		ADD image_path 		VARCHAR(250);

-- 발송 결과를 wiseU SENDLOG 테이블에 업데이트하는 작업 플래그
ALTER TABLE mzftsendlog ADD sync_flag CHAR(1) DEFAULT 'N' NOT NULL;
CREATE INDEX idx_mzftsendlog_sync_flag ON mzftsendlog (sync_flag);