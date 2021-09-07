package com.mnwise.wiseu.web.report.model;

public class LinkTraceResult2Vo {
    private int campaignNo;
    private int linktraceCnt;
    private int uniqueLinkCnt;
    private int allLinkCnt;
    private String linkUrl;
    private String linkTitle;
    private int bestLinkCnt;

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public int getLinktraceCnt() {
        return linktraceCnt;
    }

    public void setLinktraceCnt(int linktraceCnt) {
        this.linktraceCnt = linktraceCnt;
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getBestLinkCnt() {
        return bestLinkCnt;
    }

    public void setBestLinkCnt(int bestLinkCnt) {
        this.bestLinkCnt = bestLinkCnt;
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

}
