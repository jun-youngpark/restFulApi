<%-------------------------------------------------------------------------------------------------
 * - [대상자>대상자 등록>1단계] 테스트 쿼리 - SQL 수정(팝업)
 * - URL : /segment/testQueryPopup.do <br/>
 * - Controller :com.mnwise.wiseu.web.segment.web.Segment1StepController <br/>
 * - 이전 파일명 : modify_test_popup.jsp
-------------------------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="segment.menu.sql.popup.modifytestquery"/></title><!-- 테스트 발송 대상자 쿼리 수정 -->
<%@ include file="/jsp/include/plugin.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        initEventBind();
    });

    function initEventBind() {
        // 저장 버튼 클릭
        $('#saveBtn').on("click", function(event) {
            var param = $.mdf.serializeObject('#editorFrm');
            $.post('/segment/updateTestQuery.do', $.param(param, true), function(result) {
                opener.location.reload();
            });
        });
    }
</script>
</head>
<body class="overflow-y-hidden">
    <form action="/segment/updateTestQuery.do" name="editorFrm" id="editorFrm" method="post">
    <input type="hidden" name="segmentNo" value="${segmentVo.segmentNo}"/>
        <div class="card pop-card">
            <div class="card-header"><!-- title -->
                <div class="row table_option">
                    <div class="col-10"><h5 class="mb-0"><spring:message code="segment.menu.sql.popup.modifytestquery"/></h5></div><!-- 테스트 발송 대상자 쿼리 수정 -->
                    <div class="col-2 justify-content-end">
                        <img src="/images/common/close.png" id="closeImg" style="cursor: pointer;">
                    </div><!-- 닫기 -->
                </div>
            </div>

            <div class="card-body">
                <!-- 테스트 쿼리 수정-->
                <h2 class="h5"><spring:message code="segment.menu.sql.target"/> : ${segmentVo.segmentNm}</h2><!-- 대상자명 -->
                <div class="form-group">
                    <textarea id="template" name="testQuery" class="form-control" cols="10" style="height: 188px;">${segmentVo.webTestQuery}</textarea>
                </div>
                <!-- //테스트 쿼리 수정-->
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
