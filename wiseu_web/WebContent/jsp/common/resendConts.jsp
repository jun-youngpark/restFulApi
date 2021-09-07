<%-------------------------------------------------------------------------------------------------
 * - URL : /common/resendConts.do
 * - Controller : com.mnwise.wiseu.web.common.web.MailMimeViewController
 * - 이전 파일명 : mail_mime_resend.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<script type="text/javascript">
<c:if test="${'S' eq result}">
    alert('<spring:message code="resend.send.end.alert"/>');  // 재발송 완료 되었습니다.
    window.close();
</c:if>
<c:if test="${'F' eq result}">
    alert('<spring:message code="resend.send.fail.alert"/>\n${msg}');  // 재발송이 실패하였습니다.
    window.close();
</c:if>
</script>
</head>
<body>

</body>
</html>