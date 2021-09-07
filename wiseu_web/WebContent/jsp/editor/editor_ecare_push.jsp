<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>2단계] IFRAME내 저작기 화면 출력 (PUSH)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : ecare_push_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<head>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src="/plugin/form/jquery.placeholder-1.8.7.js"></script>
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
        cleanup : 0, // 이것을 사용하게 되면 템플릿이 표준에 맞게 변하진 않으나 tinyMCE 속성값이 붙음(0:false, 1:true)
        fullpage_default_title : "", // title 태그 초기값
        theme_advanced_buttons1 : "htmlToggle,|,mysplitbutton,bold,italic,underline,strikethrough,|,cut,copy,paste,pastetext,pasteword,|,justifyleft,justifycenter,justifyright,justifyfull,fontselect,fontsizeselect,bullist,numlist,forecolor,backcolor,print",
        theme_advanced_buttons2 : "",
        theme_advanced_buttons3 : "",
        theme_advanced_buttons4 : "",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "none",
        theme_advanced_resizing : false,
        tab_focus : ":prev,:next",
        content_css : "/css/tinymce.css",
        setup : function(ed) { // tinyMCE 렌더링 완료 후 추가된 이벤트
            ed.onClick.add(function(ed, e) {
                isTitleFocus = false;
            });
            ed.onKeyUp.add(function(ed, evt) { // tinyMCE 렌더링 완료 후 추가된 이벤트
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text", no_events: true }), 2000);
            });
            ed.onLoadContent.add(function(ed, evt) { // tinyMCE 렌더링 완료 후 추가된 이벤트
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text", no_events: true}), 2000);
                // template 정보를 가져온 후가 이쪽 이벤트라서 초기화를 여기서 한다.
                updatePush("title");
                updatePush("template");
                updatePush("template_pop");
                updatePushBigImage();
            });
        }
    }); // /TinyMCE

    var isTitleFocus = false;
    $(document).ready(function() {
        initEditorEventBind();
        initEditorPage();
    });

    function initEditorEventBind() {
        $("#ecarePreface").keyup(function() {
            updatePush("title");
        });
        $("#template_pop").keyup(function() {
            updatePush("template_pop");
            getByteAndLimitByte("chkBytePushPop", $("#template_pop").val(), 2000, "template_pop");  //알림내용
        });
        $("#pushBigImgUrl").change(function() {
            updatePushBigImage();
        });
        $("#htmlToggle").on("click", function(event) {
            tinyMCE.execCommand('mceToggleEditor',false,'template');
            $("#htmlToggleTr").css("display","none");
        });
        $("#editorSave").on("click", function(event) {  //적용버튼 클릭
            editorSaveHandler(false);  // common2step.js  유효성체크
        });
    }

    //적용 버튼
    function editorSave(allSave){
         var deferred = $.Deferred(); //전체 저장을 위한 함수
         var rules ={   // 유효성 체크
             'template_pop' : { notBlank : true , maxlength: 2000 },
             'pushMsgInfo' : { notBlank : true },
             'pushMenuId' : { notBlank : true, maxlength : 6, alphaDigit : true },
             'pushWebUrl' : { maxlength : 500, url : true },
             'ecarePreface' : { notBlank : true, maxlength: 80 },
             'handler' : {notBlank:true}
         };
         var messages ={ 'handler' : $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.handler")) }
         if($.mdf.validForm('#editorFrm', rules, messages) === false){
             return deferred.reject($.i18n.prop("vaildation.check.fail"));  //유효성 체크 실패
         }
         $('#handlerSeq').val($('#handlerList').val()); //핸들러 번호
         var param = $.mdf.serializeObject('#editorFrm');
         var templates = new Array(); //템플릿
         var template = tinyMCE.get("template").getContent();
         makeTemplate(ecareNo, template, " ", templates)
         makeTemplate(ecareNo, $('#template_pop').val(), "POPUP", templates);
         param.templateVos=  templates;    //템플릿
         param.pushData=  makePushVo();    //PUSH 기본정보
         $.mdf.postJSON("/ecare/ecareEditor.json", JSON.stringify(param), function(result) {
             ajaxCallback(result, allSave, deferred);// common2step.js  전체적용일 경우 deferred 성공/실패 return
         });
         return deferred;
    }

    function initEditorPage() {
        // 단건 발송일 경우는 핸들러를 사용하지 않기 때문에 getEcareInfo function을 호출하지 않음
        var sendOne = "${sendOne}"; // 단건발송여부 (O:단건)
        if(sendOne != "O") {
            getEcareInfo();
        } else {
            $("#handler-tab").hide();
        }
        // 기본 핸들러 매니저 설정
        var message = {
            initError : '<spring:message code="editor.alert.editor.1" arguments="MMS" />'
          , noHandler : '<spring:message code="editor.option.handler.0" />'
          , toggleDefault : '<spring:message code="editor.alert.handler.4" />'
          , toggleEdit : '<spring:message code="editor.alert.handler.5" />'
          , changeHandler : '<spring:message code="env.alert.handler.default"/>'
        };
        var option = {serviceGubun : 'EC' , serviceNo : ecareNo ,message : message};
        $.extend(true, DefaultHandlerManager, option);
        showPushAppList();
    }

    function showPushAppList() {
        //PushService.selectPushAppList("Y", { callback: showPushAppCallback });
        //getAjaxCall("/env/Y/env_push_setting.do" , showPushAppCallback);
        $.post("/env/selectPushAppList.json?useOnly=Y", null, function(result) {
            if(result == null) {
            	alert("<spring:message code='campaign.alert.msg.init.1' />");
                $("#pushAppId").append("<option value=''><spring:message code='common.option.no.registered.app' /></option>");
                if(top) top.location.href="/ecare/ecareList.do";  // /ecare/ecare.do
                else location.href="/ecare/ecareList.do";  // /ecare/ecare.do
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

    function updatePush(editId) {
        if("title"==editId) {
            $("#previewTitle").html($(parent.document).find("#ecarePreface").val());
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

    function makePushVo(){
        return JSON.stringify({
            pushWebUrl: $("#pushWebUrl").val(),  //웹 url
            pushMenuId: $("#pushMenuId").val(), //메뉴 ID
            pushMsgInfo: $("#pushMsgInfo").val(),//메세지유형
            pushAppId: $("#pushAppId").val(),//앱 ID
            svcType: "E",//서비스타입 E:이케어
            pushBigImgUrl: $("#pushBigImgUrl").val(),    //큰이미지 url 사용x
            pushImgUrl: $("#pushImgUrl").val(), //이미지 url 사용x
            pushPopImgUse: "N",
            pushBigImgUse: "N"
        });
    }
</script>
</head>
<form id="editorFrm" name="editorFrm" method="post">
    <input type="hidden" name="no" value="${ecareScenarioVo.ecareVo.ecareNo}" />
    <input type="hidden" name="serviceType" value="${ecareScenarioVo.ecareVo.serviceType}" />
    <input type="hidden" name="subType" value="${ecareScenarioVo.ecareVo.subType}" />
    <input type="hidden" name="mailType" value="${ecareScenarioVo.ecareVo.mailType}" />
    <input type="hidden" name="userId" value="${sessionScope.adminSessionVo.userVo.userId}">
    <input type="hidden" name="templateType" value="1" />
    <input type="hidden" name="historyMsg" value="" />
    <input type="hidden" name="tagType" value="ecare_default" />
    <input type="hidden" name="handlerType" value="G" />
    <input type="hidden" name="channelType" value="${ecareScenarioVo.ecareVo.channelType}" />
    <input type="hidden" name="handlerSeq" id="handlerSeq" value="0" />
    <input type="hidden" name="imsiTemplate" value="" />
    <input type="hidden" name="imsiChannelType" value="" />

<div class="card-body px-3">
    <div class="mt-3 justify-content-end">
          <button class="btn btn-sm btn-outline-primary btn-round mr-2" id="editorSave">
              <i class="fas fa-check"></i> <spring:message code="button.apply"/><!-- 적용 -->
          </button>
     </div>

    <c:if test="${serviceType eq 'schedule' or serviceType eq 'scheduleM'}">
    <div class="table-responsive gridWrap">
        <table class="table table-sm dataTable">
            <tr>
                <th scope="row" width="110"><em class="required">required</em><spring:message code="ecare.menu.subject" /></th>
                <td colspan="5"><input type="text" class="form-control form-control-sm" id="ecarePreface" name="ecarePreface" value="${ecareScenarioVo.ecareVo.ecarePreface}" /></td>
            </tr>
        </table>
    </div>
    </c:if>

    <div class="row">
        <div class="col-3 col-phone"><!-- mobile preview -->
            <div class="phone_preview">
                <div class="preview_box">
                    <div id="previewTitle" title="제목" style="width:212px; font-weight: bold;"></div>
                    <div id="previewPushPopup" title="상태창 메시지" style="width:212px; margin-top:5px;height: 150px;"></div>
                    <div id="previewBigImage" title="상태창 이미지" style="width:212px; margin-top:10px; max-height:150px;"></div>
                </div>
            </div>
        </div><!-- //mobile preview -->

        <div class="col-9"><!-- message list -->
            <ul class="nav nav-tabs tab01"><!-- tab -->
                <li class="nav-item">
                    <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true"><spring:message code="editor.template"/></a><!--  템플릿 -->
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false"><spring:message code="editor.handler"/></a><!-- 핸들러 -->
                </li>
            </ul>
            <div class="tab-content"><!-- tab content -->
                <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 템플릿 tab -->
                    <div class="table-responsive gridWrap overflow-x-hidden">
                        <table class="table table-sm dataTable table-fixed">
                            <colgroup>
                                <col width="112" />
                                <col width="*" />
                                <col width="12%" />
                                <col width="20%" />
                                <col width="9%" />
                                <col width="20%" />
                            </colgroup>
                            <tbody>
                            <tr>
                                <th scope="row" class="align-text-top">
                                    <em class="required">required</em><spring:message code="common.menu.header.push.body" /><!-- 알림 내용 -->
                                </th>
                                <td colspan="5">
                                    <div class="form-row align-items-center">
                                        <span class="letter_count" style="text-align: left;">
                                            <input type="text" id="chkBytePushPop" size="25" class="chk-byte" style="text-align: left;"/>
                                        </span>
                                    </div>
                                    <div class="row mt-2">
                                        <div class="col-12">
                                            <textarea id="template_pop" name="template_pop" class="form-control"></textarea>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row"><em class="required">required</em><spring:message code="common.menu.header.push.app" /></th><!-- 앱 선택 -->
                                <td>
                                    <select  id="pushAppId" class="form-control form-control-sm"></select>
                                </td>
                                <th scope="row"><em class="required">required</em><spring:message code="common.menu.header.push.message.type" /></th><!-- 메시지 유형 -->
                                <td>
                                    <select id="pushMsgInfo" class="form-control form-control-sm">
                                        <c:forEach var="msgtype" items="${pushMsgInfoList}">
                                            <option value="${msgtype.CD}" <c:if test="${msgtype.CD eq pushInfoMap.PUSH_MSG_TYPE}">selected</c:if>>${msgtype.CD_DESC}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <th scope="row"><em class="required">required</em><spring:message code="common.menu.header.push.menu.id" /></th><!-- 메뉴 ID -->
                                <td><input type="text" class="form-control form-control-sm" id="pushMenuId" name="pushMenuId" maxlength="6" value="${pushInfoMap.PUSH_MENU_ID}"/></td>
                            </tr>
                            <tr style="display: none;">
                                <th scope="row"><spring:message code="common.menu.header.push.image.popup.url"/></th>
                                <td colspan="5">
                                    <input type="text" class="form-control form-control-sm"  id="pushImgUrl" maxlength="500" value="${pushInfoMap.PUSH_IMG_URL}" placeholder="http://www.mnwise.com/images/logo.png/"/>
                                </td>
                            </tr>
                            <tr style="display: none;">
                                <th scope="row"><spring:message code="common.menu.header.push.large.image.url"/></th>
                                <td colspan="5">
                                    <input type="text" class="form-control form-control-sm"  id="pushBigImgUrl" maxlength="500"  value="${pushInfoMap.PUSH_BIG_IMG_URL}" placeholder="http://www.mnwise.com/images/logo.png"/>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row"><spring:message code="common.menu.header.push.large.web.url" /></th>
                                <td colspan="5">
                                    <input type="text" class="form-control form-control-sm" id="pushWebUrl" name="pushWebUrl" maxlength="500" value="${pushInfoMap.PUSH_CLICK_LINK}" placeholder="http://www.mnwise.com/index.php"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" class="pl-0 pr-2">
                                    <span class="letter_count" >
                                        <input type="text" id="chkByte" size="25" class="chk-byte" style="text-align: left;"/>
                                    </span>
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
                    </div><!-- //edit area - option -->
                </div><!-- //핸들러 tab -->
            </div><!-- //tab content -->
        </div><!-- //message list -->
    </div>
</div>
</form>
