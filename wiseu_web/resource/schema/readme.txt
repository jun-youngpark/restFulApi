---------------------------------------------------------------------
-- Ȯ���� �ʿ��� ���� (TS�� Ȯ����)
---------------------------------------------------------------------
1. LGU SMS�� V_SMS_FAIL_FILE_LOG ����� ���� ���� (01_wiseU5_create_view_lgu.sql)
  - Oracle : tr_rsltstat = '06', rslt = '1000'
  - MSSQL  : tr_rsltstat <> '06', rslt <> '1000'

2. SKT SMS�� �ʱⵥ��Ÿ �Է������� MSSQL/MySQL���� �ߺ� �߻�
    INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
    VALUES ('AS0001', 'A', 'AS00', '�ڵ��� ��ȣ ó����', '�ڵ��� ��ȣ ó����', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'ko');
    INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
    VALUES ('AS0001', 'a', 'AS00', '�Ͻ� ���� ����', '�Ͻ� ���� ����', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'ko');

3. NVSENDLOG/NVECARESENDLOG ���̺� MESSAGE_KEY �÷��� ���� �ε��� ������ �ʿ����� ����?
   - com.mnwise.common.batch.IF.MmsResultBatch, com.mnwise.common.batch.IF.MmsResultBatch.SmsResultBatch Ŭ������
     �ش� �÷����� FULL SCAN �ϴ� ������ �߰ߵ�

---------------------------------------------------------------------
-- �������
---------------------------------------------------------------------
1. Ű/�ε��� �ڸ����� �ִ� 30�ڸ� (Oracle ����)
2. ���� �÷��� NUMERIC ���� ����
3. PK ���� ���� ���� ���
    ALTER TABLE ���̺�� ADD CONSTRAINT PK_���̺�� PRIMARY KEY (�÷�s);
4. �ε��� ���� ���� ���� ���
    CREATE INDEX �ε����� ON ���̺�� (�÷�s);
5. FK ���� ���� ���� ���
    ALTER TABLE ���̺�� ADD CONSTRAINT FK�� FOREIGN KEY (�÷�s) REFERENCES �θ����̺�� (�θ��÷�s);

---------------------------------------------------------------------
-- DB�� �������
---------------------------------------------------------------------
1. VARCHAR �÷�
  - Oracle : VARCHAR2
  - MSSQL/MySQL/DB2 : VARCHAR
2. LOB �÷�
  - Oracle : LONG, CLOB
  - MSSQL  : TEXT
  - MySQL  : LONGTEXT
  - DB2    : CLOB(ũ��) NOT LOGGED
3. ��������(YYYYMMDD) ���ϱ�
  - Oracle : TO_CHAR(SYSDATE, 'YYYYMMDD')
  - MSSQL  : CONVERT(CHAR(8), GETDATE(),112)
  - MySQL  : DATE_FORMAT(NOW(), '%Y%m%d')
  - DB2    : TO_CHAR(CURRENT DATE, 'YYYYMMDD')
4. �����Ͻ�(YYYYMMDDHH24MISS) ���ϱ�
  - Oracle : TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
  - MSSQL  : REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ','')
  - MySQL  : DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')
  - DB2    : TO_CHAR(CURRENT TIMESTAMP, 'YYYYMMDDHH24MISS')
5. ���ڿ� ����
  - Oracle/DB2 : '���ڿ�1' || '���ڿ�2'
  - MSSQL  : '���ڿ�1' + '���ڿ�2'
  - MySQL  : CONCAT('���ڿ�1', '���ڿ�2')
6. Validate Query
  - Oracle/MySQL : SELECT 1 FROM dual
  - MSSQL  : SELECT 1
  - DB2    : SELECT 1 FROM sysibm.sysdummy1
7. PK ���� ���� ���
  - Oracle/MSSQL/DB2 : ALTER TABLE ���̺�� DROP CONSTRAINT PK��;
  - MySQL            : ALTER TABLE ���̺�� DROP PRIMARY KEY;
8. �ε��� ���� ���� ���
  - Oracle/DB2  : DROP INDEX �ε�����;
  - MSSQL/MySQL : DROP INDEX �ε����� ON ���̺��;
9. FK ���� ���� ���
  - Oracle/MSSQL/DB2 : ALTER TABLE ���̺�� DROP CONSTRAINT FK��;
  - MySQL            : ALTER TABLE ���̺�� DROP FOREIGN KEY FK��;
10. ����� �÷� : NVBLOCKSERVICE ���̺� KEY �÷� (01_wiseU5_create_table_old.sql)
  - Oracle/DB2  : KEY
  - MSSQL/MySQL : KEY1
11. ����� �÷� : NVSEMANTIC/NVEBR_SEMANTIC ���̺� MAXVALUE �÷� (04_wiseU5_create_table_etc.sql, 01_wiseU5_create_table_ebranch.sql)
  - Oracle/MSSQL/DB2 : MAXVALUE
  - MySQL            : `MAXVALUE`
12. �÷� DEFAULT�� �����Ͻ�(YYYYMMDDHH24MISS) �ο� : NV_CD_MST ���̺� REG_DTM �÷� (04_wiseU5_create_table_etc.sql)
  - Oracle    : DEFAULT TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')
  - MSSQL     : DEFAULT REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ','')
  - MySQL/DB2 : �ο� �Ұ�
13. ���ڿ� �÷� �����ε��� : NVECARERETURNMAIL ���̺� IDX_NVECARERETURNMAIL_RDT_RTM �ε��� (05_wiseU5_create_table_receive.sql)
  - Oracle          : CREATE INDEX IDX_NVECARERETURNMAIL_RDT_RTM ON NVECARERETURNMAIL (RECEIVE_DT||RECEIVE_TM);
  - MSSQL/MySQL/DB2 : ���� �Ұ�

---------------------------------------------------------------------
-- DB�� �÷� �߰�
---------------------------------------------------------------------
1. 2015-03-23
- NVECARERPTOPEN ���̺� �÷� �߰�
- START_DT, OPEN_DCNT

---------------------------------------------------------------------
-- DB�� �ε��� �߰�
---------------------------------------------------------------------
1. 2015-03-23
- NVECARERPTOPEN ���̺� �ε��� �߰�
- CREATE INDEX IDX_NVECARERPTOPEN_SDT_ENO ON  NVECARERPTOPEN (START_DT,ECARE_NO);