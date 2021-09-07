<%-------------------------------------------------------------------------------------------------
 * 이케어 리포트 상단 공통 인클루드
 * - 인클루드에서 사용할 데이타는 리포트 인터셉터에서 실어서 보내준다.
 * - 이전 파일명 : ecare_report_tab_inc.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
//검색결과 돌아가기
    $(document).ready(function() {
        initTabEventBind();
    });

    function initTabEventBind() {
        // 목록 버튼 클릭
        $("#listBtn").on("click", function(event) {
            var form = $('<form>', {action : '/report/ecare/ecareRptList.do', method : 'post', target : '_self'});  // /report/ecare/ecare_list.do

            form.append($('<input>', {type: 'hidden', name: 'serviceType',    value: '${param.serviceType}'}));
            form.append($('<input>', {type: 'hidden', name: 'subType',        value: '${param.listSubType}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchChannel',  value: '${param.channelType}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchQstartDt', value: '${param.searchStartDt}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchQendDt',   value: '${param.searchEndDt}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchWord',     value: '${param.searchWord}'}));

            form.appendTo('body').submit();
        });
    }
</script>
<div class="card-body px-0 pb-0">
    <div class="row align-items-center py-1 table_option">
        <div class="col-9"><!-- tabs -->
            <ul class="nav nav-tabs tab02" id="tabs-text" role="tablist">
                <c:if test="${scenarioInfoVo.ecareInfoVo.channelType ne 'P'}">
                <li class="nav-item">
                    <!-- /report/ecare/summary.do -->
                    <a class="nav-link mb-sm-3 mb-md-0 <c:if test="${requestUri eq '/report/ecare/summaryRpt.do'}">active</c:if>" href="/report/ecare/summaryRpt.do?scenarioNo=${scenarioInfoVo.scenarioNo}&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}">
                        <spring:message code="report.campaign.head.alt.summary"/><!-- 요약분석 -->
                    </a>
                </li>
                </c:if>
                <li class="nav-item">
                    <!-- /report/ecare/err_summary.do -->
                    <a class="nav-link mb-sm-3 mb-md-0 <c:if test="${requestUri eq '/report/ecare/errorRpt.do'}">active</c:if>" href="/report/ecare/errorRpt.do?scenarioNo=${scenarioInfoVo.scenarioNo}&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}">
                        <spring:message code="report.campaign.head.alt.error"/><!-- 오류분석 -->
                    </a>
                </li>
                <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">
                <li class="nav-item">
                    <!-- /report/ecare/linkclick.do -->
                    <a class="nav-link mb-sm-3 mb-md-0 <c:if test="${requestUri eq '/report/ecare/linkClickRpt.do'}">active</c:if>" href="/report/ecare/linkClickRpt.do?scenarioNo=${scenarioInfoVo.scenarioNo}&ecareNo=${scenarioInfoVo.ecareInfoVo.ecareNo}&serviceType=${scenarioInfoVo.ecareInfoVo.serviceType}&subType=${scenarioInfoVo.ecareInfoVo.subType}&resultSeq=${scenarioInfoVo.ecareInfoVo.resultSeq}&reportDt=${scenarioInfoVo.ecareInfoVo.reportDt}">
                        <spring:message code="report.campaign.head.alt.link"/><!--  링크클릭 분석 -->
                    </a>
                </li>
                </c:if>
            </ul>
        </div>
        <div class="col-3 justify-content-end" style="padding-top: 10px;">
            <button type="button" class="btn btn-sm btn-outline-primary" id="listBtn">
                <i class="fas fa-list"></i> <spring:message code="button.list"/><!-- 목록 -->
            </button>
            <button type="button" class="btn btn-sm btn-outline-primary" onclick="history.go(-1);">
                <i class="fas fa-chevron-left"></i> <spring:message code="button.previous"/><!-- 이전 -->
            </button>
        </div>
    </div>
</div><!-- //card body -->

<div class="table-responsive gridWrap">
    <table class="table table-sm dataTable table-fixed">
        <colgroup>
            <col width="11%" />
            <col width="*" />
            <col width="11%" />
            <col width="20%" />
            <col width="11%" />
            <col width="15%" />
        </colgroup>
        <tbody>
        <tr>
            <th scope="row"><spring:message code="report.ecare.title.name_${webExecMode}" /></th><!-- 이케어명 -->
            <td colspan="3">${scenarioInfoVo.ecareInfoVo.ecareNm}</td>
            <th scope="row"><spring:message code="ecare.menu.cno_${webExecMode}" /></th><!-- 이케어 번호 -->
            <td>
                <em class="txt_channel ${scenarioInfoVo.ecareInfoVo.channelType}">
                    <c:choose>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">E</c:when>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.channelType eq 'T'}">M</c:when>
                        <c:otherwise>${scenarioInfoVo.ecareInfoVo.channelType}</c:otherwise>
                    </c:choose>
                </em>
                <span>${scenarioInfoVo.ecareInfoVo.ecareNo}</span>
            </td>
        </tr>
        <tr>
            <c:if test="${ scenarioInfoVo.ecareInfoVo.serviceType == 'S' && scenarioInfoVo.ecareInfoVo.subType == 'S' }">
            <th scope="row"><spring:message code="report.ecare.title.date" /></th><!-- 발송수행일 -->
            <td>${ecareSendResultVo.sendStartDtToDateStr} ~ ${ecareSendResultVo.sendEndDtToDateStr}</td>
            <th scope="row"><spring:message code="report.ecare.title.time" /></th><!-- 발송시간 -->
            <td>${ecareSendResultVo.formatPeriod}</td>
            </c:if>

            <th scope="row"><spring:message code="report.ecare.title.send.status" /></th><!-- 현재 수행 상태 -->
            <td <c:if test="${scenarioInfoVo.ecareInfoVo.serviceType != 'S' || scenarioInfoVo.ecareInfoVo.subType != 'S' }">colspan="5"</c:if>>
                <c:choose>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='R'}"><spring:message code="report.ecare.status.run" /></c:when>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='P'}"><spring:message code="report.ecare.status.stop" /></c:when>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='E'}"><spring:message code="report.ecare.status.end" /></c:when>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='W'}"><spring:message code="report.ecare.status.send" /></c:when>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th><spring:message code="report.ecare.title.type" /></th><!-- 이케어 유형 -->
            <td>
                <c:choose>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.relationType=='N' or empty scenarioInfoVo.ecareInfoVo.relationType}"><spring:message code="report.ecare.type.normal" /></c:when>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.relationType=='R'}">${scenarioInfoVo.ecareInfoVo.depthNo}<spring:message code="report.ecare.type.resend" /></c:when>
                    <c:when test="${scenarioInfoVo.ecareInfoVo.relationType=='L'}">${scenarioInfoVo.ecareInfoVo.depthNo}<spring:message code="report.ecare.type.target.resend" /></c:when>
                </c:choose>
            </td>
            <th scope="row"><spring:message code="report.ecare.title.grp" /></th><!-- 담당부서 -->
            <td>${scenarioInfoVo.userVo.grpNm}</td>
            <th scope="row"><spring:message code="report.ecare.title.user" /></th><!-- 담당자 -->
            <td>${scenarioInfoVo.userVo.nameKor}</td>
        </tr>
        </tbody>
    </table>
</div><!-- e.Light table -->

<!-- 검색목록 돌아가기 -->
<form id="pageBackFrm" name="pageBackFrm" method="post">
    <input type="hidden" name="searchWord" value="${searchEWord}">
    <input type="hidden" name="searchQstartDt" value="${searchEQstartDt}">
    <input type="hidden" name="searchQendDt" value="${searchEQendDt}">
</form>