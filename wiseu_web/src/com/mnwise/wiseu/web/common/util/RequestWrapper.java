package com.mnwise.wiseu.web.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.mnwise.common.util.StringUtil;

public class RequestWrapper extends HttpServletRequestWrapper {
    public RequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if(values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for(int i = 0; i < count; i++) {
            encodedValues[i] = escapeXss(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if(value == null) {
            return null;
        }
        return escapeXss(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(value == null) {
            return null;
        }
        return escapeXss(value);
    }

    // 2015-08-19 대구은행 xss 방어 필터 적용
    public static String escapeXss(String str) {
        return StringUtil.isBlank(str) ? "" : str.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
