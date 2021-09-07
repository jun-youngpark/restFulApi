package com.mnwise.wiseu.web.ecare.model;

import java.util.Arrays;
import java.util.List;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.SearchVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

/**
 * NVECARESCENARIO 테이블 모델 클래스
 */
public class EcareScenarioVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private int scenarioNo;
    private String userId;
    private String grpCd;
    private String scenarioNm;
    private String scenarioDesc;
    private String scenarioType;
    private String createDt;
    private String createTm;
    private String lastUpdateDt;
    private String lastUpdateTm;
    private String finishYn;
    private String finishDt;
    private String serviceType;
    private int tagNo;
    private String subType;
    private String handlerType;
    private String chrgNm;
    private String brcNm;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String categoryCd;
    private String channelUseList;
    private String initChannels;
    private String channelCd;
    private String mailCd;
    private String smsCd;
    private String mmsCd;
    private String faxCd;
    private String alimtalkCd;
    private String pushCd;
    private String serviceTypeNm;
    private String segmentNm;
    private int segmentSize;
    private String subTypeNm;
    private int segmentNo;
    private boolean noChange;
    private int newScenarioNo;
    private int viewDepthNo;
    private int maxDepthNo;

    private int[] scenarioNoArray;
    private int[] scenarioIndexList;
    private String tagNm;
    private String[] channels;
    private String grpNm;
    private EcareVo ecareVo = new EcareVo();
    private List<EcareVo> ecareList;

    private String svcId;
    private String useServiceCdOrderby;

    private String verifyYn;

    private boolean serviceTypeNoChange;

    private String reqUser;
    private String reqDept;

    //webExecMode 1: wiseu / 2: wiseMoka
    private String webExecMode;
    private String smsIndividualCharge;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getScenarioNo() {
        return this.scenarioNo;
    }

    public void setScenarioNo(int scenarioNo) {
        this.scenarioNo = scenarioNo;
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

    public String getScenarioNm() {
        return this.scenarioNm;
    }

    public void setScenarioNm(String scenarioNm) {
        this.scenarioNm = scenarioNm;
    }

    public String getScenarioDesc() {
        return this.scenarioDesc;
    }

    public void setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
    }

    public String getScenarioType() {
        return this.scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
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

    public String getFinishYn() {
        return this.finishYn;
    }

    public void setFinishYn(String finishYn) {
        this.finishYn = finishYn;
    }

    public String getFinishDt() {
        return this.finishDt;
    }

    public void setFinishDt(String finishDt) {
        this.finishDt = finishDt;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public String getSubType() {
        return StringUtil.trimToNull(this.subType);
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getHandlerType() {
        return this.handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getChrgNm() {
        return this.chrgNm;
    }

    public void setChrgNm(String chrgNm) {
        this.chrgNm = chrgNm;
    }

    public String getBrcNm() {
        return this.brcNm;
    }

    public void setBrcNm(String brcNm) {
        this.brcNm = brcNm;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getReqUser() {
        return reqUser;
    }

    public void setReqUser(String reqUser) {
        this.reqUser = reqUser;
    }

    public String getReqDept() {
        return reqDept;
    }

    public void setReqDept(String reqDept) {
        this.reqDept = reqDept;
    }

    public String getSmsIndividualCharge() {
        return smsIndividualCharge;
    }

    public void setSmsIndividualCharge(String smsIndividualCharge) {
        this.smsIndividualCharge = smsIndividualCharge;
    }

    public String getVerifyYn() {
        return verifyYn;
    }

    public void setVerifyYn(String verifyYn) {
        this.verifyYn = verifyYn;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public int[] getScenarioIndexList() {
        return scenarioIndexList;
    }

    public void setScenarioIndexList(int[] scenarioIndexList) {
        this.scenarioIndexList = scenarioIndexList;
    }

    public String getCategoryCd() {
        return categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getChannelCd() {
        return channelCd;
    }

    public void setChannelCd(String channelCd) {
        this.channelCd = channelCd;
    }

    public List<EcareVo> getEcareList() {
        return ecareList;
    }

    public void setEcareList(List<EcareVo> ecareList) {
        this.ecareList = ecareList;
    }

    public EcareVo getEcareVo() {
        return ecareVo;
    }

    public void setEcareVo(EcareVo ecareVo) {
        this.ecareVo = ecareVo;
    }

    public String getInitChannels() {
        return initChannels;
    }

    public void setInitChannels(String initChannels) {
        this.initChannels = initChannels;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public String getMailCd() {
        return mailCd;
    }

    public void setMailCd(String mailCd) {
        this.mailCd = mailCd;
    }

    public String getSmsCd() {
        return smsCd;
    }

    public void setSmsCd(String smsCd) {
        this.smsCd = smsCd;
    }

    public String getMmsCd() {
        return mmsCd;
    }

    public void setMmsCd(String mmsCd) {
        this.mmsCd = mmsCd;
    }

    public String getFaxCd() {
        return faxCd;
    }

    public void setFaxCd(String faxCd) {
        this.faxCd = faxCd;
    }

    public String getAlimtalkCd() {
        return alimtalkCd;
    }

    public void setAlimtalkCd(String alimtalkCd) {
        this.alimtalkCd = alimtalkCd;
    }

    public String getPushCd() {
        return pushCd;
    }

    public void setPushCd(String pushCd) {
        this.pushCd = pushCd;
    }

    public String getServiceTypeNm() {
        return serviceTypeNm;
    }

    public void setServiceTypeNm(String serviceTypeNm) {
        this.serviceTypeNm = serviceTypeNm;
    }

    public String getSubTypeNm() {
        return subTypeNm;
    }

    public void setSubTypeNm(String subTypeNm) {
        this.subTypeNm = subTypeNm;
    }

    public int[] getScenarioNoArray() {
        return scenarioNoArray;
    }

    public void setScenarioNoArray(int[] scenarioNoArray) {
        this.scenarioNoArray = scenarioNoArray;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public String getSegmentNm() {
        return segmentNm;
    }

    public void setSegmentNm(String segmentNm) {
        this.segmentNm = segmentNm;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public int getChannelUseSize() {
        return channelUseList.length();
    }

    public String getChannelUseList() {
        return channelUseList;
    }

    public void setChannelUseList(String channelUseList) {
        this.channelUseList = channelUseList;
    }

    public boolean getNoChange() {
        return noChange;
    }

    public void setNoChange(boolean noChange) {
        this.noChange = noChange;
    }

    public int getNewScenarioNo() {
        return newScenarioNo;
    }

    public void setNewScenarioNo(int newScenarioNo) {
        this.newScenarioNo = newScenarioNo;
    }

    public int getViewDepthNo() {
        return viewDepthNo;
    }

    public void setViewDepthNo(int viewDepthNo) {
        this.viewDepthNo = viewDepthNo;
    }

    public int getMaxDepthNo() {
        return maxDepthNo;
    }

    public void setMaxDepthNo(int maxDepthNo) {
        this.maxDepthNo = maxDepthNo;
    }

    public String getUseServiceCdOrderby() {
        return useServiceCdOrderby == null ? "" : useServiceCdOrderby;
    }

    public void setUseServiceCdOrderby(String useServiceCdOrderby) {
        this.useServiceCdOrderby = useServiceCdOrderby;
    }

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public boolean isServiceTypeNoChange() {
        return serviceTypeNoChange;
    }

    public void setServiceTypeNoChange(boolean serviceTypeNoChange) {
        this.serviceTypeNoChange = serviceTypeNoChange;
    }


    public String getWebExecMode() {
        return webExecMode;
    }

    public void setWebExecMode(String webExecMode) {
        this.webExecMode = webExecMode;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getCreateDtToDateStr() {
        return FormatUtil.toBasicStrDate(createDt, createTm);
    }

    public String getLastUpdateDtToDateStr() {
        return FormatUtil.toBasicStrDate(lastUpdateDt, lastUpdateTm);
    }

    public String getNoChangeToString() {
        return String.valueOf(noChange);
    }

    public int getMaxEcareLevel() {
        int[] maxEcareLevel;
        if(ecareList != null) {
            maxEcareLevel = new int[ecareList.size()];
            for(int i = 0, n = ecareList.size(); i < n; i++) {
                EcareVo ecareVo = (EcareVo) ecareList.get(i);
                maxEcareLevel[i] = Integer.parseInt(ecareVo.getEcareLevel());
            }
            Arrays.sort(maxEcareLevel);
            return maxEcareLevel[maxEcareLevel.length - 1];
        }
        return 0;
    }

    public EcareVo getEcareByChannel() {
        if(ecareList != null) {
            for(int i = 0, n = ecareList.size(); i < n; i++) {
                EcareVo ecareVo = (EcareVo) ecareList.get(i);
                if(this.channelCd.equals(ecareVo.getChannelType())) {
                    return ecareVo;
                }
            }
        }
        return new EcareVo();
    }
}
