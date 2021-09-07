package com.mnwise.wiseu.web.campaign.model;

import java.util.List;

import org.springframework.util.StringUtils;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;

/**
 * NVCAMPAIGN 테이블 모델 클래스
 */
public class CampaignVo extends BaseVo {
    private static final long serialVersionUID = 1L;
    private int campaignNo;
    private String grpCd;
    private String userId;
    private int segmentNo;
    private String campaignNm;
    private String campaignDesc;
    private String campaignPreface;
    private String campaignSts;
    private String createDt;
    private String createTm;
    private String lastUpdateDt;
    private String lastUpdateTm;
    private String campaignType;
    private int templateType;
    private String sendingSts;
    private String sendingCycle;
    private String sendStartDt;
    private String sendStartTm;
    private String sendFinishDt;
    private String sendFinishTm;
    private int targetCnt;
    private String surveyEndYn;
    private String sendingMode;
    private int surveyResponseCnt;
    private int surveyNo;
    private String logYn;
    private int keepDay;
    private String shareYn;
    private String surveyStartDt;
    private String surveyStartTm;
    private String surveyEndDt;
    private String surveyEndTm;
    private Integer retryCnt;
    private String rptCreateDt;
    private String senderNm;
    private String senderEmail;
    private String receiverNm;
    private String senderTel;
    private String retmailReceiver;
    private String htmlUpdateYn;
    private String reportSts;
    private String campaignClass;
    private String channelType;
    private String relationType;
    private String relationTree;
    private String retMailSendYn;
    private String etcInfo1;
    private String etcInfo2;
    private String promotionType;
    private String campaignLevel;
    private String categoryCd;
    private int scenarioNo;
    private int depthNo;
    private String editorId;
    private int sendServer;
    private String approvalSts;
    private String reqDeptId;
    private String reqUserId;
    // 20170412 정다운 분할발송
    private String divideYn;
    private int divideInterval;
    private int divideCnt;
    private String abTestType;

    /** A/B 선택 조건 */
    private String abTestCond;

    /** A/B 테스트 대상자 선정 비율 */
    private int abTestRate;

    /** A/B테스트 제목 */
    private String campaignPrefaceAb;
    private String multiContInfo;

    /** 기본핸들러 SEQ */
    private int handlerSeq;
    private String kakaoSenderKey;
    private String kakaoTmplCd;
    private int kakaoImageNo;
    private String failbackSendYn;
    private String failbackSubject;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int newCampaignNo;
    private String campaignStsNm;
    private String sendstartDtm;
    // 2014-07-09 남기욱 멀티 디바이스 추가
    private String relationTypeNm;
    private String template;
    private String handlerType;

    private long resultSeq;
    private String envSenderNm;
    private String envSenderEmail;
    private String envReceiverNm;
    private String envSenderTel;
    private String envRetmailReceiver;
    private String envSenderFax;
    private String envReceiverFax;
    private String approvalStsNm;
    private String templateSenderKey;
    private String kakaoYellowId;
    private String serviceType;
    private String subType;
    private String termType;
    private int ecmScheduleNo;
    private ScheduleVo scheduleVo = new ScheduleVo();
    private TraceInfoVo traceInfoVo = new TraceInfoVo();
    private SendResultVo sendResultVo;
    private HandlerVo handlerVo;
    private int[] campaignNoArray;
    private int[] ecmScheduleNoArray;
    private List<TemplateVo> templateList;
    private List<DivideSchedule> campaignDivideScheduleList;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getCampaignNo() {
        return this.campaignNo;
    }

    public void setCampaignNo(int campaignNo) {
        this.campaignNo = campaignNo;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSegmentNo() {
        return this.segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getCampaignNm() {
        return this.campaignNm;
    }

    public void setCampaignNm(String campaignNm) {
        this.campaignNm = campaignNm;
    }

    public String getCampaignDesc() {
        return this.campaignDesc;
    }

    public void setCampaignDesc(String campaignDesc) {
        this.campaignDesc = campaignDesc;
    }

    public String getCampaignPreface() {
        return this.campaignPreface;
    }

    public void setCampaignPreface(String campaignPreface) {
        this.campaignPreface = campaignPreface;
    }

    public String getCampaignSts() {
        return this.campaignSts;
    }

    public void setCampaignSts(String campaignSts) {
        this.campaignSts = campaignSts;
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

    public String getLastUpdateDt() {
        return this.lastUpdateDt;
    }

    public void setLastUpdateDt(String lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public String getLastUpdateTm() {
        return this.lastUpdateTm;
    }

    public void setLastUpdateTm(String lastUpdateTm) {
        this.lastUpdateTm = lastUpdateTm;
    }

    public String getCampaignType() {
        return StringUtil.defaultIfBlank(this.campaignType, "N");
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public int getTemplateType() {
        return this.templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getSendingSts() {
        return this.sendingSts;
    }

    public void setSendingSts(String sendingSts) {
        this.sendingSts = sendingSts;
    }

    public String getSendingCycle() {
        return this.sendingCycle;
    }

    public void setSendingCycle(String sendingCycle) {
        this.sendingCycle = sendingCycle;
    }

    public String getSendStartDt() {
        return this.sendStartDt;
    }

    public void setSendStartDt(String sendStartDt) {
        this.sendStartDt = sendStartDt;
    }

    public String getSendStartTm() {
        return this.sendStartTm;
    }

    public void setSendStartTm(String sendStartTm) {
        this.sendStartTm = sendStartTm;
    }

    public String getSendFinishDt() {
        return this.sendFinishDt;
    }

    public void setSendFinishDt(String sendFinishDt) {
        this.sendFinishDt = sendFinishDt;
    }

    public String getSendFinishTm() {
        return this.sendFinishTm;
    }

    public void setSendFinishTm(String sendFinishTm) {
        this.sendFinishTm = sendFinishTm;
    }

    public int getTargetCnt() {
        return this.targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
    }

    public String getSurveyEndYn() {
        return this.surveyEndYn;
    }

    public void setSurveyEndYn(String surveyEndYn) {
        this.surveyEndYn = surveyEndYn;
    }

    public String getSendingMode() {
        return this.sendingMode;
    }

    public void setSendingMode(String sendingMode) {
        this.sendingMode = sendingMode;
    }

    public int getSurveyResponseCnt() {
        return this.surveyResponseCnt;
    }

    public void setSurveyResponseCnt(int surveyResponseCnt) {
        this.surveyResponseCnt = surveyResponseCnt;
    }

    public int getSurveyNo() {
        return this.surveyNo;
    }

    public void setSurveyNo(int surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getLogYn() {
        return this.logYn;
    }

    public void setLogYn(String logYn) {
        this.logYn = logYn;
    }

    public int getKeepDay() {
        return this.keepDay;
    }

    public void setKeepDay(int keepDay) {
        this.keepDay = keepDay;
    }

    public String getShareYn() {
        return this.shareYn;
    }

    public void setShareYn(String shareYn) {
        this.shareYn = shareYn;
    }

    public String getSurveyStartDt() {
        return this.surveyStartDt;
    }

    public void setSurveyStartDt(String surveyStartDt) {
        this.surveyStartDt = surveyStartDt;
    }

    public String getSurveyStartTm() {
        return this.surveyStartTm;
    }

    public void setSurveyStartTm(String surveyStartTm) {
        this.surveyStartTm = surveyStartTm;
    }

    public String getSurveyEndDt() {
        return this.surveyEndDt;
    }

    public void setSurveyEndDt(String surveyEndDt) {
        this.surveyEndDt = surveyEndDt;
    }

    public String getSurveyEndTm() {
        return this.surveyEndTm;
    }

    public void setSurveyEndTm(String surveyEndTm) {
        this.surveyEndTm = surveyEndTm;
    }

    public Integer getRetryCnt() {
        return this.retryCnt;
    }

    public void setRetryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
    }

    public String getRptCreateDt() {
        return this.rptCreateDt;
    }

    public void setRptCreateDt(String rptCreateDt) {
        this.rptCreateDt = rptCreateDt;
    }

    public String getSenderNm() {
        return (this.senderNm == null) ? this.envSenderNm : this.senderNm;
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }

    public String getSenderEmail() {
        if(StringUtil.isBlank(this.senderEmail)) {
            this.senderEmail = this.channelType.equals("F") ? this.envSenderFax : this.envSenderEmail;
        }

        return this.senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverNm() {
        return StringUtil.defaultIfBlank(this.receiverNm, envReceiverNm);
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getSenderTel() {
        return StringUtil.defaultIfBlank(this.senderTel, envSenderTel);
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getRetmailReceiver() {
        if(StringUtil.isBlank(this.retmailReceiver)) {
            this.retmailReceiver = this.channelType.equals("F") ? this.envReceiverFax : this.envRetmailReceiver;
        }

        return this.retmailReceiver;
    }

    public void setRetmailReceiver(String retmailReceiver) {
        this.retmailReceiver = retmailReceiver;
    }

    public String getHtmlUpdateYn() {
        return this.htmlUpdateYn;
    }

    public void setHtmlUpdateYn(String htmlUpdateYn) {
        this.htmlUpdateYn = htmlUpdateYn;
    }

    public String getReportSts() {
        return this.reportSts;
    }

    public void setReportSts(String reportSts) {
        this.reportSts = reportSts;
    }

    public String getCampaignClass() {
        return this.campaignClass;
    }

    public void setCampaignClass(String campaignClass) {
        this.campaignClass = campaignClass;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getRelationType() {
        return this.relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationTree() {
        return this.relationTree;
    }

    public void setRelationTree(String relationTree) {
        this.relationTree = relationTree;
    }

    public String getRetMailSendYn() {
        return this.retMailSendYn;
    }

    public void setRetmailSendYn(String retMailSendYn) {
        this.retMailSendYn = retMailSendYn;
    }

    public String getEtcInfo1() {
        return this.etcInfo1;
    }

    public void setEtcInfo1(String etcInfo1) {
        this.etcInfo1 = etcInfo1;
    }

    public String getEtcInfo2() {
        return this.etcInfo2;
    }

    public void setEtcInfo2(String etcInfo2) {
        this.etcInfo2 = etcInfo2;
    }

    public String getPromotionType() {
        return this.promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getCampaignLevel() {
        return this.campaignLevel;
    }

    public void setCampaignLevel(String campaignLevel) {
        this.campaignLevel = campaignLevel;
    }

    public String getCategoryCd() {
        return this.categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public int getScenarioNo() {
        return this.scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public int getDepthNo() {
        return this.depthNo;
    }

    public void setDepthNo(int depthNo) {
        this.depthNo = depthNo;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public int getSendServer() {
        return this.sendServer;
    }

    public void setSendServer(int sendServer) {
        this.sendServer = sendServer;
    }

    public String getApprovalSts() {
        return this.approvalSts;
    }

    public void setApprovalSts(String approvalSts) {
        this.approvalSts = approvalSts;
    }

    public String getReqDeptId() {
        return this.reqDeptId;
    }

    public void setReqDeptId(String reqDeptId) {
        this.reqDeptId = reqDeptId;
    }

    public String getReqUserId() {
        return this.reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getDivideYn() {
        return this.divideYn;
    }

    public void setDivideYn(String divideYn) {
        this.divideYn = divideYn;
    }

    public int getDivideInterval() {
        return this.divideInterval;
    }

    public void setDivideInterval(int divideInterval) {
        this.divideInterval = divideInterval;
    }

    public int getDivideCnt() {
        return this.divideCnt;
    }

    public void setDivideCnt(int divideCnt) {
        this.divideCnt = divideCnt;
    }

    public String getAbTestType() {
        return this.abTestType;
    }

    public void setAbTestType(String abTestType) {
        this.abTestType = abTestType;
    }

    public String getAbTestCond() {
        return this.abTestCond;
    }

    public void setAbTestCond(String abTestCond) {
        this.abTestCond = abTestCond;
    }

    public int getAbTestRate() {
        return this.abTestRate;
    }

    public void setAbTestRate(int abTestRate) {
        this.abTestRate = abTestRate;
    }

    public String getCampaignPrefaceAb() {
        return this.campaignPrefaceAb;
    }

    public void setCampaignPrefaceAb(String campaignPrefaceAb) {
        this.campaignPrefaceAb = campaignPrefaceAb;
    }

    public String getMultiContInfo() {
        return this.multiContInfo;
    }

    public void setMultiContInfo(String multiContInfo) {
        this.multiContInfo = multiContInfo;
    }

    public int getHandlerSeq() {
        return this.handlerSeq;
    }

    public void setHandlerSeq(int handlerSeq) {
        this.handlerSeq = handlerSeq;
    }

    public String getKakaoSenderKey() {
        return this.kakaoSenderKey;
    }

    public void setKakaoSenderKey(String kakaoSenderKey) {
        this.kakaoSenderKey = kakaoSenderKey;
    }

    public String getKakaoTmplCd() {
        return this.kakaoTmplCd;
    }

    public void setKakaoTmplCd(String kakaoTmplCd) {
        this.kakaoTmplCd = kakaoTmplCd;
    }

    public int getKakaoImageNo() {
        return this.kakaoImageNo;
    }

    public void setKakaoImageNo(int kakaoImageNo) {
        this.kakaoImageNo = kakaoImageNo;
    }

    public String getFailbackSendYn() {
        return this.failbackSendYn;
    }

    public void setFailbackSendYn(String failbackSendYn) {
        this.failbackSendYn = failbackSendYn;
    }

    public String getFailbackSubject() {
        return this.failbackSubject;
    }

    public void setFailbackSubject(String failbackSubject) {
        this.failbackSubject = failbackSubject;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public int getNewCampaignNo() {
        return newCampaignNo;
    }

    public void setNewCampaignNo(int newCampaignNo) {
        this.newCampaignNo = newCampaignNo;
    }

    public String getCampaignStsNm() {
        return campaignStsNm;
    }

    public void setCampaignStsNm(String campaignStsNm) {
        this.campaignStsNm = campaignStsNm;
    }

    public String getSendstartDtm() {
        return sendstartDtm;
    }

    public void setSendstartDtm(String sendstartDtm) {
        this.sendstartDtm = sendstartDtm;
    }

    public String getRelationTypeNm() {
        return relationTypeNm;
    }

    public void setRelationTypeNm(String relationTypeNm) {
        this.relationTypeNm = relationTypeNm;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public long getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(long resultSeq) {
        this.resultSeq = resultSeq;
    }

    public String getEnvSenderNm() {
        return envSenderNm;
    }

    public void setEnvSenderNm(String envSenderNm) {
        this.envSenderNm = envSenderNm;
    }

    public String getEnvSenderEmail() {
        return envSenderEmail;
    }

    public void setEnvSenderEmail(String envSenderEmail) {
        this.envSenderEmail = envSenderEmail;
    }

    public String getEnvReceiverNm() {
        return envReceiverNm;
    }

    public void setEnvReceiverNm(String envReceiverNm) {
        this.envReceiverNm = envReceiverNm;
    }

    public String getEnvSenderTel() {
        return envSenderTel;
    }

    public void setEnvSenderTel(String envSenderTel) {
        this.envSenderTel = envSenderTel;
    }

    public String getEnvRetmailReceiver() {
        return envRetmailReceiver;
    }

    public void setEnvRetmailReceiver(String envRetmailReceiver) {
        this.envRetmailReceiver = envRetmailReceiver;
    }

    public String getEnvSenderFax() {
        return envSenderFax;
    }

    public void setEnvSenderFax(String envSenderFax) {
        envSenderFax = envSenderFax;
    }

    public String getEnvReceiverFax() {
        return envReceiverFax;
    }

    public void setEnvReceiverFax(String envReceiverFax) {
        envReceiverFax = envReceiverFax;
    }

    public String getApprovalStsNm() {
        return approvalStsNm;
    }

    public void setApprovalStsNm(String approvalStsNm) {
        this.approvalStsNm = approvalStsNm;
    }

    public String getTemplateSenderKey() {
        return templateSenderKey;
    }

    public void setTemplateSenderKey(String templateSenderKey) {
        this.templateSenderKey = templateSenderKey;
    }

    public String getKakaoYellowId() {
        return kakaoYellowId;
    }

    public void setKakaoYellowId(String kakaoYellowId) {
        this.kakaoYellowId = kakaoYellowId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSubType() {
        return StringUtil.trimToNull(subType);
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

    public int getEcmScheduleNo() {
        return ecmScheduleNo;
    }

    public void setEcmScheduleNo(int ecmScheduleNo) {
        this.ecmScheduleNo = ecmScheduleNo;
    }

    public ScheduleVo getScheduleVo() {
        return scheduleVo;
    }

    public void setScheduleVo(ScheduleVo scheduleVo) {
        this.scheduleVo = scheduleVo;
    }

    public TraceInfoVo getTraceInfoVo() {
        return traceInfoVo;
    }

    public void setTraceInfoVo(TraceInfoVo traceInfoVo) {
        this.traceInfoVo = traceInfoVo;
    }

    public SendResultVo getSendResultVo() {
        return sendResultVo;
    }

    public void setSendResultVo(SendResultVo sendResultVo) {
        this.sendResultVo = sendResultVo;
    }

    public HandlerVo getHandlerVo() {
        return handlerVo;
    }

    public void setHandlerVo(HandlerVo handlerVo) {
        this.handlerVo = handlerVo;
    }

    public int[] getCampaignNoArray() {
        return campaignNoArray;
    }

    public void setCampaignNoArray(int[] campaignNoArray) {
        this.campaignNoArray = campaignNoArray;
    }

    public int[] getEcmScheduleNoArray() {
        return ecmScheduleNoArray;
    }

    public void setEcmScheduleNoArray(int[] ecmScheduleNoArray) {
        this.ecmScheduleNoArray = ecmScheduleNoArray;
    }

    public List<TemplateVo> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<TemplateVo> templateList) {
        this.templateList = templateList;
    }

    public List<DivideSchedule> getCampaignDivideScheduleList() {
        return campaignDivideScheduleList;
    }

    public void setCampaignDivideScheduleList(List<DivideSchedule> campaignDivideScheduleList) {
        this.campaignDivideScheduleList = campaignDivideScheduleList;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getSendStartDtDB() {
        return StringUtils.deleteAny(sendStartDt, "-:");
    }

    public String getSendStartTmDB() {
        return StringUtils.deleteAny(sendStartTm, "-:");
    }

    public String getSendStartDtToDateStr() {
        return FormatUtil.toBasicStrDate(StringUtils.deleteAny(sendStartDt, "-:"), StringUtils.deleteAny(sendStartTm, "-:"));
    }

    public String getSendStartDtToSimpleDateStr() {
        return FormatUtil.toSimpleBasicStrDate(StringUtils.deleteAny(sendStartDt, "-:"), StringUtils.deleteAny(sendStartTm, "-:"));
    }

    public boolean getEditAble() {
        if(StringUtil.isNotEmpty(campaignSts)) {
            if("RWEOC".indexOf(campaignSts) > -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public String getRelationTypeToStr() {
        String relationTypeToStr = "";
        if(StringUtil.isNotEmpty(relationType)) {
            if(relationType.equals("R")) {
                relationTypeToStr = "재발송";
            } else if(relationType.equals("L")) {
                relationTypeToStr = "반응";
            }
        }
        return relationTypeToStr;
    }

}
