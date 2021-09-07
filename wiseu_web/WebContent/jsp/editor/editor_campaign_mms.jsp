<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] IFRAME내 저작기 화면 출력 (LMS/MMS)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : campaign_mms_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%
    String campaignNo = request.getParameter("campaignNo"); // 캠페인번호
    String segmentNo = request.getParameter("segmentNo"); // 세그먼트번호
    String handlerType = request.getParameter("handlerType"); // 핸들러타입(G:그루비, S:스크립트)

    if(segmentNo == null || segmentNo.equals("")) segmentNo = "0"; // 세그먼트번호가 없으면 임시로 0 셋팅
%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="panel" >
<%@ include file="/jsp/include/plugin.jsp" %>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src="/js/editor/mms.js"></script>
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
        force_br_newlines : 0, // 새로운 한줄 라인을 <br> 태그로 표시할 지 여부(0:false, 1:true)
        force_p_newlines : 0, // 새로운 한줄 라인을 <p> 태그로 표시할 지 여부(0:false, 1:true)
        visual : 0,
        editor_selector : "mceEditor",
        editor_deselector : "mceNoEditor",
        convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
        // Theme options
        theme_advanced_buttons1 : "sematic",
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
        content_css : "/css/tinymce.css",
        setup : function(ed) {
            // tinyMCE 렌더링 완료 후 추가된 이벤트들
          /*   ed.onPaste.add(function (ed, e) { //사용안함,현재는 html 태그가 저장되지 않음
                e.preventDefault();
                var content = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                ed.execCommand('mceInsertContent', false, content.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;"));
            }); */

            ed.onClick.add(function(ed, e) {
                isTitleFocus = false;
            });
            ed.onKeyUp.add(function(ed, evt) {
            	getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 1000);
            });
            ed.onLoadContent.add(function(ed, evt) {
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 1000);
            });
            ed.onSetContent.add(function(ed, evt) {
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"} ), 1000);
            });
            ed.onClick.add(function(ed, evt) {
                setFocus();
            });
        }
    });  // /TinyMCE

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $('#template-tab').on("click", function(event) {
            getContentsList("T");
        });
        $('#image-tab').on("click", function(event) {
            getContentsList("I");
        });
    }

    function initPage() {
        getCampaignInfo();
        setDongboHandler();
        getContents();
        getContentsList("T");

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

                getSlot($("#editorFrm textarea[name=handler]").val());

                tinyMCE.execCommand('mceAddControl', false, 'template');
                if(templateArr.length > 0) {
                    $(this).delay(100, function() {
                        tinyMCE.get("template").setContent(replaceSpecialChar(templateArr[0].template));
                    });
                }
            }, 1000);
        }
    }

    // 이미지 태그는 제외하고 텍스트 내용 중 특수문자를 변경한다
    function replaceSpecialChar(template) {
        // 이미지 태그 임시 저장
        var re = new RegExp(/[<]img[^>]*[>]/gi);
        if(template.match(re) != null) {
            var tmpImg = template.match(re);
        }

        template = template.replace(/[<][i][m][g][^>]*[>]/gi, "");

        template = template.replace(/\n/g, "<br/>");

        // 태그 제거후  이미지 추가
        if(tmpImg != null) {
            var img = "";
            for(var j = 0; j < tmpImg.length; j++) {
                img += tmpImg[j];
            }
            template = img + template;
        }

        return template;
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
            tinyMCE.get("template").setContent(replaceSpecialChar(templateArr[0].template));
        }
        window.setTimeout(parent.loadSet, 1000);
    }

    function insertMmsTemplateContents(filePreviewPath) {
        filePreviewPath = '<img height="149px" width="203px"  src="' + filePreviewPath + '">';
        //tinyMCE.get('template').setContent(filePreviewPath + templateArr[0].template);
    }

    function setDongboHandler() {
        var template = $("#editorFrm textarea[name=template]").val();

        var handlerLine = new Line($("#editorFrm textarea[name=handler]").val());
        var line = "";
        var reHandler = "";

        // 개인전송
        if((template.indexOf("{%") > -1 && template.indexOf("%}")) || (template.indexOf("{$") > -1 && template.indexOf("$}"))) {
            while((line = handlerLine.readLine()) != null) {
                if(line.indexOf('mmsagent.setDongbo(') > -1) {
                    reHandler += line.replace("TRUE", "FALSE");
                    reHandler += "\n";
                } else {
                    reHandler += line;
                    reHandler += "\n";
                }
            }
        } else { // 동보전송
            while((line = handlerLine.readLine()) != null) {
                if(line.indexOf('mmsagent.setDongbo') > -1) {
                    reHandler += line.replace("FALSE", "TRUE");
                    reHandler += "\n";
                } else {
                    reHandler += line;
                    reHandler += "\n";
                }
            }
        }

        $("#editorFrm textarea[name=handler]").val(reHandler);
    }

    function addChar(value) {
        tinyMCE.execCommand('mceInsertContent', false, value);
    }

    function getContentsList(fileType) {
         if(fileType=='T'){    //텍스트
             var param = $.mdf.serializeObject('#mmsContentsFrm');
             var url = "/editor/mmsContsList.do";
         }else{
             var param = $.mdf.serializeObject('#mmsImageFrm');
             var url = "/editor/mmsContsList.do";
         }
         $.post(url, $.param(param, true), function(result) {
             $("#tmplWrap").html(result);
             $.mdf.resizeIframe("#editorIfrm");
         });
    }


    // TODO : 이미지 등록후에는 포커스가 갈수 없도록 수정
    function setFocus() {
    }

</script>
</head>
<body>
<form id="mmsContentsFrm" name="mmsContentsFrm" action="/editor/mmsContsList.do" method="post" ><!-- /editor/mms_contents_list.do -->
    <input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}">
    <input type="hidden" name="fileType" value="T">
    <input type="hidden" name="contsTxt">
    <input type="hidden" name="countPerPage" value="6">
</form>

<form id="mmsImageFrm" name="mmsImageFrm" action="/editor/mmsContsList.do" method="post" ><!-- /editor/mms_contents_list.do -->
    <input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}">
    <input type="hidden" name="fileType" value="I">
    <input type="hidden" name="contsTxt">
    <input type="hidden" name="countPerPage" value="8">
</form>

<form id="editorFrm" name="editorFrm" method="post">
<input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}" />
<input type="hidden" name='no' value="<%=campaignNo%>" />
<input type="hidden" name='channel' value="T" />
<input type="hidden" name='segmentNo' value="<%=segmentNo%>" />
<input type="hidden" name='handlerType' value="<%=handlerType%>" />
<input type="hidden" name='seg' value=" "/>
<input type="hidden" name='templateType' value="3" />
<input type="hidden" name="retryCnt" />
<input type="hidden" name="campaignVo.handlerSeq" id="handlerSeq" value="0" />
<input type="hidden" name="templateContent" id="templateContent" />

<div class="table-responsive gridWrap">
    <table class="table table-sm dataTable table-fixed" style="margin-top: -1px !important;">
        <colgroup>
            <col width="11%" />
            <col width="*" />
        </colgroup>
        <tbody>
        <tr>
           <th scope="row"><spring:message code="campaign.menu.subject" /></th><!-- 제목 -->
           <td colspan="5">
           <input type="text"  name="campaignPreface" id="campaignPreface" onfocus="javascript:titleFocus()"
               onselect="javascript:storeCaret(this);" onclick="javascript:storeCaret(this);" onkeyup="javascript:storeCaret(this);"
                maxlength="100" class="form-control form-control-sm" required/>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div class="row">
    <div class="col-3 col-phone"><!-- mobile preview -->
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
    </div><!-- //mobile preview -->

    <div class="col-9"><!-- message list -->
        <ul class="nav nav-tabs tab01"><!-- tab -->
            <li class="nav-item">
                <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true">
                    <spring:message code="editor.template"/><!-- 템플릿 -->
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="image-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="image" aria-selected="true">
                    <spring:message code="editor.image"/><!-- 이미지 -->
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false">
                    <spring:message code="editor.handler"/><!--  핸들러 -->
                </a>
            </li>
        </ul><!-- //tab -->

        <div class="tab-content"><!-- tab 내용 -->
            <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 템플릿 tab -->
                <div  id="tmplWrap"></div>
                <!-- <iframe name="mmsContentsList" id="mmsContentsList" src="about:blank" frameborder="0" scrolling="no" width="100%" height="565"></iframe> -->
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
        </div><!-- //tab 내용 -->
    </div><!-- //message list -->
</div>
</form>
</body>
</html>