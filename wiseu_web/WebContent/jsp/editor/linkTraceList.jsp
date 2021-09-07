<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 저작기>링크>링크추적 클릭 - 링크추적리스트(팝업) 화면 출력
 - URL : /editor/linkTraceList.do
 - Controller : com.mnwise.wiseu.web.editor.web.EditorLinktraceController
 * - 이전 파일명 : linktrace_template.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        parent.getIframe();
    });
</script>
</head>
<body>
${template}
</body>
</html>