/****************************************************************
 * 알림톡
*****************************************************************/

ALTER TABLE MZSENDTRAN                        	MODIFY PHONE_NUM  	VARCHAR2(100);
ALTER TABLE MZSENDLOG                        		MODIFY PHONE_NUM  	VARCHAR2(100);

ALTER TABLE MZSENDTRAN                        	MODIFY SND_MSG 	VARCHAR2(4000);
ALTER TABLE MZSENDLOG                        		MODIFY SND_MSG 	VARCHAR2(4000);

ALTER TABLE MZSENDTRAN                        	MODIFY SMS_SND_NUM 	VARCHAR2(100);
ALTER TABLE MZSENDLOG                        		MODIFY SMS_SND_NUM 	VARCHAR2(100);

ALTER TABLE MZSENDTRAN                        	MODIFY SMS_SND_MSG 	VARCHAR2(4000);
ALTER TABLE MZSENDLOG                        		MODIFY SMS_SND_MSG 	VARCHAR2(4000);

ALTER TABLE MZSENDTRAN                        	MODIFY SLOT1 	VARCHAR2(256);
ALTER TABLE MZSENDLOG                        		MODIFY SLOT1  	VARCHAR2(256);

ALTER TABLE MZSENDTRAN                        	MODIFY SLOT2 	VARCHAR2(256);
ALTER TABLE MZSENDLOG                        		MODIFY SLOT2 	VARCHAR2(256);

/****************************************************************
 * 친구톡
*****************************************************************/

ALTER TABLE MZFTSENDTRAN                        	MODIFY PHONE_NUM  	VARCHAR2(100);
ALTER TABLE MZFTSENDLOG                        		MODIFY PHONE_NUM  	VARCHAR2(100);

ALTER TABLE MZFTSENDTRAN                        	MODIFY SND_MSG 	VARCHAR2(4000);
ALTER TABLE MZFTSENDLOG                        		MODIFY SND_MSG 	VARCHAR2(4000);

ALTER TABLE MZFTSENDTRAN                        	MODIFY SMS_SND_NUM 	VARCHAR2(100);
ALTER TABLE MZFTSENDLOG                        		MODIFY SMS_SND_NUM 	VARCHAR2(100);

ALTER TABLE MZFTSENDTRAN                        	MODIFY SMS_SND_MSG 	VARCHAR2(4000);
ALTER TABLE MZFTSENDLOG                        		MODIFY SMS_SND_MSG 	VARCHAR2(4000);

ALTER TABLE MZFTSENDTRAN                        	MODIFY SLOT1 	VARCHAR2(256);
ALTER TABLE MZFTSENDLOG                        		MODIFY SLOT1  	VARCHAR2(256);

ALTER TABLE MZFTSENDTRAN                        	MODIFY SLOT2 	VARCHAR2(256);
ALTER TABLE MZFTSENDLOG                        		MODIFY SLOT2 	VARCHAR2(256);

ALTER TABLE MZFTSENDTRAN 							MODIFY USER_KEY VARCHAR2(100);
ALTER TABLE MZFTSENDLOG 							MODIFY USER_KEY VARCHAR2(100);

/****************************************************************
 * 문자
*****************************************************************/

ALTER TABLE EM_TRAN               MODIFY TRAN_PHONE             VARCHAR2(100);
ALTER TABLE EM_TRAN               MODIFY TRAN_CALLBACK          VARCHAR2(100);
ALTER TABLE EM_LOG		          MODIFY TRAN_PHONE             VARCHAR2(100);
ALTER TABLE EM_LOG		          MODIFY TRAN_CALLBACK          VARCHAR2(100);


/****************************************************************
 * 브랜드톡
*****************************************************************/
ALTER TABLE MZBTSENDTRAN                          MODIFY PHONE_NUM    VARCHAR2(100);
ALTER TABLE MZBTSENDLOG                               MODIFY PHONE_NUM    VARCHAR2(100);

ALTER TABLE MZBTSENDTRAN                          MODIFY SND_MSG  VARCHAR2(4000);
ALTER TABLE MZBTSENDLOG                               MODIFY SND_MSG  VARCHAR2(4000);

ALTER TABLE MZBTSENDTRAN                          MODIFY SLOT1    VARCHAR2(256);
ALTER TABLE MZBTSENDLOG                               MODIFY SLOT1    VARCHAR2(256);

ALTER TABLE MZBTSENDTRAN                          MODIFY SLOT2    VARCHAR2(256);
ALTER TABLE MZBTSENDLOG                               MODIFY SLOT2    VARCHAR2(256);