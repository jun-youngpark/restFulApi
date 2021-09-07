<%-------------------------------------------------------------------------------------------------
 * - [리포트>이케어>이케어 리스트] 이케어 리포트 리스트 <br/>
 * - URL : /report/ecare/ecareRptList.do <br/>
 * - Controller :com.mnwise.wiseu.web.report.web.ecare.EcareListController <br/>
 * - 이전 파일명 : ecare_list.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="report.ecare.title_${webExecMode}" /></title>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
        initPage();
    });

    function initEventBind() {
        // 검색어 엔터키 입력
        $("#searchWord").on('keydown',function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                $("#searchBtn").trigger('click');
            }
        });

        // 검색 버튼 클릭
        $("#searchBtn").on("click", function(event) {
            $("#searchForm").attr('action', '/report/ecare/ecareRptList.do').submit();  // /report/ecare/ecare_list.do
            return false;
        });
    }

    function initPage() {
        new mdf.Date("#searchQstartDt");  // 발송기간-시작일
        new mdf.Date("#searchQendDt");  // 발송기간-종료일
    }

    function goDailyRpt(scenarioNo, ecareNo , channelType) {
        $("#pageFrm #scenarioNo").val(scenarioNo);
        $("#pageFrm #ecareNo").val(ecareNo);
        $("#pageFrm #channelType").val(channelType);
        $("#pageFrm").attr('action', '/report/ecare/dailyRpt.do').submit();  // /report/ecare/ecare_daily.do
    }

    function changeTab(serviceType, subType) {
        $("#searchForm input[name=serviceType]").val(serviceType);
        $("#searchForm input[name=subType]").val(subType);

        $("#searchForm").submit();
        return false;
    }
</script>

</head>
<body>
    <!-- find_bar end -->
    <c:choose>
    <c:when test="${serviceType eq 'R'}">
        <c:set var="serviceType" scope="session" value="R"/>
        <c:set var="subType" scope="session" value=""/>
    </c:when>
    <c:otherwise>
        <c:choose>
        <c:when test="${subType eq 'N'}">
            <c:set var="serviceType" scope="session" value="S"/>
            <c:set var="subType" scope="session" value="N"/>
        </c:when>
        <c:when test="${subType eq 'R'}">
            <c:set var="serviceType" scope="session" value="S"/>
            <c:set var="subType" scope="session" value="R"/>
        </c:when>
        <c:otherwise>
            <c:set var="serviceType" scope="session" value="S"/>
            <c:set var="subType" scope="session" value="S"/>
        </c:otherwise>
        </c:choose>
    </c:otherwise>
    </c:choose>

    <form id="searchForm" name="searchForm" action="/report/ecare/ecareRptList.do" method="post"><!-- /report/ecare/ecare_list.do -->
        <input type="hidden" name="serviceType" value="${serviceType}"/>
        <input type="hidden" name="subType" value="${subType}"/>
        <input type="hidden" name="searchColumn" value="scenarioNm" id="id_searchColumn"/>

    <div class="main-panel">
        <div class="container-fluid mt-4 mb-2"><!-- Page content -->
            <div class="card">
                <div class="card-header"><!-- title -->
                    <h3 class="mb-0"><spring:message code="report.ecare.title.list_${webExecMode}" /></h3><!-- 이케어 리포트 리스트 -->
                </div>

                <div class="card-body px-0 pb-0">
                    <div class="row align-items-center py-1 mb-2 table_option">
                        <div class="col-6"><!-- tabs -->
                            <ul class="nav nav-tabs tab02" id="tabs-text" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link mb-sm-3 mb-md-0 <c:if test="${subType eq 'N'}">active</c:if>" onClick="changeTab('S', 'N'); return false;">
                                        <spring:message code="report.ecare.monthly.nrealtime" /><!-- 준실시간 -->
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link mb-sm-3 mb-md-0 <c:if test="${subType eq 'R'}">active</c:if>" onClick="changeTab('S', 'R'); return false;">
                                        <spring:message code="report.ecare.monthly.minschedule" /><!-- 스케쥴(분) -->
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link mb-sm-3 mb-md-0 <c:if test="${subType eq 'S'}">active</c:if>" onClick="changeTab('S', 'S'); return false;">
                                        <spring:message code="report.ecare.monthly.schedule" /><!-- 스케쥴 -->
                                    </a>
                                </li>
                            </ul>
                        </div>

                        <div class="col-6 searchWrap" style="padding-top: 10px;">
                            <div class="form-group searchbox pl-1">
                                <select name="searchChannel" class="form-control form-control-sm"><!-- 채널 -->
                                    <option value=""><spring:message code="campaign.menu.channel" /></option>
                                    <c:forEach var="channel" items="${userChannelList}">
                                    <c:choose>
                                        <c:when test="${channel eq 'M'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'M'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'S'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'S'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'T'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'T'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'A'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'A'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'C'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'C'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'P'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'P'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'F'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'F'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                        <c:when test="${channel eq 'B'}"><option value="${channel}" <c:if test="${param.searchChannel eq 'C'}">selected="selected"</c:if>><spring:message code="common.channel.${channel}"/></option></c:when>
                                    </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group searchbox pl-1">
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
                                    value="${param.searchWord}" placeholder="<spring:message code='report.ecare.title.name_${webExecMode}'/>" style="width:130px;"/>
                                <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                                    <spring:message code="button.search"/><!-- 검색 -->
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-sm dataTable table-hover table-fixed">
                            <thead class="thead-light">
                                <tr>
                                    <th scope="col" width="6%"><spring:message code="ecare.menu.no_${webExecMode}.br" /></th><!-- 이케어번호 -->
                                    <th scope="col" width="*"><spring:message code="report.ecare.title.name_${webExecMode}" /></th><!-- 이케어명 -->
                                    <th scope="col" width="5%"><spring:message code="report.ecare.summary.channel" /></th><!-- 채널 -->
                                    <th scope="col" width="8%"><spring:message code="report.campaign.head.title.status" /></th><!-- 상태 -->
                                    <c:if test="${serviceType eq 'S' and subType eq 'S'}">
                                        <th scope="col" width="10%"><spring:message code="report.campaign.allsummary.targetcnt" /></th><!-- 대상수 -->
                                        <th scope="col" width="13%"><spring:message code="report.campaign.head.title.send.time" /></th><!-- 발송시간 -->
                                    </c:if>
                                    <th scope="col" width="10%"><spring:message code="report.campaign.head.title.user" /></th><!-- 담당자 -->
                                    <th scope="col" width="10%"><spring:message code="report.campaign.head.title.grp" /></th><!-- 요청 부서 -->
                                </tr>
                            </thead>
                            <tbody>

                            <c:if test="${!empty ecareReportList}">
                            <c:forEach var="ecareScenarioVo" items="${ecareReportList}" varStatus="status">
                            <tr style="cursor: pointer;" onClick="goDailyRpt('${ecareScenarioVo.scenarioNo}','${ecareScenarioVo.ecareInfoVo.ecareNo}','${ecareScenarioVo.ecareInfoVo.channelType}'); return false">
                                <td scope="row">${ecareScenarioVo.ecareInfoVo.ecareNo}</td><!-- 이케어번호 -->
                                <td class="text-left">${ecareScenarioVo.scenarioNm}</td><!-- 이케어명 -->
                                <td><!-- 채널 -->
                                    <em class="txt_channel ${ecareScenarioVo.ecareInfoVo.channelType}">
                                        <c:choose>
                                            <c:when test="${ecareScenarioVo.ecareInfoVo.channelType eq 'M'}">E</c:when>
                                            <c:when test="${ecareScenarioVo.ecareInfoVo.channelType eq 'T'}">M</c:when>
                                            <c:otherwise>${ecareScenarioVo.ecareInfoVo.channelType}</c:otherwise>
                                        </c:choose>
                                    </em>
                                </td>
                                <td>${ecareScenarioVo.ecareInfoVo.ecareStsNm}</td><!-- 상태 -->

                                <c:if test="${serviceType eq 'S' and subType eq 'S'}">
                                <td class="text-right">${ecareScenarioVo.ecareInfoVo.sendResultVo.target_cnt} <spring:message code="common.menu.persons"/></td><!-- 대상수 -->
                                <td><!-- 발송시간 -->
                                    <c:if test="${not empty fn:trim(ecareScenarioVo.ecareInfoVo.sendResultVo.sendStartTm)}">
                                        <fmt:parseDate value="${ecareScenarioVo.ecareInfoVo.sendResultVo.sendStartTm}" var="invokeTm" pattern="HHmmss" />
                                        <fmt:formatDate value="${invokeTm}" pattern="HH:mm:ss" />
                                    </c:if>
                                </td>
                                </c:if>

                                <td>${ecareScenarioVo.ecareInfoVo.sendResultVo.userNm}</td><!-- 담당자 -->
                                <td>${ecareScenarioVo.ecareInfoVo.sendResultVo.gprNm}</td><!-- 요청 부서 -->
                            </tr>
                            </c:forEach>
                            </c:if>

                            <c:if test="${empty ecareReportList}" >
                            <tr>
                                <c:choose>
                                    <c:when test="${serviceType eq 'S' and subType eq 'S'}">
                                        <td colspan="8"><spring:message code="report.ecare.list.nodata" /></td><!-- 검색 결과가 없습니다. -->
                                    </c:when>
                                    <c:otherwise>
                                        <td colspan="6"><spring:message code="report.ecare.list.nodata" /></td><!-- 검색 결과가 없습니다. -->
                                    </c:otherwise>
                                </c:choose>
                           </tr>
                           </c:if>
                           </tbody>
                        </table>
                    </div><!-- //Light table -->

                    <!-- 페이징 -->
                    ${paging}
                </div><!-- e.card body -->
            </div><!-- //card -->
        </div><!-- e.page content -->
    </div><!-- e.main-panel -->
    </form>

<form id="pageFrm" name="pageFrm" method="post">
    <input type="hidden" id="scenarioNo" name="scenarioNo" />
    <input type="hidden" id="ecareNo" name="ecareNo" />
    <input type="hidden" id="cmd" name="cmd" />
    <input type="hidden" id="page" name="page" />
    <input type="hidden" id="searchColumn" name="searchColumn" value="${searchColumn}">
    <input type="hidden" id="searchWord" name="searchWord" value="${searchWord}">
    <input type="hidden" id="orderColumn" name="orderColumn" value="${orderColumn}">
    <input type="hidden" id="orderSort" name="orderSort" value="${orderSort}">
    <input type="hidden" id="countPerPage" name="countPerPage" value="${countPerPage}">
    <input type="hidden" id="nowPage" name="nowPage" value="${nowPage}">
    <input type="hidden" id="serviceType" name="serviceType" value="${serviceType}">
    <input type="hidden" id="subType" name="subType" value="${subType}">
    <input type="hidden" id="searchStartDt" name="searchStartDt" value="${searchQstartDt}">
    <input type="hidden" id="searchEndDt" name="searchEndDt" value="${searchQendDt}">
    <input type="hidden" id="webExecMode" name="webExecMode" value="${webExecMode}">
    <input type="hidden" id="channelType" name="channelType" />
</form>
</body>
</html>
