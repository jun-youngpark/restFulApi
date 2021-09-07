package com.mnwise.wiseu.web.common.util;

import com.mnwise.wiseu.web.base.Const.Channel;

public class CodeUtil {

    private static String mailErrCd = PropertyUtil.getProperty("email.code.success", "250");
    private static String smsErrCd = PropertyUtil.getProperty("sms.code.success", "0");
    private static String pushErrCd = PropertyUtil.getProperty("push.code.success", "0");
    private static String faxErrCd = PropertyUtil.getProperty("fax.code.success", "0");
    private static String altalkErrCd = PropertyUtil.getProperty("altalk.code.success", "0000");
    private static String frtalkErrCd = PropertyUtil.getProperty("frtalk.code.success", "0000");
    private static String brandtalkErrCd = PropertyUtil.getProperty("brandtalk.code.success", "0000");

    public static String getErrCdByChannel(String channel) {
        if(Channel.MAIL.equals(channel)) {
            return mailErrCd;
        } else if(Channel.SMS.equals(channel) || Channel.LMSMMS.equals(channel)) {
            return smsErrCd;
        } else if(Channel.PUSH.equals(channel)) {
            return pushErrCd;
        } else if(Channel.FAX.equals(channel)) {
            return faxErrCd;
        } else if(Channel.ALIMTALK.equals(channel)) {
            return altalkErrCd;
        }else if(Channel.FRIENDTALK.equals(channel)) {
            return frtalkErrCd;
        }else if(Channel.BRANDTALK.equals(channel)) {
            return brandtalkErrCd;
        } else {
            return "250";
        }
    }
}
