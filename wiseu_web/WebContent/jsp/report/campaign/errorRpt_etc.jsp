<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 오류 분석(메일 외 채널)<br/>
 * - URL : /report/campaign/errorRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignErrSummaryController <br/>
 * - 이전 파일명 : etc_err_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.err.title" /></title><!-- 캠페인 오류 리포트 -->
<%@ include file="/jsp/include/pluginChart.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $('A[rel="code"]').on("click", function(event) {
            $.mdf.popupGet($(this).attr('href'), 'codePopup', 400, 500);
            return false;
        });
    }

    function initPage() {
        viewChart();
    }

    function viewChart() {
        var $trArray = $("#rptTableBody").children();
        var itemArray = $.mdf.getTableCellValueArray($trArray, 0, false);
        var valueArray = [{
                "name" : '<spring:message code="report.campaign.list.title.sendfail"/>',  // 발송실패
                "data" : $.mdf.getTableCellValueArray($trArray, 2, true)
            }
        ];

        var chart = new mdf.Chart('chartDiv', null, '<spring:message code="report.count.unit"/>');  // 건수 (건)
        chart.renderChart("VER_BAR", itemArray, valueArray);
    }

    function downloadExcel(errorCd) {
        $("#errorCd").val(errorCd);
        $("#campaignReportFrm").attr('action', '/report/campaign/excelErrSummary.do').submit();
    }
</script>
</head>

<body>
<form name="campaignReportFrm" id="campaignReportFrm" method="post" action="/report/campaign/excelErrSummary.do">
<input type="hidden" name="campaignNo" value="${param.campaignNo}" />
<input type="hidden" name="scenarioNo" value="${param.scenarioNo}" />
<input type="hidden" name="channelType" value="${param.channelType}" />
<input type="hidden" name="resultSeq" id="resultSeq" value="${param.resultSeq}" />
<input type="hidden" name="errorCd" id="errorCd" />
</form>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.error" /></h3><!-- 오류분석 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body pb-0 px-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.head.alt.error" /></h4><!-- 오류분석 -->
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="80"><spring:message code="report.campaign.domain.title.errorcode1" /></th><!-- 오류코드 -->
                            <th scope="col"><spring:message code="report.campaign.domain.title.errorname" /></th><!-- 오류 설명 -->
                            <th scope="col" width="15%"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 건수 -->
                            <th scope="col" width="11%">% <spring:message code="report.campaign.err.send.compare" /></th><!-- % (발송대비) -->
                            <th scope="col" width="10%"><spring:message code="report.campaign.err.send.share" /></th><!-- 점유율 -->
                            <th scope="col" width="80"><spring:message code="report.campaign.list.title.download" /></th><!-- 다운로드 -->
                        </tr>
                        </thead>

                        <c:if test="${fn:length(campaignReportErrorList) > 0}">
                        <tbody id="rptTableBody">
                        <c:forEach var="loop" items="${campaignReportErrorList}">
                        <c:set var="totalSendCnt" value="${totalSendCnt + loop.sendCnt}"/>
                        <c:if test="${loop.gid eq 0}">
                        <tr>
                            <td>${loop.errorCd}</td>
                            <td class="text-left">${loop.errorMsg}</td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${campaignReportBasicVo.sendCnt eq 0}">0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" value="${loop.sendCnt / campaignReportBasicVo.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.sendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="downloadExcel('${loop.errorCd}'); return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:if>
                        </c:forEach>
                        </tbody>
                        <tfoot class="bg-secondary">
                        <tr>
                            <td colspan="2" scope="row"><spring:message code="report.total" /></td><!-- 합계 -->
                            <td class="text-right"><fmt:formatNumber value="${totalSendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${campaignReportBasicVo.sendCnt eq 0}">0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" value="${totalSendCnt / campaignReportBasicVo.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${totalSendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${fn:length(campaignReportErrorList) eq 0}">
                        <tbody>
                        <tr>
                            <td colspan="6"><spring:message code="report.campaign.err.nodata" /></td><!-- 오류 데이터가 존재하지 않습니다. -->
                        </tr>
                        </tbody>
                        </c:if>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->
        </div><!-- e.card -->

        <div class="card-body mt-5"><!-- 차트 영역 -->
            <div id="chartDiv" class="chart"/>
        </div>
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>
