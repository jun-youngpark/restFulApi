-- 로그인 이메일 인증 옵션
CREATE TABLE nvauthcheck (
    user_id                   VARCHAR(15)         NOT NULL,
    req_dtm                   VARCHAR(14)         NOT NULL,
    token                     CHAR(40)            NOT NULL,
    flag                      VARCHAR(10)         DEFAULT 'READY' NOT NULL
)

CREATE INDEX idx_nvauthcheck ON nvauthcheck (user_id, flag, token);