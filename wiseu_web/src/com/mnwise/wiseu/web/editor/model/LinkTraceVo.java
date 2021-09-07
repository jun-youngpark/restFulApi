package com.mnwise.wiseu.web.editor.model;

/**
 * NVLINKTRACE, NVECARELINKTRACE 테이블 공통 모델 클래스
 */
public class LinkTraceVo {
    private int no;
    private int linkSeq;
    private String linkUrl;
    private String linkDesc;
    private String linkTitle;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String linkType;
    private String linkYn;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getLinkSeq() {
        return this.linkSeq;
    }

    public void setLinkSeq(int linkSeq) {
        this.linkSeq = linkSeq;
    }

    public String getLinkUrl() {
        return this.linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkDesc() {
        return this.linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public String getLinkTitle() {
        return this.linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkYn() {
        return linkYn;
    }

    public void setLinkYn(String linkYn) {
        this.linkYn = linkYn;
    }
}
