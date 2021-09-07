package com.mnwise.wiseu.web.base;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfig {
    
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer () {
        PropertyPlaceholderConfigurer p = new PropertyPlaceholderConfigurer();
        
        p.setProperties(EncryptPropertyPlaceholderConfigurer.getProperties());
        
        return p;
    }
}
 