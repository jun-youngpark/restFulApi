package com.mnwise.wiseu.web.base;

public class ResultDto {
    /** 결과코드 : 성공 */
    public static final String OK = "OK";
    /** 결과코드 : 실패 */
    public static final String FAIL = "FAIL";

    /** 결과코드 */
    private String code;

    /** 메시지코드 */
    private String message;

    /** 적용 row수 */
    private int rowCount;

    /** 결과값 */
    private Object value;

    public ResultDto() {
    }

    public ResultDto(String code) {
        this.code = code;
    }

    public ResultDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
