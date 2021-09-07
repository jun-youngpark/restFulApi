/*--------------------------------------------------------------
 * 캠페인/이케어 저작기 2단계 공통
----------------------------------------------------------------*/
function setSemantic(segmentNo) {
    $("#editorFrm input[name=segmentNo]").val(segmentNo);

    var serviceType = $("#editorFrm input[name=serviceType]").val();
    if($.mdf.isBlank(serviceType)) {  // 캠페인
        getSemantic4Segment(segmentNo);
    } else { // 이케어
        getSemantic4Segment(segmentNo, serviceType);
    }
}
// 숨김처리할 id
function hideButtonByAuth(selector){
	$.each(selector , function(index , value){
		$("#"+value).hide();
	});
}
/**
 * 웹에디터 제목 커서 위치
 */
function storeCaret(preface) {
    if (preface.createTextRange && 'undefined' != typeof(document.selection)) {  // IE11 대응
        preface.caretPos = document.selection.createRange().duplicate();
    }
}

/**
 * 웹에디터 제목 커서 위치에 개인화 넣기
 */
function insertAtCaret(obj, text) {
    if($.mdf.isBlank($("#editorFrm input[name=serviceType]").val())) {  // 캠페인
        if (obj.createTextRange && obj.caretPos) {
            var caretPos = obj.caretPos;
            caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? text + ' ' : text;
        } else if (typeof(document.selection) != 'undefined') {
            var range = document.selection.createRange();

            if (range.parentElement() != obj) return;

            range.text = text;
            range.select();
        } else if (typeof(obj.selectionStart) != 'undefined') {
            var start = obj.selectionStart;

            obj.value = obj.value.substr(0, start) + text + obj.value.substr(obj.selectionEnd, obj.value.length);

            start += text.length;
            obj.setSelectionRange(start, start);
        } else {
            obj.value += text;
        }
    } else { // 이케어
        $(obj).insertAtCaret(text);
    }
}

/**
 * 문자, 알림톡에서 tiny_mce 사용시 생성되는 불필요 HTML 태그 제거. <br/>
 * - IE 는 P 태그로 생성되고, 비IE 에서 div 로 생성되어 구분 추가함.
 * @param template
 * @returns
 */
function stripMobileTemplateTag(template){
    template = template.replace(/<br \/>\n/g, "<br \/>");	// 저장된 값+캐리지 리턴시 엔터가 하나 더생기는 문제가 생김..
    template = template.replace(/<br \/>/g, "\n");

    // <> 사용. P 태그만 삭제.
    var strip = new RegExp();
    // div 또는 p 삭제.. 20161014. a 태그도 삭제.
    strip = /[<][/]*[d][i][v][>]|[<][/]*[p][>]|[<][/]*[a][^>]*[>]/gi;
    template = template.replace(strip, ""); // HTML 제거
    return template;
}

/**
 * 문자, 알림톡에서 html 엔티티 변환된 값을 원래 기호로 변경. <br/>
 * @param template
 * @returns
 */
function splitMobileTemplateHtmlEntity(template){
    template = template.replace(/[<][b][r][ *][/][>]/g, "\n");
    return template;
}

/**
 * 문자, 알림톡에서 기호를 html 엔티티 변환된 값을 원래 기호로 변경. <br/>
 * @param template
 * @returns
 */
function replaceNewLineToBrTag(template){
    return template.replace(/\n/g, "<br/>");
}

/**
 * 템플릿 하단에 이미지를 합친다
 * @param template
 * @returns
 */
function setImageTemplate(template , imageUrl){
  if($.mdf.isNotBlank(imageUrl)) {
          imageUrl = '<img height="149px" width="203px" src="' + imageUrl + '"/>'
          template = template + imageUrl;
  }
  return template;
}


/**
 * 이미지 TAG만 불러오기
 * @param template
 * @returns
 */
function getImageTag(template){
	var Img = "";
    var re = new RegExp(/[<]img[^>]*[>]/gi);
    if(template.match(re) != null) {
        var tmpImg = template.match(re);
        template = template.replace(/[<]img[^>]*[>]/gi,"");	 	// 태그 제거후  이미지 추가
        template = template.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;");
        if(tmpImg != null) {
            for(var j = 0; j < tmpImg.length; j++) {
                Img += tmpImg[j];
            }
        }
    }
    return Img;
}

/**
 * 이미지 템플릿 상단에 위치
 * @param template
 * @returns
 */
function setImageLocation(template){
    var re = new RegExp(/[<]img[^>]*[>]/gi);
    if(template.match(re) != null) {
        var tmpImg = template.match(re);
        template = template.replace(/[<]img[^>]*[>]/gi,"");	 	// 태그 제거후  이미지 추가
        template = template.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br/>").replace(/ /g, "&nbsp;");
        if(tmpImg != null) {
            var Img = "";
            for(var j = 0; j < tmpImg.length; j++) {
                Img += tmpImg[j];
            }
            template = Img + template;
        }
    }
    return template;
}

function goBadWordEc() {
    tinyMCE.triggerSave();
    //A/B 사용중 템플릿 일때
    if(channelType=='P') {
        $("#editorFrm input[name=imsiTemplate]").val($("#editorFrm textarea[name=template]").val() + $("#editorFrm textarea[name=template_pop]").val());
    } else if(channelType=='M') {
        $("#editorFrm input[name=imsiTemplate]").val($("#editorFrm textarea[name=textmode]").val());
    } else{
        $("#editorFrm input[name=imsiTemplate]").val($("#editorFrm textarea[name=template]").val());
    }

    $("#editorFrm input[name=imsiChannelType]").val(channelType);
    $.mdf.popupSubmit("#editorFrm", '/common/badWordCheckPopup.do', "badWordPopup", 600, 480);  // /common/badword_popup.do

    $("#editorFrm input[name=imsiTemplate]").val("");
    $("#editorFrm").attr('target', '');
}

function goLinkCheckEc() {
    $("#editorFrm input[name=imsiTemplate]").val($("#editorFrm textarea[name=textmode]").val());

    $.mdf.popupSubmit("#editorFrm", '/common/linkCheckPopup.do', "linkCheckPopup", 600, 480);  // /common/linkcheck_popup.do

    $("#editorFrm input[name=imsiTemplate]").val("");
    $("#editorFrm").attr('target', '');
}