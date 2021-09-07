/*-------------------------------------------------------------------------------------------------
 * 저작기 2단계에서 사용되는 공통 자바스크립트
-------------------------------------------------------------------------------------------------*/
/**
 * 저작기 컨텐츠(템플릿) 저장 시 필요 한 templateVo를 생성
 * @param no 이케어번호
 * @param template 컨텐츠(템플릿)
 * @param kakaoSenderKey 프로필 키(카카오에서 사용)
 * @param kakaoTmplCd 템플릿코드(카카오에서 사용)
 * @param templateArray 저장할 배열이 존재 할 경우 해당 객체에 입력
 * @returns
 */
var makeTemplate = function(no, template, seg, templateArray) {
   var templateVo = {
            no: no,
            template: template,
            seg: seg,
      };
   if(templateArray===undefined){
       return  $.makeArray(templateVo);
   }else{
       templateArray.push(templateVo)
   }
};

/**
 * 저작기 컨텐츠(템플릿) 저장 시 필요 한 templateVo를 생성
 * @param no 이케어번호
 * @param template 컨텐츠(템플릿)
 * @param kakaoSenderKey 프로필 키(카카오에서 사용)
 * @param kakaoTmplCd 템플릿코드(카카오에서 사용)
 * @param contNo 모바일 컨텐츠 테이블 Key(카카오에서 사용)
 * @param templateArray 저장할 배열이 존재 할 경우 해당 객체에 입력
 * @returns
 */
var makeKkoTemplate = function(no, template, seg, kakaoSenderKey, kakaoTmplCd, contsNo, templateArray) {
   var templateVo = {
            no: no,
            template: template,
            seg: seg,
            kakaoSenderKey: kakaoSenderKey,
            kakaoTmplCd: kakaoTmplCd,
            contsNo: contsNo
      };
   if(templateArray===undefined){
       return  $.makeArray(templateVo);
   }else{
       templateArray.push(templateVo)
   }
};
/**
 * 자리수 구하기(byte 기준이아님) 최대 길이 제한
 * fomat:text 형태로 넘길 경우 엔티티 변환 통합 필요하지 않음.
 * @param 선택자
 * @param template
 * @param 최대 길이
 */
function getLengthAndLimitLength(selector, template, maxLength) {
	var orgTemplate = template;
	//tinymce에서 자동으로 삽입되는 태그에 대해서는 byte체크 하지않음.
	template = template.replace("<br data-mce-bogus=\"1\">","")
    for(var i = 0; i < template.length; i++) {
        // 개인화가 있는경우 개인화 내용은 포함시키지 않음
        if(template.indexOf("{$") > -1) {
            var fPos = template.indexOf("{$");
            var ePos = template.indexOf("$}");
            var temp = template.substring(fPos, ePos + 2);
            template = template.replace(temp, "");
        }
    }

    var i;
    var msglen= template.length

    if(msglen > maxLength){
    	alert($.i18n.prop("template.alert.msg.reg.8",maxLength))
        tinyMCE.get("template").setContent($.mdf.cutByte(orgTemplate.replace(/\n/g, "<br/>"), maxLength));
        $("#"+selector).val(maxLength + $.i18n.prop("editor.unit.char"));
    }else{
        $("#"+selector).val(msglen + $.i18n.prop("editor.unit.char"));
    }
}

/**
 * Byte 길이 구하기, 최대 길이 제한
 * fomat:text 형태로 넘길 경우 엔티티 변환 통합 필요하지 않음.
 * @param 선택자
 * @param template
 * @param 최대 길이
 * @param 변경될 템플릿 id
 */
function getByteAndLimitByte(selector, template, maxLength, chgTextId) {
	var orgTemplate = template;
	//tinymce에서 자동으로 삽입되는 태그에 대해서는 byte체크 하지않음.
	template = template.replace("<br data-mce-bogus=\"1\">","")
	//
    for(var i = 0; i < template.length; i++) {
        // 개인화가 있는경우 개인화 내용은 포함시키지 않음
        if(template.indexOf("{$") > -1) {
            var fPos = template.indexOf("{$");
            var ePos = template.indexOf("$}");
            var temp = template.substring(fPos, ePos + 2);
            template = template.replace(temp, "");
        }
    }

    var i;
    var msglen= $.mdf.getByteLength(template);
    if(msglen > maxLength){
    	alert($.i18n.prop("template.alert.msg.reg.8",maxLength))
        if($.mdf.isNotBlank(chgTextId)){
        	$("#"+chgTextId).val($.mdf.cutByte(orgTemplate, maxLength));
        }else{
        	tinyMCE.get("template").setContent($.mdf.cutByte(orgTemplate.replace(/\n/g, "<br/>"), maxLength));
        }
        $("#"+selector).val(maxLength + "bytes");
    }else{
        $("#"+selector).val(msglen + "bytes");
    }
}
/**
 * 알림톡 선택정보를 설정
 * @param senderKey
 * @param tmplCd
 */
function setAltalkData(kakaoSenderKey, tmplCd, kakaoButtons, kakaoQuickReplies, contsNo){
    $("#kakaoSenderKey").val(kakaoSenderKey);
    $("#kakaoTmplCd").val(tmplCd);
    $("#contsNo").val(contsNo);
    removeKakaoButtons();
    removeQuickReplys();
    if($.trim(kakaoButtons).length > 2) {
        setKakaoButtons(kakaoButtons);
    }
    if($.trim(kakaoQuickReplies).length > 2) {
    	setKakaoQuickReplys(kakaoQuickReplies);
    }
}

/**
 * 아이프레임 크기 조절.
 * @param dynheight
 */
function resizeTopIframe(dynheight) {
    $("#editorIfrm").height(parseInt(dynheight));
}

function setBrandTalkButtons(json) {
    removeKakaoButtons();
    $(json).appendTo('#kakao_button_table tbody');
    //$('#kakao_button_table tbody:last').after(json);
}

function setKakaoButtons(json) {
    removeKakaoButtons();
    $("<tr id='kakaoButtonList' class='buttonlistheader'><th colspan='7'>"+$.i18n.prop('template.kakao.button')+"</th></tr>").appendTo('#kakao_button_table tbody');
    $(json).appendTo('#kakao_button_table tbody');
    //$('#kakao_button_table tbody:last').after(json);
}
function setKakaoQuickReplys(json) {
    removeQuickReplys();
    $("<tr id='kakaoButtonList' class='buttonlistheader'><th colspan='7'>바로연결</th></tr>").appendTo('#kakao_button_table tbody');
    $(json).appendTo('#quick_reply_table tbody');
    //$('#kakao_button_table tbody:last').after(json);
}
function removeKakaoButtons() {
    $('#kakao_button_table tbody #kakaoButtonList').each(function (i, tr) {
        tr.parentNode.removeChild(tr);
    });
    $('#kakao_button_table tbody #kakaoButtonSubList').each(function (i, tr) {
    	tr.parentNode.removeChild(tr);
    });
}
function removeQuickReplys() {
    $('#quick_reply_table tbody #kakaoButtonList').each(function (i, tr) {
        tr.parentNode.removeChild(tr);
    });
    $('#quick_reply_table tbody #kakaoButtonSubList').each(function (i, tr) {
    	tr.parentNode.removeChild(tr);
    });
}

/**
 * 화면로딩상태를 완료로 설정. tinyMCE를 사용하는 경우 tinyMCE에서 이 메소드를 호출.
 * @returns
 */
function loadSet() {
    loadStatus = 1;  // 화면로딩상태[0:로딩중, 1:로딩완료]
}

/**
 * 화면로딩상태 체크. 로딩중인 경우 안내 메시지 alert.
 * @returns
 */
function checkLoaded() {
    if(loadStatus == 0) {
    	alert($.i18n.prop("common.alert.save.msg1"));  // 화면이 로딩중입니다. 로딩이 완료 된 후 진행해주시기 바랍니다.
        return false;
    }
    return true;
}

//사용 여부 확인 필요
function sendOnce(channel,serviceNo,send_dtm,callBack,phoneNum,sendDt,sendTm,subject,senderKey,tmplCd,smsSndYn,contsNo,adFlag){
  var template = $("#editorFrm textarea[name=template]").val(); // 템플릿
  template = tinyMCE.get("template").getContent({ format: "text"});

  if (template.match(/[<]img[^>]+[>]/gi) != null) {
      template = template.replace(/[<]img[^>]+[>]/gi, "").replace(/^&nbsp;/,'')
      if(template.match(/^\s/,'')) {
          template = template.replace(/^\s/,'')
      }
  } else {
      contsNo = "";
  }
  if(template.length == 0){
	  alert($.i18n.prop("editor.alert.editor.20"));  // 템플릿 내용을 입력해주세요.
      return;
  }

  //SendMessageService.sendMessageOnce(channel,serviceNo,send_dtm,callBack,phoneNum,sendDt,sendTm,subject,template,senderKey,tmplCd,smsSndYn,contsNo,adFlag);
  var param = {
      channel : channel,
      serviceNo : serviceNo,
      send_dtm : send_dtm,
      callBack : callBack,
      phoneNum : phoneNum,
      sendDt : sendDt,
      sendTm : sendTm,
      subject : subject,
      template : template,
      senderKey : senderKey,
      tmplCd : tmplCd,
      smsSndYn : smsSndYn,
      contsNo : contsNo,
      adFlag : adFlag
  };

  $.post("/common/sendMessageOnce.json", $.param(param, true), function(result) {
      if(result.code == "OK") {
          parent.goSubmit();
      }
  });
}

/**
 * 저작기 저장 전 상태 체크
 * @param allSave 전체 저장: true , 에디터만 저장:false
 * @return editorSave
 */
function editorSaveHandler(allSave){
    if(editAble === false) { //ecare2stepInfo.jsp
    	alert($.i18n.prop("common.alert.save.e1_"+webExecMode));
        return false;
    }
    if(ecareSts === "C") {	//ecare2stepInfo.jsp
    	alert($.i18n.prop("common.alert.save.e2_"+webExecMode));
        return false;
    }
    // <!-- 수정 사유 입력 추가 -->
    // 핸들러 수정 이력저장 여부 확인
     if('Y'==$("#ecareFrm input[name=handlerUpdateYn]").val()) {
        // 이케어 이력 저장 레이어 팝업
        $('#layerReason').show();
        return false;
    }
    return editorSave(allSave);
}
/**
 * 전체 적용일 경우 deferred, 일부적용일 경우 바로 alert창 표시
 * @param result 데이터 적재 성공여부
 * @param allSave 전체 저장: true , 에디터만 저장:false
 * @param deferred 전체 저장에서 사용(비동기식 처리)
 * @return
 */
function ajaxCallback(result , allSave, deferred){
	  resetHandlerStat()	//handler.js 핸들러 수정사유값 초기화
      if(allSave){
           if(result.code=='OK'){
                deferred.resolve(result.code);
           }
           else{
                deferred.reject(result.message);
           }
           return deferred.promise();
       }else{
             if(result.code=='OK'){
            	 alert($.i18n.prop("ecare.2step.save"),{timeout:1000});
             }else{
            	 alert(result.message)
             }
       }
}