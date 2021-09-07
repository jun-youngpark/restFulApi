package com.mnwise.wiseu.web.report.model;

import com.mnwise.wiseu.web.base.util.FormatUtil;

public class LinkTraceResultVo {
    private int linkSeq;
    private String linkUrl;
    private String linkTitle;
    private int uniqueLinkCnt;
    private int allLinkCnt;
    private int sendCnt = 0;
    private int openCnt = 0;
    private String linkDesc;

    public int getLinkSeq() {
        return linkSeq;
    }

    public void setLinkSeq(int linkSeq) {
        this.linkSeq = linkSeq;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getUniqueLinkCnt() {
        return uniqueLinkCnt;
    }

    public void setUniqueLinkCnt(int uniqueLinkCnt) {
        this.uniqueLinkCnt = uniqueLinkCnt;
    }

    public int getAllLinkCnt() {
        return allLinkCnt;
    }

    public void setAllLinkCnt(int allLinkCnt) {
        this.allLinkCnt = allLinkCnt;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public String getByUniqueSend() {
        return FormatUtil.toStrPercent(uniqueLinkCnt, sendCnt, 1);
    }

    public String getByUniqueOpen() {
        return FormatUtil.toStrPercent(uniqueLinkCnt, openCnt, 1);
    }

    public String getByDupSend() {
        return FormatUtil.toStrPercent(allLinkCnt, sendCnt, 1);
    }

    public String getByDupOpen() {
        return FormatUtil.toStrPercent(allLinkCnt, openCnt, 1);
    }

    public String getLinkTitle() {

        if(linkTitle == null) {
            linkTitle = "<a href=\"" + linkUrl + "\" target=\"_blank\">" + linkUrl + "</a>";
        } else if(linkTitle.indexOf("src") > 0) {
            linkTitle = linkTitle.replaceAll("IMG", "IMG  height=50 width=200");
        } else {
            linkTitle = "<a href=\"" + linkUrl + "\" target=\"_blank\">" + linkUrl + "</a>";
        }

        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

}
