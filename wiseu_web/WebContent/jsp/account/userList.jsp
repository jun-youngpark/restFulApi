<%-------------------------------------------------------------------------------------------------
 * - [사용자>사용자 관리] 사용자 관리 <br/>
 * - URL : /account/account_user_list.do <br/>
 * - Controller :com.mnwise.wiseu.web.account.web.AccountUserController <br/>
 * - 이전 파일명 : account_user.jsp, accountUserList.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>

<table class="table table-xs dataTable table-hover mt-1">
    <thead class="thead-light">
    <tr>
        <c:if test="${isaRole eq 'A'}">
        <th width="45"><spring:message code="account.menu.user.select"/></th><!-- 선택 -->
        </c:if>

        <th><spring:message code="common.menu.no"/></th><!-- no -->
        <th><spring:message code="account.menu.user.id"/></th><!-- 사용자ID  -->
        <th><spring:message code="account.menu.user.name"/></th><!-- 성명  -->
        <th><spring:message code="account.menu.user.dname"/></th><!-- 부서명  -->
        <th><spring:message code="account.menu.user.position"/></th><!-- 직급 -->
        <th><spring:message code="account.menu.user.email"/></th><!-- 이메일 -->
        <th><spring:message code="account.menu.user.permission"/></th><!-- 권한 -->
        <th><spring:message code="account.menu.user.contact"/></th><!-- 전화번호 -->

        <c:if test="${(UserVo.userRole eq 'M') or (UserVo.userRole eq 'A')}">
        <th><spring:message code="account.menu.user.status"/></th><!-- 상태 -->
        <th><spring:message code="account.menu.user.unlocking"/></th><!-- 잠금해제 -->
        </c:if>
    </tr>
    </thead>
    <tbody>

    <c:forEach var="loop" items="${userList}" varStatus="i">
    <tr style="cursor: pointer;" onclick="javascript:popupUser('${loop.userId}', 'view');">
        <c:if test="${isaRole eq 'A'}">
            <th scope="row" onclick="event.cancelBubble=true">
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" id="checkbox_${i.count}" name="checkUserList" value="${loop.userId}" class="custom-control-input" />
                    <label class="custom-control-label" for="checkbox_${i.count}"></label>
                </div>
            </th>
        </c:if>

        <td>${(i.index+1)+prePageCnt}</td>
        <td class="text-left">${loop.userId}</td>
        <td class="text-left">${loop.nameKor}</td>
        <td class="text-left">${loop.grpNm}</td>
        <td>${loop.userClass}</td>
        <td class="text-left">${loop.email}</td>
        <td>${loop.userTypeNm}</td>
        <td>${loop.telNo}</td>

        <c:if test="${(UserVo.userRole eq 'M') or (UserVo.userRole eq 'A')}">
        <td id="status_${loop.userId}" >
            <c:choose>
                <c:when test="${loop.loginCnt > 4}">Locked</c:when>
                <c:otherwise>Active(${loop.loginCnt})</c:otherwise>
            </c:choose>
        </td>
        <td id="reset_${loop.userId}" onclick="event.cancelBubble=true">
            <c:choose>
                <c:when test="${loop.loginCnt > 0}">
                    <button type="button" class="btn btn-xs btn-outline-primary btn-round" onclick="javascript:resetLoginCnt('${loop.userId}');">
                        <spring:message code="button.reset"/><!-- 초기화 -->
                    </button>
                </c:when>
                <c:otherwise>-</c:otherwise>
            </c:choose>
        </td>
        </c:if>
    </tr>
    </c:forEach>

    <c:if test="${empty userList}">
    <tr>
        <td colspan="11"><spring:message code="account.alert.user.msg1"/></td><!-- 등록된 사용자가 없습니다. -->
    </tr>
    </c:if>
    </tbody>
</table>

<!-- 페이징 -->
<c:import url="/common/page.do">
        <c:param name="viewPath" value="/common/page_ajax" />
        <c:param name="actionPath" value="/account/userList.do" /><%-- /template/brandtalkTemplateList.do --%>
        <c:param name="method" value="get" />
        <c:param name="total" value="${totalCount}" />
        <c:param name="countPerPage" value="${countPerPage}" />
        <c:param name="nowPage" value="${nowPage}" />
        <c:param name="hiddenParam" value="grpCd:${grpCd}" />
        <c:param name="returnDocumentId" value="userListDiv"/>
    </c:import>
