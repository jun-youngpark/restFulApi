/*
-------------------------------------------------------------------------------
-- DBRO 제품 테이블
-------------------------------------------------------------------------------
*/

CREATE TABLE em_log
(
    `tran_pr`          INT(11)        NOT NULL,
    `tran_refkey`      VARCHAR(40)    ,
    `tran_id`          VARCHAR(20)    ,
    `tran_phone`       VARCHAR(100)   ,
    `tran_callback`    VARCHAR(100)   ,
    `tran_status`      CHAR(1)        ,
    `tran_date`        DATETIME       NOT NULL,
    `tran_rsltdate`    DATETIME       ,
    `tran_reportdate`  DATETIME       ,
    `tran_rslt`        CHAR(1)        ,
    `tran_net`         VARCHAR(3)     ,
    `tran_msg`         VARCHAR(255)   ,
    `tran_etc1`        VARCHAR(64)    ,
    `tran_etc2`        VARCHAR(16)    ,
    `tran_etc3`        VARCHAR(16)    ,
    `tran_etc4`        INT(11)        DEFAULT NULL,
    `tran_type`        INT(11)        NOT NULL
);


CREATE TABLE em_tran
(
    `tran_pr`          INT(11)        NOT NULL AUTO_INCREMENT,
    `tran_refkey`      VARCHAR(40)    ,
    `tran_id`          VARCHAR(20)    ,
    `tran_phone`       VARCHAR(100)   ,
    `tran_callback`    VARCHAR(100)   ,
    `tran_status`      CHAR(1)        ,
    `tran_date`        DATETIME       NOT NULL,
    `tran_rsltdate`    DATETIME       ,
    `tran_reportdate`  DATETIME       ,
    `tran_rslt`        CHAR(1)        ,
    `tran_net`         VARCHAR(3)     ,
    `tran_msg`         VARCHAR(255)   ,
    `tran_etc1`        VARCHAR(64)    ,
    `tran_etc2`        VARCHAR(16)    ,
    `tran_etc3`        VARCHAR(16)    ,
    `tran_etc4`        INT(11)        DEFAULT NULL,
    `tran_type`        INT(11)        NOT NULL,
    PRIMARY KEY `pk_em_tran` (`tran_pr`)
);
CREATE INDEX idx_em_tran_01 ON em_tran (tran_status, tran_date);


CREATE TABLE em_tran_mms
(
    `mms_seq`        INT(11)       NOT NULL AUTO_INCREMENT,
    `file_cnt`       INT(11)       NOT NULL,
    `build_yn`       CHAR(1)       ,
    `mms_body`       TEXT          ,
    `mms_subject`    VARCHAR(40)   ,
    `file_type1`     VARCHAR(3)    ,
    `file_type2`     VARCHAR(3)    ,
    `file_type3`     VARCHAR(3)    ,
    `file_type4`     VARCHAR(3)    ,
    `file_type5`     VARCHAR(3)    ,
    `file_name1`     VARCHAR(100)  ,
    `file_name2`     VARCHAR(100)  ,
    `file_name3`     VARCHAR(100)  ,
    `file_name4`     VARCHAR(100)  ,
    `file_name5`     VARCHAR(100)  ,
    `service_dep1`   VARCHAR(3)    ,
    `service_dep2`   VARCHAR(3)    ,
    `service_dep3`   VARCHAR(3)    ,
    `service_dep4`   VARCHAR(3)    ,
    `service_dep5`   VARCHAR(3)    ,
    `skn_file_name`  VARCHAR(255)  ,
    PRIMARY KEY `pk_em_tran_mms` (`mms_seq`)
);


/*
-------------------------------------------------------------------------------
-- WISEU 연동
-------------------------------------------------------------------------------
*/
-- WISEU 연동 기본 컬럼
ALTER TABLE em_log     ADD COLUMN `req_dept_cd`          VARCHAR(20);
ALTER TABLE em_log     ADD COLUMN `service_type`         CHAR(2);
ALTER TABLE em_log     ADD COLUMN `service_no`           DECIMAL(15)    DEFAULT NULL;
ALTER TABLE em_log     ADD COLUMN `result_seq`           DECIMAL(16)    DEFAULT NULL;
ALTER TABLE em_log     ADD COLUMN `list_seq`             VARCHAR(10);
ALTER TABLE em_log     ADD COLUMN `seq`                  VARCHAR(100);
ALTER TABLE em_log     ADD COLUMN `svc_id`               VARCHAR(20);
ALTER TABLE em_log     ADD COLUMN `slot1`                VARCHAR(100);
ALTER TABLE em_log     ADD COLUMN `slot2`                VARCHAR(100);
ALTER TABLE em_log     ADD COLUMN `req_user_id`          VARCHAR(100);
ALTER TABLE em_log     ADD COLUMN `req_dept_id`          VARCHAR(50);

ALTER TABLE em_tran    ADD COLUMN `req_dept_cd`         VARCHAR(20)    DEFAULT '#admin';
ALTER TABLE em_tran    ADD COLUMN `service_type`        CHAR(2)        ;
ALTER TABLE em_tran    ADD COLUMN `service_no`          DECIMAL(15)    DEFAULT NULL;
ALTER TABLE em_tran    ADD COLUMN `result_seq`          DECIMAL(16)    DEFAULT NULL;
ALTER TABLE em_tran    ADD COLUMN `list_seq`            VARCHAR(10)    ;
ALTER TABLE em_tran    ADD COLUMN `seq`                 VARCHAR(100)   ;
ALTER TABLE em_tran    ADD COLUMN `svc_id`              VARCHAR(20)    ;
ALTER TABLE em_tran    ADD COLUMN `slot1`               VARCHAR(100)   ;
ALTER TABLE em_tran    ADD COLUMN `slot2`               VARCHAR(100)   ;
ALTER TABLE em_tran    ADD COLUMN `req_user_id`         VARCHAR(100)   ;
ALTER TABLE em_tran    ADD COLUMN `req_dept_id`         VARCHAR(50)    ;

-- wiseMOKA 기능 개발 과정에 추가
ALTER TABLE em_tran_mms    ADD COLUMN   list_seq           VARCHAR(10);
ALTER TABLE em_tran_mms    ADD COLUMN   result_seq         NUMERIC(16);
ALTER TABLE em_tran_mms    ADD COLUMN   result_seq_page    NUMERIC(5);
CREATE INDEX idx_em_tran_mms_rslt ON em_tran_mms (result_seq, result_seq_page);

-- EM_LOG 테이블의 발송 결과를 wiseU NVSENDLOG, NVECARESENDLOG 테이블에 업데이트하는 작업 플래그
ALTER TABLE em_log ADD sync_flag CHAR(1) DEFAULT 'N' NOT NULL;
CREATE INDEX idx_em_log_sync_flag ON em_log (sync_flag);

-- SMS/LMS/MMS 재발송 시 사용
CREATE INDEX idx_emlog_srls ON em_log (service_no, result_seq, list_seq, service_type);
CREATE INDEX idx_emlog_tdt ON em_log (tran_date);
