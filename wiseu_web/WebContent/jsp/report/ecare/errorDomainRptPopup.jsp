<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석  - 도메인 리포트(팝업)
 * - 해당 도메인의 에러코드별 리포트를 출력
 * - URL : /report/ecare/errorDomainRptPopup.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareErrSummaryController
 * - 이전 파일명 : err_bydomain_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.domain.error_${webExecMode}" /></title><!-- 이케어 도메인별 에러코드 리포트 -->
<%@ include file="/jsp/include/plugin.jsp" %>
</head>

<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="report.campaign.domain.title.alt.1"/></h5></div><!-- 도메인별 발송 오류 현황 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-sm dataTable">
                <thead class="thead-light">
                <tr>
                    <th scope="col" width="100"><spring:message code="report.ecare.title.gubun" /></th><!-- 구분 -->
                    <th scope="col"><spring:message code="report.ecare.bounce.detail" /></th><!-- 세부 Bounce 내용 -->
                    <th scope="col" width="100"><spring:message code="report.ecare.title.count" /></th><!-- 건수 -->
                    <th scope="col" width="80"><spring:message code="report.ecare.title.share" /></th><!-- 점유율 -->
                </tr>
                </thead>

                <tbody>
                <c:if test="${totalSoftBounceCnt eq 0 and totalHardBounceCnt eq 0}">
                <tr>
                   <td colspan="4"><spring:message code="report.ecare.summary.errdomain.nodata" /></td><!-- 오류분석 데이터가 존재하지 않습니다. -->
                </tr>
                </c:if>

                <c:if test="${totalSoftBounceCnt ne 0}">
                <c:forEach var="domainLogVo" items="${domainResultMsgListBySoft}" varStatus="num">
                <tr>
                    <c:if test="${num.count eq 1}">
                    <td rowspan="${domainResultMsgListBySoftSize+1}">Soft Bounce</td>
                    </c:if>

                    <td class="text-left">${domainLogVo.errorCd} : ${domainLogVo.errorDesc}</td>
                    <td class="text-right">${domainLogVo.targetCnt}</td>
                    <td class="text-right">
                        <c:choose>
                            <c:when test="${totalSoftBounceCnt ne 0}"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/totalSoftBounceCnt}" /></c:when>
                            <c:otherwise>0%</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </c:forEach>

                <tr class="bg-subtotal">
                    <td><span class="text-warning"><spring:message code="report.subtotal" /></span></td><!-- 소계 -->
                    <td class="text-right"><fmt:formatNumber type="number" value="${totalSoftBounceCnt}" /></td>
                    <td class="text-right">100%</td>
                </tr>
                </c:if>

                <c:if test="${totalHardBounceCnt ne 0}">
                <c:forEach var="domainLogVo" items="${domainResultMsgListByHard}" varStatus="num">
                <tr>
                    <c:if test="${num.count eq 1}">
                    <td rowspan="${domainResultMsgListByHardSize+1}">Hard Bounce</td>
                    </c:if>

                    <td class="text-left">${domainLogVo.errorCd} : ${domainLogVo.errorDesc}</td>
                    <td class="text-right"><fmt:formatNumber value="${domainLogVo.targetCnt}" type="number"/></td>
                    <td class="text-right">
                        <c:choose>
                            <c:when test="${totalHardBounceCnt ne 0}"><fmt:formatNumber type="percent" maxFractionDigits="1" value="${domainLogVo.targetCnt/totalHardBounceCnt}" /></c:when>
                            <c:otherwise>0%</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </c:forEach>

                <tr class="bg-subtotal">
                    <td><span class="text-warning"><spring:message code="report.subtotal" /></span></td><!-- 소계 -->
                    <td class="text-right"><fmt:formatNumber type="number" value="${totalHardBounceCnt}" /></td>
                    <td class="text-right">100%</td>
                </tr>
                </c:if>

                <c:if test="${totalSoftBounceCnt ne 0 and totalHardBounceCnt ne 0}">
                <tr class="bg-secondary">
                    <td colspan="2"><spring:message code="report.total" /></td><!-- 합계 -->
                    <td class="text-right"><fmt:formatNumber value="${totalBounceCnt}" type="number"/></td>
                    <td>-</td>
                </tr>
                </c:if>
                </tbody>
            </table>
        </div><!-- //Light table -->
    </div>
</div>
</body>
</html>
