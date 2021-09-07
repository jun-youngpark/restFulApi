<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 수신거부 분석(메일)<br/>
 * - URL : /report/campaign/rejectRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignRejectSummaryController<br/>
 * - 이전 파일명 : reject_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.title" /></title><!-- 캠페인 리포트 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 엑셀다운로드 버튼 클릭
        $('#downloadBtn').on("click", function(event) {
            $("#errorCd").val(errorCd);
            $("#campaignReportFrm").attr('action', '/report/campaign/excelRejectSummary.do').submit();
        });
    }
</script>
</head>

<body>
<form name="campaignReportFrm" id="campaignReportFrm" method="post" action="/report/campaign/excelRejectSummary.do">
    <input type="hidden" name="campaignNo" value="${param.campaignNo}" />
    <input type="hidden" name="scenarioNo" value="${param.scenarioNo}" />
</form>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.reject"/></h3><!-- 수신거부 분석 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.campaign.head.alt.reject"/></h4><!-- 수신거부 분석 -->
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" rowspan="2"><spring:message code="report.campaign.list.title.name" /></th><!-- 캠페인명 -->
                            <th scope="col" rowspan="2" width="15%"><spring:message code="report.campaign.list.title.sendcnt" /></th><!-- 발송건수 -->
                            <th scope="col" colspan="2" width="25%"><spring:message code="report.campaign.list.title.reject" /></th><!-- 수신거부 -->
                            <th scope="col" rowspan="2" width="80"><spring:message code="report.campaign.list.title.download" /></th><!-- 다운로드 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 수신거부 : 건수 -->
                            <th scope="col">%</th><!-- 수신거부 : % -->
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="loop" items="${campaignReportRejectList}"><!-- 일반 데이터 -->
                        <tr>
                            <td class="text-left">${loop.scenarioNm}</td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.rejectCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.rejectCnt / loop.sendCnt * 100}" pattern="##.#"/>%</td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" id="downloadBtn">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:forEach>

                        <c:if test="${fn:length(campaignReportRejectList) eq 0}">
                        <tr>
                            <td colspan="7"><spring:message code="report.campaign.list.reject.nodata" /></td><!-- 수신거부 데이터가 존재하지 않습니다. -->
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div>
</body>
</html>
