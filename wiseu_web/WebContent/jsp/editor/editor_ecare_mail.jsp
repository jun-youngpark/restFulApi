<%-------------------------------------------------------------------------------------------------
 * - [이케어>이케어 등록>2단계] IFRAME내 저작기 화면 출력 (메일)
 * - URL : /editor/editor.do
 * - Controller : com.mnwise.wiseu.web.editor.web.EditorForwardController
 * - 이전 파일명 : ecare_mail_editor.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/include/taglibs.jsp"%>
<head>
<%@ include file="/jsp/include/pluginEditor.jsp" %>
<%@ include file="/jsp/include/mailEditorClone.jsp" %>
<script src="/js/editor/multipartfile.js"></script>
<script src='/js/editor/jquery.insertAtCaret.js'></script>
<script src="/js/editor/linktrace.js" type="text/javascript"></script>
<script type="text/javascript">

    var initAtFileSize;

    $(document).ready(function() {
        initEditorEventBind();
        initEditorPage();
    });

    function initEditorEventBind() {
        $("#template-tab").on("click", function(event) {
            toggleEditor('template');
        });
        $("#attach-tab").on("click", function(event) {
            toggleEditor('attach');
        });
        $("#handler-tab").on("click", function(event) {
            toggleEditor('handler');
        });
        $("#extractBtn").on("click", function(event) {
            extractSample();
        });
        $("#jsonPreviewBtn").on("click", function(event) {
            previewSample();
        });
        $("#helpBtn").on("click", function(event) {
            changeHelp();
        });
        //첨부파일 버튼 클릭 이벤트
        /* $('[id*="AttFileBtn"]').on("click", function(event) {
            addAttachFile($(this).attr('value'));
        }); */
        $('[id="attachFileSelect"]').on("change", function(event) {
            addAttachFile($(this).val());
            $('#attachFileSelect > option:eq(0)').prop('selected', true);
        });
        //HTMLPDF 암호화 여부 클릭 이벤트
        $('#encYn_mailType').on("change", function(event) {
            if($(this).prop("checked")){
                $("#secuField_"+mailType).attr("disabled", false);
            }else{
                $("#secuField_"+mailType).val("");
                $("#secuField_"+mailType).attr("disabled", true);
            }
        });

        $("#attachFileTable").on("click", ".deleteRow", function (event) {
            if(confirm($.i18n.prop("editor.alert.multipartfile.1")) == true) {
                 $(this).closest("tr").remove();
            }
         });

        $("#editorSave").on("click", function(event) {  //적용버튼 클릭
            editorSaveHandler(false);  // common2step.js  유효성체크
        });
    }

    function editorSave(allSave){
         var deferred = $.Deferred(); //전체 저장을 위한 함수
         addEditorValidRule();
         if($.mdf.validForm('#editorFrm') === false){
                  // 첨부파일명 미입력시 첨부파일 탭 이동
                  if(mailType != "NONE" && ($("#fileName_"+mailType).prop('class').indexOf('error')!== -1
                           || $("#secuField_"+mailType).prop('class').indexOf('error') !== -1
                           || $("#attachmode").prop('class').indexOf('error') !== -1)){
                       $('#attach-tab').click();
                  }
                  return deferred.reject($.i18n.prop("vaildation.check.fail"));  //유효성 체크 실패
         }

           $('#handlerSeq').val($('#handlerList').val()); //핸들러 번호
           var param = $.mdf.serializeObject('#editorFrm');
           var templates = new Array();
           var multipartFiles  = new Array();
           if(mailType != "NONE") { // 보안메일
        	   var attach= $("#editorFrm #attachmode").val();
               makeTemplate(ecareNo, attach, mailType, templates) //첨부 템플릿
               makeMultipart("#attachTemplate", multipartFiles); //첨부 템플릿 암호화 여부,비밀번호 필드
           }

          makeMultipart("#attachFileTable tr#attach", multipartFiles);
          makeTemplate(ecareNo, $('#textmode').val(), " ", templates)
          param.templateVos= templates;    //템플릿
          param.multipartFileVos= multipartFiles; //첨부파일 배열
          $.mdf.postJSON("/ecare/ecareEditor.json", JSON.stringify(param), function(result) {
              ajaxCallback(result, allSave, deferred);// common2step.js  전체적용일 경우 deferred 성공/실패 return
          });
          return deferred;
    }

    function addEditorValidRule() {
        $("#editorFrm").validate({
            ignore : [],
          //  focusInvalid: false,
            invalidHandler: function(form, validator) {
                if (!validator.numberOfInvalids()){
                    return;
                }
                $('html, body').animate({
                    scrollTop: ($(validator.errorList[0].element).offset().top -300)
                }, 2000);
            }
        });
        $("#editorFrm input[name*='fileName']").each(function() {
            $(this).rules("add", {notBlank: true, maxlength :30});
        });
        $("#editorFrm input[name*='filePath']").each(function() {
            $(this).rules("add", {notBlank: true, maxlength :100});
        });
        $("#editorFrm input[name*='secuField']").each(function() {
            $(this).rules("add", {notBlank: true, maxlength :20});
        });
        $("#editorFrm input[name='ecarePreface']").rules("add", {notBlank: true, maxlength:50});
        $("#editorFrm textarea[name='textmode']").rules("add", {
            notBlank: true ,
            messages: {
                notBlank: $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.text"))
            }
        });
        $("#editorFrm textarea[name='handler']").rules("add", {
            notBlank: true ,
            messages: {
                notBlank: $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.handler"))
            }
        });
        if(mailType != "NONE") { // 보안메일,PDF
            $("#editorFrm textarea[name='attachmode']").rules("add",{
           			notBlank: true ,
           			messages: {
           				notBlank: $.i18n.prop("vaildate.required.param",$.i18n.prop("editor.menu.attachfile"))
         			}
          	    });
        }
    }

    function initEditorPage() {
        // 서버정보(수신확인 등) 조회
        getEcareInfo();
        toggleEditor('template');
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
        var option = {serviceGubun : 'EC' , serviceNo : ecareNo ,message : message};
        $.extend(true, DefaultHandlerManager, option);
        if($("#encYn_mailType").prop("checked") === false){
            $("#secuField_"+mailType).attr("disabled", true);
        }else{
            $("#secuField_"+mailType).attr("disabled", false);
        }
        initAtFileSize = $("#attachFileTable tr:visible").length;
    }

    function uploadAttachFile(fileName, filePath){
        $('#attachFileTable tbody').append(makeUploadAttach(fileName, filePath));
    }

    function addAttachFile(type){
        if($("#attachFileTable tr:visible").length >2){
        	alert($.i18n.prop("editor.alert.maximun.attach.file"));
            return false;
        }
        initAtFileSize++;
        if(type === 'upload'){
            $.mdf.popupGet('/editor/importPopup.do?type=EC&uploadType=file&no=' + ecareNo, 'uploadPopup', 560, 260);
            return;
        }else if(type === 'path'){
            $trNew = $("#attachPathClone").clone();
        }else if(type === 'smailatt'){
            $trNew = $("#attachSmailattClone").clone();
        }else{
            $trNew = $("#encPdfClone").clone();
        }
        $trNew.removeClass('dp-none')
        $trNew.attr('id',"attach")
        $trNew.find('input').each(function() {
            this.name= this.name.replace('[0]', '['+initAtFileSize+']');
        });
        $('#attachFileTable tbody').append($trNew);
    }

    // JSON 변수 정보를 담고 있는 객체
    function JsonObj(name) {
        this.name = name;
        this.values = [];
        this.isSame = function(val) {
            return this.name === val ? true : false;
        }
        this.push = function(val) {
            this.values.push(val);
        }
    }

    // 템플릿에서 전물을 추출해서 JSON 형태 문자열로 생성
    function extractSample() {
        var template = document.getElementById('textmode').value;
        var regexArr = template.match(/[#]{.+}/g);
        if(regexArr === null) {
            return;
        }

        var len = regexArr.length;
        var jsonSrc = '';

        if(len > 0) {
            // 추출한 변수 목록에서 중복을 제거한다.
            var uniqueArr = [];
            for(var i = 0; i < len; i++) {
                var isNew = true;

                var nameVar = regexArr[i].replace('#{', '').replace('}', '');
                for(var j = 0; j < uniqueArr.length; j++) {
                    if(nameVar === uniqueArr[j]) {
                        isNew = false;
                        break;
                    }
                }

                if(isNew) {
                    uniqueArr.push(nameVar);
                }
            }

            var primitiveArray = [];
            var objectArray = [];
            var arrayArray = [];

            var arrayRegex = RegExp('.+\\[.+\\][.]*.*');
            var objectRegex = RegExp('.+[^\\[\\]]+[.].*');

            var jsonObj;
            for(var i = 0; i < uniqueArr.length; i++) {
                var data = uniqueArr[i];

                if(arrayRegex.test(data)) {
                    var arr = data.split('.');

                    isNew = true;
                    var name = arr[0].replace(/[\\[].+/g, '');

                    for(var j = 0; j < arrayArray.length; j++) {
                        if(arrayArray[j].isSame(name)) {
                            if(arr.length > 1) {
                                arrayArray[j].push(arr[1]);
                            }
                            isNew = false;
                            break;
                        }
                    }

                    if(isNew) {
                        jsonObj = new JsonObj(name);
                        if(arr.length > 1) {
                            jsonObj.push(arr[1]);
                        }
                        arrayArray.push(jsonObj);
                    }

                    continue;
                }

                if(objectRegex.test(data)) {
                    var arr = data.split('.');

                    isNew = true;
                    var name = arr[0];
                    for(var j = 0; j < objectArray.length; j++) {
                        if(objectArray[j].isSame(name)) {
                            if(arr.length > 1) {
                                objectArray[j].push(arr[1]);
                            }
                            isNew = false;
                            break;
                        }
                    }

                    if(isNew) {
                        jsonObj = new JsonObj(name);
                        if(arr.length > 1) {
                            jsonObj.push(arr[1]);
                        }
                        objectArray.push(jsonObj);
                    }

                    continue;
                }

                primitiveArray.push(data);
            }

            jsonSrc += '{\r\n    ';
            var len2 = primitiveArray.length;
            if(len2 > 0) {
                for(var i = 0; i < len2; i++) {
                    jsonSrc += '\"';
                    jsonSrc += primitiveArray[i]
                    jsonSrc += '\" : \"#{'
                    jsonSrc += primitiveArray[i]
                    jsonSrc += '}\", '
                }
            }

            len2 = objectArray.length;
            if(len2 > 0) {
                for(var i = 0; i < len2; i++) {
                    var jsonObj = objectArray[i];
                    jsonSrc += '\r\n    ';
                    jsonSrc += '\"';
                    jsonSrc += jsonObj.name;
                    jsonSrc += '\" : { ';

                    var values = jsonObj.values;
                    if(values.length == 0) {
                        jsonSrc += ' }, ';
                    } else {
                        for(var j = 0; j < values.length; j++) {
                            jsonSrc += '\"'
                            jsonSrc += values[j];
                            jsonSrc += '\" : \"#{'
                            jsonSrc += values[j];
                            jsonSrc += '}\", '
                        }
                        jsonSrc = jsonSrc.replace(/,\s$/, '');
                        jsonSrc += ' }, ';
                    }
                }
            }

            len2 = arrayArray.length;
            if(len2 > 0) {
                for(var i = 0; i < len2; i++) {
                    var jsonObj = arrayArray[i];
                    jsonSrc += '\r\n    ';
                    jsonSrc += '\"';
                    jsonSrc += jsonObj.name;
                    jsonSrc += '\" : [ { ';

                    var values = jsonObj.values;
                    if(values.length == 0) {
                        jsonSrc += ' }, ';
                    } else {
                        for(var j = 0; j < values.length; j++) {
                            jsonSrc += '\"'
                            jsonSrc += values[j];
                            jsonSrc += '\" : \"#{'
                            jsonSrc += values[j];
                            jsonSrc += '}\", '
                        }
                        jsonSrc = jsonSrc.replace(/,\s$/, '');
                        jsonSrc += ' } ], ';
                    }
                }
            }
            jsonSrc = jsonSrc.replace(/,\s$/, '');
            jsonSrc += '\r\n}';
            document.getElementById('jsonSource').value = jsonSrc;
        }
    }

    // 샘플 전문으로 템플릿 미리보기를 실행
    function previewSample() {
        var form = document.editorFrm;
        form.action = '/common/previewConts.do?cmd=preview&previewType=T';  // /common/previewMime.do
        form.target = 'preview';
        form.method = 'POST';

        var json;
        var data = document.getElementsByName('data');
        var len = data.length;
        if(len > 0) {
            json = data[0];
        } else {
            json = document.createElement("input");
            json.type = 'hidden';
            json.name = 'data';
            form.appendChild(json);
        }
        json.value = document.getElementById('jsonSource').value;
        window.open('', 'preview', 'scrollbars=yes,toolbar=no,width=825,height=600,resizable=yes,menubar=no');
        form.submit();
    }

    var helpStatus = 'hide';

    // 도움말과 도움말 버튼 상태 변경
    function changeHelp() {
        if(helpStatus === 'hide') {
            helpStatus = 'show';
            $("#helpDetail").show();
        } else {
            helpStatus = 'hide';
            $("#helpDetail").hide();
        }
    }

    // 에디터/HTML 변환
    function toggleEditor(val) {
         if(val === 'attach') { //첨부파일 클릭
               document.editorFrm.tagType.value = 'ecare_attach';
               $("#textmode").hide();
               $("#attachmode").show();
               $(".attachMenu").show();
               $(".textMenu").hide();
               $("#jsonSample").hide();
         } else if(val === 'template') {
               document.editorFrm.tagType.value = 'ecare_textmode';
               $("#attachmode").hide();
               $("#textmode").show();
               $(".textMenu").show();
               $(".attachMenu").hide();
               $("#jsonSample").hide();
         }else if(val === 'handler') {
            document.editorFrm.tagType.value = 'ecare_handler';
            $(".attachMenu").hide();
            $("#jsonSample").hide();
        } else if(val === 'jonmun') {
            if($("#jsonSample").css("display") == "none") {
                $("#jsonSample").css("display", "inline");
            } else {
                $("#jsonSample").css("display", "none");
                $("#helpDetail").hide();
            }
        }
    }

    //첨부파일 업로드
    function makeUploadAttach(fileName, filePath){
        var elementStr = '';
        elementStr += '<tr id="attach" class="upload">'
        elementStr += '<td class="text-center"><button class="btn btn-sm btn-outline-danger deleteRow" type="button"><i class="fas fa-minus"></i></button></td>'
        elementStr += '<th scope="row"><spring:message code="editor.attach.upload"/></th>'
        elementStr += '<td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.file.name"/></td>';
        elementStr += '<td><input type="text" class="form-control form-control-sm" readonly="readonly" name="fileName['+length+']"  value="'+fileName+'"  ></td>'
        elementStr += '<td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.file.path"/></td>'
        elementStr += '<td colspan="3"><input type="text"  class="form-control form-control-sm" readonly="readonly" name="filePath['+length+']"  value="'+filePath+'" ></td>'
        elementStr += '<input type="hidden" name="fileType['+initAtFileSize+']"  value="UPLOAD"/>'
        elementStr += '</tr>';
        return elementStr;
    }

 // 링크추적을 팝업창을 띄운다.
    var linktrace= null;
    function showLinktrace() {
        if(editAble == "false"){
        	alert("service is running");
            return;
        }
        linktrace = new Linktrace();
        $.mdf.popupGet("/editor/linkPopup.do?type=linktrace", "linkTracePopup", 1024, 620);
    }

    // 저작기에서 드레그한 부분을 수신거부를 설정한다.
    function insertReject() {
           if(editAble == "false"){
        	   alert("service is running");
               return;
           }
        var text = "";
        if(navigator.appName.indexOf("Microsoft") > -1) {
            text = document.selection.createRange().text;
        } else {
            var start = document.editorFrm.textmode.selectionStart;
            var end = document.editorFrm.textmode.selectionEnd;
            text = document.editorFrm.textmode.value.substring(start, end);
        }
        if(text.length == 0) {
        	alert('<spring:message code="editor.alert.editor.12"/>');
            return;
        }

        if(confirm('<spring:message code="editor.alert.editor.13"/>') == true) {
            insertAtCaret(document.getElementById("textmode"), text.link("<\%=(Reject)%>"));
        }
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
<input type="hidden" name="linkSeq" value="0" />
<c:set var="mailType" value="${ecareScenarioVo.ecareVo.mailType}"/> <!-- 메일유형-->

<div class="card-body px-3 pb-10">
    <div class="mt-3 justify-content-end">
        <button class="btn btn-sm btn-outline-primary btn-round mr-2" id="editorSave">
            <i class="fas fa-check"></i> <spring:message code="button.apply"/><!-- 적용 -->
        </button>
    </div>

    <div class="table-responsive gridWrap">
	    <c:if test="${mailType eq 'HTMLPDF'}">
	    <div class="col-12 alert alert-secondary mb-0 attachMenu" role="alert">
	          <i class="fas fa-circle fa-xs"></i><spring:message code="ecare.2step.help4" /><!--비밀번호 필드 입력 시 {변수} 형태를 넣어주세요. 예시) ${SEQ} (context 사용 시 : #{변수}) --><br/>
	          <i class="fas fa-circle fa-xs"></i><spring:message code="ecare.2step.help9" /><!-- 미리보기/테스트 발송의 경우 비밀번호 값이 security.properties파일의 [security.default.password]항목 값으로 고정됩니다.-->
	    </div>
	    </c:if>
        <table class="table table-sm dataTable">
            <c:if test="${serviceType eq 'schedule' or serviceType eq 'scheduleM'}">
                <tr>
                    <th scope="row" width="110"><em class="required">required</em><spring:message code="ecare.menu.subject" /></th>
                    <td colspan="5"><input type="text" class="form-control form-control-sm" name="ecarePreface" value="${ecareScenarioVo.ecareVo.ecarePreface}" /></td>
                </tr>
            </c:if>
            <c:if test="${mailType eq 'HTMLPDF' or mailType eq 'SMAIL'}">
                <c:forEach var="loop" items="${multipartFileList}" varStatus="status">
                    <c:if test="${mailType eq loop.fileType}">
                        <c:set var="attachFileName" value="${loop.fileName}" />
                        <c:set var="attachSecuField" value="${loop.secuField}" />
                        <c:set var="attachEncYn" value="${loop.encYn}" />
                    </c:if>
                </c:forEach>
                <tr id="attachTemplate" class="attachMenu">
                    <input type="hidden" name="fileType[${mailType}]" value="${mailType}"/>
                    <!-- 파일경로가 없으므로 타입 입력 -->
                    <input type="hidden" name="filePath[${mailType}]" value="${mailType}"/>
                    <th scope="row" width="110"><em class="required">required</em><spring:message code="editor.attach.name"/></th><!--  파일명-->
                    <td>
                        <input type="text" class="form-control form-control-sm" name="fileName[${mailType}]"  id="fileName_${mailType}"  value="${attachFileName}" >
                    </td>
                    <c:if test="${mailType eq 'HTMLPDF'}">
                    <th scope="row" width="110"><spring:message code="editor.attach.encyn"/></th><!--  암호화 여부-->
                        <td>
                            <div class="custom-toggle-wrap">
                                <label class="custom-toggle">
                                    <input type="checkbox" id="encYn_mailType" name="encYn[${mailType}]" <c:if test="${attachEncYn eq 'Y'}">checked="checked"</c:if>>
                                    <span class="custom-toggle-slider rounded-circle" data-label-off="N" data-label-on="Y"></span>
                                </label>
                            </div>
                        </td>
                    </c:if>
                    <th scope="row" width="110">
                        <c:if test="${mailType eq 'SMAIL'}">
                            <em class="required">required</em>
                        </c:if>
                        <spring:message code="editor.attach.secufield"/>
                    </th><!-- 비밀번호 필드 -->
                    <td><input type="text" class="form-control form-control-sm" name="secuField[${mailType}]" id="secuField_${mailType}" value="${attachSecuField}"/></td>
                 </tr>
            </c:if>
        </table>
    </div>

    <div class="col-12 px-0 mb-5">
        <ul class="nav nav-tabs tab01"><!-- tab -->
            <li class="nav-item">
                <a class="nav-link active" id="template-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="template" aria-selected="true">
                    <spring:message code="editor.text"/><!-- 본문 -->
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="attach-tab" data-toggle="tab" href="#templateTab" role="tab" aria-controls="attach" aria-selected="true" style="display:none; ">
                    <spring:message code="editor.menu.attachfile"/><!-- 첨부파일 -->
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
                <div class="row my-2"><!-- search -->
                    <div class="col search_input textMenu" id="textMenu">
                        <button onclick="toggleEditor('jonmun')" type="button" class="btn btn-outline-primary btn-sm">
                            <spring:message code="button.test.sentence"/><!-- 전문 테스트 -->
                        </button>
                        <button onclick="showLinktrace()" type="button" class="btn btn-outline-primary btn-sm">
                            <spring:message code="editor.link.trace"/><!-- 링크추적-->
                        </button>
                        <button type="button" onclick="insertReject();" class="btn btn-sm btn-outline-danger btn_mail_reject">
                            <spring:message code="button.rejectmail"/><!-- 수신거부-->
                        </button>
                    </div>
                </div><!-- //search -->

                <div id="tcpip" style="display: none; width: 100%; height: 350px;"><!-- TCP/IP -->
                    <div id="itemScroll" style="width: 100%; height: 300px; overflow-y: scroll;">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                            <tr>
                                <td height="25" class="editor_line_01">
                                    <div style="width: 700px; border-width: 1px; border-style: solid; padding: 1px 5px 1px 5px;">
                                        <span><spring:message code="editor.alert.realtime.4"/></span><br/>
                                    </div>
                                </td>
                            </tr>
                        </table>

                        <div id="itemList"></div><!-- 개인화정보 -->

                        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                            <tr>
                                <td height="25" class="editor_line_01">
                                    <strong><spring:message code="editor.alert.realtime.5"/></strong>
                                    <a href="javascript:addRealtimeItem(null, null);">
                                        <img src="/images${lang}/ecare/btn_tcp_02.gif" style="vertical-align: middle"/>
                                    </a>
                                    <a href="javascript:makeRealtimeSource();">
                                        <img src="/images${lang}/ecare/btn_tcp_03.gif" style="vertical-align: middle"/>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td height="25" class="editor_line_01"><strong><spring:message code="editor.alert.realtime.6"/></strong></td>
                        </tr>
                        <tr>
                            <td><textarea id="source" rows="15" cols="1" style="width: 100%; height: 150px;"></textarea></td>
                        </tr>
                    </table>
                </div><!-- e.TCP/IP -->

                <div id="jsonSample" style="display: none; width: 100%; height: 300px;"><!-- JSON SAMPLE -->
                    <div class="test_btn_area mb-2">
                        <div class="row mt-3 mb-1">
                            <div class="col-12">
                                <button type="button" id="extractBtn" class="btn btn-outline-primary btn-sm ">
                                    <spring:message code='button.extract.sample' />
                                </button>
                                <button type="button" id="jsonPreviewBtn" class="btn btn-outline-primary btn-sm">
                                    <spring:message code="button.preview"/><!-- 미리보기 -->
                                </button>
                                <button type="button" class="btn btn-sm" id="helpBtn" >
                                    <i class="fas fa-question-circle fa-2x"></i>
                                </button>
                            </div>
                        </div>
                        <div id="helpDetail" class="border p-3" style="display: none;">
                            <h5 class="h3 text-primary mt-1 mb-1">JSON 샘플</h5>
                            <pre class="font-size-13" style="font-family: inherit;">{ "이름" : "홍길동", "내역" : [ {"가맹점" : "A마트", "날짜" : "8월 1일", "금액" : "50,000원"}, {"가맹점" : "B편의점", "날짜" : "8월 2일", "금액" : "20,000원"} ] }</pre>
                            <h5 class="h3 text-primary mt-2 mb-1">템플릿 샘플</h5>
                            <pre class="font-size-13" style="font-family: inherit; white-space: pre-line;">안녕하세요. #{이름} 님.
                                이번 달 카드 이용내역입니다.
                                &lt;table&gt;
                                    &lt;tr&gt;
                                        &lt;th&gt;날짜&lt;/th&gt;
                                        &lt;th&gt;가맹점&lt;/th&gt;
                                        &lt;th&gt;금액&lt;/th&gt;
                                    &lt;/tr&gt;
                                &lt;% for(int i = 0; i &lt; jRecord.getSize("내역"); i++) { %&gt;
                                    &lt;tr&gt;
                                        &lt;td&gt;#{내역[" + i + "].날짜}&lt;/td&gt;
                                        &lt;td&gt;#{내역[" + i + "].가맹점}&lt;/td&gt;
                                        &lt;td&gt;#{내역[" + i + "].금액}&lt;/td&gt;
                                    &lt;/tr&gt;
                                &lt;% } %&gt;
                                &lt;/table&gt;
                             </pre>
                        </div>
                        <div class="edit_area">
                            <textarea class="form-control" id="jsonSource" rows="5"></textarea>
                        </div>
                    </div>
                </div>
                <!-- JSON SAMPLE -->

                <div class="edit_area">
                    <textarea class="form-control" id="attachmode" name="attachmode" style="word-break:break-all" rows="15"></textarea>
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

    <div class="alert alert-secondary mb-0" role="alert">
        <i class="fas fa-circle fa-xs"></i><spring:message code="ecare.2step.help8" /><%-- 템플릿 내 개인화 태그 사용은 #{변수}형태로 넣어주세요 --%>
    </div>

    <div class="card-body" id="attachTab"><!-- 첨부파일 -->
        <div class="row mt-2 mb-0">
           <div class="col-9 align-items-center py-1"><!-- title -->
                <h4 class="h3 text-primary m-0"></i><spring:message code="editor.menu.attachfile"/></h4>
          </div>
          <div class="col-3 justify-content-end pb-1">
            <select name="attachFileSelect" id="attachFileSelect" class="form-control form-control-sm">
                <option><spring:message code="ecare.attachfile.add"/></option>
                <option id="uploadAttFileBtn" value="upload"><spring:message code="editor.attach.upload"/></option><!--  내 pc 불러오기-->
                <option id="pathAttFileBtn" value="path"><spring:message code="editor.attach.path"/></option><!--  경로 입력-->
                <option id="encpdfAttFileBtn" value="encpdf"><spring:message code="editor.attach.encpdf"/></option><!-- pdf 암호화 -->
                <c:if test="${mailType eq 'SMAIL'}">
                    <option id="smailattAttFileBtn" value="smailatt"><spring:message code="editor.attach.smailatt"/></option><!-- 보안메일 내 첨부 -->
                </c:if>
            </select>
          </div>
      </div>
      <div class="col-12 alert alert-secondary mb-0" role="alert">
          <i class="fas fa-circle fa-xs"></i><spring:message code="ecare.2step.help4" /> <!--비밀번호 필드 입력 시 {변수} 형태를 넣어주세요. 예시) ${SEQ} (context 사용 시 : #{변수}) -->
      </div>
        <div class="col-12">
            <div class="table table-sm dataTable" id="attachFileTable">
                <table class="table table-sm dataTable">
                     <colgroup>
                         <col width="6%" /><!-- en:width -->
                         <col width="8%" /><!-- en:width -->
                         <col width="8%" /><!-- en:width -->
                         <col width="15%" />
                         <col width="8%" />
                         <col width="15%" />
                         <col width="10%" />
                         <col width="*" />
                     </colgroup>
                    <c:set var="count" value="0" />
                    <tbody class="bg-white" id="templateBody">
                    <c:forEach var="multipartFileVo" items="${multipartFileList}" varStatus="status">
                    <c:if test="${multipartFileVo.fileType eq 'ENCPDF' || multipartFileVo.fileType eq 'UPLOAD' || multipartFileVo.fileType eq 'PATH' || multipartFileVo.fileType eq 'SMAILATT'}">
                        <tr id="attach">
                            <input type="hidden" name="fileType[${count}]" value="${multipartFileVo.fileType}"/>
                            <td class="text-center">
                                <button class="btn btn-sm btn-outline-danger deleteRow" type="button" >
                                    <i class="fas fa-minus"></i>
                                    <%-- <spring:message code="button.delete"/><!-- 삭제 --> --%>
                                </button>
                            </td>
                            <th scope="row">
                                <spring:message code="editor.attach.${fn:toLowerCase(multipartFileVo.fileType)}"/>
                            </th>
                            <td class="bg-subtotal" ${multipartFileVo.fileType eq 'SMAILATT'?'style=display:none':''}>
                                <em class="required">required</em>
                                <spring:message code="editor.file.name"/>
                            </td>
                            <td ${multipartFileVo.fileType eq 'SMAILATT'?'style=display:none':''}>
                              <input type="text" class="form-control form-control-sm" name="fileName[${count}]" value="${multipartFileVo.fileName}" ${multipartFileVo.fileType eq 'UPLOAD'?'readonly':''}>
                            </td>
                            <c:if test="${multipartFileVo.fileType eq 'ENCPDF'}">
                            <td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.attach.secufield"/></td><!--  비밀번호 필드-->
                            <td><input type="text"  class="form-control form-control-sm" name="secuField[${count}]" value="${multipartFileVo.secuField}" ></td>
                            </c:if>
                            <td class="bg-subtotal"><em class="required">required</em><spring:message code="editor.file.path"/></td><!-- 파일경로 -->
                            <td colspan="8">
                              <input type="text"  class="form-control form-control-sm" name="filePath[${count}]" value="${multipartFileVo.filePath}" ${multipartFileVo.fileType eq 'UPLOAD'?'readonly':''} >
                            </td>
                        </tr>
                        <c:set var="count" value="${count + 1}" />
                    </c:if>
                    </c:forEach>
                    </tbody>
                 </table>
             </div>
        </div>
    </div>
    <!-- 파일첨부 레이어 끝 -->

</div>
</form>