-- sms 리포트 전송 성공자 다운로드
create view v_sms_success_file_log (
    tran_id, tran_phone, tran_date, tran_rslt, service_type,
    service_no, result_seq, slot1, slot2, tran_type
) as
select
    tr_id as tran_id, tr_phone as tran_phone, tran_date, tr_rsltstat as tran_rslt, service_type,
    service_no, result_seq, slot1, slot2, tr_etc2 as tran_type
from sc_log
where tr_rsltstat = '06'
union all
select
    id as tran_id, phone as tran_phone, tran_date, rslt as tran_rslt, service_type,
    service_no, result_seq, slot1, slot2, etc2 as tran_type
from mms_log
where rslt = '1000';

-- sms 리포트 전송 실패자 다운로드
create view v_sms_fail_file_log (
    tran_id, tran_phone, tran_date, tran_rslt, service_type,
    service_no, result_seq, slot1, slot2, tran_type
) as
select
    tr_id as tran_id, tr_phone as tran_phone, date_format(tr_senddate, '%y-%m-%d %h:%i') tran_date, tr_rsltstat as tran_rslt, service_type,
    service_no, result_seq, slot1, slot2, tr_etc2 as tran_type
from sc_log
where tr_rsltstat = '06'
union all
select
    id as tran_id, phone as tran_phone, date_format(reqdate, '%y-%m-%d %h:%i') tran_date, rslt as tran_rslt, service_type,
    service_no, result_seq, slot1, slot2, etc2 as tran_type
from mms_log
where rslt = '1000';

-- sms 리포트 오류 분석 리스트
create view v_sms_error_result (
    tran_date, tran_rslt, service_type, service_no, result_seq
) as
select
    date_format(tr_senddate,'%y%m%d%') as tran_date, tr_rsltstat as tran_rslt, service_type, service_no, result_seq
from sc_log
where tr_rsltstat not in ('06')
union all
select
    date_format(reqdate,'%y%m%d%') as tran_date, rslt as tran_rslt, service_type, service_no, result_seq
from mms_log
where rslt not in ('1000');

-- 연동 쪽에서 sms 결과를 조회하는 용도 (nvrealtimeaccept를 통한 준실시간 연동에서 사용)
create view view_sms_log (
    tran_phone, tran_callback, tran_rsltdate, req_user_id, status,
    tran_rslt, tran_msg, sms_type, slot1, slot2, seq
) as
select
    phone, callback, rsltdate as tran_rsltdate, req_user_id, status,
    (case when status='3' and rslt='1000' then '06' else rslt end) as rslt, msg, etc2, slot1, slot2, seq
from mms_log
union all
select
    tr_phone, tr_callback, tr_rsltdate, req_user_id, '3',
    tr_rsltstat, tr_msg, 'sms', slot1, slot2, seq
from sc_log;
