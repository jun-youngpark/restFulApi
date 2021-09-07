package com.mnwise.wiseu.web.segment.model;


/**
 * NVSEMANTIC 테이블 모델 클래스
 */
public class SemanticVo {
    private int segmentNo;
    private int fieldSeq;
    private String fieldNm;
    private String fieldDesc;
    private String fieldType;
    private int fieldLength;
    private String initvalue;
    private String minvalue;
    private String maxvalue;
    private String nullYn;
    private String fieldKey;

    public SemanticVo() {}
    /////////////////////////////////////////////////////////////////
    // 기본 getter/setter
    public int getSegmentNo() {
        return this.segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public int getFieldSeq() {
        return this.fieldSeq;
    }

    public void setFieldSeq(int fieldSeq) {
        this.fieldSeq = fieldSeq;
    }

    public String getFieldNm() {
        return this.fieldNm;
    }

    public void setFieldNm(String fieldNm) {
        this.fieldNm = fieldNm;
    }

    public String getFieldDesc() {
        return this.fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getFieldLength() {
        return this.fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getInitvalue() {
        return this.initvalue;
    }

    public void setInitvalue(String initvalue) {
        this.initvalue = initvalue;
    }

    public String getMinvalue() {
        return this.minvalue;
    }

    public void setMinvalue(String minvalue) {
        this.minvalue = minvalue;
    }

    public String getMaxvalue() {
        return this.maxvalue;
    }

    public void setMaxvalue(String maxvalue) {
        this.maxvalue = maxvalue;
    }

    public String getNullYn() {
        return this.nullYn;
    }

    public void setNullYn(String nullYn) {
        this.nullYn = nullYn;
    }

    public String getFieldKey() {
        return this.fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
   //builder pattern
    public SemanticVo(Builder builder) {
        this.segmentNo =builder.segmentNo;
        this.fieldSeq =builder.fieldSeq;
        this.fieldNm =builder.fieldNm;
        this.fieldNm =builder.fieldNm;
        this.fieldLength =builder.fieldLength;
        this.nullYn =builder.nullYn;
        this.fieldKey =builder.fieldKey;
        this.fieldDesc =builder.fieldDesc;
    }
    //builder pattern
    public static class Builder  {
        private int segmentNo;
        private int fieldSeq;
        private String fieldNm;
        private String fieldDesc;
        private int fieldLength= 0;
        private String nullYn ="N";
        private String fieldKey;

        public Builder(int segmentNo) {
            this.segmentNo = segmentNo;
        }

        public Builder setFieldSeq(int fieldSeq) {
            this.fieldSeq = fieldSeq;
            return this;
        }


        public Builder setFieldDesc(String fieldDesc) {
            this.fieldDesc = fieldDesc;
            return this;
        }

        public Builder setFieldNm(String fieldNm) {
            this.fieldNm = fieldNm;
            return this;
        }

        public Builder setFieldKey(String fieldKey) {
            this.fieldKey = fieldKey;
            return this;
        }
        public Builder setNullYn(String nullYn) {
            this.nullYn = nullYn;
            return this;
        }

        public Builder setFieldLength(int fieldLength) {
            this.fieldLength = fieldLength;
            return this;
        }

        public SemanticVo build(){
            return new SemanticVo(this);
        }
    }
}
