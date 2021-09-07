    function refreshCsvProgress() {
        //CSVMonitor.getInsertedCntInfo(updateProgress);
        // /segment/upload/getInsertedCntInfo.json
        $.post("/segment/getInsertedCntInfo.json", null, function(data) {
            csvUpdateProgress(data);
        });
    }

    function csvUpdateProgress(insertedCntInfo) {
        if (insertedCntInfo.inProgress) {
            var totalCnt = insertedCntInfo.totalCnt;
            var insertedCnt = insertedCntInfo.insertedCnt;
            var progressPercent = Math.ceil((insertedCnt / totalCnt) * 100);
            $('#insertProgress').progressBar(progressPercent);
            //window.setTimeout('refreshCsvProgress()', 500);
        }
        window.setTimeout('refreshCsvProgress()', 500);
        return true;
    }

    function csvUploadstartProgress() {
        window.setTimeout('refreshCsvProgress()', 500);
        return true;
    }