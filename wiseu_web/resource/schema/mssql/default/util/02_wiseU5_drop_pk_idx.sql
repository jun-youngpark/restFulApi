ALTER TABLE NVACCEPTSVCID               DROP CONSTRAINT PK_NVACCEPTSVCID;
ALTER TABLE NVAPPLICATION               DROP CONSTRAINT PK_NVAPPLICATION ;
ALTER TABLE NVBADWORD                   DROP CONSTRAINT PK_NVBADWORD;
ALTER TABLE NVCAMPAIGN                  DROP CONSTRAINT PK_NVCAMPAIGN;
ALTER TABLE NVCAMPAIGNINVOKEHISTORY     DROP CONSTRAINT PK_NVCAMPAIGNINVOKEHISTORY;
ALTER TABLE NVCAMPTAG                   DROP CONSTRAINT PK_NVCAMPTAG;
ALTER TABLE NVCONTENTS                  DROP CONSTRAINT PK_NVCONTENTS;
ALTER TABLE NVCONTENTSTAG               DROP CONSTRAINT PK_NVCONTENTSTAG;
ALTER TABLE NVCYCLEITEM                 DROP CONSTRAINT PK_NVCYCLEITEM;
ALTER TABLE NVDBINFO                    DROP CONSTRAINT PK_NVDBINFO;
ALTER TABLE NVDEFAULTHANDLER            DROP CONSTRAINT PK_NVDEFAULTHANDLER;
ALTER TABLE NVDOMAINLOG                 DROP CONSTRAINT PK_NVDOMAINLOG;
ALTER TABLE NVDURATIONINFO              DROP CONSTRAINT PK_NVDURATIONINFO;
ALTER TABLE NVECAREDOMAINLOG            DROP CONSTRAINT PK_NVECAREDOMAINLOG;
ALTER TABLE NVECAREINVOKEHISTORY        DROP CONSTRAINT PK_NVECAREINVOKEHISTORY;
ALTER TABLE NVECAREKMMAP                DROP CONSTRAINT PK_NVECAREKMMAP;
ALTER TABLE NVECARELINKRESULT           DROP CONSTRAINT PK_NVECARELINKRESULT;
ALTER TABLE NVECARELINKTRACE            DROP CONSTRAINT PK_NVECARELINKTRACE;
ALTER TABLE NVECAREMIMEINFOLOG          DROP CONSTRAINT PK_NVECAREMIMEINFOLOG;
ALTER TABLE NVECAREMSG                  DROP CONSTRAINT PK_NVECAREMSG;
ALTER TABLE NVECAREMSGTAG               DROP CONSTRAINT PK_NVECAREMSGTAG;
ALTER TABLE NVECAREMULTIPARTFILE        DROP CONSTRAINT PK_NVECAREMLTPARTFILE;
ALTER TABLE NVECARERECEIPT              DROP CONSTRAINT PK_NVECARERECEIPT;
ALTER TABLE NVECAREREJECT               DROP CONSTRAINT PK_NVECAREREJECT;
ALTER TABLE NVECARERETURNMAIL           DROP CONSTRAINT PK_NVECARERETURNMAIL;
ALTER TABLE NVECARERPTDURATION          DROP CONSTRAINT PK_NVECARERPTDURATION;
ALTER TABLE NVECARERPTLINK              DROP CONSTRAINT PK_NVECARERPTLINK;
ALTER TABLE NVECARERPTOPEN              DROP CONSTRAINT PK_NVECARERPTOPEN;
ALTER TABLE NVECARERPTSENDRESULT        DROP CONSTRAINT PK_NVECARERPTSENDRESULT;
ALTER TABLE NVECARESCENARIO             DROP CONSTRAINT PK_NVECARESCENARIO;
ALTER TABLE NVECARESENDLOG              DROP CONSTRAINT PK_NVECARESENDLOG;
ALTER TABLE NVECARESENDRESULT           DROP CONSTRAINT PK_NVECARESENDRESULT;
ALTER TABLE NVECARESENDRESULTDETAIL     DROP CONSTRAINT PK_NVECARESENDRESULTDETAIL;
ALTER TABLE NVECARETEMPLATE             DROP CONSTRAINT PK_NVECARETEMPLATE;
ALTER TABLE NVECARETRACEINFO            DROP CONSTRAINT PK_NVECARETRACEINFO;
ALTER TABLE NVECMSCHEDULE               DROP CONSTRAINT PK_NVECMSCHEDULE;
ALTER TABLE NVECMSGHANDLER              DROP CONSTRAINT PK_NVECMSGHANDLER;
ALTER TABLE NVECMSGHANDLERBASIC         DROP CONSTRAINT PK_NVECMSGHANDLERBASIC;
ALTER TABLE NVFILEUPLOAD                DROP CONSTRAINT PK_NVFILEUPLOAD;
ALTER TABLE NVGRPMENUROLE               DROP CONSTRAINT PK_NVGRPMENUROLE;
ALTER TABLE NVLINKCONDITION             DROP CONSTRAINT PK_NVLINKCONDITION;
ALTER TABLE NVLINKRESULT                DROP CONSTRAINT PK_NVLINKRESULT;
ALTER TABLE NVLINKTRACE                 DROP CONSTRAINT PK_NVLINKTRACE;
ALTER TABLE NVMENU                      DROP CONSTRAINT PK_NVMENU;
ALTER TABLE NVMENUDEFAULTROLE           DROP CONSTRAINT PK_NVMENUDEFAULTROLE;
ALTER TABLE NVMENUROLE                  DROP CONSTRAINT PK_NVMENUROLE;
ALTER TABLE NVMENU_LANG                 DROP CONSTRAINT PK_NVMENU_LANG;
ALTER TABLE NVMIMEINFOLOG               DROP CONSTRAINT PK_NVMIMEINFOLOG;
ALTER TABLE NVMOBILECONTENTS            DROP CONSTRAINT PK_NVMOBILECONTENTS;
ALTER TABLE NVMOBILECONTENTSTAG         DROP CONSTRAINT PK_NVMOBILECONTENTSTAG;
ALTER TABLE NVMULTIPARTFILE             DROP CONSTRAINT PK_NVMLTPARTFILE;
ALTER TABLE NVREALTIMEACCEPT            DROP CONSTRAINT PK_NVREALTIMEACCEPT;
ALTER TABLE NVREALTIMEACCEPTDATA        DROP CONSTRAINT PK_NVREALTIMEACCEPTDATA;
ALTER TABLE NVRECEIPT                   DROP CONSTRAINT PK_NVRECEIPT;
ALTER TABLE NVREJECT                    DROP CONSTRAINT PK_NVREJECT;
ALTER TABLE NVRETURNMAIL                DROP CONSTRAINT PK_NVRETURNMAIL;
ALTER TABLE NVRPTDURATION               DROP CONSTRAINT PK_NVRPTDURATION;
ALTER TABLE NVRPTLINK                   DROP CONSTRAINT PK_NVRPTLINK;
ALTER TABLE NVRPTLINKDISTINCT           DROP CONSTRAINT PK_NVRPTLINKDISTINCT;
ALTER TABLE NVRPTOPEN                   DROP CONSTRAINT PK_NVRPTOPEN;
ALTER TABLE NVSCENARIO                  DROP CONSTRAINT PK_NVSCENARIO;
ALTER TABLE NVSCHEDULE                  DROP CONSTRAINT PK_NVSCHEDULE;
ALTER TABLE NVSCHEDULEACCEPT            DROP CONSTRAINT PK_NVSCHEDULEACCEPT;
ALTER TABLE NVSCHEDULEACCEPTDATA        DROP CONSTRAINT PK_NVSCHEDULEACCEPTDATA;
ALTER TABLE NVSEGGENEALOGY              DROP CONSTRAINT PK_NVSEGGENEALOGY;
ALTER TABLE NVSEGMENT                   DROP CONSTRAINT PK_NVSEGMENT;
ALTER TABLE NVSEGMENTCHECK              DROP CONSTRAINT PK_NVSEGMENTCHECK;
ALTER TABLE NVSEGMENTTAG                DROP CONSTRAINT PK_NVSEGMENTTAG;
ALTER TABLE NVSEMANTIC                  DROP CONSTRAINT PK_NVSEMANTIC;
ALTER TABLE NVSENDBLOCKDATE             DROP CONSTRAINT PK_NVSENDBLOCKDATE;
ALTER TABLE NVSENDBLOCKDATEINFO         DROP CONSTRAINT PK_NVSENDBLOCKDATEINFO;
ALTER TABLE NVSENDCYCLE                 DROP CONSTRAINT PK_NVSENDCYCLE;
ALTER TABLE NVSENDERR                   DROP CONSTRAINT PK_NVSENDERR;
ALTER TABLE NVSENDLOG                   DROP CONSTRAINT PK_NVSENDLOG;
ALTER TABLE NVSENDRESULT                DROP CONSTRAINT PK_NVSENDRESULT;
ALTER TABLE NVSENDRESULTDETAIL          DROP CONSTRAINT PK_NVSENDRESULTDETAIL;
ALTER TABLE NVSERVERINFO                DROP CONSTRAINT PK_NVSERVERINFO;
ALTER TABLE NVSMSRPTSENDRESULT          DROP CONSTRAINT PK_NVSMSRPTSENDRESULT;
ALTER TABLE NVSMTPCATEGORY              DROP CONSTRAINT PK_NVSMTPCATEGORY;
ALTER TABLE NVTEMPLATE                  DROP CONSTRAINT PK_NVTEMPLATE;
ALTER TABLE NVTESTECARESENDLOG          DROP CONSTRAINT PK_NVTESTECARESENDLOG;
ALTER TABLE NVTESTSENDLOG               DROP CONSTRAINT PK_NVTESTSENDLOG;
ALTER TABLE NVTESTUSER                  DROP CONSTRAINT PK_NVTESTUSER;
ALTER TABLE NVTESTUSERPOOL              DROP CONSTRAINT PK_NVTESTUSERPOOL;
ALTER TABLE NVTOPDOMAIN                 DROP CONSTRAINT PK_NVTOPDOMAIN;
ALTER TABLE NVTRACEINFO                 DROP CONSTRAINT PK_NVTRACEINFO;
ALTER TABLE NVUSER                      DROP CONSTRAINT PK_NVUSER;
ALTER TABLE NVUSERGRP                   DROP CONSTRAINT PK_NVUSERGRP;
ALTER TABLE NVUSERMAILINFO              DROP CONSTRAINT PK_NVUSERMAILINFO;
ALTER TABLE NV_CD_MST                   DROP CONSTRAINT PK_NV_CD_MST;
ALTER TABLE NV_SPAM_SENDLOG             DROP CONSTRAINT PK_NV_SPAM_SENDLOG;
ALTER TABLE NV_STATUS_REQ               DROP CONSTRAINT PK_NV_STATUS_REQ;
ALTER TABLE NV_STATUS_REQ_RESULT        DROP CONSTRAINT PK_NV_STATUS_REQ_RESULT;
ALTER TABLE NV_SVC_MAIN                 DROP CONSTRAINT PK_NV_SVC_MAIN;
ALTER TABLE NV_SVC_MTS                  DROP CONSTRAINT PK_NV_SVC_MTS;
ALTER TABLE NV_SVC_TRANS                DROP CONSTRAINT PK_NV_SVC_TRANS;
ALTER TABLE NV_SVR_INFO                 DROP CONSTRAINT PK_NV_SVR_INFO;
ALTER TABLE NV_TEST_GRP                 DROP CONSTRAINT PK_NV_TEST_GRP;
DROP INDEX IDX_NVCAMPAIGN_SCENARIO_NO ON NVCAMPAIGN;
DROP INDEX IDX_NVECAREDOMAINLOG_SDT_ENO ON NVECAREDOMAINLOG;
DROP INDEX IDX_NVECARELINKRESULT_LDT_ENO ON NVECARELINKRESULT;
DROP INDEX IDX_NVECARERECEIPT_MLT1 ON NVECARERECEIPT;
DROP INDEX IDX_NVECAREREJECT_RDT_ENO ON NVECAREREJECT;
DROP INDEX IDX_NVECARERETURNMAIL_RDT_ENO ON NVECARERETURNMAIL;
DROP INDEX IDX_NVECARERPTDURATION_RDT_ENO ON NVECARERPTDURATION;
DROP INDEX IDX_NVECARERPTLINK_RDT_ENO ON NVECARERPTLINK;
DROP INDEX IDX_NVECARERPTOPEN_RDT_ENO ON NVECARERPTOPEN;
DROP INDEX IDX_NVECARERPTSENDRESULT_MLT1 ON NVECARERPTSENDRESULT;
DROP INDEX IDX_NVECARESENDLOG_MESSAGE_KEY ON NVECARESENDLOG;
DROP INDEX IDX_NVECARESENDLOG_SDT_CKY ON NVECARESENDLOG;
DROP INDEX IDX_NVECARESENDLOG_SDT_CML ON NVECARESENDLOG;
DROP INDEX IDX_NVECARESENDLOG_SDT_ENO ON NVECARESENDLOG;
DROP INDEX IDX_NVECARESENDLOG_SEQ ON NVECARESENDLOG;
DROP INDEX IDX_NVECARESENDRESULT_EDT_ENO ON NVECARESENDRESULT;
DROP INDEX IDX_NVECARESENDRESULT_MANUBAT ON NVECARESENDRESULT;
DROP INDEX IDX_NVECARESENDRESULT_MLT1 ON NVECARESENDRESULT;
DROP INDEX IDX_NVECARESENDRESULT_SDT_ENO ON NVECARESENDRESULT;
DROP INDEX IDX_NVECARETEMPLATEHISTORY_ENO ON NVECARETEMPLATEHISTORY;
DROP INDEX IDX_NVECMSGHANDLERHISTORY_ENO ON NVECMSGHANDLERHISTORY;
DROP INDEX IDX_NVLINKRESULT_LDT_CNO ON NVLINKRESULT;
DROP INDEX IDX_NVREALTIMEACCEPT_ENO_RSEQ ON NVREALTIMEACCEPT;
DROP INDEX IDX_NVREALTIMEACCEPT_REQ_DT ON NVREALTIMEACCEPT;
DROP INDEX IDX_NVREALTIMEACCEPT_SEND_FG ON NVREALTIMEACCEPT;
DROP INDEX IDX_NVRECEIPT_ODT_CNO ON NVRECEIPT;
DROP INDEX IDX_NVREJECT_RDT_CNO ON NVREJECT;
DROP INDEX IDX_NVRETURNMAIL_RDT_CNO ON NVRETURNMAIL;
DROP INDEX IDX_NVRPTDURATION_REPORT_DT ON NVRPTDURATION;
DROP INDEX IDX_NVRPTLINKDISTINCT_RPT_DT ON NVRPTLINKDISTINCT;
DROP INDEX IDX_NVRPTLINK_RDT_CNO ON NVRPTLINK;
DROP INDEX IDX_NVRPTOPEN_REPORT_DT ON NVRPTOPEN;
DROP INDEX IDX_NVSCHEDULEACCEPT_REQ_DT ON NVSCHEDULEACCEPT;
DROP INDEX IDX_NVSCHEDULEACCEPT_SEND_FG ON NVSCHEDULEACCEPT;
DROP INDEX IDX_NVSENDLOG_CNO_ECD ON NVSENDLOG;
DROP INDEX IDX_NVSENDLOG_MESSAGE_KEY ON NVSENDLOG;
DROP INDEX IDX_NVSENDLOG_SDT_CKY ON NVSENDLOG;
DROP INDEX IDX_NVSENDLOG_SDT_CML ON NVSENDLOG;
DROP INDEX IDX_NVSENDLOG_SEQ ON NVSENDLOG;
DROP INDEX IDX_NVSMSRPTSENDRESULT_SNO_RSQ ON NVSMSRPTSENDRESULT;
DROP INDEX IDX_NVSMSRPTSENDRESULT_SNO_SGB ON NVSMSRPTSENDRESULT;
DROP INDEX IDX_NVSMSRPTSENDRESULT_SNO_STP ON NVSMSRPTSENDRESULT;
DROP INDEX IDX_NVTESTECARESENDLOG_SEND_DT ON NVTESTECARESENDLOG;
DROP INDEX IDX_NVTESTSENDLOG_SEND_DT ON NVTESTSENDLOG;
DROP INDEX IDX_NV_CD_MST_PAR_CD_CAT ON NV_CD_MST;