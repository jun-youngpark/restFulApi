/*--------------------------------------------------------------
 * 캠페인/이케어 저작기 2단계 핸들러 내용이 첨가되거나 수정
----------------------------------------------------------------*/

function resetHandlerStat(){
	$("#ecareFrm input[name=handlerUpdateYn]").val('N');
    $('[name=historyPopupMsg]').val(' ');
    $('[name=historyPopupMsg]').val(' ');
}
/**
 * 핸들러 수정모드 전환
 * @param flag true:수정모드, false:기본핸들러
 * @returns
 */
function toggleEditMode(flag) {
    if(flag) {
        $("#editorFrm textarea[name=handler]").css("background", "#fff").prop("readonly", false);
        $("#editorFrm input[name=templateType]").val(11);  // HTML+핸들러수정
        // ecare만.. handlerAlert3 변수가 없으면 적용 안되도록 수정.
        var msg = $.i18n.prop("editor.alert.handler.3");  // 핸들러 수정 시 수정 이력을 기재해야 합니다.
        if($.mdf.isNotBlank($("#editorFrm input[name=serviceType]").val()) && $('#handlerAlert3').length==0 && 'undefined' != typeof(msg)) {
            $('#handlerSwich').parent().parent().parent().append("<div id='handlerAlert3' class='text-warning font-weight-bold d-inline'> " + msg + '</div>');
        }
    } else {
        $("#editorFrm textarea[name=handler]").css("background", "#fafafa").prop("readonly", true);
        $("#editorFrm input[name=templateType]").val(1);  // HTML
        if($('#handlerAlert3').length > 0){
            $('#handlerAlert3').remove();
        }
    }
}

/**
 * setMailFrom 반송메일
 * setFrom 마임 보낸사람
 * setTo 마임 받을사람
 * setRcpt 받을사람
 * setCallback SMS 콜백
 * 핸들러 설정
 */
function resetHanlder(handler) {
    var map = new Map();
    for(var i = 0; i < semantic.length; i++) {
        if($("#editorFrm input[name=serviceType]").val() == "R") {
            map.put(semantic[i].itemType, semantic[i].itemfieldNm);
        } else {
            if($.trim(semantic[i].fieldKey) != "") {
                if(semantic[i].fieldKey == "A" // SLOT1(OPTION)
                    || semantic[i].fieldKey == "B" // SLOT2(OPTION)
                    || semantic[i].fieldKey == "X" // SEND_NAME(OPTION)
                    || semantic[i].fieldKey == "Y" // SEND_EMAIL(OPTION)
                    || semantic[i].fieldKey == "Z" // RETURN_MAIL(OPTION)
                    || semantic[i].fieldKey == "C") { // CALLBACK_NO(OPTION)
                    map.put(semantic[i].fieldKey, semantic[i].fieldNm);
                } else {
                    map.put(semantic[i].fieldKey, semantic[i].fieldSeq);
                }
            }
        }
    }

    // A/B 테스트 사용 일 경우 setRcpt매개변수 변경 하지 않음
    var con = abTestType;
    if(con != "N") return handler;

    var channel = ($("#editorFrm input[name=channel]").val() == "M") ? "E" : $("#editorFrm input[name=channel]").val();
    var target = "target.getString(";
    var context = "context.get(";

    if($("#editorFrm input[name=serviceType]").val() == "R") {
        target = context;
    }

    var handlerLine = new Line(handler);
    var reHandler = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("mailagent.setMailFrom(") > -1) { // Retrun 메일 다르게 설정
            reHandler += line.substring(0, line.indexOf("mailagent.setMailFrom("));
            reHandler += "mailagent.setMailFrom(";
            reHandler += map.get("Z") != null ? target : context;
            reHandler += "\"";
            reHandler += map.get("Z") == null ? "_RETMAIL_RECEIVER" : map.get("Z");
            reHandler += "\"))";
        } else if(line.indexOf("body.setFrom(") > -1) { // From 이름, 메일 다르게 설정
            reHandler += line.substring(0, line.indexOf("body.setFrom("));
            reHandler += "body.setFrom(";
            reHandler += map.get("X") != null ? target : context;
            reHandler += "\"";
            reHandler += map.get("X") == null ? "_SENDER_NM" : map.get("X");
            reHandler += "\"), ";
            reHandler += map.get("Y") != null ? target : context;
            reHandler += "\"";
            reHandler += map.get("Y") == null ? "_SENDER_EMAIL" : map.get("Y");
            reHandler += "\"))";
        } else if(line.indexOf("body.setTo(") > -1) { // To
            reHandler += line.substring(0, line.indexOf("body.setTo("));
            reHandler += "body.setTo(";
            reHandler += target;
            reHandler += map.get("N");
            reHandler += "), ";
            reHandler += target;
            reHandler += map.get(channel);
            reHandler += "))";
        } else if(line.indexOf("mailagent.setRcpt(") > -1) { // Rcpt
            reHandler += line.substring(0, line.indexOf("mailagent.setRcpt("));
            reHandler += "mailagent.setRcpt(";
            reHandler += target;
            reHandler += map.get(channel);
            reHandler += "), ";
            reHandler += target;
            reHandler += map.get("K");
            reHandler += "), ";
            reHandler += target;
            reHandler += map.get("N");
            reHandler += "), ";
            reHandler += "\"0\"";

            if(map.get("A") != null) {
                reHandler += ", ";
                reHandler += target;
                reHandler += "\"";
                reHandler += map.get("A");
                reHandler += "\")";
            }

            if(map.get("B") != null) {
                reHandler += ", ";
                reHandler += target;
                reHandler += "\"";
                reHandler += map.get("B");
                reHandler += "\")";
            }

            reHandler += ")";
        } else {
            reHandler += line;
        }

        reHandler += "\n";
    }

    return reHandler;
}


/**
 * setMailFrom 반송메일
 * setFrom 마임 보낸사람
 * setTo 마임 받을사람
 * setRcpt 받을사람
 * setCallback SMS 콜백
 * 핸들러 설정
 */
function resetPhoneHanlder(handler) {
    var map = new Map();
    for(var i = 0; i < semantic.length; i++) {
        if($("#editorFrm input[name=serviceType]").val() == "R") {
            map.put(semantic[i].itemType, semantic[i].itemfieldNm);
        } else {
            if($.trim(semantic[i].fieldKey) != "") {
                if(semantic[i].fieldKey == "A"
                    || semantic[i].fieldKey == "B"
                    || semantic[i].fieldKey == "X"
                    || semantic[i].fieldKey == "Y"
                    || semantic[i].fieldKey == "Z"
                    || semantic[i].fieldKey == "C") {
                    map.put(semantic[i].fieldKey, semantic[i].fieldNm);
                } else {
                    map.put(semantic[i].fieldKey, semantic[i].fieldSeq);
                }
            }
        }
    }

    var channel = $("#editorFrm input[name=channel]").val();
    var target = "target.getString(";
    var context = "context.get(";

    if($("#editorFrm input[name=serviceType]").val() == "R") {
        target = context;
    }

    var phoneAgentHandlerString = "";
    var callbackNoHandlerString = "";
    if(channel == 'S') {
        callbackNoHandlerString = "smsagent.setCallback(";
        phoneAgentHandlerString = "smsagent.setRcpt(";
    }else if(channel == 'T'){
        callbackNoHandlerString = "mmsagent.setCallbackNo(";
        phoneAgentHandlerString = "mmsagent.setRcpt(";
    }
    //친구톡,알림톡 핸들러 - 대상자 선택(등록) 후 핸들러 내용 설정
    else if(channel == 'C'){
        callbackNoHandlerString = "mmsagent.setCallbackNo(";
        phoneAgentHandlerString = "mmsagent.setRcpt(";
    }else if(channel == 'B'){
        callbackNoHandlerString = "mmsagent.setCallbackNo(";
        phoneAgentHandlerString = "mmsagent.setRcpt(";
    }

    var handlerLine = new Line(handler);
    var reHandler = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf(callbackNoHandlerString) > -1) { // 콜백 번호를 다르게
            reHandler += line.substring(0, line.indexOf(callbackNoHandlerString));
            reHandler += callbackNoHandlerString;
            reHandler += map.get("C") != null ? target : context;
            reHandler += "\"";
            reHandler += map.get("C") == null ? "_SENDER_TEL" : map.get("C");
            reHandler += "\"))";
        } else if(line.indexOf(phoneAgentHandlerString) > -1) { // Rcpt
            reHandler += line.substring(0, line.indexOf(phoneAgentHandlerString));
            reHandler += phoneAgentHandlerString;
            reHandler += target;
            reHandler += map.get("S");
            reHandler += "), ";
            reHandler += target;
            reHandler += map.get("K");
            reHandler += "), ";
            reHandler += target;
            reHandler += map.get("N");
            reHandler += ")";

            if(channel == 'T') { // MMS인 경우 rcpt에 recordseq를 인자로 넘겨야 함. (sms는 되어있지 않음)
                reHandler += ",\"0\"";
            }

            if(map.get("A") != null) {
                reHandler += ", ";
                reHandler += target;
                reHandler += "\"";
                reHandler += map.get("A");
                reHandler += "\")";
            }

            if(map.get("B") != null) {
                reHandler += ", ";
                reHandler += target;
                reHandler += "\"";
                reHandler += map.get("B");
                reHandler += "\")";
            }

            reHandler += ")";
        } else {
            reHandler += line;
        }

        reHandler += "\n";
    }

    return reHandler;
}

/**
 * 기본핸들러 관련 처리를 위한 Object
 */
var DefaultHandlerManager = {
    // 최초 실행 체크
    isFirst: true,
    // 초기화 실행여부
    runInit: false,
    // 서비스구분 EM, EC
    serviceGubun: 'EM',
    // 서비스 번호
    serviceNo: 0
};

/**
 * 핸들러 목록
 * @param handlerSeq
 * @param isHandlerEdit 핸들러 수정 여부
 * @returns
 */
DefaultHandlerManager.initHandlerList = function(handlerSeq, isHandlerEdit){
    // 기본핸들러 번호를 가져와서 저장. 0이면 선택된게 없다.
    $("#handlerSeq").val( handlerSeq );

    //DefaultHandle.getHandlerUseList(DefaultHandlerManager.serviceGubun, DefaultHandlerManager.serviceNo, {
    //getAjaxCall("/env/"+ DefaultHandlerManager.serviceGubun + "/"+DefaultHandlerManager.serviceNo+"/deafulthandler.do", function(handlerInfo) {
    var param = {
        serviceGubun : DefaultHandlerManager.serviceGubun,
        serviceNo : DefaultHandlerManager.serviceNo
    };

    $.post("/env/getHandlerUseList.do", $.param(param, true), function(handlerInfo) {
        if("error" == handlerInfo) {
            DefaultHandlerManager.runInit = true;
            alert($.i18n.prop("editor.alert.handler.2"));  // 기본핸들러를 가져오는 도중 에러가 발생하였습니다.
        } else if(null==handlerInfo.list || 'undefined'==typeof(handlerInfo.list) || ''==handlerInfo.list){
            alert($.i18n.prop("editor.alert.editor.1"));  // 기본핸들러가 등록되지 않았습니다.\\n[환경설정] - [기본핸들러설정] 에서 기본핸들러를 등록하십시오.
            //dwr.util.addOptions("handlerList", [{"SEQ":-1, "HANDLE_NM":DefaultHandlerManager.message.noHandler}], "SEQ", "HANDLE_NM")
            $("#handlerList").append("<option value='-1'>" + $.i18n.prop("editor.option.handler.0") + "</option>");  // 핸들러 없음
        } else {
            //dwr.util.addOptions("handlerList", handlerInfo.list, "SEQ", "HANDLE_NM");
            $(handlerInfo.list).each(function(i, row) {
                $("#handlerList").append("<option value='"+ row.seq +"'>" + row.handleNm + "</option>");
            });
            if(0 < handlerInfo.handlerSeq){
                $("#handlerList").val(handlerInfo.handlerSeq);
            }
        }

        $('#handlerSwich').change(function() {
            var isHandlerEdit = $(this).prop('checked');
            if(isHandlerEdit){
                alert($.i18n.prop("editor.alert.handler.5"));  // 핸들러 수정이 가능합니다.
                  toggleEditMode(true);
            }else{
                alert($.i18n.prop("editor.alert.handler.4"));  // 작성된 내용이 모두 삭제되고 선택된 기본핸들러가 적용됩니다.
                DefaultHandlerManager.loadHandler($("#handlerList").val(), true);
                toggleEditMode(false);
            }
        });

        $("#handlerList").change(function(){
            DefaultHandlerManager.loadHandler($("#handlerList").val());
        });

        // 템플릿타입이 11 이면 핸들러 수정모드로 설정
        if(isHandlerEdit) {
            toggleEditMode(true);
        } else {
            toggleEditMode(false);
        }

        DefaultHandlerManager.runInit = true;
    });
};

/**
 * 핸들러 불러오기
 * @param handlerSeq
 * @param init
 * @returns
 */
DefaultHandlerManager.loadHandler = function(handlerSeq, init){
    if(null==handlerSeq || 'undefined'==typeof(handlerSeq) || ''==handlerSeq || 'undefined'==handlerSeq ){
    } else if(handlerSeq < 1) {
        if(handlerSeq==0){
            toggleEditMode(true);
        }
    } else {
        //DefaultHandle.loadHandler(handlerSeq,{
        //getAjaxCall("/env/"+handlerSeq+"/loadhandler.do" , function(envHandler) {
        var param = {
            seq : handlerSeq
        };

        $.post("/env/loadHandler.json", $.param(param, true), function(envHandler) {
            if("error" == envHandler) {
                alert($.i18n.prop("editor.alert.handler.2"));  // 기본핸들러를 가져오는 도중 에러가 발생하였습니다.
            } else if(init){ // init 시 confirm 제외.
                $("#handler").val(envHandler.handler)
                if($("#handlerList").val()!=envHandler.seq){
                    $("#handlerList").val(envHandler.seq);
                }
                toggleEditMode(false);
            } else {
                if(confirm($.i18n.prop("editor.alert.handler.1")+"\n"+envHandler.handleNm+"("+envHandler.handleDesc+")")){  // 기본 핸들러를 적용하시겠습니까?
                    $("#handler").val(envHandler.handler)
                    if($("#handlerList").val()!=envHandler.seq){
                        $("#handlerList").val(envHandler.seq);
                    }
                    toggleEditMode(false);
                }
            }
        });
    }
};

/**
 * handler 로드시 handlerList 완전 생성까지 기다리고 기본핸들러 처리까지.
 */
DefaultHandlerManager.setDefaultHandler = function(){
    if(DefaultHandlerManager.runInit){
        var handlerSeq = $("select[name=handlerList]").val();
        if(handlerSeq < 0){
            alert($.i18n.prop("editor.alert.editor.1"));  // 기본핸들러가 등록되지 않았습니다.\\n[환경설정] - [기본핸들러설정] 에서 기본핸들러를 등록하십시오.
        } else {
            DefaultHandlerManager.loadHandler(handlerSeq, true);
            tmp_handler = $("#editorFrm textarea[name=handler]").val();
            getSlot(tmp_handler);
        }
    } else {
        setTimeout(DefaultHandlerManager.setDefaultHandler,500);
    }
}