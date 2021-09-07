/*-------------------------------------------------------------------------------------------------
 * - 이전 파일명 : campaignPushEditor.js
-------------------------------------------------------------------------------------------------*/
/**
 * 핸들러와 템플릿 저장
 */
var isRealSave = false;
function saveEditor(val) {
    var preface = parent.$("#editorFrm input[name=campaignPreface]").val(); // 캠페인제목
    var handlerSeq = $("#editorFrm select[name=handlerList]").val();

    $("#editorFrm input[name='campaignVo.handlerSeq']").val(handlerSeq);
    $("#editorFrm input[name=campaignPreface]").val(preface);

    isRealSave = val;

    //EditorCampaignService.updateEditorCampaign(campaignNo, preface, prefaceTest, templateType, handler, handlerType, segmentNo, template, templateAb, seg, channelType, abTestType, handlerSeq, setConfirmCampaign);
    //sendAjaxCall("/editor/c_editor.do", "PUT", true , "editorFrm", null, setConfirmCampaign);
    var param = $.mdf.serializeObject('#editorFrm');
    $.mdf.postJSON("/editor/updateEditor.json?type=C", JSON.stringify(param), function(returnValue) {
        setConfirmCampaign(returnValue);
    });
}

function setConfirmCampaign(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.2"));  // 캠페인 정보를 저장하는 도중 에러가 발생하였습니다.
    } else {
        //EditorCampaignService.updateEditorCampaignMmsHandler(no, handler, handlerType, setConfirmHandler);
        //sendAjaxCall("/editor/P/c_editor.do", "PUT", true , "editorFrm", null, setConfirmHandler);
        var param = $.mdf.serializeObject('#editorFrm');
        $.mdf.postJSON("/editor/updateEditorHandler.json?type=C&channelType=P", JSON.stringify(param), function(returnValue) {
            setConfirmHandler(returnValue);
        });
    }
}

function setConfirmHandler(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.15"));  // 핸들러를 저장하는 도중 에러가 발생하였습니다.
    } else {
        var template = tinyMCE.get("template").getContent();
        $("#editorFrm input[name=templateContent]").val(template);

        //EditorCampaignService.updateEditorCampaignPushTemplate(no, segmentNo, template, seg, handlerType, setConfirmTemplate);
        //sendAjaxCall("/editor/P/template/c_editor.do", "PUT", true , "editorFrm", null, setConfirmTemplate);
        var param = $.mdf.serializeObject('#editorFrm');
        $.mdf.postJSON("/editor/updateEditorTemplate.json?type=C&channelType=P", JSON.stringify(param), function(returnValue) {
            setConfirmTemplate(returnValue);
        });
    }
}

function setConfirmTemplate(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.16"));  // 템플릿을 저장하는 도중 에러가 발생하였습니다.
    } else {
        var template = $("#editorFrm textarea[name=template_pop]").val();
        template = stripMobileTemplateTag(template)

        $("#editorFrm input[name=templateContent]").val(template);
        $("#editorFrm input[name=seg]").val("POPUP");
        //EditorCampaignService.updateEditorCampaignPushTemplate(no, segmentNo, template, seg, handlerType, setConfirmTemplatePop);
        //sendAjaxCall("/editor/P/template/c_editor.do", "PUT", true , "editorFrm", null, setConfirmTemplatePop);
        var param = $.mdf.serializeObject('#editorFrm');
        $.mdf.postJSON("/editor/updateEditorTemplate.json?type=C&channelType=P", JSON.stringify(param), function(returnValue) {
            setConfirmTemplatePop(returnValue);
        });

    }
}

function setConfirmTemplatePop(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.17"));  // 팝업 템플릿을 저장하는 도중 에러가 발생하였습니다.
    } else {
        if($("#editorFrm input[name=pushImgUrl]").val().length > 0){
            $("#editorFrm input[name=pushPopImgUse]").val("Y");
        }
        if($("#editorFrm input[name=pushBigImgUrl]").val().length > 0){
            $("#editorFrm input[name=pushBigImgUse]").val("Y");
        }
        //sendAjaxCall("/env/env_push_setting.do", "PUT", true , "editorFrm", null, setConfirmPushServiceInfo);
        var param = $.mdf.serializeObject('#editorFrm');
        $.post("/env/updatePushServiceInfo.json", $.param(param, true), function(result) {
            if(result == 0) {
                alert($.i18n.prop("editor.alert.editor.21"));  // PUSH 정보를 저장하는 도중 에러가 발생하였습니다.
            } else if(returnValue > 0) {
                if(isRealSave == true) parent.goSubmit();
            }
        });
    }
}

/**
 * 캠페인 정보와 핸들러 가져온다.
 */
function getCampaignInfo() {
    //EditorCampaignService.selectEditorCampaign(document.editorFrm.no.value, getEditorCampaign);
    //getAjaxCall("/editor/"+document.editorFrm.no.value+"/c_editor.do", getEditorCampaign);
    var param = {
        type : "C",
        serviceNo : $("#editorFrm input[name=no]").val()
    };

    $.post("/editor/selectEditor.json", $.param(param, true), function(campaignEditor) {
        getEditorCampaign(campaignEditor);
    });
}

function getEditorCampaign(campaignEditor) {
    if(campaignEditor.campaignPreface == null) {
        parent.$("#editorFrm input[name=campaignPreface]").val("");
    } else {
        parent.$("#editorFrm input[name=campaignPreface]").val(campaignEditor.campaignPreface);
        $("#editorFrm input[name=templateType]").val(campaignEditor.templateType);
    }

    // 기본핸들러 초기화.
    DefaultHandlerManager.initHandlerList(campaignEditor.handlerSeq, campaignEditor.templateType == '11');

    //EditorCampaignService.selectEditorCampaignHandler(document.editorFrm.no.value, getCampaignHandler);
    //getAjaxCall("/editor/"+document.editorFrm.no.value+"/handler/c_editor.do", getCampaignHandler);
    var param = {
        type : "C",
        serviceNo : $("#editorFrm input[name=no]").val()
    };

    $.post("/editor/selectEditorHandler.json", $.param(param, true), function(handlerVo) {
        getCampaignHandler(handlerVo);
    });
}

/**
 * 핸들러 가져온 결과
 */
function getCampaignHandler(handlerVo) {
    if(handlerVo != null && handlerVo.handler != null) {
        $("#editorFrm textarea[name=handler]").val(handlerVo.handler);
    } else { // 핸들러가 없다면 >> 이부분은 핸들러 리스트 초기화로 이동함.
        DefaultHandlerManager.setDefaultHandler();
    }

    getTemplateList($("#editorFrm input[name=no]").val(), $("#editorFrm input[name=segmentNo]").val());
}

/**
 * 템플릿을 조회해서 배열에 저장
 */
var templateArr = new Array();
function getTemplateList(no, segmentNo) {
    //getAjaxCall("/editor/" +no + "/" + segmentNo +"/P/c_editor.do", function(templateVoList) {
    var param = {
        no : no,
        segmentNo : segmentNo
    };

    $.mdf.postJSON("/editor/selectEditorPhoneTemplate.json?type=C&channelType=P", JSON.stringify(param), function(templateVoList) {
            if("error" == templateVoList) {
                alert($.i18n.prop("editor.alert.editor.18"));  // 템플릿을 가져오는 도중 에러가 발생하였습니다.
            } else {
                templateArr = templateVoList;
                 reload();
            }
        });
    getSemantic(segmentNo);
}

/**
 * 시맨틱 정보 가져온다.
 */
var semantic = new Array();
function getSemantic(segmentNo) {
    //EditorCampaignService.selectEditorCampaignSemantic(segmentNo, {
    //getAjaxCall("/editor/"+segmentNo+"/segment/c_editor.do", function(campaignSemanticList) {
    $.mdf.postJSON("/editor/selectEditorCampaignSemantic.json?segmentNo=" + segmentNo, null, function(campaignSemanticList) {
        if(campaignSemanticList == null) {
            alert($.i18n.prop("editor.alert.editor.19"));  // 개인화정보를 가져오는 도중 에러가 발생하였습니다.
        } else {
            semantic = campaignSemanticList;
            getOptionSemantic();
        }
    });
}

/**
 * 대상자를 선택했을 때 개인화정보 가져옴.
 */
function getSemantic4Segment(segmentNo) {
    //EditorCampaignService.selectEditorCampaignSemantic(segmentNo, {
    //getAjaxCall("/editor/"+segmentNo+"/segment/c_editor.do", function(campaignSemanticList) {
    $.mdf.postJSON("/editor/selectEditorCampaignSemantic.json?segmentNo=" + segmentNo, null, function(campaignSemanticList) {
        if(campaignSemanticList == null) {
            alert($.i18n.prop("editor.alert.editor.19"));  // 개인화정보를 가져오는 도중 에러가 발생하였습니다.
        } else {
            semantic = campaignSemanticList;
            getOptionSemantic();
            reload();
        }
    });
}

/**
 * 옵션에 개인화정보를 보여줌
 */
function getOptionSemantic() {
    if($("#editorFrm select[name=optionItem]").length > 0) {
        setSemanticOption();
    } else {
        setTimeout(getOptionSemantic, 100);
    }
}

function setSemanticOption() {
    $("#editorFrm select[name=optionItem] option").remove();

    if(semantic.length == 0) {
        $("#editorFrm select[name=optionItem]").append("<option value=''>" + $.i18n.prop("editor.option.noperson") + "</option>");  // 개인화없음
    } else {
        $("#editorFrm select[name=optionItem]").append("<option value=''>" + $.i18n.prop("editor.option.person") + "</option>");  // 개인화정보

        for(var i = 0; i < semantic.length; i++) {
            $("#editorFrm select[name=optionItem]").append("<option value='" + semantic[i].fieldDesc + "'>" + semantic[i].fieldDesc + "</option>");
        }
    }
}

function selectSemantic(thisObj){
    if(null==thisObj||''==thisObj||'undefined'==typeof(thisObj)){}
    else {
        if(''==thisObj.value){
        }
        else {
                var tempValue = "{$"+thisObj.value+"$}";
                    var $txt = jQuery("#template_pop");
                    var caretPos = $txt[0].selectionStart;
                    var textAreaTxt = $txt.val();
                    $txt.val(textAreaTxt.substring(0, caretPos) + tempValue + textAreaTxt.substring(caretPos) );
        }
    }
}