package com.mnwise.wiseu.web.rest.model.segment;

import lombok.Getter;
import lombok.Setter;

/**
 * NVSEMANTIC 테이블 모델 클래스
 */
@Setter
@Getter
public class Semantic {
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

}
