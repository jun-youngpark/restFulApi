INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA00', '-', '00', '알림톡 공통', '알림톡에서 사용하는 에러코드', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , null, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '0000', 'AA00', '정상코드', '성공', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 1, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1001', 'AA00', 'NoJsonBody', 'Request Body 가 Json 형식이 아님', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 2, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1002', 'AA00', 'InvalidHubPartnerKey', '허브 파트너 키가 유효하지 않음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 3, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1003', 'AA00', 'InvalidSenderKey', '발신 프로필 키가 유효하지 않음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 4, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1004', 'AA00', 'NoValueJsonElement', 'Request Body(Json)에서 name 을 찾을 수 없음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 5, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1005', 'AA00', 'SenderNotFound', '발신프로필을 찾을 수 없음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 6, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1006', 'AA00', 'DeleteSender', '삭제된 발신프로필. (메시지 사업 담당자에게 문의)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 7, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1007', 'AA00', 'StoppedSender', '차단 상태의 발신프로필. (메시지 사업 담당자에게 문의)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 8, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1011', 'AA00', 'ContractNotFound', '계약정보를 찾을 수 없음. (메시지 사업 담당자에게 문의)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1012', 'AA00', 'InvaldUserKeyException', '잘못된 형식의 유저키 요청', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1013', 'AA00', 'InvalidAppLink', '유효하지 않은 app연결', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1014', 'AA00', 'InvalidBizNum', '유효하지 않은 사업자번호', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1015', 'AA00', 'TalkUserIdNotFonud', '유효하지 않은 app user id 요청', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1016', 'AA00', 'BizNumNotEqual', '사업자등록번호 불일치', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1021', 'AA00', 'BlockedProfile', '차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1022', 'AA00', 'DeactivatedProfile', '닫힘 상태의 플러스친구 (플러스친구 운영툴에서 확인)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1023', 'AA00', 'DeletedProfile', '삭제된 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1024', 'AA00', 'DeletingProfile', '삭제대기 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1025', 'AA00', 'SpammedProfile', '메시지차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '1030', 'AA00', 'InvalidParameterException', '잘못된 파라메터 요청', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 12, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '2003', 'AA00', 'FailedToSendMessageByNoFriendshipException', '메시지 전송 실패(테스트 서버에서 친구관계가 아닌 경우)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 13, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '2004', 'AA00', 'FailedToMatchTemplateException', '템플릿 일치 확인시 오류 발생 (내부 오류 발생)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 14, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '2006', 'AA00', 'FailedToMatchSerialNumberPrefixPattern', '시리얼넘버 형식 불일치', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 14, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3000', 'AA00', 'UnexpectedException', '예기치 않은 오류 발생', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 15, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3005', 'AA00', 'AckTimeoutException', '메시지를 발송했으나 10초 이내에 고객 단말기로부터 ACK 수신을 받지못한 경우', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 16, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3006', 'AA00', 'FailedToSendMessageException', '내부 시스템 오류로 메시지 전송 실패', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 17, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3008', 'AA00', 'InvalidPhoneNumberException', '전화번호 오류', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 18, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3010', 'AA00', 'JsonParseException', 'Json 파싱 오류', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 19, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3011', 'AA00', 'MessageNotFoundException', '메시지가 존재하지 않음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 20, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3012', 'AA00', 'SerialNumberDuplicatedException', '메시지 일련번호가 중복됨', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 21, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3013', 'AA00', 'MessageEmptyException', '메시지가 비어 있음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 22, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3014', 'AA00', 'MessageLengthOverLimitException', '메시지 길이 제한 오류 (템플릿별 제한 길이 또는 1000자 초과)', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 23, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3015', 'AA00', 'TemplateNotFoundException', '템플릿을 찾을 수 없음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 24, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3016', 'AA00', 'NoMatchedTemplateException', '메시지 내용이 템플릿과 일치하지 않음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 25, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3018', 'AA00', 'NoSendAvaliableStatusException', '메시지를 전송할 수 없음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 26, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3025', 'AA00', 'ExceedMaxVariableLengthException', '변수 글자수 제한 초과', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 27, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3026', 'AA00', 'Button chat_extra(event)-InvalidExtra(EventName)Exception', '상담/봇 전환 버튼 extra, event 글자수 제한 초과', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 28,'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '3027', 'AA00', 'NoMatchedTemplateButtonException', '메시지 버튼이 템플릿과 일치하지 않음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 29, 'ko');

INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '4000', 'AA00', 'ResponseHistoryNotFoundException', '메시지 전송 결과를 찾을 수 없음', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 30, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '4001', 'AA00', 'UnknownMessageStatusError', '알 수 없는 메시지 상태', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 31, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '9998', 'AA00', 'DoNotServiceExcetpion', '시스템에 문제가 발생하여 담당자가 확인하고 있는 경우', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 32, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '9999', 'AA00', 'SystemErrorExcetpion', '시스템에 문제가 발생하여 담당자가 확인하고 있는 경우', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 33, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '910', 'AA00', 'data format', '유효하지 않은 형식의 값을 입력 함', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 34, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '999', 'AA00', 'TimeoutExcetpion', '카카오톡 서버로부터 정상적인 응답 혹은 발송 결과 응답을 받지 못한 경우', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 34, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '888', 'AA00', 'UnknownErrorException', '카카오톡 서버로부터 정상적인 응답 혹은 발송 결과 응답을 받지 못한 경우', 'y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 35, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '777', 'AA00',  'AlimtalkAgentError', 'Agent 점검필요','y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 36, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '900', 'AA00',  '핸들러 오류', '핸들러 오류','y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 36, 'ko');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_YN, REG_DTM, CD_ORD, LANG)
VALUES ('AA0001', '010', 'AA00',  '발송중(결과수신전)', '발송중(결과수신전)','y', CONVERT(VARCHAR(8), getdate(),112)+REPLACE((CONVERT(VARCHAR(8), getdate(),108)),':','') , 36, 'ko');
