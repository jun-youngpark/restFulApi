<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] IFRAME내 저작기 화면 출력 (SMS)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : campaign_sms_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%
    String campaignNo = request.getParameter("campaignNo");
    String segmentNo = request.getParameter("segmentNo");
    String handlerType = request.getParameter("handlerType");

    if(segmentNo == null || segmentNo.equals("")) segmentNo = "0";
%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="panel" >
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src="/js/editor/${editorJsFileName}"></script><!-- 채널별 JS 파일 호출 -->
<script type="text/javascript">
    // TinyMCE
    tinyMCE.init({
        // General options
        mode : "textareas",
        language : "ko",
        theme : "advanced",
        plugins : "mnwise",
        forced_root_block : false,
        visual : 0,
        force_br_newlines : true, // 새로운 한줄 라인을 <br> 태그로 표시할 지 여부(0:false, 1:true)
        force_p_newlines : false, // 새로운 한줄 라인을 <p> 태그로 표시할 지 여부(0:false, 1:true)
        forced_root_block : '',
        editor_selector : "mceEditor",
        editor_deselector : "mceNoEditor",
        // Theme options
        theme_advanced_buttons1 : "sematic",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 : "",
        theme_advanced_buttons4 : "",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "none",
        theme_advanced_resizing : false,
        tab_focus : ":prev,:next",
        init_instance_callback: "editorFocus",
        content_css : "/css/tinymce.css",
        setup : function(ed) {
            // tinyMCE 렌더링 완료 후 추가된 이벤트들
         /*    ed.onPaste.add(function (ed, e) { //사용안함,현재는 html 태그가 저장되지 않음
                e.preventDefault();
                var content = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                ed.execCommand('mceInsertContent', false, content.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;"));
            }); */

            ed.onClick.add(function(ed, e) {
                isTitleFocus = false;
            });
            ed.onKeyUp.add(function(ed, evt) {
            	getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 90);
            });
            ed.onLoadContent.add(function(ed, evt) {
            	getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 90);
            });
            ed.onSetContent.add(function(ed, evt) {
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"} ), 90);
            });
        }
    });  // /TinyMCE

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
    }

    function initPage() {
        getCampaignInfo();
        getContentsList();

        $.extend(true, DefaultHandlerManager, {serviceGubun : 'EM', serviceNo : <%=campaignNo%>});  // 기본 핸들러 매니저 설정
    }

    function getContentsList() {
         var param = $.mdf.serializeObject('#smsContentsFrm');
         $.post("/editor/smsContsList.do", $.param(param, true), function(result) {
              $("#tmplWrap").html(result);
              $.mdf.resizeIframe("#editorIfrm");
         });
    }

    function reload() {
        var ed = tinyMCE.get('template');
        if(ed == null) {
            setTimeout(reload, 500);
        } else {
            ed.setProgressState(1); // Show progress
            window.setTimeout(function() {
                if(templateArr.length > 0) {
                	tinyMCE.get("template").setContent(templateArr[0].template.replace(/\n/g, "<br/>"));
//                  tinyMCE.get("template").setContent(templateArr[0].template.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;"));
                }

                ed.setProgressState(0); // Hide progress
                tinyMCE.execCommand('mceRemoveControl', false, 'template');
                getSlot($("#editorFrm textarea[name=handler]").val());
                tinyMCE.execCommand('mceAddControl', false, 'template');
            }, 500);
        }
    }

    var isTitleFocus = false;

    function titleFocus() {
        isTitleFocus = true;
    }

    // 웹에디터에 포커스 체크 - 개인화 및 전문 인자 넣기 위함
    function editorFocus(ed) {
        if(tinymce.isIE) {
            tinymce.dom.Event.add(ed.getWin(), 'focus', function(e) {
                isTitleFocus = false;
            });
        } else {
            tinymce.dom.Event.add(ed.getDoc(), 'focus', function(e) {
                isTitleFocus = false;
            });
        }

        if(templateArr.length > 0) {
        	tinyMCE.get("template").setContent(templateArr[0].template.replace(/\n/g, "<br/>"));
            //tinyMCE.get("template").setContent(templateArr[0].template.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;"));
        }
        window.setTimeout(parent.loadSet, 1000);
    }

    function addChar(value) {
        tinyMCE.execCommand('mceInsertContent', false, value);
    }
</script>
</head>
<body>
<form id="smsContentsFrm" name="smsContentsFrm" action="/editor/smsContsList.do" method="post" ><!-- /editor/sms_contents_list.do -->
    <input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}">
    <input type="hidden" name="contsTxt">
    <input type="hidden" name="countPerPage" value="6">
</form>

<form id="editorFrm" name="editorFrm" method="post">
<input type="hidden" name='no' value="<%=campaignNo%>" />
<input type="hidden" name='channel' value="S" />
<input type="hidden" name='segmentNo' value="<%=segmentNo%>" />
<input type="hidden" name='handlerType' value="<%=handlerType%>" />
<input type="hidden" name='seg' value=" "/>
<input type="hidden" name='templateType' value="2" />
<input type="hidden" name="retryCnt" />
<input type="hidden" name="campaignVo.handlerSeq" id="handlerSeq" value="0" />
<input type="hidden" name="templateContent" id="templateContent" />

<div class="row">
    <div class="col-3 col-phone"><!-- mobile preview -->
        <div class="phone_preview">
            <div class="preview_box">
                <div class="text">
                    <textarea id="template" name="template" style="word-break: break-all;" class="mceEditor"></textarea>
                </div>
            </div>
             <span class="letter_count">
                 <input type="text" id="chkByte" size="25" class="chk-byte"/>
             </span>
        </div>
    </div><!-- //mobile preview -->

    <div class="col-9"><!-- message list -->
        <ul class="nav nav-tabs tab01"><!-- tab -->
            <li class="nav-item">
                <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true"><spring:message code="editor.template"/></a><!-- 템플릿 -->
            </li>
            <li class="nav-item">
                <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false"><spring:message code="editor.handler"/></a><!--  핸들러 -->
            </li>
        </ul><!-- //tab -->

        <div class="tab-content"><!-- tab content -->
            <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 템플릿 tab -->
                <!-- 템플릿 리스트 -->
                    <div id="tmplWrap"></div><!-- 이전 Iframe <iframe name="smsContentsList" id="smsContentsList" src="about:blank" width="100%" frameborder="0" scrolling="no"></iframe> -->
            </div>

            <div class="tab-pane" id="handlerTab" role="tabpanel" aria-labelledby="handler-tab"><!-- 핸들러 tab -->
                <div class="edit_area mt-2">
                    <textarea class="form-control" id="handler" name="handler" rows="15"></textarea>
                </div>

                <!-- edit area - option -->
                <div class="row mt-3 mb-1 align-items-center">
                    <div class="col-3 mx-3">
                        <div class="row align-items-center">
                            <label class="form-control-label mr-1"><spring:message code="common.default.handler" /></label><!-- 기본 핸들러-->
                            <select name="handlerList" id="handlerList" class="form-control form-control-sm d-inline-block"></select>
                        </div>
                    </div>
                    <div class="col-3 align-items-center"><!-- en:col-3 -->
                        <div class="custom-control custom-switch" id="handlerWork">
                            <input type="checkbox" class="custom-control-input" id="handlerSwich" />
                            <label class="custom-control-label" for="handlerSwich"><spring:message code="editor.option.handler.3"/></label><!-- 핸들러수정-->
                        </div>
                    </div>
                </div><!-- //edit area - option -->
            </div><!-- //핸들러 tab -->
        </div><!-- //tab content -->
    </div><!-- //message list -->
</div>
</form>
</body>
</html>
