package com.mnwise.wiseu.web.channel.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import com.mnwise.wiseu.web.base.BaseDao;
import com.mnwise.wiseu.web.base.Const;

/**
 * NVPUSHSERVICEINFO 테이블 DAO 클래스
 */
@Repository
public class PushServiceInfoDao extends BaseDao {

    /**
     * PUSH SERVICE 정보 추가
     *
     * @param svcType 이케어, 캠페인 구분(참고: {@link Const})
     * @param svcNo 서비스 번호(캠페인 또는 이케어 번호)
     * @param pushAppId PUSH 앱 ID
     * @param pushMsgType
     * @param pushMenuId
     * @param pushPopImgUse
     * @param pushImgUrl
     * @param pushPopBigImgUse
     * @param pushBigImgUrl
     * @param pushWebUrl
     * @return
     */
    public int insertPushServiceInfo(String svcType, int svcNo, String pushAppId, String pushMsgType, String pushMenuId, String pushPopImgUse, String pushImgUrl, String pushPopBigImgUse,
        String pushBigImgUrl, String pushWebUrl) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("svcType", svcType);
        paramMap.put("svcNo", svcNo);
        paramMap.put("pushAppId", pushAppId);
        paramMap.put("pushMsgType", pushMsgType);
        paramMap.put("pushMenuId", pushMenuId);
        paramMap.put("pushPopImgUse", pushPopImgUse);
        paramMap.put("pushImgUrl", "Y".equals(pushPopImgUse) ? pushImgUrl : "");
        paramMap.put("pushPopBigImgUse", pushPopBigImgUse);
        paramMap.put("pushBigImgUrl", "Y".equals(pushPopBigImgUse) ? pushBigImgUrl : "");
        paramMap.put("pushClickLink", pushWebUrl);

        return insert("PushServiceInfo.insertPushServiceInfo", paramMap);
    }

    public int copyPushServiceInfo(Map<String, Object> paramMap) {
        return insert("PushServiceInfo.copyPushServiceInfo", paramMap);
    }

    /**
     * PUSH SERVICE 정보 변경
     *
     * @param svcType 이케어, 캠페인 구분(참고: {@link Const})
     * @param svcNo 서비스 번호(캠페인 또는 이케어 번호)
     * @param pushAppId PUSH 앱 ID
     * @param pushMsgType
     * @param pushMenuId
     * @param pushPopImgUse
     * @param pushImgUrl
     * @param pushPopBigImgUse
     * @param pushBigImgUrl
     * @param pushWebUrl
     * @return
     */
    public int updatePushServiceInfo(String svcType, int svcNo, String pushAppId, String pushMsgType, String pushMenuId, String pushPopImgUse, String pushImgUrl, String pushPopBigImgUse, String pushBigImgUrl, String pushWebUrl) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("svcType", svcType);
        paramMap.put("svcNo", svcNo);
        paramMap.put("pushAppId", pushAppId);
        paramMap.put("pushMsgType", pushMsgType);
        paramMap.put("pushMenuId", pushMenuId);
        paramMap.put("pushPopImgUse", pushPopImgUse);
        paramMap.put("pushImgUrl", "Y".equals(pushPopImgUse) ? pushImgUrl : "");
        paramMap.put("pushPopBigImgUse", pushPopBigImgUse);
        paramMap.put("pushBigImgUrl", "Y".equals(pushPopBigImgUse) ? pushBigImgUrl : "");
        paramMap.put("pushClickLink", pushWebUrl);

        return update("PushServiceInfo.updatePushServiceInfo", paramMap);
    }

    public int deletePushServiceInfoByPk(String svcType, int svcNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("svcType", svcType);
        paramMap.put("svcNo", svcNo);
        return delete("PushServiceInfo.deletePushServiceInfoByPk", paramMap);
    }

    /**
     * PUSH SERVICE 정보.
     *
     * @param svcType 이케어, 캠페인 구분(참고: {@link Const})
     * @param svcNo 서비스 번호(캠페인 또는 이케어 번호)
     * @return PUSH 서비스 추가 정보 맵
     */
    public CaseInsensitiveMap selectPushServiceInfo(String svcType, int svcNo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("svcType", svcType);
        paramMap.put("svcNo", svcNo);
        return (CaseInsensitiveMap) selectOne("PushServiceInfo.selectPushServiceInfo", paramMap);
    }

}