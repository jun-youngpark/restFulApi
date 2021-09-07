<%---------------------------------------------------------------
 * Title		: 빈 화면용 데코레이터
 * Description	: 데코레이터 적용이 필요없는 페이지에 적용할 때 사용함
 *                기존의 panel.jsp가 사용되고 있으나 head부분이 빠져있고 이미 사용중인 페이지들이 있어서
 *                새로 만들어서 적용함. 기존 panel.jsp의 사용중인 곳을 체크해서 합쳐야할 필요 있음
 -----------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<decorator:head />
<decorator:body />