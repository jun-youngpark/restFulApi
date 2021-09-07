<%-------------------------------------------------------------------------------------------------
 * - [사용자 관리>부서 권한 관리] 부서 권한 관리 <br/>
 * - URL : /account/deptRoleList.do <br/>
 * - Controller : com.mnwise.wiseu.web.account.web.AccountDeptPermissionController
 * - 이전 파일명 : accountDeptPermissionList.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        initRolePage();
    });

    function initRolePage() {
        <c:if test="${(isaRole ne 'M') and  (isaRole ne 'A')}">
        $("#roleListTable :input[type=checkbox]").attr("disabled", "disabled");
        </c:if>
    }
</script>

<form id="deptFrm" name="deptFrm" method="post" action="/account/deptRoleList.do"><!-- /account/account_dept_permission_list.do -->
<input type="hidden" name="grpCd" id="grpCd" value="${userGrpVo.grpCd}" />
<input type="hidden" name="grpNm" id="grpNm" value="${userGrpVo.grpNm}" />
<input type="hidden" name="supragrpCd" id="supragrpCd" value="${userGrpVo.supraGrpCd}" />
<input type="hidden" name="userRole" id="userRole" value="${UserVo.userRole}" />
<input type="hidden" name="workDoc" id="workDoc" value="by admin" />
<input type="hidden" name="permissionListSize" id="permissionListSize" value="${permissionList == null ? 0 : permissionList.size()}" />

<div class="table-responsive overflow-auto" style="height: 430px;">
    <table class="table table-sm dataTable table-fixed" id="roleListTable">
        <thead class="thead-light">
        <tr>
            <th width="280"><spring:message code="account.menu.permission.menu" /></th><!-- 메뉴 -->
            <th><spring:message code="account.menu.permission.permission" /></th><!-- 권한 -->
        </tr>
        </thead>
        <tbody>
        <c:forEach var="permissionRoleVo" items="${permissionList}" varStatus="status">
        <input type="hidden" name="menuCd" value="${permissionRoleVo.menuCd}" />

        <c:if test="${empty permissionRoleVo.pmenuCd}">
        <tr onmouseout="this.style.backgroundColor='';"><!-- 상위목록 -->
            <td class="th-bg text-left font-weight-bold">${permissionRoleVo.menuNm}</td>
            <td class="th-bg text-left font-weight-bold">
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" id="PR_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="R"
                    <c:if test="${permissionRoleVo.readAuth eq 'R'}">checked</c:if> class="custom-control-input">
                        <label class="custom-control-label" for="PR_${permissionRoleVo.menuCd}">
                        <spring:message code="account.menu.permission.read" /><!-- 읽기 -->
                    </label>
                </div>
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" id="PW_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="W"
                    <c:if test="${permissionRoleVo.writeAuth eq 'W'}">checked</c:if> class="custom-control-input">
                        <label class="custom-control-label" for="PW_${permissionRoleVo.menuCd}">
                        <spring:message code="account.menu.permission.write" /><!-- 쓰기 -->
                    </label>
                </div>
           </td>
        </tr>
        </c:if>

        <c:if test="${!empty permissionRoleVo.pmenuCd}">
        <tr onmouseover="this.style.backgroundColor='#F3F3F3';" onmouseout="this.style.backgroundColor='';"><!-- 하위목록 -->
            <td class="text-left pl-5">${permissionRoleVo.menuNm}</td>
            <td class="text-left">
                <c:if test="${permissionRoleVo.menuCd ne '0301' and permissionRoleVo.menuCd ne '0302' and permissionRoleVo.menuCd ne '0305'}">
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="R_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="R" <c:if test="${permissionRoleVo.readAuth eq 'R'}">checked</c:if>>
                    <label class="custom-control-label" for="R_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.read" /></label><!-- 읽기 -->
                </div>
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="W_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="W" <c:if test="${permissionRoleVo.writeAuth eq 'W'}">checked</c:if>>
                    <label class="custom-control-label" for="W_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.write" /></label><!-- 쓰기 -->
                </div>
                </c:if>

                <c:if test="${permissionRoleVo.menuCd eq '0002'}">
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                    <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                </div>
                </c:if>

                <c:if test="${permissionRoleVo.menuCd eq '0103'}">
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                    <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                </div>
                <input type="hidden" value="X_${permissionRoleVo.menuCd}">
                </c:if>

                <c:if test="${permissionRoleVo.menuCd eq '0202'}">
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                    <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                </div>
                <input type="hidden" value="X_${permissionRoleVo.menuCd}">
                </c:if>

                <c:if test="${permissionRoleVo.menuCd eq '0802'}">
                <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="X_${permissionRoleVo.menuCd}" name="${permissionRoleVo.menuCd}" value="X" <c:if test="${permissionRoleVo.executeAuth eq 'X'}">checked</c:if>>
                    <label class="custom-control-label" for="X_${permissionRoleVo.menuCd}"><spring:message code="account.menu.permission.execute" /></label><!-- 실행-->
                </div>
                <input type="hidden" value="X_${permissionRoleVo.menuCd}">
                </c:if>
            </td>
        </tr>
        </c:if>
        </c:forEach>
        </tbody>
    </table>
</div>
</form>
