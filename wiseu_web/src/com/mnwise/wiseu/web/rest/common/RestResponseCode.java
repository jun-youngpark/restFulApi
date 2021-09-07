package com.mnwise.wiseu.web.rest.common;

public class RestResponseCode {

    // HTTP응답 코드
    public static final int OK = 200; // 성공
    public static final int BAD = 400; // 오류

    // wiseU RESTful 코드
    public static final int SUCCESS = 0; // 요청 처리 성공
    public static final int DATA_PARSING_ERROR = -1; // 요청 데이터 파싱 실패
    public static final int ESSENTIAL_DATA_ERROR = -2; // 요청 데이터 파싱중 필수데이터 누락으로 인한 실패
    public static final int DATABASE_CRUD_ERROR = -3; // CRUD 작업 실패

}
