<%-------------------------------------------------------------------------------------------------
 * - [캠페인>캠페인 등록] 캠페인 테스트발송(팝업) - 테스트 발송 결과보기<br/>
 * - [이케어>이케어 등록] 이케어 테스트발송(팝업) - 테스트 발송 결과보기 <br/>
 * - URL : /common/testSendResultList.do <br/>
 * - Controller : com.mnwise.wiseu.web.common.web.TestSendController <br/>
 * - 이전 파일명 : tester_popup_send_view.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body >
    <div class="table-responsive overflow-auto mt--1">
        <table class="table table-xs dataTable table-fixed">
            <colgroup>
                <col width="10%" />
                <col width="20%" />
                <col width="10%" />
                <col width="15%" />
                <col width="50%" />
            </colgroup>
            <thead class="thead-light">
                <tr>
                    <th><spring:message code="common.menu.no"/></th><!-- No -->
                    <th>
                        <c:choose>
                            <c:when test="${channelType eq 'M'}"><spring:message code="common.menu.remail"/><!-- 수신자 이메일 --></c:when>
                            <c:when test="${channelType eq 'F'}"><spring:message code="common.menu.rfax"/><!-- 수신자 팩스 --></c:when>
                            <c:otherwise><spring:message code="common.menu.rtel"/><!-- 수신자 전화번호 --></c:otherwise>
                        </c:choose>
                    </th><!-- 수신자 이메일 -->
                    <th><spring:message code="common.menu.rname"/></th><!-- 수신자 이름 -->
                    <th><spring:message code="common.menu.sdate"/></th><!-- 발송일 -->
                    <th><spring:message code="common.menu.sresult"/></th><!-- 발송결과 -->
                </tr>
            </thead>
            <c:if test="${!empty testSendList}">
                <c:forEach var="testSendVo" items="${testSendList}" varStatus="testSendLoop">
                    <tr class="messageRow">
                        <td>${testSendVo.serviceNo}</td>
                        <td>${testSendVo.customerEmail}</td>
                        <td>${testSendVo.customerNm}</td>
                        <td>${testSendVo.sendDt} ${testSendVo.sendTm}</td>
                        <td><div class="long_message">[${testSendVo.errorCd}] ${testSendVo.errorMsg} ${testSendVo.errMsgKor}</div></td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${empty testSendList}">
                <tr>
                    <td colspan="5"><spring:message code="common.msg.nodata"/></td><!-- 검색결과가 없습니다. -->
                </tr>
            </c:if>
        </table>
    </div>

    <!-- 페이징 -->
    <c:import url="/common/page.do">
        <c:param name="viewPath"     value="/common/page"/>
        <c:param name="actionPath"   value="/common/testSendPopup.do"/><%-- /common/testSend_popup.do --%>
        <c:param name="total"        value="${totalCount}"/>
        <c:param name="countPerPage" value="${countPerPage}"/>
        <c:param name="nowPage"      value="${nowPage}"/>
        <c:param name="hiddenParam"  value="no:${no}"/>
        <c:param name="hiddenParam"  value="countPerPage:${countPerPage}"/>
        <c:param name="hiddenParam"  value="serviceType:${serviceType}"/>
        <c:param name="hiddenParam"  value="serviceNo:${serviceNo}"/>
    </c:import>
</body>
</html>