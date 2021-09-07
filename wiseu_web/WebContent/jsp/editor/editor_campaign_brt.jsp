<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] IFRAME내 저작기 화면 출력 (브랜드톡)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : campaign_brandtalk_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%
    String campaignNo = request.getParameter("campaignNo");
    String segmentNo = request.getParameter("segmentNo");
    String handlerType = request.getParameter("handlerType");
    String webExecMode = request.getParameter("webExecMode");
    String channelType = request.getParameter("channelType");
    // 알림톡은 개인화인자를 사용하지 않음
    //String semantic = "";
    if(segmentNo == null || segmentNo.equals("")) segmentNo = "0";
    if(webExecMode == null || webExecMode.equals(""))
        webExecMode = "1";
%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="panel" >
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src="/js/editor/${editorJsFileName}"></script><!-- 채널별 JS 파일 호출 -->
<!-- TinyMCE -->
<script type="text/javascript">
    var language = '${lang}';

    //var iUserId = "${sessionScope.adminSessionVo.userVo.userId}"; // IUSERID
    //var iName = "${sessionScope.adminSessionVo.userVo.nameKor}"; // INAME
    //var iEmail = "${sessionScope.adminSessionVo.userVo.email}"; // IEMAIL
    //var telNo = "${sessionScope.adminSessionVo.userVo.telNo}"; // TEL_NO

    var campaignNo = <%=campaignNo%>; // 이케어번호
    var segmentNo = <%=segmentNo%>; // 세그먼트번호
    var handlerType = "<%=handlerType%>"; // 서비스타입(G:그루비, S:스크립트)
    var channelType = "<%=channelType%>;" //채널 타입(M:메일, S:SMS, T:MMS,  F:팩스, A:알림톡,B:브랜드톡)
    var webExecMode = "<%=webExecMode%>";

    // TinyMCE
    tinyMCE.init({
        // General options
        readonly : 1,   //1은 읽기 모드, 0은 쓰기 가능모드
        mode : "textareas",
        language : "ko",
        theme : "advanced",
        plugins : "mnwise",
        forced_root_block : false,
        visual : 0,
        force_br_newlines : 0, // 새로운 한줄 라인을 <br> 태그로 표시할 지 여부(0:false, 1:true)
        force_p_newlines : 0, // 새로운 한줄 라인을 <p> 태그로 표시할 지 여부(0:false, 1:true)
        editor_selector : "mceEditor",
        editor_deselector : "mceNoEditor",
        convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
        // Theme options
        theme_advanced_buttons1 : "", // 개인화 사용시 주석 해제..
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 : "",
        theme_advanced_buttons4 : "",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "none",
        theme_advanced_resizing : false,
        //auto_focus : "template",
        tab_focus : ":prev,:next",
        init_instance_callback: "editorFocus",
        content_css : "/css/tinymce/altalk.css",
        setup : function(ed) {
            // tinyMCE 렌더링 완료 후 추가된 이벤트들
        /*     ed.onPaste.add(function (ed, e) {    //사용안함,현재는 html 태그가 저장되지 않음
                e.preventDefault();
                var content = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                ed.execCommand('mceInsertContent', false, content.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;"));
            }); */
            ed.onClick.add(function(ed, e) {
                isTitleFocus = false;
            });
            ed.onKeyUp.add(function(ed, evt) {
            	getLengthAndLimitLength("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 1000);
            });
            ed.onSetContent.add(function(ed, evt) {
            	getLengthAndLimitLength("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 1000);
            });
        }
    });

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $('#handler-tab').on("click", function(event) {
            parent.resizeTopIframe(600);
        });
    }
    function initPage() {
        getCampaignInfo();
          // 에디터 제목 자동완성
        getContentsList();
        //$.mdf.resizeIframe("#editorIfrm");
        // 핸들러 수정 상태일 경우 멘트 출력

        if(parent.$("#editorFrm input[name='campaignVo.templateType']").val() == '11') {
            toggleEditMode(true);
        }

        $.extend(true, DefaultHandlerManager, {serviceGubun : 'EM', serviceNo : <%=campaignNo%>});  // 기본 핸들러 매니저 설정
    }

    function reload() {
        var ed = tinyMCE.get('template');
        if(ed == null) {
            setTimeout(reload, 100);
        } else {
            ed.setProgressState(1); // Show progress
            window.setTimeout(function() {
                ed.setProgressState(0); // Hide progress
                tinyMCE.execCommand('mceRemoveControl', false, 'template');
                tinyMCE.execCommand('mceAddControl', false, 'template');
                if(templateArr.length > 0) {
                    $(this).delay(100, function() {
                        setContsNo();
                    });
                }
            }, 1000);
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
            var tem = templateArr[0].template;
            tem = tem.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;");
            var url = templateArr[0].imageUrl;
            if(url != null) {
                tem = '<img style="width: 100%; height: 120px;" src="' + url + '" mce_src="' + url +'" >' + tem;
            }

            tinyMCE.get("template").setContent(tem);
        }
        window.setTimeout(parent.loadSet, 1000);
    }

    // 에디터/HTML 변환
    function getContentsList() {
        var param = $.mdf.serializeObject('#brtContentsFrm');
        $.post("/editor/kkoContsList.do", $.param(param, true), function(result) {
            $("#tmplWrap").html(result);
        });
    }

    function setFocus() {
        var template = tinyMCE.get("template").getContent();
        var index = template.indexOf("<img");
    }

    function setContsNo() {
        var contsNo = parent.$("input[name='campaignVo.kakaoImageNo']").val();
        if(contsNo>0){
            viewBrt(contsNo, 'B');
        }
    }

</script>
</head>
<body >
<form id="editorFrm" name="editorFrm" method="post">
<input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}" />
<input type="hidden" name='no' value="<%=campaignNo%>" />
<input type="hidden" name='channel' value="<%=channelType%>" />
<input type="hidden" name='segmentNo' value="<%=segmentNo%>" />
<input type="hidden" name='handlerType' value="<%=handlerType%>" />
<input type="hidden" name='seg' value=" " />
<input type="hidden" name='templateType' value="3" />
<input type="hidden" name="retryCnt" />
<input type="hidden" name="campaignPreface" />
<input type="hidden" name="campaignVo.handlerSeq" id="handlerSeq" value="0" />
<input type="hidden" name="templateContent" id="templateContent" />
<input type="hidden" name="kakaoButtons" id="kakaoButtons" />

<div class="row">
    <!-- mobile preview -->
    <div class="col-3">
        <div class="phone_preview">
            <div class="preview_box">
                <div class="text">
                    <textarea id="template" name="template" style="word-break: break-all;" class="mceEditor"></textarea>
                </div>
            </div>
            <span class="letter_count" style="overflow: inherit;">
                <input type="text" id="chkByte" size="25" readonly="readonly" class="chk-byte"/>
            </span>
        </div>
    </div>
    <!-- //mobile preview -->

    <!-- message list -->
    <div class="col-9">
        <!-- tab -->
        <ul class="nav nav-tabs tab01">
            <li class="nav-item">
                <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true">
                    <spring:message code="editor.template"/><!--  템플릿-->
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false">
                    <spring:message code="editor.handler"/><!-- 핸들러 -->
                </a>
            </li>
        </ul> <!-- //tab -->

        <!-- tab content -->
        <div class="tab-content">
            <!-- 템플릿 tab -->
            <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab">
            <div  id="tmplWrap"><!-- 템플릿 리스트 --> </div>
                 <!-- <iframe id="brtContentsIfrm" name="brtContentsIfrm" src="about:blank" scrolling="no" style="height:450px"></iframe> -->
            </div>

            <!-- 핸들러 tab -->
            <div class="tab-pane" id="handlerTab" role="tabpanel" aria-labelledby="handler-tab">
                <!-- textarea -->
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
                </div> <!-- //edit area - option -->
            </div> <!-- //핸들러 tab -->
        </div> <!-- //tab content -->
    </div> <!-- //message list -->
</div> <!-- //message -->
</form>

<form id="brtContentsFrm" name="brtContentsFrm" method="post" target="brtContentsIfrm"><!-- /editor/kakaoContentsList.do -->
    <input type="hidden" name="fileType" value="B">
    <input type="hidden" name="contsTxt">
    <input type="hidden" name="countPerPage" value="8">
     <input type="hidden" name="searchColumn">
    <input type="hidden" name="searchWord">
    <input type="hidden" name="channelType" value="<%=channelType%>" />
</form>

</body>
</html>
