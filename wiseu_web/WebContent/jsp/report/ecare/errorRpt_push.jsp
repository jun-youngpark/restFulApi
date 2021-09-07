<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석 (PUSH)
 * - URL : /report/ecare/errorRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareErrSummaryController
 * - 이전 파일명 : push_err_summary_view.jsp
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
            <div class="card-body px-0 pb-0">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.head.alt.error"/></h4><!-- title -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" width="120"><spring:message code="report.campaign.list.gubun" /></th><!-- 구분 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.bounce" /></th><!-- 세부 Bounce 내용 -->
                            <th scope="col" width="15%"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 건수 -->
                            <th scope="col" width="10%">% <spring:message code="report.campaign.err.send.compare" /></th><!-- % (발송대비) -->
                            <th scope="col" width="10%"><spring:message code="report.campaign.err.send.share" /></th><!-- 점유율 -->
                        </tr>
                        </thead>

                        <c:set var="errorGubun" scope="page" value=""/>
                        <c:if test="${!empty domainResultMsgListBySoft}">
                        <tbody>
                        <c:forEach var="loop" items="${domainResultMsgListBySoft}">
                        <c:set var="tmpSendCnt" value="${loop.sendCnt}.0" />

                        <c:if test="${loop.gid eq 0}"> <!-- 일반데이터 출력  -->
                        <tr>
                            <td><c:if test="${loop.errorGubun ne errorGubun}">${loop.errorGubun}</c:if></td>
                            <td class="text-left">${loop.errorCd} : ${loop.errorMsg}</td>
                            <td class="text-right"><fmt:formatNumber value="${tmpSendCnt}" /></td>
                            <!-- % : 전체 발송수 중 해당 오류가 차지하는 비율 -->
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt ne 0}">
                                        <fmt:formatNumber type="number" value="${tmpSendCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:when>
                                    <c:otherwise>0%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.softBounceCnt ne 0}">
                                        <fmt:formatNumber type="number" value="${tmpSendCnt / (ecareSendResultVo.sendCnt - ecareSendResultVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:when>
                                    <c:otherwise>0%</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:set var="errorGubun" scope="page" value="${loop.errorGubun}"/>
                        </c:if>

                        <c:if test="${loop.gid eq 1}">
                        <tr class="bg-subtotal">
                            <td colspan="2"><spring:message code="report.subtotal" /></td><!-- 소계 -->
                            <td class="text-right"><fmt:formatNumber value="${tmpSendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${tmpSendCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(ecareSendResultVo.sendCnt - ecareSendResultVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${tmpSendCnt / (ecareSendResultVo.sendCnt - ecareSendResultVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:if>

                        <c:if test="${loop.gid eq 3}">
                        <tr class="bg-secondary">
                            <td colspan="2"><spring:message code="report.total" /></td><!-- 합계 -->
                            <td class="text-right"><fmt:formatNumber value="${tmpSendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${tmpSendCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${(ecareSendResultVo.sendCnt - ecareSendResultVo.successCnt) eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${tmpSendCnt / (ecareSendResultVo.sendCnt - ecareSendResultVo.successCnt) * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:if>
                        </c:forEach>
                        </tbody>
                        </c:if>

                        <c:if test="${empty domainResultMsgListBySoft}">
                        <tbody>
                            <tr>
                                <td colspan="5"><spring:message code="report.campaign.err.nodata" /></td><!-- 오류 데이터가 존재하지 않습니다. -->
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
