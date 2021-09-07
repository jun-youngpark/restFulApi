package com.mnwise.wiseu.web.base;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.mnwise.wiseu.web.base.util.xCryption;

/**
 * 프라퍼티 암호화 모듈 적용
 */

public class EncryptPropertyPlaceholderConfigurer  extends PropertyPlaceholderConfigurer {
    
   
    private String[] decryptKeys;
    private static Properties properties;
    
    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
       

        @SuppressWarnings("unchecked")
        Enumeration<String> enums = (Enumeration<String>) props.propertyNames();
        String key = null;
        String value;

        loop: while(enums.hasMoreElements()) {
            key = enums.nextElement();
            value = props.getProperty(key);

            if(decryptKeys != null) {
                for(int i = 0; i < decryptKeys.length; i++) {
                    if(key.equals(decryptKeys[i])) {
                        props.setProperty(key, xCryption.decrypt(value).replaceAll("[$]\\{WU_HOME\\}", System.getProperty("wiseu.home")));
                        continue loop;
                    }
                }
            }
            props.setProperty(key, value.replaceAll("[$]\\{WU_HOME\\}", System.getProperty("wiseu.home")));
        }

        properties = props;
        
    }
    
    public static Properties getProperties() {
        return properties;
    }
    
    

    public void setDecryptKeys(String[] decryptKeys) {
        this.decryptKeys = decryptKeys;
    }

    /**
     * 프로퍼티 값 가져오기
     * 
     * @param key 프로퍼티 키
     * @return
     */
    public String getProperty(String key) {
        String val = properties.getProperty(key);
        return val == null ? "" : val;
    }

    /**
     * 프로퍼티 값 가져오기
     * 
     * @param key 프로퍼티 키
     * @param defaultValue 기본값
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String val = properties.getProperty(key);
        return val == null || val.equals("") ? defaultValue : val;
    }

    /**
     * 프로퍼티 값 추가
     * 
     * @param key 키
     * @param value 값
     */
    public void put(String key, String value) {
        properties.put(key, value);
    }
}
