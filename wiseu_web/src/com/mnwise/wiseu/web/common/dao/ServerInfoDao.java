package com.mnwise.wiseu.web.common.dao;

import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.editor.model.ServerInfoVo;

/**
 * NVSERVERINFO 테이블 DAO 클래스
 */
@Repository
public class ServerInfoDao extends BaseDao {
    public int insertServerInfo(ServerInfoVo serverInfo) {
        return insert("ServerInfo.insertServerInfo", serverInfo);
    }

    public int updateServerInfoByPk(ServerInfoVo serverInfo) {
        return update("ServerInfo.updateServerInfoByPk", serverInfo);
    }
    /**
     * 환경 변수를 업데이트 한다.
     *
     * @param envServerInfoVo
     */
    public int updateEnvServerInfo(ServerInfoVo serverInfo) {
        return update("ServerInfo.updateEnvServerInfo", serverInfo);
    }


    public int deleteServerInfoByPk(String hostNm) {
        return delete("ServerInfo.deleteServerInfoByPk", hostNm);
    }

    public ServerInfoVo selectServerInfoByPk(String hostNm) {
        return (ServerInfoVo) selectOne("ServerInfo.selectServerInfoByPk", hostNm);
    }

    /**
     * 환경설정의 NVSERVERINFO 테이블 정보를 조회한다.
     */
    public ServerInfoVo selectServerInfo() {
        return (ServerInfoVo) selectOne("ServerInfo.selectServerInfo");
    }

    /**
     * 재발송 에러코드 반환(재발송에서 제외)
     * @return
     */
    public String getResendErrorCd() {
        return (String) selectOne("ServerInfo.selectResendErrorCd");
    }

    /**
     * NVSERVERINFO 테이블 KAKAO_TEMPALTE_LAST_SYNC_DTM 컬럼 SELECT
     *
     * @return
     */
    public String findKakaoTemplateLastSyncDtm() {
        return (String) selectOne("ServerInfo.selectKakaoTemplateLastSyncDtm");
    }

    public String getResendMailErrorCd() {
        return (String) selectOne("ServerInfo.selectResendMailErrorCd");
    }

    public String getResendFaxErrorCd() {
        return (String) selectOne("ServerInfo.selectResendFaxErrorCd");
    }

    public String getResendSmsErrorCd() {
        return (String) selectOne("ServerInfo.selectResendSmsErrorCd");
    }

    public String getResendAltalkErrorCd() {
        return (String) selectOne("ServerInfo.selectResendAltalkErrorCd");
    }

    public String getResendFRTErrorCd() {
        return (String) selectOne("ServerInfo.selectResendFRTErrorCd");
    }

}