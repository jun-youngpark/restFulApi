/*-------------------------------------------------------------------------------------------------
 * 캠페인 저작기 (메일)
 * - 이전 파일명 : campaignMailEditor.js
-------------------------------------------------------------------------------------------------*/
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
    if(campaignEditor.campaignPreface == null) {
        $("#editorFrm input[name=campaignPreface]").val(""); // 캠페인제목
    } else {
        $("#editorFrm input[name=campaignPreface]").val(campaignEditor.campaignPreface);

        if(abTestType == "S") {  // A/B테스트 조건
            $("#editorFrm input[name=campaignPrefaceAb]").val(campaignEditor.campaignPrefaceAb);
        }
    }

    // 템플릿타입 - 1:기본핸들러, 11:수정핸들러, 2:SMS핸들러, 3:MMS핸들러
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
    var con = abTestType;  // A/B테스트 조건

    if(handlerVo != null && handlerVo.handler != null) { // 기존에 핸들러가 있다면
        // option 정보가 있는지 확인
        getSlot(handlerVo.handler);

        $("#editorFrm textarea[name=handler]").val(handlerVo.handler);
    } else { // 핸들러가 없다면 >> 이부분은 핸들러 리스트 초기화로 이동함.
        DefaultHandlerManager.setDefaultHandler();
    }

    //EditorCampaignService.selectEditorCampaignMultipartfile(campaignNo, getEditorCampaignMultipartfile);
    //getAjaxCall("/editor/"+campaignNo+"/file/c_editor.do", getEditorCampaignMultipartfile);
    var param = {
        type : "C",
        serviceNo : campaignNo
    };

    $.post("/editor/selectEditorMultipartfile.json", $.param(param, true), function(multipartfileList) {
        getEditorCampaignMultipartfile(multipartfileList);
    });
}

/**
 * 캠페인 첨부파일 조회
 */
var multipartfile = new Array();
function getEditorCampaignMultipartfile(multipartfileList) {
    if(multipartfileList != null && multipartfileList.length > 0) {
        multipartfile = multipartfileList;

        // 저작기 하단에 첨부파일 태그 생성
        $("#multifileTagId").empty();
        for(var i = 0; i < multipartfileList.length; i++) {
            $("#multifileTagId").append(createMultifileElement(i));
        }
        $("#fileCount").empty().append($('#multifileTagId').children().length);
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
        personalSelectBoxAb();
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
        getLinktraceseqMax();
    });
}

/**
 * 캠페인 링크트레이스 link_seq 최대 값 조회
 */
function getLinktraceseqMax() {
    //EditorCampaignService.selectEditorCampaignLinkseqMax(campaignNo, {
    //getAjaxCall("/editor/"+ campaignNo + "/link/c_editor.do", function(returnValue) {
    $.get("/editor/selectEditorMaxLinkSeq.json?type=C&serviceNo=" + campaignNo, function(returnValue) {
        $("#editorFrm input[name=linkSeq]").val(returnValue);
        //A/B 테스트 발송시 본문A와 본문B의 linkSeq 값 충돌 방지 본문B의 linkSeq는 100부터
        $("#editorFrm input[name=linkSeqAb]").val(100);

        // tinyMCE 생성
        if(tinyMCE.get("template") == null) {
            tinyMCEInit();
        } else {
            reload();
        }

        //A/B테스트 조건 중 템플릿 경우만
        var con = abTestType;
        if(con == "T"){
            // tinyMCE 생성
            if(tinyMCE.get("templateAb") == null) {
                tinyMCEInit();
            } else {
                reloadAb();
            }
        }
    });
}

/**
 * 핸들러 및 템플릿 저장
 */
function updateCampaign() {
    //A/B테스트 조건
    var tmpAbTestType = abTestType;
    var template = tinyMCE.get("template").getContent(); // 템플릿
    $("#editorFrm input[name=templateContent]").val(template);

    var templateAb = "";
    var handlerSeq = $("#editorFrm select[name=handlerList]").val();
    $("#editorFrm input[name='campaignVo.handlerSeq']").val(handlerSeq);

    //A/B테스트 발송시 템플릿 선택
    if(tmpAbTestType == "T"){
        templateAb = tinyMCE.get("templateAb").getContent(); // 템플릿
        seg = "AB";

        $("#editorFrm input[name=templateAbContent]").val(templateAb);
        $("#editorFrm input[name=seg]").val("AB");
    }

    //EditorCampaignService.updateEditorCampaign(no, preface, prefaceTest, templateType, handler, handlerType, segmentNo, template, templateAb, seg, channelType, tmpAbTestType, handlerSeq, returnUpdateCampaign);
    //sendAjaxCall("/editor/c_editor.do", "PUT", true , "editorFrm", null, returnUpdateCampaign);
    var param = $.mdf.serializeObject('#editorFrm');
    $.mdf.postJSON("/editor/updateEditor.json?type=C", JSON.stringify(param), function(returnValue) {
        returnUpdateCampaign(returnValue);
    });
}

function returnUpdateCampaign(returnValue) {
    var con = abTestType;

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

        if(linktrace == null || linktrace.size() == 0) {
            if(isRealSave == true) parent.goSubmit(nextStep);
            else getCampaignInfo();
        } else {
            // EditorCampaignService.updateEditorCampaignLinktrace(linktrace.toArray(), {
            var param = linktrace.toArray();
            //sendAjaxCall("/editor/linkTrace/c_editor.do", "PUT", true, null, param, function (returnValue) {
            $.mdf.postJSON("/editor/updateEditorLinktrace.json?type=C", JSON.stringify(param), function(returnValue) {
                linktrace = null;
                if(isRealSave == true) parent.goSubmit(nextStep);
                else getCampaignInfo();
            });
        }//end else

          if(con == "T" && linktraceAb != null){
              if(linktraceAb == null || linktraceAb.size() == 0) {
                    if(isRealSave == true) parent.goSubmit(nextStep);
                    else getCampaignInfo();
                } else {
                    // EditorCampaignService.updateEditorCampaignLinktrace(linktraceAb.toArray(), {
                    var param = linktraceAb.toArray();
                    //sendAjaxCall("/editor/linkTrace/c_editor.do", "PUT", true, null, param, function (returnValue) {
                    $.mdf.postJSON("/editor/updateEditorLinktrace.json?type=C", JSON.stringify(param), function(returnValue) {
                        linktraceAb = null;
                        if(isRealSave == true) parent.goSubmit(nextStep);
                        else getCampaignInfo();
                    });
                }//end else
              }//end if
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
 * A/B 템플릿 사용시 개인화정보 셀렉트박스
 */
function personalSelectBoxAb() {
    $("#editorFrm select[name=personalizeFactorAb] option").remove();
    $("#editorFrm select[name=personalizeFactorAb]").append("<option value=''>" + $.i18n.prop("editor.option.person") + "</option>");  // 개인화정보
    for(var i = 0; i < semantic.length; i++) {
        $("#editorFrm select[name=personalizeFactorAb]").append("<option value='" + semantic[i].fieldSeq + "'>" + semantic[i].fieldDesc + "</option>");
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
        personalSelectBoxAb();
        getOptionSemantic();
        $("#editorFrm textarea[name=handler]").val(resetHanlder($("#editorFrm textarea[name=handler]").val()));
        reload();

        if(abTestType == "T") {  // AB기능테스트 템플릿
            reloadAb();
        }
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

/**
 * 에디터 제목에 개인화정보 처리
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
        if(isTitleFocus == true) { // 제목에 개인화 인자를 추가할 경우
            insertAtCaret(document.editorFrm.campaignPreface, "<%=" + fieldDesc + "=%>");
        } else if(document.editorFrm.campaignPrefaceAb !=null && isTitleFocusAb == true) {
            insertAtCaret(document.editorFrm.campaignPrefaceAb, "<%=" + fieldDesc + "=%>");
        } else {  // 에디터에 개인화 인자를 추가할 경우
            if(fieldKey == null || fieldKey != 'D') {  // 전문인자가 아닌 경우
                tinyMCE.execCommand('mceInsertContent', false, "{$" + fieldDesc + "$}");
            }
        }
    }
}

/**************************************************************
 * A/B 템플릿
 * 저작기
 * 개인화정보, 불러오기, 링크, 수신거부 이벤트
 **************************************************************/
function selectSemanticAb(obj) {
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
        if(isTitleFocus == true) { // 제목에 개인화 인자를 추가할 경우
            insertAtCaret(document.editorFrm.campaignPreface, "<%=" + fieldDesc + "=%>");
        } else { // 에디터에 개인화 인자를 추가할 경우
            tinyMCE.execCommand('mceInsertContent', false, "{$" + fieldDesc + "$}");
        }
    }
}

function selectOpen(obj) {
    var no = $("#editorFrm input[name=no]").val();

    // /editor/editor_sub.do, /editor/template_editor.do
    if(obj.value == "1") { // ZIP 파일 불러오기
        $.mdf.popupGet('/editor/importPopup.do?type=EM&uploadType=template&no=' + no, 'uploadPopup', 560, 280);
    } else if(obj.value == "2") { // HTML 파일불러오기
        $.mdf.popupGet('/editor/importPopup.do?type=html&uploadType=html', 'uploadPopup', 560, 260);
    } else if(obj.value == "3") { // 템플릿 파일불러오기
        $.mdf.popupGet('/editor/importTemplateListPopup.do?templateType=editor', 'tmplPopup', 840, 550);
    } else if(obj.value == "4") { // URL 파일불러오기
        $.mdf.popupGet('/editor/importPopup.do?uploadType=url', 'urlPopup', 560, 260);
    } else if(obj.value == "5") { // 파일 첨부하기
        $.mdf.popupGet('/editor/importPopup.do?type=EM&uploadType=file&no=' + no, 'uploadPopup', 560, 260);
    }
}

//A/B 테스트 발송 템플릿 사용시
function selectOpenAb(obj) {
    var no = $("#editorFrm input[name=no]").val();

    // /editor/editor_sub.do, /editor/template_editor.do
    if(obj.value == "1") { // ZIP 파일 불러오기
        $.mdf.popupGet('/editor/importPopup.do?type=EM&uploadType=template&templateType=ab&no=' + no, 'uploadPopup', 560, 260);
    } else if(obj.value == "2") { // HTML 파일불러오기
        $.mdf.popupGet('/editor/importPopup.do?type=html&uploadType=html&templateType=ab', 'uploadPopup', 560, 260);
    } else if(obj.value == "3") { // 템플릿 파일불러오기
        $.mdf.popupGet('/editor/importTemplateListPopup.do?templateType=ab', 'tmplPopup', 840, 550);
    } else if(obj.value == "4") { // URL 파일불러오기
        $.mdf.popupGet('/editor/importPopup.do?uploadType=url&templateType=ab', 'urlPopup', 560, 260);
    } else if(obj.value == "5") { // 파일 첨부하기
        $.mdf.popupGet('/editor/importPopup.do?type=EM&uploadType=file&templateType=ab&no=' + no, 'uploadPopup', 560, 260);
    }
}

function selectLink(obj) {
    if(obj.value == "1") {
        // 영역 선택 안 될 경우
        if(tinyMCE.get('template').selection.getNode()==null){
            alert($.i18n.prop("editor.alert.hyper.click"));  // 하이퍼링크 할 영역을 선택하여 주십시오.
            return false;
        }
        var element = tinyMCE.get('template').selection.getNode().nodeName;
        var hyperlink = tinyMCE.get('template').selection.getContent();

        if(element == 'A' && hyperlink.length > 0) {
            if(confirm($.i18n.prop("editor.alert.hyper.confirm")) == true) {  // 하이퍼링크가 설정되어 있습니다. 삭제하시겠습니까?
                tinyMCE.execCommand('unlink', false, hyperlink);
            }
        } else {
            if(hyperlink.length > 0) {
                hyperlink = escape(encodeURIComponent(hyperlink));
                // /editor/editor_link.do
                $.mdf.popupGet('/editor/linkPopup.do?type=hyperlink&hyperlink=' + hyperlink, 'hyperLinkPopup', 520, 270);
            } else {
                alert($.i18n.prop("editor.alert.hyper.click"));  // 하이퍼링크 할 영역을 선택하여 주십시오.
            }
        }
    } else if(obj.value == "2") {
        if(semantic.length == 0) {
            alert($.i18n.prop("editor.alert.editor.3"));  // 대상자를 먼저 등록하신 후 링크를 설정하실 수 있습니다.
        } else {
            linktrace = new Linktrace();
            // /editor/editor_link.do
            $.mdf.popupGet("/editor/linkPopup.do?type=linktrace", "linkTracePopup", 1024, 620);
        }
    }
}

//A/B 템플릿 사용시
function selectLinkAb(obj) {
    if(obj.value == "1") {
        // 영역 선택 안 될 경우
        if(tinyMCE.get('templateAb').selection.getNode()==null){
            alert($.i18n.prop("editor.alert.hyper.confirm"));  // 하이퍼링크가 설정되어 있습니다. 삭제하시겠습니까?
            return false;
        }
        var element = tinyMCE.get('templateAb').selection.getNode().nodeName;
        var hyperlink = tinyMCE.get('templateAb').selection.getContent();

        if(element == 'A' && hyperlink.length > 0) {
            if(confirm($.i18n.prop("editor.alert.hyper.confirm")) == true) {  // 하이퍼링크가 설정되어 있습니다. 삭제하시겠습니까?
                tinyMCE.execCommand('unlink', false, hyperlink);
            }
        } else {
            if(hyperlink.length > 0) {
                hyperlink = escape(encodeURIComponent(hyperlink));
                // /editor/editor_link.do
                $.mdf.popupGet('/editor/linkPopup.do?type=hyperlink&hyperlink=' + hyperlink, 'hyperLinkPopup', 520, 270);
            } else {
                alert($.i18n.prop("editor.alert.hyper.click"));  // 하이퍼링크 할 영역을 선택하여 주십시오.
            }
        }
    } else if(obj.value == "2") {
        if(semantic.length == 0) {
            alert($.i18n.prop("editor.alert.editor.3"));  // 대상자를 먼저 등록하신 후 링크를 설정하실 수 있습니다.
        } else {
            linktraceAb = new Linktrace();
            // /editor/editor_link.do
            $.mdf.popupGet("/editor/linkPopup.do?type=linktrace&templateType=ab", "linkTracePopup", 1024, 620);
        }
    }
}

function setupReject() {
    if(semantic.length == 0) {
        alert($.i18n.prop("editor.alert.editor.4"));  // 대상자를 먼저 등록하신 후 수신거부를 설정하실 수 있습니다.
        return;
    }

    var rejectLink = "{$=(Reject)$}";

    var rejectSelect = tinyMCE.get('template').selection.getContent();
    var parentRejectSelect = tinyMCE.get('template').selection.getNode().parentNode.parentNode.innerHTML;

    if((rejectSelect != null && rejectSelect.indexOf(rejectLink) > -1)
        || (parentRejectSelect != null && parentRejectSelect.indexOf(rejectLink) > -1)) {
        if(confirm($.i18n.prop("editor.alert.editor.5")) == true) {  // 수신거부가 설정되어 있습니다. 해제하시겠습니까?
            tinyMCE.execCommand('unlink', false, tinyMCE.get('template').selection.getContent());
        }
    } else {
        if(rejectSelect != null && rejectSelect.length > 0) {
            if(confirm($.i18n.prop("editor.alert.editor.6")) == true) {  // 선택한 영역에 수신거부를 설정하시겠습니까?
                tinyMCE.execCommand('createlink', false, rejectLink);
            }
        } else {
            alert($.i18n.prop("editor.alert.editor.7"));  // 수신거부 할 영역을 선택하여 주십시오.
        }
    }
}

function setupRejectAb() {
    if(semantic.length == 0) {
        alert($.i18n.prop("editor.alert.editor.4"));  // 대상자를 먼저 등록하신 후 수신거부를 설정하실 수 있습니다.
        return;
    }

    var rejectLink = "{$=(Reject)$}";

    var rejectSelect = tinyMCE.get('templateAb').selection.getContent();
    var parentRejectSelect = tinyMCE.get('templateAb').selection.getNode().parentNode.parentNode.innerHTML;

    if((rejectSelect != null && rejectSelect.indexOf(rejectLink) > -1)
        || (parentRejectSelect != null && parentRejectSelect.indexOf(rejectLink) > -1)) {
        if(confirm($.i18n.prop("editor.alert.editor.5")) == true) {  // 수신거부가 설정되어 있습니다. 해제하시겠습니까?
            tinyMCE.execCommand('unlink', false, tinyMCE.get('templateAb').selection.getContent());
        }
    } else {
        if(rejectSelect != null && rejectSelect.length > 0) {
            if(confirm($.i18n.prop("editor.alert.editor.6")) == true) {  // 선택한 영역에 수신거부를 설정하시겠습니까?
                tinyMCE.execCommand('createlink', false, rejectLink);
            }
        } else {
            alert($.i18n.prop("editor.alert.editor.7"));  // 수신거부 할 영역을 선택하여 주십시오.
        }
    }
}