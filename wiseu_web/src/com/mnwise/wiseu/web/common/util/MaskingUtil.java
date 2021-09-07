package com.mnwise.wiseu.web.common.util;

import com.mnwise.common.util.StringUtil;

public class MaskingUtil {
    private static MaskingUtil maskingUtil = new MaskingUtil();

    public static MaskingUtil getInstance() {
        return maskingUtil;
    }

    /**
     * 2014.04.09 최형욱 메일 마스킹 처리
     *
     * @param mail
     * @return
     */
    public static String mailMask(String mail) {
        String mark = "";
        if(!getInstance().useMasking())
            return mail;

        if("".equals(StringUtil.defaultIfEmpty(mail, "")))
            return mail;
        if(mail.length() <= 3)
            return mail;
        //메일일 경우
        if(mail.indexOf("@") != -1) {
            for(int i = 0; i < mail.substring(3).length(); i++) {
                mark = mark + getInstance().getMaskingChar();
            }
            return (mail.substring(0, 3) + mark).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            //팩스일 경우 telMask(String tel) 메소드를 불러와서 리턴한다.
        } else {
            return telMask(mail);
        }
    }

    /**
     * 2014.04.09 최형욱 이름 마스킹 처리
     *
     * @param name
     * @return
     */
    public static String nameMask(String name) {
        if(!getInstance().useMasking())
            return name;

        int nameLength = name.length();
        if(StringUtil.isEmpty(name) || nameLength == 1) {
            return name;
        }

        //이름이 두자일때의 마스킹 처리
        final String maskingChar = getInstance().getMaskingChar();
        if(nameLength == 2) {
            name = name.substring(0, 1) + maskingChar;
        } else {
            int nMaskLen = nameLength - 2;
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < nMaskLen; i++) {
                sb.append(maskingChar);
            }

            name = name.substring(0, 1) + sb.toString() + name.substring(nameLength - 1, nameLength);
        }
        return name.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    /**
     * 2016.09.09 ksj1001 문자내용 중에 숫자를 전부 마스킹 처리.
     *
     * @param tel
     * @return
     */
    public static String numberMask(String msg) {
        if(!getInstance().useMasking())
            return msg;

        if("".equals(StringUtil.defaultIfEmpty(msg, "")))
            return msg;

        // 문자 내용 중에 숫자를 마스킹
        String maskStr = "";
        if(StringUtil.isNotBlank(msg)) {
            char[] charArr = msg.toCharArray();
            int cnt = charArr.length;
            for(int i = 0; i < cnt; i++) {
                if(Character.isDigit(charArr[i])) {
                    maskStr += getInstance().getMaskingChar();
                } else {
                    maskStr += charArr[i];
                }
            }

        }

        return maskStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    /**
     * 2014.04.09 최형욱 전화번호 형식의 마스킹 처리
     *
     * @param tel
     * @return
     */
    public static String telMask(String tel) {
        if(!getInstance().useMasking())
            return tel;

        if("".equals(StringUtil.defaultIfEmpty(tel, "")))
            return tel;
        if(tel.length() <= 3)
            return tel;
        // 가려질 *의 숫자만큼 문자열로 * 추가
        String sMask = "";
        for(int i = 0; i < tel.length() - 7; i++) {
            sMask = sMask + getInstance().getMaskingChar();
        }
        tel = tel.substring(0, 3) + sMask + tel.substring(tel.length() - 4, tel.length());
        tel = tel.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        return tel;
    }

    private String maskChar = "*";

    private boolean isUseMasking = false;

    private MaskingUtil() {
        isUseMasking = "on".equals(PropertyUtil.getProperty("privacy.mask.use", "off"));
        maskChar = PropertyUtil.getProperty("privacy.mask.char", "*");
    }

    /**
     * 마스킹 문자 리턴
     *
     * @return
     */
    public String getMaskingChar() {
        return this.maskChar;
    }

    /**
     * 마스킹 적용여부
     *
     * @return
     */
    public boolean useMasking() {
        return this.isUseMasking;
    }
}
