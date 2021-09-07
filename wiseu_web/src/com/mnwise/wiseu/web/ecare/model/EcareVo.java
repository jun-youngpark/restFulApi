package com.mnwise.wiseu.web.ecare.model;

import java.util.List;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.Const;
import com.mnwise.wiseu.web.base.model.BaseVo;
import com.mnwise.wiseu.web.campaign.model.DivideSchedule;
import com.mnwise.wiseu.web.editor.model.HandlerVo;
import com.mnwise.wiseu.web.editor.model.TemplateVo;

/**
 * NVECAREMSG 테이블 모델 클래스
 */
public class EcareVo extends BaseVo {
    private static final long serialVersionUID = -1L;
    private int ecareNo;
    private String userId;
    private String grpCd;
    private int segmentNo;
    private String ecareNm;
    private String ecareDesc;
    private String ecarePreface;
    private String ecareSts;
    private String createDt;
    private String createTm;
    private String lastUpdateDt;
    private String lastUpdateTm;
    private String campaignType;
    private int templateType;
    private String sendingSts;
    private int targetCnt;
    private String shareYn;
    private String msgassortCd;
    private String logYn;
    private int keepday;
    private int ecmScheduleNo;
    private String senderNm;
    private String senderEmail;
    private String sendingMode;
    private Integer retryCnt;
    private String receiverNm;
    private String senderTel;
    private String retmailReceiver;
    private String ecareClass;
    private String relationType;
    private String relationTree;
    private String channelType;
    private String htmlmakerType;
    private String serviceType;
    private String accountDt;
    private String ecareLevel;
    private String categoryCd;
    private String resendYn;
    private int resendCnt;
    private int resendTm;
    private String svcId;
    private String subType;
    private String surveyEndYn;
    private int surveyResponseCnt;
    private int surveyNo;
    private String surveyStartDt;
    private String surveyStartTm;
    private String surveyEndDt;
    private String surveyEndTm;
    private int depthNo;
    private String editorId;
    private int scenarioNo;
    private String verifyYn;
    private String verifyGrpCd;
    private int sendServer;
    private String blockYn;
    private String verifyB4Send;
    private String deleteYn;
    private String mailType;
    private String reqDeptId;
    private String reqUserId;
    private int resendEcareNo;
    private String templateSenderKey;
    private String kakaoSenderKey;
    private String kakaoTmplCd;
    private int kakaoImageNo;
    private String failbackSendYn;
    private String failbackSubject;
    private int tmplVer;
    private int coverVer;
    private int handlerVer;
    private int prefaceVer;
    private int handlerSeq;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String prevEcareSts;
    private String ecareStsNm;
    private String template;
    private String termType;
    private String relationTypeNm;
    private int newEcareNo;
    private String cardType;
    private int[] ecareNoArray;
    private int[] ecmScheduleNoArray;
    private EcareSendResultVo ecareSendResultVo = new EcareSendResultVo();
    private EcareScheduleVo ecareScheduleVo = new EcareScheduleVo();
    private SendCycleVo sendCycleVo = new SendCycleVo();
    private CycleItemVo cycleItemVo = new CycleItemVo();
    private String tagNm;
    private HandlerVo handlerVo;
    private List<TemplateVo> templateList;
    private List<CycleItemVo> cycleItemList;
    private String envSenderNm;
    private String envSenderEmail;
    private String envReceiverNm;
    private String envSenderTel;
    private String envRetmailReceiver;
    private String envSenderFax;
    private String envReceiverFax;
    private String verifyGrpNm;

    // 검증용 일시 정지, 오발송 방지 기능 사용 여부
    private String verifyBeforesend;
    private String serviceBlock;
    private String resultSeq;

    // SMS 부서별 발송 관련 컬럼 추가
    private String reqDept;
    private String reqUser;

    String resendEcareNm;

    // 20160418. 알림톡
    private String kakaoYellowId;
    // slot1~10 필드 추가
    private String slot1Field;
    private String slot2Field;
    private String slot3Field;
    private String slot4Field;
    private String slot5Field;
    private String slot6Field;
    private String slot7Field;
    private String slot8Field;
    private String slot9Field;
    private String slot10Field;
    private String deployType;
    private String emailOpenType;
    
    //[20210727] [ljh] [분할발송 추가]
    private String divideYn;
    private int divideInterval;
    private int divideCnt;
    private List<DivideSchedule> ecareDivideScheduleList;
    

    public String getDivideYn() {
        return divideYn;
    }

    public void setDivideYn(String divideYn) {
        this.divideYn = divideYn;
    }

    public int getDivideInterval() {
        return divideInterval;
    }

    public void setDivideInterval(int divideInterval) {
        this.divideInterval = divideInterval;
    }

    public int getDivideCnt() {
        return divideCnt;
    }

    public void setDivideCnt(int divideCnt) {
        this.divideCnt = divideCnt;
    }

    public List<DivideSchedule> getEcareDivideScheduleList() {
        return ecareDivideScheduleList;
    }

    public void setEcareDivideScheduleList(List<DivideSchedule> ecareDivideScheduleList) {
        this.ecareDivideScheduleList = ecareDivideScheduleList;
    }

    public String getSlot1Field() {
        return slot1Field;
    }

    public void setSlot1Field(String slot1Field) {
        this.slot1Field = slot1Field;
    }

    public String getSlot2Field() {
        return slot2Field;
    }

    public void setSlot2Field(String slot2Field) {
        this.slot2Field = slot2Field;
    }

    public String getSlot3Field() {
        return slot3Field;
    }

    public void setSlot3Field(String slot3Field) {
        this.slot3Field = slot3Field;
    }

    public String getSlot4Field() {
        return slot4Field;
    }

    public void setSlot4Field(String slot4Field) {
        this.slot4Field = slot4Field;
    }

    public String getSlot5Field() {
        return slot5Field;
    }

    public void setSlot5Field(String slot5Field) {
        this.slot5Field = slot5Field;
    }

    public String getSlot6Field() {
        return slot6Field;
    }

    public void setSlot6Field(String slot6Field) {
        this.slot6Field = slot6Field;
    }

    public String getSlot7Field() {
        return slot7Field;
    }

    public void setSlot7Field(String slot7Field) {
        this.slot7Field = slot7Field;
    }

    public String getSlot8Field() {
        return slot8Field;
    }

    public void setSlot8Field(String slot8Field) {
        this.slot8Field = slot8Field;
    }

    public String getSlot9Field() {
        return slot9Field;
    }

    public void setSlot9Field(String slot9Field) {
        this.slot9Field = slot9Field;
    }

    public String getSlot10Field() {
        return slot10Field;
    }

    public void setSlot10Field(String slot10Field) {
        this.slot10Field = slot10Field;
    }



    public String getDeployType() {
        return deployType;
    }

    public void setDeployType(String deployType) {
        this.deployType = deployType;
    }

    public String getEmailOpenType() {
        return emailOpenType;
    }

    public void setEmailOpenType(String emailOpenType) {
        this.emailOpenType = emailOpenType;
    }

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getEcareNo() {
        return this.ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public int getSegmentNo() {
        return this.segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getEcareNm() {
        return this.ecareNm;
    }

    public void setEcareNm(String ecareNm) {
        this.ecareNm = ecareNm;
    }

    public String getEcareDesc() {
        return this.ecareDesc;
    }

    public void setEcareDesc(String ecareDesc) {
        this.ecareDesc = ecareDesc;
    }

    public String getEcarePreface() {
        return this.ecarePreface;
    }

    public void setEcarePreface(String ecarePreface) {
        this.ecarePreface = ecarePreface;
    }

    public String getEcareSts() {
        return this.ecareSts;
    }

    public void setEcareSts(String ecareSts) {
        this.ecareSts = ecareSts;
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

    public int getTargetCnt() {
        return this.targetCnt;
    }

    public void setTargetCnt(int targetCnt) {
        this.targetCnt = targetCnt;
    }

    public String getShareYn() {
        return this.shareYn;
    }

    public void setShareYn(String shareYn) {
        this.shareYn = shareYn;
    }

    public String getMsgassortCd() {
        return this.msgassortCd;
    }

    public void setMsgassortCd(String msgassortCd) {
        this.msgassortCd = msgassortCd;
    }

    public String getLogYn() {
        return this.logYn;
    }

    public void setLogYn(String logYn) {
        this.logYn = logYn;
    }

    public int getKeepday() {
        return this.keepday;
    }

    public void setKeepday(int keepday) {
        this.keepday = keepday;
    }

    public int getEcmScheduleNo() {
        return this.ecmScheduleNo;
    }

    public void setEcmScheduleNo(int ecmScheduleNo) {
        this.ecmScheduleNo = ecmScheduleNo;
    }

    public String getSenderNm() {
        return StringUtil.defaultIfBlank(this.senderNm, this.envSenderNm);
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }

    public String getSenderEmail() {
        return this.senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSendingMode() {
        return this.sendingMode;
    }

    public void setSendingMode(String sendingMode) {
        this.sendingMode = sendingMode;
    }

    public Integer getRetryCnt() {
        return this.retryCnt;
    }

    public void setRetryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
    }

    public String getReceiverNm() {
        return StringUtil.defaultIfBlank(this.receiverNm, this.envReceiverNm);
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getSenderTel() {
        return this.senderTel;
    }

    public void setSenderTel(String senderTel) {
        this.senderTel = senderTel;
    }

    public String getRetmailReceiver() {
        return this.retmailReceiver;
    }

    public void setRetmailReceiver(String retmailReceiver) {
        this.retmailReceiver = retmailReceiver;
    }

    public String getEcareClass() {
        return this.ecareClass;
    }

    public void setEcareClass(String ecareClass) {
        this.ecareClass = ecareClass;
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

    public String getChannelType() {
        return this.channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getHtmlmakerType() {
        return this.htmlmakerType;
    }

    public void setHtmlmakerType(String htmlmakerType) {
        this.htmlmakerType = htmlmakerType;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAccountDt() {
        return this.accountDt;
    }

    public void setAccountDt(String accountDt) {
        this.accountDt = accountDt;
    }

    public String getEcareLevel() {
        return this.ecareLevel;
    }

    public void setEcareLevel(String ecareLevel) {
        this.ecareLevel = ecareLevel;
    }

    public String getCategoryCd() {
        return this.categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getResendYn() {
        return this.resendYn;
    }

    public void setResendYn(String resendYn) {
        this.resendYn = resendYn;
    }

    public int getResendCnt() {
        return this.resendCnt;
    }

    public void setResendCnt(int resendCnt) {
        this.resendCnt = resendCnt;
    }

    public int getResendTm() {
        return this.resendTm;
    }

    public void setResendTm(int resendTm) {
        this.resendTm = resendTm;
    }

    public String getSvcId() {
        return this.svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getSubType() {
        return StringUtil.trimToNull(this.subType);
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSurveyEndYn() {
        return this.surveyEndYn;
    }

    public void setSurveyEndYn(String surveyEndYn) {
        this.surveyEndYn = surveyEndYn;
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

    public int getScenarioNo() {
        return this.scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
    }

    public String getVerifyYn() {
        return this.verifyYn;
    }

    public void setVerifyYn(String verifyYn) {
        this.verifyYn = verifyYn;
    }

    public String getVerifyGrpCd() {
        return this.verifyGrpCd;
    }

    public void setVerifyGrpCd(String verifyGrpCd) {
        this.verifyGrpCd = verifyGrpCd;
    }

    public int getSendServer() {
        return this.sendServer;
    }

    public void setSendServer(int sendServer) {
        this.sendServer = sendServer;
    }

    public String getBlockYn() {
        return StringUtil.defaultIfBlank(this.blockYn, "N");
    }

    public void setBlockYn(String blockYn) {
        this.blockYn = blockYn;
    }

    public String getVerifyB4Send() {
        return this.verifyB4Send;
    }

    public void setVerifyB4Send(String verifyB4Send) {
        this.verifyB4Send = verifyB4Send;
    }

    public String getDeleteYn() {
        return this.deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getMailType() {
        return this.mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = StringUtil.defaultIfBlank(mailType, Const.MailType.NONE);
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

    public int getResendEcareNo() {
        return this.resendEcareNo;
    }

    public void setResendEcareNo(int resendEcareNo) {
        this.resendEcareNo = resendEcareNo;
    }

    public String getTemplateSenderKey() {
        return this.templateSenderKey;
    }

    public void setTemplateSenderKey(String templateSenderKey) {
        this.templateSenderKey = templateSenderKey;
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

    public int getTmplVer() {
        return this.tmplVer;
    }

    public void setTmplVer(int tmplVer) {
        this.tmplVer = tmplVer;
    }

    public int getCoverVer() {
        return this.coverVer;
    }

    public void setCoverVer(int coverVer) {
        this.coverVer = coverVer;
    }

    public int getHandlerVer() {
        return this.handlerVer;
    }

    public void setHandlerVer(int handlerVer) {
        this.handlerVer = handlerVer;
    }

    public int getPrefaceVer() {
        return this.prefaceVer;
    }

    public void setPrefaceVer(int prefaceVer) {
        this.prefaceVer = prefaceVer;
    }

    public int getHandlerSeq() {
        return this.handlerSeq;
    }

    public void setHandlerSeq(int handlerSeq) {
        this.handlerSeq = handlerSeq;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getKakaoYellowId() {
        return kakaoYellowId;
    }

    public void setKakaoYellowId(String kakaoYellowId) {
        this.kakaoYellowId = kakaoYellowId;
    }

    public String getResendEcareNm() {
        return resendEcareNm;
    }

    public void setResendEcareNm(String resendEcareNm) {
        this.resendEcareNm = resendEcareNm;
    }

    public String getReqDept() {
        return reqDept;
    }

    public void setReqDept(String reqDept) {
        this.reqDept = reqDept;
    }

    public String getReqUser() {
        return reqUser;
    }

    public void setReqUser(String reqUser) {
        this.reqUser = reqUser;
    }

    public String getVerifyGrpNm() {
        return verifyGrpNm;
    }

    public void setVerifyGrpNm(String verifyGrpNm) {
        this.verifyGrpNm = verifyGrpNm;
    }

    public EcareScheduleVo getEcareScheduleVo() {
        return ecareScheduleVo;
    }

    public void setEcareScheduleVo(EcareScheduleVo ecareScheduleVo) {
        this.ecareScheduleVo = ecareScheduleVo;
    }

    public SendCycleVo getSendCycleVo() {
        return sendCycleVo;
    }

    public void setSendCycleVo(SendCycleVo sendCycleVo) {
        this.sendCycleVo = sendCycleVo;
    }

    public CycleItemVo getCycleItemVo() {
        return cycleItemVo;
    }

    public void setCycleItemVo(CycleItemVo cycleItemVo) {
        this.cycleItemVo = cycleItemVo;
    }

    public EcareSendResultVo getEcareSendResultVo() {
        return ecareSendResultVo;
    }

    public void setEcareSendResultVo(EcareSendResultVo ecareSendResultVo) {
        this.ecareSendResultVo = ecareSendResultVo;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public String getPrevEcareSts() {
        return prevEcareSts;
    }

    public void setPrevEcareSts(String prevEcareSts) {
        this.prevEcareSts = prevEcareSts;
    }

    public int[] getEcareNoArray() {
        return ecareNoArray;
    }

    public void setEcareNoArray(int[] ecareNoArray) {
        this.ecareNoArray = ecareNoArray;
    }

    public void setTargetCnt(Integer targetCnt) {
        this.targetCnt = targetCnt;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public int[] getEcmScheduleNoArray() {
        return ecmScheduleNoArray;
    }

    public void setEcmScheduleNoArray(int[] ecmScheduleNoArray) {
        this.ecmScheduleNoArray = ecmScheduleNoArray;
    }

    public HandlerVo getHandlerVo() {
        return handlerVo;
    }

    public void setHandlerVo(HandlerVo handlerVo) {
        this.handlerVo = handlerVo;
    }

    public List<TemplateVo> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<TemplateVo> templateList) {
        this.templateList = templateList;
    }

    public String getEcareStsNm() {
        return ecareStsNm;
    }

    public void setEcareStsNm(String ecareStsNm) {
        this.ecareStsNm = ecareStsNm;
    }

    public List<CycleItemVo> getCycleItemList() {
        return cycleItemList;
    }

    public void setCycleItemList(List<CycleItemVo> cycleItemList) {
        this.cycleItemList = cycleItemList;
    }

    public String getRelationTypeNm() {
        return relationTypeNm;
    }

    public void setRelationTypeNm(String relationTypeNm) {
        this.relationTypeNm = relationTypeNm;
    }

    public int getNewEcareNo() {
        return newEcareNo;
    }

    public void setNewEcareNo(int newEcareNo) {
        this.newEcareNo = newEcareNo;
    }

    public boolean getEditAble() {
        if(StringUtil.isNotEmpty(ecareSts)) {
            if("RC".indexOf(ecareSts) > -1) {
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

    public String getEnvSenderFax() {
        return envSenderFax;
    }

    public void setEnvSenderFax(String envSenderFax) {
        this.envSenderFax = envSenderFax;
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

    public String getVerifyBeforesend() {
        return verifyBeforesend;
    }

    public void setVerifyBeforesend(String verifyBeforesend) {
        this.verifyBeforesend = verifyBeforesend;
    }

    public String getServiceBlock() {
        return serviceBlock;
    }

    public void setServiceBlock(String serviceBlock) {
        this.serviceBlock = serviceBlock;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }

}
