<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트] 이케어 리포트 - 일정별 발송현황(PUSH)
 * - URL : /report/ecare/dailyRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareDailyController
 * - 이전 파일명 : ecare_daily_push.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title_${webExecMode}" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<%@ include file="/jsp/include/pluginChart.jsp" %>
<script type="text/javascript">
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

            if(btnClick == 'Y'){
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
        if((serviceType=='S' && subType =='S') || (serviceType=='S' && subType =='R')){
            addCnt = 1;
        }

        var valueArray = [{
                "name" : '<spring:message code="report.ecare.title.send.count"/>',  // 발송수
                "data" : $.mdf.getTableCellValueArray($trArray, 2+addCnt, true)
            }, {
                "name" : 'IOS'+'<spring:message code="report.ecare.title.success"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 3+addCnt, true)
            }, {
                "name" : 'IOS'+'<spring:message code="report.ecare.title.fail"/>',  // 발송실패
                "data" : $.mdf.getTableCellValueArray($trArray, 4+addCnt, true)
            }, {
                "name" : 'ANDROID'+'<spring:message code="report.ecare.title.success"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 6+addCnt, true)
            }, {
                "name" : 'ANDROID'+'<spring:message code="report.ecare.title.fail"/>',  // 발송실패
                "data" : $.mdf.getTableCellValueArray($trArray, 7+addCnt, true)
            }, {
                "name" : 'etc'+'<spring:message code="report.ecare.title.success"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 10+addCnt, true)
            }, {
                "name" : 'etc'+'<spring:message code="report.ecare.title.success"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 10+addCnt, true)
            }, {, {
                "name" : 'etc'+'<spring:message code="report.ecare.title.success"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 10+addCnt, true)
            }, {, {
                "name" : 'etc'+'<spring:message code="report.ecare.title.success"/>',  // 발송취소
                "data" : $.mdf.getTableCellValueArray($trArray, 10+addCnt, true)
            }, {
        ];

        var option = {
            colors: ['#7cb5ec', '#90ed7d', '#ff305c',' #BDB76B','#A52A2A','#FFC0CB','#A9A9A9', '#acacac']  // 그래프 색상 변경
        };
        var chart = new mdf.Chart('chartDiv', null, '<spring:message code="report.count.unit"/>', option);  // 건수 (건)
        chart.renderChart("VER_LINE", itemArray, valueArray);
    }

    function goView(scenarioNo, ecareNo, serviceType, subType, resultSeq, reportDt) {
        var url = "/report/ecare/errorRpt.do";  // /report/ecare/err_summary.do

        // 기본 파라미터
        url += "?scenarioNo=" + scenarioNo + "&ecareNo=" + ecareNo + "&serviceType=" + serviceType
             + "&subType=" + subType + "&resultSeq=" + resultSeq + "&reportDt=" + reportDt;

        // 목록 파라미터
        url += "&listSubType=${param.subType}&channelType=${param.channelType}&searchStartDt=${param.searchStartDt}&searchEndDt=${param.searchEndDt}&searchWord=${param.searchWord}";
        location.href = url;
    }
</script>
</head>

<body>
<form id="reportFrm" name="reportFrm" action="/report/ecare/dailyRpt.do" method="post"><!-- /report/ecare/ecare_daily.do -->
<input type="hidden" name="scenarioNo" value="${scenarioInfoVo.scenarioNo}"/>
<input type="hidden" name="ecareNo" value="${scenarioInfoVo.ecareInfoVo.ecareNo}"/>
<input type="hidden" name="serviceType" id="serviceType" value="${scenarioInfoVo.serviceType}"/>
<input type="hidden" name="searchYear" value="${searchYear}"/>
<input type="hidden" name="searchMonth" value="${searchMonth}"/>
<input type="hidden" name="subType" id="subType" value="${scenarioInfoVo.ecareInfoVo.subType}"/>
<input type="hidden" id="btnClick" name="btnClick" value="" />
<input type="hidden" id="format" name="format" value="" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2">
        <div class="card mb-0">
            <div class="card-header">
                <h3 class="mb-0"><spring:message code="report.ecare.title_${webExecMode}"/></h3><!-- 이케어 리포트 -->
            </div>
            <%@ include file="/jsp/report/ecare/dailyRptHead_inc.jsp"%><!-- /jsp/report/ecare/ecare_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-6 align-items-center pt-1">
                        <h4 class="h3 text-primary m-0"><spring:message code="report.ecare.service.subtitle" /></h4><!-- 일정별 발송현황 -->
                    </div>
                    <div class="col-6 justify-content-end">
                        <c:if test="${!empty ecareDailyReportList}">
                            <button class="btn btn-sm btn-outline-primary" id="downloadBtn">
                                <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel"/><!-- 엑셀 다운로드 -->
                            </button>
                        </c:if>
                    </div>
                </div><!-- //title -->

                <div class="table-responsive">
                    <c:set var="colCount" value="12"/>
                    <table class="table table-xs dataTable table-hover table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col" width="67"><spring:message code="report.ecare.title.day" /></th><!-- 발송일 -->
                            <th rowspan="2" scope="col"><spring:message code="report.ecare.title.week" /></th><!-- 요일 -->

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" scope="col" width="117"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                            </c:if>

                            <th rowspan="2" scope="col"><spring:message code="report.ecare.title.sendcount" /></th><!-- 발송건수 -->
                            <th colspan="3" scope="col" ><spring:message code="report.chart.push.label.ios"/></th><!-- iOS -->
                            <th colspan="3" scope="col" ><spring:message code="report.chart.push.label.and"/></th><!-- Android -->
                            <th colspan="4" scope="col"><spring:message code="report.chart.push.label.etc"/></th><!-- 기타 -->

                            <c:if test="${webExecMode eq '1' }">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" scope="col" width="51"><spring:message code="report.ecare.title.time" /></th><!-- 발송시간 -->
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" width="40" class="ls--1px"><spring:message code='common.send.type.resend' /></th><!-- 재발송  -->
                            </c:if>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.success" /></th><!-- iOS : 발송성공 -->
                            <th scope="col"><spring:message code="report.ecare.title.fail" /></th><!-- iOS : 발송실패 -->
                            <th scope="col"><spring:message code="report.ecare.title.open" /></th><!-- iOS : 수신확인 -->
                            <th scope="col"><spring:message code="report.ecare.title.success" /></th><!-- Android : 발송성공 -->
                            <th scope="col"><spring:message code="report.ecare.title.fail" /></th><!-- Android : 발송실패 -->
                            <th scope="col"><spring:message code="report.ecare.title.open" /></th><!-- Android : 수신확인 -->
                            <th scope="col"><spring:message code="report.reqcnt" /></th><!-- 기타 : 요청건수 -->
                            <th scope="col"><spring:message code="report.reqsuccess" /></th><!-- 기타 : 요청성공 -->
                            <th scope="col"><spring:message code="report.reqfail" /></th><!-- 기타 : 요청실패 -->
                            <th scope="col"><spring:message code="report.ecare.title.cancel" /></th><!-- 기타 : 발송취소 -->
                        </tr>
                        </thead>

                        <c:if test="${!empty ecareDailyReportList}">
                        <c:set var="totalSendCnt" value="0"/>
                        <c:set var="totalIOSSuccCnt" value="0.0"/>
                        <c:set var="totalIOSFailCnt" value="0.0"/>
                        <c:set var="totalIOSOpenCnt" value="0.0"/>
                        <c:set var="totalANDSuccCnt" value="0.0"/>
                        <c:set var="totalANDFailCnt" value="0.0"/>
                        <c:set var="totalANDOpenCnt" value="0.0"/>
                        <c:set var="totalETCReqCnt" value="0.0"/>
                        <c:set var="totalETCSuccCnt" value="0.0"/>
                        <c:set var="totalETCFailCnt" value="0.0"/>
                        <c:set var="totalCancelCnt" value="0.0"/>

                        <tbody id="rptTableBody">
                        <c:forEach var="sendResultVo" items="${ecareDailyReportList}" varStatus="status">
                        <fmt:formatNumber type="number" pattern="0.0" var="IOS_SUCC_CNT" value="${pushSendlogList[status.index].IOS_SUCC_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="IOS_FAIL_CNT" value="${pushSendlogList[status.index].IOS_FAIL_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="IOS_RECEIPT" value="${pushReceiveList[status.index].IOS_RECEIPT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="AND_SUCC_CNT" value="${pushSendlogList[status.index].AND_SUCC_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="AND_FAIL_CNT" value="${pushSendlogList[status.index].AND_FAIL_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="AND_RECEIPT" value="${pushReceiveList[status.index].AND_RECEIPT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="ETC_REQ_CNT" value="${pushSendlogList[status.index].ETC_REQ_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="ETC_SUCC_CNT" value="${pushSendlogList[status.index].ETC_SUCC_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="ETC_FAIL_CNT" value="${pushSendlogList[status.index].ETC_FAIL_CNT}"/>
                        <fmt:formatNumber type="number" pattern="0.0" var="cancelCnt" value="${sendResultVo.cancelCnt}"/>

                        <c:set var="totalSendCnt" value="${totalSendCnt + sendResultVo.sendCnt }"/>
                        <c:set var="totalIOSSuccCnt" value="${totalIOSSuccCnt + IOS_SUCC_CNT}"/>
                        <c:set var="totalIOSFailCnt" value="${totalIOSFailCnt + IOS_FAIL_CNT}"/>
                        <c:set var="totalIOSOpenCnt" value="${totalIOSOpenCnt + IOS_RECEIPT}"/>
                        <c:set var="totalANDSuccCnt" value="${totalANDSuccCnt + AND_SUCC_CNT}"/>
                        <c:set var="totalANDFailCnt" value="${totalANDFailCnt + AND_FAIL_CNT}"/>
                        <c:set var="totalANDOpenCnt" value="${totalANDOpenCnt + AND_RECEIPT}"/>
                        <c:set var="totalETCReqCnt"  value="${totalETCReqCnt  + ETC_REQ_CNT}"/>
                        <c:set var="totalETCSuccCnt" value="${totalETCSuccCnt + ETC_SUCC_CNT}"/>
                        <c:set var="totalETCFailCnt" value="${totalETCFailCnt + ETC_FAIL_CNT}"/>
                        <c:set var="totalCancelCnt" value="${totalCancelCnt + cancelCnt}"/>

                        <c:set var="lineFailCnt" value="${sendResultVo.bounceCnt}"/>
                        <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'P'}">
                            <c:set var="lineFailCnt" value="${IOS_FAIL_CNT + AND_FAIL_CNT + ETC_FAIL_CNT}"/>
                        </c:if>

                        <fmt:formatNumber var="tmpSendCnt" type="number" value="${sendResultVo.sendCnt}" pattern="0.0" />

                        <tr style="cursor: pointer;" onclick="javascript:goView('${scenarioInfoVo.scenarioNo}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.serviceType}','${scenarioInfoVo.ecareInfoVo.subType}','${sendResultVo.resultSeq}','${sendResultVo.sendStartDt}')">
                            <td>${sendResultVo.sendStartDtToSimpleDateStr}</td>
                            <td>${sendResultVo.weekday}</td>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <td>
                                <c:choose>
                                    <c:when test="${sendResultVo.resendSts eq 'T'}"><font style="color: green; ">[<spring:message code="resend.type.target" />]</font></c:when>
                                    <c:when test="${sendResultVo.resendSts eq 'F'}"><font style="color: blue; ">[<spring:message code="resend.type.fail" />]</font></c:when>
                                </c:choose>
                                ${sendResultVo.resultSeq}
                            </td>
                            </c:if>

                            <td class="text-right"><fmt:formatNumber type="number" value="${sendResultVo.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].IOS_SUCC_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].IOS_FAIL_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushReceiveList[status.index].IOS_RECEIPT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].AND_SUCC_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].AND_FAIL_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushReceiveList[status.index].AND_RECEIPT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].ETC_REQ_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].ETC_SUCC_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].ETC_FAIL_CNT}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sendResultVo.cancelCnt}" /></td>

                            <c:if test="${webExecMode eq '1' }">
                            <td class="align_title">
                                <!--  스케줄 레포트 인경우에만 발송시간을 보여줍니다. -->
                                <c:choose>
                                    <c:when test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                                        ${sendResultVo.formatPeriod}
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <td onclick="event.cancelBubble=true"><!-- 재발송 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:viewResendForm('${sendResultVo.resultSeq}','${sendResultVo.superSeq}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.channelType}','${lineFailCnt}'); return false;">
                                    <i class="fas fa-paper-plane fa-lg"></i>
                                </button>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                        </tbody>

                        <fmt:formatNumber var="totalSendCnt"    type="number" value="${totalSendCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalIOSSuccCnt" type="number" value="${totalIOSSuccCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalIOSFailCnt" type="number" value="${totalIOSFailCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalIOSOpenCnt" type="number" value="${totalIOSOpenCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalANDSuccCnt" type="number" value="${totalANDSuccCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalANDFailCnt" type="number" value="${totalANDFailCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalANDOpenCnt" type="number" value="${totalANDOpenCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalETCReqCnt"  type="number" value="${totalETCReqCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalETCSuccCnt" type="number" value="${totalETCSuccCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalETCFailCnt" type="number" value="${totalETCFailCnt}" pattern="0.0"/>
                        <fmt:formatNumber var="totalCancelCnt" type="number" value="${totalCancelCnt}" pattern="0.0"/>
                        <c:set var="tmpCols" value="2"/>
                        <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <c:set var="tmpCols" value="3"/>
                        </c:if>

                        <tfoot class="bg-secondary" id="totalTr">
                        <tr>
                            <td colspan="${tmpCols}"><spring:message code="report.ecare.daily.title.total"/></td>
                            <td class="text-right">${totalSendCnt}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalIOSSuccCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalIOSFailCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalIOSOpenCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalANDSuccCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalANDFailCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalANDOpenCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalETCReqCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalETCSuccCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalETCFailCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalCancelCnt}"/></td>

                            <c:if test="${webExecMode eq '1' }">
                            <td>-</td>
                            </c:if>

                            <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType eq 'S' and scenarioInfoVo.ecareInfoVo.subType eq 'S'}">
                            <td>-</td>
                            </c:if>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${empty ecareDailyReportList}">
                        <tr>
                            <td colspan="${colCount}"><spring:message code="report.ecare.daily.result.nodata" /></td><!-- 발송결과 데이터가 존재하지 않습니다. -->
                        </tr>
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
</div><!--e.Contents area-->
</form>
</body>
</html>
