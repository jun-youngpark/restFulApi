    function refreshSendProgress() {
        //SendMonitor.getSendInfo(updateProgress);
        $.post("/ecare/getSendInfo.json", null, function(data) {
            sendUpdateProgress(data);
        });
    }

    function sendUpdateProgress(sendInfo) {
        if (sendInfo.inProgress) {
            var totalCnt = sendInfo.totalCnt;
            var insertedCnt = sendInfo.insertedCnt;
            var progressPercent = Math.ceil((insertedCnt / totalCnt) * 100);
            $('#sendInsertProgress').progressBar(progressPercent);
            //window.setTimeout('refreshSendProgress()', 500);
        }
        window.setTimeout('refreshSendProgress()', 500);
        return true;
    }

    function sendStartProgress() {
        window.setTimeout('refreshSendProgress()', 500);
        return true;
    }