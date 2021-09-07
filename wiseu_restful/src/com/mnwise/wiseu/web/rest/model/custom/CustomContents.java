package com.mnwise.wiseu.web.rest.model.custom;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.rest.model.BaseVo;
import com.mnwise.wiseu.web.rest.model.Groups;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

/**
 * 고객사별 발송결과 테이블 커스터마이징 필드 추가
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class CustomContents extends BaseVo {
	@NotNull(groups = {Groups.create.class})
	private String templateType;
	@NotNull(groups = {Groups.create.class})
	private String templateName;
	@NotNull(groups = {Groups.update.class})
	private int contsNo;
	@NotNull(groups = {Groups.create.class})
	private String userId;
	@NotNull(groups = {Groups.create.class})
	private String grpCd;
	private String templateCode;
	private String contsNm;
	private String contsDesc;
	private String filePath;
	private String fileName;
	private String fileUrlName;
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
	private String template;
	private String filePreviewPath;
	private String filePreviewName;
	private String categoryCd;
	private String fileSize;
	private String kakaoSenderKey;
	private String kakaoTmplCd;
	private String kakaoTmplStatus;
	private String kakaoInspStatus;
	private String button;
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

	public void setContsNm(String contsNm) {
		this.contsNm = StringUtil.escapeXss(contsNm);
	}
	public void setTemplate(String template) {
		this.template = StringUtil.escapeXss(template);
	}
	public String getKakaoTmplMsgType() {
		return StringUtils.defaultIfBlank(kakaoTmplMsgType, "BA");
	}
	public String getKakaoEmType() {
		return StringUtils.defaultIfBlank(kakaoEmType, "NONE");
	}
	public String getAuthType() {
		return StringUtils.defaultIfBlank(authType, "A");
	}
	public String getCategoryCd() {
		return StringUtils.defaultIfBlank(categoryCd, "999999");
	}
	
}
