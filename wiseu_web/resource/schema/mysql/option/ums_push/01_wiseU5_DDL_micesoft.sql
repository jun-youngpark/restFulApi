/*
 * micesoft의 오라클 기준 쿼리 입니다.
 * 타 db 사용할 경우 변경이 필요합니다.
 */
alter table tmb_pushmsg add service_type varchar(2);
alter table tmb_pushmsg add service_no numeric(15);
alter table tmb_pushmsg add job_seq numeric(16);
alter table tmb_pushmsg add log_send_fg varchar(1) default 'n';

create index tmb_pushmsg_sd_lsf on tmb_pushmsg (send_dt desc, log_send_fg asc);
create index tmb_pushmsg_msg_no on tmb_pushmsg (msg_no desc);

alter table tmb_pushmsg_recv add log_send_fg varchar(1) default 'n';
create index tmb_pushmsg_recv_lsf on tmb_pushmsg_recv (log_send_fg asc);

alter table tmb_pushmsg_open add log_send_fg varchar(1) default 'n';
create index tmb_pushmsg_open_lsf on tmb_pushmsg_open (log_send_fg asc);

insert into tmb_notify_status ( noti_type, noti_type_nm, sts_cd, updr_id, upd_dt, ttl )
values ( '01', '일반', '000', 'admin', sysdate, 60 );
insert into tmb_notify_status ( noti_type, noti_type_nm, sts_cd, updr_id, upd_dt, ttl )
values ( '02', '정보', '000', 'admin', sysdate, 60 );
insert into tmb_notify_status ( noti_type, noti_type_nm, sts_cd, updr_id, upd_dt, ttl )
values ( '03', '마케팅', '000', 'admin', sysdate, 60 );