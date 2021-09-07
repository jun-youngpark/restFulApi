// 대상자 리스트/검색 팝업
function popupTargetList(segmentNo, segType, segmentSize, serviceType) {
    var url = '/segment/targetListPopup.do?segmentNo=' + segmentNo;  // /segment/segmentpopup.do

    if($.mdf.isNotBlank(segType)) {
        url += '&segType=' + segType;
    }

    if($.mdf.isNotBlank(segmentSize)) {
        url += '&segmentSize=' + segmentSize;
    }
    if($.mdf.isNotBlank(serviceType)) {
    	url += '&serviceType=' + serviceType;
    }

    $.mdf.popupGet(url, 'targetListPopup', 900, 735);
}

// 대상자 선택 팝업
function popupTargetSelect(type, segmentNo, channelType, serviceType, serviceNo) {
    // 크롬 업데이트로 showModalDialog 함수 사라짐
    // /bookmark/bookmark_popup.do
    var url = "/segment/segmentSelectPopup.do?bookmarkKind=F&type=" + type + "&segmentNo=" + segmentNo + "&channelType=" + channelType +"&serviceNo=" +serviceNo;

    if($.mdf.isNotBlank(serviceType)) {
        url += '&serviceType=' + serviceType;
    }

    $.mdf.popupGet(url,'targetSelectPopup', 850, 705);
}

// 대상자 등록 팝업
function popupTargetRegist() {
    // 크롬 업데이트로 showModalDialog 함수 사라짐
    var url = "/target/fileSegment1Step.do?tabUse=Y&tabType=F";  // /segment/target/upload.do

    $.mdf.popupGet(url,'targetRegistPopup', 980, 830);
}


// 쿼리 체크
function checkTargetQuery(dbInfoSeq, query) {
    var param = {
        dbInfoSeq : dbInfoSeq ,
        query : query
    };
    $.post("/segment/checkTargetQuery.json", $.param(param, true), function(result) {
        if(result.code = "OK") {
            alert(result.value);
            if(result.value != null && result.value.indexOf("Success") > -1) {
                return true;
            }
        } else {
            alert(result.message);
        }

        return false;
    });
}