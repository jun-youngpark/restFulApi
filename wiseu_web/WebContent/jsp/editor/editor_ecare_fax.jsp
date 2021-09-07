<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>2단계] IFRAME내 저작기 화면 출력 (팩스)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : ecare_mail_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp"%>
<head>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<script src='/js/editor/jquery.insertAtCaret.js'></script>
<script type="text/javascript">
var templates = new Array(); //템플릿
    $(document).ready(function() {
        initEditorEventBind();
        initEditorPage();
    });

    function initEditorEventBind() {
        $("#editorSave").on("click", function(event) {  //적용버튼 클릭
            editorSaveHandler(false);  // common2step.js  유효성체크
        });
    }

    //적용 버튼
    function editorSave(allSave){
        var deferred = $.Deferred(); //전체 저장을 위한 함수
        var rules ={   // 유효성 체크
            'textmode' : {notBlank:true},
            'handler' : {notBlank:true}
        }
         var messages ={
            'handler' : $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.handler")),
            'textmode' : $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.text"))
        }
        if($.mdf.validForm('#editorFrm', rules, messages) === false){
            return deferred.reject($.i18n.prop("vaildation.check.fail"));  //유효성 체크 실패
        }
        $('#handlerSeq').val($('#handlerList').val()); //핸들러 번호
        var param = $.mdf.serializeObject('#editorFrm');
        param.templateVos= makeTemplate(ecareNo, $("#textmode").val(), " ");  //템플릿
        $.mdf.postJSON("/ecare/ecareEditor.json", JSON.stringify(param), function(result) {
            ajaxCallback(result, allSave, deferred);// common2step.js  전체적용일 경우 deferred 성공/실패 return
        });
        return deferred;
    }

    function initEditorPage() {
        // 서버정보(수신확인 등) 조회
        getEcareInfo();
        // 현재 도움말이 한국어 버전만 제작되어 다른 언어 버전은 숨김
        if('${lang}' !== '') {
            document.getElementById('helpBtn').style.display = 'none';
        }
        // 기본 핸들러 매니저 설정
        var message={
                initError : '<spring:message code="editor.alert.editor.1" arguments="EMAIL" />'
                ,noHandler : '<spring:message code="editor.option.handler.0" />'
                ,toggleDefault : '<spring:message code="editor.alert.handler.4" />'
                ,toggleEdit : '<spring:message code="editor.alert.handler.5" />'
                ,changeHandler : '<spring:message code="env.alert.handler.default"/>'
        };
        var option = {serviceGubun : 'EC' , serviceNo : '${ecareScenarioVo.ecareVo.ecareNo}' ,message : message};
        $.extend(true, DefaultHandlerManager, option);
    }
</script>
</head>
    <form name="editorFrm" id="editorFrm" method="post">
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
    <c:set var="mailType" value="${ecareScenarioVo.ecareVo.mailType}"/> <!-- 준실시간 -->
        <div class="card-body px-3">
            <div class="mt-3 justify-content-end">
                 <button class="btn btn-sm btn-outline-primary btn-round mr-2" id="editorSave">
                     <i class="fas fa-check"></i> <spring:message code="button.apply"/><!-- 적용 -->
                 </button>
             </div>
               <div class="col-12 px-0 mb-3">
                <ul class="nav nav-tabs tab01"><!-- tab -->
                    <li class="nav-item">
                        <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true">
                            <spring:message code="editor.text"/><!-- 본문 -->
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="handler-tab" data-toggle="tab" href="#handlerTab" role="tab" aria-controls="handler" aria-selected="false">
                            <spring:message code="editor.handler"/><!--  핸들러 -->
                        </a>
                    </li>
                </ul><!-- //tab -->

                <div class="tab-content"><!-- tab content -->
                    <div class="tab-pane active" id="templateTab" role="tabpanel" aria-labelledby="template-tab"><!-- 템플릿 tab -->
                        <div class="edit_area">
                            <textarea class="form-control" id="textmode" name="textmode" style="word-break:break-all" rows="15"></textarea>
                        </div>
                    </div><!-- //템플릿 tab -->

                    <div class="tab-pane" id="handlerTab" role="tabpanel" aria-labelledby="handler-tab"><!-- 핸들러 tab -->
                        <div class="edit_area mt-2">
                            <textarea class="form-control" id="handler" name="handler" style="word-break:break-all" rows="18"></textarea>
                        </div>
                        <div class="row mt-3 mb-1 align-items-center">
                            <div class="col-3 mx-3">
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
                </div><!-- //tab content -->
            </div><!-- //message -->
        </div>
    </form>