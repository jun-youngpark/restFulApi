package com.mnwise.wiseu.web.base.util;

import com.mnwise.sw.tms.tm.Const.Channel;

public class CodeUtil {

		private static String MailSuccessCode = PropertyUtil.getProperty("email.code.success", "250");
	    private static String SmsSuccessCode = PropertyUtil.getProperty("sms.code.success", "0");
	    private static String PushSuccessCode = PropertyUtil.getProperty("push.code.success", "0");
	    private static String FaxSuccessCode = PropertyUtil.getProperty("fax.code.success", "0");
	    private static String AltalkSuccessCode = PropertyUtil.getProperty("altalk.code.success", "0000");
	    private static String FrtalkSuccessCode = PropertyUtil.getProperty("frtalk.code.success", "0000");
	    private static String BrandtalkSuccessCode = PropertyUtil.getProperty("brandtalk.code.success", "0000");

	    public static String getSuccessCodeByChannel(String channel) {
	        if(Channel.MAIL.equals(channel)) {
	            return MailSuccessCode;
	        } else if(Channel.SMS.equals(channel) || Channel.LMSMMS.equals(channel)) {
	            return SmsSuccessCode;
	        } else if(Channel.PUSH.equals(channel)) {
	            return PushSuccessCode;
	        } else if(Channel.FAX.equals(channel)) {
	            return FaxSuccessCode;
	        } else if(Channel.ALIMTALK.equals(channel)) {
	            return AltalkSuccessCode;
	        }else if(Channel.FRIENDTALK.equals(channel)) {
	            return FrtalkSuccessCode;
	        }else if(Channel.BRANDTALK.equals(channel)) {
	            return BrandtalkSuccessCode;
	        } else {
	            return "250";
	        }
	    }
}
