<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 도메인별 분석(메일)<br/>
 * - URL : /report/campaign/domainRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignDomainController <br/>
 * - 이전 파일명 : domain_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.title" /></title><!-- 캠페인 리포트 -->
</head>
<script>
function downloadExcel(errorCd,domainNm) {
    $("#errorCd").val(errorCd);
    $("#domainNm").val(domainNm);
    $("#campaignReportFrm").attr('action', '/report/campaign/excelDomainSummary.do').submit();
 }
</script>
<body>
<form name="campaignReportFrm" id="campaignReportFrm" method="post" action="/report/campaign/excelDomainSummary.do">
<input type="hidden" name="campaignNo" value="${param.campaignNo}" />
<input type="hidden" name="scenarioNo" value="${param.scenarioNo}" />
<input type="hidden" name="domainNm" id="domainNm" />
<input type="hidden" name="errorCd" id="errorCd" />
</form>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.domain.title.alt.1"/></h3><!-- 도메인별 분석 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <div class="card mb-0">
            <div class="card-body pb-0 px-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.domain.title.alt.1" /></h4><!-- 도메인별 발송 오류 현황 -->
                    </div>
                </div>

                <div class="col-12 alert alert-secondary mb-0" role="alert">
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.domain.help" /><br/>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" rowspan="2" width="25%"><spring:message code="report.campaign.domain.title.domain" /></th><!-- 도메인 -->
                            <th scope="col" rowspan="2" width="80"><spring:message code="report.campaign.domain.title.errorcode" /></th><!-- 에러코드 -->
                            <th scope="col" rowspan="2"><spring:message code="report.campaign.domain.title.detail" /></th><!-- 상세내용  -->
                            <th scope="col" colspan="2" width="20%"><spring:message code="report.campaign.result.comparison.send.count" /></th><!-- 발송수 -->
                            <th scope="col" rowspan="2" width="80"><spring:message code="report.campaign.domain.title.download" /></th><!-- 다운로드 -->
                        </tr>
                        <tr>
                            <th scope="col" width="100"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 건수 -->
                            <th scope="col" width="80">%</th><!-- % -->
                        </tr>
                        </thead>

                        <c:if test="${fn:length(campaignReportDomainList) > 0}">
                        <c:set var="domainNm" value="" scope="page"/>
                        <tbody>
                        <c:forEach var="loop" items="${campaignReportDomainList}">
                        <c:if test="${loop.gid eq 0}"><!-- 일반 데이타  -->
                        <tr>
                            <td class="text-left">
                                <c:choose>
                                    <c:when test="${loop.domainNm ne 'ZZZ.DOMAIN'}">
                                        <c:if test="${loop.domainNm ne domainNm}">${loop.domainNm}</c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${loop.domainNm ne domainNm}"><spring:message code="report.campaign.err.bycode.etc" /></c:if>
                                    </c:otherwise>
                                </c:choose>
                            <c:set var="domainNm" value="${loop.domainNm}"/>
                            </td>
                            <td>${loop.errorCd}</td>
                            <td class="text-left">${loop.errorMsg}</td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt / loop.domainSendCnt * 100}" pattern="##.#" />%</td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="downloadExcel('${loop.errorCd}','${loop.domainNm}'); return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${loop.gid eq 1}">
                        <tr class="bg-subtotal"><!-- 소계  -->
                            <td colspan="3" scope="row"><span class="text-warning"><spring:message code="report.subtotal" /></span></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt / loop.domainSendCnt * 100}" pattern="##.#" />%</td>
                            <td></td>
                        </tr>
                        </c:if>

                        <c:if test="${loop.gid eq 3}">
                        </tbody>
                        <tfoot class="bg-secondary"><!-- 합계  -->
                        <tr>
                            <td colspan="3"><spring:message code="report.total" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber value="${loop.sendCnt / campaignReportBasicVo.targetCnt * 100}" pattern="##.#" />%</td>
                            <td></td>
                        </tr>
                        </tfoot>
                        </c:if>
                        </c:forEach>
                        </c:if>

                        <c:if test="${fn:length(campaignReportDomainList) eq 0}">
                        <tbody>
                        <tr>
                            <td colspan="6"><spring:message code="report.campaign.domain.list.nodata" /></td><!-- 도메인 분석 데이터가 존재하지 않습니다. -->
                        </tr>
                        </tbody>
                        </c:if>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->

</body>
</html>
