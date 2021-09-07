package com.mnwise.wiseu.web.template.model;

/**
 * NVKAKAOPROFILE 테이블 모델 클래스
 */
public class KakaoProfile {
    private String userId;
    private String kakaoSenderKey;
    private String kakaoYellowId;
    private String lastSyncDtm;

    // 추가 멤버변수

    // 생성자
    /**
     * 기본 생성자
     */
    public KakaoProfile() {
        super();
    }

    // 기본 getter/setter
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKakaoSenderKey() {
        return this.kakaoSenderKey;
    }

    public void setKakaoSenderKey(String kakaoSenderKey) {
        this.kakaoSenderKey = kakaoSenderKey;
    }

    public String getKakaoYellowId() {
        return this.kakaoYellowId;
    }

    public void setKakaoYellowId(String kakaoYellowId) {
        this.kakaoYellowId = kakaoYellowId;
    }

    public String getLastSyncDtm() {
        return this.lastSyncDtm;
    }

    public void setLastSyncDtm(String lastSyncDtm) {
        this.lastSyncDtm = lastSyncDtm;
    }

    // 추가 getter/setter

}
