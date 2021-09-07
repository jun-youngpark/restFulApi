package com.mnwise.wiseu.web.report.model.campaign;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;

public class CampaignInfoVo extends BaseVo {
    private static final long serialVersionUID = -1L;

    private int campaignNo;
    private String campaignNm; // 캠페인명
    private String campaignPreface; // 메일제목
    private String campaignState; // 상태
    private String relationType; // 일반/재발송
    private String channelType; // M : Mail, S : SMS
    private String sendStartDt;
    private String sendStartTm;
    private String traceStartDt; // 캠페인 반응추적 시작일
    private String traceStartTm;
    private String traceEndDt; // 반응추적 종료일
    private String traceEndTm;
    private String resultSts;
    private String logSts;
    private String createDt;
    private String reportSts;
    private int depthNo;
    private String searchDomainNm;
    private String searchErrCd;
    private RptSendResultVo sendResultVo = new RptSendResultVo();
    private int segmentNo; // 컨텐츠 반응 분석 시 템플릿을 가져오는데 SEGMENT_NO가 필요함.

    public int getCampaignNo() {
        return campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getCampaignNm() {
        return campaignNm;
    }

    public void setCampaignNm(String campaignNm) {
        this.campaignNm = campaignNm;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getCampaignName() {
        return campaignNm;
    }

    public void setCampaignName(String campaignName) {
        this.campaignNm = campaignName;
    }

    public String getCampaignPreface() {
        return campaignPreface;
    }

    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
    }

    public String getCampaignState() {
        return campaignState;
    }

    public void setCampaignState(String campaignState) {
        this.campaignState = campaignState;
    }

    public String getRealationType() {
        return relationType;
    }

    public void setRealationType(String realationType) {
        this.relationType = realationType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getTraceStartDt() {
        return traceStartDt;
    }

    public void setTraceStartDt(String traceStartDt) {
        this.traceStartDt = traceStartDt;
    }

    public String getTraceEndDt() {
        return traceEndDt;
    }

    public void setTraceEndDt(String traceEndDt) {
        this.traceEndDt = traceEndDt;
    }

    public String getTraceStartYear() {
        return (traceStartDt != null) ? traceStartDt.substring(0, 4) : traceStartDt;
    }

    public String getTraceStartMonth() {
        return (traceStartDt != null) ? traceStartDt.substring(4, 6) : traceStartDt;
    }

    public String getTraceStartDay() {
        return (traceStartDt != null) ? traceStartDt.substring(6) : traceStartDt;
    }

    public String getTraceEndYear() {
        return (traceEndDt != null) ? traceEndDt.substring(0, 4) : traceEndDt;
    }

    public String getTraceEndMonth() {
        return (traceEndDt != null) ? traceEndDt.substring(4, 6) : traceEndDt;
    }

    public String getTraceEndDay() {
        return (traceEndDt != null) ? traceEndDt.substring(6) : traceEndDt;
    }

    public String getTraceStartTm() {
        return traceStartTm;
    }

    public void setTraceStartTm(String traceStartTm) {
        this.traceStartTm = traceStartTm;
    }

    public String getTraceEndTm() {
        return traceEndTm;
    }

    public void setTraceEndTm(String traceEndTm) {
        this.traceEndTm = traceEndTm;
    }

    public RptSendResultVo getSendResultVo() {
        return sendResultVo;
    }

    public void setSendResultVo(RptSendResultVo sendResultVo) {
        this.sendResultVo = sendResultVo;
    }

    public String getResultSts() {
        return resultSts;
    }

    public void setResultSts(String resultSts) {
        this.resultSts = resultSts;
    }

    public String getLogSts() {
        return logSts;
    }

    public void setLogSts(String logSts) {
        this.logSts = logSts;
    }

    public String getSendStartDt() {
        return sendStartDt;
    }

    public void setSendStartDt(String sendStartDt) {
        this.sendStartDt = sendStartDt;
    }

    public String getSendStartTm() {
        return sendStartTm;
    }

    public void setSendStartTm(String sendStartTm) {
        this.sendStartTm = sendStartTm;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getReportSts() {
        return reportSts;
    }

    public void setReportSts(String reportSts) {
        this.reportSts = reportSts;
    }

    public int getDepthNo() {
        return depthNo;
    }

    public void setDepthNo(int depthNo) {
        this.depthNo = depthNo;
    }

    public String getSearchDomainNm() {
        return searchDomainNm;
    }

    public void setSearchDomainNm(String searchDomainNm) {
        this.searchDomainNm = searchDomainNm;
    }

    public String getSearchErrCd() {
        return searchErrCd;
    }

    public void setSearchErrCd(String searchErrCd) {
        this.searchErrCd = searchErrCd;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }
}
