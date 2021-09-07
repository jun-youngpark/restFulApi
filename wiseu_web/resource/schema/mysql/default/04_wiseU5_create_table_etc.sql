-- 대상자db접속정보
CREATE TABLE nvdbinfo (
    dbinfo_seq                NUMERIC(2)          NOT NULL,
    driver_nm                 VARCHAR(250),
    server_nm                 VARCHAR(100),
    driver_dsn                VARCHAR(250),
    dbuser_id                 VARCHAR(30),
    dbpassword                VARCHAR(50),
    encoding                  VARCHAR(20),
    decoding                  VARCHAR(20),
    dbkind                    VARCHAR(20),
    testquery                 VARCHAR(250)
);
ALTER TABLE nvdbinfo ADD constraint pk_nvdbinfo PRIMARY KEY (dbinfo_seq);

-- 금칙어
CREATE TABLE nvbadword (
    channel_type              VARCHAR(10)         NOT NULL,
    bad_words                 longtext
);
ALTER TABLE nvbadword ADD constraint pk_nvbadword PRIMARY KEY (channel_type);

-- 기본핸들러
CREATE TABLE nvdefaulthandler (
    seq                       NUMERIC(10)         NOT NULL,
    handle_nm                 VARCHAR(300),
    handle_desc               VARCHAR(500),
    service_type              CHAR(1),
    channel                   CHAR(1)             NOT NULL,
    handle_type               CHAR(1)             DEFAULT 'G',
    handle_attr               VARCHAR(10),
    user_id                   VARCHAR(50),
    create_dt                 CHAR(8),
    create_tm                 CHAR(6),
    handler                   LONGTEXT,
    ab_test_yn                VARCHAR(1)          DEFAULT 'N',
    msg_type                  VARCHAR(1)
);
ALTER TABLE nvdefaulthandler ADD CONSTRAINT pk_nvdefaulthandler PRIMARY KEY (seq);

-- 유효수신구간
CREATE TABLE nvdurationinfo (
    durationinfo_cd           CHAR(2)             NOT NULL,
    maxtime                   NUMERIC(15),
    mintime                   NUMERIC(15),
    valid_chk                 CHAR(1)
);
ALTER TABLE nvdurationinfo ADD CONSTRAINT pk_nvdurationinfo PRIMARY KEY (durationinfo_cd);

-- 대상자파일업로드
CREATE TABLE nvfileupload (
    target_no                 NUMERIC(15)         NOT NULL,
    customer_id               VARCHAR(50)         NOT NULL,
    customer_nm               VARCHAR(100),
    customer_email            VARCHAR(100),
    customer_tel              VARCHAR(15),
    customer_fax              VARCHAR(15),
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    slot3                     VARCHAR(100),
    slot4                     VARCHAR(100),
    slot5                     VARCHAR(100),
    slot6                     VARCHAR(100),
    slot7                     VARCHAR(100),
    slot8                     VARCHAR(100),
    slot9                     VARCHAR(100),
    slot10                    VARCHAR(100),
    slot11                    VARCHAR(100),
    slot12                    VARCHAR(100),
    slot13                    VARCHAR(100),
    slot14                    VARCHAR(100),
    slot15                    VARCHAR(100),
    slot16                    VARCHAR(100),
    slot17                    VARCHAR(100),
    slot18                    VARCHAR(100),
    slot19                    VARCHAR(100),
    slot20                    VARCHAR(100),
    slot21                    VARCHAR(100),
    slot22                    VARCHAR(100),
    slot23                    VARCHAR(100),
    slot24                    VARCHAR(100),
    slot25                    VARCHAR(100),
    slot26                    VARCHAR(100),
    slot27                    VARCHAR(100),
    slot28                    VARCHAR(100),
    slot29                    VARCHAR(100),
    slot30                    VARCHAR(100),
    slot31                    VARCHAR(100),
    slot32                    VARCHAR(100),
    slot33                    VARCHAR(100),
    slot34                    VARCHAR(100),
    slot35                    VARCHAR(100),
    slot36                    VARCHAR(100),
    slot37                    VARCHAR(100),
    slot38                    VARCHAR(100),
    slot39                    VARCHAR(100),
    slot40                    VARCHAR(100),
    slot41                    VARCHAR(100),
    slot42                    VARCHAR(100),
    slot43                    VARCHAR(100),
    slot44                    VARCHAR(100),
    slot45                    VARCHAR(100),
    slot46                    VARCHAR(100),
    slot47                    VARCHAR(100),
    slot48                    VARCHAR(100),
    slot49                    VARCHAR(100),
    slot50                    VARCHAR(100),
    seg                       VARCHAR(20),
    call_back                 VARCHAR(50),
    customer_slot1            VARCHAR(50),
    customer_slot2            VARCHAR(50),
    sender_nm                 VARCHAR(50),
    sender_email              VARCHAR(100),
    retmail_receiver          VARCHAR(100),
    sentence                  VARCHAR(2000)
);
ALTER TABLE nvfileupload ADD CONSTRAINT pk_nvfileupload PRIMARY KEY (target_no, customer_id);

-- 발송주기
CREATE TABLE nvsendcycle (
    cycle_cd                  CHAR(1)             NOT NULL,
    cycle_nm                  VARCHAR(20)
);
ALTER TABLE nvsendcycle ADD CONSTRAINT pk_nvsendcycle PRIMARY KEY (cycle_cd);

-- 메일발송결과코드
CREATE TABLE nvsenderr (
    error_cd                  VARCHAR(4)          NOT NULL,
    error_nm                  VARCHAR(100),
    error_desc                VARCHAR(250),
    error_sms_desc            VARCHAR(250),
    category_cd               VARCHAR(12),
    channel_type              CHAR(1),
    spam_yn                   CHAR(1)             DEFAULT 'N'
);
ALTER TABLE nvsenderr ADD CONSTRAINT pk_nvsenderr PRIMARY KEY (error_cd);

-- 서버환경정보
CREATE TABLE nvserverinfo (
    host_nm                                       VARCHAR(250)        NOT NULL,
    port_no                                       VARCHAR(5),
    driver_nm                                     VARCHAR(250),
    driver_dsn                                    VARCHAR(250),
    dbuser_id                                     VARCHAR(30),
    dbpassword                                    VARCHAR(30),
    openclick_path                                VARCHAR(250),
    survey_path                                   VARCHAR(250),
    link_path                                     VARCHAR(250),
    htmlmaker_path                                VARCHAR(250),
    openimage_path                                VARCHAR(250),
    duration_path                                 VARCHAR(250),
    reject_path                                   VARCHAR(250),
    smtp_ip                                       VARCHAR(250),
    smtp_port                                     VARCHAR(5),
    fula_ip                                       VARCHAR(250),
    fula_port                                     VARCHAR(5),
    ftp_yn                                        CHAR(1),
    ftp_user_id                                   VARCHAR(30),
    ftp_password                                  VARCHAR(30),
    lastupdate_dt                                 CHAR(8),
    editor_id                                     VARCHAR(15),
    ret_domain                                    VARCHAR(100),
    retry_cnt                                     NUMERIC(2),
    b4_send_approve_yn                            CHAR(1)             DEFAULT 'N',
    b4_send_verify_yn                             CHAR(1)             DEFAULT 'N',
    b4_real_send_test_send_yn                     CHAR(1)             DEFAULT 'N',
    ase_link_merge_param                          VARCHAR(500),
    ase_reject_merge_param                        VARCHAR(500),
    ase_open_scriptlet                            VARCHAR(2000),
    groovy_link_merge_param                       VARCHAR(500),
    groovy_reject_merge_param                     VARCHAR(500),
    groovy_open_scriptlet                         VARCHAR(2000),
    resend_include_returnmail_yn                  CHAR(1)             DEFAULT 'Y',
    resend_include_mail_key_yn                    CHAR(1)             DEFAULT 'Y',
    resend_error_cd                               VARCHAR(500),
    fax_resend_error_cd                           VARCHAR(500),
    sms_resend_error_cd                           VARCHAR(500),
    altalk_resend_error_cd                        VARCHAR(500),
    push_resend_error_cd                          VARCHAR(500),
    spool_preserve_period                         VARCHAR(4)          DEFAULT '7',
    log_preserve_period                           VARCHAR(4),
    result_file_download_yn                       CHAR(1)             DEFAULT 'Y',
    sucs_result_file_download_yn                  CHAR(1)             DEFAULT 'N',
    kakao_template_last_sync_dtm                  VARCHAR(14)
);
ALTER TABLE nvserverinfo ADD CONSTRAINT pk_nvserverinfo PRIMARY KEY (host_nm);

-- 메일발송결과카테고리
CREATE TABLE nvsmtpcategory (
    category_cd               VARCHAR(12)         NOT NULL,
    grp_cd                    CHAR(2),
    pcategory_cd              VARCHAR(12),
    level_cd                  NUMERIC(2),
    category_nm               CHAR(50),
    category_desc             CHAR(100),
    active_yn                 CHAR(1)
);
ALTER TABLE nvsmtpcategory ADD CONSTRAINT pk_nvsmtpcategory PRIMARY KEY (category_cd);

-- 테스트수신자
CREATE TABLE nvtestuser (
    campaign_no               NUMERIC(15)         NOT NULL,
    campaign_type             CHAR(1)             NOT NULL,
    user_id                   VARCHAR(15)         NOT NULL,
    seq_no                    NUMERIC(5)          NOT NULL
);
ALTER TABLE nvtestuser ADD CONSTRAINT pk_nvtestuser PRIMARY KEY (campaign_no, campaign_type, user_id, seq_no);

-- 테스트수신자풀
CREATE TABLE nvtestuserpool (
    user_id                   VARCHAR(15)         NOT NULL,
    seq_no                    NUMERIC(5)          NOT NULL,
    testreceiver_email        VARCHAR(100)        NOT NULL,
    testreceiver_tel          VARCHAR(50),
    testreceiver_nm           VARCHAR(50)         NOT NULL,
    testreceiver_fax          VARCHAR(50),
    test_grp_cd               NUMERIC(5)
);
ALTER TABLE nvtestuserpool ADD CONSTRAINT pk_nvtestuserpool PRIMARY KEY (user_id, seq_no);

-- 최상위도메인
CREATE TABLE nvtopdomain (
    domain_nm                 VARCHAR(50)         NOT NULL
);
ALTER TABLE nvtopdomain ADD CONSTRAINT pk_nvtopdomain PRIMARY KEY (domain_nm);

-- 메시지발신자정보
CREATE TABLE nvusermailinfo (
    user_id                   VARCHAR(15)         NOT NULL,
    seq_no                    NUMERIC(5)          NOT NULL,
    sender_nm                 VARCHAR(50),
    sender_email              VARCHAR(100),
    receiver_nm               VARCHAR(50),
    sender_tel                VARCHAR(50),
    sender_fax                VARCHAR(50),
    receiver_fax              VARCHAR(50),
    retmail_receiver          VARCHAR(100)
);
ALTER TABLE nvusermailinfo ADD CONSTRAINT pk_nvusermailinfo PRIMARY KEY (user_id, seq_no);

-- 대상자계층
CREATE TABLE nvseggenealogy (
    segment_no                NUMERIC(15)         NOT NULL,
    genealogy_seq             NUMERIC(3)          NOT NULL,
    suprasegment_no           NUMERIC(15)
);
ALTER TABLE nvseggenealogy ADD CONSTRAINT pk_nvseggenealogy PRIMARY KEY (segment_no, genealogy_seq);

-- 대상자[세그먼트]
CREATE TABLE nvsegment (
    segment_no                NUMERIC(15)         NOT NULL,
    user_id                   VARCHAR(15),
    grp_cd                    VARCHAR(12),
    segment_nm                VARCHAR(100),
    dbinfo_seq                NUMERIC(2),
    segment_desc              VARCHAR(250),
    sqlhead                   TEXT(4000),
    sqltail                   TEXT(4000),
    sqlbody                   TEXT(4000),
    sqlfilter                 TEXT(4000),
    lastupdate_dt             CHAR(8),
    segment_size              NUMERIC(10),
    crtgrp_cd                 VARCHAR(12),
    segment_type              CHAR(1),
    filetodb_yn               CHAR(1),
    editor_id                 VARCHAR(15),
    share_yn                  CHAR(1),
    active_yn                 CHAR(1),
    category_cd               VARCHAR(12),
    segment_sts               CHAR(1),
    tag_no                    NUMERIC(10),
    psegment_no               NUMERIC(15),
    plink_seq                 VARCHAR(250),
    seg_type                  CHAR(1),
    testquery                 TEXT(4000),
    updatequery               TEXT(1000)
);
ALTER TABLE nvsegment ADD CONSTRAINT pk_nvsegment PRIMARY KEY (segment_no);

-- 대상자태그
CREATE TABLE nvsegmenttag (
    tag_no                    NUMERIC(10)         NOT NULL,
    tag_nm                    VARCHAR(50)
);
ALTER TABLE nvsegmenttag ADD CONSTRAINT pk_nvsegmenttag PRIMARY KEY (tag_no);

-- 기본대상자
CREATE TABLE nvsegmentcheck (
    segment_no                NUMERIC(15)         NOT NULL,
    user_id                   VARCHAR(15)         NOT NULL
);
ALTER TABLE nvsegmentcheck ADD CONSTRAINT pk_nvsegmentcheck PRIMARY KEY (segment_no, user_id);

-- 대상자개인화필드[시멘틱]
CREATE TABLE nvsemantic (
    segment_no                NUMERIC(15)         NOT NULL,
    field_seq                 NUMERIC(3)          NOT NULL,
    field_nm                  VARCHAR(250)        NOT NULL,
    field_desc                VARCHAR(250),
    field_type                VARCHAR(20),
    field_length              NUMERIC(5),
    initvalue                 VARCHAR(250),
    minvalue                  VARCHAR(250),
    `maxvalue`                VARCHAR(250),
    null_yn                   CHAR(1),
    field_key                 CHAR(1)
);
ALTER TABLE nvsemantic ADD CONSTRAINT pk_nvsemantic PRIMARY KEY (segment_no, field_seq);

-- 템플릿
CREATE TABLE nvcontents (
    conts_no                  NUMERIC(8)          NOT NULL,
    grp_cd                    VARCHAR(12),
    category_cd               VARCHAR(12),
    user_id                   VARCHAR(15),
    conts_nm                  VARCHAR(50),
    conts_desc                VARCHAR(100),
    file_url_name             VARCHAR(500),
    file_type                 CHAR(1),
    file_name                 VARCHAR(250),
    create_dt                 CHAR(8),
    create_tm                 CHAR(6),
    auth_type                 CHAR(1),
    tag_no                    NUMERIC(10)
);
ALTER TABLE nvcontents ADD CONSTRAINT pk_nvcontents PRIMARY KEY (conts_no);

-- 템플릿태그
CREATE TABLE nvcontentstag (
    tag_no                    NUMERIC(10)         NOT NULL,
    tag_nm                    VARCHAR(50)
);
ALTER TABLE nvcontentstag ADD CONSTRAINT pk_nvcontentstag PRIMARY KEY (tag_no);

-- 모바일템플릿
CREATE TABLE nvmobilecontents (
    conts_no                  NUMERIC(8)          NOT NULL,
    user_id                   VARCHAR(15),
    conts_nm                  VARCHAR(600),
    conts_desc                VARCHAR(100),
    file_path                 VARCHAR(250),
    file_name                 VARCHAR(100),
    image_url                 VARCHAR(500),
    image_link                VARCHAR(500),
    detour_file_path          VARCHAR(250),
    detour_file_name          VARCHAR(100),
    detour_preview_path       VARCHAR(250),
    detour_preview_name       VARCHAR(100),
    file_type                 CHAR(1),
    create_dt                 CHAR(8),
    create_tm                 CHAR(6),
    auth_type                 CHAR(1),
    tag_no                    NUMERIC(10),
    conts_txt                 VARCHAR(4000),
    file_preview_path         VARCHAR(250),
    file_preview_name         VARCHAR(50),
    grp_cd                    VARCHAR(12),
    category_cd               VARCHAR(12),
    file_size                 NUMERIC(15),
    kakao_sender_key          VARCHAR(40),
    kakao_tmpl_cd             VARCHAR(50),
    kakao_tmpl_status         CHAR(1),
    kakao_insp_status         VARCHAR(3),
    kakao_buttons             VARCHAR(4000),
    use_yn                    CHAR(1)             DEFAULT 'Y'    NOT NULL,
    content_type              VARCHAR(10),
    unsubscribe_content       VARCHAR(20),
    message_type              VARCHAR(10)
    kakao_tmpl_msg_type       CHAR(2),
    kakao_tmpl_ad             VARCHAR(240),
    kakao_tmpl_ex             VARCHAR(1500),
    kakao_security_yn         CHAR(1),
    kakao_em_type             VARCHAR(5),
    kakao_em_title            VARCHAR(150),
    kakao_em_subtitle         VARCHAR(150),
    kakao_quick_replies       VARCHAR(4000)
);
ALTER TABLE nvmobilecontents ADD CONSTRAINT pk_nvmobilecontents PRIMARY KEY (conts_no);

-- 모바일템플릿태그
CREATE TABLE nvmobilecontentstag (
    tag_no                    NUMERIC(10)         NOT NULL,
    tag_nm                    VARCHAR(50)
);
ALTER TABLE nvmobilecontentstag ADD CONSTRAINT pk_nvmobilecontentstag PRIMARY KEY (tag_no);

-- 준실시간서비스id
CREATE TABLE nvacceptsvcid (
    svc_id                    VARCHAR(20)         NOT NULL,
    ecare_no                  NUMERIC(15)         NOT NULL,
    svc_desc                  VARCHAR(200)
);
ALTER TABLE nvacceptsvcid ADD CONSTRAINT pk_nvacceptsvcid PRIMARY KEY (svc_id);

-- 준실시간발송요청
CREATE TABLE nvrealtimeaccept (
    seq                       VARCHAR(100)        NOT NULL,
    ecare_no                  NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16,0),
    list_seq                  VARCHAR(10),
    channel                   VARCHAR(2)          NOT NULL,
    svc_id                    VARCHAR(20),
    req_user_id               VARCHAR(50),
    req_dept_id               VARCHAR(50),
    req_dt                    VARCHAR(8)          NOT NULL,
    req_tm                    VARCHAR(6)          NOT NULL,
    tmpl_type                 CHAR(1)             NOT NULL,
    receiver_id               VARCHAR(50)         NOT NULL,
    receiver_nm               VARCHAR(50)         NOT NULL,
    receiver                  VARCHAR(100)        NOT NULL,
    sender_nm                 VARCHAR(50)         NOT NULL,
    sender                    VARCHAR(100)        NOT NULL,
    subject                   VARCHAR(250),
    send_fg                   CHAR(1)             DEFAULT 'R',
    secu_key                  VARCHAR(13),
    security_path             VARCHAR(1000),
    error_msg                 VARCHAR(250),
    reserved_date             VARCHAR(14),
    preview_type              CHAR(1)             DEFAULT 'N'    NOT NULL,
    data_cnt                  NUMERIC(3),
    file_path1                VARCHAR(250),
    file_path2                VARCHAR(250),
    file_path3                VARCHAR(250),
    srfidd                    VARCHAR(50),
    jonmun                    LONGTEXT,
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
ALTER TABLE nvrealtimeaccept ADD CONSTRAINT pk_nvrealtimeaccept PRIMARY KEY (seq);
create index idx_nvrealtimeaccept_send_fg on nvrealtimeaccept (send_fg);
create index idx_nvrealtimeaccept_req_dt on nvrealtimeaccept (req_dt);
create index idx_nvrealtimeaccept_eno_rseq on nvrealtimeaccept (ecare_no, result_seq);

-- 준실시간발송요청데이터
CREATE TABLE nvrealtimeacceptdata (
    seq                       VARCHAR(100)        NOT NULL,
    data_seq                  NUMERIC(3)          NOT NULL,
    attach_yn                 CHAR(1),
    attach_name               VARCHAR(100),
    secu_key                  VARCHAR(13),
    data                      LONGTEXT
);
ALTER TABLE nvrealtimeacceptdata ADD CONSTRAINT pk_nvrealtimeacceptdata PRIMARY KEY (seq, data_seq);

-- 일별발송제한
CREATE TABLE nvsendblockdate (
    chk_year                  CHAR(4)             NOT NULL,
    block_dt                  CHAR(4)             NOT NULL,
    channel_type              CHAR(1)             DEFAULT 'M'    NOT NULL
);
ALTER TABLE nvsendblockdate ADD CONSTRAINT pk_nvsendblockdate PRIMARY KEY (chk_year, block_dt, channel_type);

-- 주말발송제한
CREATE TABLE nvsendblockdateinfo (
    reg_year                  CHAR(4)             NOT NULL,
    reg_saturday_yn           CHAR(1),
    reg_sunday_yn             CHAR(1)
);
ALTER TABLE nvsendblockdateinfo ADD CONSTRAINT pk_nvsendblockdateinfo PRIMARY KEY (reg_year);

-- 캠페인failover이력
CREATE TABLE nvcampaigninvokehistory (
    invoke_time               CHAR(12)            NOT NULL
);
ALTER TABLE nvcampaigninvokehistory ADD CONSTRAINT pk_nvcampaigninvokehistory PRIMARY KEY (invoke_time);

-- 이케어failover이력
CREATE TABLE nvecareinvokehistory (
    invoke_time               CHAR(12)            NOT NULL
);
ALTER TABLE nvecareinvokehistory ADD CONSTRAINT pk_nvecareinvokehistory PRIMARY KEY (invoke_time);

-- 코드마스터
CREATE TABLE nv_cd_mst (
    cd_cat                    VARCHAR(20)         NOT NULL,
    cd                        VARCHAR(20)         NOT NULL,
    lang                      VARCHAR(10)         NOT NULL,
    par_cd_cat                VARCHAR(20)         NOT NULL,
    val                       VARCHAR(100),
    cd_desc                   VARCHAR(2000),
    use_col                   VARCHAR(50),
    use_yn                    CHAR(1)             DEFAULT 'Y'    NOT NULL,
    reg_dtm                   VARCHAR(14)         NOT NULL,
    mod_dtm                   VARCHAR(14),
    cd_ord                    NUMERIC(10)
);
ALTER TABLE nv_cd_mst ADD CONSTRAINT pk_nv_cd_mst PRIMARY KEY (cd_cat, cd, lang);
create index idx_nv_cd_mst_par_cd_cat on nv_cd_mst (par_cd_cat);

-- 부서메뉴권한
CREATE TABLE nvgrpmenurole (
    grp_cd                    VARCHAR(12)         NOT NULL,
    menu_cd                   VARCHAR(10)         NOT NULL,
    read_auth                 CHAR(1),
    write_auth                CHAR(1),
    execute_auth              CHAR(1)
);
ALTER TABLE nvgrpmenurole ADD CONSTRAINT pk_nvgrpmenurole PRIMARY KEY (grp_cd, menu_cd);

-- 테스트수신그룹
CREATE TABLE nv_test_grp (
    test_grp_cd               NUMERIC(5)          NOT NULL,
    test_grp_nm               VARCHAR(100),
    test_supragrp_cd          NUMERIC(5),
    user_id                   VARCHAR(15),
    active_yn                 CHAR(1)
);
ALTER TABLE nv_test_grp ADD CONSTRAINT pk_nv_test_grp PRIMARY KEY (test_grp_cd);

-- 스케줄발송요청
CREATE TABLE nvscheduleaccept (
    ecare_no                  NUMERIC(15)         NOT NULL,
    seq                       VARCHAR(100)        NOT NULL,
    result_seq                NUMERIC(16,0),
    list_seq                  VARCHAR(10),
    channel                   VARCHAR(2)          NOT NULL,
    svc_id                    VARCHAR(20),
    req_user_id               VARCHAR(50),
    req_dept_id               VARCHAR(50),
    req_dt                    VARCHAR(8)          NOT NULL,
    req_tm                    VARCHAR(6)          NOT NULL,
    tmpl_type                 CHAR(1)             NOT NULL,
    receiver_id               VARCHAR(50)         NOT NULL,
    receiver_nm               VARCHAR(50)         NOT NULL,
    receiver                  VARCHAR(100)        NOT NULL,
    sender_nm                 VARCHAR(50)         NOT NULL,
    sender                    VARCHAR(100)        NOT NULL,
    subject                   VARCHAR(250),
    send_fg                   CHAR(1)             DEFAULT 'R',
    slot1                     VARCHAR(100),
    slot2                     VARCHAR(100),
    secu_key                  VARCHAR(13),
    security_path             VARCHAR(1000),
    error_msg                 VARCHAR(250),
    reserved_date             VARCHAR(14),
    preview_type              CHAR(1)             DEFAULT 'N',
    data_cnt                  NUMERIC(3),
    file_path1                VARCHAR(250),
    file_path2                VARCHAR(250),
    file_path3                VARCHAR(250),
    srfidd                    VARCHAR(50),
    jonmun                    LONGTEXT
);
ALTER TABLE nvscheduleaccept ADD CONSTRAINT pk_nvscheduleaccept PRIMARY KEY (ecare_no, seq);
create index idx_nvscheduleaccept_send_fg on nvscheduleaccept (send_fg);
create index idx_nvscheduleaccept_req_dt on nvscheduleaccept (req_dt);

-- 스케줄발송요청데이타
CREATE TABLE nvscheduleacceptdata (
    seq                       VARCHAR(100)        NOT NULL,
    data_seq                  NUMERIC(3)          NOT NULL,
    attach_yn                 CHAR(1),
    attach_name               VARCHAR(100),
    secu_key                  VARCHAR(13),
    data                      LONGTEXT
);
ALTER TABLE nvscheduleacceptdata ADD CONSTRAINT pk_nvscheduleacceptdata PRIMARY KEY (seq, data_seq);

-- 재발송 요청
CREATE TABLE nvresendrequest (
    req_dt                    VARCHAR(14)         NOT NULL,
    req_user_id               VARCHAR(15)         NOT NULL,
    send_fg                   CHAR(1)             NOT NULL,
    service_no                NUMERIC(15)         NOT NULL,
    super_seq                 NUMERIC(16)         NOT NULL,
    result_seq                NUMERIC(16),
    list_seq                  VARCHAR(10),
    target_key                VARCHAR(100),
    target_contact            VARCHAR(100),
    client                    VARCHAR(2)          NOT NULL,
    sub_type                  CHAR(1)             NOT NULL,
    channel                   CHAR(1)             NOT NULL,
    resend_type               CHAR(1)             NOT NULL,
    resend_reason             VARCHAR(1000)
);
ALTER TABLE nvresendrequest ADD CONSTRAINT pk_nvresendrequest PRIMARY KEY (req_dt, req_user_id);
create index idx_nvresendrequest_rdtsfg on nvresendrequest (send_fg, result_seq);

-- 재발송 데이터
CREATE TABLE nvresendtarget (
    service_no                NUMERIC(15)         NOT NULL,
    result_seq                NUMERIC(16)         NOT NULL,
    list_seq                  VARCHAR(10)         NOT NULL,
    client                    VARCHAR(2)          NOT NULL,
    target_key                VARCHAR(100)        NOT NULL,
    target_nm                 VARCHAR(100),
    target_contact            VARCHAR(100),
    send_dt                   VARCHAR(14)         NOT NULL,
    target_lst1               VARCHAR(4000)       NOT NULL,
    target_lst2               VARCHAR(4000)
);
ALTER TABLE nvresendtarget ADD CONSTRAINT pk_nvresendtarget PRIMARY KEY (service_no, result_seq, list_seq, client);
create index idx_nvresendtarget_sdt on nvresendtarget (send_dt);

-- 분할발송스케쥴
CREATE TABLE nvdivideschedule (
    client                    VARCHAR(2)          NOT NULL,
    service_no                NUMERIC(15)         NOT NULL,
    divide_seq                NUMERIC(2)          NOT NULL,
    target_cnt                NUMERIC(10),
    start_dt                  VARCHAR(14)
);
ALTER TABLE nvdivideschedule ADD CONSTRAINT pk_nvdivideschedule PRIMARY KEY (client, service_no, divide_seq);
create index idx_nvdivideschedule_start_dt on nvdivideschedule(start_dt);

-- lts분할발송서비스정보
CREATE TABLE nv_svc_divide (
    task_id                   VARCHAR(31)         NOT NULL,
    server_id                 VARCHAR(10)         NOT NULL,
    divide_seq                NUMERIC(2)          NOT NULL,
    client                    VARCHAR(2),
    service_no                NUMERIC(15),
    target_cnt                NUMERIC(10),
    send_cnt                  NUMERIC(10),
    start_dt                  VARCHAR(14),
    end_dt                    VARCHAR(14),
    status                    CHAR(1)
);
ALTER TABLE nv_svc_divide ADD CONSTRAINT pk_nv_svc_divide PRIMARY KEY (task_id, server_id, divide_seq);
create index idx_nv_svc_divide_mlt1 on nv_svc_divide (client, service_no, divide_seq);

-- 카카오 프로필 데이터
CREATE TABLE nvkakaoprofile (
    user_id                   VARCHAR(15)         NOT NULL    comment '사용자 계정',
    kakao_sender_key          VARCHAR(40)         NOT NULL    comment '발신 프로필 키',
    kakao_yellow_id           VARCHAR(50)         NOT NULL    comment '옐로우아이디',
    last_sync_dtm             VARCHAR(14)                     comment '마지막 동기화 시간'
);
ALTER TABLE nvkakaoprofile ADD CONSTRAINT pk_nvkakaoprofile PRIMARY KEY (user_id, kakao_sender_key);

-- push서비스정보
CREATE TABLE nvpushserviceinfo (
    svc_type                  VARCHAR(1)          NOT NULL,
    svc_no                    NUMERIC(15)         NOT NULL,
    push_app_id               VARCHAR(20)         NOT NULL,
    push_msg_type             VARCHAR(2),
    push_menu_id              VARCHAR(6),
    push_pop_img_use          VARCHAR(1)          DEFAULT 'N'    NOT NULL,
    push_img_url              VARCHAR(500),
    push_pop_big_img_use      VARCHAR(1)          DEFAULT 'N'    NOT NULL,
    push_big_img_url          VARCHAR(500),
    push_click_link           VARCHAR(500)
);

ALTER TABLE nvpushserviceinfo ADD CONSTRAINT idx_nvpushserviceinfo_pk PRIMARY KEY (svc_type, svc_no);

--   push 앱 관리
CREATE TABLE nvpushapp (
    push_app_id               VARCHAR(20)         NOT NULL,
    push_app_nm               VARCHAR(100)        NOT NULL,
    use_testmode              VARCHAR(1)          DEFAULT 'N'    NOT NULL,
    use_yn                    VARCHAR(1)          DEFAULT 'Y'    NOT NULL,
    ins_dts                   VARCHAR(14),
    ins_id                    VARCHAR(50),
    upd_dts                   VARCHAR(14),
    upd_id                    VARCHAR(50)
);
ALTER TABLE nvpushapp ADD CONSTRAINT idx_nvpushapp_pk PRIMARY KEY (push_app_id);

-- copy_t
CREATE TABLE copy_t (
    no                        INT(3)              NOT NULL,
    chr                       VARCHAR(3)          NOT NULL
);