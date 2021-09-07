<%-------------------------------------------------------------------------------------------------
 * - [리포트>캠페인] 캠페인 리스트 <br/>
 * - [리포트>캠페인] 캠페인 리스트 - 세부결과 엑셀 다운로드 <br/>
 * - URL : /report/campaign/campaignRptList.do <br/>
 * - Controller :com.mnwise.wiseu.web.report.web.campaign.CampaignListController <br/>
 * - 이전 파일명 : campaign_list.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.campaign.title"/></title><!-- 캠페인 리포트 -->
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 오픈 고객 다운로드 버튼 클릭
        $("#openCustDownloadBtn").on("click", function(event) {
            if($("#campaignReportListFrm input:checkbox[name=chk]:checked").length == 0) {
                alert('<spring:message code="report.campaign.list.alert.msg.open"/>');  // 오픈고객 다운로드를 위해 선택한 캠페인이  없습니다.
                return;
            }

            $("#excel").val("y");
            $("#campaignReportListFrm").submit();
        });

        // 세부결과 엑셀 다운로드 버튼 클릭
        $("#resultDownloadBtn").on("click", function(event) {
            if(!confirm('<spring:message code="report.campaign.list.alert.msg.excel" />')) {  // 전체 캠페인 리포트 리스트가 목록이 다운됩니다. 계속하시겠습니까?
                return;
            }
            $("#excel").val("y");
            $("#campaignReportFrm").submit();
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#cmd").val("list");
            $("#excel").val("");
            $("#campaignReportFrm").submit();
        });

        // 체크박스 전체 버튼 클릭
        $("#table-check-all").on("click", function(event) {
            $.mdf.checkAll("#table-check-all");
        });
    }

    function initPage() {
        new mdf.Date("#searchQstartDt");  // 발송기간-시작일
        new mdf.Date("#searchQendDt");  // 발송기간-종료일
    }

    // 업데이트
    var updateStatus = false;
    var updateCampaignNo = 0;

    function updateCampaignList(campaignNo,channelType,sendDate) {
        if(updateStatus) {
            alert(updateCampaignNo + '<spring:message code="repott.campaign.list.alert.msg.update1" />');  // 번 캠페인이 업데이트 중입니다.
            return;
        }

        if(!confirm(campaignNo + '<spring:message code="report.campaign.list.alert.msg.update2" />')) {  // 번 캠페인을 업데이트 하시겠습니까?
            return;
        }

        updateStatus = true;
        updateCampaignNo = campaignNo;
        var url = (channelType == "M" || channelType == "P") ? "/report/campaign/updateCampaignList.json" : "/report/campaign/updateSmsCampaignList.json";
        var param = {
            campaignNo : campaignNo,
            sendDate : sendDate
        };

        $.post(url, $.param(param, true), function(result) {
            if(result.code == "OK") {
                alert(updateCampaignNo + "<spring:message code='report.campaign.list.alert.msg.update3'/>");  // 번 캠페인 업데이트가 완료되었습니다.
                document.location.reload();
            }
        });
    }

    function goView(scenarioNo, campaignNo, relationType, channelType, searchQstartDt, searchQendDt, searchWord, abTestType, abTestCond) {
        // /report/campaign/summary_all.do
        window.location.href ="/report/campaign/summaryRpt.do?scenarioNo=" + scenarioNo + "&campaignNo=" + campaignNo + "&relationType=" + relationType
                             + "&channelType=" + channelType + "&searchQstartDt=" + searchQstartDt + "&searchQendDt=" + searchQendDt
                             + "&searchWord=" + searchWord + "&abTestType=" + abTestType + "&abTestCond" + abTestCond;
    }
</script>
</head>

<body>
<div class="main-panel">
    <div class="container-fluid mt-4 mb-2"><!-- Page content -->
        <div class="card">
            <div class="card-header"><!-- title -->
                <h3 class="mb-0"><spring:message code="report.campaign.list.title"/></h3><!-- 캠페인 리포트 리스트 -->
            </div>

            <div class="card-body px-0 pb-0">
                <div class="alert alert-secondary mb-1" role="alert"><!-- 업데이트 버튼을 클릭 하시면 수신확인/리턴메일/링크클릭의 통계값이 업데이트 됩니다. -->
                    <i class="fas fa-circle fa-xs"></i> <spring:message code="report.campaign.list.update.detail" />
                </div>

                <form id="campaignReportFrm" name="campaignReportFrm" action="/report/campaign/campaignRptList.do" method="post"><!-- /report/campaign/campaign_list.do -->
                <input type="hidden" name="cmd" id="cmd" />
                <input type="hidden" id="excel" name="excel" />
                <input type="hidden" id="nowPage" name="nowPage" value="1" />
                <div class="row align-items-center py-1 table_option">
                    <div class="col-4 pr-1">
                        <c:if test="${fn:contains(userChannelList, 'M')}">
                            <button class="btn btn-sm btn-outline-primary btn-round" id="openCustDownloadBtn">
                                <i class="fas fa-download"></i> <spring:message code="button.download.open.custom"/><!-- OPEN 고객 다운로드 -->
                            </button>
                        </c:if>
                        <button class="btn btn-sm btn-outline-primary btn-round" id="resultDownloadBtn">
                            <i class="fas fa-file-excel fa-lg"></i> <spring:message code="button.download.excel.detail"/><!-- 세부결과 엑셀 다운로드 -->
                        </button>
                    </div>

                    <div class="col-8 searchWrap">
                        <div class="form-group searchbox">
                            <span class="form-control-label txt"><spring:message code="campaign.menu.channel"/></span><!-- 채널 -->
                            <select name="searchChannel" class="form-control form-control-sm">
                                <option value=""><spring:message code="common.menu.all" /></option>
                                <c:forEach var="channel" items="${userChannelList}">
                                <c:choose>
                                    <c:when test="${channel eq 'M'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'M'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    <c:when test="${channel eq 'S'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'S'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    <c:when test="${channel eq 'T'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'T'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    <c:when test="${channel eq 'P'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'P'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    <c:when test="${channel eq 'B'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'B'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    <c:when test="${channel eq 'C'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'C'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    <c:when test="${channel eq 'F'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'F'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group searchbox pl-2">
                            <span class="form-control-label txt"><spring:message code="report.campaign.search.date"/></span><!-- 기간 -->
                            <div class="periodWrap align-items-center">
                                <div class="input_datebox">
                                    <input type="hidden" name="searchQstartDt" id="searchQstartDt" value="${searchQstartDt}" maxlength="10" />
                                </div>
                                <span class="txt">~</span>
                                <div class="input_datebox">
                                    <input type="hidden" name="searchQendDt" id="searchQendDt" value="${searchQendDt}" maxlength="10" />
                                </div>
                            </div>
                        </div>

                        <div class="form-group searchbox search_input pl-0">
                            <input type="search" class="form-control form-control-sm" aria-controls="datatable-basic" name="searchWord" id="searchWord"
                                value="${param.searchWord}" placeholder="<spring:message code='report.campaign.search.title'/>" style="width:150px;"/>
                            <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                <spring:message code="report.campaign.search"/><!-- 검색 -->
                            </button>
                        </div>
                    </div>
                </div><!-- e.search area & buttons -->
                </form>

                <div class="table-responsive">
                <form id="campaignReportListFrm" name="campaignReportListFrm" action="/report/campaign/open_cust_download.do" method="post">
                    <table class="table table-sm dataTable table-hover table-fixed">
                        <thead class="thead-light">
                        <tr>
                            <th rowspan="2" scope="col" width="20" ><!-- 체크박스 -->
                                <div class="custom-control custom-checkbox">
                                    <input type="checkbox" id="table-check-all" name="customcheckbox" class="custom-control-input" />
                                    <label class="custom-control-label" for="table-check-all"></label>
                                </div>
                            </th>
                            <th rowspan="2" scope="col" width="6%"><spring:message code="campaign.menu.cno.br" /></th><!-- 캠페인번호 -->
                            <th rowspan="2" scope="col"><spring:message code="report.campaign.list.title.name"/></th><!-- 캠페인명 -->
                            <th rowspan="2" scope="col" width="5%"><spring:message code="report.campaign.list.title.channel"/></th><!-- 채널 -->
                            <th rowspan="2" scope="col" width="63"><spring:message code="report.campaign.list.title.senddate"/></th><!-- 발송일자 -->
                            <th rowspan="2" scope="col" width="8%"><spring:message code="report.campaign.list.title.targetcnt"/></th><!-- 대상건수 -->
                            <th rowspan="2" scope="col" width="8%"><spring:message code="report.campaign.list.title.sendcnt"/></th><!-- 발송건수 -->
                            <th colspan="2" scope="col" width="13%"><spring:message code="report.campaign.list.title.sendsuccess"/></th><!-- 발송성공 -->
                            <th colspan="2" scope="col" width="13%"><spring:message code="report.campaign.list.title.sendfail"/></th><!-- 발송실패 -->
                            <th colspan="2" scope="col" width="13%"><spring:message code="report.campaign.list.title.open"/></th><!-- 수신확인 -->
                            <th rowspan="2" scope="col" width="50" class="ls--1px"><spring:message code="report.campaign.list.title.update"/></th><!-- 업데이트 -->
                        </tr>
                        <tr>
                            <th scope="col" width="8%"><spring:message code="report.campaign.list.title.cnt"/></th><!-- 건수 -->
                            <th scope="col" width="5%">%</th>
                            <th scope="col" width="8%"><spring:message code="report.campaign.list.title.cnt"/></th><!-- 건수 -->
                            <th scope="col" width="5%">%</th>
                            <th scope="col" width="8%"><spring:message code="report.campaign.list.title.cnt"/></th><!-- 건수 -->
                            <th scope="col" width="5%">%</th>
                            <%-- 리턴메일
                            <th scope="col" width="5%"><spring:message code="report.campaign.list.title.cnt"/></th><!-- 건수 -->
                            <th scope="col" width="5%">%</th>
                            --%>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="loop" items="${campaignReportList}" varStatus="i">
                        <tr style="cursor: pointer;" onclick="goView('${loop.scenarioNo}', '${loop.campaignNo}', '${loop.relationType}', '${loop.channelType}', '${searchQstartDt}', '${searchQendDt}', '${param.searchWord}', '${loop.abTestType}', '${loop.abTestCond}')">
                            <th scope="row" onclick="event.cancelBubble=true"><!-- 체크박스 -->
                                <div class="custom-control custom-checkbox">
                                    <input type="checkbox" name="chk" id="chk_${i.count}" value="${loop.campaignNo}" class="custom-control-input" />
                                    <label class="custom-control-label" for="chk_${i.count}"></label>
                                </div>
                            </th>
                            <td>${loop.campaignNo}</td><!-- 캠페인번호 -->
                            <td class="text-left" <c:if test="${loop.depthNo > 1}">style="padding-left: ${(loop.depthNo - 1) * 10}px"</c:if>><!-- 캠페인명 -->
                                <c:if test="${loop.depthNo > 1}">
                                    <img src="/images/common/relation/type_${loop.relationType}.png" style="vertical-align: middle;"/>
                                </c:if>
                                ${loop.scenarioNm}
                            </td>
                            <td><!-- 채널 -->
                                <em class="txt_channel ${loop.channelType}">
                                    <c:choose>
                                        <c:when test="${loop.channelType eq 'M'}">E</c:when>
                                        <c:when test="${loop.channelType eq 'T'}">M</c:when>
                                        <c:otherwise>${loop.channelType}</c:otherwise>
                                    </c:choose>
                                </em>
                            </td>
                            <td class="ls--1px"><!-- 발송일자 -->
                                <fmt:parseDate value="${loop.sendstartDt}" var="sendstartDt" pattern="yyyyMMdd" />
                                <fmt:formatDate value="${sendstartDt}" pattern="yyyy-MM-dd" />
                            </td>
                            <td class="text-right ls--1px"><fmt:formatNumber type="number" value="${loop.targetCnt}" /></td><!-- 대상건수 -->
                            <td class="text-right ls--1px"><fmt:formatNumber type="number" value="${loop.sendCnt}" /></td><!-- 발송건수 -->
                            <td class="text-right ls--1px"><fmt:formatNumber type="number" value="${loop.successCnt}" /></td><!-- 발송성공 : 건수 -->
                            <td class="text-right ls--1px"><!-- 발송성공 : %-->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.successCnt / loop.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-right ls--1px"><fmt:formatNumber type="number" value="${loop.softBounceCnt + loop.hardBounceCnt}" /></td><!-- 발송실패 : 건수 -->
                            <td class="text-right ls--1px"><!-- 발송실패 : %-->
                                <c:choose>
                                    <c:when test="${loop.sendCnt eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${(loop.softBounceCnt + loop.hardBounceCnt) / loop.sendCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <c:choose>
                            <c:when test="${loop.channelType eq 'P' or loop.channelType eq 'M' }">
                            <td class="text-right ls--1px"><fmt:formatNumber type="number" value="${loop.openCnt}" /></td><!-- 수신확인 : 건수 -->
                            <td class="text-right ls--1px"><!-- 수신확인 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.openCnt / loop.successCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            </c:when>
                            <c:otherwise>
                                <td class="text-right">-</td>
                                <td class="text-right">-</td>
                            </c:otherwise>
                            </c:choose>

                            <%-- 리턴메일
                            <c:if test="${loop.channelType eq 'M'}">
                            <td class="text-right ls--1px"><fmt:formatNumber type="number" value="${loop.returnmailCnt}" /></td><!-- 리턴메일 : 건수 -->
                            <td class="text-right ls--1px"><!-- 리턴메일 : % -->
                                <c:choose>
                                    <c:when test="${loop.successCnt eq 0}">0%</c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber type="number" value="${loop.returnmailCnt / loop.successCnt * 100}" maxFractionDigits="1" />%
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            </c:if>

                            <c:if test="${loop.channelType ne 'M'}">
                            <td class="text-right">-</td><!-- 리턴메일 : 건수 -->
                            <td class="text-right">-</td><!-- 리턴메일 : % -->
                            </c:if>
                            --%>

                            <td onclick="event.cancelBubble=true">
                                <button class="btn btn-sm btn-outline-primary" onclick="updateCampaignList(${loop.campaignNo},'${loop.channelType}','${sendstartDt}')">
                                    <i class="fas fa-sync-alt"></i>
                                </button>
                            </td>
                        </tr>
                        </c:forEach>

                        <c:if test="${fn:length(campaignReportList) eq 0}">
                        <tr>
                            <td colspan="14"><spring:message code="report.campaign.list.nodata"/></td><!-- 캠페인 리포트가 존재하지 않습니다. -->
                        </tr>
                        </c:if>
                        </tbody>
                    </table>
                    </form>
                </div><!-- e.Light table -->

                <!-- 페이징 -->
                ${paging}
            </div><!-- e.card body -->
        </div><!-- e.card -->
    </div><!-- e.page content -->
</div><!-- e.main-panel -->
</body>
</html>
