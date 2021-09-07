package com.mnwise.wiseu.web.common.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class MimeViewVo {

    private String type;
    private String resultView;
    private String tid;
    private int serviceNo;
    private String customerKey;
    private String customerNm;
    private String customerEmail;
    private long resultSeq;
    private String seg;
    private String subType;
    private String mime;
    private String subject;
    private String from;
    private String to;
    private Map<String, Object> attachFiles;

    private String hanIdx;
    private String fileIdx;
    private String startIndex;
    private String endIndex;
    private String mimePath;
    private String attachFileNm;
    private String condition;
    private String jeonmun;
    private boolean resendButton;
    private int resendEcareNo;
    private String channel;
    private String seq;
    private String reqDt;
    private String reqTm;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSeg() {
        return seg;
    }

    public void setSeg(String seg) {
        this.seg = seg;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getResultView() {
        return resultView;
    }

    public void setResultView(String resultView) {
        this.resultView = resultView;
    }

    public String getAttachFileNm() {
        return attachFileNm;
    }

    public void setAttachFileNm(String attachFileNm) {
        this.attachFileNm = attachFileNm;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getHanIdx() {
        return hanIdx;
    }

    public void setHanIdx(String hanIdx) {
        this.hanIdx = hanIdx;
    }

    public String getFileIdx() {
        return fileIdx;
    }

    public void setFileIdx(String fileIdx) {
        this.fileIdx = fileIdx;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(Map<String, Object> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    public String getMimePath() {
        return mimePath;
    }

    public void setMimePath(String mimePath) {
        this.mimePath = mimePath;
    }

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public long getResultSeq() {
        return resultSeq;
    }

    public void setResultSeq(long resultSeq) {
        this.resultSeq = resultSeq;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setJeonmun(String jeonmun) {
        this.jeonmun = jeonmun;
    }

    public String getJeonmun() {
        return jeonmun;
    }

    public void setResendButton(boolean resendButton) {
        this.resendButton = resendButton;
    }

    public boolean isResendButton() {
        return resendButton;
    }

    public void setResendEcareNo(int resendEcareNo) {
        this.resendEcareNo = resendEcareNo;
    }

    public int getResendEcareNo() {
        return resendEcareNo;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSeq() {
        return seq;
    }

    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }

    public String getReqDt() {
        return reqDt;
    }

    public void setReqTm(String reqTm) {
        this.reqTm = reqTm;
    }

    public String getReqTm() {
        return reqTm;
    }

    /**
     * 첨부파일명을 BASE64로 인코딩하여 value에 넣어준다. (첨부파일 한글깨짐 처리를 위함)
     *
     * @return
     */
    public Map<String, String> getAttachFileNames() {
        Map<String, String> nameMap = new HashMap<>();
        if(attachFiles != null) {
            for(Iterator<String> i = attachFiles.keySet().iterator(); i.hasNext();) {
                String key = i.next();
                nameMap.put(key, new String(Base64.encodeBase64(key.getBytes())));
            }
        }
        return nameMap;
    }
}
