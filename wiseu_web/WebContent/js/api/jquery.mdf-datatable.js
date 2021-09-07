///////////////////////////////////////////////////////////////////////////////
// 데이타 테이블
var mdf = mdf || function($){};
mdf.DataTable = function(selector, option) {
    this.init(selector, option);
};

$.extend(mdf.DataTable.prototype, {
    /**
     * 생성자
     *
     * @param option 옵션
     */
    init : function(selector, option) {
        this.option = {
            ordering : false,  // 정렬 숨기기
            lengthChange: false,  // 표시 건수 숨기기
            searching: false,  // 검색 숨기기
            info: false,  // 정보표시 숨기기
            paging: false,  // 페이징 숨기기
            language : {
                emptyTable : $.i18n.prop("common.msg.nodata")  // 검색결과가 없습니다.
            }
        };

        this.option = $.extend(true, this.option, option || {});

        $(selector).DataTable(this.option);
    }
});
