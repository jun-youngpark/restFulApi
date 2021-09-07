package com.mnwise.wiseu.web.base.util;

public class Cipher {

    /**
     * <p>
     * 문자열이 신한카드 암호화 모듈로 암호화 되어 있는지 여부를 확인<br>
     * 암호화 되어 있는지 유무는 문자열의 길이와 값으로 확인
     * </p>
     * 
     * @param value 검사할 문자열
     * @return true(암호화 되어 있는 문자열) 또는 false(일반 문자열)
     */
    public static boolean isCipherShinhanCard(String value) {
        return value.matches("([0-9A-Fa-f]{32})+");
    }
}
