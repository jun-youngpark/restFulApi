<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석 (FAX)
 * - URL : /report/ecare/errorRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareErrSummaryController
 * - 이전 파일명 : fax_err_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title.error_${webExecMode}" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<script type="text/javascript">
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.ecare.title_${webExecMode}"/> - <spring:message code="report.campaign.head.alt.error"/></h3><!-- 이케어 리포트 - 오류분석 -->
            </div>
            <%@ include file="/jsp/report/ecare/ecareRptTab_inc.jsp"%><!-- /jsp/report/ecare/ecare_report_tab_inc.jsp -->
        </div>

        <div class="card">
            <div class="card-body">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.head.alt.error"/></h4><!-- 오류분석 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="80"><spring:message code="report.campaign.domain.title.errorcode1" /></th><!-- 오류코드 -->
                            <th scope="col"><spring:message code="report.campaign.domain.title.errorname" /></th><!-- 오류 설명 -->
                            <th scope="col" width="15%"><spring:message code="report.ecare.title.count" /></th><!-- 건수 -->
                            <th scope="col" width="10%">%</th>
                            <th scope="col" width="10%"><spring:message code="report.ecare.title.share" /></th><!-- 점유율 -->
                        </tr>
                        </thead>

                        <c:if test="${!empty domainResultMsgListBySoft}">
                        <tbody>
                        <c:forEach var="domainLogVo" items="${domainResultMsgListBySoft}" varStatus="num">
                        <tr>
                            <td>${domainLogVo.errorCd}</td>
                            <td class="text-left">${domainLogVo.errorDesc}</td>
                            <td class="text-right">${domainLogVo.targetCnt}</td>
                            <!-- % : 전체 발송수 중 해당 오류가 차지하는 비율 -->
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt ne 0}">
                                        <fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/ecareSendResultVo.sendCnt}" />
                                    </c:when>
                                    <c:otherwise>0%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.softBounceCnt ne 0}">
                                        <fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/ecareSendResultVo.softBounceCnt}" />
                                    </c:when>
                                    <c:otherwise>0%</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>

                        <tfoot class="bg-secondary">
                        <tr>
                            <td colspan="2"><spring:message code="report.total" /></td><!-- 합계 -->
                            <td class="text-right"><fmt:formatNumber value="${ecareSendResultVo.softBounceCnt}" type="number"/></td>
                            <td class="text-right">
                                <!-- % : 전체 발송수 중 해당 오류가 차지하는 비율 -->
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt ne 0}">
                                        <fmt:formatNumber type="percent" maxFractionDigits="1" value="${ecareSendResultVo.softBounceCnt/ecareSendResultVo.sendCnt}" />
                                    </c:when>
                                    <c:otherwise>0%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${empty domainResultMsgListBySoft}">
                        <tbody>
                        <tr>
                            <td colspan="5"><spring:message code="report.ecare.summary.errdomain.nodata" /></td><!-- 오류분석 데이터가 존재하지 않습니다. -->
                        </tr>
                        </tbody>
                        </c:if>

                        </table>
                    </div><!-- //Light table -->
                </div><!-- e.card body -->
            </div><!-- e.card -->
        </div><!-- e.page content -->
    </div><!-- e.main-panel -->
</body>
</html>
