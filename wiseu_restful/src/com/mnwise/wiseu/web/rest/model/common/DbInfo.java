package com.mnwise.wiseu.web.rest.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * NVDBINFO 테이블 모델 클래스
 */
@Getter @Setter
public class DbInfo {
    private int dbInfoSeq;
    private String driverNm;
    private String serverNm;
    private String driverDsn;
    private String dbUserId;
    private String dbPassword;
    private String encoding;
    private String decoding;
    private String dbKind;
    private String testQuery;

}
