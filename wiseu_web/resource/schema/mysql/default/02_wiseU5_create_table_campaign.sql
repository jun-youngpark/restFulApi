-- 캠페인시나리오
CREATE TABLE nvscenario (
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
    tag_no                    NUMERIC(10),
    create_tm                 CHAR(6),
    lastupdate_tm             CHAR(6),
    handler_type              CHAR(1)             DEFAULT 'G'
);
ALTER TABLE nvscenario ADD CONSTRAINT pk_nvscenario PRIMARY KEY (scenario_no);

-- 캠페인
CREATE TABLE nvcampaign (
    campaign_no               NUMERIC(15)         NOT NULL,
    grp_cd                    VARCHAR(12),
    user_id                   VARCHAR(15),
    segment_no                NUMERIC(15),
    campaign_nm               VARCHAR(100),
    campaign_desc             VARCHAR(250),
    campaign_preface          VARCHAR(250),
    campaign_sts              CHAR(1),
    create_dt                 CHAR(8),
    create_tm                 CHAR(6),
    lastupdate_dt             CHAR(8),
    lastupdate_tm             CHAR(6),
    campaign_type             CHAR(1),
    template_type             NUMERIC(2),
    sending_sts               CHAR(1),
    sending_cycle             CHAR(20),
    sendstart_dt              CHAR(8),
    sendstart_tm              CHAR(6),
    sendfinish_dt             CHAR(8),
    sendfinish_tm             CHAR(6),
    target_cnt                NUMERIC(10),
    survey_end_yn             CHAR(1),
    sending_mode              CHAR(1),
    survey_response_cnt       NUMERIC(10),
    survey_no                 NUMERIC(15),
    log_yn                    CHAR(1),
    keepday                   NUMERIC(4),
    share_yn                  CHAR(1),
    survey_start_dt           CHAR(8),
    survey_start_tm           CHAR(6),
    survey_end_dt             CHAR(8),
    survey_end_tm             CHAR(6),
    retry_cnt                 NUMERIC(2),
    rptcreate_dt              CHAR(8),
    sender_nm                 VARCHAR(50),
    sender_email              VARCHAR(100),
    receiver_nm               VARCHAR(50),
    sender_tel                VARCHAR(50),
    retmail_receiver          VARCHAR(100),
    htmlupdate_yn             CHAR(1),
    report_sts                CHAR(1),
    campaign_class            VARCHAR(10),
    channel_type              CHAR(1),
    relation_type             CHAR(1)             DEFAULT 'N',
    relation_tree             VARCHAR(100)        DEFAULT 'N',
    retmail_send_yn           CHAR(1),
    etc_info1                 VARCHAR(250),
    etc_info2                 VARCHAR(250),
    promotion_type            CHAR(1),
    campaign_level            CHAR(1)             DEFAULT 0,
    category_cd               VARCHAR(12),
    scenario_no               NUMERIC(15),
    depth_no                  NUMERIC(2)          DEFAULT 1,
    editor_id                 VARCHAR(15),
    send_server               NUMERIC(1),
    approval_sts              CHAR(1),
    req_dept_id               VARCHAR(50),
    req_user_id               VARCHAR(15),
    divide_yn                 CHAR(1)             DEFAULT 'N',
    divide_interval           NUMERIC(8),
    divide_cnt                NUMERIC(10),
    ab_test_type              CHAR(1)             DEFAULT 'N',
    ab_test_cond              CHAR(1),
    ab_test_rate              NUMERIC(3),
    campaign_preface_ab       VARCHAR(250),
    multi_cont_info           CHAR(10),
    handler_seq               NUMERIC(10),
    kakao_sender_key          VARCHAR(40),
    kakao_tmpl_cd             VARCHAR(50),
    kakao_image_no            NUMERIC(15,0)       DEFAULT 0,
    failback_send_yn          CHAR(1)             DEFAULT 'N',
    failback_subject          VARCHAR(100)
);
ALTER TABLE nvcampaign ADD CONSTRAINT pk_nvcampaign PRIMARY KEY (campaign_no);
CREATE INDEX idx_nvcampaign_scenario_no ON nvcampaign (scenario_no);

-- 캠페인태그
CREATE TABLE nvcamptag (
    tag_no                    NUMERIC(10)         NOT NULL,
    tag_nm                    VARCHAR(50)
);
ALTER TABLE nvcamptag ADD CONSTRAINT pk_nvcamptag PRIMARY KEY (tag_no);

-- 캠페인스케쥴
CREATE TABLE nvschedule (
    scenario_no               NUMERIC(15)         NOT NULL,
    schedule_seq              NUMERIC(15)         NOT NULL,
    campaign_no               NUMERIC(15),
    supracampaign_no          NUMERIC(15),
    campaign_dt               CHAR(8),
    position_x                NUMERIC(5),
    position_y                NUMERIC(5),
    sizewidth                 NUMERIC(10),
    sizeheight                NUMERIC(10),
    duration_tm               NUMERIC(5),
    receipt_yn                CHAR(1),
    fromsql                   VARCHAR(1000),
    wheresql                  VARCHAR(1000)
);
ALTER TABLE nvschedule ADD CONSTRAINT pk_nvschedule PRIMARY KEY (scenario_no, schedule_seq);

-- 캠페인핸들러
CREATE TABLE nvapplication (
    campaign_no               NUMERIC(15)         NOT NULL,
    type                      CHAR(1),
    appsource                 LONGTEXT
);
ALTER TABLE nvapplication ADD CONSTRAINT pk_nvapplication PRIMARY KEY (campaign_no);

-- 캠페인템플릿
CREATE TABLE nvtemplate (
    campaign_no               NUMERIC(15)         NOT NULL,
    seg                       VARCHAR(20)         NOT NULL,
    template                  LONGTEXT,
    kakao_buttons             VARCHAR(4000)
);
ALTER TABLE nvtemplate ADD CONSTRAINT pk_nvtemplate PRIMARY KEY (campaign_no, seg);

-- 캠페인첨부파일
CREATE TABLE nvmultipartfile (
    campaign_no               NUMERIC(15)         NOT NULL,
    seq                       NUMERIC(10)         NOT NULL,
    file_alias                VARCHAR(100)        NOT NULL,
    file_path                 VARCHAR(250)        NOT NULL,
    file_size                 NUMERIC(15)         NOT NULL,
    file_name                 VARCHAR(100)        NOT NULL
);
ALTER TABLE nvmultipartfile ADD CONSTRAINT pk_nvmltpartfile PRIMARY KEY (campaign_no, seq, file_alias);

-- 캠페인반응추적기간
CREATE TABLE nvtraceinfo (
    campaign_no               NUMERIC(15)         NOT NULL,
    trace_type                VARCHAR(10)         NOT NULL,
    start_dt                  CHAR(8),
    start_tm                  CHAR(6),
    end_dt                    CHAR(8),
    end_tm                    CHAR(6)
);
ALTER TABLE nvtraceinfo ADD CONSTRAINT pk_nvtraceinfo PRIMARY KEY (campaign_no, trace_type);

-- 링크조건
CREATE TABLE nvlinkcondition (
    scenario_no               NUMERIC(15)         NOT NULL,
    schedule_seq              NUMERIC(15)         NOT NULL,
    link_seq                  NUMERIC(3)          NOT NULL,
    tracking_yn               CHAR(1)
);
ALTER TABLE nvlinkcondition ADD CONSTRAINT pk_nvlinkcondition PRIMARY KEY (scenario_no, schedule_seq, link_seq);

-- 캠페인링크추적
CREATE TABLE nvlinktrace (
    campaign_no               NUMERIC(15)         NOT NULL,
    link_seq                  NUMERIC(3)          NOT NULL,
    link_url                  VARCHAR(2000),
    link_desc                 VARCHAR(2000),
    link_title                VARCHAR(2000)
);
ALTER TABLE nvlinktrace ADD CONSTRAINT pk_nvlinktrace PRIMARY KEY (campaign_no, link_seq);

-- 캠페인마임로그
CREATE TABLE nvmimeinfolog (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100)        NOT NULL,
    sid                       VARCHAR(5)          NOT NULL,
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
ALTER TABLE nvmimeinfolog ADD CONSTRAINT pk_nvmimeinfolog PRIMARY KEY (campaign_no, result_seq, customer_key, list_seq, record_seq);

-- 캠페인발송결과
CREATE TABLE nvsendresult (
    campaign_no               NUMERIC(15)         NOT NULL,
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
    log_sts                   CHAR(2),
    domain_sts                CHAR(2),
    returnmail_cnt            NUMERIC(10)         DEFAULT 0,
    open_cnt                  NUMERIC(10)         DEFAULT 0,
    duration_cnt              NUMERIC(10)         DEFAULT 0,
    link_cnt                  NUMERIC(10)         DEFAULT 0,
    manualbatch_sts           CHAR(1),
    resend_sts                CHAR(1)             DEFAULT NULL,
    super_seq                 NUMERIC(16)
);
ALTER TABLE nvsendresult ADD CONSTRAINT pk_nvsendresult PRIMARY KEY (campaign_no, result_seq);

-- 캠페인발송로그
CREATE TABLE nvsendlog (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100)        NOT NULL,
    sid                       VARCHAR(5)          NOT NULL,
    send_dt                   CHAR(8),
    send_tm                   CHAR(6),
    end_dt                    CHAR(8),
    end_tm                    CHAR(6),
    error_cd                  VARCHAR(5),
    send_domain               VARCHAR(50),
    err_msg                   VARCHAR(250),
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    resend_yn                 CHAR(1),
    req_dept_id               VARCHAR(50),
    req_user_id               VARCHAR(50),
    message_key               VARCHAR(20),
    seq                       VARCHAR(100),
    srfidd                    VARCHAR(50),
    open_dt                   VARCHAR(14),
    eai_send_fg               VARCHAR(1)          DEFAULT 'N' NOT NULL,
    part_message                   VARCHAR(500),
    fail_back_channel                   VARCHAR(1),
    fail_back_result_cd                   VARCHAR(5),
    fail_back_senddtm                   VARCHAR(14),
    tmpl_cd                   VARCHAR(50)
);
ALTER TABLE nvsendlog ADD CONSTRAINT pk_nvsendlog PRIMARY KEY (campaign_no, result_seq, list_seq, customer_key, record_seq);
CREATE INDEX idx_nvsendlog_sdt_cky ON nvsendlog (send_dt, customer_key);
CREATE INDEX idx_nvsendlog_sdt_cml ON nvsendlog (send_dt, customer_email);
CREATE INDEX idx_nvsendlog_seq ON nvsendlog (seq);
CREATE INDEX idx_nvsendlog_message_key ON nvsendlog (message_key);
CREATE INDEX idx_nvsendlog_cno_ecd ON nvsendlog (campaign_no, error_cd);
CREATE INDEX idx_nvsendlog_sdt_efg ON nvsendlog (send_dt, eai_send_fg);

-- 캠페인테스트발송로그
CREATE TABLE nvtestsendlog (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100)        NOT NULL,
    sid                       VARCHAR(5)          NOT NULL,
    send_dt                   CHAR(8),
    send_tm                   CHAR(6),
    error_cd                  VARCHAR(5),
    send_domain               VARCHAR(50),
    err_msg                   VARCHAR(250),
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    resend_yn                 CHAR(1),
    req_dept_id               VARCHAR(50),
    req_user_id               VARCHAR(50),
    message_key               VARCHAR(20),
    seq                       VARCHAR(200),
    part_message                   VARCHAR(500),
    tmpl_cd                   VARCHAR(50)
);
ALTER TABLE nvtestsendlog ADD CONSTRAINT pk_nvtestsendlog PRIMARY KEY (campaign_no, result_seq, list_seq, customer_key, record_seq);
CREATE INDEX idx_nvtestsendlog_send_dt ON nvtestsendlog (send_dt);

-- 캠페인발송결과상세
CREATE TABLE nvsendresultdetail (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    error_cd                  VARCHAR(4)          NOT NULL,
    cnt                       NUMERIC(10),
    channel_type              CHAR(1)
);
ALTER TABLE nvsendresultdetail ADD CONSTRAINT pk_nvsendresultdetail PRIMARY KEY (campaign_no, result_seq, error_cd);

-- 스팸로그
CREATE TABLE nv_spam_sendlog (
    campaign_no               NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    customer_key              VARCHAR(50)         NOT NULL,
    record_seq                VARCHAR(10)         DEFAULT '0'    NOT NULL,
    sid                       VARCHAR(5)          NOT NULL,
    send_domain               VARCHAR(50)         NOT NULL
);
ALTER TABLE nv_spam_sendlog ADD CONSTRAINT pk_nv_spam_sendlog PRIMARY KEY (campaign_no, result_seq, list_seq, customer_key, record_seq);