package com.mnwise.wiseu.web.common.util;

import com.mnwise.wiseu.web.base.EncryptPropertyPlaceholderConfigurer;

public class PropertyUtil {
    
    private static EncryptPropertyPlaceholderConfigurer propertyConfigurer;

    public void setPropertyConfigurer(EncryptPropertyPlaceholderConfigurer propertyConfigurer) {
        PropertyUtil.propertyConfigurer = propertyConfigurer;
    }

    public static String getProperty(String key) {
        return propertyConfigurer.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return propertyConfigurer.getProperty(key, defaultValue);
    }

}
