<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 요약분석(알림톡,친구톡)
 * - URL : /report/ecare/summaryRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareSummaryController
 * - 이전 파일명 : kakaotalk_summary_view.jsp
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
            <div class="card-body px-0 pb-0">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.ecare.title.sendresult"/></h4><!-- 발송결과 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" width="100"><spring:message code="report.ecare.title.gubun" /></th><!-- 구분 -->
                            <th rowspan="2"><spring:message code="report.ecare.title.send.count" /></th><!-- 발송수 -->
                            <th colspan="2"><spring:message code="report.ecare.title.success" /></th><!-- 발송성공 -->
                            <th colspan="2"><spring:message code="report.campaign.list.title.sendfail" /></th><!-- 발송실패 -->
                            <th rowspan="2" width="80"><spring:message code="report.ecare.title.send.date" /></th><!-- 발송일시 -->
                            <c:if test="${resultExcelUse eq 'on'}" >
                                <th rowspan="2" width="40"><spring:message code="report.ecare.success" /></th><!--  -->
                                <th rowspan="2" width="40"><spring:message code="report.ecare.fail" /></th><!--  -->
                            </c:if>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                            <th scope="col">%</th>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                            <th scope="col">%</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <td><spring:message code="common.channel.${scenarioInfoVo.ecareInfoVo.channelType}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.successCnt}" /></td>
                            <td class="text-right">${ecareSendResultVo.successCntToPercent}%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.softBounceCnt}" /></td>
                            <td class="text-right">${ecareSendResultVo.failCntToPercent}%</td>
                            <td>${ecareSendResultVo.sendStartDtToSimpleDateStr}</td>

                            <c:if test="${resultExcelUse eq 'on'}" >
                            <td>
                                <c:if test="${ecareSendResultVo.successCnt > 0}">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_success&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}'">
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${ecareSendResultVo.softBounceCnt > 0 }">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_fail&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}'">
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            </c:if>
                        </tr>

                        <c:if test="${0 < ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt}">
                        <tr>
                            <td><spring:message code="common.channel.S"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.smsSuccessCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt>0? (ecareSendResultVo.smsSuccessCnt/(ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt))*100.0 : 0.0}" />%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.smsFailCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt>0? (ecareSendResultVo.smsFailCnt/(ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt))*100.0 : 0.0}" />%</td>
                            <td>${ecareSendResultVo.sendStartDtToSimpleDateStr}</td>

                            <c:if test="${resultExcelUse eq 'on'}" >
                            <td>
                                <c:if test="${ecareSendResultVo.smsSuccessCnt > 0}">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='report/ecare/ecare_report_csv_download.do?mode=send_success&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}S&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}'";>
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${ecareSendResultVo.smsFailCnt > 0 }">
                                    <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_fail&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}S&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}'";>
                                        <i class="fas fa-file-excel fa-lg"></i>
                                    </button>
                                </c:if>
                            </td>
                            </c:if>
                        </tr>
                        </c:if>

                        <c:if test="${0 < ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt}">
                        <tr>
                            <td><spring:message code="common.channel.T"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.lmsSuccessCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt>0? (ecareSendResultVo.lmsSuccessCnt/(ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt))*100.0 : 0.0}" />%</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.lmsFailCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt>0? (ecareSendResultVo.lmsFailCnt/(ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt))*100.0 : 0.0}" />%</td>
                            <td>${ecareSendResultVo.sendStartDtToSimpleDateStr}</td>
                            <c:if test="${resultExcelUse eq 'on'}" >
                                <td>
                                    <c:if test="${ecareSendResultVo.lmsSuccessCnt > 0}">
                                        <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_success&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}L&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}'">
                                            <i class="fas fa-file-excel fa-lg"></i>
                                        </button>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${ecareSendResultVo.lmsFailCnt > 0 }">
                                        <button class="btn btn-sm btn-outline-primary" onclick="location.href='/report/ecare/ecare_report_csv_download.do?mode=send_fail&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&channelType=${scenarioInfoVo.ecareInfoVo.channelType}L&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}'">
                                            <i class="fas fa-file-excel fa-lg"></i>
                                        </button>
                                    </c:if>
                                </td>
                            </c:if>
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
