package com.mnwise.wiseu.web.editor.model;

/**
 * NVECAREKMMAP 테이블 모델 클래스
 */
public class EcareItemVo {
    private int ecareNo;
    private String itemfieldNm;
    private String knowledgemapId;
    private String grpCd;
    private String userId;
    private String itemCd;
    private String itemNm;
    private int itemindent;
    private int itemLength;
    private String itemType;
    private String itemFormat;
    private int querySeq;
    private String itemPramValue;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private String itemVal;

    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getEcareNo() {
        return this.ecareNo;
    }

    public void setEcareNo(int ecareNo) {
        this.ecareNo = ecareNo;
    }

    public String getItemfieldNm() {
        return this.itemfieldNm;
    }

    public void setItemfieldNm(String itemfieldNm) {
        this.itemfieldNm = itemfieldNm;
    }

    public String getKnowledgemapId() {
        return this.knowledgemapId;
    }

    public void setKnowledgemapId(String knowledgemapId) {
        this.knowledgemapId = knowledgemapId;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemCd() {
        return this.itemCd;
    }

    public void setItemCd(String itemCd) {
        this.itemCd = itemCd;
    }

    public String getItemNm() {
        return this.itemNm;
    }

    public void setItemNm(String itemNm) {
        this.itemNm = itemNm;
    }

    public int getItemindent() {
        return this.itemindent;
    }

    public void setItemindent(int itemindent) {
        this.itemindent = itemindent;
    }

    public int getItemLength() {
        return this.itemLength;
    }

    public void setItemLength(int itemLength) {
        this.itemLength = itemLength;
    }

    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemFormat() {
        return this.itemFormat;
    }

    public void setItemFormat(String itemFormat) {
        this.itemFormat = itemFormat;
    }

    public int getQuerySeq() {
        return this.querySeq;
    }

    public void setQuerySeq(int querySeq) {
        this.querySeq = querySeq;
    }

    public String getItemPramValue() {
        return this.itemPramValue;
    }

    public void setItemPramValue(String itemPramValue) {
        this.itemPramValue = itemPramValue;
    }

    /////////////////////////////////////////////////////////////////
    public String getItemVal() {
        return itemVal;
    }

    public void setItemVal(String itemVal) {
        this.itemVal = itemVal;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(knowledgemapId);
        sb.append("\t");
        sb.append(grpCd);
        sb.append("\t");
        sb.append(userId);
        sb.append("\t");
        sb.append(itemCd);
        sb.append("\t");
        sb.append(itemNm);
        sb.append("\t");
        sb.append(itemindent);
        sb.append("\t");
        sb.append(itemfieldNm);
        sb.append("\t");
        sb.append(itemLength);
        sb.append("\t");
        sb.append(itemType);
        sb.append("\t");
        sb.append(itemFormat);
        sb.append("\t");
        sb.append(querySeq);
        sb.append("\t");
        sb.append(itemVal);

        return sb.toString();
    }
}
