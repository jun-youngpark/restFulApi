<%-------------------------------------------------------------------------------------------------
  * - [캠페인>캠페인 등록>2단계] 템플릿 불러오기 (팝업) - 템플릿명 클릭시 템플릿 내용 가져오기 <br/>
  * - URL : /template/editor/importTemplate.do <br/>
  * - Controller : com.mnwise.wiseu.web.template.web.TemplatePreviewController <br/>
 * - 이전 파일명 : template_preview_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
    <code id="code_template">
    ${contentVo.content}
    </code>
<input type="text" name="msg" id="msg" value="${msg}" />
