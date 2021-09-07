package com.mnwise.wiseu.web.template.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mnwise.wiseu.web.base.model.SearchVo;

/**
 * NVMOBILECONTENTS 테이블 모델 클래스
 */
public class MobileVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    protected int contsNo;
    protected String userId;
    protected String contsNm;
    protected String contsDesc;
    protected String filePath = "";
    protected String fileName = "";
    protected String imageUrl = "";
    protected String imageLink = "";
    protected String detourFilePath = "";
    protected String detourFileName = "";
    protected String detourPreviewPath = "";
    protected String detourPreviewName = "";
    protected String fileType;
    protected String createDt;
    protected String createTm;
    protected String authType;
    protected int tagNo;
    protected String contsTxt = "";
    protected String filePreviewPath = "";
    protected String filePreviewName = "";
    protected String grpCd;
    protected String categoryGroup;
    protected String categoryCd;
    protected long fileSize;
    protected String kakaoSenderKey = "";
    protected String kakaoTmplCd = "";
    protected String kakaoTmplStatus;
    protected String kakaoInspStatus;
    protected String kakaoButtons;
    protected String kakaoTmplMsgType;
    protected String kakaoTmplAd;
    protected String kakaoTmplEx;
    protected String kakaoSecurityYn;
    protected String kakaoEmType;
    protected String kakaoEmTitle;
    protected String kakaoEmSubtitle;
    protected String kakaoQuickReplies;
    protected String useYn;
    protected String contentType;
    protected String unsubscribeContent;
    protected String messageType;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    protected String userNm;
    protected String grpNm;
    protected String tagNm;
    protected String command;
    protected int[] mobileNoArray;
    protected String kakaoInspStatusNm;
    protected String kakaoYellowId = "";
    protected String kakaoTmplStatusNm;
    protected String newKakaoSenderKey = "";
    protected String newKakaoTmplCd = "";
    protected List<KakaoTemplateComment> kakaoTemplateComments;
    protected String subType;

    protected MultipartFile kakaoImage;


    public MultipartFile getKakaoImage() {
        return kakaoImage;
    }

    public void setKakaoImage(MultipartFile kakaoImage) {
        this.kakaoImage = kakaoImage;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    /////////////////////////////////////////////////////////////////
    public int getContsNo() {
        return this.contsNo;
    }

    public void setContsNo(int contsNo) {
        this.contsNo = contsNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContsNm() {
        return this.contsNm;
    }

    public void setContsNm(String contsNm) {
        this.contsNm = contsNm;
    }

    public String getContsDesc() {
        return this.contsDesc;
    }

    public void setContsDesc(String contsDesc) {
        this.contsDesc = contsDesc;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDetourFilePath() {
        return this.detourFilePath;
    }

    public void setDetourFilePath(String detourFilePath) {
        this.detourFilePath = detourFilePath;
    }

    public String getDetourFileName() {
        return this.detourFileName;
    }

    public void setDetourFileName(String detourFileName) {
        this.detourFileName = detourFileName;
    }

    public String getDetourPreviewPath() {
        return this.detourPreviewPath;
    }

    public void setDetourPreviewPath(String detourPreviewPath) {
        this.detourPreviewPath = detourPreviewPath;
    }

    public String getDetourPreviewName() {
        return this.detourPreviewName;
    }

    public void setDetourPreviewName(String detourPreviewName) {
        this.detourPreviewName = detourPreviewName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public String getAuthType() {
        return this.authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public String getContsTxt() {
        return this.contsTxt;
    }

    public void setContsTxt(String contsTxt) {
        this.contsTxt = contsTxt;
    }

    public String getFilePreviewPath() {
        return this.filePreviewPath;
    }

    public void setFilePreviewPath(String filePreviewPath) {
        this.filePreviewPath = filePreviewPath;
    }

    public String getFilePreviewName() {
        return this.filePreviewName;
    }

    public void setFilePreviewName(String filePreviewName) {
        this.filePreviewName = filePreviewName;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getCategoryCd() {
        return this.categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

    public String getKakaoTmplStatus() {
        return this.kakaoTmplStatus;
    }

    public void setKakaoTmplStatus(String kakaoTmplStatus) {
        this.kakaoTmplStatus = kakaoTmplStatus;
    }

    public String getKakaoInspStatus() {
        return this.kakaoInspStatus;
    }

    public void setKakaoInspStatus(String kakaoInspStatus) {
        this.kakaoInspStatus = kakaoInspStatus;
    }

    public String getKakaoButtons() {
        return this.kakaoButtons;
    }

    public void setKakaoButtons(String kakaoButtons) {
        this.kakaoButtons = kakaoButtons;
    }

    public String getUseYn() {
        return this.useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUnsubscribeContent() {
        return this.unsubscribeContent;
    }

    public void setUnsubscribeContent(String unsubscribeContent) {
        this.unsubscribeContent = unsubscribeContent;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public int[] getMobileNoArray() {
        return mobileNoArray;
    }

    public void setMobileNoArray(int[] mobileNoArray) {
        this.mobileNoArray = mobileNoArray;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getKakaoYellowId() {
        return kakaoYellowId;
    }

    public void setKakaoYellowId(String kakaoYellowId) {
        this.kakaoYellowId = kakaoYellowId;
    }

    public String getKakaoInspStatusNm() {
        return kakaoInspStatusNm;
    }

    public void setKakaoInspStatusNm(String kakaoInspStatusNm) {
        this.kakaoInspStatusNm = kakaoInspStatusNm;
    }

    public String getKakaoTmplStatusNm() {
        return kakaoTmplStatusNm;
    }

    public void setKakaoTmplStatusNm(String kakaoTmplStatusNm) {
        this.kakaoTmplStatusNm = kakaoTmplStatusNm;
    }

    public List<KakaoTemplateComment> getKakaoTemplateComments() {
        return kakaoTemplateComments;
    }

    public void setKakaoTemplateComments(List<KakaoTemplateComment> kakaoTemplateComments) {
        this.kakaoTemplateComments = kakaoTemplateComments;
    }

    public String getNewKakaoSenderKey() {
        return newKakaoSenderKey;
    }

    public void setNewKakaoSenderKey(String newKakaoSenderKey) {
        this.newKakaoSenderKey = newKakaoSenderKey;
    }

    public String getNewKakaoTmplCd() {
        return newKakaoTmplCd;
    }

    public void setNewKakaoTmplCd(String newKakaoTmplCd) {
        this.newKakaoTmplCd = newKakaoTmplCd;
    }

    public String getKakaoTmplMsgType() {
        return kakaoTmplMsgType;
    }

    public void setKakaoTmplMsgType(String kakaoTmplMsgType) {
        this.kakaoTmplMsgType = kakaoTmplMsgType;
    }

    public String getKakaoTmplAd() {
        return kakaoTmplAd;
    }

    public void setKakaoTmplAd(String kakaoTmplAd) {
        this.kakaoTmplAd = kakaoTmplAd;
    }

    public String getKakaoTmplEx() {
        return kakaoTmplEx;
    }

    public void setKakaoTmplEx(String kakaoTmplEx) {
        this.kakaoTmplEx = kakaoTmplEx;
    }

    public String getKakaoSecurityYn() {
        return kakaoSecurityYn;
    }

    public void setKakaoSecurityYn(String kakaoSecurityYn) {
        this.kakaoSecurityYn = kakaoSecurityYn;
    }

    public String getKakaoEmType() {
        return kakaoEmType;
    }

    public void setKakaoEmType(String kakaoEmType) {
        this.kakaoEmType = kakaoEmType;
    }

    public String getKakaoEmTitle() {
        return kakaoEmTitle;
    }

    public void setKakaoEmTitle(String kakaoEmTitle) {
        this.kakaoEmTitle = kakaoEmTitle;
    }

    public String getKakaoEmSubtitle() {
        return kakaoEmSubtitle;
    }

    public void setKakaoEmSubtitle(String kakaoEmSubtitle) {
        this.kakaoEmSubtitle = kakaoEmSubtitle;
    }

    public String getKakaoQuickReplies() {
        return kakaoQuickReplies;
    }

    public void setKakaoQuickReplies(String kakaoQuickReplies) {
        this.kakaoQuickReplies = kakaoQuickReplies;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }
}
