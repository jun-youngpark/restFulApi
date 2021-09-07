package com.mnwise.wiseu.web.campaign.model;

import java.util.Arrays;
import java.util.List;

import com.mnwise.wiseu.web.base.model.SearchVo;
import com.mnwise.wiseu.web.base.util.FormatUtil;

/**
 * NVSCENARIO 테이블 모델 클래스
 */
public class ScenarioVo extends SearchVo {
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
    private int tagNo;
    private String handlerType;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    // nvcampaign 테이블 컬럼
    private CampaignVo campaignVo = new CampaignVo();
    private int segmentNo;
    /** A/B테스트 발송 타입 [N:미사용, S:제목, T:템플릿] */
    private String abTestType;

    // nvsegment 테이블 컬럼
    private String segmentNm;
    private int segmentSize;
    private String segType;

    private String tagNm;
    private String grpNm;
    private int newScenarioNo;
    private int[] scenarioNoArray;
    private String channelCd;
    private List<CampaignVo> campaignList;
    private String channelUseList;
    private String[] channels;
    private String initChannels;
    private String mailCd;
    private String smsCd;
    private String mmsCd;
    private String faxCd;
    private String pushCd;
    private String friendtalkCd;
    private String brandtalkCd;
    private boolean noChange;
    private boolean needAgree;
    private boolean serviceTypeNoChange;
    private int viewDepthNo;
    private int maxDepthNo;
    private String reqGrpNm;
    private String siteNm;

    // SMS 부설별 개별 과금 관련 요청부서, 요청자 아이디 필드 추가
    private String reqDept;
    private String reqUser;

    private String pushAppId;

    //webExecMode 1: wiseu / 2: wiseMoka
    private String webExecMode;
    private String alimtalkCd;
    // 담당자, 담당부서
    private String chrgNm;
    private String brcNm;
    private String securityMailYn;

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

    public int getTagNo() {
        return this.tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public String getHandlerType() {
        return this.handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getAbTestType() {
        return abTestType;
    }

    public void setAbTestType(String abTestType) {
        this.abTestType = abTestType;
    }

    public CampaignVo getCampaignVo() {
        return campaignVo;
    }

    public void setCampaignVo(CampaignVo campaignVo) {
        this.campaignVo = campaignVo;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getBrandtalkCd() {
        return brandtalkCd;
    }

    public void setBrandtalkCd(String brandtalkCd) {
        this.brandtalkCd = brandtalkCd;
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

    private String smsIndividualCharge;

    public String getSmsIndividualCharge() {
        return smsIndividualCharge;
    }

    public void setSmsIndividualCharge(String smsIndividualCharge) {
        this.smsIndividualCharge = smsIndividualCharge;
    }

    public String getSegType() {
        return segType;
    }

    public void setSegType(String segType) {
        this.segType = segType;
    }

    public String getSegmentNm() {
        return segmentNm;
    }

    public void setSegmentNm(String segmentNm) {
        this.segmentNm = segmentNm;
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

    public String getPushCd() {
        return pushCd;
    }

    public void setPushCd(String pushCd) {
        this.pushCd = pushCd;
    }

    public String getFriendtalkCd() {
        return friendtalkCd;
    }

    public void setFriendtalkCd(String friendtalkCd) {
        this.friendtalkCd = friendtalkCd;
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

    public List<CampaignVo> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<CampaignVo> campaignList) {
        this.campaignList = campaignList;
    }

    public CampaignVo getCampaignByChannel() {
        if(campaignList != null) {
            for(int i = 0, n = campaignList.size(); i < n; i++) {
                CampaignVo campaignVo = (CampaignVo) campaignList.get(i);
                if(this.channelCd.equals(campaignVo.getChannelType())) {
                    return campaignVo;
                }
            }
        }
        return new CampaignVo();
    }

    public String getChannelCd() {
        return channelCd;
    }

    public void setChannelCd(String channelCd) {
        this.channelCd = channelCd;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public String getInitChannels() {
        return initChannels;
    }

    public void setInitChannels(String initChannels) {
        this.initChannels = initChannels;
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

    public int getNewScenarioNo() {
        return newScenarioNo;
    }

    public void setNewScenarioNo(int newScenarioNo) {
        this.newScenarioNo = newScenarioNo;
    }

    public String getCreateDtToDateStr() {
        return FormatUtil.toBasicStrDate(createDt, createTm);
    }

    public String getCreateDtToSimpleDateStr() {
        return FormatUtil.toYmdStrDate(createDt);
    }

    public String getLastUpdateDtToDateStr() {
        return FormatUtil.toBasicStrDate(lastUpdateDt, lastUpdateTm);
    }

    public int getMaxCampaignLevel() {
        int[] maxCampaignLevel;
        if(campaignList != null) {
            maxCampaignLevel = new int[campaignList.size()];
            for(int i = 0, n = campaignList.size(); i < n; i++) {
                CampaignVo campaignVo = (CampaignVo) campaignList.get(i);
                maxCampaignLevel[i] = Integer.parseInt(campaignVo.getCampaignLevel());
            }
            Arrays.sort(maxCampaignLevel);
            return maxCampaignLevel[maxCampaignLevel.length - 1];
        }
        return 0;
    }

    public boolean getNoChange() {
        return noChange;
    }

    public String getNoChangeToString() {
        return String.valueOf(noChange);
    }

    public void setNoChange(boolean noChange) {
        this.noChange = noChange;
    }

    public boolean isNeedAgree() {
        return needAgree;
    }

    public void setNeedAgree(boolean needAgree) {
        this.needAgree = needAgree;
    }

    public boolean isServiceTypeNoChange() {
        return serviceTypeNoChange;
    }

    public void setServiceTypeNoChange(boolean serviceTypeNoChange) {
        this.serviceTypeNoChange = serviceTypeNoChange;
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

    public String getReqGrpNm() {
        return reqGrpNm;
    }

    public void setReqGrpNm(String reqGrpNm) {
        this.reqGrpNm = reqGrpNm;
    }

    public String getSiteNm() {
        return siteNm;
    }

    public void setSiteNm(String siteNm) {
        this.siteNm = siteNm;
    }

    public String getPushAppId() {
        return pushAppId;
    }

    public void setPushAppId(String pushAppId) {
        this.pushAppId = pushAppId;
    }

    public String getWebExecMode() {
        return webExecMode;
    }

    public void setWebExecMode(String webExecMode) {
        this.webExecMode = webExecMode;
    }

    public String getAlimtalkCd() {
        return alimtalkCd;
    }

    public void setAlimtalkCd(String alimtalkCd) {
        this.alimtalkCd = alimtalkCd;
    }
    public String getChrgNm() {
        return chrgNm;
    }

    public void setChrgNm(String chrgNm) {
        this.chrgNm = chrgNm;
    }

    public String getBrcNm() {
        return brcNm;
    }

    public void setBrcNm(String brcNm) {
        this.brcNm = brcNm;
    }

    public String getSecurityMailYn() {
        return securityMailYn;
    }

    public void setSecurityMailYn(String securityMailYn) {
        this.securityMailYn = securityMailYn;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
}
