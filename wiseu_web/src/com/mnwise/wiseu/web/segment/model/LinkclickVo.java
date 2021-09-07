package com.mnwise.wiseu.web.segment.model;

import org.apache.commons.lang3.ArrayUtils;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * 링크클릭
 */
public class LinkclickVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private int serviceNo;
    private int scenarioNo;
    private int linkSeq;
    private int segmentNo;
    private int segmentSize;
    private int psegmentNo;
    private int serviceSegmentNo;
    private int checkLinkSeq;
    private String serviceSts;
    private String serviceType;
    private String linkDesc;
    private String linkUrl;
    private String linkTitle;
    private int linkCnt;
    private String createDt;
    private String createTm;
    private String scenarioNm;
    private int[] linkSeqArray;

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public int getScenarioNo() {
        return scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public int getLinkSeq() {
        return linkSeq;
    }

    public void setLinkSeq(int linkSeq) {
        this.linkSeq = linkSeq;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public int getPsegmentNo() {
        return psegmentNo;
    }

    public void setPsegmentNo(int psegmentNo) {
        this.psegmentNo = psegmentNo;
    }

    public int getCheckLinkSeq() {
        return checkLinkSeq;
    }

    public void setCheckLinkSeq(int checkLinkSeq) {
        this.checkLinkSeq = checkLinkSeq;
    }

    public String getServiceSts() {
        return serviceSts;
    }

    public void setServiceSts(String serviceSts) {
        this.serviceSts = serviceSts;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public String getScenarioNm() {
        return scenarioNm;
    }

    public void setScenarioNm(String scenarioNm) {
        this.scenarioNm = scenarioNm;
    }

    public int[] getLinkSeqArray() {
        return linkSeqArray;
    }

    public void setLinkSeqArray(int[] linkSeqArray) {
        this.linkSeqArray = linkSeqArray;
    }

    public boolean getCheckByLinkSeq() {
        return ArrayUtils.contains(linkSeqArray, checkLinkSeq);
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
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

    public int getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(int linkCnt) {
        this.linkCnt = linkCnt;
    }

    public int getServiceSegmentNo() {
        return serviceSegmentNo;
    }

    public void setServiceSegmentNo(int serviceSegmentNo) {
        this.serviceSegmentNo = serviceSegmentNo;
    }

}
