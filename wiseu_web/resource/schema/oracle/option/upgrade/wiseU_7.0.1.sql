-- wiseU 7.0.0 -> 7.0.1 업그레이드 시 사용

ALTER TABLE NVCAMPAIGN ADD HANDLER_SEQ NUMERIC(10);
ALTER TABLE NVECAREMSG ADD HANDLER_SEQ NUMERIC(10);

ALTER TABLE NVDEFAULTHANDLER ADD MSG_TYPE VARCHAR2(1);

-- SMS, MMS 는 수정모드로 강제 전환 또는 기본으로 설정. 문자핸들러는 기본이 수정이므로 수정상태로..
-- select * from nvecaremsg
UPDATE NVECAREMSG SET TEMPLATE_TYPE= '11'
WHERE CHANNEL_TYPE IN ('S','T') and template_type is not null;
UPDATE NVCAMPAIGN SET TEMPLATE_TYPE= '11'
WHERE CHANNEL_TYPE IN ('S','T') and template_type is not null;

-- 기본핸들러 MSG_TYPE 설정
UPDATE NVDEFAULTHANDLER SET
    MSG_TYPE = (CASE WHEN SERVICE_TYPE IS NULL THEN 'C'
                    WHEN SERVICE_TYPE='P' THEN 'P'
                    ELSE 'A' END)
WHERE MSG_TYPE ='' OR MSG_TYPE IS NULL;