/*-------------------------------------------------------------------------------------------------
 * Title       : 핸들러와 템플릿 저장
 * Description : 핸들러, 템플릿, 옵션, 개인화인자에 관련된 기능을 처리한다.
 * - 이전 파일명 : campaignBrandtalkEditor.js
-------------------------------------------------------------------------------------------------*/
function saveEditor(val) {
    var handlerSeq = $("#editorFrm select[name=handlerList]").val();
    $("#editorFrm input[name='campaignVo.handlerSeq']").val(handlerSeq);
    isRealSave = val;
    //sendAjaxCall("/editor/c_editor.do", "PUT", true , "editorFrm", null, setConfirmCampaign);
    var param = $.mdf.serializeObject('#editorFrm');
    $.mdf.postJSON("/editor/updateEditor.json?type=C", JSON.stringify(param), function(returnValue) {
        setConfirmCampaign(returnValue);
    });
}

function addTemplate(templates, no, template, seg) {
    templates[templates.length] = {
        no: no,
        template: template,
        seg: seg
    };
}

function setConfirmCampaign(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.2"));  // 캠페인 정보를 저장하는 도중 에러가 발생하였습니다.
    } else {
        var channel = $("#editorFrm input[name=channel]").val();
        //sendAjaxCall("/editor/"+channel+"/c_editor.do", "PUT", true , "editorFrm", null, setConfirmHandler);
        var param = $.mdf.serializeObject('#editorFrm');
        $.mdf.postJSON("/editor/updateEditorHandler.json?type=C&channelType=" + channel, JSON.stringify(param), function(returnValue) {
            setConfirmHandler(returnValue);
        });
    }
}

function setConfirmHandler(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.15"));  // 핸들러를 저장하는 도중 에러가 발생하였습니다.
    } else {
        var template = tinyMCE.get("template").getContent({ format: "text"});
        $("#editorFrm input[name=templateContent]").val(template);
        $("#editorFrm input[name=kakaoButtons]").val(parent.kakaoButtons);

        //sendAjaxCall("/editor/"+channel+"/template/c_editor.do", "PUT", true , "editorFrm", null, setConfirmTemplate);
        var channel = $("#editorFrm input[name=channel]").val();
        var param = $.mdf.serializeObject("#editorFrm");
        $.mdf.postJSON("/editor/updateEditorTemplate.json?type=C&channelType=" + channel, JSON.stringify(param), function(returnValue) {
            setConfirmTemplate(returnValue);
        });
    }
}

function setConfirmTemplate(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.16"));  // 템플릿을 저장하는 도중 에러가 발생하였습니다.
    } else {
        if(isRealSave == true) parent.goSubmit();
    }
}

/**
 * 캠페인 정보와 핸들러 가져온다.
 */
function getCampaignInfo() {
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
    $("#editorFrm input[name=templateType]").val(campaignEditor.templateType);

    // 기본핸들러 초기화.
    DefaultHandlerManager.initHandlerList(campaignEditor.handlerSeq, campaignEditor.templateType == '11');

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
    } else { // 핸들러가 없다면 기본핸들러를 가져옴 >> 이부분은 핸들러 리스트 초기화로 이동함.
        DefaultHandlerManager.setDefaultHandler();
    }

    getTemplateList($("#editorFrm input[name=no]").val(), $("#editorFrm input[name=segmentNo]").val());
}

/**
 * 템플릿을 조회해서 배열에 저장
 */
var templateArr = new Array();
function getTemplateList(no, segmentNo) {
    var channel = $("#editorFrm input[name=channel]").val();
    //getAjaxCall("/editor/" +no + "/" + segmentNo +"/"+channel+"/c_editor.do", function(templateVoList) {
    var param = {
        no : no,
        segmentNo : segmentNo
    };

    $.mdf.postJSON("/editor/selectEditorPhoneTemplate.json?type=C&channelType=" + channel, JSON.stringify(param), function(templateVoList) {
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
    //getAjaxCall("/editor/"+segmentNo+"/segment/c_editor.do", function(campaignSemanticList) {
    $.mdf.postJSON("/editor/selectEditorCampaignSemantic.json?segmentNo=" + segmentNo, null, function(campaignSemanticList) {
        if(campaignSemanticList == null) {
            alert($.i18n.prop("editor.alert.editor.19"));  // 개인화정보를 가져오는 도중 에러가 발생하였습니다.
        } else {
            semantic = campaignSemanticList;
        }
    });
}

/**
 * 대상자를 선택했을 때 개인화정보 가져옴.
 */
function getSemantic4Segment(segmentNo) {
    //getAjaxCall("/editor/"+segmentNo+"/segment/c_editor.do", function(campaignSemanticList) {
    $.mdf.postJSON("/editor/selectEditorCampaignSemantic.json?segmentNo=" + segmentNo, null, function(campaignSemanticList) {
        if(campaignSemanticList == null) {
            alert($.i18n.prop("editor.alert.editor.19"));  // 개인화정보를 가져오는 도중 에러가 발생하였습니다.
        } else {
            semantic = campaignSemanticList;
            //$("#editorFrm textarea[name=handler]").val($("#editorFrm textarea[name=handler]").val());
            reload();
        }
    });
};

