package com.mnwise.wiseu.web.segment.model;

/**
 * NVSEGGENEALOGY 테이블 모델 클래스
 */
public class SegGenealogyVo {
    private int segmentNo;
    private int genealogySeq;
    private int supraSegmentNo;

    // 기본 getter/setter
    public int getSegmentNo() {
        return this.segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public int getGenealogySeq() {
        return this.genealogySeq;
    }

    public void setGenealogySeq(int genealogySeq) {
        this.genealogySeq = genealogySeq;
    }

    public int getSupraSegmentNo() {
        return this.supraSegmentNo;
    }

    public void setSupraSegmentNo(int supraSegmentNo) {
        this.supraSegmentNo = supraSegmentNo;
    }

}
