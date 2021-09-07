<%-------------------------------------------------------------------------------------------------
  * - [템플릿>모바일 템플릿 리스트>내용 조회] 모바일 템플릿 저장
  * - URL : /template/mobileUpload.do
  * - Controller : com.mnwise.wiseu.web.template.web.MobileTemplateListController
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>template</title>
</head>
<body>
    <c:choose>
        <c:when test="${empty isFail}">
            <c:choose>
                <c:when test="${empty isAlimtalk}">
                    <script>document.location.href="/template/mobileTemplateList.do?cmd=view&contsNo=${contsNo}";</script><!-- /template/mobileTemplate.do -->
                </c:when>
                <c:otherwise>
                    <script>document.location.href="/template/altTemplateList.do";</script><!-- /template/alimtalkTemplate.do -->
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:when test="${!empty imgFileUploadErro}">
            <script>
                alert("${imgFileUploadErro}");
                document.location.href="/template/mobileTemplateList.do?cmd=view&contsNo=${contsNo}";  // /template/mobileTemplate.do
            </script>
        </c:when>
        <c:otherwise>
            <center>파일을 등록중 오류가 발생하였습니다.</center>
        </c:otherwise>
    </c:choose>
</body>
</html>