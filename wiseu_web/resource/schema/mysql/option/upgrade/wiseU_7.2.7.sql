-- wiseU 7.2.4 -> 7.2.7 업그레이드 시 사용

-- 재발송 PK 필드추가
 ALTER TABLE nvresendrequest    ADD REQ_USER_ID VARCHAR(15) NOT NULL;
 ALTER TABLE nvresendrequest ADD CONSTRAINT PK_nvresendrequest PRIMARY KEY (REQ_DT, REQ_USER_ID);