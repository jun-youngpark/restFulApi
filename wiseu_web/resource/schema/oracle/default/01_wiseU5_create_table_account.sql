-- 사용자계정
CREATE TABLE NVUSER (
    USER_ID                   VARCHAR2(15)        NOT NULL,
    PASS_WD                   VARCHAR2(128),
    PASS_SALT                 VARCHAR2(50),
    GRP_CD                    VARCHAR2(12),
    NAME_KOR                  VARCHAR2(50),
    NAME_ENG                  VARCHAR2(50),
    TEL_NO                    VARCHAR2(20),
    EMAIL                     VARCHAR2(100),
    USER_CLASS                VARCHAR2(50),
    USERTYPE_CD               CHAR(1)             NOT NULL,
    LASTUPDATE_DT             CHAR(8),
    ACTIVE_YN                 CHAR(1),
    ACCEPT_YN                 CHAR(1),
    LIST_COUNT_PER_PAGE       VARCHAR2(3)         DEFAULT 10,
    SOEID                     CHAR(7),
    GEID                      CHAR(10),
    ISA_ROLE                  CHAR(1),
    WORK_DOC                  VARCHAR2(100),
    USER_ROLE                 CHAR(1),
    CREATE_USER               VARCHAR2(15),
    PERMIT_STS                CHAR(1),
    PERMIT_DT                 VARCHAR2(8),
    LOGIN_CNT                 NUMBER(1, 0)        DEFAULT 0,
    LASTLOGIN_DT              VARCHAR2(14)
);
ALTER TABLE NVUSER ADD CONSTRAINT PK_NVUSER PRIMARY KEY (USER_ID);

-- 사용자 계정 암호 이력 관리
CREATE TABLE NVUSERPWHISTORY(
    USER_ID                   VARCHAR2(15)        NOT NULL,
    CREATE_DT                 VARCHAR2(14)        NOT NULL,
    PASS_WD                   VARCHAR2(128),
    PASS_SALT                 VARCHAR2(50),
    DEFAULT_PASSYN            CHAR(1)             DEFAULT 'N'
);
ALTER TABLE NVUSERPWHISTORY ADD CONSTRAINT PK_NVUSERPWHISTORY PRIMARY KEY (USER_ID, CREATE_DT);

-- 사용자그룹[부서]
CREATE TABLE NVUSERGRP (
    GRP_CD                    VARCHAR2(12)        NOT NULL,
    GRP_NM                    VARCHAR2(100),
    SUPRAGRP_CD               VARCHAR2(12),
    GRP_DESC                  VARCHAR2(250),
    GRP_LEVEL                 NUMERIC(2),
    PERMISSION                CHAR(1),
    EDITOR_ID                 VARCHAR2(15),
    LASTUPDATE_DT             CHAR(8),
    MANAGER_ID                VARCHAR2(13),
    ACTIVE_YN                 CHAR(1),
    ETC_INFO1                 VARCHAR2(250),
    MANAGER_NM                VARCHAR2(50),
    SERVER_IP                 VARCHAR2(20),
    SERVER_PORT               VARCHAR2(5),
    FUNC_CODE                 VARCHAR2(50),
    FUNC_DESC                 VARCHAR2(50),
    WORK_DOC                  VARCHAR2(100),
    ACCEPT_YN                 CHAR(1)             DEFAULT 'Y',
    PERMIT_DT                 VARCHAR2(8),
    PERMIT_STS                CHAR(1),
    REQ_DEPT_ID               VARCHAR2(50)
);
ALTER TABLE NVUSERGRP ADD CONSTRAINT PK_NVUSERGRP PRIMARY KEY (GRP_CD);

-- 메뉴
CREATE TABLE NVMENU (
    MENU_CD                   VARCHAR2(10)        NOT NULL,
    PMENU_CD                  VARCHAR2(10),
    LEVEL_NO                  NUMERIC(1)          NOT NULL,
    MENU_NM                   VARCHAR2(50),
    MENU_LINK_URL             VARCHAR2(100),
    MENU_ICON_IMG             VARCHAR2(100),
    ACTIVE_YN                 CHAR(1)             DEFAULT 'Y',
    MODULE_NM                 VARCHAR2(20),
    SORT_NO                   NUMERIC(2),
    M_TYPE                    CHAR(1)             DEFAULT 'M'
);
ALTER TABLE NVMENU ADD CONSTRAINT PK_NVMENU PRIMARY KEY (MENU_CD);

-- 메뉴다국어
CREATE TABLE NVMENU_LANG (
    MENU_CD                   VARCHAR2(10)        NOT NULL,
    LANG                      VARCHAR2(10)        NOT NULL,
    MENU_NM                   VARCHAR2(50)
);
ALTER TABLE NVMENU_LANG ADD CONSTRAINT PK_NVMENU_LANG PRIMARY KEY (MENU_CD, LANG);

-- 기본메뉴접근권한
CREATE TABLE NVMENUDEFAULTROLE (
    USER_TYPE                 CHAR(1)             NOT NULL,
    MENU_CD                   VARCHAR2(10)        NOT NULL,
    READ_AUTH                 CHAR(1),
    WRITE_AUTH                CHAR(1),
    EXECUTE_AUTH              CHAR(1)
);
ALTER TABLE NVMENUDEFAULTROLE ADD CONSTRAINT PK_NVMENUDEFAULTROLE PRIMARY KEY (USER_TYPE, MENU_CD);

-- 사용자메뉴권한
CREATE TABLE NVMENUROLE (
    USER_ID                   VARCHAR2(30)        NOT NULL,
    MENU_CD                   VARCHAR2(10)        NOT NULL,
    READ_AUTH                 CHAR(1),
    WRITE_AUTH                CHAR(1),
    EXECUTE_AUTH              CHAR(1)
);
ALTER TABLE NVMENUROLE ADD CONSTRAINT PK_NVMENUROLE PRIMARY KEY (USER_ID, MENU_CD);
