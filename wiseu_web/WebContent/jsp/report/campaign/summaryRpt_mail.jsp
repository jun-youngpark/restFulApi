<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 전체 요약(메일)<br/>
 * - URL : /report/campaign/summaryRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignSummaryAllController <br/>
 * - 이전 파일명 : mail_summary_all_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code = "report.campaign.allsummary.title" /></title><!-- 캠페인 전체요약 리포트 -->
<%@ include file="/jsp/include/pluginChart.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 기본정보와 help 버튼의 보이기 숨기기 셋팅 (menuOpenGubun 파일은 campaignRptTab_inc.jsp에 존재한다.
        $('#helpBtn').on("click", function(event) {
            event.preventDefault();
            $('#helpDiv').toggle();
        });
    }

    function initPage() {
        viewChart();
    }

    //  excel down
    function downloadExcel(scenarioNo,campaignNo,gubun) {
        $("#cmd").val("excel");
        $("#gubun").val(gubun);
        $("#excelCampaignNo").val(campaignNo);
        $("#campaignReportFrm").attr('action', '/report/campaign/excelAllSummary.do').submit();
    }

    // 재발송
    function resend(resendMode,scenarioNo,campaignNo,depthNo,channelType,check) {
        var abTestType = $("#abTestType").val();
        var abTestCond = $("#abTestCond").val();

        var param = 'resendMode=' + resendMode;
        param += '&scenarioNo=' + scenarioNo;
        param += '&campaignNo=' + campaignNo;
        param += '&depthNo=' + depthNo;
        param += '&channelType=' + channelType;
        //A/B 테스트 타입 추가
        param += '&abTestType=' + abTestType;
        param += '&abTestCond=' + abTestCond;

        var relationType = resendMode.substring(0,1);
        var msg = "";
        var msg2 = "";

        if(relationType == 'R') {
            msg = '<spring:message code = "report.campaign.allsummary.alert.msg.resend" />';  // 재발송 하시겠습니까?
            msg2 = "<spring:message code='report.campaign.allsummary.alert.resend' />";  // 재발송 대상자가 없습니다.
        } else if(relationType == 'L') {
            msg = '<spring:message code = "report.campaign.allsummary.alert.msg.target" />';  // 타겟발송 하시겠습니까?
            msg2 = "<spring:message code='report.campaign.allsummary.alert.target' />";  // 타겟발송 대상자가 없습니다.
        }

        if(check == '' || check == 0) {
            alert(msg2);
            return;
        }

        if(!confirm(msg)) {
            return;
        }

        window.location.href= "/campaign/resend.do?" + param;
    }

    function viewChart() {
        var $tdArray = $("#rptTableBody").children().first().children();
        var valueArray = [{
            "data" : [
                ['<spring:message code="report.campaign.list.title.sendsuccess"/>', parseInt($tdArray.eq(4).text())],  // 발송성공
                ["HARD", parseInt($tdArray.eq(8).text())],
                ["SOFT", parseInt($tdArray.eq(10).text())]
            ]
        }];

        var chart = new mdf.Chart('chartDiv');
        chart.renderChart("PIE", null, valueArray);
    }
</script>
</head>

<body>

<form id="campaignReportFrm" name="campaignReportFrm" method="post" action="/report/campaign/summaryRpt.do"><!-- /report/campaign/summary_all.do -->
<input type="hidden" name="cmd" id="cmd" />
<input type="hidden" name="gubun" id="gubun" />
<input type="hidden" name="excelCampaignNo" id="excelCampaignNo" />
<input type="hidden" name="campaignNo" value="${campaignReportBasicVo.campaignNo}" />
<input type="hidden" name="scenarioNo" value="${campaignReportBasicVo.scenarioNo}" />
<input type="hidden" name="depthNo" value="${campaignReportBasicVo.depthNo}"/>
<input type="hidden" name="abTestType" id="abTestType" value="${campaignReportBasicVo.abTestType}"/>
<input type="hidden" name="abTestCond" id="abTestCond" value="${campaignReportBasicVo.abTestCond}"/>
<input type="hidden" name="channelType" id="channelType" value="${campaignReportBasicVo.channelType}"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.allsummary"/></h3><!-- 전체 요약 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <!-- 이메일 card -->
        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-6 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.campaign.allsummary.sendresult"/></h4><!-- 발송결과 -->
                    </div>
                    <div class="col-6 justify-content-end">
                        <button class="btn btn-sm mr-0" id="helpBtn">
                            <i class="fas fa-question-circle fa-2x"></i><!-- 도움말 아이콘 -->
                        </button>
                    </div>
                </div>

                <div class="row mt-0 mb-0 dp-none" id="helpDiv"><!-- 발송 결과 Help -->
                    <ul class="col-12 list-group list-group-flush list alert">
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 38px;">
                                <dt class="col-2 text-warning"><spring:message code="report.campaign.list.resend" /></dt><!-- 재발송 -->
                                <dd class="col-10 m-0">
                                    <spring:message code="report.campaign.allsummary.resend.detail1" /><br/><!-- 총 발송된 캠페인중 성공건수외의 실패건수를 대상으로 발송됩니다. -->
                                    - <spring:message code="report.campaign.allsummary.resend.detail2" /><!-- 재발송은 실패건수를 대상으로 발송이 되므로 타겟발송이 불가합니다. -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 57px;">
                                <dt class="col-2 text-warning"><spring:message code="report.campaign.list.target" /></dt><!-- 타겟발송 -->
                                <dd class="col-10 m-0">
                                    <spring:message code="report.campaign.allsummary.target.detail1" /><br/><!-- 총 발송된 캠페인의 성공건수 중 타겟으로 정한 대상자를 대상으로 발송됩니다. -->
                                    - <spring:message code="report.campaign.allsummary.target.detail2" /><br/><!-- 타겟발송은 ‘수신확인결과’에서 발송할 수 있습니다. -->
                                    - <spring:message code="report.campaign.allsummary.target.detail3" /><!-- 재발송된 결과값으로 타겟발송 시 새로운 캠페인으로 분류됩니다 -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 19px;">
                                <dt class="col-2 text-warning"><spring:message code="report.campaign.allsummary.targetcnt" /></dt><!-- 대상수 -->
                                <dd class="col-10 m-0">
                                    <spring:message code="report.campaign.allsummary.targetcnt.detail" /><!-- 대상수의 합은 원본 캠페인의 대상수를 넘을 수 없습니다. -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 19px;">
                                <dt class="col-2 text-warning"><spring:message code="report.campaign.allsummary.validsuccess" /></dt><!-- 유효 성공 -->
                                <dd class="col-10 m-0">
                                    <spring:message code="report.campaign.allsummary.validsuccess.detail" /><!-- Hard Bounce를 제외한 유효 계정 중 발송 성공 값을 나타냅니다. -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 19px;">
                                <dt class="col-2 text-warning">Hard Bounce</dt>
                                <dd class="col-10 m-0">
                                    <spring:message code="report.campaign.allsummary.hardbounce.detail1" /><!-- Hard Bounce 는 재발송 시 제외 되므로 기록이 되지 않습니다. -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 19px;">
                                <dt class="col-2 text-warning">Soft Bounce</dt>
                                <dd class="col-10 m-0">
                                    <spring:message code="report.campaign.allsummary.softbounce.detail1" /><!-- Soft Bounce 는 재발송 시 계속 줄어 합계에서는 최종 값으로 표시됩니다. -->
                                </dd>
                            </dl>
                        </li>
                    </ul>
                </div>

                <div class="table-responsive">
                    <table class="table table-xs dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.name" /></th><!-- 캠페인명 -->
                            <th rowspan="2" scope="col" width="110"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 대상수 -->
                            <th rowspan="2" scope="col"><spring:message code='report.campaign.head.title.send.cnt' /></th><!-- 발송건수 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendsuccess" /></th><!-- 발송성공 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.allsummary.validsuccess" /></th><!-- 유효 성공 -->
                            <th colspan="2" scope="col">Hard Bounce</th>
                            <th colspan="2" scope="col">Soft Bounce</th>
                            <th rowspan="2" width="70"><spring:message code='report.campaign.list.title.senddate' /></th><!-- 발송일자  -->
                            <th rowspan="2" scope="col" width="30"><spring:message code="report.campaign.list.title.success" /></th><!-- 성공 -->

                            <c:if test="${(relationType eq 'N') or (relationType eq 'R') or (relationType eq 'L')}">
                            <th rowspan="2" scope="col" width="40"><spring:message code="report.campaign.list.resend" /></th><!-- 재발송 -->
                            </c:if>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 유효 성공 : 건수 -->
                            <th scope="col">%</th><!-- 유효 성공 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- Hard Bounce : 건수 -->
                            <th scope="col">%</th><!-- Hard Bounce : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- Soft Bounce : 건수 -->
                            <th scope="col">%</th><!-- Soft Bounce : % -->
                        </tr>
                        </thead>

                        <tbody id="rptTableBody">
                        <c:forEach var="loop" items="${campaignSummaryAllList}" varStatus="status">
                        <c:if test="${loop.relationType eq 'L'}"><c:set var="targetLoop" value="true" /></c:if><!-- 타겟 발송이 존재할 경우 targetLoop에 true 값을 준다 -->
                        <c:if test="${loop.gid eq 0 and loop.relationType ne 'L'}"><!-- 일반발송 -->
                        <tr>
                            <td class="text-left" <c:if test="${loop.depthNo > 2}">style="padding-left: ${(loop.depthNo - 2) * 10}px"</c:if>><!-- 캠페인명 -->
                                <c:if test="${(loop.relationType eq 'R') && (loop.depthNo > 1)}">
                                    <div class="re_send">Resend</div>
                                </c:if>
                                ${loop.scenarioNm}
                            </td>
                            <td>${loop.resultSeq}</td>
                            <td class="text-right"><!-- 대상수 -->
                                <c:if test="${status.index eq 0}"><c:set var="totalCnt" value="${loop.targetCnt}"/></c:if>
                                <fmt:formatNumber type="number" value="${loop.targetCnt}" />
                            </td>
                            <td class="text-right"><!-- 발송건수 -->
                                <c:set var="totalSendCnt" value="${totalSendCnt + loop.sendCnt}"/>
                                <c:if test="${loop.depthNo == 1}"><c:set var="totSendCnt" value="${loop.sendCnt}"/></c:if>
                                <fmt:formatNumber type="number" value="${loop.sendCnt}" />
                            </td>
                            <td class="text-right"><!-- 발송성공 : 건수 -->
                                <c:set var="totalSuccCnt" value="${totalSuccCnt + loop.successCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.successCnt}" />
                            </td>
                            <td class="text-right"><!-- 발송성공 : % -->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.successCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.successCnt}" /></td><!-- 유효 성공 : 건수 -->
                            <td class="text-right"><!-- 유효 성공 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.successCnt / (loop.sendCnt - loop.hardBounceCnt) * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- Hard Bounce : 건수 -->
                                <c:set var="totalHardBounce" value="${totalHardBounce + loop.hardBounceCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.hardBounceCnt}" />
                            </td>
                            <td class="text-right"><!-- Hard Bounce : % -->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.hardBounceCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- Soft Bounce : 건수 -->
                                <c:set var="totalSoftBounce" value="${loop.softBounceCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.softBounceCnt}" />
                            </td>
                            <td class="text-right"><!-- Soft Bounce : % -->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.softBounceCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%
                                 </c:otherwise>
                              </c:choose>
                            </td>
                            <td>
                                <fmt:parseDate value="${loop.sendstartDt}" var="sendstartDt" pattern="yyyyMMdd" />
                                <fmt:formatDate value="${sendstartDt}" pattern="yyyy-MM-dd" />
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${loop.scenarioNo},${loop.campaignNo},'success');return false;">
                                    <i class="fas fa-file-excel fa-lg"></i><!-- 성공 : 엑셀 아이콘 -->
                                </button>
                            </td>

                            <!-- 재발송은 일반/재발송 과 타겟발송의 각각 마지막 depth_no에만 진행한다. -->
                            <c:if test="${(relationType eq 'N') or (relationType eq 'R') or (relationType eq 'L')}">
                            <td><!-- 재발송 : 메일 아이콘 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:viewResendForm('${loop.resultSeq}','${loop.resultSeq}','${loop.campaignNo}','${loop.channelType}','${loop.softBounceCnt}'); return false;">
                                    <i class="fas fa-paper-plane fa-lg"></i>
                                </button>
                            </td>
                            </c:if>
                        </tr>
                        </c:if>
                        </c:forEach>
                        </tbody>

                        <tfoot class="bg-secondary">
                        <c:if test="${(relationType eq 'N') or (relationType eq 'R') or (relationType eq 'L')}">
                        <tr>
                            <td scope="row" colspan="2"><spring:message code="report.total" /></td><!-- 합계 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totSendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalSuccCnt / totSendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totSendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalSuccCnt / (totSendCnt - totalHardBounce) * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalHardBounce}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totSendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalHardBounce / totSendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSoftBounce}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totSendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalSoftBounce / totSendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        </c:if>
                        </tfoot>
                    </table>

                    <!-- 타겟발송 결과 표시/타겟발송이 있는 경우에만 -->
                    <c:if test="${targetLoop eq 'true' }">
                    <table class="table table-xs dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.target" /></th><!-- 타겟발송 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 대상수 -->
                            <th rowspan="2" scope="col"><spring:message code='report.campaign.head.title.send.cnt' /></th><!-- 발송건수 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendsuccess" /></th><!-- 발송성공 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.allsummary.validsuccess" /></th><!-- 유효 성공 -->
                            <th colspan="2" scope="col">Hard Bounce</th>
                            <th colspan="2" scope="col">Soft Bounce</th>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.success" /></th><!-- 성공 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.resend" /></th><!-- 재발송 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 유효 성공 : 건수 -->
                            <th scope="col">%</th><!-- 유효 성공 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- Hard Bounce : 건수 -->
                            <th scope="col">%</th><!-- Hard Bounce : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- Soft Bounce : 건수 -->
                            <th scope="col">%</th><!-- Soft Bounce : % -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="loop" items="${campaignSummaryAllList}" varStatus="i">
                        <c:if test="${loop.relationType ne 'N' and loop.relationType ne 'R'}">
                        <!-- 일반 데이터일 경우 -->
                        <c:if test="${loop.gid eq 0}">
                        <tr>
                            <td class="text-right" <c:if test="${loop.depthNo > 2}">style="padding-left: ${(loop.depthNo - 2) * 10}px"</c:if>><!-- 타겟발송 -->
                                <c:if test="${loop.depthNo > 1}"><div class="target_send">Target Send</div></c:if>
                                <a href="/report/campaign/summary.do?scenarioNo=${loop.scenarioNo}&campaignNo=${loop.campaignNo}&relationType=${loop.relationType}">${loop.scenarioNm}</a>
                            </td>
                            <td><!-- 대상수 -->
                                <c:set var="targetTotalCnt" value="${targetTotalCnt + loop.targetCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.targetCnt}" />
                            </td>
                            <td><!-- 발송건수 -->
                                <c:set var="targetSendCnt" value="${targetSendCnt + loop.sendCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.sendCnt}" />
                            </td>
                            <td class="text-right"><!-- 발송성공 : 건수 -->
                                <c:set var="targetTotalSuccCnt" value="${targetTotalSuccCnt + loop.successCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.successCnt}" />
                            </td>
                            <td class="text-right"><!-- 발송성공 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.successCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- 유효 성공 : 건수 -->
                                <fmt:formatNumber type="number" value="${loop.successCnt}" />
                            </td>
                            <td class="text-right"><!-- 유효 성공 : % -->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.successCnt / (loop.sendCnt - loop.hardBounceCnt) * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- Hard Bounce : 건수 -->
                                <c:set var="targetTotalHardBounce" value="${targetTotalHardBounce + loop.hardBounceCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.hardBounceCnt}" />
                            </td>
                            <td class="text-right"><!-- Hard Bounce : % -->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.hardBounceCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- Soft Bounce : 건수 -->
                                <c:set var="targetTotalSoftBounce" value="${targetTotalSoftBounce +loop.softBounceCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.softBounceCnt}" />
                            </td>
                            <td class="text-right"><!-- Soft Bounce : % -->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.softBounceCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${loop.scenarioNo},${loop.campaignNo},'open');return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:if>
                        </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    </c:if><!-- 타겟발송 끝-->
                </div>
            </div><!-- e.card body -->

            <%@ include file="/jsp/report/campaign/summaryRptResend_inc.jsp" %><!-- 재발송 campaign_report_resend_inc.jsp -->

            <div class="card-body mt-3" style="height: 400px;"><!-- 차트 영역 ie:height -->
                <div class="row align-items-center">
                    <div class="col-4">
                        <div class="chart" id="chartDiv"></div>
                    </div>
                    <div class="col-8">
                        <ul class="list-group list">
                            <li class="list-group-item">
                                <dl class="row m-0" style="height: 24px;">
                                    <dt class="col-2 text-warning">Soft Bounce</dt>
                                    <dd class="col-10 m-0">
                                        <p class="m-0 font-size-13"><spring:message code="report.campaign.allsummary.softbounce.detail2" /></p>
                                        <!-- 캠페인 발송 시 네트워크 환경,장애로 인한 일시적인 발송실패(재발송 시 발송성공 가능성이 있는 실패) -->
                                    </dd>
                                </dl>
                            </li>
                            <li class="list-group-item">
                                <dl class="row m-0" style="height: 24px;">
                                    <dt class="col-2 text-warning">Hard Bounce</dt>
                                    <dd class="col-10 m-0">
                                        <p class="m-0 font-size-13"><spring:message code="report.campaign.allsummary.hardbounce.detail2" /></p>
                                        <!-- 대상자의 도메인, 이메일 주소가 없어 발송성공이 물리적으로 될 수 없는 상태 (재발송 시 발송 성공 불가능) -->
                                    </dd>
                                </dl>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div><!-- 이메일 e.card -->

        <div class="card"><!-- 수신확인결과 card -->
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.campaign.list.title.openresult"/></h4><!-- 수신확인결과 -->
                    </div>
                </div>

                <div class="col-12 alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.allsummary.targetresend.detail1" /><br/><!-- 타겟발송은 수신확인 결과에서 성공건수 중 지정한 대상자를 대상으로 발송됩니다. -->
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.allsummary.targetresend.detail2" /><!-- 타겟발송은 마지막 재발송 까지 합산한 총 발송 합계에서 발송이 가능합니다. -->
                </div>

                <div class="table-responsive">
                    <table class="table table-xs dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.name" /></th><!-- 캠페인명 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.sendsuccess" /></th><!-- 발송성공 -->
                            <th colspan="3" scope="col"><spring:message code="report.campaign.list.title.open" /></th><!-- 수신확인 -->
                            <th colspan="3" scope="col"><spring:message code="report.campaign.list.title.duration" /></th><!-- 유효 수신 -->
                            <th colspan="3" scope="col"><spring:message code="report.campaign.list.title.duplication" /></th><!-- 중복 수신 -->
                            <th colspan="3" scope="col"><spring:message code="report.campaign.list.title.mobile" /></th><!-- 모바일 수신 -->
                            <th rowspan="2" scope="col" width="50"><spring:message code="report.campaign.list.title.download" /></th><!-- 다운로드 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 수신확인 : 건수 -->
                            <th scope="col">%</th><!-- 수신확인 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.target" /></th><!-- 수신확인 : 타겟발송 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 유효 수신 : 건수 -->
                            <th scope="col">%</th><!-- 유효 수신 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.target" /></th><!-- 유효 수신 : 타겟발송 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 중복 수신 : 건수 -->
                            <th scope="col">%</th><!-- 중복 수신 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.target" /></th><!-- 중복 수신: 타겟발송  -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 모바일 수신 : 건수 -->
                            <th scope="col">%</th><!-- 모바일 수신 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.target" /></th><!-- 모바일 수신: 타겟발송  -->
                        </tr>
                        </thead>

                        <c:if test="${fn:length(campaignReportReceiveList) > 0}">
                        <tbody>
                        <c:forEach var="loop" items="${campaignReportReceiveList}" varStatus="i">
                        <c:if test="${loop.gid eq 0}">
                        <tr>
                            <td class="text-left"><c:if test="${loop.depthNo > 1}"><img src="/images/report/icon_enter.gif" /></c:if>${loop.scenarioNm}</td><!-- 캠페인명 -->
                            <td class="text-right"><!-- 발송성공 -->
                                <c:set var="totalSuccessCnt" value="${totalSuccessCnt + loop.successCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.successCnt}" />
                            </td>
                            <td class="text-right"><!-- 수신확인 : 건수 -->
                                <c:set var="totalOpenCnt" value="${totalOpenCnt + loop.openCnt}"/>
                                <fmt:formatNumber type="number" value="${loop.openCnt}" />
                            </td>
                            <td class="text-right"><!-- 수신확인 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.openCnt / loop.successCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td><!-- 수신확인 : 타겟발송 -->
                                <c:choose>
                                    <c:when test="${loop.depthNo > 1}">-</c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-outline-primary" onclick="javascript:resend('L_resendOpen','${loop.scenarioNo}','${loop.campaignNo}','${loop.depthNo}','${loop.channelType}','${loop.openCnt}'); return false;">
                                            <i class="fas fa-paper-plane fa-lg"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- 유효 수신 : 건수 -->
                                <c:set var="totalDurationCnt" value="${totalDurationCnt + loop.durationCnt }"/>
                                <fmt:formatNumber type="number" value="${loop.durationCnt}" />
                            </td>
                            <td class="text-right"><!-- 유효 수신 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.durationCnt / loop.successCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td><!-- 유효 수신 : 타겟발송 -->
                                <c:choose>
                                    <c:when test="${loop.depthNo > 1}">-</c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-outline-primary btn" onclick="javascript:resend('L_resendDuration','${loop.scenarioNo}','${loop.campaignNo}','${loop.depthNo}','${loop.channelType}','${loop.durationCnt}'); return false;">
                                            <i class="fas fa-paper-plane fa-lg"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- 중복 수신 : 건수 -->
                                <c:set var="totalDuplicationCnt" value="${totalDuplicationCnt + loop.duplicationCnt }"/>
                                <fmt:formatNumber type="number" value="${loop.duplicationCnt}" />
                            </td>
                            <td class="text-right"><!-- 중복 수신 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.duplicationCnt / loop.successCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td><!-- 중복 수신: 타겟발송  -->
                                <c:choose>
                                    <c:when test="${loop.depthNo > 1}">-</c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-outline-primary" onclick="javascript:resend('L_resendDuplication','${loop.scenarioNo}','${loop.campaignNo}','${loop.depthNo}','${loop.channelType}','${loop.duplicationCnt}'); return false;">
                                            <i class="fas fa-paper-plane fa-lg"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- 모바일 수신 : 건수 -->
                                <c:set var="totalMobileCnt" value="${totalMobileCnt + loop.mobileCnt }"/>
                                <fmt:formatNumber type="number" value="${loop.mobileCnt}" />
                            </td>
                            <td class="text-right"><!-- 모바일 수신 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.mobileCnt / loop.successCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td><!-- 모바일 수신: 타겟발송  -->
                                <c:choose>
                                    <c:when test="${loop.depthNo > 1}">-</c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-outline-primary" onclick="javascript:resend('L_resendMobile','${loop.scenarioNo}','${loop.campaignNo}','${loop.depthNo}','${loop.channelType}','${loop.mobileCnt}'); return false;">
                                            <i class="fas fa-paper-plane fa-lg"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><!-- 다운로드 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${loop.scenarioNo},${loop.campaignNo},'open'); return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:if>
                        </c:forEach>
                        </tbody>

                        <tfoot class="bg-secondary"><!-- 합계 -->
                        <tr>
                            <td scope="row"><spring:message code="report.campaign.list.title.total" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccessCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalOpenCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalOpenCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalDurationCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalDurationCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalDuplicationCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalDuplicationCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalMobileCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${totalMobileCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                            <td>-</td>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${empty campaignReportReceiveList}">
                        <tbody>
                            <tr><td colspan="15"><spring:message code="report.ecare.list.open.nodata" /></td></tr><!-- 수신확인 결과 데이터가 존재하지 않습니다. -->
                        </tbody>
                        </c:if>

                    </table>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-xs dataTable">
                    <thead class="thead-light">
                    <tr>
                        <th colspan="3" scope="col"><spring:message code="report.campaign.head.alt.return" /></th><!-- 리턴메일 분석 -->
                        <th colspan="3" scope="col"><spring:message code="report.campaign.head.alt.reject" /></th><!-- 수신거부 분석 -->
                    </tr>
                    <tr>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 리턴메일 분석 : 건수 -->
                        <th scope="col">%</th><!-- 리턴메일 분석 : % -->
                        <th scope="col"><spring:message code="report.campaign.list.title.download" /></th><!-- 리턴메일 분석 : 다운로드 -->
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 수신거부 분석 : 건수 -->
                        <th scope="col">%</th><!-- 수신거부 분석 : % -->
                        <th scope="col"><spring:message code="report.campaign.list.title.download" /></th><!-- 수신거부 분석 : 다운로드 -->
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="text-right">${returnCnt}</td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${campaignSendResultVo.successCnt eq 0}">0.0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${returnCnt / campaignSendResultVo.successCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <c:if test="${returnCnt >0}">
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${campaignReportBasicVo.scenarioNo},${campaignReportBasicVo.campaignNo},'returnMail'); return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </c:if>
                        </td>
                        <td class="text-right">
                            <fmt:formatNumber type="number" value="${rejectCnt}" />
                        </td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${campaignSendResultVo.successCnt eq 0}">0.0%</c:when>
                                <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${rejectCnt / campaignSendResultVo.successCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <c:if test="${rejectCnt >0}">
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${campaignReportBasicVo.scenarioNo},${campaignReportBasicVo.campaignNo},'reject'); return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </c:if>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div><!-- e. 수신확인결과 card-body -->
        </div><!-- e. 수신확인결과 card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>
</body>
</html>