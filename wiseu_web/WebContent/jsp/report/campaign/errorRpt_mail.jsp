<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 오류 분석(메일)<br/>
 * - URL : /report/campaign/errorRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignErrSummaryController <br/>
 * - 이전 파일명 : mail_err_summary_view.jsp
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
        initPage();
    });

    function initPage() {
        viewChart();
    }

    function viewChart() {
        var $trArray = $("#rptTableBody").children();
        var softItemArray = [];
        var softValueArray = [{
            "name" : '<spring:message code="report.campaign.list.title.sendfail"/>',  // 발송실패
            "data" : []
        }];
        var hardItemArray = [];
        var hardValueArray = [{
            "name" : '<spring:message code="report.campaign.list.title.sendfail"/>',  // 발송실패
            "data" : []
        }];

        var prevErrorGubun;
        $.each($trArray, function() {
            var $tdArray = $(this).children();
            var errorGubun = $.trim($tdArray.eq(0).text());
            if(errorGubun == "") {
                errorGubun = prevErrorGubun;
            }

            if(errorGubun == "Soft Bounce") {
                softItemArray.push($.trim($tdArray.eq(1).text()));
                softValueArray[0].data.push(parseInt($tdArray.eq(3).text()));
            } else if(errorGubun == "Hard Bounce") {
                hardItemArray.push($.trim($tdArray.eq(1).text()));
                hardValueArray[0].data.push(parseInt($tdArray.eq(3).text()));
            }

            prevErrorGubun = errorGubun;
        });

        var softChart = new mdf.Chart('softChartDiv', '<spring:message code="report.campaign.graph.title.softbounce"/>', '<spring:message code="report.count.unit"/>');  // 건수 (건)
        softChart.renderChart("VER_BAR", softItemArray, softValueArray);

        var hardChart = new mdf.Chart('hardChartDiv', '<spring:message code="report.campaign.graph.title.hardbounce"/>', '<spring:message code="report.count.unit"/>');  // 건수 (건)
        hardChart.renderChart("VER_BAR", hardItemArray, hardValueArray);
    }

    function downloadExcel(errorCd) {
        $("#errorCd").val(errorCd);
        $("#campaignReportFrm").attr('action', '/report/campaign/excelErrSummary.do').submit();
    }

    function popupDomainRptByErrorCd(scenarioNo, campaignNo, errorCd) {
        var url = "/report/campaign/errorCodeRptPopup.do?scenarioNo=" + scenarioNo + "&campaignNo=" + campaignNo + "&searchErrCd=" + errorCd;  // /report/campaign/err_bycode_popup.do
        $.mdf.popupGet(url, 'domainRptPopup', 450, 500);
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
                    <table class="table table-sm dataTable table-hover">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="100"><spring:message code="report.campaign.list.gubun" /></th><!-- 구분 -->
                            <th scope="col" width="80"><spring:message code="report.campaign.domain.title.errorcode" /></th><!-- 에러코드 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.bounce" /></th><!-- 세부 BOUNCE 내용 -->
                            <th scope="col" width="15%"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 건수 -->
                            <th scope="col" width="11%">% <spring:message code="report.campaign.err.send.compare" /></th><!-- % (발송대비) -->
                            <th scope="col" width="10%"><spring:message code="report.campaign.err.send.share" /></th><!-- 점유율 -->
                            <th scope="col" width="80"><spring:message code="report.campaign.list.title.download" /></th><!-- 다운로드 -->
                        </tr>
                        </thead>

                        <c:set var="errorGubun" scope="page" value=""/>
                        <c:if test="${fn:length(campaignReportErrorList) > 0}">
                        <tbody id="rptTableBody">
                        <c:forEach var="loop" items="${campaignReportErrorList}">
                        <c:if test="${loop.gid eq 0}">
                        <c:set var="totalSendCnt" value="${totalSendCnt + loop.sendCnt}"/>
                        <tr style="cursor: pointer;" onclick="popupDomainRptByErrorCd('${campaignReportBasicVo.scenarioNo}', '${campaignReportBasicVo.campaignNo}', '${loop.errorCd}')">
                            <td><c:if test="${loop.errorGubun ne errorGubun}">${loop.errorGubun}</c:if></td>
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
                                    <c:otherwise><fmt:formatNumber type="number" value="${loop.sendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td onclick="event.cancelBubble=true">
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
                            <td colspan="3" scope="row"><spring:message code="report.total" /></td>
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
                                    <c:otherwise><fmt:formatNumber type="number" value="${totalSendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${fn:length(campaignReportErrorList) eq 0}">
                        <tbody>
                        <tr>
                            <td colspan="7"><spring:message code="report.campaign.err.nodata" /></td><!-- 오류 데이터가 존재하지 않습니다. -->
                        </tr>
                        </tbody>
                        </c:if>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->

            <div class="card-body mt-5" id="softGrapicDiv">
                <div id="softChartDiv" class="chart"/>
            </div>

            <div class="card-body mt-5" id="hardGrapicDiv">
                <div id="hardChartDiv" class="chart"/>
            </div>
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div>

</body>
</html>
