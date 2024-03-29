-- 친구톡 템플릿코드 컬럼 사이즈 조정
ALTER TABLE MZFTSENDTRAN        ALTER COLUMN TMPL_CD SET DATA TYPE VARCHAR (30);
ALTER TABLE MZFTSENDTRAN        ALTER COLUMN TMPL_CD SET DEFAULT NULL;
ALTER TABLE MZFTSENDLOG         ALTER COLUMN TMPL_CD SET DATA TYPE VARCHAR (30);
ALTER TABLE MZFTSENDLOG         ALTER COLUMN TMPL_CD SET DEFAULT NULL;

-- 친구톡 디폴트 설정 제거
ALTER TABLE MZFTSENDTRAN    ALTER COLUMN SERVICE_TYPE SET DATA TYPE VARCHAR(2);
ALTER TABLE MZFTSENDTRAN    ALTER COLUMN SERVICE_TYPE SET DEFAULT NULL;
ALTER TABLE MZFTSENDTRAN    ALTER COLUMN SERVICE_NO SET DATA TYPE NUMERIC(15);
ALTER TABLE MZFTSENDTRAN    ALTER COLUMN SERVICE_NO SET DEFAULT NULL;

-- View 문법 오류 수정
CREATE OR REPLACE VIEW VIEW_SMS_LOG (
    TRAN_PHONE, TRAN_CALLBACK, TRAN_RSLTDATE, REQ_USER_ID, TRAN_RSLT,
    TRAN_MSG, SMS_TYPE, SLOT1, SLOT2, SEQ, LIST_SEQ
) AS
SELECT
    TRAN_PHONE, TRAN_CALLBACK, TRAN_RSLTDATE, REQ_USER_ID, TRAN_RSLT,
    (CASE WHEN MMS_BODY IS NULL THEN TRAN_MSG ELSE MMS_BODY END) AS TRAN_MSG, TRAN_ETC3 AS SMS_TYPE, SLOT1, SLOT2, SEQ, LOG.LIST_SEQ
FROM EM_LOG LOG LEFT OUTER JOIN EM_TRAN_MMS MMS
ON LOG.TRAN_ETC4 = MMS.MMS_SEQ;