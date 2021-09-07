package com.mnwise.wiseu.web.rest.model.env;

import lombok.Getter;
import lombok.Setter;

/**
 * 기본핸들러 테이블 Vo
 */
@Setter
@Getter
public class DefaultHandler {
	private int seq;
	private String handlerNm;
	private String handlerDesc;
	private String serviceType;
	private String channel;
	private String handleType;
	private String handleAttr;
	private String userId;
	private String createDt;
	private String createTm;
	private String handler;
	private String abTestYn;
	private String msgType;
}
