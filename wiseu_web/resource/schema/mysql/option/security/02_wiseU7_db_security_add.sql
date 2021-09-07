/****************************************************************
 * 알림톡
*****************************************************************/

alter table mzsendtran                          modify phone_num    varchar(100);
alter table mzsendlog                               modify phone_num    varchar(100);

alter table mzsendtran                          modify snd_msg  varchar(4000);
alter table mzsendlog                               modify snd_msg  varchar(4000);

alter table mzsendtran                          modify sms_snd_num  varchar(100);
alter table mzsendlog                               modify sms_snd_num  varchar(100);

alter table mzsendtran                          modify sms_snd_msg  varchar(4000);
alter table mzsendlog                               modify sms_snd_msg  varchar(4000);

alter table mzsendtran                          modify slot1    varchar(256);
alter table mzsendlog                               modify slot1    varchar(256);

alter table mzsendtran                          modify slot2    varchar(256);
alter table mzsendlog                               modify slot2    varchar(256);

/****************************************************************
 * 친구톡
*****************************************************************/

alter table mzftsendtran                            modify phone_num    varchar(100);
alter table mzftsendlog                             modify phone_num    varchar(100);

alter table mzftsendtran                            modify snd_msg  varchar(4000);
alter table mzftsendlog                             modify snd_msg  varchar(4000);

alter table mzftsendtran                            modify sms_snd_num  varchar(100);
alter table mzftsendlog                             modify sms_snd_num  varchar(100);

alter table mzftsendtran                            modify sms_snd_msg  varchar(4000);
alter table mzftsendlog                             modify sms_snd_msg  varchar(4000);

alter table mzftsendtran                            modify slot1    varchar(256);
alter table mzftsendlog                             modify slot1    varchar(256);

alter table mzftsendtran                            modify slot2    varchar(256);
alter table mzftsendlog                             modify slot2    varchar(256);

/****************************************************************
 * 문자
*****************************************************************/

alter table em_tran               modify tran_phone             varchar(100);
alter table em_tran               modify tran_callback          varchar(100);
alter table em_log                modify tran_phone             varchar(100);
alter table em_log                modify tran_callback          varchar(100);

/****************************************************************
 * 브랜드톡
*****************************************************************/
alter table mzbtsendtran                          modify phone_num    varchar(100);
alter table mzbtsendlog                               modify phone_num    varchar(100);

alter table mzbtsendtran                          modify snd_msg  varchar(4000);
alter table mzbtsendlog                               modify snd_msg  varchar(4000);

alter table mzbtsendtran                          modify slot1    varchar(256);
alter table mzbtsendlog                               modify slot1    varchar(256);

alter table mzbtsendtran                          modify slot2    varchar(256);
alter table mzbtsendlog                               modify slot2    varchar(256);