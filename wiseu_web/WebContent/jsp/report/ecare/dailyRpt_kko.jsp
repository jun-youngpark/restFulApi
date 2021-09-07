<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트] 이케어 리포트 - 일정별 발송현황(알림톡,친구톡)
 * - URL : /report/ecare/dailyRpt.do
 * - Controller :com.mnwise.wiseu.web.report.web.ecare.EcareDailyController
 * - 이전 파일명 : ecare_daily_kakaotalk.jsp
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
            $("#reportFrm").submit();
        });

        // 엑셀다운로드 버튼 클릭
        $("#downloadBtn").on("click", function(event) {
            $("#format").val("excel");
            $("#reportFrm").submit();
            $("#format").val("");
        });
    }

    function initPage() {
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
            "data" : $.mdf.getTableCellValueArray($trArray, 1+addCnt, true)
        }, {
            "name" : '<spring:message code="report.ecare.title.success"/>',  // 발송성공
            "data" : $.mdf.getTableCellValueArray($trArray, 2+addCnt, true)
        }, {
            "name" : '<spring:message code="report.ecare.title.fail"/>',  // 발송실패
            "data" : $.mdf.getTableCellValueArray($trArray, 3+addCnt, true)
        }];

        var option = {
            colors: ['#7cb5ec', '#90ed7d', '#ff305c']  // 그래프 색상 변경
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
</script>
</head>

<body>
<form id="reportFrm" name="reportFrm" action="/report/ecare/dailyRpt.do" method="post"><!-- /report/ecare/ecare_daily.do -->
<input type="hidden" name="scenarioNo" value="${scenarioInfoVo.scenarioNo}"/>
<input type="hidden" name="ecareNo" value="${scenarioInfoVo.ecareInfoVo.ecareNo}"/>
<input type="hidden" name="serviceType" id="serviceType" value="${scenarioInfoVo.serviceType}"/>
<input type="hidden" name="subType" id="subType" value="${scenarioInfoVo.ecareInfoVo.subType}"/>
<input type="hidden" name="channelType" value="${scenarioInfoVo.ecareInfoVo.channelType}"/>
<input type="hidden" name="searchYear" value="${searchYear}"/>
<input type="hidden" name="searchMonth" value="${searchMonth}"/>
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
            <div class="card-body pb-0 px-0">
                <div class="row mt-3 mb-0">
                    <div class="col-6 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.ecare.service.subtitle" /></h4><!-- 일정별 발송현황 -->
                    </div>
                    <div class="col-6 justify-content-end">
                        <c:if test="${!empty ecareDailyReportList}">
                            <button class="btn btn-sm btn-outline-primary" id="downloadBtn">
                                <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel"/><!-- 엑셀 다운로드 -->
                            </button>
                        </c:if>
                    </div>
                </div>

                <div class="table-responsive">
                    <c:set var="colCount" value="14"/>
                    <table class="table table-xs dataTable table-hover table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col" width="67"><spring:message code="report.ecare.title.day" /></th><!-- 발송일 -->

                            <c:if test="${subType ne 'N' and serviceType ne 'R'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" scope="col" width="117"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                            </c:if>

                            <th colspan="4" scope="col"><spring:message code='report.total'/></th><!-- 합계 -->

                            <th colspan="3" scope="col">
                                <c:choose>
                                    <c:when test="${scenarioInfoVo.ecareInfoVo.channelType eq 'C'}"><spring:message code='report.dept.title.frdsendcount'/><!-- 친구톡 --></c:when>
                                    <c:otherwise><spring:message code='report.dept.title.altsendcount'/><!-- 알림톡 --></c:otherwise>
                                </c:choose>
                            </th>

                            <th colspan="3" scope="col"><spring:message code='report.dept.title.smssendcount'/></th><!-- SMS -->
                            <th colspan="3" scope="col"><spring:message code='report.dept.title.lmssendcount'/></th><!-- LMS -->

                            <c:if test="${subType ne 'N' and serviceType ne 'R'}">
                            <c:set var="colCount" value="${colCount + 1}"/>
                            <th rowspan="2" width="40" class="ls--1px"><spring:message code='common.send.type.resend' /></th><!-- 재발송  -->
                            </c:if>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code='report.campaign.allsummary.targetcnt'/></th><!-- 합계 : 대상수 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.success'/></th><!-- 합계 : 성공 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.fail'/></th><!-- 합계 : 실패 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.successrate'/></th><!-- 합계 : 성공률 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.success'/></th><!-- 알림톡/친구톡 : 성공 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.fail'/></th><!-- 알림톡/친구톡 : 실패 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.successrate'/></th><!-- 알림톡/친구톡 : 성공률 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.success'/></th><!-- SMS : 성공 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.fail'/></th><!-- SMS : 실패 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.successrate'/></th><!-- SMS : 성공률 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.success'/></th><!-- LMS : 성공 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.fail'/></th><!-- LMS : 실패 -->
                            <th scope="col"><spring:message code='report.campaign.list.title.successrate'/></th><!-- LMS : 성공률 -->
                        </tr>
                        </thead>

                        <c:if test="${!empty ecareDailyReportList}">
                        <tbody id="rptTableBody">
                        <c:forEach var="sendResultVo"       items="${ecareDailyReportList}" varStatus="status">
                        <c:set var="kakakoSuccessCnt"       value="${sendResultVo.successCnt}"/>
                        <c:set var="kakakoFailureCnt"       value="${sendResultVo.softBounceCnt}"/>
                        <c:set var="kakakoTotalCnt"         value="${kakakoSuccessCnt + kakakoFailureCnt}"/>
                        <c:set var="smsSuccessCnt"          value="${sendResultVo.smsSuccessCnt}"/>
                        <c:set var="smsFailureCnt"          value="${sendResultVo.smsFailCnt}"/>
                        <c:set var="smsTotalCnt"            value="${smsSuccessCnt + smsFailureCnt}"/>
                        <c:set var="lmsSuccessCnt"          value="${sendResultVo.lmsSuccessCnt}"/>
                        <c:set var="lmsFailureCnt"          value="${sendResultVo.lmsFailCnt}"/>
                        <c:set var="lmsTotalCnt"            value="${lmsSuccessCnt + lmsFailureCnt}"/>
                        <c:set var="totalSendCnt"           value="${sendResultVo.sendCnt}"/>
                        <c:set var="totalSuccessCnt"        value="${kakakoSuccessCnt + smsSuccessCnt + lmsSuccessCnt}"/>
                        <c:set var="totalFailureCnt"        value="${totalSendCnt - totalSuccessCnt}"/>
                        <c:set var="sumOfKakaoSuccessCnt"   value="${sumOfKakaoSuccessCnt + kakakoSuccessCnt}"/>
                        <c:set var="sumOfKakaoFailureCnt"   value="${sumOfKakaoFailureCnt + kakakoFailureCnt}"/>
                        <c:set var="sumOfKakaoTotalCnt"     value="${sumOfKakaoTotalCnt + kakakoTotalCnt}"/>
                        <c:set var="sumOfSmsSuccessCnt"     value="${sumOfSmsSuccessCnt + smsSuccessCnt}"/>
                        <c:set var="sumOfSmsFailureCnt"     value="${sumOfSmsFailureCnt + smsFailureCnt}"/>
                        <c:set var="sumOfSmsTotalCnt"       value="${sumOfSmsTotalCnt + smsTotalCnt}"/>
                        <c:set var="sumOfLmsSuccessCnt"     value="${sumOfLmsSuccessCnt + lmsSuccessCnt}"/>
                        <c:set var="sumOfLmsFailureCnt"     value="${sumOfLmsFailureCnt + lmsFailureCnt}"/>
                        <c:set var="sumOfLmsTotalCnt"       value="${sumOfLmsTotalCnt + lmsTotalCnt}"/>
                        <c:set var="sumOfTotalSendCnt"      value="${sumOfTotalSendCnt + totalSendCnt}"/>
                        <c:set var="sumOfTotalSuccessCnt"   value="${sumOfTotalSuccessCnt + totalSuccessCnt}"/>
                        <c:set var="sumOfTotalFailureCnt"   value="${sumOfTotalFailureCnt + totalFailureCnt}"/>

                        <tr style="cursor: pointer;" onclick="javascript:goView('${scenarioInfoVo.scenarioNo}', '${scenarioInfoVo.ecareInfoVo.ecareNo}', '${scenarioInfoVo.ecareInfoVo.serviceType}', '${scenarioInfoVo.ecareInfoVo.subType}', '${sendResultVo.resultSeq}', '${sendResultVo.sendStartDt}')">
                            <td>${sendResultVo.reportDtToDateStr}</td><!-- 발송일 -->
                            <c:if test="${subType ne 'N' and serviceType ne 'R' and scenarioInfoVo.ecareInfoVo.channelType eq 'A'}">
                            <td><!-- 발송일련번호 -->
                                <c:choose>
                                    <c:when test="${sendResultVo.resendSts eq 'T'}"><font style="color: green; ">[<spring:message code="resend.type.target" />]</font></c:when>
                                    <c:when test="${sendResultVo.resendSts eq 'F'}"><font style="color: blue; ">[<spring:message code="resend.type.fail" />]</font></c:when>
                                </c:choose>
                                ${sendResultVo.resultSeq}
                            </td>
                            </c:if>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSendCnt}"/></td><!-- 합계 : 대상수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccessCnt}"/></td><!-- 합계 : 성공 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalFailureCnt}"/></td><!-- 합계 : 실패 -->
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${totalSuccessCnt / totalSendCnt * 100}"/>%</td><!-- 합계 : 성공률 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${kakakoSuccessCnt}"/></td><!-- 알림톡/친구톡 : 성공 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${kakakoFailureCnt}"/></td><!-- 알림톡/친구톡 : 실패 -->
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${kakakoTotalCnt > 0 ? kakakoSuccessCnt / kakakoTotalCnt * 100 : 0}"/>%</td><!-- 알림톡/친구톡 : 성공률 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${smsSuccessCnt}"/></td><!-- SMS : 성공 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${smsFailureCnt}"/></td><!-- SMS : 실패 -->
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${smsTotalCnt > 0 ? smsSuccessCnt / smsTotalCnt * 100 : 0}"/>%</td><!-- SMS : 성공률 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${lmsSuccessCnt}"/></td><!-- LMS : 성공 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${lmsFailureCnt}"/></td><!-- LMS : 실패 -->
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${lmsTotalCnt > 0 ? lmsSuccessCnt / lmsTotalCnt * 100 : 0}"/>%</td><!-- LMS : 성공률 -->

                            <c:if test="${subType ne 'N' and serviceType ne 'R'}">
                            <td onclick="event.cancelBubble=true"><!-- 재발송 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:viewResendForm('${sendResultVo.resultSeq}','${sendResultVo.superSeq}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.channelType}','${kakakoFailureCnt}'); return false;">
                                    <i class="fas fa-paper-plane fa-lg"></i>
                                </button>
                            </td>
                            </c:if>
                        </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot class="bg-secondary" id="rptTableFoot">
                        <tr><!-- 합계 -->
                            <td colspan="${(subType ne 'N' and serviceType ne 'R') ? 2 : 1}"><spring:message code="report.total"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfTotalSendCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfTotalSuccessCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfTotalFailureCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${sumOfTotalSendCnt > 0 ? sumOfTotalSuccessCnt / sumOfTotalSendCnt * 100 : 0}"/>%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfKakaoSuccessCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfKakaoFailureCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${sumOfKakaoTotalCnt > 0 ? sumOfKakaoSuccessCnt / sumOfKakaoTotalCnt * 100 : 0}"/>%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfSmsSuccessCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfSmsFailureCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${sumOfSmsTotalCnt > 0 ? sumOfSmsSuccessCnt / sumOfSmsTotalCnt * 100 : 0}"/>%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfLmsSuccessCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${sumOfLmsFailureCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${sumOfLmsTotalCnt > 0 ? sumOfLmsSuccessCnt / sumOfLmsTotalCnt * 100 : 0}"/>%</td>

                            <c:if test="${subType ne 'N' and serviceType ne 'R'}">
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
