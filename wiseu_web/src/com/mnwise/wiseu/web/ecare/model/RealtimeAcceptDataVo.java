package com.mnwise.wiseu.web.ecare.model;

/**
 * NVREALTIMEACCEPTDATA 테이블 모델 클래스
 */
public class RealtimeAcceptDataVo {
    private String seq;
    private int dataSeq;
    private String attachYn;
    private String attachName;
    private String secuKey;
    private String data;

    // 기본 getter/setter
    public String getSeq() {
        return this.seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int getDataSeq() {
        return this.dataSeq;
    }

    public void setDataSeq(int dataSeq) {
        this.dataSeq = dataSeq;
    }

    public String getAttachYn() {
        return this.attachYn;
    }

    public void setAttachYn(String attachYn) {
        this.attachYn = attachYn;
    }

    public String getAttachName() {
        return this.attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getSecuKey() {
        return this.secuKey;
    }

    public void setSecuKey(String secuKey) {
        this.secuKey = secuKey;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
