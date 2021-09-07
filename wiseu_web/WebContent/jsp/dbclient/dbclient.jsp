<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="panel" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DBClient Version 2</title>
<script language='javascript' src='/js/ajax/lib/jquery.js'></script>
<script>
function choice(jdbcDriver, serverName, jdbcUrl, jdbcUserName, jdbcPassWord, dbms) {
    var frm = document.queryFrm;
    frm.jdbcDriver.value = jdbcDriver;
    frm.jdbcUrl.value = jdbcUrl;
    frm.jdbcUserName.value = jdbcUserName;
    frm.jdbcPassWord.value = jdbcPassWord;
    frm.dbms.value = dbms;
}
function onSubmit() {
    var frm = document.queryFrm;
    if(frm.jdbcDriver.value == "") {
        alert("jdbcDriver 을 입력해주세요");
        return;
    } else if(frm.jdbcUrl.value == "") {
        alert("jdbcUrl 을 입력해주세요");
        return;
    } else if(frm.jdbcUserName.value == "") {
        alert("jdbcUserName 을 입력해주세요");
        return;
    } else if(frm.jdbcPassWord.value == "") {
        alert("jdbcPassWord 을 입력해주세요");
        return;
    } else if(frm.dbms.value == "") {
        alert("dbms 을 입력해주세요");
        return;
    } else if(frm.queryText.value == "") {
        alert("queryText 을 입력해주세요");
        return;
    }
    frm.submit();
}
</script>
</head>
<body>
<form id="queryFrm" name="queryFrm" method="post" action="/dbclient/select.do">
<input type="hidden" id="cmd" name="cmd" value="select"/>
<table border="1" cellpadding="0" cellspacing="0" style="font-size: 13px; width: 100%">
    <tr>
        <td width="15%" style="font-size: 12px; cursor: pointer; background-color:#f3f3f3; border-bottom:1px solid #cfdff6; height:20px;">데이터베이스</td>
        <td width="15%" style="font-size: 12px; cursor: pointer; background-color:#f3f3f3; border-bottom:1px solid #cfdff6; height:20px;">서버명</td>
        <td width="15%" style="font-size: 12px; cursor: pointer; background-color:#f3f3f3; border-bottom:1px solid #cfdff6; height:20px;">유저명</td>
        <td width="55%" style="font-size: 12px; cursor: pointer; background-color:#f3f3f3; border-bottom:1px solid #cfdff6; height:20px;">URL</td>
    </tr>
    <c:choose>
        <c:when test="${!empty dbClientList}">
            <c:forEach var="dbClientVo" items="${dbClientList}" varStatus="status">
                <tr style="font-size: 12px; cursor: pointer; border-bottom:1px solid #cfdff6; height:20px;" onclick="javascript:choice('${dbClientVo.jdbcDriver}','${dbClientVo.serverName}','${dbClientVo.jdbcUrl}','${dbClientVo.jdbcUserName}','${dbClientVo.jdbcPassWord}','${dbClientVo.dbms}');">
                    <td width="15%">${dbClientVo.dbms}</td>
                    <td width="15%" class="text-left">${dbClientVo.serverName}</td>
                    <td width="15%" class="text-left">${dbClientVo.jdbcUserName}</td>
                    <td width="55%">${dbClientVo.jdbcUrl}</td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan=4></td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan=4 height="30"></td>
    </tr>
</table>

<table border="1" cellpadding="0" cellspacing="0" style="font-size: 13px; width: 100%">
    <tr>
        <th align="center" width="15%">DATABASE</th>
        <td colspan="3" align="left"><input type="text" id="dbms" name="dbms" value="${dbms}" style="width:90%"/></td>
    </tr>
    <tr>
        <th align="center">DRIVER</th>
        <td colspan="3" align="left"><input type="text" id="jdbcDriver" name="jdbcDriver" value="${jdbcDriver}" style="width:90%"/></td>
    </tr>
    <tr>
        <th align="center" width="15%">USERNAME</th>
        <td align="left" width="35%"><input type="text" id="jdbcUserName" name="jdbcUserName" value="${jdbcUserName}" style="width:90%"/></td>
        <th align="center" width="15%">PASSWORD</th>
        <td align="left" width="35%"><input type="password" id="jdbcPassWord" name="jdbcPassWord" value="${jdbcPassWord}" style="width:90%" autocomplete="off"/></td>
    </tr>
    <tr>
        <th align="center">URL</th>
        <td colspan="3" align="left"><input type="text" id="jdbcUrl" name="jdbcUrl" value ="${jdbcUrl}" style="width:90%"/></td>
    </tr>
    <tr>
        <th align="center">Query</th>
        <td colspan="3" align="left">
            <textarea id="queryText" name="queryText" rows="5" cols="100" style="width:90%;overflow:hidden">${queryText}</textarea>
        </td>
    </tr>
    <tr>
        <td align="center" colspan="4"><input type="button" value="쿼리 전송" onClick="javascript:onSubmit();"/></td>
    </tr>
</table>
</form>
<br/>
<br/>
<c:if test="${!empty message}">${message}</c:if>
<br/>
<c:if test="${!empty error}">
    <br/>
    <table border="1" style="font-size: 13px; text-align: center; width: 100%">
        <tr><td width="10%">원인</td><td>${error}</td></tr>
    </table>
</c:if>
<br/>
<c:if test="${!empty columnList}">
<table border="1" cellpadding="1" cellspacing="1" style="font-size: 13px; text-align: center; width: 100%; table-layout: auto">
    <tr>
        <c:forEach var="columnName" items="${columnList}">
          <th>${columnName}</th>
        </c:forEach>
</c:if>
    </tr>
    <tr>
<c:if test="${!empty columnValue}">
    <c:set var="i" value="1"/>
    <c:forEach var="value" items="${columnValue}" varStatus="valueCount">
        <td><c:if test="${empty value or value eq ' '}">-</c:if><c:if test="${!empty value}">${value}</c:if></td>
        <c:if test="${i != 0 and i % columnCount == 0}"></tr><tr></c:if>
        <c:set var="i" value="${i + 1}"/>
    </c:forEach>
    </tr>
</table>
</c:if>
</body>
</html>