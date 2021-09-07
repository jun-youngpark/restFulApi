create table mzbtsendtran
(
    sn varchar(100) not null comment '발송요청id'
        primary key,
    sender_key varchar(40) not null comment '발신프로필키',
    channel varchar(1) not null comment '채널',
    phone_num varchar(16) not null comment '수신자휴대폰번호',
    msg_type varchar(2) not null comment '메시지 발송타입',
    tmpl_cd varchar(50) not null comment '템플릿코드',
    req_dept_cd varchar(50) default 'admin' not null comment '발송요청부서코드',
    req_usr_id varchar(50) default 'admin' not null comment '발송요청자id',
    req_dtm varchar(14) not null comment '발송요청일시',
    snd_dtm varchar(14) null comment '발송일시',
    rslt_cd varchar(10) null comment '발송결과코드',
    rcpt_msg varchar(250) null comment '발송결과메시지',
    rcpt_dtm varchar(14) null comment '결과응답일시',
    tran_sn decimal(16) null comment '전송일련번호',
    tran_sts varchar(1) default '1' not null comment '전송상태',
    agent_id varchar(20) null comment '에이전트id',
    slot1 varchar(100) null comment '발송요청부가정보1',
    slot2 varchar(100) null comment '발송요청부가정보2',
    req_id varchar(30) null comment '요청별 id',
    msg_id varchar(30) null comment '메시지 id',
    cont_type varchar(1)  null comment '텍스트유형(F:고정형, V:변수형)',
    tmpl_type varchar(1)  null comment '변수형 옵션(J:전문 방식, V:변수 방식)',
    snd_msg varchar(4000)   null comment '템플릿 본문',
    attachment varchar(4000)  null comment '메시지에 첨부할 내용',
    msg_vr varchar(4000)    null comment '템플릿 본문에 포함된 변수값',
    btn_vr varchar(4000)    null comment '템플릿 버튼 링크에 포함된 변수값',
    target_type varchar(2) default 'BD' not null comment '타겟팅 옵션 (BD: 타겟팅 안함(기본), BN: 채널 친구 제외)',
    bt_type varchar(1) null comment '브랜드톡유형'
) engine=innodb default charset=utf8 collate=utf8_bin
comment '브랜드톡 발송요청';

create index idx_mzbtsendtran_mlt1
    on mzbtsendtran (tran_sts, req_dtm);

create index idx_mzbtsendtran_mlt2
    on mzbtsendtran (req_dtm, agent_id);

create index idx_mzbtsendtran_msg_id
    on mzbtsendtran (msg_id);

create index idx_mzbtsendtran_req_id
    on mzbtsendtran (req_id);

create index idx_mzbtsendtran_tran_sn
    on mzbtsendtran (tran_sn);


create table mzbtsendlog
(
    sn varchar(100) not null comment '발송요청id'
        primary key,
    sender_key varchar(40) not null comment '발신프로필키',
    channel varchar(1) not null comment '채널',
    phone_num varchar(16) not null comment '수신자휴대폰번호',
    msg_type varchar(2) not null comment '메시지 발송타입',
    tmpl_cd varchar(50) not null comment '템플릿코드',
    req_dept_cd varchar(50) default 'admin' not null comment '발송요청부서코드',
    req_usr_id varchar(50) default 'admin' not null comment '발송요청자id',
    req_dtm varchar(14) not null comment '발송요청일시',
    snd_dtm varchar(14) null comment '발송일시',
    rslt_cd varchar(10) null comment '발송결과코드',
    rcpt_msg varchar(250) null comment '발송결과메시지',
    rcpt_dtm varchar(14) null comment '결과응답일시',
    tran_sn decimal(16) null comment '전송일련번호',
    tran_sts varchar(1) default '1' not null comment '전송상태',
    agent_id varchar(20) null comment '에이전트id',
    slot1 varchar(100) null comment '발송요청부가정보1',
    slot2 varchar(100) null comment '발송요청부가정보2',
    req_id varchar(30) null comment '요청별 id',
    msg_id varchar(30) null comment '메시지 id',
    cont_type varchar(1)  null comment '텍스트유형(F:고정형, V:변수형)',
    tmpl_type varchar(1)  null comment '변수형 옵션(J:전문 방식, V:변수 방식)',
    snd_msg varchar(4000)   null comment '템플릿 본문',
    attachment varchar(4000)  null comment '메시지에 첨부할 내용',
    msg_vr varchar(4000)    null comment '템플릿 본문에 포함된 변수값',
    btn_vr varchar(4000)    null comment '템플릿 버튼 링크에 포함된 변수값',
    target_type varchar(2) default 'BD' not null comment '타겟팅 옵션 (BD: 타겟팅 안함(기본), BN: 채널 친구 제외)',
    bt_type varchar(1) null comment '브랜드톡유형'
) engine=innodb default charset=utf8 collate=utf8_bin
comment '브랜드톡 발송결과';

/* **************************************************************************
*  wiseu 연동 스키마
*************************************************************************** */

-- 재발송 시 사용
-- service_no / service_type / result_seq 추가
alter table mzbtsendtran  add list_seq        varchar(10);
alter table mzbtsendtran  add customer_key    varchar(100);
alter table mzbtsendtran  add service_no    numeric(15);
alter table mzbtsendtran  add service_type  varchar(2);
alter table mzbtsendtran  add result_seq    numeric(16);
alter table mzbtsendlog   add list_seq        varchar(10);
alter table mzbtsendlog   add customer_key    varchar(100);
alter table mzbtsendlog   add service_no    numeric(15);
alter table mzbtsendlog   add service_type  varchar(2);
alter table mzbtsendlog   add result_seq    numeric(16);

-- 발송 결과를 wiseu sendlog 테이블에 업데이트하는 작업 플래그
alter table mzbtsendlog add sync_flag char(1) default 'N' not null;
create index idx_mzbtsendlog_sync_flag on mzbtsendlog (sync_flag);

