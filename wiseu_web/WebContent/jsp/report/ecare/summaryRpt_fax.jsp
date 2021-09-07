<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 요약분석(FAX)
 * - URL : /report/ecare/summaryRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareSummaryController
 * - 이전 파일명 : fax_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title.summary_${webExecMode}" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<script type="text/javascript">
</script>
</head>
<body>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.ecare.title_${webExecMode}"/> - <spring:message code="report.campaign.head.alt.summary"/></h3><!-- 이케어 리포트 - 요약분석 -->
            </div>
             <%@ include file="/jsp/report/ecare/ecareRptTab_inc.jsp"%><!-- /jsp/report/ecare/ecare_report_tab_inc.jsp -->
        </div>

        <div class="card">
            <div class="card-body">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.ecare.title.sendresult"/></h4><!-- 요약분석 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.ecare.title.name_${webExecMode}" /></th><!-- 이케어명 -->
                            <th rowspan="2" scope="col"><spring:message code="report.ecare.title.target.Count" /></th><!-- 대상수 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.send.count" /></th><!-- 발송수 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.title.success" /></th><!-- 발송성공 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendfail" /></th><!-- 발송실패 -->
                            <th rowspan="2" scope="col" width="80"><spring:message code="report.ecare.title.send.date" /></th><!-- 발송일시 -->
                            <c:if test="${webExecMode eq '1' }">
                            <th rowspan="2" scope="col" width="40"><spring:message code="report.ecare.success" /></th><!-- 성공 -->
                            <th rowspan="2" scope="col" width="40"><spring:message code="report.ecare.fail" /></th><!-- 실패 -->
                            </c:if>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송수 : 건수 -->
                            <th scope="col">%</th><!-- 발송수 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송실패 : 건수 -->
                            <th scope="col">%</th><!-- 발송실패 : % -->
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <td class="text-left">${scenarioInfoVo.ecareInfoVo.ecareNm}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.sendCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.successCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.successCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.softBounceCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.softBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.softBounceCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${ecareSendResultVo.sendStartDtToSimpleDateStr}</td>

                            <c:if test="${webExecMode eq '1' }">
                            <td>
                                <c:if test="${ecareSendResultVo.successCnt > 0}">
                                   <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_success&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}';">
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${(ecareSendResultVo.softBounceCnt + ecareSendResultVo.hardBounceCnt) > 0 }">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_fail&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}';">
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            </c:if>
                        </tr>

                        <c:if test="${!empty postEcareSendResultVo}">
                        <tr>
                            <td>${postEcareSendResultVo.startDateYear}.${postEcareSendResultVo.startDateMonth}.${postEcareSendResultVo.startDateDay}</td>
                            <td><spring:message code="report.ecare.before.ecare_${webExecMode}" /></td><!-- 직전 이케어 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${postEcareSendResultVo.targetCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${postEcareSendResultVo.sendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${postEcareSendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${postEcareSendResultVo.sendCnt / postEcareSendResultVo.targetCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${postEcareSendResultVo.successCnt}" /></td>
                            <td class="text-right">
                               <c:choose>
                                    <c:when test="${postEcareSendResultVo.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${postEcareSendResultVo.successCnt / postEcareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${postEcareSendResultVo.softBounceCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${postEcareSendResultVo.softBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${postEcareSendResultVo.softBounceCnt / postEcareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${postEcareSendResultVo.startDateYear}.${postEcareSendResultVo.startDateMonth}.${postEcareSendResultVo.startDateDay}</td>
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
