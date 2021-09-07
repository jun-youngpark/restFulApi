/*-------------------------------------------------------------------------------------------------
 * 캠페인/이케어 저작기 2단계 옵션 처리
-------------------------------------------------------------------------------------------------*/
/**
 * 핸들러 가져올 때, SLOT1/SLOT2 값 유무 확인
 */
var optionValue = "";
function getSlot(handler) {
    var handlerLine = new Line(handler);

    var index = -1;
    var offset = -1;
    var line = "";
    while((line = handlerLine.readLine()) != null) {
        // SLOT1, SLOT2
        if(line.indexOf("mailagent.setRcpt(") > -1) {
            index = line.indexOf("mailagent.setRcpt(") + "mailagent.setRcpt(".length;
            offset = line.length - 1;

            var rcptArr = line.substring(index, offset).split(",");

            if(rcptArr.length < 5) {
                $("#slot1Value").val("");
                $("#slot2Value").val("");
            } else {
                for(var i = 0; i < rcptArr.length; i++) {
                    // SLOT1
                    if(i == 4) {
                        index = rcptArr[i].indexOf("(\"") + 2;
                        offset = rcptArr[i].indexOf("\")");
                        $("#slot1Value").val(rcptArr[i].substring(index, offset));
                    } else if(i == 5) { // SLOT2
                        index = rcptArr[i].indexOf("(\"") + 2;
                        offset = rcptArr[i].indexOf("\")");
                        $("#slot2Value").val(rcptArr[i].substring(index, offset));
                    }
                }
            }
        } else if(line.indexOf("mailagent.setMailFrom(") > -1) { // 반송메일
            if(line.indexOf("_RETMAIL_RECEIVER") > -1) {
                $("#retmail").val("");
            } else {
                if($("#editorFrm input[name=serviceType]").val() == "R") {
                    index = line.indexOf("mailagent.setMailFrom(context.get(\"") + "mailagent.setMailFrom(context.get(\"".length;
                } else {
                    index = line.indexOf("mailagent.setMailFrom(target.getString(\"") + "mailagent.setMailFrom(target.getString(\"".length;
                }
                offset = line.length - 3;
                $("#retmail").val(line.substring(index, offset));
            }
        } else if(line.indexOf("body.setFrom(") > -1) { // 발신이름, 발신메일
            index = line.indexOf("body.setFrom(") + "body.setFrom(".length;
            offset = line.length - 1;

            var senderArr = line.substring(index, offset).split(",");
            for(var i = 0; i < senderArr.length; i++) {
                if(i == 0) { // 발신이름
                    if(line.indexOf("_SENDER_NM") > -1) {
                        $("#senderNm").val("");
                    } else {
                        if($("#editorFrm input[name=serviceType]").val() == "R") {
                            index = senderArr[i].indexOf("context.get(\"") + "context.get(\"".length;
                        } else {
                            index = senderArr[i].indexOf("target.getString(\"") + "target.getString(\"".length;
                        }
                        offset = senderArr[i].length - 2;
                        $("#senderNm").val(senderArr[i].substring(index, offset));
                    }
                } else if(i == 1) { // 발신메일
                    if(line.indexOf("_SENDER_EMAIL") > -1) {
                        $("senderEmail").val("");
                    } else {
                        if($("#editorFrm input[name=serviceType]").val() == "R") {
                            index = senderArr[i].indexOf("context.get(\"") + "context.get(\"".length;
                        } else {
                            index = senderArr[i].indexOf("target.getString(\"") + "target.getString(\"".length;
                        }
                        offset = senderArr[i].length - 2;

                        $("senderEmail").val(senderArr[i].substring(index, offset));
                    }
                }
            }
        } else if(line.indexOf("body.setCharset(") > -1) { // 다국어
            $('#charset option').each(function(){
                if(line.indexOf(this.value) > -1) {
                    $(this).attr("selected", true);
                    return;
                }
            });
        }
    }
}

/**
 * 핸들러 setRcpt 메서드 매개변수의 SLOT1/SLOT2 제거
 */
function removeSlot(seq) {
    var handler = $("#editorFrm textarea[name=handler]").val();
    var handlerLine = new Line(handler);
    var reHandler = "";

    var index = -1;
    var offset = -1;
    var line = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("mailagent.setRcpt(") > -1) {
            index = line.indexOf("mailagent.setRcpt(") + "mailagent.setRcpt(".length;
            offset = line.length - 1;

            reHandler += "  mailagent.setRcpt(";

            var rcptArr = line.substring(index, offset).split(",");

            if(seq == 1 && rcptArr.length != 5) {
                alert($.i18n.prop("editor.alert.option.1"));  // SLOT1이 설정되어 있지 않거나 SLOT2가 설정되어 있습니다.
                return;
            } else if(seq == 2 && rcptArr.length != 6) {
                alert($.i18n.prop("editor.alert.option.2"));  // SLOT2이 설정되어 있지 않습니다.
                return;
            }

            if(seq == 1) { // slot1
                $("#slot1Value").val("");
            } else { // slot2
                $("#slot2Value").val("");
            }

            for(var i = 0; i < rcptArr.length - 1; i++) {
                if(i == (rcptArr.length - 2)) {
                    reHandler += rcptArr[i] + ")\n";
                } else {
                    reHandler += rcptArr[i] + ",";
                }
            }
        } else {
            reHandler += line + "\n";
        }
    }

    // SLOT1/SLOT2 제거된 핸들러
    $("#editorFrm textarea[name=handler]").val(reHandler);
}

/**
 * 핸들러 setRcpt 메서드 매개변수에 SLOT1/SLOT2 추가
 */
function addSlot(seq) {
    var handler = $("#editorFrm textarea[name=handler]").val();
    var handlerLine = new Line(handler);
    var reHandler = "";

    if(optionValue == null || optionValue == "") {
        alert($.i18n.prop("editor.alert.option.3"));  // 개인화정보가 없습니다.
        return;
    }

    var index = -1;
    var offset = -1;
    var line = "";
    var space = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("mailagent.setRcpt(") > -1) {
            index = line.indexOf("mailagent.setRcpt(") + "mailagent.setRcpt(".length;
            offset = line.length - 1;
            space = line.substring(0, line.indexOf("mailagent.setRcpt("));

            reHandler += space + "mailagent.setRcpt(";

            var rcptArr = line.substring(index, offset).split(",");

            if(seq == 2 && rcptArr.length < 5) {
                alert($.i18n.prop("editor.alert.option.4"));  // SLOT1이 설정되어 있지 않습니다.
                return;
            }

            if((seq == 1 && rcptArr.length >= 5)
                    || (seq == 2 && rcptArr.length >= 6)) {
                alert($.i18n.prop("editor.alert.option.5") + " [SLOT" + seq + "]");  // SLOT이 설정되어 있습니다.
                return;
            }

            if(seq == 1) { // slot1
                $("#slot1Value").val(optionValue);
            } else { // slot2
                $("#slot2Value").val(optionValue);
            }

            for(var i = 0; i < rcptArr.length; i++) {
                reHandler += rcptArr[i] + ",";
            }

            if($("#editorFrm input[name=serviceType]").val() == "R") {
                reHandler += " context.get(\"";
            } else {
                reHandler += " target.getString(\"";
            }

            reHandler += optionValue + "\"))\n";
        } else {
            reHandler += line + "\n";
        }
    }

    $("#editorFrm textarea[name=handler]").val(reHandler);
}

/**
 * 보내는사람 이름, 이메일 추가
 */
function sender(condition, subCondition) {
    var handler = $("#editorFrm textarea[name=handler]").val();
    var handlerLine = new Line(handler);
    var reHandler = "";

    var line = "";
    var isName = subCondition === 'name';
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("body.setFrom(") > -1) {

            if(isName) {
                var headWord = line.substring(0, line.indexOf('body.setFrom(') + 13);
                var tailWord = line.substring(line.indexOf(","));
            } else {
                var headWord = line.substring(0, line.indexOf(',') + 1);
                var tailWord = line.substring(line.indexOf("))") + 1);
            }

            // 보낸이름
            if(($("#editorFrm input[name=serviceType]").val()  == "R") || condition === 'remove') {
                var newWord = "context.get(\"";
            } else {
                var newWord = "target.getString(\"";
            }

            if(isName) {
                if(condition == 'add') {
                    if(optionValue == null || optionValue == "") {
                        newWord += "_SENDER_NM";
                        $("#senderNm").val("");
                    } else {
                        newWord += optionValue;
                        $("#senderNm").val(optionValue);
                    }
                } else {
                    newWord += "_SENDER_NM";
                    $("#senderNm").val("");
                }
            } else {
                if(condition == 'add') {
                    if(optionValue == null || optionValue == "") {
                        newWord += "_SENDER_EMAIL";
                        $("senderEmail").val("");
                    } else {
                        newWord += optionValue;
                        $("senderEmail").val(optionValue);
                    }
                } else {
                    newWord += "_SENDER_EMAIL";
                    $("senderEmail").val("");
                }
            }
            newWord += '\")'
            reHandler += headWord + newWord + tailWord + "\n";
        } else {
            reHandler += line + "\n";
        }
    }

    $("#editorFrm textarea[name=handler]").val(reHandler);
}

/**
 * 반송메일 추가
 */
function retmailReceiver(condition, val) {
    var handler = $("#editorFrm textarea[name=handler]").val();
    var handlerLine = new Line(handler);
    var reHandler = "";

    if(optionValue == null || optionValue == "") {
        alert($.i18n.prop("editor.alert.option.6"));  // 반송메일 값이 없습니다.
        return;
    }

    var line = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("mailagent.setMailFrom(") > -1) {
            reHandler += "mailagent.setMailFrom(";

            if(($("#editorFrm input[name=serviceType]").val() == "R") || condition === 'remove') {
                reHandler += "context.get(\"";
            } else {
                reHandler += "target.getString(\"";
            }

            if(condition == 'add') {
                reHandler += optionValue;
                $("#retmail").val(optionValue);
            } else {
                reHandler += "_RETMAIL_RECEIVER";
                $("#retmail").val("");
            }
            reHandler += "\"))\n";
        } else {
            reHandler += line + "\n";
        }
    }

    $("#editorFrm textarea[name=handler]").val(reHandler);
}

/**
 * 옵션 보기/감추기
 */
function option() {
    if($("#option").hasClass("dp-none")) {
        $("#option").removeClass("dp-none");
    } else {
        $("#option").addClass("dp-none");
        var h = parent.$("#editorIfrm").height();
        parent.$("#editorIfrm").height(h - 256);  // 옵션 출력 높이을 뺀다
    }

    $.mdf.resizeIframe("#editorIfrm");
}

/**
 * SMS 옵션 값이 설정되어 있는지 확인한다.
 */
function addSmsOption(seq) {
    var result = false;

    if(optionValue != "") {
        if(seq == 1) {
            if((result = addSmsSlot(seq)) == false) {
                alert($.i18n.prop("editor.alert.option.7"));  // 이미 로그부가정보1이 설정되어 있습니다.
            } else {
                $("#slot1Value").val(optionValue);
            }
        } else if(seq == 2) {
            if((result = addSmsSlot(seq)) == false) {
                alert($.i18n.prop("editor.alert.option.8"));  // 로그부가정보1이 설정되어 있지 않거나 이미 로그부가정보2가 설정되어 있습니다.
            } else {
                $("#slot2Value").val(optionValue);
            }
        } else if(seq == 3) {
            if((result = addSmsSlot(seq)) == false) {
                alert($.i18n.prop("editor.alert.option.9"));  // 이미 콜백번호가 설정되어 있습니다.
            } else {
                $("#senderSms").val(optionValue);
            }
        }
    } else {
        alert($.i18n.prop("editor.alert.option.3"));  // 개인화정보가 없습니다.
    }
}

/**
 * SMS 옵션 SLOT1/SLOT2/CALLBACK 값을 설정한다.
 */
function addSmsSlot(seq) {
    var handlerLine = new Line($("textarea[name=handler]").val());
    var reHandler = new String();

    var line = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("agent.setRcpt(") > -1 && (seq == 1 || seq == 2)) {
            var index = line.indexOf("agent.setRcpt(") + "agent.setRcpt(".length;
            var offset = line.length - 1;
            var lineArr = new Array();
            lineArr = line.substring(index, offset).split(",");

            if(seq == 1 && lineArr.length == 4) {
                reHandler += line.substring(0, offset);

                if($("#editorFrm input[name=serviceType]").val() == "R") {
                    reHandler += ", context.get(\"";
                } else {
                    reHandler += ", target.getString(\"";
                }

                reHandler += optionValue + "\"))\n";
            } else if(seq == 2 && lineArr.length == 5) {
                reHandler += line.substring(0, offset);

                if($("#editorFrm input[name=serviceType]").val() == "R") {
                    reHandler += ", context.get(\"";
                } else {
                    reHandler += ", target.getString(\"";
                }

                reHandler += optionValue + "\"))\n";
            } else {
                return false;
            }
        } else if(line.indexOf("smsagent.setCallback(") > -1 && seq == 3) {
            reHandler += line.substring(0, "smsagent.setCallback(".length);
            reHandler += "context.get(\"" + optionValue + "\"))\n";
        } else if(line.indexOf("mmsagent.setCallbackNo(") > -1 && seq == 3) {
            reHandler += line.substring(0, "mmsagent.setCallbackNo(".length);
            reHandler += "context.get(\"" + optionValue + "\"))\n";
        } else {
            reHandler += line + "\n";
        }
    }

    $("textarea[name=handler]").val(reHandler);
    return true;
}

/**
 * SMS 옵션 SLOT1/SLOT2/CALLBACK 값을 제거한다.
 */
function removeSmsOption(seq) {
    var result = false;

    if(optionValue != "") {
        if(seq == 1) {
            if((result = removeSmsSlot(seq)) == false) {
                alert($.i18n.prop("editor.alert.option.10"));  // 로그부가정보1이 설정되어 있지 않거나 로그부가정보2가 설정되어 있습니다.
            } else {
                $("#slot1Value").val("");
            }
        } else if(seq == 2) {
            if((result = removeSmsSlot(seq)) == false) {
                alert($.i18n.prop("editor.alert.option.11"));  // 로그부가정보2가 설정되어 있지 않습니다.
            } else {
                $("#slot2Value").val("");
            }
        } else if(seq == 3) {
            if((result = removeSmsSlot(seq)) == false) {
                alert($.i18n.prop("editor.alert.option.12"));  // 콜백번호가 설정되어 있지 않습니다.
            } else {
                $("#senderSms").val("");
            }
        }
    } else {
        alert($.i18n.prop("editor.alert.option.3"));  // 개인화정보가 없습니다.
    }
}

/**
 * SMS 옵션 SLOT1/SLOT2/CALLBACK 값을 제거한다.
 */
function removeSmsSlot(seq) {
    var handlerLine = new Line($("textarea[name=handler]").val());
    var reHandler = new String();

    var line = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("agent.setRcpt(") > -1
            && (seq == 1 || seq == 2)) {
            var index = line.indexOf("agent.setRcpt(") + "agent.setRcpt(".length;
            var offset = line.length - 1;
            var lineArr = new Array();
            lineArr = line.substring(index, offset).split(",");

            if((seq == 1 && lineArr.length == 5) ||
                (seq == 2 && lineArr.length == 6)) {
                for(var i = 0; i < lineArr.length - 1; i++) {
                    if(i == lineArr.length - 2) {
                        reHandler += lineArr[i];
                    } else if(i == 0) {
                        reHandler += line.substring(0, index) + lineArr[i] + ", ";
                    } else {
                        reHandler += lineArr[i] + ", ";
                    }
                }
                reHandler += ")\n";
            } else {
                return false;
            }
        } else if(line.indexOf("smsagent.setCallback(") > -1 && seq == 3) {
            reHandler += line.substring(0, "agent.setCallback(".length);
            reHandler += "context.get(\"_SENDER_TEL\"))\n";
        } else if(line.indexOf("mmsagent.setCallbackNo(") > -1 && seq == 3) {
            reHandler += line.substring(0, "mmsagent.setCallbackNo(".length);
            reHandler += "context.get(\"_SENDER_TEL\"))\n";
        } else {
            reHandler += line + "\n";
        }
    }

    $("textarea[name=handler]").val(reHandler);
    return true;
}

function getSpace(str) {
    var space = "";

    for(var i = 0; i < str.length; i++) {
        if(str.charCodeAt(i) == 32) space += " ";
        else break;
    }

    return space;
};

/**
 * 설정된 다국어 선택
 */
function selectCharset(obj) {
    // UTF-8(UTF-8/BASE64) 의 형태로 들어가서 '(' 부터 ')' 까지안의 문자열의 / 구분자로 (인코딩타입/캐릭셋)
    // 이었으나 다른형태의 데이터 있어서 encodingVal 를 추가하여 처리한다..
    var encodingStr = obj[obj.selectedIndex].text;
    var encodingVal = obj[obj.selectedIndex].value;
    var encodings = null;

    var handler = $("textarea[name=handler]").val();
    var reHandler = "";
    var handlerLine = new Line(handler);
    var charset = "";
    var contentTransferEncoding = "base64";

    encodingStr = encodingStr.substring(encodingStr.indexOf("(") + 1, encodingStr.length - 1);
    encodings = encodingStr.split("/");

    charset = encodingVal;
    //encodings[1] 가 없는경우에 디폴트값 사용하도록 변경한다.
    contentTransferEncoding = (encodings[1]!=null)?encodings[1]:contentTransferEncoding;

    var line = "";
    while((line = handlerLine.readLine()) != null) {
        if(reHandler.length > 0) reHandler += "\r\n";

        if(line.indexOf("body.setEncodingType") > -1) {
            reHandler += getSpace(line) + "body.setEncodingType(\"" + contentTransferEncoding + "\")";
        } else if(line.indexOf("body.setCharset") > -1) {
            reHandler += getSpace(line) + "body.setCharset(\"" + charset + "\")";
        } else {
            reHandler += line;
        }
    }

    $("textarea[name=handler]").val(reHandler);
}

/**
 * eCare 반응추적기간 처리용
 */
function setEcareTraceTerm(termType){
    var handlerLine = new Line($("textarea[name=handler]").val());
    var reHandler = new String();

    var line = "";
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("addterm = term.addTime(dt,") > -1){
            var temp ="";
            switch(termType){
            case "1": temp ="7"; break;
            case "2": temp ="14"; break;
            case "3": temp ="21"; break;
            case "4": temp ="28"; break;
            }

            reHandler += "addterm = term.addTime(dt,\"" + temp + "\",\"00\",\"00\",\"00\")\n" + handlerLine.toString() + "\n";  // 남은핸들러 내용 추가
            break;
        } else {
            reHandler += line+"\n";
        }
    } // end of while

    $("textarea[name=handler]").val(reHandler);
}