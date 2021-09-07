<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 요약분석(메일)
 * - URL : /report/ecare/summaryRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareSummaryController
 * - 이전 파일명 : summary_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title.summary_${webExecMode}" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 엑셀다운로드 버튼 클릭
        $('#downloadBtn').on('click', function(event) {
            var url = window.location.toString();

            if(url.indexOf("?") > 0)
                location.href = url + "&format=excel";
            else
                location.href = url + "?format=excel";
        });
    }
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
            <div class="card-body px-0 pb-0">
                <div class="row">
                    <div class="col-6" style="padding-top: 6px;"><!-- title -->
                        <h4 class="h3 text-primary mb-0"><spring:message code="report.ecare.title.sendresult"/></h4><!-- 발송결과 -->
                    </div>
                    <div class="col-6 justify-content-end">
                        <button class="btn btn-sm btn-outline-primary" id="downloadBtn">
                            <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel"/><!-- 엑셀 다운로드 -->
                        </button>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" rowspan="2"><spring:message code="report.ecare.title.name_${webExecMode}" /></th><!-- 이케어명 -->
                            <th scope="col" rowspan="2"><spring:message code="report.ecare.title.target.Count" /></th><!-- 대상수 -->
                            <th scope="col" colspan="2"><spring:message code="report.ecare.title.send.count" /></th><!-- 발송수 -->
                            <th scope="col" colspan="2"><spring:message code="report.ecare.title.success" /></th><!-- 발송성공 -->
                            <th scope="col" colspan="2"><spring:message code="report.ecare.title.softbounce" /></th><!-- Soft Bounce -->
                            <th scope="col" colspan="2"><spring:message code="report.ecare.title.hardbounce" /></th><!-- Hard Bounce -->
                            <th scope="col" rowspan="2" width="80"><spring:message code="report.ecare.title.send.date" /></th><!-- 발송일시 -->
                            <th scope="col" rowspan="2" width="40"><spring:message code="report.ecare.success" /></th><!-- 성공 -->
                            <th scope="col" rowspan="2" width="40"><spring:message code="report.ecare.fail" /></th><!-- 실패 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송수 : 건수 -->
                            <th scope="col">%</th><!-- 발송수 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- Soft Bounce : 건수 -->
                            <th scope="col">%</th><!-- Soft Bounce : % -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- Hard Bounce : 건수 -->
                            <th scope="col">%</th><!-- Hard Bounce : % -->
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <td class="text-left">${scenarioInfoVo.ecareInfoVo.ecareNm}</td><!-- 이케어명 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}" /></td><!-- 대상수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}" /></td><!-- 발송수 : 건수 -->
                            <td class="text-right"><!-- 발송수 : % -->
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.sendCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.successCnt}" /></td><!-- 발송성공 : 건수 -->
                            <td class="text-right"><!-- 발송성공 : % -->
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.successCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.softBounceCnt}" /></td><!-- Soft Bounce : 건수 -->
                            <td class="text-right"><!-- Soft Bounce : % -->
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.softBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.softBounceCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.hardBounceCnt}" /></td><!-- Hard Bounce : 건수 -->
                            <td class="text-right"><!-- Hard Bounce : % -->
                                <c:choose>
                                    <c:when test="${ecareSendResultVo.hardBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.hardBounceCnt / ecareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${ecareSendResultVo.sendStartDtToSimpleDateStr}</td><!-- 발송일시 -->
                            <td><!-- 성공 -->
                                <c:if test="${ecareSendResultVo.successCnt > 0}">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_success&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}';">
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            <td><!-- 실패 -->
                                <c:if test="${(ecareSendResultVo.softBounceCnt + ecareSendResultVo.hardBounceCnt) > 0 }">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_fail&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}';">
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                        </tr>

                        <c:if test="${!empty postEcareSendResultVo}">
                        <tr>
                            <td>${postEcareSendResultVo.startDateYear}.${postEcareSendResultVo.startDateMonth}.${postEcareSendResultVo.startDateDay}</td>
                            <td><spring:message code="report.ecare.before.ecare_${webExecMode}" /></td>
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
                            <td class="text-right"><fmt:formatNumber type="number" value="${postEcareSendResultVo.hardBounceCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${postEcareSendResultVo.hardBounceCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${postEcareSendResultVo.hardBounceCnt / postEcareSendResultVo.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td colspan="2"></td>
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->

        <div class="card">
            <div class="card-body px-0" style="padding-top: 6px;">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.list.title.openresult"/></h4><!-- 수신확인결과 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                            <tr>
                                <th rowspan="2" scope="col"><spring:message code="report.ecare.list.title.sendsuccess" /></th><!-- 발송성공 -->
                                <th colspan="2" scope="col"><spring:message code="report.ecare.list.title.open" /></th><!-- 수신확인 -->
                                <th colspan="2" scope="col"><spring:message code="report.ecare.list.title.duration" /></th><!-- 유효 수신 -->
                                <th colspan="2" scope="col"><spring:message code="report.ecare.list.title.duplication" /></th><!-- 중복 수신 -->
                                <th colspan="2" scope="col"><spring:message code="report.ecare.list.title.mobile" /></th><!-- 모바일 수신 -->
                            </tr>
                            <tr>
                                <th scope="col"><spring:message code="report.ecare.list.title.cnt" /></th><!-- 수신확인 : 건수 -->
                                <th scope="col">%</th><!-- 수신확인 : % -->
                                <th scope="col"><spring:message code="report.ecare.list.title.cnt" /></th><!-- 유효 수신 : 건수 -->
                                <th scope="col">%</th><!-- 유효 수신 : % -->
                                <th scope="col"><spring:message code="report.ecare.list.title.cnt" /></th><!-- 중복 수신 : 건수 -->
                                <th scope="col">%</th><!-- 중복 수신 : % -->
                                <th scope="col"><spring:message code="report.ecare.list.title.cnt" /></th><!-- 모바일 수신 : 건수 -->
                                <th scope="col">%</th><!-- 모바일 수신 : % -->
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${fn:length(ecareReactionResultList) eq 0}">
                        <tr>
                            <td colspan="9"><spring:message code="report.ecare.list.open.nodata" /></td>
                        </tr>
                        </c:if>

                        <c:forEach var="reactionResultVo" items="${ecareReactionResultList}">
                        <tr>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.successCnt}" /></td><!-- 발송성공 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${reactionResultVo.openCnt}" /></td><!-- 수신확인 : 건수 -->
                            <td class="text-right"><!-- 수신확인 : % -->
                                <c:choose>
                                    <c:when test="${reactionResultVo.openCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${reactionResultVo.openCnt / ecareSendResultVo.successCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${reactionResultVo.durationCnt}" /></td><!-- 유효 수신 : 건수 -->
                            <td class="text-right"><!-- 유효 수신 : % -->
                                <c:choose>
                                    <c:when test="${reactionResultVo.durationCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${reactionResultVo.durationCnt / ecareSendResultVo.successCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${reactionResultVo.duplicationCnt}" /></td><!-- 중복 수신 : 건수 -->
                            <td class="text-right"><!-- 중복 수신 : % -->
                                <c:choose>
                                    <c:when test="${reactionResultVo.duplicationCnt eq 0}">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="${reactionResultVo.duplicationCnt / ecareSendResultVo.successCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="0" /></td><!-- 모바일 수신 : 건수 -->
                            <td class="text-right"><!-- 모바일 수신 : % -->
                                <c:choose>
                                    <c:when test="0">0.0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" pattern="0.0" value="0" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>
