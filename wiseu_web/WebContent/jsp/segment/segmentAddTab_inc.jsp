<%-------------------------------------------------------------------------------------------------
 * [캠페인>캠페인 등록>메시지 작성>대상자 등록(팝업)] 반응대상자 탭
 * - URL : /segment/segment_addtab_include.do <br/>
 * - Controller : com.mnwise.wiseu.web.segment.web.SegmentAddTabController <br/>
 * - 이전 파일명 : segment_add_tab_include.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<c:choose>
    <c:when test="${not empty tabUse}">
        <ul class="nav nav-tabs tab01" style="margin-bottom: -9px;"><!-- tab -->
            <li class="nav-item"><!-- 대상자 파일 올리기 /segment/target/upload.do -->
                <a class="nav-link <c:if test="${tabType eq 'F'}">active</c:if>" id="importFile" href="/target/fileSegment1Step.do?tabUse=${tabUse}&tabType=F&segmentNo=${segmentNo}" id="importFile"><spring:message code='segment.msg.upload'/></a>
            </li>
            <li class="nav-item"><!-- 반응 대상자 /segment/target/linkservice.do -->
                <a class="nav-link <c:if test="${tabType eq 'L'}">active</c:if>" id="linkclick" href="/target/linkClickServiceList.do?serviceType=EM&tabUse=${tabUse}&tabType=L&segmentNo=${segmentNo}"><spring:message code='segment.msg.reaction'/></a>
            </li>
            <li class="nav-item dp-none"><!-- 쿼리 등록 -->
                <a class="nav-link <c:if test="${tabType eq 'S'}">active</c:if>" id="target-tab" href="/target/sqlSegment1Step.do?tabUse=Y&tabType=S&segmentNo=${segmentVo.segmentNo}"><spring:message code='segment.msg.regquery'/></a>
            </li>
        </ul>
</c:when>
</c:choose>
