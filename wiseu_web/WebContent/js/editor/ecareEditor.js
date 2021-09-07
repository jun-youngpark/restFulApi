/*-------------------------------------------------------------------------------------------------
 * 이케어 저작기 (모든 채널)
 * - 이전 파일명 : ecareSmsEditor.js ,ecareMmsEditor.js, ecareMailEditor.js, ecareFaxEditor.js, ecareAltalkEditor.js, ecarePushEditor.js
-------------------------------------------------------------------------------------------------*/
/**
 * 핸들러 가져오기
 */
function getEcareInfo() {
    //EditorEcareService.selectEditorEcare(document.editorFrm.no.value, getEditorEcare);
    //getAjaxCall("/editor/"+document.editorFrm.no.value+"/e_editor.do", getEditorEcare);
    var param = {
        type : "E",
        serviceNo : ecareNo
    };

    if('M' === channelType && 'NONE' === mailType) {
        $("#attach-tab").hide();
    } else {// 첨부파일(보안메일,PDF)
        $("#attach-tab").show();
    }

    // 핸들러 수정 상태일 경우 멘트 출력
    if('11'==$("#editorFrm input[name=templateType]").val()) {
        toggleEditMode(true);
    }

    $.post("/editor/selectEditor.json", $.param(param, true), function(ecareEditor) {
        getEditorEcare(ecareEditor);
    });
}

function getEditorEcare(ecareEditor) {
    document.editorFrm.templateType.value = ecareEditor.templateType;
    // 기본핸들러 초기화.
    DefaultHandlerManager.initHandlerList(ecareEditor.handlerSeq, document.editorFrm.templateType.value=='11');

    //EditorEcareService.selectEditorEcareHandler(document.editorFrm.no.value, getEcareHandler);
    //getAjaxCall("/editor/"+document.editorFrm.no.value+"/handler/e_editor.do", getEcareHandler);
    var param = {
        type : "E",
        serviceNo : ecareNo
    };

    $.post("/editor/selectEditorHandler.json", $.param(param, true), function(handlerVo) {
        getEcareHandler(handlerVo);
    });
}

/* handler get */
function getEcareHandler(handlerVo) {
    if(handlerVo != null && handlerVo.handler != null) {
        $("#handler").val(handlerVo.handler);
    } else { // 핸들러가 없다면 기본핸들러를 가져옴 >> 이부분은 핸들러 리스트 초기화로 이동함.
        DefaultHandlerManager.setDefaultHandler();
    }

    getTemplateList(ecareNo, segmentNo, serviceType);
}

/**
 * 템플릿을 조회해서 배열에 저장
 */
var templateArr = new Array();
function getTemplateList(no, segmentNo, serviceType) {

    if(channelType==='M' || channelType==='F'){	//메일 ,FAX 탬플릿 불러오기
        var param = {
                type : "E", serviceNo : ecareNo, segmentNo : segmentNo, serviceType : serviceType
        };
        $.post("/editor/selectEditorTemplate.json", $.param(param, true), function(templateVoList) {
                templateArr = templateVoList;
                setByChannel();
        });
    }else{ //메일 ,FAX을 제외한 채널 탬플릿 불러오기
        var param = {
                no : no, segmentNo : segmentNo, serviceType : serviceType, userId : userId
        };
        //getDataAjaxCall("/editor/" +no + "/" + segmentNo +"/S/e_editor.do", param, function(templateVoList) {
        $.mdf.postJSON("/editor/selectEditorPhoneTemplate.json?type=E&channelType="+channelType, JSON.stringify(param), function(templateVoList) {
            if("error" == templateVoList) {
                alert($.i18n.prop("editor.alert.editor.18"));  // 템플릿을 가져오는 도중 에러가 발생하였습니다.
            } else {
                templateArr = templateVoList;
                reload();	//템플릿 에디터에 설정
            }
        });
    }
}

function reload() {
    var ed = tinyMCE.get('template');
    if(ed == null) {
        setTimeout(reload, 100);
    } else {
        ed.setProgressState(1); // Show progress
        window.setTimeout(function() {
            ed.setProgressState(0); // Hide progress
            for(var i=0; i < templateArr.length; i++){
               var template= templateArr[i].template;
               var imageUrl = templateArr[i].imageUrl;
               if('POPUP'==templateArr[i].seg){	//PUSH 팝업
                   $("#template_pop").val(template);
               } else {
                   // PUSH 템플릿은 HTML 형식으로 저장되어 있어서 줄바꿈 기호를 br 태그로 변경하지 않는다.
                   if(template.indexOf("<html") == -1) {
                       template = replaceNewLineToBrTag(template);
                   }
                   template = setImageTemplate(template , imageUrl)
                   tinyMCE.get('template').setContent(template);
               }
            }
            tinyMCE.execCommand('mceRemoveControl', false, 'template');
            tinyMCE.execCommand('mceAddControl', false, 'template');
            setByChannel();
        }, 1000);
    }
}

//	채널 별 설정이 필요 할 경우
function setByChannel(){
    switch (channelType){
        case "M" :
                        if(templateArr.length > 0) {
                            if(mailType == "NONE") {
                                document.editorFrm.textmode.value = templateArr[0].template;
                            } else {// 첨부파일(보안메일,PDF)
                                 for(var i = 0; i < templateArr.length; i++) {
                                     if(templateArr[i].seg == " ") {
                                         document.editorFrm.textmode.value = templateArr[i].template;
                                     } else {
                                         document.editorFrm.attachmode.value = templateArr[i].template;
                                     }
                                 }
                            }
                        }
                        break;
        case "A" :
                        if(templateArr[0] != null){
                            var kakaoButtons = templateArr[0].kakaoButtons;
                            var kakaoQuickReplies = templateArr[0].kakaoQuickReplies;
                            var contsNo = templateArr[0].contsNo;
                            if(kakaoButtons != null && $.trim(kakaoButtons).length > 2) {
                                setKakaoButtons(kakaoButtons);	//카카오버튼
                            }
                            if(kakaoQuickReplies != null && $.trim(kakaoQuickReplies).length > 2) {
                            	setKakaoQuickReplys(kakaoQuickReplies);	//바로연결
                            }
                            if(contsNo !=0){
                            	  $("#contsNo").val(contsNo);
                            }
                        }
                        break;
        case "C" :
                        if(templateArr[0] != null){
                            var kakaoButtons = templateArr[0].kakaoButtons;
                            if(kakaoButtons != null && $.trim(kakaoButtons).length > 2) {
                                setKakaoButtons(kakaoButtons);
                            }
                        }
                        break;
        case "B" : 	isLoadBrt()
        				break;
        case "F" :
				        if(templateArr.length > 0) {
				        	document.editorFrm.textmode.value = templateArr[0].template;
				        }
				     break;
        case "S" : break;
        case "T" : break;
        default: break;
    }
}