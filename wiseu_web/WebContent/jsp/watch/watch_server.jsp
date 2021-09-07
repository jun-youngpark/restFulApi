<%-------------------------------------------------------------------------------------------------
 * - [wiseWatch] 프로세스 탭
 * - URL : /watch/watch.do?menu=server
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchController
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Page-Enter" content="BlendTrans(Duration=0,2)"/>
<meta http-equiv="Page-Exit" content="BlendTrans(Duration=0,2)"/>
<title>wiseWatch - Process</title>
</head>
<body>
    <div class="tab-pane active" id="process" role="tabpanel" aria-labelledby="process-tab" style="height: 670px; overflow-x:hidden; overflow-y:auto !important;"><!-- 프로세스 -->
        <h2 class="hide"><spring:message code="watch.msg.process"/></h2><!-- 프로세스 현황 -->
        <c:forEach var="list" items="${getListServer}">
            <h3 class="h3 text-primary my-0 pl-2">${fn:toUpperCase(list.serverName)}</h3><!-- 프로세스 ID -->
            <c:forEach var="id" items="${list.serverId}">
                <!-- /watch/watch_${list.serverName} -->
                <iframe name="${id}" scrolling="no" align="bottom" style="height:160px;" src="/watch/processStatus_${list.serverName}.do?serverId=${id}&menu=server"></iframe>
            </c:forEach>
        </c:forEach>
    </div><!-- //프로세스 tab -->
</body>
</html>
