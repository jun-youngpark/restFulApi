<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트>이케어 리포트(일정별 발송현황)] 링크클릭 분석
 * - URL : /report/ecare/linkClickRpt.do
 * - Controller : com.mnwise.wiseu.web.report.web.ecare.EcareLinkClickController
 * - 이전 파일명 : linkclick_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.link" /> : NO.${scenarioInfoVo.ecareInfoVo.ecareNo} ${scenarioInfoVo.ecareInfoVo.ecareNm}</title>
<script type="text/javascript">
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.ecare.title_${webExecMode}"/> - <spring:message code="report.campaign.head.alt.link"/></h3><!-- 이케어 리포트 - 링크클릭 분석 -->
            </div>
            <%@ include file="/jsp/report/ecare/ecareRptTab_inc.jsp"%><!-- /jsp/report/ecare/ecare_report_tab_inc.jsp -->
        </div>

        <div class="card">
            <div class="card-body px-0 pb-0">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.linkclick.title.alt.1"/></h4><!-- 링크클릭반응결과 -->

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th scope="col" rowspan="2" width="40"><spring:message code="report.ecare.title.no" /></th><!-- NO -->
                            <th scope="col" rowspan="2"><spring:message code="report.ecare.title.url" /></th><!-- URL / 이미지 -->
                            <th scope="col" colspan="3" width="30%"><spring:message code="report.ecare.title.link" /></th><!-- 링크클릭 -->
                            <th scope="col" colspan="3" width="30%"><spring:message code="report.ecare.link.duration" /></th><!-- 중복 링크클릭 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 링크클릭 : 건수 -->
                            <th scope="col"><spring:message code="report.ecare.send.compare" /></th><!-- 링크클릭 : 발송대비% -->
                            <th scope="col"><spring:message code="report.ecare.open.compare" /></th><!-- 링크클릭 : 오픈대비% -->
                            <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 중복 링크클릭 : 건수 -->
                            <th scope="col"><spring:message code="report.ecare.send.compare" /></th><!-- 중복 링크클릭 : 발송대비% -->
                            <th scope="col"><spring:message code="report.ecare.open.compare" /></th><!-- 중복 링크클릭 : 오픈대비% -->
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="linkTraceResultVo" items="${linkTraceResultList}">
                        <tr>
                            <td>${linkTraceResultVo.linkSeq}</td>
                            <td class="text-left">${linkTraceResultVo.linkTitle}</td>
                            <td class="text-right">${linkTraceResultVo.uniqueLinkCnt}</td>
                            <td class="text-right">${linkTraceResultVo.byUniqueSend}</td>
                            <td class="text-right">${linkTraceResultVo.byUniqueOpen}</td>
                            <td class="text-right">${linkTraceResultVo.allLinkCnt}</td>
                            <td class="text-right">${linkTraceResultVo.byDupSend}</td>
                            <td class="text-right">${linkTraceResultVo.byDupOpen}</td>
                        </tr>
                        </c:forEach>

                        <c:if test="${fn:length(linkTraceResultList) eq 0}">
                        <tr>
                            <td colspan="8"><spring:message code="report.campaign.list.linkclick.nodata" /></td><!-- 링크클릭 데이터가 존재하지 않습니다. -->
                        </tr>
                        </c:if>

                        </tbody>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>
