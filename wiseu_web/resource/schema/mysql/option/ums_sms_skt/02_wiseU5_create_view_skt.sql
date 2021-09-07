-- SMS 리포트 전송 성공자 다운로드
CREATE VIEW v_sms_success_file_log (
    tran_id, tran_phone, tran_date, tran_rslt, service_type, tran_status, tran_msg, tran_callback,
    service_no, result_seq, slot1, slot2, tran_type, req_user_id, seq, list_seq, req_dept_id
) AS
SELECT
    tran_id, tran_phone, tran_date, tran_rslt, service_type, tran_status, tran_msg, tran_callback,
    service_no, result_seq, slot1, slot2, tran_etc3 as tran_type, req_user_id, seq, list_seq, req_dept_id
FROM em_log
WHERE tran_rslt IN ('0', '00000');

-- SMS 리포트 전송 실패자 다운로드
CREATE VIEW v_sms_fail_file_log (
    tran_id, tran_phone, tran_date, tran_rslt, service_type, tran_status, tran_msg, tran_callback,
    service_no, result_seq, slot1, slot2, tran_type, req_user_id, seq, list_seq, req_dept_id
) AS
SELECT
    tran_id, tran_phone, tran_date, tran_rslt, service_type, tran_status, tran_msg, tran_callback,
    service_no, result_seq, slot1, slot2, tran_etc3 as tran_type, req_user_id, seq, list_seq, req_dept_id
FROM em_log
WHERE tran_rslt NOT IN ('0', '00000');

-- SMS 리포트 오류 분석 리스트
CREATE VIEW v_sms_error_result (
    tran_date, tran_rslt, service_type, service_no, result_seq, seq, list_seq
) AS
SELECT
    DATE_FORMAT(tran_date,'%Y%m%d%') tran_date, tran_rslt, service_type, service_no, result_seq, seq, list_seq
FROM em_log
WHERE tran_rslt NOT IN ('0', '00000');

-- 연동 쪽에서 SMS 결과를 조회하는 용도 (NVREALTIMEACCEPT를 통한 준실시간 연동에서 사용)
CREATE VIEW view_sms_log (
    tran_phone, tran_callback, tran_rsltdate, req_user_id, tran_rslt,
    tran_msg, sms_type, slot1, slot2, seq, list_seq
) AS
SELECT
    tran_phone, tran_callback, tran_rsltdate, req_user_id, tran_rslt,
    (CASE WHEN mms_body IS NULL THEN tran_msg ELSE mms_body END) AS tran_msg, tran_etc3 AS sms_type, slot1, slot2, seq, log.list_seq
FROM em_log log LEFT OUTER JOIN em_tran_mms mms
ON log.tran_etc4 = mms.mms_seq;