package com.mnwise.wiseu.web.base.util;

/**
 * 페이징 처리를 위한 Bean
 * 
 * @author hkwee
 * @since 2009.10.08.
 */
public class PageStringBean {

    private String prevIco;
    private String nextIco;
    private String startIco;
    private String endIco;
    private int blockSize;
    private int pageSize;
    private String bgcolor;
    private int currentPage;
    private int totalRowCnt;

    private String pageVariableName;
    private String pageBlockVariableName;
    private String messageSeqVariableName;
    private String pageMode;
    private String notUseParameter; // page 링크 URL에서 제거가 필요한
                                    // 파라미터명[parameterName=parameterName=]

    public PageStringBean() {
        this.prevIco = "";
        this.nextIco = "";
        this.startIco = "";
        this.endIco = "";
        this.blockSize = 10;
        this.pageSize = 10;
        this.bgcolor = "#999999";
        this.currentPage = 1;
        this.pageVariableName = "pgNo";
        this.pageBlockVariableName = "pgBlockNo";
        this.messageSeqVariableName = "messageSeq";
        this.pageMode = "pagingMode";
        this.notUseParameter = "";
    }

    public String getPageMode() {
        return pageMode;
    }

    public void setPageMode(String pageMode) {
        this.pageMode = pageMode;
    }

    public int getTotalRowCnt() {
        return totalRowCnt;
    }

    public void setTotalRowCnt(int totalRowCnt) {
        this.totalRowCnt = totalRowCnt;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public String getEndIco() {
        return endIco;
    }

    public void setEndIco(String endIco) {
        this.endIco = endIco;
    }

    public String getNextIco() {
        return nextIco;
    }

    public void setNextIco(String nextIco) {
        this.nextIco = nextIco;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPrevIco() {
        return prevIco;
    }

    public void setPrevIco(String prevIco) {
        this.prevIco = prevIco;
    }

    public String getStartIco() {
        return startIco;
    }

    public void setStartIco(String startIco) {
        this.startIco = startIco;
    }

    public int getStRecNo() {
        return (this.currentPage - 1) * this.pageSize + 1;
    }

    public int getEnRecNo() {
        return this.currentPage * this.pageSize;
    }

    public String getPageVariableName() {
        return pageVariableName;
    }

    public void setPageVariableName(String pageVariableName) {
        this.pageVariableName = pageVariableName;
    }

    public String getPageBlockVariableName() {
        return pageBlockVariableName;
    }

    public void setPageBlockVariableName(String pageBlockVariableName) {
        this.pageBlockVariableName = pageBlockVariableName;
    }

    public String getMessageSeqVariableName() {
        return messageSeqVariableName;
    }

    public void setMessageSeqVariableName(String messageSeqVariableName) {
        this.messageSeqVariableName = messageSeqVariableName;
    }

    public String getNotUseParameter() {
        return notUseParameter;
    }

    public void setNotUseParameter(String notUseParameter) {
        this.notUseParameter = notUseParameter;
    }
}
