<%---------------------------------------------------------------
 * Title		: 대상자  데코레이터
 * Description	: 대상자 팝업 데코레이터
 -----------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><decorator:title default="WISE-U" /></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<decorator:head />
</head>

<body style="margin: 10px 10px 0px 10px; height: 810px;">
<!--content start -->
<decorator:body />
<!--content end -->
</body>
</html>
