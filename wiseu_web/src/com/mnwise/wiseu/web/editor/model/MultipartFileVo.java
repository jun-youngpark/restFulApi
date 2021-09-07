package com.mnwise.wiseu.web.editor.model;

/**
 * NVMULTIPARTFILE, NVECAREMULTIPARTFILE 테이블 공통 모델 클래스
 */
public class MultipartFileVo {
    /** 캠페인 번호/이케어 번호 */
    private int no;
    private int seq;
    private String fileAlias;
    private String filePath;
    private long fileSize;
    private String fileName;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String fileUnit;
    private String msg;
    // 추가 이케어 전용
    private String secuField;
    private String fileType;
    private String encYn;

    public String getSecuField() {
        return secuField;
    }

    public void setSecuField(String secuField) {
        this.secuField = secuField;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getEncYn() {
        return encYn;
    }

    public void setEncYn(String encYn) {
        this.encYn = encYn;
    }

    // 기본 getter/setter
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getSeq() {
        return this.seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getFileAlias() {
        return this.fileAlias;
    }

    public void setFileAlias(String fileAlias) {
        this.fileAlias = fileAlias;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getFileUnit() {
        return fileUnit;
    }

    public void setFileUnit(String fileUnit) {
        this.fileUnit = fileUnit;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
