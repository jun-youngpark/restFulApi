package com.mnwise.wiseu.web.report.model.ecare;

import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.report.model.RptSendResultVo;

public class EcareInfoVo extends BaseVo {
    /**
     * 
     */
    private static final long serialVersionUID = -6629894382962003901L;
    private int ecareNo;
    private long resultSeq;
    private String ecareNm; // 캠페인명
    private String ecarePreface; // 메일제목
    private String ecareSts; // 상태
    private String ecareStsNm;
    private String channelType; // M : Mail, S : SMS
    private String subType;
    private String termType;
    private String traceStartDt; // 캠페인 반응추적 시작일
    private String traceStartTm;
    private String traceEndDt; // 반응추적 종료일
    private String traceEndTm;
    private String serviceType; // R : 실시간, S : 스케쥴
    private String reportDt;
    private int accuCnt;
    private String cycleCd;
    private String reportSts;
    private String relationType;
    private int depthNo;
    private int scenarioNo;
    private String searchDomainNm;
    private String searchErrCd;
    private RptSendResultVo sendResultVo;

    public int getEcareNo() {
        return ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public long getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(long resultSeq) {
        this.resultSeq = resultSeq;
    }

    public String getEcareNm() {
        return ecareNm;
    }

    public void setEcareNm(String ecareNm) {
        this.ecareNm = ecareNm;
    }

    public String getEcarePreface() {
        return ecarePreface;
    }

    public void setEcarePreface(String ecarePreface) {
        this.ecarePreface = ecarePreface;
    }

    public String getEcareSts() {
        return ecareSts;
    }

    public void setEcareSts(String ecareSts) {
        this.ecareSts = ecareSts;
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

    public String getTraceStartTm() {
        return traceStartTm;
    }

    public void setTraceStartTm(String traceStartTm) {
        this.traceStartTm = traceStartTm;
    }

    public String getTraceEndDt() {
        return traceEndDt;
    }

    public void setTraceEndDt(String traceEndDt) {
        this.traceEndDt = traceEndDt;
    }

    public String getTraceEndTm() {
        return traceEndTm;
    }

    public void setTraceEndTm(String traceEndTm) {
        this.traceEndTm = traceEndTm;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getReportDt() {
        return reportDt;
    }

    public void setReportDt(String reportDt) {
        this.reportDt = reportDt;
    }

    public RptSendResultVo getSendResultVo() {
        return sendResultVo;
    }

    public void setSendResultVo(RptSendResultVo sendResultVo) {
        this.sendResultVo = sendResultVo;
    }

    public String getEcareStsNm() {
        return ecareStsNm;
    }

    public void setEcareStsNm(String ecareStsNm) {
        this.ecareStsNm = ecareStsNm;
    }

    public int getAccuCnt() {
        return accuCnt;
    }

    public void setAccuCnt(int accuCnt) {
        this.accuCnt = accuCnt;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public String getCycleCd() {
        return cycleCd;
    }

    public void setCycleCd(String cycleCd) {
        this.cycleCd = cycleCd;
    }

    public String getReportSts() {
        return reportSts;
    }

    public void setReportSts(String reportSts) {
        this.reportSts = reportSts;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
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

    public int getScenarioNo() {
        return scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

}
