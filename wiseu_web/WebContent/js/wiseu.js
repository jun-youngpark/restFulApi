$(document).ready(function () {
    initGlobalEventBind();
    initGlobalPage();
});

function initGlobalEventBind() {
    // 닫기 이미지 클릭
    $("#closeImg").on("click", function(event) {
        window.close();
    });

    // input file 라벨에 파일이름 추가
    $('input[type=file]').on('change', function(event) {
        if (window.Filereder) { // modern browser
            var filename = $(this)[0].files[0].name;
        } else { // oldIE
            var filename = $(this).val().split('/').pop().split('\\').pop();
        }
        $(this).next('.custom-file-label').text(filename);
    })
}

function initGlobalPage() {
    // button type 설정(엔터키 입력시 submit 방지)
    $(':button').prop("type", "button");

    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            if (settings.type == 'POST' || settings.type == 'PUT' || settings.type == 'DELETE') {
                xhr.setRequestHeader('X-CSRF-Token', $("meta[name='_csrf']").attr("content"));
            }
        }
    });

    $(document).ajaxStart(function(event) {
        $.mdf.openLoading();
    });

    $(document).ajaxStop(function(event) {
        $.mdf.closeLoading();
    });

    $(document).ajaxComplete(function(event) {
        $.mdf.closeLoading();
    });

}

