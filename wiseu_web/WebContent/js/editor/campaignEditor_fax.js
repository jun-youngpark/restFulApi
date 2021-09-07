/*-------------------------------------------------------------------------------------------------
 * 캠페인 저작기 (FAX)
 * - 이전 파일명 : campaignFaxEditor.js
-------------------------------------------------------------------------------------------------*/
// A/B 테스트 타입 ('N': 사용 안함)
var abTestType = "N";

/**
 * 캠페인정보 조회
 */
function getCampaignInfo() {
    //EditorCampaignService.selectEditorCampaign(campaignNo, getEditorCampaign);
    //getAjaxCall("/editor/"+campaignNo+"/c_editor.do", getEditorCampaign);
    var param = {
        type : "C",
        serviceNo : campaignNo
    };

    $.post("/editor/selectEditor.json", $.param(param, true), function(campaignEditor) {
        getEditorCampaign(campaignEditor);
    });
}

/**
 * 캠페인 제목, 설문번호, 템플릿타입 조회
 */
function getEditorCampaign(campaignEditor) {
    $("#editorFrm input[name=templateType]").val(campaignEditor.templateType);

    // 기본핸들러 초기화.
    DefaultHandlerManager.initHandlerList(campaignEditor.handlerSeq, campaignEditor.templateType == '11');

    //EditorCampaignService.selectEditorCampaignHandler(campaignNo, getCampaignHandler);
    //getAjaxCall("/editor/"+campaignNo+"/handler/c_editor.do", getCampaignHandler);

    var param = {
        type : "C",
        serviceNo : campaignNo
    };

    $.post("/editor/selectEditorHandler.json", $.param(param, true), function(handlerVo) {
        getCampaignHandler(handlerVo);
    });
}

/**
 * 캠페인 핸들러를 가져온다. 핸들러가 없다면 기본핸들러 사용
 */
function getCampaignHandler(handlerVo) {
    if(handlerVo != null && handlerVo.handler != null) { // 기존에 핸들러가 있다면
        // option 정보가 있는지 확인
        getSlot(handlerVo.handler);

        $("#editorFrm textarea[name=handler]").val(handlerVo.handler);

    } else { // 핸들러가 없다면>> 이부분은 핸들러 리스트 초기화로 이동함.
        DefaultHandlerManager.setDefaultHandler();
    }

    getSemantic();
}

/**
 * 개인화정보 상세조회
 */
var semantic = new Array();
function getSemantic() {
    //EditorCampaignService.selectEditorCampaignSemantic(segmentNo, {
    //getAjaxCall("/editor/"+segmentNo+"/segment/c_editor.do", function(campaignSemanticList) {
    $.mdf.postJSON("/editor/selectEditorCampaignSemantic.json?segmentNo=" + segmentNo, null, function(campaignSemanticList) {
        semantic = campaignSemanticList;

        personalSelectBox();
        getOptionSemantic();
        getTemplateList();
    });
}

/**
 * 캠페인 템플릿 조회
 */
var templateArr = new Array();
function getTemplateList() {
    //getAjaxCall("/editor/"+ campaignNo + "/"+segmentNo+"/c_editor.do", function(templateVoList) {
    var param = {
        type : "C",
        serviceNo : campaignNo,
        segmentNo : segmentNo
    };

    $.post("/editor/selectEditorTemplate.json", $.param(param, true), function(templateVoList) {
        templateArr = templateVoList;

        // tinyMCE 생성
        if(tinyMCE.get("template") == null) {
            tinyMCEInit();
        } else {
            reload();
        }
    });
}

/**
 * 핸들러 및 템플릿 저장
 */
function updateCampaign() {
    var template = tinyMCE.get("template").getContent(); // 템플릿
    $("#editorFrm input[name=templateContent]").val(template);

    //EditorCampaignService.updateEditorCampaign(campaignNo, preface, prefaceTest, templateType, handler, handlerType, segmentNo, template, templateAb, seg, channelType, abTestType, handlerSeq, returnUpdateCampaign);
    //sendAjaxCall("/editor/c_editor.do", "PUT", true , "editorFrm", null, returnUpdateCampaign);
    var param = $.mdf.serializeObject('#editorFrm');
    $.mdf.postJSON("/editor/updateEditor.json?type=C", JSON.stringify(param), function(returnValue) {
        returnUpdateCampaign(returnValue);
    });
}

function returnUpdateCampaign(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.2"));  // 캠페인 정보를 저장하는 도중 에러가 발생하였습니다.
    }else if(returnValue == 100) {
        alert($.i18n.prop("editor.alert.editor.14"));  // 제목은 250byte 이상을 입력 할 수 없습니다. (한글 125자, 영문 250자)
    } else {
        var no = $("#editorFrm input[name=no]").val();

        /*
         * 메시지저장: isRealSave true
         * 임시저장: isRealSave false
         */
        if(isRealSave == true) parent.goSubmit(nextStep);
        else getCampaignInfo();
    }
}

/**
 * 개인화정보 셀렉트박스
 */
function personalSelectBox() {
    $("#editorFrm select[name=personalizeFactor] option").remove();
    $("#editorFrm select[name=personalizeFactor]").append("<option value=''>" + $.i18n.prop("editor.option.person") + "</option>");  // 개인화정보
    for(var i = 0; i < semantic.length; i++) {
        $("#editorFrm select[name=personalizeFactor]").append("<option value='" + semantic[i].fieldSeq + "'>" + semantic[i].fieldDesc + "</option>");
    }
}

/**
 * 대상자를 선택했을 때 개인화정보 리로딩
 */
function getSemantic4Segment(segmentNo) {
    //EditorCampaignService.selectEditorCampaignSemantic(segmentNo, {
    //getAjaxCall("/editor/"+segmentNo+"/segment/c_editor.do", function(campaignSemanticList) {
    $.mdf.postJSON("/editor/selectEditorCampaignSemantic.json?segmentNo=" + segmentNo, null, function(campaignSemanticList) {
        semantic = campaignSemanticList;

        personalSelectBox();
        getOptionSemantic();
        $("#editorFrm textarea[name=handler]").val(resetHanlder($("#editorFrm textarea[name=handler]").val()));
        reload();
    });
};

/**
 * 옵션에 개인화정보를 보여줌
 */
function getOptionSemantic() {
    if($("#editorFrm select[name=optionItem]").length > 0) {
        $("#editorFrm select[name=optionItem] option").remove();

        if(semantic.length == 0) {
            $("#editorFrm select[name=optionItem]").append("<option value=''>" + $.i18n.prop("editor.option.noperson") + "</option>");  // 개인화없음
        } else {
            for(var i = 0; i < semantic.length; i++) {
                if(i == 0) optionValue = semantic[i].fieldNm;
                $("#editorFrm select[name=optionItem]").append("<option value='" + semantic[i].fieldNm + "'>" + semantic[i].fieldDesc + "</option>");
            }
        }
    } else {
        setTimeout(getOptionSemantic, 100);
    }
}

/**
 * zip 파일로 된 템플릿 불러오기
 */
function getTemplateFile(filePath) {
    if(filePath != "") {
        /*EditorTemplateFileService.getTemplateFile("EM", campaignNo, filePath, {
            callback : function(templateVo) {
                tinyMCE.execCommand('mceInsertContent', false, templateVo.template);
            }
        });*/

        var param = {
            type : "EM",
            no : campaignNo,
            filePath : filePath
        };

        $.post("/editor/getTemplateFile.json", $.param(param, true), function(templateVo) {
            tinyMCE.execCommand('mceInsertContent', false, templateVo.template);
        });
    }
}

/**************************************************************
 * 저작기
 * 개인화정보, 불러오기, 링크, 수신거부 이벤트
 **************************************************************/
function selectSemantic(obj) {
    var fieldDesc = "";
    var fieldKey = "";

    for(var i = 0; i < semantic.length; i++) {
        if(obj.value == semantic[i].fieldSeq) {
            if(semantic[i].fieldKey == 'D') {
                fieldDesc = semantic[i].fieldNm;
            } else {
                fieldDesc = semantic[i].fieldDesc;
            }

            fieldKey = semantic[i].fieldKey;
            break;
        }
    }

    if(fieldDesc != "") {
        if(fieldKey == null || fieldKey != 'D') {  // 전문인자가 아닌 경우
            tinyMCE.execCommand('mceInsertContent', false, "{$" + fieldDesc + "$}");
        }
    }
}

function selectOpen(obj) {
    var no = $("#editorFrm input[name=no]").val();

    // /editor/editor_sub.do, /editor/template_editor.do
    if(obj.value == "1") { // ZIP 파일 불러오기
        $.mdf.popupGet('/editor/importPopup.do?type=EM&uploadType=template&no=' + no, 'uploadPopup', 560, 260);
    } else if(obj.value == "2") { // HTML 파일불러오기
        $.mdf.popupGet('/editor/importPopup.do?type=html&uploadType=html', 'uploadPopup', 560, 260);
    } else if(obj.value == "3") { // 템플릿 파일불러오기
        $.mdf.popupGet('/editor/importTemplateListPopup.do?templateType=editor', 'tmplPopup', 840, 550);
    } else if(obj.value == "4") { // URL 파일불러오기
        $.mdf.popupGet('/editor/importPopup.do?uploadType=url', 'urlPopup', 560, 260);
    }
}