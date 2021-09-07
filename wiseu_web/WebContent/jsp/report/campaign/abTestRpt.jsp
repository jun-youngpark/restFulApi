<%-------------------------------------------------------------------------------------------------
 * - 캠페인 A/B 분석 리포트 <br/>
 * - URL : /report/campaign/abTestRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignAbTestSummaryController  <br/>
 * - 이전 파일명 : ab_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.title" /></title>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.abtest"/></h3><!-- A/B 테스트 분석 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body pb-0 px-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.head.alt.abtest" /></h4><!-- A/B 테스트 분석 -->
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col">
                                <spring:message code="report.campaign.ab.target" /><!-- A/B 비교 대상 -->
                                <c:choose>
                                    <c:when test="${abTestType eq 'S' }"><spring:message code="report.campaign.ab.sub" /><!-- (제목) --></c:when>
                                    <c:otherwise><spring:message code="report.campaign.ab.temp" /><!-- (템플릿) --></c:otherwise>
                                </c:choose>
                            </th>
                            <th rowspan="2" scope="col" width="15%">A/B<spring:message code="report.campaign.list.title.sendcnt" /></th><!-- A/B발송건수 -->
                            <th colspan="2" scope="col" width="25%"><!-- A/B링크클릭 -->
                                <c:choose>
                                    <c:when test="${abTestCond eq 'O' }">A/B<spring:message code="report.campaign.list.title.open" /><!-- 수신확인 --></c:when>
                                    <c:otherwise>A/B<spring:message code="report.campaign.graph.title.linkclick" /><!-- 링크클릭 --></c:otherwise>
                                </c:choose>
                            </th>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 건수 -->
                            <th scope="col">%</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="loop" items="${campaignReportAbTestList}" varStatus="status"><!-- 일반 데이타  -->
                        <tr>
                            <td class="text-left">${loop.abType}</td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.openCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.totRate}" pattern="##.#"/>%</td>
                        </tr>
                        </c:forEach>

                        <c:if test="${fn:length(campaignReportAbTestList) eq 0}">
                        <tr>
                            <td colspan="4"><spring:message code="report.campaign.ab.noData" /></td><!-- 데이터가 존재하지 않습니다. -->
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- //Light table -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col" width="100"><spring:message code="report.campaign.max.type" /></th><!-- 최종 발송 결과 -->
                            <th rowspan="2" scope="col" width="15%"><spring:message code="report.campaign.template" /></th><!-- 캠페인 템플릿 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.head.title.subject" /></th><!-- 메일제목 -->
                            <th rowspan="2" scope="col" width="15%"><spring:message code="report.campaign.list.title.sendcnt" /></th><!-- 발송건수 -->
                            <th colspan="2" scope="col" width="25%"><!-- 링크클릭 -->
                                <c:choose>
                                    <c:when test="${abTestCond eq 'O' }"><spring:message code="report.campaign.list.title.open" /></c:when>
                                    <c:otherwise><spring:message code="report.campaign.graph.title.linkclick" /></c:otherwise>
                                </c:choose>
                            </th>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 링크클릭 : 건수 -->
                            <th scope="col">%</th><!-- 링크클릭 : % -->
                        </tr>
                        </thead>

                        <tbody>
                        <c:if test="${fn:length(campaignReportAbTestList) ne 0}">
                        <tr>
                            <td>${campaignAbTestVo.abType }</td>
                            <td><img id="myImg" src="/images${lang}/preview/${campaignNo}_${campaignAbTestVo.abType}.png" width="100px" height="200px"></td>
                            <td class="text-left">
                                <c:choose>
                                    <c:when test="${abTestType eq 'S' }">
                                        <c:choose>
                                            <c:when test="${campaignAbTestVo.abType eq 'A'}">${campaignReportBasicVo.campaignPreface}</c:when>
                                            <c:otherwise>${campaignReportBasicVo.campaignPrefaceAb}</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>${campaignReportBasicVo.campaignPreface}</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber value="${campaignReportBasicVo.sendCnt - campaignReportBasicVo.abTestTarget}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${realOpenLinkCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(campaignReportBasicVo.sendCnt - campaignReportBasicVo.abTestTarget) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${realOpenLinkCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.abTestTarget) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${fn:length(campaignReportAbTestList) eq 0}">
                        <tr>
                            <td colspan="6" style="text-align:center;">
                                <spring:message code="report.campaign.ab.noData" /><!-- 데이터가 존재하지 않습니다. -->
                            </td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->

</body>
</html>
