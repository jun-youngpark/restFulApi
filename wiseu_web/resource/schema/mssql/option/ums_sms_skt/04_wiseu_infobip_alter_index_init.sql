-- 인포빕 연동 컬럼 추가 및 변경
ALTER TABLE EM_TRAN    ALTER COLUMN TRAN_ID   VARCHAR(50);
ALTER TABLE EM_TRAN    ALTER COLUMN TRAN_RSLT VARCHAR(5);
ALTER TABLE EM_TRAN    ADD TRAN_STATUS2   VARCHAR(4) DEFAULT NULL;
ALTER TABLE EM_TRAN    ADD TRAN_MCC VARCHAR(3) DEFAULT NULL;
ALTER TABLE EM_TRAN    ADD TRAN_MNC VARCHAR(3) DEFAULT NULL;
ALTER TABLE EM_TRAN    ADD MESSAGE_ID VARCHAR(100) DEFAULT NULL;
ALTER TABLE EM_TRAN    ADD TRAN_SN NUMERIC;
ALTER TABLE EM_TRAN    ADD AGENT_ID VARCHAR(20);
ALTER TABLE EM_TRAN    ADD INFOBIP_ID VARCHAR(50);

ALTER TABLE EM_LOG    ALTER COLUMN TRAN_ID   VARCHAR(50);
ALTER TABLE EM_LOG    ALTER COLUMN TRAN_RSLT VARCHAR(5);
ALTER TABLE EM_LOG    ADD TRAN_STATUS2   VARCHAR(4) DEFAULT NULL;
ALTER TABLE EM_LOG    ADD TRAN_MCC VARCHAR(3) DEFAULT NULL;
ALTER TABLE EM_LOG    ADD TRAN_MNC VARCHAR(3) DEFAULT NULL;
ALTER TABLE EM_LOG    ADD MESSAGE_ID VARCHAR(100) DEFAULT NULL;
ALTER TABLE EM_LOG    ADD TRAN_SN NUMERIC;
ALTER TABLE EM_LOG    ADD AGENT_ID VARCHAR(20);
ALTER TABLE EM_LOG    ADD INFOBIP_ID VARCHAR(50);

-- 인포빕 연동 인덱스 추가
-- DROP INDEX IDX_EM_TRAN_01 ON EM_TRAN;
-- CREATE INDEX IDX_EM_TRAN_01 ON EM_TRAN (TRAN_STATUS, TRAN_DATE);
CREATE INDEX IDX_EM_TRAN_02 ON EM_TRAN (TRAN_SN, TRAN_STATUS, AGENT_ID);
CREATE INDEX IDX_EM_TRAN_03 ON EM_TRAN (INFOBIP_ID, TRAN_DATE);

-- DROP INDEX IDX_EM_LOG_01 ON EM_LOG;
CREATE INDEX IDX_EM_LOG_01 ON EM_LOG (TRAN_STATUS, TRAN_DATE);
CREATE INDEX IDX_EM_LOG_02 ON EM_LOG (TRAN_SN,TRAN_STATUS,AGENT_ID);
CREATE INDEX IDX_EM_LOG_03 ON EM_LOG (INFOBIP_ID,AGENT_ID);

-- 인포빕 코드 정보
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '00000', 'AS00', 'NO_ERROR', 'NO_ERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10001', 'AS00', 'EC_UNKNOWN_SUBSCRIBER', 'EC_UNKNOWN_SUBSCRIBER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10005', 'AS00', 'EC_UNIDENTIFIED_SUBSCRIBER', 'EC_UNIDENTIFIED_SUBSCRIBER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10006', 'AS00', 'EC_ABSENT_SUBSCRIBER_SM', 'EC_ABSENT_SUBSCRIBER_SM', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10009', 'AS00', 'EC_ILLEGAL_SUBSCRIBER', 'EC_ILLEGAL_SUBSCRIBER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '30010', 'AS00', 'EC_BEARER_SERVICE_NOT_PROVISIONED', 'EC_BEARER_SERVICE_NOT_PROVISIONED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10011', 'AS00', 'EC_TELESERVICE_NOT_PROVISIONED', 'EC_TELESERVICE_NOT_PROVISIONED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10012', 'AS00', 'EC_ILLEGAL_EQUIPMENT','EC_ILLEGAL_EQUIPMENT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10013', 'AS00', 'EC_CALL_BARRED','EC_CALL_BARRED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '30020', 'AS00', 'EC_SS_INCOMPATIBILITY','EC_SS_INCOMPATIBILITY', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10021', 'AS00', 'EC_FACILITY_NOT_SUPPORTED','EC_FACILITY_NOT_SUPPORTED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10031', 'AS00', 'EC_SUBSCRIBER_BUSY_FOR_MT_SMS','EC_SUBSCRIBER_BUSY_FOR_MT_SMS', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10032', 'AS00', 'EC_SM_DELIVERY_FAILURE','EC_SM_DELIVERY_FAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10033', 'AS00', 'EC_MESSAGE_WAITING_LIST_FULL','EC_MESSAGE_WAITING_LIST_FULL', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10034', 'AS00', 'EC_SYSTEM_FAILURE','EC_SYSTEM_FAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10035', 'AS00', 'EC_DATA_MISSING','EC_DATA_MISSING', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10036', 'AS00', 'EC_UNEXPECTED_DATA_VALUE','EC_UNEXPECTED_DATA_VALUE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '30051', 'AS00', 'EC_RESOURCE_LIMITATION','EC_RESOURCE_LIMITATION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '30071', 'AS00', 'EC_UNKNOWN_ALPHABET','EC_UNKNOWN_ALPHABET', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10072', 'AS00', 'EC_USSD_BUSY','EC_USSD_BUSY', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10255', 'AS00', 'EC_UNKNOWN_ERROR','EC_UNKNOWN_ERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10256', 'AS00', 'EC_SM_DF_MEMORYCAPACITYEXCEEDED','EC_SM_DF_MEMORYCAPACITYEXCEEDED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10257', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10258', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10259', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10260', 'AS00', 'EC_SM_DF_EQUIPMENTPROTOCOLERROR','EC_SM_DF_EQUIPMENTPROTOCOLERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10261', 'AS00', 'EC_SM_DF_INVALIDSME_ADDRESS','EC_SM_DF_INVALIDSME_ADDRESS', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10262', 'AS00', 'EC_SM_DF_SUBSCRIBERNOTSC_SUBSCRIBER','EC_SM_DF_SUBSCRIBERNOTSC_SUBSCRIBER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10500', 'AS00', 'EC_PROVIDER_GENERAL_ERROR','EC_PROVIDER_GENERAL_ERROR', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '30501', 'AS00', 'EC_INVALID_RESPONSE_RECEIVED','EC_INVALID_RESPONSE_RECEIVED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10502', 'AS00', 'EC_NO_RESPONSE','EC_NO_RESPONSE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10503', 'AS00', 'EC_SERVICE_COMPLETION_FAILURE','EC_SERVICE_COMPLETION_FAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10504', 'AS00', 'EC_UNEXPECTED_RESPONSE_FROM_PEER','EC_UNEXPECTED_RESPONSE_FROM_PEER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10507', 'AS00', 'EC_MISTYPED_PARAMETER','EC_MISTYPED_PARAMETER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10508', 'AS00', 'EC_NOT_SUPPORTED_SERVICE','EC_NOT_SUPPORTED_SERVICE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10509', 'AS00', 'EC_DUPLICATED_INVOKE_ID','EC_DUPLICATED_INVOKE_ID', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '10511', 'AS00', 'EC_INITIATING_RELEASE','EC_INITIATING_RELEASE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11024', 'AS00', 'EC_OR_APPCONTEXTNOTSUPPORTED','EC_OR_APPCONTEXTNOTSUPPORTED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11026', 'AS00', 'EC_OR_INVALIDORIGINATINGREFERENCE','EC_OR_INVALIDORIGINATINGREFERENCE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11027', 'AS00', 'EC_OR_ENCAPSULATEDAC_NOTSUPPORTED','EC_OR_ENCAPSULATEDAC_NOTSUPPORTED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11028', 'AS00', 'EC_OR_TRANSPORTPROTECTIONNOTADEQUATE','EC_OR_TRANSPORTPROTECTIONNOTADEQUATE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11029', 'AS00', 'EC_OR_NOREASONGIVEN','EC_OR_NOREASONGIVEN', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11030', 'AS00', 'EC_OR_POTENTIALVERSIONINCOMPATIBILITY','EC_OR_POTENTIALVERSIONINCOMPATIBILITY', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11031', 'AS00', 'EC_OR_REMOTENODENOTREACHABLE','EC_OR_REMOTENODENOTREACHABLE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11152', 'AS00', 'EC_NNR_NOTRANSLATIONFORANADDRESSOFSUCHNATURE','EC_NNR_NOTRANSLATIONFORANADDRESSOFSUCHNATURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11153', 'AS00', 'EC_NNR_NOTRANSLATIONFORTHISSPECIFICADDRESS','EC_NNR_NOTRANSLATIONFORTHISSPECIFICADDRESS', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11154', 'AS00', 'EC_NNR_SUBSYSTEMCONGESTION','EC_NNR_SUBSYSTEMCONGESTION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11155','AS00','EC_NNR_SUBSYSTEMFAILURE','EC_NNR_SUBSYSTEMFAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11156','AS00','EC_NNR_UNEQUIPPEDUSER','EC_NNR_UNEQUIPPEDUSER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11157','AS00','EC_NNR_MTPFAILURE','EC_NNR_MTPFAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11158','AS00','EC_NNR_NETWORKCONGESTION','EC_NNR_NETWORKCONGESTION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11159','AS00','EC_NNR_UNQUALIFIED','EC_NNR_UNQUALIFIED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11160','AS00','EC_NNR_ERRORINMESSAGETRANSPORTXUDT','EC_NNR_ERRORINMESSAGETRANSPORTXUDT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11161','AS00','EC_NNR_ERRORINLOCALPROCESSINGXUDT','EC_NNR_ERRORINLOCALPROCESSINGXUDT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11162','AS00','EC_NNR_DESTINATIONCANNOTPERFORMREASSEMBLYXUDT','EC_NNR_DESTINATIONCANNOTPERFORMREASSEMBLYXUDT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11163','AS00','EC_NNR_SCCPFAILURE','EC_NNR_SCCPFAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11164','AS00','EC_NNR_HOPCOUNTERVIOLATION','EC_NNR_HOPCOUNTERVIOLATION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11165','AS00','EC_NNR_SEGMENTATIONNOTSUPPORTED','EC_NNR_SEGMENTATIONNOTSUPPORTED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11166','AS00','EC_NNR_SEGMENTATIONFAILURE','EC_NNR_SEGMENTATIONFAILURE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11281','AS00','EC_UA_USERSPECIFICREASON','EC_UA_USERSPECIFICREASON', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11282','AS00','EC_UA_USERRESOURCELIMITATION','EC_UA_USERRESOURCELIMITATION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11283','AS00','EC_UA_RESOURCEUNAVAILABLE','EC_UA_RESOURCEUNAVAILABLE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11284','AS00','EC_UA_APPLICATIONPROCEDURECANCELLATION','EC_UA_APPLICATIONPROCEDURECANCELLATION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11536','AS00','EC_PA_PROVIDERMALFUNCTION','EC_PA_PROVIDERMALFUNCTION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11537','AS00','EC_PA_SUPPORTINGDIALOGORTRANSACTIONREALEASED','EC_PA_SUPPORTINGDIALOGORTRANSACTIONREALEASED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11538','AS00','EC_PA_RESSOURCELIMITATION','EC_PA_RESSOURCELIMITATION', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11539','AS00','EC_PA_MAINTENANCEACTIVITY','EC_PA_MAINTENANCEACTIVITY', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11540','AS00','EC_PA_VERSIONINCOMPATIBILITY','EC_PA_VERSIONINCOMPATIBILITY', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11541','AS00','EC_PA_ABNORMALMAPDIALOG','EC_PA_ABNORMALMAPDIALOG', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11792','AS00','EC_NC_ABNORMALEVENTDETECTEDBYPEER','EC_NC_ABNORMALEVENTDETECTEDBYPEER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11793','AS00','EC_NC_RESPONSEREJECTEDBYPEER','EC_NC_RESPONSEREJECTEDBYPEER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11794','AS00','EC_NC_ABNORMALEVENTRECEIVEDFROMPEER','EC_NC_ABNORMALEVENTRECEIVEDFROMPEER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11795','AS00','EC_NC_MESSAGECANNOTBEDELIVEREDTOPEER','EC_NC_MESSAGECANNOTBEDELIVEREDTOPEER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '11796','AS00','EC_NC_PROVIDEROUTOFINVOKE','EC_NC_PROVIDEROUTOFINVOKE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '32048','AS00','EC_TIME_OUT','EC_TIME_OUT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '22049','AS00','EC_IMSI_BLACKLISTED','EC_IMSI_BLACKLISTED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '32050','AS00','EC_DEST_ADDRESS_BLACKLISTED','EC_DEST_ADDRESS_BLACKLISTED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '32051','AS00','EC_INVALIDMSCADDRESS','EC_INVALIDMSCADDRESS', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '24096','AS00','EC_INVALID_PDU_FORMAT','EC_INVALID_PDU_FORMAT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '34097','AS00','EC_NOTSUBMITTEDTOGMSC','EC_NOTSUBMITTEDTOGMSC', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '24100','AS00','EC_MESSAGE_CANCELED','EC_MESSAGE_CANCELED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '24101','AS00','EC_VALIDITYEXPIRED','EC_VALIDITYEXPIRED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '34102','AS00','EC_NOTSUBMITTEDTOSMPPCHANNEL','EC_NOTSUBMITTEDTOSMPPCHANNEL', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '05000','AS00','VOICE_ANSWERED','VOICE_ANSWERED', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '05001','AS00','VOICE_ANSWERED_MACHINE','VOICE_ANSWERED_MACHINE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '05002','AS00','EC_VOICE_USER_BUSY','EC_VOICE_USER_BUSY', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '25003','AS00','EC_VOICE_NO_ANSWER','EC_VOICE_NO_ANSWER', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '25004','AS00','EC_VOICE_ERROR_DOWNLOADING_FILE','EC_VOICE_ERROR_DOWNLOADING_FILE', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '25005','AS00','EC_VOICE_ERROR_UNSUPPORTED_AUDIO_FORMAT','EC_VOICE_ERROR_UNSUPPORTED_AUDIO_FORMAT', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '99990','AS00','HTTP 오류','인포빕 API로 HTTP 통신하는 동안 오류', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');
INSERT INTO NV_CD_MST (CD_CAT, CD, PAR_CD_CAT, VAL, CD_DESC, USE_COL, USE_YN, REG_DTM, MOD_DTM, CD_ORD, LANG)
VALUES ('AS0003', '99999','AS00','에이전트 오류','에이전트내 처리하는 동안 오류', NULL, 'y', REPLACE(REPLACE(REPLACE(CONVERT(VARCHAR,GETDATE(),120),'-',''),':',''),' ',''), NULL, NULL, 'en');