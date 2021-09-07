package com.mnwise.wiseu.web.rest.dto;

import lombok.Data;

public class ReturnDto {

	@Data
	public static class CampaignDto{
		private int campaignNo;
		private String channelType;
		private String campaignNm;
		private String campaignPreface;
		private String createDt;
		private String createTm;
		private String grpNm;
		private String userNm;
		private int targetCnt;
		private String useYn;
		private String campaignSts;
		private String campaignStsNm;
		private int segmentNo;
		private String relationType;
		private String relationTypeNm;
		private String divideYn;
		private int divideInterval;
		private int divideCnt;
		private String senderEmail;
		private String senderNm;
		private String senderTel;
		private String sendstartDtm;
		private String kakaoSenderKey;
		private String failbackSendYn;
		private String failbackSubject;
		private int depthNo;
		private String kakaoButtons;
		private String template;
		private int scenarioNo;
		private String kakaoImageNo;
		private String relationTree;
	}

	@Data
	public static class EcareDto{
		private int ecareNo;
		private String channelType;
		private String ecareNm;
		private String ecarePreface;
		private String createDt;
		private String createTm;
		private String grpNm;
		private String userNm;
		private int targetCnt;
		private String useYn;
		private String ecareSts;
		private String ecareStsNm;
		private int segmentNo;
		private String relationType;
		private String relationTypeNm;
		private String divideYn;
		private int divideInterval;
		private int divideCnt;
		private String senderEmail;
		private String senderNm;
		private String senderTel;
		private String sendstartDt;
		private String sendstartTm;
		private String invokeTm;
		private String kakaoSenderKey;
		private String failbackSendYn;
		private String failbackSubject;
		private int depthNo;
		private String kakaoButtons;
		private String template;

		private String secuTemplate;
		private String kakaoTmplCd;
		private String deployType;
		private String mailType;
		private String openType;

		private int scenarioNo;
		private String kakaoImageNo;
		private String relationTree;


	}

}
