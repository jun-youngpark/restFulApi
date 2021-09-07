/* 마이스 제품 설치 DB에서 실행 */
INSERT INTO TMB_NOTIFY_STATUS ( NOTI_TYPE, NOTI_TYPE_NM, STS_CD, UPDR_ID, UPD_DT, TTL ) VALUES ( '01', '일반', '000', admin, SYSDATE, 60 );
INSERT INTO TMB_NOTIFY_STATUS ( NOTI_TYPE, NOTI_TYPE_NM, STS_CD, UPDR_ID, UPD_DT, TTL ) VALUES ( '02', '정보', '000', admin, SYSDATE, 60 );
INSERT INTO TMB_NOTIFY_STATUS ( NOTI_TYPE, NOTI_TYPE_NM, STS_CD, UPDR_ID, UPD_DT, TTL ) VALUES ( '03', '마케팅', '000', admin, SYSDATE, 60 );
/* 마이스 제품 설치 DB에서 실행  끝 */

/**********************************/
/* Table Name: 이케어수신확인 */
/**********************************/
ALTER TABLE NVECARERECEIPT      ADD     CLIENT_INFO  VARCHAR2(200);
ALTER TABLE NVECARERECEIPT      ADD     MOBILE_YN    VARCHAR2(1);

COMMENT ON COLUMN NVECARERECEIPT.CLIENT_INFO is '클라이언트종류';
COMMENT ON COLUMN NVECARERECEIPT.MOBILE_YN is '모바일 유무';

ALTER TABLE NVSERVERINFO ADD PUSH_RESEND_ERROR_CD  VARCHAR2(500);

ALTER TABLE NVPUSHAPP ADD USE_TESTMODE VARCHAR2(1) DEFAULT 'N' NOT NULL;

-- PUSH 공통 코드 분류
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG) VALUES ('AP01', '-', '00', 'PUSH 공통 코드', 'PUSH에서 사용하는 공통 코드', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , null, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG) VALUES ('AP0101', '-', 'AP01', 'PUSH 메시지 유형', 'PUSH 메시지 유형', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , null, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG) VALUES ('AP0101','01','AP0101','일반','일반', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 1, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG) VALUES ('AP0101','02','AP0101','정보','정보', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 2, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG) VALUES ('AP0101','03','AP0101','마케팅','마케팅', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 3, 'ko');

UPDATE NVSERVERINFO SET PUSH_RESEND_ERROR_CD='250,000,700';