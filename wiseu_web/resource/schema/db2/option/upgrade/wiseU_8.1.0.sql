ALTER TABLE nvdefaulthandler ALTER COLUMN handle_attr SET DATA TYPE VARCHAR(10);
ALTER TABLE nvecaremultipartfile ADD file_type VARCHAR(10) DEFAUL '' NOT NULL;
ALTER TABLE nvecaremultipartfile ADD enc_yn VARCHAR(1) DEFAULT 'N' NOT NULL;
ALTER TABLE nvecaremultipartfile ADD secu_field VARCHAR(30);

-- 부가데이터 쿼리 테이블
CREATE TABLE nvaddquery (
    ecare_no               NUMERIC(15)         NOT NULL,
    query_seq              NUMERIC(2)          NOT NULL,
    query_type             VARCHAR(10)       NOT NULL,
    execute_type           VARCHAR(10)       NOT NULL,
    result_id                 VARCHAR(10)       NULL,
    dbinfo_seq              NUMERIC(2)          NOT NULL,
    query                     VARCHAR(4000)   NOT NULL
);
ALTER TABLE nvaddquery ADD CONSTRAINT pk_nvaddquery PRIMARY KEY (ecare_no,query_seq);
ALTER TABLE nvecaremsg add deploy_type VARCHAR(10);
ALTER TABLE nvecaremsg add mail_type VARCHAR(10) DEFAULT 'NONE';
ALTER TABLE nvecaremsg add open_type VARCHAR(10);
ALTER TABLE nvecaremsg add slot1_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot2_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot3_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot4_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot5_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot6_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot7_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot8_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot9_field VARCHAR(30);
ALTER TABLE nvecaremsg add slot10_field VARCHAR(30);

ALTER TABLE nvecaresendlog add slot3 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot4 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot5 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot6 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot7 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot8 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot9 VARCHAR(100);
ALTER TABLE nvecaresendlog add slot10 VARCHAR(100);

ALTER TABLE nvrealtimeaccept add slot3 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot4 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot5 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot6 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot7 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot8 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot9 VARCHAR(100);
ALTER TABLE nvrealtimeaccept add slot10 VARCHAR(100);

ALTER TABLE nvtestecaresendlog add slot3 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot4 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot5 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot6 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot7 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot8 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot9 VARCHAR(100);
ALTER TABLE nvtestecaresendlog add slot10 VARCHAR(100);

-- 알림톡 템플릿 관련 컬럼 추가
ALTER TABLE nvmobilecontents ADD kakao_tmpl_msg_type CHAR(2);
ALTER TABLE nvmobilecontents ADD kakao_tmpl_ad VARCHAR(240);
ALTER TABLE nvmobilecontents ADD kakao_tmpl_ex VARCHAR(1500);
ALTER TABLE nvmobilecontents ADD kakao_security_yn CHAR(1);
ALTER TABLE nvmobilecontents ADD kakao_em_type VARCHAR(5);
ALTER TABLE nvmobilecontents ADD kakao_em_title VARCHAR(150);
ALTER TABLE nvmobilecontents ADD kakao_em_subtitle VARCHAR(150);
ALTER TABLE nvmobilecontents ADD kakao_quick_replies VARCHAR(4000);
ALTER TABLE nvecaretemplate ADD conts_no NUMERIC(8);
ALTER TABLE nvecaretemplatehistory ADD conts_no NUMERIC(8);

-- CD_CAT, PAR_CD_CAT 컬럼에 저장하는 데이터를 의미있게 변경하기 위해 컬럼 크기 변경 10 -> 20
ALTER TABLE nv_cd_mst ALTER COLUMN cd_cat SET DATA TYPE VARCHAR(20);
ALTER TABLE nv_cd_mst ALTER COLUMN par_cd_cat SET DATA TYPE VARCHAR(20);


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
UPDATE nvmenu SET menu_link_url = '/segment/subSegment1Step.do' WHERE menu_cd = '0503';  -- /segment/segment_sub.do
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
-- 하위 대상자 메뉴 삭제
DELETE FROM NVMENU WHERE MENU_CD='0503';
DELETE FROM NVMENU_LANG WHERE MENU_CD='0503';
DELETE FROM NVMENUROLE WHERE MENU_CD='0503';