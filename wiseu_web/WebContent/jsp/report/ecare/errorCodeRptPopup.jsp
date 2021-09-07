<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 오류분석 - 에러코드 리포트(팝업)
 * - 해당 에러코드의 도메인별 리포트를 출력
 * - URL : /report/ecare/errorCodeRptPopup.do
 * - Controller :com.mnwise.wiseu.web.report.web.ecare.EcareErrSummaryController
 * - 이전 파일명 : err_bycode_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.error.domain_${webExecMode}" /></title><!-- 이케어 코드 별 도메인 리포트 -->
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body>
<div class="card pop-card">
    <div class="card-header"><!-- title -->
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="report.ecare.error.domain_${webExecMode}" /></h5></div><!-- 이케어 코드 별 도메인 리포트 -->
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
                    <th scope="col" width="40"><spring:message code="report.ecare.title.no" /></th><!-- No -->
                    <th scope="col"><spring:message code="report.campaign.domain.title.domain" /></th><!-- 도메인 -->
                    <th scope="col" width="80"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 대상수 -->
                </tr>
                </thead>

                <tbody>
                <c:forEach var="domainLogVo" items="${errCdResultMsgList}" varStatus="num">
                <tr>
                    <td scope="row">${num.count}</td>
                    <td class="text-left">
                        <c:choose>
                            <c:when test="${domainLogVo.domainNm ne 'ZZZ.DOMAIN'}">${domainLogVo.domainNm}</c:when>
                            <c:otherwise><spring:message code="report.campaign.err.bycode.etc" /><!-- 기타 --></c:otherwise>
                        </c:choose>
                     </td>
                    <td class="text-right"><fmt:formatNumber type="number" value="${domainLogVo.sendCnt}" /></td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div><!-- //Light table -->
    </div>
</div>
</body>
</html>