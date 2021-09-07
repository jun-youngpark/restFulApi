/*-------------------------------------------------------------------------------------------------
 * 이케어 저작기 실시간
-------------------------------------------------------------------------------------------------*/
/**
 * 실시간 인자값 조회
 */
var count = 0;
function getRealtimeItem() {
    count = 0;
    document.getElementById("itemList").innerHTML = "";

    for(var i = 0; i < semantic.length; i++) {
        addRealtimeItem(semantic[i].itemNm, semantic[i].itemfieldNm, semantic[i].itemVal, semantic[i].knowledgemapId);
    }

    defaultSetting();
}

/**
 * 기본설정 : 아이디, 이름, 이메일 필드 삭제금지 / 필드설명 수정금지
 */
function defaultSetting() {
    var divs = document.getElementById("itemList").childNodes;
    for(var i=0; i < 3; i++) {
        var children = divs.item(i).childNodes;
        for(var j=0; j < children.length; j++) {
            if(children.item(j).nodeName == "INPUT" && children.item(j).getAttribute("id").indexOf("itemNm") > -1) {
                children.item(j).setAttribute("readOnly", "true");
            }
            if(children.item(j).nodeName == "A" && children.item(j).getAttribute("href").indexOf("delete") > -1) {
                children.item(j).setAttribute("href", "javascript:alert('" + $.i18n.prop("editor.alert.realtime.7") + "');");  // 필수입력 필드는 삭제할 수 없습니다.
            }
        }
    }
}

/**
 * 실시간 개인인자 값을 추가 및 태그 생성
 */

function addRealtimeItem(itemNm, itemfieldNm, itemVal, knowledgemapId) {
    count++;
    if(itemNm == null || itemNm == "undefined") {
        itemNm = "";
    }

    if(itemfieldNm == null || itemfieldNm == "undefined") {
        itemfieldNm = "";
    }

    if(itemVal == null || itemVal == "undefined") {
        itemVal = "";
    }

    if(knowledgemapId == null || knowledgemapId == "undefined") {
        knowledgemapId = count;
    }

    var div = document.createElement("div");
    div.setAttribute("id", knowledgemapId);
    div.setAttribute("align", "center");

    var fieldNm = document.createElement("input");
    fieldNm.setAttribute("type", "text");
    fieldNm.setAttribute("id", "itemNm" + knowledgemapId);
    fieldNm.setAttribute("value", itemNm);
    fieldNm.setAttribute("size", "12");

    var fieldKey = document.createElement("input");
    fieldKey.setAttribute("type", "text");
    fieldKey.setAttribute("id", "itemfieldNm" + knowledgemapId);
    fieldKey.setAttribute("value", itemfieldNm);
    fieldKey.setAttribute("size", "12");

    var fieldValue = document.createElement("input");
    fieldValue.setAttribute("id", "itemVal" + knowledgemapId);
    fieldValue.setAttribute("type", "text");
    fieldValue.setAttribute("size", "30");
    fieldValue.setAttribute("value", itemVal);

    var insert;
    var remove;
    if(navigator.appName.indexOf("Microsoft") > -1) {
        insert = document.createElement("<a href='javascript:if(confirm(\""+$.i18n.prop("common.alert.3")+"\") == true) insertRealtimeItem(" + knowledgemapId + ");'>");  // 저장 하시겠습니까?
        remove = document.createElement("<a href='javascript:if(confirm(\""+$.i18n.prop("common.alert.4")+"\") == true) deleteRealtimeItem(" + knowledgemapId + ");'>");  // 삭제 하시겠습니까?
    } else {
        insert = document.createElement("a");
        insert.setAttribute("href", "javascript:if(confirm(\""+$.i18n.prop("common.alert.3")+"\") == true) insertRealtimeItem(" + knowledgemapId + ");");  // 저장 하시겠습니까?
        remove = document.createElement("a");
        remove.setAttribute("href", "javascript:if(confirm(\""+$.i18n.prop("common.alert.4")+"\") == true) deleteRealtimeItem(" + knowledgemapId + ");");  // 삭제 하시겠습니까?
    }

    var insertImg = document.createElement("img");
    insertImg.setAttribute("src", "/images"+language+"/common/search_bar_btn_save.gif");
    insert.appendChild(insertImg);

    var removeImg = document.createElement("img");
    removeImg.setAttribute("src", "/images"+language+"/common/Search_but_del.gif");
    remove.appendChild(removeImg);

    div.appendChild(document.createTextNode(count + " " + $.i18n.prop("editor.msg.realtime.1") +": "));  // 필드설명
    div.appendChild(fieldNm);
    div.appendChild(document.createTextNode(" " + $.i18n.prop("editor.msg.realtime.2") + ": "));  // 필드(인자)명
    div.appendChild(fieldKey);
    div.appendChild(document.createTextNode(" "));
    div.appendChild(insert);
    div.appendChild(document.createTextNode(" "));
    div.appendChild(remove);
    div.appendChild(document.createTextNode(" " + $.i18n.prop("editor.msg.realtime.3") + ": "));  // 필드값
    div.appendChild(fieldValue);

    var list = document.getElementById("itemList");
    list.appendChild(div);

    $("#itemScroll").attr("scrollTop", document.getElementById("itemScroll").scrollHeight);
}
/**
 * 실시간 개인인자 추가/수정
 *
 * @param knowledgemapId
 * @return
 */
function insertRealtimeItem(knowledgemapId) {

    var ecareNo = document.getElementById("ecareNo").value;
    var grpCd = document.getElementById("grpCd").value;
    var userId = document.getElementById("userId").value;
    var itemNm = document.getElementById("itemNm" + knowledgemapId).value;
    var itemfieldNm = document.getElementById("itemfieldNm" + knowledgemapId).value;
    var itemVal = document.getElementById("itemVal" + knowledgemapId).value;
    var itemType = null;

    if(itemNm == "") {
        alert($.i18n.prop("editor.alert.realtime.1"));  // 필드설명 값이 없습니다.
    } else if(itemfieldNm == "") {
        alert($.i18n.prop("editor.alert.realtime.2"));  // 필드(인자)명 값이 없습니다.
    } else if(itemVal== "") {
        alert($.i18n.prop("editor.alert.realtime.9"));  // 필드값을 입력하세요.
    } else if(hasItemName(itemfieldNm)) {
        alert($.i18n.prop("editor.alert.realtime.8"));  // 필드(인자)명이 존재합니다. 다른 필드(인자)명을 입력하세요
        document.getElementById("itemfieldNm" + knowledgemapId).value = "";
        document.getElementById("itemfieldNm" + knowledgemapId).focus();
    } else {
        // "CO" is value of ITEM_CD field into NVECAREITEM table
        var param = {"ecareNo":ecareNo , "knowledgemapId":knowledgemapId, "grpCd":grpCd, "userId":userId, "itemCd":"CO"
            , "itemNm":itemNm, "itemfieldNm":itemfieldNm, "itemType":itemType,"itemVal":itemVal};

        //sendAjaxCall("/editor/item/editor.do" , "PUT" , true , null , param , function(returnValue) {
        $.post("/editor/updateEditorEcareItem.json", $.param(param, true), function(result) {
            if(result.code == "OK" && result.value != "2" && result.value != "0") {
                // 임시로 사용한 knowledgemapId 값을 새로 추가된 인자의 knowledgemapId(returnValue) 값으로 셋팅한다.
                document.getElementById(knowledgemapId).setAttribute("id", returnValue);
                document.getElementById("itemNm" + knowledgemapId).setAttribute("id", "itemNm" + returnValue);
                document.getElementById("itemfieldNm" + knowledgemapId).setAttribute("id", "itemfieldNm" + returnValue);
                document.getElementById("itemVal" + knowledgemapId).setAttribute("id", "itemVal" + returnValue);
                var children = document.getElementById(returnValue).childNodes;
                for(var j=0; j < children.length; j++) {
                    if(children.item(j).nodeName=="A") {
                        var item = children.item(j).getAttribute("href");
                        item = item.replace(knowledgemapId, returnValue);
                        children.item(j).setAttribute("href", item);
                    }
                }

                alert($.i18n.prop("common.alert.1"));  // 저장 되었습니다.
                setSemantic(0, 'R');
            } else if(result.code == "OK" && result.value == "2") {
                alert($.i18n.prop("common.alert.5"));  // 수정되었습니다.
            } else {
                alert($.i18n.prop("editor.alert.realtime.3"));  // 이미 존재합니다.
            }
            // 20160624. 알림톡 추가. 알림톡은 개인화 셀렉트박스가 없다..
            if(document.personalizeFactor) {
                getSemantic();
            }
        });
    }
}


/**
 * 필드(인자)명 중복 확인
 */
function hasItemName(itemfieldNm) {
    var hasName = 0;
    var divs = document.getElementById("itemList").childNodes;
    for(var i=0; i < divs.length; i++) {
        var children = divs.item(i).childNodes;
        for(var j=0; j < children.length; j++) {
            if(children.item(j).nodeName=="INPUT" && children.item(j).getAttribute("id").indexOf("itemfieldNm") > -1) {
                var item = children.item(j).getAttribute("value");
                if(item == itemfieldNm) {
                    hasName++;
                    if(hasName > 1) return true;
                }
            }
        }
    }
    return false;
}

/**
 * 실시간 인자 지우기
 */
function deleteRealtimeItem(knowledgemapId) {
    var itemfieldNm = document.getElementById("itemfieldNm" + knowledgemapId).value;
    var no = document.getElementById("ecareNo").value;

    var param = {"ecareNo":ecareNo , "itemfieldNm":itemfieldNm};

    // EditorEcareService.deleteEditorEcareItem(no, itemfieldNm, {
    // sendAjaxCall("/editor/item/editor.do" , "DELETE" , true , null , param , function(returnValue) {
    $.post("/editor/deleteEditorEcareItem.json", $.param(param, true), function(result) {
        if(result.code == "OK" && result.rowCount == 1) {
            var item = document.getElementById(knowledgemapId);
            item.parentNode.removeChild(item);
            alert($.i18n.prop("common.alert.2"));  // 삭제 되었습니다.
            setSemantic(0, 'R');
        } else {
            var item = document.getElementById(knowledgemapId);
            item.parentNode.removeChild(item);
        }
    });
}

/**
 * 실시간 연동 JSP 코드 생성
 * LTS 가 바뀌는 내용에 따라 수정될 수 있으니 확인해봐야 한다.
 */
function makeRealtimeSource() {
    var list = document.getElementById("itemList");
    var source = "";
    var items = "";
    var itemsCheck = "";
    var itemsArg = "";
    var itemsData = "";
    var itemsNull = "";
    var item = "";
    var key = "";

    var itemArr = new Array();

    var divs = list.childNodes;
    for(var i = 0; i < divs.length; i++) {
        var innerDiv = divs.item(i).childNodes;

        for(var j = 0; j < innerDiv.length; j++) {
            if(innerDiv.item(j).nodeName == "INPUT"
                && innerDiv.item(j).getAttribute("id").indexOf("itemfieldNm") > -1) {
                item = innerDiv.item(j).getAttribute("value");
            }

            if(innerDiv.item(j).nodeName == "SELECT") {
                var options = innerDiv.item(j).childNodes;

                for(var k = 0; k < options.length; k++) {
                    if(options.item(k).getAttribute("selected") == true) {
                        key = options.item(k).getAttribute("value");
                    }
                }
            }

            if(item != "" && key != "") {
                itemArr[itemArr.length] = {
                    item : item,
                    key : key
                };

                item = key = "";
            }
        }
    }

    // LTS 연동 코드 생성
    for(var i = 0; i < itemArr.length; i++) {
        if(itemArr[i].item == "") continue;

        items += '\n\tString ' + itemArr[i].item.toLowerCase() + ' = request.getParameter("' + itemArr[i].item + '");';

        itemsCheck += '\n\t\tif(' + itemArr[i].item + ' == null) throw new Exception("parameter ' + itemArr[i].item + ' is null");';

        if(itemArr[i].key == "content") {
            itemsData += '\n\t\tsRet = tc.setData("' + itemArr[i].item + '", ' + itemArr[i].item.toLowerCase() + ');';
            itemsData += '\n\t\tif (sRet == null || !sRet.startsWith("250")) throw new Exception("mehtod TcpipClient.setData() error : " + sRet);';
              itemsData += '\n\t\tif (bDebug) out.println(sRet);';
        } else {
            itemsArg += '\n\t\tsRet = tc.setArg("' + itemArr[i].item + '", ' + itemArr[i].item.toLowerCase() + ');';
            itemsArg += '\n\t\tif (sRet == null || !sRet.startsWith("250")) throw new Exception("mehtod TcpipClient.setArg() error : " + sRet);';
            itemsArg += '\n\t\tif (bDebug) out.println(sRet);';
            itemsArg += '\n';
        }

        itemsNull += '\n\t\t' + itemArr[i].item.toLowerCase() + ' = null;';
    }

    source += '<%@ page import="java.io.*,com.mnwise.lts.*" %>';
    source += '\n<%';
    source += items;
    source += '\n\tboolean bDebug = false;';
    source += '\n\tString sRet = null;';
    source += '\n\tTcpipClient tc = null;';
    source += '\n';
    source += '\n\ttry {';
    source += itemsCheck;
    source += '\n';
    source += '\n\t\ttc = new TcpipClient();';
    source += '\n';
    source += '\n\t\tsRet = tc.open("127.0.0.1", 9100);';
    source += '\n\t\tif (sRet == null || !sRet.startsWith("220")) throw new Exception("mehtod TcpipClient.open() error : " + sRet);';
    source += '\n\t\tif (bDebug) out.println(sRet);';
    source += '\n';
    source += '\n\t\tsRet = tc.setAID("1");';
    source += '\n\t\tif (sRet == null || !sRet.startsWith("250")) throw new Exception("mehtod TcpipClient.setAID() error : " + sRet);';
    source += '\n\t\tif (bDebug) out.println(sRet);';
    source += '\n';
    source += itemsArg;
    if(itemsData != "") {
        source += itemsData;
        source += '\n';
    }
    source += '\n\t\tsRet = tc.commit();';
    source += '\n\t\tif (sRet == null || !sRet.startsWith("250")) throw new Exception("mehtod TcpipClient.commit() error : " + sRet);';
    source += '\n\t\tif (bDebug) out.println(sRet);';
    source += '\n';
    source += '\n\t\tout.println("job succeeded");';
    source += '\n';
    source += '\n\t} catch (Exception e) {';
    source += '\n\t\tout.println("following error occurred.");';
    source += '\n\t\tout.println(e.toString());';
    source += '\n\t} finally {';
    source += '\n\t\ttry { if (tc != null) tc.quit(); } catch (Exception e) {}';
    source += '\n\t\ttc = null;';
    source += '\n\t\tsRet = null;';
    source += itemsNull;
    source += '\n\t}';
    source += '\n%>';

    document.getElementById("source").value = source;
}
