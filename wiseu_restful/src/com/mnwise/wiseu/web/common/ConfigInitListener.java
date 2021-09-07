package com.mnwise.wiseu.web.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 환경 설정 초기화 리스너 운영에 필요한 항목들을(디렉토리, 파일) 체크하여 생성되지 않았으면 생성하도록 한다.
 */
public class ConfigInitListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(ConfigInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        if(log.isInfoEnabled()) {
            log.info("ConfigInitListener Start...");
        }
        log.debug(ctx.getInitParameter("contextConfigLocation"));

        String log4jdbcConfPath = ctx.getInitParameter("log4jdbcLog4j2ConfigLocation");
        if(log4jdbcConfPath != null) {
            System.setProperty("log4jdbc.log4j2.properties.file", log4jdbcConfPath);
            log.debug("loaded " + log4jdbcConfPath);
        }

        // servletContext.getInitParameter("");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if(log.isInfoEnabled()) {
            log.info("Config Init Listener Destory...");
        }
    }

}
