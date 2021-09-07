<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록>2단계] IFRAME내 저작기 화면 출력 (PUSH)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : campaign_push_editor.jsp
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
<script src="/plugin/form/jquery.placeholder-1.8.7.js"></script>
<script src="/js/editor/${editorJsFileName}"></script><!-- 채널별 JS 파일 호출 -->
<script type="text/javascript">
    // TinyMCE
    tinyMCE.init({
        // General options
        mode : "textareas",
        language : "${sessionScope.adminSessionVo.language}", // 언어(en:영어, ko:한국어)
        theme : "advanced",
        plugins : "fullpage,mnwise", // 플러그인
        editor_selector : "mceEditor",
        editor_deselector : "mceNoEditor",
        theme_advanced_fonts : '<spring:message code="editor.font" />',
        theme_advanced_font_sizes : "1,2,3,4,5,6",
        forced_root_block : 0,
        force_br_newlines : 0, // 새로운 한줄 라인을 <br> 태그로 표시할 지 여부(0:false, 1:true)
        force_p_newlines : 1, // 새로운 한줄 라인을 <p> 태그로 표시할 지 여부(0:false, 1:true)
        visual : 0, // 템플릿 테두리 점선 표시 여부
        convert_fonts_to_spans : false, // font 태그를 <span font-style> 태그로 변경하지 않게 함.
        extended_valid_elements : "td[*|nowrap],style[*],object[*],param[*],embed[*],center[*],img[*],area[*],a[*]", // 유효성 체크 제외할 태그들
        convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
        cleanup : 1, // 이것을 사용하게 되면 템플릿이 표준에 맞게 변하진 않으나 tinyMCE 속성값이 붙음(0:false, 1:true)
        fullpage_default_title : "", // <title> 태그 초기값
        // Theme options
        theme_advanced_buttons1 : "sematic,htmlToggle,|,mysplitbutton,bold,italic,underline,strikethrough,|,cut,copy,paste,pastetext,pasteword,|,justifyleft,justifycenter,justifyright,justifyfull,fontselect,fontsizeselect,bullist,numlist,forecolor,backcolor,print",
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
        setup : function(ed) { // tinyMCE 렌더링 완료 후 추가된 이벤트
            ed.onClick.add(function(ed, e) {
                isTitleFocus = false;
            });

            ed.onKeyUp.add(function(ed, evt) { // tinyMCE 렌더링 완료 후 추가된 이벤트
                getByte(ed.id);
                updatePush(ed.id);
            });

            ed.onClick.add(function(ed, evt) { // tinyMCE 렌더링 완료 후 추가된 이벤트
            });

            ed.onLoadContent.add(function(ed, evt) { // tinyMCE 렌더링 완료 후 추가된 이벤트
                getByte(ed.id);
                // 배경 투명.. 스타일을 바꿔버리면 영향이 있을거라서 ㅋ
                $(".defaultSkin table").css("background","transparent");
                $(".defaultSkin iframe").css("background","transparent");
                updatePush("template");
                // template 정보를 가져온 후가 이쪽 이벤트라서 초기화를 여기서 한다.
                getByte("template_pop");
                updatePush("template_pop");
                updatePush("title");
                updatePushBigImage();
                setSemanticOption();
            });
        }
    });  // /TinyMCE

    var rules = {
        template_pop : { notBlank : true , maxlength: 2000 },
        pushMsgInfo : { notBlank : true },
        pushMenuId : { notBlank : true, maxlength : 6, alphaDigit : true },
        pushImgUrl : { maxlength : 500, url : true },
        pushBigImgUrl : { maxlength : 500, url : true },
        pushWebUrl : { maxlength : 500, url : true },
    };

    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $("#template_pop").keyup(function() {
            getByte("template_pop");
            updatePush("template_pop");
        });

        $("#htmlToggle").on("click", function(event) {
            tinyMCE.execCommand('mceToggleEditor',false,'template');
            $("#htmlToggleTr").css("display","none");
        });

        $("#closeBtn").on("click", function(event) {
            toggleHandler();
        });

        $('#template-tab').on("click", function(event) {
            parent.resizeTopIframe(600);
        });

        $('#handler-tab').on("click", function(event) {
            parent.resizeTopIframe(450);
        });
    }

    function initPage() {
        getCampaignInfo();
        showPushAppList();
        initPush();
        $("#pushMenuId").placeholder();
        $("#pushImgUrl").placeholder();
        $("#pushWebUrl").placeholder();
        $("#pushBigImgUrl").placeholder();

        $.extend(true, DefaultHandlerManager, {serviceGubun : 'EM', serviceNo : <%=campaignNo%>});  // 기본 핸들러 매니저 설정
        $.mdf.resizeIframe("#editorIfrm");
    }

    function initPush() {
        $("#pushBigImgUrl").change(function() {
            updatePushBigImage();
        });

        $(parent.document).find("#campaignPreface").keyup(function() {
            updatePush("title");
        });
    }

    function showPushAppList() {
        //PushService.selectPushAppList("Y", { callback: showPushAppCallback });
        //getAjaxCall("/env/Y/env_push_setting.do" , showPushAppCallback);
        $.post("/env/selectPushAppList.json?useOnly=Y", null, function(result) {
            if(result == null) {
                alert("<spring:message code='campaign.alert.msg.init.1' />");  // 등록된 앱이 없습니다.[환경설정 > PUSH 앱 관리] 메뉴에서 앱을 등록하세요.
                $("#pushAppId").append("<option value=''><spring:message code='common.option.no.registered.app' /></option>");
                if(top) top.location.href="/campaign/campaignList.do";  // /campaign/campaign.do
                else location.href="/campaign/campaignList.do";  // /campaign/campaign.do
                return;
            }

            //$("#pushAppId").append("<option value=''>PUSH APP LIST</option>");
            for(var i=0; i < result.length; i++) {
                // <%-- pushAppId 설정 및 사용. --%>
                // <c:set var="currPushAppId" value="" />${scenarioVo.pushAppId}|${scenarioVo.pushAppId}
                // <c:if test="${not empty param.pushAppId}"><c:set var="currPushAppId" value="${param.pushAppId}" /></c:if>
                // <c:if test="${not empty pushInfoMap}"><c:set var="currPushAppId" value="${pushInfoMap.PUSH_APP_ID}" /></c:if>
                var selected = "";
                if('${currPushAppId}'==result[i].push_app_id) {
                    selected = "selected='selected'";
                }
                $("#pushAppId").append("<option value='"+result[i].push_app_id+"' "+selected+">"+result[i].push_app_nm+"</option>");
            }
        });
    }

    function validatePushData() {
        if(!$.mdf.validForm("#editorFrm", rules)) {
            return false;
        }

        $(".placeholder").val("");
        return true;
    }

    function validateURL(selector) {
        var value = ($(selector).val() == $(selector).attr("placeholder")) ? "" : $(selector).val();

        if(value.length > 0 && $.mdf.isNotBlank(value) && !$.mdf.isUrl(value)) {
            alert('<spring:message code="common.alert.url.not.valid" />');  // 올바른 URL 형태가 아닙니다.
            $(selector).focus();
            return false;
        }
        return true;
    }

    function validateMaxLength(selector) {
        var value = ($(selector).val() == $(selector).attr("placeholder")) ? "" : $(selector).val();

        var maxLength = $(selector).attr("maxlength");
        if($.mdf.getByteLength(value) > maxLength) {
            alert('<spring:message code="common.alert.exceed.max.length" />' + ' Max: ' + maxLength);  // 입력한 값이 최대 길이를 초과했습니다.
            $(selector).focus();
            return false;
        }
        return true;
    }

    function reload() {
        var ed = tinyMCE.get('template');

        if(ed == null) {
            setTimeout(reload, 1000);
        } else {
            ed.setProgressState(1); // Show progress
            window.setTimeout(function() {
                for(var i=0; i < templateArr.length; i++) {
                    if(i==0) {
                        ed.setContent(templateArr[i].template);
                    } else {
                        if('POPUP'==templateArr[i].seg) {
                            $("#template_pop").val(templateArr[i].template);
                        }
                    }
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
            tinyMCE.get(ed.editorId).setContent(templateArr[0].template);
        }
        window.setTimeout(parent.loadSet, 1000);
    }

    function addChar(value) {
        tinyMCE.execCommand('mceInsertContent', false, value);
    }

    function getByte(editId) {
        var template = "";
        if(editId == "template_pop") {
            template = $("#"+editId).val();
        } else {
            template = tinyMCE.get(editId).getContent({ format: "text"});
        }


        if('template'==editId) {
        	getByteAndLimitByte("chkByte", template, 2000);
        } else {
        	getByteAndLimitByte("chkBytePushPop", template, 2000);
        }
    }


    function updatePush(editId) {
        if("title"==editId) {
            $("#previewTitle").html($(parent.document).find("#campaignPreface").val());
        } else if("template_pop"==editId) {
            $("#previewPushPopup").html($("#"+editId).val().replace(/\n/g,"<br/>"));
        } else {
            $("#previewMessage").html(tinyMCE.get(editId).getContent());
        }
    }

    function updatePushBigImage() {
        var imgSrc = $("#pushBigImgUrl").val();
        if(null!=imgSrc && ''!=imgSrc && 'undefind'!=typeof(imgSrc)) {
            $("#previewBigImage").html("<img src='"+$("#pushBigImgUrl").val()+"' style='width:100%;' />");
        } else {
            $("#previewBigImage").empty();
        }
    }

    function toggleHandler() {
        $("#handlerDiv").toggle();
    }
</script>
</head>
<body>

<form id="editorFrm" name="editorFrm" method="post">
<input type="hidden" name='no' value="<%=campaignNo%>" />
<input type="hidden" name='channel' value="S" />
<input type="hidden" name='segmentNo' value="<%=segmentNo%>" />
<input type="hidden" name='handlerType' value="<%=handlerType%>" />
<input type="hidden" name='seg' value=" "/>
<input type="hidden" name='templateType' value="3" />
<input type="hidden" name="retryCnt" />
<input type="hidden" name="campaignVo.handlerSeq" id="handlerSeq" value="0" />
<input type="hidden" name="templateContent" />
<input type="hidden" name="campaignPreface" />
<input type="hidden" name="pushPopImgUse" value = "N" />
<input type="hidden" name="pushBigImgUse" value = "N" />
<input type="hidden" name="svcType" value = "C" />

<div class="row">
    <div class="col-3 col-phone"><!-- mobile preview -->
        <div class="phone_preview" >
            <div class="preview_box">
                <div id="previewTitle" title="제목" style="width:212px; font-weight: bold;"></div>
                <div id="previewPushPopup" title="상태창 메시지" style="width:212px; margin-top:5px;height: 150px;"></div>
                <div id="previewBigImage" title="상태창 이미지" style="width:212px; margin-top:10px; max-height:150px;"></div>
            </div>
        </div>
    </div><!-- //mobile preview -->

    <div class="col-9">
        <ul class="nav nav-tabs tab01"><!-- tab -->
            <li class="nav-item"><a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true"><spring:message code="editor.template"/></a></li><!-- 템플릿 -->
            <li class="nav-item"><a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false"><spring:message code="editor.handler"/></a></li><!-- 핸들러 -->
        </ul>

        <div class="tab-content">
            <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 템플릿 tab -->
                <div class="table-responsive gridWrap overflow-x-hidden">
                    <table class="table table-sm dataTable table-fixed" style="margin-top: -1px !important;">
                        <colgroup>
                            <col width="130" />
                            <col width="*" />
                            <col width="110" />
                            <col width="*" />
                            <col width="75" />
                            <col width="*" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row" class="align-text-top"><em class="required"></em><spring:message code="common.menu.header.push.body" /></th><!-- 알림 내용 -->
                            <td colspan="5">
                                <div class="form-row align-items-center">
                                    <div class="col-6">
                                         <select class="form-control form-control-sm" name="optionItem" id="optionItem" onchange="selectSemantic(this);"></select>
                                    </div>
                                    <input type="text" id="chkBytePushPop" size="25" readonly="readonly" class="chk-byte"/>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-12">
                                        <textarea id="template_pop" name="template_pop" class="form-control"></textarea>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row"><em class="required"></em><spring:message code="common.menu.header.push.app" /></th><!-- 앱 선택 -->
                            <td>
                                <select name="pushAppId" id="pushAppId" class="form-control form-control-sm"></select>
                            </td>
                            <th scope="row"><em class="required"></em><spring:message code="common.menu.header.push.message.type" /></th><!-- 메시지 유형 -->
                            <td>
                                <select name="pushMsgInfo" class="form-control form-control-sm">
                                <c:forEach var="msgtype" items="${pushMsgInfoList}">
                                    <option value="${msgtype.CD}" <c:if test="${msgtype.CD eq pushInfoMap.PUSH_MSG_TYPE}">selected</c:if>>${msgtype.CD_DESC}</option>
                                </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><em class="required"></em><spring:message code="common.menu.header.push.menu.id" /></th><!-- 메뉴 ID -->
                            <td><input type="text" class="form-control form-control-sm" name="pushMenuId" id="pushMenuId" maxlength="6" value="${pushInfoMap.PUSH_MENU_ID}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="common.menu.header.push.image.popup.url"/></th><!-- 이미지 팝업 URL -->
                            <td colspan="5"><input type="text" class="form-control form-control-sm" name="pushImgUrl" id="pushImgUrl" maxlength="500" value="${pushInfoMap.PUSH_IMG_URL}" placeholder="http://www.mnwise.com/images/logo.png/"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="common.menu.header.push.large.image.url"/></th><!-- 큰 이미지 URL -->
                            <td colspan="5"><input type="text" class="form-control form-control-sm" name="pushBigImgUrl" id="pushBigImgUrl" maxlength="500" id="pushBigImgUrl" value="${pushInfoMap.PUSH_BIG_IMG_URL}" placeholder="http://www.mnwise.com/images/logo.png"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><spring:message code="common.menu.header.push.large.web.url" /></th><!-- 웹 URL -->
                            <td colspan="5"><input type="text" class="form-control form-control-sm" name="pushWebUrl" id="pushWebUrl" maxlength="500" value="${pushInfoMap.PUSH_CLICK_LINK}" placeholder="http://www.mnwise.com/index.php"/></td>
                        </tr>
                        <tr>
                            <!--th scope="row" class="align-text-top"><spring:message code="common.menu.inappmsg" /></th--><!-- 앱 내 메시지 -->
                            <td colspan="6" class="pl-0 pr-2">
                                <input type="text" id="chkByte" size="25" readonly="readonly" class="chk-byte"/>
                                <textarea id="template" name="template" style="width: 100%; height: 180px; background:transparent;" class="mceEditor"></textarea>
                            </td>
                        </tr>
                        <tr id="htmlToggleTr" style="display:none;">
                            <td colspan="6">
                                <button type="button" id="htmlToggle" class="btn btn-outline-primary btn-sm btn_search">
                                    <spring:message code="button.change.mode.edit" /><!-- 편집모드 전환 -->
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
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
        </div>
    </div>
</div>
</form>
</body>
</html>
