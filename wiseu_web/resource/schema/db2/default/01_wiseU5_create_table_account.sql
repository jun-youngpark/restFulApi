-- 사용자계정
CREATE TABLE NVUSER (
    USER_ID                   VARCHAR(15)         NOT NULL,
    PASS_WD                   VARCHAR(128),
    PASS_SALT                 VARCHAR(50),
    GRP_CD                    VARCHAR(12),
    NAME_KOR                  VARCHAR(50),
    NAME_ENG                  VARCHAR(50),
    TEL_NO                    VARCHAR(20),
    EMAIL                     VARCHAR(100),
    USER_CLASS                VARCHAR(50),
    USERTYPE_CD               CHAR(1)             NOT NULL,
    LASTUPDATE_DT             CHAR(8),
    ACTIVE_YN                 CHAR(1),
    ACCEPT_YN                 CHAR(1),
    LIST_COUNT_PER_PAGE       VARCHAR(3)          DEFAULT 10,
    SOEID                     CHAR(7),
    GEID                      CHAR(10),
    ISA_ROLE                  CHAR(1),
    WORK_DOC                  VARCHAR(100),
    USER_ROLE                 CHAR(1),
    CREATE_USER               VARCHAR(15),
    PERMIT_STS                CHAR(1),
    PERMIT_DT                 VARCHAR(8),
    LOGIN_CNT                 NUMERIC(1)          DEFAULT 0,
    LASTLOGIN_DT              VARCHAR(14)
);
ALTER TABLE NVUSER ADD CONSTRAINT PK_NVUSER PRIMARY KEY (USER_ID);

-- 사용자 계정 암호 이력 관리
CREATE TABLE NVUSERPWHISTORY(
    USER_ID                   VARCHAR(15)         NOT NULL,
    CREATE_DT                 VARCHAR(14)         NOT NULL,
    PASS_WD                   VARCHAR(128),
    PASS_SALT                 VARCHAR(50),
    DEFAULT_PASSYN            CHAR(1)             DEFAULT 'N'
);
ALTER TABLE NVUSERPWHISTORY ADD CONSTRAINT PK_NVUSERPWHISTORY PRIMARY KEY (USER_ID, CREATE_DT);

-- 사용자그룹[부서]
CREATE TABLE NVUSERGRP (
    GRP_CD                    VARCHAR(12)         NOT NULL,
    GRP_NM                    VARCHAR(100),
    SUPRAGRP_CD               VARCHAR(12),
    GRP_DESC                  VARCHAR(250),
    GRP_LEVEL                 NUMERIC(2),
    PERMISSION                CHAR(1),
    EDITOR_ID                 VARCHAR(15),
    LASTUPDATE_DT             CHAR(8),
    MANAGER_ID                VARCHAR(13),
    ACTIVE_YN                 CHAR(1),
    ETC_INFO1                 VARCHAR(250),
    MANAGER_NM                VARCHAR(50),
    SERVER_IP                 VARCHAR(20),
    SERVER_PORT               VARCHAR(5),
    FUNC_CODE                 VARCHAR(50),
    FUNC_DESC                 VARCHAR(50),
    WORK_DOC                  VARCHAR(100),
    ACCEPT_YN                 CHAR(1)             DEFAULT 'Y',
    PERMIT_DT                 VARCHAR(8),
    PERMIT_STS                CHAR(1),
    REQ_DEPT_ID               VARCHAR(50)
);
ALTER TABLE NVUSERGRP ADD CONSTRAINT PK_NVUSERGRP PRIMARY KEY (GRP_CD);

-- 메뉴
CREATE TABLE NVMENU (
    MENU_CD                   VARCHAR(10)         NOT NULL,
    PMENU_CD                  VARCHAR(10),
    LEVEL_NO                  NUMERIC(1)          NOT NULL,
    MENU_NM                   VARCHAR(50),
    MENU_LINK_URL             VARCHAR(100),
    MENU_ICON_IMG             VARCHAR(100),
    ACTIVE_YN                 CHAR(1)             DEFAULT 'Y',
    MODULE_NM                 VARCHAR(20),
    SORT_NO                   NUMERIC(2),
    M_TYPE                    CHAR(1)             DEFAULT 'M'
);
ALTER TABLE NVMENU ADD CONSTRAINT PK_NVMENU PRIMARY KEY (MENU_CD);

-- 메뉴다국어
CREATE TABLE NVMENU_LANG (
    MENU_CD                   VARCHAR(10)         NOT NULL,
    LANG                      VARCHAR(10)         NOT NULL,
    MENU_NM                   VARCHAR(50)
);
ALTER TABLE NVMENU_LANG ADD CONSTRAINT PK_NVMENU_LANG PRIMARY KEY (MENU_CD, LANG);

-- 기본메뉴접근권한
CREATE TABLE NVMENUDEFAULTROLE (
    USER_TYPE                 CHAR(1)             NOT NULL,
    MENU_CD                   VARCHAR(10)         NOT NULL,
    READ_AUTH                 CHAR(1),
    WRITE_AUTH                CHAR(1),
    EXECUTE_AUTH              CHAR(1)
);
ALTER TABLE NVMENUDEFAULTROLE ADD CONSTRAINT PK_NVMENUDEFAULTROLE PRIMARY KEY (USER_TYPE, MENU_CD);

-- 사용자메뉴권한
CREATE TABLE NVMENUROLE (
    USER_ID                   VARCHAR(30)         NOT NULL,
    MENU_CD                   VARCHAR(10)         NOT NULL,
    READ_AUTH                 CHAR(1),
    WRITE_AUTH                CHAR(1),
    EXECUTE_AUTH              CHAR(1)
);
ALTER TABLE NVMENUROLE ADD CONSTRAINT PK_NVMENUROLE PRIMARY KEY (USER_ID, MENU_CD);
