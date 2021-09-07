package com.mnwise.wiseu.web.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.common.model.CdMstVo;
import com.mnwise.wiseu.web.common.model.SendErrVo;

/**
 * NV_CD_MST 테이블 DAO 클래스
 */
@Repository
public class CdMstDao extends BaseDao {
    public int insertCdMst(CdMstVo cdMst) {
        return insert("CdMst.insertCdMst", cdMst);
    }

    /**
     * PUSH MSG_TYPE 추가
     * @param cd
     * @param cd_desc
     * @param curr_date
     * @param lang
     * @return
     */
    public int insertPushMsgType(Map<String, Object> paramMap) {
        return insert("CdMst.insertPushMsgType", paramMap);
    }

    public int updateCdMstByPk(CdMstVo cdMst) {
        return update("CdMst.updateCdMstByPk", cdMst);
    }

    /**
     * PUSH MSG_TYPE 수정
     * @param cd
     * @param cd_desc
     * @param curr_date
     * @param use_yn
     * @return
     */
    public int updatePushMsgType(Map<String, Object> paramMap) {
        return update("CdMst.updatePushMsgType", paramMap);
    }

    public int deleteCdMstByPk(String cdCat, String cd, String lang) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cdCat", cdCat);
        paramMap.put("cd", cd);
        paramMap.put("lang", lang);
        return delete("CdMst.deleteCdMstByPk", paramMap);
    }

    public CdMstVo selectCdMstByPk(String cdCat, String cd, String lang) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cdCat", cdCat);
        paramMap.put("cd", cd);
        paramMap.put("lang", lang);
        return (CdMstVo) selectOne("CdMst.selectCdMstByPk", paramMap);
    }

    /**
     * 코드 리스트
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CdMstVo> selectCodeList(String parCdCat) {
        return selectList("CdMst.selectCodeList", parCdCat);
    }

    /**
     * 공통 코드 마스터에서 cd_cat 코드값으로 목록을 조회한다.
     *
     * @param cdCat 코드 카테고리
     * @param lang 사용 언어
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CdMstVo> getCdMstList(String cdCat, String lang) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("cdCat", cdCat);
        paramMap.put("lang", lang);
        return selectList("CdMst.getCdMstList", paramMap);
    }

    /**
     * PUSH 사용 코드 정보
     * @param treeElement
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> selectCodeGroup(String parCdCat) {
        return selectList("CdMst.selectCodeGroup", parCdCat);
    }

    /**
     * PUSH MSG_TYPE (메시지유형) 목록
     * @param useOnly
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CaseInsensitiveMap> selectPushMsgTypeList(boolean useOnly) {
        // useOnly가 false일 경우 모든 데이터를 가져오기 위해 파라미터 공백 전달
        return selectList("CdMst.selectPushMsgTypeList", useOnly ? "y" : "");
    }

    /**
     * SMS 코드 정보를 불러온다 (캠페인 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendErrVo> selectSMSSendErrList() {
        return selectList("CdMst.selectSMSSendErrList");
    }

    /**
     * MMS/LMS 코드 정보를 불러온다 (캠페인 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendErrVo> selectMMSSendErrList() {
        return selectList("CdMst.selectMMSSendErrList");
    }

    /**
     * FAX 코드 정보를 불러온다 (캠페인 고객발송 내역의 발송결과 콤보박스에서 사용)
     *
     * @param sendErrVo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendErrVo> selectFAXSendErrList() {
        return selectList("CdMst.selectFAXSendErrList");
    }

    /**
     * 카카오 ERROR 코드 리스트
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendErrVo> getKakaoSendErrList(String channel) {
        SendErrVo param = new SendErrVo();
        param.setChannelType(channel);
        return selectList("CdMst.selectKakaoSendErrList", param);
    }

    /**
     * PUSH ERROR 코드 리스트
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SendErrVo> getPushSendErrCodeList() {
        return selectList("CdMst.selectPushErrList");
    }

    /**
     * 알림톡 템플릿 검수 상태 코드와 코드명이 담긴 Map 객체 반환
     *
     * @return
     */
    public CaseInsensitiveMap findKakaoTemplateInspStatusNameMap() {
        return selectMap("CdMst.selectAlimtalkTemplateInspStatusCode", null, "cd", "val");
    }

    /**
     * 알림톡 템플릿 상태 코드와 코드명이 담긴 Map 객체 반환
     *
     * @return
     */
    public CaseInsensitiveMap findKakaoTemplateStatusNameMap() {
        return selectMap("CdMst.selectAlimtalkTemplateStatusCode", null, "cd", "val");
    }

    /**
     * 알림톡 템플릿 코멘트 상태 코드와 코드명이 담긴 Map 객체 반환
     *
     * @return
     */
    public CaseInsensitiveMap findKakaoTemplateCommentStatusNameMap() {
        return selectMap("CdMst.selectAlimtalkTemplateCommentStatusCode", null, "cd", "val");
    }

    /**
     * 알림톡 템플릿 카테고리 그룹 코드와 코드명 조회
     *
     * @return
     */
    public List<CaseInsensitiveMap> selectAlimtalkTemplateCategoryGroupList() {
        return selectList("CdMst.selectAlimtalkTemplateCategoryGroupList");
    }

    /**
     * 알림톡 템플릿 카테고리 코드와 코디명 조회
     *
     * @return
     * @param categoryGroup
     */
    public List<CaseInsensitiveMap> selectAlimtalkTemplateCategoryCdList(String categoryGroup) {
        return selectList("CdMst.selectAlimtalkTemplateCategoryCdList", categoryGroup);
    }
}
