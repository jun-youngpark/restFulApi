<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%! // log4j 기록
    Log log = LogFactory.getLog("errorpage");
%>
<%
    response.setStatus(HttpServletResponse.SC_OK);
    String errMsg = request.getParameter("errMsg");
    if(errMsg==null) errMsg = "";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="panel">
<title>error</title>
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body>
<div class="card pop-card">
        <div class="card-header"><!-- title -->
            <h5 class="mb-0"><i class="fas fa-ban text-danger"></i>&nbsp;Error</h5>
        </div>
        <div class="card-body">
                <div class="table-responsive gridWrap">
                   <% // log4j 기록
                        if(null!=exception) {
                            String sErrStackTrace = "";
                            StackTraceElement st[] = exception.getStackTrace();
                            if(null!=st) {
                                for(int cnt=0; cnt < st.length; cnt++)
                                {
                                    sErrStackTrace += st[cnt].toString()+System.getProperty("line.separator");
                                }
                                log.error(
                                    exception.getMessage()
                                    +System.getProperty("line.separator")
                                    +"==================================[ error trace start ]====================================="
                                    +System.getProperty("line.separator")
                                    +sErrStackTrace
                                    +"=================================[ error trace end ]========================================"
                                );
                            } else {
                                log.error(
                                    exception.getMessage()
                                    +"==================================[ error trace start ]====================================="
                                    +System.getProperty("line.separator")
                                    +exception.getStackTrace()
                                    +"=================================[ error trace end ]========================================"
                                );
                            }
                        }
                        %>
                        <c:choose>
                            <c:when test="${empty errMsg}">
                                요청하신 페이지를 찾을 수 없습니다. 관리자에게 문의하세요.
                            </c:when>
                            <c:otherwise>
                                ${errMsg}
                            </c:otherwise>
                        </c:choose>
                </div>
        </div>
        <div class="card-footer">
            <button type="button" class="btn btn-outline-primary" onclick="history.go(-1)" >
                <spring:message code="button.backward"/><!-- 돌아가기 -->
            </button>
        </div>
    </div>

</body>
</html>