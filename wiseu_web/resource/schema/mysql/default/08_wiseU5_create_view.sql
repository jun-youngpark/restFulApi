CREATE VIEW v_ecarerptsendresult (
    ecare_no, accu_cnt, service_type, report_dt, target_cnt, send_cnt, success_cnt,
    soft_bounce_cnt, hard_bounce_cnt, returnmail_cnt, open_cnt, duration_cnt, link_cnt
) AS
SELECT
    ecare_no,
    count(ecare_no) AS accu_cnt,
    (case max(result_seq) WHEN 1 THEN 'R' ELSE 'S' END) AS service_type,
    max(report_dt) AS report_dt,
    sum(target_cnt) AS target_cnt,
    sum(send_cnt) AS send_cnt,
    sum(success_cnt) AS success_cnt,
    sum(smtp_except_cnt + no_route_cnt + refused_cnt + etc_except_cnt) AS soft_bounce_cnt,
    sum(unknown_uSER_cnt + unknown_host_cnt + invalid_address_cnt) AS hard_bounce_cnt,
    sum(returnmail_cnt) AS returnmail_cnt,
    sum(open_cnt) AS open_cnt,
    sum(duration_cnt) AS DUration_cnt,
    sum(link_cnt) AS link_cnt
FROM nvecarerptsendresult
GROUP BY ecare_no;

CREATE VIEW v_nvtestuser (
    user_id, seq_no, campaign_no, campaign_type,
    testreceiver_email, testreceiver_tel, testreceiver_fax, testreceiver_nm
) AS
SELECT
    u.user_id, u.seq_no, u.campaign_no, u.campaign_type,
    p.testreceiver_email, p.testreceiver_tel, p.testreceiver_fax, p.testreceiver_nm
FROM nvtestuser u, nvtestuserpool p
WHERE u.user_id = p.user_id
AND u.seq_no = p.seq_no;