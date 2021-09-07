package com.mnwise.wiseu.web.rest.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mnwise.wiseu.web.rest.model.BaseVo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * NVTRACEINFO 테이블 모델 클래스
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TraceInfo extends BaseVo {
    private int serviceNo;
    private String traceType;
    private String startDt;
    private String startTm;
    private String endDt;
    private String endTm;
    private String termType;

}
