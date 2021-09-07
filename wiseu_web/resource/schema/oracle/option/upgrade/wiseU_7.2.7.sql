-- wiseU 7.2.4 -> 7.2.7 업그레이드 시 사용

-- 재발송 PK 필드추가
 ALTER TABLE nvresendrequest    ADD REQ_USER_ID VARCHAR(15) NOT NULL;
 ALTER TABLE nvresendrequest ADD CONSTRAINT PK_nvresendrequest PRIMARY KEY (REQ_DT, REQ_USER_ID);


 --오라클 LONG 형 CLOB 변경 쿼리
ALTER TABLE NVAPPLICATION MODIFY APPSOURCE CLOB;
ALTER TABLE NVTEMPLATE MODIFY TEMPLATE CLOB;
ALTER TABLE NVECMSGHANDLER MODIFY APPSOURCE CLOB;
ALTER TABLE NVECMSGHANDLERBASIC MODIFY APPSOURCE CLOB;
ALTER TABLE NVECARETEMPLATE MODIFY TEMPLATE CLOB;
ALTER TABLE NVECMSGHANDLERHISTORY MODIFY APPSOURCE CLOB;
ALTER TABLE NVECARETEMPLATEHISTORY MODIFY TEMPLATE CLOB;
ALTER TABLE NVBADWORD MODIFY BAD_WORDS CLOB;
ALTER TABLE NVDEFAULTHANDLER MODIFY HANDLER CLOB;
ALTER TABLE NVMPSSCHEDULERINFO MODIFY SCHEDULER_QUERY CLOB;
ALTER TABLE NVREALTIMEACCEPT MODIFY JONMUN CLOB;
ALTER TABLE NVREALTIMEACCEPTDATA MODIFY DATA CLOB;
ALTER TABLE NVSCHEDULEACCEPT MODIFY JONMUN CLOB;
ALTER TABLE NVSCHEDULEACCEPTDATA MODIFY DATA CLOB;

--인덱스가 깨졌을 경우 리빌딩 쿼리
alter INDEX PK_NVTEMPLATE REBUILD;
alter INDEX PK_NVAPPLICATION REBUILD;
alter INDEX PK_NVECMSGHANDLER REBUILD;
alter INDEX PK_NVECARETEMPLATE REBUILD;
alter INDEX PK_NVECMSGHANDLERHISTORY REBUILD;
alter INDEX PK_NVECARETEMPLATEHISTORY REBUILD;