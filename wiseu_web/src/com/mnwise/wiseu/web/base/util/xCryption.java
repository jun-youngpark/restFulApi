package com.mnwise.wiseu.web.base.util;

import com.mnwise.common.InetUtil.BASE64Decoder;
import com.mnwise.common.InetUtil.BASE64Encoder;
import com.mnwise.common.security.des.Des;

/**
 * DES방식으로 암호화 및 복호화하는 메소드를 제공한다.
 */
public class xCryption {
    private static final String seed = "wiseU";

    public xCryption() {
    }

    /**
     * seed을 key로 message를 base64로 decoding한 문자열을 decryption한 문자열을 return한다. DES 대칭키 방식으로 암호화한다. ECB 운영방법을 사용한다.
     *
     * @param seed key of the 8 Byte (64bit)
     * @param message
     * @return
     */
    public static String decrypt(String seed, String message) {
        BASE64Decoder dec = new BASE64Decoder();
        byte[] deValue = dec.decodeBuffer(message);
        String validSeed = xCryption.make64Seed(seed);
        return Des.decrypt(validSeed, deValue).trim();
    }

    public static String decrypt(String message) {
        if(message == null || message.length() == 0)
            return "";
        return decrypt(seed, message);
    }

    /**
     * seed을 key로 message를 encryption하여 base64로 encoding한 문자열을 return한다. DES 대칭키 방식으로 암호화한다. ECB 운영방법을 사용한다.
     *
     * @param seed key of the 8 Byte (64bit)
     * @param message
     * @return
     */
    public static String encrypt(String seed, String message) {
        String validSeed = xCryption.make64Seed(seed);
        byte[] enValue = Des.encrypt(validSeed, message);
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encodeBuffer(enValue).trim();
    }

    public static String encrypt(String message) {
        if(message == null || message.length() == 0)
            return "";
        return encrypt(seed, message);
    }

    /**
     * seed를 64Bit로 만든다.
     *
     * @param seed key of the 8 Byte (64bit)
     * @return
     */
    private static String make64Seed(String seedMsg) {
        byte byteSeed[] = (seedMsg + "indimail").getBytes();
        return new String(byteSeed, 0, 8);
    }
}
