ALTER TABLE nvdefaulthandler MODIFY handle_attr VARCHAR2(10);
ALTER TABLE nvecaremultipartfile ADD file_type VARCHAR2(10) DEFAULT '' NOT NULL;
ALTER TABLE nvecaremultipartfile ADD enc_yn VARCHAR2(1) DEFAULT 'N' NOT NULL;
ALTER TABLE nvecaremultipartfile ADD secu_field VARCHAR2(30);

-- 부가데이터 쿼리 테이블
CREATE TABLE nvaddquery (
    ecare_no               NUMERIC(15)         NOT NULL,
    query_seq              NUMERIC(2)          NOT NULL,
    query_type             VARCHAR2(10)       NOT NULL,
    execute_type           VARCHAR2(10)       NOT NULL,
    result_id                 VARCHAR2(10)       NULL,
    dbinfo_seq              NUMERIC(2)          NOT NULL,
    query                     VARCHAR2(4000)   NOT NULL
);
ALTER TABLE nvaddquery ADD CONSTRAINT pk_nvaddquery PRIMARY KEY (ecare_no,query_seq);
ALTER TABLE nvecaremsg add deploy_type VARCHAR2(10);
ALTER TABLE nvecaremsg add mail_type VARCHAR2(10) DEFAULT 'NONE';
ALTER TABLE nvecaremsg add open_type VARCHAR2(10);
ALTER TABLE nvecaremsg add slot1_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot2_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot3_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot4_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot5_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot6_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot7_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot8_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot9_field VARCHAR2(30);
ALTER TABLE nvecaremsg add slot10_field VARCHAR2(30);

ALTER TABLE nvecaresendlog add slot3 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot4 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot5 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot6 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot7 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot8 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot9 VARCHAR2(100);
ALTER TABLE nvecaresendlog add slot10 VARCHAR2(100);

ALTER TABLE nvrealtimeaccept add slot3 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot4 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot5 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot6 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot7 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot8 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot9 VARCHAR2(100);
ALTER TABLE nvrealtimeaccept add slot10 VARCHAR2(100);

ALTER TABLE nvtestecaresendlog add slot3 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot4 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot5 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot6 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot7 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot8 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot9 VARCHAR2(100);
ALTER TABLE nvtestecaresendlog add slot10 VARCHAR2(100);

-- 코멘트

COMMENT ON COLUMN nvdefaulthandler.handle_attr IS '핸들러 유형';
COMMENT ON COLUMN nvecaremultipartfile.file_type IS '첨부 파일 유형';
COMMENT ON COLUMN nvecaremultipartfile.enc_yn IS '첨부 파일 암호화 여부';
COMMENT ON COLUMN nvecaremultipartfile.secu_field IS '비밀번호 필드';

COMMENT ON COLUMN nvaddquery.ecare_no IS '이케어 번호';
COMMENT ON COLUMN nvaddquery.query_seq IS '쿼리 순번';
COMMENT ON COLUMN nvaddquery.query_type IS '쿼리유형';
COMMENT ON COLUMN nvaddquery.execute_type IS '실행 유형';
COMMENT ON COLUMN nvaddquery.result_id IS '결과 ID';
COMMENT ON COLUMN nvaddquery.dbinfo_seq IS 'DB정보순번';
COMMENT ON COLUMN nvaddquery.query IS '쿼리';

COMMENT ON COLUMN nvecaremsg.slot1_field IS '이력테이블 부가데이터1';
COMMENT ON COLUMN nvecaremsg.slot2_field IS '이력테이블 부가데이터2';
COMMENT ON COLUMN nvecaremsg.slot3_field IS '이력테이블 부가데이터3';
COMMENT ON COLUMN nvecaremsg.slot4_field IS '이력테이블 부가데이터4';
COMMENT ON COLUMN nvecaremsg.slot5_field IS '이력테이블 부가데이터5';
COMMENT ON COLUMN nvecaremsg.slot6_field IS '이력테이블 부가데이터6';
COMMENT ON COLUMN nvecaremsg.slot7_field IS '이력테이블 부가데이터7';
COMMENT ON COLUMN nvecaremsg.slot8_field IS '이력테이블 부가데이터8';
COMMENT ON COLUMN nvecaremsg.slot9_field IS '이력테이블 부가데이터9';
COMMENT ON COLUMN nvecaremsg.slot10_field IS '이력테이블 부가데이터10';

COMMENT ON COLUMN nvecaresendlog.SLOT3 IS '부가정보3';
COMMENT ON COLUMN nvecaresendlog.SLOT4 IS '부가정보4';
COMMENT ON COLUMN nvecaresendlog.SLOT5 IS '부가정보5';
COMMENT ON COLUMN nvecaresendlog.SLOT6 IS '부가정보6';
COMMENT ON COLUMN nvecaresendlog.SLOT7 IS '부가정보7';
COMMENT ON COLUMN nvecaresendlog.SLOT8 IS '부가정보8';
COMMENT ON COLUMN nvecaresendlog.SLOT9 IS '부가정보9';
COMMENT ON COLUMN nvecaresendlog.SLOT10 IS '부가정보10';

COMMENT ON COLUMN nvrealtimeaccept.SLOT3 IS '부가정보3';
COMMENT ON COLUMN nvrealtimeaccept.SLOT4 IS '부가정보4';
COMMENT ON COLUMN nvrealtimeaccept.SLOT5 IS '부가정보5';
COMMENT ON COLUMN nvrealtimeaccept.SLOT6 IS '부가정보6';
COMMENT ON COLUMN nvrealtimeaccept.SLOT7 IS '부가정보7';
COMMENT ON COLUMN nvrealtimeaccept.SLOT8 IS '부가정보8';
COMMENT ON COLUMN nvrealtimeaccept.SLOT9 IS '부가정보9';
COMMENT ON COLUMN nvrealtimeaccept.SLOT10 IS '부가정보10';

COMMENT ON COLUMN nvtestecaresendlog.SLOT3  IS '부가정보3';
COMMENT ON COLUMN nvtestecaresendlog.SLOT4  IS '부가정보4';
COMMENT ON COLUMN nvtestecaresendlog.SLOT5  IS '부가정보5';
COMMENT ON COLUMN nvtestecaresendlog.SLOT6  IS '부가정보6';
COMMENT ON COLUMN nvtestecaresendlog.SLOT7  IS '부가정보7';
COMMENT ON COLUMN nvtestecaresendlog.SLOT8  IS '부가정보8';
COMMENT ON COLUMN nvtestecaresendlog.SLOT9  IS '부가정보9';
COMMENT ON COLUMN nvtestecaresendlog.SLOT10 IS '부가정보10';

-- 알림톡 템플릿 관련 컬럼 추가
ALTER TABLE nvmobilecontents ADD kakao_tmpl_msg_type CHAR(2);
ALTER TABLE nvmobilecontents ADD kakao_tmpl_ad VARCHAR2(240);
ALTER TABLE nvmobilecontents ADD kakao_tmpl_ex VARCHAR2(1500);
ALTER TABLE nvmobilecontents ADD kakao_security_yn CHAR(1);
ALTER TABLE nvmobilecontents ADD kakao_em_type VARCHAR2(5);
ALTER TABLE nvmobilecontents ADD kakao_em_title VARCHAR2(150);
ALTER TABLE nvmobilecontents ADD kakao_em_subtitle VARCHAR2(150);
ALTER TABLE nvmobilecontents ADD kakao_quick_replies VARCHAR2(4000);
ALTER TABLE nvecaretemplate ADD conts_no NUMERIC(8);
ALTER TABLE nvecaretemplatehistory ADD conts_no NUMERIC(8);

-- CD_CAT, PAR_CD_CAT 컬럼에 저장하는 데이터를 의미있게 변경하기 위해 컬럼 크기 변경 10 -> 20
ALTER TABLE nv_cd_mst MODIFY cd_cat VARCHAR2(20);
ALTER TABLE nv_cd_mst MODIFY par_cd_cat VARCHAR2(20);

UPDATE nvmenu SET menu_link_url = '/campaign/campaignList.do' WHERE menu_cd = '01';  -- /campaign/campaign.do
UPDATE nvmenu SET menu_link_url = '/campaign/campaignList.do' WHERE menu_cd = '0102';  -- /campaign/campaign.do
UPDATE nvmenu SET menu_link_url = '/campaign/campaign1Step.do' WHERE menu_cd = '0103';  -- /campaign/campaign_step_form.do
UPDATE nvmenu SET menu_link_url = '/campaign/campaignSendLog.do' WHERE menu_cd = '0104';  -- /campaign/campaign_per_history.do
UPDATE nvmenu SET menu_link_url = '/ecare/ecareList.do' WHERE menu_cd = '02';  -- /ecare/ecare.do
UPDATE nvmenu SET menu_link_url = '/ecare/ecareList.do' WHERE menu_cd = '0201';  -- /ecare/ecare.do
UPDATE nvmenu SET menu_link_url = '/ecare/ecare1Step.do' WHERE menu_cd = '0202';  -- /ecare/ecare_step_form.do
UPDATE nvmenu SET menu_link_url = '/ecare/ecareSendLog.do' WHERE menu_cd = '0203';  -- /ecare/ecare_per_history.do
UPDATE nvmenu SET menu_link_url = '/ecare/contsHistoryList.do' WHERE menu_cd = '0204';  -- /ecare/ecare_conts_history.do
UPDATE nvmenu SET menu_link_url = '/ecare/onceSend.do' WHERE menu_cd = '0205';  -- /ecare/send_once.do
UPDATE nvmenu SET menu_link_url = '/report/campaign/campaignRptList.do' WHERE menu_cd = '03';  -- /report/campaign/campaign_list.do
UPDATE nvmenu SET menu_link_url = '/report/campaign/campaignRptList.do' WHERE menu_cd = '030101';  -- /report/campaign/campaign_list.do
UPDATE nvmenu SET menu_link_url = '/report/campaign/compareRpt.do' WHERE menu_cd = '030105';  -- /report/campaign/compare_with.do
UPDATE nvmenu SET menu_link_url = '/report/campaign/monthlyRpt.do' WHERE menu_cd = '030106';  -- /report/campaign/monthly_stat.do
UPDATE nvmenu SET menu_link_url = '/report/ecare/ecareRptList.do' WHERE menu_cd = '030201';  -- /report/ecare/ecare_list.do
UPDATE nvmenu SET menu_link_url = '/report/ecare/monthlyRpt.do' WHERE menu_cd = '030203';  -- /report/ecare/monthly_stat.do
UPDATE nvmenu SET menu_link_url = '/template/templateList.do' WHERE menu_cd = '04';  -- /template/template.do
UPDATE nvmenu SET menu_link_url = '/template/templateList.do' WHERE menu_cd = '0401';  -- /template/template.do
UPDATE nvmenu SET menu_link_url = '/template/mobileTemplateList.do' WHERE menu_cd = '0403';  -- /template/mobileTemplate.do
UPDATE nvmenu SET menu_link_url = '/template/altTemplateList.do' WHERE menu_cd = '0405';  -- /template/alimtalkTemplate.do
UPDATE nvmenu SET menu_link_url = '/template/altTemplateReg.do' WHERE menu_cd = '0406';  -- /template/alimtalkTemplateReg.do
UPDATE nvmenu SET menu_link_url = '/template/brtTemplateList.do' WHERE menu_cd = '0407';  -- /template/brandtalkTemplateList.do
UPDATE nvmenu SET menu_link_url = '/template/brtTemplateReg.do' WHERE menu_cd = '0408';  -- /template/brandtalkTemplateReg.do
UPDATE nvmenu SET menu_link_url = '/segment/segmentList.do' WHERE menu_cd = '05';  -- /segment/segment.do
UPDATE nvmenu SET menu_link_url = '/segment/segmentList.do' WHERE menu_cd = '0501';  -- /segment/segment.do
UPDATE nvmenu SET menu_link_url = '/segment/sqlSegment1Step.do' WHERE menu_cd = '0502';  -- /segment/sqlSegment1step.do
UPDATE nvmenu SET menu_link_url = '/segment/fileSegment1Step.do' WHERE menu_cd = '0504';  -- /segment/upload/upload.do
UPDATE nvmenu SET menu_link_url = '/account/accountDept.do' WHERE menu_cd = '06';  -- /account/account_dept.do
UPDATE nvmenu SET menu_link_url = '/account/accountDept.do' WHERE menu_cd = '0606';  -- /account/account_dept.do
UPDATE nvmenu SET menu_link_url = '/account/accountUser.do' WHERE menu_cd = '0607';  -- /account/account_user.do
UPDATE nvmenu SET menu_link_url = '/account/accountDeptRole.do' WHERE menu_cd = '0608';  -- /account/account_dept_permission.do
UPDATE nvmenu SET menu_link_url = '/env/blockDate.do' WHERE menu_cd = '0703';  -- /env/blockdate.do
UPDATE nvmenu SET menu_link_url = '/env/defaultHandlerList.do' WHERE menu_cd = '0707';  -- /env/deafulthandler.do
UPDATE nvmenu SET menu_link_url = '/env/smtpCode.do' WHERE menu_cd = '0710';  -- /env/smtpcode.do
UPDATE nvmenu SET menu_link_url = '/env/senderInfo.do' WHERE menu_cd = '0711';  -- /env/senderinfo.do
UPDATE nvmenu SET menu_link_url = '/env/myInfo.do' WHERE menu_cd = '0712';  -- /env/env_myinfo.do
UPDATE nvmenu SET menu_link_url = '/env/logFileList.do' WHERE menu_cd = '0714';  -- /env/env_logfiledown.do
UPDATE nvmenu SET menu_link_url = '/env/pushSetUp.do' WHERE menu_cd = '0715';  -- /env/env_push_setting.do

--하위 대상자 메뉴 삭제
DELETE FROM NVMENU WHERE MENU_CD='0503';
DELETE FROM NVMENU_LANG WHERE MENU_CD='0503';
DELETE FROM NVMENUROLE WHERE MENU_CD='0503';