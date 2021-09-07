-- wiseU 5.11.0 -> 5.12.0 업그레이드 시 사용

-- 분할 발송 관련 SQL
ALTER TABLE NVCAMPAIGN DROP (DIVIDE_SEND, DIVIDE_JOB_STS, DIVIDE_SEND_SEQ);

ALTER TABLE NVECAREMSG DROP COLUMN DIVIDE_SEND;
ALTER TABLE NVECAREMSG DROP COLUMN DIVIDE_JOB_STS;
ALTER TABLE NVECAREMSG DROP COLUMN DIVIDE_CNT;
ALTER TABLE NVECAREMSG DROP COLUMN DIVIDE_SEND_SEQ;

ALTER TABLE NVSERVERINFO DROP COLUMN USE_DIVIDE_SEND_YN;

ALTER TABLE NVEBRANCH DROP COLUMN DIVIDE_SEND;

ALTER TABLE NVCAMPAIGN ADD DIVIDE_YN CHAR(1) DEFAULT 'N';
 
-- 분할발송스케쥴
CREATE TABLE NVDIVIDESCHEDULE (
    CLIENT              VARCHAR(2)          NOT NULL,
    SERVICE_NO          NUMERIC(15)         NOT NULL,
    DIVIDE_SEQ          NUMERIC(2)          NOT NULL,
    TARGET_CNT          NUMERIC(10),
    START_DT            VARCHAR(14)
);
ALTER TABLE NVDIVIDESCHEDULE ADD CONSTRAINT PK_NVDIVIDESCHEDULE PRIMARY KEY (CLIENT, SERVICE_NO, DIVIDE_SEQ);
CREATE INDEX IDX_NVDIVIDESCHEDULE_START_DT ON NVDIVIDESCHEDULE(START_DT);

-- LTS분할발송서비스정보
CREATE TABLE NV_SVC_DIVIDE (
    TASK_ID             VARCHAR(31)         NOT NULL,
    SERVER_ID           VARCHAR(10)         NOT NULL,
    DIVIDE_SEQ          NUMERIC(2)          NOT NULL,
    CLIENT              VARCHAR(2),
    SERVICE_NO          NUMERIC(15),
    TARGET_CNT          NUMERIC(10),
    SEND_CNT            NUMERIC(10),
    START_DT            VARCHAR(14),
    END_DT              VARCHAR(14),
    STATUS              CHAR(1)
);
ALTER TABLE NV_SVC_DIVIDE ADD CONSTRAINT PK_NV_SVC_DIVIDE PRIMARY KEY (TASK_ID, SERVER_ID, DIVIDE_SEQ);
CREATE INDEX IDX_NV_SVC_DIVIDE_MLT1 ON NV_SVC_DIVIDE (CLIENT, SERVICE_NO, DIVIDE_SEQ);

INSERT INTO NV_CD_MST(CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('100111', 'V', '10011', '분할정지', '분할정지', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST(CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('100111', 'V', '10011', 'Divide Stop', 'Divide', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'en');

-- A/B 테스트 관련 SQL
ALTER TABLE NVCAMPAIGN          ADD AB_TEST_TYPE  CHAR(1);
ALTER TABLE NVCAMPAIGN          ADD AB_TEST_COND  CHAR(1);
ALTER TABLE NVCAMPAIGN          ADD AB_TEST_RATE  NUMERIC(3);
ALTER TABLE NVCAMPAIGN          ADD CAMPAIGN_PREFACE_AB  VARCHAR(250);
ALTER TABLE NVRECEIPT           ADD AB_TYPE VARCHAR(4);
ALTER TABLE NVLINKRESULT        ADD AB_TYPE VARCHAR(4);
ALTER TABLE NVDEFAULTHANDLER    ADD AB_TEST_YN VARCHAR(1) DEFAULT 'N';