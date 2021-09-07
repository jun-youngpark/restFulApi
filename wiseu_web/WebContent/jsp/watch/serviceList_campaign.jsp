<%-------------------------------------------------------------------------------------------------
 * - [wiseWatch>캠페인 탭] 캠페인 목록
 * - URL : /watch/serviceList.do?menu=campaign
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchController
 * - 이전 파일명 : watch_info_service.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initPage();
    });

    function initPage() {
        new mdf.DataTable("#serviceListTable", {
            ordering : true,
            scrollX: false,
            autoWidth: false,
            order: [[1, "desc"]],
            columns: [
                {orderable: false},  // 0
                null,                // 1
                {orderable: false},  // 2
                {orderable: false},  // 3
                {orderable: false},  // 4
                {orderable: false},  // 5
                {orderable: false},  // 6
                {orderable: false}   // 7
            ]
        });
    }
</script>

<table id="serviceListTable" class="table table-xs dataTable table-fixed overflow-x-hidden" style="margin: 6px 0 0 0 !important;">
    <thead class="thead-light">
    <tr>
        <th scope="col" width="5%"><spring:message code="watch.menu.select"/></th><!-- 선택 -->
        <th scope="col" width="10%"><spring:message code="watch.menu.sdate"/></th><!-- 발송날짜 -->
        <th scope="col" width="10%"><spring:message code="campaign.menu.cno"/></th><!-- 캠페인 번호 en:width -->
        <th scope="col" width="37%"><spring:message code="campaign.menu.cname"/></th><!-- 캠페인명 -->
        <th scope="col" width="8%"><spring:message code="watch.menu.channel"/></th><!-- 채널 -->
        <th scope="col" width="10%"><spring:message code="watch.menu.smod"/></th><!-- 발송모드 -->
        <th scope="col" width="10%"><spring:message code="watch.menu.sstatus"/></th><!-- 발송상태 -->
        <th scope="col" width="10%"><spring:message code="common.menu.creator"/></th><!-- 작성자 -->
    </tr>
    </thead>

    <tbody>
    <c:set var="i" value="0"></c:set>
    <c:forEach var="list" items="${getListService}" varStatus="status">
    <c:if test="${list.client eq 'EM' }">
    <tr>
        <th scope="row">
            <div class="custom-control custom-radio">
                <c:choose>
                    <c:when test="${list.tid eq tid}">
                        <script>$("#serviceStatus").val('${list.serviceStatus}');</script>
                        <input type="radio" name="radio" id="radio_${status.count}" value="${list.tid}" checked="checked" onclick="javascript:selectServiceStatus('${list.tid}','${list.serviceStatus}');" class="custom-control-input"/>
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="radio" id="radio_${status.count}" value="${list.tid}" onclick="javascript:selectServiceStatus('${list.tid}','${list.serviceStatus}');" class="custom-control-input"/>
                    </c:otherwise>
                </c:choose>
                <label class="custom-control-label" for="radio_${status.count}"></label>
            </div>
        </th>
        <td><c:if test="${empty list.startTime}">&nbsp;</c:if>${list.startTime}</td>
        <td>${list.no}</td>
        <td class="text-left"><c:if test="${empty list.serviceName}">&nbsp;</c:if>${list.serviceName}</td>
        <td><spring:message code="common.channel.${list.channel}"/></td>
        <td>
            <c:choose>
                <c:when test="${list.sendMode eq 'R'}"><spring:message code="watch.menu.rsend"/><!-- 실발송 --></c:when>
                <c:otherwise><spring:message code="watch.menu.tsend"/><!-- 테스트발송 --></c:otherwise>
            </c:choose>
        </td>
        <td>
            <c:choose>
                <c:when test="${list.serviceStatus eq 'R'}"><spring:message code="campaign.status.R"/><!-- 발송대기 --></c:when>
                <c:when test="${list.serviceStatus eq 'W'}"><spring:message code="campaign.status.W"/><!-- 발송중 --></c:when>
                <c:when test="${list.serviceStatus eq 'E'}"><spring:message code="campaign.status.E"/><!-- 발송종료 --></c:when>
                <c:when test="${list.serviceStatus eq 'O'}"><spring:message code="campaign.status.O"/><!-- 발송에러 --></c:when>
                <c:when test="${list.serviceStatus eq 'P'}"><spring:message code="campaign.status.P"/><!-- 보류 --></c:when>
                <c:when test="${list.serviceStatus eq 'S'}"><spring:message code="campaign.status.S"/><!-- 일시정지 --></c:when>
                <c:when test="${list.serviceStatus eq 'T'}"><spring:message code="campaign.status.T"/><!-- 분할정지 --></c:when>
                <c:when test="${list.serviceStatus eq 'I'}"><spring:message code="campaign.status.I"/><!-- 작성중 --></c:when>
                <c:when test="${list.serviceStatus eq 'D'}"><spring:message code="campaign.status.D"/><!-- 승인거부 --></c:when>
                <c:when test="${list.serviceStatus eq 'C'}"><spring:message code="campaign.status.C"/><!-- 승인요청 --></c:when>
                <c:otherwise><spring:message code="watch.menu.no"/><!-- 알수없음 --></c:otherwise>
            </c:choose>
        </td>
        <td>${list.userId}</td>
    </tr>
    <c:set var="i" value="${i + 1}"></c:set>
    </c:if>
    </c:forEach>

    <c:if test="${i eq 0}">
    <tr>
        <td colspan="9"><spring:message code="watch.alert.msg3"/></td><!-- 캠페인 서비스 정보가 없습니다. -->
    </tr>
    </c:if>
    </tbody>
</table>
