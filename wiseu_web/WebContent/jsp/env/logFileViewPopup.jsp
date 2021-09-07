<%-------------------------------------------------------------------------------------------------
 * - [환경설정>로그 관리] 로그파일 미리보기 (팝업)
 * - URL : /env/logFileViewPopup.do <br/>
 * - Controller : com.mnwise.wiseu.web.env.web.EnvLogFileController <br/>
 * - 이전 파일명 : env_logfilepreview
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<html>
<head>
<title><spring:message code="env.msg.viewfilelog" /></title><!-- 로그 파일 보기 -->
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="env.msg.viewfilelog" /></h5></div><!-- 로그 파일 보기 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
         <div class="form-group">
             <h5 class="form-control-label"> <spring:message code="env.msg.filepath" /> :<span class="pl-2">${folderPath}</span></h5><!-- 경로 -->
             <textarea class="form-control" style="height: 590px;" readonly>${fileContent}</textarea>
         </div>
    </div>
</div>
</body>
</html>