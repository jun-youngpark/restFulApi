package com.mnwise.wiseu.web.template.model;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * NVCONTENTS 테이블 모델 클래스
 */
public class ContentVo extends SearchVo {
    private static final long serialVersionUID = 1L;
    private int contsNo;
    private String grpCd;
    private String categoryCd;
    private String userId;
    private String contsNm;
    private String contsDesc;
    private String fileUrlName;
    private String fileType;
    private String fileName;
    private String createDt;
    private String createTm;
    private String authType;
    private int tagNo;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int[] templateNoArray;
    private String tagNm;
    private String grpNm;
    private String userNm;
    private String fileUrlNm;  // TODO : fileUrlName로 대체후 제거 필요
    private String fileNm;  // TODO : fileName로 대체후 제거 필요
    private String command;
    private String content;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getContsNo() {
        return this.contsNo;
    }

    public void setContsNo(int contsNo) {
        this.contsNo = contsNo;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getCategoryCd() {
        return this.categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContsNm() {
        return this.contsNm;
    }

    public void setContsNm(String contsNm) {
        this.contsNm = contsNm;
    }

    public String getContsDesc() {
        return this.contsDesc;
    }

    public void setContsDesc(String contsDesc) {
        this.contsDesc = contsDesc;
    }

    public String getFileUrlName() {
        return this.fileUrlName;
    }

    public void setFileUrlName(String fileUrlName) {
        this.fileUrlName = fileUrlName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateDt() {
        return this.createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getCreateTm() {
        return this.createTm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    public String getAuthType() {
        return this.authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int[] getTemplateNoArray() {
        return templateNoArray;
    }

    public void setTemplateNoArray(int[] templateNoArray) {
        this.templateNoArray = templateNoArray;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getFileUrlNm() {
        return fileUrlNm;
    }

    public void setFileUrlNm(String fileUrlNm) {
        this.fileUrlNm = fileUrlNm;
    }

    public String getFileNm() {
        return fileNm;
    }

    public void setFileNm(String fileNm) {
        this.fileNm = fileNm;
    }

    /**
     * @return
     * @author
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ContentVo[");
        buffer.append("serialVersionUID = ").append(serialVersionUID);
        buffer.append(" authType = ").append(authType);
        buffer.append(" categoryCd = ").append(categoryCd);
        buffer.append(" command = ").append(command);
        buffer.append(" content = ").append(content);
        buffer.append(" contsNo = ").append(contsNo);
        buffer.append(" contsDesc = ").append(contsDesc);
        buffer.append(" contsNm = ").append(contsNm);
        buffer.append(" createDt = ").append(createDt);
        buffer.append(" createTm = ").append(createTm);
        buffer.append(" fileNm = ").append(fileNm);
        buffer.append(" fileType = ").append(fileType);
        buffer.append(" fileUrlNm = ").append(fileUrlNm);
        buffer.append(" grpCd = ").append(grpCd);
        buffer.append(" tagNm = ").append(tagNm);
        buffer.append(" tagNo = ").append(tagNo);
        if(templateNoArray == null) {
            buffer.append(" templateNoArray = ").append("null");
        } else {
            buffer.append(" templateNoArray = ").append("[");
            for(int i = 0; i < templateNoArray.length; i++) {
                if(i != 0)
                    buffer.append(", ");
                buffer.append(templateNoArray[i]);
            }
            buffer.append("]");
        }
        buffer.append(" userId = ").append(userId);
        buffer.append(" userNm = ").append(userNm);
        buffer.append("]");
        return buffer.toString();
    }

}
