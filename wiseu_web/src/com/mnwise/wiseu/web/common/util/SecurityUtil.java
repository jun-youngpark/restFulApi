package com.mnwise.wiseu.web.common.util;

import com.mnwise.common.security.aria.Aria;

public class SecurityUtil {
    
    /**
     * added by parkhj 2019.04.16
     * lst 파일 암호화 적용 되어있을 경우 사용 
     * @param data
     * @param mode
     * @return
     */
    public static String ariaDecrypt(String data) {
        
        // lst 파일 암호화여부 확인 
        boolean secuFlag = "on".equals(PropertyUtil.getProperty("use.encryption.make.list", "off").toLowerCase());
        // lst 파일이 암호화 되어 있지 않을 경우 바로 패스 
        if(!secuFlag) return data;
        
        boolean encModeFlag = false;
        String[] tmp = data.split("\t");
        StringBuilder sb = new StringBuilder();
     
        for(String str : tmp) {
            sb.append(Aria.wiseuDecrypt(str));
            sb.append("\t");
        }
        
        return sb.toString();
        
    }
    
    /**
     * added by parkhj 2019.05.30
     * lst 파일이 암호화 적용 되어있을 경우 사용 
     * @param data
     * @return
     */
    public static String ariaEncrypt(String data) {
        
     // lst 파일 암호화여부 확인 
        boolean secuFlag = "on".equals(PropertyUtil.getProperty("use.encryption.make.list", "off").toLowerCase());
        // lst 파일이 암호화 되어 있지 않을 경우 바로 패스 
        if(!secuFlag) return data;
        
        boolean encModeFlag = false;
        String[] tmp = data.split("\t");
        StringBuilder sb = new StringBuilder();
     
        for(String str : tmp) {
            sb.append(Aria.wiseuEncrypt(str));
            sb.append("\t");
        }
        
        return sb.toString();
        
        
    }

}
