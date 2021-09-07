///////////////////////////////////////////////////////////////////////////////
// 날짜
var mdf = mdf || function($){};
mdf.Date = function(dateSelector, initDate, option) {
    this.init(dateSelector, initDate, option);
    this.initEventBind();
};

$.extend(mdf.Date.prototype, {
    /**
     * 생성자
     *
     * @param option 옵션
     */
    init : function(dateSelector, initDate, option) {
        this.option = $.extend(true, {
            context : '',
            dateSelector : dateSelector,
            type : "date",
            language : Cookies.get('lang'),
            showToday : true,
            input : {},
            usageStatistics : false,
            weekStartDay : "Mon",
            calendarSelector : dateSelector + "Calendar",
            wrapperSelector : dateSelector + "Wrap"
        }, option || {});

        var calendarSelector = this.option.calendarSelector;
        var wrapperSelector = this.option.wrapperSelector;

        var html = '<input type="text" id="' + calendarSelector.substring(1) + '"  class="form-control form-control-sm datepicker" />';
        $(calendarSelector).parent().remove();
        $(wrapperSelector).remove();
        $(dateSelector).after(html);
        $('<div id="' + wrapperSelector.substring(1) + '"></div>').appendTo($('body'));

        var dateValue = '';
        if($.mdf.isBlank(initDate)) {
            initDate = $(dateSelector).val();
        }

        if(initDate) {
            if(initDate.constructor === Date) {
                switch(this.option.minViewMode) {
                case "date" :  // 년월일
                    dateValue = $.mdf.toDateString(initDate);
                    break;
                case "month" : // 년월
                    dateValue = $.mdf.toDateString(initDate);
                    dateValue = dateValue.substring(0, 6);
                    break;
                case "year" : // 년
                    dateValue = $.mdf.toDateString(initDate);
                    dateValue = dateValue.substring(0, 4);
                    break;
                }
            } else if(initDate.constructor === String) {
                dateValue = initDate;
            }
        }

        $(dateSelector).val(dateValue);

        if($.mdf.isNotBlank(dateValue)) {
            this.option.date = $.mdf.toDate(dateValue);
        }

        var type = this.option.type;

        if(type == "date") {
            this.option.input.format = 'yyyy-MM-dd';
        } else if(type == "month") {
            this.option.input.format = 'yyyy-MM';
        } else if(type == "year") {
            this.option.input.format = 'yyyy';
        }
        this.option.input.element = calendarSelector;
        var datePicker = new tui.DatePicker(wrapperSelector, this.option);
        datePicker.on('change', function() {
            var dateString = $.mdf.toDateString(datePicker.getDate());
            if(type == "date") {
                $(dateSelector).val(dateString);
            } else if(type == "month") {
                $(dateSelector).val(dateString.substring(0,6));
            } else if(type == "year") {
                $(dateSelector).val(dateString.substring(0,4));
            }
        });
    },
    /**
     * 이벤트 바인딩 초기화
     */
    initEventBind : function() {
        var wrapperSelector = this.option.wrapperSelector;
        $(this.option.calendarSelector).on('click', function() {
            var pos = $(this).offset();
            $(wrapperSelector).css({
                position : 'absolute',
                top : pos.top + 32,
                left : pos.left
            });
        });
    },
    getDateString : function() {
        return $(this.option.dateSelector).val();
    },
    clear : function() {
        var dateSelector = this.option.dateSelector;
        $(this.option.dateSelector).val('');
        $(this.option.calendarSelector).val('');
    }
});

///////////////////////////////////////////////////////////////////////////////
// 다국어 처리
tui.DatePicker.localeTexts['vn'] = {
    titles: {
        // days
        DD: ['CN', 'Hai', 'Ba', 'Tư', 'Năm', 'Sáu', 'Bảy'],
        // daysShort
        D: ['CN', 'Hai', 'Ba', 'Tư', 'Năm', 'Sáu', 'Bảy'],
        // months
        MMMM: [
            'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
            'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
        ],
        // monthsShort
        MMM: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12']
    },
    titleFormat: 'yyyy.MM',
    todayFormat: 'hôm nay: yyyy.MM.dd (D)',
    date: 'ngày tháng',
    time: 'thời gian'
};