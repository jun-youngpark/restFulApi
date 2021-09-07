/*-------------------------------------------------------------------------------------------------
 * Title       : 핸들러와 템플릿 저장
 * Description : 핸들러, 템플릿, 옵션, 개인화인자에 관련된 기능을 처리한다.
 * - 이전 파일명 : campaignMmsEditor.js
-------------------------------------------------------------------------------------------------*/
function saveEditor(val) {
    var preface = $("#editorFrm input[name=campaignPreface]").val(); // 캠페인제목
    if(preface == "") {
    	$("#editorFrm input[name=campaignPreface]").val(" ");
    }

    var handlerSeq = $("#editorFrm select[name=handlerList]").val();

    isRealSave = val;

    //EditorCampaignService.updateEditorCampaign(campaignNo, preface, prefaceTest, templateType, handler, handlerType, segmentNo, template, templateAb, seg, '', abTestType, handlerSeq, setConfirmCampaign);
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
        //EditorCampaignService.updateEditorCampaignMmsHandler(no, handler, handlerType, setConfirmHandler);
        //sendAjaxCall("/editor/T/c_editor.do", "PUT", true , "editorFrm", null, setConfirmHandler);
        var param = $.mdf.serializeObject('#editorFrm');
        $.mdf.postJSON("/editor/updateEditorHandler.json?type=C&channelType=T", JSON.stringify(param), function(returnValue) {
            setConfirmHandler(returnValue);
        });
    }
}

function setConfirmHandler(returnValue) {
    if(returnValue == 0) {
        alert($.i18n.prop("editor.alert.editor.15"));  // 핸들러를 저장하는 도중 에러가 발생하였습니다.
    } else {
        var img = getImageTag(tinyMCE.get("template").getContent());
        var template = img + tinyMCE.get("template").getContent({ format: "text"}) ;

        $("#editorFrm input[name=templateContent]").val(template);

        //EditorCampaignService.updateEditorCampaignMmsTemplate(no, segmentNo, template, seg, handlerType, setConfirmTemplate);
        //sendAjaxCall("/editor/T/template/c_editor.do", "PUT", true , "editorFrm", null, setConfirmTemplate);
        var param = $.mdf.serializeObject("#editorFrm");
        $.mdf.postJSON("/editor/updateEditorTemplate.json?type=C&channelType=T", JSON.stringify(param), function(returnValue) {
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
        $("#editorFrm input[name=campaignPreface]").val("");
    } else {
        $("#editorFrm input[name=campaignPreface]").val(campaignEditor.campaignPreface);
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
    //getAjaxCall("/editor/" +no + "/" + segmentNo +"/T/c_editor.do", function(templateVoList) {
    var param = {
        no : no,
        segmentNo : segmentNo
    };

    $.mdf.postJSON("/editor/selectEditorPhoneTemplate.json?type=C&channelType=T", JSON.stringify(param), function(templateVoList) {
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
            //getOptionSemantic();
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
        if (campaignSemanticList == null) {
            alert($.i18n.prop("editor.alert.editor.19"));  // 개인화정보를 가져오는 도중 에러가 발생하였습니다.
        } else {
            semantic = campaignSemanticList;
            //getOptionSemantic();
            $("#editorFrm textarea[name=handler]").val(resetPhoneHanlder($("#editorFrm textarea[name=handler]").val()));
            reload();
        }
    });
};

/**
 * 옵션에 개인화정보를 보여줌 (미사용)
 */
/*function getOptionSemantic() {
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
}*/

/**
 * 에디터 제목에 개인화정보 처리를 한다.
 */
var subjectTag = ""; // handler subject tag
var isPrefaceItem = false;
function checkItem(preface) {
    var startCnt = 0; // subject offset
    var endCnt = 0;  // subject length
    var tmp = preface;
    var item = ""; // item
    var desc = ""; // item description
    var subjectArr = new Array(); // subject with item
    var cnt = 0; // item index

    if(tmp.lastIndexOf("<%=") > -1 && tmp.lastIndexOf("=%>") > -1) {
        if($("#editorFrm input[name=handlerType]").val() == "S") {
            subjectTag = "Subj = java.lang.StringBuffer.new()\n";
        } else {
            subjectTag = "StringBuffer Subj = new StringBuffer()\n";
        }

        isPrefaceItem = true;
    } else {
        isPrefaceItem = false;
    }

    while(tmp.lastIndexOf("<%=") > -1 && tmp.lastIndexOf("=%>") > -1) {
        startCnt = tmp.lastIndexOf("<%=");
        endCnt = tmp.lastIndexOf("=%>");
        item = tmp.substring(startCnt + 3, endCnt);
        desc = tmp.substring(endCnt + 3);
        tmp = tmp.substring(0, startCnt);

        if(desc.length > 0) {
            subjectArr[cnt] = "Subj.append(\"" + desc + "\")";
            cnt++;
        }

        for(var j = 0; j < semantic.length; j++) {
            if(item == semantic[j].fieldDesc) {
                subjectArr[cnt] = "Subj.append(target.getString(" + semantic[j].fieldSeq + "))";
                cnt++;
                break;
            }
        }
    }

    if(tmp.length > 0) {
        subjectArr[cnt] = "Subj.append(\"" + tmp + "\")";;
    }

    for(var k = subjectArr.length - 1; k >= 0 ; k--) {
        subjectTag += subjectArr[k] + "\n";
    }

    if(isPrefaceItem == true) {
        preface = "Subj.toString()";
    }

    return preface;
}

/**
* 에디터 제목에 "" replace 처리
*/
function replaceSubject(obj) {
    var str = obj.value;
        var rtnStr = "";
        var strCompare = "";

        for(var i=0;i<str.length;i++){
            strCompare = str.charCodeAt(i);
            if(strCompare == 34){
                strCompare = 65282;
            }
            rtnStr += String.fromCharCode(strCompare);
    }

    obj.value = rtnStr;
}