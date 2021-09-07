<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/jsp/include/plugin.jsp" %>
<link rel="stylesheet" href="/plugin/jquery/ui/jquery.ui.slider.css">
<decorator:head />
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="overflow-x-hidden">
    <div class="wrapper">
        <!-- TOP 메뉴 -->
        <jsp:include page="/jsp/common/top_menu.jsp"></jsp:include>
        <!-- 왼쪽 메뉴 -->
        <jsp:include page="/jsp/common/sub_menu.jsp"></jsp:include>
        <!-- 컨텐츠 영역 -->
        <decorator:body />
    </div>
</body>
</html>