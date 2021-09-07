package com.mnwise.wiseu.web.rest.model.ecare;
/**
 * 이케어 테이블 Vo
 */

import static com.mnwise.wiseu.web.base.util.DateUtils.getToDate;
import static com.mnwise.wiseu.web.base.util.DateUtils.getToTime;
import static com.mnwise.wiseu.web.rest.model.Groups.update;
import static com.mnwise.wiseu.web.rest.model.Groups.create;
import static com.mnwise.wiseu.web.rest.model.Groups.delete;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnwise.wiseu.web.rest.annotation.Code;
import com.mnwise.wiseu.web.rest.common.Const;
import com.mnwise.wiseu.web.rest.model.BaseVo;
import com.mnwise.wiseu.web.rest.model.Groups.get;
import com.mnwise.wiseu.web.rest.model.env.DefaultHandler;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ecare extends BaseVo{

	public interface createEcOmni {};
	public interface updateEcState {};

	private int newEcareNo;
	@NotNull(groups = {createEcOmni.class,updateEcState.class,update.class,get.class})
	private int ecareNo;
	@NotNull(groups = {create.class})
	private String userId;
	@NotNull(groups = {create.class})
	private String grpCd;
	@Code(codes = {"M","S","T","A","C","P"} ,groups = {create.class})
	@NotNull(groups = {create.class,createEcOmni.class})
	private String channelType;
	private int segmentNo;
	@NotNull(groups = {create.class})
	private String ecareNm;
	private String ecareDesc;
	private String ecarePreface;
	//중지 P , 실행 R
	@Code(codes = { "P", "R" , "O"},groups = {updateEcState.class})
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
	private String keepday;
	private int ecmScheduleNo;
	private String senderNm;
	private String senderEmail;
	private String sendingMode;
	private int retryCnt;
	private String receiverNm;
	private String senderTel;
	private String retmailReceiver;
	private String ecareClass;
	@Code(codes = { "S", "F" , "O"},groups = {createEcOmni.class})
	private String relationType;
	private String relationTree;
	private String htmlmakerType;
	private String serviceType	= "S";
	private String accountDt;
	private String ecareLevel;
	private String categoryCd;
	private String resendYn;
	private int resendCnt;
	private String resendTm;
	private String svcId;
	@Code(codes = { Const.SubType.SCHDULE, Const.SubType.SCHDULE_M , Const.SubType.NREALTIME}
	,groups = {create.class})
	@NotNull(groups = {create.class})
	private String subType;
	private String surveyEndYn;
	private String surveyResponseCnt;
	private String surveyNo;
	private String surveyStartDt;
	private String surveyStartTm;
	private String surveyEndDt;
	private String surveyEndTm;
	private String depthNo;
	private String editorId;
	private int scenarioNo;
	private String verifyYn;
	private String verifyGrpCd;
	private String sendServer;
	private String blockYn;
	private String verifyB4Send;
	private String deleteYn;
	private String securityMailYn;
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
	private String deployType;
	private String mailType;
	private String openType;
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

	//핸들러
	private String handler;
	private String handlerType;
	//템플릿
	private String template;
	private String secutemplate;
	private String kakaoButtons;
	private String seg;
	//추가컬럼
	private String chrgNm;
	private String brcNm;
	private String grpNm;
	private String userNm;
	private String ecareStsNm;
	private String relationTypeNm;
	private String serviceTypeNm;
	private String subTypeNm;
	private String sendStartDt;	//스케줄 시작일자
	private String sendEndDt;	//스케줄 종료일자
	private String sendStartTm;	//스케줄 시작시간
	private String scheduleType;	//스케줄 유형
	private String scheduleSubType; //스케줄 sub 유형


	@NotNull(groups = {delete.class})
	@Code(codes = { "N", "Y"},groups = {delete.class})
	private String useYn;


	public void setDefaultHandler(DefaultHandler defaultHandler) throws Exception{
		handlerSeq = defaultHandler.getSeq();
		handler = defaultHandler.getHandler();
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
	}
	public String getSeg() {
		return StringUtils.defaultIfBlank(seg, Const.SPACE);
	}
	public String getHandlerType() {
		return StringUtils.defaultIfBlank(handlerType, Const.GROOVY);
	}
	public String getMailType(){
		return StringUtils.defaultIfBlank(mailType, Const.MailType.NONE);
	}
}
