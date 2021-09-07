package com.mnwise.wiseu.web.rest.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import static com.mnwise.wiseu.web.rest.common.RestApiUtils.getHeaders;
import static com.mnwise.wiseu.web.rest.common.RestApiUtils.getRequestBody;
import static com.mnwise.wiseu.web.rest.common.RestApiUtils.getResponseBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(filterName = "Filter", urlPatterns = { "/api/*" })
public class LoggingFilter implements Filter{

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();
        chain.doFilter(requestWrapper, responseWrapper);
        long end = System.currentTimeMillis();
        log.debug("\n" +
                        "[REQUEST] {} - {} {} - {}\n" +
                        "Headers : {}\n" +
                        "Request : {}\n" +
                        "Response : {}\n",
                ((HttpServletRequest) request).getMethod(),
                ((HttpServletRequest) request).getRequestURI(),
                responseWrapper.getStatus(),
                (end - start) / 1000.0,
                getHeaders((HttpServletRequest) request),
                getRequestBody(requestWrapper),
                getResponseBody(responseWrapper));
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

