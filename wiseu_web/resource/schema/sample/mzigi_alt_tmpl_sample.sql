-- 알림톡 플러스 친구 정보 ( NVKAKAOPROFILE )
INSERT INTO NVKAKAOPROFILE (USER_ID,KAKAO_SENDER_KEY,KAKAO_YELLOW_ID,LAST_SYNC_DTM) VALUES 
('admin','10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','엠지기',NULL);


-- 알림톡 템플릿 리스트 정보 ( NVMOBILECONTENTS )
INSERT INTO NVMOBILECONTENTS (CONTS_NO,USER_ID,CONTS_NM,CONTS_DESC,FILE_PATH,FILE_NAME,IMAGE_URL,DETOUR_FILE_PATH,DETOUR_FILE_NAME,DETOUR_PREVIEW_PATH,DETOUR_PREVIEW_NAME,FILE_TYPE,CREATE_DT,CREATE_TM,AUTH_TYPE,TAG_NO,CONTS_TXT,FILE_PREVIEW_PATH,FILE_PREVIEW_NAME,GRP_CD,CATEGORY_CD,FILE_SIZE,KAKAO_SENDER_KEY,KAKAO_TMPL_CD,KAKAO_TMPL_STATUS,KAKAO_INSP_STATUS,KAKAO_BUTTONS,USE_YN) VALUES 
(1,'admin','메일스팸감지건수',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20190527','144521','U',1,'[CarryM 장애모니터링 - Critical]
고객사명 : #{고객명}
메일 도메인 : #{도메인}
스팸 감지 건수 : #{스팸건수}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZTEST0000001','R','REG','[{"ordering":1,"name":"엠지기웹","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://www.mzigi.com:18080/mzigi/","linkPc":"http://www.mzigi.com:18080/mzigi/","linkIos":null,"linkAnd":null},{"ordering":2,"name":"엠지기SMS웹","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://demo-mzigi.carrym.com:8088/mzigi/","linkPc":"http://demo-mzigi.carrym.com:8088/mzigi/","linkIos":null,"linkAnd":null}]','Y')
,(2,'admin','콜백 무통장입금 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20181120','214534','U',1,'[콜백]
무통장 입금 신청알림!
아이디 : #{아이디}
금액 : #{금액} ',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','CBMSG000001','R','REG',NULL,'Y')
,(3,'admin','MZMSG1000004',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180531','183756','U',1,'[알림] 테스트 메시지입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZMSG1000004','A','APR',NULL,'Y')
,(4,'admin','테스트샘플등록입니다.',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180525','163532','U',1,'안녕하세요

#{회사명} 템플릿 등록 테스트 입니다.

테스트 템플릿 입니다.

감사합니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','TEST_SAMPLE_CALL','A','APR',NULL,'Y')
,(5,'admin','가비아 무통장입금 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180711','095038','U',1,'[가비아 엠지기]
무통장 입금 신청알림!
아이디 : #{아이디}
금액 : #{금액}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','GABIAMSG100001','R','APR',NULL,'Y')
,(6,'admin','관리자 로그인 인증번호',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180619','174129','U',1,'[엠지기 관리자] 로그인 인증번호 : #{인증번호}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZMSG100006','A','APR',NULL,'Y')
,(7,'admin','메일 승인 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180619','174132','U',1,'[엠지기 관리자] 발송 승인 요청
발송시간 : #{시간}
발송번호 : #{이커에번호}
',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZMSG100007','R','REG',NULL,'Y')
,(8,'admin','사내일정통지',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161014','154848','U',1,'등록한 내용이 정상적으로 완료되었습니다.
행사: #{행사명}
일정: #{행사일시}
장소: #{행사장소}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZIGISH0032','R','REG',NULL,'Y')
,(9,'admin','긴급공지',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170810','184652','U',1,'사내 긴급 공지사항이 발행되었습니다.
전 직원 분들은 홈페이지에 접속하여 확인 후 회신 바랍니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MW17071802','R','REG',NULL,'Y')
,(10,'admin','MZMSG1000005',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180531','183825','U',1,'[알림]
출근확인되었습니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZMSG1000005','R','REG',NULL,'Y')
;
INSERT INTO NVMOBILECONTENTS (CONTS_NO,USER_ID,CONTS_NM,CONTS_DESC,FILE_PATH,FILE_NAME,IMAGE_URL,DETOUR_FILE_PATH,DETOUR_FILE_NAME,DETOUR_PREVIEW_PATH,DETOUR_PREVIEW_NAME,FILE_TYPE,CREATE_DT,CREATE_TM,AUTH_TYPE,TAG_NO,CONTS_TXT,FILE_PREVIEW_PATH,FILE_PREVIEW_NAME,GRP_CD,CATEGORY_CD,FILE_SIZE,KAKAO_SENDER_KEY,KAKAO_TMPL_CD,KAKAO_TMPL_STATUS,KAKAO_INSP_STATUS,KAKAO_BUTTONS,USE_YN) VALUES 
(11,'admin','승인요청내역',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160219','091730','U',1,'[Mzigi 알림] 발송 승인 요청내역이 존재합니다. No : [#{authKey}]',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','mzigi01','R','REG',NULL,'Y')
,(12,'admin','문자예약알림(영문)',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160612','125329','U',1,'[Mzigi]This is your message about reservation information. SEND MESSAGE TIME : #{예약시간}, SERVICE_NO : #{메시지 번호}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZ0002','R','REG',NULL,'Y')
,(13,'admin','관리자로그인',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160219','091816','U',1,'[Mzigi 관리자] [#{authKey}]인증번호를 입력하세요.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','mzigi02','R','REG',NULL,'Y')
,(14,'admin','무통장입금 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180531','182558','U',1,'[엠지기]
무통장 입금 신청알림!
아이디 : #{아이디}
금액 : #{금액}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZMSG100001','A','APR',NULL,'Y')
,(15,'admin','테스트 템플릿',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20180131','110918','U',1,'
[엠앤와이즈]
테스트 템플릿입니다.
1. 테스트1
2. 테스트2',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','TEST_mzigi_098','A','APR',NULL,'Y')
,(16,'admin','예약 리마인드',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170703','131416','U',1,'[#{기업명}]

#{회원명}님, 안녕하세요.
회원님께서 예약하신 서비스 [#{서비스명}]의 날짜 #{예약일}가 도래하여 재알람 드립니다.

요청하셨던 서비스 잘 받아보시기 바랍니다.
감사합니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','testCode_20170703','A','APR',NULL,'Y')
,(17,'admin','보안일일결산 웹체크',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170731','183708','U',1,'금일 사무실 보안일일결산 항목을 체크하셔야 합니다.
다음 링크를 참고하여 빠짐없이 체크 바랍니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MW17071803','A','APR','[{"ordering":1,"name":"회사 홈페이지에서 체크","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://www.mnwise.com","linkPc":null,"linkIos":null,"linkAnd":null}]','Y')
,(18,'admin','test032',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161028','181817','U',1,'test032',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','test032','A','APR',NULL,'Y')
,(19,'admin','신한은행 알림톡 미리보기 발송',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171122','U',1,'안녕하세요
고객님의 %변수1%의 현재 잔고 %변수2%는 %변수3%의 이율로 운용중에 있습니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000006','R','REG','[{"ordering":1,"name":"신한은행 앱 바로가기","linkType":"AL","linkTypeName":"앱링크","linkMo":null,"linkPc":null,"linkIos":"%변수4%","linkAnd":"%변수5%"},{"ordering":2,"name":"신한은행 홈페이지 바로가기","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수6%","linkPc":"%변수7%","linkIos":null,"linkAnd":null}]','Y')
,(20,'admin','신한은행 알림톡 테스트 발송(개발중)',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171121','U',1,'안녕하세요, %변수1%고객님!
이번달 청구 금액은 %변수2% 입니다. 
신한은행을 이용해 주셔서 감사합니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000008','R','REG',NULL,'Y')
;
INSERT INTO NVMOBILECONTENTS (CONTS_NO,USER_ID,CONTS_NM,CONTS_DESC,FILE_PATH,FILE_NAME,IMAGE_URL,DETOUR_FILE_PATH,DETOUR_FILE_NAME,DETOUR_PREVIEW_PATH,DETOUR_PREVIEW_NAME,FILE_TYPE,CREATE_DT,CREATE_TM,AUTH_TYPE,TAG_NO,CONTS_TXT,FILE_PREVIEW_PATH,FILE_PREVIEW_NAME,GRP_CD,CATEGORY_CD,FILE_SIZE,KAKAO_SENDER_KEY,KAKAO_TMPL_CD,KAKAO_TMPL_STATUS,KAKAO_INSP_STATUS,KAKAO_BUTTONS,USE_YN) VALUES 
(21,'admin','히어로스클럽 이용가이드',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171121','U',1,'[HERO''S CLUB 이용가이드]로 콘텐츠를 더욱 쉽게 만나보세요. 

아래 URL주소로 접속하시면  
히어로스클럽 pc버전 이용가이드로 연결됩니다. 

★HERO''S CLUB이용가이드 바로가기 : 
★모바일버전 이용가이드는 4월 홈페http://me2.do/FyYDG4eR 
이지 개편과 함께 찾아뵙겠습니다',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000002','R','REG','[{"ordering":1,"name":"★HERO''S CLUB이용가이드","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://me2.do/FyYDG4eR","linkPc":"http://me2.do/FyYDG4eR","linkIos":null,"linkAnd":null}]','Y')
,(22,'admin','신한은행 메시지 발송 테스트용 알림톡',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171121','U',1,'신한은행 메시지 발송 테스트용 알림톡입니다.
%변수1%%변수2%%변수3%%변수4%%변수5%
%변수6%',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000007','R','REG',NULL,'Y')
,(23,'admin','신한은행 발송 테스트 알림톡',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171121','U',1,'
안녕하세요, %변수1%고객님!
이번달 청구 금액은 %변수2% 입니다. 
신한은행을 이용해 주셔 감사합니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000005','R','REG','[{"ordering":1,"name":"%변수3%","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수4%","linkPc":"%변수5%","linkIos":null,"linkAnd":null},{"ordering":2,"name":"%변수6%","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수7`%","linkPc":"%변수8%","linkIos":null,"linkAnd":null},{"ordering":3,"name":"%변수9%","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수10%","linkPc":"%변수11%","linkIos":null,"linkAnd":null},{"ordering":4,"name":"%변수12%","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수13%","linkPc":"%변수14%","linkIos":null,"linkAnd":null},{"ordering":5,"name":"%변수15%","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수16%","linkPc":"%변수17%","linkIos":null,"linkAnd":null}]','Y')
,(24,'admin','신한은행 미리보기 테스트용 알림톡',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171120','U',1,'안녕하세요 신한은행입니다. 본 메시지는 미리보기를 위한 테스트 발신용 템플릿 입니다. 실사용 시 승인이 불허 할 수 있으니 이점 유념하시어 활용하시기 바랍니다.
-UMS관리자-
%변수1%%변수2%%변수3%%변수4%%변수5%',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000004','R','REG','[{"ordering":1,"name":"신한은행 홈페이지 바로가기","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수6%","linkPc":"%변수7%","linkIos":null,"linkAnd":null},{"ordering":2,"name":"신한은행 CRM 바로가기","linkType":"WL","linkTypeName":"웹링크","linkMo":"%변수8%","linkPc":"%변수9%","linkIos":null,"linkAnd":null},{"ordering":3,"name":"신한은행 앱 바로가기","linkType":"AL","linkTypeName":"앱링크","linkMo":null,"linkPc":null,"linkIos":"%변수10%","linkAnd":"%변수11%"},{"ordering":4,"name":"신한은행 S 알리미 바로가기","linkType":"AL","linkTypeName":"앱링크","linkMo":null,"linkPc":null,"linkIos":"%변수12%","linkAnd":"%변수13%"}]','Y')
,(25,'admin','청구금액 알림톡 01',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171120','U',1,'청구금액 알림톡 01 %변수1% 고객님, 이번달 납부금액은 %변수2% 입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000003','R','REG','[{"ordering":1,"name":"홈페이지바로가기","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://m.shinhan.com","linkPc":"http://www.shinhan.com","linkIos":null,"linkAnd":null}]','Y')
,(26,'admin','신한은행 신규 알림톡 공지',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','171120','U',1,'안녕하세요 %변수1%고객님.
신한은행에서 알림톡서비스를 %변수2%일부터 시작하오니 많은 관심 부탁 드립니다.
',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000001','R','REG','[{"ordering":1,"name":"신한은행홈페이지 바로가기","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://m.shinhan.com","linkPc":"http://www.shinhan.com","linkIos":null,"linkAnd":null}]','Y')
,(27,'admin','신한은행 미리보기전용 템플릿',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','170739','U',1,'신한은행 메시지 발송 테스트용 알림톡입니다.
%변수1%%변수2%%변수3%%변수4%%변수5%
%변수6%',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000010','R','REG',NULL,'Y')
,(28,'admin','템플릿2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20171027','151022','U',1,'안녕하세요. 엠앤와이즈 입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHB0000000009','R','REG',NULL,'Y')
,(29,'admin','시스템 점검 담당자알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170921','213036','U',1,'#{이름}님은 금일 시스템 점검 담당이시므로
퇴근전까지 점검일지를 작성하여 주시기 바랍니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST20170921','R','REG','[{"ordering":1,"name":"홈페이지","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://www.mnwise.com","linkPc":null,"linkIos":null,"linkAnd":null},{"ordering":2,"name":"점검바로가기","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://www.mnwise.com/daily_secu","linkPc":null,"linkIos":null,"linkAnd":null}]','Y')
,(30,'admin','템플릿등록 테스트',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170419','184722','U',1,'#{홍길동}님 안녕하세요. 테스트 입니다.
',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','TEST_011','A','APR',NULL,'Y')
;
INSERT INTO NVMOBILECONTENTS (CONTS_NO,USER_ID,CONTS_NM,CONTS_DESC,FILE_PATH,FILE_NAME,IMAGE_URL,DETOUR_FILE_PATH,DETOUR_FILE_NAME,DETOUR_PREVIEW_PATH,DETOUR_PREVIEW_NAME,FILE_TYPE,CREATE_DT,CREATE_TM,AUTH_TYPE,TAG_NO,CONTS_TXT,FILE_PREVIEW_PATH,FILE_PREVIEW_NAME,GRP_CD,CATEGORY_CD,FILE_SIZE,KAKAO_SENDER_KEY,KAKAO_TMPL_CD,KAKAO_TMPL_STATUS,KAKAO_INSP_STATUS,KAKAO_BUTTONS,USE_YN) VALUES 
(31,'admin','테스트02',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161026','100926','U',1,'생성 테스트',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST0003','R','APR',NULL,'Y')
,(32,'admin','SHTEST0004',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170717','114203','U',1,'TEST 템플릿 등록입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST0004','A','APR',NULL,'Y')
,(33,'admin','청구내역 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170720','150356','U',1,'[#{기업명} 청구서 도착 알림]

#{고객명}님 안녕하세요. 고객님이 이용하신 #{년월} 월 분의 이용 금액을 전달드립니다.
#{년월} 월 이용금액 바로확인 ▶ #{고객URL}

청구금이 실제 사용분과 차이가 있을경우 하기 담당자에게 문의주시면 신속히 처리해 드리겠습니다.
담당자 : #{고객담당자} #{담당자연락처}

감사합니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','test_20170720','R','APR',NULL,'Y')
,(34,'admin','보안일일결산',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170718','165848','U',1,'금일 사무실 보안일일결산 항목을 체크하셔야 합니다.
다음 링크를 참고하여 빠짐없이 체크 바랍니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MW17071801','A','APR','[{"ordering":1,"name":"회사 홈페이지","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://www.mnwise.com","linkPc":"http://www.mnwise.com","linkIos":null,"linkAnd":null},{"ordering":2,"name":"체크완료 했습니다","linkType":"BK","linkTypeName":"봇키워드","linkMo":null,"linkPc":null,"linkIos":null,"linkAnd":null}]','Y')
,(35,'admin','템플릿등록테스트',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170330','160006','U',1,'#{홍길동}님 안녕하세요. 테스트 입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','TEST_001','A','APR','[{"ordering":1,"name":"엠앤와이즈","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://mnwise.com
","linkPc":null,"linkIos":null,"linkAnd":null}]','Y')
,(36,'admin','라이나생명 신규부서 템플릿 테스트 요청건_20170421',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170421','170059','U',1,'[라이나생명] #엠앤와이즈 테스트템플릿

안녕하세요. #{고객명}님
가입하신 #{상품명} 치아보험에서는 아래와 같은 덴탈 헬스케어 서비스를 제공하여 드립니다.

1. ''콕닥 덴탈'' 병원 예약 서비스
해외에서 아프거나 다쳤을 때 이용하실 수 있는 서비스입니다.
150개국 11,000 개 병원 네트워크를 활용하여 믿을 수 있는 병원을 찾아 예약해 드립니다.
치과 병원도 포함되어 있습니다.

2. 진료 통역 서비스
한국인 전담 간호사가 통역 서비스(영어)도 제공합니다.

3. 중국 병원 예약 할인 서비스
중국 병원의 경우 5%~40%의 할인을 제공합니다.
예약 후 모바일 멤버십 카드를 발송하여 드리니 병원에 제시하여 주세요.

4. 치아 관련 건강 컨텐츠 제공
''스케일링 후 먹으면 안 되는 음식은?'', ''임플란트 후 발생할 수 있는 대표 부작용은?'' 과 같은 치아 관련 유용한 내용들을 읽어 보실 수 있습니다.

콕닥 덴탈 서비스 주소
#{URL}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','test_20170421','R','APR',NULL,'Y')
,(37,'admin','버튼테스트',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20170216','203956','U',1,'버튼 테스트 문자입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','test001','A','APR','[{"ordering":1,"name":"엠앤와이즈","linkType":"WL","linkTypeName":"웹링크","linkMo":"http://www.mnwise.com/","linkPc":null,"linkIos":null,"linkAnd":null}]','Y')
,(38,'admin','위비test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161026','095803','U',1,'우리은행 위비톡 테스트입니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST0002','R','APR',NULL,'Y')
,(39,'admin','특근 접수 확인',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160802','121621','U',1,'#{신청월일}의 특근 승인이 완료되었습니다.
항상 건강 유념하시고 행복하세요.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZIGISH005','R','APR',NULL,'Y')
,(40,'admin','사내 알림톡 안내',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160802','112638','U',1,'사내 알림톡 기능이 강화되었습니다.
엠지기 접속하시어 추가된 기능을 확인하여 업무에 참고하시기 바랍니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZIGISH001','R','APR',NULL,'Y')
;
INSERT INTO NVMOBILECONTENTS (CONTS_NO,USER_ID,CONTS_NM,CONTS_DESC,FILE_PATH,FILE_NAME,IMAGE_URL,DETOUR_FILE_PATH,DETOUR_FILE_NAME,DETOUR_PREVIEW_PATH,DETOUR_PREVIEW_NAME,FILE_TYPE,CREATE_DT,CREATE_TM,AUTH_TYPE,TAG_NO,CONTS_TXT,FILE_PREVIEW_PATH,FILE_PREVIEW_NAME,GRP_CD,CATEGORY_CD,FILE_SIZE,KAKAO_SENDER_KEY,KAKAO_TMPL_CD,KAKAO_TMPL_STATUS,KAKAO_INSP_STATUS,KAKAO_BUTTONS,USE_YN) VALUES 
(41,'admin','알림톡 확인 요청',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160823','202127','U',1,'알림톡 #{알림톡명}의 메세지 발송이 완료되었습니다.
해당 건수 확인하시어 업무에 참고 바랍니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST0031','R','APR',NULL,'Y')
,(42,'admin','템플릿 등록 확인',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160823','193856','U',1,'#{템플릿명} 템플릿이 등록되었습니다.
전산 담당자를 통해 해당 템플릿을 사용 할 수 있도록 요청하시기 바랍니다.
- 늘 힘이되는 메시지 친구 엠기지 드림.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST00030','R','APR',NULL,'Y')
,(43,'admin','템플릿 검수 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161013','163938','U',1,'알림톡 템플릿 검수 결과 안내
승인: #{승인건수}
반려: #{반려건수}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MWTPLALARM01','A','APR',NULL,'Y')
,(44,'admin','연체 부활 안내',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161021','182008','U',1,'[메트라이프 생명] 테스트용
안녕하세요.
고객님의 #{상품명} 보험이 보험료 연체로 인한 효력
상실이 되어 관련 안내드립니다.
그간 보험을 성실히 유지해 주신 고객님을 대상으로 한시적
으로 전화로 부활을 간편하게 처리해드리고 있습니다. 첨부드린 #{첨부내역} 보험 다시보기 자료 꼭 한번 확인하시고
간편 부활 관련해서 문의사항 있으시면  언제든 #{전화번호}로 전화주시기 바랍니다. 감사합니다.
보험연체 안내 주소
#{URL}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','metlife02','R','APR',NULL,'Y')
,(45,'admin','기능 개선 안내',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161021','182008','U',1,'[메트라이프 생명] 테스트용

안녕하세요. 계약유지관리팀입니다.
2016년 7월 1일 부터 보다 편리한 수금 관리를 위해 멧스퀘어와 MOS 기능을 개선하였습니다. 자세한 내용은 공지내용을 참고 부탁드립니다.
공지페이지 연결
#{URL}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','metlife03','A','APR',NULL,'Y')
,(46,'admin','휴가 결과 안내',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160818','164215','U',1,'#{휴가신청일}의 휴가 승인이 완료되었습니다. 즐거운 휴가되세요.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZIGISH007','A','APR',NULL,'Y')
,(47,'admin','연구소 도서 구매 알림',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20161014','155130','U',1,'연구소에서 도서 구매를 요청하였습니다.
검토후 승인 바랍니다.

신청일시: #{신청일시}
신청자: #{신청자명}
도서명: #{도서명}
출판사: #{출판사}
가격: #{가격}',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','SHTEST0001','R','APR',NULL,'Y')
,(48,'admin','영업지원시스템 OTP 문자 인증',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160930','130710','U',1,'[메트라이프생명] 인증번호 [#{인증번호}] 입니다. 정확히 입력해주세요.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','metlife01','A','APR',NULL,'Y')
,(49,'admin','경비정산 확인',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160802','121621','U',1,'요청하신 #{신청월}의 경비가 지급처리 되었습니다.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZIGISH004','A','APR',NULL,'Y')
,(50,'admin','알림톡 발송 안내',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'A','20160802','112913','U',1,'[알림톡 발송 안내]
요청하신 메세지 [#{메세지명}] 가 발송처리 되었습니다.
#{발송일시} : #{발송건수}
항상 행복하세요.',NULL,NULL,'01',NULL,0,'10df82a7d4d10a7417a2a5d60aff9664f0da9f4f','MZIGISH003','A','APR',NULL,'Y')
;