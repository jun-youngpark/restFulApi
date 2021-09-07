<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트] 이케어 리포트 - 일정별 발송현황(메일,FAX,문자)
 * - URL : /report/ecare/dailyRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareDailyController
 * - 이전 파일명 : ecare_daily.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title_${webExecMode}" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<%@ include file="/jsp/include/pluginChart.jsp" %>
<script>
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#btnClick").val("Y");
            $("#reportFrm").submit();
        });

        // 엑셀다운로드 버튼 클릭
        $("#downloadBtn").on("click", function(event) {
            var btnClick = $("#btnClick").val();

            if(btnClick == 'Y') {
                $("#format").val("excel");
                $("#reportFrm").submit();
                $("#format").val("");
            } else {
                alert('<spring:message code="report.ecare.daily.search.alt" />');  // 발송일을 검색하여 주세요.
            }
        });
    }

    function initPage() {
        if('${empty ecareDailyReportList}' == 'false') {
            $("#btnClick").val("Y");
        }

        $("#chartDiv").css("display", "");
        viewChart();
    }

    function viewChart() {
        var $trArray = $("#rptTableBody").children();
        var itemArray = $.mdf.getTableCellValueArray($trArray, 0, false);
        var serviceType= $("#serviceType").val();
        var subType= $("#subType").val();

        var addCnt= 0;
        //스케줄일경우
        if(serviceType=='S' && subType =='S') {
            addCnt = 1;
        }

        //alert('serviceType=' + serviceType + ", subType=" + subType + "=>" + addCnt);

        var valueArray = [{
            "name" : '<spring:message code="report.ecare.title.send.count"/>',  // 발송수
            "data" : $.mdf.getTableCellValueArray($trArray, 2+addCnt, true)
        }, {
            "name" : '<spring:message code="report.ecare.title.success"/>',  // 발송성공
            "data" : $.mdf.getTableCellValueArray($trArray, 3+addCnt, true)
        }, {
            "name" : '<spring:message code="report.ecare.title.fail"/>',  // 발송실패
            "data" : $.mdf.getTableCellValueArray($trArray, 5+addCnt, true)
        }, {
            "name" : '<spring:message code="report.ecare.title.cancel"/>',  // 발송취소
            "data" : $.mdf.getTableCellValueArray($trArray, 7+addCnt, true)
        }];

        var option = {
            colors: ['#7cb5ec', '#90ed7d', '#ff305c', '#acacac']  // 그래프 색상 변경
        };
        var chart = new mdf.Chart('chartDiv', null, '<spring:message code="report.count.unit"/>', option);  // 건수 (건)
        chart.renderChart("VER_LINE", itemArray, valueArray);
    }

    function goView(scenarioNo, ecareNo, serviceType, subType, resultSeq, reportDt) {
        var url = "/report/ecare/summaryRpt.do";  // /report/ecare/summary.do

        // 기본 파라미터
        url += "?scenarioNo=" + scenarioNo + "&ecareNo=" + ecareNo + "&serviceType=" + serviceType
             + "&subType=" + subType + "&resultSeq=" + resultSeq + "&reportDt=" + reportDt;

        // 목록 파라미터
        url += "&listSubType=${param.subType}&channelType=${param.channelType}&searchStartDt=${param.searchStartDt}&searchEndDt=${param.searchEndDt}&searchWord=${param.searchWord}";
        location.href = url;
    }

    // 수신 업데이트
    var updateStatus = false;
    var updateEcareNo = 0;

    function updateEcareList(ecareNo, channelType, serviceType, subType, sendStartDt, resultSeq) {
        var btnClick = $("#btnClick").val();

        if(btnClick == 'Y') {
            if(updateStatus) {
                alert(updateEcareNo + '<spring:message code="report.ecare.daily.alert.msg.update1_${webExecMode}" />');  // 번 이케어가 업데이트 중입니다.
                return;
            }

            if(!confirm(ecareNo + '<spring:message code="report.ecare.daily.alert.msg.update2_${webExecMode}" />')) {  // 번 이케어를 업데이트 하시겠습니까?.
                return;
            }

            updateStatus = true;
            updateEcareNo = ecareNo;
            $("#updateImg" + ecareNo).attr("src","/images/ajax/plugin/wait_small_fbisk.gif");

            if(channelType === "M") {
                var param = {
                    ecareNo : ecareNo,
                    sendStartDt : sendStartDt,
                    resultSeq : resultSeq
                };

                var url = (serviceType ==='S' && subType === 'S')
                        ? "/report/ecare/updateEcareDailyScheduleReceiptList.json"
                        : "/report/ecare/updateEcareDailyRealtimeReceiptList.json";

                $.post(url, $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        $("#updateImg" + updateEcareNo).attr("src","/images/common/check.png");
                        alert(updateEcareNo + "<spring:message code='report.ecare.daily.alert.msg.update3_${webExecMode}'/>");
                        document.location.reload();
                    }
                });
            } else {
                alert('<spring:message code="report.ecare.daily.alert.msg.update.err" />');  // 수신 업데이트가 불가능한 채널입니다.
            }
        } else {
            alert('<spring:message code="report.ecare.daily.search.alt" />');  // 발송일을 검색하여 주세요.
        }
    }
</script>
</head>
<body>
<form id="reportFrm" name="reportFrm" action="/report/ecare/dailyRpt.do" method="post"><!-- /report/ecare/ecare_daily.do -->
<input type="hidden" name="scenarioNo" value="${scenarioInfoVo.scenarioNo}"/>
<input type="hidden" name="ecareNo" value="${scenarioInfoVo.ecareInfoVo.ecareNo}"/>
<input type="hidden" name="serviceType" id="serviceType" value="${scenarioInfoVo.ecareInfoVo.serviceType}"/>
<input type="hidden" name="searchYear" value="${searchYear}"/>
<input type="hidden" name="searchMonth" value="${searchMonth}"/>
<input type="hidden" name="subType" id="subType" value="${scenarioInfoVo.ecareInfoVo.subType}"/>
<input type="hidden" id="btnClick" name="btnClick" />
<input type="hidden" id="format" name="format" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.ecare.title_${webExecMode}"/></h3><!-- 이케어 리포트 -->
            </div>
            <%@ include file="/jsp/report/ecare/dailyRptHead_inc.jsp"%><!-- /jsp/report/ecare/ecare_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-6 align-items-center pt-2"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.ecare.service.subtitle" /></h4><!-- 일정별 발송현황 -->
                    </div>
                    <div class="col-6 justify-content-end pb-1">
                        <c:if test="${!empty ecareDailyReportList}">
                            <button class="btn btn-sm btn-outline-primary" id="downloadBtn">
                                <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel"/><!-- 엑셀 다운로드 -->
                            </button>
                        </c:if>
                    </div>
                </div>

                <div class="col-12 alert alert-secondary mb-0" role="alert"><!-- 업데이트 버튼을 클릭 하시면 수신확인/리턴메일/링크클릭의 통계값이 업데이트 됩니다. -->
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.list.update.detail" />
                </div>

                <div class="table-responsive">
                    <c:set var="colCount" value="7"/>
                    <table class="table table-xs dataTable table-hover">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col" width="67"><spring:message code="report.ecare.title.day" /></th><!-- 발송일 -->
                            <th rowspan="2" scope="col"><spring:message code="report.ecare.title.week" /></th><!-- 요일 -->

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" scope="col" width="117"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                            </c:if>

                            <th rowspan="2" scope="col"><spring:message code="report.ecare.title.sendcount" /></th><!-- 발송건수 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.success" /></th><!-- 발송성공 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.fail" /></th><!-- 발송실패 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.cancel" /></th><!-- 발송취소 -->

                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}"><%-- 메일 채널인 경우 --%>
                            <c:set var="colCount" value="${colCount + 4}"/>
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.open" /></th><!-- 수신확인 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.duration" /></th><!-- 유효수신 -->
                            </c:if>

                            <c:if test="${webExecMode eq '1' }">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" scope="col" width="51"><spring:message code="report.ecare.title.time" /></th><!-- 발송시간 -->
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" scope="col" width="45" class="ls--1px"><spring:message code="report.ecare.daily.update" /></th><!-- 업데이트 -->
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" width="40" class="ls--1px"><spring:message code='common.send.type.resend' /></th><!-- 재발송  -->
                            </c:if>
                        </tr>

                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송실패 : 건수 -->
                            <th scope="col">%</th><!-- 발송실패 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송취소 : 건수 -->
                            <th scope="col">%</th><!-- 발송취소 : % -->

                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}"><%-- 메일 채널인 경우 --%>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 수신확인 : 건수 -->
                            <th scope="col">%</th><!-- 수신확인 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 유효수신 : 건수 -->
                            <th scope="col">%</th><!-- 유효수신 : % -->
                            </c:if>
                        </tr>
                        </thead>

                        <c:if test="${!empty ecareDailyReportList}">
                        <tbody id="rptTableBody">
                        <c:forEach var="sendResultVo" items="${ecareDailyReportList}" varStatus="status">
                        <tr style="cursor: pointer;" onclick="javascript:goView('${scenarioInfoVo.scenarioNo}', '${scenarioInfoVo.ecareInfoVo.ecareNo}', '${scenarioInfoVo.ecareInfoVo.serviceType}', '${scenarioInfoVo.ecareInfoVo.subType}', '${sendResultVo.resultSeq}', '${sendResultVo.sendStartDt}')">
                            <td>${sendResultVo.sendStartDtToSimpleDateStr}</td><!-- 발송일 -->
                            <td>${sendResultVo.weekday}</td><!-- 요일 -->

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <td><!-- 발송일련번호 -->
                                <c:choose>
                                    <c:when test="${sendResultVo.resendSts eq 'T'}"><font style="color: green; ">[<spring:message code="resend.type.target" />]</font></c:when>
                                    <c:when test="${sendResultVo.resendSts eq 'F'}"><font style="color: blue; ">[<spring:message code="resend.type.fail" />]</font></c:when>
                                </c:choose>
                                ${sendResultVo.resultSeq}
                            </td>
                            </c:if>

                            <td class="text-right"><!-- 발송건수 -->
                                <c:set var="totalSendCnt" value="${totalSendCnt + sendResultVo.sendCnt }"/>
                                <fmt:formatNumber type="number" value="${sendResultVo.sendCnt}"/>
                            </td>
                            <td class="text-right"><!-- 발송성공 : 건수 -->
                                <c:set var="totalSuccessCnt" value="${totalSuccessCnt + sendResultVo.successCnt}"/>
                                <fmt:formatNumber type="number" value="${sendResultVo.successCnt}"/>
                            </td>
                            <td class="text-right"><!-- 발송성공 : % -->
                               <c:choose>
                                    <c:when test="${sendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${sendResultVo.successCntToPercent}" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- 발송실패 : 건수 -->
                                 <c:set var="totalBounceCnt" value="${totalBounceCnt + sendResultVo.bounceCnt }"/>
                                 <fmt:formatNumber type="number" value="${sendResultVo.bounceCnt}"/>
                            </td>
                            <td class="text-right"><!-- 발송실패 : % -->
                                <c:choose>
                                    <c:when test="${sendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${sendResultVo.bounceCntToPercent}" />%</c:otherwise>
                                </c:choose>
                            </td>
                            
                            <td class="text-right"><!-- 발송취소 : 건수 -->
                                 <c:set var="totalCancelCnt" value="${totalCancelCnt + sendResultVo.cancelCnt }"/>
                                 <fmt:formatNumber type="number" value="${sendResultVo.cancelCnt}"/>
                            </td>
                            <td class="text-right"><!-- 발송취소 : % -->
                                <c:choose>
                                    <c:when test="${sendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${sendResultVo.cancelCntToPercent}" />%</c:otherwise>
                                </c:choose>
                            </td>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">
                            <td class="text-right"><!-- 수신확인 : 건수 -->
                               <c:set var="totalOpenCnt" value="${totalOpenCnt + sendResultVo.openCnt}"/>
                               <fmt:formatNumber type="number" value="${sendResultVo.openCnt}"/>
                            </td>
                            <td class="text-right"><!-- 수신확인 : % -->
                               <c:choose>
                                    <c:when test="${sendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${sendResultVo.openCntToPercent}" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- 유효수신 : 건수 -->
                                <c:set var="totalDurationCnt" value="${totalDurationCnt + sendResultVo.durationCnt }"/>
                                <fmt:formatNumber type="number" value="${sendResultVo.durationCnt}"/>
                            </td>
                            <td class="text-right"><!-- 유효수신 : % -->
                                <c:choose>
                                    <c:when test="${sendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${sendResultVo.durationCntToPercent}"/>%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            </c:if>

                            <c:if test="${webExecMode eq '1' }">
                            <td><!-- 발송시간 : 스케줄인 경우 출력 -->
                                <c:choose>
                                    <c:when test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">${sendResultVo.formatPeriod}</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">
                            <td onclick="event.cancelBubble=true"><!-- 업데이트 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="updateEcareList('${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.channelType}','${scenarioInfoVo.ecareInfoVo.serviceType}','${scenarioInfoVo.ecareInfoVo.subType}','${sendResultVo.sendStartDt}','${sendResultVo.resultSeq}'); return false;">
                                    <i class="fas fa-sync-alt"></i>
                                </button>
                            </td>
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <td onclick="event.cancelBubble=true"><!-- 재발송 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:viewResendForm('${sendResultVo.resultSeq}','${sendResultVo.superSeq}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.channelType}','${sendResultVo.bounceCnt}'); return false;">
                                    <i class="fas fa-paper-plane fa-lg"></i>
                                </button>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                        </tbody>

                        <c:set var="tmpCols" value="2"/>
                        <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <c:set var="tmpCols" value="3"/>
                        </c:if>

                        <tfoot class="bg-secondary">
                        <tr><!-- 합계 -->
                            <td scope="row" colspan="${tmpCols}" ><spring:message code="report.total"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSendCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccessCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${totalSuccessCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalBounceCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${totalBounceCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <!-- 발송취소 합계 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalCancelCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalCancelCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${totalCancelCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <!--  채널이 M인경우에만 보여주는 정보. PUSH 추가 -->
                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">
                                <td class="text-right"><fmt:formatNumber type="number" value="${totalOpenCnt}"/></td>
                                <td class="text-right">
                                    <c:choose>
                                        <c:when test="${totalOpenCnt eq 0}">0.0%</c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber type="number" pattern="0.0" value="${totalOpenCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-right"><fmt:formatNumber type="number" value="${totalDurationCnt}"/></td>
                                <td class="text-right">
                                    <c:choose>
                                        <c:when test="${totalDurationCnt eq 0}">0.0%</c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber type="number" pattern="0.0" value="${totalDurationCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                            <c:if test="${webExecMode eq '1' }">
                                <td>-</td>
                            </c:if>
                            <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M' or scenarioInfoVo.ecareInfoVo.channelType eq 'P'}">
                                <td>-</td>
                            </c:if>
                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                                <td>-</td>
                            </c:if>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${empty ecareDailyReportList}">
                        <tbody>
                            <tr><td colspan="${colCount}"><spring:message code="report.ecare.daily.result.nodata" /></td></tr><!-- 발송결과 데이터가 존재하지 않습니다. -->
                        </tbody>
                        </c:if>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->

            <%@ include file="/jsp/report/ecare/dailyRptResend_inc.jsp"%><!-- 재발송 /jsp/report/ecare/ecare_report_resend_inc.jsp -->

            <div class="card-body mt-5"><!-- 차트 영역 -->
                <div id="chartDiv" class="chart"/>
            </div>
        </div><!-- //card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>
</body>
</html>
