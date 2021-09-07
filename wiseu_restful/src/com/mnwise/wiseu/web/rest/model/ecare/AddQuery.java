package com.mnwise.wiseu.web.rest.model.ecare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * NVADDQUERY 테이블 모델 클래스
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AddQuery {
    private int ecareNo;
    private int querySeq;
    private String queryType;
    private String executeType;
    private String resultId;
    private int dbInfoSeq;
    private String query;

}
