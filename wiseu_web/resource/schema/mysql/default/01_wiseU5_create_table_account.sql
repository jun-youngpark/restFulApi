-- 사용자계정
CREATE TABLE nvuser (
    user_id                   VARCHAR(15)         NOT NULL,
    pass_wd                   VARCHAR(128),
    pass_salt                 VARCHAR(50),
    grp_cd                    VARCHAR(12),
    name_kor                  VARCHAR(50),
    name_eng                  VARCHAR(50),
    tel_no                    VARCHAR(20),
    email                     VARCHAR(100),
    user_class                VARCHAR(50),
    usertype_cd               CHAR(1)             NOT NULL,
    lastupdate_dt             CHAR(8),
    active_yn                 CHAR(1),
    accept_yn                 CHAR(1),
    list_count_per_page       VARCHAR(3)          DEFAULT 10,
    soeid                     CHAR(7),
    geid                      CHAR(10),
    isa_role                  CHAR(1),
    work_doc                  VARCHAR(100),
    user_role                 CHAR(1),
    create_user               VARCHAR(15),
    permit_sts                CHAR(1),
    permit_dt                 VARCHAR(8),
    login_cnt                 NUMERIC(1)          DEFAULT 0,
    lastlogin_dt              VARCHAR(14)
);
ALTER TABLE nvuser ADD CONSTRAINT pk_nvuser PRIMARY KEY (user_id);

-- 사용자 계정 암호 이력 관리
CREATE TABLE nvuserpwhistory(
    user_id                   VARCHAR(15)         NOT NULL,
    create_dt                 VARCHAR(14)         NOT NULL,
    pass_wd                   VARCHAR(128),
    pass_salt                 VARCHAR(50),
    default_passyn            CHAR(1)             DEFAULT 'N'
);
ALTER TABLE nvuserpwhistory ADD CONSTRAINT pk_nvuserpwhistory PRIMARY KEY (user_id, create_dt);

-- 사용자그룹[부서]
CREATE TABLE nvusergrp (
    grp_cd                    VARCHAR(12)         NOT NULL,
    grp_nm                    VARCHAR(100),
    supragrp_cd               VARCHAR(12),
    grp_desc                  VARCHAR(250),
    grp_level                 NUMERIC(2),
    permission                CHAR(1),
    editor_id                 VARCHAR(15),
    lastupdate_dt             CHAR(8),
    manager_id                VARCHAR(13),
    active_yn                 CHAR(1),
    etc_info1                 VARCHAR(250),
    manager_nm                VARCHAR(50),
    server_ip                 VARCHAR(20),
    server_port               VARCHAR(5),
    func_code                 VARCHAR(50),
    func_desc                 VARCHAR(50),
    work_doc                  VARCHAR(100),
    accept_yn                 CHAR(1)             DEFAULT 'Y',
    permit_dt                 VARCHAR(8),
    permit_sts                CHAR(1),
    req_dept_id               VARCHAR(50)
);
ALTER TABLE nvusergrp ADD CONSTRAINT pk_nvusergrp PRIMARY KEY (grp_cd);

-- 메뉴
CREATE TABLE nvmenu (
    menu_cd                   VARCHAR(10)         NOT NULL,
    pmenu_cd                  VARCHAR(10),
    level_no                  NUMERIC(1)          NOT NULL,
    menu_nm                   VARCHAR(50),
    menu_link_url             VARCHAR(100),
    menu_icon_img             VARCHAR(100),
    active_yn                 CHAR(1)             DEFAULT 'Y',
    module_nm                 VARCHAR(20),
    sort_no                   NUMERIC(2),
    m_type                    CHAR(1)             DEFAULT 'M'
);
ALTER TABLE nvmenu ADD CONSTRAINT pk_nvmenu PRIMARY KEY (menu_cd);

-- 메뉴다국어
CREATE TABLE nvmenu_lang (
    menu_cd                   VARCHAR(10)         NOT NULL,
    lang                      VARCHAR(10)         NOT NULL,
    menu_nm                   VARCHAR(50)
);
ALTER TABLE nvmenu_lang ADD CONSTRAINT pk_nvmenu_lang PRIMARY KEY (menu_cd, lang);

-- 기본메뉴접근권한
CREATE TABLE nvmenudefaultrole (
    user_type                 CHAR(1)             NOT NULL,
    menu_cd                   VARCHAR(10)         NOT NULL,
    read_auth                 CHAR(1),
    write_auth                CHAR(1),
    execute_auth              CHAR(1)
);
ALTER TABLE nvmenudefaultrole ADD CONSTRAINT pk_nvmenudefaultrole PRIMARY KEY (user_type, menu_cd);

-- 사용자메뉴권한
CREATE TABLE nvmenurole (
    user_id                   VARCHAR(30)         NOT NULL,
    menu_cd                   VARCHAR(10)         NOT NULL,
    read_auth                 CHAR(1),
    write_auth                CHAR(1),
    execute_auth              CHAR(1)
);
ALTER TABLE nvmenurole ADD CONSTRAINT pk_nvmenurole PRIMARY KEY (user_id, menu_cd);
