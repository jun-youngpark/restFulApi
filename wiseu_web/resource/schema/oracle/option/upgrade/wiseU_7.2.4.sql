
-- 알림톡 결과코드 문구 수정
UPDATE NV_CD_MST SET CD_DESC = '차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AA0001' AND CD = '1021';
UPDATE NV_CD_MST SET CD_DESC = '닫힘 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AA0001' AND CD = '1022';
UPDATE NV_CD_MST SET CD_DESC = '삭제된 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AA0001' AND CD = '1023';
UPDATE NV_CD_MST SET CD_DESC = '삭제대기 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AA0001' AND CD = '1024';
UPDATE NV_CD_MST SET CD_DESC = '메시지차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AA0001' AND CD = '1025';

-- 친구톡 결과코드 문구 수정
UPDATE NV_CD_MST SET CD_DESC = '차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AC0001' AND CD = '1021';
UPDATE NV_CD_MST SET CD_DESC = '닫힘 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AC0001' AND CD = '1022';
UPDATE NV_CD_MST SET CD_DESC = '삭제된 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AC0001' AND CD = '1023';
UPDATE NV_CD_MST SET CD_DESC = '삭제대기 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AC0001' AND CD = '1024';
UPDATE NV_CD_MST SET CD_DESC = '메시지차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)' WHERE CD_CAT = 'AC0001' AND CD = '1025';

-- 신규 알림톡 결과코드 추가
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1013', 'AA00', 'InvalidAppLink', '유효하지 않은 app연결', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1014', 'AA00', 'InvalidBizNum', '유효하지 않은 사업자번호', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1015', 'AA00', 'TalkUserIdNotFonud', '유효하지 않은 app user id 요청', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1016', 'AA00', 'BizNumNotEqual', '사업자등록번호 불일치', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '2006', 'AA00', 'FailedToMatchSerialNumberPrefixPattern', '시리얼넘버 형식 불일치', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 14, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3025', 'AA00', 'ExceedMaxVariableLengthException', '변수 글자수 제한 초과', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 27, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3026', 'AA00', 'Button chat_extra(event)-InvalidExtra(EventName)Exception', '상담/봇 전환 버튼 extra, event 글자수 제한 초과', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 28, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3027', 'AA00', 'NoMatchedTemplateButtonException', '메시지 버튼이 템플릿과 일치하지 않음', 'y', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') , 29, 'ko');