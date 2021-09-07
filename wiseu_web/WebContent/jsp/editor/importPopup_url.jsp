<%-------------------------------------------------------------------------------------------------
 * - [공통팝업>불러오기] URL 불러오기 (팝업)
 * - URL : /editor/importPopup.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorSubForwardController
 * - 이전 파일명 : url_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="blank">
<title><spring:message code="editor.import.url"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 등록 버튼 클릭
        $("#registBtn").on("click", function(event) {
            $("#urlFrm input[name=web]").val($("input[name='url']").val());
            $("#urlFrm").submit();
        });
    }
</script>
</head>
<body>
<form id="urlFrm" name="urlFrm" action="/editor/url_template.do" method="post">
<input type="hidden" name="web">
<input type="hidden" name="templateType" value="${templateType}">
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="editor.import.url"/></h5></div><!-- URL 불러오기 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="table-responsive gridWrap">
            <table class="table table-sm dataTable">
                <colgroup>
                    <col width="25%" />
                    <col width="*" />
                </colgroup>
                <tbody>
                    <tr>
                        <th scope="row"><em class="required">required</em><spring:message code="editor.import.url"/></th>
                        <td><input type="text" class="form-control form-control-sm" name="url" value="http://" /></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div><!-- //Light table -->

    <div class="card-footer">
        <button type="button" class="btn btn-outline-primary" id="registBtn">
            <spring:message code="button.regist"/><!-- 등록 -->
        </button>
    </div>
</div>
</form>

<!-- HTML 불러오기 -->
<c:if test="${not empty template}">
    <div id="html" style="display: none;">
        <textarea id="template_html">
            ${template}
        </textarea>
    </div>
    <script type="text/javascript">
        if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
            window.opener.tinyMCE.get('templateAb').setContent("");
            window.opener.tinyMCE.get('templateAb').setContent($("#template_html").val());
        } else if(opener.frameElement.contentWindow.tinyMCEmode == false) {
            window.opener.$("#editorFrm textarea[name=template]").val("");
            window.opener.$("#editorFrm textarea[name=template]").val($("#template_html").html());
        } else {
            window.opener.tinyMCE.get('template').setContent("");
            window.opener.tinyMCE.get('template').setContent($("#template_html").val());
        }
        self.close();
    </script>
</c:if>

</div>
</body>
</html>