package com.mnwise.wiseu.web.rest.dao.contents;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.rest.model.custom.CustomContents;
import com.mnwise.wiseu.web.rest.parent.BaseDao;


@Repository
public class ContentsDao extends BaseDao {

    /**
	 * 템플릿 등록(이메일)
	 */
    public int insertTemplateMail(CustomContents customContents) throws Exception{
		return insert("Contents.insertContents", customContents);

    }
	/**
	 * 템플릿 등록(문자)
	 */
    public int insertTemplateMassage(CustomContents customContents) throws Exception{
		return insert("Contents.insertTemplateMassage", customContents);
    }
	/**
	 * 템플릿 등록(알림톡)
	 */
    public int insertMobileContents(CustomContents customContents) throws Exception{
		return insert("Contents.insertMobileContents", customContents);

    }
    /**
	 * 템플릿 수정(이메일)
	 */
    public int updateTemplateMail(CustomContents customContents) throws Exception{
		return update("Contents.updateTemplateMail", customContents);

    }
	/**
	 * 템플릿 수정(알림톡)
	 */
    public int updateKakaoTemplate(CustomContents customContents) throws Exception{
		return update("Contents.updateKakaoTemplate", customContents);

    }
	/**
	 * 템플릿 수정(메시지)
	 */
    public int updateMobileTemplate(CustomContents customContents) throws Exception{
		return update("Contents.updateMobileTemplate", customContents);

    }
	/**
	 * 템플릿 삭제(이메일)
	 */
    public int deleteContentsByPk(CustomContents customContents) throws Exception{
		return delete("Contents.deleteContentsByPk", customContents);

    }
	/**
	 * 템플릿 삭제(메시지)
	 */
    public int deleteMobileContentsByPk(CustomContents customContents) throws Exception{
		return delete("Contents.deleteMobileContentsByPk", customContents);

    }
	/**
	 * 템플릿 삭제(알림톡)
	 */
    public int deleteAlimtalkTemplate(CustomContents customContents) throws Exception{
		return delete("Contents.deleteAlimtalkTemplate", customContents);

    }
	/**
	 * 템플릿 조회(이메일)
	 */
    public List<CustomContents> selectTemplateMail(CustomContents customContents) throws Exception{
		return (List<CustomContents>) selectList("Contents.selectTemplateMail", customContents);

    }
	/**
	 * 템플릿 조회(메시지)
	 */
    public List<CustomContents> selectTemplateMassage(CustomContents customContents) throws Exception{
		return (List<CustomContents>) selectList("Contents.selectTemplateMassage", customContents);

    }
	/**
	 * 템플릿 조회(알림톡)
	 */
    public List<CustomContents> selectTemplateAlimTalk(CustomContents customContents) throws Exception{
		return (List<CustomContents>) selectList("Contents.selectTemplateAlimTalk", customContents);

    }
    /**
     * 알림톡 템플릿 정보 반환
     *
     */
    public CustomContents findAlimtalkTemplateInfo(CustomContents customContents) {
    	CustomContents tmp =  (CustomContents) selectOne("Contents.selectAlimtalkTemplateInfo", customContents);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }
    public String selectNextContsNo() {
    	return selectOne("Contents.selectNextContsNo").toString();
    }
    public String selectNextMobileContsNo() {
    	return selectOne("Contents.selectNextMobileContsNo").toString();
    }
    public String selectAlimtalkInspect(CustomContents customContents) {
    	String tmp =  (String) selectOne("Contents.selectAlimtalkInspect", customContents);
        return tmp;
    }
    public int selectTemplateCount(CustomContents customContents) {
    	int tmp =  (int) selectOne("Contents.selectTemplateCount", customContents);
        return tmp;
    }
    public int selectMobileTemplateCount(CustomContents customContents) {
    	int tmp =  (int) selectOne("Contents.selectMobileTemplateCount", customContents);
        return tmp;
    }
    public int selectAilmTalkTemplateCount(CustomContents customContents) {
    	int tmp =  (int) selectOne("Contents.selectAilmTalkTemplateCount", customContents);
        return tmp;
    }
    

}