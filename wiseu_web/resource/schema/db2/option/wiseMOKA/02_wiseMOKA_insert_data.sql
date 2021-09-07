INSERT INTO NVDEFAULTHANDLER ( SEQ, HANDLE_NM, HANDLE_DESC, SERVICE_TYPE, CHANNEL, HANDLE_TYPE, HANDLE_ATTR, USER_ID, CREATE_DT, CREATE_TM, HANDLER, AB_TEST_YN)
VALUES (1, 'ecare', 'ecare', 'S', 'S', 'G', 'D', 'admin', '', '', ' ', 'N');
INSERT INTO NVDEFAULTHANDLER ( SEQ, HANDLE_NM, HANDLE_DESC, SERVICE_TYPE, CHANNEL, HANDLE_TYPE, HANDLE_ATTR, USER_ID, CREATE_DT, CREATE_TM, HANDLER, AB_TEST_YN)
VALUES (2, 'ecare', 'ecare', 'S', 'T', 'G', 'D', 'admin', '', '', ' ', 'N');
INSERT INTO NVDEFAULTHANDLER ( SEQ, HANDLE_NM, HANDLE_DESC, SERVICE_TYPE, CHANNEL, HANDLE_TYPE, HANDLE_ATTR, USER_ID, CREATE_DT, CREATE_TM, HANDLER, AB_TEST_YN)
VALUES (3, 'ecare', 'ecare', 'S', 'A', 'G', 'D', 'admin', '', '', ' ', 'N');
INSERT INTO NVDEFAULTHANDLER ( SEQ, HANDLE_NM, HANDLE_DESC, SERVICE_TYPE, CHANNEL, HANDLE_TYPE, HANDLE_ATTR, USER_ID, CREATE_DT, CREATE_TM, HANDLER, AB_TEST_YN)
VALUES (4, 'ecare', 'ecare', 'S', 'C', 'G', 'D', 'admin', '', '', ' ', 'N');

INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (1, 'admin', '01', 'SMS 기간계', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (2, 'admin', '01', 'LMS/MMS 기간계', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (3, 'admin', '01', '알림톡 기간계', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (4, 'admin', '01', '친구톡 기간계', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (5, 'admin', '01', 'SMS 단건 발송', NULL, NULL, '20180419', '20180417', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (6, 'admin', '01', 'LMS/MMS 단건 발송', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (7, 'admin', '01', '알림톡 단건발송', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);
INSERT INTO NVECARESCENARIO
(SCENARIO_NO, USER_ID, GRP_CD, SCENARIO_NM, SCENARIO_DESC, SCENARIO_TYPE, CREATE_DT, LASTUPDATE_DT, FINISH_YN, FINISH_DT, CREATE_TM, LASTUPDATE_TM, SERVICE_TYPE, TAG_NO, SUB_TYPE, HANDLER_TYPE, CHRG_NM, BRC_NM)
VALUES (8, 'admin', '01', '친구톡 단건발송', NULL, NULL, '20180419', '20180419', 'N', NULL, '000000', '000000', 'S', 1, 'S', 'G', NULL, NULL);

INSERT INTO NVSEGMENT
(SEGMENT_NO, USER_ID, GRP_CD, SEGMENT_NM, DBINFO_SEQ, SEGMENT_DESC, SQLHEAD, SQLTAIL, SQLBODY, SQLFILTER, LASTUPDATE_DT, SEGMENT_SIZE, CRTGRP_CD, SEGMENT_TYPE, FILETODB_YN, EDITOR_ID, SHARE_YN, ACTIVE_YN, CATEGORY_CD, SEGMENT_STS, TAG_NO, PSEGMENT_NO, PLINK_SEQ, SEG_TYPE, TESTQUERY, UPDATEQUERY)
VALUES (1, 'admin', '01', 'Default', 1, NULL, 'SELECT A.CUSTOMER_ID,A.CUSTOMER_NM,A.CUSTOMER_TEL FROM NVFILEUPLOAD A ', NULL, '  WHERE TARGET_NO = 1', NULL, '20180419', 1, NULL, 'N', 'Y', NULL, 'Y', 'Y', NULL, NULL, 1, 0, NULL, 'F', NULL, NULL);

INSERT INTO NVSEMANTIC
(SEGMENT_NO, FIELD_SEQ, FIELD_NM, FIELD_DESC, FIELD_TYPE, FIELD_LENGTH, INITVALUE, MINVALUE, MAXVALUE, NULL_YN, FIELD_KEY)
VALUES (1, 1, 'CUSTOMER_ID', '아이디', NULL, NULL, NULL, NULL, NULL, 'N', 'K');
INSERT INTO NVSEMANTIC
(SEGMENT_NO, FIELD_SEQ, FIELD_NM, FIELD_DESC, FIELD_TYPE, FIELD_LENGTH, INITVALUE, MINVALUE, MAXVALUE, NULL_YN, FIELD_KEY)
VALUES (1, 2, 'CUSTOMER_NM', '이름', NULL, NULL, NULL, NULL, NULL, 'N', 'N');
INSERT INTO NVSEMANTIC
(SEGMENT_NO, FIELD_SEQ, FIELD_NM, FIELD_DESC, FIELD_TYPE, FIELD_LENGTH, INITVALUE, MINVALUE, MAXVALUE, NULL_YN, FIELD_KEY)
VALUES (1, 3, 'CUSTOMER_TEL', '고객휴대폰', NULL, NULL, NULL, NULL, NULL, 'N', 'S');

INSERT INTO NVFILEUPLOAD
(TARGET_NO, CUSTOMER_ID, CUSTOMER_NM, CUSTOMER_EMAIL, CUSTOMER_TEL, CUSTOMER_FAX, SLOT1, SLOT2, SLOT3, SLOT4, SLOT5, SLOT6, SLOT7, SLOT8, SLOT9, SLOT10, SLOT11, SLOT12, SLOT13, SLOT14, SLOT15, SLOT16, SLOT17, SLOT18, SLOT19, SLOT20, SLOT21, SLOT22, SLOT23, SLOT24, SLOT25, SLOT26, SLOT27, SLOT28, SLOT29, SLOT30, SLOT31, SLOT32, SLOT33, SLOT34, SLOT35, SLOT36, SLOT37, SLOT38, SLOT39, SLOT40, SLOT41, SLOT42, SLOT43, SLOT44, SLOT45, SLOT46, SLOT47, SLOT48, SLOT49, SLOT50, SEG, CALL_BACK, CUSTOMER_SLOT1, CUSTOMER_SLOT2, SENDER_NM, SENDER_EMAIL, RETMAIL_RECEIVER, SENTENCE)
VALUES (1, '1', 'bizmsg', NULL, '01000000000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (1, 'admin', '01', 1, 'SMS 기간계', NULL, NULL, 'R', NULL, NULL, '20180419', '000000', 'Y', 0, NULL, 1, NULL, NULL, NULL, NULL, 1, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'S', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 1, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, NULL, NULL, NULL, 0, 'N', NULL, 1, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (2, 'admin', '01', 1, 'LMS/MMS 기간계', NULL, 'LMS 발송 테스트', 'R', NULL, NULL, '20180419', '000000', 'Y', 3, NULL, 1, NULL, NULL, NULL, NULL, 2, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'T', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 2, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, NULL, NULL, NULL, 0, 'N', NULL, 1, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (3, 'admin', '01', 1, '알림톡 기간계', NULL, NULL, 'R', NULL, NULL, '20180419', '000000', 'Y', 0, NULL, 1, NULL, NULL, NULL, NULL, 3, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'A', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 3, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, '0156eb72408d4ba4c732c72a904d605219aa36a5', '0156eb72408d4ba4c732c72a904d605219aa36a5', 'mnwise_1801_0001', 0, 'Y', '실패시 우회발송', 1, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (4, 'admin', '01', 1, '친구톡 기간계', NULL, NULL, 'R', NULL, NULL, '20180419', '000000', 'Y', 3, NULL, 1, NULL, NULL, NULL, NULL, 4, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'C', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 4, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, NULL, '0156eb72408d4ba4c732c72a904d605219aa36a5', NULL, 0, 'N', NULL, 2, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (5, 'admin', '01', 1, 'SMS 단건 발송', NULL, NULL, 'R', NULL, NULL, '20180417', '000000', 'Y', 0, NULL, 1, NULL, NULL, NULL, NULL, 5, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'S', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 5, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, NULL, 0, NULL, NULL, NULL, 0, 'N', NULL, 1, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (6, 'admin', '01', 1, 'LMS/MMS 단건 발송', NULL, NULL, 'R', NULL, NULL, '20180419', '000000', 'Y', 3, NULL, 1, NULL, NULL, NULL, NULL, 6, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'T', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 6, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, NULL, NULL, NULL, 0, 'N', NULL, 1, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (7, 'admin', '01', 1, '알림톡 단건발송', NULL, NULL, 'R', NULL, NULL, '20180419', '000000', 'Y', 0, NULL, 1, NULL, NULL, NULL, NULL, 7, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'A', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 7, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, '0156eb72408d4ba4c732c72a904d605219aa36a5', '0156eb72408d4ba4c732c72a904d605219aa36a5', 'mnwise_1801_0001', 0, 'N', NULL, 1, 0, 1, 0);
INSERT INTO NVECAREMSG
(ECARE_NO, USER_ID, GRP_CD, SEGMENT_NO, ECARE_NM, ECARE_DESC, ECARE_PREFACE, ECARE_STS, CREATE_DT, CREATE_TM, LASTUPDATE_DT, LASTUPDATE_TM, CAMPAIGN_TYPE, TEMPLATE_TYPE, SENDING_STS, TARGET_CNT, SHARE_YN, MSGASSORT_CD, LOG_YN, KEEPDAY, ECMSCHEDULE_NO, SENDER_NM, SENDER_EMAIL, SENDING_MODE, RETRY_CNT, RECEIVER_NM, SENDER_TEL, RETMAIL_RECEIVER, ECARE_CLASS, RELATION_TYPE, CHANNEL_TYPE, HTMLMAKER_TYPE, SERVICE_TYPE, ACCOUNT_DT, ECARE_LEVEL, CATEGORY_CD, RESEND_YN, RESEND_CNT, RESEND_TM, SVC_ID, SUB_TYPE, SURVEY_END_YN, SURVEY_RESPONSE_CNT, SURVEY_NO, SURVEY_START_DT, SURVEY_START_TM, SURVEY_END_DT, SURVEY_END_TM, DEPTH_NO, EDITOR_ID, SCENARIO_NO, VERIFY_YN, VERIFY_GRP_CD, SEND_SERVER, BLOCK_YN, VERIFY_B4_SEND, DELETE_YN, SECURITY_MAIL_YN, REQ_DEPT_ID, REQ_USER_ID, RESEND_ECARE_NO, TEMPLATE_SENDER_KEY, KAKAO_SENDER_KEY, KAKAO_TMPL_CD, KAKAO_IMAGE_NO, FAILBACK_SEND_YN, FAILBACK_SUBJECT, TMPL_VER, COVER_VER, HANDLER_VER, PREFACE_VER)
VALUES (8, 'admin', '01', 1, '친구톡 단건발송', NULL, NULL, 'R', NULL, NULL, '20180419', '000000', 'Y', 3, NULL, 1, NULL, NULL, NULL, NULL, 8, 'mnwise', '', NULL, 1, NULL, '02-000-0000', '', 'A', NULL, 'C', NULL, 'S', NULL, '3', NULL, NULL, NULL, NULL, NULL, 'S', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'admin', 8, NULL, NULL, NULL, NULL, 'N', NULL, 'N', NULL, 'admin', 0, NULL, '0156eb72408d4ba4c732c72a904d605219aa36a5', NULL, 0, 'N', NULL, 2, 0, 1, 0);

INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (1, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (2, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (3, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (4, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (5, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (6, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (7, 'G', '11');
INSERT INTO NVECMSGHANDLER (ECARE_NO, TYPE, APPSOURCE) VALUES (8, 'G', '11');

INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (1, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (2, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (3, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (4, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (5, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (6, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (7, 'TRACE', NULL, NULL, NULL, NULL, '1');
INSERT INTO NVECARETRACEINFO (ECARE_NO, TRACE_TYPE, START_DT, START_TM, END_DT, END_TM, TERM_TYPE) VALUES (8, 'TRACE', NULL, NULL, NULL, NULL, '1');

INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (1, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (2, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (3, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (4, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (5, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (6, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (7, ' ', '');
INSERT INTO NVECARETEMPLATE (ECARE_NO, SEG, TEMPLATE) VALUES (8, ' ', '');