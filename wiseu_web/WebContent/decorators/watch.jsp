<%-------------------------------------------------------------------------------------------------
 * [wiseWatch] 메인 (팝업)
 * - URL : /watch/watch.do
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchController
 * - 프로세스/캠페인/이케어 탭을 출력
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/jsp/include/plugin.jsp" %>
<decorator:head />
<script type="text/javascript">
    function goView(menu) {
        $("#watchFrm input[name=menu]").val(menu);
        $("#watchFrm").submit();
    }

    $(window).unload(function() {
        $("#watchFrm input[name=session]").val("logOut");
        $("#watchFrm").submit();
    });
</script>

</head>

<body style="overflow-y:auto;">
<form id="watchFrm" name="watchFrm" action="/watch/watch.do" method="post">
    <input type="hidden" name="menu"/>
    <input type="hidden" name="session"/>
    <input type="hidden" name="useSubMenu" value="${useSubMenu}"/>
</form>

<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0">wiseU Watch</h5></div>
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <div class="card-body mb-0 overflow-hidden">
        <ul class="nav nav-tabs tab01 mb-2"><!-- tab -->
            <c:if test="${sessionScope.adminSessionVo.userVo.userTypeCd eq 'A'}">
                <li class="nav-item">
                    <a class="nav-link <c:if test="${menu eq 'server'}">active</c:if>" id="process-tab" href="javascript:goView('server');">
                        <spring:message code="watch.menu.process"/><!-- 프로세스 -->
                    </a>
                </li>
            </c:if>
            <li class="nav-item">
                <a class="nav-link <c:if test="${menu eq 'campaign' or menu eq ''}"> active </c:if>" id="campaign-tab" href="javascript:goView('campaign');" >
                    <spring:message code="common.menu.campaign"/><!-- 캠페인 -->
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link <c:if test="${menu eq 'ecare'}"> active </c:if>" id="ecare-tab" href="javascript:goView('ecare');">
                    <spring:message code="common.menu.ecare_1"/><!-- 이케어 -->
                </a>
            </li>
        </ul>

        <decorator:body />
    </div>
</div>
</body>
</html>