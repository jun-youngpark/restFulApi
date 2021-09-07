jQuery(function($) {
    //  datepicker
    $(".datepicker").datepicker({
        beforeShow: function(input) {
            setTimeout(function(){
               $('#ui-datepicker-div').css({'position':'', 'top':'', 'bottom':'', 'left':'', 'z-index':''});
            })
        },
        changeMonth: true, // 월 선택 콤보박스 표시
        changeYear: true, // 년 선택 콤보박스 표시
        dateFormat: 'yy-mm-dd',
        showMonthAfterYear: true, // 년,월 순으로 표시
        yearSuffix: "",
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNames: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        monthNamesShort: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        showOtherMonths: true,
        currentText: '오늘 날짜' , // 오늘 날짜로 이동하는 버튼 패널
        closeText: '닫기'  // 닫기 버튼 패널
    });
});
