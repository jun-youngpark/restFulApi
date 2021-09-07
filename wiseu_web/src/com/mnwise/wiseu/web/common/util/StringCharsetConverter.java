package com.mnwise.wiseu.web.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mnwise.common.util.StringUtil;

/**
 * DB 캐릭터셋이 EUC-KR 이 아닐경우 getString 하면 깨지기 때문에 변환하는 과정을 거쳐야 한다.
 */
public class StringCharsetConverter {
    private static final Logger log = LoggerFactory.getLogger(StringCharsetConverter.class);

    private static String dbEnc;
    private static String viewEnc;

    public static void init(String db, String view) {
        dbEnc = db;
        viewEnc = view;
    }

    public static String convert(String s) {
        if(s != null && !StringUtil.isEmpty(dbEnc)) {
            try {
                s = new String(s.getBytes(dbEnc), viewEnc);
            } catch(Exception e) {
                log.error(null, e);
            }
        }
        return s;
    }
}
