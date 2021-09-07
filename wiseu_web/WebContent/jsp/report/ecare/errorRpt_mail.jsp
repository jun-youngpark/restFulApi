<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석 (메일)
 * - URL : /report/ecare/errorRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareErrSummaryController
 * - 이전 파일명 : err_summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title.error_${webExecMode}" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<script type="text/javascript">
    // 이케어 코드 별 도메인 리포트 팝업
    function popupDomainRptByErrorCd(scenarioNo, ecareNo, resultSeq, serviceType, subType, reportDt, errorCd) {
        // /report/ecare/err_bycode_popup.do
        var url = "/report/ecare/errorCodeRptPopup.do?scenarioNo=" + scenarioNo + "&ecareNo=" + ecareNo + "&resultSeq=" + resultSeq
                + "&serviceType=" + serviceType + "&subType=" + subType + "&reportDt=" + reportDt + "&searchErrCd=" + errorCd;
        $.mdf.popupGet(url, 'domainRptPopup', 450, 500);
    }

    // 도메인별 발송 오류 현황 팝업
    function popupErrorRptByDomain(scenarioNo, ecareNo, serviceType, subType, resultSeq, reportDt, domainNm) {
        // /report/ecare/err_bydomain_popup.do
        var url = "/report/ecare/errorDomainRptPopup.do?scenarioNo=" + scenarioNo + "&ecareNo=" + ecareNo + "&serviceType=" + serviceType + "&subType=" + subType
                + "&resultSeq=" + resultSeq + "&reportDt=" + reportDt + "&searchDomainNm=" + domainNm;
        $.mdf.popupGet(url, 'domainRptPopup', 850, 700);
    }
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
            <div class="card-body px-0 pb-0">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.head.alt.error"/></h4><!-- 오류분석 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable table-hover">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="100"><spring:message code="report.ecare.title.gubun" /></th><!-- 구분 -->
                            <th scope="col"><spring:message code="report.ecare.bounce.detail" /></th><!-- 세부 Bounce 내용 -->
                            <th scope="col" width="15%"><spring:message code="report.ecare.title.count" /></th><!-- 건수 -->
                            <th scope="col" width="10%">%</th>
                            <th scope="col" width="10%"><spring:message code="report.ecare.title.share" /></th><!-- 점유율 -->
                        </tr>
                        </thead>

                        <tbody>
                        <c:if test="${totalBounceCnt > 0}">

                        <c:if test="${totalSoftBounceCnt ne 0}">
                        <c:forEach var="domainLogVo" items="${domainResultMsgListBySoft}" varStatus="num">
                        <tr style="cursor: pointer;" onclick="popupDomainRptByErrorCd('${scenarioInfoVo.scenarioNo}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.resultSeq}','${scenarioInfoVo.ecareInfoVo.serviceType}','${scenarioInfoVo.ecareInfoVo.subType}','${scenarioInfoVo.ecareInfoVo.reportDt}','${domainLogVo.errorCd}')">
                            <c:if test="${num.count eq 1}">
                            <td rowspan="${domainResultMsgListBySoftSize+1}" onclick="event.cancelBubble=true">Soft Bounce</td><!-- Soft Bounce -->
                            </c:if>

                            <td class="text-left">${domainLogVo.errorCd} : ${domainLogVo.errorDesc}</td>
                            <td class="text-right">${domainLogVo.targetCnt}</td>
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/ecareSendResultVo.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/totalSoftBounceCnt}" /></td>
                        </tr>
                        </c:forEach>

                        <tr class="bg-subtotal">
                           <td><span class="text-warning"><spring:message code="report.subtotal" /></span></td><!-- 소계 -->
                           <td class="text-right"><fmt:formatNumber type="number" value="${totalSoftBounceCnt}" /></td>
                           <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${totalSoftBounceCnt/ecareSendResultVo.sendCnt}" /></td>
                           <td class="text-right">100%</td>
                        </tr>
                        </c:if>

                        <c:if test="${totalHardBounceCnt ne 0}">
                        <c:forEach var="domainLogVo" items="${domainResultMsgListByHard}" varStatus="num">
                        <tr style="cursor: pointer;" onclick="popupDomainRptByErrorCd('${scenarioInfoVo.scenarioNo}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.resultSeq}','${scenarioInfoVo.ecareInfoVo.serviceType}','${scenarioInfoVo.ecareInfoVo.subType}','${scenarioInfoVo.ecareInfoVo.reportDt}','${domainLogVo.errorCd}')">
                            <c:if test="${num.count eq 1}">
                            <td rowspan="${domainResultMsgListByHardSize+1}" onclick="event.cancelBubble=true">Hard Bounce</td><!-- Hard Bounce -->
                            </c:if>

                            <td class="text-left">${domainLogVo.errorCd} : ${domainLogVo.errorDesc}</td>
                            <td class="text-right"><fmt:formatNumber value="${domainLogVo.targetCnt}" type="number"/></td>
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/ecareSendResultVo.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/totalHardBounceCnt}" /></td>
                        </tr>
                        </c:forEach>

                        <tr class="bg-subtotal">
                           <td><span class="text-warning"><spring:message code="report.subtotal" /></span></td><!-- 소계 -->
                           <td class="text-right"><fmt:formatNumber type="number" value="${totalHardBounceCnt}" /></td>
                           <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${totalHardBounceCnt/ecareSendResultVo.sendCnt}" /></td>
                           <td class="text-right">100%</td>
                        </tr>
                        </c:if>

                        <tr class="bg-secondary">
                            <td colspan="2"><spring:message code="report.total" /></td><!-- 합계 -->
                            <td class="text-right"><fmt:formatNumber value="${totalBounceCnt}" type="number"/></td>
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${totalBounceCnt/ecareSendResultVo.sendCnt}" /></td>
                            <td class="text-right">100%</td>
                        </tr>
                        </c:if>

                        <c:if test="${totalBounceCnt eq 0}">
                        <tr>
                            <td colspan="5"><spring:message code="report.campaign.err.nodata" /></td><!-- 오류 데이터가 존재하지 않습니다. -->
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->

        <div class="card">
            <div class="card-body px-0 pb-0">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.domain.title.alt.1"/></h4><!-- 도메인별 발송 오류 현황 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable table-hover">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" rowspan="2" width="40"><spring:message code="report.ecare.title.no" /></th><!-- NO -->
                            <th scope="col" rowspan="2"><spring:message code="report.ecare.title.domain" /></th><!-- 도메인 -->
                            <th scope="col" colspan="2"><spring:message code="report.ecare.title.target.Count" /></th><!-- 대상수 -->
                            <th scope="col" colspan="2"><spring:message code="report.ecare.title.success" /></th><!-- 발송성공 -->
                            <th scope="col" colspan="3"><spring:message code="report.ecare.title.softbounce" /></th><!-- Soft Bounce -->
                            <th scope="col" colspan="3"><spring:message code="report.ecare.title.hardbounce" /></th><!-- Hard Bounce -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 대상수 : 건수 -->
                            <th scope="col">%</th><!-- 대상수 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- Soft Bounce : 건수 -->
                            <th scope="col">%</th><!-- Soft Bounce : % -->
                            <th scope="col"><spring:message code="report.ecare.title.share" /></th><!-- Soft Bounce : 점유율 -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- Hard Bounce : 건수 -->
                            <th scope="col">%</th><!-- Hard Bounce : % -->
                            <th scope="col"><spring:message code="report.ecare.title.share" /></th><!-- Hard Bounce : 점유율 -->
                        </tr>
                        </thead>

                        <c:if test="${!empty domainLogList}">
                        <tbody>
                        <c:forEach var="domainLogVo" items="${domainLogList}" varStatus="num">
                        <tr style="cursor: pointer;" onclick="popupErrorRptByDomain('${scenarioInfoVo.scenarioNo}','${scenarioInfoVo.ecareInfoVo.ecareNo}','${scenarioInfoVo.ecareInfoVo.serviceType}','${scenarioInfoVo.ecareInfoVo.subType}','${scenarioInfoVo.ecareInfoVo.resultSeq}','${scenarioInfoVo.ecareInfoVo.reportDt}','${domainLogVo.domainNm}')">
                            <td>${num.count}</td><!-- NO -->
                            <td class="text-left"><!-- 도메인 -->
                                <c:choose>
                                    <c:when test="${domainLogVo.domainNm ne 'ZZZ.DOMAIN'}">${domainLogVo.domainNm}</c:when>
                                    <c:otherwise><spring:message code="report.ecare.title.etc" /><!-- 기타 --></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${domainLogVo.targetCnt}"/></td><!-- 대상수 : 건수 -->
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/ecareSendResultVo.sendCnt}" /></td><!-- 대상수 : % -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${domainLogVo.successCnt}"/></td><!-- 발송성공 : 건수 -->
                            <td class="text-right"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.successCnt/domainLogVo.targetCnt}" /></td><!-- 발송성공 : % -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${domainLogVo.softCnt}" /></td><!-- Soft Bounce : 건수 -->
                            <td class="text-right"><!-- Soft Bounce : % -->
                                <c:choose>
                                    <c:when test="${domainLogVo.targetCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.softCnt/domainLogVo.targetCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- Soft Bounce : 점유율 -->
                                <c:choose>
                                    <c:when test="${totalSoftBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.softCnt/totalSoftBounceCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${domainLogVo.hardCnt}" /></td><!-- Hard Bounce : 건수 -->
                            <td class="text-right"><!-- Hard Bounce : % -->
                                <c:choose>
                                    <c:when test="${domainLogVo.targetCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.hardCnt/domainLogVo.targetCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><!-- Hard Bounce : 점유율 -->
                                <c:choose>
                                    <c:when test="${totalHardBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.hardCnt/totalHardBounceCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>

                        <tfoot class="bg-secondary">
                        <tr>
                            <td colspan="2"><spring:message code="report.total" /></td><!-- 합계 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}"/></td>
                            <td class="text-right">100%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.successCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${ecareSendResultVo.successCnt/ecareSendResultVo.sendCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSoftBounceCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSoftBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${totalSoftBounceCnt/ecareSendResultVo.sendCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">100%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalHardBounceCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSoftBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="percent" maxFractionDigits="1" value="${totalHardBounceCnt/ecareSendResultVo.sendCnt}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">100%</td>
                        </tr>
                        </tfoot>
                        </c:if>

                        <c:if test="${empty domainLogList}">
                        <tbody>
                        <tr>
                            <td colspan="7"><spring:message code="report.ecare.summary.errdomain.nodata" /></td><!-- 오류분석 데이터가 존재하지 않습니다. -->
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
