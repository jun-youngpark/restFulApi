package com.mnwise.wiseu.web.common.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

import xecure.servlet.XecureConfig;
import xecure.servlet.XecureHttpServletRequest;
import xecure.servlet.XecureHttpServletResponse;
import xecure.servlet.XecureServlet;

public class XecureDispatcherServlet extends DispatcherServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 8178309396874873230L;
    public static XecureConfig xconfig = null;
    private String q = null;

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(getXecureSessionId(request) == null) {
            super.doDispatch(request, response);
        } else {
            if(XecureDispatcherServlet.xconfig == null)
                xconfig = new XecureConfig();
            XecureServlet xecureServlet = new XecureServlet(xconfig, request, response);
            XecureHttpServletRequest xRequest = xecureServlet.request;
            XecureHttpServletResponse xResponse = xecureServlet.response;
            xRequest.setAttribute("q", q);
            super.doDispatch(xRequest, xResponse);
        }
    }

    protected String getXecureSessionId(HttpServletRequest httpservletrequest) {
        String s = httpservletrequest.getParameter("q");
        if(s == null || s.equals(""))
            return null;
        int i = s.indexOf(";");
        if(i == 0)
            return null;
        if(i < 0)
            i = s.length();
        q = s.substring(0, i);
        return q;
    }
}
