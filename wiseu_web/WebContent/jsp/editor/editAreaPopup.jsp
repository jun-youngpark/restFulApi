<%-------------------------------------------------------------------------------------------------
 * - URL : /editor/editAreaPopup.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorAreaPopupController
 * - 이전 파일명 : edit_area_popup.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%
    String type = request.getParameter("type"); // 에디터 타입 (campaign,ecare)
%>
<html>
<head>
<%@ include file="/jsp/include/plugin.jsp" %>
<script src='/js/editor/jquery.insertAtCaret.js'></script>
<script src="/plugin/edit_area/edit_area_full.js"></script>
<script type="text/javascript">
    var language = '${lang}';
    var tagType = '';
    var syntaxType = 'html';
    var openType = '<%=type%>';
    var readonlyYn = true;

    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        $("#editorTab", opener.document).attr("src", "/images${lang}/editor/btn_editor_off.gif");
        tagType = opener.$("#editorFrm input[name=tagType]").val();
        var textEditorArea = $("textarea[name=textEditorArea]").val();
        //alert(openType);
        if(openType == 'ecare') {
            //alert(tagType);
            if (tagType == 'ecare_cover') {
                $("textarea[name=textEditorArea]").val(opener.$("#editorFrm textarea[name=cover]").val());
            } else if (tagType == 'ecare_preface') {
                $("textarea[name=textEditorArea]").val(opener.$("#editorFrm textarea[name=preface]").val());
            } else if (tagType == 'ecare_textmode') {
                $("textarea[name=textEditorArea]").val(opener.$("#editorFrm textarea[name=textmode]").val());
            } else if (tagType == 'ecare_handler') {
                syntaxType = "java";
                // 핸들러 수정외 일 시
                if (opener.$("#editorFrm input[name=templateType]").val() != 11) {
                    readonlyYn = false;
                }

                $("textarea[name=textEditorArea]").val(opener.$("#editorFrm textarea[name=handler]").val());
            } else {
                alert("No Data select");
            }
        } else if(openType == 'campaign') {
            //alert(tagType);
            if (tagType == 'campaign_template') {
                $("textarea[name=textEditorArea]").val(opener.$("#editorFrm textarea[name=template]").val());
            } else if (tagType == 'campaign_handler') {
                syntaxType = "java";
                // 핸들러 수정외 일 시
                if (opener.$("#editorFrm input[name=templateType]").val() != 11) {
                    readonlyYn = false;
                }

                $("textarea[name=textEditorArea]").val(opener.$("#editorFrm textarea[name=handler]").val());
            } else {
                alert("No Data select");
            }
        }

        editAreaLoader.init({
            id: "textEditorArea"    // id of the textarea to transform
          , start_highlight: true
          , font_size: "9"
          , font_family: "verdana, monospace"
          , allow_resize: "y"
          , allow_toggle: false
          , language: "en"
          , syntax: syntaxType
          , toolbar: "search, go_to_line, |, undo, redo, |, select_font, |, highlight"
          , is_editable: readonlyYn
         });
    }

    // 부모창으로 데이터 보내기
    function sendOpener() {
        tagType = opener.$("#editorFrm input[name=tagType]").val();
        // EditorArea iframe 내부의 textarea의 데이터를 외부의 textarea에 적용
        $("textarea[name=textEditorArea]").val(parent.frame_textEditorArea.textarea.value);

           var inputText = $("textarea[name=textEditorArea]").val();
        if(openType == 'ecare') {
            if(tagType == 'ecare_cover') {
                parent.opener.$("#editorFrm textarea[name=cover]").val(inputText);
            } else if(tagType == 'ecare_preface') {
                parent.opener.$("#editorFrm textarea[name=preface]").val(inputText);
            } else if(tagType == 'ecare_textmode') {
                parent.opener.$("#editorFrm textarea[name=textmode]").val(inputText);
            } else if(tagType == 'ecare_handler') {
                parent.opener.$("#editorFrm textarea[name=handler]").val(inputText);
            } else {
                alert("Faild Data");
            }
        } else if (openType == 'campaign') {
            if(tagType == 'campaign_template') {
                parent.opener.$("#editorFrm textarea[name=template]").val(inputText);
                 //parent.opener.template_ifr.tinymce.value = inputText;
            } else if(tagType == 'campaign_handler') {
                parent.opener.$("#editorFrm textarea[name=handler]").val(inputText);
            } else {
                alert("Faild Data");
            }
        }
        window.close();
    }
</script>

</head>
<body style="margin: 0px;">
<form id="editorFrm" name="editorFrm" action="/editor/editorAreaPopup.do" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#e9edf0">
    <!-- 저작기 시작 -->
    <tr height="30" style="padding-left:10px;">
        <td class="Title_text_01"><img src="/images/common/popup/popup_t_blet.gif" style="vertical-align: middle"><spring:message code="editor.ecare.window"/></td>
        <td align="right" style="padding-right:10px;"><a href="javascript:close();"><img src="/images/common/popup/popup_t_close.gif"></a></td>
    </tr>
</table>
<div>
    <textarea id="textEditorArea" name="textEditorArea" style="width: 100%; height: 90%;  word-break:break-all;"></textarea>
</div>
<div align="center" valign="middle" style="height: 30px;"><a href="javascript:sendOpener();"><img src="/images${lang}/editor/btn_editor_refresh.gif" style="vertical-align: middle;"/></a></div>
</form>

</body>
</html>