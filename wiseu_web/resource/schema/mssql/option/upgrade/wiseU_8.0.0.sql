-- 쿼리 수정
-- 리포트 메뉴 dept 삭제, 월별 발송 통계 중복 명칭 변경
update nvmenu set PMENU_CD='03',LEVEL_NO=1  where MENU_CD in('030106','030107','030203','030204','030201','030101','030105');
delete from nvmenu where MENU_CD in('0301','0302');
update nvmenu  set MENU_NM='캠페인 월별 통계' where MENU_CD='030106';
update nvmenu  set MENU_NM='이케어 월별 통계' where MENU_CD='030203';
update nvmenu_lang set MENU_NM='캠페인 월별 통계' where MENU_CD='030106' and lang='ko';
update nvmenu_lang set MENU_NM='이케어 월별 통계' where MENU_CD='030203' and lang='ko';
update nvmenu_lang set MENU_NM='Campaign monthly report' where MENU_CD='030106' and lang='en';
update nvmenu_lang set MENU_NM='Ecare monthly report' where MENU_CD='030203' and lang='en';
update nvserverinfo set ALTALK_RESEND_ERROR_CD = '0000,A1000' where HOST_NM = '1';

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '010', 'AA00',  '발송중(결과수신전)', '발송중(결과수신전)','y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 36, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AF0001', '010', 'AF00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AC0001', '010', 'AC00', '발송중(결과수신전)', '발송중(결과수신전)', 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ','') , 33, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AS0001', '010', 'AS00', '발송중(결과수신전)', '발송중(결과수신전)', 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AP0001','010','AP0001','발송중(결과수신전)','발송중(결과수신전)', 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ','') , null, 'ko');
