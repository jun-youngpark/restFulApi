package com.mnwise.wiseu.web.editor.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class MmsVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private String filePath;
    private String fileName;
    private String fileType;
    private int contsNo;
    private String contsTxt;
    private String filePreviewPath;
    private String filePreviewName;
    private String contsNm;
    private Integer fileSize;
    private String createDt;
    private String createTm;

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getCreateTm() {
        return createTm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getContsNm() {
        return contsNm;
    }

    public void setContsNm(String contsNm) {
        this.contsNm = contsNm;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContsTxt() {
        return contsTxt;
    }

    public void setContsTxt(String contsTxt) {
        this.contsTxt = contsTxt;
    }

    public String getFilePreviewPath() {
        return filePreviewPath;
    }

    public void setFilePreviewPath(String filePreviewPath) {
        this.filePreviewPath = filePreviewPath;
    }

    public String getFilePreviewName() {
        return filePreviewName;
    }

    public void setFilePreviewName(String filePreviewName) {
        this.filePreviewName = filePreviewName;
    }

    public int getContsNo() {
        return contsNo;
    }

    public void setContsNo(int contsNo) {
        this.contsNo = contsNo;
    }
}
