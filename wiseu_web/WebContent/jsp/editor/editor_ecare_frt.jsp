<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>2단계] IFRAME내 저작기 화면 출력 (친구톡)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : ecare_frtalk_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<%
    String ecareNo = request.getParameter("ecareNo");
    String segmentNo = request.getParameter("segmentNo");
    String serviceType = request.getParameter("serviceType");
    String handlerType = request.getParameter("handlerType");
    String subType = request.getParameter("subType");
    String sendOne = request.getParameter("sendOne");	// 단건발송여부 (O:단건)
    String webExecMode = request.getParameter("webExecMode");
    // 알림톡은 개인화인자를 사용하지 않음
    String semantic = "";

    if(segmentNo == null || segmentNo.equals("")) segmentNo = "0";

    if(webExecMode == null || webExecMode.equals(""))
        webExecMode = "1";
%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="panel">
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script type="text/javascript">
    var language = '${lang}';

    var iUserId = "${sessionScope.adminSessionVo.userVo.userId}"; // IUSERID
    var iName = "${sessionScope.adminSessionVo.userVo.nameKor}"; // INAME
    var iEmail = "${sessionScope.adminSessionVo.userVo.email}"; // IEMAIL
    var telNo = "${sessionScope.adminSessionVo.userVo.telNo}"; // TEL_NO

    var ecareNo = <%=ecareNo%>; // 이케어번호
    var segmentNo = <%=segmentNo%>; // 세그먼트번호
    var serviceType = "<%=serviceType%>"; // 서비스타입(S:스케쥴/준실시간, R:실시간)
    var subType = "<%=subType%>";
    var handlerType = "<%=handlerType%>"; // 서비스타입(G:그루비, S:스크립트)
    var channelType = "A"; //채널 타입(M:메일, S:SMS, T:MMS,  F:팩스, A:알림톡)
    var sendOne = "<%=sendOne%>";
    var webExecMode = "<%=webExecMode%>";

    // TinyMCE
    tinyMCE.init({
        // General options
        mode : "textareas",
        language : "ko",
        theme : "advanced",
        plugins : "mnwise",
        forced_root_block : false,
        visual : 0,
        readonly : 0,
        editor_selector : "mceEditor",
        editor_deselector : "mceNoEditor",
        convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
        // Theme options
        theme_advanced_buttons1 : "<%=semantic%>", // 개인화 사용시 주석 해제..
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 : "",
        theme_advanced_buttons4 : "",
        theme_advanced_toolbar_location : "<%if("sematic".equals(semantic)) {%>top<%}%>",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "none",
        theme_advanced_resizing : false,
        //auto_focus : "template",
        tab_focus : ":prev,:next",
        init_instance_callback: "editorFocus",
        content_css : "/css/tinymce.css",
        setup : function(ed) {
            // tinyMCE 렌더링 완료 후 추가된 이벤트들
        /*     ed.onPaste.add(function (ed, e) { //사용안함,현재는 html 태그가 저장되지 않음
                e.preventDefault();
                var content = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                ed.execCommand('mceInsertContent', false, content.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;"));
            }); */

            ed.onClick.add(function(ed, e) {
                isTitleFocus = false;
            });

            ed.onKeyUp.add(function(ed, evt) {
                getLength();
            });

            ed.onClick.add(function(ed, evt) {
                setFocus();
            });

            ed.onLoadContent.add(function(ed, evt) {
                getLength();

                // 배경 투명.. 스타일을 바꿔버리면 영향이 있을거라서 ㅋ
                $(".defaultSkin table").css("background","transparent");
                $(".defaultSkin iframe").css("background","transparent");
            });
        }
    });  // /TinyMCE

    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        // sendOne 변수 값이 "0" 이면 단건 발송이다.
        // 단건 발송일 경우는 핸들러를 사용하지 않기 때문에 getEcareInfo function을 호출하지 않음
        if(sendOne != "O") {
            getEcareInfo();
        } else {
            $("#handler-tab").hide();
        }

        toggleEditor("C");
        $.mdf.resizeIframe("#editorIfrm");

        $(".altalk").show();

        // 기본 핸들러 매니저 설정
        var message = {
            initError : '<spring:message code="editor.alert.editor.1" arguments="FRIENDTALK" />'
          , noHandler : '<spring:message code="editor.option.handler.0" />'
          , toggleDefault : '<spring:message code="editor.alert.handler.4" />'
          , toggleEdit : '<spring:message code="editor.alert.handler.5" />'
          , changeHandler : '<spring:message code="env.alert.handler.default"/>'
        };

        var option = {serviceGubun : 'EC' , serviceNo : <%=ecareNo%> ,message : message};
        $.extend(true, DefaultHandlerManager, option);
    }

    function reload() {
        var ed = tinyMCE.get('template');

        if(ed == null) {
            setTimeout(reLoad, 100);
        } else {
            ed.setProgressState(1); // Show progress
            window.setTimeout(function() {
                ed.setProgressState(0); // Hide progress
                tinyMCE.execCommand('mceRemoveControl', false, 'template');

                tinyMCE.execCommand('mceAddControl', false, 'template');

                if(templateArr.length > 0) {
                    $(this).delay(100, function() {
                        var template = templateArr[0].template;
                        // 이미지 태그 임시 저장
                        var re = new RegExp(/[<]img[^>]*[>]/gi);
                        if(template.match(re) != null) {
                            var tmpImg = template.match(re);
                        }
                        template=template.replace(/[<]img[^>]*[>]/gi, "");
                        template=template.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;");

                        var url = templateArr[0].imageUrl;
                        if(url != null) {
                            template = '<img style="width: 100%; height: 120px;" src="' + url + '" mce_src="' + url +'">' + template;
                        }

                        // 태그 제거후  이미지 추가
                        if(tmpImg != null) {
                            var Img = "";
                            for(var j=0;j<tmpImg.length;j++) {
                                Img += tmpImg[j];
                            }
                            template = Img + template;
                        }

                        tinyMCE.get("template").setContent(template);
                        getLength();
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
                tem = '<img style="width: 100%; height: 120px;" src="' + url + '" mce_src="' + url +'">' + tem;
            }

            tinyMCE.get("template").setContent(tem);
        }

        window.setTimeout(parent.loadSet, 1000);
    }

    // 에디터/HTML 변환
    function toggleEditor(val) {
        if(val === 5) {
            if($("#tcpip").css("display") == "none") {
                $("#tcpip").css("display", "inline");
                getRealtimeItem();
            } else {
                $("#tcpip").css("display", "none");
            }
        } else {
            if(val == "C") {
                if(webExecMode == "1" && sendOne != 'O') {   //wiseu mode & 대량발송일 때만
                    var tab = document.getElementById("handTab");
                    if(tab != null) {
                        tab.src = "/images${lang}/phone/hand_tab_01_off.gif";
                    }
                }

                $("iframe[name='frtContentsIfrm']").show();
                $("#handler_div").hide();

                document.frtContentsFrm.fileType.value = val;
                document.frtContentsFrm.submit();
            } else {
                if(webExecMode == "1") {   //wiseu mode 일 때만
                    var tab = document.getElementById("handTab");
                    if(tab != null) {
                        tab.src = "/images${lang}/phone/hand_tab_01_on.gif";
                    }
                }

                $("iframe[name='frtContentsIfrm']").hide();
                $("#handler_div").show();
                return;
            }
        }
        $.mdf.resizeIframe("#editorIfrm");
    }

    function getLength() {
        var template = getTemplateText();
        editorFrm.chkByte.value = template.length + ' ' + '<spring:message code="editor.unit.char"/>';
    }

    function getTemplateText() {
        var template = tinyMCE.get("template").getContent({ format: "text"});

        if(template.length > 1000) {
        	alert("<spring:message code='common.alert.template.alt'/>");
            template = template.substring(0, 1000);
            tinyMCE.get("template").setContent(template.replace(/\n/g, "<br/>"));
        }

        // 위 최대값 체크 때문에 아래로 이동함.
        for(var i=0;i<template.length;i++) {
            // 개인화가 있는경우 개인화 내용은 포함시키지 않음
            if(template.indexOf("{$") > -1) {
                var fPos = template.indexOf("{$");
                var ePos = template.indexOf("$}");

                var temp = template.substring(fPos, ePos+2);
                template = template.replace(temp,"");
            }
        }
        return template;
    }

    function setFocus() {
        var template = tinyMCE.get("template").getContent({ format: "text"});
        var index = template.indexOf("<img");
    }

    function resizeMiddleIframe(dynheight) {
        // 손자 IFrame 확장 후
        document.getElementById("frtContentsIfrm").style.height = parseInt(dynheight) + 10+"px";
        // 부모에게 본인 페이지 높이를 알려줌
        var height = document.body.scrollHeight;
        parent.resizeTopIframe(height);
    }

    // 템플릿 전문 변환. #｛샘플｝형태를 찾아서 순서대로 전문으로 바꿔준다.
    function replaceTemplateJeonmun() {
        if(!confirm("<spring:message code='editor.confirm.editor.1'/>")) return;
        var regexObj=/(\#｛[^#]+｝)/gi;
        var template = tinyMCE.get("template").getContent({ format: "text"});
        var cnt=1;

        while(regexObj.test(template)) {
            template = template.replace(/(\#｛[^#]+｝)/,"{$=(jm.get(\"@s@\", "+cnt+"))$}");
            cnt++;
        }
        tinyMCE.get("template").setContent(template);
    }
</script>
</head>

<body class="m-0">
<form id="editorFrm" name="editorFrm" method="post">
<input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}" />
<input type="hidden" name='no' value="<%=ecareNo%>" />
<input type="hidden" name='channel' value="C" />
<input type="hidden" name='segmentNo' value="<%=segmentNo%>" />
<input type="hidden" name='handlerType' value="<%=handlerType%>" />
<input type="hidden" name='seg' value=" " />
<input type="hidden" name='templateType' value="3" />
<input type="hidden" name='serviceType' value="<%=serviceType%>" />
<input type="hidden" name="subType" value="<%=subType%>" />
<input type="hidden" name="retryCnt" />
<input type="hidden" name="ecarePreface" />
<input type="hidden" name="ecareVo.handlerSeq" id="handlerSeq" value="0" />
<input type="hidden" name="historyMsg" />
<input type="hidden" name="templateContent"/>

<div class="card-body message_contents">
    <div class="row">
        <div class="col-3 col-phone">
            <div class="phone_preview">
                <div class="preview_box">
                    <div class="text">
                        <textarea id="template" name="template" style="word-break: break-all;" class="mceEditor"></textarea>
                    </div>
                </div>
                <span class="letter_count" style="overflow: inherit;">
                    <input type="text" id="chkByte" size="25" readonly="readonly" class="chk-byte" />
                </span>
            </div>
        </div>
        <div class="col-9">
            <ul class="nav nav-tabs tab01">
                <li class="nav-item">
                    <a class="nav-link active" id="image-tab" data-toggle="tab" href="#imageTab" role="tab" aria-controls="image" aria-selected="true">이미지</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false"><spring:message code="editor.handler"/></a><!--  핸들러-->
                </li>
            </ul>

            <div class="tab-content"><!-- tab content -->
                <div class="tab-pane active" id="imageTab" role="tabpanel" aria-labelledby="image-tab"><!-- 이미지 tab -->
                    <iframe name="frtContentsIfrm" id="frtContentsIfrm" src="about:blank" width="100%" height="375" frameborder="0" scrolling="no"></iframe>
                </div>

                <div class="tab-pane" id="handlerTab" role="tabpanel" aria-labelledby="handler-tab"><!-- 핸들러 tab -->
                    <div class="edit_area mt-2">
                        <textarea class="form-control" id="handler" name="handler" rows="15"></textarea>
                    </div>

                    <div class="row mt-3 mb-1 align-items-center">
                        <div class="col-4 mx-3">
                            <div class="row align-items-center"><!-- 기본 핸들러-->
                                <label class="form-control-label mr-1"><spring:message code="common.default.handler" /></label>
                                <select name="handlerList" id="handlerList" class="form-control form-control-sm d-inline-block"></select>
                            </div>
                        </div>
                        <div class="col-3 align-items-center"><!-- en:col-3 -->
                            <div class="custom-control custom-switch" id="handlerWork"><!-- 핸들러수정-->
                                <input type="checkbox" class="custom-control-input" id="handlerSwich" />
                                <label class="custom-control-label" for="handlerSwich"><spring:message code="editor.option.handler.3"/></label>
                            </div>
                        </div>
                    </div>
                </div><!-- //핸들러 tab -->
            </div>
        </div>
    </div>
</div>
</form>

<form name="frtContentsFrm" action="/editor/frtContsList.do" method="post" target="frtContentsIfrm"><!-- /editor/mms_contents_list.do -->
    <input type="hidden" name="fileType" value="C">
    <input type="hidden" name="contsTxt">
    <input type="hidden" name="countPerPage" value="6">
</form>

</body>
</html>