-- 로그인 이메일 인증 옵션
CREATE TABLE NVAUTHCHECK (
    USER_ID                   VARCHAR(15)         NOT NULL,
    REQ_DTM                   VARCHAR(14)         NOT NULL,
    TOKEN                     CHAR(40)            NOT NULL,
    FLAG                      VARCHAR(10)         DEFAULT 'READY' NOT NULL
)

CREATE INDEX idx_nvauthcheck ON nvauthcheck (USER_ID, FLAG, TOKEN);