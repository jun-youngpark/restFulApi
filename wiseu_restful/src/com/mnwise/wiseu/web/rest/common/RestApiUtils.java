package com.mnwise.wiseu.web.rest.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import com.mnwise.wiseu.web.rest.dto.ResultDto;

import org.json.simple.JSONObject;

public class RestApiUtils {

	private static final Logger log = LoggerFactory.getLogger(RestApiUtils.class);
//
//	@Autowired private CommonService commonService;


	/**
	 * 문자열을 바이트 열로 변환한다.
	 * @param string 문자열
	 */
	public static byte[] getBytes(String string) {
		return string.getBytes();
	}

	/**
	 * 바이트 열을 String으로 변환한다.
	 */
	public static String getString(byte[] bytes) {
		return new String(bytes);
	}

	/**
	 * 바이트 열을 String으로 변환한다.
	 */
	public static String getString(byte[] bytes, int offset, int length) {
		return new String(bytes, offset, length);
	}

	/**
	 * 년도를 리턴(yyyyMMdd)
	 */
	public static String getShortDateString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * 일자를 리턴(dd)
	 */
	public static String getDateDayString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("dd", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * 현재 시간 리턴 (HHmmss)
	 */
	public static String getShortTimeString() {
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat ("HHmmss", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * 현재 시간 리턴 (HHmmssSS)
	 */
	public static String getLongTimeString() {
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat ("HHmmssSS", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date()).substring(0,8);
	}

	/**
	 * 현재 시간 리턴 (HHmmss.SS)
	 */
	public static String getLongTimeP9String() {
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat ("HHmmss.SS", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date()).substring(0,9);
	}

	/**
	 * yyyyMMddHHmmssSS
	 */
	public static String getLongTimeP16String() {
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat ("yyyyMMddHHmmssSS", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date()).substring(0,16);
	}

	/**
	 * yyyy/MM/dd/ HH:mm:ss.SS
	 */
	public static String getLongTimeP16StringFormated() {
		java.text.SimpleDateFormat formatter =
			new java.text.SimpleDateFormat ("yyyy/MM/dd HH:mm:ss.SSS", java.util.Locale.KOREA);
		return formatter.format(new java.util.Date());
	}

	/**
	 * HostName 리턴
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getHostName() throws UnknownHostException{
		return InetAddress.getLocalHost().getHostName();
	}

	/**
	 * 시스템 IP Address 리턴
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getIpAddress() throws UnknownHostException{
		return InetAddress.getLocalHost().getHostAddress();
	}

	/**
	 * 지정된 길이에 맞도록 오른쪽 패딩한 문자열을 반환한다.
	 * 만일 대상 문자열이 깅이보다 클 경우, 원래 문자열을 그냥 반환한다.
	 * @param source 처리 대상 문자열
	 * @param padder 채울(패딩) 문자
	 * @param length 전체 길이
	 * @return
	 */
	public static String rpad(String source, char padder, int length) {
		if (source.length() >= length) {
			return source;
		}

		StringBuffer sbuf = new StringBuffer(source);

		for (int i = 0; i < (length - source.length()); i++) {
			sbuf.append(padder);
		}

		return sbuf.toString();
	}

	public static String rpad(byte[] source, char padder, int length) {
		if (source.length >= length) {
			return getString(source);
		}

		StringBuffer sbuf = new StringBuffer(getString(source));

		for (int i = 0; i < (length - source.length); i++) {
			sbuf.append(padder);
		}

		return sbuf.toString();
	}

	/**
	 * 전체 길이에 맞도록 왼쪽 패딩한 문자열을 반환한다.
	 * 만일 대상 문자열이 길이보다 클 경우, 원래 문자열을 그냥 반환한다.
	 * @param source 처리 대상 문자열
	 * @param padder 채울(패딩) 문자
	 * @param length 전체 길이
	 * @return
	 */
	public static String lpad(String source, char padder, int length) {
		if (source.length() >= length) {
			return source;
		}

		StringBuffer sbuf = new StringBuffer();

		for (int i = 0; i < (length - source.length()); i++) {
			sbuf.append(padder);
		}

		sbuf.append(source);

		return sbuf.toString();
	}

	public static String getError(Exception e) {

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return errors.toString();
	}

	public static String getError(Throwable e) {

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return errors.toString();
	}

	public static String getUri() {

		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();

		return builder.buildAndExpand().getPath();
	}

	public static String nullToBlank(String str) {
		if (str == null) {
			return "";
		}

		return str;
	}


	public static Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();

        Enumeration headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = (String) headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

	public static String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

	public static String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }

}
