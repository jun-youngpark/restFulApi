package com.mnwise.wiseu.web.common.util;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.mnwise.common.util.StringUtil;

/**
 * 시리얼 넘버 생성 getSn(String serverType, int serverIndex) 사용시
 */
public class WiseuSerialNumber {
    private static int snSeq = 0;

    private static synchronized int getSeq() {
        snSeq = (snSeq + 1) % 1000000;
        return snSeq;
    }

    public static String getSn(String serverType, int serverIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(FastDateFormat.getInstance("yyyyMMddHHmmssSSS", Locale.getDefault()).format(new Date()));
        sb.append(StringUtil.leftPad(String.valueOf(getSeq()), 6, "0"));
        sb.append(serverType.toUpperCase());
        sb.append(StringUtil.leftPad(String.valueOf(serverIndex), 2, "0"));
        return sb.toString();
    }
}
