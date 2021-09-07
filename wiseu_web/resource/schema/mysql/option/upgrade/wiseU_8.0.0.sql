-- 쿼리 수정
-- 리포트 메뉴 dept 삭제, 월별 발송 통계 중복 명칭 변경
update nvmenu set pmenu_cd='03',level_no=1 where menu_cd in('030106','030107','030203','030204','030201','030101','030105');
delete from nvmenu where menu_cd in('0301','0302');
update nvmenu  set menu_nm='캠페인 월별 통계' where menu_cd='030106';
update nvmenu  set menu_nm='이케어 월별 통계' where menu_cd='030203';
update nvmenu_lang set menu_nm='캠페인 월별 통계' where menu_cd='030106' and lang='ko';
update nvmenu_lang set menu_nm='이케어 월별 통계' where menu_cd='030203' and lang='ko';
update nvmenu_lang set menu_nm='Campaign monthly report' where menu_cd='030106' and lang='en';
update nvmenu_lang set menu_nm='Ecare monthly report' where menu_cd='030203' and lang='en';
update nvserverinfo set altalk_resend_error_cd = '0000,A1000' where HOST_NM = '1';

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AA0001', '010', 'AA00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AF0001', '010', 'AF00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AC0001', '010', 'AC00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', '010', 'AS00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AP0001','010','AP0001','발송중(결과수신전)','발송중(결과수신전)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'ko');