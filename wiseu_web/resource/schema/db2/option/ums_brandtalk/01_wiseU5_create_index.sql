CREATE TABLE MZBTSENDTRAN
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

ALTER TABLE MZBTSENDTRAN ADD CONSTRAINT MZBTSENDTRAN PRIMARY KEY (SN);
CREATE INDEX IDX_MZBTSENDTRAN_MLT1 ON MZBTSENDTRAN (TRAN_STS, REQ_DTM);
CREATE INDEX IDX_MZBTSENDTRAN_MLT2 ON MZBTSENDTRAN (REQ_DTM, AGENT_ID);
CREATE INDEX IDX_MZBTSENDTRAN_MSG_ID ON MZBTSENDTRAN (MSG_ID);
CREATE INDEX IDX_MZBTSENDTRAN_REQ_ID ON MZBTSENDTRAN (REQ_ID);
CREATE INDEX IDX_MZBTSENDTRAN_TRAN_SN ON MZBTSENDTRAN (TRAN_SN);

comment on table MZBTSENDTRAN is '브랜드톡 발송요청';
comment on column MZBTSENDTRAN.SN is '발송요청ID';
comment on column MZBTSENDTRAN.SENDER_KEY is '발신프로필키';
comment on column MZBTSENDTRAN.CHANNEL is '채널';
comment on column MZBTSENDTRAN.PHONE_NUM is '수신자휴대폰번호';
comment on column MZBTSENDTRAN.MSG_TYPE is '메시지 발송타입';
comment on column MZBTSENDTRAN.TMPL_CD is '템플릿코드';
comment on column MZBTSENDTRAN.REQ_DEPT_CD is '발송요청부서코드';
comment on column MZBTSENDTRAN.REQ_USR_ID  is '발송요청자ID';
comment on column MZBTSENDTRAN.REQ_DTM is '발송요청일시';
comment on column MZBTSENDTRAN.SND_DTM is '발송일시';
comment on column MZBTSENDTRAN.RSLT_CD is '발송결과코드';
comment on column MZBTSENDTRAN.RCPT_MSG is '발송결과메시지';
comment on column MZBTSENDTRAN.RCPT_DTM is '결과응답일시';
comment on column MZBTSENDTRAN.TRAN_SN is '전송일련번호';
comment on column MZBTSENDTRAN.TRAN_STS is '전송상태';
comment on column MZBTSENDTRAN.AGENT_ID is '에이전트ID';
comment on column MZBTSENDTRAN.SLOT1 is '발송요청부가정보1';
comment on column MZBTSENDTRAN.SLOT2 is '발송요청부가정보2';
comment on column MZBTSENDTRAN.REQ_ID is '요청별 ID';
comment on column MZBTSENDTRAN.MSG_ID is '메시지 ID';
comment on column MZBTSENDTRAN.CONT_TYPE is '텍스트유형(F:고정형, V:변수형)';
comment on column MZBTSENDTRAN.TMPL_TYPE is '변수형 옵션(J:전문 방식, V:변수 방식)';
comment on column MZBTSENDTRAN.SND_MSG is '템플릿 본문';
comment on column MZBTSENDTRAN.ATTACHMENT is '메시지에 첨부할 내용';
comment on column MZBTSENDTRAN.MSG_VR is '템플릿 본문에 포함된 변수값';
comment on column MZBTSENDTRAN.BTN_VR is '템플릿 버튼 링크에 포함된 변수값';
comment on column MZBTSENDTRAN.TARGET_TYPE is '타겟팅 옵션 (BD: 타겟팅 안함(기본), BN: 채널 친구 제외)';
comment on column MZBTSENDTRAN.BT_TYPE is '브랜드톡유형';

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

comment on table MZBTSENDLOG is '브랜드톡 발송결과';
comment on column MZBTSENDLOG.SN is '발송요청ID';
comment on column MZBTSENDLOG.SENDER_KEY is '발신프로필키';
comment on column MZBTSENDLOG.CHANNEL is '채널';
comment on column MZBTSENDLOG.PHONE_NUM is '수신자휴대폰번호';
comment on column MZBTSENDLOG.MSG_TYPE is '메시지 발송타입';
comment on column MZBTSENDLOG.TMPL_CD is '템플릿코드';
comment on column MZBTSENDLOG.REQ_DEPT_CD is '발송요청부서코드';
comment on column MZBTSENDLOG.REQ_USR_ID  is '발송요청자ID';
comment on column MZBTSENDLOG.REQ_DTM is '발송요청일시';
comment on column MZBTSENDLOG.SND_DTM is '발송일시';
comment on column MZBTSENDLOG.RSLT_CD is '발송결과코드';
comment on column MZBTSENDLOG.RCPT_MSG is '발송결과메시지';
comment on column MZBTSENDLOG.RCPT_DTM is '결과응답일시';
comment on column MZBTSENDLOG.TRAN_SN is '전송일련번호';
comment on column MZBTSENDLOG.TRAN_STS is '전송상태';
comment on column MZBTSENDLOG.AGENT_ID is '에이전트ID';
comment on column MZBTSENDLOG.SLOT1 is '발송요청부가정보1';
comment on column MZBTSENDLOG.SLOT2 is '발송요청부가정보2';
comment on column MZBTSENDLOG.REQ_ID is '요청별 ID';
comment on column MZBTSENDLOG.MSG_ID is '메시지 ID';
comment on column MZBTSENDLOG.CONT_TYPE is '텍스트유형(F:고정형, V:변수형)';
comment on column MZBTSENDLOG.TMPL_TYPE is '변수형 옵션(J:전문 방식, V:변수 방식)';
comment on column MZBTSENDLOG.SND_MSG is '템플릿 본문';
comment on column MZBTSENDLOG.ATTACHMENT is '메시지에 첨부할 내용';
comment on column MZBTSENDLOG.MSG_VR is '템플릿 본문에 포함된 변수값';
comment on column MZBTSENDLOG.BTN_VR is '템플릿 버튼 링크에 포함된 변수값';
comment on column MZBTSENDLOG.TARGET_TYPE is '타겟팅 옵션 (BD: 타겟팅 안함(기본), BN: 채널 친구 제외)';
comment on column MZBTSENDLOG.BT_TYPE is '브랜드톡유형';


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

