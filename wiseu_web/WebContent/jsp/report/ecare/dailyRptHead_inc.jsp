<%-------------------------------------------------------------------------------------------------
 * 이케어 리포트 상단 공통 인클루드
 * - 인클루드에서 사용할 데이타는 리포트 인터셉터에서 실어서 보내준다.
 * - 이전 파일명 : ecare_report_head_inc.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initHeadEventBind();
        initHeadPage();
    });

    function initHeadEventBind() {
        // 목록 버튼 클릭
        $("#listBtn").on("click", function(event) {
            var form = $('<form>', {action : '/report/ecare/ecareRptList.do', method : 'post', target : '_self'});  // /report/ecare/ecare_list.do

            form.append($('<input>', {type: 'hidden', name: 'serviceType',    value: '${param.serviceType}'}));
            form.append($('<input>', {type: 'hidden', name: 'subType',        value: '${param.subType}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchChannel',  value: '${param.channelType}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchQstartDt', value: '${param.searchStartDt}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchQendDt',   value: '${param.searchEndDt}'}));
            form.append($('<input>', {type: 'hidden', name: 'searchWord',     value: '${param.searchWord}'}));

            form.appendTo('body').submit();
        });
    }

    function initHeadPage() {
        new mdf.Date("#searchStartDt");  // 발송일-시작일
        new mdf.Date("#searchEndDt");  // 발송일-종료일
    }

</script>

<div class="card-body px-0 pb-0">
    <div class="row align-items-center py-1 table_option">
        <div class="col-2">
            <button type="button" class="btn btn-sm btn-outline-primary" id="listBtn">
                <i class="fas fa-list"></i> <spring:message code="button.list"/><!-- 목록 -->
            </button>
        </div>
        <div class="col-10 searchWrap">
            <div class="form-group searchbox">
                <label class="form-control-label mr-1 txt"> <spring:message code="report.ecare.title.day" /> :</label><!-- 발송일 -->
                <div class="input_datebox">
                    <input type="hidden" name="searchStartDt" id="searchStartDt" value="${searchStartDt}" maxlength="10" />
                </div>
                <span class="txt">~</span>
                <div class="input_datebox">
                    <input type="hidden" name="searchEndDt" id="searchEndDt" value="${searchEndDt}" maxlength="10" />
                </div>
            </div>
            <div class="form-group searchbox pl-1">
                <button class="btn btn-sm btn-outline-info btn_search" id="searchBtn">
                    <spring:message code="button.search"/><!-- 검색 -->
                </button>
            </div>
        </div>
    </div><!-- //row -->

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

            <c:if test="${webExecMode eq '1' }">
            <tr>
                <th scope="row"><spring:message code="report.ecare.title.schedule" /></th><!-- 스케쥴 -->
                <td <c:if test="${scenarioInfoVo.ecareInfoVo.channelType ne 'M'}">colspan="3"</c:if>>
                    <c:if test="${scenarioInfoVo.ecareInfoVo.cycleCd eq ''}"><spring:message code="report.ecare.cycle.real" /><!-- 실시간 --></c:if>
                    <c:if test="${scenarioInfoVo.ecareInfoVo.cycleCd eq '1'}"><spring:message code="report.ecare.cycle.daily" /><!-- 매일 --></c:if>
                    <c:if test="${scenarioInfoVo.ecareInfoVo.cycleCd eq '2'}"><spring:message code="report.ecare.cycle.weekly" /><!-- 매주 --></c:if>
                    <c:if test="${scenarioInfoVo.ecareInfoVo.cycleCd eq '3'}"><spring:message code="report.ecare.cycle.monthly" /><!-- 매월 --></c:if>
                    <c:if test="${scenarioInfoVo.ecareInfoVo.cycleCd eq '4'}"><spring:message code="report.ecare.cycle.one" /><!-- 한번만 --></c:if>
                </td>

                <c:if test="${scenarioInfoVo.ecareInfoVo.channelType eq 'M'}">
                    <th scope="row"><spring:message code="report.ecare.title.trace" /></th><!-- 반응추적기간 -->
                    <td>${scenarioInfoVo.ecareInfoVo.termType}<spring:message code="report.ecare.trace.week" /></td>
                </c:if>
                <th scope="row"><spring:message code="report.ecare.title.present.status" /></th><!-- 현재 수행 상태 -->
                <td>
                    <c:choose>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='R'}"><spring:message code="report.ecare.status.run" /><!-- 실행 --></c:when>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='P'}"><spring:message code="report.ecare.status.stop" /><!-- 중지 --></c:when>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='E'}"><spring:message code="report.ecare.status.end" /><!-- 발송완료 --></c:when>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.ecareSts=='W'}"><spring:message code="report.ecare.status.send" /><!-- 발송중 --></c:when>
                    </c:choose>
                </td>
            </tr>
            </c:if>
            <tr>
                <th><spring:message code="report.ecare.title.type" /></th><!-- 이케어 유형 -->
                <td>
                    <c:choose>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.relationType=='N' or empty scenarioInfoVo.ecareInfoVo.relationType}">
                            <spring:message code="report.ecare.type.normal" /><!-- 일반발송 -->
                        </c:when>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.relationType=='R'}">
                            ${scenarioInfoVo.ecareInfoVo.depthNo}<spring:message code="report.ecare.type.resend" /><!-- 차 발송 (재) -->
                        </c:when>
                        <c:when test="${scenarioInfoVo.ecareInfoVo.relationType=='L'}">
                            ${scenarioInfoVo.ecareInfoVo.depthNo}<spring:message code="report.ecare.type.target.resend" /><!-- 차 반응 재발송 -->
                        </c:when>
                    </c:choose>
                </td>
                <th scope="row"><spring:message code="report.ecare.title.grp" /></th><!-- 담당부서 -->
                <td>${scenarioInfoVo.userVo.grpNm}</td>
                <th scope="row"><spring:message code="report.ecare.title.user" /></th><!-- 담당자 -->
                <td>${scenarioInfoVo.userVo.nameKor}</td>
            </tr>
            </tbody>
        </table>
    </div><!-- //Light table -->
</div><!-- //card body -->
