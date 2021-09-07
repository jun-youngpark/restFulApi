/* **************************************************************************
*  File Name : 알림톡5.3.1 Agent 설치 스키마_MYSQL.sql
*
*    Author : @mnwise.com
*
*    Desc : MYSQL용 알림톡 Agent 테이블 생성 스키마
*
*************************************************************************** */

-- CREATE MZSENDTRAN 알림톡5.3.1
CREATE TABLE mzsendtran (
    sn            VARCHAR (100 )  NOT NULL comment '일련번호'
  , sender_key    VARCHAR (40  )  NOT NULL comment '발송프로필키'
  , channel       VARCHAR (1   )  NOT NULL comment '채널'
  , snd_type      VARCHAR (1   )  DEFAULT 'P' comment '알림톡 발송방식'
  , phone_num     VARCHAR (16  )  NOT NULL comment '수신자휴대폰번호'
  , tmpl_cd       VARCHAR (30  )  NOT NULL comment '알림톡탬플릿코드'
  , subject       VARCHAR (40  )  comment 'lms제목'
  , snd_msg       NVARCHAR(1000)  NOT NULL comment '발송메시지'
  , sms_snd_msg   VARCHAR (2000)  comment 'sms발송메시지'
  , sms_snd_num   VARCHAR (16  ) comment 'sms발신번호'
  , req_dept_cd   VARCHAR (50  )  DEFAULT 'admin' NOT NULL comment '발송요청부서코드'
  , req_usr_id    VARCHAR (50  )  DEFAULT 'admin' NOT NULL comment '발송요청자id'
  , req_dtm       VARCHAR (14  )  NOT NULL comment '발송요청일시'
  , snd_dtm       VARCHAR (14  ) comment 'agent 발송일시'
  , rslt_cd       VARCHAR (4   ) comment '알림톡처리결과코드'
  , rcpt_msg      VARCHAR (250 ) comment '알림톡처리결과메시지'
  , rcpt_dtm      VARCHAR (14  ) comment 'agent 수신일시'
  , sms_snd_dtm   VARCHAR (14  ) comment 'sms발송일시'
  , sms_rslt_cd   VARCHAR (4   ) comment 'sms처리결과코드'
  , sms_rcpt_msg  VARCHAR (250 ) comment 'sms처리메시지'
  , sms_rcpt_dtm  VARCHAR (14  ) comment 'sms수신일시'
  , sms_gb        VARCHAR (1   ) comment 'sms 구분. s:sms, l:lms'
  , sms_snd_yn    VARCHAR (1   ) DEFAULT 'N' NOT NULL comment 'sms 발송여부'
  , tran_sn       decimal  (16,0) comment '처리일련번호'
  , tran_sts      VARCHAR (1   )  DEFAULT '1' NOT NULL comment '처리상태'
  , agent_id      VARCHAR (20  ) comment 'agent 서버 식별자'
  , slot1         VARCHAR (100 ) comment '발송요청부가정보1'
  , slot2         VARCHAR (100 ) comment '발송요청부가정보2'
  , tr_type_cd    VARCHAR (1   )  DEFAULT '9' comment '발송유형. 9:배치, 1:실시간'
  , attachment    VARCHAR (4000) comment '링크 버튼 json'
  , app_user_id   VARCHAR (20  ) comment '앱유저아이디'
  , title         VARCHAR (50  ) comment '강조표기'
  , supplement    VARCHAR (4000) comment '바로연결 버튼 json'
  , list_seq                  VARCHAR(10)        NOT NULL comment '고객 일련번호'
  , customer_key              VARCHAR(50)        NOT NULL comment '고객키'
  , service_no               decimal(15,0)         NOT NULL comment '서비스 번호'
  , service_type               VARCHAR(2)         NOT NULL comment '서비스 유형'
  , result_seq                decimal(16,0)         NOT NULL   comment '발송 일련번호'
) engine=innodb DEFAULT charset=utf8 comment='알림톡발송요청';


/*
-------------------------------------------------------------------------------
-- pk & index
-------------------------------------------------------------------------------
*/

alter table mzsendtran add constraint pk_mzsendtran primary key (sn);

create index idx_mzsendtran_tran_sn on mzsendtran(
    tran_sn
);

create index idx_mzsendtran_req_dtm on mzsendtran(
    req_dtm
);

create index idx_mzsendtran_mlt1 on mzsendtran
(
    tran_sts, tr_type_cd, req_dtm
);





/*
-------------------------------------------------------------------------------
-- create mzsendlog
-------------------------------------------------------------------------------
*/

CREATE TABLE mzsendlog (
    sn            VARCHAR (100 )  NOT NULL comment '일련번호'
  , sender_key    VARCHAR (40  )  NOT NULL comment '발송프로필키'
  , channel       VARCHAR (1   )  NOT NULL comment '채널'
  , snd_type      VARCHAR (1   )  NOT NULL comment '알림톡 발송방식'
  , phone_num     VARCHAR (16  )  NOT NULL comment '수신자휴대폰번호'
  , tmpl_cd       VARCHAR (30  )  NOT NULL comment '알림톡탬플릿코드'
  , subject       VARCHAR (40  ) comment 'lms제목'
  , snd_msg       NVARCHAR (1000) comment '발송메시지'
  , sms_snd_msg   VARCHAR (2000  ) comment 'sms발송메시지'
  , sms_snd_num   VARCHAR (16  ) comment 'sms발신번호'
  , req_dept_cd   VARCHAR (50  )  NOT NULL comment '발송요청부서코드'
  , req_usr_id    VARCHAR (50  )  NOT NULL comment '발송요청자id'
  , req_dtm       VARCHAR (14  )  NOT NULL comment '발송요청일시'
  , snd_dtm       VARCHAR (14  ) comment 'agent 발송일시'
  , rslt_cd       VARCHAR (4   ) comment '알림톡처리결과코드'
  , rcpt_msg      VARCHAR (250 ) comment '알림톡처리결과메시'
  , rcpt_dtm      VARCHAR (14  ) comment 'agent 수신일시'
  , sms_snd_dtm   VARCHAR (14  ) comment 'sms발송일시'
  , sms_rslt_cd   VARCHAR (4   ) comment 'sms처리결과코드'
  , sms_rcpt_msg  VARCHAR (250 ) comment 'sms처리메시지'
  , sms_rcpt_dtm  VARCHAR (14  ) comment 'sms수신일시'
  , sms_gb        VARCHAR (1   ) comment 'sms 구분. s:sms, l:lms'
  , sms_snd_yn    VARCHAR (1   ) NOT NULL comment 'sms 발송여부'
  , tran_sn       decimal  (16,0) comment '처리일련번호'
  , tran_sts      VARCHAR (1   )  NOT NULL comment '처리상태'
  , agent_id      VARCHAR (20  ) comment 'agent 서버 식별자'
  , slot1         VARCHAR (100 ) comment '발송요청부가정보1'
  , slot2         VARCHAR (100 ) comment '발송요청부가정보2'
  , tr_type_cd    VARCHAR (1   ) comment '발송유형. 9:배치, 1:실시간'
  , attachment    VARCHAR (4000) comment '링크 버튼 json'
  , app_user_id   VARCHAR (20  ) comment '앱유저아이디'
  , title         VARCHAR (50  ) comment '강조표기'
  , supplement    VARCHAR (4000) comment '바로연결 버튼 json'
  , list_seq                  VARCHAR(10)        NOT NULL comment '고객 일련번호'
  , customer_key              VARCHAR(50)        NOT NULL comment '고객키'
  , service_no               decimal(15,0)         NOT NULL comment '서비스 번호'
  , service_type               VARCHAR(2)         NOT NULL comment '서비스 유형'
  , result_seq                decimal(16,0)         NOT NULL   comment '발송 일련번호'
) engine=innodb DEFAULT charset=utf8 comment='알림톡발송결과';


/*
-------------------------------------------------------------------------------
-- pk & index
-------------------------------------------------------------------------------
*/

create index idx_mzsendlog_sn on mzsendlog(
    sn
);

create index idx_mzsendlog_tran_sts on mzsendlog(
    tran_sts
);

create index idx_mzsendlog_rdt_rcd on mzsendlog(
    req_dtm, rslt_cd
);

create index idx_mzsendlog_snd_dtm on mzsendlog(
    snd_dtm
);

-- 발송 결과를 wiseu sendlog 테이블에 업데이트하는 작업 플래그
alter table mzsendlog add sync_flag CHAR(1) DEFAULT 'N' NOT NULL;
create index idx_mzsendlog_sync_flag on mzsendlog (sync_flag);
