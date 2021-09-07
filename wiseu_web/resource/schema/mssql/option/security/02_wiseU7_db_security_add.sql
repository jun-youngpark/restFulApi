/****************************************************************
 * 알림톡
*****************************************************************/

ALTER TABLE MZSENDTRAN                        	ALTER COLUMN PHONE_NUM  	VARCHAR(100);
ALTER TABLE MZSENDLOG                        		ALTER COLUMN PHONE_NUM  	VARCHAR(100);

ALTER TABLE MZSENDTRAN                        	ALTER COLUMN SND_MSG 	VARCHAR(4000);
ALTER TABLE MZSENDLOG                        		ALTER COLUMN SND_MSG 	VARCHAR(4000);

ALTER TABLE MZSENDTRAN                        	ALTER COLUMN SMS_SND_NUM 	VARCHAR(100);
ALTER TABLE MZSENDLOG                        		ALTER COLUMN SMS_SND_NUM 	VARCHAR(100);

ALTER TABLE MZSENDTRAN                        	ALTER COLUMN SMS_SND_MSG 	VARCHAR(4000);
ALTER TABLE MZSENDLOG                        		ALTER COLUMN SMS_SND_MSG 	VARCHAR(4000);

ALTER TABLE MZSENDTRAN                        	ALTER COLUMN SLOT1 	VARCHAR(256);
ALTER TABLE MZSENDLOG                        		ALTER COLUMN SLOT1  	VARCHAR(256);

ALTER TABLE MZSENDTRAN                        	ALTER COLUMN SLOT2 	VARCHAR(256);
ALTER TABLE MZSENDLOG                        		ALTER COLUMN SLOT2 	VARCHAR(256);

/****************************************************************
 * 친구톡
*****************************************************************/

ALTER TABLE MZFTSENDTRAN                        	ALTER COLUMN PHONE_NUM  	VARCHAR(100);
ALTER TABLE MZFTSENDLOG                        		ALTER COLUMN PHONE_NUM  	VARCHAR(100);

ALTER TABLE MZFTSENDTRAN                        	ALTER COLUMN SND_MSG 	VARCHAR(4000);
ALTER TABLE MZFTSENDLOG                        		ALTER COLUMN SND_MSG 	VARCHAR(4000);

ALTER TABLE MZFTSENDTRAN                        	ALTER COLUMN SMS_SND_NUM 	VARCHAR(100);
ALTER TABLE MZFTSENDLOG                        		ALTER COLUMN SMS_SND_NUM 	VARCHAR(100);

ALTER TABLE MZFTSENDTRAN                        	ALTER COLUMN SMS_SND_MSG 	VARCHAR(4000);
ALTER TABLE MZFTSENDLOG                        		ALTER COLUMN SMS_SND_MSG 	VARCHAR(4000);

ALTER TABLE MZFTSENDTRAN                        	ALTER COLUMN SLOT1 	VARCHAR(256);
ALTER TABLE MZFTSENDLOG                        		ALTER COLUMN SLOT1  	VARCHAR(256);

ALTER TABLE MZFTSENDTRAN                        	ALTER COLUMN SLOT2 	VARCHAR(256);
ALTER TABLE MZFTSENDLOG                        		ALTER COLUMN SLOT2 	VARCHAR(256);

/****************************************************************
 * 문자
*****************************************************************/

ALTER TABLE EM_TRAN               ALTER COLUMN TRAN_PHONE             VARCHAR(100);
ALTER TABLE EM_TRAN               ALTER COLUMN TRAN_CALLBACK          VARCHAR(100);
ALTER TABLE EM_LOG		          ALTER COLUMN TRAN_PHONE             VARCHAR(100);
ALTER TABLE EM_LOG		          ALTER COLUMN TRAN_CALLBACK          VARCHAR(100);


/****************************************************************
 * 브랜드톡
*****************************************************************/
ALTER TABLE MZBTSENDTRAN                          ALTER COLUMN PHONE_NUM    VARCHAR(100);
ALTER TABLE MZBTSENDLOG                               ALTER COLUMN PHONE_NUM    VARCHAR(100);

ALTER TABLE MZBTSENDTRAN                          ALTER COLUMN SND_MSG  VARCHAR(4000);
ALTER TABLE MZBTSENDLOG                               ALTER COLUMN SND_MSG  VARCHAR(4000);

ALTER TABLE MZBTSENDTRAN                          ALTER COLUMN SLOT1    VARCHAR(256);
ALTER TABLE MZBTSENDLOG                               ALTER COLUMN SLOT1    VARCHAR(256);

ALTER TABLE MZBTSENDTRAN                          ALTER COLUMN SLOT2    VARCHAR(256);
ALTER TABLE MZBTSENDLOG                               ALTER COLUMN SLOT2    VARCHAR(256);