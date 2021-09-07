<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!-- 카카오 버튼-->
<c:forEach items="${kakaoButtonList}" var="kakaoButton" varStatus="status">
<tr id="kakaoButtonList" class="kakaoButtonList">
    <th ${kakaoButton.linkType eq 'AL' ? 'rowspan="2"' : ''}>
        <spring:message code='template.kakao.button.type' />
    </th>
    <td class="ls--1px" ${kakaoButton.linkType eq 'AL' ? 'rowspan="2"' : ''}>
        <spring:message code='template.kakao.link.type.${kakaoButton.linkType}' text="no Code" />
        <input data="linkType" name="linkType" type="hidden" value="${kakaoButton.linkType}" />
    </td>
    <th class="ls--1px" ${kakaoButton.linkType eq 'AL' ? 'rowspan="2"' : ''}><em class="required">required</em><spring:message code='template.kakao.button.name' /></th>
    <td ${kakaoButton.linkType eq 'AL' ? 'rowspan="2"' : ''}>
        <input data="name" name="name[${status.index+1}]" id="name[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.name}" ${kakaoButton.linkType eq 'AC' or kakaoButton.linkType eq 'BC' ? 'disabled' : ''} />
    </td>

    <c:choose>
    <c:when test="${kakaoButton.linkType == 'WL'}">
    <th class="ls--1px"><em class="required">required</em><spring:message code='template.kakao.link.mobile' /></th>
    <td>
        <input data="linkMo" name="linkMo[${status.index+1}]" id="linkMo[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.linkMo}" title="${kakaoButton.linkMo}" />
    </td>
    <th class="ls--1px"><spring:message code='template.kakao.link.pc' /></th>
    <td>
        <input data="linkPc" name="linkPc[${status.index+1}]"  id="linkPc[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.linkPc}" title="${kakaoButton.linkPc}" />
    </td>
    </c:when>
    <c:when test="${kakaoButton.linkType == 'AL'}">
    <th class="ls--1px"><em class="required">required</em><spring:message code='template.kakao.link.android' /></th><!-- 안드로이드 앱링크 -->
    <td>
        <input data="linkAnd" name="linkAnd[${status.index+1}]" id="linkAnd[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.linkAnd}" title="${kakaoButton.linkAnd}" />
    </td>
    <th class="ls--1px"><em class="required">required</em><spring:message code='template.kakao.link.ios' /></th><!-- iOS 앱링크 -->
    <td>
        <input data="linkIos" name="linkIos[${status.index+1}]" id="linkIos[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.linkIos}" title="${kakaoButton.linkIos}" />
    </td>
    </c:when>
    </c:choose>
</tr>
<c:choose>
<c:when test="${kakaoButton.linkType == 'AL'}">
<tr id="kakaoButtonSubList" class="kakaoButtonList">
    <th class="ls--1px"><em class="required">required</em><spring:message code='template.kakao.link.mobile' /></th>
    <td>
        <input data="linkMo" name="linkMo[${status.index+1}]" id="linkMo[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.linkMo}" title="${kakaoButton.linkMo}" />
    </td>
    <th class="ls--1px"><spring:message code='template.kakao.link.pc' /></th>
    <td>
        <input data="linkPc" name="linkPc[${status.index+1}]"  id="linkPc[${status.index+1}]" type="text" class="form-control form-control-sm" value="${kakaoButton.linkPc}" title="${kakaoButton.linkPc}" />
    </td>
</tr>
</c:when>
</c:choose>
</c:forEach>
<!-- e.카카오 버튼-->
