package com.mnwise.wiseu.web.rest.model.ecare;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.Getter;
import lombok.Setter;

/**
 * 스케줄 요청 테이블 Vo
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchRequest extends BaseVo {
    @NotBlank protected String ecareNo; // 이케어 번호
    protected String svcId;             // SVCID
    //protected String channel; //발송 채널 구분 (MAIL: M, SMS: S, MMS/LMS: T, FAX: F, P:PUSH, A:알림톡, C:친구톡, B:브랜드톡)
    @NotBlank protected String batchSeq;// 배치 발송 요청분
    @NotNull protected int datCnt;         // 데이터 개수
    protected String filePath;          // DATA 경로
}
