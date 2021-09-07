<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 리턴메일 분석(메일)<br/>
 * - URL : /report/campaign/returnMailRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignReportReturnMailController<br/>
 * - 이전 파일명 : rtn_summary_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.title" /></title><!-- 캠페인 리포트 -->
<script type="text/javascript">
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
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.return"/></h3><!-- 리턴메일 분석 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body pb-0 px-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.head.alt.return" /></h4><!-- 리턴메일 분석 -->
                    </div>
                </div>

                <div class="col-12 alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.return.help" /><!-- 메일이 반송된 원인과 현황을 파악할 수 있습니다. -->
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="100"><spring:message code="report.campaign.list.gubun" /></th><!-- 구분 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.bounce" /></th><!-- 세부 BOUNCE 내용 -->
                            <th scope="col" width="15%"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 건수 -->
                            <th scope="col" width="10%">%</th>
                        </tr>
                        </thead>

                        <c:if test="${fn:length(campaignReportReturnMailDetailList) > 0}">
                        <tbody>
                        <c:forEach var="loop" items="${campaignReportReturnMailDetailList}">
                        <c:if test="${loop.gid eq 0}"><!-- 일반 데이터 -->
                        <tr>
                            <td>${loop.errorGubun}</td>
                            <td class="text-left">${loop.smtpCode} : ${loop.errorMsg}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.sendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.sendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                               </c:choose>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${loop.gid eq 1}"><!-- 소계 -->
                        <tr>
                            <td>${loop.errorGubun}</td>
                            <td><spring:message code="report.subtotal" /></strong></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.sendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.sendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${loop.gid eq 3}"><!-- 합계 -->
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="2"><spring:message code="report.total" /></td>
                            <td><fmt:formatNumber type="number" value="${loop.sendCnt}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${(campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.sendCnt / (campaignReportBasicVo.sendCnt - campaignReportBasicVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                               </c:choose>
                            </td>
                        </tr>
                        </tfoot>
                        </c:if>
                        </c:forEach>
                        </c:if>

                        <c:if test="${fn:length(campaignReportReturnMailDetailList) eq 0}">
                        <tbody>
                        <tr>
                            <td colspan="7"><spring:message code="report.campaign.list.return.nodata" /></td><!-- 리턴 메일 데이터가 존재하지 않습니다. -->
                        </tr>
                        </tbody>
                        </c:if>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div>
</body>
</html>
