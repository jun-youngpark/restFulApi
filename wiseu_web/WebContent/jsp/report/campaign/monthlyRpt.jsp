<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인] 캠페인 월별 발송 통계 <br/>
 * - URL : /report/campaign/monthlyRpt.do <br/>
 * - Controller :com.mnwise.wiseu.web.report.web.campaign.CampaignMonthlyStatController <br/>
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><spring:message code="report.campaign.result.sending.report.title"/></title><!-- 캠페인 월별 발송 통계 -->
<%@ include file="/jsp/include/pluginChart.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 엑셀다운로드 버튼 클릭
        $('#downloadBtn').on("click", function(event) {
            $("#excelFrm").submit();
        });

        // 검색 버튼 클릭
        $('#searchBtn').on("click", function(event) {
            var rules = {
                searchChannel : {selected : true}
            };

            if($.mdf.validForm("#searchForm", rules) == false) {
                return;
            }

            $("#searchForm").submit();
        });
    }

    function initPage() {
        $("#searchYear > option[value='${searchYear}']").attr("selected", "true");
        $("#searchMonth > option[value='${searchMonth}']").attr("selected", "true");
        $("#searchChannel > option[value='${searchChannel}']").attr("selected", "true");

        new mdf.DataTable("#monthlyRptTable");
        viewChart();
    }

    function viewChart() {
        var $trArray = $("#rptTableBody").children();
        var itemArray = $.mdf.getTableCellValueArray($trArray, 0);
        var valueArray = [{
                "name" : '<spring:message code="report.campaign.result.comparison.send.count"/>',  // 발송수
                "data" : $.mdf.getTableCellValueArray($trArray, 1, true)
            }, {
                "name" : '<spring:message code="report.campaign.list.title.sendsuccess"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 2, true)
            }, {
                "name" : '<spring:message code="report.campaign.list.title.sendfail"/>',  // 발송실패
                "data" : $.mdf.getTableCellValueArray($trArray, 4, true)
            }
        ];

        var option = {
            colors: ['#7cb5ec', '#90ed7d', '#ff305c']  // 그래프 색상 변경
        };
        var chart = new mdf.Chart('chartDiv', null, '<spring:message code="report.count.unit"/>', option);  // 건수 (건)
        chart.renderChart("VER_BAR", itemArray, valueArray);
    }
</script>
</head>

<body>
<form id="searchForm" name="searchForm" action="/report/campaign/monthlyRpt.do" method="post"><!-- /report/campaign/monthly_stat.do -->
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.result.sending.report.title"/></h3><!-- 캠페인 월별 발송 통계 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="row align-items-center py-1 table_option">
                    <div class="col-2"><!-- buttons -->
                        <c:if test="${totalSendCnt > 0}">
                        <button class="btn btn-sm btn-outline-primary btn-round" id="downloadBtn">
                            <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel"/><!-- 엑셀 다운로드 -->
                        </button>
                        </c:if>&nbsp;
                    </div>
                    <div class="col-10 searchWrap"><!-- search -->
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" id="searchYear" name="searchYear">
                                <c:forEach var="i" begin="${minDt}" end="${maxDt}" step="1" varStatus="status">
                                    <option value="${i}">${i}<spring:message code="report.campaign.result.sending.report.year"/></option><!-- 년 -->
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" id="searchMonth" name="searchMonth">
                                <option value=""><spring:message code="report.campaign.result.sending.report.month.select"/></option><!-- 월 선택 -->
                                <%  String[] month_ko = {"1월","2월", "3월", "4월", "5월", "6월", "7월","8월","9월","10월","11월", "12월"}; %>
                                <%  String[] month_en = {"Jan","Feb", "Mar", "Apr", "May", "Jun", "Jul","Aug","Sep","Oct","Nov", "Dec"}; %>
                                <c:choose>
                                    <c:when test="${lang eq '_en'}"><c:set var="month" value="<%=month_en%>"/></c:when>
                                    <c:when test="${lang eq '_vn'}"><c:set var="month" value="<%=month_en%>"/></c:when>
                                    <c:otherwise><c:set var="month" value="<%=month_ko%>"/></c:otherwise>
                                </c:choose>
                                <c:forEach var="str" items="${month}" varStatus="status">
                                    <option value="<c:if test="${status.count < 10}">0</c:if>${status.count}">${str}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <c:if test="${channelUseSize > 1}">
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" id="searchChannel" name="searchChannel">
                                <option><spring:message code="report.campaign.result.sending.report.channel.select"/></option><!-- 채널 선택 -->
                                <c:forEach var="channel" items="${channelUseList}">
                                    <option value="${channel}"><spring:message code="common.channel.${channel}"/></option>
                                </c:forEach>
                            </select>
                            <error><label for="searchChannel" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                        </div>
                        </c:if>
                        <div class="form-group searchbox pl-1">
                            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                <spring:message code="button.search"/><!-- 검색 -->
                            </button>
                        </div>
                    </div><!-- //search -->
                </div><!-- e.search area & buttons -->
            </div><!-- e.card body -->

            <div class="table-responsive overflow-x-hidden">
                <table class="table table-sm dataTable" id="monthlyRptTable">
                    <thead class="thead-light">
                    <tr>
                        <th rowspan="2" scope="col"><spring:message code="report.campaign.list.gubun"/></th><!-- 구분 -->
                        <th rowspan="2" scope="col"><spring:message code="report.campaign.result.comparison.send.count"/></th><!-- 발송수 -->
                        <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendsuccess"/></th><!-- 발송성공 -->
                        <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.sendfail"/></th><!-- 발송실패 -->
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.open"/></th><!-- 수신확인 -->
                        <th colspan="2" scope="col"><spring:message code="report.campaign.graph.title.duration"/></th><!-- 유효수신 -->
                        <th colspan="2" scope="col"><spring:message code="report.campaign.graph.title.linkclick"/></th><!-- 링크클릭 -->
                        <th colspan="2" scope="col"><spring:message code="report.campaign.list.title.return"/></th><!-- 리턴메일 -->
                        </c:if>
                    </tr>
                    <tr>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt"/></th><!-- 건수 -->
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt"/></th>
                        <th scope="col">%</th>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt"/></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt"/></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt"/></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.campaign.list.title.cnt"/></th>
                        <th scope="col">%</th>
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                    </tr>
                    </thead>
                    <tbody id="rptTableBody">
                    <c:forEach var="campaignSendResultVo" items="${campaignStat}">
                    <tr>
                        <td>${campaignSendResultVo.reportDt}</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.sendCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.successCnt}"/></td>
                        <td class="text-right">${campaignSendResultVo.successCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.failCnt}"/></td>
                        <td class="text-right">${campaignSendResultVo.failCntToPercent}%</td>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.openCnt}"/></td>
                        <td class="text-right">${campaignSendResultVo.openCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.durationCnt}"/></td>
                        <td class="text-right">${campaignSendResultVo.durationCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.linkCnt}"/></td>
                        <td class="text-right">${campaignSendResultVo.linkCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.returnMailCnt}"/></td>
                        <td class="text-right">${campaignSendResultVo.returnMailCntToPercent}%</td>
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                    </tr>
                    </c:forEach>
                    </tbody>

                    <c:if test="${totalSendCnt > 0 }">
                    <tfoot class="bg-secondary">
                    <tr>
                        <th scope="row"><spring:message code="report.total"/></th>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalSendCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccessCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalSuccessCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalFailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalFailCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalFailCnt / totalSendCnt * 100}" maxFractionDigits="1"/>%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalOpenCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt >0}">
                                        <fmt:formatNumber type="number" value="${totalOpenCnt / totalSuccessCnt * 100}" maxFractionDigits="1"/>%
                                    </c:when>
                                    <c:otherwise>0.0%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalDurationCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt > 0}">
                                        <fmt:formatNumber type="number" value="${totalDurationCnt / totalSuccessCnt * 100}" maxFractionDigits="1"/>%
                                    </c:when>
                                   <c:otherwise>0.0%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalLinkCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt > 0}">
                                        <fmt:formatNumber type="number" value="${totalLinkCnt / totalSuccessCnt * 100}" maxFractionDigits="1"/>%
                                    </c:when>
                                   <c:otherwise>0.0%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalReturnMailCnt}"/></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${totalSuccessCnt > 0}">
                                        <fmt:formatNumber type="number" value="${totalReturnMailCnt / totalSuccessCnt * 100}" maxFractionDigits="1"/>%
                                    </c:when>
                                   <c:otherwise>0.0%</c:otherwise>
                                </c:choose>
                            </td>
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                    </tr>
                    </tfoot>
                    </c:if>
                </table>
            </div><!-- e.Light table -->

            <div class="card-body mt-5"><!-- 차트 영역 -->
                <div id="chartDiv" class="chart"/>
            </div>
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>

<form id="excelFrm" name="excelFrm" action="/report/campaign/monthlyRpt.do" method="post"><!-- /report/campaign/monthly_stat.do -->
    <input type="hidden" name="searchYear"    value="${searchYear}">
    <input type="hidden" name="searchMonth"   value="${searchMonth}">
    <input type="hidden" name="searchChannel" value="${searchChannel}">
    <input type="hidden" name="format"        value="excel"/>
</form>

</body>
</html>
