package com.mnwise.wiseu.web.segment.model;

import com.mnwise.wiseu.web.base.model.SearchVo;
import com.mnwise.wiseu.web.common.model.Tag;

/**
 * 대상자 선택 모델 클래스
 */
public class BookmarkVo extends SearchVo {
    private static final long serialVersionUID = -6022752191152784829L;
    private int segmentNo;
    private String userId;
    private String grpCd;
    private String segmentNm;
    private int dbinfoSeq;
    private String segmentDesc;
    private String sqlHead;
    private String sqlTail;
    private String sqlBody;
    private String lastUpdateDt;
    private int segmentSize;
    private String crtgrpCd;
    private String segmentType;
    private String fileToDbYn;
    private String editorId;
    private String shareYn;
    private String activeYn;
    private String categoryCd;
    private String segmentSts;
    private String bookmarkKind;
    private String segType;
    // 채널에 따른 대상자 조회 용도
    private String semanticKey;

    private String grpNm;
    private Tag bookmarkTagVo;

    public Tag getBookmarkTagVo() {
        return bookmarkTagVo;
    }

    public void setBookmarkTagVo(Tag bookmarkTagVo) {
        this.bookmarkTagVo = bookmarkTagVo;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getSegmentNm() {
        return segmentNm;
    }

    public void setSegmentNm(String segmentNm) {
        this.segmentNm = segmentNm;
    }

    public int getDbinfoSeq() {
        return dbinfoSeq;
    }

    public void setDbinfoSeq(int dbinfoSeq) {
        this.dbinfoSeq = dbinfoSeq;
    }

    public String getSegmentDesc() {
        return segmentDesc;
    }

    public void setSegmentDesc(String segmentDesc) {
        this.segmentDesc = segmentDesc;
    }

    public String getSqlHead() {
        return sqlHead;
    }

    public void setSqlHead(String sqlHead) {
        this.sqlHead = sqlHead;
    }

    public String getSqlTail() {
        return sqlTail;
    }

    public void setSqlTail(String sqlTail) {
        this.sqlTail = sqlTail;
    }

    public String getSqlBody() {
        return sqlBody;
    }

    public void setSqlBody(String sqlBody) {
        this.sqlBody = sqlBody;
    }

    public String getLastUpdateDt() {
        return lastUpdateDt;
    }

    public void setLastUpdateDt(String lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public String getCrtgrpCd() {
        return crtgrpCd;
    }

    public void setCrtgrpCd(String crtgrpCd) {
        this.crtgrpCd = crtgrpCd;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public String getFileToDbYn() {
        return fileToDbYn;
    }

    public void setFileToDbYn(String fileToDbYn) {
        this.fileToDbYn = fileToDbYn;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getShareYn() {
        return shareYn;
    }

    public void setShareYn(String shareYn) {
        this.shareYn = shareYn;
    }

    public String getActiveYn() {
        return activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    public String getCategoryCd() {
        return categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getSegmentSts() {
        return segmentSts;
    }

    public void setSegmentSts(String segmentSts) {
        this.segmentSts = segmentSts;
    }

    public String getBookmarkKind() {
        return bookmarkKind;
    }

    public void setBookmarkKind(String bookmarkKind) {
        this.bookmarkKind = bookmarkKind;
    }

    public String getSegType() {
        return segType;
    }

    public void setSegType(String segType) {
        this.segType = segType;
    }

    public String getSemanticKey() {
        return semanticKey;
    }

    public void setSemanticKey(String semanticKey) {
        this.semanticKey = semanticKey;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }
}
