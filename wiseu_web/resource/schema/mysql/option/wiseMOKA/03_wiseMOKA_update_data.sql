-- 캠페인
update nvmenu set active_yn = 'N' where menu_cd = '01';
update nvmenu set active_yn = 'N' where menu_cd = '0102';
update nvmenu set active_yn = 'N' where menu_cd = '0103';
update nvmenu set active_yn = 'N' where menu_cd = '0104';

-- 컨텐츠 변경이력
update nvmenu set active_yn = 'N' where menu_cd = '0204';

-- 리포트
update nvmenu set active_yn = 'N' where menu_cd = '0301';
update nvmenu set active_yn = 'N' where menu_cd = '030101';
update nvmenu set active_yn = 'N' where menu_cd = '030105';
update nvmenu set active_yn = 'N' where menu_cd = '030106';

-- 템플릿
update nvmenu set active_yn = 'N' where menu_cd = '0401';
update nvmenu set active_yn = 'N' where menu_cd = '0402';

update nvmenu set active_yn = 'N' where menu_cd = '0502';

update nvmenu set active_yn = 'N' where menu_cd = '0603';
update nvmenu set active_yn = 'N' where menu_cd = '0604';
update nvmenu set active_yn = 'N' where menu_cd = '0605';

update nvmenu set active_yn = 'N' where menu_cd = '0701';
update nvmenu set active_yn = 'N' where menu_cd = '0703';
update nvmenu set active_yn = 'N' where menu_cd = '0704';
update nvmenu set active_yn = 'N' where menu_cd = '0705';
update nvmenu set active_yn = 'N' where menu_cd = '0707';
update nvmenu set active_yn = 'N' where menu_cd = '0710';
update nvmenu set active_yn = 'N' where menu_cd = '0714';
update nvmenu set active_yn = 'N' where menu_cd = '0715';

-- 메뉴 활성화
update nvmenu set active_yn = 'Y' where menu_cd = '0205';
update nvmenu set active_yn = 'Y' where menu_cd = '0403';
update nvmenu set active_yn = 'Y' where menu_cd = '0404';

-- 리포트 메뉴의 기본 url을 이케어 리스트도 변경
update nvmenu set menu_link_url = '/report/ecare/ecareRptList.do' where menu_cd = '03';
update nvmenu set menu_link_url = '/template/mobileTemplateList.do' where menu_cd = '04';

-- 이케어 메뉴명을 메시지로 변경
update nvmenu_lang set menu_nm = '메시지' where menu_cd = '02' and lang = 'ko';
update nvmenu_lang set menu_nm = '메시지 리스트' where menu_cd = '0201' and   lang = 'ko';
update nvmenu_lang set menu_nm = '메시지 등록' where menu_cd = '0202' and   lang = 'ko';
update nvmenu_lang set menu_nm = '메시지 고객이력' where menu_cd = '0203' and   lang = 'ko';
update nvmenu_lang set menu_nm = '메시지' where menu_cd = '0302' and   lang = 'ko';
update nvmenu_lang set menu_nm = '메시지 리스트' where menu_cd = '030201' and   lang = 'ko';
update nvmenu_lang set menu_nm = '메시지이력관리' where menu_cd = '0713' and   lang = 'ko';