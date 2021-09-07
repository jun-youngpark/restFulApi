package com.mnwise.wiseu.web.admin.web;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mnwise.common.util.StringUtil;

public class XSSHandlerInterceptor extends HandlerInterceptorAdapter {
    private Properties invaildWordMappings;

    public void setInvaildWordMappings(Properties invaildWordMappings) {
        this.invaildWordMappings = invaildWordMappings;
    }

    /**
     * 전처리
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        for(Enumeration<String> names = request.getParameterNames(); names.hasMoreElements();) {
            String name = names.nextElement();

            String retName = getMatchInvaildWord(name);
            if(null != retName) {
                String message = retName + " 인자에 유효하지 않은 단어(" + retName + ")가 포함되어 크로스 서버 스크립팅(XSS)검사에 위배되어 에러가 발생했습니다.";
                Exception e = new RuntimeException(message);
                request.setAttribute("error", e);
                request.setAttribute("uri", request.getRequestURI());
                request.setAttribute("message", message);
                request.setAttribute("exception_type", "RuntimeException");
                throw e;
            }

            String value = StringUtil.defaultIfEmpty(request.getParameter(name), "");
            String retVal = getMatchInvaildWord(value);
            if(null != retVal) {
                String message = name + " 항목에 유효하지 않은 단어(" + retVal + ")가 포함되어 크로스 서버 스크립팅(XSS)검사에 위배되어 에러가 발생했습니다.";
                Exception e = new RuntimeException(message);
                request.setAttribute("error", e);
                request.setAttribute("uri", request.getRequestURI());
                request.setAttribute("message", message);
                request.setAttribute("exception_type", "RuntimeException");
                throw e;
            }
        }

        return true;
    }

    /**
     * 유효하지 않은 단어가 포함되었는지 확인
     *
     * @param val
     * @return
     */
    public boolean existInvaildWord(String val) throws Exception {
        boolean existInvaildWord = false;
        if(null != getMatchInvaildWord(val)) {
            existInvaildWord = true;
        }
        return existInvaildWord;
    }

    /**
     * 유효하지 않은 단어가 포함되었다면 어떤 단어인지 반환
     *
     * @param val
     * @return
     */
    public String getMatchInvaildWord(String val) throws Exception {
        for(Enumeration<?> names = invaildWordMappings.propertyNames(); names.hasMoreElements();) {
            String key = (String) names.nextElement();
            String invaildWord = invaildWordMappings.getProperty(key);

            if(val.indexOf(invaildWord) > -1) {
                return invaildWord;
            }
        }
        return null;
    }
}
