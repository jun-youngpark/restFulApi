-- 인포빕 연동 컬럼 추가 및 변경
ALTER TABLE em_tran    MODIFY COLUMN tran_id   VARCHAR(50);
ALTER TABLE em_tran    MODIFY COLUMN tran_rslt VARCHAR(5);
ALTER TABLE em_tran    ADD tran_status2   VARCHAR(4) DEFAULT NULL;
ALTER TABLE em_tran    ADD tran_mcc VARCHAR(3) DEFAULT NULL;
ALTER TABLE em_tran    ADD tran_mnc VARCHAR(3) DEFAULT NULL;
ALTER TABLE em_tran    ADD message_id VARCHAR(100) DEFAULT NULL;
ALTER TABLE em_tran    ADD tran_sn NUMERIC;
ALTER TABLE em_tran    ADD agent_id VARCHAR(20);
ALTER TABLE em_tran    ADD infobip_id VARCHAR(50);

ALTER TABLE em_log    MODIFY COLUMN tran_id   VARCHAR(50);
ALTER TABLE em_log    MODIFY COLUMN tran_rslt VARCHAR(5);
ALTER TABLE em_log    ADD tran_status2   VARCHAR(4) DEFAULT NULL;
ALTER TABLE em_log    ADD tran_mcc VARCHAR(3) DEFAULT NULL;
ALTER TABLE em_log    ADD tran_mnc VARCHAR(3) DEFAULT NULL;
ALTER TABLE em_log    ADD message_id VARCHAR(100) DEFAULT NULL;
ALTER TABLE em_log    ADD tran_sn NUMERIC;
ALTER TABLE em_log    ADD agent_id VARCHAR(20);
ALTER TABLE em_log    ADD INFOBIP_ID VARCHAR(50);

-- 인포빕 연동 인덱스 추가
-- ALTER TABLE EM_TRAN DROP INDEX IDX_EM_TRAN_01;
-- CREATE INDEX IDX_EM_TRAN_01 on em_tran (tran_status, tran_date);
CREATE INDEX idx_em_tran_02 ON em_tran (tran_sn, tran_status, agent_id);
CREATE INDEX idx_em_tran_03 ON em_tran (infobip_id, tran_date);

-- ALTER TABLE EM_LOG DROP INDex idx_em_log_01;
CREATE INDEX idx_em_log_01 ON em_log (tran_status, tran_date);
CREATE INDEX idx_em_log_02 ON em_log (tran_sn,tran_status,agent_id);
CREATE INDEX idx_em_log_03 ON em_log (infobip_id,agent_id);

-- 인포빕 코드 정보
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '00000', 'AS00', '오류없음', '오류없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10001', 'AS00', '알수없는가입자', '알수없는가입자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10005', 'AS00', '식별되지않은가입자', '식별되지않은가입자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10006', 'AS00', '부재중인가입자', '부재중인가입자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10009', 'AS00', '불법가입자', '불법가입자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30010', 'AS00', '전달자서비스가프로비저닝되지않음', '전달자서비스가프로비저닝되지않음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10011', 'AS00', '텔레서비스가프로비저닝되지않음', '텔레서비스가프로비저닝되지않음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10012', 'AS00', '불법장비','불법장비', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10013', 'AS00', '통화차단','통화차단', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30020', 'AS00', 'SS비호환성','SS비호환성', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10021', 'AS00', '지원되지않는설비','지원되지않는설비', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10031', 'AS00', 'MtSMS에대해가입자통화중','MtSMS에대해가입자통화중', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10032', 'AS00', 'SM전달실패','SM전달실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10033', 'AS00', '메시지대기목록꽉참','메시지대기목록꽉참', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10034', 'AS00', '시스템장애','시스템장애', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10035', 'AS00', '데이터누락','데이터누락', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10036', 'AS00', '예상치못한데이터값','예상치못한데이터값', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30051', 'AS00', '리소스제한','리소스제한', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30071', 'AS00', '알수없는알파벳','알수없는알파벳', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10072', 'AS00', 'Ussd통화중','Ussd통화중', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10255', 'AS00', '알수없는오류','EC_UNK알수없는오류NOWN_ERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10256', 'AS00', 'SMDF메모리용량초과','SMDF메모리용량초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10257', 'AS00', 'SMDF장비프로토콜오류','SMDF장비프로토콜오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10258', 'AS00', 'SMDF장비프로토콜오류','SMDF장비프로토콜오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10259', 'AS00', 'SMDF장비프로토콜오류','SMDF장비프로토콜오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10260', 'AS00', 'SMDF장비프로토콜오류','SMDF장비프로토콜오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10261', 'AS00', 'SMDFInvalidSME주소','SMDFInvalidSME주소', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10262', 'AS00', 'SMDFSubscribernotsc가입자','SMDFSubscribernotsc가입자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10500', 'AS00', '공급업체일반오류','공급업체일반오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30501', 'AS00', '잘못된응답수신','잘못된응답수신', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10502', 'AS00', '응답없음','응답없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10503', 'AS00', '서비스완료실패','서비스완료실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10504', 'AS00', '피어로부터예상치못한응답','피어로부터예상치못한응답', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10507', 'AS00', '잘못입력된매개변수','잘못입력된매개변수', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10508', 'AS00', '지원되는서비스','지원되는서비스', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10509', 'AS00', '중복된ID호출','중복된ID호출', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10511', 'AS00', '릴리즈시작중','릴리즈시작중', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11024', 'AS00', '앱컨텍스트지원안됨','앱컨텍스트지원안됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11026', 'AS00', '잘못된원본참조','잘못된원본참조', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11027', 'AS00', '캡슐화된AC지원안됨','캡슐화된AC지원안됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11028', 'AS00', '전송보호부적절함','전송보호부적절함', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11029', 'AS00', '제공된사유없음','제공된사유없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11030', 'AS00', '잠재적버전비호환성','잠재적버전비호환성', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11031', 'AS00', '원격노드연결불가','원격노드연결불가', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11152', 'AS00', '이러한특성의주소에대한변환없음','이러한특성의주소에대한변환없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11153', 'AS00', '이특정주소에대한변환없음','이특정주소에대한변환없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11154', 'AS00', '하위시스템정체','하위시스템정체', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11155','AS00','하위시스템장애','하위시스템장애', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11156','AS00','장비를갖추지않은사용자','장비를갖추지않은사용자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11157','AS00','MTP실패','MTP실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11158','AS00','네트워크정체','네트워크정체', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11159','AS00','자격없음','자격없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11160','AS00','메시지전송XUDT에오류있음','메시지전송XUDT에오류있음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11161','AS00','로컬처리XUDT에오류있음','로컬처리XUDT에오류있음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11162','AS00','목적지에서리어셈블리XUDT를수행할수없음','목적지에서리어셈블리XUDT를수행할수없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11163','AS00','SCCP실패','SCCP실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11164','AS00','홉카운터위반','홉카운터위반', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11165','AS00','세그먼트화지원안됨','세그먼트화지원안됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11166','AS00','세그먼트화실패','세그먼트화실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11281','AS00','사용자별사유','사용자별사유', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11282','AS00','사용자리소스제한','사용자리소스제한', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11283','AS00','리소스사용불가','리소스사용불가', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11284','AS00','애플리케이션프로시저취소','애플리케이션프로시저취소', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11536','AS00','공급업체오작동','공급업체오작동', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11537','AS00','지원대화상자또는트랜잭션릴리즈됨','지원대화상자또는트랜잭션릴리즈됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11538','AS00','리소스제한','리소스제한', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11539','AS00','유지보수활동','유지보수활동', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11540','AS00','버전비호환성','버전비호환성', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11541','AS00','비정상적맵대화상자','비정상적맵대화상자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11792','AS00','피어에서비정상적이벤트감지됨','피어에서비정상적이벤트감지됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11793','AS00','피어에서응답거부','피어에서응답거부', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11794','AS00','피어로부터비정상적이벤트수신됨','피어로부터비정상적이벤트수신됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11795','AS00','메시지를피어에전달할수없음','메시지를피어에전달할수없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11796','AS00','공급업체호출불가','공급업체호출불가', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '32048','AS00','시간초과','시간초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '22049','AS00','IMSI차단됨','IMSI차단됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '32050','AS00','DND차단됨','DND차단됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '32051','AS00','텍스트차단됨','텍스트차단됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '24096','AS00','잘못된PDU형식','잘못된PDU형식', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '34097','AS00','GMSC에제출되지않음','GMSC에제출되지않음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '24100','AS00','메시지취소됨','메시지취소됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '24101','AS00','Validityperiod만료됨','Validityperiod만료됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '34102','AS00','Smpp채널에제출안됨','Smpp채널에제출안됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '05000','AS00','사람이통화에응답함','사람이통화에응답함', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '05001','AS00','기계가통화에응답함','기계가통화에응답함', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '05002','AS00','통화시도중에사용자가통화중이었음','통화시도중에사용자가통화중이었음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '25003','AS00','사용자에게알렸지만전화를받지않음','사용자에게알렸지만전화를받지않음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '25004','AS00','통화를위해제공된파일을다운로드할수없었음','통화를위해제공된파일을다운로드할수없었음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '25005','AS00','통화를위해제공된파일의형식이지원되지않음','통화를위해제공된파일의형식이지원되지않음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '99990','AS00','HTTP 오류','인포빕 API로 HTTP 통신하는 동안 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '99999','AS00','에이전트 오류','에이전트내 처리하는 동안 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '00000', 'AS00', 'NO_ERROR', 'NO_ERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10001', 'AS00', 'EC_UNKNOWN_SUBSCRIBER', 'EC_UNKNOWN_SUBSCRIBER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10005', 'AS00', 'EC_UNIDENTIFIED_SUBSCRIBER', 'EC_UNIDENTIFIED_SUBSCRIBER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10006', 'AS00', 'EC_ABSENT_SUBSCRIBER_SM', 'EC_ABSENT_SUBSCRIBER_SM', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10009', 'AS00', 'EC_ILLEGAL_SUBSCRIBER', 'EC_ILLEGAL_SUBSCRIBER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30010', 'AS00', 'EC_BEARER_SERVICE_NOT_PROVISIONED', 'EC_BEARER_SERVICE_NOT_PROVISIONED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10011', 'AS00', 'EC_TELESERVICE_NOT_PROVISIONED', 'EC_TELESERVICE_NOT_PROVISIONED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10012', 'AS00', 'EC_ILLEGAL_EQUIPMENT','EC_ILLEGAL_EQUIPMENT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10013', 'AS00', 'EC_CALL_BARRED','EC_CALL_BARRED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30020', 'AS00', 'EC_SS_INCOMPATIBILITY','EC_SS_INCOMPATIBILITY', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10021', 'AS00', 'EC_FACILITY_NOT_SUPPORTED','EC_FACILITY_NOT_SUPPORTED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10031', 'AS00', 'EC_SUBSCRIBER_BUSY_FOR_MT_SMS','EC_SUBSCRIBER_BUSY_FOR_MT_SMS', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10032', 'AS00', 'EC_SM_DELIVERY_FAILURE','EC_SM_DELIVERY_FAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10033', 'AS00', 'EC_MESSAGE_WAITING_LIST_FULL','EC_MESSAGE_WAITING_LIST_FULL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10034', 'AS00', 'EC_SYSTEM_FAILURE','EC_SYSTEM_FAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10035', 'AS00', 'EC_DATA_MISSING','EC_DATA_MISSING', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10036', 'AS00', 'EC_UNEXPECTED_DATA_VALUE','EC_UNEXPECTED_DATA_VALUE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30051', 'AS00', 'EC_RESOURCE_LIMITATION','EC_RESOURCE_LIMITATION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30071', 'AS00', 'EC_UNKNOWN_ALPHABET','EC_UNKNOWN_ALPHABET', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10072', 'AS00', 'EC_USSD_BUSY','EC_USSD_BUSY', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10255', 'AS00', 'EC_UNKNOWN_ERROR','EC_UNKNOWN_ERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10256', 'AS00', 'EC_SM_DF_MEMORYCAPACITYEXCEEDED','EC_SM_DF_MEMORYCAPACITYEXCEEDED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10257', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10258', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10259', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10260', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10261', 'AS00', 'EC_SM_DF_INVALIDSME_ADDRESS','EC_SM_DF_INVALIDSME_ADDRESS', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10262', 'AS00', 'EC_SM_DF_SUBSCRIBERNOTSC_SUBSCRIBER','EC_SM_DF_SUBSCRIBERNOTSC_SUBSCRIBER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10500', 'AS00', 'EC_PROVIDER_GENERAL_ERROR','EC_PROVIDER_GENERAL_ERROR', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '30501', 'AS00', 'EC_INVALID_RESPONSE_RECEIVED','EC_INVALID_RESPONSE_RECEIVED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10502', 'AS00', 'EC_NO_RESPONSE','EC_NO_RESPONSE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10503', 'AS00', 'EC_SERVICE_COMPLETION_FAILURE','EC_SERVICE_COMPLETION_FAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10504', 'AS00', 'EC_UNEXPECTED_RESPONSE_FROM_PEER','EC_UNEXPECTED_RESPONSE_FROM_PEER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10507', 'AS00', 'EC_MISTYPED_PARAMETER','EC_MISTYPED_PARAMETER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10508', 'AS00', 'EC_NOT_SUPPORTED_SERVICE','EC_NOT_SUPPORTED_SERVICE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10509', 'AS00', 'EC_DUPLICATED_INVOKE_ID','EC_DUPLICATED_INVOKE_ID', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '10511', 'AS00', 'EC_INITIATING_RELEASE','EC_INITIATING_RELEASE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11024', 'AS00', 'EC_OR_APPCONTEXTNOTSUPPORTED','EC_OR_APPCONTEXTNOTSUPPORTED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11026', 'AS00', 'EC_OR_INVALIDORIGINATINGREFERENCE','EC_OR_INVALIDORIGINATINGREFERENCE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11027', 'AS00', 'EC_OR_ENCAPSULATEDAC_NOTSUPPORTED','EC_OR_ENCAPSULATEDAC_NOTSUPPORTED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11028', 'AS00', 'EC_OR_TRANSPORTPROTECTIONNOTADEQUATE','EC_OR_TRANSPORTPROTECTIONNOTADEQUATE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11029', 'AS00', 'EC_OR_NOREASONGIVEN','EC_OR_NOREASONGIVEN', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11030', 'AS00', 'EC_OR_POTENTIALVERSIONINCOMPATIBILITY','EC_OR_POTENTIALVERSIONINCOMPATIBILITY', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11031', 'AS00', 'EC_OR_REMOTENODENOTREACHABLE','EC_OR_REMOTENODENOTREACHABLE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11152', 'AS00', 'EC_NNR_NOTRANSLATIONFORANADDRESSOFSUCHNATURE','EC_NNR_NOTRANSLATIONFORANADDRESSOFSUCHNATURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11153', 'AS00', 'EC_NNR_NOTRANSLATIONFORTHISSPECIFICADDRESS','EC_NNR_NOTRANSLATIONFORTHISSPECIFICADDRESS', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11154', 'AS00', 'EC_NNR_SUBSYSTEMCONGESTION','EC_NNR_SUBSYSTEMCONGESTION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11155','AS00','EC_NNR_SUBSYSTEMFAILURE','EC_NNR_SUBSYSTEMFAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11156','AS00','EC_NNR_UNEQUIPPEDUSER','EC_NNR_UNEQUIPPEDUSER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11157','AS00','EC_NNR_MTPFAILURE','EC_NNR_MTPFAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11158','AS00','EC_NNR_NETWORKCONGESTION','EC_NNR_NETWORKCONGESTION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11159','AS00','EC_NNR_UNQUALIFIED','EC_NNR_UNQUALIFIED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11160','AS00','EC_NNR_ERRORINMESSAGETRANSPORTXUDT','EC_NNR_ERRORINMESSAGETRANSPORTXUDT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11161','AS00','EC_NNR_ERRORINLOCALPROCESSINGXUDT','EC_NNR_ERRORINLOCALPROCESSINGXUDT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11162','AS00','EC_NNR_DESTINATIONCANNOTPERFORMREASSEMBLYXUDT','EC_NNR_DESTINATIONCANNOTPERFORMREASSEMBLYXUDT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11163','AS00','EC_NNR_SCCPFAILURE','EC_NNR_SCCPFAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11164','AS00','EC_NNR_HOPCOUNTERVIOLATION','EC_NNR_HOPCOUNTERVIOLATION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11165','AS00','EC_NNR_SEGMENTATIONNOTSUPPORTED','EC_NNR_SEGMENTATIONNOTSUPPORTED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11166','AS00','EC_NNR_SEGMENTATIONFAILURE','EC_NNR_SEGMENTATIONFAILURE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11281','AS00','EC_UA_USERSPECIFICREASON','EC_UA_USERSPECIFICREASON', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11282','AS00','EC_UA_USERRESOURCELIMITATION','EC_UA_USERRESOURCELIMITATION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11283','AS00','EC_UA_RESOURCEUNAVAILABLE','EC_UA_RESOURCEUNAVAILABLE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11284','AS00','EC_UA_APPLICATIONPROCEDURECANCELLATION','EC_UA_APPLICATIONPROCEDURECANCELLATION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11536','AS00','EC_PA_PROVIDERMALFUNCTION','EC_PA_PROVIDERMALFUNCTION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11537','AS00','EC_PA_SUPPORTINGDIALOGORTRANSACTIONREALEASED','EC_PA_SUPPORTINGDIALOGORTRANSACTIONREALEASED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11538','AS00','EC_PA_RESSOURCELIMITATION','EC_PA_RESSOURCELIMITATION', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11539','AS00','EC_PA_MAINTENANCEACTIVITY','EC_PA_MAINTENANCEACTIVITY', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11540','AS00','EC_PA_VERSIONINCOMPATIBILITY','EC_PA_VERSIONINCOMPATIBILITY', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11541','AS00','EC_PA_ABNORMALMAPDIALOG','EC_PA_ABNORMALMAPDIALOG', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11792','AS00','EC_NC_ABNORMALEVENTDETECTEDBYPEER','EC_NC_ABNORMALEVENTDETECTEDBYPEER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11793','AS00','EC_NC_RESPONSEREJECTEDBYPEER','EC_NC_RESPONSEREJECTEDBYPEER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11794','AS00','EC_NC_ABNORMALEVENTRECEIVEDFROMPEER','EC_NC_ABNORMALEVENTRECEIVEDFROMPEER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11795','AS00','EC_NC_MESSAGECANNOTBEDELIVEREDTOPEER','EC_NC_MESSAGECANNOTBEDELIVEREDTOPEER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '11796','AS00','EC_NC_PROVIDEROUTOFINVOKE','EC_NC_PROVIDEROUTOFINVOKE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '32048','AS00','EC_TIME_OUT','EC_TIME_OUT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '22049','AS00','EC_IMSI_BLACKLISTED','EC_IMSI_BLACKLISTED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '32050','AS00','EC_DEST_ADDRESS_BLACKLISTED','EC_DEST_ADDRESS_BLACKLISTED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '32051','AS00','EC_INVALIDMSCADDRESS','EC_INVALIDMSCADDRESS', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '24096','AS00','EC_INVALID_PDU_FORMAT','EC_INVALID_PDU_FORMAT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '34097','AS00','EC_NOTSUBMITTEDTOGMSC','EC_NOTSUBMITTEDTOGMSC', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '24100','AS00','EC_MESSAGE_CANCELED','EC_MESSAGE_CANCELED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '24101','AS00','EC_VALIDITYEXPIRED','EC_VALIDITYEXPIRED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '34102','AS00','EC_NOTSUBMITTEDTOSMPPCHANNEL','EC_NOTSUBMITTEDTOSMPPCHANNEL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '05000','AS00','VOICE_ANSWERED','VOICE_ANSWERED', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '05001','AS00','VOICE_ANSWERED_MACHINE','VOICE_ANSWERED_MACHINE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '05002','AS00','EC_VOICE_USER_BUSY','EC_VOICE_USER_BUSY', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '25003','AS00','EC_VOICE_NO_ANSWER','EC_VOICE_NO_ANSWER', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '25004','AS00','EC_VOICE_ERROR_DOWNLOADING_FILE','EC_VOICE_ERROR_DOWNLOADING_FILE', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '25005','AS00','EC_VOICE_ERROR_UNSUPPORTED_AUDIO_FORMAT','EC_VOICE_ERROR_UNSUPPORTED_AUDIO_FORMAT', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '99990','AS00','HTTP 오류','인포빕 API로 HTTP 통신하는 동안 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0003', '99999','AS00','에이전트 오류','에이전트내 처리하는 동안 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');