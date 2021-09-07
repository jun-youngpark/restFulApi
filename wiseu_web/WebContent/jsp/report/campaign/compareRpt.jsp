<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인] 캠페인 비교분석 <br/>
 * - URL : /report/campaign/compareRpt.do <br/>
 * - Controller :com.mnwise.wiseu.web.report.web.campaign.CampaignCompareWithController <br/>
 * - Description	: 캠페인 태그와 기간으로 비교하는 화면
 * - 이전 파일명 : compare_with_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.result.comparison.title" /></title><!-- 캠페인 비교분석 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            event.preventDefault();

            var rules = {
                searchStartDt : {notBlank : true},
                searchEndDt : {notBlank : true},
                searchChannel : {selected : true},
                searchTagNo : {selected : true}
            };

            if($.mdf.validForm("#searchForm", rules) == false) {
                return;
            }

            $("#searchForm").attr('action', '/report/campaign/compareRpt.do').submit();  // /report/campaign/compare_with.do
        });
    }

    function initPage() {
        new mdf.Date("#searchStartDt");  // 발송기간-시작일
        new mdf.Date("#searchEndDt");  // 발송기간-종료일

        new mdf.DataTable("#sendResultTable");  // 발송결과
        new mdf.DataTable("#linkClickTable");  // 반응결과
        new mdf.DataTable("#linkTraceTable");  // 링크클릭반응결과

        $("#searchChannel > option[value='${searchChannel}']").attr("selected", "true");
        $("#searchTagNo > option[value='${searchTagNo}']").attr("selected", "true");
    }
</script>
</head>
<body>
<form id="searchForm" name="searchForm" method="post">
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.result.comparison.title"/></h3><!-- 캠페인 비교분석 -->
            </div>

            <div class="card-body pb-0 px-0">
                <div class="table-responsive gridWrap">
                    <table class="table table-sm dataTable table-fixed">
                        <colgroup>
                            <col width="10%" />
                            <col width="25%" />
                            <col width="10%" />
                            <col width="15%" />
                            <col width="10%" />
                            <col width="*" />
                            <col width="100" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th scope="row"><em class="required">required</em><spring:message code="common.menu.period"/></th><!-- 발송기간 -->
                            <td>
                                <div class="periodWrap align-items-center"><!-- period -->
                                    <div class="input_datebox">
                                        <input type="hidden" name="searchStartDt" id="searchStartDt" value="${searchStartDt}" maxlength="10" />
                                    </div>
                                    <span class="txt">~</span>
                                    <div class="input_datebox">
                                        <input type="hidden" name="searchEndDt" id="searchEndDt" value="${searchEndDt}" maxlength="10" />
                                    </div>
                                </div>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="ecare.channel" /></th><!-- 채널 -->
                            <td>
                                <select class="form-control form-control-sm" id="searchChannel" name="searchChannel">
                                    <option value=""><spring:message code="common.option.select"/></option><!-- 선택 -->
                                    <c:forEach var="channel" items="${channelUseList}">
                                        <option value="${channel}"><spring:message code="common.channel.${channel}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <th scope="row"><em class="required">required</em><spring:message code="campaign.menu.tag"/></th><!-- 태그 -->
                            <td>
                                <select class="form-control form-control-sm" id="searchTagNo" name="searchTagNo">
                                    <option value=""><spring:message code="common.option.select"/></option><!-- 선택 -->
                                    <c:forEach var="tagVo" items="${tagList}">
                                        <option value="${tagVo.tagNo}">${tagVo.tagNm}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="text-center">
                                <button class="btn btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div><!-- // Light table -->

                <h4 class="h3 text-primary mt-3 mb-0"><spring:message code="report.campaign.allsummary.sendresult" /></h4><!-- 발송결과 -->
                <div class="table-responsive overflow-x-hidden"><!-- Light table 발송결과-->
                    <table class="table table-xs dataTable" id="sendResultTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" width="7%"><spring:message code="common.menu.campaign"/><br/><spring:message code="ecare.channel.num"/></th><!-- 캠페인번호 -->
                            <th rowspan="2" width="7%"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 대상수 -->
                            <th colspan="2" width="15%"><spring:message code="report.campaign.result.comparison.send.count" /></th><!-- 발송수 -->
                            <th colspan="2" width="15%"><spring:message code="report.campaign.list.title.sendsuccess" /></th><!-- 발송성공 -->
                            <c:if test="${searchChannel eq 'M'}">
                            <th colspan="2" width="15%">Soft Bounce</th>
                            <th colspan="2" width="15%">Hard Bounce</th>
                            </c:if>
                            <th rowspan="2" width="10%"><spring:message code="report.campaign.result.comparison.send.date" /></th><!-- 발송일시 -->
                            <th rowspan="2" width="8%"><spring:message code="report.campaign.head.title.send.time" /></th><!-- 발송시간 -->
                            <th rowspan="2" width="8%"><spring:message code="report.campaign.head.title.status" /></th><!-- 상태 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송수 : 건수 -->
                            <th scope="col">%</th><!-- 발송수 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 발송성공 : 건수 -->
                            <th scope="col">%</th><!-- 발송성공 : % -->
                            <c:if test="${searchChannel eq 'M'}">
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- Soft Bounce : 건수 -->
                            <th scope="col">%</th><!-- Soft Bounce : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- Hard Bounce : 건수 -->
                            <th scope="col">%</th><!-- Hard Bounce : % -->
                            </c:if>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="campaignSendResultVo" items="${sendResultList}">
                        <tr>
                            <td scope="row">${campaignSendResultVo.campaignNo}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.targetCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.sendCnt}"/></td>
                            <td class="text-right">${campaignSendResultVo.sendCntPercent}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.successCnt}"/></td>
                            <td class="text-right">${campaignSendResultVo.successCntPercent}</td>
                            <c:if test="${searchChannel eq 'M'}">
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.softBounce}"/></td>
                            <td class="text-right">${campaignSendResultVo.softBouncePercent}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignSendResultVo.hardBounce}"/></td>
                            <td class="text-right">${campaignSendResultVo.hardBouncePercent}</td>
                            </c:if>
                            <td>${campaignSendResultVo.sendStartDtToSimpleDateStr}</td>
                            <td>
                                <c:if test="${campaignSendResultVo.formatPeriod eq 'sending'}">
                                    <spring:message code="report.survey.summary.title.W"/>
                                </c:if>
                                <c:if test="${campaignSendResultVo.formatPeriod eq 'fail'}">
                                    <spring:message code="report.ecare.title.fail"/>
                                </c:if>
                                <c:if test="${campaignSendResultVo.formatPeriod ne 'sending' && campaignSendResultVo.formatPeriod ne 'fail'}">
                                    ${campaignSendResultVo.formatPeriod}
                                </c:if>
                            </td>
                            <td>${campaignSendResultVo.sendResultStatusNm}</td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div><!-- e.card body -->
        </div><!-- //card -->

        <!-- card  반응결과-->
        <c:if test="${searchChannel eq 'M'}">
        <div class="card">
            <div class="card-body">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.linkclick.title.alt.4" /></h4><!-- 반응결과 -->

                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-xs dataTable" id="linkClickTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" width="60"><spring:message code="common.menu.campaign"/><br/><spring:message code="ecare.channel.num"/></th><!-- 캠페인번호 -->
                            <th rowspan="2"><spring:message code="report.campaign.list.title.sendsuccess" /></th><!-- 발송성공 -->
                            <th colspan="2"><spring:message code="report.campaign.list.title.open" /></th><!-- 수신확인 -->
                            <th colspan="3"><spring:message code="report.campaign.graph.title.duration" /></th><!-- 유효수신 -->
                            <th colspan="3"><spring:message code="report.campaign.graph.title.linkclick" /></th><!-- 링크클릭 -->
                            <th rowspan="2" width="110"><spring:message code="report.campaign.result.comparison.send.date" /></th><!-- 발송일시 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 수신확인 : 건수 -->
                            <th scope="col">%</th><!-- 수신확인 : % -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 유효수신 : 건수 -->
                            <th scope="col"><spring:message code="report.campaign.result.comparison.send.compare" />%</th><!-- 유효수신 : 발송대비% -->
                            <th scope="col"><spring:message code="report.campaign.result.comparison.open.compare" />%</th><!-- 유효수신 : 오픈대비% -->
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 링크클릭 : 건수 -->
                            <th scope="col"><spring:message code="report.campaign.result.comparison.send.compare" />%</th><!-- 링크클릭 : 발송대비% -->
                            <th scope="col"><spring:message code="report.campaign.result.comparison.open.compare" />%</th><!-- 링크클릭 : 오픈대비% -->
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="campaignReactionResult2Vo" items="${campaignReactionResultList}">
                        <tr>
                            <td scope="row">${campaignReactionResult2Vo.campaignNo}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReactionResult2Vo.sendCnt}"/></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReactionResult2Vo.openCnt}"/></td>
                            <td class="text-right">${campaignReactionResult2Vo.openBySend}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReactionResult2Vo.durationCnt}"/></td>
                            <td class="text-right">${campaignReactionResult2Vo.durationBySend}</td>
                            <td class="text-right">${campaignReactionResult2Vo.durationByOpen}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${campaignReactionResult2Vo.clickCnt}"/></td>
                            <td class="text-right">${campaignReactionResult2Vo.clickBySend}</td>
                            <td class="text-right">${campaignReactionResult2Vo.clickByOpen}</td>
                            <td>${campaignReactionResult2Vo.sendDate}</td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->
        </div>
        </c:if><!-- e.card 반응결과-->

        <!-- card  링크추적-->
        <c:if test="${searchChannel eq 'M'}">
        <div class="card">
            <div class="card-body">
                <h4 class="h3 text-primary mb-0"><spring:message code="report.campaign.linkclick.title.alt.1" /></h4><!-- 링크클릭반응결과 -->

                <div class="table-responsive overflow-x-hidden">
                    <table class="table table-xs dataTable" id="linkTraceTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" width="60"><spring:message code="common.menu.campaign"/><br/><spring:message code="ecare.channel.num"/></th><!-- 캠페인번호 -->
                            <th rowspan="2"><spring:message code="report.campaign.result.comparison.link" /> <spring:message code="report.campaign.result.comparison.count" /></th><!-- 링크 수 -->
                            <th colspan="2"><spring:message code="report.campaign.result.comparison.click" /></th><!-- 클릭 -->
                            <th colspan="2"><spring:message code="report.campaign.result.comparison.click" />(<spring:message code="report.campaign.result.comparison.containing.duplicate" />)</th><!-- 클릭(중복포함) -->
                            <th colspan="2"><spring:message code="report.campaign.result.comparison.best.link" /></th><!-- 베스트 링크 -->
                        </tr>
                        <tr>
                            <th class="subtitle"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 클릭 : 건수 -->
                            <th class="subtitle"><spring:message code="report.campaign.result.comparison.average" /></th><!-- 클릭 : 평균 -->
                            <th class="subtitle"><spring:message code="report.campaign.list.title.cnt" /></th><!-- 클릭(중복포함) : 건수 -->
                            <th class="subtitle"><spring:message code="report.campaign.result.comparison.average" /></th><!-- 클릭(중복포함) : 평균 -->
                            <th class="subtitle"><spring:message code="report.campaign.result.comparison.item" /></th><!-- 베스트 링크 : 항목 -->
                            <th class="subtitle"><spring:message code="report.campaign.result.comparison.click.count" /></th><!-- 베스트 링크 : 클릭 수 -->
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="linkTraceResult2Vo" items="${linkTraceResultList}">
                        <tr>
                            <td scope="row">${linkTraceResult2Vo.campaignNo}</td>
                            <td class="text-right"><fmt:formatNumber value="${linkTraceResult2Vo.linktraceCnt}" type="number"/></td>
                            <td class="text-right"><fmt:formatNumber value="${linkTraceResult2Vo.uniqueLinkCnt}" type="number"/></td>
                            <td class="text-right"><fmt:formatNumber value="${linkTraceResult2Vo.uniqueLinkCnt/linkTraceResult2Vo.linktraceCnt}" maxFractionDigits="1" type="number"/></td>
                            <td class="text-right"><fmt:formatNumber value="${linkTraceResult2Vo.allLinkCnt}" type="number"/></td>
                            <td class="text-right"><fmt:formatNumber value="${linkTraceResult2Vo.allLinkCnt/linkTraceResult2Vo.linktraceCnt}" maxFractionDigits="1" type="number"/></td>
                            <td class="text-left">${linkTraceResult2Vo.linkTitle}</td>
                            <td class="text-right"><fmt:formatNumber value="${linkTraceResult2Vo.bestLinkCnt}" type="number"/></td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div><!-- //Light table -->
            </div><!-- e.card body -->
        </div>
        </c:if><!-- e.card 반응결과-->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form><!-- //search form -->
</body>
</html>

