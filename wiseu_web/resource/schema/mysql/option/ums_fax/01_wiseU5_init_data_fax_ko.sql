-- 코드마스터 : FAX 오류사항 관련 공통코드
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF00', '-', '00', 'FAX 공통', 'FAX에서 사용하는 공통코드', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '0', 'AF00', '송신 성공', '송신 성공', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '1', 'AF00', '송신 대기', '송신 대기', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '2', 'AF00', '응답 없음 (전화번호 오류 등)', '응답 없음 (전화번호 오류 등)', NULL,'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '3', 'AF00', '통화중', '통화중', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '4', 'AF00', '없는 전화번호', '없는 전화번호', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '6', 'AF00', '잘못된 이미지', '잘못된 이미지', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '7', 'AF00', '알 수 없음', '알 수 없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '8', 'AF00', '문서생성 실패', '문서생성 실패', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '9', 'AF00', '문서 없음', '문서 없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '10', 'AF00', '일반전화 / 음성사서함', '일반전화 / 음성사서함', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '11', 'AF00', '전화번호 오류', '전화번호 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '12', 'AF00', '일부페이지만 성공', '일부페이지만 성공', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '13', 'AF00', '시스템 에러', '시스템 에러', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '14', 'AF00', '통신망 장애', '통신망 장애', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '15', 'AF00', '보드 API 오류', '보드 API 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '16', 'AF00', '송수신 거부', '송수신 거부', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '17', 'AF00', '시나리오 종료', '시나리오 종료', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '18', 'AF00', 'Socket Error', 'Socket Error', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '22', 'AF00', '송신 취소', '송신 취소', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '900', 'AF00', '핸들러 오류', '핸들러 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AF0001', '010', 'AF00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');