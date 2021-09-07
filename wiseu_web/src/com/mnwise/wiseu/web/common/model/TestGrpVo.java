package com.mnwise.wiseu.web.common.model;

import java.io.Serializable;

/**
 * NV_TEST_GRP 테이블 모델 클래스
 */
public class TestGrpVo implements Serializable {
    private static final long serialVersionUID = -1L;
    private int testGrpCd;
    private String testGrpNm;
    private int testSupragrpCd;
    private String userId;
    private String activeYn;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String testSupragrpNm;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getTestGrpCd() {
        return this.testGrpCd;
    }

    public void setTestGrpCd(int testGrpCd) {
        this.testGrpCd = testGrpCd;
    }

    public String getTestGrpNm() {
        return this.testGrpNm;
    }

    public void setTestGrpNm(String testGrpNm) {
        this.testGrpNm = testGrpNm;
    }

    public int getTestSupragrpCd() {
        return this.testSupragrpCd;
    }

    public void setTestSupragrpCd(int testSupragrpCd) {
        this.testSupragrpCd = testSupragrpCd;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActiveYn() {
        return this.activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getTestSupragrpNm() {
        return testSupragrpNm;
    }

    public void setTestSupragrpNm(String testSupragrpNm) {
        this.testSupragrpNm = testSupragrpNm;
    }
}
