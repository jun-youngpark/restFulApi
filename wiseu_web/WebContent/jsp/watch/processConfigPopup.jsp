<%-------------------------------------------------------------------------------------------------
 * - [wiseWatch>프로세스 탭] 환경설정 (팝업)
 * - URL : /watch/processConfigPopup.do
 * - Controller : com.mnwise.wiseu.web.watch.web.WatchController
 * 각 엔진별 Config
 * - 이전 파일명 : watch_config_server.jsp
---------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>wiseWatch - Config</title>
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $("#saveBtn").on("click", function(event) {
            if(confirm("<spring:message code='common.alert.test.msg21'/>") == true) {  // 저장하시겠습니까?
                var param = {};
                param.config = $("#configVal").val();
                $.post("/watch/saveProcessConfig.json?serverId=${serverId}", $.param(param, true), function(result) {
                    if(result.code == "OK") {
                        alert("<spring:message code='common.alert.test.msg3'/>");  // 저장되었습니다.
                    } else {
                        alert("<spring:message code='watch.alert.fail'/>");  // 실패하였습니다.
                    }
                });

            }
        });
    }

</script>
</head>
<body>
<div class="card pop-card">
    <div class="card-header">
        <div class="row table_option">
            <div class="col-10"><h5 class="mb-0">${serverId} Config</h5></div>
            <div class="col-2 justify-content-end">
                <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
            </div><!-- 닫기 -->
        </div>
    </div>

    <form id="updateForm" name="updateForm">
    <div class="card-body">
        <textarea class="form-control" id="configVal" name="configVal" style="height: 585px; word-break; wrap=virtual;">${getConfigString}</textarea>
    </div>

    <div class="card-footer">
        <button type="button" class="btn btn-outline-primary" id="saveBtn">
            <spring:message code="button.save"/><!-- 저장 -->
        </button>
    </div>
    </form>
</div>
</body>
</html>
