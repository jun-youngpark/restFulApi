package com.mnwise.wiseu.web.rest.model;

import javax.servlet.http.HttpServletRequest;

public interface RequestVo {

    /**
     * 요청 Method 반환
     * 
     * @return
     */
    public String getRequestMethod();

    /**
     * 상태코드 반환
     * 
     * @return { "httpCode":int, "response":{ code: int, msg: String } }
     */
    public Object getState();

    /**
     * 에러발생 유무
     * 
     * @return
     */
    public boolean isError();

    /**
     * 요청 정보 저장
     * 
     * @param
     */
    public void setRequestInfo(HttpServletRequest request);

    /**
     * 요청객체의 상태를 변경
     * 
     * @param httpCd (HTTP 상태코드)
     * @param stateCd (요청 상태코드)
     * @param stateMsg (요청 상태메시지)
     */
    public void setState(int httpCd, int stateCd, String stateMsg);

}
