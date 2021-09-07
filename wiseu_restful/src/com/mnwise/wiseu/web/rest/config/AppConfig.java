package com.mnwise.wiseu.web.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
	@PropertySource("file:///${rest.home}/conf/web/restful.properties"),
	@PropertySource("file:///${wiseu.home}/conf/tms/global.conf")
})
@EnableAspectJAutoProxy
public class AppConfig{
   @Bean
    public static PropertySourcesPlaceholderConfigurer loadProperties(){
        return new PropertySourcesPlaceholderConfigurer();
   }

}

