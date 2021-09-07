<%-------------------------------------------------------------------------------------------------
 * - [템플릿>모바일 템플릿 등록] 친구톡 이미지 파일 올리기
 * - URL : /template/ftrImgUpload.do
 * - Controller :com.mnwise.wiseu.web.template.web.MobileTemplateRegController
 * - 이전 파일명 : mobile_upload.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script src="/plugin/jquery/jquery-1.12.4.js"></script>
<title>template</title>
</head>
<script type="text/javascript">
$(document).ready(function() {
    parent.uploadImageResult('${code}', '${message}', '${url}', '${mobileVo.filePath}', '${mobileVo.fileName}');
});
</script>
<body>
</body>
</html>