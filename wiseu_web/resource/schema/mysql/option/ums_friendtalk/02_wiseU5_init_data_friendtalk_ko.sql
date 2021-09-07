INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC00', '-', '00', '친구톡 공통', '친구톡에서 사용하는 에러코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '0000', 'AC00', '정상코드', '성공', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 1, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1001', 'AC00', 'NoJsonBody', 'Request Body 가 Json 형식이 아님', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 2, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1002', 'AC00', 'InvalidHubPartnerKey', '허브 파트너 키가 유효하지 않음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 3, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1003', 'AC00', 'InvalidSenderKey', '발신 프로필 키가 유효하지 않음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 4, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1004', 'AC00', 'NoValueJsonElement', 'Request Body(Json)에서 name 을 찾을 수 없음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 5, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1005', 'AC00', 'SenderNotFound', '발신프로필을 찾을 수 없음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 6, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1006', 'AC00', 'DeleteSender', '삭제된 발신프로필. (메시지 사업 담당자에게 문의)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 7, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1007', 'AC00', 'StoppedSender', '차단 상태의 발신프로필. (메시지 사업 담당자에게 문의)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 8, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1011', 'AC00', 'ContractNotFound', '계약정보를 찾을 수 없음. (메시지 사업 담당자에게 문의)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 9, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1012', 'AC00', 'InvalidUserKeyException', '잘못된 형식의 유저키 요청', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 10, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1021', 'AC00', 'BlockedProfile', '차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 11, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1022', 'AC00', 'DeactivatedProfile', '닫힘 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 12, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1023', 'AC00', 'DeletedProfile', '삭제된 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 13, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1024', 'AC00', 'DeletingProfile', '삭제대기 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 14, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1025', 'AC00', 'SpammedProfile', '메시지차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 15, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '1030', 'AC00', 'InvalidParameterException', '잘못된 파라메터 요청', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 16, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '2003', 'AC00', 'FailedToSendMessageByNoFriendshipException', '메시지 전송 실패(테스트 서버에서 친구관계가 아닌 경우)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 17, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '2006', 'AC00', 'FailedToMatchSerialNumberPrefixPattern', '시리얼넘버 형식 불일치','y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 18, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3000', 'AC00', 'UnexpectedException', '예기치 않은 오류 발생', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 19, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3006', 'AC00', 'FailedToSendMessageException', '내부 시스템 오류로 메시지 전송 실패', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 21, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3008', 'AC00', 'InvalidPhoneNumberException', '전화번호 오류', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 22, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3010', 'AC00', 'JsonParseException', 'Json 파싱 오류', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 23, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3011', 'AC00', 'MessageNotFoundException', '메시지가 존재하지 않음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 24, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3012', 'AC00', 'SerialNumberDuplicatedException', '메시지 일련번호가 중복됨', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 25, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3013', 'AC00', 'MessageEmptyException', '메시지가 비어 있음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 26, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3014', 'AC00', 'MessageLengthOverLimitException', '메시지 길이 제한 오류 (메시지 제한 길이 또는 1000 자 초과)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 27, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3018', 'AC00', 'NoSendAvaliableStatusException', '메시지를 전송할 수 없는 상태', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 30, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3019', 'AC00', 'MessageNotSentException', '메시지가 발송되지 않은 상태','y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 30, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3020', 'AC00', 'SeenInfoNotFoundException', '메시지 확인 정보를 찾을 수 없음','y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 30, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3022', 'AC00', 'NoSendAvailableTimeException', '메시지 발송 가능한 시간이 아님 (친구톡 / 마케팅 메시지는 08시부터 20시까지 발송 가능)','y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 30, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '3024', 'AC00', 'MessageInvalidImageException', '메시지에 포함된 이미지를 전송할 수 없음','y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 30, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '4000', 'AC00', 'ResponseHistoryNotFoundException', '메시지 전송 결과를 찾을 수 없음', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 31, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '4001', 'AC00', 'UnknownMessageStatusError', '알 수 없는 메시지 상태', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 32, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '888', 'AC00', 'UnknownErrorException', '알 수 없는 에러 발생', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 34, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '910', 'AC00', 'invalid data format', '유효하지 않은 형식의 값을 입력 함', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 35, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '999', 'AC00', 'TimeoutExcetpion', 'Timeout ', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 36, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '9998', 'AC00', '현재 서비스를 제공하고 있지 않습니다.', '시스템에 문제가 발생하여 담당자가 확인하고 있는 경우','y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 33, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AC0001', '9999', 'AC00', 'SystemErrorExcetpion', '시스템 에러 발생', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , 33, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AC0001', '900', 'AC00', '핸들러 오류', '핸들러 오류', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('AC0001', '010', 'AC00', '발송중(결과수신전)', '발송중(결과수신전)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'ko');