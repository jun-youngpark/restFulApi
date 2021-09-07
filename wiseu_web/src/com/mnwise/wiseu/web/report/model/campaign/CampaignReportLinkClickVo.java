package com.mnwise.wiseu.web.report.model.campaign;

import java.io.Serializable;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * 캠페인 링크클릭 리포트 VO
 * 
 * @author hkwee
 * @since 2010-02-05
 */
public class CampaignReportLinkClickVo extends SearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int campaignNo;
    private int linkSeq;
    private String linkDesc;
    private String linkUrl;
    private String linkTitle;
    private int totalCnt;
    private int oneClickCnt;
    private int moreClickCnt;
    private int gid;

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public int getLinkSeq() {
        return linkSeq;
    }

    public void setLinkSeq(int linkSeq) {
        this.linkSeq = linkSeq;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getOneClickCnt() {
        return oneClickCnt;
    }

    public void setOneClickCnt(int oneClickCnt) {
        this.oneClickCnt = oneClickCnt;
    }

    public int getMoreClickCnt() {
        return moreClickCnt;
    }

    public void setMoreClickCnt(int moreClickCnt) {
        this.moreClickCnt = moreClickCnt;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getLinkClickTitle() {

        if(linkTitle == null) {
            linkTitle = "<a href=\"" + linkUrl + "\" target=\"_blank\">" + linkUrl + "</a>";
        } else if(linkTitle.indexOf("src") > 0) {
            linkTitle = linkTitle.replaceAll("IMG", "IMG  height=50 width=200");
        } else {
            linkTitle = "<a href=\"" + linkUrl + "\" target=\"_blank\">" + linkUrl + "</a>";
        }

        return linkTitle;
    }
}
