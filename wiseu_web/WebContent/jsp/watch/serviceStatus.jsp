<%-------------------------------------------------------------------------------------------------
 * [wiseWatch>캠페인 탭] 서비스 상태
 * [wiseWatch>이케어 탭] 서비스 상태
 * - URL : /watch/serviceStatus.do
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchController
 * 하나의 캠페인 서비스 모니터링
 * - 이전 파일명 : watch_info_service.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        getInfoService();
    }

    function getInfoService() {
        $.post("/watch/getServiceInfo.json?tid=${tid}", function(service) {
            var sumTotalCount = 0;
            var sumMadeCount = 0;
            var sumSendCount = 0;
            var sumSuccessCount = 0;
            var sumUnknownUserCount = 0;
            var sumUnknownHostCount = 0;
            var sumSmtpExceptCount = 0;
            var sumNoRouteCount = 0;
            var sumRefusedCount = 0;
            var sumEtcExceptCount = 0;
            var sumInvalidAddressCount = 0;

            var mtsCount = 0;

            // watch - 웹과 TMS연결이 가끔끊겨 에러발생하는거 alert안뿌리도록 임시로 try ~ catch로 묶는다(자바스크립트)
            var serviceLength = 0;
            try {
                serviceLength = service.length;
            } catch(e) {
            }
            // serverId 별로 서비스 정보 가져온다.
            for(var i = 0; i < serviceLength; i++) {
                if(service[i].serverName == "LTS") {
                    $("#ltsServiceStatus").html(getServiceStatus(service[i].serviceStatus));
                    $("#targetCount").html(service[i].targetCount);

                } else if(service[i].serverName == "MTS") {
                    if($.mdf.outerHTML("#" + service[i].serverId) == null) {
                        $("#" + service[i].serverName).append(getMtsHtml(service[i].serverId, service[i].startTime, service[i].retry,service[i].channel));
                    }

                    $("#" + service[i].serverId + "serviceStatus").html(getServiceStatus(service[i].serviceStatus));
                    $("#" + service[i].serverId + "totalCount").html(service[i].totalCount);
                    $("#" + service[i].serverId + "madeCount").html(service[i].madeCount);
                    $("#" + service[i].serverId + "sendCount").html(service[i].sendCount);
                    $("#" + service[i].serverId + "successCount").html(service[i].successCount);
                    $("#" + service[i].serverId + "unknownUserCount").html(service[i].unknownUserCount);
                    $("#" + service[i].serverId + "unknownHostCount").html(service[i].unknownHostCount);
                    $("#" + service[i].serverId + "smtpExceptCount").html(service[i].smtpExceptCount);
                    $("#" + service[i].serverId + "noRouteCount").html(service[i].noRouteCount);
                    $("#" + service[i].serverId + "refusedCount").html(service[i].refusedCount);
                    $("#" + service[i].serverId + "etcExceptCount").html(service[i].etcExceptCount);
                    $("#" + service[i].serverId + "invalidAddressCount").html(service[i].invalidAddressCount);
                    $("#" + service[i].serverId + "errorMsg").html(service[i].errorMsg);

                    sumTotalCount += service[i].totalCount;
                    sumMadeCount += service[i].madeCount;
                    sumSendCount += service[i].sendCount;
                    sumSuccessCount += service[i].successCount;
                    sumUnknownUserCount += service[i].unknownUserCount;
                    sumUnknownHostCount += service[i].unknownHostCount;
                    sumSmtpExceptCount += service[i].smtpExceptCount;
                    sumNoRouteCount += service[i].noRouteCount;
                    sumRefusedCount += service[i].refusedCount;
                    sumEtcExceptCount += service[i].etcExceptCount;
                    sumInvalidAddressCount += service[i].invalidAddressCount;

                    mtsCount++;
                    $("#server" + mtsCount).html(service[i].serverId);
                    $("#status" + mtsCount).html(getServiceStatus(service[i].serviceStatus));
                }
            }

            if(mtsCount > 1) {
                $("#sumTotalCount").html(sumTotalCount);
                $("#sumMadeCount").html(sumMadeCount);
                $("#sumSendCount").html(sumSendCount);
                $("#sumSuccessCount").html(sumSuccessCount);
                $("#sumUnknownUserCount").html(sumUnknownUserCount);
                $("#sumUnknownHostCount").html(sumUnknownHostCount);
                $("#sumSmtpExceptCount").html(sumSmtpExceptCount);
                $("#sumNoRouteCount").html(sumNoRouteCount);
                $("#sumRefusedCount").html(sumRefusedCount);
                $("#sumEtcExceptCount").html(sumEtcExceptCount);
                $("#sumInvalidAddressCount").html(sumInvalidAddressCount);

                $("#sumMts").css("display", "block");
            }

            setTimeout(getInfoService, 15 * 1000);
        });
    }

    function getMtsHtml(serverId, startTime, retry, channel) {
        var mtsHtml = "";
        //mtsHtml += '<tr><td bgcolor="#f8f8f8" colspan="15" style="height: 10px;"></td></tr>';
        mtsHtml += '<tr class="thead-light">';
        mtsHtml += '    <th scope="col" rowspan="2" id="' + serverId + '" width="8%">' + serverId + '</th>';
        mtsHtml += '    <th scope="col"><spring:message code="watch.menu.date"/></th>';  // 날짜
        mtsHtml += '    <th scope="col"><spring:message code="watch.menu.count.stotal"/></th>';  // 발송 합계
        mtsHtml += '    <th scope="col"><spring:message code="watch.menu.count.total"/></th>';  // 총건수
        mtsHtml += '    <th scope="col"><spring:message code="watch.menu.count.new"/></th>';  // 생성건수
        mtsHtml += '    <th scope="col"><spring:message code="watch.menu.re"/></th>';  // 재시도
        mtsHtml += '    <th scope="col" class="text-blue"><spring:message code="watch.menu.count.send"/></th>';  // 발송건수
        mtsHtml += '    <th scope="col" class="text-green"><spring:message code="watch.menu.count.success"/></th>';  // 성공건수
        if(channel=='M') {
            mtsHtml += '    <th scope="col">UnknownUser</th>';
            mtsHtml += '    <th scope="col">UnknownHost</th>';
            mtsHtml += '    <th scope="col">SmtpExcept</th>';
            mtsHtml += '    <th scope="col">Noroute</th>';
            mtsHtml += '    <th scope="col">Refused</th>';
            mtsHtml += '    <th scope="col">EtcExcept</th>';
            mtsHtml += '    <th scope="col">InvalidAddress</th>';
        }
        mtsHtml += '</tr>';
        mtsHtml += '<tr>';
        mtsHtml += '    <td >' + startTime + '</td>';
        mtsHtml += '    <td id="' + serverId + 'serviceStatus" ></td>';
        mtsHtml += '    <td id="' + serverId + 'totalCount" ></td>';
        mtsHtml += '    <td id="' + serverId + 'madeCount" ></td>';
        mtsHtml += '    <td >' + retry +'</td>';
        mtsHtml += '    <td id="' + serverId + 'sendCount" class="text-blue"></td>';
        mtsHtml += '    <td id="' + serverId + 'successCount" class="text-green"></td>';
        if(channel=='M') {
            mtsHtml += '    <td id="' + serverId + 'unknownUserCount" ></td>';
            mtsHtml += '    <td id="' + serverId + 'unknownHostCount" ></td>';
            mtsHtml += '    <td id="' + serverId + 'smtpExceptCount" ></td>';
            mtsHtml += '    <td id="' + serverId + 'noRouteCount" ></td>';
            mtsHtml += '    <td id="' + serverId + 'refusedCount" ></td>';
            mtsHtml += '    <td id="' + serverId + 'etcExceptCount" ></td>';
            mtsHtml += '    <td id="' + serverId + 'invalidAddressCount" ></td>';
        }
        mtsHtml += '</tr>';
        mtsHtml += '<tr class="thead-light">';
        mtsHtml += '    <th scope="col"><spring:message code="watch.menu.error"/></th>';  // 에러내용
        mtsHtml += '    <td id="' + serverId + 'errorMsg" colspan="14"></td>';
        mtsHtml += '</tr>';

        return mtsHtml;
    }

    function getServiceStatus(serviceStatus) {
        var status = "";

        switch(serviceStatus) {
        case "R" : status = "<spring:message code='campaign.status.R'/>"; break;  // 발송대기
        case "W" : status = "<spring:message code='campaign.status.W'/>"; break;  // 발송중
        case "E" : status = "<spring:message code='campaign.status.E'/>"; break;  // 발송종료
        case "O" : status = "<spring:message code='campaign.status.O'/>"; break;  // 발송에러
        case "P" : status = "<spring:message code='campaign.status.P'/>"; break;  // 보류
        case "S" : status = "<spring:message code='campaign.status.S'/>"; break;  // 일시정지
        case "V" : status = "<spring:message code='campaign.status.V'/>"; break;  // 분할정지
        case "T" : status = "<spring:message code='campaign.status.T'/>"; break;  // 발송중단
        case "I" : status = "<spring:message code='campaign.status.I'/>"; break;  // 작성중
        case "D" : status = "<spring:message code='campaign.status.D'/>"; break;  // 승인거부
        case "C" : status = "<spring:message code='campaign.status.C'/>"; break;  // 승인요청
        default : status = "<spring:message code='watch.menu.no'/>";  // 알수없음
        }

        return status;
    }
</script>

<h1 class="h3 text-primary mb-1"><spring:message code='watch.menu.status2' /></h1><!-- 서비스상태 -->
<div class="px-0 pb-0 overflow-y-auto" style="height:220px;">
    <div class="form-inline mb-2">
        <c:forEach var="info" items="${getInfoService}">
        <c:choose>
        <c:when test="${info.serverName eq 'common'}">
        <div class="col-6 pl-0"><!-- 서비스 상태 공통 -->
            <div class="table-responsive">
                <table class="table table-xs dataTable">
                    <thead class="thead-light">
                    <tr>
                        <th scope="col"><spring:message code="ecare.table.sname_${wiseu:getProperty('web.exec.mode', '1')}"/></th><!-- 서비스명 -->
                        <th scope="col"><spring:message code="common.menu.creator"/></th><!-- 작성자 -->
                        <th scope="col"><spring:message code="watch.menu.smod"/></th><!-- 발송모드 -->
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${info.serviceName}</td>
                        <td>${info.userId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${info.sendMode eq 'T'}"><spring:message code="watch.menu.tsend"/><!-- 테스트발송 --></c:when>
                                <c:otherwise><spring:message code="watch.menu.rsend"/><!-- 실발송 --></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div><!-- e.col -->
        </c:when>

        <c:when test="${info.serverName eq 'LTS'}"><!-- LTS 정보 -->
        <div class="col-6 pr-0">
            <div class="table-responsive ">
                <table class="table table-xs dataTable ">
                <tr class="thead-light">
                    <th scope="col" rowspan="2">${info.serverId}</th><!-- 프로세스 ID -->
                    <th scope="col"><spring:message code="watch.menu.date"/></th><!-- 날짜-->
                    <th scope="col"><spring:message code="watch.menu.status2"/></th><!-- 서비스 상태 -->
                    <th scope="col"><spring:message code="watch.menu.target"/></th><!-- 대상자수 -->
                    <th scope="col"><spring:message code="watch.menu.channel"/></th><!-- 채널 -->
                </tr>
                <tr>
                    <td>${info.startTime}</td>
                    <td id="ltsServiceStatus">
                        <c:choose>
                            <c:when test="${info.serviceStatus eq 'R'}"><spring:message code="campaign.status.R"/><!-- 발송대기 --></c:when>
                            <c:when test="${info.serviceStatus eq 'W'}"><spring:message code="campaign.status.W"/><!-- 발송중 --></c:when>
                            <c:when test="${info.serviceStatus eq 'E'}"><spring:message code="campaign.status.E"/><!-- 발송종료 --></c:when>
                            <c:when test="${info.serviceStatus eq 'O'}"><spring:message code="campaign.status.O"/><!-- 발송에러 --></c:when>
                            <c:when test="${info.serviceStatus eq 'P'}"><spring:message code="campaign.status.P"/><!-- 보류 --></c:when>
                            <c:when test="${info.serviceStatus eq 'S'}"><spring:message code="campaign.status.S"/><!-- 일시정지 --></c:when>
                            <c:when test="${info.serviceStatus eq 'V'}"><spring:message code="campaign.status.V"/><!-- 분할정지 --></c:when>
                            <c:when test="${info.serviceStatus eq 'T'}"><spring:message code="campaign.status.T"/><!-- 발송중단 --></c:when>
                            <c:when test="${info.serviceStatus eq 'I'}"><spring:message code="campaign.status.I"/><!-- 작성중 --></c:when>
                            <c:when test="${info.serviceStatus eq 'D'}"><spring:message code="campaign.status.D"/><!-- 승인거부 --></c:when>
                            <c:when test="${info.serviceStatus eq 'C'}"><spring:message code="campaign.status.C"/><!-- 승인요청 --></c:when>
                            <c:otherwise><spring:message code="watch.menu.no"/><!-- 알수없음 --></c:otherwise>
                        </c:choose>
                    </td>
                    <td>${info.targetCount}</td>
                    <td><spring:message code="common.channel.${info.channel}"/></td>
                </tr>
                </table>
            </div>
        </div><!-- e.col -->
    </div><!-- e.row -->

    <c:if test="${not empty info and not empty info.mtsNo and info.mtsNo gt 0}"><!-- mts 정보 -->
    <div class="table-responsive mb-2">
        <table class="table table-xs dataTable" style="margin: 0px !important;">
            <tr class="thead-light">
                <th scope="col" rowspan="2" width="22%">MTS <spring:message code="watch.menu.status2"/></th><!-- MTS 서비스 상태 -->

                <c:forEach var="cnt" begin="1" end="${info.mtsNo}">
                <th scope="col" id="server${cnt}">${cnt}</th>
                </c:forEach>
            </tr>
            <tr>
                <c:forEach var="cnt" begin="1" end="${info.mtsNo}">
                <td id="status${cnt}">${cnt}</td>
                </c:forEach>
            </tr>
        </table>
    </div>
    </c:if>

    <div class="table-responsive mb-2" ><!-- MTS 발송 합계 start -->
        <table class="table table-xs dataTable" id="MTS" style="margin: 0px !important;">
        </table>
    </div>
    </c:when>
    </c:choose>
    </c:forEach>

    <div class="table-responsive" id="sumMts" style="display: none;"><!-- MTS 발송 합계 start -->
        <table class="table table-xs dataTable" style="margin: 0px !important;">
            <tr class="thead-light">
                <th rowspan="2" scope="col">MTS <spring:message code="watch.menu.count.stotal"/></th><!-- 프로세스 id -->
                <th scope="col"><spring:message code="watch.menu.count.total"/></th><!-- 총합계 -->
                <th scope="col"><spring:message code="watch.menu.count.new"/></th><!-- 생성건수 -->
                <th scope="col" class="text-blue"><spring:message code="watch.menu.count.send"/></th><!-- 발송건수 -->
                <th scope="col" class="text-green"><spring:message code="watch.menu.count.success"/></th><!-- 성공건수 -->

                <c:if test="${info.channel eq 'M'}">
                <th scope="col">UnknownUser</th>
                <th scope="col">UnknownHost</th>
                <th scope="col">SmtpExcept</th>
                <th scope="col">Noroute</th>
                <th scope="col">Refused</th>
                <th scope="col">EtcExcept</th>
                <th scope="col">InvalidAddress</th>
                </c:if>
            </tr>
            <tr>
                <td id="sumTotalCount" ></td>
                <td id="sumMadeCount" ></td>
                <td id="sumSendCount" class="text-blue"></td>
                <td id="sumSuccessCount" class="text-green"></td>

                <c:if test="${info.channel eq 'M'}">
                <td id="sumUnknownUserCount" ></td>
                <td id="sumUnknownHostCount" ></td>
                <td id="sumSmtpExceptCount" ></td>
                <td id="sumNoRouteCount" ></td>
                <td id="sumRefusedCount" ></td>
                <td id="sumEtcExceptCount" ></td>
                <td id="sumInvalidAddressCount" ></td>
                </c:if>
            </tr>
        </table>
    </div>
</div>
