-- 코드마스터 : SMS 오류사항 관련 공통코드
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS00', '-', '00', 'SMS 공통', 'SMS에서 사용하는 공통코드', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', '0', 'AS00', '성공', '성공', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', '1', 'AS00', 'TIMEOUT', 'TIMEOUT', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'A', 'AS00', '핸드폰 번호 처리중', '핸드폰 번호 처리중', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'B', 'AS00', '음영지역', '음영지역', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'C', 'AS00', 'power off', 'power off', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'D', 'AS00', '메시지 저장개수 초과', '메시지 저장개수 초과', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', '2', 'AS00', '잘못된 전화번호', '잘못된 전화번호', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'a', 'AS00', '일시 서비스 정지', '일시 서비스 정지', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'b', 'AS00', '기타 단말기 문제', '기타 단말기 문제', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'c', 'AS00', '착신 거절', '착신 거절', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'd', 'AS00', '기타', '기타', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'e', 'AS00', '이통사 SMC 형식오류', '이통사 SMC 형식오류', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'f', 'AS00', '중계사 자체 형식 오류', '중계사 자체 형식 오류', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'g', 'AS00', 'SMS 서비스 불가 단말기', 'SMS 서비스 불가 단말기', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'h', 'AS00', '핸드폰 번호 불가 상태', '핸드폰 번호 불가 상태', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'i', 'AS00', 'SMC 운영자가 메시지 삭제', 'SMC 운영자가 메시지 삭제', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'j', 'AS00', '이통사 내부 메시지 Que Full', '이통사 내부 메시지 Que Full', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'k', 'AS00', '이통사에서 spam 처리', '이통사에서 spam 처리', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'l', 'AS00', '중계사에서 spam 처리한건', 'www.nospan.go.kr에 등록된 번호에 대해 중계사에서 spam 처리한건', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'm', 'AS00', '중계사에서 Span 처리한건', 'SK 네크웍스에서 실패처리함', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'n', 'AS00', '건수제안', '건수제안 계약이 되어 있는경우 건수 제안', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'o', 'AS00', '메시지길이가 제안된 길이를 벗어남', '메시지길이가 제안된 길이를 벗어남', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'p', 'AS00', '폰 번호가 형식에 어긋남', '폰 번호가 형식에 어긋남', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'q', 'AS00', '필드형식이 잘못됨', 'ex)데이터 내용이 없는 경우', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'x', 'AS00', 'MMS 콘텐트의정보를 참조할수 없음', 'MMS 콘텐트의정보를 참조할수 없음', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'z', 'AS00', '처리되지 않은 기타 오류', '처리되지 않은 기타 오류', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', '900', 'AS00', '핸들러 오류', '핸들러 오류', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', 'v', 'AS00', '발신자 번호 미등록', '발신자 번호 미등록', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0001', '010', 'AS00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL, NULL, 'ko');