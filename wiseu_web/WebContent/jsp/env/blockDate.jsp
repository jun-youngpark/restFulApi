<%-------------------------------------------------------------------------------------------------
  * - [환경설정>공휴일 관리] 공휴일 관리 <br/>
  * - URL : /env/blockDate.do <br/>
  * - Controller :com.mnwise.wiseu.web.env.web.EnvBlockDateController <br/>
  * - 이전 파일명 : env_blockdate.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="env.msg.offday"/></title>
<script type="text/javascript">
    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        var dt = new Date();
        var currentYear = dt.getFullYear();
        var currentDate = new Date();

        $('#datepicker_year').datepicker({
            defaultDate:new Date(${currentYear}, 1 - 1, 1),
            dayNames : [
                "<spring:message code='env.menu.offday.sun'/>", "<spring:message code='env.menu.offday.mon'/>", "<spring:message code='env.menu.offday.tue'/>", "<spring:message code='env.menu.offday.wed'/>",
                "<spring:message code='env.menu.offday.thu'/>", "<spring:message code='env.menu.offday.fri'/>", "<spring:message code='env.menu.offday.sat'/>"
            ],
            dayNamesShort : [
                "<spring:message code='env.menu.offday.sun'/>", "<spring:message code='env.menu.offday.mon'/>", "<spring:message code='env.menu.offday.tue'/>", "<spring:message code='env.menu.offday.wed'/>",
                "<spring:message code='env.menu.offday.thu'/>", "<spring:message code='env.menu.offday.fri'/>", "<spring:message code='env.menu.offday.sat'/>"
            ],
            dayNamesMin : [
                "<spring:message code='env.menu.offday.sun'/>", "<spring:message code='env.menu.offday.mon'/>", "<spring:message code='env.menu.offday.tue'/>", "<spring:message code='env.menu.offday.wed'/>",
                "<spring:message code='env.menu.offday.thu'/>", "<spring:message code='env.menu.offday.fri'/>", "<spring:message code='env.menu.offday.sat'/>"
            ],
            monthNames : [
                "<spring:message code='env.menu.offday.january'/>", "<spring:message code='env.menu.offday.february'/>", "<spring:message code='env.menu.offday.march'/>", "<spring:message code='env.menu.offday.april'/>",
                "<spring:message code='env.menu.offday.may'/>", "<spring:message code='env.menu.offday.june'/>", "<spring:message code='env.menu.offday.july'/>", "<spring:message code='env.menu.offday.august'/>",
                "<spring:message code='env.menu.offday.september'/>", "<spring:message code='env.menu.offday.october'/>", "<spring:message code='env.menu.offday.november'/>", "<spring:message code='env.menu.offday.december'/>"
            ],
            monthNamesShort : [
                "<spring:message code='env.menu.offday.sun'/>", "<spring:message code='env.menu.offday.mon'/>", "<spring:message code='env.menu.offday.tue'/>", "<spring:message code='env.menu.offday.wed'/>",
                "<spring:message code='env.menu.offday.thu'/>", "<spring:message code='env.menu.offday.fri'/>", "<spring:message code='env.menu.offday.sat'/>"
            ],
            monthNames : [
                " <spring:message code='env.menu.offday.january'/>", "<spring:message code='env.menu.offday.february'/>", "<spring:message code='env.menu.offday.march'/>", "<spring:message code='env.menu.offday.april'/>",
                "<spring:message code='env.menu.offday.may'/>", "<spring:message code='env.menu.offday.june'/>", "<spring:message code='env.menu.offday.july'/>", "<spring:message code='env.menu.offday.august'/>",
                "<spring:message code='env.menu.offday.september'/>", "<spring:message code='env.menu.offday.october'/>", "<spring:message code='env.menu.offday.november'/>", "<spring:message code='env.menu.offday.december'/>"
            ],
            showMonthAfterYear:true,
            changeMonth:false,
            changeYear:false,
            prevText:"<spring:message code='env.menu.offday.prevyear'/>",  // 전년
            nextText:"<spring:message code='env.menu.offday.nextyear'/>",  // 후년
            numberOfMonths: [3, 4],
            beforeShowDay:changeHoliDays,
            stepMonths:12,
            onSelect : function(value) {
                sendDate(value);
            },
            onChangeMonthYear:function(value) {
                $("#currentYear").val(value);
                // 주말발송제한 정보 조회
                $.post("/env/selectWeekendBlockDateInfo.json?year=" + value, null, function(data) {

                });
            }
        });
    }

    function regComplete(data) {
        alert("<spring:message code='env.alert.offday.reg'/>");  // 성공적으로 등록되었습니다.
        document.location.replace("/env/blockDate.do?currentYear=" + $("#currentYear").val());  // /env/blockdate.do
    }

    function delComplete(data) {
        alert("<spring:message code='env.alert.offday.del'/>");  // 성공적으로 삭제되었습니다.
        document.location.reload("/env/blockDate.do?currentYear=" + $("#currentYear").val());  // /env/blockdate.do
    }

    var date = "";
    function sendDate(value) {
        date = value.split("-");
        var param = {
            chkYear : date[0],
            blockDt : date[1] + date[2]
        };

        $.post("/env/confirmRestDays.json", $.param(param, true), function(result) {  // 공휴일 등록가능여부 확인
            if(result.code == "OK") {
                if(confirm(value + "<spring:message code='env.alert.offday.reg1'/>")) {  // 공휴일 등록
                    $.post("/env/insertRestDays.json", $.param(param, true), function(result) {
                        if(result.code == "OK") {
                            alert(value + "<spring:message code='env.alert.offday.reg2'/>");  // 일이 공휴일로 등록되었습니다.
                            document.location.reload();
                        }
                    });
                }
            } else {
                if(confirm(value + "<spring:message code='env.alert.offday.del1'/>")) {  // 공휴일 해제
                    $.post("/env/deleteRestDays.json", $.param(param, true), function(result) {
                        if(result.code == "OK") {
                            alert(value + "<spring:message code='env.alert.offday.del2'/>");  // 일의 공휴일이 해제되었습니다.
                            document.location.reload();
                        }
                    });
                }
            }
        });
    }


    var holiDays = [${restDay}];

    function changeHoliDays(date) {
        var currentYear = date.getFullYear();

        for(var i = 0; i < holiDays.length; i++) {
            if(currentYear == holiDays[i][0] && date.getMonth() == holiDays[i][1] - 1 && date.getDate() == holiDays[i][2]) {
                return [true, 'ui-state-block'];
            }
        }
        return [true, ''];
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="env.msg.offday"/></h3><!-- 공휴일 관리 -->
            </div>
            <div class="card-body">
                <div id="datepicker_year"></div>
            </div>
        </div>
    </div>
</div>

<form id="envFrm" name="envFrm" action="/env/blockDate.do" method="post"><!-- /env/blockdate.do -->
    <input type="hidden" name="cmd" value="update"/>
    <input type="hidden" name="seqNo" value="1"/>
    <input type="hidden" name="userId" value="${envSenderInfoVo.userId}"/>
    <input type="hidden" id="currentYear" name="currentYear" />
</form>

</body>
</html>