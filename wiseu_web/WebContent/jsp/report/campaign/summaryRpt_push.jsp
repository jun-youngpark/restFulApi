<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인>캠페인 리스트] 캠페인 리포트 - 전체 요약(PUSH)<br/>
 * - URL : /report/campaign/summaryRpt.do <br/>
 * - Controller : com.mnwise.wiseu.web.report.web.campaign.CampaignSummaryAllController <br/>
 * - 이전 파일명 : push_summary_all_view.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code = "report.campaign.allsummary.title" /></title><!-- 캠페인 전체요약 리포트 -->
<%@ include file="/jsp/include/pluginChart.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        $('#helpBtn').on("click", function(event) {
            event.preventDefault();
            $('#helpDiv').toggle();
        });
    }

    function initPage() {
        viewChart();
    }

    //  excel down
    function downloadExcel(scenarioNo,campaignNo,gubun) {
        $("#cmd").val("excel");
        $("#gubun").val(gubun);
        $("#excelCampaignNo").val(campaignNo);
        $("#campaignReportFrm").attr('action', '/report/campaign/excelAllSummary.do').submit();
    }

    // 재발송
    function resend(resendMode,scenarioNo,campaignNo,depthNo,channelType,check) {
        var abTestType = $("#abTestType").val();
        var abTestCond = $("#abTestCond").val();

        var param = 'resendMode=' + resendMode;
        param += '&scenarioNo=' + scenarioNo;
        param += '&campaignNo=' + campaignNo;
        param += '&depthNo=' + depthNo;
        param += '&channelType=' + channelType;
        //A/B 테스트 타입 추가
        param += '&abTestType=' + abTestType;
        param += '&abTestCond=' + abTestCond;

        var relationType = resendMode.substring(0,1);
        var msg = "";
        var msg2 = "";

        if(relationType == 'R') {
            msg = '<spring:message code = "report.campaign.allsummary.alert.msg.resend" />';  // 재발송 하시겠습니까?
            msg2 = "<spring:message code='report.campaign.allsummary.alert.resend' />";  // 재발송 대상자가 없습니다.
        } else if(relationType == 'L') {
            msg = '<spring:message code = "report.campaign.allsummary.alert.msg.target" />';  // 타겟발송 하시겠습니까?
            msg2 = "<spring:message code='report.campaign.allsummary.alert.target' />";  // 타겟발송 대상자가 없습니다.
        } else {
        }

        if(check == '' || check == 0) {
            alert(msg2);
            return;
        }

        if(!confirm(msg)) {
            return;
        }

        window.location.href= "/campaign/resend.do?" + param;
    }

    function viewChart() {
        var itemArray = [
            '<spring:message code="report.chart.push.label.ios"/>',  // iOS
            '<spring:message code="report.chart.push.label.and"/>',  // Android
            '<spring:message code="report.chart.push.label.etc"/>'  // 기타
        ];

        var $tdArray = $("#rptTableBody").children().last().children();
        var valueArray = [{
                "name" : '<spring:message code="report.campaign.head.title.success.cnt" />',  // 발송성공
                "data" : [parseInt($tdArray.eq(4).text()), parseInt($tdArray.eq(7).text()), parseInt($tdArray.eq(11).text())]
            }, {
                "name" : '<spring:message code="report.campaign.head.title.fail.cnt" />',  // 발송실패
                "data" : [parseInt($tdArray.eq(5).text()), parseInt($tdArray.eq(8).text()), parseInt($tdArray.eq(12).text())]
            }
        ];

        var option = {
            colors: ['#90ed7d', '#ff305c']  // 그래프 색상 변경
        };
        var chart = new mdf.Chart('sendChartDiv', null, '<spring:message code="report.count.unit"/>', option);  // 건수 (건)
        chart.renderChart("VER_BAR", itemArray, valueArray);
    }
</script>
</head>

<body>
<form id="campaignReportFrm" name="campaignReportFrm" method="post" action="/report/campaign/summaryRpt.do"><!-- /report/campaign/summary_all.do -->
<input type="hidden" name="cmd" id="cmd" />
<input type="hidden" name="gubun" id="gubun" />
<input type="hidden" name="excelCampaignNo" id="excelCampaignNo" />
<input type="hidden" name="campaignNo" value="${campaignReportBasicVo.campaignNo}" />
<input type="hidden" name="scenarioNo" value="${campaignReportBasicVo.scenarioNo}" />
<input type="hidden" name="depthNo" value="${campaignReportBasicVo.depthNo}"/>
<input type="hidden" name="abTestType" id="abTestType" value="${campaignReportBasicVo.abTestType}"/>
<input type="hidden" name="abTestCond" id="abTestCond" value="${campaignReportBasicVo.abTestCond}"/>
<input type="hidden" name="channelType" id="channelType" value="${campaignReportBasicVo.channelType}"/>

<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card mb-0">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.head.alt.allsummary"/></h3><!-- 전체 요약 -->
            </div>
            <%@ include file="/jsp/report/campaign/campaignRptTab_inc.jsp" %><!-- campaign_report_head_inc.jsp -->
        </div>

        <!-- 발송결과 card -->
        <div class="card mb-0">
            <div class="card-body px-0 pb-0">
                <div class="row mt-3 mb-0">
                    <div class="col-6 align-items-center pt-1"><!-- title -->
                        <h4 class="h3 text-primary m-0"><spring:message code="report.campaign.allsummary.sendresult"/></h4><!-- 발송결과 -->
                    </div>
                    <div class="col-6 justify-content-end">
                        <button class="btn btn-sm" id="helpBtn">
                            <i class="fas fa-question-circle fa-2x"></i><!-- 도움말 아이콘 -->
                        </button>
                    </div>
                </div>

                <div class="row mt-0 mb-0 dp-none" id="helpDiv"><!-- 발송 결과 Help -->
                    <ul class="col-12 list-group list-group-flush list alert">
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 38px;">
                                <dt class="col-sm-2 text-warning"><spring:message code="report.campaign.list.resend" /></dt><!-- 재발송 -->
                                <dd class="col-sm-10 m-0">
                                    <spring:message code="report.campaign.allsummary.resend.detail1" /><br/><!-- 총 발송된 캠페인중 성공건수외의 실패건수를 대상으로 발송됩니다. -->
                                    - <spring:message code="report.campaign.allsummary.resend.detail2" /><!-- 재발송은 실패건수를 대상으로 발송이 되므로 타겟발송이 불가합니다. -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 57px;">
                                <dt class="col-sm-2 text-warning"><spring:message code="report.campaign.list.target" /></dt><!-- 타겟발송 -->
                                <dd class="col-sm-10 m-0">
                                    <spring:message code="report.campaign.allsummary.target.detail1" /><br/><!-- 총 발송된 캠페인의 성공건수 중 타겟으로 정한 대상자를 대상으로 발송됩니다. -->
                                    - <spring:message code="report.campaign.allsummary.target.detail2" /><br/><!-- 타겟발송은 ‘수신확인결과’에서 발송할 수 있습니다. -->
                                    - <spring:message code="report.campaign.allsummary.target.detail3" /><!-- 재발송된 결과값으로 타겟발송 시 새로운 캠페인으로 분류됩니다 -->
                                </dd>
                            </dl>
                        </li>
                        <li class="list-group-item">
                            <dl class="row m-0" style="height: 19px;">
                                <dt class="col-sm-2 text-warning"><spring:message code="report.campaign.allsummary.targetcnt" /></dt><!-- 대상수 -->
                                <dd class="col-sm-10 m-0">
                                    <spring:message code="report.campaign.allsummary.targetcnt.detail" /><!-- 대상수의 합은 원본 캠페인의 대상수를 넘을 수 없습니다. -->
                                </dd>
                            </dl>
                        </li>
                    </ul>
                </div>

                <div class="table-responsive">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.name" /></th><!-- 캠페인명 -->
                            <th rowspan="2" scope="col" width="110"><spring:message code="report.campaign.result.comparison.resultseq" /></th><!-- 발송일련번호 -->
                            <th colspan="2" scope="col"><spring:message code="report.ecare.monthly.all" /></th><!-- 전체 -->
                            <th colspan="3" scope="col"><spring:message code="report.chart.push.label.ios" /></th><!-- iOS -->
                            <th colspan="3" scope="col"><spring:message code="report.chart.push.label.and" /></th><!-- Android -->
                            <th colspan="3" scope="col"><spring:message code="report.chart.push.label.etc" /></th><!-- 기타 -->
                            <th rowspan="2" scope="col" width="30"><spring:message code="report.campaign.list.title.success" /><!-- 성공 -->
                            <th rowspan="2" scope="col" width="40"><spring:message code="report.campaign.list.resend" /></th><!-- 재발송 -->
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.head.title.send.cnt" /></th><!-- 전체 : 발송건수 -->
                            <th scope="col"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 전체 : 대상수 -->
                            <th scope="col"><spring:message code="report.campaign.head.title.success.cnt" /></th><!-- iOS : 성공건수 -->
                            <th scope="col"><spring:message code="report.campaign.head.title.fail.cnt" /></th><!-- iOS : 실패건수 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.open" /></th><!-- iOS : 수신확인 -->
                            <th scope="col"><spring:message code="report.campaign.head.title.success.cnt" /></th><!-- Android : 성공건수 -->
                            <th scope="col"><spring:message code="report.campaign.head.title.fail.cnt" /></th><!-- Android : 실패건수 -->
                            <th scope="col"><spring:message code="report.campaign.list.title.open" /></th><!-- Android : 수신확인 -->
                            <th scope="col"><spring:message code="report.reqcnt" /></th><!-- 기타 : 요청건수 -->
                            <th scope="col"><spring:message code="report.reqsuccess" /></th><!-- 기타 : 요청성공 -->
                            <th scope="col"><spring:message code="report.reqfail" /></th><!-- 기타 : 요청실패 -->
                        </tr>
                        </thead>

                        <c:set var="totalSendCnt" value="0"/>
                        <c:set var="totalTargetCnt" value="0"/>
                        <c:set var="totalIOSSuccCnt" value="0"/>
                        <c:set var="totalIOSFailCnt" value="0"/>
                        <c:set var="totalIOSOpenCnt" value="0"/>
                        <c:set var="totalANDSuccCnt" value="0"/>
                        <c:set var="totalANDFailCnt" value="0"/>
                        <c:set var="totalANDOpenCnt" value="0"/>
                        <c:set var="totalETCSuccCnt" value="0"/>
                        <c:set var="totalETCFailCnt" value="0"/>
                        <c:set var="totalETCReqCnt" value="0"/>
                        <tbody id="rptTableBody">
                        <c:forEach var="loop" items="${campaignSummaryAllList}" varStatus="status">
                        <c:if test="${loop.relationType eq 'L'}"><c:set var="targetLoop" value="true" /></c:if><!-- 타겟 발송이 존재할 경우 targetLoop에 true 값을 준다 -->
                        <c:set var="totalSendCnt" value="${totalSendCnt + loop.sendCnt}"/>
                        <c:set var="totalTargetCnt" value="${totalTargetCnt + loop.targetCnt}"/>
                        <c:set var="totalIOSSuccCnt" value="${totalIOSSuccCnt + pushSendlogList[status.index].IOS_SUCC_CNT}"/>
                        <c:set var="totalIOSFailCnt" value="${totalIOSFailCnt + pushSendlogList[status.index].IOS_FAIL_CNT}"/>
                        <c:set var="totalIOSOpenCnt" value="${totalIOSOpenCnt + pushReceiveList[status.index].IOS_RECEIPT}"/>
                        <c:set var="totalANDSuccCnt" value="${totalANDSuccCnt + pushSendlogList[status.index].AND_SUCC_CNT}"/>
                        <c:set var="totalANDFailCnt" value="${totalANDFailCnt + pushSendlogList[status.index].AND_FAIL_CNT}"/>
                        <c:set var="totalANDOpenCnt" value="${totalANDOpenCnt + pushReceiveList[status.index].AND_RECEIPT}"/>
                        <c:set var="totalETCSuccCnt" value="${totalETCSuccCnt + pushSendlogList[status.index].ETC_SUCC_CNT}"/>
                        <c:set var="totalETCFailCnt" value="${totalETCFailCnt + pushSendlogList[status.index].ETC_FAIL_CNT}"/>
                        <c:set var="totalETCReqCnt" value="${totalETCReqCnt + pushSendlogList[status.index].ETC_REQ_CNT}"/>

                        <c:if test="${loop.gid eq 0 and loop.relationType ne 'L'}"><!-- 일반발송 -->
                        <tr>
                            <td class="text-left"><!-- 캠페인명 -->
                                <c:if test="${loop.relationType eq 'R' && loop.depthNo > 1}"><div class="re_send">Resend</div></c:if>
                                ${loop.scenarioNm}
                            </td>
                            <td>${loop.resultSeq}</td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.targetCnt}" /></td><!-- 전체 : 발송건수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.sendCnt}" /></td><!-- 전체 : 대상수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].IOS_SUCC_CNT}" /></td><!-- iOS : 성공건수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].IOS_FAIL_CNT}" /></td><!-- iOS : 실패건수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushReceiveList[status.index].IOS_RECEIPT}" /></td><!-- iOS : 수신확인 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].AND_SUCC_CNT}" /></td><!-- Android : 성공건수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].AND_FAIL_CNT}" /></td><!-- Android : 실패건수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushReceiveList[status.index].AND_RECEIPT}" /></td><!-- Android : 수신확인 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].ETC_REQ_CNT}" /></td><!-- 기타 : 요청건수 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].ETC_SUCC_CNT}" /></td><!-- 기타 : 요청성공 -->
                            <td class="text-right"><fmt:formatNumber type="number" value="${pushSendlogList[status.index].ETC_FAIL_CNT}" /></td><!-- 기타 : 요청실패 -->
                            <td class="text-center"><!-- 성공 -->
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${loop.scenarioNo},${loop.campaignNo},'success');return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                            <td><!-- 재발송 : 일반/재발송 과 타겟발송의 각각 마지막 depth_no에만 진행한다. -->
                                <c:choose>
                                    <c:when test="${loop.relationType eq 'L'}">
                                        <c:choose>
                                            <c:when test="${loop.depthNo eq maxDepthNoL}">
                                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:resend('L_resendFail','${loop.scenarioNo}','${loop.campaignNo}','${loop.depthNo}','${loop.channelType}','${loop.softBounceCnt}'); return false;">
                                                    <i class="fas fa-paper-plane fa-lg"></i>
                                                </button>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${loop.depthNo eq maxDepthNoN}">
                                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:viewResendForm('${loop.resultSeq}','${loop.resultSeq}','${loop.campaignNo}','${loop.channelType}','${loop.softBounceCnt}'); return false;">
                                                    <i class="fas fa-paper-plane fa-lg"></i>
                                                </button>
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </c:if>
                        </c:forEach>
                        </tbody>

                        <tfoot class="bg-secondary"><!-- 합계 -->
                            <td scope="row" colspan="2"><spring:message code="report.total" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalTargetCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalSendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalIOSSuccCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalIOSFailCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalIOSOpenCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalANDSuccCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalANDFailCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalANDOpenCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalETCReqCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalETCSuccCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${totalETCFailCnt}" /></td>
                            <td class="text-center">-</td>
                            <td class="text-center">-</td>
                        </tr>
                        <tfoot>
                    </table>

                    <!-- 타겟발송 결과 표시/타겟발송이 있는 경우에만 -->
                    <c:if test="${targetLoop eq 'true' }">
                    <table class="table table-sm dataTable">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.target" /></th>
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.allsummary.targetcnt" /></th>
                            <th rowspan="2" scope="col"><spring:message code='report.campaign.head.title.send.cnt' /></th>
                            <th colspan="2" scope="col"><spring:message code="report.campaign.head.title.success.cnt" /></th>
                            <th colspan="2" scope="col"><spring:message code="report.campaign.head.title.fail.cnt" /></th>
                            <th rowspan="2"><spring:message code="report.campaign.list.title.success" /></th>
                        </tr>
                        <tr>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th>
                            <th scope="col">%</th>
                            <th scope="col"><spring:message code="report.campaign.list.title.cnt" /></th>
                            <th scope="col">%</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="loop" items="${campaignSummaryAllList}" varStatus="i">
                        <c:if test="${loop.relationType ne 'N' and loop.relationType ne 'R'}">
                        <!-- 일반 데이터일 경우 -->
                        <c:if test="${loop.gid eq 0}">
                        <c:set var="targetTotalCnt" value="${targetTotalCnt + loop.targetCnt}"/>
                        <c:set var="targetSendCnt" value="${targetSendCnt + loop.sendCnt}"/>
                        <c:set var="targetTotalSuccCnt" value="${targetTotalSuccCnt + loop.successCnt}"/>
                        <tr>
                            <td class="text-right" <c:if test="${loop.depthNo > 2}">style="padding-left: ${(loop.depthNo - 2) * 10}px"</c:if>>
                                <c:if test="${loop.depthNo > 1}"><div class="target_send">Target Send</div></c:if>
                                ${loop.scenarioNm}
                            </td>
                            <td><fmt:formatNumber type="number" value="${loop.targetCnt}" /></td>
                            <td><fmt:formatNumber type="number" value="${loop.sendCnt}" /></td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.successCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.successCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right"><fmt:formatNumber type="number" value="${loop.failCnt}" /></td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0.0%</c:when>
                                    <c:otherwise><fmt:formatNumber type="number" pattern="0.0" value="${loop.failCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="javascript:downloadExcel(${loop.scenarioNo},${loop.campaignNo},'open');return false;">
                                    <i class="fas fa-file-excel fa-lg"></i>
                                </button>
                            </td>
                        </tr>
                        </c:if>
                        </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    </c:if><!-- 타겟발송 끝-->
                </div>
            </div><!-- e.card body -->

            <%@ include file="/jsp/report/campaign/summaryRptResend_inc.jsp" %><!-- 재발송 campaign_report_resend_inc.jsp -->

            <div class="card-body mt-3"><!-- 차트 영역 -->
                <div id="sendChartDiv" style="width: 800px; margin: auto;" class="chart"/>
            </div>
        </div><!-- 발송결과 e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</form>

</body>
</html>