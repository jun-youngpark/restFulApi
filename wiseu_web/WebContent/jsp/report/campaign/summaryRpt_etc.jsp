<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 전체 요약(SMS/LMS/MMS, 알림톡/친구톡/브랜드톡, FAX)
 * - URL : /report/campaign/summaryRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignSummaryAllController
 * - 이전 파일명 : etc_summary_all_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.summary.title" /></title><!-- 캠페인 요약리포트 -->
<script type="text/javascript">
    // 엑셀 다운로드
    function downloadExcel(scenarioNo, campaignNo, resultSeq, gubun) {
        $("#cmd").val("excel");
        $("#gubun").val(gubun);
        $("#resultSeq").val(resultSeq);
        $("input[name=excelCampaignNo]").val(campaignNo);
        $("#campaignReportFrm").attr('action', '/report/campaign/excelAllSummary.do').submit();
    }
</script>
</head>

<body>

<form name="campaignReportFrm" id="campaignReportFrm" method="post" action="/report/campaign/summary.do">
<input type="hidden" name="cmd" id="cmd" />
<input type="hidden" name="gubun" id="gubun" />
<input type="hidden" name="campaignNo" value="${campaignReportBasicVo.campaignNo}" />
<input type="hidden" name="excelCampaignNo" value="" />
<input type="hidden" name="scenarioNo" value="${campaignReportBasicVo.scenarioNo}" />
<input type="hidden" name="channelType" value="${campaignReportBasicVo.channelType}" />

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.allsummary"/></h3><!-- 전체 요약 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <!-- 발송결과 -->
        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-12 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.campaign.allsummary.sendresult"/></h4><!-- 발송결과 -->
                    </div>
                </div>

                <div class="col-12 alert alert-secondary mb-0" role="alert"><!-- 대상수의 합은 원본 캠페인의 대상수를 넘을 수 없습니다. -->
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.allsummary.targetcnt.detail" />
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.name" /></th><!-- 캠페인명 -->
                            <th rowspan="2" scope="col" width="110"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 대상수 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.head.title.send.cnt" /></th><!-- 발송건수 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendsuccess" /></th><!-- 발송성공 -->
                            <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendfail" /></th><!-- 발송실패  -->

                            <c:if test="${(campaignReportBasicVo.channelType eq 'C') or (campaignReportBasicVo.channelType eq 'A')}">
                            <th colspan="2" scope="col">SMS </th>
                            <th colspan="2" scope="col">LMS</th>
                            </c:if>

                            <th rowspan="2" width="74"><spring:message code='report.campaign.list.title.senddate' /></th><!-- 발송일자  -->
                            <th rowspan="2" width="30"><spring:message code='report.campaign.list.title.success' /></th><!-- 성공  -->
                            <th rowspan="2" width="40"><spring:message code='common.send.type.resend' /></th><!-- 재발송  -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송건수 : 건수 -->
                            <th scope="col">%</th><!-- 발송건수 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송실패 : 건수 -->
                            <th scope="col">%</th><!-- 발송실패 : % -->

                            <c:if test="${(campaignReportBasicVo.channelType eq 'C') or (campaignReportBasicVo.channelType eq 'A')}">
                            <th scope="col"><spring:message code="report.campaign.list.title.success" /></th><!-- SMS : 성공 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.fail" /></th><!-- SMS : 실패 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.success" /></th><!-- LMS : 성공 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.fail" /></th><!-- LMS : 실패 -->
                            </c:if>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="campaignReportVo" items="${listCampaignReportVo}" varStatus="status">
                        <tr>
                            <td class="text-left">
                                <c:choose>
                                    <c:when test="${campaignReportVo.resendSts eq 'T'}"><font style="color: green; ">[<spring:message code="resend.type.target" />]</font></c:when>
                                    <c:when test="${campaignReportVo.resendSts eq 'F'}"><font style="color: blue; ">[<spring:message code="resend.type.fail" />]</font></c:when>
                                </c:choose>
                                ${campaignReportVo.scenarioNm}
                            </td>
                            <td>${campaignReportVo.resultSeq}</td>
                            <td class="text-right">
                                <fmt:formatNumber type="number" value="${campaignReportVo.targetCnt}" />
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReportVo.sendCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${campaignReportVo.targetCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${campaignReportVo.sendCnt / campaignReportVo.targetCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReportVo.successCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${campaignReportVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${campaignReportVo.successCnt / campaignReportVo.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReportVo.failCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${campaignReportVo.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${(campaignReportVo.failCnt) / campaignReportVo.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>

                            <c:if test="${(campaignReportBasicVo.channelType eq 'C') or (campaignReportBasicVo.channelType eq 'A')}">
                            <td class="text-right"></td>
                            <td class="text-right"></td>
                            <td class="text-right"></td>
                            <td class="text-right"></td>
                            </c:if>

                            <td>
                                <fmt:parseDate value="${campaignReportVo.sendstartDt}" var="sendstartDt" pattern="yyyyMMdd" />
                                <fmt:formatDate value="${sendstartDt}" pattern="yyyy-MM-dd" />
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${campaignReportBasicVo.scenarioNo},${campaignReportBasicVo.campaignNo},${campaignReportVo.resultSeq},'success'); return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:viewResendForm('${campaignReportVo.resultSeq}','${campaignReportVo.resultSeq}','${campaignReportBasicVo.campaignNo}','${campaignReportBasicVo.channelType}','${campaignReportVo.failCnt}'); return false;">
                                    <i class="fas fa-paper-plane fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- 발송결과 e.card -->

        <%@ include file="/jsp/report/campaign/summaryRptResend_inc.jsp" %><!-- 재발송 campaign_report_resend_inc.jsp -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>

</body>
</html>
