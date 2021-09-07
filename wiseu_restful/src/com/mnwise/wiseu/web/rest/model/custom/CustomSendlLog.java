package com.mnwise.wiseu.web.rest.model.custom;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.ecare.EcareSendlLog;

import lombok.Getter;
import lombok.Setter;

/**
 * 고객사별 발송결과 테이블 커스터마이징 필드 추가
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class CustomSendlLog extends EcareSendlLog {

	@NotBlank
	private String startDate;
	@NotBlank
	private String endDate;
	private String reqSeq;
	private String channel;


	@Getter
    public static class ResponseSendlog {
        private int seq;
        private String channel;
        private String errorCd;
        private String errMsg;
        private String customerKey;
        private String cutomerNm;
        private String customerEmail;
        private String sendDt;
        private String sendTm;
        private String openDt;
        private String slot1;
        private String slot2;
    }

}
