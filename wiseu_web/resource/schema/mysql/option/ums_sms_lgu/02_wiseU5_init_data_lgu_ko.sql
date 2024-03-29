-- 코드마스터 : SMS 오류사항 관련 공통코드
-- (1) SMS 결과
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS00', '-', '00', 'SMS 공통', 'SMS에서 사용하는 공통코드', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '00', 'AS00', '결과 수신 대기', '결과 수신 대기', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '01', 'AS00', '시스템 장애', '시스템 장애', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '02', 'AS00', '인증실패, 직후 연결을 끊음', '인증실패, 직후 연결을 끊음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '03', 'AS00', '메시지 형식 오류', '메시지 형식 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '04', 'AS00', 'BIND 안됨', 'BIND 안됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '05', 'AS00', '번호형식오류', '번호형식오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '06', 'AS00', '전송 성공', '전송 성공', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '07', 'AS00', '비가입자, 결번, 서비스정지', '비가입자, 결번, 서비스정지', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '08', 'AS00', '단말기 Power-off 상태', '단말기 Power-off 상태', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '09', 'AS00', '음영', '음영', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '10', 'AS00', '단말기 메시지 FULL', '단말기 메시지 FULL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '11', 'AS00', '기타에러(타임아웃등 11: 이통사, 14: 무선망)', '기타에러(타임아웃등 11: 이통사, 14: 무선망)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '13', 'AS00', '번호이동관련', '번호이동관련', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '14', 'AS00', '기타에러(타임아웃등 11: 이통사, 14: 무선망)', '기타에러(타임아웃등 11: 이통사, 14: 무선망)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '18', 'AS00', '메시지 중복 오류', '메시지 중복 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '19', 'AS00', '월 송신 건수 초과', '월 송신 건수 초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '20', 'AS00', 'UNKNOWN', 'UNKNOWN', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '21', 'AS00', '착신번호 에러(자리수에러)', '착신번호 에러(자리수에러)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '22', 'AS00', '착신번호 에러(없는 국번)', '착신번호 에러(없는 국번)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '23', 'AS00', '수신거부 메시지 없음', '수신거부 메시지 없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '24', 'AS00', '21시 이후 광고', '21시 이후 광고', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '25', 'AS00', '성인광고, 대출광고 등 기타 제한', '성인광고, 대출광고 등 기타 제한', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '40', 'AS00', '단말기착신거부(스팸등)', '단말기착신거부(스팸등)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '70', 'AS00', '기타오류 - KTF URL', '기타오류 - KTF URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '80', 'AS00', '결번(이통사 Nack) - SKT URL', '결번(이통사 Nack) - SKT URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '81', 'AS00', '전송실패(정지고객등) - SKT URL', '전송실패(정지고객등) - SKT URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '82', 'AS00', '번호이동 DB 조회불가 - SKT URL', '번호이동 DB 조회불가 - SKT URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '83', 'AS00', '번호이동번호 - SKT URL', '번호이동번호 - SKT URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '84', 'AS00', '타임아웃(이통사) - SKT URL', '타임아웃(이통사) - SKT URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0001', '85', 'AS00', '전송실패(기타에러) - SKT URL', '전송실패(기타에러) - SKT URL', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
-- (2) MMS 결과
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '1000', 'AS00', '성공', '성공', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2000', 'AS00', '포맷 관련 알 수 없는 오류 발생', '포맷 관련 알 수 없는 오류 발생', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2001', 'AS00', '주소(포맷) 에러 - 전화번호 유효하지않음,결번,번호이동', '주소(포맷) 에러 - 전화번호 유효하지않음,결번,번호이동', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2002', 'AS00', 'Content-length 오류', 'Content-length 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2003', 'AS00', 'MIME 형식 오류 (컨텐츠 개체, 사이즈, 타입 등의 오류)', 'MIME 형식 오류 (컨텐츠 개체, 사이즈, 타입 등의 오류)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2004', 'AS00', 'Message ID 오류 (중복, 부재)', 'Message ID 오류 (중복, 부재)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2005', 'AS00', 'Head 내 각 필드의 부적절', 'Head 내 각 필드의 부적절', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2006', 'AS00', 'Body 내 각 필드의 부적절', 'Body 내 각 필드의 부적절', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '2007', 'AS00', '지원하지 않는 미디어 존재', '지원하지 않는 미디어 존재', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3000', 'AS00', 'MMS를 미 지원 단말', 'MMS를 미 지원 단말', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3001', 'AS00', '단말 수신용량 초과', '단말 수신용량 초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3002', 'AS00', '전송 시간 초과- 단말기정상수신Report가 4일이내 미도착', '전송 시간 초과- 단말기정상수신Report가 4일이내 미도착', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3003', 'AS00', '읽기 확인 미 지원 단말', '읽기 확인 미 지원 단말', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3004', 'AS00', '전원 꺼짐', '전원 꺼짐', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3005', 'AS00', '음영지역', '음영지역', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '3006', 'AS00', '기타', '기타', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '4000', 'AS00', '서버실패(프로세스 또는 시스템 에러)', '서버실패(프로세스 또는 시스템 에러)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '4001', 'AS00', '인증실패 - 1.G/W인증실패 2.단말기(SKT)제한적인증실패', '인증실패 - 1.G/W인증실패 2.단말기(SKT)제한적인증실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '4002', 'AS00', '네트워크 에러 발생', '네트워크 에러 발생', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '4003', 'AS00', '서비스의 일시적인 에러', '서비스의 일시적인 에러', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '5000', 'AS00', '번호이동에러', '번호이동에러', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '5001', 'AS00', '선불발급 발송건수 초과', '선불발급 발송건수 초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9001', 'AS00', '유효시간 초과', '유효시간 초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9002', 'AS00', '폰 넘버 에러', '폰 넘버 에러', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9003', 'AS00', '스팸 번호(스팸 테이블 사용시)', '스팸 번호(스팸 테이블 사용시)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9004', 'AS00', '이통사에서 응답 없음', '이통사에서 응답 없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9005', 'AS00', '파일크기 오류', '파일크기 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9006', 'AS00', '지원되지 않는 파일', '지원되지 않는 파일', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AS0002', '9007', 'AS00', '파일오류', '파일오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
