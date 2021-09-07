package com.mnwise.wiseu.web.rest.model.campaign;

import static com.mnwise.wiseu.web.base.util.DateUtils.getToDate;
import static com.mnwise.wiseu.web.base.util.DateUtils.getToTime;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnwise.wiseu.web.rest.annotation.Code;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.model.BaseVo;
import com.mnwise.wiseu.web.rest.model.Groups.create;
import com.mnwise.wiseu.web.rest.model.Groups.delete;
import com.mnwise.wiseu.web.rest.model.Groups.get;
import com.mnwise.wiseu.web.rest.model.Groups.update;
import com.mnwise.wiseu.web.rest.model.env.DefaultHandler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * 캠페인 테이블 Vo
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Campaign extends BaseVo{

	public interface updateState {};
	public interface createOmni {};

	private int newCampaignNo;

	@NotNull(groups = {update.class,delete.class
			,updateState.class,createOmni.class,get.class})
	private int campaignNo;

	@NotNull(groups = {create.class})
	private String grpCd;
	@NotNull(groups = {create.class})
	private String userId;
	private int segmentNo;
	@NotNull(groups = {create.class})
	private String campaignNm;
	private String campaignDesc;
	private String campaignPreface;
	@NotNull(groups = {updateState.class})
	//중지 P , 실행 R
	@Code(codes = { "P", "R"},groups = {updateState.class})
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
	private String sendfinishDt;
	private String sendfinishTm;
	private int targetCnt;
	private String surveyEndYn;
	private String sendingMode;
	private int surveyResponseCnt;
	private int surveyNo;
	private String logYn;
	private int keepday;
	private String shareYn;
	private String surveyStartDt;
	private String surveyStartTm;
	private String surveyEndDt;
	private String surveyEndTm;
	private int retryCnt;
	private String rptcreateDt;
	private String senderNm;
	private String senderEmail;
	private String receiverNm;
	private String senderTel;
	private String retmailReceiver;
	private String htmlupdateYn;
	private String reportSts;
	private String campaignClass;
	@NotNull(groups = {create.class, createOmni.class})
	private String channelType;
	@Code(codes = { "S", "F" , "O"},groups = {createOmni.class})
	@NotNull(groups = {createOmni.class})
	private String relationType;	// relationType 'S': 성공, 'F': 실패, 'O': 오픈
	private String relationTree;
	private String retmailSendYn;
	private String etcInfo1;
	private String etcInfo2;
	private String promotionType;
	private String campaignLevel;
	private String categoryCd;
	private int scenarioNo;
	private int depthNo;
	private String editorId;
	private String sendServer;
	private String approvalSts;
	private String reqDeptId;
	private String reqUserId;
	private String divideYn;
	private int divideInterval;
	private int divideCnt;
	private String abTestType;
	private String abTestCond;
	private String abTestRate;
	private String campaignPrefaceAb;
	private String multiContInfo;
	private int handlerSeq;
	private String kakaoSenderKey;
	private String kakaoTmplCd;
	private int kakaoImageNo;
	private String failbackSendYn;
	private String failbackSubject;
	@NotNull(groups = {delete.class})
	@Code(codes = { "N", "Y"},groups = {delete.class})
	private String useYn;

	//시나리오
	private String reqUser;
	private String reqDept;

	//스케줄
	private int scheduleNo;
	//핸들러
	private String handler;
	private String handlerType;
	//템플릿
	private String template;
	private String kakaoButtons;
	private String seg;
	//추가 컬럼
	private String grpNm;
	private String userNm;
	private String campaignStsNm;
	private String relationTypeNm;


	public void setDefaultHandler(DefaultHandler defaultHandler) throws Exception{
		handlerSeq = defaultHandler.getSeq();
		handler = defaultHandler.getHandler();
	}

	public String getFailbackSendYn() {
		return StringUtils.defaultIfBlank(failbackSendYn, "N");
	}

	public String getCreateDt() {
		return StringUtils.defaultIfBlank(createDt, getToDate());
	}

	public String getCreateTm() {
		return StringUtils.defaultIfBlank(createTm, getToTime());
	}

	public String getLastUpdateDt() {
		return StringUtils.defaultIfBlank(lastUpdateDt, getToDate());
	}

	public String getLastUpdateTm() {
		return StringUtils.defaultIfBlank(lastUpdateTm, getToTime());
	};

	public String getSeg() {
		return StringUtils.defaultIfBlank(seg, Const.SPACE);
	}

	public String getHandlerType() {
		return StringUtils.defaultIfBlank(handlerType, Const.GROOVY);
	}

	public String getAbTestType() {
		return StringUtils.defaultIfBlank(abTestType, "N");
	}




}

