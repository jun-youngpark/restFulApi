package com.mnwise.wiseu.web.template.model;

import static com.mnwise.common.util.DateUtil.dateToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.multipart.MultipartFile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandtalkTemplateVo extends MobileVo {
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String templateName;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private String status;
    private List<KakaoButton> buttons;
    private MultipartFile brandTalkImageFile;
    private String senderKey;
    private String inspectionStatus;

    /////////////////////////////////////////////////////////////////
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public String getCode() {
        return code;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<KakaoButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<KakaoButton> buttons) {
        this.buttons = buttons;
    }

    public MultipartFile getBrandTalkImageFile() {
        return brandTalkImageFile;
    }

    public void setBrandTalkImageFile(MultipartFile brandTalkImageFile) {
        this.brandTalkImageFile = brandTalkImageFile;
    }

    /////////////////////////////////////////////////////////////////
    @Override
    @JsonProperty()
    public void setNowPage(int p_nowPage) {
        this.nowPage = p_nowPage;
    }

    public String convertButtonsToJson() {
        if(buttons == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for(KakaoButton button : buttons) {
            sb.append(button.toJsonString()).append(",");
        }

        sb.append("]");

        return sb.toString().replaceAll(",]", "]").replaceAll("\\[\\]", "");
    }

    public void setTime() {
        final String date = dateToString("yyyMMddHHmmss", new Date());
        setCreateDt(date.substring(0, 8));
        setCreateTm(date.substring(8));
    }

    public void setFile(Map<String, String> fileUploadMap) {
        if(fileUploadMap != null) {
            setFileName(fileUploadMap.get("fileNm"));
            setFilePath(fileUploadMap.get("filePath"));
            setFilePreviewPath(fileUploadMap.get("filePreviewPath"));
        }
    }
}
