-- 이케어시나리오
CREATE TABLE nvecarescenario (
    scenario_no               NUMERIC(15)         NOT NULL,
    user_id                   VARCHAR(15),
    grp_cd                    VARCHAR(12),
    scenario_nm               VARCHAR(100),
    scenario_desc             VARCHAR(250),
    scenario_type             CHAR(1),
    create_dt                 CHAR(8),
    lastupdate_dt             CHAR(8),
    finish_yn                 CHAR(1),
    finish_dt                 CHAR(8),
    create_tm                 CHAR(6),
    lastupdate_tm             CHAR(6),
    service_type              CHAR(1),
    tag_no                    NUMERIC(10),
    sub_type                  CHAR(1),
    handler_type              CHAR(1),
    chrg_nm                   VARCHAR(30),
    brc_nm                    VARCHAR(30)
);
ALTER TABLE nvecarescenario ADD CONSTRAINT pk_nvecarescenario PRIMARY KEY (scenario_no);

-- 이케어
CREATE TABLE nvecaremsg (
    ecare_no                  NUMERIC(15)         NOT NULL,
    user_id                   VARCHAR(15),
    grp_cd                    VARCHAR(12),
    segment_no                NUMERIC(15),
    ecare_nm                  VARCHAR(100),
    ecare_desc                VARCHAR(250),
    ecare_preface             VARCHAR(250),
    ecare_sts                 CHAR(1),
    create_dt                 CHAR(8),
    create_tm                 CHAR(6),
    lastupdate_dt             CHAR(8),
    lastupdate_tm             CHAR(6),
    campaign_type             CHAR(1),
    template_type             NUMERIC(2),
    sending_sts               CHAR(1),
    target_cnt                NUMERIC(10),
    share_yn                  CHAR(1),
    msgassort_cd              CHAR(1),
    log_yn                    CHAR(1),
    keepday                   NUMERIC(4),
    ecmschedule_no            NUMERIC(5),
    sender_nm                 VARCHAR(50),
    sender_email              VARCHAR(100),
    sending_mode              CHAR(1),
    retry_cnt                 NUMERIC(2),
    receiver_nm               VARCHAR(50),
    sender_tel                VARCHAR(50),
    retmail_receiver          VARCHAR(100),
    ecare_class               CHAR(1),
    relation_type             CHAR(1)             DEFAULT 'N',
    relation_tree             VARCHAR(100)        DEFAULT 'N',
    channel_type              CHAR(1),
    htmlmaker_type            CHAR(1),
    service_type              CHAR(1),
    account_dt                CHAR(2),
    ecare_level               CHAR(1)             DEFAULT 1,
    category_cd               VARCHAR(12),
    resend_yn                 CHAR(1),
    resend_cnt                NUMERIC(2),
    resend_tm                 NUMERIC(2),
    svc_id                    VARCHAR(20),
    sub_type                  CHAR(1),
    survey_end_yn             CHAR(1),
    survey_response_cnt       NUMERIC(10),
    survey_no                 NUMERIC(15),
    survey_start_dt           CHAR(8),
    survey_start_tm           CHAR(6),
    survey_end_dt             CHAR(8),
    survey_end_tm             CHAR(6),
    depth_no                  NUMERIC(2)          DEFAULT 1,
    editor_id                 VARCHAR(15),
    scenario_no               NUMERIC(15),
    verify_yn                 CHAR(1),
    verify_grp_cd             VARCHAR(12),
    send_server               NUMERIC(1),
    block_yn                  CHAR(1),
    verify_b4_send            CHAR(1)             DEFAULT 'N',
    delete_yn                 CHAR(1),
    security_mail_yn          CHAR(1)             DEFAULT 'N',
    req_dept_id               VARCHAR(50),
    req_user_id               VARCHAR(15),
    resend_ecare_no           NUMERIC(15),
    template_sender_key       VARCHAR(40),
    kakao_sender_key          VARCHAR(40),
    kakao_tmpl_cd             VARCHAR(50),
    kakao_image_no            NUMERIC(15)         DEFAULT 0,
    failback_send_yn          CHAR(1)             DEFAULT 'N',
    failback_subject          VARCHAR(100),
    tmpl_ver                  NUMERIC(5)          DEFAULT 0    NOT NULL,
    cover_ver                 NUMERIC(5)          DEFAULT 0    NOT NULL,
    handler_ver               NUMERIC(5)          DEFAULT 0    NOT NULL,
    preface_ver               NUMERIC(5)          DEFAULT 0    NOT NULL,
    handler_seq               NUMERIC(10),
    deploy_type               VARCHAR(10),
    mail_type                 VARCHAR(10) DEFAULT 'NONE',
    open_type               VARCHAR(10),
    slot1_field               VARCHAR(30),
    slot2_field               VARCHAR(30),
    slot3_field               VARCHAR(30),
    slot4_field               VARCHAR(30),
    slot5_field               VARCHAR(30),
    slot6_field               VARCHAR(30),
    slot7_field               VARCHAR(30),
    slot8_field               VARCHAR(30),
    slot9_field               VARCHAR(30),
    slot10_field              VARCHAR(30)
);
ALTER TABLE nvecaremsg ADD CONSTRAINT pk_nvecaremsg PRIMARY KEY (ecare_no);

-- 이케어태그
CREATE TABLE nvecaremsgtag (
    tag_no                    NUMERIC(10)         NOT NULL,
    tag_nm                    VARCHAR(50)
);
ALTER TABLE nvecaremsgtag ADD CONSTRAINT pk_nvecaremsgtag PRIMARY KEY (tag_no);

-- 이케어실시간발송맵
CREATE TABLE nvecarekmmap (
    ecare_no                  NUMERIC(15)         NOT NULL,
    itemfield_nm              VARCHAR(50)         NOT NULL,
    knowledgemap_id           VARCHAR(12)         NOT NULL,
    grp_cd                    VARCHAR(12),
    user_id                   VARCHAR(15),
    item_cd                   CHAR(2),
    item_nm                   VARCHAR(50),
    itemindent                NUMERIC(2),
    item_length               NUMERIC(5),
    item_type                 VARCHAR(50),
    item_format               VARCHAR(50),
    query_seq                 NUMERIC(2),
    item_pram_value           VARCHAR(100)
);
ALTER TABLE nvecarekmmap ADD CONSTRAINT pk_nvecarekmmap PRIMARY KEY (ecare_no, itemfield_nm);

-- 이케어스케쥴일정
CREATE TABLE nvcycleitem (
    ecmschedule_no            NUMERIC(5)          NOT NULL,
    cycleitem                 VARCHAR(10)         NOT NULL,
    check_yn                  CHAR(1)
);
ALTER TABLE nvcycleitem ADD CONSTRAINT pk_nvcycleitem PRIMARY KEY (ecmschedule_no, cycleitem);

-- 이케어스케쥴
CREATE TABLE nvecmschedule (
    ecmschedule_no            NUMERIC(5)          NOT NULL,
    ecmschedule_nm            VARCHAR(50),
    cycle_cd                  CHAR(1),
    sendstart_dt              CHAR(8),
    sendend_dt                CHAR(8),
    invoke_tm                 CHAR(6),
    invoke_every_min          CHAR(1),
    day                       NUMERIC(2),
    sche_weeknumber           NUMERIC(1),
    weekday                   VARCHAR(10),
    start_tm                  CHAR(6),
    end_tm                    CHAR(6),
    term_min                  CHAR(2)
);
ALTER TABLE nvecmschedule ADD CONSTRAINT pk_nvecmschedule PRIMARY KEY (ecmschedule_no);

-- 이케어핸들러
CREATE TABLE nvecmsghandler (
    ecare_no                  NUMERIC(15)         NOT NULL,
    type                      CHAR(1),
    appsource                 LONGTEXT
);
ALTER TABLE nvecmsghandler ADD CONSTRAINT pk_nvecmsghandler PRIMARY KEY (ecare_no);

-- 이케어기본핸들러
CREATE TABLE nvecmsghandlerbasic (
    seq                       NUMERIC(10)         NOT NULL,
    service_type              CHAR(1)             NOT NULL,
    default_yn                CHAR(1)             DEFAULT 'N'    NOT NULL,
    handler_desc              VARCHAR(250),
    appsource                 LONGTEXT
);
ALTER TABLE nvecmsghandlerbasic ADD CONSTRAINT pk_nvecmsghandlerbasic PRIMARY KEY (seq);

-- 이케어템플릿
CREATE TABLE nvecaretemplate (
    ECARE_NO                  NUMERIC(15)         NOT NULL,
    SEG                       VARCHAR(20)         NOT NULL,
    TEMPLATE                  LONGTEXT,
    KAKAO_BUTTONS             VARCHAR(4000),
    CONTS_NO                  NUMERIC(8)
);
ALTER TABLE nvecaretemplate ADD CONSTRAINT pk_nvecaretemplate PRIMARY KEY (ecare_no, seg);

-- 이케어반응추적기간
CREATE TABLE nvecaretraceinfo (
    ecare_no                  NUMERIC(15)         NOT NULL,
    trace_type                VARCHAR(10)         NOT NULL,
    start_dt                  CHAR(8),
    start_tm                  CHAR(6),
    end_dt                    CHAR(8),
    end_tm                    CHAR(6),
    term_type                 CHAR(1)
);
ALTER TABLE nvecaretraceinfo ADD CONSTRAINT pk_nvecaretraceinfo PRIMARY KEY (ecare_no, trace_type);

-- 이케어링크추적
CREATE TABLE nvecarelinktrace (
    ecare_no                  NUMERIC(15)         NOT NULL,
    link_seq                  NUMERIC(2)          NOT NULL,
    link_desc                 VARCHAR(2000),
    link_url                  VARCHAR(2000),
    link_title                VARCHAR(2000)
);
ALTER TABLE nvecarelinktrace ADD CONSTRAINT pk_nvecarelinktrace PRIMARY KEY (ecare_no, link_seq);

-- 이케어마임로그
CREATE TABLE nvecaremimeinfolog (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    sid                       VARCHAR(5)          NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100)        NOT NULL,
    send_dt                   CHAR(8),
    send_tm                   CHAR(6),
    handler_index             VARCHAR(2),
    file_index                VARCHAR(2),
    start_offset              NUMERIC(15),
    end_offset                NUMERIC(15),
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    mime_full_path            VARCHAR(250)
);
ALTER TABLE nvecaremimeinfolog ADD CONSTRAINT pk_nvecaremimeinfolog PRIMARY KEY (ecare_no, result_seq, list_seq, record_seq);

-- 이케어발송결과
CREATE TABLE nvecaresendresult (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    result_desc               VARCHAR(250),
    result_sts                CHAR(2),
    start_dt                  CHAR(8),
    start_tm                  CHAR(6),
    end_dt                    CHAR(8),
    end_tm                    CHAR(6),
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
    crtgrp_cd                 VARCHAR(12),
    editor_id                 VARCHAR(15),
    log_sts                   CHAR(2),
    super_seq                 NUMERIC(16),
    resend_retry_cnt          NUMERIC(2),
    resend_sts                CHAR(1),
    returnmail_cnt            NUMERIC(10)         DEFAULT 0,
    manualbatch_sts           CHAR(1),
    tmpl_ver                  NUMERIC(5)          DEFAULT 0    NOT NULL,
    cover_ver                 NUMERIC(5)          DEFAULT 0    NOT NULL,
    preface_ver               NUMERIC(5)          DEFAULT 0    NOT NULL,
    handler_ver               NUMERIC(5)          DEFAULT 0    NOT NULL
);
ALTER TABLE nvecaresendresult ADD CONSTRAINT pk_nvecaresendresult PRIMARY KEY (ecare_no, result_seq);
CREATE INDEX idx_nvecaresendresult_mlt1 ON nvecaresendresult (ecare_no, log_sts, result_sts, start_dt);
CREATE INDEX idx_nvecaresendresult_sdt_eno ON nvecaresendresult (start_dt, ecare_no);
CREATE INDEX idx_nvecaresendresult_edt_eno ON nvecaresendresult (end_dt, ecare_no);
CREATE INDEX idx_nvecaresendresult_manubat ON nvecaresendresult (manualbatch_sts);

-- 이케어발송로그
CREATE TABLE nvecaresendlog (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    sid                       VARCHAR(5)          NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100)        NOT NULL,
    send_dt                   CHAR(8),
    send_tm                   CHAR(6),
    end_dt                    CHAR(8),
    end_tm                    CHAR(6),
    error_cd                  VARCHAR(5),
    send_domain               VARCHAR(50),
    err_msg                   VARCHAR(250),
    resend_yn                 CHAR(1),
    req_dept_id               VARCHAR(50),
    req_user_id               VARCHAR(50),
    message_key               VARCHAR(20),
    seq                       VARCHAR(100),
    srfidd                    VARCHAR(50),
    open_dt                   VARCHAR(14),
    eai_send_fg               VARCHAR(1)         DEFAULT 'N' NOT NULL,
    sub_ecare_no              NUMERIC(15)        DEFAULT 0,
    sub_result_seq            NUMERIC(16)        DEFAULT 0,
    part_message              VARCHAR(500),
    fail_back_channel         VARCHAR(1),
    fail_back_result_cd       VARCHAR(5),
    fail_back_senddtm         VARCHAR(14),
    tmpl_cd                   VARCHAR(50),
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    slot3                     VARCHAR(100),
    slot4                     VARCHAR(100),
    slot5                     VARCHAR(100),
    slot6                     VARCHAR(100),
    slot7                     VARCHAR(100),
    slot8                     VARCHAR(100),
    slot9                     VARCHAR(100),
    slot10                    VARCHAR(100)
);
ALTER TABLE nvecaresendlog ADD CONSTRAINT pk_nvecaresendlog PRIMARY KEY (ecare_no, result_seq, list_seq, customer_key, record_seq);
CREATE INDEX idx_nvecaresendlog_sdt_cky ON nvecaresendlog (send_dt, customer_key);
CREATE INDEX idx_nvecaresendlog_sdt_cml ON nvecaresendlog (send_dt, customer_email);
CREATE INDEX idx_nvecaresendlog_sdt_eno ON nvecaresendlog (send_dt, ecare_no);
CREATE INDEX idx_nvecaresendlog_seq ON nvecaresendlog (seq);
CREATE INDEX idx_nvecaresendlog_message_key ON nvecaresendlog (message_key);
CREATE INDEX idx_nvecaresendlog_sdt_efg  ON nvecaresendlog (send_dt, eai_send_fg);
CREATE INDEX idx_nvecaresendlog_sub1_sub2 ON nvecaresendlog (sub_result_seq, sub_ecare_no);

-- 이케어테스트발송로그
CREATE TABLE nvtestecaresendlog (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_email            VARCHAR(100)        NOT NULL,
    customer_nm               VARCHAR(100),
    sid                       VARCHAR(5)          NOT NULL,
    send_dt                   CHAR(8),
    send_tm                   CHAR(6),
    send_domain               VARCHAR(50),
    error_cd                  VARCHAR(5),
    err_msg                   VARCHAR(250),
    resend_yn                 CHAR(1),
    req_dept_id               VARCHAR(50),
    req_user_id               VARCHAR(50),
    message_key               VARCHAR(20),
    seq                       VARCHAR(200),
    part_message              VARCHAR(500),
    tmpl_cd                   VARCHAR(50),
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    slot3                     VARCHAR(100),
    slot4                     VARCHAR(100),
    slot5                     VARCHAR(100),
    slot6                     VARCHAR(100),
    slot7                     VARCHAR(100),
    slot8                     VARCHAR(100),
    slot9                     VARCHAR(100),
    slot10                    VARCHAR(100)

);
ALTER TABLE nvtestecaresendlog ADD CONSTRAINT pk_nvtestecaresendlog PRIMARY KEY (ecare_no, result_seq, list_seq, customer_key, record_seq);
CREATE INDEX idx_nvtestecaresendlog_send_dt ON nvtestecaresendlog (send_dt);

-- 이케어발송결과상세
CREATE TABLE nvecaresendresultdetail (
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    error_cd                  VARCHAR(4)          NOT NULL,
    cnt                       NUMERIC(10),
    channel_type              CHAR(1)
);
ALTER TABLE nvecaresendresultdetail ADD CONSTRAINT pk_nvecaresendresultdetail PRIMARY KEY (ecare_no, result_seq, error_cd);

-- 이케어첨부파일
CREATE TABLE nvecaremultipartfile (
    ecare_no                  NUMERIC(15)         NOT NULL,
    seq                       NUMERIC(10)         NOT NULL,
    file_alias                VARCHAR(100)        NOT NULL,
    file_path                 VARCHAR(250)        NOT NULL,
    file_size                 NUMERIC(15)         NOT NULL,
    file_name                 VARCHAR(100)        NOT NULL,
    file_type                 VARCHAR(10)         NOT NULL,
    enc_yn                    VARCHAR(1)          DEFAULT 'N' NOT NULL,
    secu_field                VARCHAR(30)
);
ALTER TABLE nvecaremultipartfile ADD CONSTRAINT pk_nvecaremltpartfile PRIMARY KEY (ecare_no, seq, file_alias);

-- 이케어핸들러이력
CREATE TABLE nvecmsghandlerhistory (
    ecare_no                  NUMERIC(15)         NOT NULL,
    user_id                   VARCHAR(15)         NOT NULL,
    lastupdate_dt             CHAR(8),
    lastupdate_tm             VARCHAR(9),
    type                      CHAR(1)             NOT NULL,
    appsource                 LONGTEXT,
    history_msg               VARCHAR(1000),
    handler_ver               NUMERIC(5)          DEFAULT 0    NOT NULL
);
CREATE INDEX idx_nvecmsghandlerhistory_eno ON nvecmsghandlerhistory (ecare_no);
ALTER TABLE nvecmsghandlerhistory ADD CONSTRAINT pk_nvecmsghandlerhistory PRIMARY KEY (ecare_no, handler_ver);

-- 이케어템플릿이력
CREATE TABLE nvecaretemplatehistory (
    ecare_no                  NUMERIC(15)         NOT NULL,
    user_id                   VARCHAR(15)         NOT NULL,
    lastupdate_dt             CHAR(8),
    lastupdate_tm             VARCHAR(9),
    seg                       VARCHAR(20)         NOT NULL,
    template                  LONGTEXT,
    history_msg               VARCHAR(1000),
    tmpl_ver                  NUMERIC(5)          DEFAULT 0    NOT NULL,
    kakao_buttons             VARCHAR(4000),
    conts_no                  NUMERIC(8)
);
CREATE INDEX idx_nvecaretemplatehistory_eno ON nvecaretemplatehistory (ecare_no);
ALTER TABLE nvecaretemplatehistory ADD CONSTRAINT pk_nvecaretemplatehistory PRIMARY KEY (ecare_no, seg, tmpl_ver);

-- 부가데이터 쿼리 테이블
CREATE TABLE nvaddquery (
    ecare_no               NUMERIC(15)         NOT NULL,
    query_seq              NUMERIC(2)          NOT NULL,
    query_type             VARCHAR(10)         NOT NULL,
    execute_type           VARCHAR(10)         NOT NULL,
    result_id              VARCHAR(10),
    dbinfo_seq             NUMERIC(2)          NOT NULL,
    query                  VARCHAR(4000)       NOT NULL
);
ALTER TABLE nvaddquery ADD CONSTRAINT pk_nvaddquery PRIMARY KEY (ecare_no,query_seq);