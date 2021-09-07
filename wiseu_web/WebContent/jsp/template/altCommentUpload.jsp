<%-------------------------------------------------------------------------------------------------
 * - [템플릿>알림톡 템플릿 리스트>알림톡 템플릿 내용조회] 문의 등록 <br/>
 * - URL : /template/altCommentUpload.do<br/>
 * - Controller : com.mnwise.wiseu.web.template.web.AlimtalkTemplateListController <br/>
 * - 이전 파일명 : comment_upload.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>template</title>
</head>
<script type="text/javascript">
$(document).ready(function() {
    parent.submitCallback('${code}');
});
</script>
<body>
</body>
</html>