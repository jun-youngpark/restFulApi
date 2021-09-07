-- 캠페인도메인별발송로그
CREATE TABLE nvdomainlog (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    domain_nm                 VARCHAR(50)         NOT NULL,
    error_cd                  VARCHAR(4)          NOT NULL,
    send_cnt                  NUMERIC(10)
);
ALTER TABLE nvdomainlog ADD CONSTRAINT pk_nvdomainlog PRIMARY KEY (campaign_no, result_seq, domain_nm, error_cd);

-- 캠페인유효수신통계
CREATE TABLE nvrptduration (
    campaign_no               NUMERIC(15)         NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    durationinfo_cd           CHAR(2),
    validopen_cnt             NUMERIC(10),
    validopen_ocnt            NUMERIC(10),
    result_seq                NUMERIC(16)         NOT NULL
);
ALTER TABLE nvrptduration ADD CONSTRAINT pk_nvrptduration PRIMARY KEY (campaign_no, result_seq, report_dt, report_tm);
CREATE INDEX idx_nvrptduration_report_dt ON nvrptduration (report_dt);

-- 캠페인링크클릭통계
CREATE TABLE nvrptlink (
    campaign_no               NUMERIC(15)         NOT NULL,
    link_seq                  NUMERIC(3)          NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    link_cnt                  NUMERIC(10),
    link_ocnt                 NUMERIC(10),
    result_seq                NUMERIC(16)         NOT NULL
);
ALTER TABLE nvrptlink ADD CONSTRAINT pk_nvrptlink PRIMARY KEY (campaign_no, result_seq, link_seq, report_dt, report_tm);
CREATE INDEX idx_nvrptlink_rdt_cno ON nvrptlink (report_dt, campaign_no);

-- 캠페인링크클릭비중복통계
CREATE TABLE nvrptlinkdistinct (
    campaign_no               NUMERIC(15)         NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    link_cnt                  NUMERIC(10),
    link_ocnt                 NUMERIC(10),
    result_seq                NUMERIC(16)         NOT NULL
);
ALTER TABLE nvrptlinkdistinct ADD CONSTRAINT pk_nvrptlinkdistinct PRIMARY KEY (campaign_no, result_seq, report_dt, report_tm);
CREATE INDEX idx_nvrptlinkdistinct_rpt_dt ON nvrptlinkdistinct (report_dt);

-- 캠페인수신확인통계
CREATE TABLE nvrptopen (
    campaign_no               NUMERIC(15)         NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    open_cnt                  NUMERIC(10),
    refuse_cnt                NUMERIC(10),
    open_ocnt                 NUMERIC(10),
    result_seq                NUMERIC(16)         NOT NULL
);
ALTER TABLE nvrptopen ADD CONSTRAINT pk_nvrptopen PRIMARY KEY (campaign_no, result_seq, report_dt, report_tm);
CREATE INDEX idx_nvrptopen_report_dt ON nvrptopen (report_dt);

-- 이케어도메인별발송로그
CREATE TABLE nvecaredomainlog (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    domain_nm                 VARCHAR(50)         NOT NULL,
    error_cd                  VARCHAR(4)          NOT NULL,
    send_dt                   CHAR(8)             NOT NULL,
    send_cnt                  NUMERIC(10)         NOT NULL
);
ALTER TABLE nvecaredomainlog ADD CONSTRAINT pk_nvecaredomainlog PRIMARY KEY (ecare_no, result_seq, domain_nm, error_cd, send_dt);
CREATE INDEX idx_nvecaredomainlog_sdt_eno ON nvecaredomainlog (send_dt, ecare_no);

-- 이케어링크클릭통계
CREATE TABLE nvecarerptlink (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    link_seq                  NUMERIC(2)          NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    link_cnt                  NUMERIC(10),
    link_ocnt                 NUMERIC(10)
);
ALTER TABLE nvecarerptlink ADD CONSTRAINT pk_nvecarerptlink PRIMARY KEY (ecare_no, result_seq, link_seq, report_dt, report_tm);
CREATE INDEX idx_nvecarerptlink_rdt_eno ON nvecarerptlink (report_dt, ecare_no);

-- 이케어수신확인통계
CREATE TABLE nvecarerptopen (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    start_dt                  CHAR(8)             NOT NULL,
    open_cnt                  NUMERIC(10),
    refuse_cnt                NUMERIC(10),
    open_ocnt                 NUMERIC(10),
    open_dcnt                 NUMERIC(10)
);
ALTER TABLE nvecarerptopen ADD CONSTRAINT pk_nvecarerptopen PRIMARY KEY (ecare_no, result_seq, report_dt, report_tm);
CREATE INDEX idx_nvecarerptopen_rdt_eno ON nvecarerptopen (report_dt, ecare_no);
CREATE INDEX idx_nvecarerptopen_sdt_eno ON  nvecarerptopen (start_dt,ecare_no);

-- 이케어발송결과통계
CREATE TABLE nvecarerptsendresult (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    result_desc               VARCHAR(250),
    result_sts                CHAR(2),
    target_cnt                NUMERIC(10),
    send_cnt                  NUMERIC(10),
    success_cnt               NUMERIC(10),
    fail_cnt                  NUMERIC(10),
    unknown_user_cnt          NUMERIC(10),
    unknown_host_cnt          NUMERIC(10),
    smtp_except_cnt           NUMERIC(10),
    no_route_cnt              NUMERIC(10),
    refused_cnt               NUMERIC(10),
    etc_except_cnt            NUMERIC(10),
    invalid_address_cnt       NUMERIC(10),
    log_sts                   CHAR(2),
    returnmail_cnt            NUMERIC(10)         DEFAULT 0,
    open_cnt                  NUMERIC(10)         DEFAULT 0,
    duration_cnt              NUMERIC(10)         DEFAULT 0,
    link_cnt                  NUMERIC(10)         DEFAULT 0,
    start_dt                  VARCHAR(8),
    start_tm                  VARCHAR(6),
    end_dt                    VARCHAR(8),
    end_tm                    VARCHAR(6),
    resend_sts                CHAR(1)
);
ALTER TABLE nvecarerptsendresult ADD CONSTRAINT pk_nvecarerptsendresult PRIMARY KEY (ecare_no, result_seq, report_dt);
CREATE INDEX idx_nvecarerptsendresult_mlt1 ON nvecarerptsendresult (report_dt, ecare_no);

-- 이케어유효수신통계
CREATE TABLE nvecarerptduration (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    report_dt                 CHAR(8)             NOT NULL,
    report_tm                 CHAR(2)             NOT NULL,
    durationinfo_cd           CHAR(2),
    validopen_cnt             NUMERIC(10),
    validopen_ocnt            NUMERIC(10)
);
ALTER TABLE nvecarerptduration ADD CONSTRAINT pk_nvecarerptduration PRIMARY KEY (ecare_no, result_seq, report_dt, report_tm);
CREATE INDEX idx_nvecarerptduration_rdt_eno ON nvecarerptduration (report_dt, ecare_no);

-- SMS발송결과통계
CREATE TABLE nvsmsrptsendresult (
    report_dt                 CHAR(8)             NOT NULL,
    service_no                NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    service_type              CHAR(2)             NOT NULL,
    target_cnt                NUMERIC(10),
    send_cnt                  NUMERIC(10),
    success_cnt               NUMERIC(10),
    fail_cnt                  NUMERIC(10),
    send_gbn                  VARCHAR(3)          NOT NULL,
    send_tm                   CHAR(6)             NOT NULL
);
ALTER TABLE nvsmsrptsendresult ADD CONSTRAINT pk_nvsmsrptsendresult PRIMARY KEY (report_dt, service_no, result_seq, service_type, send_gbn);
CREATE INDEX idx_nvsmsrptsendresult_sno_sgb ON nvsmsrptsendresult (service_no, send_gbn);
CREATE INDEX idx_nvsmsrptsendresult_sno_rsq ON nvsmsrptsendresult (service_no, result_seq);
CREATE INDEX idx_nvsmsrptsendresult_sno_stp ON nvsmsrptsendresult (service_no, service_type);

-- 알림톡/친구톡 발송통계
CREATE TABLE nvkakaorptsendresult(
    report_dt                 CHAR(8)             NOT NULL,
    service_no                NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    service_type              CHAR(2)             NOT NULL,
    req_dept_id               VARCHAR(50)         NOT NULL,
    tmpl_cd                   VARCHAR(50)         DEFAULT 'FT'    NOT NULL,
    target_cnt                NUMERIC(10),
    send_cnt                  NUMERIC(10),
    success_cnt               NUMERIC(10),
    fail_cnt                  NUMERIC(10),
    sms_target_cnt            NUMERIC(10),
    sms_send_cnt              NUMERIC(10),
    sms_success_cnt           NUMERIC(10),
    sms_fail_cnt              NUMERIC(10),
    lms_target_cnt            NUMERIC(10),
    lms_send_cnt              NUMERIC(10),
    lms_success_cnt           NUMERIC(10),
    lms_fail_cnt              NUMERIC(10),
    mms_target_cnt            NUMERIC(10),
    mms_send_cnt              NUMERIC(10),
    mms_success_cnt           NUMERIC(10),
    mms_fail_cnt              NUMERIC(10),
    send_gbn                  VARCHAR(3)          NOT NULL,
    send_tm                   CHAR(6)             NOT NULL
);
ALTER TABLE nvkakaorptsendresult ADD CONSTRAINT pk_nvkakaorptsendresult PRIMARY KEY (report_dt, service_no, result_seq, service_type,req_dept_id, tmpl_cd);
CREATE INDEX idx_nvkakakorpt_sno_sgb ON nvkakaorptsendresult (service_no, send_gbn);
CREATE INDEX idx_nvkakakorpt_sno_rsq ON nvkakaorptsendresult (service_no, result_seq);
CREATE INDEX idx_nvkakakorpt_sno_stp ON nvkakaorptsendresult (service_no, service_type);