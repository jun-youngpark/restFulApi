-- 알림톡, 친구톡 챗버블 저장할 컬럼 추가
ALTER TABLE NVMOBILECONTENTS         ADD KAKAO_BUTTONS VARCHAR(4000);
ALTER TABLE NVECARETEMPLATE          ADD KAKAO_BUTTONS VARCHAR(4000);
ALTER TABLE NVTEMPLATE               ADD KAKAO_BUTTONS VARCHAR(4000);
ALTER TABLE NVECARETEMPLATEHISTORY   ADD KAKAO_BUTTONS VARCHAR(4000);

-- 알림톡 템플릿 이름의 길이가 최대 200자라서 이를 저장하는 NVMOBILECONTENTS.CONTS_NM 컬럼 길이를 50 에서 600 으로 변경
ALTER TABLE NVMOBILECONTENTS        MODIFY COLUMN CONTS_NM VARCHAR(600);

-- 캠페인 친구톡 채널 컴럼 추가
ALTER TABLE NVCAMPAIGN     ADD KAKAO_SENDER_KEY    VARCHAR(40);
ALTER TABLE NVCAMPAIGN     ADD KAKAO_TMPL_CD       VARCHAR(30);
ALTER TABLE NVCAMPAIGN     ADD KAKAO_IMAGE_NO      NUMERIC(15,0) DEFAULT 0;
ALTER TABLE NVCAMPAIGN     ADD FAILBACK_SEND_YN    CHAR(1) DEFAULT 'N';
ALTER TABLE NVCAMPAIGN     ADD FAILBACK_SUBJECT    VARCHAR(100);

-- 캠페인 친구톡 채널 컴럼 변경
ALTER TABLE NVCAMPAIGN MODIFY COLUMN CAMPAIGN_CLASS VARCHAR(10);

-- 친구톡 발송 mms 우회발송시 사용할 이미지경로
ALTER TABLE MZFTSENDTRAN    ADD IMAGE_PATH VARCHAR(250);

-- 이케어 옴니채널 추가 컬럼
ALTER TABLE NVECAREMSG        MODIFY COLUMN RELATION_TYPE CHAR(1) DEFAULT 'N';
ALTER TABLE NVECAREMSG        ADD RELATION_TREE VARCHAR(100) DEFAULT 'N';
ALTER TABLE NVECARESENDLOG    ADD SUB_ECARE_NO NUMERIC(15) DEFAULT 0;
ALTER TABLE NVECARESENDLOG    ADD SUB_RESULT_SEQ NUMERIC(16)  DEFAULT 0;
ALTER TABLE NVECARERECEIPT    ADD SUB_ECARE_NO NUMERIC(15) DEFAULT 0;
ALTER TABLE NVECARERECEIPT    ADD SUB_RESULT_SEQ NUMERIC(16)  DEFAULT 0;

-- 이케어 옴니채널 인덱스 추가
CREATE INDEX IDX_NVECARESENDLOG_SUB1_SUB2 ON NVECARESENDLOG (SUB_RESULT_SEQ, SUB_ECARE_NO);
CREATE INDEX IDX_NVECARERECEIPT_SUB1_SUB2 ON NVECARERECEIPT (SUB_RESULT_SEQ, SUB_ECARE_NO);


-- 이케어 준실시간 옴니채널용 기준 세그먼트
INSERT INTO NVSEGMENT (SEGMENT_NO, USER_ID, GRP_CD, SEGMENT_NM, DBINFO_SEQ, SEGMENT_DESC, SQLHEAD, SQLTAIL, SQLBODY, SQLFILTER, LASTUPDATE_DT, SEGMENT_SIZE, CRTGRP_CD, SEGMENT_TYPE, FILETODB_YN, EDITOR_ID, SHARE_YN, ACTIVE_YN, CATEGORY_CD, SEGMENT_STS, TAG_NO, PSEGMENT_NO, PLINK_SEQ, SEG_TYPE, TESTQUERY, UPDATEQUERY)
VALUES(-1, 'admin', '01', 'DEFAULT TARGET', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 이케어 준실시간 옴니채널용 기준 시멘틱
INSERT INTO NVSEMANTIC (SEGMENT_NO, FIELD_SEQ, FIELD_NM, FIELD_DESC, FIELD_TYPE, FIELD_LENGTH, INITVALUE, `MINVALUE`, `MAXVALUE`, NULL_YN, FIELD_KEY)
VALUES(-1, 1, 'RECEIVER_ID', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'K');
INSERT INTO NVSEMANTIC (SEGMENT_NO, FIELD_SEQ, FIELD_NM, FIELD_DESC, FIELD_TYPE, FIELD_LENGTH, INITVALUE, `MINVALUE`, `MAXVALUE`, NULL_YN, FIELD_KEY)
VALUES(-1, 2, 'RECEIVER_NM', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO NVSEMANTIC (SEGMENT_NO, FIELD_SEQ, FIELD_NM, FIELD_DESC, FIELD_TYPE, FIELD_LENGTH, INITVALUE, `MINVALUE`, `MAXVALUE`, NULL_YN, FIELD_KEY)
VALUES(-1, 3, 'RECEIVER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'E');