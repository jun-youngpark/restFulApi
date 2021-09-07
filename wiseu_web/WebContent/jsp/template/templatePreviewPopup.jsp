<%-------------------------------------------------------------------------------------------------
  * - [템플릿>템플릿 등록] 템플릿 보기(팝업) <br/>
  * - URL : /template/templatePreviewPopup.do <br/>
  * - Controller : com.mnwise.wiseu.web.template.web.TemplatePreviewController <br/>
  * - 이전 파일명 : template_preview_content.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<html>
<head>
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body style="margin: 0; top: 0; left: 0; overflow-y=scroll">
${contentVo.content}
</body>
</html>