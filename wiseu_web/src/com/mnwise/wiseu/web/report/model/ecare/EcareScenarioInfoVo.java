package com.mnwise.wiseu.web.report.model.ecare;

import java.util.List;

import com.mnwise.wiseu.web.base.model.SearchVo;

public class EcareScenarioInfoVo extends SearchVo {
    private static final long serialVersionUID = -4822985926599560272L;
    private int scenarioNo;
    private String scenarioNm;
    private String scenarioDesc;
    private int tagNo;
    private String userId;
    private EcareInfoVo ecareInfoVo;
    private int segmentNo;
    private String segmentNm;
    private String tagNm;
    private String[] channels;
    private int[] campaignNoArray;
    private String serviceType;
    private String subType;
    @SuppressWarnings("rawtypes")
    private List ecareList;

    private long totalSendCnt;

    private int rnum;
    private String channelType;
    private int ecareNo;
    private String ecarePreface;
    private int accuCnt;
    private String ecareSts;
    private int sendCnt;
    private double sendCntPer;
    private int successCnt;
    private double successCntPer;
    private int failCnt;
    private double failCntPer;
    private int openCnt;
    private double openCntPer;
    private int durationCnt;
    private double durationCntPer;
    private int duplicationCnt;
    private double duplicationCntPer;
    private int softBounceCnt;
    private double softBounceCntPer;
    private int hardBounceCnt;
    private double hardBounceCntPer;
    private int returnmailCnt;
    private double returnmailCntPer;
    private int linkCnt;

    private String[] alimtalkSearchCodes;
    private String[] friendtalkSearchCodes;
    private String[] searchCodes;
    private String serarchCdCat;


    public String getSerarchCdCat() {
        return serarchCdCat;
    }
    public void setSerarchCdCat(String serarchCdCat) {
        this.serarchCdCat = serarchCdCat;
    }
    public String[] getSearchCodes() {
        return searchCodes;
    }
    public void setSearchCodes(String[] searchCodes) {
        this.searchCodes = searchCodes;
    }

    public int getRnum() {
        return rnum;
    }

    public void setRnum(int rnum) {
        this.rnum = rnum;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public int getEcareNo() {
        return ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getEcarePreface() {
        return ecarePreface;
    }

    public void setEcarePreface(String ecarePreface) {
        this.ecarePreface = ecarePreface;
    }

    public int getAccuCnt() {
        return accuCnt;
    }

    public void setAccuCnt(int accuCnt) {
        this.accuCnt = accuCnt;
    }

    public String getEcareSts() {
        return ecareSts;
    }

    public void setEcareSts(String ecareSts) {
        this.ecareSts = ecareSts;
    }

    public int getSendCnt() {
        return sendCnt;
    }

    public void setSendCnt(int sendCnt) {
        this.sendCnt = sendCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(int successCnt) {
        this.successCnt = successCnt;
    }

    public int getOpenCnt() {
        return openCnt;
    }

    public void setOpenCnt(int openCnt) {
        this.openCnt = openCnt;
    }

    public int getDurationCnt() {
        return durationCnt;
    }

    public void setDurationCnt(int durationCnt) {
        this.durationCnt = durationCnt;
    }

    public int getDuplicationCnt() {
        return duplicationCnt;
    }

    public void setDuplicationCnt(int duplicationCnt) {
        this.duplicationCnt = duplicationCnt;
    }

    public int getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(int linkCnt) {
        this.linkCnt = linkCnt;
    }

    public int getSoftBounceCnt() {
        return softBounceCnt;
    }

    public void setSoftBounceCnt(int softBounceCnt) {
        this.softBounceCnt = softBounceCnt;
    }

    public int getHardBounceCnt() {
        return hardBounceCnt;
    }

    public void setHardBounceCnt(int hardBounceCnt) {
        this.hardBounceCnt = hardBounceCnt;
    }

    public int getReturnmailCnt() {
        return returnmailCnt;
    }

    public void setReturnmailCnt(int returnmailCnt) {
        this.returnmailCnt = returnmailCnt;
    }

    public EcareScenarioInfoVo() {
        ecareInfoVo = new EcareInfoVo();
    }

    public int getScenarioNo() {
        return scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public String getScenarioNm() {
        return scenarioNm;
    }

    public void setScenarioNm(String scenarioNm) {
        this.scenarioNm = scenarioNm;
    }

    public int getTagNo() {
        return tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public EcareInfoVo getEcareInfoVo() {
        return ecareInfoVo;
    }

    public void setEcareInfoVo(EcareInfoVo ecareInfoVo) {
        this.ecareInfoVo = ecareInfoVo;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public int[] getCampaignNoArray() {
        return campaignNoArray;
    }

    public void setCampaignNoArray(int[] campaignNoArray) {
        this.campaignNoArray = campaignNoArray;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getScenarioDesc() {
        return scenarioDesc;
    }

    public void setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
    }

    @SuppressWarnings("rawtypes")
    public List getEcareList() {
        return ecareList;
    }

    @SuppressWarnings("rawtypes")
    public void setEcareList(List ecareList) {
        this.ecareList = ecareList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getSegmentNm() {
        return segmentNm;
    }

    public void setSegmentNm(String segmentNm) {
        this.segmentNm = segmentNm;
    }

    public long getTotalSendCnt() {
        return totalSendCnt;
    }

    public void setTotalSendCnt(long totalSendCnt) {
        this.totalSendCnt = totalSendCnt;
    }

    public double getSendCntPer() {
        return sendCntPer;
    }

    public void setSendCntPer(double sendCntPer) {
        this.sendCntPer = sendCntPer;
    }

    public double getSuccessCntPer() {
        return successCntPer;
    }

    public void setSuccessCntPer(double successCntPer) {
        this.successCntPer = successCntPer;
    }

    public int getFailCnt() {
        return failCnt;
    }

    public void setFailCnt(int failCnt) {
        this.failCnt = failCnt;
    }

    public double getFailCntPer() {
        return failCntPer;
    }

    public void setFailCntPer(double failCntPer) {
        this.failCntPer = failCntPer;
    }

    public double getOpenCntPer() {
        return openCntPer;
    }

    public void setOpenCntPer(double openCntPer) {
        this.openCntPer = openCntPer;
    }

    public double getDurationCntPer() {
        return durationCntPer;
    }

    public void setDurationCntPer(double durationCntPer) {
        this.durationCntPer = durationCntPer;
    }

    public double getDuplicationCntPer() {
        return duplicationCntPer;
    }

    public void setDuplicationCntPer(double duplicationCntPer) {
        this.duplicationCntPer = duplicationCntPer;
    }

    public double getSoftBounceCntPer() {
        return softBounceCntPer;
    }

    public void setSoftBounceCntPer(double softBounceCntPer) {
        this.softBounceCntPer = softBounceCntPer;
    }

    public double getHardBounceCntPer() {
        return hardBounceCntPer;
    }

    public void setHardBounceCntPer(double hardBounceCntPer) {
        this.hardBounceCntPer = hardBounceCntPer;
    }

    public double getReturnmailCntPer() {
        return returnmailCntPer;
    }

    public void setReturnmailCntPer(double returnmailCntPer) {
        this.returnmailCntPer = returnmailCntPer;
    }

    public String[] getAlimtalkSearchCodes() {
        return alimtalkSearchCodes;
    }

    public void setAlimtalkSearchCodes(String[] alimtalkSearchCodes) {
        this.alimtalkSearchCodes = alimtalkSearchCodes;
    }

    public String[] getFriendtalkSearchCodes() {
        return friendtalkSearchCodes;
    }

    public void setFriendtalkSearchCodes(String[] friendtalkSearchCodes) {
        this.friendtalkSearchCodes = friendtalkSearchCodes;
    }

}
