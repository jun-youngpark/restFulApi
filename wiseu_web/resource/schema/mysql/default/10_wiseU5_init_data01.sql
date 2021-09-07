-- 메뉴
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030106', '03', 1, '캠페인 월별 통계', '/report/campaign/monthlyRpt.do', NULL, 'Y', 'report', 5, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030107', '03', 1, '부서별 과금 통계', '/report/campaign/dept_stat.do', NULL, 'N', 'report', 6, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('05', NULL, 0, '대상자', '/segment/segmentList.do', NULL, 'Y', 'segment', 5, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('07', NULL, 0, '환경설정', '/env/env.do', NULL, 'Y', 'env', 7, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0401', '04', 1, '템플릿리스트', '/template/templateList.do', NULL, 'Y', 'template', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0701', '07', 1, '서버환경정보', '/env/env.do', NULL, 'Y', 'env', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0707', '07', 1, '기본핸들러설정', '/env/defaultHandlerList.do', NULL, 'Y', 'env', 7, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0703', '07', 1, '공휴일관리', '/env/blockDate.do', NULL, 'Y', 'env', 3, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('03', NULL, 0, '리포트', '/report/campaign/campaignRptList.do', NULL, 'Y', 'report', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('04', NULL, 0, '탬플릿', '/template/templateList.do', NULL, 'Y', 'template', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('06', NULL, 0, '사용자', '/account/accountDept.do', NULL, 'Y', 'account', 6, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0102', '01', 1, '캠페인 리스트', '/campaign/campaignList.do', NULL, 'Y', 'campaign', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0103', '01', 1, '캠페인 등록', '/campaign/campaign1Step.do', NULL, 'Y', 'campaign', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0104', '01', 1, '캠페인 고객이력', '/campaign/campaignSendLog.do', NULL, 'Y', 'campaign', 3, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0201', '02', 1, '이케어 리스트', '/ecare/ecareList.do', NULL, 'Y', 'ecare', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0202', '02', 1, '이케어 등록', '/ecare/ecare1Step.do', NULL, 'Y', 'ecare', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0203', '02', 1, '이케어 고객이력', '/ecare/ecareSendLog.do', NULL, 'Y', 'ecare', 3, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0204', '02', 1, '컨텐츠 변경이력', '/ecare/contsHistoryList.do', NULL, 'Y', 'ecare', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0205', '02', 1, '단건발송', '/ecare/onceSend.do', NULL, 'N', 'ecare', 5, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0402', '04', 1, '템플릿등록', '/template/templateReg.do', NULL, 'Y', 'template', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0702', '07', 1, 'DB관리', '/env/database.do', NULL, 'Y', 'env', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0704', '07', 1, '도메인관리', '/env/domain.do', NULL, 'Y', 'env', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0712', '07', 1, '개인정보관리', '/env/myInfo.do', NULL, 'Y', 'env', 6, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0705', '07', 1, '금칙어관리', '/env/badWord.do', NULL, 'Y', 'env', 5, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0710', '07', 1, '코드관리', '/env/smtpCode.do', NULL, 'Y', 'env', 8, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0708', '07', 1, '배치핸들러설정', '/env/schedulebatchlist.do', NULL, 'N', 'env', 9, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0501', '05', 1, '대상자조회', '/segment/segmentList.do', NULL, 'Y', 'segment', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0502', '05', 1, '대상자등록', '/segment/sqlSegment1Step.do', NULL, 'Y', 'segment', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0601', '06', 1, '사용자관리', '/account/account_management.do', NULL, 'N', 'account', 7, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030203', '03', 1, '이케어 월별 통계', '/report/ecare/monthlyRpt.do', NULL, 'Y', 'report', 12, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030204', '03', 1, '부서별 과금 통계', '/report/ecare/dept_stat.do', NULL, 'N', 'report', 13, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030201', '03', 1, '이케어 리스트', '/report/ecare/ecareRptList.do', NULL, 'Y', 'report', 10, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0403', '04', 1, '모바일템플릿리스트', '/template/mobileTemplateList.do', NULL, 'Y', 'template', 3, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0404', '04', 1, '모바일템플릿등록', '/template/mobileTemplateReg.do', NULL, 'Y', 'template', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0405', '04', 1, '알림톡 리스트', '/template/altTemplateList.do', NULL, 'Y', 'template', 5, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0406', '04', 1, '알림톡 등록', '/template/altTemplateReg.do', NULL, 'Y', 'template', 6, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('01', NULL, 0, '캠페인', '/campaign/campaignList.do', NULL, 'Y', 'campaign', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('02', NULL, 0, '이케어', '/ecare/ecareList.do', NULL, 'Y', 'ecare', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030101', '03', 1, '캠페인 리스트', '/report/campaign/campaignRptList.do', NULL, 'Y', 'report', 2, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('030105', '03', 1, '비교 분석', '/report/campaign/compareRpt.do', NULL, 'Y', 'report', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0504', '05', 1, '대상자파일올리기', '/segment/fileSegment1Step.do', NULL, 'Y', 'segment', 4, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0711', '07', 1, '메시지 발신자 정보', '/env/senderInfo.do', NULL, 'Y', 'env', 10, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0606', '06', 1, '부서 관리', '/account/accountDept.do', NULL, 'Y', 'account', 1, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0607', '06', 1, '사용자 관리', '/account/accountUser.do', NULL, 'Y', 'account', 3, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0608', '06', 1, '부서 권한 관리', '/account/accountDeptRole.do', NULL, 'Y', 'account', 2, 'M');
INSERT INTO nvmenu ( menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0714', '07', 1, '로그관리', '/env/logFileList.do', NULL, 'Y', 'env', 13, 'M');
INSERT INTO nvmenu ( menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0715', '07', 1, 'PUSH 설정','/env/pushSetUp.do', NULL, 'Y', 'env', 14, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0407', '04', 1, '브랜드톡 리스트', '/template/brtTemplateList.do', NULL, 'Y', 'template', 7, 'M');
INSERT INTO nvmenu (menu_cd, pmenu_cd, level_no, menu_nm, menu_link_url, menu_icon_img, active_yn, module_nm, sort_no, m_type)
VALUES ('0408', '04', 1, '브랜드톡 등록', '/template/brtTemplateReg.do', NULL, 'Y', 'template', 8, 'M');

-- TAG 초기값
INSERT INTO nvcamptag (tag_no,tag_nm) VALUES (1,'');
INSERT INTO nvecaremsgtag (tag_no,tag_nm) VALUES (1,'');
INSERT INTO nvsegmenttag (tag_no,tag_nm) VALUES (1,'');
INSERT INTO nvcontentstag (tag_no,tag_nm) VALUES (1,'');
INSERT INTO nvmobilecontentstag (tag_no,tag_nm) VALUES (1,'');

-- 금칙어검사 초기값
INSERT INTO nvbadword (bad_words, channel_type) VALUES ('','M');
INSERT INTO nvbadword (bad_words, channel_type) VALUES ('','S,T,P');

-- 준실시간 DB 인터페이스 처리를 위한 기본 값 등록
INSERT INTO nvecaremsg(ecare_no) VALUES (0);
INSERT INTO nvecmsghandler(ecare_no) VALUES (0);

-- 유효수신구간
INSERT INTO nvdurationinfo( durationinfo_cd, maxtime, mintime,valid_chk) VALUES ('01', 10, 10, 'Y');

-- 서버환경정보
INSERT INTO nvserverinfo (
    host_nm, port_no, driver_nm, driver_dsn, dbuser_id,
    dbpassword, openclick_path, survey_path, link_path, htmlmaker_path,
    openimage_path, duration_path, reject_path, smtp_ip, smtp_port,
    fula_ip, fula_port, ftp_yn, ftp_user_id, ftp_password,
    lastupdate_dt, editor_id, ret_domain, retry_cnt, b4_send_approve_yn,
    b4_send_verify_yn, b4_real_send_test_send_yn, ase_link_merge_param, ase_reject_merge_param, ase_open_scriptlet,
    groovy_link_merge_param, groovy_reject_merge_param, groovy_open_scriptlet, resend_include_returnmail_yn, resend_include_mail_key_yn,
    resend_error_cd, fax_resend_error_cd , sms_resend_error_cd , altalk_resend_error_cd , push_resend_error_cd ,
    spool_preserve_period, log_preserve_period, result_file_download_yn,
    sucs_result_file_download_yn, kakao_template_last_sync_dtm
) VALUES (
    '1', NULL, NULL, NULL, NULL,
    NULL, 'http://127.0.0.1:9000/servlet/ReceiveConfirmEM', '', 'http://127.0.0.1:9000/servlet/LinkTraceEM', '',
    NULL, NULL, 'http://127.0.0.1:9000/servlet/RejectEM', NULL, NULL,
    NULL, NULL, NULL, NULL, NULL,
    NULL, NULL, NULL, 1, 'N',
    'N', 'N', '?SEND_MODE=context.get("_SEND_MODE")&CAMPAIGN_NO=context.get("_CAMPAIGN_NO")&RESULT_SEQ=context.get("_RESULT_SEQ")&START_DATE=context.get("_START_DATE")&END_DATE=context.get("_END_DATE")&LIST_SEQ=context.get("_LIST_SEQ")&RECORD_SEQ=context.get("_RECORD_SEQ")&CUSTOMER_ID=context.getEncryptedString("_CUSTOMER_ID")', '?SEND_MODE=context.get("_SEND_MODE")&CAMPAIGN_NO=context.get("_CAMPAIGN_NO")&RESULT_SEQ=context.get("_RESULT_SEQ")&START_DATE=context.get("_START_DATE")&END_DATE=context.get("_END_DATE")&LIST_SEQ=context.get("_LIST_SEQ")&RECORD_SEQ=context.get("_RECORD_SEQ")&CUSTOMER_ID=context.getEncryptedString("_CUSTOMER_ID")', '?SEND_MODE=context.get("_SEND_MODE")&CAMPAIGN_NO=context.get("_CAMPAIGN_NO")&RESULT_SEQ=context.get("_RESULT_SEQ")&START_DATE=context.get("_START_DATE")&END_DATE=context.get("_END_DATE")&LIST_SEQ=context.get("_LIST_SEQ")&RECORD_SEQ=context.get("_RECORD_SEQ")&CUSTOMER_ID=context.getEncryptedString("_CUSTOMER_ID")',
    '?SEND_MODE=context.get("_SEND_MODE")&CAMPAIGN_NO=context.get("_CAMPAIGN_NO")&RESULT_SEQ=context.get("_RESULT_SEQ")&START_DATE=context.get("_START_DATE")&END_DATE=context.get("_END_DATE")&LIST_SEQ=context.get("_LIST_SEQ")&RECORD_SEQ=context.get("_RECORD_SEQ")&CUSTOMER_ID=context.getEncryptedString("_CUSTOMER_ID")', '?SEND_MODE=context.get("_SEND_MODE")&CAMPAIGN_NO=context.get("_CAMPAIGN_NO")&RESULT_SEQ=context.get("_RESULT_SEQ")&START_DATE=context.get("_START_DATE")&END_DATE=context.get("_END_DATE")&LIST_SEQ=context.get("_LIST_SEQ")&RECORD_SEQ=context.get("_RECORD_SEQ")&CUSTOMER_ID=context.getEncryptedString("_CUSTOMER_ID")&CUSTOMER_EMAIL=context.getEncryptedString("_CUSTOMER_EMAIL")', '?SEND_MODE=context.get("_SEND_MODE")&CAMPAIGN_NO=context.get("_CAMPAIGN_NO")&RESULT_SEQ=context.get("_RESULT_SEQ")&START_DATE=context.get("_START_DATE")&END_DATE=context.get("_END_DATE")&LIST_SEQ=context.get("_LIST_SEQ")&RECORD_SEQ=context.get("_RECORD_SEQ")&CUSTOMER_ID=context.getEncryptedString("_CUSTOMER_ID")', 'Y', 'N',
    '250,550,553,610,800', '250', '0,00000' , '0000,A1000', '250',
    '7', '7', 'Y', 'Y', ''
);

INSERT INTO copy_t (no, chr) VALUES (1, '1');
INSERT INTO copy_t (no, chr) VALUES (2, '2');
INSERT INTO copy_t (no, chr) VALUES (3, '3');
INSERT INTO copy_t (no, chr) VALUES (4, '4');
INSERT INTO copy_t (no, chr) VALUES (5, '5');
INSERT INTO copy_t (no, chr) VALUES (6, '6');
INSERT INTO copy_t (no, chr) VALUES (7, '7');
INSERT INTO copy_t (no, chr) VALUES (8, '8');
INSERT INTO copy_t (no, chr) VALUES (9, '9');
INSERT INTO copy_t (no, chr) VALUES (10, '10');
INSERT INTO copy_t (no, chr) VALUES (11, '11');
INSERT INTO copy_t (no, chr) VALUES (12, '12');
INSERT INTO copy_t (no, chr) VALUES (13, '13');
INSERT INTO copy_t (no, chr) VALUES (14, '14');
INSERT INTO copy_t (no, chr) VALUES (15, '15');
INSERT INTO copy_t (no, chr) VALUES (16, '16');
INSERT INTO copy_t (no, chr) VALUES (17, '17');
INSERT INTO copy_t (no, chr) VALUES (18, '18');
INSERT INTO copy_t (no, chr) VALUES (19, '19');
INSERT INTO copy_t (no, chr) VALUES (20, '20');
INSERT INTO copy_t (no, chr) VALUES (21, '21');
INSERT INTO copy_t (no, chr) VALUES (22, '22');
INSERT INTO copy_t (no, chr) VALUES (23, '23');
INSERT INTO copy_t (no, chr) VALUES (24, '24');
INSERT INTO copy_t (no, chr) VALUES (25, '25');
INSERT INTO copy_t (no, chr) VALUES (26, '26');
INSERT INTO copy_t (no, chr) VALUES (27, '27');
INSERT INTO copy_t (no, chr) VALUES (28, '28');
INSERT INTO copy_t (no, chr) VALUES (29, '29');
INSERT INTO copy_t (no, chr) VALUES (30, '30');
INSERT INTO copy_t (no, chr) VALUES (31, '31');
INSERT INTO copy_t (no, chr) VALUES (32, '32');
INSERT INTO copy_t (no, chr) VALUES (33, '33');
INSERT INTO copy_t (no, chr) VALUES (34, '34');
INSERT INTO copy_t (no, chr) VALUES (35, '35');
INSERT INTO copy_t (no, chr) VALUES (36, '36');
INSERT INTO copy_t (no, chr) VALUES (37, '37');
INSERT INTO copy_t (no, chr) VALUES (38, '38');
INSERT INTO copy_t (no, chr) VALUES (39, '39');
INSERT INTO copy_t (no, chr) VALUES (40, '40');
INSERT INTO copy_t (no, chr) VALUES (41, '41');
INSERT INTO copy_t (no, chr) VALUES (42, '42');
INSERT INTO copy_t (no, chr) VALUES (43, '43');
INSERT INTO copy_t (no, chr) VALUES (44, '44');
INSERT INTO copy_t (no, chr) VALUES (45, '45');
INSERT INTO copy_t (no, chr) VALUES (46, '46');
INSERT INTO copy_t (no, chr) VALUES (47, '47');
INSERT INTO copy_t (no, chr) VALUES (48, '48');
INSERT INTO copy_t (no, chr) VALUES (49, '49');
INSERT INTO copy_t (no, chr) VALUES (50, '50');
INSERT INTO copy_t (no, chr) VALUES (51, '51');
INSERT INTO copy_t (no, chr) VALUES (52, '52');
INSERT INTO copy_t (no, chr) VALUES (53, '53');
INSERT INTO copy_t (no, chr) VALUES (54, '54');
INSERT INTO copy_t (no, chr) VALUES (55, '55');
INSERT INTO copy_t (no, chr) VALUES (56, '56');
INSERT INTO copy_t (no, chr) VALUES (57, '57');
INSERT INTO copy_t (no, chr) VALUES (58, '58');
INSERT INTO copy_t (no, chr) VALUES (59, '59');
INSERT INTO copy_t (no, chr) VALUES (60, '60');
INSERT INTO copy_t (no, chr) VALUES (61, '61');
INSERT INTO copy_t (no, chr) VALUES (62, '62');
INSERT INTO copy_t (no, chr) VALUES (63, '63');
INSERT INTO copy_t (no, chr) VALUES (64, '64');
INSERT INTO copy_t (no, chr) VALUES (65, '65');
INSERT INTO copy_t (no, chr) VALUES (66, '66');
INSERT INTO copy_t (no, chr) VALUES (67, '67');
INSERT INTO copy_t (no, chr) VALUES (68, '68');
INSERT INTO copy_t (no, chr) VALUES (69, '69');
INSERT INTO copy_t (no, chr) VALUES (70, '70');
INSERT INTO copy_t (no, chr) VALUES (71, '71');
INSERT INTO copy_t (no, chr) VALUES (72, '72');
INSERT INTO copy_t (no, chr) VALUES (73, '73');
INSERT INTO copy_t (no, chr) VALUES (74, '74');
INSERT INTO copy_t (no, chr) VALUES (75, '75');
INSERT INTO copy_t (no, chr) VALUES (76, '76');
INSERT INTO copy_t (no, chr) VALUES (77, '77');
INSERT INTO copy_t (no, chr) VALUES (78, '78');
INSERT INTO copy_t (no, chr) VALUES (79, '79');
INSERT INTO copy_t (no, chr) VALUES (80, '80');
INSERT INTO copy_t (no, chr) VALUES (81, '81');
INSERT INTO copy_t (no, chr) VALUES (82, '82');
INSERT INTO copy_t (no, chr) VALUES (83, '83');
INSERT INTO copy_t (no, chr) VALUES (84, '84');
INSERT INTO copy_t (no, chr) VALUES (85, '85');
INSERT INTO copy_t (no, chr) VALUES (86, '86');
INSERT INTO copy_t (no, chr) VALUES (87, '87');
INSERT INTO copy_t (no, chr) VALUES (88, '88');
INSERT INTO copy_t (no, chr) VALUES (89, '89');
INSERT INTO copy_t (no, chr) VALUES (90, '90');
INSERT INTO copy_t (no, chr) VALUES (91, '91');
INSERT INTO copy_t (no, chr) VALUES (92, '92');
INSERT INTO copy_t (no, chr) VALUES (93, '93');
INSERT INTO copy_t (no, chr) VALUES (94, '94');
INSERT INTO copy_t (no, chr) VALUES (95, '95');
INSERT INTO copy_t (no, chr) VALUES (96, '96');
INSERT INTO copy_t (no, chr) VALUES (97, '97');
INSERT INTO copy_t (no, chr) VALUES (98, '98');
INSERT INTO copy_t (no, chr) VALUES (99, '99');
INSERT INTO copy_t (no, chr) VALUES (100, '100');