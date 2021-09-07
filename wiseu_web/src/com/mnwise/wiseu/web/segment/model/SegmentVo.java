package com.mnwise.wiseu.web.segment.model;

import java.util.List;

import com.mnwise.common.util.StringUtil;
import com.mnwise.wiseu.web.base.model.SearchVo;


/**
 * NVSEGMENT 테이블 모델 클래스
 */
public class SegmentVo extends SearchVo {
    private static final long serialVersionUID = -1L;
    private int segmentNo;
    private String userId;
    private String grpCd;
    private String segmentNm;
    private int dbinfoSeq;
    private String segmentDesc;
    private String sqlHead;
    private String sqlTail;
    private String sqlBody;
    private String sqlfilter;
    private String lastUpdateDt;
    private int segmentSize;
    private String crtGrpCd;
    private String segmentType;
    private String fileToDbYn;
    private String editorId;
    private String shareYn;
    private String activeYn;
    private String categoryCd;
    private String segmentSts;
    private int tagNo;
    private int psegmentNo;
    private String plinkSeq;
    private String segType;
    private String testQuery;
    private String updateQuery;

    /////////////////////////////////////////////////////////////////
    // 추가 멤버변수
    private int newSegmentNo;
    private String nameKor; // 작성자명 표시를 위해 추가
    private String[] grpCdArr;
    private String serverNm;
    private String sqlWebHead;
    private String sqlWebTail;
    private String sqlWebBody;
    private String webTestQuery;
    private String bookmarkSeg;
    private String bookmarkYn;
    private String tagNm;
    // private int pserviceNo;
    private int targetNo;
    private SemanticVo semanticVo;
    private List<SemanticVo> semanticList;
    private DbInfoVo dbInfoVo;
    private List<DbInfoVo> dbInfoList;
    private String sqlContext; // Sql 전체 문장
    private int permissionCount;
    private String serviceSts;
    private String delimiter;
    private String inMetaData;
    private boolean largeUpload;
    private String fieldKey;
    private String errorMsg;
    private String reject;
    private String webUpdateQuery;

    public SegmentVo() {}
    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getSegmentNo() {
        return this.segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrpCd() {
        return this.grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getSegmentNm() {
        return this.segmentNm;
    }

    public void setSegmentNm(String segmentNm) {
        this.segmentNm = segmentNm;
    }

    public int getDbinfoSeq() {
        return this.dbinfoSeq;
    }

    public void setDbinfoSeq(int dbinfoSeq) {
        this.dbinfoSeq = dbinfoSeq;
    }

    public String getSegmentDesc() {
        return this.segmentDesc;
    }

    public void setSegmentDesc(String segmentDesc) {
        this.segmentDesc = segmentDesc;
    }

    public String getSqlHead() {
        return StringUtil.replaceEntities(this.sqlHead);
    }

    public void setSqlHead(String sqlHead) {
        this.sqlHead = sqlHead;
        setSqlWebHead(sqlHead);
    }

    public String getSqlTail() {
        return StringUtil.replaceEntities(this.sqlTail);
    }

    public void setSqlTail(String sqlTail) {
        this.sqlTail = sqlTail;
        setSqlWebTail(sqlTail);
    }

    public String getSqlBody() {
        return StringUtil.replaceEntities(this.sqlBody);
    }

    public void setSqlBody(String sqlBody) {
        this.sqlBody = sqlBody;
        setSqlWebBody(sqlBody);
    }

    public String getSqlfilter() {
        return this.sqlfilter;
    }

    public void setSqlfilter(String sqlfilter) {
        this.sqlfilter = sqlfilter;
    }

    public String getLastUpdateDt() {
        return this.lastUpdateDt;
    }

    public void setLastUpdateDt(String lastUpdateDt) {
        this.lastUpdateDt = lastUpdateDt;
    }

    public int getSegmentSize() {
        return this.segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public String getCrtGrpCd() {
        return this.crtGrpCd;
    }

    public void setCrtGrpCd(String crtGrpCd) {
        this.crtGrpCd = crtGrpCd;
    }

    public String getSegmentType() {
        return this.segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public String getFileToDbYn() {
        return this.fileToDbYn;
    }

    public void setFileToDbYn(String fileToDbYn) {
        this.fileToDbYn = fileToDbYn;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getShareYn() {
        return this.shareYn;
    }

    public void setShareYn(String shareYn) {
        this.shareYn = shareYn;
    }

    public String getActiveYn() {
        return this.activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

    public String getCategoryCd() {
        return this.categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public String getSegmentSts() {
        return this.segmentSts;
    }

    public void setSegmentSts(String segmentSts) {
        this.segmentSts = segmentSts;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public void setTagNo(int tagNo) {
        this.tagNo = tagNo;
    }

    public int getPsegmentNo() {
        return this.psegmentNo;
    }

    public void setPsegmentNo(int psegmentNo) {
        this.psegmentNo = psegmentNo;
    }

    public String getPlinkSeq() {
        return this.plinkSeq;
    }

    public void setPlinkSeq(String plinkSeq) {
        this.plinkSeq = plinkSeq;
    }

    public String getSegType() {
        return this.segType;
    }

    public void setSegType(String segType) {
        this.segType = segType;
    }

    public String getTestQuery() {
        return StringUtil.replaceEntities(this.testQuery);
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
        setWebTestQuery(testQuery);
    }

    public String getUpdateQuery() {
        return StringUtil.replaceEntities(this.updateQuery);
    }

    public void setUpdateQuery(String updateQuery) {
        this.updateQuery = updateQuery;
        setWebUpdateQuery(updateQuery);
    }

    /////////////////////////////////////////////////////////////////
    // 추가 getter/setter
    public String getWebUpdateQuery() {
        return StringUtil.replaceEntities(this.webUpdateQuery);
    }

    public void setWebUpdateQuery(String webUpdateQuery) {
        this.webUpdateQuery = webUpdateQuery;
    }

    public String getServiceSts() {
        return this.serviceSts;
    }

    public void setServiceSts(String serviceSts) {
        this.serviceSts = serviceSts;
    }

    public int getPermissionCount() {
        return this.permissionCount;
    }

    public void setPermissionCount(int permissionCount) {
        this.permissionCount = permissionCount;
    }

    public String getBookmarkYn() {
        return this.bookmarkYn;
    }

    public void setBookmarkYn(String bookmarkYn) {
        this.bookmarkYn = bookmarkYn;
    }

    public String[] getGrpCdArr() {
        return this.grpCdArr;
    }

    public void setGrpCdArr(String[] grpCdArr) {
        this.grpCdArr = grpCdArr;
    }

    public SemanticVo getSemanticVo() {
        return this.semanticVo;
    }

    public void setSemanticVo(SemanticVo semanticVo) {
        this.semanticVo = semanticVo;
    }

    public List<SemanticVo> getSemanticList() {
        return this.semanticList;
    }

    public void setSemanticList(List<SemanticVo> semanticList) {
        this.semanticList = semanticList;
    }

    public String getSqlContext() {
        return StringUtil.replaceEntities(this.sqlContext);
    }

    public void setSqlContext(String sqlContext) {
        this.sqlContext = sqlContext;
    }

    public List<DbInfoVo> getDbInfoList() {
        return this.dbInfoList;
    }

    public void setDbInfoList(List<DbInfoVo> dbInfoList) {
        this.dbInfoList = dbInfoList;
    }

    public DbInfoVo getDbInfoVo() {
        return this.dbInfoVo;
    }

    public void setDbInfoVo(DbInfoVo dbinfoVo) {
        this.dbInfoVo = dbinfoVo;
    }

    public String getNameKor() {
        return this.nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getServerNm() {
        return this.serverNm;
    }

    public void setServerNm(String serverNm) {
        this.serverNm = serverNm;
    }

    public String getSqlWebHead() {
        return StringUtil.replaceEntities(this.sqlWebHead);
    }

    public void setSqlWebHead(String sqlWebHead) {
        this.sqlWebHead = sqlWebHead;
    }

    public String getSqlWebTail() {
        return StringUtil.replaceEntities(this.sqlWebTail);
    }

    public void setSqlWebTail(String sqlWebTail) {
        this.sqlWebTail = sqlWebTail;
    }

    public String getSqlWebBody() {
        return StringUtil.replaceEntities(this.sqlWebBody);
    }

    public void setSqlWebBody(String sqlWebBody) {
        this.sqlWebBody = sqlWebBody;
    }

    public String getBookmarkSeg() {
        return this.bookmarkSeg;
    }

    public void setBookmarkSeg(String bookmarkSeg) {
        this.bookmarkSeg = bookmarkSeg;
    }

    public String getTagNm() {
        return this.tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public int getTargetNo() {
        return this.targetNo;
    }

    public void setTargetNo(int targetNo) {
        this.targetNo = targetNo;
    }

    public String getWebTestQuery() {
        return StringUtil.replaceEntities(webTestQuery);
    }

    public void setWebTestQuery(String webTestQuery) {
        this.webTestQuery = webTestQuery;
    }

    public int getNewSegmentNo() {
        return this.newSegmentNo;
    }

    public void setNewSegmentNo(int newSegmentNo) {
        this.newSegmentNo = newSegmentNo;
    }

    public String getReject() {
        return this.reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getInMetaData() {
        return this.inMetaData;
    }

    public void setInMetaData(String inMetaData) {
        this.inMetaData = inMetaData;
    }

    public boolean isLargeUpload() {
        return this.largeUpload;
    }

    public void setLargeUpload(boolean largeUpload) {
        this.largeUpload = largeUpload;
    }

    public String getFieldKey() {
        return this.fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /////////////////////////////////////////////////////////////////
    // 멤버함수
    public String getFileToDbYnNm() {
        return this.fileToDbYn;
    }

    public void set(SegmentVo vo) {
        this.segmentNo = vo.segmentNo;
        this.newSegmentNo = vo.newSegmentNo;
        this.userId = vo.userId;
        this.nameKor = vo.nameKor;
        this.grpCd = vo.grpCd;
        this.grpCdArr = vo.grpCdArr;
        this.segmentNm = vo.segmentNm;
        this.dbinfoSeq = vo.dbinfoSeq;
        this.serverNm = vo.serverNm;
        this.segmentDesc = vo.segmentDesc;
        this.sqlHead = vo.sqlHead;
        this.sqlWebHead = vo.sqlWebHead;
        this.sqlTail = vo.sqlTail;
        this.sqlWebTail = vo.sqlWebTail;
        this.sqlBody = vo.sqlBody;
        this.sqlWebBody = vo.sqlWebBody;
        this.testQuery = vo.testQuery;
        this.updateQuery = vo.updateQuery;
        this.webTestQuery = vo.webTestQuery;
        this.webUpdateQuery = vo.webUpdateQuery;
        this.lastUpdateDt = vo.lastUpdateDt;
        this.segmentSize = vo.segmentSize;
        this.segmentType = vo.segmentType;
        this.fileToDbYn = vo.fileToDbYn;
        this.editorId = vo.editorId;
        this.shareYn = vo.shareYn;
        this.activeYn = vo.activeYn;
        this.categoryCd = vo.categoryCd;
        this.segmentSts = vo.segmentSts;
        this.bookmarkSeg = vo.bookmarkSeg;
        this.bookmarkYn = vo.bookmarkYn;
        this.tagNo = vo.tagNo;
        this.tagNm = vo.tagNm;
        this.psegmentNo = vo.psegmentNo;
        // this.pserviceNo = vo.pserviceNo;
        this.plinkSeq = vo.plinkSeq;
        this.segType = vo.segType;
        this.targetNo = vo.targetNo;
        this.sqlfilter = vo.sqlfilter;
        this.semanticVo = vo.semanticVo;
        this.semanticList = vo.semanticList;
        this.dbInfoVo = vo.dbInfoVo;
        this.dbInfoList = vo.dbInfoList;
        this.sqlContext = vo.sqlContext;
        this.userVo = vo.userVo;
        this.permissionCount = vo.permissionCount;
        this.serviceSts = vo.serviceSts;
        this.delimiter = vo.delimiter;
        this.inMetaData = vo.inMetaData;
        this.largeUpload = vo.largeUpload;
        this.fieldKey = vo.fieldKey;
        this.reject = vo.reject;
        this.errorMsg = vo.errorMsg;
    }
    //builder pattern
    public SegmentVo(Builder builder) {
        this.segmentNo =builder.segmentNo;
        this.dbinfoSeq =builder.dbinfoSeq;
        this.sqlHead =builder.sqlHead;
        this.sqlTail =builder.sqlTail;
        this.sqlBody =builder.sqlBody;
        this.segmentSize =builder.segmentSize;
        this.updateQuery =builder.updateQuery;
        this.psegmentNo =builder.psegmentNo;
        this.lastUpdateDt =builder.lastUpdateDt;
        this.userId =builder.userId;
        this.grpCd =builder.grpCd;
        this.segmentNm =builder.segmentNm;
        this.activeYn =builder.activeYn;
        this.shareYn =builder.shareYn;
        this.testQuery =builder.testQuery;
        this.segmentType =builder.segmentType;
        this.fileToDbYn =builder.fileToDbYn;
    }
    //builder pattern
    public static class Builder  {
        private int segmentNo;
        private int dbinfoSeq;
        private String sqlHead;
        private String sqlTail;
        private String sqlBody;
        private String testQuery= "";
        private int segmentSize;
        private String updateQuery;
        private int psegmentNo= 0;
        private String lastUpdateDt;
        private String userId;
        private String grpCd;
        private String segmentNm;
        private String activeYn;
        private String shareYn;
        private String segmentType;
        private String fileToDbYn;


        public Builder(int segmentNo) {
            this.segmentNo = segmentNo;
        }

        public Builder setFileToDbYn(String fileToDbYn) {
            this.fileToDbYn = fileToDbYn;
            return this;
        }

        public Builder setSegmentType(String segmentType) {
            this.segmentType = segmentType;
            return this;
        }
        public void setSegmentNo(int segmentNo) {
            this.segmentNo = segmentNo;
        }

        public Builder setTestQuery(String testQuery) {
            this.testQuery = testQuery;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setGrpCd(String grpCd) {
            this.grpCd = grpCd;
            return this;
        }

        public Builder setSegmentNm(String segmentNm) {
            this.segmentNm = segmentNm;
            return this;
        }

        public Builder setActiveYn(String activeYn) {
            this.activeYn = activeYn;
            return this;
        }

        public Builder setShareYn(String shareYn) {
            this.shareYn = shareYn;
            return this;
        }

        public Builder setDbinfoSeq(int dbinfoSeq) {
            this.dbinfoSeq = dbinfoSeq;
            return this;
        }

        public Builder setSqlHead(String sqlHead) {
            this.sqlHead = sqlHead;
            return this;
        }

        public Builder setSqlTail(String sqlTail) {
            this.sqlTail = sqlTail;
            return this;
        }

        public Builder setSqlBody(String sqlBody) {
            this.sqlBody = sqlBody;
            return this;
        }

        public Builder setSegmentSize(int segmentSize) {
            this.segmentSize = segmentSize;
            return this;
        }

        public Builder setUpdateQuery(String updateQuery) {
            this.updateQuery = updateQuery;
            return this;
        }

        public Builder setPsegmentNo(int psegmentNo) {
            this.psegmentNo = psegmentNo;
            return this;
        }

        public Builder setLastUpdateDt(String lastUpdateDt) {
            this.lastUpdateDt = lastUpdateDt;
            return this;
        }
        public SegmentVo build() {
            return new SegmentVo(this);
        }
    }
}
