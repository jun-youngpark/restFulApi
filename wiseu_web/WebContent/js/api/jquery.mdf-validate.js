function initValidator() {
    // /////////////////////////////////////////////////////////////////////////////
    // 기본 메시지 다국어 처리
    $.extend($.validator.messages, {
        required: $.i18n.prop("valid.required"),  // 필수 항목입니다.
        remote: $.i18n.prop("valid.remote"),  // 항목을 수정하세요.
        email: $.i18n.prop("valid.email"),  // 유효하지 않은 이메일주소입니다.
        url: $.i18n.prop("valid.url"),  // 유효하지 않은 URL입니다.
        date: $.i18n.prop("valid.date"),  // 올바른 날짜를 입력하세요.
        dateISO: $.i18n.prop("valid.dateISO"),  // 올바른 날짜(ISO)를 입력하세요.
        number: $.i18n.prop("valid.number"),  // 유효한 숫자가 아닙니다.
        digits: $.i18n.prop("valid.digits"),  // 숫자만 입력 가능합니다.
        creditcard: $.i18n.prop("valid.creditcard"),  // 신용카드 번호가 바르지 않습니다.
        equalTo: $.i18n.prop("valid.equalTo"),  // 같은 값을 다시 입력하세요.
        extension: $.validator.format($.i18n.prop("valid.extension")),  // 올바른 확장자가 아닙니다.
        maxlength: $.validator.format($.i18n.prop("valid.maxlength")),  // {0}자를 넘을 수 없습니다.
        minlength: $.validator.format($.i18n.prop("valid.minlength")),  // {0}자 이상 입력하세요.
        rangelength: $.validator.format($.i18n.prop("valid.rangelength")),  // 문자 길이가 {0} 에서 {1} 사이의 값을 입력하세요.
        range: $.validator.format($.i18n.prop("valid.range")),  // {0} 에서 {1} 사이의 값을 입력하세요.
        max: $.validator.format($.i18n.prop("valid.max")),  // {0} 이하의 값을 입력하세요.
        min: $.validator.format($.i18n.prop("valid.min"))  // {0} 이상의 값을 입력하세요.
    });

    // /////////////////////////////////////////////////////////////////////////////
    // 커스텀 유효성 체크 메소드 추가
    // 영문 대문자 포함여부 체크
    jQuery.validator.addMethod("containsUpperCase", function(value, element) {
        return this.optional(element) || $.mdf.containsUpperCase(value);
    }, $.i18n.prop("valid.containsUpperCase"));  // 영문 대문자를 포함해야 합니다.

    // 영문 소문자 포함여부 체크
    jQuery.validator.addMethod("containsLowerCase", function(value, element) {
        return this.optional(element) || $.mdf.containsLowerCase(value);
    }, $.i18n.prop("valid.containsLowerCase"));  // 영문 소문자를 포함해야 합니다.

    // 숫자 포함여부 체크
    jQuery.validator.addMethod("containsDigit", function(value, element) {
        return this.optional(element) || $.mdf.containsDigit(value);
    }, $.i18n.prop("valid.containsDigit"));  // 숫자를 포함해야 합니다.

    // 특수문자 포함여부 체크
    jQuery.validator.addMethod("containsSpecialChar", function(value, element) {
        return this.optional(element) || $.mdf.containsSpecialChar(value);
    }, $.i18n.prop("valid.containsSpecialChar"));  // 특수문자를 포함해야 합니다.

    // 영문, 숫자 체크
    jQuery.validator.addMethod("alphaDigit", function(value, element) {
        return this.optional(element) || $.mdf.isAlphaDigit(value);
    }, $.i18n.prop("valid.alphaDigit"));  // 영문과 숫자만 입력 할 수 있습니다.

    // 영문, 숫자, 특수문자('_') 체크
    jQuery.validator.addMethod("alphaDigitUnderscore", function(value, element) {
        return this.optional(element) || $.mdf.isAlphaDigitUnderscore(value);
    }, $.i18n.prop("valid.alphaDigitUnderscore"));  // 영문과 숫자만 입력 할 수 있습니다.

    // 최대 바이트수 체크
    jQuery.validator.addMethod("maxbyte", function(value, element, param) {
        return this.optional(element) || ($.mdf.getByteLength(value) <= param);
    }, $.i18n.prop("valid.maxbyte"));  // {0}byte를 넘을 수 없습니다.

    // 입력값 not blank 여부
    jQuery.validator.addMethod("notBlank", function(value, element) {
        var result = $.mdf.isNotBlank(value);
        if(result == false) {
            $(element).val($.trim(value));
        }
        return result;
    }, $.i18n.prop("valid.required"));  // 필수 항목입니다.

    jQuery.validator.addMethod("noSpaceFilename", function(value, element) {
        return $.mdf.defaultString(value).indexOf(" ") == -1;
    }, $.i18n.prop("valid.noSpaceFilename"));  // 파일명은 공백을 포함할 수 없습니다.

    // 콤보박스 선택여부 체크
    jQuery.validator.addMethod("selected", function(value, element) {
        return $(element).find("option:selected").index() > 0;
    }, $.i18n.prop("valid.selected"));  // 항목을 선택하세요.

    // 전화번호 체크
    jQuery.validator.addMethod("telnum", function(value, element){
        return /^[0-9]{2,3}[-]*[0-9]{3,4}[-]*[0-9]{4}$/.test(value) ? true : this.optional(element) || false;
    }, $.i18n.prop("valid.telnum"));  // 유효하지 않는 전화번호입니다.

    // 수신거부 전화번호 체크
    jQuery.validator.addMethod("dnd", function(value, element){
        return /^080-[0-9]{3,4}-[0-9]{4}$/.test(value) ? true : this.optional(element) || false;
    }, $.i18n.prop("valid.dnd"));  // 유효하지 않는 수신거부 전화번호입니다. (특수문자 - 포함, 080-으로 시작)
}
