<%-------------------------------------------------------------------------------------------------
 * - [공통팝업>불러오기] 하이퍼링크 (팝업)<br/>
 * - URL : /editor/linkPopup.do?type=hyperlink <br/>
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorLinkForwardController
 * Title       : 저작기 하이퍼링크
 * Description : 저작기에서 선택된 값에 하이퍼링크를 건다.
 * - 이전 파일명 : hyperlink_editor.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>

<%@page import="java.net.URLDecoder"%><html>
<head>
<meta name="decorator" content="blank">
<title>Upload</title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 등록 버튼 클릭
        $("#registBtn").on("click", function(event) {
            var url = $("input[name='url']").val();
            var hyperlink = decodeURIComponent("${hyperlink}");

            hyperlink = hyperlink.link(url);
            hyperlink = hyperlink.replace(/href=\'/i, "href=\"");
            hyperlink = hyperlink.replace(/\'>/i, "\">");

            window.opener.tinyMCE.execCommand('mceInsertContent', false, hyperlink);
            self.close();
        });
    }

    function initPage() {
        $("input[name='url']").focus();
    }
</script>
</head>
<body>
    <div class="card pop-card">
        <div class="card-header"><!-- title -->
            <div class="row table_option">
                <div class="col-10"><h5 class="mb-0"><spring:message code="editor.link.hyper"/></h5></div><!-- 하이퍼링크 -->
                <div class="col-2 justify-content-end">
                    <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                </div><!-- 닫기 -->
            </div>
        </div>

        <form id="urlFrm" name="urlFrm" action="/editor/url_template.do" method="post">
        <input type="hidden" name="web">
        <input type="hidden" name="templateType" value="${templateType}">
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
                <spring:message code="button.regist" /><!-- 등록 -->
            </button>
        </div>
        </form>
    </div>
</body>
</html>