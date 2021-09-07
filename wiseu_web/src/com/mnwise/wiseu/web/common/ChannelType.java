package com.mnwise.wiseu.web.common;

public class ChannelType {
    public static final String EMAIL = "M";
    public static final String SMS = "S";
    public static final String LMSMMS = "T";
    public static final String FAX = "F";
    public static final String ALIMTALK = "A";
    public static final String FRIENDTALK = "C";
    public static final String PUSH = "P";
    public static final String DM = "D";

    public static String getChannelName(String channelType) {
        if(EMAIL.equalsIgnoreCase(channelType)) {
            return "EMAIL";
        }
        else if(SMS.equalsIgnoreCase(channelType)) {
            return "SMS";
        }
        else if(LMSMMS.equalsIgnoreCase(channelType)) {
            return "LMSMMS";
        }
        else if(FAX.equalsIgnoreCase(channelType)) {
            return "FAX";
        }
        else if(ALIMTALK.equalsIgnoreCase(channelType)) {
            return "ALIMTALK";
        }
        else if(FRIENDTALK.equalsIgnoreCase(channelType)) {
            return "FRIENDTALK";
        }
        else if(PUSH.equalsIgnoreCase(channelType)) {
            return "PUSH";
        }
        else if(DM.equalsIgnoreCase(channelType)) {
            return "DM";
        }
        else {
            return null;
        }
    }
}
