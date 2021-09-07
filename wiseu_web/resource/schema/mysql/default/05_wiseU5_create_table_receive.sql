-- 이케어링크반응결과
CREATE TABLE nvecarelinkresult (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    link_seq                  NUMERIC(2)          NOT NULL,
    link_dt                   CHAR(8)             NOT NULL,
    link_tm                   CHAR(6)             NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0',
    customer_id               VARCHAR(50)         NOT NULL,
    customer_email            VARCHAR(100)        NOT NULL,
    customer_nm               VARCHAR(100),
    click_cnt                 NUMERIC(10)
);
ALTER TABLE nvecarelinkresult ADD CONSTRAINT pk_nvecarelinkresult PRIMARY KEY (ecare_no, result_seq, list_seq, link_dt, link_tm);
CREATE INDEX idx_nvecarelinkresult_ldt_eno ON nvecarelinkresult (link_dt, ecare_no);

-- 이케어수신확인
CREATE TABLE nvecarereceipt (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    open_dt                   CHAR(8)             NOT NULL,
    open_tm                   CHAR(6)             NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0',
    customer_email            VARCHAR(100)        NOT NULL,
    customer_nm               VARCHAR(100),
    null_yn                   CHAR(1),
    reading_dt                CHAR(8),
    reading_tm                CHAR(6),
    reading_duration          NUMERIC(10),
    valid_cnt                 NUMERIC(10),
    log_send_fg               VARCHAR(1)          DEFAULT 'N',
    sub_ecare_no              NUMERIC(15)         DEFAULT 0,
    sub_result_seq            NUMERIC(16)         DEFAULT 0
);
ALTER TABLE nvecarereceipt ADD CONSTRAINT pk_nvecarereceipt PRIMARY KEY (ecare_no, result_seq, customer_id, list_seq, open_dt, open_tm);
CREATE INDEX idx_nvecarereceipt_mlt1 ON nvecarereceipt (open_dt, ecare_no, reading_duration);
CREATE INDEX idx_nvecarereceipt_sub1_sub2 ON nvecarereceipt (sub_result_seq, sub_ecare_no);

-- 이케어수신거부
CREATE TABLE nvecarereject (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_email            VARCHAR(100)        NOT NULL,
    customer_nm               VARCHAR(100)        NOT NULL,
    reject_dt                 CHAR(8)             NOT NULL,
    reject_tm                 CHAR(6)             NOT NULL
);
ALTER TABLE nvecarereject ADD CONSTRAINT pk_nvecarereject PRIMARY KEY (ecare_no, result_seq, list_seq, customer_id, record_seq);
CREATE INDEX idx_nvecarereject_rdt_eno ON nvecarereject (reject_dt, ecare_no);

-- 이케어반송메일
CREATE TABLE nvecarereturnmail (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_email            VARCHAR(100)        NOT NULL,
    customer_nm               VARCHAR(100)        NOT NULL,
    receive_dt                CHAR(8)             NOT NULL,
    receive_tm                CHAR(6)             NOT NULL,
    smtpcode                  CHAR(3)             NOT NULL,
    update_yn                 CHAR(1)             DEFAULT 'N',
    return_msg                VARCHAR(200)
);
ALTER TABLE nvecarereturnmail ADD CONSTRAINT pk_nvecarereturnmail PRIMARY KEY (ecare_no, result_seq, list_seq, customer_id, record_seq);
CREATE INDEX idx_nvecarereturnmail_rdt_eno ON nvecarereturnmail (receive_dt, ecare_no);

-- 캠페인링크반응결과
CREATE TABLE nvlinkresult (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    link_dt                   CHAR(8)             NOT NULL,
    link_tm                   CHAR(6)             NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0',
    link_seq                  NUMERIC(3)          NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    customer_nm               VARCHAR(100)        NOT NULL,
    customer_email            VARCHAR(100)        NOT NULL,
    click_cnt                 NUMERIC(10),
    ab_type                   VARCHAR(4)
);
ALTER TABLE nvlinkresult ADD CONSTRAINT pk_nvlinkresult PRIMARY KEY (campaign_no, result_seq, list_seq, link_dt, link_tm);
CREATE INDEX idx_nvlinkresult_ldt_cno ON nvlinkresult (link_dt, campaign_no);

-- 캠페인수신확인
CREATE TABLE nvreceipt (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    open_dt                   CHAR(8)             NOT NULL,
    open_tm                   CHAR(6)             NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0',
    customer_nm               VARCHAR(100)        NOT NULL,
    customer_email            VARCHAR(100)        NOT NULL,
    reading_dt                CHAR(8),
    reading_tm                CHAR(6),
    reading_duration          NUMERIC(10),
    valid_cnt                 NUMERIC(10),
    client_info               VARCHAR(200),
    mobile_yn                 CHAR(1)             DEFAULT 'N',
    ab_type                   VARCHAR(4),
    log_send_fg               VARCHAR(1)          DEFAULT 'N' NOT NULL
);
ALTER TABLE nvreceipt ADD CONSTRAINT pk_nvreceipt PRIMARY KEY (campaign_no, result_seq, customer_id, list_seq, open_dt, open_tm);
CREATE INDEX idx_nvreceipt_odt_cno ON nvreceipt (open_dt, campaign_no);

-- 캠페인수신거부
CREATE TABLE nvreject (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100)        NOT NULL,
    reject_dt                 CHAR(8),
    reject_tm                 CHAR(6),
    reject_reason             VARCHAR(250)
);
ALTER TABLE nvreject ADD CONSTRAINT pk_nvreject PRIMARY KEY (campaign_no, result_seq, list_seq, customer_id, record_seq);
CREATE INDEX idx_nvreject_rdt_cno ON nvreject (reject_dt, campaign_no);

-- 캠페인반송메일
CREATE TABLE nvreturnmail (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100),
    receive_dt                CHAR(8),
    receive_tm                CHAR(6),
    smtpcode                  CHAR(3),
    update_yn                 CHAR(1)             DEFAULT 'N',
    return_msg                VARCHAR(200)
);
ALTER TABLE nvreturnmail ADD CONSTRAINT pk_nvreturnmail PRIMARY KEY (campaign_no, result_seq, list_seq, customer_id, record_seq);
CREATE INDEX idx_nvreturnmail_rdt_cno ON nvreturnmail (receive_dt, campaign_no);