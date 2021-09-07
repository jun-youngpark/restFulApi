package com.mnwise.wiseu.web.rest.model.segment;

import static com.mnwise.wiseu.web.base.util.DateUtils.getToDate;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;


/**
 * NVSEGMENT 테이블 모델 클래스
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Segment  {
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

    public String getEditorId(){
    	return StringUtils.defaultIfBlank(editorId, getToDate());
    }

}
