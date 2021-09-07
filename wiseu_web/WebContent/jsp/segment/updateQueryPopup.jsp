<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 등록>1단계] 업데이트 쿼리 - SQL 수정(팝업)
 * - URL : /segment/updateQueryPopup.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.Segment1StepController <br/>
 * - 이전 파일명 : modify_update_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.menu.sql.uquery"/></title><!-- 업데이트 쿼리 -->
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
             var param = $.mdf.serializeObject('#editorFrm');
             $.post('/segment/updateUpdateQuery.do', $.param(param, true), function(result) {
                 opener.location.reload();
             });
        });
    }
</script>
</head>
<body class="overflow-y-hidden">
    <form action="/segment/updateUpdateQuery.do" name="editorFrm" id="editorFrm" method="post">
    <input type="hidden" name="segmentNo" value="${segmentVo.segmentNo}"/>
        <div class="card pop-card">
            <div class="card-header"><!-- title -->
                <div class="row table_option">
                    <div class="col-10"><h5 class="mb-0"><spring:message code="segment.menu.sql.uquery"/></h5></div><!-- 업데이트 쿼리 -->
                    <div class="col-2 justify-content-end">
                        <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                    </div><!-- 닫기 -->
                </div>
            </div>

            <div class="card-body">
                <!-- 대상자 쿼리 수정-->
                <h2 class="h5"><spring:message code="segment.menu.sql.target"/> : ${segmentVo.segmentNm}</h2><!-- 대상자명 -->
                <div class="form-group">
                    <textarea id="template" name="updateQuery" class="form-control" cols="10" style="height: 188px;">${segmentVo.webUpdateQuery}</textarea>
                </div>
                <!-- //대상자 쿼리 수정-->
            </div>

            <div class="card-footer">
                <button type="button" class="btn btn-outline-primary" id="saveBtn">
                    <spring:message code="button.save"/><!-- 저장 -->
                </button>
            </div>
        </div>
    </form>
</body>
</html>
