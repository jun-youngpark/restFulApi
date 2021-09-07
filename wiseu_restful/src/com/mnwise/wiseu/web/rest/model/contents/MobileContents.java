package com.mnwise.wiseu.web.rest.model.contents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class MobileContents extends BaseVo {
	private int contsNo;
	private String userId;
	private String contsNm;
	private String contsDesc;
	private String filePath;
	private String fileName;
	private String imageUrl;
	private String imageLink;
	private String detourFilePath;
	private String detourFileName;
	private String detourPreviewPath;
	private String detourPreviewName;
	private String fileType;
	private String createDt;
	private String createTm;
	private String authType;
	private String tagNo;
	private String contsTxt;
	private String filePreviewPath;
	private String filePreviewName;
	private String grpCd;
	private String categoryCd;
	private String fileSize;
	private String kakaoSenderKey;
	private String kakaoTmplCd;
	private String kakaoTmplStatus;
	private String kakaoInspStatus;
	private String kakaoButtons;
	private String useYn;
	private String contentType;
	private String unsubscribeContent;
	private String messageType;
	private String kakaoTmplMsgType;
	private String kakaoTmplAd;
	private String kakaoTmplEx;
	private String kakaoSecurityYn;
	private String kakaoEmType;
	private String kakaoEmTitle;
	private String kakaoEmSubtitle;
	private String kakaoQuickReplies;
	private String newKakaoSenderKey;
	private String newKakaoTmplCd;
	public String getFileSize() {
		return StringUtil.defaultIfBlank(fileSize, "0");
	}
}
