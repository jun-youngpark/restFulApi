package com.mnwise.wiseu.web.common.sql;

/**
 * @FileName : CharsetConversionException.java
 * @Date : 2010. 1. 21.
 * @작성자 : carpe3m
 * @변경이력 :
 * @프로그램 설명 : Charset 문자 변환시 Exception
 */
public class CharsetConversionException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -8749926249028425240L;

    public CharsetConversionException() {
    }

    public CharsetConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CharsetConversionException(String message) {
        super(message);
    }

    public CharsetConversionException(Throwable cause) {
        super(cause);
    }
}