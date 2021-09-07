<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어] 이케어 월별 발송 통계 <br/>
 * - URL : /report/ecare/monthlyRpt.do <br/>
 * - Controller :com.mnwise.wiseu.web.report.web.ecare.EcareMonthlyStatController <br/>
 * - 이전 파일명 : monthly_stat_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.monthly.title_${webExecMode}" /></title>
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

        // 검색 년/월/이케어타입 선택 변경시 - 이케어 유형도 변경
        $('#searchYear, #searchMonth, #ecareType').on("change", function(event) {
            changeEcareType();
        });
    }

    function initPage() {
        $("#searchYear > option[value='${searchYear}']").attr("selected", "true");
        $("#searchMonth > option[value='${searchMonth}']").attr("selected", "true");
        $("#searchChannel > option[value='${searchChannel}']").attr("selected", "true");
        $("#ecareType > option[value='${searchServiceType}']").attr("selected", "true");

        if('${searchServiceType}' != '') {
            changeEcareType();
        }

        new mdf.DataTable("#monthlyRptTable");
        viewChart();
    }

    function changeEcareType() {
        var year = $("#searchYear").val();
        var month = $("#searchMonth").val();
        var startDt = year + month;

        var param = {
            ecareType : $("#ecareType").val(),
            startDt : startDt
        };

        $("#ecareNo").children('option:not(:first)').remove();
        if($.mdf.isBlank(param.ecareType) == false) {
            $.post("/report/ecare/selectEcareType.do", $.param(param, true), function(data) {
                $(data).each(function(i, row) {
                    try {
                        $("#ecareNo").append("<option value='"+ row.ecareNo +"'>" + row.ecareNm + "</option>");
                        $("#ecareNo > option[value='${ecareNo}']").attr("selected", "true");
                    } catch (e) {
                        alert(e.message);
                    }
                });
            });
        }
    }

    function viewChart() {
        var $trArray = $("#rptTableBody").children();
        var itemArray = $.mdf.getTableCellValueArray($trArray, 0);
        var valueArray = [{
                "name" : '<spring:message code="report.ecare.title.send.count"/>',  // 발송수
                "data" : $.mdf.getTableCellValueArray($trArray, 1, true)
            }, {
                "name" : '<spring:message code="report.ecare.title.success"/>',  // 발송성공
                "data" : $.mdf.getTableCellValueArray($trArray, 2, true)
            }, {
                "name" : '<spring:message code="report.ecare.title.fail"/>',  // 발송실패
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
<form id="searchForm" name="searchForm" action="/report/ecare/monthlyRpt.do" method="post"><!-- /report/ecare/monthly_stat.do -->
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.ecare.monthly.title_${webExecMode}"/></h3><!-- 이케어 월별 발송 통계 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="row py-1 table_option">
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
                                    <option value="${i}">${i}<spring:message code="report.campaign.result.sending.report.year"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group searchbox pl-1">
                            <select class="form-control form-control-sm" id="searchMonth" name="searchMonth">
                                <option value=""><spring:message code = "report.ecare.monthly.monthselect" /></option>
                                <%  String[] month_ko = {"1월", "2월", "3월", "4월", "5월", "6월", "7월","8월","9월","10월","11월", "12월"}; %>
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
                        <div class="form-group searchbox pl-1">
                            <c:if test="${channelUseSize > 1}">
                                <select class="form-control form-control-sm" id="searchChannel" name="searchChannel">>
                                    <option value=""><spring:message code="report.ecare.monthly.channel.select" /></option><!-- 채널 선택 -->
                                    <c:forEach var="channel" items="${channelUseList}">
                                        <option value="${channel}" <c:if test="${channel eq searchChannel}">selected</c:if>><spring:message code="common.channel.${channel}" /></option>
                                    </c:forEach>
                                </select>
                                <error><label for="searchChannel" class="error"></label></error><!-- 에러 표시 위치 지정 -->
                            </c:if>
                        </div>
                        <c:if test="${webExecMode eq '1' }">
                            <div class="form-group searchbox pl-1">
                                <select class="form-control form-control-sm" name="ecareType" id="ecareType">
                                    <option value="" ><spring:message code="report.ecare.monthly.ecare.type_${webExecMode}" /></option>
                                    <option value="R" style="display: none;"><spring:message code="report.ecare.monthly.realtime" /></option>
                                    <option value="SN"><spring:message code="report.ecare.monthly.nrealtime" /></option>
                                    <option value="SR"><spring:message code="report.ecare.monthly.minschedule" /></option>
                                    <option value="SS"><spring:message code="report.ecare.monthly.schedule" /></option>
                                </select>
                            </div>
                            <div class="form-group searchbox pl-1">
                                <select class="form-control form-control-sm" name="ecareNo" id="ecareNo">
                                    <option value=""><spring:message code="report.ecare.monthly.all"/></option>
                                    <c:forEach var="loop" items="${accountDtList}">
                                        <option value="${loop.ecareNo}" <c:if test="${ecareNo eq loop.ecareNo}">selected</c:if>>${loop.ecareNm}</option>
                                    </c:forEach>
                                </select>
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
                        <th scope="col" rowspan="2"><spring:message code="report.ecare.title.gubun" /></th><!-- 구분 -->
                        <th scope="col" rowspan="2"><spring:message code="report.ecare.title.send.count" /></th><!-- 발송수 -->
                        <th scope="col" colspan="2">
                            <c:if test="${searchChannel eq 'A'}"><spring:message code="common.channel.A"/></c:if>
                            <c:if test="${searchChannel eq 'C'}"><spring:message code="common.channel.C"/></c:if>
                            <spring:message code="report.ecare.title.success" /><!-- 발송성공 -->
                        </th>
                        <th scope="col" colspan="2">
                            <c:if test="${searchChannel eq 'A'}"><spring:message code="common.channel.A"/></c:if>
                            <c:if test="${searchChannel eq 'C'}"><spring:message code="common.channel.C"/></c:if>
                            <spring:message code="report.ecare.title.fail" /><!-- 발송실패 -->
                        </th>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <th scope="col" colspan="2"><spring:message code="report.ecare.title.open" /></th><!-- 수신확인 -->
                        <th scope="col" colspan="2"><spring:message code="report.ecare.title.duration" /></th><!-- 유효수신 -->
                        <th scope="col" colspan="2"><spring:message code="report.ecare.title.link" /></th><!-- 링크클릭 -->
                        <th scope="col" colspan="2"><spring:message code="report.ecare.monthly.return" /></th><!-- 리턴메일 -->
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <!-- 채널 구분 알림톡 추가 -->
                        <c:if test="${searchChannel eq 'A' or searchChannel eq 'C'}">
                        <th scope="col" colspan="2"><spring:message code="common.channel.S"/><spring:message code="report.ecare.title.success" /></th><!-- 발송성공 -->
                        <th scope="col" colspan="2"><spring:message code="common.channel.S"/><spring:message code="report.ecare.title.fail" /></th><!-- 발송실패 -->
                        <th scope="col" colspan="2"><spring:message code="common.channel.T"/><spring:message code="report.ecare.title.success" /></th>
                        <th scope="col" colspan="2"><spring:message code="common.channel.T"/><spring:message code="report.ecare.title.fail" /></th>
                        </c:if>
                        <!-- 채널 구분 알림톡 추가 끝 -->
                    </tr>
                    <tr>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th><!-- 건수 -->
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <!-- 채널 구분 알림톡 추가 -->
                        <c:if test="${searchChannel eq 'A' or searchChannel eq 'C'}">
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        <th scope="col"><spring:message code="report.ecare.title.count" /></th>
                        <th scope="col">%</th>
                        </c:if>
                        <!-- 채널 구분 알림톡 추가 끝 -->
                    </tr>
                    </thead>

                    <tbody  id="rptTableBody">
                    <c:forEach var="ecareSendResultVo" items="${ecareStat}">
                    <tr>
                        <td scope="row">${ecareSendResultVo.reportDt}</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.sendCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.successCnt}"/></td>
                        <td class="text-right">${ecareSendResultVo.successCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.failCnt}"/></td>
                        <td class="text-right">${ecareSendResultVo.failCntToPercent}%</td>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->
                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.openCnt}"/></td>
                        <td class="text-right">${ecareSendResultVo.openCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.durationCnt}"/></td>
                        <td class="text-right">${ecareSendResultVo.durationCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.linkCnt}"/></td>
                        <td class="text-right">${ecareSendResultVo.linkCntToPercent}%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.returnMailCnt}"/></td>
                        <td class="text-right">${ecareSendResultVo.returnMailCntToPercent}%</td>
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->

                        <c:if test="${searchChannel eq 'A' or searchChannel eq 'C'}"><!-- 채널 구분 알림톡 추가 -->
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.smsSuccessCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt>0? (ecareSendResultVo.smsSuccessCnt/(ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt))*100.0 : 0.0}" />%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.smsFailCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt>0? (ecareSendResultVo.smsFailCnt/(ecareSendResultVo.smsSuccessCnt+ecareSendResultVo.smsFailCnt))*100.0 : 0.0}" />%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.lmsSuccessCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt>0? (ecareSendResultVo.lmsSuccessCnt/(ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt))*100.0 : 0.0}" />%</td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${ecareSendResultVo.lmsFailCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" pattern="0.0" value="${ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt>0? (ecareSendResultVo.lmsFailCnt/(ecareSendResultVo.lmsSuccessCnt+ecareSendResultVo.lmsFailCnt))*100.0 : 0.0}" />%</td>
                        </c:if>
                    </tr>
                    </c:forEach>
                    </tbody>

                    <c:if test="${totalSendCnt > 0 }">
                    <tfoot class="bg-secondary">
                    <tr>
                        <td><spring:message code="report.total" /></td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalSendCnt}"/></td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalSuccessCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalSuccessCnt / totalSendCnt * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalFailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalFailCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalFailCnt / totalSendCnt * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>

                        <c:if test="${searchChannel eq 'M' or searchChannel eq null or searchChannel eq ''}">
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalOpenCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalOpenCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalDurationCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalSuccessCnt > 0}">
                                    <fmt:formatNumber type="number" value="${totalDurationCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%
                                </c:when>
                            <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalLinkCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalSuccessCnt > 0}">
                                    <fmt:formatNumber type="number" value="${totalLinkCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%
                                </c:when>
                            <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalReturnMailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalSuccessCnt > 0}">
                                    <fmt:formatNumber type="number" value="${totalReturnMailCnt / totalSuccessCnt * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        </c:if>
                        <!-- 채널 구분이 M이 아닌경우에는 수신확인, 유효수신, 링크클릭, 리턴메일을 숨긴다. -->

                        <!-- 채널 구분 알림톡 추가 -->
                        <c:if test="${searchChannel eq 'A'}">
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalAlimtalkSmsSuccessCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalAlimtalkSmsSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalAlimtalkSmsSuccessCnt / (totalAlimtalkSmsSuccessCnt+totalAlimtalkSmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalAlimtalkSmsFailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalAlimtalkSmsFailCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalAlimtalkSmsFailCnt / (totalAlimtalkSmsSuccessCnt+totalAlimtalkSmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalAlimtalkLmsSuccessCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalAlimtalkLmsSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalAlimtalkLmsSuccessCnt / (totalAlimtalkLmsSuccessCnt+totalAlimtalkLmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalAlimtalkLmsFailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalAlimtalkLmsFailCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalAlimtalkLmsFailCnt / (totalAlimtalkLmsSuccessCnt+totalAlimtalkLmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        </c:if>
                        <!-- 채널 구분 알림톡 추가 끝 -->

                        <!-- 채널 구분 친구톡 추가 -->
                        <c:if test="${searchChannel eq 'C'}">
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalFriendtalkSmsSuccessCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalFriendtalkSmsSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalFriendtalkSmsSuccessCnt / (totalFriendtalkSmsSuccessCnt+totalFriendtalkSmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalFriendtalkSmsFailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalFriendtalkSmsFailCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalFriendtalkSmsFailCnt / (totalFriendtalkSmsSuccessCnt+totalFriendtalkSmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalFriendtalkLmsSuccessCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalFriendtalkLmsSuccessCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalFriendtalkLmsSuccessCnt / (totalFriendtalkLmsSuccessCnt+totalFriendtalkLmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right"><fmt:formatNumber type="number" value="${totalFriendtalkLmsFailCnt}"/></td>
                        <td class="text-right">
                            <c:choose>
                                <c:when test="${totalFriendtalkLmsFailCnt >0}">
                                    <fmt:formatNumber type="number" value="${totalFriendtalkLmsFailCnt / (totalFriendtalkLmsSuccessCnt+totalFriendtalkLmsFailCnt) * 100}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0.0%</c:otherwise>
                            </c:choose>
                        </td>
                        </c:if>
                        <!-- 채널 구분 친구톡 추가 끝 -->
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

<form id="excelFrm" name="excelFrm" action="/report/ecare/monthlyRpt.do" method="post"><!-- /report/ecare/monthly_stat.do -->
    <input type="hidden" name="searchYear" value="${searchYear}">
    <input type="hidden" name="searchMonth" value="${searchMonth}">
    <input type="hidden" name="searchChannel" value="${searchChannel}">
    <input type="hidden" name="ecareType" value="${ecareType}">
    <input type="hidden" name="ecareNo" value="${ecareNo}" />
    <input type="hidden" name="format" value="excel" />
</form>
</body>
</html>
