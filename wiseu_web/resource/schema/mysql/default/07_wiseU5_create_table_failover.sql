-- 서비스트랜잭션
CREATE TABLE nv_svc_trans (
    t_id                      VARCHAR(31)         NOT NULL,
    server_id                 VARCHAR(10)         NOT NULL,
    create_tm                 VARCHAR(17),
    prc_flag                  CHAR(1)
);
ALTER TABLE nv_svc_trans ADD CONSTRAINT pk_nv_svc_trans PRIMARY KEY (t_id, server_id);

-- LTS서비스정보
CREATE TABLE nv_svc_main (
    t_id                      VARCHAR(31)         NOT NULL,
    server_id                 VARCHAR(10)         NOT NULL,
    start_tm                  VARCHAR(14),
    end_tm                    VARCHAR(14),
    service_sts               VARCHAR(2),
    service_nm                VARCHAR(100),
    send_mode                 CHAR(1),
    service_type              CHAR(1),
    client                    VARCHAR(2),
    user_id                   VARCHAR(20),
    mts_cnt                   NUMERIC(2),
    target_cnt                NUMERIC(10),
    channel                   CHAR(1),
    etc_info                  VARCHAR(500),
    err_msg                   VARCHAR(500),
    display_yn                VARCHAR(1),
    status                    VARCHAR(2),
    del_yn                    CHAR(1),
    rtn_mail_cnt              NUMERIC(10),
    rcv_cnfm_em               NUMERIC(10),
    lnk_trc_em                NUMERIC(10),
    rcv_cnfm_ec               NUMERIC(10),
    rcv_trc_ec                NUMERIC(10),
    rcv_usr_def               NUMERIC(10),
    update_tm                 VARCHAR(14),
    create_tm                 VARCHAR(14)
);
ALTER TABLE nv_svc_main ADD CONSTRAINT pk_nv_svc_main PRIMARY KEY (t_id, server_id);

-- MTS서비스정보
CREATE TABLE nv_svc_mts (
    t_id                      VARCHAR(31)         NOT NULL,
    server_id                 VARCHAR(10)         NOT NULL,
    start_tm                  VARCHAR(14),
    end_tm                    VARCHAR(14),
    service_sts               VARCHAR(2),
    tot_cnt                   NUMERIC(10),
    made_cnt                  NUMERIC(10),
    max_retry                 NUMERIC(10),
    send_cnt                  NUMERIC(10),
    success_cnt               NUMERIC(10),
    unknown_user_cnt          NUMERIC(10),
    unknown_host_cnt          NUMERIC(10),
    smtp_except_cnt           NUMERIC(10),
    no_route_cnt              NUMERIC(10),
    refused_cnt               NUMERIC(10),
    etc_except_cnt            NUMERIC(10),
    invalid_addr_cnt          NUMERIC(10),
    queue_cnt                 NUMERIC(4),
    thread_cnt                NUMERIC(4),
    handler_thread_cnt        NUMERIC(4),
    err_msg                   VARCHAR(500),
    update_tm                 VARCHAR(14),
    create_tm                 VARCHAR(14)
);
ALTER TABLE nv_svc_mts ADD CONSTRAINT pk_nv_svc_mts PRIMARY KEY (t_id, server_id);

-- 서버상태정보
CREATE TABLE nv_svr_info (
    server_id                                     VARCHAR(10)         NOT NULL,
    last_update_tm                                VARCHAR(14),
    start_tm                                      VARCHAR(14),
    job_cnt                                       NUMERIC(10),
    process_thread_cnt                            NUMERIC(5),
    work_thread_cnt                               NUMERIC(5),
    openfile_desc_cnt                             NUMERIC(5),
    max_memory                                    NUMERIC(10),
    used_memory                                   NUMERIC(10),
    cpu_rate                                      NUMERIC(10,2),
    max_queue_size                                NUMERIC(5),
    used_queue_size                               NUMERIC(5),
    status                                        NUMERIC(1),
    polling_act_status                            CHAR(1),
    polling_act_status_update_tm                  VARCHAR(14),
    disp_no                                       NUMERIC(2),
    exec_info                                     VARCHAR(500),
    config_cont                                   LONGTEXT
);
ALTER TABLE nv_svr_info ADD CONSTRAINT pk_nv_svr_info PRIMARY KEY (server_id);

-- 서버/서비스동작요청
CREATE TABLE nv_status_req (
    req_kind                  VARCHAR(10)         NOT NULL,
    create_tm                 VARCHAR(14)         NOT NULL,
    task_id                   VARCHAR(31)         NOT NULL,
    req                       VARCHAR(20),
    result                    NUMERIC(1),
    update_tm                 VARCHAR(14),
    user_id                   VARCHAR(20)
);
ALTER TABLE nv_status_req ADD CONSTRAINT pk_nv_status_req PRIMARY KEY (req_kind, create_tm, task_id);

-- 서버/서비스동작요청결과
CREATE TABLE nv_status_req_result (
    server_id                 VARCHAR(10)         NOT NULL,
    req_create_tm             VARCHAR(14)         NOT NULL,
    task_id                   VARCHAR(31)         NOT NULL,
    req                       VARCHAR(20)         NOT NULL,
    result                    NUMERIC(1),
    result_msg                VARCHAR(500),
    create_tm                 VARCHAR(14)
);
ALTER TABLE nv_status_req_result ADD CONSTRAINT pk_nv_status_req_result PRIMARY KEY (server_id, req_create_tm, task_id, req);