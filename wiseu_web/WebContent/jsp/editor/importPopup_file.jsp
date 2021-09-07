<%-------------------------------------------------------------------------------------------------
 * - [공통팝업>불러오기] ZIP 파일 불러오기 (팝업)
 * - [공통팝업>불러오기] HTML 파일 불러오기 (팝업)
 * - [공통팝업>불러오기] 파일 첨부하기 (팝업)
 * - URL : /editor/importPopup.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorSubForwardController
 * - 이전 파일명 : upload_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%
    String type = request.getParameter("type");
    String uploadType = request.getParameter("uploadType");
    String no = request.getParameter("no");
    if(uploadType == null) {
        if(request.getAttribute("uploadType") != null) {
            uploadType = (String)request.getAttribute("uploadType");
        } else {
            uploadType = "";
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="blank">
<title><spring:message code="editor.import"/></title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script src="/js/segment/upload.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 전송 버튼 클릭
        $("#tranBtn").on("click", function(event) {
            uploadStartProgress();
            $("#uploadFrm").attr('action', "/editor/upload.do?type=${type}&uploadType=${uploadType}&no=${no}&templateType=${templateType}").submit();
        });
    }

    function initPage() {
        if("<%=uploadType%>" == "template") {
            $("#attention").each(function() {
                $(this).show();
            });
        }
    }

    // 폼 입력값 Check - 현재 사용 안함
    function characterCheck(value, condition) {
        /*
         * 설명  : 폼 입력값을 정규식패턴을 이용해서 체크함
         * 사용법 : frmchk_char(문자열, 조건)
         * 결과값 : true/false
         * 조건  :
         *   0 = 첫글자 영문, 영문, 숫자, _ 사용가능
         *   1 = 영문만 사용가능
         *   2 = 숫자만 사용가능
         *   3 = 한글만 사용가능
         *   4 = 영문, 숫자 사용가능
         *   5 = 영문, 숫자, 한글 사용가능
         *   6 = 한글, 숫자 사용가능
         *   7 = 한글, 영문 사용가능
         *   8 = 한글을 포함하는지 여부
         */
        var objPattern;
        switch (condition) {
        case 0:
            objPattern = /^[a-zA-Z]{1}[a-zA-Z0-9_]+$/;
            break;
        case 1:
            objPattern = /^[a-zA-Z]+$/;
            break;
        case 2:
            objPattern = /^[0-9]+$/;
            break;
        case 3:
            objPattern = /^[가-힣]+$/;
            break;
        case 4:
            objPattern = /^[a-zA-Z0-9]+$/;
            break;
        case 5:
            objPattern = /^[가-힣a-zA-Z0-9]+$/;
            break;
        case 6:
            objPattern = /^[가-힣0-9]+$/;
            break;
        case 7:
            objPattern = /^[가-힣a-zA-Z]+$/;
            break;
        case 8:
            objPattern = /[가-힣]/;
        }

        return objPattern.test(value);
    }
</script>
</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0">${name}</h5></div>
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <form:form id="uploadFrm" name="uploadFrm" commandName="command" method="post" enctype="multipart/form-data" accept-charset="UTF-8" cssClass="mb-0">
        <form:errors path="*" cssClass="error_message" />
        <div class="table-responsive gridWrap overflow-y-hidden">
            <table class="table table-sm dataTable table-fixed">
                <colgroup>
                    <col width="25%" />
                    <col width="*" />
                </colgroup>
                <tbody>
                    <tr id="attention" style="display: none;">
                        <td colspan="2" class="pl-0 pr-0">
                            <div class="alert alert-secondary mb-0" role="alert">
                                <i class="fas fa-exclamation-circle"></i> <spring:message code="editor.import.zip.alert2" />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row"><em class="required">required</em><spring:message code="editor.import.msg.sfile"/></th><!-- 파일선택 -->
                        <td>
                            <div class="custom-file custom-file-sm">
                                <input type="file" class="custom-file-input custom-file-input-sm" name="file" id="file1">
                                <label class="custom-file-label custom-file-label-sm mb-0" for="file1"></label>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div><!-- //Light table -->
        <div id="progressBar" style="display: none;">
            <div id="progressBarText"></div>
            <div id="progressBarBox"><div id="progressBarBoxContent"></div></div>
        </div>
        <div class="card-footer">
            <button type="button" class="btn btn-outline-primary" id="tranBtn">
                <spring:message code="button.transfer"/><!-- 전송 -->
            </button>
        </div>
        </form:form>
    </div>
</div>

<!-- 템플릿 내용 보기 - 확인차 임시로 둠 -->
<!-- 템플릿 파일 -->
<c:if test="${not empty templateVo}">
    <textarea id="template" style="display: none;">${templateVo.template}</textarea>
</c:if>
<c:if test="${not empty multipartfileVo}">
    <!-- 첨부 파일 -->
    <input type="hidden" id="fileAlias" value="${multipartfileVo.fileAlias}">
    <input type="hidden" id="fileName" value="${multipartfileVo.fileName}">
    <input type="hidden" id="fileSize" value="${multipartfileVo.fileSize}">
    <input type="hidden" id="fileUnit" value="${multipartfileVo.fileUnit}">
    <input type="hidden" id="msg" value="${multipartfileVo.msg}">
    <input type="hidden" id="filePath" value="${multipartfileVo.filePath}">
</c:if>

<!-- 파일 첨부 -->
<c:if test="${not empty multipartfileVo || not empty templateVo}">
    <c:choose>
        <c:when test="${not empty templateVo and templateVo.result eq -1}">
            <script type="text/javascript">
                alert("${templateVo.exceptionMsg}");
            </script>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">
                var uploadType = "${templateVo.uploadType}";
                var type = "${type}";
                // ZIP 파일로 템플릿을 올렸을 때 수행
                if(uploadType == "template") {
                    if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
                        window.opener.tinyMCE.get('templateAb').setContent("");
                        window.opener.tinyMCE.get('templateAb').setContent($("#template").val());
                    } else if(opener.frameElement.contentWindow.tinyMCEmode == false) {
                        window.opener.$("#editorFrm textarea[name=template]").val("");
                        window.opener.$("#editorFrm textarea[name=template]").val($("#template").val());
                    } else {
                       window.opener.tinyMCE.get('template').setContent("");
                       window.opener.tinyMCE.get('template').setContent($("#template").val());
                   }
                } else { // 첨부파일
                    if(type=='EC'){
                           window.opener.uploadAttachFile($("#fileName").val(),$("#filePath").val());
                    }else{
                           window.opener.insertMultifile($("#fileAlias").val(), $("#fileName").val(),$("#fileSize").val(), $("#fileUnit").val(), $("#msg").val());
                    }
                }

                window.close();
            </script>
        </c:otherwise>
    </c:choose>
</c:if>

<!-- HTML 불러오기 -->
<c:if test="${not empty html}">
    <div id="html" style="display: none;">
    <code id="code_html">
        ${html}
    </code>
    </div>
    <script type="text/javascript">
        if(window.opener.tinyMCE.activeEditor == null) {
            window.opener.$("#editorFrm textarea[name=textmode]").val("");
            window.opener.$("#editorFrm textarea[name=textmode]").val($("#code_html").html());
        } else if(opener.frameElement.contentWindow.tinyMCEmode == false) {
            window.opener.$("#editorFrm textarea[name=template]").val("");
            window.opener.$("#editorFrm textarea[name=template]").val($("#code_html").html());
        } else {
            if('${templateType}' == "ab" && opener.frameElement.contentWindow.templateB == true) {
                window.opener.tinyMCE.get('templateAb').setContent("");
                window.opener.tinyMCE.get('templateAb').setContent($("#code_html").html());
            } else {
                window.opener.tinyMCE.get('template').setContent("");
                window.opener.tinyMCE.get('template').setContent($("#code_html").html());
            }
        }
        self.close();
    </script>
</c:if>
</body>
</html>