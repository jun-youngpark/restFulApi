/**
 * 옴니채널 마케팅용 하위 캠페인을 생성한다.
 * 캠페인 리포트에서 사용하던 재발송 기능을 최대한 활용하여 개발함.
 *
 * @param scenarioNo 상위 캠페인의 시나리오 번호
 * @param campaignNo 상위 캠페인 번호
 * @param relationType 하위 캠페인의 재발송 유형
 *        'S': 성공, 'F': 실패, 'O': 오픈
 * @returns
 */
function makeSubCampaign(scenarioNo, campaignNo, relationType) {
    if(!confirm($.i18n.prop("campaign.confirm.make.campaign"))) {  // 캠페인을 생성하시겠습니까?
        return;
    }

    var channel = $('#select_channel_' + relationType).val();

    var form = $('<form>', {action : '/campaign/resend.do', method : 'post'});
    form.append($('<input>', {type: 'hidden', name: 'campaignNo',  value: campaignNo}));
    form.append($('<input>', {type: 'hidden', name: 'scenarioNo',  value: scenarioNo}));
    form.append($('<input>', {type: 'hidden', name: 'resendMode',  value: relationType + '_' + getSendMode(relationType) + '_' + channel}));
    form.append($('<input>', {type: 'hidden', name: 'channelType',  value: channel}));
    form.append($('<input>', {type: 'hidden', name: 'depthNo',  value: $("#depthNo").val()}));

    form.appendTo('body').submit();
}

/**
 * Send Mode 값을 리턴한다.
 * 캠페인 재발송 로직을 그대로 활용하기 위해 문구를 유지함.
 *
 * @param relationType 'S': 성공, 'F': 실패, 'O': 오픈
 * @returns
 */
function getSendMode(relationType) {
    if (relationType === 'S') {
        return 'resendSuccess';
    } else if(relationType === 'F') {
        return 'resendFail';
    } else if(relationType === 'O') {
        return 'resendOpen';
    }
}