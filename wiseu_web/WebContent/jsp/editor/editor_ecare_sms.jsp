<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>2단계] IFRAME내 저작기 화면 출력 (SMS)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : ecare_sms_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<head>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script type="text/javascript">
        tinyMCE.init({  // TinyMCE
        mode : "textareas",
        language : "ko",
        theme : "advanced",
        plugins : "mnwise",
        forced_root_block : false,
        editor_selector : "mceEditor",
        convert_urls : 0, // 템플릿에 있는 url 값의 convert 를 사용할 지 여부(0:false, 1:true)
        // Theme options
        theme_advanced_buttons1 : "",
        theme_advanced_buttons2 : "",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "none",
        theme_advanced_resizing : false,
        content_css : "/css/tinymce.css",
        setup : function(ed) {  // tinyMCE 렌더링 완료 후 추가된 이벤트들
     /*        ed.onPaste.add(function (ed, e) {
                var content = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                content = content.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;");
                ed.execCommand('mceInsertContent', false, content);
            }); */
            ed.onKeyUp.add(function(ed, evt) {
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"} ), 90);
            });
            ed.onLoadContent.add(function(ed, evt) {
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"}), 90);
            });
            ed.onSetContent.add(function(ed, evt) {
                getByteAndLimitByte("chkByte", tinyMCE.get("template").getContent({ format: "text"} ), 90);
            });
        }
    }); // /TinyMCE
    $(document).ready(function() {
        initEditorEventBind();
        initEditorPage();
        addEditorValidRule();
    });

    function initEditorEventBind() {
        $("#editorSave").on("click", function(event) {  //적용버튼 클릭
            editorSaveHandler(false);  // common2step.js  유효성체크
        });
    }
    function addEditorValidRule() {
        $("#editorFrm").validate({});
        $("#editorFrm #tinymce").rules("add", {notBlank: true});
    }

    function editorSave(allSave){
        var deferred = $.Deferred(); //전체 저장을 위한 함수
        var template= tinyMCE.get("template").getContent({ format: "text"} )
        if($.mdf.isBlank(template)){
        	alert($.i18n.prop("vaildate.required.param",$.i18n.prop("editor.template")));
            return deferred.reject("noAlram");
        }
        var rules ={   // 유효성 체크
                'handler' : {notBlank:true}
        }
        var messages ={ 'handler' : $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.handler")) }
        if($.mdf.validForm('#editorFrm', rules, messages) === false){
               return deferred.reject($.i18n.prop("vaildation.check.fail"));  //유효성 체크 실패
        }
        $('#handlerSeq').val($('#handlerList').val()); //핸들러 번호
        var param = $.mdf.serializeObject('#editorFrm');
        param.templateVos= makeTemplate(ecareNo, template, " ");    //템플릿
        $.mdf.postJSON("/ecare/ecareEditor.json", JSON.stringify(param), function(result) {
             ajaxCallback(result, allSave, deferred); // common2step.js  전체적용일 경우 deferred 성공/실패 return
        });
        return deferred;
    }

    function initEditorPage() {
        getContentsList();
        // 단건 발송일 경우는 핸들러를 사용하지 않기 때문에 getEcareInfo function을 호출하지 않음
        var sendOne = "${sendOne}"; // 단건발송여부 (O:단건)
        if(sendOne != "O") {
            getEcareInfo();
        } else {
            $("#handler-tab").hide();
        }
        // 기본 핸들러 매니저 설정
        var message = {
            initError : '<spring:message code="editor.alert.editor.1" arguments="SMS" />'
          , noHandler : '<spring:message code="editor.option.handler.0" />'
          , toggleDefault : '<spring:message code="editor.alert.handler.4" />'
          , toggleEdit : '<spring:message code="editor.alert.handler.5" />'
          , changeHandler : '<spring:message code="env.alert.handler.default"/>'
        };
        var option = {serviceGubun : 'EC' , serviceNo : ecareNo ,message : message};
        $.extend(true, DefaultHandlerManager, option);
    }

    function getContentsList(){
         var param = $.mdf.serializeObject('#smsContentsFrm');
         $.post("/editor/smsContsList.do", $.param(param, true), function(result) {
              $("#tmplWrap").html(result);
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
                    <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true"><spring:message code="editor.template"/></a><!--  템플릿 -->
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false"><spring:message code="editor.handler"/></a><!-- 핸들러 -->
                </li>
            </ul>
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

<form name="smsContentsFrm" id="smsContentsFrm" action="/editor/smsContsList.do" method="post" ><!-- /editor/sms_contents_list.do -->
    <input type="hidden" name="contsTxt">
    <input type="hidden" name="countPerPage" value="6">
</form>