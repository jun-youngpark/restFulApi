package com.mnwise.wiseu.web.common.model;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.BaseVo;

/**
 * 태그 테이블(NVCAMPTAG, NVECAREMSGTAG, NVCONTENTSTAG, NVSEGMENTTAG, NVMOBILECONTENTSTAG) 공통 모델 클래스
 */
public class Tag extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int tagNo;
    private String tagNm;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int tagCnt;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getTagNo() {
        return tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public int getTagCnt() {
        return tagCnt;
    }

    public void setTagCnt(int tagCnt) {
        this.tagCnt = tagCnt;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getTagNmForList() {
        return StringUtil.abbreviate(tagNm, 5);
    }

}
