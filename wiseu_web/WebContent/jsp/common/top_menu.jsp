<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/include/taglibs.jsp" %>
<script type="text/javascript">
    function openWatch(useSubMenu) {
        var url = "/watch/watch.do?useSubMenu=" + useSubMenu + "&selectNum=10";
        $.mdf.popupGet(url, 'watchPopup', 1200, 800);
    }

    function logout() {
        location.href = "/admin/logout.do";
    }

</script>
<div class="logout-box px-3 pt-2 justify-content-end">
    <div class="logout">
        <span class="font-size-13">
            <i class="fas fa-user-alt"></i> ${sessionScope.adminSessionVo.userVo.nameKor}/${sessionScope.adminSessionVo.userVo.grpNm}
        </span>
        <button class="btn btn-xs btn-outline-primary ml-3" onclick="javascript:logout();">
            <i class="fas fa-sign-out-alt"></i> <spring:message code="common.msg.logout"/><!-- 로그아웃 -->
        </button>
        <button class="btn btn-xs btn-outline-primary ml-2" onclick="javascript:openWatch('${subMenuList}');">
            <i class="fas fa-external-link-alt"></i> wiseWacth
        </button>
    </div>
</div>
