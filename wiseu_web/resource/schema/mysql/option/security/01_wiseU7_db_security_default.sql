/****************************************************************
 * ALTER
 * DB암호화로 인한 사이즈 변경
 * SMS: 25 ->88  (->100)
 * 이메일:100 ->152 (->256)
 * 이름: 100 ->152 (->256)
*****************************************************************/

ALTER TABLE nvfileupload          modify customer_tel           VARCHAR(100);

ALTER TABLE nvfileupload          modify customer_nm            VARCHAR(256);
ALTER TABLE nvfileupload          modify customer_email         VARCHAR(256);
ALTER TABLE nvfileupload          modify customer_fax           VARCHAR(256);

ALTER TABLE nvsendlog 	          modify customer_nm            VARCHAR(256);
ALTER TABLE nvsendlog	          modify customer_email         VARCHAR(256);
ALTER TABLE nvtestsendlog         modify customer_nm            VARCHAR(256);
ALTER TABLE nvtestsendlog         modify customer_email         VARCHAR(256);

ALTER TABLE nvecaremsg            modify receiver_nm            VARCHAR(256);
ALTER TABLE nvecaremsg            modify sender_tel             VARCHAR(100);
ALTER TABLE nvecaremsg            modify sender_email           VARCHAR(256);
ALTER TABLE nvecaremsg            modify sender_nm              VARCHAR(256);
ALTER TABLE nvecaremsg 			  modify retmail_receiver       VARCHAR(256);

ALTER TABLE nvecaremimeinfolog    modify customer_nm            VARCHAR(256);
ALTER TABLE nvecaremimeinfolog    modify customer_email         VARCHAR(256);

ALTER TABLE nvecaresendlog        modify customer_nm            VARCHAR(256);
ALTER TABLE nvecaresendlog        modify customer_email         VARCHAR(256);

ALTER TABLE nvtestecaresendlog    modify customer_nm            VARCHAR(256);
ALTER TABLE nvtestecaresendlog    modify customer_email         VARCHAR(256);

ALTER TABLE nvdbinfo              modify dbpassword             VARCHAR(100);

ALTER TABLE nvtestuserpool        modify testreceiver_email     VARCHAR(256);
ALTER TABLE nvtestuserpool        modify testreceiver_nm        VARCHAR(256);
ALTER TABLE nvtestuserpool        modify testreceiver_tel       VARCHAR(100);
ALTER TABLE nvtestuserpool        modify testreceiver_fax       VARCHAR(100);

ALTER TABLE nvusermailinfo        modify sender_nm              VARCHAR(256);
ALTER TABLE nvusermailinfo        modify sender_email           VARCHAR(256);
ALTER TABLE nvusermailinfo        modify receiver_nm            VARCHAR(256);
ALTER TABLE nvusermailinfo        modify sender_tel             VARCHAR(100);
ALTER TABLE nvusermailinfo        modify sender_fax             VARCHAR(100);
ALTER TABLE nvusermailinfo        modify receiver_fax           VARCHAR(100);
ALTER TABLE nvusermailinfo        modify retmail_receiver       VARCHAR(256);

ALTER TABLE nvrealtimeaccept      modify receiver_nm            VARCHAR(256);
ALTER TABLE nvrealtimeaccept      modify receiver               VARCHAR(256);
ALTER TABLE nvrealtimeaccept      modify sender_nm              VARCHAR(256);
ALTER TABLE nvrealtimeaccept      modify sender                 VARCHAR(256);

ALTER TABLE nvscheduleaccept      modify receiver_nm            VARCHAR(256);
ALTER TABLE nvscheduleaccept      modify receiver               VARCHAR(256);
ALTER TABLE nvscheduleaccept      modify sender_nm              VARCHAR(256);
ALTER TABLE nvscheduleaccept      modify sender                 VARCHAR(256);

ALTER TABLE nvecarelinkresult     modify customer_nm            VARCHAR(256);
ALTER TABLE nvecarelinkresult     modify customer_email         VARCHAR(256);

ALTER TABLE nvecarereject         modify customer_nm            VARCHAR(256);
ALTER TABLE nvecarereject         modify customer_email         VARCHAR(256);

ALTER TABLE nvecarereturnmail     modify customer_nm            VARCHAR(256);
ALTER TABLE nvecarereturnmail     modify customer_email         VARCHAR(256);

ALTER TABLE nvlinkresult          modify customer_nm            VARCHAR(256);
ALTER TABLE nvlinkresult          modify customer_email         VARCHAR(256);

ALTER TABLE nvreject              modify customer_nm            VARCHAR(256);
ALTER TABLE nvreject              modify customer_email         VARCHAR(256);

ALTER TABLE nvreturnmail          modify customer_nm            VARCHAR(256);
ALTER TABLE nvreturnmail          modify customer_email         VARCHAR(256);


ALTER TABLE nvfileupload                 	modify  call_back    	VARCHAR(100);

ALTER TABLE nvecarereceipt            	modify customer_email     	VARCHAR(256);
ALTER TABLE nvmimeinfolog              	modify customer_email     	VARCHAR(256);
ALTER TABLE nvreceipt                       	modify customer_email     	VARCHAR(256);

ALTER TABLE nvecarereceipt            	modify customer_nm        	VARCHAR(256);
ALTER TABLE nvmimeinfolog              	modify customer_nm        	VARCHAR(256);
ALTER TABLE nvreceipt                       	modify customer_nm        	VARCHAR(256);

ALTER TABLE nvfileupload                 	modify customer_slot1        	VARCHAR(100);

ALTER TABLE nvfileupload                 	modify customer_slot2        	VARCHAR(100);

ALTER TABLE nvfileupload                 	modify customer_tel        	VARCHAR(100);

ALTER TABLE nvserverinfo                 	modify dbpassword         	VARCHAR(100);

ALTER TABLE nvuser              	         	modify email         	VARCHAR(256);

ALTER TABLE nvecarekmmap              	modify item_pram_value         	VARCHAR(256);

ALTER TABLE nvusergrp                     	modify manager_nm         	VARCHAR(100);

ALTER TABLE nvecaresendlog  modify message_key         	VARCHAR(100);
ALTER TABLE nvsendlog        	         	modify message_key         	VARCHAR(100);
ALTER TABLE nvtestecaresendlog   	modify message_key         	VARCHAR(100);
ALTER TABLE nvtestsendlog	         	modify message_key         	VARCHAR(100);

ALTER TABLE nvuser 	         	         	modify name_eng         	VARCHAR(100);

ALTER TABLE nvuser 	         	         	modify name_kor         	VARCHAR(100);

ALTER TABLE nvuser 	         	         	modify pass_wd         	VARCHAR(256);
ALTER TABLE nvuserpwhistory modify pass_wd         	VARCHAR(256);

ALTER TABLE nvcampaign             	modify receiver_nm        	VARCHAR(256);

ALTER TABLE nvrealtimeaccept     	modify secu_key        	VARCHAR(100);
ALTER TABLE nvrealtimeacceptdata   modify secu_key        	VARCHAR(100);
ALTER TABLE nvscheduleaccept    	modify secu_key        	VARCHAR(100);
ALTER TABLE nvscheduleacceptdata         	modify secu_key        	VARCHAR(100);

ALTER TABLE nvcampaign                   	      	modify sender_email        	VARCHAR(256);
ALTER TABLE nvfileupload                 	      	modify sender_email        	VARCHAR(256);

ALTER TABLE nvcampaign                   	      	modify sender_nm        	VARCHAR(100);
ALTER TABLE nvfileupload                 	      	modify sender_nm        	VARCHAR(100);

ALTER TABLE nvcampaign                              modify sender_tel         	VARCHAR(100);

ALTER TABLE nvuser                                    	modify tel_no         	VARCHAR(100);

/****************************************************************
 * 기본 사용자 정보 변경 (default: Aria 사용시)
*****************************************************************/
UPDATE nvuser
SET name_kor='3EE9C92DB3C4ADA25DF79FFCBABB3107', name_eng='39DD2707A7EA1A04D149DE1948A203AD', tel_no='9E6E05A271870D9324E4FE8D2BBDA754', email='A9551A6D2796C6D19188E66427F03B09'
WHERE user_id='admin';

UPDATE nvuser
SET name_kor='1226361BB6F228B307F9590BC2CA61BA', name_eng='F2447C4E584FF7CF9DCA7504E3086B4C', tel_no='9E6E05A271870D9324E4FE8D2BBDA754', email='7E1BFAA7138628F59DCCFE56A543CE450DD3977473AA8356CE4ACBDAAF2B1FE5'
WHERE user_id='manager';

UPDATE nvuser
SET name_kor='71C788D06567E2DA07A89AE1E93EAFA2', name_eng='D17C7DF6824BC4E6D90234ABE3948F29', tel_no='9E6E05A271870D9324E4FE8D2BBDA754', email='5235C0A703F7295CF0751A8AAA333C1E'
WHERE user_id='test';