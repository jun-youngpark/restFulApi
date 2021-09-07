///////////////////////////////////////////////////////////////////////////////
// 날짜
var mdf = mdf || function($){};
mdf.Time = function(timeSelector, initDate, option) {
    this.init(timeSelector, initDate, option);
    this.initEventBind();
};

$.extend(mdf.Time.prototype, {
    /**
     * 생성자
     *
     * @param option 옵션
     */
    init : function(timeSelector, initTime, option) {
        this.option = $.extend(true, {
            context : '',
            timeSelector : timeSelector,
            entrySelector : timeSelector + "Entry"
        }, option || {});

        var entrySelector = this.option.entrySelector;
        var disabled = this.option.disabled ? "disabled" : "";
        var html = '<input class="form-control form-control-sm timepicker" type="text" name="' + entrySelector.substring(1) + '" id="' + entrySelector.substring(1) + '" maxlength="8" style="width:70px;">';
        $(entrySelector).remove();
        $(timeSelector).after(html);

        $(entrySelector).timeEntry({
            show24Hours : true,
            showSeconds : true,
            spinnerImage: ''
        });

        var dateValue = '';
        if($.mdf.isBlank(initTime)) {
            initTime = $(timeSelector).val();
        }

        if(initTime) {
            if(initTime.constructor === Date) {
                dateValue = $.mdf.toDateString(initTime);
            } else if(initTime.constructor === String) {
                dateValue = initTime;
            }
        }

        $(timeSelector).val(dateValue);
        $(entrySelector).timeEntry('setTime', new Date(0, 0, 0, dateValue.substring(0,2), dateValue.substring(2,4), dateValue.substring(4,6)));
    },
    /**
     * 이벤트 바인딩 초기화
     */
    initEventBind : function() {
        var timeSelector = this.option.timeSelector;
        $(this.option.entrySelector).bind('change', function() {
            $(timeSelector).val($.mdf.toTimeString($(this).timeEntry('getTime')));
        });
    },
    getTimeString : function() {
        return $(this.option.timeSelector).val();
    },
    clear : function() {
        var timeSelector = this.option.timeSelector;
        $(this.option.timeSelector).val('');
    }
});
