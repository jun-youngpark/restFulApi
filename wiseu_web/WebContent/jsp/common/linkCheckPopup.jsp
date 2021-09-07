<%-------------------------------------------------------------------------------------------------
 - [캠페인>캠페인 등록>2단계] 링크 유효성 검사 (팝업)
 - [이케어>이케어 등록>2단계] 링크 유효성 검사 (팝업)
 - URL : /common/linkCheckPopup.do
 - Controller : com.mnwise.wiseu.web.common.web.LinkCheckController
 * - 이전 파일명 : linkcheck_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="campaign.alt.action.link"/></title><!-- 링크 유효성 검사 -->
<%@ include file="/jsp/include/plugin.jsp" %>
</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0"><spring:message code="campaign.alt.action.link"/></h5></div><!-- 링크 유효성 검사 -->
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-sm dataTable table-fixed">
                <thead class="thead-light">
                <tr>
                    <th scope="col" width="100"><spring:message code="common.alert.link.class"/></th><!-- 구분 -->
                    <th scope="col"><spring:message code="common.alert.link.details"/></th><!-- 상세내용 -->
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="font-weight-bold">A TAG</td>
                    <td class="text-left">
                        <c:if test="${empty linkList.alinkList}"><spring:message code="common.alert.link.a"/><!-- A 태그 중에 링크 문제는 없습니다. --></c:if>
                        <c:if test="${!empty linkList.alinkList}">
                            <c:forEach var="alinkList" items="${linkList.alinkList}">
                                ${alinkList.linkUrl}</br>
                            </c:forEach>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td class="font-weight-bold">AREA TAG</td>
                    <td class="text-left">
                        <c:if test="${empty linkList.areaList}"><spring:message code="common.alert.link.area"/><!-- AREA 태그 중에 링크 문제는 없습니다. --></c:if>
                        <c:if test="${!empty linkList.areaList}">
                            <c:forEach var="areaList" items="${linkList.areaList}">
                                ${areaList.linkUrl}</br>
                            </c:forEach>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td class="font-weight-bold">IMAGE TAG</td>
                    <td class="text-left">
                        <c:if test="${empty linkList.imageList}"><spring:message code="common.alert.link.image"/><!-- IMAGE 태그 중에 링크 문제는 없습니다. --></c:if>
                        <c:if test="${!empty linkList.imageList}">
                            <c:forEach var="imageList" items="${linkList.imageList}">
                                ${imageList.linkUrl}</br>
                            </c:forEach>
                        </c:if>
                    </td>
                </tr>
                </tbody>
            </table>
        </div><!-- //Light table -->
    </div>
</div>
</body>
</html>