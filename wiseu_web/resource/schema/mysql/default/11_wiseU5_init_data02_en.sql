-- TODO 한글 하드코딩 제거
-- 메뉴다국어
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('01', 'en', 'Campaign');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0102', 'en', 'View campaign lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0103', 'en', 'Create campaign');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0104', 'en', 'Sending history');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('02', 'en', 'Ecare');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0201', 'en', 'View E-care lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0202', 'en', 'Create E-care');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0203', 'en', 'Sending history');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0204', 'en', 'Contents history');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('03', 'en', 'Report');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030101', 'en', 'Campaign lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030105', 'en', 'Result comparison');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030106', 'en', 'Campaign Monthly report');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030107', 'en', 'Departmental billing statistics');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030201', 'en', 'E-care lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030203', 'en', 'Ecare Monthly report');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030204', 'en', 'Departmental billing statistics');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0303', 'en', 'Customer History');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('030301', 'en', 'Customer Reference / resend');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('04', 'en', 'Template');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0401', 'en', 'Template');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0402', 'en', 'Create template');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0403', 'en', 'Mobile template lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0404', 'en', 'Create Mobile template');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('05', 'en', 'Segment');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0501', 'en', 'Segment lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0502', 'en', 'Create segments');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0504', 'en', 'Upload segment');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('06', 'en', 'User');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0601', 'en', 'Manage user');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0602', 'en', 'Manage authorization');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0603', 'en', 'Session Log');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0604', 'en', 'Admin Log');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0606', 'en', 'Dept Management');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0607', 'en', 'User Management');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0608', 'en', 'Dept permission');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('07', 'en', 'Configuration');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0701', 'en', 'Server properties');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0702', 'en', 'DB Management');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0703', 'en', 'Service off-day');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0704', 'en', 'Domain');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0705', 'en', 'Forbidden words');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0706', 'en', 'Deffered time');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0707', 'en', 'Default handler');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0708', 'en', 'Batch handler set-up');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0710', 'en', 'SmtpCode management');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0711', 'en', 'Sender information');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0712', 'en', 'User information');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0714', 'en', 'Log management');
INSERT INTO NVMENU_LANG ( MENU_CD, LANG, MENU_NM)
VALUES ('0715', 'en', 'PUSH Setting');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0407', 'en', 'Brandtalk Lists');
INSERT INTO nvmenu_lang (menu_cd, lang, menu_nm)
VALUES ('0408', 'en', 'Create Brandtalk');

-- 코드마스터
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('00', '-', '-', 'common code master', 'common code', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('1001', '-', '00', 'Campaign-related', 'Campaign-related code', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('10011', '-', '1001', 'Campaing Performing state', NULL, 'campaign_sts', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', '1', '10011', 'Error', 'select_schedule moment', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', '2', '10011', 'Error', 'make_target moment', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', '3', '10011', 'Error', 'mas_send moment', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'A', '10011', 'Approval', 'Approval', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'C', '10011', 'Approval requested', 'Approval requested', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'D', '10011', 'Approval rejected', 'Approval rejected', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'E', '10011', 'Executed', 'Executed', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'I', '10011', 'Incomplete', 'Incomplete', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'O', '10011', 'Send Error', 'Send Error', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'P', '10011', 'Pending', 'Pending', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'R', '10011', 'Scheduled', 'Scheduled', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'S', '10011', 'Holding', 'Holding', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'T', '10011', 'Stop sending', 'Stop sending', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'V', '10011', 'Divide Stop', 'Divide', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100111', 'W', '10011', 'In progress', 'In progress', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('10012', '-', '1001', 'eBasic Performing state', NULL, 'campaign_sts', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'A', '10012', 'Complete approval', 'Complete approval', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'C', '10012', 'Approval requested', 'Approval requested', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'D', '10012', 'Approval rejected', 'Approval rejected', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'E', '10012', 'Executed', 'Executed', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'I', '10012', 'Uncompleted', 'Uncompleted', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'R', '10012', 'Ready', 'Ready', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100121', 'O', '10012', 'Send Error', 'Send Error', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('10020', '-', '1001', '진행상태', NULL, 'result_sts', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'MS', '10020', 'MAS 작업시작', 'MAS 작업시작(3.0 서버 사용시)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'LW', '10020', 'In progress', '진행중(3.0 서버 사용시)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'SE', '10020', 'Executed', '종료(3.0 서버 사용시)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'XE', '10020', 'Executed', '발송완료', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'XO', '10020', 'Send Error', '에러', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'XR', '10020', 'ready', '준비중', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'XS', '10020', 'Holding', '일시정지', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'XT', '10020', 'Stop sending', '중지', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100201', 'XW', '10020', 'ready', '작업중', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('10022', '-', '1001', 'Sending Channel type', '발송 채널 타입', 'relation_type', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100221', 'L', '10022', 'Response', 'Response', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100221', 'N', '10022', '1st send', '1st send', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100221', 'R', '10022', 'Resend', 'Resend', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100221', 'S', '10022', 'Success', 'Success', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100221', 'F', '10022', 'Fail', 'Fail', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('100221', 'O', '10022', 'Open', 'Open', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('2001', '-', '00', 'Ecare-related', 'Ecare-related', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('20011', '-', '2001', 'Ecare Performing state', 'Ecare Performing state', 'ecare_sts', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200111', 'I', '20011', 'Uncompleted', 'Uncompleted', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200111', 'P', '20011', 'Stop sending', 'Stop sending', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200111', 'R', '20011', 'Executed', 'Executed', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200111', 'S', '20011', 'Holding', 'Holding', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200111', 'T', '20011', 'Service Check', 'Service Check', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('20013', '-', '2001', 'Ecare Service Type', 'Ecare Service Type', 'service_type', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200131', 'SN', '20013', 'deferred time', 'deferred time', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200131', 'SR', '20013', 'schedule(min)', 'schedule(min)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200131', 'SS', '20013', 'schedule', 'schedule', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('20014', '-', '2001', '이케어 스케쥴 서브 타입', '이케어 스케쥴 서브 타입', 'sub_type', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200141', 'N', '20014', 'deferred time', 'deferred time', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200141', 'R', '20014', 'schedule(min)', 'schedule(min)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('200141', 'S', '20014', 'schedule', 'schedule', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('4001', '-', '00', 'report', NULL, NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C000', '-', '00', '공통코드', '전체 프로그램에서 사용되는 공통코드', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C0001', '-', 'C000', 'Send err code[soft bounce]', '메일 에러코드 soft bounce', 'err_cd', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00001', '250', 'C0001', 'Send success', 'Send success', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '421', 'C0001', 'service is not available (system error in SMTP server of receiving side)', '서비스 활용 불가능(상대방 SMTP의 시스템 문제)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '450', 'C0001', 'mail in-box is locked', '우편함이 잠긴 상태', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '451', 'C0001', 'server error (system error in SMTP server of receiving side)', '서버에러 (상대방 SMTP의 시스템 문제)', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '452', 'C0001', 'short of storage space in the system', '시스템 저장 공간이 부족함', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '552', 'C0001', 'overweighed mail box', '메일 박스 용량 초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '554', 'C0001', 'failed to transaction', '트랜잭션이 실패함', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '600', 'C0001', 'host is busy, or the port requested doesn’t provide service', '호스트가 바쁘거나 요청된 포트에서 서비스가 제공되지 않음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '601', 'C0001', 'over-time for connection', '연결 시간 초과', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '602', 'C0001', 'Connection Failure', 'Connection Failure', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '603', 'C0001', 'Connection Error', 'Connection Error', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '604', 'C0001', 'Disconnection or IOException occurs in waiting', '응답을 기다리던 중 접속이 끊기거나 IOException이 발생', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '700', 'C0001', 'connection is broken as communication is ongoing', '통신 중 접속이 끊어짐', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '701', 'C0001', 'IOException', 'IOException', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '702', 'C0001', 'there is less than 3 in the length of response code', '응답 코드의 길이가 3이하', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '703', 'C0001', 'IO exception occurred when waiting for instruction transfer or response code', '명령어 전송 또는 응답 코드를 기다리는 중 IOException', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '704', 'C0001', 'SMTP Time out', 'SMTP Time out', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '888', 'C0001', 'other reasons', '기타 건수', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00011', '999', 'C0001', 'unsubscriber', '수신 거부된 사용자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C0002', '-', 'C000', 'Send err code [hard bounce]', '메일 에러코드 hard bounce', 'err_cd', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00021', '550', 'C0002', 'unknown user', '사용자 없음', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00021', '553', 'C0002', 'mail box is not valid or instruction is dropped.', '우편함 이름이 유효하지 않아서 명령어가 중단됨', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00021', '610', 'C0002', 'Unknown Host', 'Unknown Host', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00021', '800', 'C0002', 'Invalid Address', 'Invalid Address', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C0006', '-', 'C000', '사용자 권한', '사용자 권한', 'usertype_cd', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00061', 'A', 'C0006', 'ADMIN', '관리자', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00061', 'M', 'C0006', 'MANAGER', '매니저', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
INSERT INTO nv_cd_mst(cd_cat, cd, par_cd_cat, val, cd_desc, use_col, use_yn, reg_dtm, mod_dtm, cd_ord, lang)
VALUES ('C00061', 'U', 'C0006', 'USER', '유저', NULL, 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, NULL, 'en');
-- PUSH error code
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP00', '-', '00', 'PUSH 공통 결과코드', 'PUSH에서 사용하는 공통 결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
-- 코드별 카테고리
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0001', '-', 'AP00', '정상 결과코드', '정상  결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '-', 'AP00', '시스템 에러 결과코드', '시스템 에러 결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0003', '-', 'AP00', 'APNS 에러 결과코드', 'APNS 에러 결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0004', '-', 'AP00', 'HTTP 에러 결과코드', 'HTTP 에러 결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0005', '-', 'AP00', 'GCM 에러 결과코드', 'PUSH 에러 결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0006', '-', 'AP00', '기타 에러 결과코드', '기타 에러 결과코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
-- 엠앤와이즈 기본코드
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0001', '250', 'AP0001', '발송요청 성공', '발송요청 성공', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '550', 'AP0002', '수신자 정보 없음(앱미설치 등)', '수신자 정보 없음(앱미설치 등)', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '554', 'AP0002', '발송요청 실패', '발송요청 실패', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '700', 'AP0002', '통신실패', '통신실패', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '701', 'AP0002', 'IOException', 'IOException', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '702', 'AP0002', '응답 코드의 길이가 3 미만', '응답 코드의 길이가 3 미만', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0002', '703', 'AP0002', '명령어 전송 또는 응답 코드를 기다리는 중 IOException', '명령어 전송 또는 응답 코드를 기다리는 중 IOException', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0006', '704', 'AP0006', 'Time Out, Interrupted Exception', 'Time Out, Interrupted Exception', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0006', '800', 'AP0006', 'Invalid Address', 'Invalid Address', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0006', '888', 'AP0006', 'Etc', 'Etc', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');

-- 메일발송결과카테고리
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('01', '03', NULL, 0, 'SMTP', 'SMTP Category', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0101', '03', '01', 1, 'Unknown User', 'Unknown User Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0102', '03', '01', 1, 'Unknown Host', 'Unknown Host Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0103', '03', '01', 1, 'SMTP Exception', 'SMTP Exception Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0104', '03', '01', 1, 'NoRoute To Host', 'NoRoute To Host Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0105', '03', '01', 1, 'Connection Refused', 'Connection Refused Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0106', '03', '01', 1, 'Exception', 'Exception Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0107', '03', '01', 1, 'Invalid Address', 'Invalid Address Group', 'Y');
INSERT INTO nvsmtpcategory(category_cd, grp_cd, pcategory_cd, level_cd, category_nm, category_desc, active_yn)
VALUES('0108', '03', '01', 1, 'Receive Rejection', 'Receive Rejection', 'Y');

-- 메일발송결과코드
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('250', NULL, 'success', '01', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('421', NULL, 'service is not available (system error in SMTP server of receiving side)', '0106', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('450', NULL, 'mail in-box is locked', '0106', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('451', NULL, 'server error (system error in SMTP server of receiving side)', '0106', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('452', NULL, 'short of storage space in the system', '0106', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('550', NULL, 'unknown user', '0101', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('552', NULL, 'overweighed mail box', '0106', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('553', NULL, 'mail box is not valid or instruction is dropped.', '0101', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('554', NULL, 'failed to transaction', '0106', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('600', NULL, 'host is busy, or the port requested doesn’t provide service', '0105', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('601', NULL, 'over-time for connection', '0104', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('602', NULL, 'Connection Failure', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('610', NULL, 'Unknown Host', '0102', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('603', NULL, 'Connection Error', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('604', NULL, 'Disconnection or IOException occurs in waiting', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('700', NULL, 'connection is broken as communication is ongoing', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('701', NULL, 'IOExcepition', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('702', NULL, 'there is less than 3 in the length of response code', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('703', NULL, 'IO exception occurred when waiting for instruction transfer or response code', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('704', NULL, 'SMTP TIME OUT', '0103', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('800', NULL, 'Invalid Address', '0107', 'M');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('888', NULL, 'other reasons', '0106', 'M');
INSERT INTO NVSENDERR (ERROR_CD, ERROR_NM, ERROR_DESC, ERROR_SMS_DESC, CATEGORY_CD, CHANNEL_TYPE, SPAM_YN)
VALUES('900', NULL, 'handler error', NULL, '0106', 'M', 'N');
INSERT INTO nvsenderr(error_cd, error_nm, error_desc, category_cd, channel_type)
VALUES('999', NULL, 'unsubscriber', '0107', 'M');

-- 발송주기
INSERT INTO nvsendcycle(cycle_cd, cycle_nm) VALUES ('1', 'Daily');
INSERT INTO nvsendcycle(cycle_cd, cycle_nm) VALUES ('2', 'Weekly');
INSERT INTO nvsendcycle(cycle_cd, cycle_nm) VALUES ('3', 'Monthly');
INSERT INTO nvsendcycle(cycle_cd, cycle_nm) VALUES ('4', 'Once');
INSERT INTO nvsendcycle(cycle_cd, cycle_nm) VALUES ('5', 'Minute');

-- 사용자그룹[부서]
INSERT INTO nvusergrp (grp_cd, grp_nm, supragrp_cd, grp_desc, grp_level, permission, editor_id, lastupdate_dt, manager_id, active_yn, etc_info1, manager_nm)
VALUES('01', 'mnwise', NULL, 'Top Dept', 0, 'Y', 'admin', DATE_FORMAT(NOW(), '%Y%m%d'), 'manager', 'Y', NULL, 'manager');

-- 사용자
-- 관리자 (암호화 : vq2IfHWRcHE=, 복호화 : 1111)
INSERT INTO nvuser (user_id, pass_wd, grp_cd, name_kor, name_eng, tel_no, email, user_class, usertype_cd, lastupdate_dt, active_yn, accept_yn, user_role)
VALUES('admin', '1111', '01', 'administrator', 'Administrator', '02-3445-0922', 'admin@mnwise.com', 'manager', 'A', DATE_FORMAT(NOW(), '%Y%m%d'), 'Y', 'Y', 'A');
-- 최상위 부서장 등록
INSERT INTO nvuser (user_id, pass_wd, grp_cd, name_kor, name_eng, tel_no, email, user_class, usertype_cd, lastupdate_dt, active_yn, accept_yn)
VALUES('manager', '1111', '01', 'manager', 'Manager', '02-3445-0922', 'manager@mnwise.com', 'manager', 'M', DATE_FORMAT(NOW(), '%Y%m%d'), 'Y', 'Y');
 -- TEST유져 등록
INSERT INTO NVUSER (user_id, pass_wd, grp_cd, name_kor, name_eng, tel_no, email, user_class, usertype_cd, lastupdate_dt, active_yn, accept_yn)
VALUES('test', '1111', '01', 'tester', 'User', '02-3445-0922', 'test@mnwise.com', 'tester', 'U', DATE_FORMAT(NOW(), '%Y%m%d'), 'Y', 'Y');

-- 대상자DB접속정보
INSERT INTO nvdbinfo(dbinfo_seq, driver_nm) VALUES(1, 'Temporary DB info');

-- 이케어 준실시간 옴니채널용 기준 세그먼트
INSERT INTO nvsegment (segment_no, user_id, grp_cd, segment_nm, dbinfo_seq, segment_desc, sqlhead, sqltail, sqlbody, sqlfilter, lastupdate_dt, segment_size, crtgrp_cd, segment_type, filetodb_yn, editor_id, share_yn, active_yn, category_cd, segment_sts, tag_no, psegment_no, plink_seq, seg_type, testquery, updatequery)
VALUES(-1, 'admin', '01', 'DEFAULT TARGET', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 이케어 준실시간 옴니채널용 기준 시멘틱
INSERT INTO nvsemantic (segment_no, field_seq, field_nm, field_desc, field_type, field_length, initvalue, `minvalue`, `maxvalue`, null_yn, field_key)
VALUES(-1, 1, 'RECEIVER_ID', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'K');
INSERT INTO nvsemantic (segment_no, field_seq, field_nm, field_desc, field_type, field_length, initvalue, `minvalue`, `maxvalue`, null_yn, field_key)
VALUES(-1, 2, 'RECEIVER_NM', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'N');
INSERT INTO nvsemantic (segment_no, field_seq, field_nm, field_desc, field_type, field_length, initvalue, `minvalue`, `maxvalue`, null_yn, field_key)
VALUES(-1, 3, 'RECEIVER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'E');

INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP01', '-', '00', 'PUSH 공통 코드', 'PUSH에서 사용하는 공통 코드', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');
INSERT INTO nv_cd_mst (cd_cat, cd, par_cd_cat, val, cd_desc, use_yn, reg_dtm, cd_ord, lang)
VALUES ('AP0101', '-', 'AP01', 'PUSH 메시지 유형', 'PUSH 메시지 유형', 'y', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s') , null, 'en');