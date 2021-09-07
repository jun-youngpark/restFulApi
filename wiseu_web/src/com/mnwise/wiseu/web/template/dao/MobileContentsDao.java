package com.mnwise.wiseu.web.template.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.editor.model.MmsVo;
import com.mnwise.wiseu.web.editor.model.SmsVo;
import com.mnwise.wiseu.web.template.model.BrandtalkTemplateVo;
import com.mnwise.wiseu.web.template.model.MobileVo;

/**
 * NVMOBILECONTENTS 테이블 DAO 클래스
 */
@Repository
public class MobileContentsDao extends BaseDao {
    public int insertMobileContents(MobileVo mobileContents) {
        return insert("MobileContents.insertMobileContents", mobileContents);
    }

    /**
     * 템플릿 데이터 입력
     * @param BrandtalkTemplateVo
     */
    public int insertBrandTalkContents(BrandtalkTemplateVo brandtalkTemplateVo) {
        return insert("MobileContents.insertBrandTalkContents", brandtalkTemplateVo);
    }

    public int updateMobileContentsByPk(MobileVo mobileContents) {
        return update("MobileContents.updateMobileContentsByPk", mobileContents);
    }

    /**
     * NVMOBILECONTENTS 테이블에 KAKAO_SENDER_KEY, KAKAO_TMPL_CD 컬럼을 이용하여 UPDATE
     *
     * @param vo
     * @return
     */
    public int updateKakaoTemplate(MobileVo vo) {
        return update("MobileContents.updateKakaoTemplate", vo);
    }

    /**
     * NVMOBILECONTENTS 테이블에 KAKAO_SENDER_KEY, KAKAO_TMPL_CD 컬럼을 이용하여 UPDATE
     *
     * @param vo
     * @return
     */
    public int updateKakaoTemplateAuthType(MobileVo mobileVo) {
        return update("MobileContents.updateKakaoTemplateAuthType", mobileVo);
    }

    public int modifyMobileContents(MobileVo mobileVo) {
        return update("MobileContents.modifyMobileContents", mobileVo);
    }

    /**
     * 템플릿 수정
     * @param BrandtalkTemplateVo
     */
    public int updateBrandTalkTemplate(BrandtalkTemplateVo brandtalkTemplateVo) {
        return update("MobileContents.updateBrandTalkTemplate", brandtalkTemplateVo);
    }

    /**
     * 템플릿 삭제
     * @param BrandtalkTemplateVo
     */
    public int updateUseYnToN(BrandtalkTemplateVo brandtalkTemplateVo) {
        return update("MobileContents.updateUseYnToN", brandtalkTemplateVo);
    }

    public int deleteMobileContentsByPk(int contsNo) {
        return delete("MobileContents.deleteMobileContentsByPk", contsNo);
    }

    /**
     * 발신 프로필 키와 템플릿 코드가 일치하는 알림톡 템플릿을 삭제한다.
     *
     * @param mobileVo
     * @return
     */
    public int deleteAlimtalkTemplate(MobileVo mobileVo) {
        return delete("MobileContents.deleteAlimtalkTemplate", mobileVo);
    }

    public MobileVo selectMobileContentsByPk(int contsNo) {
        return (MobileVo) selectOne("MobileContents.selectMobileContentsByPk", contsNo);
    }

    public MobileVo getMobileContentsInfo(int contsNo) {
        MobileVo tmp = (MobileVo) selectOne("MobileContents.selectMobileInfo", contsNo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    public int selectNextContsNo() {
        return (Integer) selectOne("MobileContents.selectNextContsNo");
    }

    public String getCampaignKakaoImageUrl(int serviceNo) {
        Object object = selectOne("MobileContents.getCampaignKakaoImageUrl", serviceNo);
        //데이터가 없을 경우 처리
        return (object == null) ? "" : object.toString();
    }

    public String getEcareKakaoImageUrl(int serviceNo) {
        Object object = selectOne("MobileContents.getEcareKakaoImageUrl", serviceNo);
        return (object == null) ? "" : object.toString();
    }

    public String getFriendtalkImagePath(String contsNo) {
        return (String) selectOne("MobileContents.getFriendtalkImagePath", contsNo);
    }

    public MobileVo getKakaoImageUrl(String contsNo) {
        return (MobileVo) selectOne("MobileContents.getKakaoImageUrl", contsNo);
    }

    public String selectEditorMmsContentsFilePreviewPath(String filePath) {
        return (String) selectOne("MobileContents.selectEditorMmsContentsFilePreviewPath", filePath);
    }

    /**
     * 모바일템플릿 리스트 count
     *
     * @param mobileVo
     * @return
     */
    public int selectEditorMobileContentsListMax(MobileVo mobileVo) {
        Integer count = (Integer) selectOne("MobileContents.selectMobileContentsListMax", mobileVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 모바일템플릿 리스트
     *
     * @param mobileVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MobileVo> selectEditorMobileContentsList(MobileVo mobileVo) {
        return selectList("MobileContents.selectMobileContentsList", mobileVo);
    }

    public int selectEditorMmsContentsListMax(MmsVo mmsVo) {
        Integer count = (Integer) selectOne("MobileContents.selectEditorMmsContentsListMax", mmsVo);
        return (count == null) ? 0 : count;
    }

    /**
     * MMS 컨텐츠 목록
     */
    @SuppressWarnings("unchecked")
    public List<MmsVo> selectEditorMmsContentsList(MmsVo mmsVo) {
        return selectList("MobileContents.selectEditorMmsContentsList", mmsVo);
    }

    public int selectEditorSmsContentsListMax(SmsVo smsVo) {
        Integer count = (Integer) selectOne("MobileContents.selectEditorSmsContentsListMax", smsVo);
        return (count == null) ? 0 : count;
    }

    /**
     * SMS 컨텐츠 목록
     */
    @SuppressWarnings("unchecked")
    public List<SmsVo> selectEditorSmsContentsList(SmsVo smsVo) {
        return selectList("MobileContents.selectEditorSmsContentsList", smsVo);
    }

    public int selectKakaoContentsListMax(MobileVo kakaoVo) {
        Integer count = (Integer) selectOne("MobileContents.selectKakaoContentsListMax", kakaoVo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<MobileVo> selectKakaoContentsList(MobileVo kakaoVo) {
        return selectList("MobileContents.selectKakaoContentsList", kakaoVo);
    }

    public MobileVo selectKakaoContent(MobileVo kakaoVo) {
        return (MobileVo) selectOne("MobileContents.selectKakaoContent", kakaoVo);
    }

    public int getMobileContentTotalCount(MobileVo mobileVo) {
        Integer count = (Integer) selectOne("MobileContents.selectMobileTotalCount", mobileVo);
        return (count == null) ? 0 : count;
    }

    @SuppressWarnings("unchecked")
    public List<MobileVo> getMobileList(MobileVo mobileVo) {
        List<MobileVo> tmp = (List<MobileVo>) selectList("MobileContents.selectMobileContentList", mobileVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    @SuppressWarnings("unchecked")
    public List<MobileVo> getMobileList(Map<String, Object> map) {
        return selectList("MobileContents.selectMobileContentListForEditor", map);
    }

    public MobileVo selectKakaoTemplate(MobileVo mobileVo) {
        return (MobileVo) selectOne("MobileContents.selectKakaoTemplate", mobileVo);
    }

    /**
     * 사용자의 알림톡 템플릿 중에 검수 상태가 등록(REG)인 것을 조회
     *
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MobileVo> findRegStatusTemplate(String userId) {
        return selectList("MobileContents.selectRegStatusTemplate", userId);
    }

    /**
     * 알림톡 템플릿 정보 반환
     *
     * @param mobileVo
     * @return
     */
    public MobileVo findAlimtalkTemplateInfo(MobileVo mobileVo) {
        MobileVo tmp =  (MobileVo) selectOne("MobileContents.selectAlimtalkTemplateInfo", mobileVo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 알림톡 템플릿 건수 확인
     *
     * @param mobileVo
     * @return
     */
    public int getAlimtalkContentTotalCount(MobileVo mobileVo) {
        Integer count = (Integer) selectOne("MobileContents.selectAlimtalkTotalCount", mobileVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 알림톡 템플릿 조회
     *
     * @param mobileVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MobileVo> getAlimtalkTemplateList(MobileVo mobileVo) {
        List<MobileVo> tmp = (List<MobileVo>) selectList("MobileContents.selectAlimtalkContentList", mobileVo);
        security.securityObjectList(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 알림톡 리스트 화면에서 선택한 템플릿 정보를 조회한다.
     *
     * @param mobileVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MobileVo> findAlimtalkTemplates(MobileVo mobileVo) {
        return selectList("MobileContents.selectAlimtalkTemplates", mobileVo);
    }

    /**
     * 템플릿 단건 조회
     * @param BrandtalkTemplateVo
     * @return BrandtalkTemplateVo
     */
    public BrandtalkTemplateVo selectBrandTalkContentInfo(BrandtalkTemplateVo brandtalkTemplateVo) {
        BrandtalkTemplateVo tmp = (BrandtalkTemplateVo) selectOne("MobileContents.selectBrandTalkContentInfo", brandtalkTemplateVo);
        security.securityObject(tmp, "DECRYPT");
        return tmp;
    }

    /**
     * 템플릿 목록 Count 조회
     * @param BrandtalkTemplateVo
     * @return int
     */
    public int selectBrandTalkContentListCount(BrandtalkTemplateVo brandtalkTemplateVo) {
        Integer count = (Integer) selectOne("MobileContents.selectBrandTalkContentListCount", brandtalkTemplateVo);
        return (count == null) ? 0 : count;
    }

    /**
     * 템플릿 목록 조회
     * @param BrandtalkTemplateVo
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<BrandtalkTemplateVo> selectBrandTalkContentList(BrandtalkTemplateVo brandtalkTemplateVo) {
        return selectList("MobileContents.selectBrandTalkContentList", brandtalkTemplateVo);
    }

}