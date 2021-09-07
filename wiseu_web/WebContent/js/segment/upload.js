function refreshProgress() {
    //UploadMonitor.getUploadInfo(updateProgress);
    // /segment/upload/getUploadInfo.json
    $.post("/segment/getUploadInfo.json", null, function(data) {
        uploadUpdateProgress(data);
    });
}

function uploadUpdateProgress(uploadInfo) {
    if (uploadInfo.inProgress) {
        $('#tranBtn').attr("disabled", true);
        $('#file1').attr("disabled", true);

        var progressPercent = Math.ceil((uploadInfo.bytesRead / uploadInfo.totalSize) * 100);
        $('#progressBarText').html('upload in progress: ' + progressPercent + '%, transfered ' + uploadInfo.bytesRead + ' of ' + uploadInfo.totalSize + ' bytes');

        var widthMultiplier = $('#progressBarBox').innerWidth() / 100;
        $('#progressBarBoxContent').width(parseInt(progressPercent * widthMultiplier) + 'px');

        window.setTimeout('refreshProgress()', 500);
    } else {
        $('#tranBtn').attr("disabled", false);
        $('#file1').attr("disabled", false);
    }
    return true;
}

function uploadStartProgress() {
    $('#progressBarBoxContent').width('0px');
    $('#progressBarText').html('upload in progress: 0%');
    $('#tranBtn').attr("disabled", true);
    window.setTimeout("refreshProgress()", 1500);
    $('#progressBar').css("display", "block");

    return true;
}
