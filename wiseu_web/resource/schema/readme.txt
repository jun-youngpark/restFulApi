---------------------------------------------------------------------
-- 확인이 필요한 사항 (TS팀 확인중)
---------------------------------------------------------------------
1. LGU SMS의 V_SMS_FAIL_FILE_LOG 뷰생성 쿼리 상이 (01_wiseU5_create_view_lgu.sql)
  - Oracle : tr_rsltstat = '06', rslt = '1000'
  - MSSQL  : tr_rsltstat <> '06', rslt <> '1000'

2. SKT SMS의 초기데이타 입력쿼리가 MSSQL/MySQL에서 중복 발생
    INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
    VALUES ('AS0001', 'A', 'AS00', '핸드폰 번호 처리중', '핸드폰 번호 처리중', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'ko');
    INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
    VALUES ('AS0001', 'a', 'AS00', '일시 서비스 정지', '일시 서비스 정지', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'ko');

3. NVSENDLOG/NVECARESENDLOG 테이블에 MESSAGE_KEY 컬럼에 대해 인덱스 생성이 필요한지 여부?
   - com.mnwise.common.batch.IF.MmsResultBatch, com.mnwise.common.batch.IF.MmsResultBatch.SmsResultBatch 클래스내
     해당 컬럼으로 FULL SCAN 하는 쿼리가 발견됨

---------------------------------------------------------------------
-- 공통사항
---------------------------------------------------------------------
1. 키/인덱스 자리수는 최대 30자리 (Oracle 제약)
2. 숫자 컬럼은 NUMERIC 으로 통일
3. PK 생성 공통 쿼리 양식
    ALTER TABLE 테이블명 ADD CONSTRAINT PK_테이블명 PRIMARY KEY (컬럼s);
4. 인덱스 생성 공통 쿼리 양식
    CREATE INDEX 인덱스명 ON 테이블명 (컬럼s);
5. FK 생성 공통 쿼리 양식
    ALTER TABLE 테이블명 ADD CONSTRAINT FK명 FOREIGN KEY (컬럼s) REFERENCES 부모테이블명 (부모컬럼s);

---------------------------------------------------------------------
-- DB별 제약사항
---------------------------------------------------------------------
1. VARCHAR 컬럼
  - Oracle : VARCHAR2
  - MSSQL/MySQL/DB2 : VARCHAR
2. LOB 컬럼
  - Oracle : LONG, CLOB
  - MSSQL  : TEXT
  - MySQL  : LONGTEXT
  - DB2    : CLOB(크기) NOT LOGGED
3. 현재일자(YYYYMMDD) 구하기
  - Oracle : TO_CHAR(SYSDATE, 'YYYYMMDD')
  - MSSQL  : CONVERT(CHAR(8), GETDATE(),112)
  - MySQL  : DATE_FORMAT(NOW(), '%Y%m%d')
  - DB2    : TO_CHAR(CURRENT DATE, 'YYYYMMDD')
4. 현재일시(YYYYMMDDHH24MISS) 구하기
  - Oracle : TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
  - MSSQL  : REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ','')
  - MySQL  : DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')
  - DB2    : TO_CHAR(CURRENT TIMESTAMP, 'YYYYMMDDHH24MISS')
5. 문자열 연결
  - Oracle/DB2 : '문자열1' || '문자열2'
  - MSSQL  : '문자열1' + '문자열2'
  - MySQL  : CONCAT('문자열1', '문자열2')
6. Validate Query
  - Oracle/MySQL : SELECT 1 FROM dual
  - MSSQL  : SELECT 1
  - DB2    : SELECT 1 FROM sysibm.sysdummy1
7. PK 삭제 쿼리 양식
  - Oracle/MSSQL/DB2 : ALTER TABLE 테이블명 DROP CONSTRAINT PK명;
  - MySQL            : ALTER TABLE 테이블명 DROP PRIMARY KEY;
8. 인덱스 삭제 쿼리 양식
  - Oracle/DB2  : DROP INDEX 인덱스명;
  - MSSQL/MySQL : DROP INDEX 인덱스명 ON 테이블명;
9. FK 삭제 쿼리 양식
  - Oracle/MSSQL/DB2 : ALTER TABLE 테이블명 DROP CONSTRAINT FK명;
  - MySQL            : ALTER TABLE 테이블명 DROP FOREIGN KEY FK명;
10. 예약어 컬럼 : NVBLOCKSERVICE 테이블 KEY 컬럼 (01_wiseU5_create_table_old.sql)
  - Oracle/DB2  : KEY
  - MSSQL/MySQL : KEY1
11. 예약어 컬럼 : NVSEMANTIC/NVEBR_SEMANTIC 테이블 MAXVALUE 컬럼 (04_wiseU5_create_table_etc.sql, 01_wiseU5_create_table_ebranch.sql)
  - Oracle/MSSQL/DB2 : MAXVALUE
  - MySQL            : `MAXVALUE`
12. 컬럼 DEFAULT로 현재일시(YYYYMMDDHH24MISS) 부여 : NV_CD_MST 테이블 REG_DTM 컬럼 (04_wiseU5_create_table_etc.sql)
  - Oracle    : DEFAULT TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
  - MSSQL     : DEFAULT REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ','')
  - MySQL/DB2 : 부여 불가
13. 문자열 컬럼 연결인덱스 : NVECARERETURNMAIL 테이블 IDX_NVECARERETURNMAIL_RDT_RTM 인덱스 (05_wiseU5_create_table_receive.sql)
  - Oracle          : CREATE INDEX IDX_NVECARERETURNMAIL_RDT_RTM ON NVECARERETURNMAIL (RECEIVE_DT||RECEIVE_TM);
  - MSSQL/MySQL/DB2 : 생성 불가

---------------------------------------------------------------------
-- DB별 컬럼 추가
---------------------------------------------------------------------
1. 2015-03-23
- NVECARERPTOPEN 테이블 컬럼 추가
- START_DT, OPEN_DCNT

---------------------------------------------------------------------
-- DB별 인덱스 추가
---------------------------------------------------------------------
1. 2015-03-23
- NVECARERPTOPEN 테이블 인덱스 추가
- CREATE INDEX IDX_NVECARERPTOPEN_SDT_ENO ON  NVECARERPTOPEN (START_DT,ECARE_NO);